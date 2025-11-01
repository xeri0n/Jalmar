package com.jalmarquest.shared.skills

import kotlinx.serialization.Serializable

/**
 * Skill archetypes representing different combat specializations.
 */
@Serializable
enum class SkillArchetype {
    /** Physical combat specialist - high damage, melee focus */
    FIGHTER,
    
    /** Agility and precision specialist - ranged attacks, multi-hit combos */
    RANGER,
    
    /** Defense and support specialist - healing, buffs, damage mitigation */
    GUARDIAN;
    
    fun displayName(): String = when (this) {
        FIGHTER -> "Fighter"
        RANGER -> "Ranger"
        GUARDIAN -> "Guardian"
    }
    
    fun description(): String = when (this) {
        FIGHTER -> "Physical combat specialist wielding twigs and pebbles with devastating force"
        RANGER -> "Swift forager using seeds, feathers, and precision strikes from range"
        GUARDIAN -> "Stalwart protector with acorn shields and bark armor to defend allies"
    }
}
