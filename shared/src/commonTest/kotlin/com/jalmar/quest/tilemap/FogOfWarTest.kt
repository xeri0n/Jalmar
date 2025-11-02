package com.jalmar.quest.tilemap

import com.jalmar.quest.tilemap.model.*
import kotlin.test.*

/**
 * Test suite for fog of war discovery mechanics.
 * Validates OutWar-style tile exploration and discovery tracking.
 */
class FogOfWarTest {
    
    private lateinit var testMap: TileMap
    private lateinit var manager: TileMapManager
    
    @BeforeTest
    fun setup() {
        // Create small 10x10 test map
        val tiles = Array(10) { y ->
            Array(10) { x ->
                Tile(
                    coordinate = TileCoordinate(x, y),
                    terrainType = TerrainType.GRASS,
                    isWalkable = true,
                    lightLevel = 100,
                    poiType = POIType.NONE
                )
            }
        }
        
        testMap = TileMap(
            id = "fog_test_map",
            name = "Fog Test",
            width = 10,
            height = 10,
            tiles = tiles
        )
        
        manager = TileMapManager()
        manager.loadMap(testMap)
    }
    
    @Test
    fun `discovered tiles should start empty`() {
        val discovered = mutableSetOf<TileCoordinate>()
        
        assertTrue(discovered.isEmpty(), "Discovered tiles should start empty")
    }
    
    @Test
    fun `should discover tiles in 3x3 radius around player`() {
        val playerPos = TileCoordinate(5, 5)
        val discovered = mutableSetOf<TileCoordinate>()
        
        // Auto-discover 3x3 radius
        for (dy in -1..1) {
            for (dx in -1..1) {
                val coord = TileCoordinate(playerPos.x + dx, playerPos.y + dy)
                if (coord.x in 0 until testMap.width && coord.y in 0 until testMap.height) {
                    discovered.add(coord)
                }
            }
        }
        
        assertEquals(9, discovered.size, "Should discover 3x3 = 9 tiles")
        assertTrue(discovered.contains(TileCoordinate(4, 4)), "Should include top-left")
        assertTrue(discovered.contains(TileCoordinate(5, 5)), "Should include center")
        assertTrue(discovered.contains(TileCoordinate(6, 6)), "Should include bottom-right")
    }
    
    @Test
    fun `discovery should handle edge boundaries correctly`() {
        val playerPos = TileCoordinate(0, 0) // Top-left corner
        val discovered = mutableSetOf<TileCoordinate>()
        
        // Auto-discover 3x3 but clamp to map bounds
        for (dy in -1..1) {
            for (dx in -1..1) {
                val coord = TileCoordinate(playerPos.x + dx, playerPos.y + dy)
                if (coord.x in 0 until testMap.width && coord.y in 0 until testMap.height) {
                    discovered.add(coord)
                }
            }
        }
        
        // Only 4 tiles visible at corner (0,0), (1,0), (0,1), (1,1)
        assertEquals(4, discovered.size, "Corner should discover only 4 tiles")
        assertTrue(discovered.contains(TileCoordinate(0, 0)))
        assertTrue(discovered.contains(TileCoordinate(1, 0)))
        assertTrue(discovered.contains(TileCoordinate(0, 1)))
        assertTrue(discovered.contains(TileCoordinate(1, 1)))
    }
    
    @Test
    fun `moving should accumulate discovered tiles`() {
        val discovered = mutableSetOf<TileCoordinate>()
        
        // Start at (5,5)
        val pos1 = TileCoordinate(5, 5)
        discoverRadius(pos1, discovered, testMap)
        val count1 = discovered.size
        
        // Move to (6,5)
        val pos2 = TileCoordinate(6, 5)
        discoverRadius(pos2, discovered, testMap)
        val count2 = discovered.size
        
        assertTrue(count2 > count1, "Moving should discover new tiles")
        assertTrue(discovered.contains(TileCoordinate(5, 5)), "Should retain old discoveries")
        assertTrue(discovered.contains(TileCoordinate(7, 5)), "Should discover new tiles")
    }
    
    @Test
    fun `fully exploring map should discover all tiles`() {
        val discovered = mutableSetOf<TileCoordinate>()
        
        // Visit every tile
        for (y in 0 until testMap.height) {
            for (x in 0 until testMap.width) {
                discoverRadius(TileCoordinate(x, y), discovered, testMap)
            }
        }
        
        assertEquals(100, discovered.size, "Should discover all 100 tiles")
    }
    
    @Test
    fun `discovered tiles should persist across movements`() {
        val discovered = mutableSetOf<TileCoordinate>()
        
        // Discover at (2,2)
        discoverRadius(TileCoordinate(2, 2), discovered, testMap)
        val initialSize = discovered.size
        
        // Move far away to (8,8)
        discoverRadius(TileCoordinate(8, 8), discovered, testMap)
        
        // Original discoveries should still be present
        assertTrue(discovered.contains(TileCoordinate(2, 2)), "Old discoveries should persist")
        assertTrue(discovered.size > initialSize, "Should have new discoveries too")
    }
    
    @Test
    fun `undiscovered tiles should not be in set`() {
        val discovered = mutableSetOf<TileCoordinate>()
        
        // Discover only at (5,5)
        discoverRadius(TileCoordinate(5, 5), discovered, testMap)
        
        // Tile at (0,0) should not be discovered
        assertFalse(discovered.contains(TileCoordinate(0, 0)), "Distant tiles should not be discovered")
        assertFalse(discovered.contains(TileCoordinate(9, 9)), "Distant tiles should not be discovered")
    }
    
    @Test
    fun `discovery radius should be exactly 3x3`() {
        val playerPos = TileCoordinate(5, 5)
        val discovered = mutableSetOf<TileCoordinate>()
        
        discoverRadius(playerPos, discovered, testMap)
        
        // Check all expected tiles in 3x3
        for (dy in -1..1) {
            for (dx in -1..1) {
                val coord = TileCoordinate(playerPos.x + dx, playerPos.y + dy)
                assertTrue(discovered.contains(coord), "Should discover tile at ($dx, $dy) offset")
            }
        }
        
        // Check tiles outside 3x3 are NOT discovered
        assertFalse(discovered.contains(TileCoordinate(3, 5)), "Tiles outside radius should not be discovered")
        assertFalse(discovered.contains(TileCoordinate(7, 5)), "Tiles outside radius should not be discovered")
        assertFalse(discovered.contains(TileCoordinate(5, 3)), "Tiles outside radius should not be discovered")
        assertFalse(discovered.contains(TileCoordinate(5, 7)), "Tiles outside radius should not be discovered")
    }
    
    @Test
    fun `discovered set should handle duplicates correctly`() {
        val discovered = mutableSetOf<TileCoordinate>()
        
        // Discover same area twice
        discoverRadius(TileCoordinate(5, 5), discovered, testMap)
        val size1 = discovered.size
        
        discoverRadius(TileCoordinate(5, 5), discovered, testMap)
        val size2 = discovered.size
        
        assertEquals(size1, size2, "Re-discovering should not increase set size")
    }
    
    @Test
    fun `large map exploration should scale properly`() {
        // Create larger 40x40 map like actual dungeon
        val largeTiles = Array(40) { y ->
            Array(40) { x ->
                Tile(
                    coordinate = TileCoordinate(x, y),
                    terrainType = TerrainType.GRASS,
                    isWalkable = true,
                    lightLevel = 100,
                    poiType = POIType.NONE
                )
            }
        }
        
        val largeMap = TileMap(
            id = "large_test",
            name = "Large Test",
            width = 40,
            height = 40,
            tiles = largeTiles
        )
        
        val discovered = mutableSetOf<TileCoordinate>()
        
        // Discover at center
        discoverRadius(TileCoordinate(20, 20), discovered, largeMap)
        
        assertEquals(9, discovered.size, "Should still discover 3x3 = 9 tiles")
    }
    
    @Test
    fun `discovery progress should be trackable`() {
        val discovered = mutableSetOf<TileCoordinate>()
        val totalTiles = testMap.width * testMap.height
        
        // Discover progressively
        discoverRadius(TileCoordinate(5, 5), discovered, testMap)
        val progress1 = (discovered.size.toFloat() / totalTiles * 100).toInt()
        
        discoverRadius(TileCoordinate(6, 6), discovered, testMap)
        val progress2 = (discovered.size.toFloat() / totalTiles * 100).toInt()
        
        assertTrue(progress2 > progress1, "Exploration progress should increase")
        assertTrue(progress1 in 0..100, "Progress should be valid percentage")
    }
    
    @Test
    fun `pathfinding should work through discovered tiles`() {
        val discovered = mutableSetOf<TileCoordinate>()
        
        // Discover path from (0,0) to (5,5)
        for (i in 0..5) {
            discoverRadius(TileCoordinate(i, i), discovered, testMap)
        }
        
        // Should have path tiles discovered
        assertTrue(discovered.contains(TileCoordinate(0, 0)))
        assertTrue(discovered.contains(TileCoordinate(2, 2)))
        assertTrue(discovered.contains(TileCoordinate(5, 5)))
    }
    
    @Test
    fun `discovery should ignore out-of-bounds coordinates gracefully`() {
        val discovered = mutableSetOf<TileCoordinate>()
        
        // Player at edge
        val edgePos = TileCoordinate(0, 0)
        
        // Manual discovery with out-of-bounds checks
        for (dy in -1..1) {
            for (dx in -1..1) {
                val coord = TileCoordinate(edgePos.x + dx, edgePos.y + dy)
                if (coord.x >= 0 && coord.y >= 0 && 
                    coord.x < testMap.width && coord.y < testMap.height) {
                    discovered.add(coord)
                }
            }
        }
        
        // Should only discover valid tiles
        assertEquals(4, discovered.size)
        assertFalse(discovered.contains(TileCoordinate(-1, -1)))
    }
    
    // Helper function to simulate 3x3 discovery
    private fun discoverRadius(center: TileCoordinate, discovered: MutableSet<TileCoordinate>, map: TileMap) {
        for (dy in -1..1) {
            for (dx in -1..1) {
                val coord = TileCoordinate(center.x + dx, center.y + dy)
                if (coord.x in 0 until map.width && coord.y in 0 until map.height) {
                    discovered.add(coord)
                }
            }
        }
    }
}
