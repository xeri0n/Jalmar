package com.jalmarquest.shared.combat

import com.jalmarquest.shared.inventory.Inventory
import com.jalmarquest.shared.inventory.InventoryManager
import kotlin.random.Random

/**
 * Result of generating loot from an enemy.
 */
sealed class LootResult {
    /** Loot generated successfully */
    data class Success(val itemsDropped: List<Pair<String, Int>>) : LootResult() {
        fun summary(): String {
            if (itemsDropped.isEmpty()) return "No items dropped"
            return itemsDropped.joinToString(", ") { (itemId, quantity) -> "$quantity x $itemId" }
        }
    }
    
    /** No loot dropped (all rolls failed) */
    data object NoLoot : LootResult()
}

/**
 * System for generating loot drops from defeated enemies.
 */
object LootSystem {
    
    /**
     * Generates loot from an enemy's loot table.
     * Each item has an independent roll based on its drop chance.
     * 
     * @param lootTable The enemy's loot table
     * @return List of (itemId, quantity) pairs that dropped
     */
    fun generateLoot(lootTable: LootTable): LootResult {
        val droppedItems = mutableListOf<Pair<String, Int>>()
        
        lootTable.drops.forEach { drop ->
            // Roll for drop chance
            if (Random.nextFloat() < drop.dropChance) {
                // Determine quantity
                val quantity = Random.nextInt(drop.minQuantity, drop.maxQuantity + 1)
                droppedItems.add(drop.itemId to quantity)
            }
        }
        
        return if (droppedItems.isEmpty()) {
            LootResult.NoLoot
        } else {
            LootResult.Success(droppedItems)
        }
    }
    
    /**
     * Generates loot and adds it to a player's inventory.
     * 
     * @param lootTable The enemy's loot table
     * @param inventory The player's current inventory
     * @return Pair of (updated inventory, loot result)
     */
    fun generateAndAddLoot(lootTable: LootTable, inventory: Inventory): Pair<Inventory, LootResult> {
        val lootResult = generateLoot(lootTable)
        
        return when (lootResult) {
            is LootResult.NoLoot -> inventory to lootResult
            is LootResult.Success -> {
                var updatedInventory = inventory
                
                // Add each dropped item to inventory
                lootResult.itemsDropped.forEach { (itemId, quantity) ->
                    val (newInventory, _) = InventoryManager.addItem(updatedInventory, itemId, quantity)
                    updatedInventory = newInventory
                }
                
                updatedInventory to lootResult
            }
        }
    }
    
    /**
     * Calculates the expected value (average) of a loot table.
     * Useful for balancing enemy rewards.
     * 
     * @param lootTable The loot table to analyze
     * @return Map of itemId to expected quantity per defeat
     */
    fun calculateExpectedLoot(lootTable: LootTable): Map<String, Float> {
        val expectedLoot = mutableMapOf<String, Float>()
        
        lootTable.drops.forEach { drop ->
            val avgQuantity = (drop.minQuantity + drop.maxQuantity) / 2.0f
            val expectedValue = avgQuantity * drop.dropChance
            expectedLoot[drop.itemId] = expectedValue
        }
        
        return expectedLoot
    }
}
