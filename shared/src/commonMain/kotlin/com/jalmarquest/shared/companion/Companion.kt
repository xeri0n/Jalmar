package com.jalmarquest.shared.companion

import com.jalmarquest.shared.combat.CombatParticipant
import com.jalmarquest.shared.combat.StatusEffect
import com.jalmarquest.shared.npc.NPCPersonality
import com.jalmarquest.shared.npc.NPCSpecies
import com.jalmarquest.shared.skills.SkillEffect
import kotlinx.serialization.Serializable

/**
 * Represents a recruitable companion who can follow the player and assist in combat.
 * Companions have unique abilities, personalities, and loyalty mechanics.
 * 
 * Design Philosophy:
 * - Companions are NPCs that join the player's journey
 * - Loyalty affects combat effectiveness and ability availability
 * - Each companion has unique personality and backstory
 * - Abilities integrate with existing SkillEffect system
 * 
 * @property id Unique identifier for this companion
 * @property name Display name
 * @property species Companion species (quail, mole, mouse, etc.)
 * @property personality Personality traits affecting behavior and dialogue
 * @property backstory Lore and recruitment story
 * @property recruitmentQuestId Quest required to recruit this companion
 * @property maxHp Maximum hit points for combat
 * @property strength Combat stat (affects physical damage)
 * @property agility Combat stat (affects initiative and dodge)
 * @property vitality Combat stat (affects HP and defense)
 * @property intelligence Combat stat (affects skill damage)
 * @property luck Combat stat (affects critical hits)
 * @property combatBehavior How companion acts in combat
 * @property abilities List of unique companion abilities
 * @property defaultDialogueTreeId Dialogue tree for companion conversations
 * @property favoriteItems Items that increase loyalty when gifted
 * @property dislikedItems Items that decrease loyalty when gifted
 */
@Serializable
data class Companion(
    val id: String,
    val name: String,
    val species: NPCSpecies,
    val personality: NPCPersonality,
    val backstory: String,
    val recruitmentQuestId: String,
    val maxHp: Int,
    val strength: Int,
    val agility: Int,
    val vitality: Int,
    val intelligence: Int,
    val luck: Int,
    val combatBehavior: CompanionBehavior,
    val abilities: List<CompanionAbility>,
    val defaultDialogueTreeId: String,
    val favoriteItems: List<String> = emptyList(),
    val dislikedItems: List<String> = emptyList()
) {
    init {
        require(id.isNotBlank()) { "Companion ID cannot be blank" }
        require(name.isNotBlank()) { "Companion name cannot be blank" }
        require(maxHp > 0) { "Max HP must be positive: $maxHp" }
        require(strength >= 0) { "Strength cannot be negative: $strength" }
        require(agility >= 0) { "Agility cannot be negative: $agility" }
        require(vitality >= 0) { "Vitality cannot be negative: $vitality" }
        require(intelligence >= 0) { "Intelligence cannot be negative: $intelligence" }
        require(luck >= 0) { "Luck cannot be negative: $luck" }
        require(abilities.isNotEmpty()) { "Companion must have at least one ability" }
    }
    
    /**
     * Converts companion to combat data for battle participation.
     * 
     * @param combatId Unique identifier for this combat instance
     * @param currentHp Current HP (defaults to max HP)
     * @param loyaltyScore Current loyalty score (affects stats)
     * @return Combat data structure
     */
    fun toCombatData(
        combatId: String,
        currentHp: Int = maxHp,
        loyaltyScore: Int = 50
    ): CompanionCombatData {
        // Apply loyalty modifier to stats
        val loyaltyModifier = getLoyaltyModifier(loyaltyScore)
        
        return CompanionCombatData(
            id = "$combatId-$id",
            name = name,
            currentHp = currentHp.coerceIn(0, maxHp),
            maxHp = maxHp,
            strength = (strength * loyaltyModifier).toInt(),
            agility = (agility * loyaltyModifier).toInt(),
            vitality = (vitality * loyaltyModifier).toInt(),
            intelligence = (intelligence * loyaltyModifier).toInt(),
            luck = (luck * loyaltyModifier).toInt(),
            companionId = id,
            loyaltyScore = loyaltyScore,
            activeStatusEffects = emptyList()
        )
    }
    
    /**
     * Calculates stat modifier based on loyalty score.
     * 
     * Loyalty Tiers:
     * - 0-24: DISTRUSTFUL (-30% stats)
     * - 25-49: NEUTRAL (no modifier)
     * - 50-74: FRIENDLY (+10% stats)
     * - 75-99: LOYAL (+20% stats)
     * - 100: DEVOTED (+30% stats)
     */
    private fun getLoyaltyModifier(loyaltyScore: Int): Double {
        return when (loyaltyScore) {
            in 0..24 -> 0.7   // -30%
            in 25..49 -> 1.0  // No modifier
            in 50..74 -> 1.1  // +10%
            in 75..99 -> 1.2  // +20%
            100 -> 1.3        // +30%
            else -> 1.0
        }
    }
}

/**
 * Companion AI behavior in combat.
 * Determines how companion prioritizes actions and targets.
 */
@Serializable
enum class CompanionBehavior {
    /**
     * Aggressive: Prioritizes damage output, attacks strongest enemies.
     * Best for high-damage companions (Grumble, Sparrow Scout).
     */
    AGGRESSIVE,
    
    /**
     * Defensive: Protects player, uses defensive abilities when player in danger.
     * Best for tanky companions (Shield-bearing mouse).
     */
    DEFENSIVE,
    
    /**
     * Supportive: Uses buffs/heals, assists with status effects.
     * Best for support companions (Pip, Firefly Guide).
     */
    SUPPORTIVE
}

/**
 * Unique companion ability that unlocks at specific loyalty thresholds.
 * Uses existing SkillEffect system for consistency.
 * 
 * @property id Unique ability identifier
 * @property name Display name
 * @property description What the ability does
 * @property loyaltyRequired Minimum loyalty to unlock (0-100)
 * @property effects List of skill effects applied when used
 * @property cooldownRounds Rounds before ability can be used again
 */
@Serializable
data class CompanionAbility(
    val id: String,
    val name: String,
    val description: String,
    val loyaltyRequired: Int,
    val effects: List<SkillEffect>,
    val cooldownRounds: Int = 0
) {
    init {
        require(id.isNotBlank()) { "Ability ID cannot be blank" }
        require(name.isNotBlank()) { "Ability name cannot be blank" }
        require(loyaltyRequired in 0..100) { "Loyalty required must be 0-100: $loyaltyRequired" }
        require(effects.isNotEmpty()) { "Ability must have at least one effect" }
        require(cooldownRounds >= 0) { "Cooldown cannot be negative: $cooldownRounds" }
    }
}

/**
 * Companion loyalty status based on loyalty score.
 * Affects combat effectiveness, ability availability, and dialogue options.
 */
@Serializable
enum class CompanionLoyaltyStatus {
    /** 0-24: Companion is unhappy, may flee in combat, reduced stats (-30%) */
    DISTRUSTFUL,
    
    /** 25-49: Companion is neutral, normal performance */
    NEUTRAL,
    
    /** 50-74: Companion is friendly, slight stat bonus (+10%) */
    FRIENDLY,
    
    /** 75-99: Companion is loyal, significant stat bonus (+20%), more abilities */
    LOYAL,
    
    /** 100: Companion is devoted, maximum stat bonus (+30%), all abilities */
    DEVOTED;
    
    companion object {
        /**
         * Gets loyalty status for a given score.
         */
        fun fromScore(score: Int): CompanionLoyaltyStatus {
            return when (score) {
                in 0..24 -> DISTRUSTFUL
                in 25..49 -> NEUTRAL
                in 50..74 -> FRIENDLY
                in 75..99 -> LOYAL
                100 -> DEVOTED
                else -> NEUTRAL
            }
        }
    }
}

/**
 * Combat data for companion participant.
 * Implements CombatParticipant for integration with existing combat system.
 * 
 * @property companionId Reference to companion catalog ID
 * @property loyaltyScore Current loyalty (affects stats)
 */
@Serializable
data class CompanionCombatData(
    override val id: String,
    override val name: String,
    override val currentHp: Int,
    override val maxHp: Int,
    override val strength: Int,
    override val agility: Int,
    override val vitality: Int,
    override val intelligence: Int,
    override val luck: Int,
    val companionId: String,
    val loyaltyScore: Int,
    override val activeStatusEffects: List<StatusEffect> = emptyList()
) : CombatParticipant {
    init {
        require(currentHp >= 0) { "Current HP cannot be negative: $currentHp" }
        require(maxHp > 0) { "Max HP must be positive: $maxHp" }
        require(strength >= 0) { "Strength cannot be negative: $strength" }
        require(agility >= 0) { "Agility cannot be negative: $agility" }
        require(vitality >= 0) { "Vitality cannot be negative: $vitality" }
        require(intelligence >= 0) { "Intelligence cannot be negative: $intelligence" }
        require(luck >= 0) { "Luck cannot be negative: $luck" }
        require(loyaltyScore in 0..100) { "Loyalty score must be 0-100: $loyaltyScore" }
    }
    
    /**
     * Gets loyalty status for this companion.
     */
    fun getLoyaltyStatus(): CompanionLoyaltyStatus {
        return CompanionLoyaltyStatus.fromScore(loyaltyScore)
    }
    
    /**
     * Checks if companion will fight based on loyalty.
     * DISTRUSTFUL companions may flee if player is losing.
     */
    fun willFight(playerHpPercentage: Float): Boolean {
        return when (getLoyaltyStatus()) {
            CompanionLoyaltyStatus.DISTRUSTFUL -> playerHpPercentage > 0.3f
            else -> true
        }
    }
}

/**
 * Tracks companion recruitment and loyalty state in GameState.
 * Serialized with save files.
 * 
 * @property companionId Companion catalog ID
 * @property loyaltyScore Current loyalty (0-100)
 * @property abilityCooldowns Map of ability ID to remaining cooldown rounds
 * @property lastInteractionTimestamp Last time player interacted with companion
 */
@Serializable
data class CompanionProgress(
    val companionId: String,
    val loyaltyScore: Int = 50,
    val abilityCooldowns: Map<String, Int> = emptyMap(),
    val lastInteractionTimestamp: Long = System.currentTimeMillis()
) {
    init {
        require(companionId.isNotBlank()) { "Companion ID cannot be blank" }
        require(loyaltyScore in 0..100) { "Loyalty score must be 0-100: $loyaltyScore" }
    }
    
    /**
     * Modifies loyalty score by given amount, clamped to 0-100.
     */
    fun modifyLoyalty(change: Int): CompanionProgress {
        val newScore = (loyaltyScore + change).coerceIn(0, 100)
        return copy(loyaltyScore = newScore)
    }
    
    /**
     * Updates last interaction timestamp to now.
     */
    fun updateInteraction(): CompanionProgress {
        return copy(lastInteractionTimestamp = System.currentTimeMillis())
    }
    
    /**
     * Sets cooldown for specific ability.
     */
    fun setCooldown(abilityId: String, rounds: Int): CompanionProgress {
        val newCooldowns = abilityCooldowns.toMutableMap()
        newCooldowns[abilityId] = rounds
        return copy(abilityCooldowns = newCooldowns)
    }
    
    /**
     * Decrements all ability cooldowns by 1 round.
     */
    fun decrementCooldowns(): CompanionProgress {
        val newCooldowns = abilityCooldowns
            .mapValues { (it.value - 1).coerceAtLeast(0) }
            .filter { it.value > 0 }
        return copy(abilityCooldowns = newCooldowns)
    }
}
