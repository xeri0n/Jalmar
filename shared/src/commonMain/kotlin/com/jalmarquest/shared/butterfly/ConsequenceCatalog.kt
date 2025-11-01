package com.jalmarquest.shared.butterfly

import java.util.UUID

/**
 * Catalog of predefined consequence chains for common player choices.
 * 
 * This catalog defines the "butterfly effect" consequences that make JalmarQuest's world feel alive.
 * Each consequence chain shows how a single player choice ripples through the game world, sometimes
 * in unexpected ways.
 * 
 * Design Philosophy:
 * - **Authenticity**: Mundane acts (helping, stealing, being rude) have real consequences
 * - **Delayed Gratification**: Best consequences trigger after player has forgotten the choice
 * - **Cascading Effects**: One choice creates a chain of 2-5 consequences over time
 * - **Emergent Storytelling**: Combinations of choices create unique narratives
 * - **Proportional Impact**: Minor choices = minor effects, major choices = game-changing effects
 * 
 * Integration:
 * - DialogueManager uses these when player makes dialogue choices
 * - QuestManager uses these for quest outcomes
 * - CombatManager uses these for mercy/kill decisions
 * - NPCManager applies relationship consequences
 */
object ConsequenceCatalog {
    
    // ========================================
    // DIALOGUE CONSEQUENCES
    // ========================================
    
    /**
     * Player insulted Grumble Forgepaw (the mole craftsman).
     * 
     * Chain:
     * 1. Immediate: Relationship drops (-20)
     * 2. After 1 day: Grumble raises prices for this player
     * 3. After 3 days: Grumble tells other merchants, spreading bad reputation
     * 4. If player visits merchant guild: Merchants collectively raise prices
     */
    fun createInsultGrumbleConsequences(choiceId: String): List<Consequence> {
        return listOf(
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.NPC_RELATIONSHIP,
                trigger = ConsequenceTrigger.Immediate,
                effectKey = "npc_grumble_relationship_decrease",
                magnitude = 20, // -20 relationship
                description = "Grumble is offended by your insult"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.ITEM_AVAILABILITY,
                trigger = ConsequenceTrigger.TimeBased(ticksDelay = 1440 * 60), // 1 day (1440 minutes * 60 ticks)
                effectKey = "npc_grumble_raise_prices",
                magnitude = 50, // 50% price increase
                description = "Grumble raises prices for rude customers"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.WORLD_STATE,
                trigger = ConsequenceTrigger.TimeBased(ticksDelay = 4320 * 60), // 3 days
                effectKey = "flag_grumble_spread_rumors",
                magnitude = 100,
                description = "Grumble tells other merchants about your rudeness"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.ITEM_AVAILABILITY,
                trigger = ConsequenceTrigger.LocationBased(locationId = "merchant_guild"),
                effectKey = "merchants_collective_price_increase",
                magnitude = 25, // 25% price increase from all merchants
                description = "Merchants have heard of your reputation and raise prices"
            )
        )
    }
    
    /**
     * Player helped Grumble find his lost tools.
     * 
     * Chain:
     * 1. Immediate: Relationship increases (+30)
     * 2. After 2 days: Grumble offers discount on crafting
     * 3. After 1 week: Grumble teaches special crafting recipe
     * 4. When boss fight occurs: Grumble provides free equipment upgrade
     */
    fun createHelpGrumbleConsequences(choiceId: String): List<Consequence> {
        return listOf(
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.NPC_RELATIONSHIP,
                trigger = ConsequenceTrigger.Immediate,
                effectKey = "npc_grumble_relationship_increase",
                magnitude = 30,
                description = "Grumble is grateful for your help"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.ITEM_AVAILABILITY,
                trigger = ConsequenceTrigger.TimeBased(ticksDelay = 2880 * 60), // 2 days
                effectKey = "npc_grumble_offer_discount",
                magnitude = 20, // 20% discount
                description = "Grumble offers discounts to loyal friends"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.QUEST_UNLOCK,
                trigger = ConsequenceTrigger.TimeBased(ticksDelay = 10080 * 60), // 7 days
                effectKey = "quest_grumble_apprentice_unlock",
                magnitude = 100,
                description = "Grumble offers to teach you master crafting techniques"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.SPECIAL_EVENT,
                trigger = ConsequenceTrigger.CombinationTrigger(
                    conditions = listOf(
                        ConsequenceTrigger.QuestBased(questId = "boss_shadow_titan"),
                        ConsequenceTrigger.LocationBased(locationId = "the_quailsmith")
                    ),
                    requireAll = true
                ),
                effectKey = "event_grumble_free_upgrade",
                magnitude = 100,
                description = "Grumble provides free equipment upgrade before boss fight"
            )
        )
    }
    
    // ========================================
    // QUEST CONSEQUENCES
    // ========================================
    
    /**
     * Player saved beetle companion from drowning in puddle.
     * 
     * Chain:
     * 1. Immediate: Unlock beetle companion
     * 2. After 3 days: Beetle alerts player to hidden loot
     * 3. During combat: Beetle distracts enemy (+10% dodge)
     * 4. At critical moment: Beetle warns of ambush, saves player
     */
    fun createSaveBeetleConsequences(choiceId: String): List<Consequence> {
        return listOf(
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.COMPANION_UNLOCK,
                trigger = ConsequenceTrigger.Immediate,
                effectKey = "companion_beetle_unlock",
                magnitude = 100,
                description = "The grateful beetle joins you as a companion"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.LORE_UNLOCK,
                trigger = ConsequenceTrigger.TimeBased(ticksDelay = 4320 * 60), // 3 days
                effectKey = "lore_beetle_finds_ancient_scroll",
                magnitude = 100,
                description = "Your beetle companion discovers a hidden lore fragment"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.SPECIAL_EVENT,
                trigger = ConsequenceTrigger.LocationBased(locationId = "shadow_garden"),
                effectKey = "event_beetle_warns_ambush",
                magnitude = 100,
                description = "Beetle senses danger and warns you of ambush"
            )
        )
    }
    
    /**
     * Player abandoned quest "Find Old Quill's Glasses".
     * 
     * Chain:
     * 1. Immediate: Quest marked as failed
     * 2. After 1 day: Old Quill's relationship drops (-15)
     * 3. After 3 days: Old Quill closes library to player
     * 4. Permanent: Lore quests become unavailable
     */
    fun createAbandonQuillQuestConsequences(choiceId: String): List<Consequence> {
        return listOf(
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.QUEST_OBJECTIVE,
                trigger = ConsequenceTrigger.Immediate,
                effectKey = "quest_quill_glasses_failed",
                magnitude = 100,
                description = "Quest 'Find Old Quill's Glasses' marked as failed"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.NPC_RELATIONSHIP,
                trigger = ConsequenceTrigger.TimeBased(ticksDelay = 1440 * 60), // 1 day
                effectKey = "npc_quill_relationship_decrease",
                magnitude = 15,
                description = "Old Quill is disappointed by your lack of commitment"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.LOCATION_ACCESS,
                trigger = ConsequenceTrigger.TimeBased(ticksDelay = 4320 * 60), // 3 days
                effectKey = "location_quill_library_locked",
                magnitude = 100,
                description = "Old Quill closes the library to unreliable adventurers"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.QUEST_UNLOCK,
                trigger = ConsequenceTrigger.Immediate,
                effectKey = "quest_lore_category_locked",
                magnitude = 100,
                description = "Lore-based quests become unavailable"
            )
        )
    }
    
    // ========================================
    // COMBAT CONSEQUENCES
    // ========================================
    
    /**
     * Player showed mercy to defeated Shadow Sparrow boss.
     * 
     * Chain:
     * 1. Immediate: Boss survives, flees
     * 2. After 5 days: Shadow Sparrow becomes neutral NPC
     * 3. When visiting Nest: Shadow Sparrow visits and offers alliance
     * 4. During final boss: Shadow Sparrow aids player (+50% damage)
     */
    fun createMercyShadowSparrowConsequences(choiceId: String): List<Consequence> {
        return listOf(
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.NPC_BEHAVIOR,
                trigger = ConsequenceTrigger.Immediate,
                effectKey = "npc_shadow_sparrow_flee",
                magnitude = 100,
                description = "Shadow Sparrow flees, remembering your mercy"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.NPC_RELATIONSHIP,
                trigger = ConsequenceTrigger.TimeBased(ticksDelay = 7200 * 60), // 5 days
                effectKey = "npc_shadow_sparrow_become_neutral",
                magnitude = 50,
                description = "Shadow Sparrow's hatred turns to respect"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.QUEST_UNLOCK,
                trigger = ConsequenceTrigger.LocationBased(locationId = "jalmar_nest"),
                effectKey = "quest_shadow_alliance_unlock",
                magnitude = 100,
                description = "Shadow Sparrow approaches your nest with a peace offering"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.SPECIAL_EVENT,
                trigger = ConsequenceTrigger.QuestBased(questId = "final_boss_garden_gnome"),
                effectKey = "event_shadow_sparrow_ally",
                magnitude = 50, // +50% damage boost
                description = "Shadow Sparrow arrives to repay the debt of mercy"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.ENDING_PATH,
                trigger = ConsequenceTrigger.QuestBased(questId = "final_boss_garden_gnome"),
                effectKey = "ending_mercy_path_unlock",
                magnitude = 100,
                description = "Mercy-based ending becomes available"
            )
        )
    }
    
    /**
     * Player killed defeated Shadow Sparrow boss.
     * 
     * Chain:
     * 1. Immediate: Shadow Sparrow dies
     * 2. After 2 days: Shadow Sparrow's mate seeks revenge
     * 3. After 1 week: Shadow Faction declares player an enemy
     * 4. Permanent: Mercy-based ending locked
     */
    fun createKillShadowSparrowConsequences(choiceId: String): List<Consequence> {
        return listOf(
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.NPC_BEHAVIOR,
                trigger = ConsequenceTrigger.Immediate,
                effectKey = "npc_shadow_sparrow_death",
                magnitude = 100,
                description = "Shadow Sparrow is slain"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.SPECIAL_EVENT,
                trigger = ConsequenceTrigger.TimeBased(ticksDelay = 2880 * 60), // 2 days
                effectKey = "event_shadow_mate_revenge",
                magnitude = 75,
                description = "Shadow Sparrow's mate hunts you for revenge"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.FACTION_STANDING,
                trigger = ConsequenceTrigger.TimeBased(ticksDelay = 10080 * 60), // 7 days
                effectKey = "faction_shadow_declare_enemy",
                magnitude = 100, // Max hostility
                description = "The Shadow Faction marks you as their mortal enemy"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.ENDING_PATH,
                trigger = ConsequenceTrigger.Immediate,
                effectKey = "ending_mercy_path_locked",
                magnitude = 100,
                description = "Mercy-based ending is no longer possible"
            )
        )
    }
    
    // ========================================
    // EXPLORATION CONSEQUENCES
    // ========================================
    
    /**
     * Player stole Shiny Pebble from merchant stand.
     * 
     * Chain:
     * 1. Immediate: Gain Shiny Pebble item
     * 2. After 30 minutes: Merchant notices theft
     * 3. After 1 day: Merchant spreads word, all merchants distrust player
     * 4. When visiting merchant guild: Guards arrest player, fine imposed
     */
    fun createStealPebbleConsequences(choiceId: String): List<Consequence> {
        return listOf(
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.WORLD_STATE,
                trigger = ConsequenceTrigger.Immediate,
                effectKey = "item_shiny_pebble_acquired",
                magnitude = 1,
                description = "You obtain the Shiny Pebble"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.NPC_BEHAVIOR,
                trigger = ConsequenceTrigger.TimeBased(ticksDelay = 30 * 60), // 30 minutes
                effectKey = "npc_merchant_notice_theft",
                magnitude = 100,
                description = "The merchant realizes the pebble is missing"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.FACTION_STANDING,
                trigger = ConsequenceTrigger.TimeBased(ticksDelay = 1440 * 60), // 1 day
                effectKey = "faction_merchants_distrust",
                magnitude = 50,
                description = "Merchants collectively distrust you"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.SPECIAL_EVENT,
                trigger = ConsequenceTrigger.LocationBased(locationId = "merchant_guild"),
                effectKey = "event_guard_arrest_fine",
                magnitude = 100, // 100 seeds fine
                description = "Guards arrest you and impose a hefty fine"
            )
        )
    }
    
    // ========================================
    // SOCIAL CONSEQUENCES
    // ========================================
    
    /**
     * Player gave gift to lonely garden snail NPC.
     * 
     * Chain:
     * 1. Immediate: Snail relationship +40
     * 2. After 2 days: Snail leaves trail to hidden treasure
     * 3. After 1 week: Snail introduces player to Snail Council
     * 4. Unlock: Special snail-based crafting recipes
     */
    fun createGiftSnailConsequences(choiceId: String): List<Consequence> {
        return listOf(
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.NPC_RELATIONSHIP,
                trigger = ConsequenceTrigger.Immediate,
                effectKey = "npc_snail_relationship_increase",
                magnitude = 40,
                description = "The lonely snail is deeply touched by your kindness"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.SPECIAL_EVENT,
                trigger = ConsequenceTrigger.TimeBased(ticksDelay = 2880 * 60), // 2 days
                effectKey = "event_snail_treasure_trail",
                magnitude = 100,
                description = "The snail leaves a slime trail leading to hidden treasure"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.QUEST_UNLOCK,
                trigger = ConsequenceTrigger.TimeBased(ticksDelay = 10080 * 60), // 7 days
                effectKey = "quest_snail_council_unlock",
                magnitude = 100,
                description = "The snail introduces you to the secretive Snail Council"
            ),
            Consequence(
                id = UUID.randomUUID().toString(),
                triggeringChoiceId = choiceId,
                type = ConsequenceType.ITEM_AVAILABILITY,
                trigger = ConsequenceTrigger.QuestBased(questId = "quest_snail_council_trust"),
                effectKey = "recipes_snail_shell_crafting",
                magnitude = 100,
                description = "Snail Council shares ancient shell-crafting techniques"
            )
        )
    }
    
    // ========================================
    // CATALOG HELPER FUNCTIONS
    // ========================================
    
    /**
     * Get all available consequence templates.
     * 
     * This is used by game systems to look up appropriate consequences
     * based on player choices.
     */
    fun getAllConsequenceTemplates(): Map<String, (String) -> List<Consequence>> {
        return mapOf(
            "dialogue_grumble_insult" to ::createInsultGrumbleConsequences,
            "dialogue_grumble_help" to ::createHelpGrumbleConsequences,
            "quest_save_beetle" to ::createSaveBeetleConsequences,
            "quest_quill_abandon" to ::createAbandonQuillQuestConsequences,
            "combat_mercy_shadow_sparrow" to ::createMercyShadowSparrowConsequences,
            "combat_kill_shadow_sparrow" to ::createKillShadowSparrowConsequences,
            "exploration_steal_pebble" to ::createStealPebbleConsequences,
            "social_gift_snail" to ::createGiftSnailConsequences
        )
    }
    
    /**
     * Get consequences for a specific choice key.
     * 
     * @param choiceKey The choice identifier
     * @param choiceId The UUID of the player choice
     * @return List of consequences, or empty list if choice key not found
     */
    fun getConsequencesForChoice(choiceKey: String, choiceId: String): List<Consequence> {
        val template = getAllConsequenceTemplates()[choiceKey]
        return template?.invoke(choiceId) ?: emptyList()
    }
}
