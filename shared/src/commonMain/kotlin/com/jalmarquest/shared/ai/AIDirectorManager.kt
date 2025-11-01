package com.jalmarquest.shared.ai

import kotlin.math.max
import kotlin.math.min

/**
 * AI Director Manager: Stateless director operations.
 * 
 * Manages AI Director state updates and decision-making logic. All functions are pure
 * (no side effects) - they take current state and return new state.
 * 
 * Decision Algorithm:
 * 1. Analyze current director state (tension, engagement, skill, etc.)
 * 2. Calculate urgency scores for each potential action
 * 3. Select highest-priority action that fits current game state
 * 4. Return decision with reasoning for debugging/tuning
 * 
 * Thread-Safe: Stateless functional design, safe for concurrent calls.
 */
class AIDirectorManager {
    
    // ========== PARAMETER TRACKING ==========
    
    /**
     * Track a combat victory and update director state.
     * 
     * Updates:
     * - Resets consecutive defeats
     * - Increments consecutive victories
     * - Reduces tension (successful combat lowers stress)
     * - Updates skill level based on combat efficiency
     * 
     * @param director Current director state
     * @param combatDurationSeconds How long the fight lasted
     * @param damageEfficiency Percentage of player's potential damage output used (0-100)
     * @return Updated director state
     */
    fun trackCombatVictory(
        director: AIDirector,
        combatDurationSeconds: Int,
        damageEfficiency: Int = 50
    ): DirectorResult {
        if (combatDurationSeconds < 0 || damageEfficiency !in 0..100) {
            return DirectorResult.Failure(DirectorFailure.INVALID_PARAMETERS)
        }
        
        // Fast, efficient wins boost skill more than slow wins
        val skillBoost = if (combatDurationSeconds < 30 && damageEfficiency > 70) 3 else 1
        
        val updatedDirector = director.copy(
            consecutiveVictories = director.consecutiveVictories + 1,
            consecutiveDefeats = 0,
            tension = max(0, director.tension - 10), // Victory reduces tension
            skillLevel = min(100, director.skillLevel + skillBoost),
            averageCombatDuration = ((director.averageCombatDuration * (director.consecutiveVictories + director.consecutiveDefeats) + combatDurationSeconds) / 
                                     max(1, director.consecutiveVictories + director.consecutiveDefeats + 1))
        )
        
        return DirectorResult.Success(updatedDirector)
    }
    
    /**
     * Track a combat defeat and update director state.
     * 
     * Updates:
     * - Resets consecutive victories
     * - Increments consecutive defeats
     * - Increases tension (failures raise stress)
     * - Increments death count
     * - Reduces skill level slightly (death suggests overestimation)
     * 
     * @param director Current director state
     * @return Updated director state
     */
    fun trackCombatDefeat(director: AIDirector): DirectorResult {
        val updatedDirector = director.copy(
            consecutiveVictories = 0,
            consecutiveDefeats = director.consecutiveDefeats + 1,
            tension = min(100, director.tension + 15), // Defeat raises tension significantly
            skillLevel = max(0, director.skillLevel - 2),
            deathCount = director.deathCount + 1
        )
        
        return DirectorResult.Success(updatedDirector)
    }
    
    /**
     * Track quest completion and update director state.
     * 
     * @param director Current director state
     * @param questsCompleted Total quests completed
     * @param questsStarted Total quests started
     * @return Updated director state
     */
    fun trackQuestCompletion(
        director: AIDirector,
        questsCompleted: Int,
        questsStarted: Int
    ): DirectorResult {
        if (questsCompleted < 0 || questsStarted < 0 || questsCompleted > questsStarted) {
            return DirectorResult.Failure(DirectorFailure.INVALID_PARAMETERS)
        }
        
        val completionRate = if (questsStarted > 0) {
            (questsCompleted * 100) / questsStarted
        } else {
            0
        }
        
        val updatedDirector = director.copy(
            questCompletionRate = completionRate,
            engagement = min(100, director.engagement + 5) // Quest progress shows engagement
        )
        
        return DirectorResult.Success(updatedDirector)
    }
    
    /**
     * Track boss defeat (major milestone).
     * 
     * @param director Current director state
     * @return Updated director state
     */
    fun trackBossDefeat(director: AIDirector): DirectorResult {
        val updatedDirector = director.copy(
            bossDefeatedCount = director.bossDefeatedCount + 1,
            tension = max(0, director.tension - 20), // Boss victory is major tension release
            skillLevel = min(100, director.skillLevel + 5),
            consecutiveVictories = director.consecutiveVictories + 1,
            consecutiveDefeats = 0
        )
        
        return DirectorResult.Success(updatedDirector)
    }
    
    /**
     * Update session duration (called periodically).
     * 
     * @param director Current director state
     * @param additionalMinutes Minutes to add to session
     * @return Updated director state
     */
    fun updateSessionDuration(
        director: AIDirector,
        additionalMinutes: Int
    ): DirectorResult {
        if (additionalMinutes < 0) {
            return DirectorResult.Failure(DirectorFailure.INVALID_PARAMETERS)
        }
        
        val newDuration = director.sessionDurationMinutes + additionalMinutes
        
        // Update phase based on duration
        val newPhase = when {
            newDuration < 15 -> DirectorPhase.INTRO
            newDuration < 45 -> DirectorPhase.BUILDUP
            newDuration < 60 -> DirectorPhase.CLIMAX
            else -> DirectorPhase.COOLDOWN
        }
        
        val updatedDirector = director.copy(
            sessionDurationMinutes = newDuration,
            currentPhase = newPhase
        )
        
        return DirectorResult.Success(updatedDirector)
    }
    
    /**
     * Track health potion usage to gauge difficulty.
     * 
     * @param director Current director state
     * @param potionsUsed Number of potions used
     * @param combatsCompleted Number of combats completed
     * @return Updated director state
     */
    fun trackHealthPotionUsage(
        director: AIDirector,
        potionsUsed: Int,
        combatsCompleted: Int
    ): DirectorResult {
        if (potionsUsed < 0 || combatsCompleted < 0) {
            return DirectorResult.Failure(DirectorFailure.INVALID_PARAMETERS)
        }
        
        val usageRate = if (combatsCompleted > 0) {
            potionsUsed.toDouble() / combatsCompleted
        } else {
            0.0
        }
        
        // High potion usage suggests difficulty is too high
        val tensionAdjustment = if (usageRate > 1.5) 5 else 0
        
        val updatedDirector = director.copy(
            healthPotionUsageRate = usageRate,
            tension = min(100, director.tension + tensionAdjustment)
        )
        
        return DirectorResult.Success(updatedDirector)
    }
    
    /**
     * Record player action to track engagement.
     * 
     * @param director Current director state
     * @param timestampMillis Current timestamp
     * @return Updated director state
     */
    fun recordPlayerAction(
        director: AIDirector,
        timestampMillis: Long
    ): DirectorResult {
        val timeSinceLastAction = if (director.lastActionTimestamp > 0) {
            timestampMillis - director.lastActionTimestamp
        } else {
            0L
        }
        
        // If >5 minutes since last action, reduce engagement
        val engagementAdjustment = if (timeSinceLastAction > 300_000) -10 else 5
        
        val updatedDirector = director.copy(
            lastActionTimestamp = timestampMillis,
            engagement = min(100, max(0, director.engagement + engagementAdjustment))
        )
        
        return DirectorResult.Success(updatedDirector)
    }
    
    // ========== DECISION ENGINE ==========
    
    /**
     * Analyze current game state and decide what action to take.
     * 
     * Decision Priority:
     * 1. **Critical**: Player struggling badly (3+ deaths, high tension) → Provide assistance
     * 2. **High**: Player bored (5+ easy wins, low tension) → Increase challenge
     * 3. **High**: Long session with high tension → Grant rest
     * 4. **Medium**: Boss readiness check → Initiate boss fight
     * 5. **Low**: Fine-tuning (spawn rate, difficulty multiplier adjustments)
     * 6. **None**: Optimal state → No action
     * 
     * @param director Current director state
     * @return Decision with recommended action and reasoning
     */
    fun decideAction(director: AIDirector): DirectorDecision {
        // Priority 1: Player struggling (critical intervention)
        if (director.isStruggling()) {
            val assistLevel = when {
                director.deathCount >= 5 -> 3 // Major assistance
                director.consecutiveDefeats >= 5 -> 3
                director.deathCount >= 3 -> 2 // Moderate assistance
                director.consecutiveDefeats >= 3 -> 2
                else -> 1 // Minor assistance
            }
            
            return DirectorDecision(
                recommendedAction = DirectorAction.ProvideAssistance(assistLevel),
                reasoning = "Player struggling: ${director.deathCount} deaths, ${director.consecutiveDefeats} consecutive defeats, ${director.tension}% tension",
                priority = 90
            )
        }
        
        // Priority 2: Player bored (needs challenge)
        if (director.isBored()) {
            val challengeLevel = when {
                director.consecutiveVictories >= 10 -> 3 // Major challenge
                director.consecutiveVictories >= 7 -> 2
                else -> 1
            }
            
            return DirectorDecision(
                recommendedAction = DirectorAction.IncreaseChallenge(challengeLevel),
                reasoning = "Player bored: ${director.consecutiveVictories} consecutive wins, ${director.tension}% tension, ${director.engagement}% engagement",
                priority = 80
            )
        }
        
        // Priority 3: Player needs break (fatigue)
        if (director.needsBreak()) {
            return DirectorDecision(
                recommendedAction = DirectorAction.GrantRest,
                reasoning = "Player needs break: ${director.sessionDurationMinutes} minutes played, ${director.tension}% tension",
                priority = 75
            )
        }
        
        // Priority 4: Boss fight readiness (climax phase + good performance)
        if (director.currentPhase == DirectorPhase.CLIMAX && 
            director.skillLevel >= 60 && 
            director.consecutiveVictories >= 3 &&
            director.tension < 60) {
            
            return DirectorDecision(
                recommendedAction = DirectorAction.InitiateBossFight("boss_placeholder"),
                reasoning = "Player ready for boss: CLIMAX phase, ${director.skillLevel}% skill, ${director.consecutiveVictories} wins",
                priority = 70
            )
        }
        
        // Priority 5: Difficulty tuning
        val targetDifficulty = calculateTargetDifficulty(director)
        if (kotlin.math.abs(director.difficultyMultiplier - targetDifficulty) > 0.1) {
            return DirectorDecision(
                recommendedAction = DirectorAction.AdjustDifficulty(targetDifficulty),
                reasoning = "Difficulty adjustment: current ${director.difficultyMultiplier}, target $targetDifficulty based on skill ${director.skillLevel}%",
                priority = 50
            )
        }
        
        // Priority 6: Spawn rate tuning based on engagement
        val targetSpawnRate = when {
            director.engagement > 80 -> 1.2 // High engagement = more content
            director.engagement < 40 -> 0.8 // Low engagement = reduce pressure
            else -> 1.0
        }
        
        return DirectorDecision(
            recommendedAction = DirectorAction.ModifySpawnRate(targetSpawnRate),
            reasoning = "Spawn rate tuning: ${director.engagement}% engagement suggests ${targetSpawnRate}x rate",
            priority = 30
        )
    }
    
    /**
     * Calculate target difficulty multiplier based on player skill.
     * 
     * Mapping:
     * - Skill 0-20: 0.5x (very easy, tutorial)
     * - Skill 20-40: 0.75x (easy)
     * - Skill 40-60: 1.0x (normal)
     * - Skill 60-80: 1.25x (hard)
     * - Skill 80-100: 1.5-2.0x (very hard to extreme)
     * 
     * @param director Current director state
     * @return Target difficulty multiplier
     */
    private fun calculateTargetDifficulty(director: AIDirector): Double {
        return when {
            director.skillLevel <= 20 -> 0.5
            director.skillLevel <= 40 -> 0.75
            director.skillLevel <= 60 -> 1.0
            director.skillLevel <= 80 -> 1.25
            else -> 1.5 + ((director.skillLevel - 80) * 0.025) // Linear scale from 1.5 to 2.0
        }
    }
    
    /**
     * Execute a director action and return updated state.
     * 
     * Note: This only updates the director state. Actual game effects (spawning enemies, etc.)
     * must be handled by the calling system.
     * 
     * @param director Current director state
     * @param action Action to execute
     * @return Updated director state
     */
    fun executeAction(director: AIDirector, action: DirectorAction): DirectorResult {
        val updatedDirector = when (action) {
            is DirectorAction.AdjustDifficulty -> {
                director.copy(difficultyMultiplier = action.newMultiplier)
            }
            is DirectorAction.ProvideAssistance -> {
                // Reduce tension and lower difficulty slightly
                director.copy(
                    tension = max(0, director.tension - (action.assistLevel * 10)),
                    difficultyMultiplier = max(0.5, director.difficultyMultiplier - (action.assistLevel * 0.1))
                )
            }
            is DirectorAction.IncreaseChallenge -> {
                // Increase tension (excitement) and raise difficulty
                director.copy(
                    tension = min(100, director.tension + (action.challengeLevel * 10)),
                    difficultyMultiplier = min(2.0, director.difficultyMultiplier + (action.challengeLevel * 0.1))
                )
            }
            is DirectorAction.GrantRest -> {
                // Major tension reduction
                director.copy(tension = max(0, director.tension - 30))
            }
            is DirectorAction.InitiateBossFight -> {
                // Boss fights raise tension (anticipation)
                director.copy(tension = min(100, director.tension + 20))
            }
            is DirectorAction.SpawnEnemy,
            is DirectorAction.GrantLoot,
            is DirectorAction.TriggerEvent,
            is DirectorAction.ModifySpawnRate,
            DirectorAction.NoAction -> {
                // These actions don't directly modify director state
                director
            }
        }
        
        return DirectorResult.Success(updatedDirector)
    }
    
    /**
     * Get recommended difficulty multiplier for new player.
     */
    fun getNewPlayerDifficulty(): Double = 0.75
    
    /**
     * Reset director state for new session.
     */
    fun resetForNewSession(director: AIDirector): AIDirector {
        return director.copy(
            sessionDurationMinutes = 0,
            consecutiveVictories = 0,
            consecutiveDefeats = 0,
            deathCount = 0,
            currentPhase = DirectorPhase.INTRO,
            tension = 50,
            engagement = 50,
            lastActionTimestamp = 0L
        )
    }
}
