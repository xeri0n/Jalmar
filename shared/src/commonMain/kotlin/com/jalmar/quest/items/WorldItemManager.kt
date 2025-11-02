package com.jalmar.quest.items

import com.jalmar.quest.tilemap.model.TileCoordinate
import com.jalmarquest.shared.inventory.Item
import com.jalmarquest.shared.inventory.ItemCatalog
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Manages items placed on tiles (world items).
 * Thread-safe with Mutex for concurrent access.
 */
class WorldItemManager {
    private val mutex = Mutex()
    private val worldItems = mutableMapOf<TileCoordinate, MutableList<WorldItem>>()
    
    /**
     * Represents an item placed in the world.
     */
    data class WorldItem(
        val itemId: String,
        val quantity: Int = 1
    )
    
    /**
     * Place an item at a coordinate.
     */
    suspend fun placeItem(coordinate: TileCoordinate, itemId: String, quantity: Int = 1) {
        mutex.withLock {
            val items = worldItems.getOrPut(coordinate) { mutableListOf() }
            items.add(WorldItem(itemId, quantity))
        }
    }
    
    /**
     * Get all items at a coordinate.
     */
    suspend fun getItemsAt(coordinate: TileCoordinate): List<WorldItem> {
        return mutex.withLock {
            worldItems[coordinate]?.toList() ?: emptyList()
        }
    }
    
    /**
     * Pick up an item at a coordinate (removes it from world).
     */
    suspend fun pickupItem(coordinate: TileCoordinate, itemId: String): WorldItem? {
        return mutex.withLock {
            val items = worldItems[coordinate] ?: return@withLock null
            val item = items.find { it.itemId == itemId }
            if (item != null) {
                items.remove(item)
                if (items.isEmpty()) {
                    worldItems.remove(coordinate)
                }
            }
            item
        }
    }
    
    /**
     * Check if there are items at a coordinate.
     */
    suspend fun hasItemsAt(coordinate: TileCoordinate): Boolean {
        return mutex.withLock {
            worldItems[coordinate]?.isNotEmpty() == true
        }
    }
    
    /**
     * Remove all items at a coordinate.
     */
    suspend fun clearItemsAt(coordinate: TileCoordinate) {
        mutex.withLock {
            worldItems.remove(coordinate)
        }
    }
}
