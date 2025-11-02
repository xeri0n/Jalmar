package com.jalmar.quest.tilemap

import com.jalmar.quest.tilemap.model.POIType
import com.jalmar.quest.tilemap.model.TileCoordinate
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Trigger event data for tile POIs.
 */
data class TriggerEvent(
    val type: TriggerType,
    val data: String
)

/**
 * Types of triggers that can occur on tiles.
 */
enum class TriggerType {
    NPC_ENCOUNTER,
    ITEM_FOUND,
    ENEMY_ENCOUNTER,
    MAP_TRANSITION,
    QUEST_UPDATE,
    RESOURCE_GATHER,
    INTERACT
}

/**
 * Manages triggers and interactions on tile POIs.
 */
class MapTriggerManager(
    private val tileMapManager: TileMapManager
) {
    private val mutex = Mutex()
    private val triggeredTiles = mutableSetOf<String>() // "mapId:x:y"
    
    /**
     * Check if a tile has been triggered before.
     */
    suspend fun hasBeenTriggered(mapId: String, coordinate: TileCoordinate): Boolean {
        return mutex.withLock {
            triggeredTiles.contains(getTileKey(mapId, coordinate))
        }
    }
    
    /**
     * Mark a tile as triggered.
     */
    suspend fun markAsTriggered(mapId: String, coordinate: TileCoordinate) {
        mutex.withLock {
            triggeredTiles.add(getTileKey(mapId, coordinate))
        }
    }
    
    /**
     * Reset a tile's triggered status.
     */
    suspend fun resetTrigger(mapId: String, coordinate: TileCoordinate) {
        mutex.withLock {
            triggeredTiles.remove(getTileKey(mapId, coordinate))
        }
    }
    
    /**
     * Check for trigger event at a tile position.
     */
    suspend fun checkForTrigger(mapId: String, coordinate: TileCoordinate): TriggerEvent? {
        val tile = tileMapManager.getTileAt(mapId, coordinate.x, coordinate.y) 
            ?: return null
        
        if (tile.poiType == POIType.NONE) return null
        
        val tileKey = getTileKey(mapId, coordinate)
        val alreadyTriggered = mutex.withLock { 
            triggeredTiles.contains(tileKey) 
        }
        
        // Auto-triggers always fire, others only once
        if (!tile.poiType.isAutoTrigger && alreadyTriggered) {
            return null
        }
        
        return when (tile.poiType) {
            POIType.NPC -> TriggerEvent(TriggerType.NPC_ENCOUNTER, tile.poiData ?: "")
            POIType.ITEM -> TriggerEvent(TriggerType.ITEM_FOUND, tile.poiData ?: "")
            POIType.ENEMY -> TriggerEvent(TriggerType.ENEMY_ENCOUNTER, tile.poiData ?: "")
            POIType.ENTRANCE, POIType.EXIT -> TriggerEvent(TriggerType.MAP_TRANSITION, tile.poiData ?: "")
            POIType.QUEST_MARKER -> TriggerEvent(TriggerType.QUEST_UPDATE, tile.poiData ?: "")
            POIType.RESOURCE -> TriggerEvent(TriggerType.RESOURCE_GATHER, tile.poiData ?: "")
            POIType.CRAFTING_STATION, POIType.SHOP, POIType.INN, POIType.HOUSE -> 
                TriggerEvent(TriggerType.INTERACT, tile.poiData ?: "")
            else -> null
        }
    }
    
    /**
     * Get all triggered tiles for a map.
     */
    suspend fun getTriggeredTilesForMap(mapId: String): List<TileCoordinate> {
        return mutex.withLock {
            triggeredTiles
                .filter { it.startsWith("$mapId:") }
                .mapNotNull { key ->
                    val parts = key.split(":")
                    if (parts.size == 3) {
                        TileCoordinate(parts[1].toIntOrNull() ?: 0, parts[2].toIntOrNull() ?: 0)
                    } else null
                }
        }
    }
    
    /**
     * Clear all triggered tiles.
     */
    suspend fun clearAllTriggers() {
        mutex.withLock {
            triggeredTiles.clear()
        }
    }
    
    /**
     * Generate unique key for a tile.
     */
    private fun getTileKey(mapId: String, coordinate: TileCoordinate): String {
        return "$mapId:${coordinate.x}:${coordinate.y}"
    }
}
