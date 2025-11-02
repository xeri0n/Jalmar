package com.jalmar.quest.tilemap

import kotlinx.coroutines.test.runTest
import com.jalmar.quest.tilemap.catalog.TileMapCatalog
import com.jalmar.quest.tilemap.model.*
import kotlin.test.*

class TileMapManagerTest {
    private lateinit var manager: TileMapManager
    private lateinit var testMap: TileMap
    
    @BeforeTest
    fun setup() {
        manager = TileMapManager()
        testMap = TileMapCatalog.createButtonburghMap()
    }
    
    @Test
    fun `loadMap should set current map and reset discoveries`() = runTest {
        manager.loadMap(testMap)
        
        val currentMap = manager.currentMap.value
        assertNotNull(currentMap)
        assertEquals("buttonburgh", currentMap.id)
        
        // Should auto-discover spawn area
        val discoveries = manager.discoveredTiles.value
        assertTrue(discoveries.isNotEmpty())
        assertTrue(discoveries.contains(testMap.spawnPoint))
    }
    
    @Test
    fun `getTileAt should return correct tile`() = runTest {
        manager.loadMap(testMap)
        
        val tile = manager.getTileAt(10, 10)
        assertNotNull(tile)
        assertEquals(10, tile.x)
        assertEquals(10, tile.y)
        assertEquals(TerrainType.WATER, tile.terrainType) // Fountain at center
        assertFalse(tile.isWalkable)
    }
    
    @Test
    fun `getTileAt should return null for out of bounds`() = runTest {
        manager.loadMap(testMap)
        
        assertNull(manager.getTileAt(-1, 0))
        assertNull(manager.getTileAt(0, -1))
        assertNull(manager.getTileAt(100, 100))
    }
    
    @Test
    fun `isWalkable should check tile walkability`() = runTest {
        manager.loadMap(testMap)
        
        // Border walls should not be walkable
        assertFalse(manager.isWalkable(0, 0))
        
        // Center fountain not walkable
        assertFalse(manager.isWalkable(9, 9))
        assertFalse(manager.isWalkable(10, 10))
        
        // Regular grass should be walkable
        assertTrue(manager.isWalkable(5, 7))
    }
    
    @Test
    fun `discoverTile should add to discovered set`() = runTest {
        manager.loadMap(testMap)
        
        val initialCount = manager.discoveredTiles.value.size
        manager.discoverTile(15, 15)
        
        val discoveries = manager.discoveredTiles.value
        assertTrue(discoveries.contains(TileCoordinate(15, 15)))
        assertEquals(initialCount + 1, discoveries.size)
    }
    
    @Test
    fun `getPOITiles should return all POI tiles`() = runTest {
        manager.loadMap(testMap)
        
        val poiTiles = manager.getPOITiles()
        assertTrue(poiTiles.isNotEmpty())
        
        // Check for specific POIs we know exist
        assertTrue(poiTiles.any { it.poiId == "gilded_seed_inn" })
        assertTrue(poiTiles.any { it.poiId == "quailsmith" })
        assertTrue(poiTiles.any { it.poiId == "elder_quill" })
        assertTrue(poiTiles.any { it.poiId == "millet_seeds" })
        assertTrue(poiTiles.any { it.poiId == "meadow_path" })
    }
    
    @Test
    fun `findPath should return valid path between walkable tiles`() = runTest {
        manager.loadMap(testMap)
        
        val path = manager.findPath(
            TileCoordinate(5, 5),
            TileCoordinate(8, 8)
        )
        
        assertNotNull(path)
        assertEquals(TileCoordinate(5, 5), path.tiles.first())
        assertEquals(TileCoordinate(8, 8), path.tiles.last())
        assertTrue(path.totalCost > 0)
    }
    
    @Test
    fun `findPath should return null for unreachable destination`() = runTest {
        manager.loadMap(testMap)
        
        val path = manager.findPath(
            TileCoordinate(5, 5),
            TileCoordinate(0, 0) // Wall tile
        )
        
        assertNull(path)
    }
    
    @Test
    fun `findPath should avoid obstacles`() = runTest {
        manager.loadMap(testMap)
        
        // Path around the fountain (9-10, 9-10)
        val path = manager.findPath(
            TileCoordinate(8, 10),
            TileCoordinate(11, 10)
        )
        
        assertNotNull(path)
        // Should not pass through fountain tiles
        assertFalse(path.tiles.any { it.x in 9..10 && it.y in 9..10 })
    }
    
    @Test
    fun `findPath should calculate correct movement costs`() = runTest {
        val dungeonMap = TileMapCatalog.createTestDungeonMap()
        manager.loadMap(dungeonMap)
        
        // Path through mud (higher cost)
        val path = manager.findPath(
            TileCoordinate(6, 7),
            TileCoordinate(9, 11)
        )
        
        assertNotNull(path)
        // Mud tiles cost 3, so total cost should be higher than tile count
        assertTrue(path.totalCost > path.tiles.size)
    }
    
    @Test
    fun `concurrent operations should be thread safe`() = runTest {
        manager.loadMap(testMap)
        
        // Simulate concurrent access
        val jobs = List(10) { index ->
            kotlinx.coroutines.launch {
                manager.discoverTile(index, index)
                manager.getTileAt(index, index)
                manager.isWalkable(index, index)
            }
        }
        
        jobs.forEach { it.join() }
        
        // Should complete without race conditions
        assertTrue(manager.discoveredTiles.value.size >= 10)
    }
    
    @Test
    fun `A-star should find optimal path`() = runTest {
        manager.loadMap(testMap)
        
        // Direct path on clear terrain
        val path = manager.findPath(
            TileCoordinate(10, 11),
            TileCoordinate(10, 15)
        )
        
        assertNotNull(path)
        // Should be straight line (5 tiles including start and end)
        assertEquals(5, path.tiles.size)
        assertEquals(4, path.totalCost) // 4 moves at cost 1 each
    }
    
    @Test
    fun `quail level stupid - trying to path through fountain`() = runTest {
        manager.loadMap(testMap)
        
        // Try to path to fountain center
        val path = manager.findPath(
            TileCoordinate(8, 9),
            TileCoordinate(10, 10) // Fountain center
        )
        
        assertNull(path) // Cannot reach non-walkable tile
    }
}
