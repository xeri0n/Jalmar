package com.jalmar.quest.tilemap.model

import kotlinx.serialization.Serializable

/**
 * Represents a complete tile-based map.
 */
@Serializable
data class TileMap(
    val id: String,
    val name: String,
    val width: Int,
    val height: Int,
    val tiles: List<Tile>,
    val spawnPoint: TileCoordinate = TileCoordinate(0, 0),
    val exits: List<MapExit> = emptyList()
) {
    init {
        require(id.isNotBlank()) { "Map ID cannot be blank" }
        require(width > 0) { "Width must be positive" }
        require(height > 0) { "Height must be positive" }
        require(tiles.size == width * height) { "Tiles list must match width * height" }
    }
    
    /**
     * Get tile at specific coordinate.
     */
    fun getTileAt(x: Int, y: Int): Tile? {
        if (x < 0 || x >= width || y < 0 || y >= height) return null
        val index = y * width + x
        return tiles.getOrNull(index)
    }
    
    /**
     * Get tile at coordinate.
     */
    fun getTileAt(coord: TileCoordinate): Tile? {
        return getTileAt(coord.x, coord.y)
    }
    
    /**
     * Check if coordinate is within bounds.
     */
    fun isInBounds(x: Int, y: Int): Boolean {
        return x in 0 until width && y in 0 until height
    }
    
    /**
     * Check if coordinate is walkable.
     */
    fun isWalkable(x: Int, y: Int): Boolean {
        return getTileAt(x, y)?.isWalkable == true
    }
}

/**
 * Represents an exit from one map to another.
 */
@Serializable
data class MapExit(
    val fromCoordinate: TileCoordinate,
    val toMapId: String,
    val toCoordinate: TileCoordinate,
    val requiresCondition: String? = null // e.g., "has_key_001"
)

/**
 * Result of a pathfinding operation.
 */
data class PathfindingResult(
    val path: List<TileCoordinate>,
    val cost: Int,
    val success: Boolean
) {
    companion object {
        fun failure(): PathfindingResult {
            return PathfindingResult(emptyList(), 0, false)
        }
    }
}
