package com.jalmarquest.shared.events

import kotlinx.serialization.Serializable

/**
 * Dynamic World Events System: AI-driven random encounters and world reactivity.
 * 
 * The World Events system creates a living, breathing world that responds to player actions,
 * time of day, weather conditions, and AI Director state. Events range from minor encounters
 * (finding a lost item) to major world-altering occurrences (merchant caravan arrives).
 * 
 * Core Principles:
 * - **AI-Driven**: AI Director influences event frequency and intensity
 * - **Context-Aware**: Events respect time, weather, location, player state
 * - **Emergent**: Events chain together to create dynamic narratives
 * - **Proportional**: Event scale matches player progression and skill
 * - **Memorable**: Major events have lasting consequences via Butterfly Effect
 * 
 * Example Events:
 * - Weather: Sudden rainstorm forces player to seek shelter
 * - Encounter: Friendly beetle offers to trade rare item
 * - Discovery: Player stumbles upon hidden cache of seeds
 * - Social: Two NPCs arguing, player can mediate
 * - Quest: Injured NPC needs immediate help
 * - Environmental: Fallen log blocks path, requires detour
 * 
 * Architecture:
 * - Stateless functional design (all state in WorldEventState)
 * - Priority-based event triggering (urgent vs background events)
 * - Trigger conditions (time, weather, location, player state, AI Director state)
 * - Multiple outcome paths based on player choice
 * - Integration with Butterfly Effect for consequences
 */

/**
 * World event record tracking what happened and when.
 * 
 * @property id Unique event identifier (generated UUID)
 * @property eventKey Catalog key for this event type
 * @property type Category of event (WEATHER, ENCOUNTER, DISCOVERY, etc.)
 * @property priority How urgent this event is (CRITICAL, HIGH, NORMAL, LOW)
 * @property timestamp When event occurred (game world ticks)
 * @property locationId Where event occurred
 * @property description Human-readable event description
 * @property outcomeChosen Player's choice if event had options
 * @property consequencesTriggered Butterfly Effect consequences created
 * @property metadata Additional event context
 */
@Serializable
data class WorldEvent(
    val id: String,
    val eventKey: String,
    val type: EventType,
    val priority: EventPriority = EventPriority.NORMAL,
    val timestamp: Long,
    val locationId: String,
    val description: String,
    val outcomeChosen: String? = null,
    val consequencesTriggered: List<String> = emptyList(),
    val metadata: Map<String, String> = emptyMap()
) {
    init {
        require(id.isNotBlank()) { "Event ID cannot be blank" }
        require(eventKey.isNotBlank()) { "Event key cannot be blank" }
        require(locationId.isNotBlank()) { "Location ID cannot be blank" }
        require(description.isNotBlank()) { "Description cannot be blank" }
        require(timestamp >= 0) { "Timestamp cannot be negative" }
    }
}

/**
 * Type of world event.
 */
@Serializable
enum class EventType {
    WEATHER,            // Weather changes (rainstorm, fog, wind)
    ENCOUNTER,          // NPC/creature encounters
    DISCOVERY,          // Finding hidden items/locations
    SOCIAL,             // NPC interactions and drama
    QUEST,              // Dynamic quest opportunities
    ENVIRONMENTAL,      // World changes (fallen tree, flooded path)
    DANGER,             // Immediate threats (predator, hazard)
    OPPORTUNITY,        // Time-limited positive events
    LORE,               // Lore fragment discoveries
    MYSTERY             // Strange occurrences requiring investigation
}

/**
 * Event priority for triggering order.
 */
@Serializable
enum class EventPriority {
    CRITICAL,           // Immediate attention required (danger, urgent quest)
    HIGH,               // Important but not urgent (opportunity, social)
    NORMAL,             // Standard events (discovery, encounter)
    LOW,                // Background events (weather, minor environmental)
    BACKGROUND          // Passive world changes (time-based atmosphere)
}

/**
 * Trigger conditions for when an event can occur.
 * 
 * Sealed class hierarchy for type-safe trigger evaluation.
 */
@Serializable
sealed class EventTrigger {
    /**
     * Triggers randomly with probability.
     * @property probability Chance per evaluation (0.0-1.0)
     */
    @Serializable
    data class RandomChance(val probability: Double) : EventTrigger() {
        init {
            require(probability in 0.0..1.0) { "Probability must be 0.0-1.0" }
        }
    }
    
    /**
     * Triggers during specific time periods.
     * @property startHour Hour to begin (0-23)
     * @property endHour Hour to end (0-23)
     */
    @Serializable
    data class TimeOfDay(val startHour: Int, val endHour: Int) : EventTrigger() {
        init {
            require(startHour in 0..23) { "Start hour must be 0-23" }
            require(endHour in 0..23) { "End hour must be 0-23" }
        }
    }
    
    /**
     * Triggers during specific weather.
     * @property weatherType Required weather (e.g., "RAIN", "CLEAR_SKY")
     */
    @Serializable
    data class WeatherCondition(val weatherType: String) : EventTrigger()
    
    /**
     * Triggers at specific location types.
     * @property biomeType Required biome (e.g., "GRASSLAND", "FOREST")
     */
    @Serializable
    data class LocationType(val biomeType: String) : EventTrigger()
    
    /**
     * Triggers when player meets criteria.
     * @property minLevel Minimum player level (null = no requirement)
     * @property maxLevel Maximum player level (null = no requirement)
     * @property minStamina Minimum stamina percentage (0-100)
     */
    @Serializable
    data class PlayerState(
        val minLevel: Int? = null,
        val maxLevel: Int? = null,
        val minStamina: Int = 0
    ) : EventTrigger() {
        init {
            minLevel?.let { require(it > 0) { "Min level must be positive" } }
            maxLevel?.let { require(it > 0) { "Max level must be positive" } }
            require(minStamina in 0..100) { "Min stamina must be 0-100" }
        }
    }
    
    /**
     * Triggers based on AI Director state.
     * @property minTension Minimum tension level (0-100)
     * @property maxTension Maximum tension level (0-100)
     * @property minEngagement Minimum engagement (0-100)
     */
    @Serializable
    data class DirectorState(
        val minTension: Int = 0,
        val maxTension: Int = 100,
        val minEngagement: Int = 0
    ) : EventTrigger() {
        init {
            require(minTension in 0..100) { "Min tension must be 0-100" }
            require(maxTension in 0..100) { "Max tension must be 0-100" }
            require(minEngagement in 0..100) { "Min engagement must be 0-100" }
        }
    }
    
    /**
     * Triggers when multiple conditions are met.
     * @property conditions All conditions to check
     * @property requireAll If true, all must be met; if false, any one suffices
     */
    @Serializable
    data class CombinationTrigger(
        val conditions: List<EventTrigger>,
        val requireAll: Boolean = true
    ) : EventTrigger()
    
    /**
     * Always triggers when evaluated.
     */
    @Serializable
    object Always : EventTrigger()
}

/**
 * Possible outcomes when an event occurs.
 * 
 * @property id Unique outcome identifier
 * @property outcomeKey Identifier for this outcome path
 * @property description What happens if this outcome is chosen
 * @property choiceText Text shown to player (null = automatic outcome)
 * @property rewards Items, currency, XP gained
 * @property penalties Items lost, damage taken, stamina cost
 * @property consequences Butterfly Effect consequences triggered
 * @property followUpEvents Events that trigger after this outcome
 */
@Serializable
data class EventOutcome(
    val id: String,
    val outcomeKey: String,
    val description: String,
    val choiceText: String? = null,
    val rewards: Map<String, Int> = emptyMap(),      // "seeds" → 50, "xp" → 100
    val penalties: Map<String, Int> = emptyMap(),     // "stamina" → -20, "health" → -10
    val consequences: List<String> = emptyList(),     // Butterfly Effect consequence IDs
    val followUpEvents: List<String> = emptyList()    // Event keys to trigger next
) {
    init {
        require(id.isNotBlank()) { "Outcome ID cannot be blank" }
        require(outcomeKey.isNotBlank()) { "Outcome key cannot be blank" }
        require(description.isNotBlank()) { "Description cannot be blank" }
    }
}

/**
 * World event state tracking active and historical events.
 * 
 * @property activeEvents Events currently happening (require player response)
 * @property completedEvents Historical record of all events
 * @property eventCooldowns Cooldown timers to prevent event spam
 * @property eventCounts Tracking how many times each event has occurred
 */
@Serializable
data class WorldEventState(
    val activeEvents: List<WorldEvent> = emptyList(),
    val completedEvents: List<WorldEvent> = emptyList(),
    val eventCooldowns: Map<String, Long> = emptyMap(),  // eventKey → timestamp when available again
    val eventCounts: Map<String, Int> = emptyMap()       // eventKey → occurrence count
) {
    /**
     * Check if an event is on cooldown.
     */
    fun isOnCooldown(eventKey: String, currentTimestamp: Long): Boolean {
        val cooldownEnds = eventCooldowns[eventKey] ?: return false
        return currentTimestamp < cooldownEnds
    }
    
    /**
     * Get count of how many times an event has occurred.
     */
    fun getEventCount(eventKey: String): Int {
        return eventCounts[eventKey] ?: 0
    }
    
    /**
     * Get all events of a specific type.
     */
    fun getEventsByType(type: EventType): List<WorldEvent> {
        return completedEvents.filter { it.type == type }
    }
    
    /**
     * Get events that occurred at a specific location.
     */
    fun getEventsByLocation(locationId: String): List<WorldEvent> {
        return completedEvents.filter { it.locationId == locationId }
    }
}

/**
 * Result of triggering an event.
 */
@Serializable
sealed class EventTriggerResult {
    @Serializable
    data class Success(
        val updatedState: WorldEventState,
        val event: WorldEvent,
        val availableOutcomes: List<EventOutcome>
    ) : EventTriggerResult()
    
    @Serializable
    data class Failure(val reason: EventFailure) : EventTriggerResult()
}

/**
 * Result of applying an event outcome.
 */
@Serializable
sealed class EventOutcomeResult {
    @Serializable
    data class Success(
        val updatedState: WorldEventState,
        val rewards: Map<String, Int>,
        val penalties: Map<String, Int>,
        val consequenceIds: List<String>,
        val followUpEventKeys: List<String>
    ) : EventOutcomeResult()
    
    @Serializable
    data class Failure(val reason: EventFailure) : EventOutcomeResult()
}

/**
 * Failure reasons for event operations.
 */
@Serializable
enum class EventFailure {
    INVALID_EVENT_DATA,         // Missing required fields
    EVENT_ON_COOLDOWN,          // Event cannot trigger yet
    TRIGGER_CONDITION_NOT_MET,  // Conditions not satisfied
    EVENT_NOT_FOUND,            // Referenced event doesn't exist
    OUTCOME_NOT_FOUND,          // Referenced outcome doesn't exist
    NO_ACTIVE_EVENT             // No event to apply outcome to
}
