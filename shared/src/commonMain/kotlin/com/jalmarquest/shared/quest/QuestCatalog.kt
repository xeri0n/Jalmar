package com.jalmarquest.shared.quest

/**
 * Static catalog of all quests in the game.
 * Follows the established pattern from LocationCatalog, EnemyCatalog, ItemCatalog, DungeonCatalog.
 */
object QuestCatalog {
    
    /**
     * All available quests in the game.
     */
    val allQuests: List<Quest> = listOf(
        // ========== TUTORIAL QUESTS ==========
        
        Quest(
            id = "tutorial_first_steps",
            name = "First Steps",
            description = "Welcome to the world, little quail! Learn the basics of movement and exploration.",
            questType = QuestType.TUTORIAL,
            difficulty = QuestDifficulty.TRIVIAL,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.REACH,
                    description = "Explore the Starting Village",
                    targetId = "starting_village",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 50, seeds = 10),
            level = 1,
            giver = "elder_quail",
            autoComplete = true
        ),
        
        Quest(
            id = "tutorial_first_combat",
            name = "A Bug's Life",
            description = "The garden is full of insects. Defeat your first enemy to prove your mettle!",
            questType = QuestType.TUTORIAL,
            difficulty = QuestDifficulty.TRIVIAL,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat any enemy",
                    targetId = "", // Any enemy
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 100, items = listOf("twig_spear")),
            prerequisiteQuestIds = listOf("tutorial_first_steps"),
            level = 1,
            giver = "elder_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "tutorial_inventory",
            name = "Pack Your Bags",
            description = "Gather some supplies for your journey. Collect twigs from around the garden.",
            questType = QuestType.TUTORIAL,
            difficulty = QuestDifficulty.TRIVIAL,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Collect 3 Twigs",
                    targetId = "twig",
                    targetCount = 3
                )
            ),
            rewards = QuestReward(xp = 75, seeds = 15),
            prerequisiteQuestIds = listOf("tutorial_first_steps"),
            level = 1,
            giver = "elder_quail",
            autoComplete = true
        ),
        
        // ========== MAIN QUEST (EARLY GAME) ==========
        
        Quest(
            id = "main_gnome_threat",
            name = "The Garden Gnome's Shadow",
            description = "A towering ceramic gnome looms over the garden, its hollow interior now a fortress " +
                    "for territorial beetles. The elder quail fears they will spread beyond the gnome. " +
                    "Investigate this threat.",
            questType = QuestType.MAIN,
            difficulty = QuestDifficulty.EASY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.REACH,
                    description = "Find the Garden Gnome Fortress",
                    targetId = "garden_gnome_location",
                    targetCount = 1
                ),
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat 5 Beetles",
                    targetId = "beetle",
                    targetCount = 5
                )
            ),
            rewards = QuestReward(xp = 500, items = listOf("beetle_shell", "acorn_cap"), seeds = 50),
            prerequisiteQuestIds = listOf("tutorial_first_combat"),
            level = 3,
            giver = "elder_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "main_burrow_depths",
            name = "Secrets of the Burrow",
            description = "Strange sounds echo from the Abandoned Burrow. The elder believes ancient secrets " +
                    "lie within. Clear the first floor and report back.",
            questType = QuestType.MAIN,
            difficulty = QuestDifficulty.EASY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.DUNGEON_CLEAR,
                    description = "Clear Abandoned Burrow Floor 1",
                    targetId = "abandoned_burrow_f1",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 800, items = listOf("mole_fur_cloak"), glimmerShards = 5),
            prerequisiteQuestIds = listOf("main_gnome_threat"),
            level = 5,
            giver = "elder_quail",
            autoComplete = false
        ),
        
        // ========== SIDE QUESTS ==========
        
        Quest(
            id = "side_grasshopper_hunt",
            name = "Grasshopper Menace",
            description = "Grasshoppers are eating the garden's crops. A local farmer needs help culling them.",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.EASY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat 10 Grasshoppers",
                    targetId = "grasshopper",
                    targetCount = 10
                )
            ),
            rewards = QuestReward(xp = 300, seeds = 30),
            level = 2,
            giver = "farmer_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "side_lost_feather",
            name = "The Lost Feather",
            description = "A young quail lost their favorite feather in the meadow. Help them find it!",
            questType = QuestType.FETCH,
            difficulty = QuestDifficulty.TRIVIAL,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Find the Lost Feather",
                    targetId = "lost_feather",
                    targetCount = 1
                ),
                QuestObjective(
                    type = ObjectiveType.TALK,
                    description = "Return to the Young Quail",
                    targetId = "young_quail",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 100, seeds = 20, glimmerShards = 1),
            level = 1,
            giver = "young_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "side_spider_silk",
            name = "Silk Collector",
            description = "The local craftsman needs spider silk for a new recipe. Gather some from defeated spiders.",
            questType = QuestType.FETCH,
            difficulty = QuestDifficulty.MEDIUM,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Collect 5 Spider Silk",
                    targetId = "spider_silk",
                    targetCount = 5
                )
            ),
            rewards = QuestReward(xp = 400, unlockRecipeIds = listOf("silk_armor"), seeds = 40),
            level = 8,
            giver = "craftsman_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "side_compost_explorer",
            name = "Compost Expedition",
            description = "An expedition into the Compost Heap Depths. Prove your worth by clearing the first floor.",
            questType = QuestType.EXPLORATION,
            difficulty = QuestDifficulty.EASY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.DUNGEON_CLEAR,
                    description = "Clear Compost Heap Depths Floor 1",
                    targetId = "compost_heap_depths_f1",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 600, items = listOf("grub_jerky"), glimmerShards = 3),
            level = 4,
            giver = "explorer_quail",
            autoComplete = true
        ),
        
        Quest(
            id = "side_level_milestone",
            name = "Growing Stronger",
            description = "Train hard and reach level 5. The elder wants to see your progress.",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.EASY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.LEVEL,
                    description = "Reach Level 5",
                    targetId = "player_level",
                    targetCount = 5
                )
            ),
            rewards = QuestReward(xp = 500, seeds = 50, glimmerShards = 5),
            level = 1,
            giver = "elder_quail",
            autoComplete = true
        ),
        
        // ========== COMBAT QUESTS ==========
        
        Quest(
            id = "combat_beetle_brawl",
            name = "Beetle Brawl",
            description = "The beetles are getting aggressive. Show them who's boss!",
            questType = QuestType.COMBAT,
            difficulty = QuestDifficulty.MEDIUM,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat 15 Beetles",
                    targetId = "beetle",
                    targetCount = 15
                )
            ),
            rewards = QuestReward(xp = 700, items = listOf("beetle_shell_armor"), seeds = 60),
            level = 6,
            giver = "warrior_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "combat_spider_slayer",
            name = "Spider Slayer",
            description = "Spiders are terrorizing the garden paths. Eliminate them!",
            questType = QuestType.COMBAT,
            difficulty = QuestDifficulty.MEDIUM,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat 8 Spiders",
                    targetId = "spider",
                    targetCount = 8
                )
            ),
            rewards = QuestReward(xp = 650, glimmerShards = 8),
            level = 9,
            giver = "warrior_quail",
            autoComplete = false
        ),
        
        // ========== CRAFTING QUEST ==========
        
        Quest(
            id = "craft_first_weapon",
            name = "Forge Your Path",
            description = "Craft your first weapon at The Quailsmith. A twig spear will serve you well.",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.TRIVIAL,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.CRAFT,
                    description = "Craft a Twig Spear",
                    targetId = "twig_spear",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 150, seeds = 25),
            prerequisiteQuestIds = listOf("tutorial_inventory"),
            level = 2,
            giver = "craftsman_quail",
            autoComplete = true
        ),
        
        Quest(
            id = "craft_armor_set",
            name = "Armored and Ready",
            description = "Craft a full set of beetle shell armor to protect yourself.",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.MEDIUM,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.CRAFT,
                    description = "Craft Beetle Shell Helmet",
                    targetId = "beetle_shell_helmet",
                    targetCount = 1
                ),
                QuestObjective(
                    type = ObjectiveType.CRAFT,
                    description = "Craft Beetle Shell Chestplate",
                    targetId = "beetle_shell_chestplate",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 900, unlockRecipeIds = listOf("advanced_armor"), glimmerShards = 10),
            level = 10,
            giver = "craftsman_quail",
            autoComplete = true
        ),
        
        // ========== MAIN STORYLINE (ACT 1: THE GARDEN) ==========
        
        Quest(
            id = "main_forest_whispers",
            name = "Whispers in the Forest",
            description = "The forest beyond the garden is ancient and mysterious. The elder believes answers to " +
                    "the garden's troubles lie within. Venture into the forest and discover what secrets await.",
            questType = QuestType.MAIN,
            difficulty = QuestDifficulty.MEDIUM,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.REACH,
                    description = "Explore the Ancient Forest",
                    targetId = "ancient_forest",
                    targetCount = 1
                ),
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat the Mantis Guardian",
                    targetId = "mantis",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 1200, items = listOf("forest_map_fragment"), glimmerShards = 15),
            prerequisiteQuestIds = listOf("main_burrow_depths"),
            level = 10,
            giver = "elder_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "main_the_quailsmith",
            name = "Meeting Grumble Forgepaw",
            description = "A mole craftsman named Grumble Forgepaw has set up shop in Buttonburgh. Visit him at " +
                    "The Quailsmith to learn advanced crafting techniques.",
            questType = QuestType.MAIN,
            difficulty = QuestDifficulty.EASY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.TALK,
                    description = "Speak with Grumble Forgepaw",
                    targetId = "grumble_forgepaw",
                    targetCount = 1
                ),
                QuestObjective(
                    type = ObjectiveType.CRAFT,
                    description = "Craft any weapon",
                    targetId = "", // Any weapon recipe
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 600, unlockRecipeIds = listOf("flint_axe", "glass_blade"), seeds = 80),
            prerequisiteQuestIds = listOf("craft_first_weapon"),
            level = 7,
            giver = "elder_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "main_swamp_expedition",
            name = "Into the Swamp",
            description = "Strange lights flicker in the distant swamp. The elder fears a dark power is awakening. " +
                    "Investigate the swamp and uncover the source of this corruption.",
            questType = QuestType.MAIN,
            difficulty = QuestDifficulty.HARD,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.REACH,
                    description = "Find the Corrupted Swamp",
                    targetId = "corrupted_swamp",
                    targetCount = 1
                ),
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat 3 Bog Horrors",
                    targetId = "bog_horror",
                    targetCount = 3
                )
            ),
            rewards = QuestReward(xp = 2000, items = listOf("swamp_essence", "antidote"), glimmerShards = 25),
            prerequisiteQuestIds = listOf("main_forest_whispers"),
            level = 18,
            giver = "elder_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "main_mountain_ascent",
            name = "The Mountain's Call",
            description = "From the mountain peaks, one can see the entire world. An ancient oracle is said to dwell " +
                    "at the summit. Make the perilous climb to seek her wisdom.",
            questType = QuestType.MAIN,
            difficulty = QuestDifficulty.HARD,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.REACH,
                    description = "Reach the Mountain Summit",
                    targetId = "mountain_summit",
                    targetCount = 1
                ),
                QuestObjective(
                    type = ObjectiveType.TALK,
                    description = "Speak with the Oracle",
                    targetId = "mountain_oracle",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 2500, items = listOf("oracle_feather", "mountain_map_fragment"), glimmerShards = 30),
            prerequisiteQuestIds = listOf("main_swamp_expedition"),
            level = 22,
            giver = "elder_quail",
            autoComplete = false
        ),
        
        // ========== MAIN STORYLINE (ACT 2: THE DESERT) ==========
        
        Quest(
            id = "main_desert_sands",
            name = "Sands of Time",
            description = "The oracle spoke of an ancient temple buried in the desert sands. Journey across the " +
                    "scorching dunes to find this lost ruin and unlock its secrets.",
            questType = QuestType.MAIN,
            difficulty = QuestDifficulty.EXPERT,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.REACH,
                    description = "Find the Buried Temple",
                    targetId = "buried_temple",
                    targetCount = 1
                ),
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Retrieve the Sun Stone",
                    targetId = "sun_stone",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 3500, items = listOf("desert_map_fragment"), glimmerShards = 40),
            prerequisiteQuestIds = listOf("main_mountain_ascent"),
            level = 26,
            giver = "mountain_oracle",
            autoComplete = false
        ),
        
        Quest(
            id = "main_cactus_guardian",
            name = "Guardian of the Sands",
            description = "A colossal living cactus guards the temple's inner sanctum. Defeat this ancient guardian " +
                    "to claim the power within.",
            questType = QuestType.MAIN,
            difficulty = QuestDifficulty.EXPERT,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat the Cactus Guardian",
                    targetId = "cactus_guardian",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 4000, items = listOf("guardian_spine", "life_sap"), glimmerShards = 50),
            prerequisiteQuestIds = listOf("main_desert_sands"),
            level = 30,
            giver = "mountain_oracle",
            autoComplete = false
        ),
        
        Quest(
            id = "main_coastal_voyage",
            name = "Where Land Meets Sea",
            description = "With three map fragments in hand, the elder deciphers a route to the Coastal Cliffs. " +
                    "A final fragment awaits where waves crash against stone.",
            questType = QuestType.MAIN,
            difficulty = QuestDifficulty.EXPERT,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.REACH,
                    description = "Discover the Coastal Cliffs",
                    targetId = "coastal_cliffs",
                    targetCount = 1
                ),
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Find the Coastal Map Fragment",
                    targetId = "coastal_map_fragment",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 4500, items = listOf("pearl", "sea_shell"), glimmerShards = 60),
            prerequisiteQuestIds = listOf("main_cactus_guardian"),
            level = 33,
            giver = "elder_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "main_complete_map",
            name = "The World Unveiled",
            description = "Four map fragments collected. Return to the elder to piece together the complete map " +
                    "and reveal the location of the Shadow Realm.",
            questType = QuestType.MAIN,
            difficulty = QuestDifficulty.MEDIUM,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.TALK,
                    description = "Return to the Elder",
                    targetId = "elder_quail",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 5000, items = listOf("complete_map"), glimmerShards = 75),
            prerequisiteQuestIds = listOf("main_coastal_voyage"),
            level = 35,
            giver = "elder_quail",
            autoComplete = false
        ),
        
        // ========== MAIN STORYLINE (ACT 3: THE SHADOW REALM) ==========
        
        Quest(
            id = "main_shadow_entrance",
            name = "The Veil Between Worlds",
            description = "The complete map reveals a hidden cave entrance. This dark portal leads to the Shadow Realm, " +
                    "a place where nightmares dwell. Steel yourself and enter.",
            questType = QuestType.MAIN,
            difficulty = QuestDifficulty.LEGENDARY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.REACH,
                    description = "Enter the Shadow Realm",
                    targetId = "shadow_realm_entrance",
                    targetCount = 1
                ),
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat 5 Shadow Lurkers",
                    targetId = "shadow_lurker",
                    targetCount = 5
                )
            ),
            rewards = QuestReward(xp = 6000, items = listOf("shadow_essence", "void_crystal"), glimmerShards = 100),
            prerequisiteQuestIds = listOf("main_complete_map"),
            level = 38,
            giver = "elder_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "main_frost_wastes",
            name = "The Frozen Heart",
            description = "Deep within the Shadow Realm lies a frozen wasteland. Cross this tundra to reach the " +
                    "Heart of Shadows, where the final confrontation awaits.",
            questType = QuestType.MAIN,
            difficulty = QuestDifficulty.LEGENDARY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.REACH,
                    description = "Cross the Frozen Wastes",
                    targetId = "frozen_wastes",
                    targetCount = 1
                ),
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat the Frost Moth",
                    targetId = "frost_moth",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 7000, items = listOf("frost_wing", "frozen_essence"), glimmerShards = 120),
            prerequisiteQuestIds = listOf("main_shadow_entrance"),
            level = 40,
            giver = "elder_quail",
            autoComplete = false
        ),
        
        // ========== SIDE QUESTS (EXPANDED) ==========
        
        Quest(
            id = "side_honey_harvest",
            name = "Sweet Harvest",
            description = "Bees are producing exceptional honey this season. Collect honeycombs for the innkeeper " +
                    "at The Gilded Seed Inn.",
            questType = QuestType.FETCH,
            difficulty = QuestDifficulty.EASY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Collect 5 Honeycombs",
                    targetId = "honeycomb",
                    targetCount = 5
                )
            ),
            rewards = QuestReward(xp = 400, items = listOf("honey_drop", "honey_drop", "honey_drop"), seeds = 45),
            level = 6,
            giver = "innkeeper_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "side_dragonfly_wings",
            name = "Wings of Light",
            description = "Dragonfly wings shimmer with magical properties. A scholar needs them for research.",
            questType = QuestType.FETCH,
            difficulty = QuestDifficulty.MEDIUM,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Collect 3 Dragonfly Wings",
                    targetId = "dragonfly_wing",
                    targetCount = 3
                )
            ),
            rewards = QuestReward(xp = 800, unlockRecipeIds = listOf("wing_cape"), glimmerShards = 12),
            level = 8,
            giver = "scholar_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "side_mysterious_egg",
            name = "The Mysterious Egg",
            description = "You've found a peculiar egg in the forest. The elder recognizes it as a quail egg from " +
                    "a long-lost cousin species. Keep it safe until it hatches.",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.EASY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Find the Mysterious Egg",
                    targetId = "mysterious_egg",
                    targetCount = 1
                ),
                QuestObjective(
                    type = ObjectiveType.REACH,
                    description = "Wait for the egg to hatch (return to nest)",
                    targetId = "player_nest",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 1000, items = listOf("quail_chick_companion"), glimmerShards = 20),
            level = 12,
            giver = "",
            autoComplete = false
        ),
        
        Quest(
            id = "side_moth_dust_collector",
            name = "Dust in the Wind",
            description = "Moths leave behind a mysterious dust. Collect some for alchemical experiments.",
            questType = QuestType.FETCH,
            difficulty = QuestDifficulty.TRIVIAL,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Collect 10 Moth Dust",
                    targetId = "moth_dust",
                    targetCount = 10
                )
            ),
            rewards = QuestReward(xp = 350, seeds = 35, glimmerShards = 5),
            level = 4,
            giver = "alchemist_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "side_worm_invasion",
            name = "Worm Invasion",
            description = "Giant earthworms are tunneling under the garden, damaging root systems. Drive them out!",
            questType = QuestType.COMBAT,
            difficulty = QuestDifficulty.MEDIUM,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat 6 Earthworms",
                    targetId = "earthworm",
                    targetCount = 6
                )
            ),
            rewards = QuestReward(xp = 1100, items = listOf("worm_segment", "rich_soil"), seeds = 75),
            level = 14,
            giver = "farmer_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "side_lost_lore",
            name = "Lost Lore Fragment",
            description = "An ancient lore fragment was spotted in the swamp. Retrieve it for the scholar.",
            questType = QuestType.EXPLORATION,
            difficulty = QuestDifficulty.HARD,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Find the Lore Fragment",
                    targetId = "lore_fragment_swamp",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 1500, glimmerShards = 30),
            level = 16,
            giver = "scholar_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "side_scorpion_menace",
            name = "Scorpion Menace",
            description = "Scorpions from the mountains are venturing too close to trade routes. Clear them out.",
            questType = QuestType.COMBAT,
            difficulty = QuestDifficulty.HARD,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat 8 Scorpions",
                    targetId = "scorpion",
                    targetCount = 8
                )
            ),
            rewards = QuestReward(xp = 1800, items = listOf("scorpion_stinger", "venom_sac"), glimmerShards = 25),
            level = 19,
            giver = "trader_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "side_tarantula_terror",
            name = "Tarantula Terror",
            description = "A massive tarantula has made the desert its hunting ground. Eliminate this threat.",
            questType = QuestType.COMBAT,
            difficulty = QuestDifficulty.EXPERT,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat the Tarantula",
                    targetId = "tarantula",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 3000, items = listOf("spider_fang", "spider_silk"), glimmerShards = 45),
            level = 27,
            giver = "desert_nomad",
            autoComplete = false
        ),
        
        Quest(
            id = "side_jellyfish_research",
            name = "Jellyfish Study",
            description = "A marine researcher needs jellyfish tentacles to study their bioluminescence.",
            questType = QuestType.FETCH,
            difficulty = QuestDifficulty.EXPERT,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Collect 4 Jellyfish Tentacles",
                    targetId = "jellyfish_tentacle",
                    targetCount = 4
                )
            ),
            rewards = QuestReward(xp = 2800, unlockRecipeIds = listOf("glowstone"), glimmerShards = 40),
            level = 32,
            giver = "marine_researcher",
            autoComplete = false
        ),
        
        Quest(
            id = "side_legendary_clover",
            name = "Four-Leaf Fortune",
            description = "Find the legendary four-leaf clover hidden somewhere in the world. It brings unmatched luck.",
            questType = QuestType.EXPLORATION,
            difficulty = QuestDifficulty.LEGENDARY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Find the Four-Leaf Clover",
                    targetId = "four_leaf_clover",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 5000, items = listOf("luck_clover"), glimmerShards = 100),
            level = 35,
            giver = "",
            autoComplete = true
        ),
        
        Quest(
            id = "side_phoenix_feather",
            name = "The Phoenix Feather",
            description = "Legends speak of a phoenix that once roamed these lands. Find its last feather.",
            questType = QuestType.EXPLORATION,
            difficulty = QuestDifficulty.LEGENDARY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Find the Phoenix Feather",
                    targetId = "phoenix_feather",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 6000, unlockRecipeIds = listOf("revive_nectar"), glimmerShards = 150),
            level = 40,
            giver = "ancient_sage",
            autoComplete = false
        ),
        
        Quest(
            id = "side_level_20",
            name = "Veteran Adventurer",
            description = "Reach level 20 to prove yourself as a seasoned adventurer.",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.MEDIUM,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.LEVEL,
                    description = "Reach Level 20",
                    targetId = "player_level",
                    targetCount = 20
                )
            ),
            rewards = QuestReward(xp = 2000, seeds = 200, glimmerShards = 50),
            level = 15,
            giver = "elder_quail",
            autoComplete = true
        ),
        
        Quest(
            id = "side_level_35",
            name = "Master of the Realm",
            description = "Reach level 35 to join the ranks of legendary heroes.",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.EXPERT,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.LEVEL,
                    description = "Reach Level 35",
                    targetId = "player_level",
                    targetCount = 35
                )
            ),
            rewards = QuestReward(xp = 4000, seeds = 400, glimmerShards = 100),
            level = 25,
            giver = "elder_quail",
            autoComplete = true
        ),
        
        Quest(
            id = "side_master_crafter",
            name = "Master Crafter",
            description = "Craft 25 different items to become a master craftsman.",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.HARD,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.CRAFT,
                    description = "Craft 25 unique items",
                    targetId = "", // Any 25 items
                    targetCount = 25
                )
            ),
            rewards = QuestReward(xp = 3500, unlockRecipeIds = listOf("enchanted_staff", "crystal_crown"), glimmerShards = 75),
            level = 20,
            giver = "grumble_forgepaw",
            autoComplete = true
        ),
        
        Quest(
            id = "side_recipe_collector",
            name = "Recipe Collector",
            description = "Unlock 50 crafting recipes to become a master of all trades.",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.EXPERT,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Unlock 50 recipes",
                    targetId = "recipes_unlocked",
                    targetCount = 50
                )
            ),
            rewards = QuestReward(xp = 5000, items = listOf("recipe_compendium"), glimmerShards = 120),
            level = 30,
            giver = "grumble_forgepaw",
            autoComplete = true
        ),
        
        // ========== EXPLORATION QUESTS ==========
        
        Quest(
            id = "explore_all_grasslands",
            name = "Grassland Explorer",
            description = "Explore every corner of the grasslands and document all locations.",
            questType = QuestType.EXPLORATION,
            difficulty = QuestDifficulty.EASY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.REACH,
                    description = "Discover 5 grassland locations",
                    targetId = "grassland_locations",
                    targetCount = 5
                )
            ),
            rewards = QuestReward(xp = 800, seeds = 80, glimmerShards = 15),
            level = 5,
            giver = "explorer_quail",
            autoComplete = true
        ),
        
        Quest(
            id = "explore_all_forest",
            name = "Forest Cartographer",
            description = "Map the entire ancient forest, from clearings to deepest groves.",
            questType = QuestType.EXPLORATION,
            difficulty = QuestDifficulty.MEDIUM,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.REACH,
                    description = "Discover 8 forest locations",
                    targetId = "forest_locations",
                    targetCount = 8
                )
            ),
            rewards = QuestReward(xp = 1500, items = listOf("forest_compass"), glimmerShards = 25),
            level = 10,
            giver = "explorer_quail",
            autoComplete = true
        ),
        
        Quest(
            id = "explore_all_biomes",
            name = "World Wanderer",
            description = "Visit every biome in the world: grassland, forest, swamp, mountain, desert, coastal, tundra, cave.",
            questType = QuestType.EXPLORATION,
            difficulty = QuestDifficulty.EXPERT,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.REACH,
                    description = "Visit all 8 biome types",
                    targetId = "all_biomes",
                    targetCount = 8
                )
            ),
            rewards = QuestReward(xp = 5000, items = listOf("world_atlas"), glimmerShards = 100),
            level = 25,
            giver = "explorer_quail",
            autoComplete = true
        ),
        
        Quest(
            id = "explore_hidden_caves",
            name = "Cave Spelunker",
            description = "Find and explore 10 hidden caves scattered across the world.",
            questType = QuestType.EXPLORATION,
            difficulty = QuestDifficulty.HARD,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.REACH,
                    description = "Discover 10 hidden caves",
                    targetId = "hidden_caves",
                    targetCount = 10
                )
            ),
            rewards = QuestReward(xp = 3000, items = listOf("cave_torch", "spelunking_kit"), glimmerShards = 60),
            level = 18,
            giver = "cave_explorer",
            autoComplete = true
        ),
        
        Quest(
            id = "explore_secret_paths",
            name = "Secret Pathfinder",
            description = "Discover 5 secret pathways that connect distant locations.",
            questType = QuestType.EXPLORATION,
            difficulty = QuestDifficulty.HARD,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.REACH,
                    description = "Find 5 secret paths",
                    targetId = "secret_paths",
                    targetCount = 5
                )
            ),
            rewards = QuestReward(xp = 2500, items = listOf("pathfinder_compass"), glimmerShards = 50),
            level = 22,
            giver = "explorer_quail",
            autoComplete = true
        ),
        
        // ========== COMBAT CHALLENGES ==========
        
        Quest(
            id = "combat_hawk_hunt",
            name = "Sky Hunter Challenge",
            description = "Mountain hawks are apex predators. Prove your prowess by defeating 3 of them.",
            questType = QuestType.COMBAT,
            difficulty = QuestDifficulty.EXPERT,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat 3 Mountain Hawks",
                    targetId = "mountain_hawk",
                    targetCount = 3
                )
            ),
            rewards = QuestReward(xp = 3500, items = listOf("hawk_talon", "feather"), glimmerShards = 70),
            level = 20,
            giver = "hunter_quail",
            autoComplete = false
        ),
        
        Quest(
            id = "combat_boss_rush",
            name = "Champion's Gauntlet",
            description = "Defeat all 5 major bosses: Mantis, Bog Horror, Cactus Guardian, Frost Moth, Shadow Lurker.",
            questType = QuestType.COMBAT,
            difficulty = QuestDifficulty.LEGENDARY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat the Mantis Guardian",
                    targetId = "mantis",
                    targetCount = 1
                ),
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat a Bog Horror",
                    targetId = "bog_horror",
                    targetCount = 1
                ),
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat the Cactus Guardian",
                    targetId = "cactus_guardian",
                    targetCount = 1
                ),
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat the Frost Moth",
                    targetId = "frost_moth",
                    targetCount = 1
                ),
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat a Shadow Lurker",
                    targetId = "shadow_lurker",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 10000, items = listOf("champion_trophy", "legendary_weapon"), glimmerShards = 250),
            level = 45,
            giver = "arena_master",
            autoComplete = true
        ),
        
        Quest(
            id = "combat_100_enemies",
            name = "Century Slayer",
            description = "Defeat 100 enemies of any type to earn the title of Century Slayer.",
            questType = QuestType.COMBAT,
            difficulty = QuestDifficulty.MEDIUM,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat 100 enemies",
                    targetId = "", // Any enemy
                    targetCount = 100
                )
            ),
            rewards = QuestReward(xp = 4000, items = listOf("slayer_badge"), glimmerShards = 80),
            level = 15,
            giver = "warrior_quail",
            autoComplete = true
        ),
        
        // ========== HIDDEN/SECRET QUESTS ==========
        
        Quest(
            id = "secret_lore_fragments",
            name = "Keeper of Lore",
            description = "Collect all 10 lore fragments scattered throughout the world to unlock the complete history.",
            questType = QuestType.EXPLORATION,
            difficulty = QuestDifficulty.LEGENDARY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Collect 10 Lore Fragments",
                    targetId = "lore_fragment",
                    targetCount = 10
                )
            ),
            rewards = QuestReward(xp = 8000, items = listOf("complete_lore_book"), glimmerShards = 200),
            level = 30,
            giver = "",
            autoComplete = true
        ),
        
        Quest(
            id = "secret_quail_level_stupid",
            name = "A Quail-Level Stupid Way to Die",
            description = "Attempt something so reckless that even a quail would know better. (Die in a particularly absurd way)",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.TRIVIAL,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Experience a comedic death",
                    targetId = "absurd_death",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 100, items = listOf("dunce_feather"), seeds = 1),
            level = 1,
            giver = "",
            autoComplete = true
        ),
        
        Quest(
            id = "secret_broody_male",
            name = "The Broody Male",
            description = "Discover a rare broody male quail attempting to incubate eggs. (Easter egg for quail enthusiasts)",
            questType = QuestType.EXPLORATION,
            difficulty = QuestDifficulty.MEDIUM,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.REACH,
                    description = "Find the Broody Male Quail",
                    targetId = "broody_male_location",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 1500, items = listOf("broody_badge"), glimmerShards = 30),
            level = 8,
            giver = "",
            autoComplete = true
        ),
        
        Quest(
            id = "secret_all_companions",
            name = "Quail Family Reunion",
            description = "Collect all 5 quail chick companions from mysterious eggs.",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.HARD,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Collect 5 quail chick companions",
                    targetId = "quail_chick_companion",
                    targetCount = 5
                )
            ),
            rewards = QuestReward(xp = 5000, items = listOf("family_portrait"), glimmerShards = 100),
            level = 25,
            giver = "",
            autoComplete = true
        ),
        
        Quest(
            id = "secret_mirror_encounter",
            name = "Mirror, Mirror",
            description = "Find the magic mirror and confront your reflection in a unique encounter.",
            questType = QuestType.EXPLORATION,
            difficulty = QuestDifficulty.EXPERT,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.REACH,
                    description = "Find the Magic Mirror",
                    targetId = "magic_mirror_location",
                    targetCount = 1
                ),
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Defeat your Reflection",
                    targetId = "player_reflection",
                    targetCount = 1
                )
            ),
            rewards = QuestReward(xp = 6000, items = listOf("mirror_shard", "self_knowledge"), glimmerShards = 150),
            level = 35,
            giver = "",
            autoComplete = false
        ),
        
        Quest(
            id = "secret_no_filter_mode",
            name = "Unfiltered Reality",
            description = "Unlock No Filter Mode by witnessing 10 absurdly satirical events.",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.MEDIUM,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Witness 10 satirical events",
                    targetId = "satirical_events",
                    targetCount = 10
                )
            ),
            rewards = QuestReward(xp = 2000, items = listOf("no_filter_badge"), glimmerShards = 50),
            level = 15,
            giver = "",
            autoComplete = true
        ),
        
        Quest(
            id = "secret_max_level",
            name = "Apex Predator",
            description = "Reach the maximum level of 50 to become the apex predator of the garden.",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.LEGENDARY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.LEVEL,
                    description = "Reach Level 50",
                    targetId = "player_level",
                    targetCount = 50
                )
            ),
            rewards = QuestReward(xp = 0, items = listOf("apex_crown", "godly_feather"), glimmerShards = 500),
            level = 40,
            giver = "",
            autoComplete = true
        ),
        
        Quest(
            id = "secret_full_completion",
            name = "True Hero of JalmarQuest",
            description = "Complete every single quest in the game (54 quests total).",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.LEGENDARY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Complete all 54 quests",
                    targetId = "quests_completed",
                    targetCount = 54
                )
            ),
            rewards = QuestReward(xp = 15000, items = listOf("jalmar_crown", "true_hero_badge"), glimmerShards = 1000),
            level = 50,
            giver = "",
            autoComplete = true
        )
    )
    
    /**
     * Retrieves a quest by its unique ID.
     * 
     * @param questId The quest identifier
     * @return The quest, or null if not found
     */
    fun getQuest(questId: String): Quest? {
        return allQuests.find { it.id == questId }
    }
    
    /**
     * Retrieves all quests of a specific type.
     * 
     * @param questType The quest type to filter by
     * @return List of quests matching the type
     */
    fun getQuestsByType(questType: QuestType): List<Quest> {
        return allQuests.filter { it.questType == questType }
    }
    
    /**
     * Retrieves all quests given by a specific NPC.
     * 
     * @param giverId NPC identifier
     * @return List of quests given by that NPC
     */
    fun getQuestsByGiver(giverId: String): List<Quest> {
        return allQuests.filter { it.giver == giverId }
    }
    
    /**
     * Retrieves all quests suitable for a player level.
     * Returns quests where level <= playerLevel <= (level + 5).
     * 
     * @param playerLevel The player's current level
     * @return List of level-appropriate quests
     */
    fun getQuestsForLevel(playerLevel: Int): List<Quest> {
        return allQuests.filter { quest ->
            playerLevel >= quest.level && playerLevel <= quest.level + 5
        }
    }
    
    /**
     * Retrieves all quests with a specific difficulty.
     * 
     * @param difficulty The difficulty tier
     * @return List of quests matching difficulty
     */
    fun getQuestsByDifficulty(difficulty: QuestDifficulty): List<Quest> {
        return allQuests.filter { it.difficulty == difficulty }
    }
    
    /**
     * Returns total quest count (for validation/stats).
     */
    fun getTotalQuestCount(): Int = allQuests.size
    
    /**
     * Validates that all quests have unique IDs.
     * Throws IllegalStateException if duplicates found.
     */
    fun validateCatalog() {
        val ids = allQuests.map { it.id }
        val duplicates = ids.groupingBy { it }.eachCount().filter { it.value > 1 }
        
        if (duplicates.isNotEmpty()) {
            throw IllegalStateException("Duplicate quest IDs found: ${duplicates.keys}")
        }
        
        // Validate prerequisites exist
        allQuests.forEach { quest ->
            quest.prerequisiteQuestIds.forEach { prereqId ->
                if (getQuest(prereqId) == null) {
                    throw IllegalStateException("Quest '${quest.id}' has invalid prerequisite: $prereqId")
                }
            }
        }
    }
}
