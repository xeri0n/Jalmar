package com.jalmarquest.shared.combat

import kotlin.random.Random

/**
 * AI system for enemy combat decisions.
 * Determines what action an enemy takes during its turn based on behavior type and combat state.
 */
object EnemyAI {
    
    /**
     * Decides what action an enemy should take during combat.
     * 
     * @param enemyId The ID of the enemy taking action
     * @param behaviorType The enemy's AI behavior pattern
     * @param combatState Current combat state
     * @return The combat action to execute
     */
    fun decideAction(
        enemyId: String,
        behaviorType: EnemyBehaviorType,
        combatState: CombatState
    ): CombatAction {
        val enemy = combatState.getEnemy(enemyId) ?: return CombatAction.Defend
        
        return when (behaviorType) {
            EnemyBehaviorType.AGGRESSIVE -> decideAggressive(enemy, combatState)
            EnemyBehaviorType.DEFENSIVE -> decideDefensive(enemy, combatState)
            EnemyBehaviorType.FLEEING -> decideFleeing(enemy, combatState)
            EnemyBehaviorType.RANDOM -> decideRandom(enemy, combatState)
            EnemyBehaviorType.SUPPORTIVE -> decideSupportive(enemy, combatState)
        }
    }
    
    /**
     * Aggressive behavior: Always attacks, prioritizes low-HP targets.
     */
    private fun decideAggressive(enemy: EnemyCombatData, combatState: CombatState): CombatAction {
        // Always target the player (only target available currently)
        return CombatAction.Attack(combatState.player.id)
    }
    
    /**
     * Defensive behavior: Defends when HP < 50%, attacks otherwise.
     */
    private fun decideDefensive(enemy: EnemyCombatData, combatState: CombatState): CombatAction {
        val hpPercentage = enemy.hpPercentage()
        
        return if (hpPercentage < 0.5f) {
            // Low HP - go defensive
            // Note: Enemies can't actually defend in current implementation,
            // so we'll attack but could add defensive buffs here in future
            CombatAction.Attack(combatState.player.id)
        } else {
            // Healthy - attack
            CombatAction.Attack(combatState.player.id)
        }
    }
    
    /**
     * Fleeing behavior: Attempts to flee when HP < 30%, attacks when healthy.
     */
    private fun decideFleeing(enemy: EnemyCombatData, combatState: CombatState): CombatAction {
        val hpPercentage = enemy.hpPercentage()
        
        return if (hpPercentage < 0.3f) {
            // Critical HP - try to flee
            CombatAction.Flee
        } else {
            // Still healthy - attack
            CombatAction.Attack(combatState.player.id)
        }
    }
    
    /**
     * Random behavior: Chooses a random action each turn.
     */
    private fun decideRandom(enemy: EnemyCombatData, combatState: CombatState): CombatAction {
        val actions = listOf(
            CombatAction.Attack(combatState.player.id),
            CombatAction.Attack(combatState.player.id), // Attack weighted 2x
            CombatAction.Flee
        )
        
        return actions.random()
    }
    
    /**
     * Supportive behavior: Future - heals/buffs allies, attacks when alone.
     * Currently just attacks (no ally support system yet).
     */
    private fun decideSupportive(enemy: EnemyCombatData, combatState: CombatState): CombatAction {
        // Future: Check for low-HP allies, use healing/buff skills
        // Current: Just attack
        return CombatAction.Attack(combatState.player.id)
    }
    
    /**
     * Determines if an enemy should attempt to flee based on combat conditions.
     * Used as a general-purpose flee check for any behavior type.
     * 
     * @param enemy The enemy considering fleeing
     * @param combatState Current combat state
     * @return True if the enemy should try to flee
     */
    fun shouldFlee(enemy: EnemyCombatData, combatState: CombatState): Boolean {
        // Flee if HP is critically low (< 20%)
        if (enemy.hpPercentage() < 0.2f) return true
        
        // Flee if stunned and low HP (< 40%)
        if (enemy.hasStatusEffect(StatusEffectType.STUN) && enemy.hpPercentage() < 0.4f) return true
        
        // Flee if player is much stronger (player has > 2x enemy's max HP and enemy is weakened)
        if (combatState.player.maxHp > enemy.maxHp * 2 && enemy.hasStatusEffect(StatusEffectType.WEAKEN)) {
            return Random.nextFloat() < 0.3f // 30% chance to flee
        }
        
        return false
    }
}
