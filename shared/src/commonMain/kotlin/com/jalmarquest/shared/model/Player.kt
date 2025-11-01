package com.jalmarquest.shared.model

import com.jalmarquest.shared.equipment.Equipment
import com.jalmarquest.shared.equipment.EquipmentSlot
import com.jalmarquest.shared.inventory.Inventory
import kotlinx.serialization.Serializable

/**
 * Core player data model containing all player state.
 */
@Serializable
data class Player(
    val id: String,
    val name: String,
    val level: Int = 1,
    val experience: Long = 0,
    val stats: PlayerStats = PlayerStats(),
    val position: Position = Position(0, 0, "starting_village"),
    val inventory: Inventory = Inventory(),
    val equippedItems: Map<EquipmentSlot, Equipment> = emptyMap(),
    val seeds: Long = 0,
    val glimmerShards: Long = 0,
    val playTimeSeconds: Long = 0,
    val learnedSkills: Set<String> = emptySet(),
    val skillPoints: Int = 0
) {
    init {
        require(name.isNotBlank()) { "Player name cannot be blank" }
        require(level >= 1) { "Level must be at least 1" }
        require(level <= 50) { "Level cannot exceed 50" }
        require(experience >= 0) { "Experience cannot be negative" }
        require(seeds >= 0) { "Seeds cannot be negative" }
        require(glimmerShards >= 0) { "Glimmer Shards cannot be negative" }
        require(skillPoints >= 0) { "Skill points cannot be negative" }
    }
    
    fun getExperienceForNextLevel(): Long {
        // Simple exponential curve: 100 * level^2
        return 100L * (level + 1) * (level + 1)
    }
    
    fun canLevelUp(): Boolean {
        return experience >= getExperienceForNextLevel() && level < 50
    }
}

/**
 * Player position in the game world.
 */
@Serializable
data class Position(
    val x: Int,
    val y: Int,
    val locationId: String = "starting_village"
) {
    fun distanceTo(other: Position): Double {
        if (locationId != other.locationId) return Double.POSITIVE_INFINITY
        val dx = (x - other.x).toDouble()
        val dy = (y - other.y).toDouble()
        return kotlin.math.sqrt(dx * dx + dy * dy)
    }
}
