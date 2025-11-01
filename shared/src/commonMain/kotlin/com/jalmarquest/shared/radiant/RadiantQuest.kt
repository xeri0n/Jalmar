package com.jalmarquest.shared.radiant

import com.jalmarquest.shared.quest.ObjectiveType
import com.jalmarquest.shared.quest.QuestDifficulty
import com.jalmarquest.shared.quest.QuestType
import kotlinx.serialization.Serializable

/**
 * Template for generating procedural radiant quests.
 * 
 * Radiant quests are AI-generated quests that adapt to player level, location, and game state.
 * Templates define the structure, while actual quests are generated with context-specific parameters.
 * 
 * Example: "Gather Seeds for {npcName}" becomes "Gather Seeds for Grumble Forgepaw"
 * 
 * @property templateId Unique template identifier
 * @property nameTemplate Name with parameter slots ("{npcName}", "{itemName}", etc.)
 * @property descriptionTemplate Description narrative with parameter slots
 * @property questType Type of quest (FETCH, COMBAT, EXPLORATION, etc.)
 * @property baseDifficulty Base difficulty (can be adjusted by context)
 * @property objectiveTemplates List of objective templates
 * @property rewardScaling Reward calculation rules
 * @property contextRequirements Conditions for quest generation
 * @property cooldownTicks Minimum ticks between generations of this template
 * @property metadata Additional template data
 */
@Serializable
data class RadiantQuestTemplate(
    val templateId: String,
    val nameTemplate: String,
    val descriptionTemplate: String,
    val questType: QuestType,
    val baseDifficulty: QuestDifficulty,
    val objectiveTemplates: List<ObjectiveTemplate>,
    val rewardScaling: RewardScaling,
    val contextRequirements: ContextRequirements = ContextRequirements(),
    val cooldownTicks: Long = 0,
    val metadata: Map<String, String> = emptyMap()
) {
    init {
        require(templateId.isNotBlank()) { "Template ID cannot be blank" }
        require(nameTemplate.isNotBlank()) { "Name template cannot be blank" }
        require(descriptionTemplate.isNotBlank()) { "Description template cannot be blank" }
        require(objectiveTemplates.isNotEmpty()) { "Template must have at least one objective" }
        require(cooldownTicks >= 0) { "Cooldown ticks cannot be negative" }
    }
}

/**
 * Template for a single quest objective with variable parameters.
 * 
 * @property type Type of objective (KILL, COLLECT, REACH, TALK, etc.)
 * @property descriptionTemplate Description with parameter slots
 * @property targetParameter Parameter name for target selection ("itemId", "npcId", "locationId", "enemyId")
 * @property countMin Minimum count for objective
 * @property countMax Maximum count for objective
 * @property isOptional Whether this objective is required
 */
@Serializable
data class ObjectiveTemplate(
    val type: ObjectiveType,
    val descriptionTemplate: String,
    val targetParameter: String,
    val countMin: Int = 1,
    val countMax: Int = 1,
    val isOptional: Boolean = false
) {
    init {
        require(descriptionTemplate.isNotBlank()) { "Description template cannot be blank" }
        require(targetParameter.isNotBlank()) { "Target parameter cannot be blank" }
        require(countMin >= 1) { "Count min must be at least 1, got $countMin" }
        require(countMax >= countMin) { "Count max ($countMax) must be >= count min ($countMin)" }
    }
    
    /**
     * Get random count within range.
     */
    fun randomCount(): Int = (countMin..countMax).random()
}

/**
 * Reward scaling rules based on player level and difficulty.
 * 
 * Formula:
 * - XP = baseXP + (xpPerLevel * playerLevel) * difficultyMultiplier
 * - Seeds = baseSeeds + (seedsPerLevel * playerLevel) * difficultyMultiplier
 * - Glimmer Shards = baseGlimmerShards + (glimmerShardsPerLevel * playerLevel) * difficultyMultiplier
 * 
 * Difficulty Multipliers:
 * - TRIVIAL: 0.5x
 * - EASY: 0.75x
 * - MEDIUM: 1.0x
 * - HARD: 1.5x
 * - EXPERT: 2.0x
 * - LEGENDARY: 3.0x
 * 
 * @property baseXP Base XP reward at level 1
 * @property xpPerLevel Additional XP per player level
 * @property baseSeeds Base seed reward
 * @property seedsPerLevel Additional seeds per level
 * @property baseGlimmerShards Base glimmer shard reward
 * @property glimmerShardsPerLevel Additional shards per level
 * @property itemRewardPool List of possible item rewards (one selected randomly)
 * @property itemRewardChance Probability of granting item reward (0.0-1.0)
 */
@Serializable
data class RewardScaling(
    val baseXP: Long,
    val xpPerLevel: Long,
    val baseSeeds: Int,
    val seedsPerLevel: Int,
    val baseGlimmerShards: Int = 0,
    val glimmerShardsPerLevel: Int = 0,
    val itemRewardPool: List<String> = emptyList(),
    val itemRewardChance: Double = 0.0
) {
    init {
        require(baseXP >= 0) { "Base XP cannot be negative" }
        require(xpPerLevel >= 0) { "XP per level cannot be negative" }
        require(baseSeeds >= 0) { "Base seeds cannot be negative" }
        require(seedsPerLevel >= 0) { "Seeds per level cannot be negative" }
        require(baseGlimmerShards >= 0) { "Base glimmer shards cannot be negative" }
        require(glimmerShardsPerLevel >= 0) { "Glimmer shards per level cannot be negative" }
        require(itemRewardChance in 0.0..1.0) { "Item reward chance must be 0.0-1.0, got $itemRewardChance" }
    }
    
    companion object {
        /**
         * Difficulty multipliers for reward scaling.
         */
        fun difficultyMultiplier(difficulty: QuestDifficulty): Double {
            return when (difficulty) {
                QuestDifficulty.TRIVIAL -> 0.5
                QuestDifficulty.EASY -> 0.75
                QuestDifficulty.MEDIUM -> 1.0
                QuestDifficulty.HARD -> 1.5
                QuestDifficulty.EXPERT -> 2.0
                QuestDifficulty.LEGENDARY -> 3.0
            }
        }
    }
}

/**
 * Context requirements for quest generation.
 * 
 * Validates whether a quest can be generated in current game state.
 * 
 * @property minPlayerLevel Minimum player level to generate
 * @property maxPlayerLevel Maximum player level to generate
 * @property requiredBiomes Biomes where quest can generate (empty = any)
 * @property requiredNPCTypes NPC types needed (e.g., "merchant", "craftsman")
 * @property requiredItems Items that must be available in game
 * @property requiredLocations Locations that must be discovered
 * @property excludedIfQuestsActive Quest IDs that conflict (don't generate if active)
 * @property minAITension Minimum AI Director tension (0-100)
 * @property maxAITension Maximum AI Director tension (0-100)
 */
@Serializable
data class ContextRequirements(
    val minPlayerLevel: Int = 1,
    val maxPlayerLevel: Int = 50,
    val requiredBiomes: List<String> = emptyList(),
    val requiredNPCTypes: List<String> = emptyList(),
    val requiredItems: List<String> = emptyList(),
    val requiredLocations: List<String> = emptyList(),
    val excludedIfQuestsActive: List<String> = emptyList(),
    val minAITension: Int = 0,
    val maxAITension: Int = 100
) {
    init {
        require(minPlayerLevel >= 1) { "Min player level must be at least 1" }
        require(maxPlayerLevel >= minPlayerLevel) { "Max player level ($maxPlayerLevel) must be >= min ($minPlayerLevel)" }
        require(minAITension in 0..100) { "Min AI tension must be 0-100, got $minAITension" }
        require(maxAITension in 0..100) { "Max AI tension must be 0-100, got $maxAITension" }
        require(maxAITension >= minAITension) { "Max AI tension ($maxAITension) must be >= min ($minAITension)" }
    }
}

/**
 * State tracking for radiant quest system.
 * 
 * Tracks generated quests, template cooldowns, and generation statistics.
 * 
 * @property generatedQuests Map of generated quest ID → template ID
 * @property templateCooldowns Map of template ID → expiry timestamp (game ticks)
 * @property generationCount Map of template ID → number of times generated
 */
@Serializable
data class RadiantQuestState(
    val generatedQuests: Map<String, String> = emptyMap(),
    val templateCooldowns: Map<String, Long> = emptyMap(),
    val generationCount: Map<String, Int> = emptyMap()
)

/**
 * Result of quest generation.
 */
sealed class GenerateQuestResult {
    data class Success(
        val state: RadiantQuestState,
        val generatedQuestId: String,
        val parameters: Map<String, String>
    ) : GenerateQuestResult()
    
    data class Failure(val reason: GenerationFailure) : GenerateQuestResult()
}

/**
 * Reasons why quest generation can fail.
 */
enum class GenerationFailure {
    TEMPLATE_NOT_FOUND,
    TEMPLATE_ON_COOLDOWN,
    CONTEXT_REQUIREMENTS_NOT_MET,
    NO_VALID_TARGETS_AVAILABLE,
    PLAYER_LEVEL_OUT_OF_RANGE,
    AI_TENSION_OUT_OF_RANGE,
    CONFLICTING_QUESTS_ACTIVE
}

/**
 * Result of context validation.
 */
sealed class ContextValidationResult {
    data object Valid : ContextValidationResult()
    data class Invalid(val reason: String) : ContextValidationResult()
}
