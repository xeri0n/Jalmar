package com.jalmarquest.shared.dialogue

import com.jalmarquest.shared.inventory.Inventory
import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.model.PlayerStats
import com.jalmarquest.shared.model.Position
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DialogueTest {
    
    // ========== Dialogue Data Model Tests ==========
    
    @Test
    fun `DialogueNode should validate ID and text`() {
        val node = DialogueNode(
            id = "test_node",
            npcId = "test_npc",
            text = "Test dialogue",
            choices = emptyList()
        )
        
        assertEquals("test_node", node.id)
        assertEquals("Test dialogue", node.text)
    }
    
    @Test
    fun `DialogueChoice should validate ID and text`() {
        val choice = DialogueChoice(
            id = "test_choice",
            text = "Player response",
            nextNodeId = "next_node"
        )
        
        assertEquals("test_choice", choice.id)
        assertEquals("Player response", choice.text)
        assertEquals("next_node", choice.nextNodeId)
    }
    
    @Test
    fun `DialogueTree should contain entry node`() {
        val tree = DialogueTree(
            id = "test_tree",
            npcId = "test_npc",
            name = "Test Tree",
            entryNodeId = "entry",
            nodes = mapOf(
                "entry" to DialogueNode(
                    id = "entry",
                    npcId = "test_npc",
                    text = "Entry node",
                    choices = emptyList()
                )
            )
        )
        
        assertNotNull(tree.getNode("entry"))
        assertEquals("entry", tree.entryNodeId)
    }
    
    @Test
    fun `DialogueTree validate should detect invalid node references`() {
        val tree = DialogueTree(
            id = "test_tree",
            npcId = "test_npc",
            name = "Test Tree",
            entryNodeId = "entry",
            nodes = mapOf(
                "entry" to DialogueNode(
                    id = "entry",
                    npcId = "test_npc",
                    text = "Entry node",
                    choices = listOf(
                        DialogueChoice(
                            id = "bad_choice",
                            text = "Go to nowhere",
                            nextNodeId = "nonexistent_node"
                        )
                    )
                )
            )
        )
        
        var exceptionThrown = false
        try {
            tree.validate()
        } catch (e: IllegalStateException) {
            exceptionThrown = true
            assertTrue(e.message?.contains("invalid node") == true)
        }
        assertTrue(exceptionThrown, "Should throw exception for invalid node reference")
    }
    
    @Test
    fun `DialogueMemory should track seen nodes`() {
        val memory = DialogueMemory()
        
        assertFalse(memory.hasSeenNode("node1"))
        
        val updated = memory.markNodeSeen("node1")
        assertTrue(updated.hasSeenNode("node1"))
    }
    
    @Test
    fun `DialogueMemory should record choices`() {
        val memory = DialogueMemory()
        
        assertEquals(null, memory.getChoiceMade("node1"))
        
        val updated = memory.recordChoice("node1", "choice_a")
        assertEquals("choice_a", updated.getChoiceMade("node1"))
    }
    
    @Test
    fun `DialogueEffects hasEffects should return true when effects present`() {
        val effects = DialogueEffects(
            setFlags = mapOf("test_flag" to true)
        )
        
        assertTrue(effects.hasEffects())
    }
    
    @Test
    fun `DialogueEffects hasEffects should return false when no effects`() {
        val effects = DialogueEffects()
        
        assertFalse(effects.hasEffects())
    }
    
    // ========== DialogueCatalog Tests ==========
    
    @Test
    fun `DialogueCatalog should contain 15 dialogue trees (5 NPCs + 10 companions)`() {
        assertEquals(15, DialogueCatalog.getTotalTreeCount())
    }
    
    @Test
    fun `DialogueCatalog should retrieve tree by ID`() {
        val tree = DialogueCatalog.getTree("elder_quail_greeting")
        
        assertNotNull(tree)
        assertEquals("elder_quail_greeting", tree.id)
        assertEquals("elder_quail", tree.npcId)
    }
    
    @Test
    fun `DialogueCatalog should return null for invalid tree ID`() {
        val tree = DialogueCatalog.getTree("nonexistent_tree")
        assertEquals(null, tree)
    }
    
    @Test
    fun `DialogueCatalog should filter trees by NPC`() {
        val elderTrees = DialogueCatalog.getTreesForNPC("elder_quail")
        
        assertEquals(2, elderTrees.size)
        assertTrue(elderTrees.all { it.npcId == "elder_quail" })
    }
    
    @Test
    fun `DialogueCatalog should validate all trees`() {
        // Should not throw exception
        DialogueCatalog.validateCatalog()
    }
    
    // ========== DialogueManager Tests ==========
    
    private fun createTestGameState(
        level: Int = 1,
        completedQuests: Set<String> = emptySet(),
        activeQuests: List<String> = emptyList(),
        flags: Map<String, Boolean> = emptyMap(),
        dialogueMemory: DialogueMemory = DialogueMemory()
    ): GameState {
        return GameState(
            player = Player(
                id = "test_player",
                name = "Test Hero",
                level = level,
                stats = PlayerStats(),
                position = Position(0, 0, "starting_village"),
                inventory = Inventory(),
                seeds = 100,
                glimmerShards = 10
            ),
            completedQuests = completedQuests,
            activeQuests = activeQuests,
            flags = flags,
            dialogueMemory = dialogueMemory
        )
    }
    
    @Test
    fun `startDialogue should succeed with valid tree`() {
        val manager = DialogueManager()
        val gameState = createTestGameState()
        val tree = DialogueCatalog.getTree("elder_quail_greeting")!!
        
        val result = manager.startDialogue(gameState, tree)
        
        assertIs<DialogueManager.DialogueResult.Success>(result)
        assertEquals("greeting_1", result.currentNode.id)
        assertTrue(result.availableChoices.size == 3)
    }
    
    @Test
    fun `startDialogue should mark node as seen`() {
        val manager = DialogueManager()
        val gameState = createTestGameState()
        val tree = DialogueCatalog.getTree("elder_quail_greeting")!!
        
        val result = manager.startDialogue(gameState, tree)
        
        assertIs<DialogueManager.DialogueResult.Success>(result)
        assertTrue(result.gameState.dialogueMemory.hasSeenNode("greeting_1"))
    }
    
    @Test
    fun `startDialogue should set node flags`() {
        val manager = DialogueManager()
        val gameState = createTestGameState()
        val tree = DialogueCatalog.getTree("elder_quail_greeting")!!
        
        val result = manager.startDialogue(gameState, tree)
        
        assertIs<DialogueManager.DialogueResult.Success>(result)
        assertEquals(true, result.gameState.flags["met_elder_quail"])
    }
    
    @Test
    fun `startDialogue should fail for onceOnly node already seen`() {
        val manager = DialogueManager()
        val memory = DialogueMemory().markNodeSeen("greeting_1")
        val gameState = createTestGameState(dialogueMemory = memory)
        val tree = DialogueCatalog.getTree("elder_quail_greeting")!!
        
        val result = manager.startDialogue(gameState, tree)
        
        assertIs<DialogueManager.DialogueResult.Failure>(result)
        assertEquals(DialogueManager.DialogueFailure.ALREADY_SEEN_ONCE_ONLY, result.reason)
    }
    
    @Test
    fun `makeChoice should progress to next node`() {
        val manager = DialogueManager()
        val gameState = createTestGameState()
        val tree = DialogueCatalog.getTree("elder_quail_greeting")!!
        
        val startResult = manager.startDialogue(gameState, tree)
        assertIs<DialogueManager.DialogueResult.Success>(startResult)
        
        val choiceResult = manager.makeChoice(
            startResult.gameState,
            tree,
            "greeting_1",
            "eager"
        )
        
        assertIs<DialogueManager.DialogueResult.Success>(choiceResult)
        assertEquals("offer_tutorial", choiceResult.currentNode.id)
    }
    
    @Test
    fun `makeChoice should record choice in memory`() {
        val manager = DialogueManager()
        val gameState = createTestGameState()
        val tree = DialogueCatalog.getTree("elder_quail_greeting")!!
        
        val startResult = manager.startDialogue(gameState, tree)
        assertIs<DialogueManager.DialogueResult.Success>(startResult)
        
        val choiceResult = manager.makeChoice(
            startResult.gameState,
            tree,
            "greeting_1",
            "eager"
        )
        
        assertIs<DialogueManager.DialogueResult.Success>(choiceResult)
        assertEquals("eager", choiceResult.gameState.dialogueMemory.getChoiceMade("greeting_1"))
    }
    
    @Test
    fun `makeChoice should apply choice effects`() {
        val manager = DialogueManager()
        val gameState = createTestGameState()
        val tree = DialogueCatalog.getTree("elder_quail_greeting")!!
        
        val startResult = manager.startDialogue(gameState, tree)
        assertIs<DialogueManager.DialogueResult.Success>(startResult)
        
        val choiceResult = manager.makeChoice(
            startResult.gameState,
            tree,
            "greeting_1",
            "independent"
        )
        
        assertIs<DialogueManager.DialogueResult.Success>(choiceResult)
        assertEquals(true, choiceResult.gameState.flags["refused_elder_help"])
    }
    
    @Test
    fun `makeChoice should end dialogue when nextNodeId is null`() {
        val manager = DialogueManager()
        val gameState = createTestGameState()
        val tree = DialogueCatalog.getTree("elder_quail_greeting")!!
        
        val startResult = manager.startDialogue(gameState, tree)
        assertIs<DialogueManager.DialogueResult.Success>(startResult)
        
        val step1 = manager.makeChoice(startResult.gameState, tree, "greeting_1", "independent")
        assertIs<DialogueManager.DialogueResult.Success>(step1)
        
        val step2 = manager.makeChoice(step1.gameState, tree, "refuse_help", "leave")
        
        assertIs<DialogueManager.DialogueResult.End>(step2)
    }
    
    @Test
    fun `makeChoice should fail for invalid choice`() {
        val manager = DialogueManager()
        val gameState = createTestGameState()
        val tree = DialogueCatalog.getTree("elder_quail_greeting")!!
        
        val startResult = manager.startDialogue(gameState, tree)
        assertIs<DialogueManager.DialogueResult.Success>(startResult)
        
        val choiceResult = manager.makeChoice(
            startResult.gameState,
            tree,
            "greeting_1",
            "nonexistent_choice"
        )
        
        assertIs<DialogueManager.DialogueResult.Failure>(choiceResult)
        assertEquals(DialogueManager.DialogueFailure.CHOICE_NOT_FOUND, choiceResult.reason)
    }
    
    @Test
    fun `checkConditions should validate QuestActive condition`() {
        val manager = DialogueManager()
        val gameState = createTestGameState(activeQuests = listOf("test_quest"))
        
        val conditions = listOf(DialogueCondition.QuestActive("test_quest"))
        assertTrue(manager.checkConditions(gameState, conditions))
        
        val failConditions = listOf(DialogueCondition.QuestActive("other_quest"))
        assertFalse(manager.checkConditions(gameState, failConditions))
    }
    
    @Test
    fun `checkConditions should validate QuestCompleted condition`() {
        val manager = DialogueManager()
        val gameState = createTestGameState(completedQuests = setOf("test_quest"))
        
        val conditions = listOf(DialogueCondition.QuestCompleted("test_quest"))
        assertTrue(manager.checkConditions(gameState, conditions))
        
        val failConditions = listOf(DialogueCondition.QuestCompleted("other_quest"))
        assertFalse(manager.checkConditions(gameState, failConditions))
    }
    
    @Test
    fun `checkConditions should validate QuestNotStarted condition`() {
        val manager = DialogueManager()
        val gameState = createTestGameState()
        
        val conditions = listOf(DialogueCondition.QuestNotStarted("test_quest"))
        assertTrue(manager.checkConditions(gameState, conditions))
        
        val activeState = createTestGameState(activeQuests = listOf("test_quest"))
        assertFalse(manager.checkConditions(activeState, conditions))
    }
    
    @Test
    fun `checkConditions should validate PlayerLevel condition`() {
        val manager = DialogueManager()
        val gameState = createTestGameState(level = 10)
        
        val conditions = listOf(DialogueCondition.PlayerLevel(5))
        assertTrue(manager.checkConditions(gameState, conditions))
        
        val failConditions = listOf(DialogueCondition.PlayerLevel(15))
        assertFalse(manager.checkConditions(gameState, failConditions))
    }
    
    @Test
    fun `checkConditions should validate FlagSet condition`() {
        val manager = DialogueManager()
        val gameState = createTestGameState(flags = mapOf("test_flag" to true))
        
        val conditions = listOf(DialogueCondition.FlagSet("test_flag", true))
        assertTrue(manager.checkConditions(gameState, conditions))
        
        val failConditions = listOf(DialogueCondition.FlagSet("test_flag", false))
        assertFalse(manager.checkConditions(gameState, failConditions))
    }
    
    @Test
    fun `checkConditions should validate CurrencyAmount condition`() {
        val manager = DialogueManager()
        val gameState = createTestGameState() // 100 seeds, 10 glimmerShards
        
        val conditions = listOf(DialogueCondition.CurrencyAmount(50, 5))
        assertTrue(manager.checkConditions(gameState, conditions))
        
        val failConditions = listOf(DialogueCondition.CurrencyAmount(200, 5))
        assertFalse(manager.checkConditions(gameState, failConditions))
    }
    
    @Test
    fun `checkConditions should validate NodeSeen condition`() {
        val manager = DialogueManager()
        val memory = DialogueMemory().markNodeSeen("node1")
        val gameState = createTestGameState(dialogueMemory = memory)
        
        val conditions = listOf(DialogueCondition.NodeSeen("node1"))
        assertTrue(manager.checkConditions(gameState, conditions))
        
        val failConditions = listOf(DialogueCondition.NodeSeen("node2"))
        assertFalse(manager.checkConditions(gameState, failConditions))
    }
    
    @Test
    fun `checkConditions should validate NodeNotSeen condition`() {
        val manager = DialogueManager()
        val memory = DialogueMemory().markNodeSeen("node1")
        val gameState = createTestGameState(dialogueMemory = memory)
        
        val conditions = listOf(DialogueCondition.NodeNotSeen("node2"))
        assertTrue(manager.checkConditions(gameState, conditions))
        
        val failConditions = listOf(DialogueCondition.NodeNotSeen("node1"))
        assertFalse(manager.checkConditions(gameState, failConditions))
    }
    
    @Test
    fun `getAvailableChoices should filter by conditions`() {
        val manager = DialogueManager()
        val gameState = createTestGameState(
            completedQuests = setOf("tutorial_first_steps")
        )
        val tree = DialogueCatalog.getTree("elder_quail_quest_turnin")!!
        
        val startResult = manager.startDialogue(gameState, tree)
        assertIs<DialogueManager.DialogueResult.Success>(startResult)
        
        val availableChoices = startResult.availableChoices
        
        // Should have "turn_in_first_steps" and "just_checking_in", but not "turn_in_gnome"
        assertTrue(availableChoices.any { it.id == "turn_in_first_steps" })
        assertTrue(availableChoices.any { it.id == "just_checking_in" })
        assertFalse(availableChoices.any { it.id == "turn_in_gnome" })
    }
    
    @Test
    fun `dialogue flow should accept quest via QuestAction`() {
        val manager = DialogueManager()
        val gameState = createTestGameState()
        val tree = DialogueCatalog.getTree("elder_quail_greeting")!!
        
        val startResult = manager.startDialogue(gameState, tree)
        assertIs<DialogueManager.DialogueResult.Success>(startResult)
        
        val choiceResult = manager.makeChoice(
            startResult.gameState,
            tree,
            "greeting_1",
            "eager"
        )
        
        assertIs<DialogueManager.DialogueResult.Success>(choiceResult)
        
        // Quest should be accepted (tutorial_first_steps in offer_tutorial node)
        assertTrue(choiceResult.gameState.activeQuests.contains("tutorial_first_steps"))
    }
    
    @Test
    fun `dialogue flow should complete full conversation path`() {
        val manager = DialogueManager()
        val gameState = createTestGameState()
        val tree = DialogueCatalog.getTree("young_quail_lost_feather")!!
        
        // Start conversation
        val start = manager.startDialogue(gameState, tree)
        assertIs<DialogueManager.DialogueResult.Success>(start)
        assertEquals("crying", start.currentNode.id)
        
        // Choose to help
        val help = manager.makeChoice(start.gameState, tree, "crying", "offer_help")
        assertIs<DialogueManager.DialogueResult.Success>(help)
        assertEquals("thank_you", help.currentNode.id)
        
        // Accept quest
        val accept = manager.makeChoice(help.gameState, tree, "thank_you", "accept")
        assertIs<DialogueManager.DialogueResult.End>(accept)
        
        // Quest should be active
        assertTrue(accept.gameState.activeQuests.contains("side_lost_feather"))
    }
    
    @Test
    fun `dialogue should track multiple choice paths for Butterfly Effect`() {
        val manager = DialogueManager()
        val gameState = createTestGameState()
        val tree = DialogueCatalog.getTree("elder_quail_greeting")!!
        
        val start = manager.startDialogue(gameState, tree)
        assertIs<DialogueManager.DialogueResult.Success>(start)
        
        val choice1 = manager.makeChoice(start.gameState, tree, "greeting_1", "curious")
        assertIs<DialogueManager.DialogueResult.Success>(choice1)
        
        val choice2 = manager.makeChoice(choice1.gameState, tree, "lore_explanation", "ready_now")
        assertIs<DialogueManager.DialogueResult.Success>(choice2)
        
        // Both choices should be recorded
        assertEquals("curious", choice2.gameState.dialogueMemory.getChoiceMade("greeting_1"))
        assertEquals("ready_now", choice2.gameState.dialogueMemory.getChoiceMade("lore_explanation"))
        
        // Both nodes should be marked as seen
        assertTrue(choice2.gameState.dialogueMemory.hasSeenNode("greeting_1"))
        assertTrue(choice2.gameState.dialogueMemory.hasSeenNode("lore_explanation"))
        assertTrue(choice2.gameState.dialogueMemory.hasSeenNode("offer_tutorial"))
    }
}
