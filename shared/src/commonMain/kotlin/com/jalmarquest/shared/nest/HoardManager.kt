package com.jalmarquest.shared.nest

/**
 * Manager for hoard operations (shiny collectible management).
 * 
 * Stateless functional approach - all methods return new instances.
 * Thread-safety delegated to GameStateManager's Mutex.
 */
class HoardManager {
    
    /**
     * Add an item to the hoard.
     * 
     * @param hoard Current hoard state
     * @param itemId ID of item to add
     * @param condition Condition of the item
     * @param timestamp Acquisition timestamp
     * @return HoardResult with updated hoard or failure reason
     */
    fun addToHoard(
        hoard: Hoard,
        itemId: String,
        condition: HoardCondition = HoardCondition.GOOD,
        timestamp: Long = System.currentTimeMillis()
    ): HoardResult {
        // Validate item exists in catalog
        val item = HoardCatalog.getItemById(itemId)
            ?: return HoardResult.Failure(HoardFailure.ITEM_NOT_FOUND)
        
        // Check if item already in hoard (no duplicates for now)
        if (hoard.items.any { it.itemId == itemId }) {
            return HoardResult.Failure(HoardFailure.ITEM_ALREADY_IN_HOARD)
        }
        
        // Create hoarded item instance
        val hoardedItem = HoardedItem(
            itemId = itemId,
            condition = condition,
            acquiredTimestamp = timestamp
        )
        
        // Add to hoard
        val updatedItems = hoard.items + hoardedItem
        
        // Recalculate total value
        val itemValue = item.calculateBaseValue()
        val finalValue = hoardedItem.calculateValue(itemValue)
        val newTotalValue = hoard.totalValue + finalValue
        
        // Check for completed sets
        val newCompletedSets = updateCompletedSets(updatedItems)
        
        // Calculate prestige bonus from rare items
        val newPrestigeBonus = calculatePrestigeBonus(updatedItems)
        
        val updatedHoard = hoard.copy(
            items = updatedItems,
            totalValue = newTotalValue,
            prestigeBonus = newPrestigeBonus,
            completedSets = newCompletedSets
        )
        
        return HoardResult.Success(updatedHoard, finalValue)
    }
    
    /**
     * Remove an item from the hoard.
     * 
     * @param hoard Current hoard state
     * @param itemId ID of item to remove
     * @return HoardResult with updated hoard or failure reason
     */
    fun removeFromHoard(
        hoard: Hoard,
        itemId: String
    ): HoardResult {
        // Find item in hoard
        val hoardedItem = hoard.items.find { it.itemId == itemId }
            ?: return HoardResult.Failure(HoardFailure.ITEM_NOT_IN_HOARD)
        
        // Get item details for value calculation
        val item = HoardCatalog.getItemById(itemId)
            ?: return HoardResult.Failure(HoardFailure.ITEM_NOT_FOUND)
        
        // Remove from hoard
        val updatedItems = hoard.items.filter { it.itemId != itemId }
        
        // Recalculate total value
        val itemValue = item.calculateBaseValue()
        val finalValue = hoardedItem.calculateValue(itemValue)
        val newTotalValue = (hoard.totalValue - finalValue).coerceAtLeast(0)
        
        // Update completed sets
        val newCompletedSets = updateCompletedSets(updatedItems)
        
        // Recalculate prestige bonus
        val newPrestigeBonus = calculatePrestigeBonus(updatedItems)
        
        val updatedHoard = hoard.copy(
            items = updatedItems,
            totalValue = newTotalValue,
            prestigeBonus = newPrestigeBonus,
            completedSets = newCompletedSets
        )
        
        return HoardResult.Success(updatedHoard, -finalValue)
    }
    
    /**
     * Calculate total hoard value from scratch.
     * Useful for validation or migration.
     * 
     * @param hoard Current hoard state
     * @return Total value including condition multipliers
     */
    fun calculateTotalValue(hoard: Hoard): Int {
        return hoard.items.sumOf { hoardedItem ->
            val item = HoardCatalog.getItemById(hoardedItem.itemId)
            if (item != null) {
                val baseValue = item.calculateBaseValue()
                hoardedItem.calculateValue(baseValue)
            } else {
                0
            }
        }
    }
    
    /**
     * Get player's hoard rank based on total value.
     * 
     * @param totalValue Total hoard value
     * @return HoardRank tier
     */
    fun getRank(totalValue: Int): HoardRank {
        return HoardRank.entries
            .filter { it.minValue <= totalValue }
            .maxByOrNull { it.minValue }
            ?: HoardRank.NOVICE_COLLECTOR
    }
    
    /**
     * Create a leaderboard entry for the player.
     * 
     * @param playerName Player's name
     * @param hoard Player's hoard
     * @return HoardLeaderboardEntry for display
     */
    fun createLeaderboardEntry(playerName: String, hoard: Hoard): HoardLeaderboardEntry {
        val rank = getRank(hoard.totalValue)
        val rareItemCount = hoard.items.count { hoardedItem ->
            val item = HoardCatalog.getItemById(hoardedItem.itemId)
            item != null && (item.rarity == HoardRarity.EPIC || 
                           item.rarity == HoardRarity.LEGENDARY || 
                           item.rarity == HoardRarity.MYTHICAL)
        }
        
        return HoardLeaderboardEntry(
            playerName = playerName,
            totalValue = hoard.totalValue,
            rank = rank,
            itemCount = hoard.items.size,
            rareItemCount = rareItemCount
        )
    }
    
    /**
     * Get value boost from completed collection sets.
     * 
     * @param hoard Current hoard state
     * @return Total value bonus from completed sets
     */
    fun getSetValueBonus(hoard: Hoard): Int {
        return hoard.completedSets.sumOf { setId ->
            val setBonus = HoardCatalog.getSetBonus(setId)
            if (setBonus != null) {
                // Calculate bonus value from set items
                val setItems = HoardCatalog.getItemsBySet(setId)
                val setItemsInHoard = hoard.items.filter { hoardedItem ->
                    setItems.any { it.id == hoardedItem.itemId }
                }
                
                setItemsInHoard.sumOf { hoardedItem ->
                    val item = setItems.find { it.id == hoardedItem.itemId }
                    if (item != null) {
                        val baseValue = item.calculateBaseValue()
                        val conditionValue = hoardedItem.calculateValue(baseValue)
                        // Apply set bonus multiplier
                        ((conditionValue * (setBonus.bonusValueMultiplier - 1.0f)).toInt())
                    } else {
                        0
                    }
                }
            } else {
                0
            }
        }
    }
    
    /**
     * Get count of items by type in the hoard.
     * 
     * @param hoard Current hoard state
     * @param type Item type to count
     * @return Number of items of this type
     */
    fun getItemCountByType(hoard: Hoard, type: HoardItemType): Int {
        return hoard.getItemCountByType(type, HoardCatalog.getAllItems())
    }
    
    /**
     * Get count of items by rarity in the hoard.
     * 
     * @param hoard Current hoard state
     * @param rarity Rarity to count
     * @return Number of items of this rarity
     */
    fun getItemCountByRarity(hoard: Hoard, rarity: HoardRarity): Int {
        return hoard.getItemCountByRarity(rarity, HoardCatalog.getAllItems())
    }
    
    /**
     * Get completion percentage for a collection set.
     * 
     * @param hoard Current hoard state
     * @param setId Set ID to check
     * @return Percentage complete (0-100)
     */
    fun getSetCompletionPercentage(hoard: Hoard, setId: String): Int {
        val setItems = HoardCatalog.getItemsBySet(setId)
        if (setItems.isEmpty()) return 0
        
        val ownedCount = setItems.count { setItem ->
            hoard.items.any { it.itemId == setItem.id }
        }
        
        return (ownedCount * 100) / setItems.size
    }
    
    /**
     * Get list of missing items from a collection set.
     * 
     * @param hoard Current hoard state
     * @param setId Set ID to check
     * @return List of missing HoardItems
     */
    fun getMissingSetItems(hoard: Hoard, setId: String): List<HoardItem> {
        val setItems = HoardCatalog.getItemsBySet(setId)
        return setItems.filter { setItem ->
            !hoard.items.any { it.itemId == setItem.id }
        }
    }
    
    /**
     * Update condition of an item in the hoard.
     * Better condition = higher value.
     * 
     * @param hoard Current hoard state
     * @param itemId Item to update
     * @param newCondition New condition
     * @return HoardResult with updated hoard
     */
    fun updateItemCondition(
        hoard: Hoard,
        itemId: String,
        newCondition: HoardCondition
    ): HoardResult {
        // Find item in hoard
        val hoardedItem = hoard.items.find { it.itemId == itemId }
            ?: return HoardResult.Failure(HoardFailure.ITEM_NOT_IN_HOARD)
        
        // Update condition
        val updatedItem = hoardedItem.copy(condition = newCondition)
        val updatedItems = hoard.items.map {
            if (it.itemId == itemId) updatedItem else it
        }
        
        // Recalculate total value
        val newTotalValue = Hoard(items = updatedItems).let { calculateTotalValue(it) }
        
        val updatedHoard = hoard.copy(
            items = updatedItems,
            totalValue = newTotalValue
        )
        
        val valueChange = updatedHoard.totalValue - hoard.totalValue
        return HoardResult.Success(updatedHoard, valueChange)
    }
    
    /**
     * Helper: Update completed sets based on current items.
     */
    private fun updateCompletedSets(items: List<HoardedItem>): Set<String> {
        val catalog = HoardCatalog.getAllItems()
        val tempHoard = Hoard(items = items)
        
        return HoardCatalog.getAllSets()
            .filter { setBonus ->
                tempHoard.hasCompletedSet(setBonus.setId, catalog)
            }
            .map { it.setId }
            .toSet()
    }
    
    /**
     * Helper: Calculate prestige bonus from rare items.
     */
    private fun calculatePrestigeBonus(items: List<HoardedItem>): Int {
        val rarityPrestige = items.sumOf { hoardedItem ->
            val item = HoardCatalog.getItemById(hoardedItem.itemId)
            val prestige: Int = when (item?.rarity) {
                HoardRarity.EPIC -> 10
                HoardRarity.LEGENDARY -> 25
                HoardRarity.MYTHICAL -> 100
                else -> 0
            }
            prestige
        }
        
        val setPrestige = HoardCatalog.getAllSets()
            .filter { setBonus ->
                val tempHoard = Hoard(items = items)
                tempHoard.hasCompletedSet(setBonus.setId, HoardCatalog.getAllItems())
            }
            .sumOf { setBonus: SetBonus -> 
                val prestige: Int = setBonus.bonusPrestige
                prestige
            }
        
        return rarityPrestige + setPrestige
    }
}
