package com.jalmarquest.shared.difficulty

import kotlin.math.max
import kotlin.math.min

/**
 * Stateless manager for adaptive difficulty system.
 * 
 * Tracks player performance, calculates skill ratings, and adjusts difficulty
 * for optimal engagement. All operations are pure functions returning new state.
 * 
 * Core Operations:
 * - trackCombatPerformance() - Record combat outcome and update metrics
 * - trackQuestPerformance() - Record quest completion and update metrics
 * - calculateSkillRating() - Compute skill level from performance data
 * - adjustDifficulty() - Calculate new difficulty based on skill ratings
 * - setManualDifficulty() - Player manual override
 * 
 * Design Principles:
 * - Stateless: All functions return new DifficultyState
 * - Pure functions: No side effects, deterministic
 * - Smooth transitions: Max 1 difficulty level change per adjustment
 * - Respect player agency: Always allow manual override
 */
object DifficultyManager {
    
    // Cooldown between auto-adjustments (10 minutes = 12000 ticks at 20 TPS)
    private const val ADJUSTMENT_COOLDOWN_TICKS = 12000L
    
    // Minimum sample size before auto-adjustment (need 10 combats or 5 quests)
    private const val MIN_COMBAT_SAMPLES = 10
    private const val MIN_QUEST_SAMPLES = 5
    
    /**
     * Track combat performance and update metrics.
     * 
     * @param state Current difficulty state
     * @param won Whether player won the combat
     * @param combatDurationSeconds How long the combat lasted
     * @param damageTaken Damage player received
     * @param damageDealt Damage player dealt
     * @param healingItemsUsed Number of healing items consumed
     * @param currentTimestamp Current game tick
     * @return Updated state with new performance data
     */
    fun trackCombatPerformance(
        state: DifficultyState,
        won: Boolean,
        combatDurationSeconds: Int,
        damageTaken: Int,
        damageDealt: Int,
        healingItemsUsed: Int,
        currentTimestamp: Long
    ): TrackPerformanceResult {
        if (combatDurationSeconds < 0 || damageTaken < 0 || damageDealt < 0 || healingItemsUsed < 0) {
            return TrackPerformanceResult.Failure(TrackPerformanceFailure.INVALID_DATA)
        }
        
        val perf = state.performanceHistory
        val totalCombats = perf.totalCombatsRecorded + 1
        
        // Rolling average for last 20 combats
        val windowSize = min(20, totalCombats)
        val weight = 1.0 / windowSize
        val keepWeight = 1.0 - weight
        
        // Update win rate
        val newWinRate = if (totalCombats <= 20) {
            // Simple average for first 20 combats
            (perf.combatWinRate * perf.totalCombatsRecorded + if (won) 1.0 else 0.0) / totalCombats
        } else {
            // Rolling average after 20 combats
            perf.combatWinRate * keepWeight + (if (won) 1.0 else 0.0) * weight
        }
        
        // Update average combat duration
        val newAvgDuration = if (totalCombats <= 20) {
            ((perf.averageCombatDuration * perf.totalCombatsRecorded) + combatDurationSeconds) / totalCombats
        } else {
            (perf.averageCombatDuration * keepWeight + combatDurationSeconds * weight).toInt()
        }
        
        // Update damage efficiency (dealt / taken, clamped 0.1-10.0)
        val damageEff = if (damageTaken > 0) {
            (damageDealt.toDouble() / damageTaken).coerceIn(0.1, 10.0)
        } else {
            10.0  // Perfect (no damage taken)
        }
        val newDamageEff = if (totalCombats <= 20) {
            (perf.damageEfficiency * perf.totalCombatsRecorded + damageEff) / totalCombats
        } else {
            perf.damageEfficiency * keepWeight + damageEff * weight
        }
        
        // Update healing usage rate
        val newHealingRate = if (totalCombats <= 20) {
            (perf.healingItemUsageRate * perf.totalCombatsRecorded + healingItemsUsed) / totalCombats
        } else {
            perf.healingItemUsageRate * keepWeight + healingItemsUsed * weight
        }
        
        val updatedPerf = perf.copy(
            combatWinRate = newWinRate,
            averageCombatDuration = newAvgDuration,
            damageEfficiency = newDamageEff,
            healingItemUsageRate = newHealingRate,
            totalCombatsRecorded = totalCombats
        )
        
        // Recalculate combat skill rating
        val combatSkill = calculateCombatSkill(updatedPerf)
        val newSkillRating = SkillRating(
            category = SkillCategory.COMBAT,
            rating = combatSkill,
            sampleSize = totalCombats,
            lastUpdated = currentTimestamp
        )
        
        val newState = state.copy(
            performanceHistory = updatedPerf,
            skillRatings = state.skillRatings + (SkillCategory.COMBAT to newSkillRating)
        )
        
        return TrackPerformanceResult.Success(newState, updatedPerf)
    }
    
    /**
     * Track quest performance and update metrics.
     * 
     * @param state Current difficulty state
     * @param completed Whether quest was completed
     * @param optionalObjectivesCompleted Number of optional objectives completed
     * @param optionalObjectivesTotal Total optional objectives
     * @param currentTimestamp Current game tick
     * @return Updated state with new performance data
     */
    fun trackQuestPerformance(
        state: DifficultyState,
        completed: Boolean,
        optionalObjectivesCompleted: Int,
        optionalObjectivesTotal: Int,
        currentTimestamp: Long
    ): TrackPerformanceResult {
        if (optionalObjectivesCompleted < 0 || optionalObjectivesTotal < 0 ||
            optionalObjectivesCompleted > optionalObjectivesTotal) {
            return TrackPerformanceResult.Failure(TrackPerformanceFailure.INVALID_DATA)
        }
        
        val perf = state.performanceHistory
        val totalQuests = perf.totalQuestsRecorded + 1
        
        // Rolling average for last 10 quests
        val windowSize = min(10, totalQuests)
        val weight = 1.0 / windowSize
        val keepWeight = 1.0 - weight
        
        // Update quest completion rate
        val newCompletionRate = if (totalQuests <= 10) {
            (perf.questCompletionRate * perf.totalQuestsRecorded + if (completed) 1.0 else 0.0) / totalQuests
        } else {
            perf.questCompletionRate * keepWeight + (if (completed) 1.0 else 0.0) * weight
        }
        
        // Update optional objectives rate
        val optionalRate = if (optionalObjectivesTotal > 0) {
            optionalObjectivesCompleted.toDouble() / optionalObjectivesTotal
        } else {
            1.0  // No optional objectives = 100%
        }
        val newOptionalRate = if (totalQuests <= 10) {
            (perf.optionalObjectivesRate * perf.totalQuestsRecorded + optionalRate) / totalQuests
        } else {
            perf.optionalObjectivesRate * keepWeight + optionalRate * weight
        }
        
        val updatedPerf = perf.copy(
            questCompletionRate = newCompletionRate,
            optionalObjectivesRate = newOptionalRate,
            totalQuestsRecorded = totalQuests
        )
        
        // Recalculate exploration skill rating (quests contribute to exploration)
        val explorationSkill = calculateExplorationSkill(updatedPerf)
        val newSkillRating = SkillRating(
            category = SkillCategory.EXPLORATION,
            rating = explorationSkill,
            sampleSize = totalQuests,
            lastUpdated = currentTimestamp
        )
        
        val newState = state.copy(
            performanceHistory = updatedPerf,
            skillRatings = state.skillRatings + (SkillCategory.EXPLORATION to newSkillRating)
        )
        
        return TrackPerformanceResult.Success(newState, updatedPerf)
    }
    
    /**
     * Calculate combat skill rating from performance metrics.
     * 
     * Formula combines:
     * - Win rate (40% weight)
     * - Damage efficiency (30% weight)
     * - Healing usage (20% weight, inverse)
     * - Combat speed (10% weight)
     * 
     * @param performance Current performance metrics
     * @return Skill rating 0.0-2.0 (0=novice, 1=average, 2=expert)
     */
    private fun calculateCombatSkill(performance: PlayerPerformance): Double {
        // Win rate component (0.0-1.0 → 0.0-2.0)
        val winComponent = performance.combatWinRate * 2.0
        
        // Damage efficiency component (0.5=poor, 1.0=average, 2.0+=excellent)
        val damageComponent = when {
            performance.damageEfficiency < 0.5 -> 0.0
            performance.damageEfficiency < 1.0 -> performance.damageEfficiency * 0.8
            performance.damageEfficiency < 2.0 -> 0.8 + (performance.damageEfficiency - 1.0) * 0.6
            else -> 1.4 + (performance.damageEfficiency - 2.0) * 0.3  // Diminishing returns
        }.coerceIn(0.0, 2.0)
        
        // Healing usage component (less usage = higher skill)
        // 0 items = 2.0, 1 item = 1.0, 2+ items = 0.0
        val healingComponent = max(0.0, 2.0 - performance.healingItemUsageRate)
        
        // Combat speed component (faster = better, assuming 60s baseline)
        val speedComponent = when {
            performance.averageCombatDuration <= 30 -> 2.0  // Very fast
            performance.averageCombatDuration <= 60 -> 1.0  // Average
            else -> max(0.0, 1.0 - (performance.averageCombatDuration - 60) / 60.0)
        }
        
        // Weighted average
        val skill = winComponent * 0.4 +
                    damageComponent * 0.3 +
                    healingComponent * 0.2 +
                    speedComponent * 0.1
        
        return skill.coerceIn(0.0, 2.0)
    }
    
    /**
     * Calculate exploration skill rating from performance metrics.
     * 
     * Formula combines:
     * - Quest completion rate (50% weight)
     * - Optional objectives rate (30% weight)
     * - Secrets found rate (20% weight)
     * 
     * @param performance Current performance metrics
     * @return Skill rating 0.0-2.0
     */
    private fun calculateExplorationSkill(performance: PlayerPerformance): Double {
        val questComponent = performance.questCompletionRate * 2.0
        val optionalComponent = performance.optionalObjectivesRate * 2.0
        val secretsComponent = performance.secretsFoundRate * 2.0
        
        val skill = questComponent * 0.5 +
                    optionalComponent * 0.3 +
                    secretsComponent * 0.2
        
        return skill.coerceIn(0.0, 2.0)
    }
    
    /**
     * Adjust difficulty based on player skill ratings.
     * 
     * Uses combat skill as primary metric. Smooth transitions (max 1 level change).
     * Respects cooldown, auto-adjust setting, and minimum sample size.
     * 
     * @param state Current difficulty state
     * @param currentTimestamp Current game tick
     * @param trigger What triggered this adjustment check
     * @return Updated state with new difficulty (if adjusted)
     */
    fun adjustDifficulty(
        state: DifficultyState,
        currentTimestamp: Long,
        trigger: AdjustmentTrigger = AdjustmentTrigger.PERFORMANCE_THRESHOLD
    ): AdjustDifficultyResult {
        // Check if on custom difficulty (cannot auto-adjust)
        if (state.currentMetrics.level == DifficultyLevel.CUSTOM) {
            return AdjustDifficultyResult.Failure(AdjustDifficultyFailure.CUSTOM_DIFFICULTY)
        }
        
        // Check if auto-adjust is enabled
        if (!state.currentMetrics.autoAdjustEnabled) {
            return AdjustDifficultyResult.Failure(AdjustDifficultyFailure.AUTO_ADJUST_DISABLED)
        }
        
        // Check cooldown
        if (!state.canAdjust(currentTimestamp, ADJUSTMENT_COOLDOWN_TICKS)) {
            return AdjustDifficultyResult.Failure(AdjustDifficultyFailure.COOLDOWN_ACTIVE)
        }
        
        // Check minimum sample size
        val perf = state.performanceHistory
        if (perf.totalCombatsRecorded < MIN_COMBAT_SAMPLES && 
            perf.totalQuestsRecorded < MIN_QUEST_SAMPLES) {
            return AdjustDifficultyResult.Failure(AdjustDifficultyFailure.INSUFFICIENT_DATA)
        }
        
        // Calculate overall skill (combat-focused with exploration as secondary)
        val combatSkill = state.getSkillRating(SkillCategory.COMBAT)
        val explorationSkill = state.getSkillRating(SkillCategory.EXPLORATION)
        val overallSkill = combatSkill * 0.7 + explorationSkill * 0.3
        
        // Determine target difficulty based on skill
        val targetLevel = when {
            overallSkill < 0.6 -> DifficultyLevel.STORY_MODE  // Struggling
            overallSkill < 0.8 -> DifficultyLevel.EASY        // Below average
            overallSkill < 1.2 -> DifficultyLevel.NORMAL      // Average
            overallSkill < 1.5 -> DifficultyLevel.HARD        // Above average
            else -> DifficultyLevel.BRUTAL                    // Expert
        }
        
        // Smooth transition (max 1 level change)
        val currentLevel = state.currentMetrics.level
        val newLevel = limitTransition(currentLevel, targetLevel)
        
        // No change needed
        if (newLevel == currentLevel) {
            return AdjustDifficultyResult.Success(state, null)
        }
        
        // Create adjustment record
        val adjustment = DifficultyAdjustment(
            timestamp = currentTimestamp,
            fromLevel = currentLevel,
            toLevel = newLevel,
            reason = buildAdjustmentReason(overallSkill, newLevel),
            triggeredBy = trigger,
            playerSkillRating = overallSkill
        )
        
        // Update state
        val newMetrics = DifficultyMetrics.fromLevel(newLevel).copy(
            autoAdjustEnabled = state.currentMetrics.autoAdjustEnabled
        )
        
        val newState = state.copy(
            currentMetrics = newMetrics,
            adjustmentHistory = state.adjustmentHistory + adjustment,
            lastAdjustmentTime = currentTimestamp
        )
        
        return AdjustDifficultyResult.Success(newState, adjustment)
    }
    
    /**
     * Limit difficulty transition to max 1 level change.
     */
    private fun limitTransition(current: DifficultyLevel, target: DifficultyLevel): DifficultyLevel {
        val levels = listOf(
            DifficultyLevel.STORY_MODE,
            DifficultyLevel.EASY,
            DifficultyLevel.NORMAL,
            DifficultyLevel.HARD,
            DifficultyLevel.BRUTAL
        )
        
        val currentIndex = levels.indexOf(current)
        val targetIndex = levels.indexOf(target)
        
        if (currentIndex == -1 || targetIndex == -1) return current
        
        return when {
            targetIndex > currentIndex + 1 -> levels[currentIndex + 1]  // Max +1
            targetIndex < currentIndex - 1 -> levels[currentIndex - 1]  // Max -1
            else -> target
        }
    }
    
    /**
     * Build human-readable adjustment reason.
     */
    private fun buildAdjustmentReason(skillRating: Double, newLevel: DifficultyLevel): String {
        val skillDescription = when {
            skillRating < 0.6 -> "struggling with current difficulty"
            skillRating < 0.8 -> "performing below average"
            skillRating < 1.2 -> "performing at average level"
            skillRating < 1.5 -> "performing above average"
            else -> "dominating current difficulty"
        }
        
        return "Player $skillDescription (skill: ${"%.2f".format(skillRating)}). Adjusted to ${newLevel.name.replace('_', ' ')} for better challenge."
    }
    
    /**
     * Set difficulty manually (player override).
     * 
     * @param state Current difficulty state
     * @param level Target difficulty level
     * @param currentTimestamp Current game tick
     * @return Updated state with new difficulty
     */
    fun setManualDifficulty(
        state: DifficultyState,
        level: DifficultyLevel,
        currentTimestamp: Long
    ): AdjustDifficultyResult {
        if (level == state.currentMetrics.level) {
            return AdjustDifficultyResult.Success(state, null)
        }
        
        val adjustment = DifficultyAdjustment(
            timestamp = currentTimestamp,
            fromLevel = state.currentMetrics.level,
            toLevel = level,
            reason = "Player manually changed difficulty",
            triggeredBy = AdjustmentTrigger.PLAYER_MANUAL,
            playerSkillRating = state.getOverallSkillRating()
        )
        
        val newMetrics = DifficultyMetrics.fromLevel(level).copy(
            autoAdjustEnabled = state.currentMetrics.autoAdjustEnabled
        )
        
        val newState = state.copy(
            currentMetrics = newMetrics,
            adjustmentHistory = state.adjustmentHistory + adjustment,
            lastAdjustmentTime = currentTimestamp
        )
        
        return AdjustDifficultyResult.Success(newState, adjustment)
    }
    
    /**
     * Toggle auto-adjustment on/off.
     */
    fun setAutoAdjust(state: DifficultyState, enabled: Boolean): DifficultyState {
        return state.copy(
            currentMetrics = state.currentMetrics.copy(autoAdjustEnabled = enabled)
        )
    }
    
    /**
     * Set custom difficulty metrics (disables auto-adjust).
     */
    fun setCustomDifficulty(
        state: DifficultyState,
        metrics: DifficultyMetrics,
        currentTimestamp: Long
    ): AdjustDifficultyResult {
        val customMetrics = metrics.copy(
            level = DifficultyLevel.CUSTOM,
            autoAdjustEnabled = false  // Custom difficulty cannot auto-adjust
        )
        
        val adjustment = DifficultyAdjustment(
            timestamp = currentTimestamp,
            fromLevel = state.currentMetrics.level,
            toLevel = DifficultyLevel.CUSTOM,
            reason = "Player set custom difficulty modifiers",
            triggeredBy = AdjustmentTrigger.PLAYER_MANUAL,
            playerSkillRating = state.getOverallSkillRating()
        )
        
        val newState = state.copy(
            currentMetrics = customMetrics,
            adjustmentHistory = state.adjustmentHistory + adjustment,
            lastAdjustmentTime = currentTimestamp
        )
        
        return AdjustDifficultyResult.Success(newState, adjustment)
    }
    
    /**
     * Get difficulty modifiers for combat calculations.
     */
    fun getCombatModifiers(state: DifficultyState): Pair<Double, Double> {
        return Pair(
            state.currentMetrics.enemyDamageMultiplier,
            state.currentMetrics.enemyHealthMultiplier
        )
    }
    
    /**
     * Get difficulty modifiers for loot calculations.
     */
    fun getLootModifiers(state: DifficultyState): Pair<Double, Double> {
        return Pair(
            state.currentMetrics.lootQualityMultiplier,
            state.currentMetrics.lootQuantityMultiplier
        )
    }
    
    /**
     * Get difficulty modifier for XP calculations.
     */
    fun getXPModifier(state: DifficultyState): Double {
        return state.currentMetrics.xpMultiplier
    }
    
    /**
     * Get difficulty modifier for stamina regeneration.
     */
    fun getStaminaRegenModifier(state: DifficultyState): Double {
        return state.currentMetrics.staminaRegenMultiplier
    }
}
