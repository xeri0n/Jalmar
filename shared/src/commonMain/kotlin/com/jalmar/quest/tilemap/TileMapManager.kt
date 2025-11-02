package com.jalmar.quest.tilemap

import com.jalmar.quest.tilemap.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Manages tile maps - loading, caching, and querying.
 */
class TileMapManager {
    private val mutex = Mutex()
    private val loadedMaps = mutableMapOf<String, TileMap>()
    private var currentMapId: String? = null
    
    private val _currentMap = MutableStateFlow<TileMap?>(null)
    val currentMap: StateFlow<TileMap?> = _currentMap.asStateFlow()
    
    /**
     * Load a map into memory.
     */
    suspend fun loadMap(map: TileMap) {
        mutex.withLock {
            loadedMaps[map.id] = map
        }
    }
    
    /**
     * Set the current active map.
     */
    suspend fun setCurrentMap(mapId: String) {
        mutex.withLock {
            require(loadedMaps.containsKey(mapId)) { "Map $mapId not loaded" }
            currentMapId = mapId
            _currentMap.value = loadedMaps[mapId]
        }
    }
    
    /**
     * Get the current active map.
     */
    suspend fun getCurrentMap(): TileMap? {
        return mutex.withLock {
            currentMapId?.let { loadedMaps[it] }
        }
    }
    
    /**
     * Get a specific map by ID.
     */
    suspend fun getMap(mapId: String): TileMap? {
        return mutex.withLock {
            loadedMaps[mapId]
        }
    }
    
    /**
     * Get tile at coordinate on current map.
     */
    suspend fun getTileAt(x: Int, y: Int): Tile? {
        return getCurrentMap()?.getTileAt(x, y)
    }
    
    /**
     * Get tile at coordinate on specific map.
     */
    suspend fun getTileAt(mapId: String, x: Int, y: Int): Tile? {
        return getMap(mapId)?.getTileAt(x, y)
    }
    
    /**
     * Check if coordinate is walkable on current map.
     */
    suspend fun isWalkable(x: Int, y: Int): Boolean {
        return getCurrentMap()?.isWalkable(x, y) ?: false
    }
    
    /**
     * Find path using A* algorithm.
     */
    suspend fun findPath(
        start: TileCoordinate,
        goal: TileCoordinate,
        mapId: String? = null
    ): PathfindingResult {
        val map = if (mapId != null) getMap(mapId) else getCurrentMap()
        if (map == null) return PathfindingResult.failure()
        
        if (!map.isInBounds(goal.x, goal.y) || !map.isWalkable(goal.x, goal.y)) {
            return PathfindingResult.failure()
        }
        
        return aStarSearch(map, start, goal)
    }
    
    /**
     * A* pathfinding implementation.
     */
    private fun aStarSearch(map: TileMap, start: TileCoordinate, goal: TileCoordinate): PathfindingResult {
        val openSet = mutableSetOf(start)
        val cameFrom = mutableMapOf<TileCoordinate, TileCoordinate>()
        val gScore = mutableMapOf(start to 0)
        val fScore = mutableMapOf(start to start.distanceTo(goal))
        
        while (openSet.isNotEmpty()) {
            val current = openSet.minByOrNull { fScore[it] ?: Int.MAX_VALUE } ?: break
            
            if (current == goal) {
                return PathfindingResult(
                    path = reconstructPath(cameFrom, current),
                    cost = gScore[current] ?: 0,
                    success = true
                )
            }
            
            openSet.remove(current)
            
            for (direction in GridDirection.values()) {
                val neighbor = current.adjacent(direction)
                
                if (!map.isInBounds(neighbor.x, neighbor.y) || !map.isWalkable(neighbor.x, neighbor.y)) {
                    continue
                }
                
                val tentativeGScore = (gScore[current] ?: Int.MAX_VALUE) + 1
                
                if (tentativeGScore < (gScore[neighbor] ?: Int.MAX_VALUE)) {
                    cameFrom[neighbor] = current
                    gScore[neighbor] = tentativeGScore
                    fScore[neighbor] = tentativeGScore + neighbor.distanceTo(goal)
                    if (neighbor !in openSet) {
                        openSet.add(neighbor)
                    }
                }
            }
        }
        
        return PathfindingResult.failure()
    }
    
    /**
     * Reconstruct path from A* search.
     */
    private fun reconstructPath(cameFrom: Map<TileCoordinate, TileCoordinate>, current: TileCoordinate): List<TileCoordinate> {
        val path = mutableListOf(current)
        var node = current
        while (cameFrom.containsKey(node)) {
            node = cameFrom[node]!!
            path.add(0, node)
        }
        return path
    }
    
    /**
     * Unload a map from memory.
     */
    suspend fun unloadMap(mapId: String) {
        mutex.withLock {
            loadedMaps.remove(mapId)
            if (currentMapId == mapId) {
                currentMapId = null
            }
        }
    }
    
    /**
     * Get all loaded map IDs.
     */
    suspend fun getLoadedMapIds(): List<String> {
        return mutex.withLock {
            loadedMaps.keys.toList()
        }
    }
}
