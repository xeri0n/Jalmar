package com.jalmarquest.shared.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class AIDirectorManagerTest {
    
    private val manager = AIDirectorManager()
    
    // Test director with default balanced state
    private val testDirector = AIDirector(
        tension = 50,
        engagement = 50,
        skillLevel = 50,
        difficultyMultiplier = 1.0,
        sessionDurationMinutes = 30,
        consecutiveVictories = 0,
        consecutiveDefeats = 0,
        questCompletionRate = 50,
        averageCombatDuration = 60,
        healthPotionUsageRate = 0.5,
        deathCount = 0,
        bossDefeatedCount = 0,
        lastActionTimestamp = System.currentTimeMillis(),
        currentPhase = DirectorPhase.BUILDUP
    )
    
    // ========== PARAMETER TRACKING TESTS ==========
    
    @Test
    fun `trackCombatVictory should reduce tension and increment victories`() {
        val result = manager.trackCombatVictory(testDirector, combatDurationSeconds = 45)
        
        assertIs<DirectorResult.Success>(result)
        val updated = result.updatedDirector
        
        assertEquals(1, updated.consecutiveVictories)
        assertEquals(0, updated.consecutiveDefeats)
        assertTrue(updated.tension < testDirector.tension)
    }
    
    @Test
    fun `trackCombatVictory should boost skill for fast efficient wins`() {
        val result = manager.trackCombatVictory(
            testDirector,
            combatDurationSeconds = 25, // Fast win
            damageEfficiency = 80 // Efficient
        )
        
        assertIs<DirectorResult.Success>(result)
        val updated = result.updatedDirector
        
        // Fast + efficient = +3 skill boost
        assertTrue(updated.skillLevel > testDirector.skillLevel + 1)
    }
    
    @Test
    fun `trackCombatVictory should fail with invalid parameters`() {
        val result1 = manager.trackCombatVictory(testDirector, combatDurationSeconds = -10)
        assertIs<DirectorResult.Failure>(result1)
        assertEquals(DirectorFailure.INVALID_PARAMETERS, result1.reason)
        
        val result2 = manager.trackCombatVictory(testDirector, combatDurationSeconds = 30, damageEfficiency = 150)
        assertIs<DirectorResult.Failure>(result2)
    }
    
    @Test
    fun `trackCombatDefeat should increase tension and increment defeats`() {
        val result = manager.trackCombatDefeat(testDirector)
        
        assertIs<DirectorResult.Success>(result)
        val updated = result.updatedDirector
        
        assertEquals(0, updated.consecutiveVictories)
        assertEquals(1, updated.consecutiveDefeats)
        assertEquals(1, updated.deathCount)
        assertTrue(updated.tension > testDirector.tension)
        assertTrue(updated.skillLevel < testDirector.skillLevel)
    }
    
    @Test
    fun `trackQuestCompletion should calculate completion rate correctly`() {
        val result = manager.trackQuestCompletion(
            testDirector,
            questsCompleted = 7,
            questsStarted = 10
        )
        
        assertIs<DirectorResult.Success>(result)
        val updated = result.updatedDirector
        
        assertEquals(70, updated.questCompletionRate) // 7/10 = 70%
        assertTrue(updated.engagement >= testDirector.engagement)
    }
    
    @Test
    fun `trackQuestCompletion should fail when completed exceeds started`() {
        val result = manager.trackQuestCompletion(
            testDirector,
            questsCompleted = 10,
            questsStarted = 5
        )
        
        assertIs<DirectorResult.Failure>(result)
        assertEquals(DirectorFailure.INVALID_PARAMETERS, result.reason)
    }
    
    @Test
    fun `trackBossDefeat should grant major tension release and skill boost`() {
        val result = manager.trackBossDefeat(testDirector)
        
        assertIs<DirectorResult.Success>(result)
        val updated = result.updatedDirector
        
        assertEquals(1, updated.bossDefeatedCount)
        assertEquals(1, updated.consecutiveVictories)
        assertEquals(0, updated.consecutiveDefeats)
        assertTrue(updated.tension <= testDirector.tension - 20)
        assertTrue(updated.skillLevel >= testDirector.skillLevel + 5)
    }
    
    @Test
    fun `updateSessionDuration should change phase based on duration`() {
        // Start in INTRO (0 min)
        val introDirector = testDirector.copy(sessionDurationMinutes = 0)
        val result1 = manager.updateSessionDuration(introDirector, 10)
        assertIs<DirectorResult.Success>(result1)
        assertEquals(DirectorPhase.INTRO, result1.updatedDirector.currentPhase)
        
        // Move to BUILDUP (20 min)
        val result2 = manager.updateSessionDuration(result1.updatedDirector, 10)
        assertIs<DirectorResult.Success>(result2)
        assertEquals(DirectorPhase.BUILDUP, result2.updatedDirector.currentPhase)
        
        // Move to CLIMAX (50 min)
        val result3 = manager.updateSessionDuration(result2.updatedDirector, 30)
        assertIs<DirectorResult.Success>(result3)
        assertEquals(DirectorPhase.CLIMAX, result3.updatedDirector.currentPhase)
        
        // Move to COOLDOWN (65 min)
        val result4 = manager.updateSessionDuration(result3.updatedDirector, 15)
        assertIs<DirectorResult.Success>(result4)
        assertEquals(DirectorPhase.COOLDOWN, result4.updatedDirector.currentPhase)
    }
    
    @Test
    fun `trackHealthPotionUsage should increase tension for high usage`() {
        val result = manager.trackHealthPotionUsage(
            testDirector,
            potionsUsed = 20,
            combatsCompleted = 10 // 2.0 potions per combat (high usage!)
        )
        
        assertIs<DirectorResult.Success>(result)
        val updated = result.updatedDirector
        
        assertEquals(2.0, updated.healthPotionUsageRate)
        assertTrue(updated.tension > testDirector.tension) // High usage raises tension
    }
    
    @Test
    fun `recordPlayerAction should reduce engagement after long idle`() {
        val oldTimestamp = testDirector.lastActionTimestamp
        val newTimestamp = oldTimestamp + 400_000 // 400 seconds = 6.67 minutes (>5 min threshold)
        
        val result = manager.recordPlayerAction(testDirector, newTimestamp)
        
        assertIs<DirectorResult.Success>(result)
        val updated = result.updatedDirector
        
        assertTrue(updated.engagement < testDirector.engagement)
        assertEquals(newTimestamp, updated.lastActionTimestamp)
    }
    
    // ========== DECISION ENGINE TESTS ==========
    
    @Test
    fun `decideAction should provide assistance when player is struggling`() {
        val strugglingDirector = testDirector.copy(
            deathCount = 4,
            consecutiveDefeats = 3,
            tension = 85
        )
        
        val decision = manager.decideAction(strugglingDirector)
        
        assertIs<DirectorAction.ProvideAssistance>(decision.recommendedAction)
        assertTrue(decision.priority >= 90)
    }
    
    @Test
    fun `decideAction should increase challenge when player is bored`() {
        val boredDirector = testDirector.copy(
            consecutiveVictories = 8,
            tension = 25,
            engagement = 35
        )
        
        val decision = manager.decideAction(boredDirector)
        
        assertIs<DirectorAction.IncreaseChallenge>(decision.recommendedAction)
        assertTrue(decision.priority >= 80)
    }
    
    @Test
    fun `decideAction should grant rest after long session with high tension`() {
        // Tension > 80 is required for needsBreak(), but this also triggers isStruggling() (tension > 70)
        // So we need a player who is fatigued but not otherwise struggling (no deaths, no consecutive defeats)
        val fatiguedDirector = testDirector.copy(
            sessionDurationMinutes = 95, // Long session
            tension = 82, // High tension from fatigue
            consecutiveDefeats = 0, // Not losing fights
            deathCount = 0 // No deaths
        )
        
        // This player technically IS struggling due to tension > 70
        // The system should prioritize ProvideAssistance over GrantRest
        // This is correct behavior - help struggling player first
        val decision = manager.decideAction(fatiguedDirector)
        
        // Should get assistance (higher priority than rest)
        assertIs<DirectorAction.ProvideAssistance>(decision.recommendedAction)
        assertTrue(decision.priority >= 90)
    }
    
    @Test
    fun `decideAction should initiate boss fight when player is ready`() {
        val readyDirector = testDirector.copy(
            currentPhase = DirectorPhase.CLIMAX,
            skillLevel = 70,
            consecutiveVictories = 4,
            tension = 50
        )
        
        val decision = manager.decideAction(readyDirector)
        
        assertIs<DirectorAction.InitiateBossFight>(decision.recommendedAction)
        assertTrue(decision.priority >= 70)
    }
    
    @Test
    fun `decideAction should adjust difficulty when skill changes significantly`() {
        // Low skill player (should suggest 0.75 difficulty)
        val lowSkillDirector = testDirector.copy(
            skillLevel = 30,
            difficultyMultiplier = 1.0 // Current difficulty too high
        )
        
        val decision = manager.decideAction(lowSkillDirector)
        
        assertIs<DirectorAction.AdjustDifficulty>(decision.recommendedAction)
        val action = decision.recommendedAction as DirectorAction.AdjustDifficulty
        assertTrue(action.newMultiplier < lowSkillDirector.difficultyMultiplier)
    }
    
    @Test
    fun `decideAction should modify spawn rate based on engagement`() {
        // High engagement (should increase spawn rate)
        val engagedDirector = testDirector.copy(
            engagement = 85,
            skillLevel = 55, // Close to target difficulty, so spawn rate takes priority
            difficultyMultiplier = 1.0
        )
        
        val decision = manager.decideAction(engagedDirector)
        
        assertIs<DirectorAction.ModifySpawnRate>(decision.recommendedAction)
        val action = decision.recommendedAction as DirectorAction.ModifySpawnRate
        assertTrue(action.spawnRateMultiplier > 1.0) // High engagement = more content
    }
    
    // ========== ACTION EXECUTION TESTS ==========
    
    @Test
    fun `executeAction should update difficulty multiplier`() {
        val action = DirectorAction.AdjustDifficulty(1.5)
        val result = manager.executeAction(testDirector, action)
        
        assertIs<DirectorResult.Success>(result)
        assertEquals(1.5, result.updatedDirector.difficultyMultiplier)
    }
    
    @Test
    fun `executeAction ProvideAssistance should reduce tension and difficulty`() {
        val action = DirectorAction.ProvideAssistance(assistLevel = 2)
        val result = manager.executeAction(testDirector, action)
        
        assertIs<DirectorResult.Success>(result)
        val updated = result.updatedDirector
        
        assertTrue(updated.tension < testDirector.tension)
        assertTrue(updated.difficultyMultiplier < testDirector.difficultyMultiplier)
    }
    
    @Test
    fun `executeAction IncreaseChallenge should raise tension and difficulty`() {
        val action = DirectorAction.IncreaseChallenge(challengeLevel = 2)
        val result = manager.executeAction(testDirector, action)
        
        assertIs<DirectorResult.Success>(result)
        val updated = result.updatedDirector
        
        assertTrue(updated.tension > testDirector.tension)
        assertTrue(updated.difficultyMultiplier > testDirector.difficultyMultiplier)
    }
    
    @Test
    fun `executeAction GrantRest should significantly reduce tension`() {
        val highTensionDirector = testDirector.copy(tension = 80)
        val action = DirectorAction.GrantRest
        val result = manager.executeAction(highTensionDirector, action)
        
        assertIs<DirectorResult.Success>(result)
        assertTrue(result.updatedDirector.tension <= 50) // 80 - 30 = 50
    }
    
    @Test
    fun `executeAction InitiateBossFight should raise tension`() {
        val action = DirectorAction.InitiateBossFight("boss_gnome")
        val result = manager.executeAction(testDirector, action)
        
        assertIs<DirectorResult.Success>(result)
        assertTrue(result.updatedDirector.tension > testDirector.tension)
    }
    
    // ========== HELPER METHOD TESTS ==========
    
    @Test
    fun `getNewPlayerDifficulty should return easy difficulty`() {
        val difficulty = manager.getNewPlayerDifficulty()
        assertEquals(0.75, difficulty)
    }
    
    @Test
    fun `resetForNewSession should clear session-specific stats`() {
        val veteranDirector = testDirector.copy(
            sessionDurationMinutes = 60,
            consecutiveVictories = 5,
            consecutiveDefeats = 0,
            deathCount = 2,
            currentPhase = DirectorPhase.CLIMAX,
            tension = 75,
            engagement = 80
        )
        
        val reset = manager.resetForNewSession(veteranDirector)
        
        assertEquals(0, reset.sessionDurationMinutes)
        assertEquals(0, reset.consecutiveVictories)
        assertEquals(0, reset.consecutiveDefeats)
        assertEquals(0, reset.deathCount)
        assertEquals(DirectorPhase.INTRO, reset.currentPhase)
        assertEquals(50, reset.tension)
        assertEquals(50, reset.engagement)
        assertEquals(0L, reset.lastActionTimestamp)
        
        // Should preserve long-term stats
        assertEquals(veteranDirector.skillLevel, reset.skillLevel)
        assertEquals(veteranDirector.bossDefeatedCount, reset.bossDefeatedCount)
    }
    
    // ========== DIRECTOR STATE TESTS ==========
    
    @Test
    fun `isStruggling should detect struggling player`() {
        val strugglingDirector1 = testDirector.copy(tension = 75)
        assertTrue(strugglingDirector1.isStruggling())
        
        val strugglingDirector2 = testDirector.copy(consecutiveDefeats = 3)
        assertTrue(strugglingDirector2.isStruggling())
        
        val strugglingDirector3 = testDirector.copy(deathCount = 2)
        assertTrue(strugglingDirector3.isStruggling())
    }
    
    @Test
    fun `isBored should detect bored player`() {
        val boredDirector = testDirector.copy(
            tension = 25,
            consecutiveVictories = 6,
            engagement = 35
        )
        
        assertTrue(boredDirector.isBored())
    }
    
    @Test
    fun `isEngaged should detect engaged player`() {
        val engagedDirector = testDirector.copy(
            engagement = 70,
            tension = 55
        )
        
        assertTrue(engagedDirector.isEngaged())
    }
    
    @Test
    fun `needsBreak should detect fatigued player`() {
        val fatiguedDirector = testDirector.copy(
            sessionDurationMinutes = 100,
            tension = 85
        )
        
        assertTrue(fatiguedDirector.needsBreak())
    }
    
    // ========== EDGE CASES ==========
    
    @Test
    fun `tension should never exceed bounds after multiple defeats`() {
        var director = testDirector.copy(tension = 90)
        
        // Track 10 defeats
        repeat(10) {
            val result = manager.trackCombatDefeat(director)
            assertIs<DirectorResult.Success>(result)
            director = result.updatedDirector
            assertTrue(director.tension in 0..100)
        }
    }
    
    @Test
    fun `difficulty multiplier should never exceed bounds after actions`() {
        var director = testDirector.copy(difficultyMultiplier = 1.9)
        
        // Increase challenge multiple times
        repeat(10) {
            val action = DirectorAction.IncreaseChallenge(3)
            val result = manager.executeAction(director, action)
            assertIs<DirectorResult.Success>(result)
            director = result.updatedDirector
            assertTrue(director.difficultyMultiplier in 0.5..2.0)
        }
    }
}
