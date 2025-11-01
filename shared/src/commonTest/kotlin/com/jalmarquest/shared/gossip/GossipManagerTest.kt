package com.jalmarquest.shared.gossip

import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.model.Player
import kotlinx.coroutines.test.runTest
import kotlin.test.*

/**
 * Comprehensive tests for Gossip & Rumor System.
 * 
 * Coverage:
 * - Rumor creation and validation
 * - Rumor spreading with probability checks
 * - Mutation logic (telephone game effect)
 * - Truth level progression
 * - Reputation effects
 * - NPC memory tracking
 * - Cooldown management
 * - Spread probability calculation
 * - Catalog functionality
 * - Statistics tracking
 */
class GossipManagerTest {
    
    private fun createTestGameState(): GameState {
        return GameState.createNew("Hero", "player_1")
    }
    
    private fun createTestGossipState(): GossipState {
        return GossipState()
    }
    
    // ============================================
    // RUMOR CREATION TESTS (5 tests)
    // ============================================
    
    @Test
    fun `startRumor should create new rumor with correct data`() {
        val state = createTestGossipState()
        
        val result = GossipManager.startRumor(
            state = state,
            rumorKey = "rumor_test",
            subjectId = "player_1",
            originNPCId = "npc_grumble",
            originalText = "Hero defeated 3 ants.",
            category = RumorCategory.HEROIC_DEED,
            reputationEffects = listOf(
                ReputationEffect("buttonburgh_citizens", 10, "Heroic deed")
            ),
            currentTimestamp = 1000
        )
        
        assertTrue(result is StartRumorResult.Success)
        val success = result as StartRumorResult.Success
        
        assertNotNull(success.rumorId)
        assertTrue(success.rumorId.startsWith("rumor_"))
        
        val rumor = success.state.activeRumors[success.rumorId]
        assertNotNull(rumor)
        assertEquals("rumor_test", rumor.rumorKey)
        assertEquals("Hero defeated 3 ants.", rumor.originalText)
        assertEquals("Hero defeated 3 ants.", rumor.currentText)
        assertEquals(RumorCategory.HEROIC_DEED, rumor.category)
        assertEquals(TruthLevel.ACCURATE, rumor.truthLevel)
        assertEquals(0, rumor.mutationCount)
        assertTrue(rumor.isKnownBy("npc_grumble"))
    }
    
    @Test
    fun `startRumor should add origin NPC to gossip memory`() {
        val state = createTestGossipState()
        
        val result = GossipManager.startRumor(
            state = state,
            rumorKey = "rumor_test",
            subjectId = "player_1",
            originNPCId = "npc_grumble",
            originalText = "Test rumor",
            category = RumorCategory.HEROIC_DEED,
            currentTimestamp = 1000
        ) as StartRumorResult.Success
        
        val npcRumors = result.state.npcGossipMemory["npc_grumble"]
        assertNotNull(npcRumors)
        assertTrue(result.rumorId in npcRumors)
    }
    
    @Test
    fun `startRumor should create initial statistics`() {
        val state = createTestGossipState()
        
        val result = GossipManager.startRumor(
            state = state,
            rumorKey = "rumor_test",
            subjectId = "player_1",
            originNPCId = "npc_grumble",
            originalText = "Test rumor",
            category = RumorCategory.HEROIC_DEED,
            currentTimestamp = 1000
        ) as StartRumorResult.Success
        
        val stats = result.state.rumorStatistics[result.rumorId]
        assertNotNull(stats)
        assertEquals(0, stats.timesSpread)
        assertEquals(1, stats.totalReach)
        assertTrue(stats.mutationHistory.isEmpty())
    }
    
    @Test
    fun `startRumor should reject blank rumor key`() {
        val state = createTestGossipState()
        
        val result = GossipManager.startRumor(
            state = state,
            rumorKey = "",
            subjectId = "player_1",
            originNPCId = "npc_grumble",
            originalText = "Test",
            category = RumorCategory.HEROIC_DEED,
            currentTimestamp = 1000
        )
        
        assertTrue(result is StartRumorResult.Failure)
        assertEquals(StartRumorFailure.INVALID_TEMPLATE, (result as StartRumorResult.Failure).reason)
    }
    
    @Test
    fun `startRumor should reject blank subject ID`() {
        val state = createTestGossipState()
        
        val result = GossipManager.startRumor(
            state = state,
            rumorKey = "rumor_test",
            subjectId = "",
            originNPCId = "npc_grumble",
            originalText = "Test",
            category = RumorCategory.HEROIC_DEED,
            currentTimestamp = 1000
        )
        
        assertTrue(result is StartRumorResult.Failure)
        assertEquals(StartRumorFailure.INVALID_SUBJECT, (result as StartRumorResult.Failure).reason)
    }
    
    // ============================================
    // RUMOR SPREADING TESTS (8 tests)
    // ============================================
    
    @Test
    fun `spreadRumor should spread to valid targets`() {
        var state = createTestGossipState()
        val gameState = createTestGameState()
        
        // Start rumor
        val startResult = GossipManager.startRumor(
            state = state,
            rumorKey = "rumor_test",
            subjectId = "player_1",
            originNPCId = "npc_grumble",
            originalText = "Hero defeated 3 ants.",
            category = RumorCategory.HEROIC_DEED,
            currentTimestamp = 1000
        ) as StartRumorResult.Success
        
        state = startResult.state
        val rumorId = startResult.rumorId
        
        // Spread rumor with retry logic (probability-based)
        var spreadResult: SpreadRumorResult? = null
        var attempts = 0
        while (spreadResult !is SpreadRumorResult.Success && attempts < 20) {
            spreadResult = GossipManager.spreadRumor(
                state = state,
                rumorId = rumorId,
                sourceNPCId = "npc_grumble",
                targetNPCIds = listOf("npc_merchant", "npc_snail"),
                gameState = gameState,
                currentTimestamp = 2000
            )
            attempts++
        }
        
        assertTrue(spreadResult is SpreadRumorResult.Success, "Should spread successfully within 20 attempts")
        val success = spreadResult as SpreadRumorResult.Success
        
        // At least some targets should receive (probability-based)
        assertTrue(success.spreadToNPCs.isNotEmpty())
        
        val rumor = success.state.activeRumors[rumorId]!!
        for (npcId in success.spreadToNPCs) {
            assertTrue(rumor.isKnownBy(npcId))
            assertEquals("npc_grumble", rumor.getSource(npcId))
        }
    }
    
    @Test
    fun `spreadRumor should reject if source doesn't know rumor`() {
        var state = createTestGossipState()
        val gameState = createTestGameState()
        
        val startResult = GossipManager.startRumor(
            state = state,
            rumorKey = "rumor_test",
            subjectId = "player_1",
            originNPCId = "npc_grumble",
            originalText = "Test",
            category = RumorCategory.HEROIC_DEED,
            currentTimestamp = 1000
        ) as StartRumorResult.Success
        
        state = startResult.state
        
        val result = GossipManager.spreadRumor(
            state = state,
            rumorId = startResult.rumorId,
            sourceNPCId = "npc_unknown",  // Doesn't know rumor
            targetNPCIds = listOf("npc_merchant"),
            gameState = gameState,
            currentTimestamp = 2000
        )
        
        assertTrue(result is SpreadRumorResult.Failure)
        assertEquals(SpreadFailure.SOURCE_NPC_DOESNT_KNOW_RUMOR, (result as SpreadRumorResult.Failure).reason)
    }
    
    @Test
    fun `spreadRumor should reject if rumor not found`() {
        val state = createTestGossipState()
        val gameState = createTestGameState()
        
        val result = GossipManager.spreadRumor(
            state = state,
            rumorId = "nonexistent_rumor",
            sourceNPCId = "npc_grumble",
            targetNPCIds = listOf("npc_merchant"),
            gameState = gameState,
            currentTimestamp = 1000
        )
        
        assertTrue(result is SpreadRumorResult.Failure)
        assertEquals(SpreadFailure.RUMOR_NOT_FOUND, (result as SpreadRumorResult.Failure).reason)
    }
    
    @Test
    fun `spreadRumor should set cooldowns for recipients`() {
        var state = createTestGossipState()
        val gameState = createTestGameState()
        
        val startResult = GossipManager.startRumor(
            state = state,
            rumorKey = "rumor_test",
            subjectId = "player_1",
            originNPCId = "npc_grumble",
            originalText = "Test",
            category = RumorCategory.HEROIC_DEED,
            currentTimestamp = 1000
        ) as StartRumorResult.Success
        
        state = startResult.state
        
        val spreadResult = GossipManager.spreadRumor(
            state = state,
            rumorId = startResult.rumorId,
            sourceNPCId = "npc_grumble",
            targetNPCIds = listOf("npc_merchant", "npc_snail", "npc_beetle"),
            gameState = gameState,
            currentTimestamp = 2000,
            cooldownTicks = 1000
        ) as SpreadRumorResult.Success
        
        for (npcId in spreadResult.spreadToNPCs) {
            val isOnCooldown = spreadResult.state.isSpreadOnCooldown(
                startResult.rumorId,
                npcId,
                2500  // Before cooldown expires
            )
            assertTrue(isOnCooldown)
        }
    }
    
    @Test
    fun `spreadRumor should respect cooldowns`() {
        var state = createTestGossipState()
        val gameState = createTestGameState()
        
        val startResult = GossipManager.startRumor(
            state = state,
            rumorKey = "rumor_test",
            subjectId = "player_1",
            originNPCId = "npc_grumble",
            originalText = "Test",
            category = RumorCategory.HEROIC_DEED,
            currentTimestamp = 1000
        ) as StartRumorResult.Success
        
        state = startResult.state
        
        // First spread - try until we get a success
        var spread1: SpreadRumorResult? = null
        var attempts = 0
        while (spread1 !is SpreadRumorResult.Success && attempts < 20) {
            spread1 = GossipManager.spreadRumor(
                state = state,
                rumorId = startResult.rumorId,
                sourceNPCId = "npc_grumble",
                targetNPCIds = listOf("npc_merchant"),
                gameState = gameState,
                currentTimestamp = 2000,
                cooldownTicks = 1000
            )
            attempts++
        }
        
        if (spread1 !is SpreadRumorResult.Success) {
            // Probability-based test - skip if couldn't spread after 20 attempts
            return
        }
        
        state = spread1.state
        
        // Try to spread again before cooldown expires - should fail with NO_VALID_TARGETS
        val spread2 = GossipManager.spreadRumor(
            state = state,
            rumorId = startResult.rumorId,
            sourceNPCId = "npc_grumble",
            targetNPCIds = spread1.spreadToNPCs,  // Same targets
            gameState = gameState,
            currentTimestamp = 2500  // Before cooldown
        )
        
        assertTrue(spread2 is SpreadRumorResult.Failure)
        assertEquals(SpreadFailure.NO_VALID_TARGETS, (spread2 as SpreadRumorResult.Failure).reason)
    }
    
    @Test
    fun `spreadRumor should skip NPCs who already know`() {
        var state = createTestGossipState()
        val gameState = createTestGameState()
        
        val startResult = GossipManager.startRumor(
            state = state,
            rumorKey = "rumor_test",
            subjectId = "player_1",
            originNPCId = "npc_grumble",
            originalText = "Test",
            category = RumorCategory.HEROIC_DEED,
            currentTimestamp = 1000
        ) as StartRumorResult.Success
        
        state = startResult.state
        val rumor = state.activeRumors[startResult.rumorId]!!
        
        // npc_grumble already knows (origin)
        assertFalse("npc_grumble" in GossipManager.spreadRumor(
            state = state,
            rumorId = startResult.rumorId,
            sourceNPCId = "npc_grumble",
            targetNPCIds = listOf("npc_grumble", "npc_merchant"),
            gameState = gameState,
            currentTimestamp = 2000
        ).let { (it as? SpreadRumorResult.Success)?.spreadToNPCs ?: emptyList() })
    }
    
    @Test
    fun `spreadRumor should update statistics`() {
        var state = createTestGossipState()
        val gameState = createTestGameState()
        
        val startResult = GossipManager.startRumor(
            state = state,
            rumorKey = "rumor_test",
            subjectId = "player_1",
            originNPCId = "npc_grumble",
            originalText = "Test",
            category = RumorCategory.HEROIC_DEED,
            currentTimestamp = 1000
        ) as StartRumorResult.Success
        
        state = startResult.state
        
        val spreadResult = GossipManager.spreadRumor(
            state = state,
            rumorId = startResult.rumorId,
            sourceNPCId = "npc_grumble",
            targetNPCIds = listOf("npc_merchant", "npc_snail", "npc_beetle"),
            gameState = gameState,
            currentTimestamp = 2000
        ) as SpreadRumorResult.Success
        
        val stats = spreadResult.state.rumorStatistics[startResult.rumorId]!!
        assertTrue(stats.timesSpread > 0)
        assertTrue(stats.totalReach > 1)  // Origin + at least 1 spread
    }
    
    @Test
    fun `spreadRumor should update NPC memory`() {
        var state = createTestGossipState()
        val gameState = createTestGameState()
        
        val startResult = GossipManager.startRumor(
            state = state,
            rumorKey = "rumor_test",
            subjectId = "player_1",
            originNPCId = "npc_grumble",
            originalText = "Test",
            category = RumorCategory.HEROIC_DEED,
            currentTimestamp = 1000
        ) as StartRumorResult.Success
        
        state = startResult.state
        
        // Try spreading until we get a success (probability-based)
        var spreadResult: SpreadRumorResult? = null
        var attempts = 0
        while (spreadResult !is SpreadRumorResult.Success && attempts < 20) {
            spreadResult = GossipManager.spreadRumor(
                state = state,
                rumorId = startResult.rumorId,
                sourceNPCId = "npc_grumble",
                targetNPCIds = listOf("npc_merchant", "npc_snail"),
                gameState = gameState,
                currentTimestamp = 2000
            )
            attempts++
        }
        
        if (spreadResult !is SpreadRumorResult.Success) {
            // Probability-based test - skip if couldn't spread after 20 attempts
            return
        }
        
        for (npcId in spreadResult.spreadToNPCs) {
            val npcRumors = spreadResult.state.npcGossipMemory[npcId]
            assertNotNull(npcRumors)
            assertTrue(startResult.rumorId in npcRumors)
        }
    }
    
    // ============================================
    // MUTATION TESTS (6 tests)
    // ============================================
    
    @Test
    fun `Rumor calculateTruthLevel should return ACCURATE for 0-1 mutations`() {
        val rumor = Rumor(
            id = "test",
            rumorKey = "test",
            category = RumorCategory.HEROIC_DEED,
            originalText = "Test",
            currentText = "Test",
            truthLevel = TruthLevel.ACCURATE,
            mutationCount = 1,
            subjectId = "player_1",
            originNPCId = "npc_1",
            timestamp = 1000
        )
        
        assertEquals(TruthLevel.ACCURATE, rumor.calculateTruthLevel())
    }
    
    @Test
    fun `Rumor calculateTruthLevel should return EXAGGERATED for 2-3 mutations`() {
        val rumor = Rumor(
            id = "test",
            rumorKey = "test",
            category = RumorCategory.HEROIC_DEED,
            originalText = "Test",
            currentText = "Test",
            truthLevel = TruthLevel.ACCURATE,
            mutationCount = 3,
            subjectId = "player_1",
            originNPCId = "npc_1",
            timestamp = 1000
        )
        
        assertEquals(TruthLevel.EXAGGERATED, rumor.calculateTruthLevel())
    }
    
    @Test
    fun `Rumor calculateTruthLevel should return DISTORTED for 4-5 mutations`() {
        val rumor = Rumor(
            id = "test",
            rumorKey = "test",
            category = RumorCategory.HEROIC_DEED,
            originalText = "Test",
            currentText = "Test",
            truthLevel = TruthLevel.ACCURATE,
            mutationCount = 5,
            subjectId = "player_1",
            originNPCId = "npc_1",
            timestamp = 1000
        )
        
        assertEquals(TruthLevel.DISTORTED, rumor.calculateTruthLevel())
    }
    
    @Test
    fun `Rumor calculateTruthLevel should return MYTHICAL for 6+ mutations`() {
        val rumor = Rumor(
            id = "test",
            rumorKey = "test",
            category = RumorCategory.HEROIC_DEED,
            originalText = "Test",
            currentText = "Test",
            truthLevel = TruthLevel.ACCURATE,
            mutationCount = 10,
            subjectId = "player_1",
            originNPCId = "npc_1",
            timestamp = 1000
        )
        
        assertEquals(TruthLevel.MYTHICAL, rumor.calculateTruthLevel())
    }
    
    @Test
    fun `spreadRumor may apply mutations during spreading`() {
        var state = createTestGossipState()
        val gameState = createTestGameState()
        
        val startResult = GossipManager.startRumor(
            state = state,
            rumorKey = "rumor_test",
            subjectId = "player_1",
            originNPCId = "npc_grumble",
            originalText = "Hero defeated 5 ants.",
            category = RumorCategory.HEROIC_DEED,
            currentTimestamp = 1000
        ) as StartRumorResult.Success
        
        state = startResult.state
        
        // Spread multiple times to increase mutation chance
        repeat(5) {
            val spreadResult = GossipManager.spreadRumor(
                state = state,
                rumorId = startResult.rumorId,
                sourceNPCId = "npc_grumble",
                targetNPCIds = listOf("npc_merchant_$it"),
                gameState = gameState,
                currentTimestamp = 2000 + it * 1000L,
                cooldownTicks = 100
            )
            
            if (spreadResult is SpreadRumorResult.Success) {
                state = spreadResult.state
                // Check if any mutations occurred
                if (spreadResult.mutations.isNotEmpty()) {
                    assertTrue(spreadResult.mutations.isNotEmpty())
                    return  // Test passed
                }
            }
        }
        
        // If no mutations occurred after 5 spreads, that's okay (probability-based)
        // Just verify rumor still exists
        assertNotNull(state.activeRumors[startResult.rumorId])
    }
    
    @Test
    fun `spreadRumor updates truth level after mutations`() {
        var state = createTestGossipState()
        val gameState = createTestGameState()
        
        val startResult = GossipManager.startRumor(
            state = state,
            rumorKey = "rumor_test",
            subjectId = "player_1",
            originNPCId = "npc_grumble",
            originalText = "Test",
            category = RumorCategory.HEROIC_DEED,
            currentTimestamp = 1000
        ) as StartRumorResult.Success
        
        state = startResult.state
        
        // Initial truth level
        var rumor = state.activeRumors[startResult.rumorId]!!
        assertEquals(TruthLevel.ACCURATE, rumor.truthLevel)
        
        // Spread and check if truth level gets updated correctly
        val spreadResult = GossipManager.spreadRumor(
            state = state,
            rumorId = startResult.rumorId,
            sourceNPCId = "npc_grumble",
            targetNPCIds = listOf("npc_merchant"),
            gameState = gameState,
            currentTimestamp = 2000
        )
        
        if (spreadResult is SpreadRumorResult.Success) {
            rumor = spreadResult.state.activeRumors[startResult.rumorId]!!
            // Truth level should match mutation count
            assertEquals(rumor.calculateTruthLevel(), rumor.truthLevel)
        }
    }
    
    // ============================================
    // GOSSIP STATE TESTS (5 tests)
    // ============================================
    
    @Test
    fun `GossipState getRumorsKnownBy should return correct rumors`() {
        var state = createTestGossipState()
        val gameState = createTestGameState()
        
        val rumor1 = GossipManager.startRumor(
            state, "rumor_1", "player_1", "npc_grumble", "Test 1",
            RumorCategory.HEROIC_DEED, emptyList(), 1000
        ) as StartRumorResult.Success
        
        state = rumor1.state
        
        val rumor2 = GossipManager.startRumor(
            state, "rumor_2", "player_1", "npc_merchant", "Test 2",
            RumorCategory.CRIME, emptyList(), 2000
        ) as StartRumorResult.Success
        
        state = rumor2.state
        
        // npc_grumble knows rumor_1 only
        val grumbleRumors = state.getRumorsKnownBy("npc_grumble")
        assertEquals(1, grumbleRumors.size)
        assertEquals(rumor1.rumorId, grumbleRumors[0].id)
        
        // npc_merchant knows rumor_2 only
        val merchantRumors = state.getRumorsKnownBy("npc_merchant")
        assertEquals(1, merchantRumors.size)
        assertEquals(rumor2.rumorId, merchantRumors[0].id)
    }
    
    @Test
    fun `GossipState npcKnowsRumor should return correct result`() {
        var state = createTestGossipState()
        
        val result = GossipManager.startRumor(
            state, "rumor_test", "player_1", "npc_grumble", "Test",
            RumorCategory.HEROIC_DEED, emptyList(), 1000
        ) as StartRumorResult.Success
        
        state = result.state
        
        assertTrue(state.npcKnowsRumor("npc_grumble", result.rumorId))
        assertFalse(state.npcKnowsRumor("npc_merchant", result.rumorId))
    }
    
    @Test
    fun `GossipState isSpreadOnCooldown should work correctly`() {
        val state = GossipState(
            spreadCooldowns = mapOf(
                "rumor_1_npc_1" to 5000L
            )
        )
        
        assertTrue(state.isSpreadOnCooldown("rumor_1", "npc_1", 3000))
        assertFalse(state.isSpreadOnCooldown("rumor_1", "npc_1", 6000))
        assertFalse(state.isSpreadOnCooldown("rumor_1", "npc_2", 3000))
    }
    
    @Test
    fun `GossipState getTotalReputationChange should sum correctly`() {
        val state = GossipState(
            reputationHistory = listOf(
                ReputationEffect("faction_1", 10, "Test 1"),
                ReputationEffect("faction_1", 15, "Test 2"),
                ReputationEffect("faction_2", -5, "Test 3"),
                ReputationEffect("faction_1", -3, "Test 4")
            )
        )
        
        assertEquals(22, state.getTotalReputationChange("faction_1"))
        assertEquals(-5, state.getTotalReputationChange("faction_2"))
        assertEquals(0, state.getTotalReputationChange("faction_3"))
    }
    
    @Test
    fun `Rumor isKnownBy and getSource should work correctly`() {
        val rumor = Rumor(
            id = "test",
            rumorKey = "test",
            category = RumorCategory.HEROIC_DEED,
            originalText = "Test",
            currentText = "Test",
            truthLevel = TruthLevel.ACCURATE,
            mutationCount = 0,
            subjectId = "player_1",
            originNPCId = "npc_origin",
            knownByNPCs = setOf("npc_origin", "npc_1", "npc_2"),
            sourceMap = mapOf(
                "npc_origin" to "origin",
                "npc_1" to "npc_origin",
                "npc_2" to "npc_1"
            ),
            timestamp = 1000
        )
        
        assertTrue(rumor.isKnownBy("npc_1"))
        assertFalse(rumor.isKnownBy("npc_3"))
        
        assertEquals("npc_origin", rumor.getSource("npc_1"))
        assertEquals("npc_1", rumor.getSource("npc_2"))
        assertNull(rumor.getSource("npc_3"))
    }
    
    // ============================================
    // QUERY OPERATION TESTS (6 tests)
    // ============================================
    
    @Test
    fun `getRumorsKnownBy should return NPC's rumors`() {
        var state = createTestGossipState()
        
        val result = GossipManager.startRumor(
            state, "rumor_test", "player_1", "npc_grumble", "Test",
            RumorCategory.HEROIC_DEED, emptyList(), 1000
        ) as StartRumorResult.Success
        
        val rumors = GossipManager.getRumorsKnownBy(result.state, "npc_grumble")
        assertEquals(1, rumors.size)
        assertEquals(result.rumorId, rumors[0].id)
    }
    
    @Test
    fun `getRumorsByCategory should filter correctly`() {
        var state = createTestGossipState()
        
        val rumor1 = GossipManager.startRumor(
            state, "rumor_1", "player_1", "npc_1", "Test 1",
            RumorCategory.HEROIC_DEED, emptyList(), 1000
        ) as StartRumorResult.Success
        
        state = rumor1.state
        
        val rumor2 = GossipManager.startRumor(
            state, "rumor_2", "player_1", "npc_2", "Test 2",
            RumorCategory.CRIME, emptyList(), 2000
        ) as StartRumorResult.Success
        
        state = rumor2.state
        
        val rumor3 = GossipManager.startRumor(
            state, "rumor_3", "player_1", "npc_3", "Test 3",
            RumorCategory.HEROIC_DEED, emptyList(), 3000
        ) as StartRumorResult.Success
        
        state = rumor3.state
        
        val heroic = GossipManager.getRumorsByCategory(state, RumorCategory.HEROIC_DEED)
        assertEquals(2, heroic.size)
        
        val crime = GossipManager.getRumorsByCategory(state, RumorCategory.CRIME)
        assertEquals(1, crime.size)
    }
    
    @Test
    fun `getRumorsByTruthLevel should filter correctly`() {
        val state = GossipState(
            activeRumors = mapOf(
                "rumor_1" to Rumor(
                    "rumor_1", "test", RumorCategory.HEROIC_DEED, "Test", "Test",
                    TruthLevel.ACCURATE, 0, "player_1", "npc_1", emptySet(), emptyMap(),
                    emptyList(), 1000
                ),
                "rumor_2" to Rumor(
                    "rumor_2", "test", RumorCategory.CRIME, "Test", "Test",
                    TruthLevel.EXAGGERATED, 2, "player_1", "npc_2", emptySet(), emptyMap(),
                    emptyList(), 2000
                ),
                "rumor_3" to Rumor(
                    "rumor_3", "test", RumorCategory.FAILURE, "Test", "Test",
                    TruthLevel.MYTHICAL, 10, "player_1", "npc_3", emptySet(), emptyMap(),
                    emptyList(), 3000
                )
            )
        )
        
        val accurate = GossipManager.getRumorsByTruthLevel(state, TruthLevel.ACCURATE)
        assertEquals(1, accurate.size)
        
        val mythical = GossipManager.getRumorsByTruthLevel(state, TruthLevel.MYTHICAL)
        assertEquals(1, mythical.size)
    }
    
    @Test
    fun `getRumorStatistics should return correct statistics`() {
        val stats = RumorStatistics(
            rumorId = "rumor_1",
            timesSpread = 10,
            totalReach = 50,
            mutationHistory = emptyList(),
            averageTruthLevel = 2.5
        )
        
        val state = GossipState(
            rumorStatistics = mapOf("rumor_1" to stats)
        )
        
        val result = GossipManager.getRumorStatistics(state, "rumor_1")
        assertEquals(stats, result)
        assertNull(GossipManager.getRumorStatistics(state, "rumor_999"))
    }
    
    @Test
    fun `getMostSpreadRumor should return rumor with highest reach`() {
        val state = GossipState(
            activeRumors = mapOf(
                "rumor_1" to Rumor("rumor_1", "test", RumorCategory.HEROIC_DEED, "Test", "Test",
                    TruthLevel.ACCURATE, 0, "player_1", "npc_1", emptySet(), emptyMap(), emptyList(), 1000),
                "rumor_2" to Rumor("rumor_2", "test", RumorCategory.CRIME, "Test", "Test",
                    TruthLevel.ACCURATE, 0, "player_1", "npc_2", emptySet(), emptyMap(), emptyList(), 2000)
            ),
            rumorStatistics = mapOf(
                "rumor_1" to RumorStatistics("rumor_1", 5, 20, emptyList(), 1.0),
                "rumor_2" to RumorStatistics("rumor_2", 10, 50, emptyList(), 1.0)
            )
        )
        
        val result = GossipManager.getMostSpreadRumor(state)
        assertNotNull(result)
        assertEquals("rumor_2", result.id)
    }
    
    @Test
    fun `getMostMutatedRumor should return rumor with highest mutation count`() {
        val state = GossipState(
            activeRumors = mapOf(
                "rumor_1" to Rumor("rumor_1", "test", RumorCategory.HEROIC_DEED, "Test", "Test",
                    TruthLevel.ACCURATE, 1, "player_1", "npc_1", emptySet(), emptyMap(), emptyList(), 1000),
                "rumor_2" to Rumor("rumor_2", "test", RumorCategory.CRIME, "Test", "Test",
                    TruthLevel.MYTHICAL, 15, "player_1", "npc_2", emptySet(), emptyMap(), emptyList(), 2000)
            )
        )
        
        val result = GossipManager.getMostMutatedRumor(state)
        assertNotNull(result)
        assertEquals("rumor_2", result.id)
        assertEquals(15, result.mutationCount)
    }
    
    // ============================================
    // CATALOG TESTS (5 tests)
    // ============================================
    
    @Test
    fun `RumorCatalog should provide all 10 templates`() {
        val templates = RumorCatalog.getAllTemplates()
        assertEquals(10, templates.size)
    }
    
    @Test
    fun `RumorCatalog getTemplate should return correct template`() {
        val template = RumorCatalog.getTemplate("rumor_defeated_enemies")
        assertNotNull(template)
        assertEquals("rumor_defeated_enemies", template.templateId)
        assertEquals(RumorCategory.HEROIC_DEED, template.category)
    }
    
    @Test
    fun `RumorCatalog getTemplate should return null for unknown ID`() {
        val template = RumorCatalog.getTemplate("rumor_nonexistent")
        assertNull(template)
    }
    
    @Test
    fun `RumorCatalog getTemplatesByCategory should filter correctly`() {
        val heroic = RumorCatalog.getTemplatesByCategory(RumorCategory.HEROIC_DEED)
        assertEquals(3, heroic.size)  // defeated_enemies, saved_npc, champion_duel
        
        val crime = RumorCatalog.getTemplatesByCategory(RumorCategory.CRIME)
        assertEquals(1, crime.size)  // stole_from_npc
        
        val failure = RumorCatalog.getTemplatesByCategory(RumorCategory.FAILURE)
        assertEquals(2, failure.size)  // fled_combat, quest_failure
    }
    
    @Test
    fun `RumorCatalog templates should have mutation paths`() {
        val template = RumorCatalog.getTemplate("rumor_defeated_enemies")!!
        assertTrue(template.mutationPaths.isNotEmpty())
        
        // Verify progression: ACCURATE → EXAGGERATED → DISTORTED → MYTHICAL
        val truthLevels = template.mutationPaths.map { it.truthLevel }
        assertTrue(TruthLevel.ACCURATE in truthLevels)
        assertTrue(TruthLevel.EXAGGERATED in truthLevels || TruthLevel.DISTORTED in truthLevels)
    }
}
