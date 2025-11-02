package com.jalmar.quest.tilemap.model

import kotlinx.serialization.Serializable

/**
 * Represents a single tile coordinate on a tile map.
 */
@Serializable
data class TileCoordinate(
    val x: Int,
    val y: Int
) {
    init {
        require(x >= 0) { "X coordinate cannot be negative" }
        require(y >= 0) { "Y coordinate cannot be negative" }
    }
    
    /**
     * Calculate Manhattan distance to another coordinate.
     */
    fun distanceTo(other: TileCoordinate): Int {
        return kotlin.math.abs(x - other.x) + kotlin.math.abs(y - other.y)
    }
    
    /**
     * Get adjacent coordinate in a direction.
     */
    fun adjacent(direction: GridDirection): TileCoordinate {
        return when (direction) {
            GridDirection.NORTH -> TileCoordinate(x, y - 1)
            GridDirection.SOUTH -> TileCoordinate(x, y + 1)
            GridDirection.EAST -> TileCoordinate(x + 1, y)
            GridDirection.WEST -> TileCoordinate(x - 1, y)
        }
    }
}

/**
 * Grid-based movement directions (4-directional).
 */
enum class GridDirection {
    NORTH, SOUTH, EAST, WEST;
    
    fun opposite(): GridDirection {
        return when (this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            EAST -> WEST
            WEST -> EAST
        }
    }
}

/**
 * Terrain types that affect movement and gameplay.
 */
@Serializable
enum class TerrainType {
    GRASS,
    DIRT,
    STONE,
    WATER,
    SAND,
    MUD,
    WOOD_FLOOR,
    CARPET,
    TILE_FLOOR,
    GRAVEL,
    SNOW,
    ICE;
    
    /**
     * Movement cost multiplier for this terrain.
     */
    val movementCost: Double
        get() = when (this) {
            GRASS, DIRT, WOOD_FLOOR, TILE_FLOOR -> 1.0
            STONE, GRAVEL -> 1.1
            SAND -> 1.3
            MUD -> 1.5
            CARPET -> 0.9
            WATER -> 2.0
            SNOW -> 1.4
            ICE -> 0.8
        }
}

/**
 * Point of Interest types on tiles.
 */
@Serializable
enum class POIType {
    NONE,
    NPC,
    ITEM,
    ENEMY,
    ENTRANCE,
    EXIT,
    QUEST_MARKER,
    RESOURCE,
    CRAFTING_STATION,
    SHOP,
    INN,
    HOUSE;
    
    val isAutoTrigger: Boolean
        get() = when (this) {
            ENTRANCE, EXIT -> true
            else -> false
        }
}

/**
 * Represents a single tile on the map.
 */
@Serializable
data class Tile(
    val coordinate: TileCoordinate,
    val terrainType: TerrainType,
    val isWalkable: Boolean = true,
    val poiType: POIType = POIType.NONE,
    val poiData: String? = null, // JSON or ID for the POI
    val lightLevel: Int = 100, // 0-100, affects visibility
    val discovered: Boolean = false
) {
    init {
        require(lightLevel in 0..100) { "Light level must be 0-100" }
    }
}
