package com.jalmar.quest.world

import com.jalmar.quest.tilemap.model.POIType
import com.jalmar.quest.tilemap.model.TerrainType
import com.jalmarquest.shared.world.BiomeType
import kotlinx.serialization.Serializable

/**
 * Serializable level range.
 */
@Serializable
data class LevelRange(
    val min: Int,
    val max: Int
) {
    fun toIntRange(): IntRange = min..max
    
    init {
        require(min >= 1) { "Min level must be at least 1" }
        require(max >= min) { "Max level must be >= min level" }
    }
}

/**
 * Represents a location in the world grid.
 * This is the primary navigation unit for the overworld map.
 */
@Serializable
data class WorldLocation(
    val id: String,
    val name: String,
    val gridX: Int,
    val gridY: Int,
    val biome: BiomeType,
    val terrainType: TerrainType,
    val levelRange: LevelRange,
    val description: String = "",
    val isSettlement: Boolean = false,
    val isLandmark: Boolean = false,
    val hasFastTravel: Boolean = false,
    val poi: POIType = POIType.NONE,
    val poiData: String? = null,
    val encounterRate: Double = 0.0,
    val connections: List<String> = emptyList() // IDs of connected locations
) {
    init {
        require(id.isNotBlank()) { "Location ID cannot be blank" }
        require(name.isNotBlank()) { "Location name cannot be blank" }
        require(encounterRate in 0.0..1.0) { "Encounter rate must be 0.0-1.0" }
    }
    
    /**
     * Calculate Manhattan distance to another location.
     */
    fun distanceTo(other: WorldLocation): Int {
        return kotlin.math.abs(gridX - other.gridX) + kotlin.math.abs(gridY - other.gridY)
    }
    
    /**
     * Check if this location is adjacent to another (distance = 1).
     */
    fun isAdjacentTo(other: WorldLocation): Boolean {
        return distanceTo(other) == 1
    }
    
    /**
     * Get display color based on biome (for map rendering).
     * Enhanced colors for better visibility and differentiation.
     */
    fun getTerrainColor(): Long {
        return when (biome) {
            BiomeType.GRASSLAND -> 0xFF66BB6A // Brighter green
            BiomeType.FOREST -> 0xFF2E7D32 // Rich dark green
            BiomeType.MOUNTAIN -> 0xFF757575 // Medium gray
            BiomeType.DESERT -> 0xFFFFE082 // Sandy yellow
            BiomeType.SWAMP -> 0xFF5D4037 // Rich brown
            BiomeType.TUNDRA -> 0xFF90CAF9 // Light blue
            BiomeType.COASTAL -> 0xFF42A5F5 // Bright blue
            BiomeType.CAVE -> 0xFF424242 // Dark gray (lighter than before)
        }
    }
    
    /**
     * Get POI marker color (for rendering on map).
     */
    fun getPOIColor(): Long? {
        return when (poi) {
            POIType.SHOP, POIType.INN -> 0xFFFFEB3B // Yellow
            POIType.QUEST_MARKER -> 0xFFE91E63 // Pink/magenta
            POIType.NPC -> 0xFF9C27B0 // Purple
            POIType.CRAFTING_STATION -> 0xFFFF9800 // Orange
            POIType.RESOURCE -> 0xFF00BCD4 // Cyan
            POIType.ENTRANCE, POIType.EXIT -> 0xFFFF5722 // Red-orange
            POIType.ENEMY -> 0xFFF44336 // Red
            POIType.ITEM -> 0xFF8BC34A // Light green
            POIType.HOUSE -> 0xFF795548 // Brown
            else -> null
        }
    }
}

/**
 * Helper to create LevelRange from IntRange.
 */
fun IntRange.toLevelRange(): LevelRange = LevelRange(first, last)
