package com.jalmar.quest.movement

import com.jalmar.quest.tilemap.model.GridDirection
import com.jalmar.quest.tilemap.model.TileCoordinate
import kotlinx.serialization.Serializable

/**
 * Result of a tile movement operation.
 */
sealed class TileMovementResult {
    /**
     * Movement succeeded.
     */
    data class Success(
        val newPosition: TileCoordinate,
        val staminaCost: Int,
        val timeCost: Int,
        val triggeredPOI: String? = null
    ) : TileMovementResult()
    
    /**
     * Movement failed due to insufficient stamina.
     */
    data class InsufficientStamina(
        val required: Int,
        val available: Int
    ) : TileMovementResult()
    
    /**
     * Movement failed because target tile is blocked.
     */
    data class Blocked(
        val targetPosition: TileCoordinate,
        val reason: String
    ) : TileMovementResult()
    
    /**
     * Movement failed because target is out of bounds.
     */
    data class OutOfBounds(
        val targetPosition: TileCoordinate
    ) : TileMovementResult()
    
    /**
     * Movement failed for another reason.
     */
    data class Failure(
        val reason: String
    ) : TileMovementResult()
}

/**
 * Movement cost calculation data.
 */
@Serializable
data class MovementCost(
    val staminaCost: Int,
    val timeCost: Int
) {
    init {
        require(staminaCost >= 0) { "Stamina cost cannot be negative" }
        require(timeCost >= 0) { "Time cost cannot be negative" }
    }
}
