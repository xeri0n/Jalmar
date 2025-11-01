package com.jalmarquest.shared.quest

import kotlinx.serialization.Serializable

/**
 * Type of quest (categorization).
 */
enum class QuestType {
    MAIN,           // Story-critical quest
    SIDE,           // Optional side quest
    FETCH,          // Collect items quest
    COMBAT,         // Combat-focused quest
    EXPLORATION,    // Discovery/travel quest
    TUTORIAL        // Early game tutorial quest
}

/**
 * Type of quest objective.
 */
enum class ObjectiveType {
    KILL,           // Defeat X enemies
    COLLECT,        // Gather X items
    REACH,          // Visit location
    TALK,           // Speak with NPC
    CRAFT,          // Craft item
    EQUIP,          // Equip item
    LEVEL,          // Reach player level
    DUNGEON_CLEAR   // Complete dungeon
}

/**
 * Quest difficulty tier.
 */
enum class QuestDifficulty {
    TRIVIAL,        // Level 1-3
    EASY,           // Level 4-10
    MEDIUM,         // Level 11-20
    HARD,           // Level 21-35
    EXPERT,         // Level 36-50
    LEGENDARY       // Endgame content
}

/**
 * A single objective within a quest.
 * 
 * @property type Type of objective (kill, collect, etc.)
 * @property description Human-readable objective text
 * @property targetId Target identifier (enemy ID, item ID, location ID, NPC ID, etc.)
 * @property targetCount Number required to complete (e.g., "Kill 5 beetles")
 * @property currentProgress Current progress toward completion
 * @property isOptional Whether this objective is required for quest completion
 */
@Serializable
data class QuestObjective(
    val type: ObjectiveType,
    val description: String,
    val targetId: String = "",
    val targetCount: Int = 1,
    val currentProgress: Int = 0,
    val isOptional: Boolean = false
) {
    init {
        require(description.isNotBlank()) { "Objective description cannot be blank" }
        require(targetCount >= 1) { "Target count must be at least 1, got $targetCount" }
        require(currentProgress >= 0) { "Progress cannot be negative, got $currentProgress" }
    }
    
    /**
     * Returns whether this objective is complete.
     */
    fun isComplete(): Boolean = currentProgress >= targetCount
    
    /**
     * Returns progress as a percentage (0.0 to 1.0).
     */
    fun progressPercentage(): Float {
        return (currentProgress.toFloat() / targetCount.toFloat()).coerceIn(0.0f, 1.0f)
    }
    
    /**
     * Returns formatted progress string (e.g., "3/5").
     */
    fun progressString(): String = "$currentProgress/$targetCount"
}

/**
 * Rewards granted upon quest completion.
 * 
 * @property xp Experience points awarded
 * @property items List of item IDs to grant
 * @property seeds Seed currency reward
 * @property glimmerShards Glimmer Shard currency reward
 * @property unlockRecipeIds Recipe IDs to unlock
 * @property unlockLocationIds Location IDs to unlock
 */
@Serializable
data class QuestReward(
    val xp: Long = 0,
    val items: List<String> = emptyList(),
    val seeds: Int = 0,
    val glimmerShards: Int = 0,
    val unlockRecipeIds: List<String> = emptyList(),
    val unlockLocationIds: List<String> = emptyList()
) {
    init {
        require(xp >= 0) { "XP reward cannot be negative, got $xp" }
        require(seeds >= 0) { "Seed reward cannot be negative, got $seeds" }
        require(glimmerShards >= 0) { "Glimmer Shard reward cannot be negative, got $glimmerShards" }
    }
    
    /**
     * Returns whether this reward grants anything.
     */
    fun hasRewards(): Boolean {
        return xp > 0 || items.isNotEmpty() || seeds > 0 || glimmerShards > 0 ||
                unlockRecipeIds.isNotEmpty() || unlockLocationIds.isNotEmpty()
    }
}

/**
 * A quest in the game.
 * 
 * @property id Unique quest identifier
 * @property name Display name
 * @property description Quest narrative description
 * @property questType Type of quest (main, side, etc.)
 * @property difficulty Difficulty tier
 * @property objectives List of objectives to complete
 * @property rewards Rewards granted on completion
 * @property prerequisiteQuestIds Quest IDs that must be completed first
 * @property level Recommended player level
 * @property giver NPC ID who gives the quest (empty if discovered)
 * @property autoComplete Whether quest completes immediately when objectives done
 */
@Serializable
data class Quest(
    val id: String,
    val name: String,
    val description: String,
    val questType: QuestType,
    val difficulty: QuestDifficulty,
    val objectives: List<QuestObjective>,
    val rewards: QuestReward,
    val prerequisiteQuestIds: List<String> = emptyList(),
    val level: Int = 1,
    val giver: String = "",
    val autoComplete: Boolean = false
) {
    init {
        require(id.isNotBlank()) { "Quest ID cannot be blank" }
        require(name.isNotBlank()) { "Quest name cannot be blank" }
        require(description.isNotBlank()) { "Quest description cannot be blank" }
        require(objectives.isNotEmpty()) { "Quest must have at least 1 objective" }
        require(level in 1..50) { "Quest level must be 1-50, got $level" }
    }
    
    /**
     * Returns whether all required objectives are complete.
     */
    fun isComplete(): Boolean {
        return objectives.filter { !it.isOptional }.all { it.isComplete() }
    }
    
    /**
     * Returns overall quest progress as a percentage (0.0 to 1.0).
     * Based on required objectives only.
     */
    fun progressPercentage(): Float {
        val requiredObjectives = objectives.filter { !it.isOptional }
        if (requiredObjectives.isEmpty()) return 1.0f
        
        val totalProgress = requiredObjectives.sumOf { it.currentProgress }
        val totalRequired = requiredObjectives.sumOf { it.targetCount }
        
        return (totalProgress.toFloat() / totalRequired.toFloat()).coerceIn(0.0f, 1.0f)
    }
    
    /**
     * Returns difficulty color/indicator for UI.
     */
    fun difficultyColor(): String = when (difficulty) {
        QuestDifficulty.TRIVIAL -> "Gray"
        QuestDifficulty.EASY -> "Green"
        QuestDifficulty.MEDIUM -> "Yellow"
        QuestDifficulty.HARD -> "Orange"
        QuestDifficulty.EXPERT -> "Red"
        QuestDifficulty.LEGENDARY -> "Purple"
    }
}

/**
 * Player's current quest state.
 * 
 * @property questId Quest identifier
 * @property isActive Whether quest is currently tracked
 * @property isCompleted Whether all objectives done
 * @property isTurnedIn Whether quest was turned in for rewards
 * @property objectives Current objective states (with progress)
 */
@Serializable
data class QuestProgress(
    val questId: String,
    val isActive: Boolean = true,
    val isCompleted: Boolean = false,
    val isTurnedIn: Boolean = false,
    val objectives: List<QuestObjective> = emptyList()
) {
    init {
        require(questId.isNotBlank()) { "Quest ID cannot be blank" }
    }
    
    /**
     * Returns whether quest can be turned in for rewards.
     */
    fun canTurnIn(): Boolean = isCompleted && !isTurnedIn
}
