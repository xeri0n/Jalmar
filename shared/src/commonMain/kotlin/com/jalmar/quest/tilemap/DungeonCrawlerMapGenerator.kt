package com.jalmar.quest.tilemap

import com.jalmar.quest.tilemap.model.*
import com.jalmar.quest.world.WorldLocationCatalog
import com.jalmarquest.shared.world.BiomeType

/**
 * Large-scale dungeon crawler map generator.
 * Creates OutWar-style explorable dungeon with 1600+ tiles (40x40 grid).
 * Each region is themed based on existing world locations and biomes.
 */
object DungeonCrawlerMapGenerator {
    
    /**
     * Generate the main explorable dungeon map.
     * 40x40 grid = 1600 tiles of explorable content.
     */
    fun generateMainDungeon(): TileMap {
        val width = 40
        val height = 40
        val tiles = mutableListOf<Tile>()
        
        // Get all world locations to map into the dungeon
        val worldLocations = WorldLocationCatalog.allLocations
        
        // Create base dungeon structure - all tiles start as walls
        for (y in 0 until height) {
            for (x in 0 until width) {
                tiles.add(
                    Tile(
                        coordinate = TileCoordinate(x, y),
                        terrainType = TerrainType.STONE,
                        isWalkable = false,
                        lightLevel = 30, // Dim ambient light in dungeon
                        discovered = false
                    )
                )
            }
        }
        
        // Generate dungeon regions based on biomes
        generateGrasslandRegion(tiles, width, 0, 0, 12, 12)      // Northwest - Buttonburgh
        generateForestRegion(tiles, width, 13, 0, 26, 12)        // Northeast - Forests
        generateMountainRegion(tiles, width, 0, 13, 12, 26)      // Southwest - Mountains
        generateSwampRegion(tiles, width, 13, 13, 26, 26)        // Southeast - Swamps
        generateDesertRegion(tiles, width, 27, 0, 39, 12)        // Far East - Desert
        generateTundraRegion(tiles, width, 27, 13, 39, 26)       // Far Southeast - Tundra
        generateCoastalRegion(tiles, width, 0, 27, 12, 39)       // Far South - Coastal
        generateCaveRegion(tiles, width, 13, 27, 39, 39)         // Deep South - Caves
        
        // Create main corridors connecting regions
        createMainCorridors(tiles, width, height)
        
        // Add POIs from world locations
        placeWorldLocationPOIs(tiles, width, worldLocations)
        
        // Add random encounters and resources
        addRandomEncounters(tiles, width, height)
        
        // Create entry/exit points
        addEntryExitPoints(tiles, width)
        
        return TileMap(
            id = "main_dungeon",
            name = "The Vast Underground",
            width = width,
            height = height,
            tiles = tiles,
            spawnPoint = TileCoordinate(6, 6), // Start in Buttonburgh region
            exits = listOf(
                MapExit(
                    fromCoordinate = TileCoordinate(39, 39),
                    toMapId = "surface_world",
                    toCoordinate = TileCoordinate(0, 0)
                )
            )
        )
    }
    
    /**
     * Generate Grassland region (Buttonburgh area).
     * Open chambers connected by corridors.
     */
    private fun generateGrasslandRegion(tiles: MutableList<Tile>, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) {
        // Create main chamber - Town Square
        createChamber(tiles, width, x1 + 2, y1 + 2, x1 + 8, y1 + 8, TerrainType.STONE, 80)
        
        // Create smaller chambers for shops/locations
        createChamber(tiles, width, x1 + 9, y1 + 2, x2 - 1, y1 + 5, TerrainType.WOOD_FLOOR, 90) // Market
        createChamber(tiles, width, x1 + 2, y1 + 9, x1 + 5, y2 - 1, TerrainType.GRASS, 70) // Gardens
        createChamber(tiles, width, x1 + 6, y1 + 9, x2 - 1, y2 - 1, TerrainType.GRASS, 70) // Meadows
        
        // Connect chambers with corridors
        createCorridor(tiles, width, x1 + 8, y1 + 5, x1 + 9, y1 + 5, TerrainType.STONE, 75)
        createCorridor(tiles, width, x1 + 5, y1 + 8, x1 + 5, y1 + 9, TerrainType.STONE, 75)
        createCorridor(tiles, width, x1 + 5, y1 + 9, x1 + 5, y2 - 1, TerrainType.GRASS, 70)
    }
    
    /**
     * Generate Forest region - dense with winding paths.
     */
    private fun generateForestRegion(tiles: MutableList<Tile>, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) {
        // Create winding forest paths
        for (x in x1..x2 step 3) {
            for (y in y1..y2 step 3) {
                if ((x + y) % 5 != 0) { // Create irregular pattern
                    createChamber(tiles, width, x, y, minOf(x + 2, x2), minOf(y + 2, y2), TerrainType.GRASS, 50)
                }
            }
        }
        
        // Connect with corridors
        for (x in x1..x2 step 6) {
            createCorridor(tiles, width, x, y1, x, y2, TerrainType.DIRT, 40)
        }
        for (y in y1..y2 step 6) {
            createCorridor(tiles, width, x1, y, x2, y, TerrainType.DIRT, 40)
        }
    }
    
    /**
     * Generate Mountain region - vertical passages.
     */
    private fun generateMountainRegion(tiles: MutableList<Tile>, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) {
        // Create vertical shafts and ledges
        for (x in x1..x2 step 4) {
            createCorridor(tiles, width, x, y1, x, y2, TerrainType.STONE, 60)
            if (x + 2 <= x2) {
                createCorridor(tiles, width, x, y1 + 2, x + 2, y1 + 2, TerrainType.GRAVEL, 55)
            }
        }
        
        // Add chambers at different heights
        createChamber(tiles, width, x1 + 1, y1 + 1, x1 + 4, y1 + 4, TerrainType.STONE, 70)
        createChamber(tiles, width, x2 - 4, y2 - 4, x2 - 1, y2 - 1, TerrainType.STONE, 70)
    }
    
    /**
     * Generate Swamp region - muddy, difficult terrain.
     */
    private fun generateSwampRegion(tiles: MutableList<Tile>, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) {
        // Create muddy pathways
        for (y in y1..y2 step 3) {
            createCorridor(tiles, width, x1, y, x2, y, TerrainType.MUD, 35)
        }
        
        // Add water pools
        for (x in x1 + 2..x2 - 2 step 5) {
            for (y in y1 + 2..y2 - 2 step 5) {
                setTile(tiles, width, x, y, TerrainType.WATER, false, 40)
            }
        }
        
        // Connect with safe paths
        for (x in x1..x2 step 6) {
            createCorridor(tiles, width, x, y1, x, y2, TerrainType.DIRT, 30)
        }
    }
    
    /**
     * Generate Desert region - sandy chambers.
     */
    private fun generateDesertRegion(tiles: MutableList<Tile>, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) {
        // Create large open sandy areas
        createChamber(tiles, width, x1 + 1, y1 + 1, x2 - 1, y1 + 5, TerrainType.SAND, 90)
        createChamber(tiles, width, x1 + 1, y1 + 6, x2 - 1, y2 - 1, TerrainType.SAND, 85)
        
        // Add oasis
        createChamber(tiles, width, x1 + 4, y1 + 8, x1 + 6, y1 + 10, TerrainType.WATER, 70)
    }
    
    /**
     * Generate Tundra region - icy passages.
     */
    private fun generateTundraRegion(tiles: MutableList<Tile>, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) {
        // Create icy corridors
        for (y in y1..y2 step 3) {
            createCorridor(tiles, width, x1, y, x2, y, TerrainType.ICE, 60)
        }
        for (x in x1..x2 step 3) {
            createCorridor(tiles, width, x, y1, x, y2, TerrainType.SNOW, 55)
        }
    }
    
    /**
     * Generate Coastal region - beaches and water.
     */
    private fun generateCoastalRegion(tiles: MutableList<Tile>, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) {
        // Create beach
        createChamber(tiles, width, x1 + 1, y1 + 1, x2 - 1, y1 + 4, TerrainType.SAND, 75)
        // Water area
        createChamber(tiles, width, x1 + 1, y1 + 5, x2 - 1, y2 - 1, TerrainType.WATER, 65)
        
        // Boardwalk
        createCorridor(tiles, width, x1 + 5, y1 + 1, x1 + 5, y2 - 1, TerrainType.WOOD_FLOOR, 80)
    }
    
    /**
     * Generate Cave region - dark, winding tunnels.
     */
    private fun generateCaveRegion(tiles: MutableList<Tile>, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) {
        // Create main cavern
        createChamber(tiles, width, x1 + 5, y1 + 3, x2 - 3, y2 - 3, TerrainType.STONE, 25)
        
        // Winding tunnels
        var currentX = x1 + 1
        var currentY = y1 + 1
        for (i in 0..20) {
            val nextX = (currentX + (if (i % 2 == 0) 2 else -1)).coerceIn(x1 + 1, x2 - 1)
            val nextY = (currentY + (if (i % 3 == 0) 2 else 1)).coerceIn(y1 + 1, y2 - 1)
            createCorridor(tiles, width, currentX, currentY, nextX, nextY, TerrainType.STONE, 20)
            currentX = nextX
            currentY = nextY
        }
    }
    
    /**
     * Create main corridors connecting all regions.
     */
    private fun createMainCorridors(tiles: MutableList<Tile>, width: Int, height: Int) {
        // Horizontal main corridor through middle
        createCorridor(tiles, width, 0, height / 2, width - 1, height / 2, TerrainType.STONE, 60)
        
        // Vertical main corridor through middle
        createCorridor(tiles, width, width / 2, 0, width / 2, height - 1, TerrainType.STONE, 60)
        
        // Diagonal connectors
        for (i in 0 until minOf(width, height) step 2) {
            setTile(tiles, width, i, i, TerrainType.STONE, true, 55)
            setTile(tiles, width, width - 1 - i, i, TerrainType.STONE, true, 55)
        }
    }
    
    /**
     * Place POIs based on world location data.
     */
    private fun placeWorldLocationPOIs(tiles: MutableList<Tile>, width: Int, worldLocations: List<com.jalmar.quest.world.WorldLocation>) {
        val settlements = worldLocations.filter { it.isSettlement }
        val landmarks = worldLocations.filter { it.isLandmark }
        
        // Place settlements in safe areas (grassland/forest regions)
        settlements.take(10).forEachIndexed { index, location ->
            val x = 5 + (index % 3) * 4
            val y = 3 + (index / 3) * 3
            if (x < width && y < width) {
                val tile = tiles.getOrNull(y * width + x)
                if (tile != null && tile.isWalkable) {
                    tiles[y * width + x] = tile.copy(
                        poiType = location.poi,
                        poiData = location.id,
                        lightLevel = 90
                    )
                }
            }
        }
        
        // Place landmarks throughout the dungeon
        landmarks.take(20).forEachIndexed { index, location ->
            val x = 10 + (index % 6) * 5
            val y = 5 + (index / 6) * 7
            if (x < width && y < width) {
                val tile = tiles.getOrNull(y * width + x)
                if (tile != null && tile.isWalkable) {
                    tiles[y * width + x] = tile.copy(
                        poiType = POIType.QUEST_MARKER,
                        poiData = location.id,
                        lightLevel = tile.lightLevel + 10
                    )
                }
            }
        }
    }
    
    /**
     * Add random encounters and resources.
     */
    private fun addRandomEncounters(tiles: MutableList<Tile>, width: Int, height: Int) {
        var added = 0
        for (y in 0 until height step 3) {
            for (x in 0 until width step 3) {
                val index = y * width + x
                val tile = tiles.getOrNull(index)
                if (tile != null && tile.isWalkable && tile.poiType == POIType.NONE && added < 50) {
                    val poiType = when ((x + y) % 4) {
                        0 -> POIType.RESOURCE
                        1 -> POIType.ENEMY
                        2 -> POIType.ITEM
                        else -> POIType.NPC
                    }
                    tiles[index] = tile.copy(
                        poiType = poiType,
                        poiData = "random_${x}_${y}"
                    )
                    added++
                }
            }
        }
    }
    
    /**
     * Add entry and exit points.
     */
    private fun addEntryExitPoints(tiles: MutableList<Tile>, width: Int) {
        // Entrance at northwest
        setTile(tiles, width, 6, 6, TerrainType.STONE, true, 100, POIType.ENTRANCE, "dungeon_entrance")
        
        // Exits at various points
        setTile(tiles, width, 38, 38, TerrainType.STONE, true, 100, POIType.EXIT, "surface_exit")
        setTile(tiles, width, 20, 2, TerrainType.STONE, true, 90, POIType.EXIT, "north_exit")
        setTile(tiles, width, 2, 38, TerrainType.STONE, true, 90, POIType.EXIT, "south_exit")
    }
    
    // Helper functions
    
    private fun createChamber(
        tiles: MutableList<Tile>,
        width: Int,
        x1: Int, y1: Int,
        x2: Int, y2: Int,
        terrain: TerrainType,
        lightLevel: Int
    ) {
        for (y in y1..y2) {
            for (x in x1..x2) {
                setTile(tiles, width, x, y, terrain, true, lightLevel)
            }
        }
    }
    
    private fun createCorridor(
        tiles: MutableList<Tile>,
        width: Int,
        x1: Int, y1: Int,
        x2: Int, y2: Int,
        terrain: TerrainType,
        lightLevel: Int
    ) {
        // Horizontal then vertical
        for (x in minOf(x1, x2)..maxOf(x1, x2)) {
            setTile(tiles, width, x, y1, terrain, true, lightLevel)
        }
        for (y in minOf(y1, y2)..maxOf(y1, y2)) {
            setTile(tiles, width, x2, y, terrain, true, lightLevel)
        }
    }
    
    private fun setTile(
        tiles: MutableList<Tile>,
        width: Int,
        x: Int, y: Int,
        terrain: TerrainType,
        walkable: Boolean,
        lightLevel: Int,
        poi: POIType = POIType.NONE,
        poiData: String? = null
    ) {
        val index = y * width + x
        if (index >= 0 && index < tiles.size) {
            tiles[index] = Tile(
                coordinate = TileCoordinate(x, y),
                terrainType = terrain,
                isWalkable = walkable,
                lightLevel = lightLevel,
                poiType = poi,
                poiData = poiData,
                discovered = false
            )
        }
    }
}
