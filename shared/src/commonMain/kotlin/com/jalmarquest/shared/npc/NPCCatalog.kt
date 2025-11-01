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
        sparrowScout
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
