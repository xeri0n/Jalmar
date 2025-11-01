package com.jalmarquest.shared.ai

import kotlinx.serialization.Serializable

/**
 * AI Director: Dynamic content and difficulty adjustment system.
 * 
 * The AI Director monitors player performance, engagement, and game state to dynamically
 * adjust encounters, loot, difficulty, and narrative pacing. This creates a personalized
 * experience that adapts to player skill and preferences.
 * 
 * Core Pillars:
 * - **Tension Management**: Balance challenge and reward (avoid frustration or boredom)
 * - **Engagement Tracking**: Monitor player investment and adjust pacing
 * - **Skill Assessment**: Adaptive difficulty based on performance
 * - **Pacing Control**: Regulate intensity of encounters and events
 * 
 * Architecture:
 * - Stateless functional design (all state in AIDirector data class)
 * - Parameter-driven decision making (no hardcoded logic)
 * - Integration with all major systems (combat, quests, spawns, loot)
 */

/**
 * AI Director state tracking player performance and engagement metrics.
 * 
 * @property tension Current tension level (0-100). High tension = recent struggles, low = smooth sailing
 * @property engagement Player engagement score (0-100). Based on session duration, actions per minute
 * @property skillLevel Estimated player skill (0-100). Derived from combat performance, quest completion rate
 * @property difficultyMultiplier Current difficulty scaling factor (0.5 - 2.0). Adjusts enemy stats, loot quality
 * @property sessionDurationMinutes Total time in current play session
 * @property consecutiveVictories Streak of combat wins without defeat
 * @property consecutiveDefeats Streak of combat losses without victory
 * @property questCompletionRate Percentage of started quests completed (0-100)
 * @property averageCombatDuration Average fight length in seconds (longer = struggling)
 * @property healthPotionUsageRate How often player uses healing (high = difficult content)
 * @property deathCount Total player deaths in current session
 * @property bossDefeatedCount Number of bosses defeated (milestone tracking)
 * @property lastActionTimestamp Milliseconds since last significant player action
 * @property currentPhase Director's current pacing phase (INTRO, BUILDUP, CLIMAX, COOLDOWN)
 */
@Serializable
data class AIDirector(
    val tension: Int = 50,
    val engagement: Int = 50,
    val skillLevel: Int = 50,
    val difficultyMultiplier: Double = 1.0,
    val sessionDurationMinutes: Int = 0,
    val consecutiveVictories: Int = 0,
    val consecutiveDefeats: Int = 0,
    val questCompletionRate: Int = 0,
    val averageCombatDuration: Int = 0,
    val healthPotionUsageRate: Double = 0.0,
    val deathCount: Int = 0,
    val bossDefeatedCount: Int = 0,
    val lastActionTimestamp: Long = 0L,
    val currentPhase: DirectorPhase = DirectorPhase.INTRO
) {
    init {
        require(tension in 0..100) { "Tension must be 0-100" }
        require(engagement in 0..100) { "Engagement must be 0-100" }
        require(skillLevel in 0..100) { "Skill level must be 0-100" }
        require(difficultyMultiplier in 0.5..2.0) { "Difficulty multiplier must be 0.5-2.0" }
        require(sessionDurationMinutes >= 0) { "Session duration cannot be negative" }
        require(consecutiveVictories >= 0) { "Consecutive victories cannot be negative" }
        require(consecutiveDefeats >= 0) { "Consecutive defeats cannot be negative" }
        require(questCompletionRate in 0..100) { "Quest completion rate must be 0-100" }
        require(averageCombatDuration >= 0) { "Average combat duration cannot be negative" }
        require(healthPotionUsageRate >= 0.0) { "Health potion usage rate cannot be negative" }
        require(deathCount >= 0) { "Death count cannot be negative" }
        require(bossDefeatedCount >= 0) { "Boss defeated count cannot be negative" }
    }
    
    /**
     * Check if player is struggling (high tension, low skill performance).
     */
    fun isStruggling(): Boolean = tension > 70 || consecutiveDefeats >= 3 || deathCount >= 2
    
    /**
     * Check if player is bored (low tension, high skill dominance).
     */
    fun isBored(): Boolean = tension < 30 && consecutiveVictories >= 5 && engagement < 40
    
    /**
     * Check if player is engaged (good tension balance, active play).
     */
    fun isEngaged(): Boolean = engagement > 60 && tension in 40..70
    
    /**
     * Check if player needs a break (long session, high tension).
     */
    fun needsBreak(): Boolean = sessionDurationMinutes > 90 && tension > 80
}

/**
 * Director pacing phases for narrative intensity.
 * 
 * The AI Director cycles through phases to create rhythm:
 * - **INTRO**: Tutorial, low difficulty, learning phase
 * - **BUILDUP**: Gradually increasing challenge, exploration
 * - **CLIMAX**: High-intensity encounters, boss fights
 * - **COOLDOWN**: Reduced difficulty, reward phase, rest
 */
@Serializable
enum class DirectorPhase {
    INTRO,      // Low intensity, tutorial (0-15 minutes)
    BUILDUP,    // Moderate intensity, exploration (15-45 minutes)
    CLIMAX,     // High intensity, boss encounters (45-60 minutes)
    COOLDOWN    // Low intensity, rewards and rest (60+ minutes)
}

/**
 * Decision parameters the AI Director monitors.
 * 
 * These metrics drive the decision engine's action selection.
 */
@Serializable
enum class DecisionParameter {
    PLAYER_SKILL,           // Combat performance, reaction time, strategy
    TENSION_LEVEL,          // Current stress/challenge level
    ENGAGEMENT_SCORE,       // Player activity and investment
    SESSION_DURATION,       // Time played in current session
    RECENT_PERFORMANCE,     // Win/loss streak, quest completion
    HEALTH_MANAGEMENT,      // How well player conserves HP
    RESOURCE_USAGE,         // Potion, skill, item usage patterns
    EXPLORATION_RATE,       // How fast player discovers content
    QUEST_PROGRESS,         // Active quests, completion rate
    BOSS_READINESS          // Is player ready for major challenge?
}

/**
 * Actions the AI Director can take to adjust gameplay.
 * 
 * Sealed class hierarchy for exhaustive when expressions.
 */
@Serializable
sealed class DirectorAction {
    /**
     * Spawn an enemy encounter with specified difficulty.
     * @property difficultyLevel 1-5 (1=easy fodder, 5=mini-boss)
     */
    @Serializable
    data class SpawnEnemy(val difficultyLevel: Int) : DirectorAction()
    
    /**
     * Grant bonus loot to reward player.
     * @property qualityBonus 0-100 (percentage increase in loot quality)
     */
    @Serializable
    data class GrantLoot(val qualityBonus: Int) : DirectorAction()
    
    /**
     * Adjust overall difficulty multiplier.
     * @property newMultiplier 0.5-2.0 (scales enemy stats, loot drops)
     */
    @Serializable
    data class AdjustDifficulty(val newMultiplier: Double) : DirectorAction()
    
    /**
     * Trigger a dynamic world event.
     * @property eventType Event category (TREASURE, AMBUSH, NPC_ENCOUNTER, etc.)
     */
    @Serializable
    data class TriggerEvent(val eventType: String) : DirectorAction()
    
    /**
     * Modify enemy spawn rate.
     * @property spawnRateMultiplier 0.5-2.0 (scales encounter frequency)
     */
    @Serializable
    data class ModifySpawnRate(val spawnRateMultiplier: Double) : DirectorAction()
    
    /**
     * Grant player a rest opportunity (safe zone, heal point).
     */
    @Serializable
    object GrantRest : DirectorAction()
    
    /**
     * Initiate boss encounter (climax phase trigger).
     * @property bossId Boss enemy ID from catalog
     */
    @Serializable
    data class InitiateBossFight(val bossId: String) : DirectorAction()
    
    /**
     * Reduce difficulty to assist struggling player.
     * @property assistLevel 1-3 (1=minor help, 3=major handicap reduction)
     */
    @Serializable
    data class ProvideAssistance(val assistLevel: Int) : DirectorAction()
    
    /**
     * Increase challenge for skilled player.
     * @property challengeLevel 1-3 (1=minor increase, 3=elite difficulty)
     */
    @Serializable
    data class IncreaseChallenge(val challengeLevel: Int) : DirectorAction()
    
    /**
     * No action needed (player state is optimal).
     */
    @Serializable
    object NoAction : DirectorAction()
}

/**
 * Result of a director decision analysis.
 * 
 * @property recommendedAction Action the director suggests
 * @property reasoning Human-readable explanation for debugging/tuning
 * @property priority Urgency score (0-100, higher = more critical)
 */
@Serializable
data class DirectorDecision(
    val recommendedAction: DirectorAction,
    val reasoning: String,
    val priority: Int
) {
    init {
        require(priority in 0..100) { "Priority must be 0-100" }
    }
}

/**
 * Result types for AI Director operations.
 */
@Serializable
sealed class DirectorResult {
    @Serializable
    data class Success(val updatedDirector: AIDirector, val decision: DirectorDecision? = null) : DirectorResult()
    
    @Serializable
    data class Failure(val reason: DirectorFailure) : DirectorResult()
}

/**
 * Failure reasons for director operations.
 */
@Serializable
enum class DirectorFailure {
    INVALID_PARAMETERS,     // Parameters out of valid range
    ACTION_NOT_AVAILABLE,   // Requested action cannot be performed now
    SYSTEM_ERROR            // Internal error
}
