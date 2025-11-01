package com.jalmarquest.shared.combat

import com.jalmarquest.shared.model.Player
import kotlin.math.max
import kotlin.math.min

/**
 * Calculates estimated win rate for player vs enemy encounters.
 * Considers all player stats, level, gear, and enemy stats.
 */
object WinRateCalculator {
    
    /**
     * Calculate estimated win rate percentage (0-100) for player vs enemy.
     * 
     * Factors considered:
     * - Level difference
     * - HP ratio (survivability)
     * - Damage output ratio
     * - Defense difference
     * - Stat advantages (STR, AGI, VIT, LCK)
     * 
     * @param player The player character
     * @param enemy The enemy to fight
     * @return Win rate percentage (0-100)
     */
    fun calculateWinRate(player: Player, enemy: Enemy): Int {
        var winRate = 50.0 // Base 50% chance
        
        // Level advantage/disadvantage (±5% per level, max ±25%)
        val levelDiff = player.level - enemy.level
        val levelBonus = (levelDiff * 5.0).coerceIn(-25.0, 25.0)
        winRate += levelBonus
        
        // HP ratio (survivability) - max ±15%
        val playerHp = player.stats.maxHealth.toDouble()
        val enemyHp = enemy.maxHp.toDouble()
        val hpRatio = playerHp / enemyHp
        val hpBonus = when {
            hpRatio >= 2.0 -> 15.0  // 2x+ HP = big advantage
            hpRatio >= 1.5 -> 10.0
            hpRatio >= 1.2 -> 5.0
            hpRatio >= 0.8 -> 0.0   // Similar HP
            hpRatio >= 0.6 -> -5.0
            hpRatio >= 0.4 -> -10.0
            else -> -15.0           // Very low HP = big disadvantage
        }
        winRate += hpBonus
        
        // Damage output comparison - max ±15%
        val playerDamage = calculatePlayerDamage(player)
        val enemyDamage = enemy.baseDamage.toDouble()
        val damageRatio = playerDamage / max(1.0, enemyDamage)
        val damageBonus = when {
            damageRatio >= 1.8 -> 15.0
            damageRatio >= 1.4 -> 10.0
            damageRatio >= 1.1 -> 5.0
            damageRatio >= 0.9 -> 0.0
            damageRatio >= 0.7 -> -5.0
            damageRatio >= 0.5 -> -10.0
            else -> -15.0
        }
        winRate += damageBonus
        
        // Defense comparison - max ±10%
        val playerDefense = player.stats.defense.toDouble()
        val enemyDefense = enemy.defense.toDouble()
        val defenseDiff = playerDefense - enemyDefense
        val defenseBonus = (defenseDiff * 2.0).coerceIn(-10.0, 10.0)
        winRate += defenseBonus
        
        // Stat advantages - max ±15% total
        val statBonus = calculateStatBonus(player, enemy)
        winRate += statBonus
        
        // Stamina check - if low stamina, reduce win rate
        val staminaPercent = player.stats.currentStamina.toDouble() / player.stats.maxStamina.toDouble()
        if (staminaPercent < 0.3) {
            winRate -= 15.0  // Low stamina = risky fight
        } else if (staminaPercent < 0.5) {
            winRate -= 5.0
        }
        
        // Health check - if damaged, reduce win rate
        val healthPercent = player.stats.currentHealth.toDouble() / player.stats.maxHealth.toDouble()
        if (healthPercent < 0.5) {
            winRate -= 10.0  // Already damaged = risky
        } else if (healthPercent < 0.75) {
            winRate -= 5.0
        }
        
        // Clamp to 5-95% (never guarantee or impossible)
        return winRate.coerceIn(5.0, 95.0).toInt()
    }
    
    /**
     * Calculate player's effective damage output.
     * Based on attack stat (placeholder for future weapon system).
     */
    private fun calculatePlayerDamage(player: Player): Double {
        // Base damage from attack stat
        val baseDamage = player.stats.attack * 0.5
        
        // Level scaling (higher level = more damage)
        val levelBonus = player.level * 0.3
        
        // Minimum 1 damage
        return max(1.0, baseDamage + levelBonus)
    }
    
    /**
     * Calculate bonus/penalty from stat comparisons.
     * Compares attack, speed, defense, luck between player and enemy.
     * 
     * @return Bonus/penalty in range -15.0 to +15.0
     */
    private fun calculateStatBonus(player: Player, enemy: Enemy): Double {
        var bonus = 0.0
        
        // Attack vs Strength comparison (±4%)
        val attackDiff = player.stats.attack - enemy.strength
        bonus += (attackDiff * 0.5).coerceIn(-4.0, 4.0)
        
        // Speed vs Agility comparison (±4%) - affects dodging
        val speedDiff = player.stats.speed - enemy.agility
        bonus += (speedDiff * 0.5).coerceIn(-4.0, 4.0)
        
        // Defense vs Vitality comparison (±4%) - affects endurance
        val defenseDiff = player.stats.defense - enemy.vitality
        bonus += (defenseDiff * 0.5).coerceIn(-4.0, 4.0)
        
        // Luck comparison (±3%) - affects crits
        val lckDiff = player.stats.luck - enemy.luck
        bonus += (lckDiff * 0.3).coerceIn(-3.0, 3.0)
        
        return bonus
    }
    
    /**
     * Get color indicator for win rate.
     * Used for UI display.
     */
    fun getWinRateColor(winRate: Int): WinRateColor {
        return when {
            winRate >= 80 -> WinRateColor.VERY_HIGH  // Green
            winRate >= 60 -> WinRateColor.HIGH       // Light green
            winRate >= 40 -> WinRateColor.MEDIUM     // Yellow
            winRate >= 20 -> WinRateColor.LOW        // Orange
            else -> WinRateColor.VERY_LOW            // Red
        }
    }
}

/**
 * Color indicators for win rate display.
 */
enum class WinRateColor {
    VERY_HIGH,   // 80-100% (green)
    HIGH,        // 60-79% (light green)
    MEDIUM,      // 40-59% (yellow)
    LOW,         // 20-39% (orange)
    VERY_LOW     // 0-19% (red)
}
