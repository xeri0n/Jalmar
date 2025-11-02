package com.jalmar.quest.tilemap

import com.jalmar.quest.tilemap.model.*

/**
 * Catalog of all tile maps in the game.
 */
object TileMapCatalog {
    
    /**
     * Create the Buttonburgh town map (15x15 grid).
     * 
     * Layout:
     * - The Gilded Seed Inn (northwest)
     * - Town Square (center)
     * - The Quailsmith (northeast)
     * - Old Quill's Study (southwest)
     * - The Hen Pen (southeast)
     */
    fun createButtonburghMap(): TileMap {
        val width = 15
        val height = 15
        val tiles = mutableListOf<Tile>()
        
        for (y in 0 until height) {
            for (x in 0 until width) {
                val coord = TileCoordinate(x, y)
                val tile = when {
                    // The Gilded Seed Inn (rows 1-4, cols 1-4)
                    y in 1..4 && x in 1..4 -> {
                        if (y == 1 && x == 2) {
                            // Entrance
                            Tile(coord, TerrainType.WOOD_FLOOR, true, POIType.ENTRANCE, "gilded_seed_inn", 100)
                        } else if (y == 2 && x == 2) {
                            // Innkeeper NPC
                            Tile(coord, TerrainType.WOOD_FLOOR, true, POIType.NPC, "innkeeper_bertha", 100)
                        } else {
                            Tile(coord, TerrainType.WOOD_FLOOR, true)
                        }
                    }
                    
                    // The Quailsmith (rows 1-4, cols 10-13)
                    y in 1..4 && x in 10..13 -> {
                        if (y == 1 && x == 11) {
                            // Entrance
                            Tile(coord, TerrainType.STONE, true, POIType.ENTRANCE, "quailsmith_forge", 80)
                        } else if (y == 2 && x == 11) {
                            // Grumble Forgepaw NPC
                            Tile(coord, TerrainType.STONE, true, POIType.NPC, "grumble_forgepaw", 80)
                        } else if (y == 3 && x == 11) {
                            // Crafting station
                            Tile(coord, TerrainType.STONE, true, POIType.CRAFTING_STATION, "forge", 80)
                        } else {
                            Tile(coord, TerrainType.STONE, true)
                        }
                    }
                    
                    // Old Quill's Study (rows 10-13, cols 1-4)
                    y in 10..13 && x in 1..4 -> {
                        if (y == 10 && x == 2) {
                            // Entrance
                            Tile(coord, TerrainType.WOOD_FLOOR, true, POIType.ENTRANCE, "old_quill_study", 90)
                        } else if (y == 11 && x == 2) {
                            // Old Quill NPC
                            Tile(coord, TerrainType.WOOD_FLOOR, true, POIType.NPC, "old_quill", 90)
                        } else if (y == 12 && x == 2) {
                            // Quest marker
                            Tile(coord, TerrainType.WOOD_FLOOR, true, POIType.QUEST_MARKER, "quest_tutorial", 90)
                        } else {
                            Tile(coord, TerrainType.WOOD_FLOOR, true)
                        }
                    }
                    
                    // The Hen Pen (rows 10-13, cols 10-13)
                    y in 10..13 && x in 10..13 -> {
                        if (y == 10 && x == 11) {
                            // Entrance
                            Tile(coord, TerrainType.DIRT, true, POIType.ENTRANCE, "hen_pen_coop", 100)
                        } else if (y == 11 && x == 11) {
                            // Shop NPC
                            Tile(coord, TerrainType.DIRT, true, POIType.SHOP, "shop_supplies", 100)
                        } else {
                            Tile(coord, TerrainType.DIRT, true)
                        }
                    }
                    
                    // Town Square (center area rows 6-8, cols 6-8)
                    y in 6..8 && x in 6..8 -> {
                        if (y == 7 && x == 7) {
                            // Center fountain/statue
                            Tile(coord, TerrainType.STONE, false, POIType.NONE, null, 100)
                        } else {
                            Tile(coord, TerrainType.STONE, true)
                        }
                    }
                    
                    // Roads connecting buildings (horizontal and vertical)
                    y == 5 || y == 9 || x == 5 || x == 9 -> {
                        Tile(coord, TerrainType.GRAVEL, true)
                    }
                    
                    // Border (unwalkable)
                    x == 0 || y == 0 || x == width - 1 || y == height - 1 -> {
                        if (y == 7 && x == 14) {
                            // East exit to world map
                            Tile(coord, TerrainType.GRASS, true, POIType.EXIT, "world_map:buttonburgh_east", 100)
                        } else {
                            Tile(coord, TerrainType.GRASS, false)
                        }
                    }
                    
                    // Grass filler
                    else -> Tile(coord, TerrainType.GRASS, true)
                }
                
                tiles.add(tile)
            }
        }
        
        return TileMap(
            id = "buttonburgh",
            name = "Buttonburgh",
            width = width,
            height = height,
            tiles = tiles,
            spawnPoint = TileCoordinate(7, 7), // Town square
            exits = listOf(
                MapExit(
                    fromCoordinate = TileCoordinate(14, 7),
                    toMapId = "world_map",
                    toCoordinate = TileCoordinate(0, 7)
                )
            )
        )
    }
    
    /**
     * Create a simple test map (5x5).
     */
    fun createTestMap(): TileMap {
        val width = 5
        val height = 5
        val tiles = mutableListOf<Tile>()
        
        for (y in 0 until height) {
            for (x in 0 until width) {
                val coord = TileCoordinate(x, y)
                val tile = when {
                    // Border
                    x == 0 || y == 0 || x == width - 1 || y == height - 1 -> {
                        Tile(coord, TerrainType.STONE, false)
                    }
                    // Center NPC
                    x == 2 && y == 2 -> {
                        Tile(coord, TerrainType.GRASS, true, POIType.NPC, "test_npc")
                    }
                    // Walkable grass
                    else -> Tile(coord, TerrainType.GRASS, true)
                }
                tiles.add(tile)
            }
        }
        
        return TileMap(
            id = "test_map",
            name = "Test Map",
            width = width,
            height = height,
            tiles = tiles,
            spawnPoint = TileCoordinate(1, 1)
        )
    }
    
    /**
     * Get all available maps.
     */
    fun getAllMaps(): List<TileMap> {
        return listOf(
            createButtonburghMap(),
            createTestMap()
        )
    }
}
