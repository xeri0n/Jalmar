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
