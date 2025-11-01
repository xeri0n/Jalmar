package com.jalmarquest.shared.equipment

import com.jalmarquest.shared.inventory.ItemCatalog
import com.jalmarquest.shared.inventory.ItemType
import com.jalmarquest.shared.inventory.InventoryManager
import com.jalmarquest.shared.model.Player

/**
 * Stateless manager for equipment operations.
 * Follows the functional pattern established by InventoryManager and CurrencyManager.
 * 
 * All operations return a new Player instance and a result, maintaining immutability.
 */
object EquipmentManager {
    
    // ===== EQUIP/UNEQUIP OPERATIONS =====
    
    /**
     * Equips an item from the player's inventory.
     * Automatically unequips any item currently in the target slot.
     * 
     * @param player The player attempting to equip the item
     * @param itemId The ID of the item to equip (must be in inventory)
     * @return Pair of new Player state and operation result
     */
    fun equip(player: Player, itemId: String): Pair<Player, EquipmentResult> {
        // Validate item exists in catalog
        val item = ItemCatalog.getItem(itemId)
            ?: return player to EquipmentResult.Failure.ItemNotFound(itemId)
        
        // Validate item is equipment type
        if (item.type != ItemType.EQUIPMENT) {
            return player to EquipmentResult.Failure.NotEquipment(itemId)
        }
        
        // Validate item has equipment slot (should always be true if type is EQUIPMENT)
        val slot = item.equipmentSlot
            ?: return player to EquipmentResult.Failure.InvalidItem("Equipment item missing slot: $itemId")
        
        // Validate item is in player's inventory
        if (!InventoryManager.hasItem(player.inventory, itemId)) {
            return player to EquipmentResult.Failure.NotInInventory(itemId)
        }
        
        // Create equipment instance with full durability
        val equipment = Equipment(
            itemId = itemId,
            slot = slot,
            currentDurability = item.maxDurability ?: 100,
            maxDurability = item.maxDurability ?: 100
        )
        
        // Check if slot already occupied
        val previouslyEquipped = player.equippedItems[slot]
        
        // Update equipped items
        val newEquippedItems = player.equippedItems.toMutableMap()
        newEquippedItems[slot] = equipment
        
        val newPlayer = player.copy(equippedItems = newEquippedItems)
        
        return newPlayer to EquipmentResult.Success.Equipped(
            itemId = itemId,
            slot = slot,
            replacedItemId = previouslyEquipped?.itemId
        )
    }
    
    /**
     * Unequips an item from the specified slot.
     * 
     * @param player The player attempting to unequip
     * @param slot The equipment slot to unequip from
     * @return Pair of new Player state and operation result
     */
    fun unequip(player: Player, slot: EquipmentSlot): Pair<Player, EquipmentResult> {
        // Check if slot has equipment
        val equipment = player.equippedItems[slot]
            ?: return player to EquipmentResult.Failure.SlotEmpty(slot)
        
        // Remove from equipped items
        val newEquippedItems = player.equippedItems.toMutableMap()
        newEquippedItems.remove(slot)
        
        val newPlayer = player.copy(equippedItems = newEquippedItems)
        
        return newPlayer to EquipmentResult.Success.Unequipped(
            itemId = equipment.itemId,
            slot = slot
        )
    }
    
    /**
     * Unequips all equipment.
     * 
     * @param player The player to unequip all items from
     * @return Pair of new Player state and list of unequipped item IDs
     */
    fun unequipAll(player: Player): Pair<Player, List<String>> {
        val unequippedItems = player.equippedItems.values.map { it.itemId }
        val newPlayer = player.copy(equippedItems = emptyMap())
        return newPlayer to unequippedItems
    }
    
    // ===== QUERY OPERATIONS =====
    
    /**
     * Returns the equipment in the specified slot, or null if empty.
     */
    fun getEquippedInSlot(player: Player, slot: EquipmentSlot): Equipment? {
        return player.equippedItems[slot]
    }
    
    /**
     * Returns all equipped items.
     */
    fun getAllEquipped(player: Player): List<Equipment> {
        return player.equippedItems.values.toList()
    }
    
    /**
     * Returns true if the specified slot has equipment.
     */
    fun hasEquipmentInSlot(player: Player, slot: EquipmentSlot): Boolean {
        return player.equippedItems.containsKey(slot)
    }
    
    /**
     * Returns the number of equipped items.
     */
    fun getEquippedCount(player: Player): Int {
        return player.equippedItems.size
    }
    
    // ===== STAT CALCULATION =====
    
    /**
     * Calculates total stat modifiers from all equipped items.
     * Broken items (0 durability) provide only 50% of their stats.
     * Includes set bonuses if applicable.
     * 
     * @param player The player whose equipment stats to calculate
     * @return Total stat modifiers from equipment
     */
    fun calculateTotalStats(player: Player): StatModifier {
        var totalStats = StatModifier()  // Start with zero stats
        
        // Add stats from each equipped item
        player.equippedItems.values.forEach { equipment ->
            val item = ItemCatalog.getItem(equipment.itemId)
            if (item != null && item.stats != null) {
                val itemStats = if (equipment.isBroken()) {
                    // Broken items provide 50% stats
                    item.stats.scale(0.5)
                } else {
                    item.stats
                }
                totalStats += itemStats
            }
        }
        
        // Add set bonuses
        val setBonuses = calculateSetBonuses(player)
        setBonuses.forEach { bonus ->
            totalStats += bonus.bonusStats
        }
        
        return totalStats
    }
    
    /**
     * Calculates active set bonuses based on equipped items.
     * 
     * @param player The player whose set bonuses to calculate
     * @return List of active set bonuses
     */
    fun calculateSetBonuses(player: Player): List<SetBonus> {
        // Count equipped items per set ID
        val setItemCounts = mutableMapOf<String, Int>()
        
        player.equippedItems.values.forEach { equipment ->
            val item = ItemCatalog.getItem(equipment.itemId)
            val setId = item?.setId
            if (setId != null) {
                setItemCounts[setId] = setItemCounts.getOrDefault(setId, 0) + 1
            }
        }
        
        // Find active set bonuses
        val activeBonuses = mutableListOf<SetBonus>()
        setItemCounts.forEach { (setId, count) ->
            val setBonus = SetBonusCatalog.getSetBonus(setId)
            if (setBonus != null && count >= setBonus.requiredPieces) {
                activeBonuses.add(setBonus)
            }
        }
        
        return activeBonuses
    }
    
    // ===== DURABILITY OPERATIONS =====
    
    /**
     * Reduces durability of equipment in the specified slot.
     * Durability cannot go below 0.
     * 
     * @param player The player whose equipment to damage
     * @param slot The equipment slot to degrade
     * @param amount Amount of durability to lose (default 1)
     * @return Pair of new Player state and whether item broke (reached 0)
     */
    fun degradeDurability(
        player: Player,
        slot: EquipmentSlot,
        amount: Int = 1
    ): Pair<Player, DurabilityResult> {
        require(amount > 0) { "Durability degradation amount must be positive: $amount" }
        
        val equipment = player.equippedItems[slot]
            ?: return player to DurabilityResult.SlotEmpty
        
        val newDurability = maxOf(0, equipment.currentDurability - amount)
        val itemBroke = equipment.currentDurability > 0 && newDurability == 0
        
        val updatedEquipment = equipment.copy(currentDurability = newDurability)
        val newEquippedItems = player.equippedItems.toMutableMap()
        newEquippedItems[slot] = updatedEquipment
        
        val newPlayer = player.copy(equippedItems = newEquippedItems)
        
        return newPlayer to DurabilityResult.Degraded(
            slot = slot,
            newDurability = newDurability,
            maxDurability = equipment.maxDurability,
            itemBroke = itemBroke
        )
    }
    
    /**
     * Repairs equipment in the specified slot to maximum durability.
     * 
     * @param player The player whose equipment to repair
     * @param slot The equipment slot to repair
     * @return Pair of new Player state and operation result
     */
    fun repair(player: Player, slot: EquipmentSlot): Pair<Player, DurabilityResult> {
        val equipment = player.equippedItems[slot]
            ?: return player to DurabilityResult.SlotEmpty
        
        if (equipment.currentDurability == equipment.maxDurability) {
            return player to DurabilityResult.AlreadyFullDurability
        }
        
        val repairedEquipment = equipment.copy(currentDurability = equipment.maxDurability)
        val newEquippedItems = player.equippedItems.toMutableMap()
        newEquippedItems[slot] = repairedEquipment
        
        val newPlayer = player.copy(equippedItems = newEquippedItems)
        
        return newPlayer to DurabilityResult.Repaired(
            slot = slot,
            restoredDurability = equipment.maxDurability - equipment.currentDurability
        )
    }
    
    /**
     * Repairs all equipped items to maximum durability.
     * 
     * @param player The player whose equipment to repair
     * @return Pair of new Player state and number of items repaired
     */
    fun repairAll(player: Player): Pair<Player, Int> {
        var repairedCount = 0
        val newEquippedItems = player.equippedItems.mapValues { (_, equipment) ->
            if (equipment.currentDurability < equipment.maxDurability) {
                repairedCount++
                equipment.copy(currentDurability = equipment.maxDurability)
            } else {
                equipment
            }
        }
        
        val newPlayer = player.copy(equippedItems = newEquippedItems)
        return newPlayer to repairedCount
    }
}

// ===== RESULT TYPES =====

/**
 * Result of an equipment operation.
 */
sealed class EquipmentResult {
    sealed class Success : EquipmentResult() {
        data class Equipped(
            val itemId: String,
            val slot: EquipmentSlot,
            val replacedItemId: String?
        ) : Success()
        
        data class Unequipped(
            val itemId: String,
            val slot: EquipmentSlot
        ) : Success()
    }
    
    sealed class Failure : EquipmentResult() {
        data class ItemNotFound(val itemId: String) : Failure()
        data class NotEquipment(val itemId: String) : Failure()
        data class InvalidItem(val reason: String) : Failure()
        data class NotInInventory(val itemId: String) : Failure()
        data class SlotEmpty(val slot: EquipmentSlot) : Failure()
    }
}

/**
 * Result of a durability operation.
 */
sealed class DurabilityResult {
    data class Degraded(
        val slot: EquipmentSlot,
        val newDurability: Int,
        val maxDurability: Int,
        val itemBroke: Boolean
    ) : DurabilityResult()
    
    data class Repaired(
        val slot: EquipmentSlot,
        val restoredDurability: Int
    ) : DurabilityResult()
    
    object SlotEmpty : DurabilityResult()
    object AlreadyFullDurability : DurabilityResult()
}
