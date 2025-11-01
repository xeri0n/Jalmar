package com.jalmarquest.shared.butterfly

import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.model.Position
import com.jalmarquest.shared.model.WorldTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Comprehensive tests for Butterfly Effect system.
 * 
 * Test Coverage:
 * - Choice recording (success, validation, duplicates)
 * - Trigger evaluation (time, quest, location, NPC, combination)
 * - Consequence application (immediate, delayed, chained)
 * - Query operations (history, statistics, lookups)
 * - Edge cases (invalid data, missing references, conflicts)
 * - Integration with GameState
 */
class ButterflyEffectManagerTest {
    
    private val manager = ButterflyEffectManager()
    
    // ========================================
    // CHOICE RECORDING TESTS
    // ========================================
    
    @Test
    fun `recordChoice should create choice and pending consequences`() {
        val state = ButterflyEffectState()
        val consequences = listOf(
            Consequence(
                id = "c1",
                triggeringChoiceId = "placeholder",
                type = ConsequenceType.NPC_RELATIONSHIP,
                trigger = ConsequenceTrigger.TimeBased(ticksDelay = 1000),
                effectKey = "test_effect",
                magnitude = 50,
                description = "Test consequence"
            )
        )
        
        val result = manager.recordChoice(
            state = state,
            category = ChoiceCategory.DIALOGUE,
            choiceKey = "dialogue_test_choice",
            timestamp = 1000L,
            locationId = "test_location",
            involvedNPCs = listOf("npc1"),
            impact = ChoiceImpact.MODERATE,
            metadata = mapOf("context" to "test"),
            consequences = consequences
        )
        
        assertIs<ChoiceResult.Success>(result)
        val success = result as ChoiceResult.Success
        assertEquals(1, success.updatedState.playerChoices.size)
        assertEquals(1, success.updatedState.pendingConsequences.size)
        assertEquals("dialogue_test_choice", success.updatedState.playerChoices.first().choiceKey)
    }
    
    @Test
    fun `recordChoice should trigger immediate consequences`() {
        val state = ButterflyEffectState()
        val consequences = listOf(
            Consequence(
                id = "c1",
                triggeringChoiceId = "placeholder",
                type = ConsequenceType.NPC_RELATIONSHIP,
                trigger = ConsequenceTrigger.Immediate,
                effectKey = "immediate_effect",
                magnitude = 50,
                description = "Immediate consequence"
            ),
            Consequence(
                id = "c2",
                triggeringChoiceId = "placeholder",
                type = ConsequenceType.NPC_RELATIONSHIP,
                trigger = ConsequenceTrigger.TimeBased(ticksDelay = 1000),
                effectKey = "delayed_effect",
                magnitude = 50,
                description = "Delayed consequence"
            )
        )
        
        val result = manager.recordChoice(
            state = state,
            category = ChoiceCategory.COMBAT,
            choiceKey = "combat_mercy",
            timestamp = 1000L,
            locationId = "battlefield",
            consequences = consequences
        )
        
        assertIs<ChoiceResult.Success>(result)
        val success = result as ChoiceResult.Success
        assertEquals(1, success.triggeredConsequences.size)
        assertEquals("immediate_effect", success.triggeredConsequences.first().effectKey)
        assertEquals(1, success.updatedState.pendingConsequences.size)
        assertEquals("delayed_effect", success.updatedState.pendingConsequences.first().effectKey)
    }
    
    @Test
    fun `recordChoice should reject invalid choice data`() {
        val state = ButterflyEffectState()
        
        val result = manager.recordChoice(
            state = state,
            category = ChoiceCategory.DIALOGUE,
            choiceKey = "", // Invalid: blank key
            timestamp = 1000L,
            locationId = "test_location"
        )
        
        assertIs<ChoiceResult.Failure>(result)
        assertEquals(ChoiceFailure.INVALID_CHOICE_DATA, (result as ChoiceResult.Failure).reason)
    }
    
    @Test
    fun `recordChoice should reject duplicate choices`() {
        val state = ButterflyEffectState(
            playerChoices = listOf(
                PlayerChoice(
                    id = "choice1",
                    category = ChoiceCategory.DIALOGUE,
                    choiceKey = "existing_choice",
                    timestamp = 1000L,
                    locationId = "location1"
                )
            )
        )
        
        val result = manager.recordChoice(
            state = state,
            category = ChoiceCategory.DIALOGUE,
            choiceKey = "existing_choice", // Duplicate
            timestamp = 2000L,
            locationId = "location1"
        )
        
        assertIs<ChoiceResult.Failure>(result)
        assertEquals(ChoiceFailure.DUPLICATE_CHOICE, (result as ChoiceResult.Failure).reason)
    }
    
    @Test
    fun `recordChoice should track consequence chains`() {
        val state = ButterflyEffectState()
        val consequences = listOf(
            Consequence(
                id = "c1",
                triggeringChoiceId = "placeholder",
                type = ConsequenceType.NPC_RELATIONSHIP,
                trigger = ConsequenceTrigger.Immediate,
                effectKey = "effect1",
                magnitude = 50,
                description = "First"
            ),
            Consequence(
                id = "c2",
                triggeringChoiceId = "placeholder",
                type = ConsequenceType.QUEST_UNLOCK,
                trigger = ConsequenceTrigger.TimeBased(ticksDelay = 1000),
                effectKey = "effect2",
                magnitude = 50,
                description = "Second"
            )
        )
        
        val result = manager.recordChoice(
            state = state,
            category = ChoiceCategory.QUEST,
            choiceKey = "quest_save_npc",
            timestamp = 1000L,
            locationId = "rescue_location",
            consequences = consequences
        )
        
        assertIs<ChoiceResult.Success>(result)
        val success = result as ChoiceResult.Success
        assertTrue(success.updatedState.consequenceChains.containsKey(success.choiceId))
        assertEquals(2, success.updatedState.consequenceChains[success.choiceId]!!.size)
    }
    
    // ========================================
    // TRIGGER EVALUATION TESTS
    // ========================================
    
    @Test
    fun `evaluateConsequences should trigger time-based consequences`() {
        val choiceId = "choice1"
        val choiceTimestamp = 1000L
        
        val state = ButterflyEffectState(
            playerChoices = listOf(
                PlayerChoice(
                    id = choiceId,
                    category = ChoiceCategory.DIALOGUE,
                    choiceKey = "test_choice",
                    timestamp = choiceTimestamp,
                    locationId = "location1"
                )
            ),
            pendingConsequences = listOf(
                Consequence(
                    id = "c1",
                    triggeringChoiceId = choiceId,
                    type = ConsequenceType.NPC_RELATIONSHIP,
                    trigger = ConsequenceTrigger.TimeBased(ticksDelay = 500), // Triggers at 1500
                    effectKey = "delayed_effect",
                    magnitude = 50,
                    description = "Delayed consequence"
                )
            )
        )
        
        val gameState = createTestGameState(worldTime = WorldTime(totalTicks = 1600)) // After trigger time
        
        val result = manager.evaluateConsequences(state, gameState)
        
        assertIs<ConsequenceEvaluationResult.Success>(result)
        val success = result as ConsequenceEvaluationResult.Success
        assertEquals(1, success.newlyTriggeredConsequences.size)
        assertEquals(0, success.updatedState.pendingConsequences.size)
        assertEquals(1, success.updatedState.triggeredConsequences.size)
    }
    
    @Test
    fun `evaluateConsequences should not trigger time-based consequences too early`() {
        val choiceId = "choice1"
        val choiceTimestamp = 1000L
        
        val state = ButterflyEffectState(
            playerChoices = listOf(
                PlayerChoice(
                    id = choiceId,
                    category = ChoiceCategory.DIALOGUE,
                    choiceKey = "test_choice",
                    timestamp = choiceTimestamp,
                    locationId = "location1"
                )
            ),
            pendingConsequences = listOf(
                Consequence(
                    id = "c1",
                    triggeringChoiceId = choiceId,
                    type = ConsequenceType.NPC_RELATIONSHIP,
                    trigger = ConsequenceTrigger.TimeBased(ticksDelay = 1000), // Triggers at 2000
                    effectKey = "delayed_effect",
                    magnitude = 50,
                    description = "Delayed consequence"
                )
            )
        )
        
        val gameState = createTestGameState(worldTime = WorldTime(totalTicks = 1500)) // Before trigger time
        
        val result = manager.evaluateConsequences(state, gameState)
        
        assertIs<ConsequenceEvaluationResult.Success>(result)
        val success = result as ConsequenceEvaluationResult.Success
        assertEquals(0, success.newlyTriggeredConsequences.size)
        assertEquals(1, success.updatedState.pendingConsequences.size)
    }
    
    @Test
    fun `evaluateConsequences should trigger quest-based consequences`() {
        val state = ButterflyEffectState(
            playerChoices = listOf(
                PlayerChoice(
                    id = "choice1",
                    category = ChoiceCategory.QUEST,
                    choiceKey = "quest_accept",
                    timestamp = 1000L,
                    locationId = "quest_giver"
                )
            ),
            pendingConsequences = listOf(
                Consequence(
                    id = "c1",
                    triggeringChoiceId = "choice1",
                    type = ConsequenceType.QUEST_UNLOCK,
                    trigger = ConsequenceTrigger.QuestBased(questId = "prerequisite_quest"),
                    effectKey = "unlock_next_quest",
                    magnitude = 100,
                    description = "Unlock next quest"
                )
            )
        )
        
        val gameState = createTestGameState(completedQuests = setOf("prerequisite_quest"))
        
        val result = manager.evaluateConsequences(state, gameState)
        
        assertIs<ConsequenceEvaluationResult.Success>(result)
        val success = result as ConsequenceEvaluationResult.Success
        assertEquals(1, success.newlyTriggeredConsequences.size)
    }
    
    @Test
    fun `evaluateConsequences should trigger location-based consequences`() {
        val state = ButterflyEffectState(
            playerChoices = listOf(
                PlayerChoice(
                    id = "choice1",
                    category = ChoiceCategory.EXPLORATION,
                    choiceKey = "discover_item",
                    timestamp = 1000L,
                    locationId = "meadow"
                )
            ),
            pendingConsequences = listOf(
                Consequence(
                    id = "c1",
                    triggeringChoiceId = "choice1",
                    type = ConsequenceType.SPECIAL_EVENT,
                    trigger = ConsequenceTrigger.LocationBased(locationId = "village"),
                    effectKey = "merchant_recognizes_item",
                    magnitude = 100,
                    description = "Merchant recognizes the item"
                )
            )
        )
        
        val gameState = createTestGameState(playerLocation = "village")
        
        val result = manager.evaluateConsequences(state, gameState)
        
        assertIs<ConsequenceEvaluationResult.Success>(result)
        val success = result as ConsequenceEvaluationResult.Success
        assertEquals(1, success.newlyTriggeredConsequences.size)
    }
    
    @Test
    fun `evaluateConsequences should trigger combination AND triggers`() {
        val state = ButterflyEffectState(
            playerChoices = listOf(
                PlayerChoice(
                    id = "choice1",
                    category = ChoiceCategory.COMBAT,
                    choiceKey = "show_mercy",
                    timestamp = 1000L,
                    locationId = "battlefield"
                )
            ),
            pendingConsequences = listOf(
                Consequence(
                    id = "c1",
                    triggeringChoiceId = "choice1",
                    type = ConsequenceType.SPECIAL_EVENT,
                    trigger = ConsequenceTrigger.CombinationTrigger(
                        conditions = listOf(
                            ConsequenceTrigger.QuestBased(questId = "boss_quest"),
                            ConsequenceTrigger.LocationBased(locationId = "throne_room")
                        ),
                        requireAll = true
                    ),
                    effectKey = "boss_remembers_mercy",
                    magnitude = 100,
                    description = "Boss remembers your mercy"
                )
            )
        )
        
        val gameState = createTestGameState(
            playerLocation = "throne_room",
            completedQuests = setOf("boss_quest")
        )
        
        val result = manager.evaluateConsequences(state, gameState)
        
        assertIs<ConsequenceEvaluationResult.Success>(result)
        val success = result as ConsequenceEvaluationResult.Success
        assertEquals(1, success.newlyTriggeredConsequences.size)
    }
    
    @Test
    fun `evaluateConsequences should not trigger combination AND triggers with partial conditions`() {
        val state = ButterflyEffectState(
            playerChoices = listOf(
                PlayerChoice(
                    id = "choice1",
                    category = ChoiceCategory.COMBAT,
                    choiceKey = "show_mercy",
                    timestamp = 1000L,
                    locationId = "battlefield"
                )
            ),
            pendingConsequences = listOf(
                Consequence(
                    id = "c1",
                    triggeringChoiceId = "choice1",
                    type = ConsequenceType.SPECIAL_EVENT,
                    trigger = ConsequenceTrigger.CombinationTrigger(
                        conditions = listOf(
                            ConsequenceTrigger.QuestBased(questId = "boss_quest"),
                            ConsequenceTrigger.LocationBased(locationId = "throne_room")
                        ),
                        requireAll = true
                    ),
                    effectKey = "boss_remembers_mercy",
                    magnitude = 100,
                    description = "Boss remembers your mercy"
                )
            )
        )
        
        val gameState = createTestGameState(
            playerLocation = "throne_room",
            completedQuests = emptySet() // Quest not completed
        )
        
        val result = manager.evaluateConsequences(state, gameState)
        
        assertIs<ConsequenceEvaluationResult.Success>(result)
        val success = result as ConsequenceEvaluationResult.Success
        assertEquals(0, success.newlyTriggeredConsequences.size)
        assertEquals(1, success.updatedState.pendingConsequences.size)
    }
    
    @Test
    fun `evaluateConsequences should trigger combination OR triggers with any condition met`() {
        val state = ButterflyEffectState(
            playerChoices = listOf(
                PlayerChoice(
                    id = "choice1",
                    category = ChoiceCategory.SOCIAL,
                    choiceKey = "help_npc",
                    timestamp = 1000L,
                    locationId = "village"
                )
            ),
            pendingConsequences = listOf(
                Consequence(
                    id = "c1",
                    triggeringChoiceId = "choice1",
                    type = ConsequenceType.NPC_BEHAVIOR,
                    trigger = ConsequenceTrigger.CombinationTrigger(
                        conditions = listOf(
                            ConsequenceTrigger.QuestBased(questId = "impossible_quest"),
                            ConsequenceTrigger.LocationBased(locationId = "village")
                        ),
                        requireAll = false // OR trigger
                    ),
                    effectKey = "npc_thanks_player",
                    magnitude = 50,
                    description = "NPC thanks player"
                )
            )
        )
        
        val gameState = createTestGameState(
            playerLocation = "village", // One condition met
            completedQuests = emptySet() // Other condition not met
        )
        
        val result = manager.evaluateConsequences(state, gameState)
        
        assertIs<ConsequenceEvaluationResult.Success>(result)
        val success = result as ConsequenceEvaluationResult.Success
        assertEquals(1, success.newlyTriggeredConsequences.size)
    }
    
    // ========================================
    // QUERY OPERATIONS TESTS
    // ========================================
    
    @Test
    fun `getChoiceHistory should return choices in chronological order`() {
        val state = ButterflyEffectState(
            playerChoices = listOf(
                PlayerChoice(
                    id = "c3",
                    category = ChoiceCategory.COMBAT,
                    choiceKey = "combat_flee",
                    timestamp = 3000L,
                    locationId = "battlefield"
                ),
                PlayerChoice(
                    id = "c1",
                    category = ChoiceCategory.DIALOGUE,
                    choiceKey = "dialogue_rude",
                    timestamp = 1000L,
                    locationId = "village"
                ),
                PlayerChoice(
                    id = "c2",
                    category = ChoiceCategory.QUEST,
                    choiceKey = "quest_accept",
                    timestamp = 2000L,
                    locationId = "quest_giver"
                )
            )
        )
        
        val history = manager.getChoiceHistory(state)
        
        assertEquals(3, history.size)
        assertEquals(1000L, history[0].timestamp)
        assertEquals(2000L, history[1].timestamp)
        assertEquals(3000L, history[2].timestamp)
    }
    
    @Test
    fun `getChoicesForNPC should return choices involving specific NPC`() {
        val state = ButterflyEffectState(
            playerChoices = listOf(
                PlayerChoice(
                    id = "c1",
                    category = ChoiceCategory.DIALOGUE,
                    choiceKey = "dialogue_grumble_help",
                    timestamp = 1000L,
                    locationId = "quailsmith",
                    involvedNPCs = listOf("grumble")
                ),
                PlayerChoice(
                    id = "c2",
                    category = ChoiceCategory.QUEST,
                    choiceKey = "quest_other",
                    timestamp = 2000L,
                    locationId = "village",
                    involvedNPCs = listOf("merchant")
                ),
                PlayerChoice(
                    id = "c3",
                    category = ChoiceCategory.SOCIAL,
                    choiceKey = "social_gift_grumble",
                    timestamp = 3000L,
                    locationId = "quailsmith",
                    involvedNPCs = listOf("grumble")
                )
            )
        )
        
        val grumbleChoices = manager.getChoicesForNPC(state, "grumble")
        
        assertEquals(2, grumbleChoices.size)
        assertEquals("dialogue_grumble_help", grumbleChoices[0].choiceKey)
        assertEquals("social_gift_grumble", grumbleChoices[1].choiceKey)
    }
    
    @Test
    fun `getChoiceStatistics should return accurate statistics`() {
        val state = ButterflyEffectState(
            playerChoices = listOf(
                PlayerChoice(
                    id = "c1",
                    category = ChoiceCategory.DIALOGUE,
                    choiceKey = "dialogue_1",
                    timestamp = 1000L,
                    locationId = "village",
                    impact = ChoiceImpact.MINOR
                ),
                PlayerChoice(
                    id = "c2",
                    category = ChoiceCategory.QUEST,
                    choiceKey = "quest_1",
                    timestamp = 2000L,
                    locationId = "village",
                    impact = ChoiceImpact.MAJOR
                ),
                PlayerChoice(
                    id = "c3",
                    category = ChoiceCategory.COMBAT,
                    choiceKey = "combat_1",
                    timestamp = 3000L,
                    locationId = "battlefield",
                    impact = ChoiceImpact.CRITICAL
                )
            ),
            pendingConsequences = listOf(
                Consequence(
                    id = "pending1",
                    triggeringChoiceId = "c1",
                    type = ConsequenceType.NPC_RELATIONSHIP,
                    trigger = ConsequenceTrigger.TimeBased(1000),
                    effectKey = "test",
                    magnitude = 50,
                    description = "Test"
                ),
                Consequence(
                    id = "pending2",
                    triggeringChoiceId = "c2",
                    type = ConsequenceType.QUEST_UNLOCK,
                    trigger = ConsequenceTrigger.TimeBased(1000),
                    effectKey = "test",
                    magnitude = 50,
                    description = "Test"
                )
            ),
            triggeredConsequences = listOf(
                Consequence(
                    id = "triggered1",
                    triggeringChoiceId = "c3",
                    type = ConsequenceType.SPECIAL_EVENT,
                    trigger = ConsequenceTrigger.Immediate,
                    effectKey = "test",
                    magnitude = 50,
                    description = "Test",
                    hasTriggered = true
                )
            )
        )
        
        val stats = manager.getChoiceStatistics(state)
        
        assertEquals(3, stats.totalChoices)
        assertEquals(1, stats.minorChoices)
        assertEquals(0, stats.moderateChoices)
        assertEquals(1, stats.majorChoices)
        assertEquals(1, stats.criticalChoices)
        assertEquals(1, stats.dialogueChoices)
        assertEquals(1, stats.questChoices)
        assertEquals(1, stats.combatChoices)
        assertEquals(2, stats.pendingConsequences)
        assertEquals(1, stats.triggeredConsequences)
    }
    
    @Test
    fun `hasPlayerMadeChoice should detect existing choices`() {
        val state = ButterflyEffectState(
            playerChoices = listOf(
                PlayerChoice(
                    id = "c1",
                    category = ChoiceCategory.DIALOGUE,
                    choiceKey = "dialogue_grumble_insult",
                    timestamp = 1000L,
                    locationId = "quailsmith"
                )
            )
        )
        
        assertTrue(manager.hasPlayerMadeChoice(state, "dialogue_grumble_insult"))
        assertFalse(manager.hasPlayerMadeChoice(state, "dialogue_grumble_help"))
    }
    
    @Test
    fun `getPendingConsequencesForLocation should filter by location trigger`() {
        val state = ButterflyEffectState(
            pendingConsequences = listOf(
                Consequence(
                    id = "c1",
                    triggeringChoiceId = "choice1",
                    type = ConsequenceType.SPECIAL_EVENT,
                    trigger = ConsequenceTrigger.LocationBased(locationId = "village"),
                    effectKey = "village_event",
                    magnitude = 100,
                    description = "Village event"
                ),
                Consequence(
                    id = "c2",
                    triggeringChoiceId = "choice2",
                    type = ConsequenceType.SPECIAL_EVENT,
                    trigger = ConsequenceTrigger.LocationBased(locationId = "forest"),
                    effectKey = "forest_event",
                    magnitude = 100,
                    description = "Forest event"
                ),
                Consequence(
                    id = "c3",
                    triggeringChoiceId = "choice3",
                    type = ConsequenceType.NPC_RELATIONSHIP,
                    trigger = ConsequenceTrigger.TimeBased(ticksDelay = 1000),
                    effectKey = "time_event",
                    magnitude = 50,
                    description = "Time event"
                )
            )
        )
        
        val villageConsequences = manager.getPendingConsequencesForLocation(state, "village")
        
        assertEquals(1, villageConsequences.size)
        assertEquals("village_event", villageConsequences.first().effectKey)
    }
    
    @Test
    fun `getPendingConsequencesForNPC should filter by NPC trigger`() {
        val state = ButterflyEffectState(
            pendingConsequences = listOf(
                Consequence(
                    id = "c1",
                    triggeringChoiceId = "choice1",
                    type = ConsequenceType.NPC_BEHAVIOR,
                    trigger = ConsequenceTrigger.NPCBased(npcId = "grumble"),
                    effectKey = "grumble_react",
                    magnitude = 100,
                    description = "Grumble reacts"
                ),
                Consequence(
                    id = "c2",
                    triggeringChoiceId = "choice2",
                    type = ConsequenceType.NPC_BEHAVIOR,
                    trigger = ConsequenceTrigger.NPCBased(npcId = "merchant"),
                    effectKey = "merchant_react",
                    magnitude = 100,
                    description = "Merchant reacts"
                )
            )
        )
        
        val grumbleConsequences = manager.getPendingConsequencesForNPC(state, "grumble")
        
        assertEquals(1, grumbleConsequences.size)
        assertEquals("grumble_react", grumbleConsequences.first().effectKey)
    }
    
    // ========================================
    // CONSEQUENCE CATALOG TESTS
    // ========================================
    
    @Test
    fun `ConsequenceCatalog should provide Grumble insult consequences`() {
        val consequences = ConsequenceCatalog.createInsultGrumbleConsequences("test_choice")
        
        assertEquals(4, consequences.size)
        assertTrue(consequences.any { it.effectKey == "npc_grumble_relationship_decrease" })
        assertTrue(consequences.any { it.effectKey == "npc_grumble_raise_prices" })
        assertTrue(consequences.any { it.effectKey == "flag_grumble_spread_rumors" })
        assertTrue(consequences.any { it.effectKey == "merchants_collective_price_increase" })
    }
    
    @Test
    fun `ConsequenceCatalog should provide save beetle consequences`() {
        val consequences = ConsequenceCatalog.createSaveBeetleConsequences("test_choice")
        
        assertEquals(3, consequences.size)
        assertTrue(consequences.any { it.type == ConsequenceType.COMPANION_UNLOCK })
        assertTrue(consequences.any { it.type == ConsequenceType.LORE_UNLOCK })
        assertTrue(consequences.any { it.type == ConsequenceType.SPECIAL_EVENT })
    }
    
    @Test
    fun `ConsequenceCatalog should provide mercy vs kill consequences`() {
        val mercyConsequences = ConsequenceCatalog.createMercyShadowSparrowConsequences("mercy_choice")
        val killConsequences = ConsequenceCatalog.createKillShadowSparrowConsequences("kill_choice")
        
        assertEquals(5, mercyConsequences.size)
        assertEquals(4, killConsequences.size)
        
        // Mercy path unlocks ending
        assertTrue(mercyConsequences.any { it.effectKey == "ending_mercy_path_unlock" })
        
        // Kill path locks ending
        assertTrue(killConsequences.any { it.effectKey == "ending_mercy_path_locked" })
    }
    
    @Test
    fun `ConsequenceCatalog getConsequencesForChoice should return correct template`() {
        val consequences = ConsequenceCatalog.getConsequencesForChoice("dialogue_grumble_help", "test_choice_id")
        
        assertTrue(consequences.isNotEmpty())
        assertEquals("test_choice_id", consequences.first().triggeringChoiceId)
        assertTrue(consequences.any { it.effectKey == "npc_grumble_relationship_increase" })
    }
    
    @Test
    fun `ConsequenceCatalog getConsequencesForChoice should return empty for unknown choice`() {
        val consequences = ConsequenceCatalog.getConsequencesForChoice("unknown_choice_key", "test_choice_id")
        
        assertTrue(consequences.isEmpty())
    }
    
    // ========================================
    // HELPER FUNCTIONS
    // ========================================
    
    private fun createTestGameState(
        worldTime: WorldTime = WorldTime(),
        playerLocation: String = "test_location",
        completedQuests: Set<String> = emptySet()
    ): GameState {
        return GameState(
            version = 1,
            player = Player(
                id = "test_player",
                name = "Test Hero",
                position = Position(x = 0, y = 0, locationId = playerLocation)
            ),
            worldTime = worldTime,
            completedQuests = completedQuests
        )
    }
}
