package com.jalmarquest.shared.skills

import com.jalmarquest.shared.combat.StatusEffect
import com.jalmarquest.shared.combat.StatusEffectType
import kotlinx.serialization.Serializable

/**
 * Effects that skills can apply during combat or provide as passive bonuses.
 */
@Serializable
sealed class SkillEffect {
    /** Deal direct damage to a single target (scales with attack stat) */
    @Serializable
    data class Damage(val baseDamage: Int, val statScaling: Float = 0.5f) : SkillEffect()
    
    /** Deal damage to all enemy targets (area of effect) */
    @Serializable
    data class AoEDamage(val baseDamage: Int, val statScaling: Float = 0.3f) : SkillEffect()
    
    /** Restore health to target */
    @Serializable
    data class Heal(val baseHealing: Int) : SkillEffect()
    
    /** Restore health to all allies (area of effect) */
    @Serializable
    data class AoEHeal(val baseHealing: Int) : SkillEffect()
    
    /** Apply a status effect to target */
    @Serializable
    data class ApplyStatus(
        val statusType: StatusEffectType,
        val duration: Int,
        val intensity: Float = 1.0f
    ) : SkillEffect()
    
    /** Increase target's attack stat for duration */
    @Serializable
    data class BuffAttack(val attackBonus: Int, val duration: Int) : SkillEffect()
    
    /** Increase target's defense stat for duration */
    @Serializable
    data class BuffDefense(val defenseBonus: Int, val duration: Int) : SkillEffect()
    
    /** Increase target's speed stat for duration */
    @Serializable
    data class BuffSpeed(val speedBonus: Int, val duration: Int) : SkillEffect()
    
    /** Decrease target's attack stat for duration */
    @Serializable
    data class DebuffAttack(val attackPenalty: Int, val duration: Int) : SkillEffect()
    
    /** Decrease target's defense stat for duration */
    @Serializable
    data class DebuffDefense(val defensePenalty: Int, val duration: Int) : SkillEffect()
    
    /** Grant damage reduction for duration (defensive stance) */
    @Serializable
    data class DamageReduction(val reductionPercent: Float, val duration: Int) : SkillEffect()
    
    /** Reflect damage back to attacker for duration */
    @Serializable
    data class ReflectDamage(val reflectPercent: Float, val duration: Int) : SkillEffect()
    
    /** Permanent passive stat bonuses (applied when skill is learned) */
    @Serializable
    data class PassiveStats(
        val healthBonus: Int = 0,
        val attackBonus: Int = 0,
        val defenseBonus: Int = 0,
        val speedBonus: Int = 0
    ) : SkillEffect()
    
    /** Multi-hit attack (execute damage multiple times) */
    @Serializable
    data class MultiHit(val hits: Int, val damagePerHit: Int, val statScaling: Float = 0.3f) : SkillEffect()
    
    /** Guaranteed critical hit (ignores normal crit calculations) */
    @Serializable
    data object GuaranteedCrit : SkillEffect()
    
    /** Ignore target's defense for this attack */
    @Serializable
    data object IgnoreDefense : SkillEffect()
    
    /** Increase flee success chance */
    @Serializable
    data class FleeBonus(val successBonus: Float) : SkillEffect()
}
