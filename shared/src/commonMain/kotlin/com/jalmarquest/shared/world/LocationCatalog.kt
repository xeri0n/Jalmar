package com.jalmarquest.shared.world

import com.jalmarquest.shared.world.catalog.GRASSLAND_LOCATIONS
import com.jalmarquest.shared.world.catalog.FOREST_LOCATIONS
import com.jalmarquest.shared.world.catalog.MOUNTAIN_LOCATIONS
import com.jalmarquest.shared.world.catalog.DESERT_LOCATIONS
import com.jalmarquest.shared.world.catalog.SWAMP_LOCATIONS
import com.jalmarquest.shared.world.catalog.TUNDRA_LOCATIONS
import com.jalmarquest.shared.world.catalog.COASTAL_LOCATIONS
import com.jalmarquest.shared.world.catalog.CAVE_LOCATIONS

/**
 * Central catalog of all game locations.
 * Contains 546 unique locations across 8 biome types.
 * - 46 original base locations
 * - 500 new expansion locations across 8 regions
 */
object LocationCatalog {
    
    /**
     * All locations in the game world.
     * Total: 546 locations (46 base + 500 expansion)
     */
    val allLocations: List<Location> by lazy {
        // Base 46 locations (original game content)
        val baseLocations = listOf(
            // STARTING AREA & GRASSLAND (9 locations)
            startingVillage,
            theHenPen,
            oldQuillsStudy,
            theQuailsmith,
            gildedSeedInn,
            meadowPath,
            rollingHills,
            windmillFarm,
            crossroads,
            
            // FOREST REGION (8 locations)
            elderwood,
            whisperingGrove,
            mushroomGlade,
            huntersLodge,
            ancientTreeHeart,
            thornBrake,
            silverleafCanopy,
            forestShrine,
            
            // MOUNTAIN REGION (7 locations)
            foothillPass,
            cragpeak,
            stonebridgeGorge,
            eaglesNest,
            frostpeak,
            dwarvenOutpost,
            mountainTemple,
            
            // DESERT REGION (5 locations)
            dunesSea,
            oasisVerdant,
            sandstoneRuins,
            scorpionGulch,
            mirageSpire,
            
            // SWAMP REGION (5 locations)
            mireMaw,
            rottenHollow,
            witchHut,
            boglanter,
            sunkenTemple,
            
            // TUNDRA REGION (4 locations)
            frozenWaste,
            icecrystalCavern,
            auroraFields,
            frostgiantLair,
            
            // COASTAL REGION (5 locations)
            harborTown,
            cliffside,
            shipwreckCove,
            lighthousePoint,
            tidepool,
            
            // CAVE/UNDERGROUND (3 locations)
            crystalMines,
            deepDark,
            forgottenCatacombs
        )
        
        // Expansion locations (500 new locations across 8 regions)
        // Concatenate all regional catalogs with base locations
        baseLocations + 
            GRASSLAND_LOCATIONS +  // 90 locations
            FOREST_LOCATIONS +     // 85 locations
            MOUNTAIN_LOCATIONS +   // 75 locations
            DESERT_LOCATIONS +     // 60 locations
            SWAMP_LOCATIONS +      // 55 locations
            TUNDRA_LOCATIONS +     // 50 locations
            COASTAL_LOCATIONS +    // 50 locations
            CAVE_LOCATIONS         // 35 locations
    }
    
    /**
     * Get a location by its ID.
     */
    fun getLocation(id: String): Location? {
        return allLocations.find { it.id == id }
    }
    
    /**
     * Get all locations in a specific biome.
     */
    fun getLocationsByBiome(biome: BiomeType): List<Location> {
        return allLocations.filter { it.biome == biome }
    }
    
    /**
     * Get all settlement locations.
     */
    fun getSettlements(): List<Location> {
        return allLocations.filter { it.isSettlement }
    }
    
    /**
     * Get all locations with fast travel.
     */
    fun getFastTravelLocations(): List<Location> {
        return allLocations.filter { it.hasFastTravel }
    }
    
    // ========== GRASSLAND LOCATIONS ==========
    
    private val startingVillage = Location(
        id = "starting_village",
        name = "Buttonburgh",
        description = LocationDescription.withAllSeasons(
            spring = "The quaint settlement of Buttonburgh bustles with activity—well, 'bustles' if you're a button quail. To you, the scattered birdseed beneath the backyard feeder is a veritable marketplace. A puddle reflects the spring sky like a grand plaza fountain, and the overturned flowerpot you call home is positively palatial. Fresh dandelions tower overhead like ancient oaks. Four establishments of great importance line the yard's perimeter.",
            summer = "Buttonburgh shimmers in the afternoon heat. The Great Seed Dispensary (bird feeder) casts a merciful shadow, and you've claimed a prime dust-bathing spot beneath the porch—your personal spa. A forgotten garden gnome looms in the distance like some terrifying colossus. The humans' sprinkler creates what you consider to be a magnificent water feature for the truly brave. The four great buildings of Buttonburgh stand ready to serve.",
            autumn = "Buttonburgh takes on golden hues as fallen leaves become mountainous obstacles in your daily patrol route. The seed supply seems endless—clearly the humans recognize your importance. A discarded acorn cap makes an excellent helmet (you've tried it). The air smells of composting things, which is to say, paradise. The town's four establishments look particularly cozy in the autumn light.",
            winter = "Buttonburgh is blanketed in the white stuff. Your fluffed-up feathers make you roughly spherical—perfect insulation. The humans have moved your seed station closer to the warm porch, bless them. A frozen puddle has become a treacherous ice rink you must navigate with great dignity (you've slipped twice, but no one saw). Your breath makes tiny clouds, proof of your dragon-like might. The four buildings offer warm shelter from the cold."
        ),
        biome = BiomeType.GRASSLAND,
        gridX = 0,
        gridY = 0,
        connections = listOf(
            LocationConnection("the_hen_pen", Direction.UP),
            LocationConnection("old_quills_study", Direction.DOWN),
            LocationConnection("the_quailsmith", Direction.EAST),
            LocationConnection("gilded_seed_inn", Direction.WEST),
            LocationConnection("meadow_path", Direction.NORTH),
            LocationConnection("rolling_hills", Direction.NORTHEAST),
            LocationConnection("windmill_farm", Direction.NORTHWEST),
            // Bridge connections to GRASSLAND expansion region
            LocationConnection("pebble_plaza", Direction.SOUTH),
            LocationConnection("puddle_lake", Direction.SOUTHWEST),
            LocationConnection("dandelion_grove", Direction.SOUTHEAST)
        ),
        isSettlement = true,
        hasFastTravel = true,
        isSafeZone = true,
        shopAvailable = true,
        innAvailable = true,
        encounterRate = 0.0,
        recommendedLevel = 1,
        lore = "Buttonburgh: Where one tiny quail's backyard becomes an entire universe. Population: 1 button quail of great renown (you), several ungrateful sparrows, and two humans who serve as your loyal retainers."
    )
    
    // ========== BUTTONBURGH BUILDINGS ==========
    
    private val theHenPen = Location(
        id = "the_hen_pen",
        name = "The Hen Pen",
        description = LocationDescription.simple(
            "Madame Zaza's establishment is the social hub of Buttonburgh. A overturned terracotta pot creates a cozy alcove where the local birds gather to gossip. Madame Zaza herself—a stately hen of indeterminate age—presides over the space with regal authority. She knows everything that happens in Buttonburgh, often before it happens. Seed hulls are scattered artfully across the ground (ambiance, she calls it), and there's always a rumor or quest to be had if you know how to ask."
        ),
        biome = BiomeType.GRASSLAND,
        gridX = 0,
        gridY = 0,
        connections = listOf(
            LocationConnection("starting_village", Direction.DOWN)
        ),
        isSettlement = true,
        isSafeZone = true,
        encounterRate = 0.0,
        recommendedLevel = 1,
        questGiverIds = listOf("madame_zaza"),
        lore = "Madame Zaza claims she hatched from a golden egg laid by a phoenix. No one believes her, but no one dares contradict her either."
    )
    
    private val oldQuillsStudy = Location(
        id = "old_quills_study",
        name = "Old Quill's Study",
        description = LocationDescription.simple(
            "Beneath the garden bench lies Old Quill's Study—a hollow carved beneath gnarled roots that feels older than time itself. The wizened quail philosopher lives surrounded by 'books' (actually seed catalogs the humans dropped), dried herbs (weeds), and mysterious artifacts (a bottle cap, two acorns, and something that might be a button). Old Quill speaks in cryptic riddles and occasionally forgets who you are mid-conversation, but his wisdom is legendary. Probably. The study smells of earth and ancient knowledge (mostly earth)."
        ),
        biome = BiomeType.GRASSLAND,
        gridX = 0,
        gridY = 0,
        connections = listOf(
            LocationConnection("starting_village", Direction.UP)
        ),
        isSettlement = true,
        isSafeZone = true,
        encounterRate = 0.0,
        recommendedLevel = 1,
        questGiverIds = listOf("old_quill"),
        lore = "Old Quill once predicted that 'the seeds would fall when the great light returns.' This happens every morning. He's never been wrong."
    )
    
    private val theQuailsmith = Location(
        id = "the_quailsmith",
        name = "The Quailsmith",
        description = LocationDescription.simple(
            "Grumble Forgepaw, the temperamental mole craftsman, operates his forge in a shallow burrow near the toolshed. The 'forge' is actually a sun-warmed flat stone, but Grumble insists on the drama of hammer strikes (he taps things with a pebble). He specializes in converting mundane items into epic equipment: twigs become spears, acorn caps become helmets, and bottle caps become shields. His work is impeccable, though he grumbles through every transaction. 'I'm an ARTIST, not a merchant,' he mutters, while carefully polishing your new twig spear."
        ),
        biome = BiomeType.GRASSLAND,
        gridX = 0,
        gridY = 0,
        connections = listOf(
            LocationConnection("starting_village", Direction.WEST)
        ),
        isSettlement = true,
        isSafeZone = true,
        shopAvailable = true,
        encounterRate = 0.0,
        recommendedLevel = 1,
        questGiverIds = listOf("grumble_forgepaw"),
        lore = "Grumble Forgepaw once crafted a suit of armor from seven acorn caps. The wearer (a particularly vain sparrow) immediately sank in a puddle. Grumble called it 'performance art.'"
    )
    
    private val gildedSeedInn = Location(
        id = "gilded_seed_inn",
        name = "The Gilded Seed Inn",
        description = LocationDescription.simple(
            "The most prestigious establishment in all of Buttonburgh! The inn is housed in an old wooden crate turned on its side, lined with soft moss and down feathers. Innkeeper Fluffbottom (a plump quail with exceptional hospitality skills) maintains the cozy space with pride. A dish of fresh water serves as the bar, and premium seeds are displayed in bottle caps like fine cuisine. Travelers (mostly local birds) gather here to rest, share stories, and occasionally break into song. The inn's motto: 'No predators, no problems, no leaving hungry.'"
        ),
        biome = BiomeType.GRASSLAND,
        gridX = 0,
        gridY = 0,
        connections = listOf(
            LocationConnection("starting_village", Direction.EAST)
        ),
        isSettlement = true,
        isSafeZone = true,
        innAvailable = true,
        shopAvailable = true,
        encounterRate = 0.0,
        recommendedLevel = 1,
        questGiverIds = listOf("innkeeper_fluffbottom"),
        lore = "The Gilded Seed Inn earned its name when Fluffbottom once found an actual gilded (spraypainted) sunflower seed. It's displayed in a place of honor. No one knows where it came from. No one asks."
    )
    
    private val meadowPath = Location(
        id = "meadow_path",
        name = "The Meadow Path",
        description = LocationDescription.withAllSeasons(
            spring = "What the humans call a 'garden path' is, to you, an epic trail through towering vegetation. Fresh dandelions loom like ancient trees, and butterflies—those show-offs—perform aerial maneuvers you can only dream of. A discarded bottle cap glints in the grass like treasure. The path is 'winding' mostly because you keep stopping to investigate interesting smells.",
            summer = "The Meadow Path has become a gauntlet of sun-baked adventure. The grass is crunchy underfoot (underclaw?), and you've learned to time your crossings between shade patches. Butterflies mock you from above with their effortless flight. A particularly ambitious beetle crosses your path—you exchange respectful nods. The heat makes your head-feathers do that thing, and you are magnificent.",
            autumn = "Fallen leaves transform the Meadow Path into an obstacle course worthy of legends. You've discovered that jumping INTO leaf piles is fun, but jumping OUT is significantly harder. The butterflies are leaving (good riddance, honestly), and the seed selection has improved dramatically—nature's autumn clearance sale. Your tiny footprints in the morning frost prove you were here first.",
            winter = "The Meadow Path is now The Meadow Path: Extreme Winter Edition. Navigation requires careful planning, as snow drifts tower overhead like miniature mountain ranges. You've perfected the art of the running-hop-flutter to minimize foot-freezing. A robin eyes you suspiciously from a fence post—this is YOUR territory, thank you very much. Your winter fluffiness makes you 40% larger and 100% more dignified."
        ),
        biome = BiomeType.GRASSLAND,
        gridX = 0,
        gridY = 1,
        connections = listOf(
            LocationConnection("starting_village", Direction.SOUTH),
            LocationConnection("elderwood", Direction.NORTH),
            LocationConnection("crossroads", Direction.EAST)
        ),
        encounterRate = 0.3,
        recommendedLevel = 1
    )
    
    private val rollingHills = Location(
        id = "rolling_hills",
        name = "Rolling Hills",
        description = LocationDescription.simple("Gentle slopes covered in tall grass. You can see for miles from the hilltops."),
        biome = BiomeType.GRASSLAND,
        gridX = 1,
        gridY = 0,
        connections = listOf(
            LocationConnection("starting_village", Direction.WEST),
            LocationConnection("crossroads", Direction.NORTH),
            LocationConnection("foothill_pass", Direction.EAST)
        ),
        encounterRate = 0.4,
        recommendedLevel = 2
    )
    
    private val windmillFarm = Location(
        id = "windmill_farm",
        name = "Windmill Farm",
        description = LocationDescription.simple("Large wooden windmills turn slowly above fields of golden wheat."),
        biome = BiomeType.GRASSLAND,
        gridX = -1,
        gridY = 0,
        connections = listOf(
            LocationConnection("starting_village", Direction.EAST),
            LocationConnection("meadow_path", Direction.NORTHEAST),
            LocationConnection("cliffside", Direction.WEST)
        ),
        isSettlement = true,
        shopAvailable = true,
        encounterRate = 0.2,
        recommendedLevel = 1
    )
    
    private val crossroads = Location(
        id = "crossroads",
        name = "The Crossroads",
        description = LocationDescription.simple("Where four paths meet. A weathered signpost points to distant lands."),
        biome = BiomeType.GRASSLAND,
        gridX = 1,
        gridY = 1,
        connections = listOf(
            LocationConnection("meadow_path", Direction.WEST),
            LocationConnection("rolling_hills", Direction.SOUTH),
            LocationConnection("elderwood", Direction.NORTH),
            LocationConnection("foothill_pass", Direction.EAST),
            // Bridge connection to GRASSLAND expansion region
            LocationConnection("seedling_nursery", Direction.EAST)
        ),
        encounterRate = 0.5,
        recommendedLevel = 2,
        lore = "Many travelers have made fateful decisions at this junction. Choose your path wisely."
    )
    
    // ========== FOREST LOCATIONS ==========
    
    private val elderwood = Location(
        id = "elderwood",
        name = "Elderwood Forest",
        description = LocationDescription.simple("Ancient trees tower overhead, their canopy filtering sunlight into green shadows."),
        biome = BiomeType.FOREST,
        gridX = 0,
        gridY = 2,
        connections = listOf(
            LocationConnection("meadow_path", Direction.SOUTH),
            LocationConnection("whispering_grove", Direction.NORTH),
            LocationConnection("crossroads", Direction.SOUTHEAST),
            LocationConnection("mushroom_glade", Direction.WEST),
            // Bridge connections to FOREST expansion region
            LocationConnection("fern_valley", Direction.NORTHWEST),
            LocationConnection("birch_grove", Direction.NORTHEAST)
        ),
        encounterRate = 0.7,
        recommendedLevel = 3
    )
    
    private val whisperingGrove = Location(
        id = "whispering_grove",
        name = "Whispering Grove",
        description = LocationDescription.simple("The wind through these trees sounds like distant voices sharing secrets."),
        biome = BiomeType.FOREST,
        gridX = 0,
        gridY = 3,
        connections = listOf(
            LocationConnection("elderwood", Direction.SOUTH),
            LocationConnection("ancient_tree_heart", Direction.NORTH),
            LocationConnection("hunters_lodge", Direction.EAST)
        ),
        encounterRate = 0.8,
        recommendedLevel = 4,
        lore = "Druids claim the trees here remember every word spoken beneath their boughs."
    )
    
    private val mushroomGlade = Location(
        id = "mushroom_glade",
        name = "Mushroom Glade",
        description = LocationDescription.simple("Massive luminescent mushrooms grow in impossible colors. The air shimmers with spores."),
        biome = BiomeType.FOREST,
        gridX = -1,
        gridY = 2,
        connections = listOf(
            LocationConnection("elderwood", Direction.EAST),
            LocationConnection("thorn_brake", Direction.NORTH)
        ),
        encounterRate = 0.9,
        recommendedLevel = 5,
        lore = "The mushrooms here are said to grant visions to those brave enough to consume them."
    )
    
    private val huntersLodge = Location(
        id = "hunters_lodge",
        name = "Hunter's Lodge",
        description = LocationDescription.simple("A rustic cabin where hunters trade stories and supplies."),
        biome = BiomeType.FOREST,
        gridX = 1,
        gridY = 3,
        connections = listOf(
            LocationConnection("whispering_grove", Direction.WEST),
            LocationConnection("silverleaf_canopy", Direction.NORTH),
            LocationConnection("foothill_pass", Direction.EAST)
        ),
        isSettlement = true,
        shopAvailable = true,
        innAvailable = true,
        encounterRate = 0.1,
        recommendedLevel = 4
    )
    
    private val ancientTreeHeart = Location(
        id = "ancient_tree_heart",
        name = "Ancient Tree Heart",
        description = LocationDescription.simple("A massive tree, hollow inside. Its heartwood chamber glows with mystical energy."),
        biome = BiomeType.FOREST,
        gridX = 0,
        gridY = 4,
        connections = listOf(
            LocationConnection("whispering_grove", Direction.SOUTH),
            LocationConnection("forest_shrine", Direction.EAST)
        ),
        encounterRate = 1.0,
        recommendedLevel = 8,
        lore = "This tree predates the first civilizations. Some say it is the forest's consciousness made physical."
    )
    
    private val thornBrake = Location(
        id = "thorn_brake",
        name = "Thorn Brake",
        description = LocationDescription.simple("Dense brambles with thorns like daggers. Something moves in the shadows."),
        biome = BiomeType.FOREST,
        gridX = -1,
        gridY = 3,
        connections = listOf(
            LocationConnection("mushroom_glade", Direction.SOUTH),
            LocationConnection("mire_maw", Direction.WEST)
        ),
        encounterRate = 1.0,
        recommendedLevel = 6
    )
    
    private val silverleafCanopy = Location(
        id = "silverleaf_canopy",
        name = "Silverleaf Canopy",
        description = LocationDescription.simple("Trees with silver-white leaves create a shimmering ceiling. Everything here feels ethereal."),
        biome = BiomeType.FOREST,
        gridX = 1,
        gridY = 4,
        connections = listOf(
            LocationConnection("hunters_lodge", Direction.SOUTH),
            LocationConnection("forest_shrine", Direction.WEST)
        ),
        encounterRate = 0.8,
        recommendedLevel = 7
    )
    
    private val forestShrine = Location(
        id = "forest_shrine",
        name = "Forest Shrine",
        description = LocationDescription.simple("An ancient stone altar covered in moss and offerings to the forest spirits."),
        biome = BiomeType.FOREST,
        gridX = 0,
        gridY = 5,
        connections = listOf(
            LocationConnection("ancient_tree_heart", Direction.WEST),
            LocationConnection("silverleaf_canopy", Direction.EAST)
        ),
        isSafeZone = true,
        encounterRate = 0.0,
        recommendedLevel = 7,
        lore = "Offerings left here are said to grant the forest's blessing for safe travels."
    )
    
    // ========== MOUNTAIN LOCATIONS ==========
    
    private val foothillPass = Location(
        id = "foothill_pass",
        name = "Foothill Pass",
        description = LocationDescription.simple("The mountain path begins here. Rocky terrain stretches upward into the clouds."),
        biome = BiomeType.MOUNTAIN,
        gridX = 2,
        gridY = 0,
        connections = listOf(
            LocationConnection("rolling_hills", Direction.WEST),
            LocationConnection("crossroads", Direction.NORTHWEST),
            LocationConnection("cragpeak", Direction.NORTH),
            LocationConnection("dunes_sea", Direction.SOUTH),
            // Bridge connection to MOUNTAIN expansion region
            LocationConnection("scree_slope_approach", Direction.NORTH)
        ),
        encounterRate = 0.6,
        recommendedLevel = 3
    )
    
    private val cragpeak = Location(
        id = "cragpeak",
        name = "Cragpeak",
        description = LocationDescription.simple("Jagged rocks and steep cliffs. The wind howls through narrow passes."),
        biome = BiomeType.MOUNTAIN,
        gridX = 2,
        gridY = 1,
        connections = listOf(
            LocationConnection("foothill_pass", Direction.SOUTH),
            LocationConnection("stonebridge_gorge", Direction.NORTH),
            LocationConnection("hunters_lodge", Direction.WEST)
        ),
        encounterRate = 0.9,
        recommendedLevel = 5
    )
    
    private val stonebridgeGorge = Location(
        id = "stonebridge_gorge",
        name = "Stonebridge Gorge",
        description = LocationDescription.simple("A massive stone bridge spans a terrifying chasm. Don't look down."),
        biome = BiomeType.MOUNTAIN,
        gridX = 2,
        gridY = 2,
        connections = listOf(
            LocationConnection("cragpeak", Direction.SOUTH),
            LocationConnection("eagles_nest", Direction.NORTH),
            LocationConnection("crystal_mines", Direction.DOWN, travelTime = 2)
        ),
        encounterRate = 0.7,
        recommendedLevel = 6
    )
    
    private val eaglesNest = Location(
        id = "eagles_nest",
        name = "Eagle's Nest",
        description = LocationDescription.simple("The highest peak. Giant eagles circle overhead. The view is breathtaking."),
        biome = BiomeType.MOUNTAIN,
        gridX = 2,
        gridY = 3,
        connections = listOf(
            LocationConnection("stonebridge_gorge", Direction.SOUTH),
            LocationConnection("frostpeak", Direction.NORTH)
        ),
        encounterRate = 1.0,
        recommendedLevel = 8,
        lore = "Only the most dedicated climbers reach this summit. The eagles here are said to be sentient."
    )
    
    private val frostpeak = Location(
        id = "frostpeak",
        name = "Frostpeak Summit",
        description = LocationDescription.simple("Eternal snow covers this peak. The temperature is bone-chilling."),
        biome = BiomeType.MOUNTAIN,
        gridX = 2,
        gridY = 4,
        connections = listOf(
            LocationConnection("eagles_nest", Direction.SOUTH),
            LocationConnection("mountain_temple", Direction.WEST),
            LocationConnection("frozen_waste", Direction.NORTH)
        ),
        encounterRate = 1.0,
        recommendedLevel = 12
    )
    
    private val dwarvenOutpost = Location(
        id = "dwarven_outpost",
        name = "Dwarven Outpost",
        description = LocationDescription.simple("A fortified dwarven settlement carved into the mountainside."),
        biome = BiomeType.MOUNTAIN,
        gridX = 3,
        gridY = 2,
        connections = listOf(
            LocationConnection("stonebridge_gorge", Direction.WEST),
            LocationConnection("crystal_mines", Direction.DOWN, travelTime = 1)
        ),
        isSettlement = true,
        hasFastTravel = true,
        shopAvailable = true,
        innAvailable = true,
        encounterRate = 0.0,
        recommendedLevel = 8,
        lore = "The dwarves here are master smiths, crafting weapons and armor of legendary quality."
    )
    
    private val mountainTemple = Location(
        id = "mountain_temple",
        name = "Mountain Temple",
        description = LocationDescription.simple("An ancient monastery where monks train in isolation and meditation."),
        biome = BiomeType.MOUNTAIN,
        gridX = 1,
        gridY = 4,
        connections = listOf(
            LocationConnection("frostpeak", Direction.EAST),
            LocationConnection("aurora_fields", Direction.NORTH)
        ),
        isSettlement = true,
        isSafeZone = true,
        shopAvailable = true,
        encounterRate = 0.0,
        recommendedLevel = 10,
        lore = "The monks here guard ancient secrets of combat and wisdom passed down through millennia."
    )
    
    // ========== DESERT LOCATIONS ==========
    
    private val dunesSea = Location(
        id = "dunes_sea",
        name = "Sea of Dunes",
        description = LocationDescription.simple("Endless golden sand stretches in all directions. The sun is merciless."),
        biome = BiomeType.DESERT,
        gridX = 2,
        gridY = -1,
        connections = listOf(
            LocationConnection("foothill_pass", Direction.NORTH),
            LocationConnection("oasis_verdant", Direction.WEST),
            LocationConnection("scorpion_gulch", Direction.EAST),
            // Bridge connections to DESERT expansion region
            LocationConnection("sand_ripple_plains", Direction.SOUTH),
            LocationConnection("wandering_dunes", Direction.SOUTHEAST)
        ),
        encounterRate = 0.8,
        recommendedLevel = 4
    )
    
    private val oasisVerdant = Location(
        id = "oasis_verdant",
        name = "Verdant Oasis",
        description = LocationDescription.simple("A blessed pool of clear water surrounded by palm trees. Life flourishes here."),
        biome = BiomeType.DESERT,
        gridX = 1,
        gridY = -1,
        connections = listOf(
            LocationConnection("dunes_sea", Direction.EAST),
            LocationConnection("sandstone_ruins", Direction.SOUTH)
        ),
        isSettlement = true,
        isSafeZone = true,
        shopAvailable = true,
        encounterRate = 0.1,
        recommendedLevel = 5
    )
    
    private val sandstoneRuins = Location(
        id = "sandstone_ruins",
        name = "Sandstone Ruins",
        description = LocationDescription.simple("The remains of a once-great civilization, now half-buried in sand."),
        biome = BiomeType.DESERT,
        gridX = 1,
        gridY = -2,
        connections = listOf(
            LocationConnection("oasis_verdant", Direction.NORTH),
            LocationConnection("mirage_spire", Direction.EAST),
            LocationConnection("forgotten_catacombs", Direction.DOWN, travelTime = 2)
        ),
        encounterRate = 1.0,
        recommendedLevel = 7,
        lore = "These ruins whisper of a kingdom that angered the gods and was buried as punishment."
    )
    
    private val scorpionGulch = Location(
        id = "scorpion_gulch",
        name = "Scorpion Gulch",
        description = LocationDescription.simple("A rocky canyon where giant scorpions make their nests. Extremely dangerous."),
        biome = BiomeType.DESERT,
        gridX = 3,
        gridY = -1,
        connections = listOf(
            LocationConnection("dunes_sea", Direction.WEST),
            LocationConnection("mirage_spire", Direction.SOUTH)
        ),
        encounterRate = 1.0,
        recommendedLevel = 9
    )
    
    private val mirageSpire = Location(
        id = "mirage_spire",
        name = "Mirage Spire",
        description = LocationDescription.simple("A shimmering tower that appears and disappears. Is it real or an illusion?"),
        biome = BiomeType.DESERT,
        gridX = 2,
        gridY = -2,
        connections = listOf(
            LocationConnection("sandstone_ruins", Direction.WEST),
            LocationConnection("scorpion_gulch", Direction.NORTH)
        ),
        encounterRate = 1.0,
        recommendedLevel = 11,
        lore = "Those who enter the spire often emerge changed, if they emerge at all."
    )
    
    // ========== SWAMP LOCATIONS ==========
    
    private val mireMaw = Location(
        id = "mire_maw",
        name = "Mire Maw",
        description = LocationDescription.simple("The entrance to the swamp. Murky water and gnarled trees create an ominous atmosphere."),
        biome = BiomeType.SWAMP,
        gridX = -2,
        gridY = 3,
        connections = listOf(
            LocationConnection("thorn_brake", Direction.EAST),
            LocationConnection("rotten_hollow", Direction.NORTH),
            LocationConnection("boglanter", Direction.WEST)
        ),
        encounterRate = 1.0,
        recommendedLevel = 7
    )
    
    private val rottenHollow = Location(
        id = "rotten_hollow",
        name = "Rotten Hollow",
        description = LocationDescription.simple("Dead trees rise from stagnant water. The smell of decay is overwhelming."),
        biome = BiomeType.SWAMP,
        gridX = -2,
        gridY = 4,
        connections = listOf(
            LocationConnection("mire_maw", Direction.SOUTH),
            LocationConnection("witch_hut", Direction.WEST),
            LocationConnection("sunken_temple", Direction.NORTH)
        ),
        encounterRate = 1.0,
        recommendedLevel = 8
    )
    
    private val witchHut = Location(
        id = "witch_hut",
        name = "Witch's Hut",
        description = LocationDescription.simple("A crooked shack on stilts. Smoke rises from the chimney. Approach with caution."),
        biome = BiomeType.SWAMP,
        gridX = -3,
        gridY = 4,
        connections = listOf(
            LocationConnection("rotten_hollow", Direction.EAST),
            LocationConnection("boglanter", Direction.SOUTH)
        ),
        isSettlement = true,
        shopAvailable = true,
        encounterRate = 0.5,
        recommendedLevel = 9,
        lore = "The witch trades in rare ingredients and forbidden knowledge. Her prices are... unusual."
    )
    
    private val boglanter = Location(
        id = "boglanter",
        name = "Boglanter's Rest",
        description = LocationDescription.simple("Eerie lights dance over the water at night, luring travelers to their doom."),
        biome = BiomeType.SWAMP,
        gridX = -3,
        gridY = 3,
        connections = listOf(
            LocationConnection("mire_maw", Direction.EAST),
            LocationConnection("witch_hut", Direction.NORTH)
        ),
        encounterRate = 1.0,
        recommendedLevel = 10
    )
    
    private val sunkenTemple = Location(
        id = "sunken_temple",
        name = "Sunken Temple",
        description = LocationDescription.simple("An ancient temple slowly sinking into the swamp. Strange chants echo from within."),
        biome = BiomeType.SWAMP,
        gridX = -2,
        gridY = 5,
        connections = listOf(
            LocationConnection("rotten_hollow", Direction.SOUTH)
        ),
        encounterRate = 1.0,
        recommendedLevel = 13,
        lore = "A cult worships something terrible in these drowned halls. Few who investigate return."
    )
    
    // ========== TUNDRA LOCATIONS ==========
    
    private val frozenWaste = Location(
        id = "frozen_waste",
        name = "Frozen Waste",
        description = LocationDescription.simple("An endless expanse of ice and snow. Visibility is nearly zero in the blizzard."),
        biome = BiomeType.TUNDRA,
        gridX = 2,
        gridY = 5,
        connections = listOf(
            LocationConnection("frostpeak", Direction.SOUTH),
            LocationConnection("icecrystal_cavern", Direction.NORTH),
            LocationConnection("aurora_fields", Direction.WEST),
            LocationConnection("frost_bite_ridge", Direction.SOUTH),
            LocationConnection("frozen_lake", Direction.SOUTH)
        ),
        encounterRate = 1.0,
        recommendedLevel = 13
    )
    
    private val icecrystalCavern = Location(
        id = "icecrystal_cavern",
        name = "Icecrystal Cavern",
        description = LocationDescription.simple("A cave made entirely of blue ice that glows from within."),
        biome = BiomeType.TUNDRA,
        gridX = 2,
        gridY = 6,
        connections = listOf(
            LocationConnection("frozen_waste", Direction.SOUTH),
            LocationConnection("frostgiant_lair", Direction.NORTH)
        ),
        encounterRate = 1.0,
        recommendedLevel = 14,
        lore = "The ice here is said to be the frozen tears of an ancient ice dragon."
    )
    
    private val auroraFields = Location(
        id = "aurora_fields",
        name = "Aurora Fields",
        description = LocationDescription.simple("The northern lights dance permanently in the sky here, casting rainbow shadows."),
        biome = BiomeType.TUNDRA,
        gridX = 1,
        gridY = 5,
        connections = listOf(
            LocationConnection("mountain_temple", Direction.SOUTH),
            LocationConnection("frozen_waste", Direction.EAST)
        ),
        isSafeZone = true,
        encounterRate = 0.3,
        recommendedLevel = 12,
        lore = "The aurora here is said to be a doorway to other realms, visible only under certain conditions."
    )
    
    private val frostgiantLair = Location(
        id = "frostgiant_lair",
        name = "Frostgiant's Lair",
        description = LocationDescription.simple("Massive ice formations carved into a fortress. The home of the Frostgiant King."),
        biome = BiomeType.TUNDRA,
        gridX = 2,
        gridY = 7,
        connections = listOf(
            LocationConnection("icecrystal_cavern", Direction.SOUTH)
        ),
        encounterRate = 1.0,
        recommendedLevel = 18,
        lore = "The Frostgiant King has ruled the tundra for a thousand years. He does not welcome visitors."
    )
    
    // ========== COASTAL LOCATIONS ==========
    
    private val harborTown = Location(
        id = "harbor_town",
        name = "Harbor Town",
        description = LocationDescription.simple("A bustling port city. Ships from distant lands dock at the wooden piers."),
        biome = BiomeType.COASTAL,
        gridX = -2,
        gridY = 0,
        connections = listOf(
            LocationConnection("cliffside", Direction.NORTH),
            LocationConnection("shipwreck_cove", Direction.SOUTH),
            LocationConnection("windmill_farm", Direction.EAST),
            // Bridge connection to COASTAL expansion region
            LocationConnection("fishing_wharf", Direction.SOUTHWEST)
        ),
        isSettlement = true,
        hasFastTravel = true,
        isSafeZone = true,
        shopAvailable = true,
        innAvailable = true,
        encounterRate = 0.0,
        recommendedLevel = 5,
        lore = "The largest trading hub in the region. Anything can be bought or sold here, for the right price."
    )
    
    private val cliffside = Location(
        id = "cliffside",
        name = "Cliffside Path",
        description = LocationDescription.simple("A narrow path along steep ocean cliffs. Waves crash against the rocks below."),
        biome = BiomeType.COASTAL,
        gridX = -2,
        gridY = 1,
        connections = listOf(
            LocationConnection("harbor_town", Direction.SOUTH),
            LocationConnection("lighthouse_point", Direction.NORTH),
            LocationConnection("windmill_farm", Direction.EAST)
        ),
        encounterRate = 0.6,
        recommendedLevel = 4
    )
    
    private val shipwreckCove = Location(
        id = "shipwreck_cove",
        name = "Shipwreck Cove",
        description = LocationDescription.simple("Dozens of wrecked ships litter this hidden cove. Treasure hunters' paradise."),
        biome = BiomeType.COASTAL,
        gridX = -2,
        gridY = -1,
        connections = listOf(
            LocationConnection("harbor_town", Direction.NORTH),
            LocationConnection("tidepool", Direction.WEST)
        ),
        encounterRate = 1.0,
        recommendedLevel = 6,
        lore = "Pirates once used this cove as their hideout. Their treasure is still said to be hidden here."
    )
    
    private val lighthousePoint = Location(
        id = "lighthouse_point",
        name = "Lighthouse Point",
        description = LocationDescription.simple("An old lighthouse stands watch over the rocky shore. Its light still burns."),
        biome = BiomeType.COASTAL,
        gridX = -2,
        gridY = 2,
        connections = listOf(
            LocationConnection("cliffside", Direction.SOUTH)
        ),
        isSafeZone = true,
        encounterRate = 0.2,
        recommendedLevel = 5,
        lore = "The lighthouse keeper has been tending this light for 40 years, waiting for someone who never returned."
    )
    
    private val tidepool = Location(
        id = "tidepool",
        name = "Tidepool Grotto",
        description = LocationDescription.simple("A network of tide pools filled with strange sea creatures and shells."),
        biome = BiomeType.COASTAL,
        gridX = -3,
        gridY = -1,
        connections = listOf(
            LocationConnection("shipwreck_cove", Direction.EAST),
            LocationConnection("deep_dark", Direction.DOWN, travelTime = 3, requiredLevel = 10)
        ),
        encounterRate = 0.8,
        recommendedLevel = 7
    )
    
    // ========== CAVE/UNDERGROUND LOCATIONS ==========
    
    private val crystalMines = Location(
        id = "crystal_mines",
        name = "Crystal Mines",
        description = LocationDescription.simple("An abandoned mine filled with valuable crystal formations. Echoes of pickaxes past."),
        biome = BiomeType.CAVE,
        gridX = 2,
        gridY = 2,
        connections = listOf(
            LocationConnection("stonebridge_gorge", Direction.UP, travelTime = 2),
            LocationConnection("dwarven_outpost", Direction.UP, travelTime = 1),
            LocationConnection("deep_dark", Direction.DOWN, travelTime = 2),
            // Bridge connection to CAVE expansion region
            LocationConnection("amethyst_chamber", Direction.EAST)
        ),
        encounterRate = 1.0,
        recommendedLevel = 8
    )
    
    private val deepDark = Location(
        id = "deep_dark",
        name = "The Deep Dark",
        description = LocationDescription.simple("The deepest caverns beneath the world. Absolute darkness. Strange sounds in the distance."),
        biome = BiomeType.CAVE,
        gridX = 0,
        gridY = -5,
        connections = listOf(
            LocationConnection("crystal_mines", Direction.UP, travelTime = 2),
            LocationConnection("tidepool", Direction.UP, travelTime = 3),
            LocationConnection("forgotten_catacombs", Direction.EAST),
            // Bridge connection to CAVE Deep Dark expansion region
            LocationConnection("whisper_corridor", Direction.DOWN),
            LocationConnection("desert_bone_maze", Direction.UP)
        ),
        encounterRate = 1.0,
        recommendedLevel = 15,
        lore = "Few who venture this deep return. Those who do speak of things that should not exist."
    )
    
    private val forgottenCatacombs = Location(
        id = "forgotten_catacombs",
        name = "Forgotten Catacombs",
        description = LocationDescription.simple("Ancient burial chambers beneath the desert. The dead here do not rest easy."),
        biome = BiomeType.CAVE,
        gridX = 1,
        gridY = -5,
        connections = listOf(
            LocationConnection("sandstone_ruins", Direction.UP, travelTime = 2),
            LocationConnection("deep_dark", Direction.WEST),
            LocationConnection("cave_bone_maze", Direction.DOWN),
            LocationConnection("ossuary_chapel", Direction.DOWN)
        ),
        encounterRate = 1.0,
        recommendedLevel = 16,
        lore = "The pharaohs of old were buried here with all their treasures. And all their curses."
    )
}
