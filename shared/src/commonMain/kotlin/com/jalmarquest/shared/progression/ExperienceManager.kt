package com.jalmarquest.shared.progression

import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.model.PlayerStats
import kotlin.math.min

/**
 * Result of granting experience points.
 */
sealed class XpGrantResult {
    /** XP granted, no level-up occurred */
    data class XpGained(val newXp: Long, val xpGained: Long) : XpGrantResult()
    
    /** XP granted and player leveled up (possibly multiple times) */
    data class LeveledUp(
        val newLevel: Int,
        val newXp: Long,
        val levelsGained: Int,
        val statPointsEarned: Int,
        val newMaxHp: Int
    ) : XpGrantResult()
}

/**
 * Result of allocating stat points.
 */
sealed class StatAllocationResult {
    /** Stat points allocated successfully */
    data class Success(val updatedStats: PlayerStats, val pointsSpent: Int) : StatAllocationResult()
    
    /** Stat allocation failed */
    data class Failure(val reason: StatAllocationFailureReason) : StatAllocationResult()
}

/**
 * Reasons why stat allocation might fail.
 */
enum class StatAllocationFailureReason {
    INSUFFICIENT_POINTS,
    INVALID_STAT_NAME,
    NEGATIVE_AMOUNT,
    STAT_CAP_EXCEEDED
}

/**
 * Stateless experience and leveling manager.
 * Handles XP gain, level-ups, and stat point allocation.
 */
object ExperienceManager {
    
    /** Stat points awarded per level */
    const val STAT_POINTS_PER_LEVEL = 5
    
    /** Max HP increase per level */
    const val HP_PER_LEVEL = 10
    
    /** Maximum stat value (prevents over-allocation) */
    const val MAX_STAT_VALUE = 999
    
    /**
     * Calculates XP required to reach a specific level from level 1.
     * Uses formula: 100 * level^2
     * 
     * @param level Target level (1-50)
     * @return Total XP required to reach that level
     */
    fun calculateXpForLevel(level: Int): Long {
        require(level in 1..50) { "Level must be 1-50, got $level" }
        
        if (level == 1) return 0L
        
        // Sum of XP for all previous levels
        var totalXp = 0L
        for (l in 2..level) {
            totalXp += 100L * l * l
        }
        return totalXp
    }
    
    /**
     * Calculates XP required for next level from current level.
     * 
     * @param currentLevel Current player level (1-49)
     * @return XP needed to reach next level
     */
    fun calculateXpForNextLevel(currentLevel: Int): Long {
        require(currentLevel in 1..49) { "Current level must be 1-49, got $currentLevel" }
        return 100L * (currentLevel + 1) * (currentLevel + 1)
    }
    
    /**
     * Grants experience points to a player.
     * Automatically handles level-ups if enough XP is gained.
     * 
     * @param player Current player state
     * @param xpAmount Amount of XP to grant (must be positive)
     * @return Pair of (updated player, XP grant result)
     */
    fun grantXp(player: Player, xpAmount: Long): Pair<Player, XpGrantResult> {
        require(xpAmount > 0) { "XP amount must be positive, got $xpAmount" }
        
        val newXp = player.experience + xpAmount
        var updatedPlayer = player.copy(experience = newXp)
        
        // Check for level-ups
        var levelsGained = 0
        while (updatedPlayer.canLevelUp()) {
            updatedPlayer = levelUpPlayer(updatedPlayer)
            levelsGained++
        }
        
        return if (levelsGained > 0) {
            val statPointsEarned = levelsGained * STAT_POINTS_PER_LEVEL
            val newMaxHp = player.stats.maxHealth + (levelsGained * HP_PER_LEVEL)
            
            updatedPlayer to XpGrantResult.LeveledUp(
                newLevel = updatedPlayer.level,
                newXp = updatedPlayer.experience,
                levelsGained = levelsGained,
                statPointsEarned = statPointsEarned,
                newMaxHp = newMaxHp
            )
        } else {
            updatedPlayer to XpGrantResult.XpGained(
                newXp = newXp,
                xpGained = xpAmount
            )
        }
    }
    
    /**
     * Levels up a player by 1 level.
     * Increases max HP, grants stat points, carries over excess XP.
     * 
     * @param player Player to level up
     * @return Updated player at new level
     */
    private fun levelUpPlayer(player: Player): Player {
        require(player.canLevelUp()) { "Player cannot level up (level=${player.level}, xp=${player.experience})" }
        
        val xpForNextLevel = player.getExperienceForNextLevel()
        val excessXp = player.experience - xpForNextLevel
        
        // Increase max HP
        val newMaxHealth = player.stats.maxHealth + HP_PER_LEVEL
        val newCurrentHealth = min(player.stats.currentHealth + HP_PER_LEVEL, newMaxHealth) // Heal on level-up
        
        // Grant stat points
        val newStatPoints = player.stats.availableStatPoints + STAT_POINTS_PER_LEVEL
        
        val updatedStats = player.stats.copy(
            maxHealth = newMaxHealth,
            currentHealth = newCurrentHealth,
            availableStatPoints = newStatPoints
        )
        
        return player.copy(
            level = player.level + 1,
            experience = excessXp,
            stats = updatedStats
        )
    }
    
    /**
     * Allocates stat points to a specific stat.
     * 
     * @param player Current player
     * @param statName Name of stat ("attack", "defense", "magicPower", "speed", "luck")
     * @param points Number of points to allocate
     * @return Pair of (updated player, allocation result)
     */
    fun allocateStat(player: Player, statName: String, points: Int): Pair<Player, StatAllocationResult> {
        if (points < 0) {
            return player to StatAllocationResult.Failure(StatAllocationFailureReason.NEGATIVE_AMOUNT)
        }
        
        if (points > player.stats.availableStatPoints) {
            return player to StatAllocationResult.Failure(StatAllocationFailureReason.INSUFFICIENT_POINTS)
        }
        
        val updatedStats = when (statName.lowercase()) {
            "attack", "atk", "str", "strength" -> {
                val newValue = player.stats.attack + points
                if (newValue > MAX_STAT_VALUE) {
                    return player to StatAllocationResult.Failure(StatAllocationFailureReason.STAT_CAP_EXCEEDED)
                }
                player.stats.copy(
                    attack = newValue,
                    availableStatPoints = player.stats.availableStatPoints - points
                )
            }
            "speed", "spd", "agi", "agility" -> {
                val newValue = player.stats.speed + points
                if (newValue > MAX_STAT_VALUE) {
                    return player to StatAllocationResult.Failure(StatAllocationFailureReason.STAT_CAP_EXCEEDED)
                }
                player.stats.copy(
                    speed = newValue,
                    availableStatPoints = player.stats.availableStatPoints - points
                )
            }
            "defense", "def", "vit", "vitality" -> {
                val newValue = player.stats.defense + points
                if (newValue > MAX_STAT_VALUE) {
                    return player to StatAllocationResult.Failure(StatAllocationFailureReason.STAT_CAP_EXCEEDED)
                }
                player.stats.copy(
                    defense = newValue,
                    availableStatPoints = player.stats.availableStatPoints - points
                )
            }
            "magicpower", "magic", "int", "intelligence" -> {
                val newValue = player.stats.magicPower + points
                if (newValue > MAX_STAT_VALUE) {
                    return player to StatAllocationResult.Failure(StatAllocationFailureReason.STAT_CAP_EXCEEDED)
                }
                player.stats.copy(
                    magicPower = newValue,
                    availableStatPoints = player.stats.availableStatPoints - points
                )
            }
            "luck", "lck" -> {
                val newValue = player.stats.luck + points
                if (newValue > MAX_STAT_VALUE) {
                    return player to StatAllocationResult.Failure(StatAllocationFailureReason.STAT_CAP_EXCEEDED)
                }
                player.stats.copy(
                    luck = newValue,
                    availableStatPoints = player.stats.availableStatPoints - points
                )
            }
            else -> return player to StatAllocationResult.Failure(StatAllocationFailureReason.INVALID_STAT_NAME)
        }
        
        val updatedPlayer = player.copy(stats = updatedStats)
        return updatedPlayer to StatAllocationResult.Success(updatedStats, points)
    }
    
    /**
     * Calculates player's progress toward next level as a percentage.
     * 
     * @param player Player to check
     * @return Progress percentage (0.0 to 1.0), or 1.0 if at max level
     */
    fun getLevelProgress(player: Player): Float {
        if (player.level >= 50) return 1.0f
        
        val xpForNextLevel = player.getExperienceForNextLevel()
        val previousLevelXp = if (player.level == 1) 0L else calculateXpForLevel(player.level)
        val xpNeededForLevel = xpForNextLevel - previousLevelXp
        val xpEarnedInLevel = player.experience - previousLevelXp
        
        return (xpEarnedInLevel.toFloat() / xpNeededForLevel.toFloat()).coerceIn(0.0f, 1.0f)
    }
    
    /**
     * Calculates total stat points earned by reaching a specific level.
     * 
     * @param level Player level
     * @return Total stat points earned (not including base stats)
     */
    fun getTotalStatPointsForLevel(level: Int): Int {
        require(level in 1..50) { "Level must be 1-50, got $level" }
        return (level - 1) * STAT_POINTS_PER_LEVEL
    }
    
    /**
     * Calculates total XP to award from defeating enemies in combat.
     * Sums xpReward from all defeated enemies.
     * 
     * @param defeatedEnemies List of defeated enemy XP rewards
     * @return Total XP to award
     */
    fun calculateCombatXp(defeatedEnemies: List<Int>): Long {
        return defeatedEnemies.sumOf { it.toLong() }
    }
}
