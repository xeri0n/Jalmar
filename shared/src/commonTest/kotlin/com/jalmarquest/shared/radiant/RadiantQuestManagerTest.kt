package com.jalmarquest.shared.radiant

import com.jalmarquest.shared.ai.AIDirector
import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.quest.QuestDifficulty
import com.jalmarquest.shared.quest.QuestType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Comprehensive tests for Radiant Quest System.
 * 
 * Test coverage:
 * - Quest generation (success/failure cases)
 * - Context validation (level, AI tension, conflicts)
 * - Reward scaling (XP, currency, items, difficulty multipliers)
 * - Template selection and parameter substitution
 * - Cooldown management
 * - Target selection
 * - Quest creation from templates
 */
class RadiantQuestManagerTest {
    
    private val manager = RadiantQuestManager()
    
    // Test helpers
    private fun createTestGameState(
        playerLevel: Int = 10,
        aiTension: Int = 50,
        activeQuests: List<String> = emptyList(),
        discoveredLocations: Set<String> = setOf("starting_village", "meadow_path", "old_oak_tree")
    ): GameState {
        return GameState(
            player = Player(id = "test_player", name = "Hero", level = playerLevel),
            aiDirector = AIDirector(tension = aiTension),
            activeQuests = activeQuests,
            discoveredLocations = discoveredLocations
        )
    }
    
    private fun createTestState(): RadiantQuestState {
        return RadiantQuestState()
    }
    
    // ========================================
    // QUEST GENERATION TESTS
    // ========================================
    
    @Test
    fun `generateQuest should succeed with valid template and context`() {
        val state = createTestState()
        val gameState = createTestGameState(playerLevel = 10, aiTension = 50)
        val template = RadiantQuestCatalog.getTemplate("fetch_seeds_for_npc")!!
        
        val result = manager.generateQuest(state, gameState, template, currentTimestamp = 1000)
        
        // Verify success
        assertTrue(result is GenerateQuestResult.Success, "Expected Success but got: $result")
        val success = result as GenerateQuestResult.Success
        
        assertNotNull(success.generatedQuestId)
        assertTrue(success.generatedQuestId.startsWith("radiant_fetch_seeds_for_npc"),
            "Quest ID should start with template ID, got: ${success.generatedQuestId}")
        
        // Check all required parameters
        assertTrue(success.parameters.containsKey("playerName"), 
            "Missing playerName parameter. Keys: ${success.parameters.keys}")
        assertEquals("Hero", success.parameters["playerName"])
        
        assertTrue(success.parameters.containsKey("npcId"),
            "Missing npcId parameter. Keys: ${success.parameters.keys}")
        assertTrue(success.parameters.containsKey("itemId"),
            "Missing itemId parameter. Keys: ${success.parameters.keys}")
        assertTrue(success.parameters.containsKey("itemId_count"),
            "Missing itemId_count parameter. Keys: ${success.parameters.keys}")
    }
    
    @Test
    fun `generateQuest should reject template on cooldown`() {
        val cooldownExpiry = 5000L
        val state = RadiantQuestState(
            templateCooldowns = mapOf("fetch_seeds_for_npc" to cooldownExpiry)
        )
        val gameState = createTestGameState()
        val template = RadiantQuestCatalog.getTemplate("fetch_seeds_for_npc")!!
        
        val result = manager.generateQuest(state, gameState, template, currentTimestamp = 3000)
        
        assertTrue(result is GenerateQuestResult.Failure)
        assertEquals(GenerationFailure.TEMPLATE_ON_COOLDOWN, (result as GenerateQuestResult.Failure).reason)
    }
    
    @Test
    fun `generateQuest should succeed after cooldown expires`() {
        val cooldownExpiry = 5000L
        val state = RadiantQuestState(
            templateCooldowns = mapOf("fetch_seeds_for_npc" to cooldownExpiry)
        )
        val gameState = createTestGameState()
        val template = RadiantQuestCatalog.getTemplate("fetch_seeds_for_npc")!!
        
        val result = manager.generateQuest(state, gameState, template, currentTimestamp = 6000)
        
        assertTrue(result is GenerateQuestResult.Success)
    }
    
    @Test
    fun `generateQuest should update state with cooldown and generation count`() {
        val state = createTestState()
        val gameState = createTestGameState()
        val template = RadiantQuestCatalog.getTemplate("fetch_seeds_for_npc")!!
        val currentTime = 10000L
        
        val result = manager.generateQuest(state, gameState, template, currentTimestamp = currentTime)
        
        assertTrue(result is GenerateQuestResult.Success)
        val success = result as GenerateQuestResult.Success
        
        // Check cooldown set
        val expectedCooldownExpiry = currentTime + template.cooldownTicks
        assertEquals(expectedCooldownExpiry, success.state.templateCooldowns["fetch_seeds_for_npc"])
        
        // Check generation count incremented
        assertEquals(1, success.state.generationCount["fetch_seeds_for_npc"])
        
        // Check quest registered
        assertTrue(success.state.generatedQuests.containsKey(success.generatedQuestId))
        assertEquals("fetch_seeds_for_npc", success.state.generatedQuests[success.generatedQuestId])
    }
    
    // ========================================
    // CONTEXT VALIDATION TESTS
    // ========================================
    
    @Test
    fun `validateContext should accept valid context`() {
        val gameState = createTestGameState(playerLevel = 10, aiTension = 50)
        val template = RadiantQuestCatalog.getTemplate("fetch_seeds_for_npc")!!
        
        val result = manager.validateContext(template, gameState)
        
        assertTrue(result is ContextValidationResult.Valid)
    }
    
    @Test
    fun `validateContext should reject player level too low`() {
        val gameState = createTestGameState(playerLevel = 1, aiTension = 50)
        val template = RadiantQuestCatalog.getTemplate("clear_location_enemies")!! // Requires level 5+
        
        val result = manager.validateContext(template, gameState)
        
        assertTrue(result is ContextValidationResult.Invalid)
        assertTrue((result as ContextValidationResult.Invalid).reason.contains("level"))
    }
    
    @Test
    fun `validateContext should reject player level too high`() {
        val gameState = createTestGameState(playerLevel = 50, aiTension = 50)
        val template = RadiantQuestTemplate(
            templateId = "test_low_level",
            nameTemplate = "Test",
            descriptionTemplate = "Test",
            questType = QuestType.FETCH,
            baseDifficulty = QuestDifficulty.TRIVIAL,
            objectiveTemplates = listOf(
                ObjectiveTemplate(
                    type = com.jalmarquest.shared.quest.ObjectiveType.COLLECT,
                    descriptionTemplate = "Test",
                    targetParameter = "itemId"
                )
            ),
            rewardScaling = RewardScaling(
                baseXP = 10L,
                xpPerLevel = 1L,
                baseSeeds = 5,
                seedsPerLevel = 1
            ),
            contextRequirements = ContextRequirements(
                minPlayerLevel = 1,
                maxPlayerLevel = 10 // Max level 10
            )
        )
        
        val result = manager.validateContext(template, gameState)
        
        assertTrue(result is ContextValidationResult.Invalid)
        assertTrue((result as ContextValidationResult.Invalid).reason.contains("level"))
    }
    
    @Test
    fun `validateContext should reject AI tension too low`() {
        val gameState = createTestGameState(playerLevel = 10, aiTension = 10)
        val template = RadiantQuestCatalog.getTemplate("clear_location_enemies")!! // Requires 30+ tension
        
        val result = manager.validateContext(template, gameState)
        
        assertTrue(result is ContextValidationResult.Invalid)
        assertTrue((result as ContextValidationResult.Invalid).reason.contains("tension"))
    }
    
    @Test
    fun `validateContext should reject conflicting active quest`() {
        val conflictingQuestId = "main_quest_1"
        val gameState = createTestGameState(activeQuests = listOf(conflictingQuestId))
        val template = RadiantQuestTemplate(
            templateId = "test_conflict",
            nameTemplate = "Test",
            descriptionTemplate = "Test",
            questType = QuestType.FETCH,
            baseDifficulty = QuestDifficulty.EASY,
            objectiveTemplates = listOf(
                ObjectiveTemplate(
                    type = com.jalmarquest.shared.quest.ObjectiveType.COLLECT,
                    descriptionTemplate = "Test",
                    targetParameter = "itemId"
                )
            ),
            rewardScaling = RewardScaling(
                baseXP = 10L,
                xpPerLevel = 1L,
                baseSeeds = 5,
                seedsPerLevel = 1
            ),
            contextRequirements = ContextRequirements(
                excludedIfQuestsActive = listOf(conflictingQuestId)
            )
        )
        
        val result = manager.validateContext(template, gameState)
        
        assertTrue(result is ContextValidationResult.Invalid)
        assertTrue((result as ContextValidationResult.Invalid).reason.contains("quest"))
    }
    
    @Test
    fun `validateContext should reject missing required location`() {
        val gameState = createTestGameState(discoveredLocations = setOf("starting_village"))
        val template = RadiantQuestTemplate(
            templateId = "test_location_req",
            nameTemplate = "Test",
            descriptionTemplate = "Test",
            questType = QuestType.EXPLORATION,
            baseDifficulty = QuestDifficulty.EASY,
            objectiveTemplates = listOf(
                ObjectiveTemplate(
                    type = com.jalmarquest.shared.quest.ObjectiveType.REACH,
                    descriptionTemplate = "Test",
                    targetParameter = "locationId"
                )
            ),
            rewardScaling = RewardScaling(
                baseXP = 10L,
                xpPerLevel = 1L,
                baseSeeds = 5,
                seedsPerLevel = 1
            ),
            contextRequirements = ContextRequirements(
                requiredLocations = listOf("shadow_garden") // Not discovered
            )
        )
        
        val result = manager.validateContext(template, gameState)
        
        assertTrue(result is ContextValidationResult.Invalid)
        assertTrue((result as ContextValidationResult.Invalid).reason.contains("location"))
    }
    
    // ========================================
    // REWARD SCALING TESTS
    // ========================================
    
    @Test
    fun `calculateRewards should scale XP by player level`() {
        val scaling = RewardScaling(
            baseXP = 50L,
            xpPerLevel = 10L,
            baseSeeds = 20,
            seedsPerLevel = 5
        )
        
        val rewardsLevel1 = manager.calculateRewards(scaling, playerLevel = 1, QuestDifficulty.MEDIUM)
        val rewardsLevel10 = manager.calculateRewards(scaling, playerLevel = 10, QuestDifficulty.MEDIUM)
        
        assertEquals(60L, rewardsLevel1.xp) // (50 + 10*1) * 1.0 = 60
        assertEquals(150L, rewardsLevel10.xp) // (50 + 10*10) * 1.0 = 150
    }
    
    @Test
    fun `calculateRewards should apply difficulty multipliers correctly`() {
        val scaling = RewardScaling(
            baseXP = 100L,
            xpPerLevel = 0L,
            baseSeeds = 50,
            seedsPerLevel = 0
        )
        val playerLevel = 1
        
        val trivial = manager.calculateRewards(scaling, playerLevel, QuestDifficulty.TRIVIAL)
        val easy = manager.calculateRewards(scaling, playerLevel, QuestDifficulty.EASY)
        val medium = manager.calculateRewards(scaling, playerLevel, QuestDifficulty.MEDIUM)
        val hard = manager.calculateRewards(scaling, playerLevel, QuestDifficulty.HARD)
        val expert = manager.calculateRewards(scaling, playerLevel, QuestDifficulty.EXPERT)
        val legendary = manager.calculateRewards(scaling, playerLevel, QuestDifficulty.LEGENDARY)
        
        assertEquals(50L, trivial.xp) // 100 * 0.5
        assertEquals(75L, easy.xp) // 100 * 0.75
        assertEquals(100L, medium.xp) // 100 * 1.0
        assertEquals(150L, hard.xp) // 100 * 1.5
        assertEquals(200L, expert.xp) // 100 * 2.0
        assertEquals(300L, legendary.xp) // 100 * 3.0
        
        assertEquals(25, trivial.seeds)
        assertEquals(37, easy.seeds) // Rounds down
        assertEquals(50, medium.seeds)
        assertEquals(75, hard.seeds)
        assertEquals(100, expert.seeds)
        assertEquals(150, legendary.seeds)
    }
    
    @Test
    fun `calculateRewards should scale glimmer shards correctly`() {
        val scaling = RewardScaling(
            baseXP = 50L,
            xpPerLevel = 10L,
            baseSeeds = 20,
            seedsPerLevel = 5,
            baseGlimmerShards = 2,
            glimmerShardsPerLevel = 1
        )
        
        val rewards = manager.calculateRewards(scaling, playerLevel = 10, QuestDifficulty.HARD)
        
        assertEquals(18, rewards.glimmerShards) // (2 + 1*10) * 1.5 = 18
    }
    
    @Test
    fun `calculateRewards should randomly grant item rewards based on chance`() {
        val scaling = RewardScaling(
            baseXP = 50L,
            xpPerLevel = 10L,
            baseSeeds = 20,
            seedsPerLevel = 5,
            itemRewardPool = listOf("item1", "item2", "item3"),
            itemRewardChance = 1.0 // 100% chance for deterministic test
        )
        
        val rewards = manager.calculateRewards(scaling, playerLevel = 1, QuestDifficulty.MEDIUM)
        
        assertEquals(1, rewards.items.size)
        assertTrue(rewards.items[0] in listOf("item1", "item2", "item3"))
    }
    
    @Test
    fun `calculateRewards should not grant items if pool is empty`() {
        val scaling = RewardScaling(
            baseXP = 50L,
            xpPerLevel = 10L,
            baseSeeds = 20,
            seedsPerLevel = 5,
            itemRewardPool = emptyList(),
            itemRewardChance = 1.0
        )
        
        val rewards = manager.calculateRewards(scaling, playerLevel = 1, QuestDifficulty.MEDIUM)
        
        assertTrue(rewards.items.isEmpty())
    }
    
    // ========================================
    // TEMPLATE FILLING TESTS
    // ========================================
    
    @Test
    fun `fillTemplate should replace all parameter placeholders`() {
        val template = "Collect {count} {itemId} for {npcId}"
        val parameters = mapOf(
            "count" to "10",
            "itemId" to "seeds",
            "npcId" to "Grumble Forgepaw"
        )
        
        val result = manager.fillTemplate(template, parameters)
        
        assertEquals("Collect 10 seeds for Grumble Forgepaw", result)
    }
    
    @Test
    fun `fillTemplate should be case insensitive`() {
        val template = "Collect {COUNT} {ItemId}"
        val parameters = mapOf(
            "count" to "5",
            "itemid" to "twigs"
        )
        
        val result = manager.fillTemplate(template, parameters)
        
        assertEquals("Collect 5 twigs", result)
    }
    
    @Test
    fun `fillTemplate should handle missing parameters gracefully`() {
        val template = "Collect {count} {itemId}"
        val parameters = mapOf("count" to "10")
        
        val result = manager.fillTemplate(template, parameters)
        
        assertEquals("Collect 10 {itemId}", result) // Unfilled parameter remains
    }
    
    // ========================================
    // TARGET SELECTION TESTS
    // ========================================
    
    @Test
    fun `selectTarget should return valid NPC ID`() {
        val gameState = createTestGameState()
        val requirements = ContextRequirements()
        
        val target = manager.selectTarget("npcId", gameState, requirements)
        
        assertNotNull(target)
        assertTrue(target in listOf("grumble_forgepaw", "old_quill", "merchant_beetle", "garden_snail"))
    }
    
    @Test
    fun `selectTarget should return valid item ID`() {
        val gameState = createTestGameState()
        val requirements = ContextRequirements()
        
        val target = manager.selectTarget("itemId", gameState, requirements)
        
        assertNotNull(target)
        assertTrue(target in listOf("seeds", "twig", "pebble", "acorn", "berry", "moss", "feather"))
    }
    
    @Test
    fun `selectTarget should prefer required items when specified`() {
        val gameState = createTestGameState()
        val requirements = ContextRequirements(requiredItems = listOf("special_item"))
        
        val target = manager.selectTarget("itemId", gameState, requirements)
        
        assertEquals("special_item", target)
    }
    
    @Test
    fun `selectTarget should return discovered location`() {
        val gameState = createTestGameState(discoveredLocations = setOf("meadow_path", "old_oak_tree"))
        val requirements = ContextRequirements()
        
        val target = manager.selectTarget("locationId", gameState, requirements)
        
        assertNotNull(target)
        assertTrue(target in setOf("meadow_path", "old_oak_tree"))
    }
    
    @Test
    fun `selectTarget should prefer required locations when specified`() {
        val gameState = createTestGameState()
        val requirements = ContextRequirements(requiredLocations = listOf("shadow_garden"))
        
        val target = manager.selectTarget("locationId", gameState, requirements)
        
        assertEquals("shadow_garden", target)
    }
    
    @Test
    fun `selectTarget should return valid enemy ID`() {
        val gameState = createTestGameState()
        val requirements = ContextRequirements()
        
        val target = manager.selectTarget("enemyId", gameState, requirements)
        
        assertNotNull(target)
        assertTrue(target in listOf("beetle", "ant", "spider", "moth", "grasshopper"))
    }
    
    @Test
    fun `selectTarget should return valid biome`() {
        val gameState = createTestGameState()
        val requirements = ContextRequirements()
        
        val target = manager.selectTarget("biome", gameState, requirements)
        
        assertNotNull(target)
        assertTrue(target in listOf("GRASSLAND", "FOREST", "SWAMP", "MEADOW"))
    }
    
    @Test
    fun `selectTarget should return null for unknown parameter`() {
        val gameState = createTestGameState()
        val requirements = ContextRequirements()
        
        val target = manager.selectTarget("unknownParam", gameState, requirements)
        
        assertNull(target)
    }
    
    // ========================================
    // QUEST CREATION TESTS
    // ========================================
    
    @Test
    fun `createQuest should generate valid Quest instance`() {
        val template = RadiantQuestCatalog.getTemplate("fetch_seeds_for_npc")!!
        val questId = "radiant_test_123"
        val parameters = mapOf(
            "npcId" to "Grumble Forgepaw",
            "itemId" to "seeds",
            "itemId_count" to "15",
            "playerName" to "Hero"
        )
        val playerLevel = 10
        
        val quest = manager.createQuest(template, questId, parameters, playerLevel)
        
        assertEquals(questId, quest.id)
        assertEquals("Gather Seeds for Grumble Forgepaw", quest.name)
        assertTrue(quest.description.contains("Grumble Forgepaw"))
        assertTrue(quest.description.contains("15"))
        assertTrue(quest.description.contains("seeds"))
        assertEquals(QuestType.FETCH, quest.questType)
        assertEquals(QuestDifficulty.EASY, quest.difficulty)
        assertEquals(1, quest.objectives.size)
        assertEquals(15, quest.objectives[0].targetCount)
        assertEquals("seeds", quest.objectives[0].targetId)
        assertEquals(playerLevel, quest.level)
        assertEquals("Grumble Forgepaw", quest.giver)
    }
    
    // ========================================
    // COOLDOWN MANAGEMENT TESTS
    // ========================================
    
    @Test
    fun `isOnCooldown should return true when cooldown active`() {
        val state = RadiantQuestState(
            templateCooldowns = mapOf("fetch_seeds_for_npc" to 10000L)
        )
        
        val result = manager.isOnCooldown(state, "fetch_seeds_for_npc", currentTimestamp = 5000L)
        
        assertTrue(result)
    }
    
    @Test
    fun `isOnCooldown should return false when cooldown expired`() {
        val state = RadiantQuestState(
            templateCooldowns = mapOf("fetch_seeds_for_npc" to 10000L)
        )
        
        val result = manager.isOnCooldown(state, "fetch_seeds_for_npc", currentTimestamp = 15000L)
        
        assertFalse(result)
    }
    
    @Test
    fun `isOnCooldown should return false when template never used`() {
        val state = RadiantQuestState()
        
        val result = manager.isOnCooldown(state, "fetch_seeds_for_npc", currentTimestamp = 5000L)
        
        assertFalse(result)
    }
    
    @Test
    fun `getGenerationCount should return zero for unused template`() {
        val state = RadiantQuestState()
        
        val count = manager.getGenerationCount(state, "fetch_seeds_for_npc")
        
        assertEquals(0, count)
    }
    
    @Test
    fun `getGenerationCount should return correct count`() {
        val state = RadiantQuestState(
            generationCount = mapOf("fetch_seeds_for_npc" to 5)
        )
        
        val count = manager.getGenerationCount(state, "fetch_seeds_for_npc")
        
        assertEquals(5, count)
    }
    
    // ========================================
    // CATALOG TESTS
    // ========================================
    
    @Test
    fun `RadiantQuestCatalog should provide all 10 templates`() {
        val templates = RadiantQuestCatalog.getAllTemplates()
        
        assertEquals(10, templates.size)
        assertNotNull(templates["fetch_seeds_for_npc"])
        assertNotNull(templates["gather_items_for_npc"])
        assertNotNull(templates["clear_location_enemies"])
        assertNotNull(templates["hunt_enemy_type"])
        assertNotNull(templates["deliver_message"])
        assertNotNull(templates["help_npc_find_item"])
        assertNotNull(templates["explore_location"])
        assertNotNull(templates["collect_crafting_materials"])
        assertNotNull(templates["investigate_mystery"])
        assertNotNull(templates["escort_npc"])
    }
    
    @Test
    fun `RadiantQuestCatalog getTemplate should return correct template`() {
        val template = RadiantQuestCatalog.getTemplate("fetch_seeds_for_npc")
        
        assertNotNull(template)
        assertEquals("fetch_seeds_for_npc", template.templateId)
        assertEquals(QuestType.FETCH, template.questType)
    }
    
    @Test
    fun `RadiantQuestCatalog getTemplate should return null for unknown ID`() {
        val template = RadiantQuestCatalog.getTemplate("nonexistent_template")
        
        assertNull(template)
    }
    
    @Test
    fun `RadiantQuestCatalog getTemplatesByType should filter correctly`() {
        val fetchQuests = RadiantQuestCatalog.getTemplatesByType(QuestType.FETCH)
        val combatQuests = RadiantQuestCatalog.getTemplatesByType(QuestType.COMBAT)
        val explorationQuests = RadiantQuestCatalog.getTemplatesByType(QuestType.EXPLORATION)
        
        assertEquals(3, fetchQuests.size) // fetch_seeds, gather_items, collect_materials
        assertEquals(2, combatQuests.size) // clear_location, hunt_enemy
        assertEquals(2, explorationQuests.size) // explore_location, investigate_mystery
    }
    
    @Test
    fun `getTemplatesByType from manager should return correct templates`() {
        val fetchTemplates = manager.getTemplatesByType(QuestType.FETCH)
        
        assertEquals(3, fetchTemplates.size)
        assertTrue(fetchTemplates.any { it.templateId == "fetch_seeds_for_npc" })
        assertTrue(fetchTemplates.any { it.templateId == "gather_items_for_npc" })
        assertTrue(fetchTemplates.any { it.templateId == "collect_crafting_materials" })
    }
}
