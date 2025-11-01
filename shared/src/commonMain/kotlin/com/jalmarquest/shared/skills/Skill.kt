package com.jalmarquest.shared.skills

import kotlinx.serialization.Serializable

/**
 * Represents a learnable skill with combat effects or passive bonuses.
 */
@Serializable
data class Skill(
    val id: String,
    val name: String,
    val description: String,
    val archetype: SkillArchetype,
    val tier: SkillTier,
    val prerequisiteSkills: List<String> = emptyList(),
    val effects: List<SkillEffect> = emptyList(),
    val isPassive: Boolean = false,
    val targetType: SkillTargetType = SkillTargetType.SINGLE_ENEMY
) {
    init {
        require(id.isNotBlank()) { "Skill ID cannot be blank" }
        require(name.isNotBlank()) { "Skill name cannot be blank" }
        require(effects.isNotEmpty() || isPassive) { "Skill must have at least one effect or be passive" }
        if (isPassive) {
            require(effects.all { it is SkillEffect.PassiveStats }) {
                "Passive skills can only have PassiveStats effects"
            }
        }
    }
    
    /**
     * Get the required level to learn this skill.
     */
    fun getRequiredLevel(): Int = tier.requiredLevel
    
    /**
     * Get the skill point cost to learn this skill.
     */
    fun getSkillPointCost(): Int = tier.skillPointCost
    
    /**
     * Check if this skill is usable in combat (not passive).
     */
    fun isUsableInCombat(): Boolean = !isPassive
}

/**
 * Defines what the skill can target during combat.
 */
@Serializable
enum class SkillTargetType {
    /** Targets a single enemy */
    SINGLE_ENEMY,
    
    /** Targets all enemies (area of effect) */
    ALL_ENEMIES,
    
    /** Targets self */
    SELF,
    
    /** Targets a single ally */
    SINGLE_ALLY,
    
    /** Targets all allies (area of effect) */
    ALL_ALLIES,
    
    /** No target required (passive or self-buff) */
    NONE
}
