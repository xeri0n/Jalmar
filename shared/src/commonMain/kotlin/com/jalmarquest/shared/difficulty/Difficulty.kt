package com.jalmarquest.shared.difficulty

import kotlinx.serialization.Serializable

/**
 * Adaptive Difficulty System - AI-powered dynamic challenge scaling.
 * 
 * Tracks player performance across combat, quests, and exploration to calculate
 * skill ratings and automatically adjust difficulty for optimal engagement.
 * 
 * Core Features:
 * - Performance tracking (win rate, combat duration, deaths, quest completion)
 * - Skill rating calculation per category (combat, exploration, social)
 * - Smooth difficulty transitions (max 1 level change per adjustment)
 * - Player override support (manual difficulty, disable auto-adjust)
 * - Transparent feedback (reasons for difficulty changes)
 * 
 * Design Philosophy:
 * - Respect player agency (always allow manual control)
 * - Smooth transitions (no sudden spikes)
 * - Category-specific tracking (combat skill ≠ puzzle skill)
 * - Transparent feedback (explain why difficulty changed)
 */

/**
 * Difficulty level presets with modifier values.
 */
@Serializable
enum class DifficultyLevel {
    STORY_MODE,     // 0.5x enemy damage, 2.0x loot quality, 1.5x XP, 1.5x stamina regen
    EASY,           // 0.75x enemy damage, 1.5x loot quality, 1.25x XP, 1.25x stamina regen
    NORMAL,         // 1.0x (baseline for all values)
    HARD,           // 1.25x enemy damage, 0.75x loot quality, 0.9x XP, 0.9x stamina regen
    BRUTAL,         // 1.5x enemy damage, 0.5x loot quality, 0.75x XP, 0.75x stamina regen
    CUSTOM          // Player-defined modifiers
}

/**
 * Current difficulty settings with granular modifiers.
 */
@Serializable
data class DifficultyMetrics(
    val level: DifficultyLevel = DifficultyLevel.NORMAL,
    val enemyDamageMultiplier: Double = 1.0,       // Enemy damage scaling
    val enemyHealthMultiplier: Double = 1.0,       // Enemy HP scaling
    val lootQualityMultiplier: Double = 1.0,       // Better loot at higher difficulties
    val lootQuantityMultiplier: Double = 1.0,      // More/less items
    val xpMultiplier: Double = 1.0,                // Experience gain scaling
    val staminaRegenMultiplier: Double = 1.0,      // Stamina recovery rate
    val autoAdjustEnabled: Boolean = true          // Allow AI to adjust difficulty
) {
    init {
        require(enemyDamageMultiplier > 0.0) { "Enemy damage multiplier must be positive" }
        require(enemyHealthMultiplier > 0.0) { "Enemy health multiplier must be positive" }
        require(lootQualityMultiplier >= 0.0) { "Loot quality multiplier cannot be negative" }
        require(lootQuantityMultiplier >= 0.0) { "Loot quantity multiplier cannot be negative" }
        require(xpMultiplier > 0.0) { "XP multiplier must be positive" }
        require(staminaRegenMultiplier > 0.0) { "Stamina regen multiplier must be positive" }
    }
    
    companion object {
        fun fromLevel(level: DifficultyLevel): DifficultyMetrics = when (level) {
            DifficultyLevel.STORY_MODE -> DifficultyMetrics(
                level = level,
                enemyDamageMultiplier = 0.5,
                enemyHealthMultiplier = 0.75,
                lootQualityMultiplier = 2.0,
                lootQuantityMultiplier = 1.5,
                xpMultiplier = 1.5,
                staminaRegenMultiplier = 1.5
            )
            DifficultyLevel.EASY -> DifficultyMetrics(
                level = level,
                enemyDamageMultiplier = 0.75,
                enemyHealthMultiplier = 0.9,
                lootQualityMultiplier = 1.5,
                lootQuantityMultiplier = 1.25,
                xpMultiplier = 1.25,
                staminaRegenMultiplier = 1.25
            )
            DifficultyLevel.NORMAL -> DifficultyMetrics(
                level = level,
                enemyDamageMultiplier = 1.0,
                enemyHealthMultiplier = 1.0,
                lootQualityMultiplier = 1.0,
                lootQuantityMultiplier = 1.0,
                xpMultiplier = 1.0,
                staminaRegenMultiplier = 1.0
            )
            DifficultyLevel.HARD -> DifficultyMetrics(
                level = level,
                enemyDamageMultiplier = 1.25,
                enemyHealthMultiplier = 1.15,
                lootQualityMultiplier = 0.75,
                lootQuantityMultiplier = 0.9,
                xpMultiplier = 0.9,
                staminaRegenMultiplier = 0.9
            )
            DifficultyLevel.BRUTAL -> DifficultyMetrics(
                level = level,
                enemyDamageMultiplier = 1.5,
                enemyHealthMultiplier = 1.3,
                lootQualityMultiplier = 0.5,
                lootQuantityMultiplier = 0.75,
                xpMultiplier = 0.75,
                staminaRegenMultiplier = 0.75
            )
            DifficultyLevel.CUSTOM -> DifficultyMetrics(level = level)
        }
    }
}

/**
 * Player skill rating in specific category.
 */
@Serializable
data class SkillRating(
    val category: SkillCategory,
    val rating: Double = 1.0,              // 0.0-2.0 scale (0=novice, 1=average, 2=expert)
    val sampleSize: Int = 0,               // Number of data points used
    val lastUpdated: Long = 0              // Timestamp of last calculation
) {
    init {
        require(rating in 0.0..2.0) { "Skill rating must be 0.0-2.0" }
        require(sampleSize >= 0) { "Sample size cannot be negative" }
    }
}

/**
 * Skill categories for performance tracking.
 */
@Serializable
enum class SkillCategory {
    COMBAT,         // Fighting, dodging, using skills
    EXPLORATION,    // Finding secrets, navigating, puzzle-solving
    SOCIAL,         // Dialogue choices, NPC relationships, trading
    RESOURCE        // Inventory management, crafting, currency use
}

/**
 * Recent player performance metrics.
 */
@Serializable
data class PlayerPerformance(
    val combatWinRate: Double = 0.5,           // 0.0-1.0 (last 20 encounters)
    val averageCombatDuration: Int = 60,       // Seconds per combat
    val deathsPerHour: Double = 0.0,           // Deaths in last hour of gameplay
    val damageEfficiency: Double = 1.0,        // Damage dealt / damage taken
    val healingItemUsageRate: Double = 0.5,    // Items used per combat
    val questCompletionRate: Double = 1.0,     // 0.0-1.0 (completed vs attempted)
    val optionalObjectivesRate: Double = 0.5,  // 0.0-1.0 (completed optional objectives)
    val secretsFoundRate: Double = 0.5,        // 0.0-1.0 (secrets found vs available)
    val puzzleSuccessRate: Double = 0.5,       // 0.0-1.0 (puzzles solved first try)
    val totalCombatsRecorded: Int = 0,         // Sample size for combat stats
    val totalQuestsRecorded: Int = 0           // Sample size for quest stats
) {
    init {
        require(combatWinRate in 0.0..1.0) { "Combat win rate must be 0.0-1.0" }
        require(averageCombatDuration >= 0) { "Combat duration cannot be negative" }
        require(deathsPerHour >= 0.0) { "Deaths per hour cannot be negative" }
        require(damageEfficiency >= 0.0) { "Damage efficiency cannot be negative" }
        require(healingItemUsageRate >= 0.0) { "Healing usage rate cannot be negative" }
        require(questCompletionRate in 0.0..1.0) { "Quest completion rate must be 0.0-1.0" }
        require(optionalObjectivesRate in 0.0..1.0) { "Optional objectives rate must be 0.0-1.0" }
        require(secretsFoundRate in 0.0..1.0) { "Secrets found rate must be 0.0-1.0" }
        require(puzzleSuccessRate in 0.0..1.0) { "Puzzle success rate must be 0.0-1.0" }
    }
}

/**
 * Record of a difficulty adjustment.
 */
@Serializable
data class DifficultyAdjustment(
    val timestamp: Long,                    // When adjustment happened
    val fromLevel: DifficultyLevel,         // Previous difficulty
    val toLevel: DifficultyLevel,           // New difficulty
    val reason: String,                     // Human-readable explanation
    val triggeredBy: AdjustmentTrigger,     // What caused the adjustment
    val playerSkillRating: Double           // Player skill at time of adjustment
)

/**
 * What triggered a difficulty adjustment.
 */
@Serializable
enum class AdjustmentTrigger {
    PERFORMANCE_THRESHOLD,      // Player skill crossed threshold
    PLAYER_MANUAL,              // Player manually changed difficulty
    DEATH_STREAK,               // Multiple deaths in short time
    PERFECT_STREAK,             // Multiple perfect victories
    SESSION_START,              // New game session started
    QUEST_MILESTONE             // Major quest completion
}

/**
 * Complete adaptive difficulty state.
 */
@Serializable
data class DifficultyState(
    val currentMetrics: DifficultyMetrics = DifficultyMetrics(),
    val skillRatings: Map<SkillCategory, SkillRating> = emptyMap(),
    val performanceHistory: PlayerPerformance = PlayerPerformance(),
    val adjustmentHistory: List<DifficultyAdjustment> = emptyList(),
    val sessionStartTime: Long = 0,         // Start of current session
    val totalPlayTime: Long = 0,            // Total ticks played
    val lastAdjustmentTime: Long = 0        // Cooldown for adjustments
) {
    /**
     * Get skill rating for category, defaulting to 1.0 (average).
     */
    fun getSkillRating(category: SkillCategory): Double {
        return skillRatings[category]?.rating ?: 1.0
    }
    
    /**
     * Get overall skill rating (average of all categories).
     */
    fun getOverallSkillRating(): Double {
        if (skillRatings.isEmpty()) return 1.0
        return skillRatings.values.map { it.rating }.average()
    }
    
    /**
     * Check if enough time has passed since last adjustment (cooldown).
     */
    fun canAdjust(currentTime: Long, cooldownTicks: Long = 36000): Boolean {
        return currentTime - lastAdjustmentTime >= cooldownTicks
    }
    
    /**
     * Get most recent adjustment.
     */
    fun getLastAdjustment(): DifficultyAdjustment? {
        return adjustmentHistory.lastOrNull()
    }
}

/**
 * Result of tracking combat performance.
 */
sealed class TrackPerformanceResult {
    data class Success(
        val state: DifficultyState,
        val updatedPerformance: PlayerPerformance
    ) : TrackPerformanceResult()
    
    data class Failure(val reason: TrackPerformanceFailure) : TrackPerformanceResult()
}

/**
 * Why performance tracking failed.
 */
@Serializable
enum class TrackPerformanceFailure {
    INVALID_DATA
}

/**
 * Result of difficulty adjustment.
 */
sealed class AdjustDifficultyResult {
    data class Success(
        val state: DifficultyState,
        val adjustment: DifficultyAdjustment?  // Null if no change needed
    ) : AdjustDifficultyResult()
    
    data class Failure(val reason: AdjustDifficultyFailure) : AdjustDifficultyResult()
}

/**
 * Why difficulty adjustment failed.
 */
@Serializable
enum class AdjustDifficultyFailure {
    AUTO_ADJUST_DISABLED,       // Player has disabled auto-adjustment
    COOLDOWN_ACTIVE,            // Too soon since last adjustment
    INSUFFICIENT_DATA,          // Not enough performance samples
    CUSTOM_DIFFICULTY           // Cannot auto-adjust custom difficulty
}
