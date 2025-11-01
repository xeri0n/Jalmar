package com.jalmarquest.shared.nest

/**
 * Catalog of all trophies available in the game.
 * Trophies are unlocked by completing achievements and displayed in the trophy room.
 */
object TrophyCatalog {
    
    private val trophies = listOf(
        // ========== BOSS DEFEATED TROPHIES ==========
        
        Trophy(
            id = "trophy_giant_spider_defeated",
            name = "Giant Spider Fang",
            description = "A massive fang from the Garden's most feared predator. A testament to your bravery.",
            type = TrophyType.BOSS_DEFEATED,
            rarity = TrophyRarity.EPIC,
            size = TrophySize.LARGE,
            basePrestige = 100,
            unlockAchievementId = "achievement_defeat_giant_spider"
        ),
        
        Trophy(
            id = "trophy_garden_gnome_conquered",
            name = "Gnome's Cracked Eye",
            description = "A ceramic shard from the Terrifying Titan. You've proven yourself against the mightiest foe.",
            type = TrophyType.BOSS_DEFEATED,
            rarity = TrophyRarity.LEGENDARY,
            size = TrophySize.LARGE,
            basePrestige = 200,
            unlockAchievementId = "achievement_defeat_garden_gnome"
        ),
        
        Trophy(
            id = "trophy_beetle_king_defeated",
            name = "Iridescent Shell Fragment",
            description = "A shimmering piece of the Beetle King's armor. It gleams with rainbow light.",
            type = TrophyType.BOSS_DEFEATED,
            rarity = TrophyRarity.EPIC,
            size = TrophySize.MEDIUM,
            basePrestige = 90,
            unlockAchievementId = "achievement_defeat_beetle_king"
        ),
        
        // ========== QUEST COMPLETE TROPHIES ==========
        
        Trophy(
            id = "trophy_first_quest",
            name = "Adventurer's First Medal",
            description = "A small acorn cap medal commemorating your first quest. Everyone starts somewhere!",
            type = TrophyType.QUEST_COMPLETE,
            rarity = TrophyRarity.COMMON,
            size = TrophySize.SMALL,
            basePrestige = 10,
            unlockAchievementId = "achievement_complete_first_quest"
        ),
        
        Trophy(
            id = "trophy_ten_quests",
            name = "Quail of Deed",
            description = "A bronze plaque recognizing your dedication to helping others.",
            type = TrophyType.QUEST_COMPLETE,
            rarity = TrophyRarity.UNCOMMON,
            size = TrophySize.SMALL,
            basePrestige = 25,
            unlockAchievementId = "achievement_complete_ten_quests"
        ),
        
        Trophy(
            id = "trophy_hundred_quests",
            name = "Hero's Hundred Honors",
            description = "A golden trophy inscribed with 100 quest symbols. You are a true champion of the garden.",
            type = TrophyType.QUEST_COMPLETE,
            rarity = TrophyRarity.EPIC,
            size = TrophySize.LARGE,
            basePrestige = 150,
            unlockAchievementId = "achievement_complete_hundred_quests"
        ),
        
        Trophy(
            id = "trophy_main_story_complete",
            name = "The Gilded Seed",
            description = "A legendary golden seed said to bring prosperity. Awarded to those who complete the epic journey.",
            type = TrophyType.QUEST_COMPLETE,
            rarity = TrophyRarity.LEGENDARY,
            size = TrophySize.MEDIUM,
            basePrestige = 250,
            unlockAchievementId = "achievement_complete_main_story"
        ),
        
        // ========== MILESTONE TROPHIES ==========
        
        Trophy(
            id = "trophy_level_10",
            name = "Veteran Quail Badge",
            description = "You've reached level 10! A milestone on your path to greatness.",
            type = TrophyType.MILESTONE,
            rarity = TrophyRarity.UNCOMMON,
            size = TrophySize.SMALL,
            basePrestige = 20,
            unlockAchievementId = "achievement_reach_level_10"
        ),
        
        Trophy(
            id = "trophy_level_25",
            name = "Elite Warrior Crest",
            description = "Level 25 marks you as an elite among quails. Few reach this height.",
            type = TrophyType.MILESTONE,
            rarity = TrophyRarity.RARE,
            size = TrophySize.MEDIUM,
            basePrestige = 60,
            unlockAchievementId = "achievement_reach_level_25"
        ),
        
        Trophy(
            id = "trophy_level_50",
            name = "Legendary Quail Crown",
            description = "The maximum level! You stand at the peak of power. Only legends earn this crown.",
            type = TrophyType.MILESTONE,
            rarity = TrophyRarity.LEGENDARY,
            size = TrophySize.LARGE,
            basePrestige = 300,
            unlockAchievementId = "achievement_reach_level_50"
        ),
        
        Trophy(
            id = "trophy_100_enemies",
            name = "Slayer's Tally",
            description = "A notched stick marking 100 defeated foes. Your combat prowess is undeniable.",
            type = TrophyType.MILESTONE,
            rarity = TrophyRarity.RARE,
            size = TrophySize.SMALL,
            basePrestige = 40,
            unlockAchievementId = "achievement_defeat_100_enemies"
        ),
        
        // ========== DISCOVERY TROPHIES ==========
        
        Trophy(
            id = "trophy_all_locations",
            name = "Explorer's Complete Map",
            description = "A hand-drawn map of every location in the garden. You've seen it all!",
            type = TrophyType.DISCOVERY,
            rarity = TrophyRarity.EPIC,
            size = TrophySize.MEDIUM,
            basePrestige = 120,
            unlockAchievementId = "achievement_discover_all_locations"
        ),
        
        Trophy(
            id = "trophy_hidden_grove",
            name = "Secret Grove Flower",
            description = "A rare flower from the hidden grove. Only the most curious adventurers find this place.",
            type = TrophyType.DISCOVERY,
            rarity = TrophyRarity.RARE,
            size = TrophySize.SMALL,
            basePrestige = 50,
            unlockAchievementId = "achievement_discover_hidden_grove"
        ),
        
        Trophy(
            id = "trophy_underwater_cavern",
            name = "Glowing Crystal",
            description = "A luminescent crystal from the underwater cavern. It pulses with mysterious energy.",
            type = TrophyType.DISCOVERY,
            rarity = TrophyRarity.EPIC,
            size = TrophySize.SMALL,
            basePrestige = 80,
            unlockAchievementId = "achievement_discover_underwater_cavern"
        ),
        
        // ========== COLLECTION TROPHIES ==========
        
        Trophy(
            id = "trophy_all_items",
            name = "Collector's Compendium",
            description = "A catalog of every item in the garden. Your collection is complete!",
            type = TrophyType.COLLECTION,
            rarity = TrophyRarity.LEGENDARY,
            size = TrophySize.LARGE,
            basePrestige = 350,
            unlockAchievementId = "achievement_collect_all_items"
        ),
        
        Trophy(
            id = "trophy_all_enemies",
            name = "Bestiary of Buttonburgh",
            description = "A comprehensive guide to every creature you've encountered and defeated.",
            type = TrophyType.COLLECTION,
            rarity = TrophyRarity.EPIC,
            size = TrophySize.MEDIUM,
            basePrestige = 140,
            unlockAchievementId = "achievement_defeat_all_enemy_types"
        ),
        
        Trophy(
            id = "trophy_all_lore_fragments",
            name = "Ancient Lore Codex",
            description = "The complete history of the garden, pieced together from scattered fragments.",
            type = TrophyType.COLLECTION,
            rarity = TrophyRarity.LEGENDARY,
            size = TrophySize.MEDIUM,
            basePrestige = 280,
            unlockAchievementId = "achievement_collect_all_lore"
        ),
        
        // ========== COMBAT TROPHIES ==========
        
        Trophy(
            id = "trophy_flawless_victory",
            name = "Pristine Combat Ribbon",
            description = "Awarded for defeating a boss without taking damage. A perfect display of skill.",
            type = TrophyType.COMBAT,
            rarity = TrophyRarity.EPIC,
            size = TrophySize.SMALL,
            basePrestige = 100,
            unlockAchievementId = "achievement_flawless_boss_victory"
        ),
        
        Trophy(
            id = "trophy_combo_master",
            name = "Chain Strike Medal",
            description = "For achieving a 50-hit combo. Your combat flow is masterful.",
            type = TrophyType.COMBAT,
            rarity = TrophyRarity.RARE,
            size = TrophySize.SMALL,
            basePrestige = 70,
            unlockAchievementId = "achievement_50_hit_combo"
        ),
        
        // ========== SOCIAL TROPHIES ==========
        
        Trophy(
            id = "trophy_all_npcs_friend",
            name = "Friend of the Garden",
            description = "Every NPC in the garden considers you a friend. Your kindness knows no bounds.",
            type = TrophyType.SOCIAL,
            rarity = TrophyRarity.LEGENDARY,
            size = TrophySize.MEDIUM,
            basePrestige = 220,
            unlockAchievementId = "achievement_befriend_all_npcs"
        ),
        
        Trophy(
            id = "trophy_max_faction_standing",
            name = "Faction Champion Emblem",
            description = "Honored by all factions. You've united the garden through diplomacy.",
            type = TrophyType.SOCIAL,
            rarity = TrophyRarity.EPIC,
            size = TrophySize.MEDIUM,
            basePrestige = 130,
            unlockAchievementId = "achievement_max_all_factions"
        ),
        
        // ========== SPECIAL TROPHIES ==========
        
        Trophy(
            id = "trophy_speedrun_record",
            name = "Temporal Medallion",
            description = "For completing the main story in record time. Time itself bows to your speed.",
            type = TrophyType.SPECIAL,
            rarity = TrophyRarity.LEGENDARY,
            size = TrophySize.SMALL,
            basePrestige = 400,
            unlockAchievementId = "achievement_speedrun_under_10_hours"
        ),
        
        Trophy(
            id = "trophy_no_filter_complete",
            name = "Badge of Chaotic Glory",
            description = "You survived 'No Filter Mode'. Your sense of humor is legendary.",
            type = TrophyType.SPECIAL,
            rarity = TrophyRarity.LEGENDARY,
            size = TrophySize.MEDIUM,
            basePrestige = 500,
            unlockAchievementId = "achievement_no_filter_complete"
        )
    )
    
    /**
     * Get all trophies in the catalog.
     */
    fun getAllTrophies(): List<Trophy> = trophies
    
    /**
     * Get trophy by ID.
     */
    fun getTrophyById(id: String): Trophy? {
        return trophies.find { it.id == id }
    }
    
    /**
     * Get all trophies of a specific type.
     */
    fun getTrophiesByType(type: TrophyType): List<Trophy> {
        return trophies.filter { it.type == type }
    }
    
    /**
     * Get all trophies of a specific rarity.
     */
    fun getTrophiesByRarity(rarity: TrophyRarity): List<Trophy> {
        return trophies.filter { it.rarity == rarity }
    }
    
    /**
     * Get trophies that can be unlocked (no achievement requirement).
     */
    fun getAlwaysAvailableTrophies(): List<Trophy> {
        return trophies.filter { it.unlockAchievementId == null }
    }
}
