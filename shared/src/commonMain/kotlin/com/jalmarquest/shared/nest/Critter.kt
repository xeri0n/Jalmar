package com.jalmarquest.shared.nest

import kotlinx.serialization.Serializable

/**
 * Types of critters that can live in the nest.
 * Each has unique preferences and provides different bonuses.
 */
@Serializable
enum class CritterType {
    /** Ladybug - Loves flowers and plants, provides luck bonus */
    LADYBUG,
    
    /** Firefly - Loves lighting cosmetics, provides stamina regen */
    FIREFLY,
    
    /** Beetle - Loves trophies, provides defense bonus */
    BEETLE,
    
    /** Spider - Loves dark corners, provides critical hit bonus */
    SPIDER,
    
    /** Moth - Loves soft materials, provides stealth bonus */
    MOTH,
    
    /** Worm - Loves dirt/natural items, provides HP regen */
    WORM,
    
    /** Snail - Loves moisture/water, provides patience bonus (XP) */
    SNAIL,
    
    /** Ant - Loves organization, provides item find bonus */
    ANT,
    
    /** Grasshopper - Loves space, provides movement speed */
    GRASSHOPPER,
    
    /** Butterfly - Loves beauty/prestige, provides happiness */
    BUTTERFLY
}

/**
 * Rarity tier of critters.
 * Rarer critters provide bigger bonuses but are harder to satisfy.
 */
@Serializable
enum class CritterRarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY
}

/**
 * Satisfaction level of a critter.
 * Affects bonus magnitude and critter behavior.
 */
@Serializable
enum class SatisfactionLevel(val multiplier: Float) {
    MISERABLE(0.0f),      // No bonus, may leave
    UNHAPPY(0.5f),        // Half bonus
    CONTENT(1.0f),        // Full bonus
    HAPPY(1.5f),          // 50% bonus increase
    DELIGHTED(2.0f)       // Double bonus
}

/**
 * Types of bonuses critters can provide.
 */
@Serializable
enum class CritterBonusType {
    HP_REGEN,
    STAMINA_REGEN,
    XP_GAIN,
    CRITICAL_CHANCE,
    DEFENSE,
    LUCK,
    STEALTH,
    MOVEMENT_SPEED,
    ITEM_FIND,
    HAPPINESS
}

/**
 * Preference for cosmetic types.
 * Critters gain satisfaction from specific cosmetic types in the nest.
 */
@Serializable
data class CosmeticPreference(
    val cosmeticType: CosmeticType,
    val satisfactionPerItem: Int
) {
    init {
        require(satisfactionPerItem > 0) { "Satisfaction per item must be positive" }
    }
}

/**
 * Definition of a critter species.
 * Immutable catalog data.
 */
@Serializable
data class Critter(
    val id: String,
    val name: String,
    val description: String,
    val type: CritterType,
    val rarity: CritterRarity,
    val baseBonusValue: Int,
    val bonusType: CritterBonusType,
    val cosmeticPreferences: List<CosmeticPreference> = emptyList(),
    val preferredNestTier: NestTier? = null,
    val maxSatisfaction: Int = 100,
    val satisfactionDecayPerDay: Int = 5
) {
    init {
        require(id.isNotBlank()) { "Critter ID cannot be blank" }
        require(name.isNotBlank()) { "Critter name cannot be blank" }
        require(baseBonusValue > 0) { "Base bonus value must be positive" }
        require(maxSatisfaction > 0) { "Max satisfaction must be positive" }
        require(satisfactionDecayPerDay >= 0) { "Satisfaction decay cannot be negative" }
    }
    
    /**
     * Get the bonus multiplier based on rarity.
     * Rarer critters provide larger bonuses.
     */
    fun getRarityMultiplier(): Float = when (rarity) {
        CritterRarity.COMMON -> 1.0f
        CritterRarity.UNCOMMON -> 1.25f
        CritterRarity.RARE -> 1.5f
        CritterRarity.EPIC -> 2.0f
        CritterRarity.LEGENDARY -> 3.0f
    }
}

/**
 * Instance of a critter living in the player's nest.
 * Mutable state tracked per-critter.
 */
@Serializable
data class NestCritter(
    val critterId: String,
    val customName: String? = null,
    val currentSatisfaction: Int = 50,
    val daysSinceFed: Int = 0,
    val totalDaysInNest: Int = 0
) {
    init {
        require(critterId.isNotBlank()) { "Critter ID cannot be blank" }
        require(currentSatisfaction >= 0) { "Satisfaction cannot be negative" }
        require(daysSinceFed >= 0) { "Days since fed cannot be negative" }
        require(totalDaysInNest >= 0) { "Total days in nest cannot be negative" }
        if (customName != null) {
            require(customName.isNotBlank()) { "Custom name cannot be blank if provided" }
            require(customName.length <= 30) { "Custom name cannot exceed 30 characters" }
        }
    }
    
    /**
     * Gets the display name for this critter.
     * Returns custom name if set, otherwise uses species name from catalog.
     */
    fun getDisplayName(catalog: Critter): String = customName ?: catalog.name
    
    /**
     * Calculates the current satisfaction level based on satisfaction points.
     * Thresholds: 0-20% = MISERABLE, 21-50% = UNHAPPY, 51-75% = CONTENT, 76-90% = HAPPY, 91-100% = DELIGHTED
     */
    fun getSatisfactionLevel(maxSatisfaction: Int): SatisfactionLevel {
        val percentage = currentSatisfaction.toFloat() / maxSatisfaction
        return when {
            percentage <= 0.20f -> SatisfactionLevel.MISERABLE
            percentage <= 0.50f -> SatisfactionLevel.UNHAPPY
            percentage <= 0.75f -> SatisfactionLevel.CONTENT
            percentage <= 0.90f -> SatisfactionLevel.HAPPY
            else -> SatisfactionLevel.DELIGHTED
        }
    }
    
    /**
     * Checks if critter is hungry (needs feeding).
     */
    fun isHungry(): Boolean = daysSinceFed >= 3
    
    /**
     * Checks if critter might leave due to low satisfaction.
     */
    fun mightLeave(): Boolean = getSatisfactionLevel(100).multiplier == 0.0f
}

/**
 * Result of feeding a critter.
 */
@Serializable
sealed class FeedResult {
    data class Success(val satisfactionGained: Int) : FeedResult()
    data class Failure(val reason: String) : FeedResult()
}

/**
 * Result of adopting a new critter.
 */
@Serializable
sealed class AdoptResult {
    data class Success(val critter: NestCritter) : AdoptResult()
    data class Failure(val reason: AdoptFailureReason) : AdoptResult()
}

/**
 * Reasons why adopting a critter might fail.
 */
@Serializable
enum class AdoptFailureReason {
    NEST_FULL,
    ALREADY_ADOPTED,
    CRITTER_NOT_FOUND,
    INSUFFICIENT_SPACE,
    NEST_TIER_TOO_LOW
}
