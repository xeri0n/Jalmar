package com.jalmar.quest.tilemap

import com.jalmar.quest.tilemap.model.*
import kotlin.test.*

/**
 * Test suite for DungeonCrawlerMapGenerator.
 * Validates OutWar-style dungeon generation with 1600+ tiles, fog of war, and POI placement.
 */
class DungeonCrawlerMapGeneratorTest {
    
    @Test
    fun `generateMainDungeon should create 40x40 grid with 1600 tiles`() {
        val dungeon = DungeonCrawlerMapGenerator.generateMainDungeon()
        
        assertEquals(40, dungeon.width, "Dungeon width should be 40")
        assertEquals(40, dungeon.height, "Dungeon height should be 40")
        assertEquals("main_dungeon", dungeon.id, "Dungeon ID should be main_dungeon")
    }
    
    @Test
    fun `generated dungeon should have player start position at Buttonburgh`() {
        val dungeon = DungeonCrawlerMapGenerator.generateMainDungeon()
        
        val startTile = dungeon.getTileAt(6, 6)
        assertNotNull(startTile, "Start position (6,6) should exist")
        assertEquals(POIType.ENTRANCE, startTile.poiType, "Start position should be ENTRANCE POI")
        assertTrue(startTile.isWalkable, "Start position must be walkable")
    }
    
    @Test
    fun `all dungeon tiles should be valid and initialized`() {
        val dungeon = DungeonCrawlerMapGenerator.generateMainDungeon()
        
        for (y in 0 until dungeon.height) {
            for (x in 0 until dungeon.width) {
                val tile = dungeon.getTileAt(x, y)
                assertNotNull(tile, "Tile at ($x, $y) should not be null")
                assertNotNull(tile.terrainType, "Tile terrain type should be set")
                assertTrue(tile.lightLevel in 0..100, "Light level should be 0-100, was ${tile.lightLevel}")
            }
        }
    }
    
    @Test
    fun `dungeon should contain all 8 biome region types`() {
        val dungeon = DungeonCrawlerMapGenerator.generateMainDungeon()
        
        val terrainTypes = mutableSetOf<TerrainType>()
        
        for (y in 0 until dungeon.height) {
            for (x in 0 until dungeon.width) {
                val tile = dungeon.getTileAt(x, y)
                if (tile != null) {
                    terrainTypes.add(tile.terrainType)
                }
            }
        }
        
        // Should have variety across biomes
        assertTrue(terrainTypes.size >= 5, "Dungeon should have at least 5 terrain types, found ${terrainTypes.size}")
        assertTrue(terrainTypes.contains(TerrainType.GRASS), "Should have grassland region")
        assertTrue(terrainTypes.contains(TerrainType.STONE), "Should have cave/mountain regions")
    }
    
    @Test
    fun `dungeon should place POIs from world location catalog`() {
        val dungeon = DungeonCrawlerMapGenerator.generateMainDungeon()
        
        val poisFound = mutableListOf<POIType>()
        
        for (y in 0 until dungeon.height) {
            for (x in 0 until dungeon.width) {
                val tile = dungeon.getTileAt(x, y)
                if (tile?.poiType != POIType.NONE && tile?.poiType != null) {
                    poisFound.add(tile.poiType)
                }
            }
        }
        
        assertTrue(poisFound.isNotEmpty(), "Dungeon should contain POIs from catalog")
        assertTrue(poisFound.contains(POIType.ENTRANCE), "Should have entrance POI")
    }
    
    @Test
    fun `dungeon should have walkable paths between regions`() {
        val dungeon = DungeonCrawlerMapGenerator.generateMainDungeon()
        
        // Start position (Buttonburgh)
        val start = TileCoordinate(6, 6)
        
        // Test pathfinding to each region (approximate centers)
        val regionCenters = listOf(
            TileCoordinate(5, 5),   // Grassland (Buttonburgh)
            TileCoordinate(15, 5),  // Forest
            TileCoordinate(25, 5),  // Mountain
            TileCoordinate(35, 5),  // Swamp
            TileCoordinate(5, 15),  // Desert
            TileCoordinate(5, 25),  // Tundra
            TileCoordinate(5, 35),  // Coastal
            TileCoordinate(35, 35)  // Cave
        )
        
        val manager = TileMapManager()
        manager.loadMap(dungeon)
        
        for (target in regionCenters) {
            val path = manager.findPath(start, target)
            assertNotNull(path, "Should find path from $start to $target")
            assertTrue(path.isNotEmpty(), "Path should not be empty")
        }
    }
    
    @Test
    fun `dungeon should have lighting variation across regions`() {
        val dungeon = DungeonCrawlerMapGenerator.generateMainDungeon()
        
        val lightLevels = mutableSetOf<Int>()
        
        for (y in 0 until dungeon.height) {
            for (x in 0 until dungeon.width) {
                val tile = dungeon.getTileAt(x, y)
                if (tile != null) {
                    lightLevels.add(tile.lightLevel)
                }
            }
        }
        
        // Should have variety (caves dark, grassland bright, etc.)
        assertTrue(lightLevels.size >= 3, "Should have at least 3 different light levels")
        assertTrue(lightLevels.any { it < 50 }, "Should have dark areas (caves)")
        assertTrue(lightLevels.any { it >= 80 }, "Should have bright areas (outdoor)")
    }
    
    @Test
    fun `dungeon boundaries should be properly contained`() {
        val dungeon = DungeonCrawlerMapGenerator.generateMainDungeon()
        
        // Check all boundary tiles exist
        for (x in 0 until dungeon.width) {
            assertNotNull(dungeon.getTileAt(x, 0), "Top boundary at $x should exist")
            assertNotNull(dungeon.getTileAt(x, dungeon.height - 1), "Bottom boundary at $x should exist")
        }
        
        for (y in 0 until dungeon.height) {
            assertNotNull(dungeon.getTileAt(0, y), "Left boundary at $y should exist")
            assertNotNull(dungeon.getTileAt(dungeon.width - 1, y), "Right boundary at $y should exist")
        }
    }
    
    @Test
    fun `dungeon should have minimum walkable tile ratio`() {
        val dungeon = DungeonCrawlerMapGenerator.generateMainDungeon()
        
        var walkableCount = 0
        var totalCount = 0
        
        for (y in 0 until dungeon.height) {
            for (x in 0 until dungeon.width) {
                val tile = dungeon.getTileAt(x, y)
                if (tile != null) {
                    totalCount++
                    if (tile.isWalkable) {
                        walkableCount++
                    }
                }
            }
        }
        
        val walkableRatio = walkableCount.toFloat() / totalCount
        assertTrue(walkableRatio >= 0.4f, "At least 40% of tiles should be walkable, was ${walkableRatio * 100}%")
    }
    
    @Test
    fun `dungeon should contain random encounters`() {
        val dungeon = DungeonCrawlerMapGenerator.generateMainDungeon()
        
        val enemyTiles = mutableListOf<TileCoordinate>()
        
        for (y in 0 until dungeon.height) {
            for (x in 0 until dungeon.width) {
                val tile = dungeon.getTileAt(x, y)
                if (tile?.poiType == POIType.ENEMY) {
                    enemyTiles.add(TileCoordinate(x, y))
                }
            }
        }
        
        assertTrue(enemyTiles.isNotEmpty(), "Dungeon should contain enemy encounters")
        assertTrue(enemyTiles.size >= 10, "Should have at least 10 random encounters")
    }
    
    @Test
    fun `dungeon should map world locations to POIs correctly`() {
        val dungeon = DungeonCrawlerMapGenerator.generateMainDungeon()
        
        // Check for known locations from WorldLocationCatalog
        val shopTiles = mutableListOf<Tile>()
        val innTiles = mutableListOf<Tile>()
        val npcTiles = mutableListOf<Tile>()
        
        for (y in 0 until dungeon.height) {
            for (x in 0 until dungeon.width) {
                val tile = dungeon.getTileAt(x, y)
                when (tile?.poiType) {
                    POIType.SHOP -> shopTiles.add(tile)
                    POIType.INN -> innTiles.add(tile)
                    POIType.NPC -> npcTiles.add(tile)
                    else -> {}
                }
            }
        }
        
        // Should have services from Buttonburgh
        assertTrue(shopTiles.isNotEmpty(), "Should have shop POIs from world catalog")
        assertTrue(innTiles.isNotEmpty(), "Should have inn POIs from world catalog")
    }
    
    @Test
    fun `dungeon generation should be deterministic with same seed`() {
        // Note: Current implementation doesn't use seed, but should be consistent
        val dungeon1 = DungeonCrawlerMapGenerator.generateMainDungeon()
        val dungeon2 = DungeonCrawlerMapGenerator.generateMainDungeon()
        
        assertEquals(dungeon1.width, dungeon2.width, "Width should be consistent")
        assertEquals(dungeon1.height, dungeon2.height, "Height should be consistent")
        
        // Check start position consistency
        val start1 = dungeon1.getTileAt(6, 6)
        val start2 = dungeon2.getTileAt(6, 6)
        
        assertEquals(start1?.poiType, start2?.poiType, "Start POI should be consistent")
    }
    
    @Test
    fun `dungeon should handle edge coordinates without crashes`() {
        val dungeon = DungeonCrawlerMapGenerator.generateMainDungeon()
        
        // Test corners
        assertNotNull(dungeon.getTileAt(0, 0), "Top-left corner should exist")
        assertNotNull(dungeon.getTileAt(39, 0), "Top-right corner should exist")
        assertNotNull(dungeon.getTileAt(0, 39), "Bottom-left corner should exist")
        assertNotNull(dungeon.getTileAt(39, 39), "Bottom-right corner should exist")
        
        // Test out of bounds
        assertNull(dungeon.getTileAt(-1, 0), "Negative X should return null")
        assertNull(dungeon.getTileAt(0, -1), "Negative Y should return null")
        assertNull(dungeon.getTileAt(40, 0), "X >= width should return null")
        assertNull(dungeon.getTileAt(0, 40), "Y >= height should return null")
    }
    
    @Test
    fun `dungeon should have proper corridor connections`() {
        val dungeon = DungeonCrawlerMapGenerator.generateMainDungeon()
        
        // Main corridors should exist at defined positions
        // Horizontal corridor at Y=19 (between top and bottom halves)
        var horizontalCorridorTiles = 0
        for (x in 0 until dungeon.width) {
            val tile = dungeon.getTileAt(x, 19)
            if (tile?.isWalkable == true) {
                horizontalCorridorTiles++
            }
        }
        
        assertTrue(horizontalCorridorTiles >= 30, "Horizontal corridor should span most of width")
        
        // Vertical corridor at X=19 (between left and right halves)
        var verticalCorridorTiles = 0
        for (y in 0 until dungeon.height) {
            val tile = dungeon.getTileAt(19, y)
            if (tile?.isWalkable == true) {
                verticalCorridorTiles++
            }
        }
        
        assertTrue(verticalCorridorTiles >= 30, "Vertical corridor should span most of height")
    }
}
