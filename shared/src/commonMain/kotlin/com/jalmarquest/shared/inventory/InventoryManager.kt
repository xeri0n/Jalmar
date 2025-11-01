package com.jalmarquest.shared.inventory

import kotlinx.serialization.Serializable

/**
 * Result of adding items to inventory.
 */
@Serializable
sealed class ItemAddResult {
    /** Successfully added all requested items */
    data class Success(val slotsUsed: Int, val quantityAdded: Int) : ItemAddResult()
    
    /** Partially added items (some overflow) */
    data class PartialSuccess(
        val quantityAdded: Int,
        val quantityOverflow: Int
    ) : ItemAddResult()
    
    /** Failed to add items */
    sealed class Failure : ItemAddResult() {
        /** No slots available */
        data object InventoryFull : Failure()
        
        /** Would exceed weight limit */
        data object WeightExceeded : Failure()
        
        /** Item doesn't exist in catalog */
        data object InvalidItem : Failure()
    }
}

/**
 * Result of removing items from inventory.
 */
@Serializable
sealed class ItemRemoveResult {
    /** Successfully removed all requested items */
    data class Success(val quantityRemoved: Int) : ItemRemoveResult()
    
    /** Not enough items to remove */
    data class InsufficientQuantity(val available: Int, val requested: Int) : ItemRemoveResult()
    
    /** Item not found in inventory */
    data object ItemNotFound : ItemRemoveResult()
}

/**
 * Criteria for sorting inventory.
 */
enum class SortCriteria {
    /** Sort by item name (A-Z) */
    NAME,
    
    /** Sort by item type (consumable, equipment, etc.) */
    TYPE,
    
    /** Sort by rarity (common → legendary) */
    RARITY,
    
    /** Sort by value (low → high) */
    VALUE,
    
    /** Sort by weight (light → heavy) */
    WEIGHT,
    
    /** Sort by quantity (low → high) */
    QUANTITY
}

/**
 * Manager for inventory operations.
 * 
 * Uses functional approach - operations return new Inventory instances.
 * Thread-safety is handled by GameStateManager's Mutex when updating Player.inventory.
 * 
 * This is a stateless utility class, not a stateful manager requiring DI.
 */
object InventoryManager {
    
    /**
     * Adds items to inventory with automatic stacking.
     * Returns new Inventory instance and result status.
     * 
     * @param inventory Current inventory state
     * @param itemId ID of item to add
     * @param quantity How many to add (must be positive)
     * @return Pair of (new inventory, add result)
     */
    fun addItem(
        inventory: Inventory,
        itemId: String,
        quantity: Int
    ): Pair<Inventory, ItemAddResult> {
        require(quantity > 0) { "Quantity must be positive: $quantity" }
        
        val item = ItemCatalog.getItem(itemId)
            ?: return inventory to ItemAddResult.Failure.InvalidItem
        
        val totalWeight = item.weight * quantity
        val newTotalWeight = inventory.currentWeight() + totalWeight
        
        // Check weight constraint
        if (newTotalWeight > inventory.maxWeight) {
            return inventory to ItemAddResult.Failure.WeightExceeded
        }
        
        var remainingToAdd = quantity
        val newSlots = inventory.slots.toMutableList()
        var slotsUsed = 0
        
        if (item.stackable) {
            // Try to stack with existing slots
            val existingSlotIndex = newSlots.indexOfFirst { it.itemId == itemId }
            
            if (existingSlotIndex != -1) {
                val existingSlot = newSlots[existingSlotIndex]
                val spaceInStack = item.maxStack - existingSlot.quantity
                val toAddToStack = minOf(remainingToAdd, spaceInStack)
                
                newSlots[existingSlotIndex] = existingSlot.copy(
                    quantity = existingSlot.quantity + toAddToStack
                )
                remainingToAdd -= toAddToStack
            }
            
            // Create new stacks for remaining items
            while (remainingToAdd > 0 && inventory.remainingSlots() > slotsUsed) {
                val stackSize = minOf(remainingToAdd, item.maxStack)
                newSlots.add(InventorySlot(itemId, stackSize))
                remainingToAdd -= stackSize
                slotsUsed++
            }
        } else {
            // Non-stackable: one slot per item
            val slotsNeeded = quantity
            if (inventory.remainingSlots() < slotsNeeded) {
                return inventory to ItemAddResult.Failure.InventoryFull
            }
            
            repeat(quantity) {
                newSlots.add(InventorySlot(itemId, 1))
            }
            slotsUsed = quantity
            remainingToAdd = 0
        }
        
        val quantityAdded = quantity - remainingToAdd
        val newInventory = inventory.copy(slots = newSlots)
        
        val result = when {
            remainingToAdd == 0 -> ItemAddResult.Success(slotsUsed, quantityAdded)
            quantityAdded > 0 -> ItemAddResult.PartialSuccess(quantityAdded, remainingToAdd)
            else -> ItemAddResult.Failure.InventoryFull
        }
        
        return newInventory to result
    }
    
    /**
     * Removes items from inventory.
     * Returns new Inventory instance and result status.
     * 
     * @param inventory Current inventory state
     * @param itemId ID of item to remove
     * @param quantity How many to remove (must be positive)
     * @return Pair of (new inventory, remove result)
     */
    fun removeItem(
        inventory: Inventory,
        itemId: String,
        quantity: Int
    ): Pair<Inventory, ItemRemoveResult> {
        require(quantity > 0) { "Quantity must be positive: $quantity" }
        
        val available = inventory.getItemQuantity(itemId)
        
        if (available == 0) {
            return inventory to ItemRemoveResult.ItemNotFound
        }
        
        if (available < quantity) {
            return inventory to ItemRemoveResult.InsufficientQuantity(available, quantity)
        }
        
        var remainingToRemove = quantity
        val newSlots = inventory.slots.toMutableList()
        val slotsToRemove = mutableListOf<Int>()
        
        // Remove from slots (iterate backwards to safely remove)
        for (i in newSlots.indices.reversed()) {
            if (newSlots[i].itemId == itemId && remainingToRemove > 0) {
                val slot = newSlots[i]
                val toRemove = minOf(slot.quantity, remainingToRemove)
                
                if (toRemove == slot.quantity) {
                    // Remove entire slot
                    slotsToRemove.add(i)
                } else {
                    // Reduce quantity
                    newSlots[i] = slot.copy(quantity = slot.quantity - toRemove)
                }
                
                remainingToRemove -= toRemove
            }
        }
        
        // Remove marked slots
        slotsToRemove.forEach { newSlots.removeAt(it) }
        
        val newInventory = inventory.copy(slots = newSlots)
        return newInventory to ItemRemoveResult.Success(quantity)
    }
    
    /**
     * Sorts inventory by specified criteria.
     * Returns new Inventory instance with sorted slots.
     */
    fun sortInventory(inventory: Inventory, sortBy: SortCriteria): Inventory {
        val sortedSlots = when (sortBy) {
            SortCriteria.NAME -> inventory.slots.sortedBy { 
                ItemCatalog.getItem(it.itemId)?.name ?: it.itemId 
            }
            SortCriteria.TYPE -> inventory.slots.sortedBy { 
                ItemCatalog.getItem(it.itemId)?.type?.ordinal ?: Int.MAX_VALUE 
            }
            SortCriteria.RARITY -> inventory.slots.sortedBy { 
                ItemCatalog.getItem(it.itemId)?.rarity?.ordinal ?: Int.MAX_VALUE 
            }
            SortCriteria.VALUE -> inventory.slots.sortedByDescending { 
                ItemCatalog.getItem(it.itemId)?.value ?: 0 
            }
            SortCriteria.WEIGHT -> inventory.slots.sortedBy { 
                ItemCatalog.getItem(it.itemId)?.weight ?: Int.MAX_VALUE 
            }
            SortCriteria.QUANTITY -> inventory.slots.sortedByDescending { it.quantity }
        }
        
        return inventory.copy(slots = sortedSlots)
    }
    
    /**
     * Filters inventory by a predicate.
     * Returns list of matching slots.
     */
    fun filterItems(inventory: Inventory, predicate: (Item) -> Boolean): List<InventorySlot> {
        return inventory.slots.filter { slot ->
            val item = ItemCatalog.getItem(slot.itemId)
            item != null && predicate(item)
        }
    }
    
    /**
     * Sets a quick slot to an item ID.
     * Returns new Inventory instance.
     * 
     * @param inventory Current inventory state
     * @param slotIndex Quick slot index (0-3)
     * @param itemId Item ID to assign (null to clear slot)
     * @return Pair of (new inventory, success flag)
     */
    fun setQuickSlot(
        inventory: Inventory,
        slotIndex: Int,
        itemId: String?
    ): Pair<Inventory, Boolean> {
        if (slotIndex !in 0..3) {
            return inventory to false
        }
        
        // Validate item exists in inventory
        if (itemId != null && inventory.getItemQuantity(itemId) == 0) {
            return inventory to false
        }
        
        val newQuickSlots = inventory.quickSlots.toMutableList()
        newQuickSlots[slotIndex] = itemId
        
        return inventory.copy(quickSlots = newQuickSlots) to true
    }
    
    /**
     * Transfers items between two slot indices (for UI drag-and-drop).
     * Returns new Inventory instance.
     * 
     * @param inventory Current inventory state
     * @param fromIndex Source slot index
     * @param toIndex Destination slot index
     * @param quantity How many to transfer (for partial stack moves)
     * @return Pair of (new inventory, success flag)
     */
    fun transferSlot(
        inventory: Inventory,
        fromIndex: Int,
        toIndex: Int,
        quantity: Int = Int.MAX_VALUE
    ): Pair<Inventory, Boolean> {
        if (fromIndex !in inventory.slots.indices || toIndex !in inventory.slots.indices) {
            return inventory to false
        }
        
        if (fromIndex == toIndex) {
            return inventory to true  // No-op
        }
        
        val fromSlot = inventory.slots[fromIndex]
        val toSlot = inventory.slots[toIndex]
        
        // If same item type, try to stack
        if (fromSlot.itemId == toSlot.itemId) {
            val item = ItemCatalog.getItem(fromSlot.itemId) ?: return inventory to false
            
            if (item.stackable) {
                val toTransfer = minOf(quantity, fromSlot.quantity)
                val spaceInDest = item.maxStack - toSlot.quantity
                val actualTransfer = minOf(toTransfer, spaceInDest)
                
                val newSlots = inventory.slots.toMutableList()
                
                // Update destination first
                newSlots[toIndex] = toSlot.copy(quantity = toSlot.quantity + actualTransfer)
                
                // Handle source slot removal/update
                if (actualTransfer == fromSlot.quantity) {
                    // Remove source slot entirely
                    // Must handle index shift if removing from before destination
                    if (fromIndex < toIndex) {
                        newSlots.removeAt(fromIndex)
                    } else {
                        newSlots.removeAt(fromIndex)
                    }
                } else {
                    // Reduce source quantity
                    newSlots[fromIndex] = fromSlot.copy(quantity = fromSlot.quantity - actualTransfer)
                }
                
                return inventory.copy(slots = newSlots) to true
            }
        }
        
        // Otherwise, swap slots
        val newSlots = inventory.slots.toMutableList()
        newSlots[fromIndex] = toSlot
        newSlots[toIndex] = fromSlot
        
        return inventory.copy(slots = newSlots) to true
    }
    
    /**
     * Checks if inventory has at least the specified quantity of an item.
     */
    fun hasItem(inventory: Inventory, itemId: String, quantity: Int = 1): Boolean {
        return inventory.getItemQuantity(itemId) >= quantity
    }
    
    /**
     * Gets all items of a specific type from inventory.
     */
    fun getItemsByType(inventory: Inventory, type: ItemType): List<InventorySlot> {
        return filterItems(inventory) { it.type == type }
    }
    
    /**
     * Gets all equipment items from inventory.
     */
    fun getEquipment(inventory: Inventory): List<InventorySlot> {
        return getItemsByType(inventory, ItemType.EQUIPMENT)
    }
    
    /**
     * Gets all consumable items from inventory.
     */
    fun getConsumables(inventory: Inventory): List<InventorySlot> {
        return getItemsByType(inventory, ItemType.CONSUMABLE)
    }
}
