package com.jalmarquest.shared.nest

import kotlinx.serialization.Serializable

/**
 * Types of shiny items that can be added to a hoard.
 * Button quails love collecting shiny objects!
 */
@Serializable
enum class HoardItemType {
    /** Shiny buttons of various materials */
    BUTTON,
    
    /** Natural crystals and gemstones */
    CRYSTAL,
    
    /** Precious gems (cut and polished) */
    GEM,
    
    /** Old coins and currency */
    COIN,
    
    /** Reflective metal pieces */
    METAL,
    
    /** Colorful glass fragments */
    GLASS,
    
    /** Rare and unique trinkets */
    TRINKET,
    
    /** Special event items */
    SPECIAL
}

/**
 * Rarity tier for hoard items.
 * Affects value multiplier and ranking contribution.
 */
@Serializable
enum class HoardRarity(val valueMultiplier: Float, val displayName: String) {
    COMMON(1.0f, "Common"),
    UNCOMMON(2.0f, "Uncommon"),
    RARE(4.0f, "Rare"),
    EPIC(8.0f, "Epic"),
    LEGENDARY(20.0f, "Legendary"),
    MYTHICAL(50.0f, "Mythical")  // Extremely rare special items
}

/**
 * Condition of a hoard item.
 * Better condition = higher value.
 */
@Serializable
enum class HoardCondition(val valueMultiplier: Float, val displayName: String) {
    POOR(0.5f, "Poor"),
    FAIR(0.75f, "Fair"),
    GOOD(1.0f, "Good"),
    EXCELLENT(1.5f, "Excellent"),
    PRISTINE(2.0f, "Pristine")
}

/**
 * Definition of a hoard item (shiny collectible).
 * Immutable catalog data.
 */
@Serializable
data class HoardItem(
    val id: String,
    val name: String,
    val description: String,
    val type: HoardItemType,
    val rarity: HoardRarity,
    val baseValue: Int,  // Base value in "shiny points"
    val weight: Int,  // Weight in milligrams (quail scale)
    val isSetItem: Boolean = false,  // Part of a collection set
    val setId: String? = null,  // Collection set identifier
    val unlockMethod: String? = null  // How to obtain (for catalog display)
) {
    init {
        require(id.isNotBlank()) { "HoardItem ID cannot be blank" }
        require(name.isNotBlank()) { "HoardItem name cannot be blank" }
        require(description.isNotBlank()) { "HoardItem description cannot be blank" }
        require(baseValue > 0) { "Base value must be positive" }
        require(weight > 0) { "Weight must be positive" }
        if (isSetItem) {
            require(!setId.isNullOrBlank()) { "Set items must have a setId" }
        }
    }
    
    /**
     * Calculate total value including rarity multiplier.
     */
    fun calculateBaseValue(): Int {
        return (baseValue * rarity.valueMultiplier).toInt()
    }
}

/**
 * Instance of a hoard item in the player's collection.
 * Tracks condition and acquisition timestamp.
 */
@Serializable
data class HoardedItem(
    val itemId: String,
    val condition: HoardCondition = HoardCondition.GOOD,
    val acquiredTimestamp: Long = 0L,
    val customNote: String? = null  // Player can add notes to items
) {
    init {
        require(itemId.isNotBlank()) { "HoardedItem itemId cannot be blank" }
    }
    
    /**
     * Calculate final value including condition multiplier.
     */
    fun calculateValue(baseValue: Int): Int {
        return (baseValue * condition.valueMultiplier).toInt()
    }
}

/**
 * Player's hoard collection with rankings.
 */
@Serializable
data class Hoard(
    val items: List<HoardedItem> = emptyList(),
    val totalValue: Int = 0,  // Cached total value
    val prestigeBonus: Int = 0,  // Bonus prestige from rare items
    val completedSets: Set<String> = emptySet()  // Collection sets completed
) {
    /**
     * Get count of items by type.
     */
    fun getItemCountByType(type: HoardItemType, catalog: List<HoardItem>): Int {
        return items.count { hoardedItem ->
            catalog.find { it.id == hoardedItem.itemId }?.type == type
        }
    }
    
    /**
     * Get count of items by rarity.
     */
    fun getItemCountByRarity(rarity: HoardRarity, catalog: List<HoardItem>): Int {
        return items.count { hoardedItem ->
            catalog.find { it.id == hoardedItem.itemId }?.rarity == rarity
        }
    }
    
    /**
     * Check if a collection set is complete.
     */
    fun hasCompletedSet(setId: String, catalog: List<HoardItem>): Boolean {
        val setItems = catalog.filter { it.setId == setId }
        if (setItems.isEmpty()) return false
        
        val ownedSetItems = items.mapNotNull { hoardedItem ->
            catalog.find { it.id == hoardedItem.itemId }
        }.filter { it.setId == setId }
        
        return ownedSetItems.size == setItems.size
    }
}

/**
 * Ranking tier based on hoard value.
 */
@Serializable
enum class HoardRank(val minValue: Int, val displayName: String, val prestigeBonus: Int) {
    NOVICE_COLLECTOR(0, "Novice Collector", 0),
    AMATEUR_HOARDER(1000, "Amateur Hoarder", 10),
    SKILLED_COLLECTOR(5000, "Skilled Collector", 25),
    EXPERT_HOARDER(15000, "Expert Hoarder", 50),
    MASTER_COLLECTOR(40000, "Master Collector", 100),
    LEGENDARY_HOARDER(100000, "Legendary Hoarder", 250),
    MYTHICAL_DRAGON(250000, "Mythical Dragon", 500)  // Ultimate rank
}

/**
 * Leaderboard entry for hoard rankings.
 */
@Serializable
data class HoardLeaderboardEntry(
    val playerName: String,
    val totalValue: Int,
    val rank: HoardRank,
    val itemCount: Int,
    val rareItemCount: Int  // Count of Epic+ items
) {
    init {
        require(playerName.isNotBlank()) { "Player name cannot be blank" }
        require(totalValue >= 0) { "Total value cannot be negative" }
        require(itemCount >= 0) { "Item count cannot be negative" }
        require(rareItemCount >= 0) { "Rare item count cannot be negative" }
    }
}

/**
 * Result of hoard operations.
 */
sealed class HoardResult {
    data class Success(val newHoard: Hoard, val valueChange: Int) : HoardResult()
    data class Failure(val reason: HoardFailure) : HoardResult()
}

enum class HoardFailure {
    ITEM_NOT_FOUND,
    ITEM_NOT_IN_HOARD,
    ITEM_ALREADY_IN_HOARD,
    INVALID_CONDITION
}

/**
 * Set bonus for completing collection sets.
 */
@Serializable
data class SetBonus(
    val setId: String,
    val setName: String,
    val description: String,
    val bonusPrestige: Int,
    val bonusValueMultiplier: Float = 1.2f  // 20% bonus to set items
) {
    init {
        require(setId.isNotBlank()) { "Set ID cannot be blank" }
        require(setName.isNotBlank()) { "Set name cannot be blank" }
        require(bonusPrestige >= 0) { "Bonus prestige cannot be negative" }
        require(bonusValueMultiplier >= 1.0f) { "Bonus multiplier must be at least 1.0" }
    }
}
