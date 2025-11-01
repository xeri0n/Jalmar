package com.jalmarquest.shared.combat

import kotlinx.serialization.Serializable

/**
 * Status effects that can be applied to combat participants.
 * Effects persist for a duration (in rounds) and modify combat behavior.
 */
@Serializable
enum class StatusEffectType {
    /** Deals damage over time (5% of max HP per round) */
    POISON,
    
    /** Deals fire damage over time (8% of max HP per round) */
    BURN,
    
    /** Prevents action for duration */
    STUN,
    
    /** Reduces damage dealt by 30% */
    WEAKEN,
    
    /** Increases damage dealt by 30% */
    STRENGTHEN,
    
    /** Reduces defense by 25% */
    VULNERABLE,
    
    /** Regenerates HP each round (10% of max HP) */
    REGENERATION
}

/**
 * An active status effect with remaining duration.
 * @property type The type of status effect
 * @property remainingRounds How many rounds until the effect expires (>= 1)
 * @property source Optional identifier for who applied the effect
 */
@Serializable
data class StatusEffect(
    val type: StatusEffectType,
    val remainingRounds: Int,
    val source: String? = null
) {
    init {
        require(remainingRounds >= 1) { "Status effect must have at least 1 remaining round, got $remainingRounds" }
    }
    
    /**
     * Decrements the duration by 1 round.
     * @return A new StatusEffect with remainingRounds - 1, or null if expired
     */
    fun tick(): StatusEffect? {
        val newRemaining = remainingRounds - 1
        return if (newRemaining >= 1) copy(remainingRounds = newRemaining) else null
    }
    
    /**
     * Returns whether this effect is expired (should be removed).
     */
    fun isExpired(): Boolean = remainingRounds <= 0
    
    /**
     * Returns a human-readable description of the effect.
     */
    fun description(): String = when (type) {
        StatusEffectType.POISON -> "Poisoned (5% HP damage/round, $remainingRounds rounds left)"
        StatusEffectType.BURN -> "Burning (8% HP damage/round, $remainingRounds rounds left)"
        StatusEffectType.STUN -> "Stunned (cannot act, $remainingRounds rounds left)"
        StatusEffectType.WEAKEN -> "Weakened (-30% damage, $remainingRounds rounds left)"
        StatusEffectType.STRENGTHEN -> "Strengthened (+30% damage, $remainingRounds rounds left)"
        StatusEffectType.VULNERABLE -> "Vulnerable (-25% defense, $remainingRounds rounds left)"
        StatusEffectType.REGENERATION -> "Regenerating (+10% HP/round, $remainingRounds rounds left)"
    }
}
