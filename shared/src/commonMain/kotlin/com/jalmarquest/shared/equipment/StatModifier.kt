package com.jalmarquest.shared.equipment

import kotlinx.serialization.Serializable

/**
 * Represents stat modifiers provided by equipment.
 * All values are additive bonuses (e.g., +5 Strength, +3 Vitality).
 */
@Serializable
data class StatModifier(
    /** Physical attack power bonus */
    val strength: Int = 0,
    
    /** Speed, evasion, and critical chance bonus */
    val agility: Int = 0,
    
    /** Health and defense bonus */
    val vitality: Int = 0,
    
    /** Magic power and mana bonus */
    val intelligence: Int = 0,
    
    /** Loot quality and critical damage bonus */
    val luck: Int = 0
) {
    init {
        require(strength >= 0) { "Strength modifier cannot be negative: $strength" }
        require(agility >= 0) { "Agility modifier cannot be negative: $agility" }
        require(vitality >= 0) { "Vitality modifier cannot be negative: $vitality" }
        require(intelligence >= 0) { "Intelligence modifier cannot be negative: $intelligence" }
        require(luck >= 0) { "Luck modifier cannot be negative: $luck" }
    }
    
    /**
     * Adds two stat modifiers together.
     */
    operator fun plus(other: StatModifier): StatModifier {
        return StatModifier(
            strength = this.strength + other.strength,
            agility = this.agility + other.agility,
            vitality = this.vitality + other.vitality,
            intelligence = this.intelligence + other.intelligence,
            luck = this.luck + other.luck
        )
    }
    
    /**
     * Scales all modifiers by a multiplier (for broken items, set bonuses, etc.).
     */
    fun scale(multiplier: Double): StatModifier {
        return StatModifier(
            strength = (strength * multiplier).toInt(),
            agility = (agility * multiplier).toInt(),
            vitality = (vitality * multiplier).toInt(),
            intelligence = (intelligence * multiplier).toInt(),
            luck = (luck * multiplier).toInt()
        )
    }
    
    /**
     * Returns true if all modifiers are zero (no stats).
     */
    fun isEmpty(): Boolean {
        return strength == 0 && agility == 0 && vitality == 0 && intelligence == 0 && luck == 0
    }
}
