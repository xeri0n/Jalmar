package com.jalmarquest.shared.radiant

import com.jalmarquest.shared.quest.ObjectiveType
import com.jalmarquest.shared.quest.QuestDifficulty
import com.jalmarquest.shared.quest.QuestType

/**
 * Catalog of predefined radiant quest templates.
 * 
 * Provides reusable quest templates for AI-driven generation.
 * Templates are organized by quest type and difficulty.
 * 
 * Templates cover:
 * - FETCH quests (gather items for NPCs)
 * - COMBAT quests (defeat enemies in locations)
 * - EXPLORATION quests (discover and reach locations)
 * - SOCIAL quests (deliver messages, help NPCs)
 * - Collection quests (bring multiple items)
 * - Mystery quests (investigate locations)
 */
object RadiantQuestCatalog {
    
    /**
     * Get all available quest templates.
     * 
     * @return Map of template ID → template
     */
    fun getAllTemplates(): Map<String, RadiantQuestTemplate> {
        return mapOf(
            "fetch_seeds_for_npc" to createFetchSeedsQuest(),
            "gather_items_for_npc" to createGatherItemsQuest(),
            "clear_location_enemies" to createClearLocationQuest(),
            "hunt_enemy_type" to createHuntEnemyQuest(),
            "deliver_message" to createDeliverMessageQuest(),
            "help_npc_find_item" to createHelpNPCQuest(),
            "explore_location" to createExploreLocationQuest(),
            "collect_crafting_materials" to createCollectMaterialsQuest(),
            "investigate_mystery" to createInvestigateMysteryQuest(),
            "escort_npc" to createEscortNPCQuest()
        )
    }
    
    /**
     * Get specific template by ID.
     * 
     * @param templateId Template identifier
     * @return Template or null if not found
     */
    fun getTemplate(templateId: String): RadiantQuestTemplate? {
        return getAllTemplates()[templateId]
    }
    
    /**
     * Get templates by quest type.
     * 
     * @param questType Quest type filter
     * @return List of matching templates
     */
    fun getTemplatesByType(questType: QuestType): List<RadiantQuestTemplate> {
        return getAllTemplates().values.filter { it.questType == questType }
    }
    
    // ========================================
    // FETCH QUESTS
    // ========================================
    
    /**
     * Basic fetch quest: gather seeds for an NPC.
     * 
     * Example: "Gather Seeds for Grumble Forgepaw"
     * Objective: Collect 10-25 seeds
     * Reward: XP + currency based on level
     */
    private fun createFetchSeedsQuest(): RadiantQuestTemplate {
        return RadiantQuestTemplate(
            templateId = "fetch_seeds_for_npc",
            nameTemplate = "Gather Seeds for {npcId}",
            descriptionTemplate = "{npcId} needs seeds for winter storage. Can you help gather {itemId_count} {itemId}?",
            questType = QuestType.FETCH,
            baseDifficulty = QuestDifficulty.EASY,
            objectiveTemplates = listOf(
                ObjectiveTemplate(
                    type = ObjectiveType.COLLECT,
                    descriptionTemplate = "Collect {itemId_count} {itemId}",
                    targetParameter = "itemId",
                    countMin = 10,
                    countMax = 25
                )
            ),
            rewardScaling = RewardScaling(
                baseXP = 50L,
                xpPerLevel = 10L,
                baseSeeds = 30,
                seedsPerLevel = 5,
                itemRewardPool = listOf("twig", "pebble", "feather"),
                itemRewardChance = 0.3
            ),
            contextRequirements = ContextRequirements(
                minPlayerLevel = 1,
                maxPlayerLevel = 20
            ),
            cooldownTicks = 2880 * 60 // 2 days
        )
    }
    
    /**
     * Varied fetch quest: gather specific items.
     * 
     * Example: "Bring Twigs to Old Quill"
     * Objective: Collect 5-15 twigs/pebbles/acorns
     */
    private fun createGatherItemsQuest(): RadiantQuestTemplate {
        return RadiantQuestTemplate(
            templateId = "gather_items_for_npc",
            nameTemplate = "Bring {itemId} to {npcId}",
            descriptionTemplate = "{npcId} needs {itemId_count} {itemId} for a project. Help them out!",
            questType = QuestType.FETCH,
            baseDifficulty = QuestDifficulty.EASY,
            objectiveTemplates = listOf(
                ObjectiveTemplate(
                    type = ObjectiveType.COLLECT,
                    descriptionTemplate = "Gather {itemId_count} {itemId}",
                    targetParameter = "itemId",
                    countMin = 5,
                    countMax = 15
                )
            ),
            rewardScaling = RewardScaling(
                baseXP = 40L,
                xpPerLevel = 8L,
                baseSeeds = 25,
                seedsPerLevel = 4,
                baseGlimmerShards = 0,
                glimmerShardsPerLevel = 1,
                itemRewardPool = listOf("acorn", "berry", "moss"),
                itemRewardChance = 0.25
            ),
            contextRequirements = ContextRequirements(
                minPlayerLevel = 1,
                maxPlayerLevel = 30
            ),
            cooldownTicks = 2160 * 60 // 1.5 days
        )
    }
    
    // ========================================
    // COMBAT QUESTS
    // ========================================
    
    /**
     * Clear location of enemies.
     * 
     * Example: "Clear the Old Oak Tree of Beetles"
     * Objective: Defeat 3-8 beetles at location
     */
    private fun createClearLocationQuest(): RadiantQuestTemplate {
        return RadiantQuestTemplate(
            templateId = "clear_location_enemies",
            nameTemplate = "Clear {locationId} of {enemyId}",
            descriptionTemplate = "{locationId} has been overrun by {enemyId}! Clear them out to make it safe again.",
            questType = QuestType.COMBAT,
            baseDifficulty = QuestDifficulty.MEDIUM,
            objectiveTemplates = listOf(
                ObjectiveTemplate(
                    type = ObjectiveType.KILL,
                    descriptionTemplate = "Defeat {enemyId_count} {enemyId} at {locationId}",
                    targetParameter = "enemyId",
                    countMin = 3,
                    countMax = 8
                )
            ),
            rewardScaling = RewardScaling(
                baseXP = 80L,
                xpPerLevel = 15L,
                baseSeeds = 50,
                seedsPerLevel = 8,
                baseGlimmerShards = 1,
                glimmerShardsPerLevel = 2,
                itemRewardPool = listOf("twig_spear", "acorn_helmet", "pebble_shield"),
                itemRewardChance = 0.4
            ),
            contextRequirements = ContextRequirements(
                minPlayerLevel = 5,
                maxPlayerLevel = 50,
                minAITension = 30,
                maxAITension = 100
            ),
            cooldownTicks = 4320 * 60 // 3 days
        )
    }
    
    /**
     * Hunt specific enemy type across any location.
     * 
     * Example: "Hunt Ants in the Grasslands"
     * Objective: Defeat 5-12 ants in specific biome
     */
    private fun createHuntEnemyQuest(): RadiantQuestTemplate {
        return RadiantQuestTemplate(
            templateId = "hunt_enemy_type",
            nameTemplate = "Hunt {enemyId} in {biome}",
            descriptionTemplate = "{enemyId} have been spotted in {biome}. Hunt down {enemyId_count} of them!",
            questType = QuestType.COMBAT,
            baseDifficulty = QuestDifficulty.MEDIUM,
            objectiveTemplates = listOf(
                ObjectiveTemplate(
                    type = ObjectiveType.KILL,
                    descriptionTemplate = "Defeat {enemyId_count} {enemyId}",
                    targetParameter = "enemyId",
                    countMin = 5,
                    countMax = 12
                )
            ),
            rewardScaling = RewardScaling(
                baseXP = 70L,
                xpPerLevel = 12L,
                baseSeeds = 40,
                seedsPerLevel = 6,
                baseGlimmerShards = 0,
                glimmerShardsPerLevel = 1,
                itemRewardPool = listOf("health_berry", "stamina_seed"),
                itemRewardChance = 0.35
            ),
            contextRequirements = ContextRequirements(
                minPlayerLevel = 3,
                maxPlayerLevel = 40,
                minAITension = 20,
                maxAITension = 100
            ),
            cooldownTicks = 2880 * 60 // 2 days
        )
    }
    
    // ========================================
    // SOCIAL QUESTS
    // ========================================
    
    /**
     * Deliver message from one NPC to another.
     * 
     * Example: "Deliver Message from Grumble to Old Quill"
     * Objective: Talk to target NPC at their location
     */
    private fun createDeliverMessageQuest(): RadiantQuestTemplate {
        return RadiantQuestTemplate(
            templateId = "deliver_message",
            nameTemplate = "Deliver Message to {npcId}",
            descriptionTemplate = "A friend needs you to deliver an important message to {npcId}. Find them and relay the message!",
            questType = QuestType.SIDE,
            baseDifficulty = QuestDifficulty.TRIVIAL,
            objectiveTemplates = listOf(
                ObjectiveTemplate(
                    type = ObjectiveType.TALK,
                    descriptionTemplate = "Find and talk to {npcId}",
                    targetParameter = "npcId",
                    countMin = 1,
                    countMax = 1
                )
            ),
            rewardScaling = RewardScaling(
                baseXP = 30L,
                xpPerLevel = 5L,
                baseSeeds = 15,
                seedsPerLevel = 3,
                itemRewardChance = 0.15
            ),
            contextRequirements = ContextRequirements(
                minPlayerLevel = 1,
                maxPlayerLevel = 50
            ),
            cooldownTicks = 1440 * 60 // 1 day
        )
    }
    
    /**
     * Help NPC find lost item.
     * 
     * Example: "Help Grumble Find His Lost Hammer"
     * Objective: Search location and collect item
     */
    private fun createHelpNPCQuest(): RadiantQuestTemplate {
        return RadiantQuestTemplate(
            templateId = "help_npc_find_item",
            nameTemplate = "Help {npcId} Find {itemId}",
            descriptionTemplate = "{npcId} has lost their {itemId}! Search {locationId} and bring it back.",
            questType = QuestType.SIDE,
            baseDifficulty = QuestDifficulty.EASY,
            objectiveTemplates = listOf(
                ObjectiveTemplate(
                    type = ObjectiveType.REACH,
                    descriptionTemplate = "Search {locationId}",
                    targetParameter = "locationId",
                    countMin = 1,
                    countMax = 1
                ),
                ObjectiveTemplate(
                    type = ObjectiveType.COLLECT,
                    descriptionTemplate = "Collect {itemId}",
                    targetParameter = "itemId",
                    countMin = 1,
                    countMax = 1
                )
            ),
            rewardScaling = RewardScaling(
                baseXP = 60L,
                xpPerLevel = 10L,
                baseSeeds = 35,
                seedsPerLevel = 5,
                baseGlimmerShards = 1,
                glimmerShardsPerLevel = 1,
                itemRewardPool = listOf("pebble", "twig", "acorn"),
                itemRewardChance = 0.5
            ),
            contextRequirements = ContextRequirements(
                minPlayerLevel = 1,
                maxPlayerLevel = 35
            ),
            cooldownTicks = 2880 * 60 // 2 days
        )
    }
    
    // ========================================
    // EXPLORATION QUESTS
    // ========================================
    
    /**
     * Explore and discover new location.
     * 
     * Example: "Explore the Shadow Garden"
     * Objective: Reach undiscovered location
     */
    private fun createExploreLocationQuest(): RadiantQuestTemplate {
        return RadiantQuestTemplate(
            templateId = "explore_location",
            nameTemplate = "Explore {locationId}",
            descriptionTemplate = "Rumors speak of a place called {locationId}. Venture forth and discover it!",
            questType = QuestType.EXPLORATION,
            baseDifficulty = QuestDifficulty.EASY,
            objectiveTemplates = listOf(
                ObjectiveTemplate(
                    type = ObjectiveType.REACH,
                    descriptionTemplate = "Discover and visit {locationId}",
                    targetParameter = "locationId",
                    countMin = 1,
                    countMax = 1
                )
            ),
            rewardScaling = RewardScaling(
                baseXP = 100L,
                xpPerLevel = 20L,
                baseSeeds = 60,
                seedsPerLevel = 10,
                baseGlimmerShards = 2,
                glimmerShardsPerLevel = 2,
                itemRewardPool = listOf("map_fragment", "compass", "explorer_badge"),
                itemRewardChance = 0.6
            ),
            contextRequirements = ContextRequirements(
                minPlayerLevel = 3,
                maxPlayerLevel = 50,
                minAITension = 0,
                maxAITension = 60
            ),
            cooldownTicks = 4320 * 60 // 3 days
        )
    }
    
    /**
     * Collect crafting materials from specific location.
     * 
     * Example: "Gather Moss from the Swamp"
     * Objective: Collect 8-20 materials from biome
     */
    private fun createCollectMaterialsQuest(): RadiantQuestTemplate {
        return RadiantQuestTemplate(
            templateId = "collect_crafting_materials",
            nameTemplate = "Gather {itemId} from {biome}",
            descriptionTemplate = "Crafters need {itemId_count} {itemId} from {biome}. Collect them for a reward!",
            questType = QuestType.FETCH,
            baseDifficulty = QuestDifficulty.EASY,
            objectiveTemplates = listOf(
                ObjectiveTemplate(
                    type = ObjectiveType.COLLECT,
                    descriptionTemplate = "Collect {itemId_count} {itemId} from {biome}",
                    targetParameter = "itemId",
                    countMin = 8,
                    countMax = 20
                )
            ),
            rewardScaling = RewardScaling(
                baseXP = 55L,
                xpPerLevel = 9L,
                baseSeeds = 30,
                seedsPerLevel = 5,
                baseGlimmerShards = 0,
                glimmerShardsPerLevel = 1,
                itemRewardPool = listOf("crafting_recipe", "rare_material"),
                itemRewardChance = 0.3
            ),
            contextRequirements = ContextRequirements(
                minPlayerLevel = 2,
                maxPlayerLevel = 40
            ),
            cooldownTicks = 2160 * 60 // 1.5 days
        )
    }
    
    // ========================================
    // MYSTERY QUESTS
    // ========================================
    
    /**
     * Investigate mysterious location for NPC.
     * 
     * Example: "Investigate Strange Sounds at Old Oak Tree"
     * Objective: Reach location and defeat optional enemy
     */
    private fun createInvestigateMysteryQuest(): RadiantQuestTemplate {
        return RadiantQuestTemplate(
            templateId = "investigate_mystery",
            nameTemplate = "Investigate {locationId}",
            descriptionTemplate = "{npcId} has heard strange sounds coming from {locationId}. Investigate and report back!",
            questType = QuestType.EXPLORATION,
            baseDifficulty = QuestDifficulty.MEDIUM,
            objectiveTemplates = listOf(
                ObjectiveTemplate(
                    type = ObjectiveType.REACH,
                    descriptionTemplate = "Investigate {locationId}",
                    targetParameter = "locationId",
                    countMin = 1,
                    countMax = 1
                ),
                ObjectiveTemplate(
                    type = ObjectiveType.KILL,
                    descriptionTemplate = "Defeat any threats (Optional)",
                    targetParameter = "enemyId",
                    countMin = 1,
                    countMax = 3,
                    isOptional = true
                )
            ),
            rewardScaling = RewardScaling(
                baseXP = 90L,
                xpPerLevel = 18L,
                baseSeeds = 55,
                seedsPerLevel = 9,
                baseGlimmerShards = 2,
                glimmerShardsPerLevel = 2,
                itemRewardPool = listOf("mystery_box", "ancient_relic", "lore_fragment"),
                itemRewardChance = 0.5
            ),
            contextRequirements = ContextRequirements(
                minPlayerLevel = 8,
                maxPlayerLevel = 50,
                minAITension = 40,
                maxAITension = 100
            ),
            cooldownTicks = 5760 * 60 // 4 days
        )
    }
    
    /**
     * Escort NPC to destination.
     * 
     * Example: "Escort Garden Snail to Meadow Path"
     * Objective: Reach location (simulates escort)
     */
    private fun createEscortNPCQuest(): RadiantQuestTemplate {
        return RadiantQuestTemplate(
            templateId = "escort_npc",
            nameTemplate = "Escort {npcId} to {locationId}",
            descriptionTemplate = "{npcId} needs safe passage to {locationId}. Accompany them on their journey!",
            questType = QuestType.SIDE,
            baseDifficulty = QuestDifficulty.MEDIUM,
            objectiveTemplates = listOf(
                ObjectiveTemplate(
                    type = ObjectiveType.REACH,
                    descriptionTemplate = "Safely escort {npcId} to {locationId}",
                    targetParameter = "locationId",
                    countMin = 1,
                    countMax = 1
                )
            ),
            rewardScaling = RewardScaling(
                baseXP = 75L,
                xpPerLevel = 14L,
                baseSeeds = 45,
                seedsPerLevel = 7,
                baseGlimmerShards = 1,
                glimmerShardsPerLevel = 2,
                itemRewardPool = listOf("friendship_token", "escort_badge"),
                itemRewardChance = 0.4
            ),
            contextRequirements = ContextRequirements(
                minPlayerLevel = 5,
                maxPlayerLevel = 50,
                minAITension = 0,
                maxAITension = 70
            ),
            cooldownTicks = 3600 * 60 // 2.5 days
        )
    }
}
