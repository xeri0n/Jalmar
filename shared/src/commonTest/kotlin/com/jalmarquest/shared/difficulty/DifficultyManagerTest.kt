package com.jalmarquest.shared.difficulty

import kotlin.test.*

/**
 * Comprehensive tests for Adaptive Difficulty System.
 * 
 * Coverage:
 * - Combat performance tracking
 * - Quest performance tracking
 * - Skill rating calculations
 * - Difficulty adjustments
 * - Manual overrides
 * - Auto-adjust toggling
 * - Custom difficulty
 * - Modifier getters
 */
class DifficultyManagerTest {
    
    private fun createTestState(): DifficultyState {
        return DifficultyState(
            currentMetrics = DifficultyMetrics.fromLevel(DifficultyLevel.NORMAL),
            sessionStartTime = 1000L,
            totalPlayTime = 0L
        )
    }
    
    // ============================================
    // COMBAT PERFORMANCE TRACKING (6 tests)
    // ============================================
    
    @Test
    fun `trackCombatPerformance should update win rate`() {
        var state = createTestState()
        
        // Win 3 combats
        repeat(3) {
            val result = DifficultyManager.trackCombatPerformance(
                state, won = true, combatDurationSeconds = 60,
                damageTaken = 50, damageDealt = 100, healingItemsUsed = 0,
                currentTimestamp = 2000L
            )
            assertTrue(result is TrackPerformanceResult.Success)
            state = (result as TrackPerformanceResult.Success).state
        }
        
        // Lose 1 combat
        val result = DifficultyManager.trackCombatPerformance(
            state, won = false, combatDurationSeconds = 90,
            damageTaken = 100, damageDealt = 30, healingItemsUsed = 2,
            currentTimestamp = 3000L
        )
        
        assertTrue(result is TrackPerformanceResult.Success)
        val perf = (result as TrackPerformanceResult.Success).updatedPerformance
        
        // Win rate should be 3/4 = 0.75
        assertEquals(0.75, perf.combatWinRate, 0.01)
        assertEquals(4, perf.totalCombatsRecorded)
    }
    
    @Test
    fun `trackCombatPerformance should update damage efficiency`() {
        var state = createTestState()
        
        val result = DifficultyManager.trackCombatPerformance(
            state, won = true, combatDurationSeconds = 60,
            damageTaken = 50, damageDealt = 150, healingItemsUsed = 0,
            currentTimestamp = 2000L
        )
        
        assertTrue(result is TrackPerformanceResult.Success)
        val perf = (result as TrackPerformanceResult.Success).updatedPerformance
        
        // Damage efficiency = 150 / 50 = 3.0
        assertEquals(3.0, perf.damageEfficiency, 0.01)
    }
    
    @Test
    fun `trackCombatPerformance should update healing usage`() {
        var state = createTestState()
        
        // First combat: 2 items
        var result = DifficultyManager.trackCombatPerformance(
            state, won = true, combatDurationSeconds = 60,
            damageTaken = 80, damageDealt = 100, healingItemsUsed = 2,
            currentTimestamp = 2000L
        ) as TrackPerformanceResult.Success
        
        state = result.state
        
        // Second combat: 0 items
        result = DifficultyManager.trackCombatPerformance(
            state, won = true, combatDurationSeconds = 45,
            damageTaken = 20, damageDealt = 120, healingItemsUsed = 0,
            currentTimestamp = 3000L
        ) as TrackPerformanceResult.Success
        
        val perf = result.updatedPerformance
        
        // Average healing = (2 + 0) / 2 = 1.0
        assertEquals(1.0, perf.healingItemUsageRate, 0.01)
    }
    
    @Test
    fun `trackCombatPerformance should calculate combat skill rating`() {
        var state = createTestState()
        
        // Win 10 combats with good performance
        repeat(10) {
            val result = DifficultyManager.trackCombatPerformance(
                state, won = true, combatDurationSeconds = 45,
                damageTaken = 30, damageDealt = 120, healingItemsUsed = 0,
                currentTimestamp = 2000L + it * 100
            )
            state = (result as TrackPerformanceResult.Success).state
        }
        
        val combatSkill = state.getSkillRating(SkillCategory.COMBAT)
        
        // Good performance should result in skill > 1.0
        assertTrue(combatSkill > 1.0, "Combat skill should be above average")
    }
    
    @Test
    fun `trackCombatPerformance should reject invalid data`() {
        val state = createTestState()
        
        val result = DifficultyManager.trackCombatPerformance(
            state, won = true, combatDurationSeconds = -10,  // Invalid
            damageTaken = 50, damageDealt = 100, healingItemsUsed = 0,
            currentTimestamp = 2000L
        )
        
        assertTrue(result is TrackPerformanceResult.Failure)
        assertEquals(TrackPerformanceFailure.INVALID_DATA, (result as TrackPerformanceResult.Failure).reason)
    }
    
    @Test
    fun `trackCombatPerformance should use rolling average after 20 combats`() {
        var state = createTestState()
        
        // Record 25 combats (all wins)
        repeat(25) {
            val result = DifficultyManager.trackCombatPerformance(
                state, won = true, combatDurationSeconds = 60,
                damageTaken = 50, damageDealt = 100, healingItemsUsed = 0,
                currentTimestamp = 2000L + it * 100
            )
            state = (result as TrackPerformanceResult.Success).state
        }
        
        // Add 1 loss - should affect rolling average
        val result = DifficultyManager.trackCombatPerformance(
            state, won = false, combatDurationSeconds = 120,
            damageTaken = 100, damageDealt = 30, healingItemsUsed = 3,
            currentTimestamp = 5000L
        ) as TrackPerformanceResult.Success
        
        val perf = result.updatedPerformance
        
        // Win rate should be close to 0.95 (19 wins in last 20)
        assertTrue(perf.combatWinRate > 0.9, "Win rate should reflect rolling window")
        assertEquals(26, perf.totalCombatsRecorded)
    }
    
    // ============================================
    // QUEST PERFORMANCE TRACKING (5 tests)
    // ============================================
    
    @Test
    fun `trackQuestPerformance should update completion rate`() {
        var state = createTestState()
        
        // Complete 3 quests
        repeat(3) {
            val result = DifficultyManager.trackQuestPerformance(
                state, completed = true,
                optionalObjectivesCompleted = 2, optionalObjectivesTotal = 3,
                currentTimestamp = 2000L
            )
            state = (result as TrackPerformanceResult.Success).state
        }
        
        // Fail 1 quest
        val result = DifficultyManager.trackQuestPerformance(
            state, completed = false,
            optionalObjectivesCompleted = 0, optionalObjectivesTotal = 2,
            currentTimestamp = 3000L
        )
        
        assertTrue(result is TrackPerformanceResult.Success)
        val perf = (result as TrackPerformanceResult.Success).updatedPerformance
        
        // Completion rate = 3/4 = 0.75
        assertEquals(0.75, perf.questCompletionRate, 0.01)
        assertEquals(4, perf.totalQuestsRecorded)
    }
    
    @Test
    fun `trackQuestPerformance should update optional objectives rate`() {
        var state = createTestState()
        
        // Quest 1: 2/3 optional
        var result = DifficultyManager.trackQuestPerformance(
            state, completed = true,
            optionalObjectivesCompleted = 2, optionalObjectivesTotal = 3,
            currentTimestamp = 2000L
        ) as TrackPerformanceResult.Success
        
        state = result.state
        
        // Quest 2: 1/2 optional
        result = DifficultyManager.trackQuestPerformance(
            state, completed = true,
            optionalObjectivesCompleted = 1, optionalObjectivesTotal = 2,
            currentTimestamp = 3000L
        ) as TrackPerformanceResult.Success
        
        val perf = result.updatedPerformance
        
        // Average optional rate = ((2/3) + (1/2)) / 2 = 0.583
        assertTrue(perf.optionalObjectivesRate in 0.58..0.59)
    }
    
    @Test
    fun `trackQuestPerformance should calculate exploration skill`() {
        var state = createTestState()
        
        // Complete 5 quests with good optional objectives
        repeat(5) {
            val result = DifficultyManager.trackQuestPerformance(
                state, completed = true,
                optionalObjectivesCompleted = 3, optionalObjectivesTotal = 3,
                currentTimestamp = 2000L + it * 100
            )
            state = (result as TrackPerformanceResult.Success).state
        }
        
        val explorationSkill = state.getSkillRating(SkillCategory.EXPLORATION)
        
        // Perfect quest completion should result in high skill
        assertTrue(explorationSkill > 1.0, "Exploration skill should be above average")
    }
    
    @Test
    fun `trackQuestPerformance should reject invalid data`() {
        val state = createTestState()
        
        val result = DifficultyManager.trackQuestPerformance(
            state, completed = true,
            optionalObjectivesCompleted = 5, optionalObjectivesTotal = 3,  // Invalid
            currentTimestamp = 2000L
        )
        
        assertTrue(result is TrackPerformanceResult.Failure)
        assertEquals(TrackPerformanceFailure.INVALID_DATA, (result as TrackPerformanceResult.Failure).reason)
    }
    
    @Test
    fun `trackQuestPerformance should handle quests with no optional objectives`() {
        var state = createTestState()
        
        val result = DifficultyManager.trackQuestPerformance(
            state, completed = true,
            optionalObjectivesCompleted = 0, optionalObjectivesTotal = 0,
            currentTimestamp = 2000L
        )
        
        assertTrue(result is TrackPerformanceResult.Success)
        val perf = (result as TrackPerformanceResult.Success).updatedPerformance
        
        // Should treat as 100% optional completion
        assertEquals(1.0, perf.optionalObjectivesRate, 0.01)
    }
    
    // ============================================
    // DIFFICULTY ADJUSTMENT (8 tests)
    // ============================================
    
    @Test
    fun `adjustDifficulty should increase difficulty for high skill`() {
        var state = createTestState()
        
        // Record 15 perfect combats (high skill)
        repeat(15) {
            val result = DifficultyManager.trackCombatPerformance(
                state, won = true, combatDurationSeconds = 30,
                damageTaken = 10, damageDealt = 200, healingItemsUsed = 0,
                currentTimestamp = 2000L + it * 100
            )
            state = (result as TrackPerformanceResult.Success).state
        }
        
        // Adjust difficulty
        val result = DifficultyManager.adjustDifficulty(
            state, currentTimestamp = 20000L
        )
        
        assertTrue(result is AdjustDifficultyResult.Success)
        val success = result as AdjustDifficultyResult.Success
        
        assertNotNull(success.adjustment)
        assertTrue(success.adjustment!!.toLevel.ordinal > DifficultyLevel.NORMAL.ordinal,
            "Difficulty should increase for high skill")
    }
    
    @Test
    fun `adjustDifficulty should decrease difficulty for low skill`() {
        var state = createTestState()
        
        // Record 15 poor combats (low skill)
        repeat(15) {
            val result = DifficultyManager.trackCombatPerformance(
                state, won = false, combatDurationSeconds = 120,
                damageTaken = 150, damageDealt = 30, healingItemsUsed = 3,
                currentTimestamp = 2000L + it * 100
            )
            state = (result as TrackPerformanceResult.Success).state
        }
        
        // Adjust difficulty
        val result = DifficultyManager.adjustDifficulty(
            state, currentTimestamp = 20000L
        )
        
        assertTrue(result is AdjustDifficultyResult.Success)
        val success = result as AdjustDifficultyResult.Success
        
        assertNotNull(success.adjustment)
        assertTrue(success.adjustment!!.toLevel.ordinal < DifficultyLevel.NORMAL.ordinal,
            "Difficulty should decrease for low skill")
    }
    
    @Test
    fun `adjustDifficulty should respect cooldown`() {
        var state = createTestState().copy(lastAdjustmentTime = 10000L)
        
        // Record combats
        repeat(15) {
            val result = DifficultyManager.trackCombatPerformance(
                state, won = true, combatDurationSeconds = 30,
                damageTaken = 10, damageDealt = 200, healingItemsUsed = 0,
                currentTimestamp = 11000L + it * 100
            )
            state = (result as TrackPerformanceResult.Success).state
        }
        
        // Try to adjust too soon (cooldown = 12000 ticks)
        val result = DifficultyManager.adjustDifficulty(
            state, currentTimestamp = 15000L  // Only 5000 ticks since last
        )
        
        assertTrue(result is AdjustDifficultyResult.Failure)
        assertEquals(AdjustDifficultyFailure.COOLDOWN_ACTIVE, (result as AdjustDifficultyResult.Failure).reason)
    }
    
    @Test
    fun `adjustDifficulty should require minimum samples`() {
        var state = createTestState()
        
        // Record only 5 combats (less than MIN_COMBAT_SAMPLES = 10)
        repeat(5) {
            val result = DifficultyManager.trackCombatPerformance(
                state, won = true, combatDurationSeconds = 60,
                damageTaken = 50, damageDealt = 100, healingItemsUsed = 0,
                currentTimestamp = 2000L + it * 100
            )
            state = (result as TrackPerformanceResult.Success).state
        }
        
        val result = DifficultyManager.adjustDifficulty(
            state, currentTimestamp = 20000L
        )
        
        assertTrue(result is AdjustDifficultyResult.Failure)
        assertEquals(AdjustDifficultyFailure.INSUFFICIENT_DATA, (result as AdjustDifficultyResult.Failure).reason)
    }
    
    @Test
    fun `adjustDifficulty should fail if auto-adjust disabled`() {
        var state = createTestState()
        state = DifficultyManager.setAutoAdjust(state, enabled = false)
        
        // Record combats
        repeat(15) {
            val result = DifficultyManager.trackCombatPerformance(
                state, won = true, combatDurationSeconds = 30,
                damageTaken = 10, damageDealt = 200, healingItemsUsed = 0,
                currentTimestamp = 2000L + it * 100
            )
            state = (result as TrackPerformanceResult.Success).state
        }
        
        val result = DifficultyManager.adjustDifficulty(
            state, currentTimestamp = 20000L
        )
        
        assertTrue(result is AdjustDifficultyResult.Failure)
        assertEquals(AdjustDifficultyFailure.AUTO_ADJUST_DISABLED, (result as AdjustDifficultyResult.Failure).reason)
    }
    
    @Test
    fun `adjustDifficulty should limit to 1 level change`() {
        var state = createTestState().copy(
            currentMetrics = DifficultyMetrics.fromLevel(DifficultyLevel.STORY_MODE)
        )
        
        // Record 15 perfect combats (should push to BRUTAL, but limit to EASY)
        repeat(15) {
            val result = DifficultyManager.trackCombatPerformance(
                state, won = true, combatDurationSeconds = 20,
                damageTaken = 5, damageDealt = 300, healingItemsUsed = 0,
                currentTimestamp = 2000L + it * 100
            )
            state = (result as TrackPerformanceResult.Success).state
        }
        
        val result = DifficultyManager.adjustDifficulty(
            state, currentTimestamp = 20000L
        )
        
        assertTrue(result is AdjustDifficultyResult.Success)
        val success = result as AdjustDifficultyResult.Success
        
        assertNotNull(success.adjustment)
        // Should only go from STORY_MODE to EASY (max +1 level)
        assertEquals(DifficultyLevel.EASY, success.adjustment!!.toLevel)
    }
    
    @Test
    fun `adjustDifficulty should not change if skill is average`() {
        var state = createTestState()
        
        // Record 15 average combats (50% win rate, balanced stats)
        repeat(15) { i ->
            val won = i % 2 == 0  // 50% win rate
            val result = DifficultyManager.trackCombatPerformance(
                state, won = won, combatDurationSeconds = 60,
                damageTaken = 50, damageDealt = 50, healingItemsUsed = 1,
                currentTimestamp = 2000L + i * 100
            )
            state = (result as TrackPerformanceResult.Success).state
        }
        
        val result = DifficultyManager.adjustDifficulty(
            state, currentTimestamp = 20000L
        )
        
        assertTrue(result is AdjustDifficultyResult.Success)
        val success = result as AdjustDifficultyResult.Success
        
        // No adjustment needed for average performance
        assertNull(success.adjustment)
        assertEquals(DifficultyLevel.NORMAL, success.state.currentMetrics.level)
    }
    
    @Test
    fun `adjustDifficulty should create adjustment record`() {
        var state = createTestState()
        
        // Record high-skill combats
        repeat(15) {
            val result = DifficultyManager.trackCombatPerformance(
                state, won = true, combatDurationSeconds = 30,
                damageTaken = 10, damageDealt = 200, healingItemsUsed = 0,
                currentTimestamp = 2000L + it * 100
            )
            state = (result as TrackPerformanceResult.Success).state
        }
        
        val result = DifficultyManager.adjustDifficulty(
            state, currentTimestamp = 20000L,
            trigger = AdjustmentTrigger.PERFORMANCE_THRESHOLD
        )
        
        assertTrue(result is AdjustDifficultyResult.Success)
        val success = result as AdjustDifficultyResult.Success
        
        assertNotNull(success.adjustment)
        assertEquals(DifficultyLevel.NORMAL, success.adjustment!!.fromLevel)
        assertEquals(AdjustmentTrigger.PERFORMANCE_THRESHOLD, success.adjustment!!.triggeredBy)
        assertTrue(success.adjustment!!.reason.isNotBlank())
    }
    
    // ============================================
    // MANUAL DIFFICULTY CONTROL (5 tests)
    // ============================================
    
    @Test
    fun `setManualDifficulty should change difficulty`() {
        val state = createTestState()
        
        val result = DifficultyManager.setManualDifficulty(
            state, level = DifficultyLevel.HARD, currentTimestamp = 5000L
        )
        
        assertTrue(result is AdjustDifficultyResult.Success)
        val success = result as AdjustDifficultyResult.Success
        
        assertEquals(DifficultyLevel.HARD, success.state.currentMetrics.level)
        assertNotNull(success.adjustment)
        assertEquals(AdjustmentTrigger.PLAYER_MANUAL, success.adjustment!!.triggeredBy)
    }
    
    @Test
    fun `setManualDifficulty should not create adjustment if no change`() {
        val state = createTestState()
        
        val result = DifficultyManager.setManualDifficulty(
            state, level = DifficultyLevel.NORMAL, currentTimestamp = 5000L
        )
        
        assertTrue(result is AdjustDifficultyResult.Success)
        val success = result as AdjustDifficultyResult.Success
        
        assertNull(success.adjustment)
    }
    
    @Test
    fun `setAutoAdjust should toggle auto-adjustment`() {
        var state = createTestState()
        
        // Disable auto-adjust
        state = DifficultyManager.setAutoAdjust(state, enabled = false)
        assertFalse(state.currentMetrics.autoAdjustEnabled)
        
        // Re-enable
        state = DifficultyManager.setAutoAdjust(state, enabled = true)
        assertTrue(state.currentMetrics.autoAdjustEnabled)
    }
    
    @Test
    fun `setCustomDifficulty should create custom difficulty`() {
        val state = createTestState()
        
        val customMetrics = DifficultyMetrics(
            level = DifficultyLevel.CUSTOM,
            enemyDamageMultiplier = 2.0,
            enemyHealthMultiplier = 1.5,
            lootQualityMultiplier = 0.5,
            lootQuantityMultiplier = 0.75,
            xpMultiplier = 0.8,
            staminaRegenMultiplier = 0.9
        )
        
        val result = DifficultyManager.setCustomDifficulty(
            state, metrics = customMetrics, currentTimestamp = 5000L
        )
        
        assertTrue(result is AdjustDifficultyResult.Success)
        val success = result as AdjustDifficultyResult.Success
        
        assertEquals(DifficultyLevel.CUSTOM, success.state.currentMetrics.level)
        assertFalse(success.state.currentMetrics.autoAdjustEnabled)
        assertEquals(2.0, success.state.currentMetrics.enemyDamageMultiplier)
    }
    
    @Test
    fun `adjustDifficulty should fail for custom difficulty`() {
        var state = createTestState()
        
        // Set custom difficulty
        val customMetrics = DifficultyMetrics(level = DifficultyLevel.CUSTOM)
        val setResult = DifficultyManager.setCustomDifficulty(
            state, metrics = customMetrics, currentTimestamp = 2000L
        )
        state = (setResult as AdjustDifficultyResult.Success).state
        
        // Record combats
        repeat(15) {
            val result = DifficultyManager.trackCombatPerformance(
                state, won = true, combatDurationSeconds = 30,
                damageTaken = 10, damageDealt = 200, healingItemsUsed = 0,
                currentTimestamp = 3000L + it * 100
            )
            state = (result as TrackPerformanceResult.Success).state
        }
        
        // Try to auto-adjust
        val adjustResult = DifficultyManager.adjustDifficulty(
            state, currentTimestamp = 20000L
        )
        
        assertTrue(adjustResult is AdjustDifficultyResult.Failure)
        assertEquals(AdjustDifficultyFailure.CUSTOM_DIFFICULTY, 
            (adjustResult as AdjustDifficultyResult.Failure).reason)
    }
    
    // ============================================
    // DIFFICULTY METRICS & MODIFIERS (6 tests)
    // ============================================
    
    @Test
    fun `DifficultyMetrics fromLevel should create STORY_MODE metrics`() {
        val metrics = DifficultyMetrics.fromLevel(DifficultyLevel.STORY_MODE)
        
        assertEquals(DifficultyLevel.STORY_MODE, metrics.level)
        assertEquals(0.5, metrics.enemyDamageMultiplier)
        assertEquals(0.75, metrics.enemyHealthMultiplier)
        assertEquals(2.0, metrics.lootQualityMultiplier)
        assertEquals(1.5, metrics.xpMultiplier)
    }
    
    @Test
    fun `DifficultyMetrics fromLevel should create BRUTAL metrics`() {
        val metrics = DifficultyMetrics.fromLevel(DifficultyLevel.BRUTAL)
        
        assertEquals(DifficultyLevel.BRUTAL, metrics.level)
        assertEquals(1.5, metrics.enemyDamageMultiplier)
        assertEquals(1.3, metrics.enemyHealthMultiplier)
        assertEquals(0.5, metrics.lootQualityMultiplier)
        assertEquals(0.75, metrics.xpMultiplier)
    }
    
    @Test
    fun `getCombatModifiers should return correct values`() {
        val state = createTestState().copy(
            currentMetrics = DifficultyMetrics.fromLevel(DifficultyLevel.HARD)
        )
        
        val (damage, health) = DifficultyManager.getCombatModifiers(state)
        
        assertEquals(1.25, damage)
        assertEquals(1.15, health)
    }
    
    @Test
    fun `getLootModifiers should return correct values`() {
        val state = createTestState().copy(
            currentMetrics = DifficultyMetrics.fromLevel(DifficultyLevel.EASY)
        )
        
        val (quality, quantity) = DifficultyManager.getLootModifiers(state)
        
        assertEquals(1.5, quality)
        assertEquals(1.25, quantity)
    }
    
    @Test
    fun `getXPModifier should return correct value`() {
        val state = createTestState().copy(
            currentMetrics = DifficultyMetrics.fromLevel(DifficultyLevel.HARD)
        )
        
        val xpMod = DifficultyManager.getXPModifier(state)
        
        assertEquals(0.9, xpMod)
    }
    
    @Test
    fun `getStaminaRegenModifier should return correct value`() {
        val state = createTestState().copy(
            currentMetrics = DifficultyMetrics.fromLevel(DifficultyLevel.BRUTAL)
        )
        
        val staminaMod = DifficultyManager.getStaminaRegenModifier(state)
        
        assertEquals(0.75, staminaMod)
    }
    
    // ============================================
    // DIFFICULTY STATE HELPERS (4 tests)
    // ============================================
    
    @Test
    fun `DifficultyState getSkillRating should return default for missing category`() {
        val state = createTestState()
        
        val skill = state.getSkillRating(SkillCategory.SOCIAL)
        
        assertEquals(1.0, skill)  // Default average skill
    }
    
    @Test
    fun `DifficultyState getOverallSkillRating should average all categories`() {
        val state = createTestState().copy(
            skillRatings = mapOf(
                SkillCategory.COMBAT to SkillRating(SkillCategory.COMBAT, 1.5, 10, 1000L),
                SkillCategory.EXPLORATION to SkillRating(SkillCategory.EXPLORATION, 0.5, 5, 1000L)
            )
        )
        
        val overall = state.getOverallSkillRating()
        
        // Average of 1.5 and 0.5 = 1.0
        assertEquals(1.0, overall)
    }
    
    @Test
    fun `DifficultyState canAdjust should respect cooldown`() {
        val state = createTestState().copy(lastAdjustmentTime = 10000L)
        
        assertFalse(state.canAdjust(currentTime = 15000L, cooldownTicks = 12000L))
        assertTrue(state.canAdjust(currentTime = 25000L, cooldownTicks = 12000L))
    }
    
    @Test
    fun `DifficultyState getLastAdjustment should return most recent`() {
        val adj1 = DifficultyAdjustment(
            timestamp = 1000L,
            fromLevel = DifficultyLevel.NORMAL,
            toLevel = DifficultyLevel.HARD,
            reason = "First",
            triggeredBy = AdjustmentTrigger.PERFORMANCE_THRESHOLD,
            playerSkillRating = 1.3
        )
        
        val adj2 = DifficultyAdjustment(
            timestamp = 2000L,
            fromLevel = DifficultyLevel.HARD,
            toLevel = DifficultyLevel.NORMAL,
            reason = "Second",
            triggeredBy = AdjustmentTrigger.PLAYER_MANUAL,
            playerSkillRating = 1.0
        )
        
        val state = createTestState().copy(
            adjustmentHistory = listOf(adj1, adj2)
        )
        
        val last = state.getLastAdjustment()
        assertNotNull(last)
        assertEquals(2000L, last.timestamp)
        assertEquals("Second", last.reason)
    }
}
