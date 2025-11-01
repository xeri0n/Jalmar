package com.jalmarquest.shared.companion

import com.jalmarquest.shared.combat.CombatAction
import com.jalmarquest.shared.combat.CombatState
import kotlin.random.Random

/**
 * AI system for companion combat decisions.
 * Determines what action a companion takes during its turn based on behavior type,
 * loyalty level, and combat state.
 * 
 * Design Philosophy:
 * - Companions are smarter than enemies (context-aware decisions)
 * - Loyalty affects decision quality (low loyalty = suboptimal choices)
 * - Each behavior type has distinct tactical priorities
 * - Companions can use abilities based on loyalty requirements
 */
object CompanionAI {
    
    /**
     * Decides what action a companion should take during combat.
     * 
     * @param companionCombatData The companion's combat data
     * @param companion The companion catalog entry
     * @param combatState Current combat state
     * @param loyaltyScore Current loyalty score (0-100)
     * @param availableAbilities Abilities unlocked at current loyalty level
     * @return The combat action to execute
     */
    fun decideAction(
        companionCombatData: CompanionCombatData,
        companion: Companion,
        combatState: CombatState,
        loyaltyScore: Int,
        availableAbilities: List<CompanionAbility>
    ): CombatAction {
        // Check if companion wants to flee based on loyalty
        if (shouldFlee(companionCombatData, combatState, loyaltyScore)) {
            return CombatAction.Flee
        }
        
        // Delegate to behavior-specific logic
        return when (companion.combatBehavior) {
            CompanionBehavior.AGGRESSIVE -> decideAggressive(
                companionCombatData,
                combatState,
                loyaltyScore,
                availableAbilities
            )
            CompanionBehavior.DEFENSIVE -> decideDefensive(
                companionCombatData,
                combatState,
                loyaltyScore,
                availableAbilities
            )
            CompanionBehavior.SUPPORTIVE -> decideSupportive(
                companionCombatData,
                combatState,
                loyaltyScore,
                availableAbilities
            )
        }
    }
    
    /**
     * Aggressive behavior: Prioritizes damage output, attacks strongest enemies.
     * 
     * Strategy:
     * - High loyalty: Uses damage abilities intelligently, targets strongest enemy
     * - Medium loyalty: Basic attacks, occasional ability use
     * - Low loyalty: Attacks randomly, rarely uses abilities
     */
    private fun decideAggressive(
        companion: CompanionCombatData,
        combatState: CombatState,
        loyaltyScore: Int,
        availableAbilities: List<CompanionAbility>
    ): CombatAction {
        val loyaltyStatus = CompanionLoyaltyStatus.fromScore(loyaltyScore)
        val livingEnemies = combatState.enemies.filter { it.isAlive() }
        
        if (livingEnemies.isEmpty()) return CombatAction.Defend
        
        // Try to use damage ability based on loyalty
        val damageAbilities = availableAbilities.filter { ability ->
            ability.effects.any { effect ->
                effect is com.jalmarquest.shared.skills.SkillEffect.Damage ||
                effect is com.jalmarquest.shared.skills.SkillEffect.AoEDamage ||
                effect is com.jalmarquest.shared.skills.SkillEffect.MultiHit
            }
        }
        
        // Use ability chance based on loyalty
        val abilityUseChance = when (loyaltyStatus) {
            CompanionLoyaltyStatus.DISTRUSTFUL -> 0.2f
            CompanionLoyaltyStatus.NEUTRAL -> 0.5f
            CompanionLoyaltyStatus.FRIENDLY -> 0.7f
            CompanionLoyaltyStatus.LOYAL -> 0.85f
            CompanionLoyaltyStatus.DEVOTED -> 1.0f
        }
        
        if (damageAbilities.isNotEmpty() && Random.nextFloat() < abilityUseChance) {
            val ability = damageAbilities.random()
            val target = selectStrongestEnemy(livingEnemies)
            // Return ability usage - this will need integration with CombatManager
            // For now, just attack the strongest enemy
            return CombatAction.Attack(target.id)
        }
        
        // Default: Attack strongest enemy (high loyalty) or random enemy (low loyalty)
        val target = if (loyaltyScore >= 50) {
            selectStrongestEnemy(livingEnemies)
        } else {
            livingEnemies.random()
        }
        
        return CombatAction.Attack(target.id)
    }
    
    /**
     * Defensive behavior: Protects player, uses defensive abilities when player in danger.
     * 
     * Strategy:
     * - High loyalty: Uses defensive buffs proactively, attacks when player safe
     * - Medium loyalty: Basic defensive stance when player low HP
     * - Low loyalty: Attacks normally, ignores player danger
     */
    private fun decideDefensive(
        companion: CompanionCombatData,
        combatState: CombatState,
        loyaltyScore: Int,
        availableAbilities: List<CompanionAbility>
    ): CombatAction {
        val loyaltyStatus = CompanionLoyaltyStatus.fromScore(loyaltyScore)
        val livingEnemies = combatState.enemies.filter { it.isAlive() }
        
        if (livingEnemies.isEmpty()) return CombatAction.Defend
        
        val playerHpPercentage = combatState.player.currentHp.toFloat() / combatState.player.maxHp
        
        // Check if player needs protection
        val playerInDanger = playerHpPercentage < 0.4f
        
        if (playerInDanger && loyaltyScore >= 50) {
            // Try to use defensive/healing ability
            val defensiveAbilities = availableAbilities.filter { ability ->
                ability.effects.any { effect ->
                    effect is com.jalmarquest.shared.skills.SkillEffect.Heal ||
                    effect is com.jalmarquest.shared.skills.SkillEffect.BuffDefense ||
                    effect is com.jalmarquest.shared.skills.SkillEffect.DamageReduction ||
                    effect is com.jalmarquest.shared.skills.SkillEffect.ReflectDamage
                }
            }
            
            if (defensiveAbilities.isNotEmpty()) {
                // For now, attack since we don't have full ability integration
                // Future: Use defensive ability on player
                val target = selectWeakestEnemy(livingEnemies)
                return CombatAction.Attack(target.id)
            }
        }
        
        // Not in danger or no defensive abilities - attack weakest enemy to eliminate threats
        val target = if (loyaltyScore >= 50) {
            selectWeakestEnemy(livingEnemies)
        } else {
            livingEnemies.random()
        }
        
        return CombatAction.Attack(target.id)
    }
    
    /**
     * Supportive behavior: Uses buffs/heals, assists with status effects.
     * 
     * Strategy:
     * - High loyalty: Proactive buffing and healing
     * - Medium loyalty: Heals when player below 50% HP
     * - Low loyalty: Attacks normally, ignores support role
     */
    private fun decideSupportive(
        companion: CompanionCombatData,
        combatState: CombatState,
        loyaltyScore: Int,
        availableAbilities: List<CompanionAbility>
    ): CombatAction {
        val loyaltyStatus = CompanionLoyaltyStatus.fromScore(loyaltyScore)
        val livingEnemies = combatState.enemies.filter { it.isAlive() }
        
        if (livingEnemies.isEmpty()) return CombatAction.Defend
        
        val playerHpPercentage = combatState.player.currentHp.toFloat() / combatState.player.maxHp
        
        // Low loyalty supportive companions don't really support
        if (loyaltyScore < 50) {
            return CombatAction.Attack(livingEnemies.random().id)
        }
        
        // Check if player needs healing
        val healingThreshold = when (loyaltyStatus) {
            CompanionLoyaltyStatus.DISTRUSTFUL, CompanionLoyaltyStatus.NEUTRAL -> 0.3f
            CompanionLoyaltyStatus.FRIENDLY -> 0.5f
            CompanionLoyaltyStatus.LOYAL -> 0.7f
            CompanionLoyaltyStatus.DEVOTED -> 0.85f
        }
        
        if (playerHpPercentage < healingThreshold) {
            // Try to use healing ability
            val healingAbilities = availableAbilities.filter { ability ->
                ability.effects.any { effect ->
                    effect is com.jalmarquest.shared.skills.SkillEffect.Heal ||
                    effect is com.jalmarquest.shared.skills.SkillEffect.AoEHeal
                }
            }
            
            if (healingAbilities.isNotEmpty()) {
                // For now, defend since we don't have full healing integration
                // Future: Use healing ability on player
                return CombatAction.Defend
            }
        }
        
        // Check if player needs buffs (early combat, high loyalty)
        if (combatState.roundNumber <= 2 && loyaltyStatus >= CompanionLoyaltyStatus.LOYAL) {
            val buffAbilities = availableAbilities.filter { ability ->
                ability.effects.any { effect ->
                    effect is com.jalmarquest.shared.skills.SkillEffect.BuffAttack ||
                    effect is com.jalmarquest.shared.skills.SkillEffect.BuffDefense ||
                    effect is com.jalmarquest.shared.skills.SkillEffect.BuffSpeed
                }
            }
            
            if (buffAbilities.isNotEmpty() && Random.nextFloat() < 0.7f) {
                // For now, defend since we don't have full buff integration
                // Future: Use buff ability on player
                return CombatAction.Defend
            }
        }
        
        // Default: Attack weakest enemy to help finish them off
        val target = selectWeakestEnemy(livingEnemies)
        return CombatAction.Attack(target.id)
    }
    
    /**
     * Determines if a companion should flee from combat.
     * Based on loyalty level and combat situation.
     * 
     * Loyalty thresholds:
     * - DISTRUSTFUL: Flees if player HP < 30% OR own HP < 40%
     * - NEUTRAL: Flees if player HP < 20% OR own HP < 25%
     * - FRIENDLY+: Never flees
     * 
     * @return True if companion should attempt to flee
     */
    fun shouldFlee(
        companion: CompanionCombatData,
        combatState: CombatState,
        loyaltyScore: Int
    ): Boolean {
        val loyaltyStatus = CompanionLoyaltyStatus.fromScore(loyaltyScore)
        
        // High loyalty companions never flee
        if (loyaltyStatus >= CompanionLoyaltyStatus.FRIENDLY) {
            return false
        }
        
        val playerHpPercentage = combatState.player.currentHp.toFloat() / combatState.player.maxHp
        val companionHpPercentage = companion.currentHp.toFloat() / companion.maxHp
        
        return when (loyaltyStatus) {
            CompanionLoyaltyStatus.DISTRUSTFUL -> {
                // Very skittish - flees easily
                playerHpPercentage < 0.3f || companionHpPercentage < 0.4f
            }
            CompanionLoyaltyStatus.NEUTRAL -> {
                // Somewhat reliable - only flees in dire situations
                playerHpPercentage < 0.2f || companionHpPercentage < 0.25f
            }
            else -> false
        }
    }
    
    /**
     * Selects the strongest enemy (highest current HP).
     * Used by aggressive companions to prioritize threats.
     */
    private fun selectStrongestEnemy(enemies: List<com.jalmarquest.shared.combat.EnemyCombatData>): com.jalmarquest.shared.combat.EnemyCombatData {
        return enemies.maxByOrNull { it.currentHp } ?: enemies.first()
    }
    
    /**
     * Selects the weakest enemy (lowest current HP).
     * Used by defensive/supportive companions to eliminate threats quickly.
     */
    private fun selectWeakestEnemy(enemies: List<com.jalmarquest.shared.combat.EnemyCombatData>): com.jalmarquest.shared.combat.EnemyCombatData {
        return enemies.minByOrNull { it.currentHp } ?: enemies.first()
    }
}
