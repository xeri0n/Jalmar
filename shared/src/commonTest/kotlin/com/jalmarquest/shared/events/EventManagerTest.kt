package com.jalmarquest.shared.events

import com.jalmarquest.shared.ai.AIDirector
import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.model.PlayerStats
import com.jalmarquest.shared.model.Position
import com.jalmarquest.shared.model.WorldTime
import com.jalmarquest.shared.weather.Weather
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Comprehensive tests for Dynamic World Events system.
 * 
 * Test Coverage:
 * - Event triggering (success, cooldown, validation)
 * - Trigger evaluation (time, weather, player state, AI Director, combination)
 * - Outcome application (rewards, penalties, consequences, follow-up events)
 * - Event management (priority sorting, cancellation, history)
 * - Edge cases (invalid data, missing events, cooldown edge cases)
 */
class EventManagerTest {
    
    private val manager = EventManager()
    
    // ========================================
    // EVENT TRIGGERING TESTS
    // ========================================
    
    @Test
    fun `triggerEvent should create active event and set cooldown`() {
        val state = WorldEventState()
        val template = createTestEventTemplate()
        
        val result = manager.triggerEvent(
            state = state,
            eventKey = "test_event",
            eventData = template,
            currentTimestamp = 1000L,
            locationId = "test_location",
            outcomes = template.outcomes,
            cooldownTicks = 5000
        )
        
        assertIs<EventTriggerResult.Success>(result)
        val success = result as EventTriggerResult.Success
        assertEquals(1, success.updatedState.activeEvents.size)
        assertEquals("test_event", success.updatedState.activeEvents.first().eventKey)
        assertTrue(success.updatedState.isOnCooldown("test_event", 3000)) // Before cooldown ends
        assertFalse(success.updatedState.isOnCooldown("test_event", 7000)) // After cooldown ends
    }
    
    @Test
    fun `triggerEvent should reject events on cooldown`() {
        val state = WorldEventState(
            eventCooldowns = mapOf("test_event" to 5000L)
        )
        val template = createTestEventTemplate()
        
        val result = manager.triggerEvent(
            state = state,
            eventKey = "test_event",
            eventData = template,
            currentTimestamp = 3000L, // Before cooldown ends
            locationId = "test_location",
            outcomes = template.outcomes
        )
        
        assertIs<EventTriggerResult.Failure>(result)
        assertEquals(EventFailure.EVENT_ON_COOLDOWN, (result as EventTriggerResult.Failure).reason)
    }
    
    @Test
    fun `triggerEvent should reject invalid data`() {
        val state = WorldEventState()
        val template = createTestEventTemplate()
        
        val result = manager.triggerEvent(
            state = state,
            eventKey = "", // Invalid: blank key
            eventData = template,
            currentTimestamp = 1000L,
            locationId = "test_location",
            outcomes = template.outcomes
        )
        
        assertIs<EventTriggerResult.Failure>(result)
        assertEquals(EventFailure.INVALID_EVENT_DATA, (result as EventTriggerResult.Failure).reason)
    }
    
    @Test
    fun `triggerEvent should increment event count`() {
        val state = WorldEventState()
        val template = createTestEventTemplate()
        
        val result1 = manager.triggerEvent(
            state = state,
            eventKey = "test_event",
            eventData = template,
            currentTimestamp = 1000L,
            locationId = "test_location",
            outcomes = template.outcomes
        )
        
        assertIs<EventTriggerResult.Success>(result1)
        val state2 = (result1 as EventTriggerResult.Success).updatedState
        assertEquals(1, state2.getEventCount("test_event"))
    }
    
    // ========================================
    // OUTCOME APPLICATION TESTS
    // ========================================
    
    @Test
    fun `applyOutcome should move event to completed and return rewards`() {
        val event = createTestWorldEvent()
        val state = WorldEventState(
            activeEvents = listOf(event)
        )
        val outcome = createTestOutcome(
            rewards = mapOf("seeds" to 50, "xp" to 100),
            penalties = mapOf("stamina" to 20)
        )
        
        val result = manager.applyOutcome(state, event.id, outcome)
        
        assertIs<EventOutcomeResult.Success>(result)
        val success = result as EventOutcomeResult.Success
        assertEquals(0, success.updatedState.activeEvents.size)
        assertEquals(1, success.updatedState.completedEvents.size)
        assertEquals(50, success.rewards["seeds"])
        assertEquals(100, success.rewards["xp"])
        assertEquals(20, success.penalties["stamina"])
    }
    
    @Test
    fun `applyOutcome should fail if event not active`() {
        val state = WorldEventState()
        val outcome = createTestOutcome()
        
        val result = manager.applyOutcome(state, "nonexistent_id", outcome)
        
        assertIs<EventOutcomeResult.Failure>(result)
        assertEquals(EventFailure.NO_ACTIVE_EVENT, (result as EventOutcomeResult.Failure).reason)
    }
    
    @Test
    fun `applyOutcome should track chosen outcome in completed event`() {
        val event = createTestWorldEvent()
        val state = WorldEventState(activeEvents = listOf(event))
        val outcome = createTestOutcome(outcomeKey = "help_npc")
        
        val result = manager.applyOutcome(state, event.id, outcome)
        
        assertIs<EventOutcomeResult.Success>(result)
        val completedEvent = (result as EventOutcomeResult.Success).updatedState.completedEvents.first()
        assertEquals("help_npc", completedEvent.outcomeChosen)
    }
    
    @Test
    fun `applyOutcome should return consequences and follow-up events`() {
        val event = createTestWorldEvent()
        val state = WorldEventState(activeEvents = listOf(event))
        val outcome = createTestOutcome(
            consequences = listOf("consequence1", "consequence2"),
            followUpEvents = listOf("event_chain_next")
        )
        
        val result = manager.applyOutcome(state, event.id, outcome)
        
        assertIs<EventOutcomeResult.Success>(result)
        val success = result as EventOutcomeResult.Success
        assertEquals(2, success.consequenceIds.size)
        assertEquals(1, success.followUpEventKeys.size)
    }
    
    // ========================================
    // TRIGGER EVALUATION TESTS
    // ========================================
    
    @Test
    fun `evaluateTrigger should return true for Always trigger`() {
        val trigger = EventTrigger.Always
        val gameState = createTestGameState()
        
        val result = manager.evaluateTrigger(trigger, gameState)
        
        assertTrue(result)
    }
    
    @Test
    fun `evaluateTrigger should evaluate TimeOfDay correctly`() {
        val trigger = EventTrigger.TimeOfDay(startHour = 10, endHour = 14)
        val gameState1 = createTestGameState(hour = 12) // Within range
        val gameState2 = createTestGameState(hour = 8)  // Outside range
        
        assertTrue(manager.evaluateTrigger(trigger, gameState1))
        assertFalse(manager.evaluateTrigger(trigger, gameState2))
    }
    
    @Test
    fun `evaluateTrigger should handle TimeOfDay wraparound`() {
        val trigger = EventTrigger.TimeOfDay(startHour = 22, endHour = 2) // Night spanning midnight
        val gameState1 = createTestGameState(hour = 23) // Within range
        val gameState2 = createTestGameState(hour = 1)  // Within range (after midnight)
        val gameState3 = createTestGameState(hour = 12) // Outside range
        
        assertTrue(manager.evaluateTrigger(trigger, gameState1))
        assertTrue(manager.evaluateTrigger(trigger, gameState2))
        assertFalse(manager.evaluateTrigger(trigger, gameState3))
    }
    
    @Test
    fun `evaluateTrigger should evaluate WeatherCondition correctly`() {
        val trigger = EventTrigger.WeatherCondition(weatherType = "RAIN")
        val gameState1 = createTestGameState(weather = Weather.RAIN_SHOWER)
        val gameState2 = createTestGameState(weather = Weather.CLEAR_SKY)
        
        assertTrue(manager.evaluateTrigger(trigger, gameState1))
        assertFalse(manager.evaluateTrigger(trigger, gameState2))
    }
    
    @Test
    fun `evaluateTrigger should evaluate PlayerState correctly`() {
        val trigger = EventTrigger.PlayerState(minLevel = 5, maxLevel = 10, minStamina = 50)
        val gameState1 = createTestGameState(level = 7, stamina = 60) // Meets all conditions
        val gameState2 = createTestGameState(level = 3, stamina = 60) // Level too low
        val gameState3 = createTestGameState(level = 7, stamina = 30) // Stamina too low
        
        assertTrue(manager.evaluateTrigger(trigger, gameState1))
        assertFalse(manager.evaluateTrigger(trigger, gameState2))
        assertFalse(manager.evaluateTrigger(trigger, gameState3))
    }
    
    @Test
    fun `evaluateTrigger should evaluate DirectorState correctly`() {
        val trigger = EventTrigger.DirectorState(minTension = 40, maxTension = 80, minEngagement = 50)
        val gameState1 = createTestGameState(tension = 60, engagement = 70) // Meets all conditions
        val gameState2 = createTestGameState(tension = 30, engagement = 70) // Tension too low
        val gameState3 = createTestGameState(tension = 60, engagement = 40) // Engagement too low
        
        assertTrue(manager.evaluateTrigger(trigger, gameState1))
        assertFalse(manager.evaluateTrigger(trigger, gameState2))
        assertFalse(manager.evaluateTrigger(trigger, gameState3))
    }
    
    @Test
    fun `evaluateTrigger should evaluate CombinationTrigger with requireAll=true`() {
        val trigger = EventTrigger.CombinationTrigger(
            conditions = listOf(
                EventTrigger.TimeOfDay(startHour = 10, endHour = 14),
                EventTrigger.PlayerState(minLevel = 5)
            ),
            requireAll = true
        )
        val gameState1 = createTestGameState(hour = 12, level = 7) // Both met
        val gameState2 = createTestGameState(hour = 8, level = 7)  // Time not met
        
        assertTrue(manager.evaluateTrigger(trigger, gameState1))
        assertFalse(manager.evaluateTrigger(trigger, gameState2))
    }
    
    @Test
    fun `evaluateTrigger should evaluate CombinationTrigger with requireAll=false`() {
        val trigger = EventTrigger.CombinationTrigger(
            conditions = listOf(
                EventTrigger.TimeOfDay(startHour = 10, endHour = 14),
                EventTrigger.PlayerState(minLevel = 5)
            ),
            requireAll = false // OR logic
        )
        val gameState1 = createTestGameState(hour = 12, level = 3) // Only time met
        val gameState2 = createTestGameState(hour = 8, level = 7)  // Only level met
        val gameState3 = createTestGameState(hour = 8, level = 3)  // Neither met
        
        assertTrue(manager.evaluateTrigger(trigger, gameState1))
        assertTrue(manager.evaluateTrigger(trigger, gameState2))
        assertFalse(manager.evaluateTrigger(trigger, gameState3))
    }
    
    // ========================================
    // EVENT MANAGEMENT TESTS
    // ========================================
    
    @Test
    fun `getActiveEventsByPriority should sort by priority correctly`() {
        val state = WorldEventState(
            activeEvents = listOf(
                createTestWorldEvent(priority = EventPriority.NORMAL),
                createTestWorldEvent(priority = EventPriority.CRITICAL),
                createTestWorldEvent(priority = EventPriority.LOW)
            )
        )
        
        val sorted = manager.getActiveEventsByPriority(state)
        
        assertEquals(EventPriority.CRITICAL, sorted[0].priority)
        assertEquals(EventPriority.NORMAL, sorted[1].priority)
        assertEquals(EventPriority.LOW, sorted[2].priority)
    }
    
    @Test
    fun `cancelEvent should remove active event`() {
        val event = createTestWorldEvent()
        val state = WorldEventState(activeEvents = listOf(event))
        
        val updatedState = manager.cancelEvent(state, event.id)
        
        assertEquals(0, updatedState.activeEvents.size)
    }
    
    @Test
    fun `getLocationEventHistory should filter by location`() {
        val state = WorldEventState(
            completedEvents = listOf(
                createTestWorldEvent(locationId = "village", timestamp = 1000),
                createTestWorldEvent(locationId = "forest", timestamp = 2000),
                createTestWorldEvent(locationId = "village", timestamp = 3000)
            )
        )
        
        val villageEvents = manager.getLocationEventHistory(state, "village")
        
        assertEquals(2, villageEvents.size)
        assertEquals(3000L, villageEvents[0].timestamp) // Sorted descending
    }
    
    @Test
    fun `hasUrgentEvents should detect high-priority events`() {
        val state1 = WorldEventState(
            activeEvents = listOf(createTestWorldEvent(priority = EventPriority.CRITICAL))
        )
        val state2 = WorldEventState(
            activeEvents = listOf(createTestWorldEvent(priority = EventPriority.LOW))
        )
        
        assertTrue(manager.hasUrgentEvents(state1))
        assertFalse(manager.hasUrgentEvents(state2))
    }
    
    @Test
    fun `getEventStatistics should return accurate statistics`() {
        val state = WorldEventState(
            completedEvents = listOf(
                createTestWorldEvent(type = EventType.ENCOUNTER),
                createTestWorldEvent(type = EventType.DISCOVERY),
                createTestWorldEvent(type = EventType.ENCOUNTER)
            ),
            activeEvents = listOf(
                createTestWorldEvent()
            ),
            eventCounts = mapOf(
                "event1" to 5,
                "event2" to 3
            )
        )
        
        val stats = manager.getEventStatistics(state)
        
        assertEquals(3, stats.totalEventsTriggered)
        assertEquals(1, stats.activeEventsCount)
        assertEquals(2, stats.typeDistribution[EventType.ENCOUNTER])
        assertEquals(1, stats.typeDistribution[EventType.DISCOVERY])
        assertEquals("event1", stats.mostCommonEvent)
        assertEquals(5, stats.mostCommonEventCount)
    }
    
    // ========================================
    // EVENT CATALOG TESTS
    // ========================================
    
    @Test
    fun `EventCatalog should provide rainstorm event`() {
        val template = EventCatalog.createSuddenRainstorm()
        
        assertEquals("weather_sudden_rainstorm", template.eventKey)
        assertEquals(EventType.WEATHER, template.type)
        assertEquals(3, template.outcomes.size)
        assertTrue(template.outcomes.any { it.outcomeKey == "seek_shelter" })
    }
    
    @Test
    fun `EventCatalog should provide ladybug trader event`() {
        val template = EventCatalog.createLadybugTrader()
        
        assertEquals("encounter_ladybug_trader", template.eventKey)
        assertEquals(EventType.ENCOUNTER, template.type)
        assertTrue(template.outcomes.any { it.outcomeKey == "accept_trade" })
        assertTrue(template.outcomes.any { it.outcomeKey == "steal" })
    }
    
    @Test
    fun `EventCatalog getAllEventTemplates should return 10 events`() {
        val templates = EventCatalog.getAllEventTemplates()
        
        assertEquals(10, templates.size)
        assertTrue(templates.containsKey("weather_sudden_rainstorm"))
        assertTrue(templates.containsKey("mystery_glowing_mushrooms"))
    }
    
    @Test
    fun `EventCatalog getEventsByType should filter correctly`() {
        val weatherEvents = EventCatalog.getEventsByType(EventType.WEATHER)
        val encounterEvents = EventCatalog.getEventsByType(EventType.ENCOUNTER)
        val dangerEvents = EventCatalog.getEventsByType(EventType.DANGER)
        
        assertEquals(1, weatherEvents.size)
        assertEquals(1, encounterEvents.size)
        assertEquals(1, dangerEvents.size)
    }
    
    // ========================================
    // HELPER FUNCTIONS
    // ========================================
    
    private fun createTestGameState(
        hour: Int = 12,
        level: Int = 5,
        stamina: Int = 100,
        weather: Weather = Weather.CLEAR_SKY,
        tension: Int = 50,
        engagement: Int = 60
    ): GameState {
        return GameState(
            version = 1,
            player = Player(
                id = "test_player",
                name = "Test Hero",
                level = level,
                stats = PlayerStats(currentStamina = stamina, maxStamina = 100),
                position = Position(x = 0, y = 0, locationId = "test_location")
            ),
            worldTime = WorldTime(totalTicks = 1000, hour = hour),
            weather = weather,
            aiDirector = AIDirector(tension = tension, engagement = engagement)
        )
    }
    
    private fun createTestEventTemplate(): EventTemplate {
        return EventTemplate(
            eventKey = "test_event",
            type = EventType.DISCOVERY,
            priority = EventPriority.NORMAL,
            description = "Test event description",
            triggers = listOf(EventTrigger.Always),
            outcomes = listOf(createTestOutcome()),
            cooldownTicks = 1000
        )
    }
    
    private fun createTestWorldEvent(
        eventKey: String = "test_event",
        type: EventType = EventType.DISCOVERY,
        priority: EventPriority = EventPriority.NORMAL,
        locationId: String = "test_location",
        timestamp: Long = 1000L
    ): WorldEvent {
        return WorldEvent(
            id = UUID.randomUUID().toString(),
            eventKey = eventKey,
            type = type,
            priority = priority,
            timestamp = timestamp,
            locationId = locationId,
            description = "Test event"
        )
    }
    
    private fun createTestOutcome(
        outcomeKey: String = "test_outcome",
        rewards: Map<String, Int> = emptyMap(),
        penalties: Map<String, Int> = emptyMap(),
        consequences: List<String> = emptyList(),
        followUpEvents: List<String> = emptyList()
    ): EventOutcome {
        return EventOutcome(
            id = UUID.randomUUID().toString(),
            outcomeKey = outcomeKey,
            description = "Test outcome",
            choiceText = "Test choice",
            rewards = rewards,
            penalties = penalties,
            consequences = consequences,
            followUpEvents = followUpEvents
        )
    }
}
