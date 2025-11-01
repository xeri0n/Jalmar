package com.jalmarquest.shared.nest

import kotlinx.serialization.Serializable

/**
 * Types of trophies players can display in their nest.
 * Each type represents a different category of achievement.
 */
@Serializable
enum class TrophyType {
    /** Trophy for defeating a boss enemy */
    BOSS_DEFEATED,
    
    /** Trophy for completing a major quest */
    QUEST_COMPLETE,
    
    /** Trophy for reaching a milestone (level 10, 100 quests, etc.) */
    MILESTONE,
    
    /** Trophy for discovering a location or secret */
    DISCOVERY,
    
    /** Trophy for completing a collection (all items, all enemies, etc.) */
    COLLECTION,
    
    /** Trophy for combat achievements (flawless victory, combo kills, etc.) */
    COMBAT,
    
    /** Trophy for social achievements (NPC relationships, faction standing) */
    SOCIAL,
    
    /** Trophy for rare/unique events */
    SPECIAL
}

/**
 * Rarity of trophies.
 * Rarer trophies provide more prestige and stronger NPC reactions.
 */
@Serializable
enum class TrophyRarity(val prestigeMultiplier: Float, val displayName: String) {
    COMMON(1.0f, "Common"),
    UNCOMMON(1.5f, "Uncommon"),
    RARE(2.0f, "Rare"),
    EPIC(3.0f, "Epic"),
    LEGENDARY(5.0f, "Legendary")
}

/**
 * Size of trophy determines how many display slots it occupies.
 */
@Serializable
enum class TrophySize(val slotsRequired: Int) {
    SMALL(1),
    MEDIUM(2),
    LARGE(4)
}

/**
 * Definition of a trophy that can be displayed in the nest.
 * Immutable catalog data.
 */
@Serializable
data class Trophy(
    val id: String,
    val name: String,
    val description: String,
    val type: TrophyType,
    val rarity: TrophyRarity,
    val size: TrophySize,
    val basePrestige: Int,
    val unlockAchievementId: String?  // Achievement that unlocks this trophy (null = always available)
) {
    init {
        require(id.isNotBlank()) { "Trophy ID cannot be blank" }
        require(name.isNotBlank()) { "Trophy name cannot be blank" }
        require(description.isNotBlank()) { "Trophy description cannot be blank" }
        require(basePrestige > 0) { "Base prestige must be positive" }
    }
    
    /**
     * Calculate total prestige value including rarity multiplier.
     */
    fun calculatePrestige(): Int {
        return (basePrestige * rarity.prestigeMultiplier).toInt()
    }
}

/**
 * Instance of a trophy displayed in the nest trophy room.
 * Tracks position in display grid.
 */
@Serializable
data class DisplayedTrophy(
    val trophyId: String,
    val slotIndex: Int  // 0-based index in trophy room grid
) {
    init {
        require(trophyId.isNotBlank()) { "DisplayedTrophy trophyId cannot be blank" }
        require(slotIndex >= 0) { "Slot index cannot be negative" }
    }
}

/**
 * NPC reactions to trophies when visiting the nest.
 * Different NPCs react differently based on personality and relationship.
 */
@Serializable
sealed class VisitorReaction {
    /**
     * NPC is impressed by a trophy.
     */
    @Serializable
    data class Impressed(
        val trophyId: String,
        val dialogueLine: String,
        val relationshipBonus: Int = 5
    ) : VisitorReaction()
    
    /**
     * NPC is envious of a trophy (rival NPCs).
     */
    @Serializable
    data class Envious(
        val trophyId: String,
        val dialogueLine: String,
        val relationshipPenalty: Int = -2
    ) : VisitorReaction()
    
    /**
     * NPC admires a trophy (mentor/friend NPCs).
     */
    @Serializable
    data class Admiring(
        val trophyId: String,
        val dialogueLine: String,
        val relationshipBonus: Int = 10
    ) : VisitorReaction()
    
    /**
     * NPC is indifferent to a trophy.
     */
    @Serializable
    data class Indifferent(
        val trophyId: String
    ) : VisitorReaction()
    
    /**
     * NPC shares a story related to a trophy.
     */
    @Serializable
    data class Storytelling(
        val trophyId: String,
        val dialogueLine: String,
        val loreFragmentId: String?  // Optional lore unlock
    ) : VisitorReaction()
}

/**
 * Trophy room configuration.
 * Defines how many trophies can be displayed and grid layout.
 */
@Serializable
data class TrophyRoom(
    val maxDisplaySlots: Int = 20,  // Total display slots (upgradeable)
    val displayedTrophies: List<DisplayedTrophy> = emptyList(),
    val totalPrestige: Int = 0  // Cached total prestige from all displayed trophies
) {
    init {
        require(maxDisplaySlots > 0) { "Max display slots must be positive" }
        require(displayedTrophies.size <= maxDisplaySlots) { "Cannot display more trophies than slots" }
    }
    
    /**
     * Get number of occupied slots (accounting for trophy sizes).
     */
    fun getOccupiedSlots(trophyCatalog: List<Trophy>): Int {
        return displayedTrophies.sumOf { displayedTrophy ->
            trophyCatalog.find { it.id == displayedTrophy.trophyId }?.size?.slotsRequired ?: 0
        }
    }
    
    /**
     * Get number of remaining slots.
     */
    fun getRemainingSlots(trophyCatalog: List<Trophy>): Int {
        return maxDisplaySlots - getOccupiedSlots(trophyCatalog)
    }
    
    /**
     * Check if a trophy can fit in remaining slots.
     */
    fun canFitTrophy(trophy: Trophy, trophyCatalog: List<Trophy>): Boolean {
        return getRemainingSlots(trophyCatalog) >= trophy.size.slotsRequired
    }
}

/**
 * Result of trophy display operations.
 */
sealed class TrophyDisplayResult {
    data class Success(val newTrophyRoom: TrophyRoom, val prestigeGained: Int) : TrophyDisplayResult()
    data class Failure(val reason: TrophyDisplayFailure) : TrophyDisplayResult()
}

enum class TrophyDisplayFailure {
    TROPHY_NOT_FOUND,
    TROPHY_LOCKED,
    TROPHY_ALREADY_DISPLAYED,
    NOT_ENOUGH_SLOTS,
    INVALID_SLOT_INDEX
}

/**
 * Result of trophy removal operations.
 */
sealed class TrophyRemovalResult {
    data class Success(val newTrophyRoom: TrophyRoom, val prestigeLost: Int) : TrophyRemovalResult()
    data class Failure(val reason: TrophyRemovalFailure) : TrophyRemovalResult()
}

enum class TrophyRemovalFailure {
    TROPHY_NOT_DISPLAYED,
    INVALID_SLOT_INDEX
}
