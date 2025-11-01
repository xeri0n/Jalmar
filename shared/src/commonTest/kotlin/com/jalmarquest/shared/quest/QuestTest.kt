package com.jalmarquest.shared.quest

import com.jalmarquest.shared.inventory.Inventory
import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.model.PlayerStats
import com.jalmarquest.shared.model.Position
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class QuestTest {
    
    // ========== Quest Data Model Tests ==========
    
    @Test
    fun `quest with all required objectives complete should be complete`() {
        val quest = Quest(
            id = "test_quest",
            name = "Test Quest",
            description = "Test description",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.EASY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Kill 5 enemies",
                    targetId = "enemy_1",
                    targetCount = 5,
                    currentProgress = 5
                )
            ),
            rewards = QuestReward(xp = 100),
            level = 1,
            giver = "npc_1"
        )
        
        assertTrue(quest.isComplete())
    }
    
    @Test
    fun `quest with incomplete objectives should not be complete`() {
        val quest = Quest(
            id = "test_quest",
            name = "Test Quest",
            description = "Test description",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.EASY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Kill 5 enemies",
                    targetId = "enemy_1",
                    targetCount = 5,
                    currentProgress = 3
                )
            ),
            rewards = QuestReward(xp = 100),
            level = 1,
            giver = "npc_1"
        )
        
        assertFalse(quest.isComplete())
    }
    
    @Test
    fun `quest with optional incomplete objectives should be complete`() {
        val quest = Quest(
            id = "test_quest",
            name = "Test Quest",
            description = "Test description",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.EASY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Kill 5 enemies",
                    targetId = "enemy_1",
                    targetCount = 5,
                    currentProgress = 5
                ),
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Collect 10 items (optional)",
                    targetId = "item_1",
                    targetCount = 10,
                    currentProgress = 0,
                    isOptional = true
                )
            ),
            rewards = QuestReward(xp = 100),
            level = 1,
            giver = "npc_1"
        )
        
        assertTrue(quest.isComplete())
    }
    
    @Test
    fun `quest progress percentage should calculate correctly`() {
        val quest = Quest(
            id = "test_quest",
            name = "Test Quest",
            description = "Test description",
            questType = QuestType.SIDE,
            difficulty = QuestDifficulty.EASY,
            objectives = listOf(
                QuestObjective(
                    type = ObjectiveType.KILL,
                    description = "Kill 10 enemies",
                    targetId = "enemy_1",
                    targetCount = 10,
                    currentProgress = 5
                ),
                QuestObjective(
                    type = ObjectiveType.COLLECT,
                    description = "Collect 20 items",
                    targetId = "item_1",
                    targetCount = 20,
                    currentProgress = 10
                )
            ),
            rewards = QuestReward(xp = 100),
            level = 1,
            giver = "npc_1"
        )
        
        // (5/10 + 10/20) / 2 = (0.5 + 0.5) / 2 = 0.5
        assertEquals(0.5f, quest.progressPercentage())
    }
    
    @Test
    fun `objective isComplete should return true when progress meets target`() {
        val objective = QuestObjective(
            type = ObjectiveType.KILL,
            description = "Kill 5 enemies",
            targetId = "enemy_1",
            targetCount = 5,
            currentProgress = 5
        )
        
        assertTrue(objective.isComplete())
    }
    
    @Test
    fun `objective isComplete should return true when progress exceeds target`() {
        val objective = QuestObjective(
            type = ObjectiveType.KILL,
            description = "Kill 5 enemies",
            targetId = "enemy_1",
            targetCount = 5,
            currentProgress = 7
        )
        
        assertTrue(objective.isComplete())
    }
    
    @Test
    fun `objective progressString should format correctly`() {
        val objective = QuestObjective(
            type = ObjectiveType.KILL,
            description = "Kill 5 enemies",
            targetId = "enemy_1",
            targetCount = 5,
            currentProgress = 3
        )
        
        assertEquals("3/5", objective.progressString())
    }
    
    @Test
    fun `quest reward hasRewards should return true when rewards present`() {
        val reward = QuestReward(xp = 100, seeds = 50)
        assertTrue(reward.hasRewards())
    }
    
    @Test
    fun `quest reward hasRewards should return false when no rewards`() {
        val reward = QuestReward()
        assertFalse(reward.hasRewards())
    }
    
    // ========== QuestCatalog Tests ==========
    
    @Test
    fun `QuestCatalog should contain expected quest count`() {
        // Verify catalog is not empty
        assertTrue(QuestCatalog.allQuests.isNotEmpty(), "Quest catalog should not be empty")
        // Log actual count for debugging
        val actualCount = QuestCatalog.getTotalQuestCount()
        println("Actual quest count: $actualCount")
        // Should have at least 10 quests
        assertTrue(actualCount >= 10, "Should have at least 10 quests, found $actualCount")
    }
    
    @Test
    fun `QuestCatalog should retrieve quest by ID`() {
        val quest = QuestCatalog.getQuest("tutorial_first_steps")
        
        assertEquals("tutorial_first_steps", quest?.id)
        assertEquals("First Steps", quest?.name)
        assertEquals(QuestType.TUTORIAL, quest?.questType)
    }
    
    @Test
    fun `QuestCatalog should return null for invalid quest ID`() {
        val quest = QuestCatalog.getQuest("nonexistent_quest")
        assertEquals(null, quest)
    }
    
    @Test
    fun `QuestCatalog should filter quests by type`() {
        val tutorialQuests = QuestCatalog.getQuestsByType(QuestType.TUTORIAL)
        
        assertEquals(3, tutorialQuests.size)
        assertTrue(tutorialQuests.all { it.questType == QuestType.TUTORIAL })
    }
    
    @Test
    fun `QuestCatalog should filter quests by giver`() {
        val elderQuests = QuestCatalog.getQuestsByGiver("elder_quail")
        
        assertTrue(elderQuests.size >= 3)
        assertTrue(elderQuests.all { it.giver == "elder_quail" })
    }
    
    @Test
    fun `QuestCatalog should filter quests by player level`() {
        val level5Quests = QuestCatalog.getQuestsForLevel(5)
        
        // Should include quests level 1-10 (within 5 levels above)
        assertTrue(level5Quests.isNotEmpty())
        assertTrue(level5Quests.all { it.level <= 5 + 5 && it.level <= 5 })
    }
    
    @Test
    fun `QuestCatalog should filter quests by difficulty`() {
        val easyQuests = QuestCatalog.getQuestsByDifficulty(QuestDifficulty.EASY)
        
        assertTrue(easyQuests.isNotEmpty())
        assertTrue(easyQuests.all { it.difficulty == QuestDifficulty.EASY })
    }
    
    @Test
    fun `QuestCatalog should have unique quest IDs`() {
        val allIds = QuestCatalog.allQuests.map { it.id }
        val uniqueIds = allIds.toSet()
        
        assertEquals(allIds.size, uniqueIds.size, "Quest IDs should be unique")
    }
    
    @Test
    fun `QuestCatalog validateCatalog should not throw for valid catalog`() {
        // Should not throw exception
        QuestCatalog.validateCatalog()
    }
    
    // ========== QuestManager Tests ==========
    
    private fun createTestGameState(level: Int = 1, completedQuests: Set<String> = emptySet()): GameState {
        return GameState(
            player = Player(
                id = "test_player",
                name = "Test Hero",
                level = level,
                stats = PlayerStats(),
                position = Position(0, 0, "starting_village"),
                inventory = Inventory()
            ),
            completedQuests = completedQuests,
            activeQuests = emptyList()
        )
    }
    
    @Test
    fun `acceptQuest should succeed for valid quest`() {
        val manager = QuestManager()
        val gameState = createTestGameState(level = 1)
        
        val result = manager.acceptQuest(gameState, "tutorial_first_steps")
        
        assertIs<QuestManager.AcceptQuestResult.Success>(result)
        assertTrue(result.gameState.activeQuests.contains("tutorial_first_steps"))
    }
    
    @Test
    fun `acceptQuest should fail for nonexistent quest`() {
        val manager = QuestManager()
        val gameState = createTestGameState()
        
        val result = manager.acceptQuest(gameState, "nonexistent_quest")
        
        assertIs<QuestManager.AcceptQuestResult.Failure>(result)
        assertEquals(QuestManager.AcceptQuestFailure.QUEST_NOT_FOUND, result.reason)
    }
    
    @Test
    fun `acceptQuest should fail if already active`() {
        val manager = QuestManager()
        val gameState = createTestGameState().copy(
            activeQuests = listOf("tutorial_first_steps")
        )
        
        val result = manager.acceptQuest(gameState, "tutorial_first_steps")
        
        assertIs<QuestManager.AcceptQuestResult.Failure>(result)
        assertEquals(QuestManager.AcceptQuestFailure.ALREADY_ACTIVE, result.reason)
    }
    
    @Test
    fun `acceptQuest should fail if already completed`() {
        val manager = QuestManager()
        val gameState = createTestGameState(completedQuests = setOf("tutorial_first_steps"))
        
        val result = manager.acceptQuest(gameState, "tutorial_first_steps")
        
        assertIs<QuestManager.AcceptQuestResult.Failure>(result)
        assertEquals(QuestManager.AcceptQuestFailure.ALREADY_COMPLETED, result.reason)
    }
    
    @Test
    fun `acceptQuest should fail if level too low`() {
        val manager = QuestManager()
        val gameState = createTestGameState(level = 1)
        
        val result = manager.acceptQuest(gameState, "combat_beetle_brawl") // Level 6 quest
        
        assertIs<QuestManager.AcceptQuestResult.Failure>(result)
        assertEquals(QuestManager.AcceptQuestFailure.LEVEL_TOO_LOW, result.reason)
    }
    
    @Test
    fun `acceptQuest should fail if prerequisites not met`() {
        val manager = QuestManager()
        val gameState = createTestGameState(level = 5)
        
        val result = manager.acceptQuest(gameState, "tutorial_first_combat") // Requires tutorial_first_steps
        
        assertIs<QuestManager.AcceptQuestResult.Failure>(result)
        assertEquals(QuestManager.AcceptQuestFailure.PREREQUISITES_NOT_MET, result.reason)
    }
    
    @Test
    fun `acceptQuest should succeed if prerequisites are met`() {
        val manager = QuestManager()
        val gameState = createTestGameState(
            level = 5,
            completedQuests = setOf("tutorial_first_steps")
        )
        
        val result = manager.acceptQuest(gameState, "tutorial_first_combat")
        
        assertIs<QuestManager.AcceptQuestResult.Success>(result)
        assertTrue(result.gameState.activeQuests.contains("tutorial_first_combat"))
    }
    
    @Test
    fun `updateObjective should progress matching objectives`() {
        val manager = QuestManager()
        val gameState = createTestGameState(level = 1).copy(
            activeQuests = listOf("tutorial_first_steps")
        )
        
        val result = manager.updateObjective(
            gameState,
            ObjectiveType.REACH,
            "starting_village",
            1
        )
        
        assertIs<QuestManager.UpdateObjectiveResult.Success>(result)
        assertEquals(1, result.questsProgressed.size)
        assertTrue(result.questsProgressed.contains("tutorial_first_steps"))
    }
    
    @Test
    fun `updateObjective should auto-complete quests with autoComplete enabled`() {
        val manager = QuestManager()
        val gameState = createTestGameState(level = 1).copy(
            activeQuests = listOf("tutorial_first_steps")
        )
        
        val result = manager.updateObjective(
            gameState,
            ObjectiveType.REACH,
            "starting_village",
            1
        )
        
        assertIs<QuestManager.UpdateObjectiveResult.Success>(result)
        // Quest should be auto-completed (moved from active to completed)
        assertTrue(result.gameState.completedQuests.contains("tutorial_first_steps"))
        assertFalse(result.gameState.activeQuests.contains("tutorial_first_steps"))
    }
    
    @Test
    fun `updateObjective should return NoChange if no active quests`() {
        val manager = QuestManager()
        val gameState = createTestGameState()
        
        val result = manager.updateObjective(
            gameState,
            ObjectiveType.KILL,
            "enemy_1",
            1
        )
        
        assertIs<QuestManager.UpdateObjectiveResult.NoChange>(result)
    }
    
    @Test
    fun `updateObjective should handle wildcard targetId`() {
        val manager = QuestManager()
        val gameState = createTestGameState(level = 1).copy(
            activeQuests = listOf("tutorial_first_combat") // Has empty targetId (any enemy)
        )
        
        val result = manager.updateObjective(
            gameState,
            ObjectiveType.KILL,
            "grasshopper",
            1
        )
        
        assertIs<QuestManager.UpdateObjectiveResult.Success>(result)
        assertEquals(1, result.questsProgressed.size)
    }
    
    @Test
    fun `getActiveQuests should return all active quests`() {
        val manager = QuestManager()
        val gameState = createTestGameState(level = 1).copy(
            activeQuests = listOf("tutorial_first_steps", "tutorial_inventory")
        )
        
        val activeQuests = manager.getActiveQuests(gameState)
        
        assertEquals(2, activeQuests.size)
        assertTrue(activeQuests.any { it.id == "tutorial_first_steps" })
        assertTrue(activeQuests.any { it.id == "tutorial_inventory" })
    }
    
    @Test
    fun `getCompletedQuests should return all completed quests`() {
        val manager = QuestManager()
        val gameState = createTestGameState(
            completedQuests = setOf("tutorial_first_steps", "tutorial_inventory")
        )
        
        val completedQuests = manager.getCompletedQuests(gameState)
        
        assertEquals(2, completedQuests.size)
        assertTrue(completedQuests.any { it.id == "tutorial_first_steps" })
        assertTrue(completedQuests.any { it.id == "tutorial_inventory" })
    }
    
    @Test
    fun `getAvailableQuests should return eligible quests`() {
        val manager = QuestManager()
        val gameState = createTestGameState(
            level = 5,
            completedQuests = setOf("tutorial_first_steps")
        )
        
        val availableQuests = manager.getAvailableQuests(gameState)
        
        // Should include quests with prerequisites met, correct level, not active/completed
        assertTrue(availableQuests.any { it.id == "tutorial_first_combat" })
        assertTrue(availableQuests.any { it.id == "tutorial_inventory" })
        assertFalse(availableQuests.any { it.id == "tutorial_first_steps" }) // Already completed
    }
    
    @Test
    fun `isQuestAvailable should return true for eligible quest`() {
        val manager = QuestManager()
        val gameState = createTestGameState(
            level = 5,
            completedQuests = setOf("tutorial_first_steps")
        )
        
        assertTrue(manager.isQuestAvailable(gameState, "tutorial_first_combat"))
    }
    
    @Test
    fun `isQuestAvailable should return false for ineligible quest`() {
        val manager = QuestManager()
        val gameState = createTestGameState(level = 1)
        
        assertFalse(manager.isQuestAvailable(gameState, "tutorial_first_combat")) // Prerequisites not met
        assertFalse(manager.isQuestAvailable(gameState, "combat_beetle_brawl")) // Level too low
    }
    
    @Test
    fun `turnInQuest should grant XP rewards`() {
        val manager = QuestManager()
        val gameState = createTestGameState(level = 1).copy(
            completedQuests = setOf("tutorial_first_steps")
        )
        
        val result = manager.turnInQuest(gameState, "tutorial_first_steps")
        
        assertIs<QuestManager.TurnInQuestResult.Success>(result)
        assertEquals(50, result.rewards.xp)
        assertEquals(50, result.player.experience)
    }
    
    @Test
    fun `turnInQuest should grant currency rewards`() {
        val manager = QuestManager()
        val gameState = createTestGameState(level = 1).copy(
            completedQuests = setOf("tutorial_first_steps")
        )
        
        val result = manager.turnInQuest(gameState, "tutorial_first_steps")
        
        assertIs<QuestManager.TurnInQuestResult.Success>(result)
        assertEquals(10, result.rewards.seeds)
        assertEquals(10, result.player.seeds)
    }
    
    @Test
    fun `turnInQuest should unlock recipes`() {
        val manager = QuestManager()
        val gameState = createTestGameState(level = 8).copy(
            completedQuests = setOf("side_spider_silk")
        )
        
        val result = manager.turnInQuest(gameState, "side_spider_silk")
        
        assertIs<QuestManager.TurnInQuestResult.Success>(result)
        assertTrue(result.gameState.unlockedRecipes.contains("silk_armor"))
    }
    
    @Test
    fun `turnInQuest should fail for quest not completed`() {
        val manager = QuestManager()
        val gameState = createTestGameState(level = 1)
        
        val result = manager.turnInQuest(gameState, "tutorial_first_steps")
        
        assertIs<QuestManager.TurnInQuestResult.Failure>(result)
        assertEquals(QuestManager.TurnInQuestFailure.NOT_COMPLETED, result.reason)
    }
    
    @Test
    fun `turnInQuest should fail for nonexistent quest`() {
        val manager = QuestManager()
        val gameState = createTestGameState()
        
        val result = manager.turnInQuest(gameState, "nonexistent_quest")
        
        assertIs<QuestManager.TurnInQuestResult.Failure>(result)
        assertEquals(QuestManager.TurnInQuestFailure.QUEST_NOT_FOUND, result.reason)
    }
}
