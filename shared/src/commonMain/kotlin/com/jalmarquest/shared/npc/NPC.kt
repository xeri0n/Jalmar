package com.jalmarquest.shared.npc

import kotlinx.serialization.Serializable

/**
 * Core NPC definition with personality, schedule, and faction.
 * 
 * @property id Unique NPC identifier
 * @property name NPC's display name
 * @property species NPC species (button quail, mole, mouse, sparrow)
 * @property personality NPC personality traits
 * @property homeLocationId NPC's home location
 * @property occupation NPC's role in the world
 * @property factionId Faction this NPC belongs to
 * @property defaultDialogueTreeId Default dialogue tree for conversations
 * @property questGiverIds Quest IDs this NPC can give
 * @property merchantInventory Item IDs NPC sells (if merchant)
 * @property schedule Daily schedule defining NPC movements
 */
@Serializable
data class NPC(
    val id: String,
    val name: String,
    val species: NPCSpecies = NPCSpecies.BUTTON_QUAIL,
    val personality: NPCPersonality,
    val homeLocationId: String,
    val occupation: NPCOccupation,
    val factionId: String,
    val defaultDialogueTreeId: String,
    val questGiverIds: List<String> = emptyList(),
    val merchantInventory: List<String> = emptyList(),
    val schedule: NPCSchedule
) {
    init {
        require(id.isNotBlank()) { "NPC ID cannot be blank" }
        require(name.isNotBlank()) { "NPC name cannot be blank" }
        require(homeLocationId.isNotBlank()) { "NPC must have a home location" }
        require(factionId.isNotBlank()) { "NPC must belong to a faction" }
        require(defaultDialogueTreeId.isNotBlank()) { "NPC must have a default dialogue tree" }
    }
    
    /**
     * Returns the NPC's current location based on the current time.
     */
    fun getCurrentLocation(currentHour: Int): String {
        val entry = schedule.getEntryForHour(currentHour)
        return entry?.locationId ?: homeLocationId
    }
    
    /**
     * Returns the NPC's current activity.
     */
    fun getCurrentActivity(currentHour: Int): String {
        val entry = schedule.getEntryForHour(currentHour)
        return entry?.activity ?: "resting"
    }
    
    /**
     * Checks if NPC is at a specific location at a given time.
     */
    fun isAtLocation(locationId: String, currentHour: Int): Boolean {
        return getCurrentLocation(currentHour) == locationId
    }
}

/**
 * NPC species types.
 */
@Serializable
enum class NPCSpecies {
    BUTTON_QUAIL,
    MOLE,
    MOUSE,
    SPARROW,
    BEETLE,      // Friendly beetle NPCs
    FIREFLY      // Mystical firefly NPCs
}

/**
 * NPC occupation/role.
 */
@Serializable
enum class NPCOccupation {
    ELDER,
    CRAFTSMAN,
    MERCHANT,
    WARRIOR,
    FARMER,
    SCHOLAR,
    CHILD,
    EXPLORER,
    GUARD,
    INNKEEPER
}

/**
 * NPC personality traits affecting behavior and dialogue.
 * 
 * @property friendliness 1-10 (grumpy to cheerful)
 * @property courage 1-10 (cowardly to brave)
 * @property wisdom 1-10 (naive to wise)
 * @property humor 1-10 (serious to joking)
 * @property traits List of personality descriptors
 */
@Serializable
data class NPCPersonality(
    val friendliness: Int,
    val courage: Int,
    val wisdom: Int,
    val humor: Int,
    val traits: List<String> = emptyList()
) {
    init {
        require(friendliness in 1..10) { "Friendliness must be 1-10" }
        require(courage in 1..10) { "Courage must be 1-10" }
        require(wisdom in 1..10) { "Wisdom must be 1-10" }
        require(humor in 1..10) { "Humor must be 1-10" }
    }
    
    /**
     * Returns a personality summary string.
     */
    fun getSummary(): String {
        val descriptors = mutableListOf<String>()
        if (friendliness >= 8) descriptors.add("cheerful")
        else if (friendliness <= 3) descriptors.add("grumpy")
        
        if (courage >= 8) descriptors.add("brave")
        else if (courage <= 3) descriptors.add("timid")
        
        if (wisdom >= 8) descriptors.add("wise")
        else if (wisdom <= 3) descriptors.add("naive")
        
        if (humor >= 8) descriptors.add("humorous")
        else if (humor <= 3) descriptors.add("serious")
        
        return (descriptors + traits).joinToString(", ")
    }
}

/**
 * NPC daily schedule defining where they are at different times.
 * 
 * @property entries List of schedule entries (time blocks)
 */
@Serializable
data class NPCSchedule(
    val entries: List<ScheduleEntry>
) {
    init {
        require(entries.isNotEmpty()) { "Schedule must have at least one entry" }
        // Validate no overlapping entries
        entries.forEachIndexed { index, entry ->
            entries.drop(index + 1).forEach { other ->
                val overlap = entry.startHour < other.endHour && entry.endHour > other.startHour
                require(!overlap) { 
                    "Schedule entries overlap: ${entry.startHour}-${entry.endHour} and ${other.startHour}-${other.endHour}" 
                }
            }
        }
    }
    
    /**
     * Gets the schedule entry for a specific hour.
     */
    fun getEntryForHour(hour: Int): ScheduleEntry? {
        require(hour in 0..23) { "Hour must be 0-23" }
        return entries.find { it.containsHour(hour) }
    }
    
    /**
     * Returns all locations this NPC visits.
     */
    fun getAllLocations(): List<String> {
        return entries.map { it.locationId }.distinct()
    }
}

/**
 * Single entry in an NPC's schedule.
 * 
 * @property startHour Start hour (0-23)
 * @property endHour End hour (0-23, exclusive)
 * @property locationId Where NPC is during this time
 * @property activity What NPC is doing
 */
@Serializable
data class ScheduleEntry(
    val startHour: Int,
    val endHour: Int,
    val locationId: String,
    val activity: String
) {
    init {
        require(startHour in 0..23) { "Start hour must be 0-23" }
        require(endHour in 0..24) { "End hour must be 0-24" }
        require(startHour < endHour) { "Start hour must be before end hour" }
        require(locationId.isNotBlank()) { "Location ID cannot be blank" }
        require(activity.isNotBlank()) { "Activity cannot be blank" }
    }
    
    /**
     * Checks if this entry contains a specific hour.
     */
    fun containsHour(hour: Int): Boolean {
        return hour >= startHour && hour < endHour
    }
    
    /**
     * Returns duration in hours.
     */
    fun getDuration(): Int = endHour - startHour
}

/**
 * Player's relationship with an NPC.
 * 
 * @property npcId NPC identifier
 * @property score Relationship score (-100 to 100)
 */
@Serializable
data class NPCRelationship(
    val npcId: String,
    val score: Int = 0
) {
    init {
        require(score in -100..100) { "Relationship score must be -100 to 100" }
    }
    
    /**
     * Gets relationship status based on score.
     */
    fun getStatus(): RelationshipStatus {
        return when {
            score >= 75 -> RelationshipStatus.BEST_FRIEND
            score >= 50 -> RelationshipStatus.FRIEND
            score >= 25 -> RelationshipStatus.FRIENDLY
            score >= -25 -> RelationshipStatus.NEUTRAL
            score >= -50 -> RelationshipStatus.UNFRIENDLY
            score >= -75 -> RelationshipStatus.HOSTILE
            else -> RelationshipStatus.ENEMY
        }
    }
    
    /**
     * Modifies relationship score and returns new relationship.
     */
    fun modify(change: Int): NPCRelationship {
        val newScore = (score + change).coerceIn(-100, 100)
        return copy(score = newScore)
    }
    
    /**
     * Returns true if relationship is positive (friendly or better).
     */
    fun isPositive(): Boolean = score >= 25
    
    /**
     * Returns true if relationship is negative (unfriendly or worse).
     */
    fun isNegative(): Boolean = score < -25
}

/**
 * Relationship status levels.
 */
@Serializable
enum class RelationshipStatus {
    ENEMY,
    HOSTILE,
    UNFRIENDLY,
    NEUTRAL,
    FRIENDLY,
    FRIEND,
    BEST_FRIEND
}

/**
 * Faction representing a group of NPCs.
 * 
 * @property id Unique faction identifier
 * @property name Faction display name
 * @property description Faction background
 * @property homeLocationId Faction headquarters
 * @property allyFactionIds Friendly factions
 * @property enemyFactionIds Hostile factions
 */
@Serializable
data class Faction(
    val id: String,
    val name: String,
    val description: String,
    val homeLocationId: String,
    val allyFactionIds: List<String> = emptyList(),
    val enemyFactionIds: List<String> = emptyList()
) {
    init {
        require(id.isNotBlank()) { "Faction ID cannot be blank" }
        require(name.isNotBlank()) { "Faction name cannot be blank" }
        require(homeLocationId.isNotBlank()) { "Faction must have a home location" }
    }
    
    /**
     * Checks if another faction is an ally.
     */
    fun isAlly(factionId: String): Boolean = allyFactionIds.contains(factionId)
    
    /**
     * Checks if another faction is an enemy.
     */
    fun isEnemy(factionId: String): Boolean = enemyFactionIds.contains(factionId)
}

/**
 * Player's standing with a faction.
 * 
 * @property factionId Faction identifier
 * @property reputation Reputation score (-100 to 100)
 */
@Serializable
data class FactionStanding(
    val factionId: String,
    val reputation: Int = 0
) {
    init {
        require(reputation in -100..100) { "Faction reputation must be -100 to 100" }
    }
    
    /**
     * Gets reputation tier based on score.
     */
    fun getTier(): ReputationTier {
        return when {
            reputation >= 75 -> ReputationTier.EXALTED
            reputation >= 50 -> ReputationTier.REVERED
            reputation >= 25 -> ReputationTier.HONORED
            reputation >= -25 -> ReputationTier.NEUTRAL
            reputation >= -50 -> ReputationTier.UNFRIENDLY
            reputation >= -75 -> ReputationTier.HOSTILE
            else -> ReputationTier.HATED
        }
    }
    
    /**
     * Modifies faction reputation and returns new standing.
     */
    fun modify(change: Int): FactionStanding {
        val newReputation = (reputation + change).coerceIn(-100, 100)
        return copy(reputation = newReputation)
    }
}

/**
 * Faction reputation tiers.
 */
@Serializable
enum class ReputationTier {
    HATED,
    HOSTILE,
    UNFRIENDLY,
    NEUTRAL,
    HONORED,
    REVERED,
    EXALTED
}
