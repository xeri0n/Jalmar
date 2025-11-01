package com.jalmarquest.shared.movement

import com.jalmarquest.shared.world.BiomeType
import kotlinx.serialization.Serializable

/**
 * Movement cost configuration for different terrain types.
 * Higher cost means slower movement and more stamina consumption.
 */
@Serializable
data class MovementCost(
    val baseStaminaCost: Int = 1,
    val timeMultiplier: Double = 1.0
) {
    companion object {
        /**
         * Get movement costs for different biome types.
         */
        fun forBiome(biomeType: BiomeType): MovementCost {
            return when (biomeType) {
                BiomeType.GRASSLAND -> MovementCost(
                    baseStaminaCost = 1,
                    timeMultiplier = 0.8
                )
                BiomeType.FOREST -> MovementCost(
                    baseStaminaCost = 2,
                    timeMultiplier = 1.2
                )
                BiomeType.MOUNTAIN -> MovementCost(
                    baseStaminaCost = 4,
                    timeMultiplier = 2.0
                )
                BiomeType.DESERT -> MovementCost(
                    baseStaminaCost = 3,
                    timeMultiplier = 1.5
                )
                BiomeType.SWAMP -> MovementCost(
                    baseStaminaCost = 5,
                    timeMultiplier = 2.5
                )
                BiomeType.TUNDRA -> MovementCost(
                    baseStaminaCost = 3,
                    timeMultiplier = 1.8
                )
                BiomeType.COASTAL -> MovementCost(
                    baseStaminaCost = 1,
                    timeMultiplier = 0.9
                )
                BiomeType.CAVE -> MovementCost(
                    baseStaminaCost = 2,
                    timeMultiplier = 1.3
                )
            }
        }
    }
}

/**
 * Result of a movement attempt.
 */
sealed class MovementResult {
    data class Success(
        val newLocationId: String,
        val staminaCost: Int,
        val timeCost: Int
    ) : MovementResult()
    
    data class Failure(val reason: MovementFailureReason) : MovementResult()
}

/**
 * Reasons why movement might fail.
 */
enum class MovementFailureReason {
    INSUFFICIENT_STAMINA,
    INVALID_DIRECTION,
    BLOCKED_PATH,
    LEVEL_REQUIREMENT_NOT_MET,
    UNLOCK_CONDITION_NOT_MET,
    LOCATION_NOT_FOUND,
    OUT_OF_BOUNDS
}

/**
 * Path node for pathfinding.
 */
data class PathNode(
    val locationId: String,
    val gCost: Int = 0,  // Cost from start
    val hCost: Int = 0,  // Heuristic cost to goal
    val parent: PathNode? = null
) {
    val fCost: Int get() = gCost + hCost
}
