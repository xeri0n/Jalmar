package com.jalmarquest.shared.companion

import kotlinx.serialization.Serializable

/**
 * Defines all loyalty gain/loss triggers and their associated point changes.
 * Loyalty score ranges from 0-100:
 * - 0-24: DISTRUSTFUL (companion may flee from combat, refuses abilities)
 * - 25-49: NEUTRAL (basic cooperation, some abilities locked)
 * - 50-74: FRIENDLY (reliable in combat, most abilities available)
 * - 75-99: LOYAL (strong bond, all abilities unlocked, stat bonuses)
 * - 100: DEVOTED (maximum effectiveness, special dialogue, unique abilities)
 */
object LoyaltyMechanics {
    
    /**
     * Loyalty changes from combat-related events.
     */
    object Combat {
        /** Companion successfully helps defeat an enemy */
        const val COMBAT_VICTORY_WITH_COMPANION = 3
        
        /** Companion deals killing blow to enemy */
        const val COMPANION_KILLS_ENEMY = 5
        
        /** Player flees combat while companion is fighting */
        const val PLAYER_FLEES_ABANDONING_COMPANION = -15
        
        /** Companion takes damage while protecting player */
        const val COMPANION_TAKES_DAMAGE_FOR_PLAYER = 4
        
        /** Player heals companion during combat */
        const val PLAYER_HEALS_COMPANION_IN_COMBAT = 3
        
        /** Companion is knocked out (HP reaches 0) */
        const val COMPANION_KNOCKED_OUT = -10
        
        /** Player continues fighting after companion is knocked out (per turn) */
        const val PLAYER_FIGHTS_AFTER_COMPANION_DOWN = 2
        
        /** Player flees immediately when companion is knocked out */
        const val PLAYER_FLEES_AFTER_COMPANION_DOWN = -5
        
        /** Player uses a companion's ability effectively (kills enemy or saves player) */
        const val EFFECTIVE_ABILITY_USE = 2
    }
    
    /**
     * Loyalty changes from gift-giving and item interactions.
     */
    object Gifts {
        /** Player gives companion a favorite item */
        const val GIVE_FAVORITE_ITEM = 8
        
        /** Player gives companion a liked item */
        const val GIVE_LIKED_ITEM = 4
        
        /** Player gives companion a neutral item */
        const val GIVE_NEUTRAL_ITEM = 1
        
        /** Player gives companion a disliked item */
        const val GIVE_DISLIKED_ITEM = -3
        
        /** Player gives companion food when companion is "hungry" (time-based) */
        const val GIVE_FOOD_WHEN_HUNGRY = 5
    }
    
    /**
     * Loyalty changes from dialogue and story choices.
     */
    object Dialogue {
        /** Player chooses dialogue option that aligns with companion's personality */
        const val ALIGNED_DIALOGUE_CHOICE = 2
        
        /** Player chooses dialogue option that contradicts companion's personality */
        const val CONTRADICTORY_DIALOGUE_CHOICE = -4
        
        /** Player includes companion in decision-making */
        const val CONSULT_COMPANION_ON_DECISION = 3
        
        /** Player ignores companion's advice and it leads to bad outcome */
        const val IGNORE_COMPANION_ADVICE_BAD_OUTCOME = -6
        
        /** Player follows companion's advice and it leads to good outcome */
        const val FOLLOW_COMPANION_ADVICE_GOOD_OUTCOME = 5
        
        /** Player completes companion's personal quest */
        const val COMPLETE_COMPANION_PERSONAL_QUEST = 15
        
        /** Player fails companion's personal quest */
        const val FAIL_COMPANION_PERSONAL_QUEST = -12
    }
    
    /**
     * Loyalty changes from time-based and neglect factors.
     */
    object Time {
        /** Player hasn't interacted with companion in 24 in-game hours */
        const val NO_INTERACTION_24_HOURS = -2
        
        /** Player hasn't interacted with companion in 72 in-game hours */
        const val NO_INTERACTION_72_HOURS = -5
        
        /** Player hasn't interacted with companion in 7 in-game days */
        const val NO_INTERACTION_7_DAYS = -10
        
        /** Companion has been active (following player) for 1 in-game day */
        const val ACTIVE_FOR_1_DAY = 1
        
        /** Companion has been dismissed for 7+ days then re-recruited */
        const val DISMISSED_FOR_WEEK_PENALTY = -8
    }
    
    /**
     * Loyalty changes from major story events.
     */
    object Story {
        /** Player sacrifices something significant for companion's benefit */
        const val PLAYER_SACRIFICE_FOR_COMPANION = 12
        
        /** Player betrays companion's trust in a major way */
        const val MAJOR_BETRAYAL = -25
        
        /** Player protects companion's secret/past when confronted */
        const val PROTECT_COMPANION_SECRET = 10
        
        /** Player reveals companion's secret to NPCs */
        const val REVEAL_COMPANION_SECRET = -15
        
        /** Player dies in combat (companion witnesses) */
        const val PLAYER_DEATH = -20
        
        /** Player is resurrected after death */
        const val PLAYER_RESURRECTION = 5
    }
    
    /**
     * Loyalty thresholds for ability unlocking.
     * Abilities can specify their required loyalty level.
     */
    object AbilityThresholds {
        /** Basic abilities available to all companions */
        const val BASIC_ABILITIES = 0
        
        /** Intermediate abilities require friendly relationship */
        const val INTERMEDIATE_ABILITIES = 50
        
        /** Advanced abilities require loyal relationship */
        const val ADVANCED_ABILITIES = 75
        
        /** Ultimate abilities require devoted relationship */
        const val ULTIMATE_ABILITIES = 100
    }
    
    /**
     * Companion behavior modifiers based on loyalty.
     * These affect how the companion acts in combat and exploration.
     */
    data class LoyaltyEffects(
        /** Multiplier applied to companion's combat stats (0.7x to 1.3x) */
        val statMultiplier: Double,
        
        /** Chance (0.0 to 1.0) companion will flee from difficult combat */
        val fleeChance: Double,
        
        /** Chance (0.0 to 1.0) companion will use abilities proactively */
        val abilityUsageChance: Double,
        
        /** Chance (0.0 to 1.0) companion will protect player from attacks */
        val protectionChance: Double,
        
        /** Whether companion provides commentary during exploration */
        val providesCommentary: Boolean,
        
        /** Whether companion's special dialogue options are available */
        val specialDialogueAvailable: Boolean
    )
    
    /**
     * Get the loyalty effects for a given loyalty score.
     */
    fun getEffectsForLoyalty(loyaltyScore: Int): LoyaltyEffects {
        require(loyaltyScore in 0..100) { "Loyalty score must be 0-100, got $loyaltyScore" }
        
        return when (CompanionLoyaltyStatus.fromScore(loyaltyScore)) {
            CompanionLoyaltyStatus.DISTRUSTFUL -> LoyaltyEffects(
                statMultiplier = 0.7,
                fleeChance = 0.4,
                abilityUsageChance = 0.2,
                protectionChance = 0.0,
                providesCommentary = false,
                specialDialogueAvailable = false
            )
            CompanionLoyaltyStatus.NEUTRAL -> LoyaltyEffects(
                statMultiplier = 0.9,
                fleeChance = 0.2,
                abilityUsageChance = 0.5,
                protectionChance = 0.1,
                providesCommentary = false,
                specialDialogueAvailable = false
            )
            CompanionLoyaltyStatus.FRIENDLY -> LoyaltyEffects(
                statMultiplier = 1.0,
                fleeChance = 0.05,
                abilityUsageChance = 0.7,
                protectionChance = 0.3,
                providesCommentary = true,
                specialDialogueAvailable = false
            )
            CompanionLoyaltyStatus.LOYAL -> LoyaltyEffects(
                statMultiplier = 1.15,
                fleeChance = 0.0,
                abilityUsageChance = 0.85,
                protectionChance = 0.6,
                providesCommentary = true,
                specialDialogueAvailable = true
            )
            CompanionLoyaltyStatus.DEVOTED -> LoyaltyEffects(
                statMultiplier = 1.3,
                fleeChance = 0.0,
                abilityUsageChance = 1.0,
                protectionChance = 0.8,
                providesCommentary = true,
                specialDialogueAvailable = true
            )
        }
    }
}

/**
 * Represents a loyalty change event that can be tracked for the Butterfly Effect Engine.
 * All companion loyalty changes should be logged for narrative consequences.
 */
@Serializable
data class LoyaltyChangeEvent(
    /** Unique identifier for this event */
    val eventId: String,
    
    /** ID of the companion whose loyalty changed */
    val companionId: String,
    
    /** The trigger that caused this change */
    val trigger: LoyaltyChangeTrigger,
    
    /** Amount of loyalty change (can be negative) */
    val loyaltyChange: Int,
    
    /** Loyalty score before the change */
    val previousLoyalty: Int,
    
    /** Loyalty score after the change */
    val newLoyalty: Int,
    
    /** In-game timestamp when this occurred */
    val timestamp: Long,
    
    /** Optional context about what caused this trigger */
    val context: String? = null
) {
    init {
        require(previousLoyalty in 0..100) { "Previous loyalty must be 0-100" }
        require(newLoyalty in 0..100) { "New loyalty must be 0-100" }
    }
}

/**
 * Categorizes all possible loyalty change triggers for tracking and Butterfly Effect.
 */
@Serializable
enum class LoyaltyChangeTrigger {
    // Combat triggers
    COMBAT_VICTORY,
    COMPANION_KILL,
    PLAYER_FLED_COMBAT,
    COMPANION_PROTECTED_PLAYER,
    PLAYER_HEALED_COMPANION,
    COMPANION_KNOCKED_OUT,
    PLAYER_FOUGHT_AFTER_COMPANION_DOWN,
    PLAYER_FLED_AFTER_COMPANION_DOWN,
    EFFECTIVE_ABILITY,
    
    // Gift triggers
    FAVORITE_ITEM_GIFT,
    LIKED_ITEM_GIFT,
    NEUTRAL_ITEM_GIFT,
    DISLIKED_ITEM_GIFT,
    FOOD_WHEN_HUNGRY,
    
    // Dialogue triggers
    ALIGNED_DIALOGUE,
    CONTRADICTORY_DIALOGUE,
    CONSULTED_ON_DECISION,
    IGNORED_ADVICE_BAD,
    FOLLOWED_ADVICE_GOOD,
    PERSONAL_QUEST_COMPLETE,
    PERSONAL_QUEST_FAILED,
    
    // Time triggers
    NEGLECT_24_HOURS,
    NEGLECT_72_HOURS,
    NEGLECT_7_DAYS,
    ACTIVE_TIME_BONUS,
    DISMISSED_PENALTY,
    
    // Story triggers
    PLAYER_SACRIFICE,
    MAJOR_BETRAYAL,
    SECRET_PROTECTED,
    SECRET_REVEALED,
    PLAYER_DEATH,
    PLAYER_RESURRECTION,
    
    // Special triggers
    MANUAL_ADJUSTMENT  // For quest rewards, debug, or special story events
}

/**
 * Helper functions for applying loyalty changes in a consistent, trackable way.
 */
object LoyaltyChangeHelper {
    
    /**
     * Calculate the new loyalty score after applying a change.
     * Clamps result to 0-100 range.
     */
    fun calculateNewLoyalty(currentLoyalty: Int, change: Int): Int {
        require(currentLoyalty in 0..100) { "Current loyalty must be 0-100" }
        return (currentLoyalty + change).coerceIn(0, 100)
    }
    
    /**
     * Check if a loyalty change crosses a threshold (e.g., from NEUTRAL to FRIENDLY).
     * Returns true if the status tier changed.
     */
    fun crossedThreshold(previousLoyalty: Int, newLoyalty: Int): Boolean {
        val previousStatus = CompanionLoyaltyStatus.fromScore(previousLoyalty)
        val newStatus = CompanionLoyaltyStatus.fromScore(newLoyalty)
        return previousStatus != newStatus
    }
    
    /**
     * Get a human-readable description of the loyalty change.
     * Useful for logging and player notifications.
     */
    fun getChangeDescription(trigger: LoyaltyChangeTrigger, change: Int, companionName: String): String {
        val direction = if (change > 0) "increased" else "decreased"
        val amount = kotlin.math.abs(change)
        
        return when (trigger) {
            LoyaltyChangeTrigger.COMBAT_VICTORY -> "$companionName's loyalty $direction by $amount after fighting alongside you."
            LoyaltyChangeTrigger.COMPANION_KILL -> "$companionName's loyalty $direction by $amount after defeating an enemy!"
            LoyaltyChangeTrigger.PLAYER_FLED_COMBAT -> "$companionName's loyalty $direction by $amount after you fled from combat."
            LoyaltyChangeTrigger.FAVORITE_ITEM_GIFT -> "$companionName's loyalty $direction by $amount! They love this gift!"
            LoyaltyChangeTrigger.DISLIKED_ITEM_GIFT -> "$companionName's loyalty $direction by $amount. They didn't like that gift."
            LoyaltyChangeTrigger.ALIGNED_DIALOGUE -> "$companionName appreciates your words. Loyalty $direction by $amount."
            LoyaltyChangeTrigger.CONTRADICTORY_DIALOGUE -> "$companionName seems disappointed. Loyalty $direction by $amount."
            LoyaltyChangeTrigger.PERSONAL_QUEST_COMPLETE -> "$companionName is deeply grateful! Loyalty $direction by $amount!"
            LoyaltyChangeTrigger.PLAYER_DEATH -> "$companionName witnessed your death. Loyalty $direction by $amount."
            LoyaltyChangeTrigger.NEGLECT_7_DAYS -> "$companionName feels neglected. Loyalty $direction by $amount."
            else -> "$companionName's loyalty $direction by $amount."
        }
    }
}
