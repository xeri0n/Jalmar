package com.jalmarquest.shared.world

import kotlinx.serialization.Serializable

/**
 * Direction for location connections.
 */
@Serializable
enum class Direction {
    NORTH, SOUTH, EAST, WEST, 
    NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST,
    UP, DOWN  // For vertical connections (caves, towers, etc.)
}

/**
 * A connection between two locations.
 */
@Serializable
data class LocationConnection(
    val targetLocationId: String,
    val direction: Direction,
    val travelTime: Int = 1,  // In game hours
    val requiredLevel: Int = 1,
    val isHidden: Boolean = false,
    val isBlocked: Boolean = false,  // Temporarily blocked paths (landslides, locked doors, etc.)
    val unlockCondition: String? = null  // Quest flag or item requirement
)

/**
 * A location in the game world.
 */
@Serializable
data class Location(
    val id: String,
    val name: String,
    val description: LocationDescription,
    val biome: BiomeType,
    val gridX: Int,
    val gridY: Int,
    val connections: List<LocationConnection> = emptyList(),
    val isSettlement: Boolean = false,
    val hasFastTravel: Boolean = false,
    val isSafeZone: Boolean = false,
    val shopAvailable: Boolean = false,
    val innAvailable: Boolean = false,
    val questGiverIds: List<String> = emptyList(),
    val encounterRate: Double = 1.0,
    val recommendedLevel: Int = 1,
    val lore: String = ""
) {
    init {
        require(id.isNotBlank()) { "Location ID cannot be blank" }
        require(name.isNotBlank()) { "Location name cannot be blank" }
        require(encounterRate >= 0.0) { "Encounter rate cannot be negative" }
        require(recommendedLevel >= 1) { "Recommended level must be at least 1" }
    }
    
    /**
     * Get all available exits from this location.
     */
    fun getAvailableExits(playerLevel: Int, unlockedFlags: Set<String>): List<LocationConnection> {
        return connections.filter { connection ->
            // Check level requirement
            if (playerLevel < connection.requiredLevel) return@filter false
            
            // Check unlock condition
            if (connection.unlockCondition != null && 
                connection.unlockCondition !in unlockedFlags) {
                return@filter false
            }
            
            true
        }
    }
    
    /**
     * Check if this location connects to another in a specific direction.
     */
    fun hasConnectionTo(locationId: String, direction: Direction? = null): Boolean {
        return connections.any { 
            it.targetLocationId == locationId && 
            (direction == null || it.direction == direction)
        }
    }
    
    /**
     * Get movement cost multiplier for this location.
     */
    fun getMovementCost(): Double {
        return BiomeProperties.getDefaultProperties(biome).movementCostMultiplier
    }
    
    /**
     * Get danger level for this location.
     */
    fun getDangerLevel(): Int {
        val biomeDanger = BiomeProperties.getDefaultProperties(biome).dangerLevel
        return if (isSafeZone) 0 else biomeDanger
    }
}

/**
 * Location discovery state.
 */
@Serializable
data class LocationDiscovery(
    val locationId: String,
    val discoveredAt: Long = System.currentTimeMillis(),
    val visitCount: Int = 0,
    val fastTravelUnlocked: Boolean = false
)
