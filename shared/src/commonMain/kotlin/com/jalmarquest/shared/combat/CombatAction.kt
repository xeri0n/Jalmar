package com.jalmarquest.shared.combat

import kotlinx.serialization.Serializable

/**
 * Actions that can be performed during combat.
 */
sealed class CombatAction {
    /** Physical attack targeting an enemy */
    @Serializable
    data class Attack(val targetId: String) : CombatAction()
    
    /** Defensive stance (reduces damage taken by 50% for 1 round) */
    @Serializable
    data object Defend : CombatAction()
    
    /** Use a combat skill (requires skill system integration) */
    @Serializable
    data class UseSkill(val skillId: String, val targetId: String) : CombatAction()
    
    /** Use a consumable item from inventory */
    @Serializable
    data class UseItem(val itemId: String, val targetId: String? = null) : CombatAction()
    
    /** Attempt to flee from combat (success rate based on agility difference) */
    @Serializable
    data object Flee : CombatAction()
}

/**
 * Result of executing a combat action.
 */
sealed class CombatActionResult {
    /** Action executed successfully */
    data class Success(
        val actionDescription: String,
        val damageDealt: Int = 0,
        val healingDone: Int = 0,
        val statusEffectsApplied: List<StatusEffect> = emptyList(),
        val statusEffectsRemoved: List<StatusEffectType> = emptyList()
    ) : CombatActionResult()
    
    /** Action failed to execute */
    data class Failure(val reason: CombatActionFailureReason) : CombatActionResult()
}

/**
 * Reasons why a combat action might fail.
 */
enum class CombatActionFailureReason {
    TARGET_NOT_FOUND,
    TARGET_ALREADY_DEAD,
    SKILL_NOT_FOUND,
    ITEM_NOT_FOUND,
    INSUFFICIENT_RESOURCES,
    STUNNED,
    FLEE_FAILED
}
