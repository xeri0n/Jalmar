package com.jalmarquest.shared.npc

/**
 * Static catalog of all NPCs in JalmarQuest.
 * 
 * NPCs are organized by location and occupation. All NPCs have daily schedules
 * that define their movements and activities throughout the day.
 */
object NPCCatalog {
    
    // ===== BUTTONBURGH VILLAGE NPCs =====
    
    /**
     * Elder Quail - Village elder and quest giver
     */
    val elderQuail = NPC(
        id = "elder_quail",
        name = "Elder Quail",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 8,
            courage = 7,
            wisdom = 10,
            humor = 6,
            traits = listOf("wise", "patient", "kind")
        ),
        homeLocationId = "buttonburgh_village",
        occupation = NPCOccupation.ELDER,
        factionId = "buttonburgh_council",
        defaultDialogueTreeId = "elder_quail_greeting",
        questGiverIds = listOf("tutorial_first_steps", "main_gnome_threat"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "buttonburgh_village", "sleeping"),
                ScheduleEntry(6, 12, "buttonburgh_village", "meditating"),
                ScheduleEntry(12, 18, "buttonburgh_village", "greeting_visitors"),
                ScheduleEntry(18, 24, "buttonburgh_village", "resting")
            )
        )
    )
    
    /**
     * Grumble Forgepaw - Mole craftsman at The Quailsmith
     */
    val grumbleForgepaw = NPC(
        id = "grumble_forgepaw",
        name = "Grumble Forgepaw",
        species = NPCSpecies.MOLE,
        personality = NPCPersonality(
            friendliness = 5,
            courage = 8,
            wisdom = 7,
            humor = 4,
            traits = listOf("gruff", "skilled", "reliable")
        ),
        homeLocationId = "the_quailsmith",
        occupation = NPCOccupation.CRAFTSMAN,
        factionId = "buttonburgh_craftsmen",
        defaultDialogueTreeId = "craftsman_greeting",
        questGiverIds = listOf("craft_first_weapon", "side_spider_silk"),
        merchantInventory = listOf("twig_spear", "acorn_helmet", "leaf_shield"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 7, "the_quailsmith", "sleeping"),
                ScheduleEntry(7, 12, "the_quailsmith", "forging"),
                ScheduleEntry(12, 13, "buttonburgh_village", "lunch"),
                ScheduleEntry(13, 19, "the_quailsmith", "forging"),
                ScheduleEntry(19, 24, "the_quailsmith", "resting")
            )
        )
    )
    
    /**
     * Pip - Young quail child, quest giver for side quests
     */
    val pipYoungQuail = NPC(
        id = "pip_young_quail",
        name = "Pip",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 10,
            courage = 3,
            wisdom = 2,
            humor = 9,
            traits = listOf("playful", "curious", "innocent")
        ),
        homeLocationId = "buttonburgh_village",
        occupation = NPCOccupation.CHILD,
        factionId = "buttonburgh_citizens",
        defaultDialogueTreeId = "young_quail_lost_feather",
        questGiverIds = listOf("side_lost_feather"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 8, "buttonburgh_village", "sleeping"),
                ScheduleEntry(8, 12, "meadow_path", "playing"),
                ScheduleEntry(12, 13, "buttonburgh_village", "eating"),
                ScheduleEntry(13, 17, "meadow_path", "exploring"),
                ScheduleEntry(17, 20, "buttonburgh_village", "playing"),
                ScheduleEntry(20, 24, "buttonburgh_village", "sleeping")
            )
        )
    )
    
    /**
     * Mabel Quail - Innkeeper at The Gilded Seed Inn
     */
    val mabelInnkeeper = NPC(
        id = "mabel_innkeeper",
        name = "Mabel Quail",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 9,
            courage = 6,
            wisdom = 7,
            humor = 8,
            traits = listOf("hospitable", "chatty", "warm")
        ),
        homeLocationId = "the_gilded_seed_inn",
        occupation = NPCOccupation.INNKEEPER,
        factionId = "buttonburgh_merchants",
        defaultDialogueTreeId = "innkeeper_greeting",
        merchantInventory = listOf("seed_bread", "berry_juice", "rest_voucher"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "the_gilded_seed_inn", "sleeping"),
                ScheduleEntry(6, 11, "the_gilded_seed_inn", "preparing_breakfast"),
                ScheduleEntry(11, 14, "the_gilded_seed_inn", "serving_lunch"),
                ScheduleEntry(14, 17, "the_gilded_seed_inn", "cleaning"),
                ScheduleEntry(17, 23, "the_gilded_seed_inn", "serving_dinner"),
                ScheduleEntry(23, 24, "the_gilded_seed_inn", "closing")
            )
        )
    )
    
    /**
     * Old Quill - Scholar at Old Quill's Study
     */
    val oldQuill = NPC(
        id = "old_quill",
        name = "Old Quill",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 6,
            courage = 5,
            wisdom = 10,
            humor = 7,
            traits = listOf("scholarly", "absent-minded", "knowledgeable")
        ),
        homeLocationId = "old_quills_study",
        occupation = NPCOccupation.SCHOLAR,
        factionId = "buttonburgh_scholars",
        defaultDialogueTreeId = "scholar_greeting",
        questGiverIds = listOf("side_ancient_texts"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 9, "old_quills_study", "sleeping"),
                ScheduleEntry(9, 12, "old_quills_study", "researching"),
                ScheduleEntry(12, 13, "the_gilded_seed_inn", "lunch"),
                ScheduleEntry(13, 22, "old_quills_study", "reading"),
                ScheduleEntry(22, 24, "old_quills_study", "writing")
            )
        )
    )
    
    /**
     * Captain Bravewing - Warrior quail, combat trainer
     */
    val captainBravewing = NPC(
        id = "captain_bravewing",
        name = "Captain Bravewing",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 7,
            courage = 10,
            wisdom = 6,
            humor = 5,
            traits = listOf("brave", "disciplined", "honorable")
        ),
        homeLocationId = "buttonburgh_village",
        occupation = NPCOccupation.WARRIOR,
        factionId = "buttonburgh_guard",
        defaultDialogueTreeId = "warrior_greeting",
        questGiverIds = listOf("side_combat_training"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 5, "buttonburgh_village", "sleeping"),
                ScheduleEntry(5, 8, "meadow_path", "training"),
                ScheduleEntry(8, 12, "buttonburgh_village", "patrolling"),
                ScheduleEntry(12, 13, "the_gilded_seed_inn", "lunch"),
                ScheduleEntry(13, 18, "buttonburgh_village", "guard_duty"),
                ScheduleEntry(18, 20, "meadow_path", "evening_patrol"),
                ScheduleEntry(20, 24, "buttonburgh_village", "resting")
            )
        )
    )
    
    /**
     * Farmer Cluck - Farmer providing resources
     */
    val farmerCluck = NPC(
        id = "farmer_cluck",
        name = "Farmer Cluck",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 8,
            courage = 5,
            wisdom = 6,
            humor = 7,
            traits = listOf("hardworking", "generous", "simple")
        ),
        homeLocationId = "buttonburgh_village",
        occupation = NPCOccupation.FARMER,
        factionId = "buttonburgh_citizens",
        defaultDialogueTreeId = "farmer_greeting",
        questGiverIds = listOf("side_beetle_problem"),
        merchantInventory = listOf("seeds", "berries", "grain"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 5, "buttonburgh_village", "sleeping"),
                ScheduleEntry(5, 12, "meadow_path", "farming"),
                ScheduleEntry(12, 13, "buttonburgh_village", "lunch"),
                ScheduleEntry(13, 18, "meadow_path", "harvesting"),
                ScheduleEntry(18, 21, "buttonburgh_village", "selling_goods"),
                ScheduleEntry(21, 24, "buttonburgh_village", "resting")
            )
        )
    )
    
    /**
     * Scout Featherfoot - Explorer quail, dungeon guide
     */
    val scoutFeatherfoot = NPC(
        id = "scout_featherfoot",
        name = "Scout Featherfoot",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 7,
            courage = 9,
            wisdom = 8,
            humor = 6,
            traits = listOf("adventurous", "observant", "cautious")
        ),
        homeLocationId = "buttonburgh_village",
        occupation = NPCOccupation.EXPLORER,
        factionId = "buttonburgh_explorers",
        defaultDialogueTreeId = "explorer_greeting",
        questGiverIds = listOf("side_map_the_swamp"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "buttonburgh_village", "sleeping"),
                ScheduleEntry(6, 10, "buttonburgh_village", "preparing"),
                ScheduleEntry(10, 16, "dungeon_roots_below", "exploring"),
                ScheduleEntry(16, 19, "buttonburgh_village", "reporting"),
                ScheduleEntry(19, 24, "buttonburgh_village", "resting")
            )
        )
    )
    
    /**
     * Guard Peckins - Village guard
     */
    val guardPeckins = NPC(
        id = "guard_peckins",
        name = "Guard Peckins",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 6,
            courage = 8,
            wisdom = 5,
            humor = 4,
            traits = listOf("vigilant", "stern", "dutiful")
        ),
        homeLocationId = "buttonburgh_village",
        occupation = NPCOccupation.GUARD,
        factionId = "buttonburgh_guard",
        defaultDialogueTreeId = "guard_greeting",
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 8, "buttonburgh_village", "sleeping"),
                ScheduleEntry(8, 16, "buttonburgh_village", "guard_duty"),
                ScheduleEntry(16, 17, "the_gilded_seed_inn", "dinner"),
                ScheduleEntry(17, 22, "buttonburgh_village", "night_watch"),
                ScheduleEntry(22, 24, "buttonburgh_village", "resting")
            )
        )
    )
    
    /**
     * Merchant Seedsworth - General merchant
     */
    val merchantSeedsworth = NPC(
        id = "merchant_seedsworth",
        name = "Merchant Seedsworth",
        species = NPCSpecies.MOUSE,
        personality = NPCPersonality(
            friendliness = 9,
            courage = 4,
            wisdom = 7,
            humor = 8,
            traits = listOf("shrewd", "friendly", "talkative")
        ),
        homeLocationId = "buttonburgh_village",
        occupation = NPCOccupation.MERCHANT,
        factionId = "buttonburgh_merchants",
        defaultDialogueTreeId = "merchant_greeting",
        merchantInventory = listOf("health_potion", "stamina_potion", "torch"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 7, "buttonburgh_village", "sleeping"),
                ScheduleEntry(7, 9, "the_gilded_seed_inn", "breakfast"),
                ScheduleEntry(9, 18, "buttonburgh_village", "selling"),
                ScheduleEntry(18, 19, "the_gilded_seed_inn", "dinner"),
                ScheduleEntry(19, 24, "buttonburgh_village", "resting")
            )
        )
    )
    
    // ===== ADDITIONAL NPCs =====
    
    /**
     * Willow - Mysterious firefly NPC
     */
    val willowFirefly = NPC(
        id = "willow_firefly",
        name = "Willow",
        species = NPCSpecies.FIREFLY,
        personality = NPCPersonality(
            friendliness = 7,
            courage = 6,
            wisdom = 9,
            humor = 5,
            traits = listOf("mysterious", "ethereal", "wise")
        ),
        homeLocationId = "firefly_glade",
        occupation = NPCOccupation.SCHOLAR,
        factionId = "firefly_circle",
        defaultDialogueTreeId = "firefly_greeting",
        questGiverIds = listOf("side_firefly_ritual"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 18, "firefly_glade", "resting"),
                ScheduleEntry(18, 24, "firefly_glade", "glowing")
            )
        )
    )
    
    /**
     * Burrow - Friendly beetle merchant
     */
    val burrowBeetle = NPC(
        id = "burrow_beetle",
        name = "Burrow",
        species = NPCSpecies.BEETLE,
        personality = NPCPersonality(
            friendliness = 8,
            courage = 5,
            wisdom = 6,
            humor = 7,
            traits = listOf("cheerful", "helpful", "quirky")
        ),
        homeLocationId = "buttonburgh_village",
        occupation = NPCOccupation.MERCHANT,
        factionId = "beetle_traders",
        defaultDialogueTreeId = "beetle_greeting",
        merchantInventory = listOf("beetle_shell_armor", "mandible_dagger"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 8, "buttonburgh_village", "sleeping"),
                ScheduleEntry(8, 16, "buttonburgh_village", "trading"),
                ScheduleEntry(16, 20, "meadow_path", "gathering"),
                ScheduleEntry(20, 24, "buttonburgh_village", "resting")
            )
        )
    )
    
    /**
     * Healer Downy - Village healer
     */
    val healerDowny = NPC(
        id = "healer_downy",
        name = "Healer Downy",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 10,
            courage = 6,
            wisdom = 8,
            humor = 6,
            traits = listOf("compassionate", "gentle", "skilled")
        ),
        homeLocationId = "buttonburgh_village",
        occupation = NPCOccupation.SCHOLAR,
        factionId = "buttonburgh_healers",
        defaultDialogueTreeId = "healer_greeting",
        questGiverIds = listOf("side_healing_herbs"),
        merchantInventory = listOf("health_potion", "antidote", "bandages"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 7, "buttonburgh_village", "sleeping"),
                ScheduleEntry(7, 12, "meadow_path", "gathering_herbs"),
                ScheduleEntry(12, 13, "buttonburgh_village", "lunch"),
                ScheduleEntry(13, 20, "buttonburgh_village", "healing"),
                ScheduleEntry(20, 24, "buttonburgh_village", "resting")
            )
        )
    )
    
    /**
     * Chirp & Cheep - Twin quail children (playmates)
     */
    val chirpTwin = NPC(
        id = "chirp_twin",
        name = "Chirp",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 10,
            courage = 7,
            wisdom = 3,
            humor = 10,
            traits = listOf("mischievous", "energetic", "loyal")
        ),
        homeLocationId = "buttonburgh_village",
        occupation = NPCOccupation.CHILD,
        factionId = "buttonburgh_citizens",
        defaultDialogueTreeId = "twin_greeting",
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 8, "buttonburgh_village", "sleeping"),
                ScheduleEntry(8, 17, "meadow_path", "playing"),
                ScheduleEntry(17, 20, "buttonburgh_village", "eating"),
                ScheduleEntry(20, 24, "buttonburgh_village", "sleeping")
            )
        )
    )
    
    val cheepTwin = NPC(
        id = "cheep_twin",
        name = "Cheep",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 10,
            courage = 6,
            wisdom = 3,
            humor = 10,
            traits = listOf("playful", "curious", "loyal")
        ),
        homeLocationId = "buttonburgh_village",
        occupation = NPCOccupation.CHILD,
        factionId = "buttonburgh_citizens",
        defaultDialogueTreeId = "twin_greeting",
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 8, "buttonburgh_village", "sleeping"),
                ScheduleEntry(8, 17, "meadow_path", "playing"),
                ScheduleEntry(17, 20, "buttonburgh_village", "eating"),
                ScheduleEntry(20, 24, "buttonburgh_village", "sleeping")
            )
        )
    )
    
    /**
     * Sparrow Scout - Enemy faction scout (hostile)
     */
    val sparrowScout = NPC(
        id = "sparrow_scout",
        name = "Sparrow Scout",
        species = NPCSpecies.SPARROW,
        personality = NPCPersonality(
            friendliness = 2,
            courage = 8,
            wisdom = 6,
            humor = 3,
            traits = listOf("aggressive", "territorial", "watchful")
        ),
        homeLocationId = "forest_edge",
        occupation = NPCOccupation.WARRIOR,
        factionId = "sparrow_raiders",
        defaultDialogueTreeId = "sparrow_warning",
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "forest_edge", "sleeping"),
                ScheduleEntry(6, 18, "forest_edge", "patrolling"),
                ScheduleEntry(18, 24, "forest_edge", "guarding")
            )
        )
    )
    
    // ===== BATCH 1: BUTTONBURGH HUB NPCS (4) =====
    
    /**
     * Flint Ironbeak - Blacksmith Apprentice
     */
    val flintIronbeak = NPC(
        id = "flint_ironbeak",
        name = "Flint Ironbeak",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 7,
            courage = 8,
            wisdom = 5,
            humor = 6,
            traits = listOf("hardworking", "eager", "strong")
        ),
        homeLocationId = "buttonburgh_quailsmith",
        occupation = NPCOccupation.CRAFTSMAN,
        factionId = "buttonburgh_council",
        defaultDialogueTreeId = "flint_greeting",
        questGiverIds = listOf("craft_forge_your_path"),
        merchantInventory = listOf(
            "copper_ore", "iron_ore", "coal", "leather_scraps", "twig_spear"
        ),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 7, "buttonburgh_village", "sleeping"),
                ScheduleEntry(7, 18, "buttonburgh_quailsmith", "crafting"),
                ScheduleEntry(18, 22, "buttonburgh_gilded_seed", "drinking"),
                ScheduleEntry(22, 24, "buttonburgh_village", "sleeping")
            )
        )
    )
    
    /**
     * Clover Softdown - Herbalist and Healer Assistant
     */
    val cloverSoftdown = NPC(
        id = "clover_softdown",
        name = "Clover Softdown",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 10,
            courage = 4,
            wisdom = 7,
            humor = 8,
            traits = listOf("gentle", "nurturing", "knowledgeable")
        ),
        homeLocationId = "buttonburgh_village",
        occupation = NPCOccupation.SCHOLAR,
        factionId = "buttonburgh_council",
        defaultDialogueTreeId = "clover_greeting",
        questGiverIds = listOf("side_herb_collection", "side_salve_recipe"),
        merchantInventory = listOf(
            "health_potion", "stamina_potion", "antidote", "herb_bundle", "healing_salve"
        ),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "buttonburgh_village", "sleeping"),
                ScheduleEntry(6, 12, "grassland_meadow", "foraging"),
                ScheduleEntry(12, 18, "buttonburgh_village", "brewing"),
                ScheduleEntry(18, 24, "buttonburgh_village", "sleeping")
            )
        )
    )
    
    /**
     * Scroll Dustfeather - Historian and Lorekeeper
     */
    val scrollDustfeather = NPC(
        id = "scroll_dustfeather",
        name = "Scroll Dustfeather",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 6,
            courage = 3,
            wisdom = 10,
            humor = 4,
            traits = listOf("scholarly", "obsessive", "forgetful")
        ),
        homeLocationId = "buttonburgh_old_quill_study",
        occupation = NPCOccupation.SCHOLAR,
        factionId = "buttonburgh_council",
        defaultDialogueTreeId = "scroll_greeting",
        questGiverIds = listOf("hidden_lore_keeper"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 8, "buttonburgh_old_quill_study", "sleeping"),
                ScheduleEntry(8, 20, "buttonburgh_old_quill_study", "researching"),
                ScheduleEntry(20, 24, "buttonburgh_old_quill_study", "reading")
            )
        )
    )
    
    /**
     * Bramble Swiftpeck - Stable Keeper and Bird Trainer
     */
    val brambleSwiftpeck = NPC(
        id = "bramble_swiftpeck",
        name = "Bramble Swiftpeck",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 8,
            courage = 6,
            wisdom = 6,
            humor = 7,
            traits = listOf("patient", "kind", "animal_lover")
        ),
        homeLocationId = "buttonburgh_hen_pen",
        occupation = NPCOccupation.FARMER,
        factionId = "buttonburgh_council",
        defaultDialogueTreeId = "bramble_greeting",
        questGiverIds = listOf("side_lost_chick", "hidden_family_reunion"),
        merchantInventory = listOf(
            "bird_seed", "feather", "eggshell_fragment", "straw", "nest_material"
        ),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 5, "buttonburgh_village", "sleeping"),
                ScheduleEntry(5, 12, "buttonburgh_hen_pen", "feeding"),
                ScheduleEntry(12, 18, "buttonburgh_hen_pen", "training"),
                ScheduleEntry(18, 24, "buttonburgh_village", "sleeping")
            )
        )
    )
    
    // ===== BATCH 2: WORLD NPCS - GRASSLAND & FOREST (8) =====
    
    /**
     * Thistle Forager - Grassland Herbalist
     */
    val thistleForager = NPC(
        id = "thistle_forager",
        name = "Thistle",
        species = NPCSpecies.MOUSE,
        personality = NPCPersonality(
            friendliness = 7,
            courage = 5,
            wisdom = 8,
            humor = 6,
            traits = listOf("curious", "resourceful", "gentle")
        ),
        homeLocationId = "grassland_meadow",
        occupation = NPCOccupation.FARMER,
        factionId = "independent",
        defaultDialogueTreeId = "thistle_greeting",
        questGiverIds = listOf("side_herb_collection"),
        merchantInventory = listOf(
            "herb_bundle", "wildflower", "grass_seed", "clover", "dandelion"
        ),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 7, "grassland_meadow", "sleeping"),
                ScheduleEntry(7, 16, "grassland_meadow", "foraging"),
                ScheduleEntry(16, 24, "grassland_meadow", "resting")
            )
        )
    )
    
    /**
     * Rusty Windwhisper - Wandering Merchant
     */
    val rustyWindwhisper = NPC(
        id = "rusty_windwhisper",
        name = "Rusty Windwhisper",
        species = NPCSpecies.MOUSE,
        personality = NPCPersonality(
            friendliness = 9,
            courage = 6,
            wisdom = 7,
            humor = 10,
            traits = listOf("jovial", "shrewd", "chatty")
        ),
        homeLocationId = "grassland_crossroads",
        occupation = NPCOccupation.MERCHANT,
        factionId = "merchant_guild",
        defaultDialogueTreeId = "rusty_greeting",
        merchantInventory = listOf(
            "health_potion", "stamina_potion", "lockpick", "rope", "torch",
            "trail_rations", "bandage", "antidote"
        ),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 24, "grassland_crossroads", "trading")
            )
        )
    )
    
    /**
     * Pebble Deepdigger - Mole Farmer
     */
    val pebbleDeepdigger = NPC(
        id = "pebble_deepdigger",
        name = "Pebble Deepdigger",
        species = NPCSpecies.MOLE,
        personality = NPCPersonality(
            friendliness = 6,
            courage = 7,
            wisdom = 5,
            humor = 5,
            traits = listOf("hardworking", "practical", "stubborn")
        ),
        homeLocationId = "grassland_farm",
        occupation = NPCOccupation.FARMER,
        factionId = "independent",
        defaultDialogueTreeId = "pebble_greeting",
        questGiverIds = listOf("side_beetle_problem"),
        merchantInventory = listOf(
            "carrot", "potato", "wheat", "seeds", "compost"
        ),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "grassland_farm", "sleeping"),
                ScheduleEntry(6, 18, "grassland_farm", "farming"),
                ScheduleEntry(18, 24, "grassland_farm", "resting")
            )
        )
    )
    
    /**
     * Willow Moonwing - Forest Firefly Mystic
     */
    val willowMoonwing = NPC(
        id = "willow_moonwing",
        name = "Willow Moonwing",
        species = NPCSpecies.FIREFLY,
        personality = NPCPersonality(
            friendliness = 8,
            courage = 5,
            wisdom = 10,
            humor = 7,
            traits = listOf("mystical", "enigmatic", "wise")
        ),
        homeLocationId = "forest_clearing",
        occupation = NPCOccupation.SCHOLAR,
        factionId = "firefly_circle",
        defaultDialogueTreeId = "willow_moonwing_greeting",
        questGiverIds = listOf("hidden_firefly_lantern"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "forest_clearing", "meditating"),
                ScheduleEntry(6, 18, "forest_clearing", "sleeping"),
                ScheduleEntry(18, 24, "forest_clearing", "illuminating")
            )
        )
    )
    
    /**
     * Oak Strongbranch - Forest Guardian
     */
    val oakStrongbranch = NPC(
        id = "oak_strongbranch",
        name = "Oak Strongbranch",
        species = NPCSpecies.BEETLE,
        personality = NPCPersonality(
            friendliness = 5,
            courage = 10,
            wisdom = 6,
            humor = 3,
            traits = listOf("stoic", "protective", "honorable")
        ),
        homeLocationId = "forest_heart",
        occupation = NPCOccupation.WARRIOR,
        factionId = "forest_guardians",
        defaultDialogueTreeId = "oak_greeting",
        questGiverIds = listOf("main_forest_whispers", "side_mantis_menace"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "forest_heart", "sleeping"),
                ScheduleEntry(6, 18, "forest_heart", "patrolling"),
                ScheduleEntry(18, 24, "forest_heart", "guarding")
            )
        )
    )
    
    /**
     * Maple Leafrunner - Forest Scout
     */
    val mapleLeafrunner = NPC(
        id = "maple_leafrunner",
        name = "Maple Leafrunner",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 7,
            courage = 8,
            wisdom = 7,
            humor = 6,
            traits = listOf("agile", "observant", "quick")
        ),
        homeLocationId = "forest_edge",
        occupation = NPCOccupation.EXPLORER,
        factionId = "buttonburgh_council",
        defaultDialogueTreeId = "maple_greeting",
        questGiverIds = listOf("explore_forest_cartographer"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "forest_edge", "sleeping"),
                ScheduleEntry(6, 18, "forest_heart", "scouting"),
                ScheduleEntry(18, 24, "forest_edge", "resting")
            )
        )
    )
    
    /**
     * Hunter Quickshot - Forest Hunter
     */
    val hunterQuickshot = NPC(
        id = "hunter_quickshot",
        name = "Hunter Quickshot",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 6,
            courage = 9,
            wisdom = 6,
            humor = 5,
            traits = listOf("focused", "skilled", "independent")
        ),
        homeLocationId = "forest_camp",
        occupation = NPCOccupation.WARRIOR,
        factionId = "independent",
        defaultDialogueTreeId = "hunter_greeting",
        questGiverIds = listOf("combat_hawk_hunt", "combat_spider_slayer"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 5, "forest_camp", "sleeping"),
                ScheduleEntry(5, 19, "forest_heart", "hunting"),
                ScheduleEntry(19, 24, "forest_camp", "resting")
            )
        )
    )
    
    /**
     * Nettle Webweaver - Forest Artisan
     */
    val nettleWebweaver = NPC(
        id = "nettle_webweaver",
        name = "Nettle Webweaver",
        species = NPCSpecies.MOUSE,
        personality = NPCPersonality(
            friendliness = 8,
            courage = 4,
            wisdom = 8,
            humor = 7,
            traits = listOf("artistic", "patient", "creative")
        ),
        homeLocationId = "forest_grove",
        occupation = NPCOccupation.CRAFTSMAN,
        factionId = "independent",
        defaultDialogueTreeId = "nettle_greeting",
        merchantInventory = listOf(
            "silk_thread", "web_rope", "leaf_cloak", "bark_armor", "vine_whip"
        ),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 7, "forest_grove", "sleeping"),
                ScheduleEntry(7, 19, "forest_grove", "weaving"),
                ScheduleEntry(19, 24, "forest_grove", "resting")
            )
        )
    )
    
    // ===== BATCH 2: WORLD NPCS - SWAMP (4) =====
    
    /**
     * Marsh Murkwater - Swamp Hermit
     */
    val marshMurkwater = NPC(
        id = "marsh_murkwater",
        name = "Marsh Murkwater",
        species = NPCSpecies.MOLE,
        personality = NPCPersonality(
            friendliness = 3,
            courage = 6,
            wisdom = 9,
            humor = 4,
            traits = listOf("reclusive", "knowledgeable", "gruff")
        ),
        homeLocationId = "swamp_hut",
        occupation = NPCOccupation.SCHOLAR,
        factionId = "independent",
        defaultDialogueTreeId = "marsh_greeting",
        questGiverIds = listOf("main_swamp_expedition", "side_swamp_research"),
        merchantInventory = listOf(
            "swamp_moss", "murky_water", "bog_root", "leech", "poison_extract"
        ),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 9, "swamp_hut", "sleeping"),
                ScheduleEntry(9, 18, "swamp_hut", "researching"),
                ScheduleEntry(18, 24, "swamp_hut", "brewing")
            )
        )
    )
    
    /**
     * Sludge Croaksong - Swamp Guide
     */
    val sludgeCroaksong = NPC(
        id = "sludge_croaksong",
        name = "Sludge Croaksong",
        species = NPCSpecies.MOUSE,
        personality = NPCPersonality(
            friendliness = 6,
            courage = 7,
            wisdom = 7,
            humor = 8,
            traits = listOf("resilient", "helpful", "cautious")
        ),
        homeLocationId = "swamp_edge",
        occupation = NPCOccupation.EXPLORER,
        factionId = "independent",
        defaultDialogueTreeId = "sludge_greeting",
        questGiverIds = listOf("explore_world_wanderer"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 7, "swamp_edge", "sleeping"),
                ScheduleEntry(7, 18, "swamp_depths", "guiding"),
                ScheduleEntry(18, 24, "swamp_edge", "resting")
            )
        )
    )
    
    /**
     * Venom Siltstalker - Swamp Alchemist
     */
    val venomSiltstalker = NPC(
        id = "venom_siltstalker",
        name = "Venom Siltstalker",
        species = NPCSpecies.BEETLE,
        personality = NPCPersonality(
            friendliness = 5,
            courage = 6,
            wisdom = 9,
            humor = 5,
            traits = listOf("sinister", "brilliant", "calculating")
        ),
        homeLocationId = "swamp_depths",
        occupation = NPCOccupation.SCHOLAR,
        factionId = "independent",
        defaultDialogueTreeId = "venom_greeting",
        questGiverIds = listOf("side_poison_research"),
        merchantInventory = listOf(
            "antidote", "poison_vial", "venom_extract", "toxic_dust", "cure_potion"
        ),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 8, "swamp_depths", "sleeping"),
                ScheduleEntry(8, 20, "swamp_depths", "brewing"),
                ScheduleEntry(20, 24, "swamp_depths", "experimenting")
            )
        )
    )
    
    /**
     * Peat Bogsinger - Swamp Bard
     */
    val peatBogsinger = NPC(
        id = "peat_bogsinger",
        name = "Peat Bogsinger",
        species = NPCSpecies.FIREFLY,
        personality = NPCPersonality(
            friendliness = 9,
            courage = 4,
            wisdom = 6,
            humor = 10,
            traits = listOf("cheerful", "whimsical", "melodic")
        ),
        homeLocationId = "swamp_edge",
        occupation = NPCOccupation.SCHOLAR,
        factionId = "firefly_circle",
        defaultDialogueTreeId = "peat_greeting",
        questGiverIds = listOf("side_lost_song"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "swamp_edge", "sleeping"),
                ScheduleEntry(6, 12, "swamp_edge", "singing"),
                ScheduleEntry(12, 18, "swamp_depths", "performing"),
                ScheduleEntry(18, 24, "swamp_edge", "composing")
            )
        )
    )
    
    // ===== BATCH 2: WORLD NPCS - MOUNTAIN (5) =====
    
    /**
     * Stone Cliffclimber - Mountain Guide
     */
    val stoneCliffclimber = NPC(
        id = "stone_cliffclimber",
        name = "Stone Cliffclimber",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 7,
            courage = 10,
            wisdom = 7,
            humor = 6,
            traits = listOf("brave", "strong", "dependable")
        ),
        homeLocationId = "mountain_base",
        occupation = NPCOccupation.EXPLORER,
        factionId = "independent",
        defaultDialogueTreeId = "stone_greeting",
        questGiverIds = listOf("main_mountain_ascent"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 5, "mountain_base", "sleeping"),
                ScheduleEntry(5, 18, "mountain_peak", "climbing"),
                ScheduleEntry(18, 24, "mountain_base", "resting")
            )
        )
    )
    
    /**
     * Granite Pickwielder - Mountain Miner
     */
    val granitePickwielder = NPC(
        id = "granite_pickwielder",
        name = "Granite Pickwielder",
        species = NPCSpecies.MOLE,
        personality = NPCPersonality(
            friendliness = 6,
            courage = 8,
            wisdom = 6,
            humor = 5,
            traits = listOf("industrious", "tough", "loyal")
        ),
        homeLocationId = "mountain_mine",
        occupation = NPCOccupation.CRAFTSMAN,
        factionId = "miner_guild",
        defaultDialogueTreeId = "granite_greeting",
        questGiverIds = listOf("side_ore_collection"),
        merchantInventory = listOf(
            "copper_ore", "iron_ore", "silver_ore", "gold_ore", "gemstone",
            "coal", "flint", "pickaxe"
        ),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "mountain_mine", "sleeping"),
                ScheduleEntry(6, 18, "mountain_mine", "mining"),
                ScheduleEntry(18, 24, "mountain_mine", "resting")
            )
        )
    )
    
    /**
     * Echo Windwhisper - Mountain Oracle
     */
    val echoWindwhisper = NPC(
        id = "echo_windwhisper",
        name = "Echo Windwhisper",
        species = NPCSpecies.SPARROW,
        personality = NPCPersonality(
            friendliness = 7,
            courage = 6,
            wisdom = 10,
            humor = 5,
            traits = listOf("prophetic", "calm", "insightful")
        ),
        homeLocationId = "mountain_peak",
        occupation = NPCOccupation.SCHOLAR,
        factionId = "independent",
        defaultDialogueTreeId = "echo_greeting",
        questGiverIds = listOf("hidden_prophecy"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 24, "mountain_peak", "meditating")
            )
        )
    )
    
    /**
     * Crag Stonefist - Mountain Warrior
     */
    val cragStonefist = NPC(
        id = "crag_stonefist",
        name = "Crag Stonefist",
        species = NPCSpecies.BEETLE,
        personality = NPCPersonality(
            friendliness = 5,
            courage = 10,
            wisdom = 5,
            humor = 4,
            traits = listOf("fierce", "determined", "unbreakable")
        ),
        homeLocationId = "mountain_fortress",
        occupation = NPCOccupation.WARRIOR,
        factionId = "mountain_guard",
        defaultDialogueTreeId = "crag_greeting",
        questGiverIds = listOf("combat_beetle_brawl"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "mountain_fortress", "sleeping"),
                ScheduleEntry(6, 18, "mountain_fortress", "training"),
                ScheduleEntry(18, 24, "mountain_fortress", "guarding")
            )
        )
    )
    
    /**
     * Slate Tunnelborer - Cave Dweller
     */
    val slateTunnelborer = NPC(
        id = "slate_tunnelborer",
        name = "Slate Tunnelborer",
        species = NPCSpecies.MOLE,
        personality = NPCPersonality(
            friendliness = 4,
            courage = 7,
            wisdom = 8,
            humor = 3,
            traits = listOf("solitary", "meticulous", "paranoid")
        ),
        homeLocationId = "mountain_cave",
        occupation = NPCOccupation.EXPLORER,
        factionId = "independent",
        defaultDialogueTreeId = "slate_greeting",
        questGiverIds = listOf("explore_cave_spelunker"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 24, "mountain_cave", "digging")
            )
        )
    )
    
    // ===== BATCH 2: WORLD NPCS - DESERT (5) =====
    
    /**
     * Dune Sandstrider - Desert Nomad
     */
    val duneSandstrider = NPC(
        id = "dune_sandstrider",
        name = "Dune Sandstrider",
        species = NPCSpecies.MOUSE,
        personality = NPCPersonality(
            friendliness = 8,
            courage = 7,
            wisdom = 7,
            humor = 6,
            traits = listOf("wanderer", "free-spirited", "adaptable")
        ),
        homeLocationId = "desert_oasis",
        occupation = NPCOccupation.EXPLORER,
        factionId = "desert_tribes",
        defaultDialogueTreeId = "dune_greeting",
        questGiverIds = listOf("main_desert_sands", "side_tarantula_terror"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "desert_oasis", "sleeping"),
                ScheduleEntry(6, 12, "desert_dunes", "traveling"),
                ScheduleEntry(12, 18, "desert_oasis", "resting"),
                ScheduleEntry(18, 24, "desert_ruins", "exploring")
            )
        )
    )
    
    /**
     * Mirage Sunseeker - Desert Trader
     */
    val mirageSunseeker = NPC(
        id = "mirage_sunseeker",
        name = "Mirage Sunseeker",
        species = NPCSpecies.BEETLE,
        personality = NPCPersonality(
            friendliness = 9,
            courage = 5,
            wisdom = 8,
            humor = 9,
            traits = listOf("shrewd", "hospitable", "cunning")
        ),
        homeLocationId = "desert_bazaar",
        occupation = NPCOccupation.MERCHANT,
        factionId = "merchant_guild",
        defaultDialogueTreeId = "mirage_greeting",
        merchantInventory = listOf(
            "desert_cloak", "sand_vial", "cactus_water", "sun_crystal", "desert_map",
            "heat_salve", "sun_amulet", "sand_goggles"
        ),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "desert_bazaar", "sleeping"),
                ScheduleEntry(6, 20, "desert_bazaar", "trading"),
                ScheduleEntry(20, 24, "desert_bazaar", "closing_shop")
            )
        )
    )
    
    /**
     * Scorpio Stingweaver - Desert Warrior
     */
    val scorpioStingweaver = NPC(
        id = "scorpio_stingweaver",
        name = "Scorpio Stingweaver",
        species = NPCSpecies.BEETLE,
        personality = NPCPersonality(
            friendliness = 4,
            courage = 10,
            wisdom = 6,
            humor = 3,
            traits = listOf("deadly", "honorable", "silent")
        ),
        homeLocationId = "desert_stronghold",
        occupation = NPCOccupation.WARRIOR,
        factionId = "desert_guard",
        defaultDialogueTreeId = "scorpio_greeting",
        questGiverIds = listOf("combat_apex_predator"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "desert_stronghold", "sleeping"),
                ScheduleEntry(6, 18, "desert_stronghold", "training"),
                ScheduleEntry(18, 24, "desert_ruins", "patrolling")
            )
        )
    )
    
    /**
     * Oasis Lifespring - Oasis Keeper
     */
    val oasisLifespring = NPC(
        id = "oasis_lifespring",
        name = "Oasis Lifespring",
        species = NPCSpecies.FIREFLY,
        personality = NPCPersonality(
            friendliness = 10,
            courage = 5,
            wisdom = 9,
            humor = 7,
            traits = listOf("nurturing", "peaceful", "radiant")
        ),
        homeLocationId = "desert_oasis",
        occupation = NPCOccupation.SCHOLAR,
        factionId = "firefly_circle",
        defaultDialogueTreeId = "oasis_greeting",
        questGiverIds = listOf("side_oasis_restoration"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "desert_oasis", "sleeping"),
                ScheduleEntry(6, 18, "desert_oasis", "tending"),
                ScheduleEntry(18, 24, "desert_oasis", "glowing")
            )
        )
    )
    
    /**
     * Ancient Sandsage - Desert Oracle
     */
    val ancientSandsage = NPC(
        id = "ancient_sandsage",
        name = "Ancient Sandsage",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 6,
            courage = 7,
            wisdom = 10,
            humor = 5,
            traits = listOf("ancient", "cryptic", "wise")
        ),
        homeLocationId = "desert_ruins",
        occupation = NPCOccupation.SCHOLAR,
        factionId = "independent",
        defaultDialogueTreeId = "sandsage_greeting",
        questGiverIds = listOf("side_phoenix_feather", "hidden_desert_secret"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 24, "desert_ruins", "meditating")
            )
        )
    )
    
    // ===== BATCH 2: WORLD NPCS - COASTAL (3) =====
    
    /**
     * Wave Tidecaller - Coastal Fisher
     */
    val waveTidecaller = NPC(
        id = "wave_tidecaller",
        name = "Wave Tidecaller",
        species = NPCSpecies.MOUSE,
        personality = NPCPersonality(
            friendliness = 8,
            courage = 6,
            wisdom = 6,
            humor = 8,
            traits = listOf("easygoing", "skilled", "superstitious")
        ),
        homeLocationId = "coastal_village",
        occupation = NPCOccupation.FARMER,
        factionId = "independent",
        defaultDialogueTreeId = "wave_greeting",
        questGiverIds = listOf("main_coastal_voyage"),
        merchantInventory = listOf(
            "fish", "seaweed", "shell", "coral", "salt", "fishing_rod"
        ),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 5, "coastal_village", "sleeping"),
                ScheduleEntry(5, 14, "coastal_shore", "fishing"),
                ScheduleEntry(14, 18, "coastal_village", "selling"),
                ScheduleEntry(18, 24, "coastal_village", "resting")
            )
        )
    )
    
    /**
     * Marina Shellseeker - Marine Researcher
     */
    val marinaShellseeker = NPC(
        id = "marina_shellseeker",
        name = "Marina Shellseeker",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 9,
            courage = 6,
            wisdom = 9,
            humor = 7,
            traits = listOf("inquisitive", "passionate", "meticulous")
        ),
        homeLocationId = "coastal_lab",
        occupation = NPCOccupation.SCHOLAR,
        factionId = "scholar_guild",
        defaultDialogueTreeId = "marina_greeting",
        questGiverIds = listOf("side_jellyfish_research"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 7, "coastal_lab", "sleeping"),
                ScheduleEntry(7, 12, "coastal_shore", "researching"),
                ScheduleEntry(12, 20, "coastal_lab", "studying"),
                ScheduleEntry(20, 24, "coastal_lab", "writing")
            )
        )
    )
    
    /**
     * Beacon Lightkeeper - Lighthouse Guardian
     */
    val beaconLightkeeper = NPC(
        id = "beacon_lightkeeper",
        name = "Beacon Lightkeeper",
        species = NPCSpecies.FIREFLY,
        personality = NPCPersonality(
            friendliness = 7,
            courage = 8,
            wisdom = 8,
            humor = 6,
            traits = listOf("vigilant", "dedicated", "lonely")
        ),
        homeLocationId = "coastal_lighthouse",
        occupation = NPCOccupation.GUARD,
        factionId = "independent",
        defaultDialogueTreeId = "beacon_greeting",
        questGiverIds = listOf("side_lighthouse_repair"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "coastal_lighthouse", "sleeping"),
                ScheduleEntry(6, 18, "coastal_lighthouse", "maintaining"),
                ScheduleEntry(18, 24, "coastal_lighthouse", "watching")
            )
        )
    )
    
    // ===== BATCH 3: COMPANIONS & SPECIAL NPCS (7) =====
    
    /**
     * Quail Chick Companion 1 - Hatched from Mysterious Egg
     */
    val quailChickCompanion1 = NPC(
        id = "quail_chick_companion_1",
        name = "Pip Jr.",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 10,
            courage = 6,
            wisdom = 4,
            humor = 9,
            traits = listOf("playful", "loyal", "curious")
        ),
        homeLocationId = "buttonburgh_hen_pen",
        occupation = NPCOccupation.CHILD,
        factionId = "buttonburgh_council",
        defaultDialogueTreeId = "chick_1_greeting",
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 8, "buttonburgh_hen_pen", "sleeping"),
                ScheduleEntry(8, 18, "buttonburgh_hen_pen", "playing"),
                ScheduleEntry(18, 24, "buttonburgh_hen_pen", "sleeping")
            )
        )
    )
    
    /**
     * Quail Chick Companion 2 - Hatched from Mysterious Egg
     */
    val quailChickCompanion2 = NPC(
        id = "quail_chick_companion_2",
        name = "Feather",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 9,
            courage = 7,
            wisdom = 5,
            humor = 8,
            traits = listOf("brave", "energetic", "adventurous")
        ),
        homeLocationId = "buttonburgh_hen_pen",
        occupation = NPCOccupation.CHILD,
        factionId = "buttonburgh_council",
        defaultDialogueTreeId = "chick_2_greeting",
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 8, "buttonburgh_hen_pen", "sleeping"),
                ScheduleEntry(8, 18, "grassland_meadow", "exploring"),
                ScheduleEntry(18, 24, "buttonburgh_hen_pen", "sleeping")
            )
        )
    )
    
    /**
     * Quail Chick Companion 3 - Hatched from Mysterious Egg
     */
    val quailChickCompanion3 = NPC(
        id = "quail_chick_companion_3",
        name = "Fluff",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 10,
            courage = 4,
            wisdom = 7,
            humor = 7,
            traits = listOf("shy", "observant", "intelligent")
        ),
        homeLocationId = "buttonburgh_hen_pen",
        occupation = NPCOccupation.CHILD,
        factionId = "buttonburgh_council",
        defaultDialogueTreeId = "chick_3_greeting",
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 8, "buttonburgh_hen_pen", "sleeping"),
                ScheduleEntry(8, 18, "buttonburgh_old_quill_study", "learning"),
                ScheduleEntry(18, 24, "buttonburgh_hen_pen", "sleeping")
            )
        )
    )
    
    /**
     * Quail Chick Companion 4 - Hatched from Mysterious Egg
     */
    val quailChickCompanion4 = NPC(
        id = "quail_chick_companion_4",
        name = "Speckle",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 8,
            courage = 8,
            wisdom = 6,
            humor = 9,
            traits = listOf("mischievous", "bold", "clever")
        ),
        homeLocationId = "buttonburgh_hen_pen",
        occupation = NPCOccupation.CHILD,
        factionId = "buttonburgh_council",
        defaultDialogueTreeId = "chick_4_greeting",
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 8, "buttonburgh_hen_pen", "sleeping"),
                ScheduleEntry(8, 18, "buttonburgh_village", "pranking"),
                ScheduleEntry(18, 24, "buttonburgh_hen_pen", "sleeping")
            )
        )
    )
    
    /**
     * Quail Chick Companion 5 - Hatched from Mysterious Egg
     */
    val quailChickCompanion5 = NPC(
        id = "quail_chick_companion_5",
        name = "Dawn",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 9,
            courage = 9,
            wisdom = 8,
            humor = 6,
            traits = listOf("wise", "protective", "mature")
        ),
        homeLocationId = "buttonburgh_hen_pen",
        occupation = NPCOccupation.CHILD,
        factionId = "buttonburgh_council",
        defaultDialogueTreeId = "chick_5_greeting",
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 7, "buttonburgh_hen_pen", "sleeping"),
                ScheduleEntry(7, 18, "buttonburgh_hen_pen", "helping"),
                ScheduleEntry(18, 24, "buttonburgh_hen_pen", "resting")
            )
        )
    )
    
    /**
     * Broody Male Quail - Easter Egg Character (from community idea)
     */
    val broodymaleQuail = NPC(
        id = "broodymale_quail",
        name = "Broodalus the Determined",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 7,
            courage = 10,
            wisdom = 6,
            humor = 8,
            traits = listOf("determined", "nurturing", "stubborn", "fiercely_protective")
        ),
        homeLocationId = "hidden_nest",
        occupation = NPCOccupation.FARMER,
        factionId = "independent",
        defaultDialogueTreeId = "broodymale_greeting",
        questGiverIds = listOf("hidden_broody_male"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 24, "hidden_nest", "brooding")
            )
        )
    )
    
    /**
     * Arena Master - Boss Rush Quest Giver
     */
    val arenaMaster = NPC(
        id = "arena_master",
        name = "Colossus Battlehorn",
        species = NPCSpecies.BEETLE,
        personality = NPCPersonality(
            friendliness = 6,
            courage = 10,
            wisdom = 7,
            humor = 5,
            traits = listOf("stern", "fair", "battle-hardened", "legendary")
        ),
        homeLocationId = "combat_arena",
        occupation = NPCOccupation.WARRIOR,
        factionId = "arena_guild",
        defaultDialogueTreeId = "arena_master_greeting",
        questGiverIds = listOf("combat_boss_rush", "hidden_true_hero"),
        schedule = NPCSchedule(
            entries = listOf(
                ScheduleEntry(0, 6, "combat_arena", "sleeping"),
                ScheduleEntry(6, 20, "combat_arena", "overseeing_battles"),
                ScheduleEntry(20, 24, "combat_arena", "training")
            )
        )
    )
    
    // ===== CATALOG METHODS =====
    
    /**
     * All NPCs in the game.
     */
    val allNPCs: List<NPC> = listOf(
        elderQuail,
        grumbleForgepaw,
        pipYoungQuail,
        mabelInnkeeper,
        oldQuill,
        captainBravewing,
        farmerCluck,
        scoutFeatherfoot,
        guardPeckins,
        merchantSeedsworth,
        willowFirefly,
        burrowBeetle,
        healerDowny,
        chirpTwin,
        cheepTwin,
        sparrowScout,
        // Batch 1: Buttonburgh Hub
        flintIronbeak,
        cloverSoftdown,
        scrollDustfeather,
        brambleSwiftpeck,
        // Batch 2: World NPCs - Grassland & Forest
        thistleForager,
        rustyWindwhisper,
        pebbleDeepdigger,
        willowMoonwing,
        oakStrongbranch,
        mapleLeafrunner,
        hunterQuickshot,
        nettleWebweaver,
        // Batch 2: World NPCs - Swamp
        marshMurkwater,
        sludgeCroaksong,
        venomSiltstalker,
        peatBogsinger,
        // Batch 2: World NPCs - Mountain
        stoneCliffclimber,
        granitePickwielder,
        echoWindwhisper,
        cragStonefist,
        slateTunnelborer,
        // Batch 2: World NPCs - Desert
        duneSandstrider,
        mirageSunseeker,
        scorpioStingweaver,
        oasisLifespring,
        ancientSandsage,
        // Batch 2: World NPCs - Coastal
        waveTidecaller,
        marinaShellseeker,
        beaconLightkeeper,
        // Batch 3: Companions & Special NPCs
        quailChickCompanion1,
        quailChickCompanion2,
        quailChickCompanion3,
        quailChickCompanion4,
        quailChickCompanion5,
        broodymaleQuail,
        arenaMaster
    )
    
    /**
     * Gets an NPC by ID.
     */
    fun getNPC(id: String): NPC? {
        return allNPCs.find { it.id == id }
    }
    
    /**
     * Gets all NPCs at a specific location at a given hour.
     */
    fun getNPCsAtLocation(locationId: String, currentHour: Int): List<NPC> {
        return allNPCs.filter { it.isAtLocation(locationId, currentHour) }
    }
    
    /**
     * Gets all NPCs with a specific occupation.
     */
    fun getNPCsByOccupation(occupation: NPCOccupation): List<NPC> {
        return allNPCs.filter { it.occupation == occupation }
    }
    
    /**
     * Gets all NPCs in a faction.
     */
    fun getNPCsByFaction(factionId: String): List<NPC> {
        return allNPCs.filter { it.factionId == factionId }
    }
    
    /**
     * Gets all quest giver NPCs.
     */
    fun getQuestGivers(): List<NPC> {
        return allNPCs.filter { it.questGiverIds.isNotEmpty() }
    }
    
    /**
     * Gets all merchant NPCs.
     */
    fun getMerchants(): List<NPC> {
        return allNPCs.filter { it.merchantInventory.isNotEmpty() }
    }
    
    /**
     * Returns total NPC count.
     */
    fun getTotalNPCCount(): Int = allNPCs.size
    
    /**
     * Validates all NPCs have unique IDs and valid schedules.
     */
    fun validateCatalog(): Boolean {
        // Check unique IDs
        val ids = allNPCs.map { it.id }
        if (ids.size != ids.distinct().size) {
            return false
        }
        
        // All NPCs are validated in their constructors
        return true
    }
}

