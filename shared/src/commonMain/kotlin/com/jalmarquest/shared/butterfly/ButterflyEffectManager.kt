package com.jalmarquest.shared.butterfly

import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.model.Position
import java.util.UUID

/**
 * Stateless manager for Butterfly Effect operations.
 * 
 * The ButterflyEffectManager is responsible for:
 * - Recording player choices with full context
 * - Evaluating trigger conditions to activate consequences
 * - Applying consequence effects to game state
 * - Tracking consequence chains (cascading effects)
 * - Providing choice history queries
 * 
 * Integration Points:
 * - DialogueManager: Record dialogue choices
 * - QuestManager: Record quest decisions (accept, abandon, outcomes)
 * - CombatManager: Record combat choices (mercy, kill, flee)
 * - NPCManager: Apply relationship consequence effects
 * - WorldUpdateCoordinator: Periodic consequence evaluation
 * 
 * All operations are pure functions that return updated state.
 */
class ButterflyEffectManager {
    
    /**
     * Record a player choice and create pending consequences.
     * 
     * @param state Current butterfly effect state
     * @param category Type of choice
     * @param choiceKey Identifier for the choice (e.g., "dialogue_grumble_help")
     * @param timestamp Game world time in ticks
     * @param locationId Where choice was made
     * @param involvedNPCs NPCs involved in the choice
     * @param impact Severity of choice
     * @param metadata Additional context
     * @param consequences Consequences to create for this choice
     * @return Updated state with choice recorded and consequences pending
     */
    fun recordChoice(
        state: ButterflyEffectState,
        category: ChoiceCategory,
        choiceKey: String,
        timestamp: Long,
        locationId: String,
        involvedNPCs: List<String> = emptyList(),
        impact: ChoiceImpact = ChoiceImpact.MODERATE,
        metadata: Map<String, String> = emptyMap(),
        consequences: List<Consequence> = emptyList()
    ): ChoiceResult {
        // Validate input
        if (choiceKey.isBlank() || locationId.isBlank() || timestamp < 0) {
            return ChoiceResult.Failure(ChoiceFailure.INVALID_CHOICE_DATA)
        }
        
        // Check for duplicate choice
        if (state.hasChoice(choiceKey)) {
            return ChoiceResult.Failure(ChoiceFailure.DUPLICATE_CHOICE)
        }
        
        // Create player choice
        val choiceId = UUID.randomUUID().toString()
        val choice = PlayerChoice(
            id = choiceId,
            category = category,
            choiceKey = choiceKey,
            timestamp = timestamp,
            locationId = locationId,
            involvedNPCs = involvedNPCs,
            impact = impact,
            metadata = metadata
        )
        
        // Separate immediate and delayed consequences
        val immediateConsequences = consequences.filter { 
            it.trigger is ConsequenceTrigger.Immediate 
        }.map { it.copy(hasTriggered = true) }
        
        val delayedConsequences = consequences.filter { 
            it.trigger !is ConsequenceTrigger.Immediate 
        }
        
        // Update state
        val updatedState = state.copy(
            playerChoices = state.playerChoices + choice,
            pendingConsequences = state.pendingConsequences + delayedConsequences,
            triggeredConsequences = state.triggeredConsequences + immediateConsequences,
            consequenceChains = state.consequenceChains + (choiceId to consequences.map { it.id })
        )
        
        return ChoiceResult.Success(
            updatedState = updatedState,
            choiceId = choiceId,
            triggeredConsequences = immediateConsequences
        )
    }
    
    /**
     * Evaluate all pending consequences and trigger those whose conditions are met.
     * 
     * @param state Current butterfly effect state
     * @param gameState Current game state for trigger evaluation
     * @return Updated state with newly triggered consequences
     */
    fun evaluateConsequences(
        state: ButterflyEffectState,
        gameState: GameState
    ): ConsequenceEvaluationResult {
        val currentTimestamp = gameState.worldTime.totalTicks
        val currentLocation = gameState.player.position.locationId
        
        val newlyTriggered = mutableListOf<Consequence>()
        val stillPending = mutableListOf<Consequence>()
        
        for (consequence in state.pendingConsequences) {
            val shouldTrigger = evaluateTrigger(
                trigger = consequence.trigger,
                currentTimestamp = currentTimestamp,
                currentLocation = currentLocation,
                gameState = gameState,
                state = state,
                consequence = consequence
            )
            
            if (shouldTrigger) {
                newlyTriggered.add(consequence.copy(hasTriggered = true))
            } else {
                stillPending.add(consequence)
            }
        }
        
        // Update state
        val updatedState = state.copy(
            pendingConsequences = stillPending,
            triggeredConsequences = state.triggeredConsequences + newlyTriggered
        )
        
        return ConsequenceEvaluationResult.Success(
            updatedState = updatedState,
            newlyTriggeredConsequences = newlyTriggered
        )
    }
    
    /**
     * Evaluate a specific trigger condition.
     * 
     * @param trigger Trigger to evaluate
     * @param currentTimestamp Current game world time
     * @param currentLocation Current player location
     * @param gameState Full game state for complex checks
     * @param state Butterfly effect state for choice lookups
     * @param consequence The consequence being evaluated (for choice timestamp)
     * @return True if trigger conditions are met
     */
    private fun evaluateTrigger(
        trigger: ConsequenceTrigger,
        currentTimestamp: Long,
        currentLocation: String,
        gameState: GameState,
        state: ButterflyEffectState,
        consequence: Consequence
    ): Boolean {
        return when (trigger) {
            is ConsequenceTrigger.Immediate -> true
            
            is ConsequenceTrigger.TimeBased -> {
                // Get original choice timestamp
                val originalChoice = state.playerChoices.find { it.id == consequence.triggeringChoiceId }
                originalChoice?.let {
                    currentTimestamp >= it.timestamp + trigger.ticksDelay
                } ?: false
            }
            
            is ConsequenceTrigger.QuestBased -> {
                // Check if quest is completed
                gameState.completedQuests.contains(trigger.questId)
            }
            
            is ConsequenceTrigger.LocationBased -> {
                // Check if player is at location
                currentLocation == trigger.locationId
            }
            
            is ConsequenceTrigger.NPCBased -> {
                // This would need integration with NPC interaction tracking
                // For now, return false - will be implemented in integration phase
                false
            }
            
            is ConsequenceTrigger.CombinationTrigger -> {
                val results = trigger.conditions.map { subTrigger ->
                    evaluateTrigger(subTrigger, currentTimestamp, currentLocation, gameState, state, consequence)
                }
                
                if (trigger.requireAll) {
                    results.all { it }
                } else {
                    results.any { it }
                }
            }
        }
    }
    
    /**
     * Get all choices made by player in chronological order.
     */
    fun getChoiceHistory(state: ButterflyEffectState): List<PlayerChoice> {
        return state.playerChoices.sortedBy { it.timestamp }
    }
    
    /**
     * Get all choices involving a specific NPC.
     */
    fun getChoicesForNPC(state: ButterflyEffectState, npcId: String): List<PlayerChoice> {
        return state.getChoicesByNPC(npcId).sortedBy { it.timestamp }
    }
    
    /**
     * Get all triggered consequences for a specific choice.
     */
    fun getTriggeredConsequencesForChoice(
        state: ButterflyEffectState,
        choiceId: String
    ): List<Consequence> {
        return state.triggeredConsequences.filter { it.triggeringChoiceId == choiceId }
    }
    
    /**
     * Get summary statistics about player choices.
     */
    fun getChoiceStatistics(state: ButterflyEffectState): ChoiceStatistics {
        return ChoiceStatistics(
            totalChoices = state.playerChoices.size,
            minorChoices = state.getChoiceCountByImpact(ChoiceImpact.MINOR),
            moderateChoices = state.getChoiceCountByImpact(ChoiceImpact.MODERATE),
            majorChoices = state.getChoiceCountByImpact(ChoiceImpact.MAJOR),
            criticalChoices = state.getChoiceCountByImpact(ChoiceImpact.CRITICAL),
            dialogueChoices = state.getChoicesByCategory(ChoiceCategory.DIALOGUE).size,
            questChoices = state.getChoicesByCategory(ChoiceCategory.QUEST).size,
            combatChoices = state.getChoicesByCategory(ChoiceCategory.COMBAT).size,
            moralChoices = state.getChoicesByCategory(ChoiceCategory.MORAL).size,
            pendingConsequences = state.pendingConsequences.size,
            triggeredConsequences = state.triggeredConsequences.size
        )
    }
    
    /**
     * Check if a specific choice has been made.
     */
    fun hasPlayerMadeChoice(state: ButterflyEffectState, choiceKey: String): Boolean {
        return state.hasChoice(choiceKey)
    }
    
    /**
     * Get all pending consequences that will trigger at a specific location.
     */
    fun getPendingConsequencesForLocation(
        state: ButterflyEffectState,
        locationId: String
    ): List<Consequence> {
        return state.pendingConsequences.filter { consequence ->
            when (val trigger = consequence.trigger) {
                is ConsequenceTrigger.LocationBased -> trigger.locationId == locationId
                is ConsequenceTrigger.CombinationTrigger -> {
                    trigger.conditions.any { subTrigger ->
                        subTrigger is ConsequenceTrigger.LocationBased && 
                        subTrigger.locationId == locationId
                    }
                }
                else -> false
            }
        }
    }
    
    /**
     * Get all pending consequences that will trigger for a specific NPC.
     */
    fun getPendingConsequencesForNPC(
        state: ButterflyEffectState,
        npcId: String
    ): List<Consequence> {
        return state.pendingConsequences.filter { consequence ->
            when (val trigger = consequence.trigger) {
                is ConsequenceTrigger.NPCBased -> trigger.npcId == npcId
                is ConsequenceTrigger.CombinationTrigger -> {
                    trigger.conditions.any { subTrigger ->
                        subTrigger is ConsequenceTrigger.NPCBased && 
                        subTrigger.npcId == npcId
                    }
                }
                else -> false
            }
        }
    }
    
    /**
     * Create a new empty butterfly effect state.
     */
    fun createNewState(): ButterflyEffectState {
        return ButterflyEffectState()
    }
}

/**
 * Statistics about player choices.
 */
data class ChoiceStatistics(
    val totalChoices: Int,
    val minorChoices: Int,
    val moderateChoices: Int,
    val majorChoices: Int,
    val criticalChoices: Int,
    val dialogueChoices: Int,
    val questChoices: Int,
    val combatChoices: Int,
    val moralChoices: Int,
    val pendingConsequences: Int,
    val triggeredConsequences: Int
)
