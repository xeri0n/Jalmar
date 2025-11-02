package com.jalmar.quest.movement

import com.jalmar.quest.tilemap.TileMapManager
import com.jalmar.quest.tilemap.model.GridDirection
import com.jalmar.quest.tilemap.model.TileCoordinate
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Manages tile-based movement for the player.
 */
class TileMovementManager(
    private val tileMapManager: TileMapManager
) {
    private val mutex = Mutex()
    
    /**
     * Base stamina cost for a single tile movement.
     */
    private val baseStaminaCost = 5
    
    /**
     * Base time cost for a single tile movement (in game minutes).
     */
    private val baseTimeCost = 1
    
    /**
     * Attempt to move the player in a direction.
     */
    suspend fun move(
        currentPosition: TileCoordinate,
        direction: GridDirection,
        currentStamina: Int
    ): TileMovementResult {
        return mutex.withLock {
            val targetPosition = currentPosition.adjacent(direction)
            
            // Check if target is in bounds
            val currentMap = tileMapManager.getCurrentMap()
                ?: return@withLock TileMovementResult.Failure("No map loaded")
            
            if (!currentMap.isInBounds(targetPosition.x, targetPosition.y)) {
                return@withLock TileMovementResult.OutOfBounds(targetPosition)
            }
            
            // Check if target is walkable
            val targetTile = currentMap.getTileAt(targetPosition)
                ?: return@withLock TileMovementResult.Blocked(targetPosition, "Tile not found")
            
            if (!targetTile.isWalkable) {
                return@withLock TileMovementResult.Blocked(targetPosition, "Tile is not walkable")
            }
            
            // Calculate movement cost
            val cost = calculateMovementCost(targetTile.terrainType.movementCost)
            
            // Check stamina
            if (currentStamina < cost.staminaCost) {
                return@withLock TileMovementResult.InsufficientStamina(
                    required = cost.staminaCost,
                    available = currentStamina
                )
            }
            
            // Movement successful
            TileMovementResult.Success(
                newPosition = targetPosition,
                staminaCost = cost.staminaCost,
                timeCost = cost.timeCost,
                triggeredPOI = if (targetTile.poiType.isAutoTrigger) targetTile.poiData else null
            )
        }
    }
    
    /**
     * Calculate the cost to move along a path.
     */
    suspend fun calculatePathCost(path: List<TileCoordinate>): MovementCost {
        if (path.isEmpty()) return MovementCost(0, 0)
        
        var totalStamina = 0
        var totalTime = 0
        
        val currentMap = tileMapManager.getCurrentMap() ?: return MovementCost(0, 0)
        
        for (coord in path) {
            val tile = currentMap.getTileAt(coord) ?: continue
            val cost = calculateMovementCost(tile.terrainType.movementCost)
            totalStamina += cost.staminaCost
            totalTime += cost.timeCost
        }
        
        return MovementCost(totalStamina, totalTime)
    }
    
    /**
     * Calculate movement cost based on terrain multiplier.
     */
    private fun calculateMovementCost(terrainMultiplier: Double): MovementCost {
        val staminaCost = max(1, (baseStaminaCost * terrainMultiplier).roundToInt())
        val timeCost = max(1, (baseTimeCost * terrainMultiplier).roundToInt())
        
        return MovementCost(staminaCost, timeCost)
    }
    
    /**
     * Check if movement to target position is possible.
     */
    suspend fun canMoveTo(
        currentPosition: TileCoordinate,
        targetPosition: TileCoordinate,
        currentStamina: Int
    ): Boolean {
        val currentMap = tileMapManager.getCurrentMap() ?: return false
        
        if (!currentMap.isInBounds(targetPosition.x, targetPosition.y)) {
            return false
        }
        
        val targetTile = currentMap.getTileAt(targetPosition) ?: return false
        if (!targetTile.isWalkable) {
            return false
        }
        
        val cost = calculateMovementCost(targetTile.terrainType.movementCost)
        return currentStamina >= cost.staminaCost
    }
    
    /**
     * Get all valid adjacent positions from current position.
     */
    suspend fun getValidAdjacentPositions(currentPosition: TileCoordinate): List<TileCoordinate> {
        val currentMap = tileMapManager.getCurrentMap() ?: return emptyList()
        
        return GridDirection.values()
            .map { currentPosition.adjacent(it) }
            .filter { 
                currentMap.isInBounds(it.x, it.y) && 
                currentMap.isWalkable(it.x, it.y) 
            }
    }
}
