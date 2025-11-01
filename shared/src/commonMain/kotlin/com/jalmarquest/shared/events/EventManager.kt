package com.jalmarquest.shared.events

import com.jalmarquest.shared.model.GameState
import java.util.UUID
import kotlin.random.Random

/**
 * Stateless manager for World Event operations.
 * 
 * The EventManager is responsible for:
 * - Evaluating trigger conditions to determine when events can occur
 * - Triggering events and making them active for player response
 * - Applying event outcomes and distributing rewards/penalties
 * - Managing event cooldowns to prevent spam
 * - Tracking event history and statistics
 * - Integrating with AI Director for dynamic event frequency
 * 
 * Integration Points:
 * - AI Director: Event intensity scales with tension/engagement
 * - WorldUpdateCoordinator: Periodic event evaluation
 * - Butterfly Effect: Major events create long-term consequences
 * - UI: Active events shown in notification system
 * 
 * All operations are pure functions that return updated state.
 */
class EventManager {
    
    /**
     * Trigger a world event and add it to active events.
     * 
     * @param state Current world event state
     * @param eventKey Catalog key for the event
     * @param eventData Event template from catalog
     * @param currentTimestamp Game world time in ticks
     * @param locationId Where event occurs
     * @param outcomes Available outcome paths for this event
     * @param cooldownTicks How long before this event can trigger again
     * @return Updated state with event triggered
     */
    fun triggerEvent(
        state: WorldEventState,
        eventKey: String,
        eventData: EventTemplate,
        currentTimestamp: Long,
        locationId: String,
        outcomes: List<EventOutcome>,
        cooldownTicks: Long = 0
    ): EventTriggerResult {
        // Validate input
        if (eventKey.isBlank() || locationId.isBlank()) {
            return EventTriggerResult.Failure(EventFailure.INVALID_EVENT_DATA)
        }
        
        // Check cooldown
        if (state.isOnCooldown(eventKey, currentTimestamp)) {
            return EventTriggerResult.Failure(EventFailure.EVENT_ON_COOLDOWN)
        }
        
        // Create event
        val eventId = UUID.randomUUID().toString()
        val event = WorldEvent(
            id = eventId,
            eventKey = eventKey,
            type = eventData.type,
            priority = eventData.priority,
            timestamp = currentTimestamp,
            locationId = locationId,
            description = eventData.description,
            metadata = eventData.metadata
        )
        
        // Update state
        val updatedCooldowns = if (cooldownTicks > 0) {
            state.eventCooldowns + (eventKey to (currentTimestamp + cooldownTicks))
        } else {
            state.eventCooldowns
        }
        
        val updatedCounts = state.eventCounts + (eventKey to (state.getEventCount(eventKey) + 1))
        
        val updatedState = state.copy(
            activeEvents = state.activeEvents + event,
            eventCooldowns = updatedCooldowns,
            eventCounts = updatedCounts
        )
        
        return EventTriggerResult.Success(
            updatedState = updatedState,
            event = event,
            availableOutcomes = outcomes
        )
    }
    
    /**
     * Apply an event outcome and move event to completed.
     * 
     * @param state Current world event state
     * @param eventId ID of the active event
     * @param outcome The chosen outcome
     * @return Updated state with outcome applied
     */
    fun applyOutcome(
        state: WorldEventState,
        eventId: String,
        outcome: EventOutcome
    ): EventOutcomeResult {
        // Find active event
        val event = state.activeEvents.find { it.id == eventId }
            ?: return EventOutcomeResult.Failure(EventFailure.NO_ACTIVE_EVENT)
        
        // Update event with chosen outcome
        val completedEvent = event.copy(
            outcomeChosen = outcome.outcomeKey,
            consequencesTriggered = outcome.consequences
        )
        
        // Move to completed
        val updatedState = state.copy(
            activeEvents = state.activeEvents.filter { it.id != eventId },
            completedEvents = state.completedEvents + completedEvent
        )
        
        return EventOutcomeResult.Success(
            updatedState = updatedState,
            rewards = outcome.rewards,
            penalties = outcome.penalties,
            consequenceIds = outcome.consequences,
            followUpEventKeys = outcome.followUpEvents
        )
    }
    
    /**
     * Evaluate if an event's trigger conditions are met.
     * 
     * @param trigger Trigger to evaluate
     * @param gameState Current game state for condition checks
     * @return True if trigger conditions are satisfied
     */
    fun evaluateTrigger(
        trigger: EventTrigger,
        gameState: GameState
    ): Boolean {
        return when (trigger) {
            is EventTrigger.Always -> true
            
            is EventTrigger.RandomChance -> {
                Random.nextDouble() < trigger.probability
            }
            
            is EventTrigger.TimeOfDay -> {
                val currentHour = gameState.worldTime.hour
                if (trigger.startHour <= trigger.endHour) {
                    currentHour in trigger.startHour..trigger.endHour
                } else {
                    // Wraps around midnight (e.g., 22-2)
                    currentHour >= trigger.startHour || currentHour <= trigger.endHour
                }
            }
            
            is EventTrigger.WeatherCondition -> {
                gameState.weather.type.toString() == trigger.weatherType
            }
            
            is EventTrigger.LocationType -> {
                // Would need LocationManager integration to get biome
                // For now, always true - will be implemented in integration phase
                true
            }
            
            is EventTrigger.PlayerState -> {
                val level = gameState.player.level
                val staminaPercent = (gameState.player.stats.currentStamina * 100) / gameState.player.stats.maxStamina
                
                val levelCheck = (trigger.minLevel == null || level >= trigger.minLevel) &&
                                (trigger.maxLevel == null || level <= trigger.maxLevel)
                val staminaCheck = staminaPercent >= trigger.minStamina
                
                levelCheck && staminaCheck
            }
            
            is EventTrigger.DirectorState -> {
                val director = gameState.aiDirector
                director.tension in trigger.minTension..trigger.maxTension &&
                director.engagement >= trigger.minEngagement
            }
            
            is EventTrigger.CombinationTrigger -> {
                val results = trigger.conditions.map { subTrigger ->
                    evaluateTrigger(subTrigger, gameState)
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
     * Cancel an active event without applying outcome.
     * 
     * @param state Current world event state
     * @param eventId ID of event to cancel
     * @return Updated state with event removed
     */
    fun cancelEvent(
        state: WorldEventState,
        eventId: String
    ): WorldEventState {
        return state.copy(
            activeEvents = state.activeEvents.filter { it.id != eventId }
        )
    }
    
    /**
     * Get all active events sorted by priority.
     */
    fun getActiveEventsByPriority(state: WorldEventState): List<WorldEvent> {
        val priorityOrder = mapOf(
            EventPriority.CRITICAL to 0,
            EventPriority.HIGH to 1,
            EventPriority.NORMAL to 2,
            EventPriority.LOW to 3,
            EventPriority.BACKGROUND to 4
        )
        
        return state.activeEvents.sortedBy { priorityOrder[it.priority] }
    }
    
    /**
     * Get event history for a specific location.
     */
    fun getLocationEventHistory(
        state: WorldEventState,
        locationId: String
    ): List<WorldEvent> {
        return state.getEventsByLocation(locationId).sortedByDescending { it.timestamp }
    }
    
    /**
     * Get event statistics.
     */
    fun getEventStatistics(state: WorldEventState): EventStatistics {
        val typeDistribution = state.completedEvents
            .groupBy { it.type }
            .mapValues { it.value.size }
        
        val priorityDistribution = state.completedEvents
            .groupBy { it.priority }
            .mapValues { it.value.size }
        
        return EventStatistics(
            totalEventsTriggered = state.completedEvents.size,
            activeEventsCount = state.activeEvents.size,
            typeDistribution = typeDistribution,
            priorityDistribution = priorityDistribution,
            mostCommonEvent = state.eventCounts.maxByOrNull { it.value }?.key,
            mostCommonEventCount = state.eventCounts.maxByOrNull { it.value }?.value ?: 0
        )
    }
    
    /**
     * Check if any high-priority events are active.
     */
    fun hasUrgentEvents(state: WorldEventState): Boolean {
        return state.activeEvents.any { 
            it.priority == EventPriority.CRITICAL || it.priority == EventPriority.HIGH 
        }
    }
    
    /**
     * Clear all event cooldowns (for testing/debugging).
     */
    fun clearCooldowns(state: WorldEventState): WorldEventState {
        return state.copy(eventCooldowns = emptyMap())
    }
    
    /**
     * Create a new empty world event state.
     */
    fun createNewState(): WorldEventState {
        return WorldEventState()
    }
}

/**
 * Event template for catalog (not serialized in save files).
 */
data class EventTemplate(
    val eventKey: String,
    val type: EventType,
    val priority: EventPriority,
    val description: String,
    val triggers: List<EventTrigger>,
    val outcomes: List<EventOutcome>,
    val cooldownTicks: Long = 0,
    val metadata: Map<String, String> = emptyMap()
)

/**
 * Statistics about world events.
 */
data class EventStatistics(
    val totalEventsTriggered: Int,
    val activeEventsCount: Int,
    val typeDistribution: Map<EventType, Int>,
    val priorityDistribution: Map<EventPriority, Int>,
    val mostCommonEvent: String?,
    val mostCommonEventCount: Int
)
