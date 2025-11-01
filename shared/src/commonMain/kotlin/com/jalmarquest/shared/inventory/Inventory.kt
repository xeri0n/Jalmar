package com.jalmarquest.shared.inventory

import kotlinx.serialization.Serializable

/**
 * Represents a single slot in the inventory containing an item and its quantity.
 * Used for stacking items in the inventory system.
 * 
 * @property itemId The unique ID of the item in this slot
 * @property quantity How many of this item are in this slot (1-99)
 */
@Serializable
data class InventorySlot(
    val itemId: String,
    val quantity: Int = 1
) {
    init {
        require(itemId.isNotBlank()) { "Item ID cannot be blank" }
        require(quantity > 0) { "Quantity must be positive: $quantity" }
        require(quantity <= 99) { "Quantity cannot exceed 99: $quantity" }
    }
}

/**
 * Player inventory with slot-based and weight-based capacity constraints.
 * 
 * **Quail-Scale Capacity:**
 * - Button quail body weight: ~50g (50,000mg)
 * - Base carry capacity: 12g (12,000mg) - about 24% of body weight
 * - Upgradeable to ~20g with better equipment (backpacks, harnesses)
 * 
 * **Slot System:**
 * - 20 base slots (upgradeable)
 * - Stackable items occupy one slot regardless of quantity (up to maxStack)
 * - Non-stackable items (equipment) occupy one slot each
 * 
 * **Quick Slots:**
 * - 4 quick-action slots for frequently used items
 * - Hold item IDs for fast access
 * - Items must exist in main inventory
 * 
 * @property slots List of occupied inventory slots
 * @property maxSlots Maximum number of slots (upgradeable)
 * @property maxWeight Maximum carry weight in MILLIGRAMS (upgradeable)
 * @property quickSlots Quick-access item IDs (4 slots)
 */
@Serializable
data class Inventory(
    val slots: List<InventorySlot> = emptyList(),
    val maxSlots: Int = 20,
    val maxWeight: Int = 12000,  // 12g in milligrams
    val quickSlots: List<String?> = List(4) { null }
) {
    init {
        require(maxSlots > 0) { "Max slots must be positive: $maxSlots" }
        require(maxWeight > 0) { "Max weight must be positive: $maxWeight" }
        require(slots.size <= maxSlots) { "Current slots (${slots.size}) exceeds max slots ($maxSlots)" }
        require(quickSlots.size == 4) { "Must have exactly 4 quick slots" }
    }
    
    /**
     * Calculates the current total weight of all items in inventory.
     * Returns weight in MILLIGRAMS.
     */
    fun currentWeight(): Int {
        return slots.sumOf { slot ->
            val item = ItemCatalog.getItem(slot.itemId) ?: return@sumOf 0
            item.weight * slot.quantity
        }
    }
    
    /**
     * Returns the current number of occupied slots.
     */
    fun currentSlotCount(): Int = slots.size
    
    /**
     * Returns remaining slot capacity.
     */
    fun remainingSlots(): Int = maxSlots - currentSlotCount()
    
    /**
     * Returns remaining weight capacity in milligrams.
     */
    fun remainingWeight(): Int = maxWeight - currentWeight()
    
    /**
     * Checks if inventory has room for a specific item quantity.
     * Considers both slot and weight constraints.
     */
    fun canFit(itemId: String, quantity: Int): Boolean {
        val item = ItemCatalog.getItem(itemId) ?: return false
        val totalWeight = item.weight * quantity
        
        // Check weight constraint
        if (currentWeight() + totalWeight > maxWeight) return false
        
        // Check slot constraint
        if (item.stackable) {
            // Can stack with existing slots of same item
            val existingSlot = slots.find { it.itemId == itemId }
            if (existingSlot != null) {
                // Check if can fit entirely in existing stack
                val spaceInStack = item.maxStack - existingSlot.quantity
                if (quantity <= spaceInStack) return true
                // Need more space - calculate overflow
                val overflow = quantity - spaceInStack
                val additionalSlotsNeeded = (overflow + item.maxStack - 1) / item.maxStack
                return remainingSlots() >= additionalSlotsNeeded
            }
            // Need new slot - calculate slots needed
            val slotsNeeded = (quantity + item.maxStack - 1) / item.maxStack
            return remainingSlots() >= slotsNeeded
        } else {
            // Non-stackable needs one slot per item
            return remainingSlots() >= quantity
        }
    }
    
    /**
     * Finds the slot index containing a specific item ID.
     * Returns -1 if not found.
     */
    fun findSlotIndex(itemId: String): Int {
        return slots.indexOfFirst { it.itemId == itemId }
    }
    
    /**
     * Gets the total quantity of a specific item across all slots.
     */
    fun getItemQuantity(itemId: String): Int {
        return slots.filter { it.itemId == itemId }.sumOf { it.quantity }
    }
    
    /**
     * Checks if an item is in a quick slot.
     */
    fun isInQuickSlot(itemId: String): Boolean {
        return quickSlots.contains(itemId)
    }
    
    /**
     * Returns formatted weight string for UI display.
     * Example: "5.2g / 12.0g"
     */
    fun formattedWeightDisplay(): String {
        val current = currentWeight() / 1000.0
        val max = maxWeight / 1000.0
        return String.format(java.util.Locale.US, "%.1fg / %.1fg", current, max)
    }
}
