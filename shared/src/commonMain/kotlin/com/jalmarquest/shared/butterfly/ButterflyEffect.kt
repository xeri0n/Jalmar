package com.jalmarquest.shared.butterfly

import kotlinx.serialization.Serializable

/**
 * Butterfly Effect Engine: Long-term consequence tracking system.
 * 
 * The Butterfly Effect Engine is the heart of JalmarQuest's "every choice matters" philosophy.
 * It tracks ALL player decisions - from major quest outcomes to minor dialogue choices - and
 * creates cascading consequences that emerge over time, often in unexpected ways.
 * 
 * Core Principles:
 * - **Memory**: Every choice is permanently recorded (never forgotten)
 * - **Delayed Impact**: Consequences don't always trigger immediately
 * - **Cascading Effects**: One choice can trigger a chain of consequences
 * - **Emergent Storytelling**: Combinations of choices create unique narratives
 * - **Authenticity**: Small decisions (like being rude to an NPC) have real impacts
 * 
 * Example Chains:
 * - Save a beetle from drowning → Beetle becomes companion → Beetle alerts you to ambush
 * - Steal from merchant → Merchant spreads rumors → Other merchants raise prices
 * - Defeat boss peacefully → Boss NPC becomes ally → Unlocks secret ending path
 * - Help NPC find lost item → NPC remembers kindness → NPC saves you in crisis
 * 
 * Architecture:
 * - Stateless functional design (all state in ButterflyEffectState)
 * - Trigger-based evaluation (time, quest, location, NPC, combination triggers)
 * - Consequence chains (one choice → multiple delayed effects)
 * - Integration with all major systems (dialogue, quests, combat, NPCs, world state)
 */

/**
 * Player choice record tracking what decision was made and context.
 * 
 * @property id Unique choice identifier (generated UUID)
 * @property category Type of choice (DIALOGUE, QUEST, COMBAT, etc.)
 * @property choiceKey Identifier for what was chosen (e.g., "dialogue_grumble_insult", "quest_save_beetle")
 * @property timestamp When choice was made (game world time in ticks)
 * @property locationId Where choice was made
 * @property involvedNPCs NPCs involved in the choice
 * @property impact Severity of choice (MINOR, MODERATE, MAJOR, CRITICAL)
 * @property metadata Additional context (JSON-compatible map)
 */
@Serializable
data class PlayerChoice(
    val id: String,
    val category: ChoiceCategory,
    val choiceKey: String,
    val timestamp: Long,
    val locationId: String,
    val involvedNPCs: List<String> = emptyList(),
    val impact: ChoiceImpact = ChoiceImpact.MODERATE,
    val metadata: Map<String, String> = emptyMap()
) {
    init {
        require(id.isNotBlank()) { "Choice ID cannot be blank" }
        require(choiceKey.isNotBlank()) { "Choice key cannot be blank" }
        require(locationId.isNotBlank()) { "Location ID cannot be blank" }
        require(timestamp >= 0) { "Timestamp cannot be negative" }
    }
}

/**
 * Category of player choice for tracking and consequence assignment.
 */
@Serializable
enum class ChoiceCategory {
    DIALOGUE,       // Dialogue option selected
    QUEST,          // Quest accepted, completed, or abandoned
    COMBAT,         // Combat decision (mercy, kill, flee, tactics)
    EXPLORATION,    // Exploration choice (path taken, item collected)
    SOCIAL,         // Social interaction (gift, trade, help)
    MORAL,          // Explicit moral choice (steal, save, sacrifice)
    FACTION         // Faction allegiance choice
}

/**
 * Impact level of choice for consequence severity.
 */
@Serializable
enum class ChoiceImpact {
    MINOR,          // Small ripple (single NPC relationship +/-5)
    MODERATE,       // Notable effect (multiple NPCs, minor quest unlock)
    MAJOR,          // Significant consequence (faction standing, major quest branch)
    CRITICAL        // Game-changing (ending paths, permanent world changes)
}

/**
 * Consequence that triggers based on player choices.
 * 
 * @property id Unique consequence identifier
 * @property triggeringChoiceId Choice that caused this consequence
 * @property type What kind of effect (NPC relationship, world state, etc.)
 * @property trigger When/how this consequence activates
 * @property effectKey Identifier for what happens (e.g., "npc_grumble_refuses_trade")
 * @property magnitude Strength of effect (context-dependent, typically 1-100)
 * @property description Human-readable description for debugging/UI
 * @property hasTriggered Whether this consequence has already occurred
 * @property chainedConsequences Additional consequences this creates (cascading effects)
 */
@Serializable
data class Consequence(
    val id: String,
    val triggeringChoiceId: String,
    val type: ConsequenceType,
    val trigger: ConsequenceTrigger,
    val effectKey: String,
    val magnitude: Int = 50,
    val description: String,
    val hasTriggered: Boolean = false,
    val chainedConsequences: List<String> = emptyList()
) {
    init {
        require(id.isNotBlank()) { "Consequence ID cannot be blank" }
        require(triggeringChoiceId.isNotBlank()) { "Triggering choice ID cannot be blank" }
        require(effectKey.isNotBlank()) { "Effect key cannot be blank" }
        require(magnitude in 1..100) { "Magnitude must be 1-100" }
        require(description.isNotBlank()) { "Description cannot be blank" }
    }
}

/**
 * Type of consequence effect.
 */
@Serializable
enum class ConsequenceType {
    NPC_RELATIONSHIP,       // Modify NPC relationship value
    WORLD_STATE,            // Change world flag or state
    QUEST_UNLOCK,           // Enable/disable quest availability
    QUEST_OBJECTIVE,        // Modify quest objectives
    ITEM_AVAILABILITY,      // Make item available/unavailable in shops
    FACTION_STANDING,       // Modify faction reputation
    LOCATION_ACCESS,        // Unlock/lock location
    NPC_BEHAVIOR,           // Change NPC dialogue or actions
    COMPANION_UNLOCK,       // Enable companion recruitment
    ENDING_PATH,            // Affect ending availability
    LORE_UNLOCK,            // Reveal lore fragment
    ACHIEVEMENT,            // Grant achievement
    SPECIAL_EVENT           // Trigger one-time event
}

/**
 * Trigger conditions for when a consequence activates.
 * 
 * Sealed class hierarchy for type-safe trigger evaluation.
 */
@Serializable
sealed class ConsequenceTrigger {
    /**
     * Triggers after X game time passes.
     * @property ticksDelay Ticks to wait after choice (60 ticks = 1 minute)
     */
    @Serializable
    data class TimeBased(val ticksDelay: Long) : ConsequenceTrigger()
    
    /**
     * Triggers when specific quest completes.
     * @property questId Quest that must be completed
     */
    @Serializable
    data class QuestBased(val questId: String) : ConsequenceTrigger()
    
    /**
     * Triggers when player visits location.
     * @property locationId Location that must be visited
     */
    @Serializable
    data class LocationBased(val locationId: String) : ConsequenceTrigger()
    
    /**
     * Triggers on next interaction with NPC.
     * @property npcId NPC that must be interacted with
     */
    @Serializable
    data class NPCBased(val npcId: String) : ConsequenceTrigger()
    
    /**
     * Triggers when multiple conditions are met.
     * @property conditions All conditions that must be satisfied
     * @property requireAll If true, all conditions must be met; if false, any one suffices
     */
    @Serializable
    data class CombinationTrigger(
        val conditions: List<ConsequenceTrigger>,
        val requireAll: Boolean = true
    ) : ConsequenceTrigger()
    
    /**
     * Triggers immediately when consequence is created.
     */
    @Serializable
    object Immediate : ConsequenceTrigger()
}

/**
 * Butterfly Effect state tracking all choices and consequences.
 * 
 * @property playerChoices All choices made by player (permanent record)
 * @property pendingConsequences Consequences waiting to trigger
 * @property triggeredConsequences Consequences that have already occurred
 * @property consequenceChains Mapping of choice IDs to consequence chains
 */
@Serializable
data class ButterflyEffectState(
    val playerChoices: List<PlayerChoice> = emptyList(),
    val pendingConsequences: List<Consequence> = emptyList(),
    val triggeredConsequences: List<Consequence> = emptyList(),
    val consequenceChains: Map<String, List<String>> = emptyMap() // choiceId → consequenceIds
) {
    /**
     * Get all choices of a specific category.
     */
    fun getChoicesByCategory(category: ChoiceCategory): List<PlayerChoice> {
        return playerChoices.filter { it.category == category }
    }
    
    /**
     * Get all choices involving a specific NPC.
     */
    fun getChoicesByNPC(npcId: String): List<PlayerChoice> {
        return playerChoices.filter { it.involvedNPCs.contains(npcId) }
    }
    
    /**
     * Check if a specific choice was made.
     */
    fun hasChoice(choiceKey: String): Boolean {
        return playerChoices.any { it.choiceKey == choiceKey }
    }
    
    /**
     * Get pending consequences for a specific choice.
     */
    fun getPendingConsequencesForChoice(choiceId: String): List<Consequence> {
        return pendingConsequences.filter { it.triggeringChoiceId == choiceId }
    }
    
    /**
     * Count choices by impact level.
     */
    fun getChoiceCountByImpact(impact: ChoiceImpact): Int {
        return playerChoices.count { it.impact == impact }
    }
}

/**
 * Result of recording a choice.
 */
@Serializable
sealed class ChoiceResult {
    @Serializable
    data class Success(
        val updatedState: ButterflyEffectState,
        val choiceId: String,
        val triggeredConsequences: List<Consequence>
    ) : ChoiceResult()
    
    @Serializable
    data class Failure(val reason: ChoiceFailure) : ChoiceResult()
}

/**
 * Result of evaluating consequences.
 */
@Serializable
sealed class ConsequenceEvaluationResult {
    @Serializable
    data class Success(
        val updatedState: ButterflyEffectState,
        val newlyTriggeredConsequences: List<Consequence>
    ) : ConsequenceEvaluationResult()
    
    @Serializable
    data class Failure(val reason: ChoiceFailure) : ConsequenceEvaluationResult()
}

/**
 * Failure reasons for choice/consequence operations.
 */
@Serializable
enum class ChoiceFailure {
    INVALID_CHOICE_DATA,        // Missing required fields
    DUPLICATE_CHOICE,           // Choice already recorded
    CONSEQUENCE_NOT_FOUND,      // Referenced consequence doesn't exist
    TRIGGER_ERROR               // Trigger evaluation failed
}
