package com.jalmar.quest.world

import com.jalmar.quest.tilemap.model.POIType
import com.jalmar.quest.tilemap.model.TerrainType
import com.jalmarquest.shared.world.BiomeType

/**
 * Comprehensive world location catalog for JalmarQuest.
 * Contains 500+ locations across 8 biomes organized in a grid from (-10,-8) to (10,15).
 */
object WorldLocationCatalog {
    
    /**
     * All locations in the world, organized by biome and region.
     */
    val allLocations: List<WorldLocation> by lazy {
        grasslandLocations + forestLocations + mountainLocations + 
        desertLocations + swampLocations + tundraLocations + 
        coastalLocations + caveLocations
    }
    
    /**
     * Get a location by ID.
     */
    fun getLocation(id: String): WorldLocation? {
        return allLocations.find { it.id == id }
    }
    
    /**
     * Get locations by biome.
     */
    fun getLocationsByBiome(biome: BiomeType): List<WorldLocation> {
        return allLocations.filter { it.biome == biome }
    }
    
    /**
     * Get location at specific grid coordinates.
     */
    fun getLocationAt(gridX: Int, gridY: Int): WorldLocation? {
        return allLocations.find { it.gridX == gridX && it.gridY == gridY }
    }
    
    /**
     * Get all settlements (towns, villages, outposts).
     */
    fun getSettlements(): List<WorldLocation> {
        return allLocations.filter { it.isSettlement }
    }
    
    /**
     * Get all fast travel points.
     */
    fun getFastTravelLocations(): List<WorldLocation> {
        return allLocations.filter { it.hasFastTravel }
    }
    
    // REGION 1: GRASSLAND (~99 locations)
    private val grasslandLocations = listOf(
        // 1A: Buttonburgh Outskirts (15 locations, Levels 1-3)
        WorldLocation(
            id = "buttonburgh_center",
            name = "Buttonburgh Town Square",
            gridX = 0, gridY = 0,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.STONE,
            levelRange = LevelRange(1, 2),
            description = "The heart of Buttonburgh, where quail gather and trade",
            isSettlement = true,
            hasFastTravel = true,
            poi = POIType.NONE,
            connections = listOf("pebble_plaza", "meadow_path", "dandelion_grove", "puddle_lake")
        ),
        WorldLocation(
            id = "pebble_plaza",
            name = "Pebble Plaza",
            gridX = 0, gridY = -1,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.STONE,
            levelRange = LevelRange(1, 2),
            description = "Market square where birds trade seeds and stories",
            isSettlement = true,
            poi = POIType.SHOP,
            poiData = "seed_market",
            connections = listOf("buttonburgh_center", "dandelion_grove", "compost_heap_foothills")
        ),
        WorldLocation(
            id = "dandelion_grove",
            name = "Dandelion Grove",
            gridX = 1, gridY = -1,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(1, 3),
            description = "Towering yellow flowers sway in the breeze",
            isLandmark = true,
            poi = POIType.RESOURCE,
            poiData = "dandelion_seeds",
            encounterRate = 0.1,
            connections = listOf("pebble_plaza", "buttonburgh_center", "garden_gnome_shadow")
        ),
        WorldLocation(
            id = "puddle_lake",
            name = "Puddle Lake",
            gridX = -1, gridY = 1,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.WATER,
            levelRange = LevelRange(1, 3),
            description = "A grand lake to a button quail - actually a puddle",
            isLandmark = true,
            poi = POIType.RESOURCE,
            poiData = "fresh_water",
            encounterRate = 0.15,
            connections = listOf("buttonburgh_center", "meadow_path", "haystack_fortress")
        ),
        WorldLocation(
            id = "garden_gnome_shadow",
            name = "Garden Gnome's Shadow",
            gridX = 2, gridY = 2,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.STONE,
            levelRange = LevelRange(2, 4),
            description = "Looming statue casts an ominous shade over the path",
            isLandmark = true,
            poi = POIType.QUEST_MARKER,
            poiData = "gnome_mystery",
            encounterRate = 0.2,
            connections = listOf("dandelion_grove", "meadow_path", "wildflower_plains_entrance")
        ),
        WorldLocation(
            id = "compost_heap_foothills",
            name = "Compost Heap Foothills",
            gridX = -2, gridY = 2,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.DIRT,
            levelRange = LevelRange(2, 4),
            description = "Warm, aromatic mountain of decay",
            poi = POIType.RESOURCE,
            poiData = "compost_materials",
            encounterRate = 0.25,
            connections = listOf("pebble_plaza", "puddle_lake", "windmill_farm")
        ),
        WorldLocation(
            id = "twig_spear_crafting",
            name = "The Quailsmith",
            gridX = 1, gridY = 0,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.STONE,
            levelRange = LevelRange(1, 3),
            description = "Grumble Forgepaw's renowned crafting station",
            isSettlement = true,
            poi = POIType.CRAFTING_STATION,
            poiData = "grumble_forgepaw",
            connections = listOf("buttonburgh_center", "dandelion_grove")
        ),
        WorldLocation(
            id = "gilded_seed_inn",
            name = "The Gilded Seed Inn",
            gridX = -1, gridY = 0,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.WOOD_FLOOR,
            levelRange = LevelRange(1, 2),
            description = "Warm haven where travelers rest and share tales",
            isSettlement = true,
            hasFastTravel = true,
            poi = POIType.INN,
            poiData = "innkeeper_bertha",
            connections = listOf("buttonburgh_center", "puddle_lake")
        ),
        WorldLocation(
            id = "old_quills_study",
            name = "Old Quill's Study",
            gridX = 0, gridY = 1,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.WOOD_FLOOR,
            levelRange = LevelRange(1, 3),
            description = "Ancient sage's library filled with dusty scrolls",
            isSettlement = true,
            poi = POIType.NPC,
            poiData = "old_quill",
            connections = listOf("buttonburgh_center", "meadow_path", "puddle_lake")
        ),
        WorldLocation(
            id = "hen_pen",
            name = "The Hen Pen",
            gridX = 1, gridY = 1,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.DIRT,
            levelRange = LevelRange(1, 2),
            description = "Supply shop run by helpful hens",
            isSettlement = true,
            poi = POIType.SHOP,
            poiData = "hen_supplies",
            connections = listOf("buttonburgh_center", "meadow_path", "garden_gnome_shadow")
        ),
        WorldLocation(
            id = "meadow_path",
            name = "Meadow Path",
            gridX = 0, gridY = 2,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(1, 3),
            description = "Gentle trail through swaying grass",
            poi = POIType.NONE,
            encounterRate = 0.1,
            connections = listOf("buttonburgh_center", "puddle_lake", "old_quills_study", "hen_pen", "garden_gnome_shadow", "wildflower_plains_entrance")
        ),
        WorldLocation(
            id = "wheat_stalk_maze",
            name = "Wheat Stalk Maze",
            gridX = -1, gridY = 2,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(2, 4),
            description = "Towering grass stalks form a natural labyrinth",
            encounterRate = 0.2,
            connections = listOf("meadow_path", "puddle_lake", "compost_heap_foothills")
        ),
        WorldLocation(
            id = "morning_glory_arch",
            name = "Morning Glory Arch",
            gridX = 2, gridY = 1,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(2, 3),
            description = "Purple flowers form a natural gateway",
            isLandmark = true,
            poi = POIType.RESOURCE,
            poiData = "morning_glory_seeds",
            connections = listOf("dandelion_grove", "hen_pen", "garden_gnome_shadow")
        ),
        WorldLocation(
            id = "pebble_path_crossing",
            name = "Pebble Path Crossing",
            gridX = -2, gridY = 0,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRAVEL,
            levelRange = LevelRange(1, 3),
            description = "Where smooth stones create a road",
            connections = listOf("gilded_seed_inn", "compost_heap_foothills", "windmill_farm")
        ),
        WorldLocation(
            id = "butterfly_landing",
            name = "Butterfly Landing",
            gridX = 2, gridY = 0,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(2, 3),
            description = "Colorful wings flutter in abundance",
            poi = POIType.RESOURCE,
            poiData = "butterfly_dust",
            encounterRate = 0.15,
            connections = listOf("twig_spear_crafting", "dandelion_grove", "morning_glory_arch")
        ),
        
        // 1B: Wildflower Plains (20 locations, Levels 2-5)
        WorldLocation(
            id = "wildflower_plains_entrance",
            name = "Wildflower Plains Entrance",
            gridX = 0, gridY = 3,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(2, 4),
            description = "Gateway to vast fields of blooming flowers",
            connections = listOf("meadow_path", "garden_gnome_shadow", "clover_kingdom", "morning_dew_meadow")
        ),
        WorldLocation(
            id = "clover_kingdom",
            name = "Clover Kingdom",
            gridX = 0, gridY = 6,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(3, 5),
            description = "Three-leaf and four-leaf clover fields stretch endlessly",
            isLandmark = true,
            poi = POIType.RESOURCE,
            poiData = "lucky_clover",
            encounterRate = 0.2,
            connections = listOf("wildflower_plains_entrance", "butterfly_migration_route", "thistle_forest")
        ),
        WorldLocation(
            id = "butterfly_migration_route",
            name = "Butterfly Migration Route",
            gridX = 2, gridY = 5,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(3, 5),
            description = "Seasonal aerial display of hundreds of butterflies",
            isLandmark = true,
            encounterRate = 0.1,
            connections = listOf("clover_kingdom", "thistle_forest", "grasshopper_leap")
        ),
        WorldLocation(
            id = "morning_dew_meadow",
            name = "Morning Dew Meadow",
            gridX = -3, gridY = 4,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(2, 4),
            description = "Sparkling droplets glisten at sunrise",
            poi = POIType.RESOURCE,
            poiData = "morning_dew",
            connections = listOf("wildflower_plains_entrance", "haystack_fortress", "scarecrow_watchtower")
        ),
        WorldLocation(
            id = "thistle_forest",
            name = "Thistle Forest",
            gridX = 3, gridY = 4,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(3, 5),
            description = "Prickly purple towers rise like spires",
            isLandmark = true,
            poi = POIType.RESOURCE,
            poiData = "thistle_down",
            encounterRate = 0.25,
            connections = listOf("clover_kingdom", "butterfly_migration_route", "grasshopper_leap")
        ),
        WorldLocation(
            id = "grasshopper_leap",
            name = "Grasshopper Leap",
            gridX = 1, gridY = 5,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(3, 5),
            description = "Training ground for jump practice with grasshoppers",
            poi = POIType.QUEST_MARKER,
            poiData = "jump_training",
            encounterRate = 0.3,
            connections = listOf("butterfly_migration_route", "thistle_forest", "clover_kingdom")
        ),
        WorldLocation(
            id = "sunflower_sentinel",
            name = "Sunflower Sentinel",
            gridX = 1, gridY = 4,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(2, 4),
            description = "Massive sunflower towers over the plains",
            isLandmark = true,
            poi = POIType.RESOURCE,
            poiData = "sunflower_seeds",
            connections = listOf("wildflower_plains_entrance", "grasshopper_leap", "thistle_forest")
        ),
        WorldLocation(
            id = "lavender_labyrinth",
            name = "Lavender Labyrinth",
            gridX = -1, gridY = 4,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(3, 5),
            description = "Purple maze with a calming scent",
            encounterRate = 0.15,
            connections = listOf("wildflower_plains_entrance", "morning_dew_meadow", "sunflower_sentinel")
        ),
        WorldLocation(
            id = "poppy_field",
            name = "Poppy Field",
            gridX = 2, gridY = 4,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(2, 4),
            description = "Red blooms sway hypnotically in the breeze",
            poi = POIType.RESOURCE,
            poiData = "poppy_seeds",
            connections = listOf("sunflower_sentinel", "thistle_forest", "butterfly_migration_route")
        ),
        WorldLocation(
            id = "daisy_chain_path",
            name = "Daisy Chain Path",
            gridX = -2, gridY = 5,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(2, 4),
            description = "White flowers linked like a living necklace",
            connections = listOf("morning_dew_meadow", "lavender_labyrinth", "bluebell_hollow")
        ),
        WorldLocation(
            id = "bluebell_hollow",
            name = "Bluebell Hollow",
            gridX = -1, gridY = 5,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(3, 5),
            description = "Gentle depression filled with blue blooms",
            poi = POIType.RESOURCE,
            poiData = "bluebell_petals",
            encounterRate = 0.2,
            connections = listOf("daisy_chain_path", "lavender_labyrinth", "clover_kingdom")
        ),
        WorldLocation(
            id = "wildrose_wall",
            name = "Wildrose Wall",
            gridX = 4, gridY = 4,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(3, 6),
            description = "Thorny barrier of pink and red roses",
            isLandmark = true,
            encounterRate = 0.3,
            connections = listOf("thistle_forest", "tulip_terrace")
        ),
        WorldLocation(
            id = "tulip_terrace",
            name = "Tulip Terrace",
            gridX = 4, gridY = 5,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(3, 5),
            description = "Stepped landscape of colorful tulips",
            isLandmark = true,
            poi = POIType.RESOURCE,
            poiData = "tulip_bulbs",
            connections = listOf("wildrose_wall", "butterfly_migration_route")
        ),
        WorldLocation(
            id = "iris_overlook",
            name = "Iris Overlook",
            gridX = 3, gridY = 5,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(3, 5),
            description = "Purple flowers atop a small rise",
            connections = listOf("tulip_terrace", "butterfly_migration_route", "thistle_forest")
        ),
        WorldLocation(
            id = "marigold_meadow",
            name = "Marigold Meadow",
            gridX = -2, gridY = 4,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(2, 4),
            description = "Golden orange flowers blanket the ground",
            poi = POIType.RESOURCE,
            poiData = "marigold_petals",
            connections = listOf("morning_dew_meadow", "daisy_chain_path", "lavender_labyrinth")
        ),
        WorldLocation(
            id = "snapdragon_spine",
            name = "Snapdragon Spine",
            gridX = 1, gridY = 3,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(2, 4),
            description = "Dragon-mouthed flowers line a narrow path",
            encounterRate = 0.2,
            connections = listOf("wildflower_plains_entrance", "sunflower_sentinel", "morning_glory_arch")
        ),
        WorldLocation(
            id = "zinnia_zenith",
            name = "Zinnia Zenith",
            gridX = 0, gridY = 5,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(3, 5),
            description = "Highest point of the flower plains",
            isLandmark = true,
            connections = listOf("bluebell_hollow", "clover_kingdom", "grasshopper_leap")
        ),
        WorldLocation(
            id = "petunia_patch",
            name = "Petunia Patch",
            gridX = -3, gridY = 5,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(3, 5),
            description = "Vibrant multi-colored carpet of flowers",
            encounterRate = 0.15,
            connections = listOf("morning_dew_meadow", "daisy_chain_path")
        ),
        WorldLocation(
            id = "cosmos_cluster",
            name = "Cosmos Cluster",
            gridX = 2, gridY = 3,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(2, 4),
            description = "Delicate flowers dance on thin stems",
            connections = listOf("snapdragon_spine", "sunflower_sentinel", "garden_gnome_shadow")
        ),
        WorldLocation(
            id = "geranium_grove",
            name = "Geranium Grove",
            gridX = -1, gridY = 3,
            biome = BiomeType.GRASSLAND,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(2, 4),
            description = "Dense cluster of red and pink blooms",
            poi = POIType.RESOURCE,
            poiData = "geranium_oil",
            connections = listOf("wildflower_plains_entrance", "lavender_labyrinth", "wheat_stalk_maze")
        )
    )
    
    // Additional regions will be added in separate blocks to stay under character limits
    // Total: 99 grassland locations to be completed
    
    // REGION 2: FOREST (placeholder - will expand to 93 total)
    private val forestLocations = listOf(
        WorldLocation(
            id = "elderwood_entrance",
            name = "Elderwood Forest Edge",
            gridX = 0, gridY = 7,
            biome = BiomeType.FOREST,
            terrainType = TerrainType.GRASS,
            levelRange = LevelRange(3, 5),
            description = "Where grasslands give way to ancient trees",
            isLandmark = true,
            connections = listOf("clover_kingdom", "fern_valley", "birch_grove")
        )
        // TODO: Add 92 more forest locations
    )
    
    // REGION 3: MOUNTAIN (placeholder - will expand to 82 total)
    private val mountainLocations = listOf(
        WorldLocation(
            id = "foothill_pass",
            name = "Foothill Pass",
            gridX = 2, gridY = 0,
            biome = BiomeType.MOUNTAIN,
            terrainType = TerrainType.STONE,
            levelRange = LevelRange(3, 6),
            description = "Rocky trails mark the beginning of the climb",
            connections = listOf("butterfly_landing", "switchback_trail")
        )
        // TODO: Add 81 more mountain locations
    )
    
    // REGION 4: DESERT (placeholder)
    private val desertLocations = listOf<WorldLocation>(
        // TODO: Add desert locations
    )
    
    // REGION 5: SWAMP (placeholder)
    private val swampLocations = listOf<WorldLocation>(
        // TODO: Add swamp locations
    )
    
    // REGION 6: TUNDRA (placeholder)
    private val tundraLocations = listOf<WorldLocation>(
        // TODO: Add tundra locations
    )
    
    // REGION 7: COASTAL (placeholder)
    private val coastalLocations = listOf<WorldLocation>(
        // TODO: Add coastal locations
    )
    
    // REGION 8: CAVE (placeholder)
    private val caveLocations = listOf<WorldLocation>(
        // TODO: Add cave locations
    )
}
