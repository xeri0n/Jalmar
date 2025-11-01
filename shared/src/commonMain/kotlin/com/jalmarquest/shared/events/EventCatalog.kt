package com.jalmarquest.shared.events

import java.util.UUID

/**
 * Catalog of predefined world events for dynamic gameplay.
 * 
 * This catalog defines the living, breathing world of JalmarQuest. Events range from minor
 * atmospheric moments to major game-changing occurrences. Each event is context-aware,
 * respecting time, weather, location, and player state.
 * 
 * Design Philosophy:
 * - **Variety**: Mix of combat, social, discovery, and environmental events
 * - **Quail-Scale**: All events fit the "tiny hero, big world" theme
 * - **Meaningful**: Events have consequences, not just flavor text
 * - **Emergent**: Events chain together to create dynamic narratives
 * - **AI-Driven**: Frequency and intensity scale with AI Director state
 * 
 * Integration:
 * - EventManager uses these templates to trigger events
 * - AI Director influences event selection based on tension/engagement
 * - Butterfly Effect tracks consequences of player choices in events
 */
object EventCatalog {
    
    // ========================================
    // WEATHER EVENTS
    // ========================================
    
    /**
     * Sudden rainstorm forces player to seek shelter.
     * 
     * Triggers: During daytime, random chance
     * Outcomes: Seek shelter (safe), Continue through rain (stamina cost), Wait it out (time cost)
     */
    fun createSuddenRainstorm(): EventTemplate {
        return EventTemplate(
            eventKey = "weather_sudden_rainstorm",
            type = EventType.WEATHER,
            priority = EventPriority.NORMAL,
            description = "Dark clouds gather overhead. A sudden rainstorm begins, heavy droplets the size of your head pounding the ground. You need to find shelter or push through!",
            triggers = listOf(
                EventTrigger.CombinationTrigger(
                    conditions = listOf(
                        EventTrigger.TimeOfDay(startHour = 6, endHour = 18),
                        EventTrigger.RandomChance(probability = 0.15)
                    ),
                    requireAll = true
                )
            ),
            outcomes = listOf(
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "seek_shelter",
                    description = "You scurry under a large leaf, staying dry and safe.",
                    choiceText = "Seek Shelter",
                    rewards = mapOf("safety" to 1),
                    penalties = emptyMap()
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "push_through",
                    description = "You bravely push through the rain, soaked but determined.",
                    choiceText = "Continue Through Rain",
                    rewards = mapOf("courage" to 1),
                    penalties = mapOf("stamina" to 15)
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "wait_out",
                    description = "You wait for the storm to pass. Eventually, the sun peeks through.",
                    choiceText = "Wait It Out",
                    rewards = emptyMap(),
                    penalties = mapOf("time" to 30) // 30 minutes
                )
            ),
            cooldownTicks = 7200 * 60, // 5 days
            metadata = mapOf("weather_change" to "RAIN")
        )
    }
    
    // ========================================
    // ENCOUNTER EVENTS
    // ========================================
    
    /**
     * Friendly ladybug offers to trade a shiny pebble for seeds.
     * 
     * Triggers: Daytime, grassland, random chance
     * Outcomes: Accept trade, Decline politely, Steal pebble
     */
    fun createLadybugTrader(): EventTemplate {
        return EventTemplate(
            eventKey = "encounter_ladybug_trader",
            type = EventType.ENCOUNTER,
            priority = EventPriority.NORMAL,
            description = "A cheerful ladybug approaches, antennae wiggling. 'Trade you this shiny pebble for 25 seeds?' she chirps hopefully.",
            triggers = listOf(
                EventTrigger.CombinationTrigger(
                    conditions = listOf(
                        EventTrigger.TimeOfDay(startHour = 8, endHour = 16),
                        EventTrigger.RandomChance(probability = 0.10)
                    ),
                    requireAll = true
                )
            ),
            outcomes = listOf(
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "accept_trade",
                    description = "You hand over 25 seeds. The ladybug beams and gives you the pebble.",
                    choiceText = "Accept Trade (25 seeds)",
                    rewards = mapOf("shiny_pebble" to 1),
                    penalties = mapOf("seeds" to 25)
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "decline",
                    description = "You politely decline. The ladybug nods understandingly and wanders off.",
                    choiceText = "Decline Politely",
                    rewards = emptyMap(),
                    penalties = emptyMap()
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "steal",
                    description = "You snatch the pebble and run! The ladybug shouts after you angrily.",
                    choiceText = "Steal Pebble",
                    rewards = mapOf("shiny_pebble" to 1),
                    penalties = emptyMap(),
                    consequences = listOf("consequence_ladybug_theft") // Butterfly Effect
                )
            ),
            cooldownTicks = 4320 * 60 // 3 days
        )
    }
    
    /**
     * Aggressive ant challenges player to combat.
     * 
     * Triggers: High AI tension, random chance
     * Outcomes: Fight, Flee, Intimidate
     */
    fun createAntAggressor(): EventTemplate {
        return EventTemplate(
            eventKey = "encounter_ant_aggressor",
            type = EventType.DANGER,
            priority = EventPriority.HIGH,
            description = "A massive ant blocks your path, mandibles clacking menacingly. It clearly wants a fight!",
            triggers = listOf(
                EventTrigger.CombinationTrigger(
                    conditions = listOf(
                        EventTrigger.DirectorState(minTension = 40, maxTension = 100),
                        EventTrigger.RandomChance(probability = 0.20)
                    ),
                    requireAll = true
                )
            ),
            outcomes = listOf(
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "fight",
                    description = "You engage the ant in combat!",
                    choiceText = "Fight",
                    rewards = mapOf("xp" to 50, "courage" to 2),
                    penalties = mapOf("health" to 20),
                    followUpEvents = listOf("combat_ant_battle")
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "flee",
                    description = "You turn and run as fast as your tiny legs can carry you!",
                    choiceText = "Flee",
                    rewards = mapOf("safety" to 1),
                    penalties = mapOf("stamina" to 30)
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "intimidate",
                    description = "You puff up your feathers and chirp loudly. The ant hesitates, then backs off!",
                    choiceText = "Intimidate",
                    rewards = mapOf("xp" to 25, "confidence" to 1),
                    penalties = emptyMap()
                )
            ),
            cooldownTicks = 2880 * 60 // 2 days
        )
    }
    
    // ========================================
    // DISCOVERY EVENTS
    // ========================================
    
    /**
     * Player finds a hidden cache of seeds.
     * 
     * Triggers: Random chance, low engagement
     * Outcomes: Take all, Take half (karma), Leave it
     */
    fun createSeedCacheDiscovery(): EventTemplate {
        return EventTemplate(
            eventKey = "discovery_seed_cache",
            type = EventType.DISCOVERY,
            priority = EventPriority.NORMAL,
            description = "Half-buried beneath a fallen twig, you spot a small cache of seeds. Someone's lost stash!",
            triggers = listOf(
                EventTrigger.CombinationTrigger(
                    conditions = listOf(
                        EventTrigger.DirectorState(minEngagement = 0, maxTension = 60),
                        EventTrigger.RandomChance(probability = 0.08)
                    ),
                    requireAll = true
                )
            ),
            outcomes = listOf(
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "take_all",
                    description = "You scoop up all 50 seeds. Finders keepers!",
                    choiceText = "Take All",
                    rewards = mapOf("seeds" to 50),
                    penalties = emptyMap(),
                    consequences = listOf("consequence_greedy_choice")
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "take_half",
                    description = "You take 25 seeds and leave the rest. Someone might need them.",
                    choiceText = "Take Half (Good Karma)",
                    rewards = mapOf("seeds" to 25, "karma" to 5),
                    penalties = emptyMap()
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "leave_it",
                    description = "You leave the cache undisturbed. Good deeds create good fortune.",
                    choiceText = "Leave It",
                    rewards = mapOf("karma" to 10),
                    penalties = emptyMap(),
                    consequences = listOf("consequence_generous_choice")
                )
            ),
            cooldownTicks = 5760 * 60 // 4 days
        )
    }
    
    /**
     * Ancient lore fragment found carved on a pebble.
     * 
     * Triggers: High engagement, rare chance
     * Outcomes: Study it (lore unlock), Ignore it
     */
    fun createLoreFragmentDiscovery(): EventTemplate {
        return EventTemplate(
            eventKey = "discovery_lore_fragment",
            type = EventType.LORE,
            priority = EventPriority.NORMAL,
            description = "You discover strange markings on a smooth pebble. Ancient writings from the Before-Times!",
            triggers = listOf(
                EventTrigger.CombinationTrigger(
                    conditions = listOf(
                        EventTrigger.DirectorState(minEngagement = 60),
                        EventTrigger.RandomChance(probability = 0.05)
                    ),
                    requireAll = true
                )
            ),
            outcomes = listOf(
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "study_fragment",
                    description = "You study the markings carefully. New lore fragment unlocked!",
                    choiceText = "Study the Fragment",
                    rewards = mapOf("lore_fragment" to 1, "xp" to 30),
                    penalties = emptyMap()
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "ignore",
                    description = "Interesting, but you have more pressing matters. You move on.",
                    choiceText = "Ignore It",
                    rewards = emptyMap(),
                    penalties = emptyMap()
                )
            ),
            cooldownTicks = 10080 * 60 // 7 days
        )
    }
    
    // ========================================
    // SOCIAL EVENTS
    // ========================================
    
    /**
     * Two snails arguing over territory.
     * 
     * Triggers: Daytime, random chance
     * Outcomes: Mediate peacefully, Side with one, Walk away
     */
    fun createSnailDispute(): EventTemplate {
        return EventTemplate(
            eventKey = "social_snail_dispute",
            type = EventType.SOCIAL,
            priority = EventPriority.NORMAL,
            description = "Two garden snails are having a heated dispute over a choice piece of moss. Both look to you for judgment!",
            triggers = listOf(
                EventTrigger.CombinationTrigger(
                    conditions = listOf(
                        EventTrigger.TimeOfDay(startHour = 10, endHour = 16),
                        EventTrigger.RandomChance(probability = 0.12)
                    ),
                    requireAll = true
                )
            ),
            outcomes = listOf(
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "mediate",
                    description = "You suggest splitting the moss fairly. Both snails agree, grateful for the wisdom.",
                    choiceText = "Mediate Peacefully",
                    rewards = mapOf("reputation" to 5, "karma" to 3),
                    penalties = emptyMap(),
                    consequences = listOf("consequence_snail_gratitude")
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "side_first",
                    description = "You side with the first snail. The second snail slithers away, dejected.",
                    choiceText = "Side with First Snail",
                    rewards = mapOf("snail_friendship" to 1),
                    penalties = emptyMap()
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "walk_away",
                    description = "Not your problem. You leave them to sort it out.",
                    choiceText = "Walk Away",
                    rewards = emptyMap(),
                    penalties = emptyMap()
                )
            ),
            cooldownTicks = 4320 * 60 // 3 days
        )
    }
    
    /**
     * Injured moth needs help.
     * 
     * Triggers: Night time, player has high stamina
     * Outcomes: Help (stamina cost), Leave it, Mercy kill
     */
    fun createInjuredMoth(): EventTemplate {
        return EventTemplate(
            eventKey = "social_injured_moth",
            type = EventType.QUEST,
            priority = EventPriority.HIGH,
            description = "A beautiful moth lies on the ground, wing torn. It chirps weakly, clearly in pain. Can you help?",
            triggers = listOf(
                EventTrigger.CombinationTrigger(
                    conditions = listOf(
                        EventTrigger.TimeOfDay(startHour = 19, endHour = 5),
                        EventTrigger.PlayerState(minStamina = 50),
                        EventTrigger.RandomChance(probability = 0.10)
                    ),
                    requireAll = true
                )
            ),
            outcomes = listOf(
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "help",
                    description = "You spend time carefully mending the moth's wing with spider silk. It recovers and flies away gratefully.",
                    choiceText = "Help the Moth",
                    rewards = mapOf("karma" to 10, "xp" to 40),
                    penalties = mapOf("stamina" to 20, "time" to 15),
                    consequences = listOf("consequence_moth_gratitude")
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "leave",
                    description = "You can't spare the time. You leave the moth to its fate.",
                    choiceText = "Leave It",
                    rewards = emptyMap(),
                    penalties = emptyMap()
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "mercy_kill",
                    description = "To end its suffering, you deliver a quick mercy. The moth passes peacefully.",
                    choiceText = "Mercy Kill",
                    rewards = mapOf("xp" to 10),
                    penalties = emptyMap(),
                    consequences = listOf("consequence_dark_mercy")
                )
            ),
            cooldownTicks = 5760 * 60 // 4 days
        )
    }
    
    // ========================================
    // OPPORTUNITY EVENTS
    // ========================================
    
    /**
     * Merchant beetle passes by with rare items.
     * 
     * Triggers: Daytime, high engagement
     * Outcomes: Browse wares, Move along
     */
    fun createMerchantBeetle(): EventTemplate {
        return EventTemplate(
            eventKey = "opportunity_merchant_beetle",
            type = EventType.OPPORTUNITY,
            priority = EventPriority.HIGH,
            description = "A traveling merchant beetle trundles by, shell loaded with goods. 'Special prices today only!' it calls.",
            triggers = listOf(
                EventTrigger.CombinationTrigger(
                    conditions = listOf(
                        EventTrigger.TimeOfDay(startHour = 9, endHour = 17),
                        EventTrigger.DirectorState(minEngagement = 50),
                        EventTrigger.RandomChance(probability = 0.07)
                    ),
                    requireAll = true
                )
            ),
            outcomes = listOf(
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "browse",
                    description = "You approach the merchant. Time to see what's for sale!",
                    choiceText = "Browse Wares",
                    rewards = emptyMap(),
                    penalties = emptyMap(),
                    followUpEvents = listOf("shop_merchant_beetle")
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "move_on",
                    description = "You nod politely but continue on your way.",
                    choiceText = "Move Along",
                    rewards = emptyMap(),
                    penalties = emptyMap()
                )
            ),
            cooldownTicks = 7200 * 60 // 5 days
        )
    }
    
    // ========================================
    // ENVIRONMENTAL EVENTS
    // ========================================
    
    /**
     * Fallen log blocks the path.
     * 
     * Triggers: Random chance
     * Outcomes: Climb over (stamina), Find detour (time), Clear path (time + stamina)
     */
    fun createFallenLog(): EventTemplate {
        return EventTemplate(
            eventKey = "environmental_fallen_log",
            type = EventType.ENVIRONMENTAL,
            priority = EventPriority.LOW,
            description = "A massive log has fallen across the path. It towers above you like a wall.",
            triggers = listOf(
                EventTrigger.RandomChance(probability = 0.10)
            ),
            outcomes = listOf(
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "climb_over",
                    description = "You scramble up and over the log. Exhausting but direct!",
                    choiceText = "Climb Over",
                    rewards = mapOf("xp" to 10),
                    penalties = mapOf("stamina" to 15)
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "find_detour",
                    description = "You circle around through the undergrowth. Takes longer but safer.",
                    choiceText = "Find Detour",
                    rewards = emptyMap(),
                    penalties = mapOf("time" to 10)
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "clear_path",
                    description = "You painstakingly clear smaller branches to create a path. Hard work!",
                    choiceText = "Clear the Path",
                    rewards = mapOf("xp" to 20, "karma" to 5),
                    penalties = mapOf("stamina" to 25, "time" to 20)
                )
            ),
            cooldownTicks = 4320 * 60 // 3 days
        )
    }
    
    /**
     * Mysterious glowing mushroom circle.
     * 
     * Triggers: Night, rare chance
     * Outcomes: Enter circle, Observe from distance, Leave
     */
    fun createGlowingMushroomCircle(): EventTemplate {
        return EventTemplate(
            eventKey = "mystery_glowing_mushrooms",
            type = EventType.MYSTERY,
            priority = EventPriority.NORMAL,
            description = "You stumble upon a circle of mushrooms glowing with ethereal light. Magic lingers in the air...",
            triggers = listOf(
                EventTrigger.CombinationTrigger(
                    conditions = listOf(
                        EventTrigger.TimeOfDay(startHour = 21, endHour = 4),
                        EventTrigger.RandomChance(probability = 0.04)
                    ),
                    requireAll = true
                )
            ),
            outcomes = listOf(
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "enter_circle",
                    description = "You step into the circle. The world shimmers... and you emerge with newfound knowledge!",
                    choiceText = "Enter the Circle",
                    rewards = mapOf("xp" to 100, "mystery" to 1),
                    penalties = emptyMap(),
                    consequences = listOf("consequence_fairy_touched")
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "observe",
                    description = "You watch from a safe distance, marveling at the beauty.",
                    choiceText = "Observe from Distance",
                    rewards = mapOf("xp" to 20),
                    penalties = emptyMap()
                ),
                EventOutcome(
                    id = UUID.randomUUID().toString(),
                    outcomeKey = "leave",
                    description = "Magic is unpredictable. Best to leave this alone.",
                    choiceText = "Leave Immediately",
                    rewards = emptyMap(),
                    penalties = emptyMap()
                )
            ),
            cooldownTicks = 14400 * 60 // 10 days
        )
    }
    
    // ========================================
    // CATALOG HELPER FUNCTIONS
    // ========================================
    
    /**
     * Get all available event templates.
     */
    fun getAllEventTemplates(): Map<String, EventTemplate> {
        return mapOf(
            "weather_sudden_rainstorm" to createSuddenRainstorm(),
            "encounter_ladybug_trader" to createLadybugTrader(),
            "encounter_ant_aggressor" to createAntAggressor(),
            "discovery_seed_cache" to createSeedCacheDiscovery(),
            "discovery_lore_fragment" to createLoreFragmentDiscovery(),
            "social_snail_dispute" to createSnailDispute(),
            "social_injured_moth" to createInjuredMoth(),
            "opportunity_merchant_beetle" to createMerchantBeetle(),
            "environmental_fallen_log" to createFallenLog(),
            "mystery_glowing_mushrooms" to createGlowingMushroomCircle()
        )
    }
    
    /**
     * Get event template by key.
     */
    fun getEventTemplate(eventKey: String): EventTemplate? {
        return getAllEventTemplates()[eventKey]
    }
    
    /**
     * Get all events of a specific type.
     */
    fun getEventsByType(type: EventType): List<EventTemplate> {
        return getAllEventTemplates().values.filter { it.type == type }
    }
    
    /**
     * Get all events matching a priority.
     */
    fun getEventsByPriority(priority: EventPriority): List<EventTemplate> {
        return getAllEventTemplates().values.filter { it.priority == priority }
    }
}
