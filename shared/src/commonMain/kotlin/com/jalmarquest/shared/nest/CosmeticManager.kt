package com.jalmarquest.shared.nest

import com.jalmarquest.shared.model.GameState

/**
 * Stateless manager for cosmetic placement operations.
 * Handles grid-based placement, collision detection, and unlock tracking.
 */
class CosmeticManager {
    
    /**
     * Result of a cosmetic placement attempt.
     */
    sealed class PlacementResult {
        data class Success(val updatedPlacedCosmetics: List<PlacedCosmetic>) : PlacementResult()
        data class Failure(val reason: PlacementFailureReason) : PlacementResult()
    }
    
    /**
     * Reasons why cosmetic placement might fail.
     */
    enum class PlacementFailureReason {
        COSMETIC_NOT_FOUND,
        COSMETIC_LOCKED,
        OUT_OF_BOUNDS,
        COLLISION,
        ALREADY_PLACED
    }
    
    /**
     * Result of cosmetic removal.
     */
    sealed class RemovalResult {
        data class Success(val updatedPlacedCosmetics: List<PlacedCosmetic>, val removedCosmetic: PlacedCosmetic) : RemovalResult()
        data class Failure(val reason: String) : RemovalResult()
    }
    
    /**
     * Attempt to place a cosmetic in the nest.
     * 
     * @param currentPlacedCosmetics Current list of placed cosmetics
     * @param unlockedCosmetics Set of unlocked cosmetic IDs
     * @param cosmeticId ID of cosmetic to place
     * @param x X coordinate on grid (0-7)
     * @param y Y coordinate on grid (0-5)
     * @return PlacementResult with updated list or failure reason
     */
    fun placeCosmetic(
        currentPlacedCosmetics: List<PlacedCosmetic>,
        unlockedCosmetics: Set<String>,
        cosmeticId: String,
        x: Int,
        y: Int
    ): PlacementResult {
        // 1. Validate cosmetic exists
        val cosmetic = CosmeticCatalog.getCosmeticById(cosmeticId)
            ?: return PlacementResult.Failure(PlacementFailureReason.COSMETIC_NOT_FOUND)
        
        // 2. Check if cosmetic is unlocked
        if (!isUnlocked(cosmeticId, unlockedCosmetics)) {
            return PlacementResult.Failure(PlacementFailureReason.COSMETIC_LOCKED)
        }
        
        // 3. Check bounds
        if (!isWithinBounds(x, y, cosmetic.width, cosmetic.height)) {
            return PlacementResult.Failure(PlacementFailureReason.OUT_OF_BOUNDS)
        }
        
        // 4. Check if already placed (same cosmetic at same position)
        if (currentPlacedCosmetics.any { it.cosmeticId == cosmeticId && it.gridX == x && it.gridY == y }) {
            return PlacementResult.Failure(PlacementFailureReason.ALREADY_PLACED)
        }
        
        // 5. Check for collisions
        if (hasCollision(currentPlacedCosmetics, x, y, cosmetic.width, cosmetic.height)) {
            return PlacementResult.Failure(PlacementFailureReason.COLLISION)
        }
        
        // 6. Create placed cosmetic and add to list
        val placedCosmetic = PlacedCosmetic(
            cosmeticId = cosmeticId,
            gridX = x,
            gridY = y
        )
        
        val updatedList = currentPlacedCosmetics + placedCosmetic
        return PlacementResult.Success(updatedList)
    }
    
    /**
     * Remove a cosmetic from the nest.
     * 
     * @param currentPlacedCosmetics Current list of placed cosmetics
     * @param x X coordinate of cosmetic to remove
     * @param y Y coordinate of cosmetic to remove
     * @return RemovalResult with updated list and removed cosmetic, or failure reason
     */
    fun removeCosmetic(
        currentPlacedCosmetics: List<PlacedCosmetic>,
        x: Int,
        y: Int
    ): RemovalResult {
        // Find cosmetic at position (check if position is within cosmetic's bounds)
        val toRemove = currentPlacedCosmetics.find { placed ->
            val cosmetic = CosmeticCatalog.getCosmeticById(placed.cosmeticId) ?: return@find false
            x >= placed.gridX && x < placed.gridX + cosmetic.width &&
            y >= placed.gridY && y < placed.gridY + cosmetic.height
        }
        
        return if (toRemove != null) {
            val updatedList = currentPlacedCosmetics - toRemove
            RemovalResult.Success(updatedList, toRemove)
        } else {
            RemovalResult.Failure("No cosmetic at position ($x, $y)")
        }
    }
    
    /**
     * Check if a cosmetic can be placed at the given position.
     */
    fun canPlace(
        currentPlacedCosmetics: List<PlacedCosmetic>,
        unlockedCosmetics: Set<String>,
        cosmeticId: String,
        x: Int,
        y: Int
    ): Boolean {
        val cosmetic = CosmeticCatalog.getCosmeticById(cosmeticId) ?: return false
        
        return isUnlocked(cosmeticId, unlockedCosmetics) &&
               isWithinBounds(x, y, cosmetic.width, cosmetic.height) &&
               !hasCollision(currentPlacedCosmetics, x, y, cosmetic.width, cosmetic.height)
    }
    
    /**
     * Calculate total prestige value of all placed cosmetics.
     */
    fun calculateTotalPrestige(placedCosmetics: List<PlacedCosmetic>): Int {
        return placedCosmetics.sumOf { placed ->
            CosmeticCatalog.getCosmeticById(placed.cosmeticId)?.prestigeValue ?: 0
        }
    }
    
    /**
     * Get all cosmetics available to unlock at the current game state.
     * Filters by level, achievements, quests, etc.
     */
    fun getAvailableCosmetics(
        gameState: GameState,
        currentlyUnlocked: Set<String>
    ): List<Cosmetic> {
        return CosmeticCatalog.allCosmetics.filter { cosmetic ->
            // Already unlocked - skip
            if (currentlyUnlocked.contains(cosmetic.id)) return@filter false
            
            // Check unlock condition
            when (val condition = cosmetic.unlockCondition) {
                null -> true  // No condition, always available
                
                is UnlockCondition.Achievement -> {
                    gameState.achievements.any { it.id == condition.achievementId && it.unlocked }
                }
                
                is UnlockCondition.Level -> {
                    gameState.player.level >= condition.requiredLevel
                }
                
                is UnlockCondition.Purchase -> {
                    // Just return true - actual purchase logic handled elsewhere
                    // This just checks if it's available for purchase
                    true
                }
                
                is UnlockCondition.Quest -> {
                    gameState.completedQuests.contains(condition.questId)
                }
                
                is UnlockCondition.Boss -> {
                    gameState.achievements.any { 
                        it.id == "defeated_${condition.enemyId}" && it.unlocked 
                    }
                }
                
                is UnlockCondition.Discovery -> {
                    gameState.discoveredLocations.contains(condition.locationId)
                }
            }
        }
    }
    
    /**
     * Attempt to unlock a cosmetic via purchase.
     * Validates currency requirements and updates inventory.
     */
    fun purchaseCosmetic(
        currentUnlocked: Set<String>,
        currentSeeds: Long,
        currentGlimmerShards: Long,
        cosmeticId: String
    ): PurchaseResult {
        val cosmetic = CosmeticCatalog.getCosmeticById(cosmeticId)
            ?: return PurchaseResult.Failure("Cosmetic not found")
        
        if (currentUnlocked.contains(cosmeticId)) {
            return PurchaseResult.Failure("Already unlocked")
        }
        
        val condition = cosmetic.unlockCondition as? UnlockCondition.Purchase
            ?: return PurchaseResult.Failure("Not available for purchase")
        
        // Check currency
        if (currentSeeds < condition.seedsCost) {
            return PurchaseResult.Failure("Not enough seeds (need ${condition.seedsCost}, have $currentSeeds)")
        }
        
        if (currentGlimmerShards < condition.glimmerShardsCost) {
            return PurchaseResult.Failure("Not enough glimmer shards (need ${condition.glimmerShardsCost}, have $currentGlimmerShards)")
        }
        
        // Deduct currency and unlock
        val newSeeds = currentSeeds - condition.seedsCost
        val newGlimmerShards = currentGlimmerShards - condition.glimmerShardsCost
        val newUnlocked = currentUnlocked + cosmeticId
        
        return PurchaseResult.Success(
            updatedUnlocked = newUnlocked,
            newSeeds = newSeeds,
            newGlimmerShards = newGlimmerShards
        )
    }
    
    sealed class PurchaseResult {
        data class Success(
            val updatedUnlocked: Set<String>,
            val newSeeds: Long,
            val newGlimmerShards: Long
        ) : PurchaseResult()
        data class Failure(val reason: String) : PurchaseResult()
    }
    
    // PRIVATE HELPERS
    
    private fun isUnlocked(cosmeticId: String, unlockedCosmetics: Set<String>): Boolean {
        val cosmetic = CosmeticCatalog.getCosmeticById(cosmeticId) ?: return false
        // If no unlock condition, it's always unlocked
        return cosmetic.unlockCondition == null || unlockedCosmetics.contains(cosmeticId)
    }
    
    private fun isWithinBounds(x: Int, y: Int, width: Int, height: Int): Boolean {
        return x >= 0 && 
               y >= 0 && 
               x + width <= NestGridConfig.GRID_WIDTH &&
               y + height <= NestGridConfig.GRID_HEIGHT
    }
    
    private fun hasCollision(
        placedCosmetics: List<PlacedCosmetic>,
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ): Boolean {
        return placedCosmetics.any { placed ->
            val cosmetic = CosmeticCatalog.getCosmeticById(placed.cosmeticId) ?: return@any false
            
            // Check rectangle overlap
            rectanglesOverlap(
                x1 = placed.gridX,
                y1 = placed.gridY,
                w1 = cosmetic.width,
                h1 = cosmetic.height,
                x2 = x,
                y2 = y,
                w2 = width,
                h2 = height
            )
        }
    }
    
    /**
     * Check if two rectangles overlap using standard AABB collision.
     */
    private fun rectanglesOverlap(
        x1: Int, y1: Int, w1: Int, h1: Int,
        x2: Int, y2: Int, w2: Int, h2: Int
    ): Boolean {
        return x1 < x2 + w2 &&
               x1 + w1 > x2 &&
               y1 < y2 + h2 &&
               y1 + h1 > y2
    }
}
