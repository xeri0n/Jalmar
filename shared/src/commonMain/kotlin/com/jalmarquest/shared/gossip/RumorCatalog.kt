package com.jalmarquest.shared.gossip

/**
 * Catalog of rumor templates for procedural gossip generation.
 * 
 * Features 10 diverse rumor templates covering:
 * - Heroic deeds (combat victories, quest completions, rescues)
 * - Crimes (theft, assault, quest abandonment)
 * - Failures (fleeing combat, getting lost, embarrassing moments)
 * - NPC gossip (relationships, drama, scandals)
 * - World events (weather disasters, mysterious phenomena)
 * - Discoveries (locations, items, secrets)
 * 
 * Each template includes mutation paths showing how rumors
 * evolve through the telephone-game effect.
 */
object RumorCatalog {
    
    /**
     * Get all rumor templates.
     */
    fun getAllTemplates(): Map<String, RumorTemplate> {
        return mapOf(
            "rumor_defeated_enemies" to createDefeatedEnemiesRumor(),
            "rumor_stole_from_npc" to createStoleFromNPCRumor(),
            "rumor_fled_combat" to createFledCombatRumor(),
            "rumor_npc_romance" to createNPCRomanceRumor(),
            "rumor_weather_disaster" to createWeatherDisasterRumor(),
            "rumor_found_treasure" to createFoundTreasureRumor(),
            "rumor_saved_npc" to createSavedNPCRumor(),
            "rumor_quest_failure" to createQuestFailureRumor(),
            "rumor_mysterious_light" to createMysteriousLightRumor(),
            "rumor_champion_duel" to createChampionDuelRumor()
        )
    }
    
    /**
     * Get template by ID.
     */
    fun getTemplate(templateId: String): RumorTemplate? {
        return getAllTemplates()[templateId]
    }
    
    /**
     * Get templates by category.
     */
    fun getTemplatesByCategory(category: RumorCategory): List<RumorTemplate> {
        return getAllTemplates().values.filter { it.category == category }
    }
    
    // ============================================
    // HEROIC DEED RUMORS
    // ============================================
    
    /**
     * Rumor: Player defeated enemies.
     * 
     * ACCURATE: "Jalmar defeated 3 ants near the garden path."
     * EXAGGERATED: "Jalmar defeated 6 ants near the garden path (single-handedly!)"
     * DISTORTED: "Jalmar defeated 12 beetles near the garden path (single-handedly!)"
     * MYTHICAL: "Jalmar slew the legendary 24 beetles near the garden path (single-handedly!)"
     */
    private fun createDefeatedEnemiesRumor(): RumorTemplate {
        return RumorTemplate(
            templateId = "rumor_defeated_enemies",
            category = RumorCategory.HEROIC_DEED,
            baseText = "{playerName} defeated {enemyCount} {enemyType} near {location}.",
            mutationPaths = listOf(
                MutationPath(
                    truthLevel = TruthLevel.ACCURATE,
                    type = MutationType.EXAGGERATE,
                    textTransform = "{playerName} defeated {enemyCount} {enemyType} near {location} (single-handedly!)",
                    parameterChanges = mapOf("enemyCount" to "multiply:2")
                ),
                MutationPath(
                    truthLevel = TruthLevel.EXAGGERATED,
                    type = MutationType.DISTORT,
                    textTransform = "{playerName} defeated {enemyCount} beetles near {location} (single-handedly!)",
                    parameterChanges = mapOf("enemyCount" to "multiply:2", "enemyType" to "beetles")
                ),
                MutationPath(
                    truthLevel = TruthLevel.DISTORTED,
                    type = MutationType.MYTHOLOGIZE,
                    textTransform = "{playerName} slew the legendary {enemyCount} beetles near {location} (single-handedly!)",
                    parameterChanges = mapOf("enemyCount" to "multiply:2")
                )
            ),
            reputationEffects = listOf(
                ReputationEffect("buttonburgh_citizens", 10, "Heard rumor of heroic combat victory")
            ),
            spreadProbability = 0.7,  // Good news spreads fast!
            mutationChance = 0.25
        )
    }
    
    /**
     * Rumor: Player saved NPC from danger.
     * 
     * ACCURATE: "Jalmar rescued Grumble Forgepaw from a spider web."
     * EXAGGERATED: "Jalmar rescued Grumble Forgepaw from a spider web, risking life and limb!"
     * DISTORTED: "Jalmar rescued Grumble Forgepaw from a giant spider, risking life and limb!"
     * MYTHICAL: "Jalmar rescued Grumble Forgepaw from a dragon-sized spider, risking life and limb!"
     */
    private fun createSavedNPCRumor(): RumorTemplate {
        return RumorTemplate(
            templateId = "rumor_saved_npc",
            category = RumorCategory.HEROIC_DEED,
            baseText = "{playerName} rescued {npcName} from {danger}.",
            mutationPaths = listOf(
                MutationPath(
                    truthLevel = TruthLevel.ACCURATE,
                    type = MutationType.EMBELLISH,
                    textTransform = "{playerName} rescued {npcName} from {danger}, risking life and limb!",
                    parameterChanges = emptyMap()
                ),
                MutationPath(
                    truthLevel = TruthLevel.EXAGGERATED,
                    type = MutationType.DISTORT,
                    textTransform = "{playerName} rescued {npcName} from a giant spider, risking life and limb!",
                    parameterChanges = mapOf("danger" to "a giant spider")
                ),
                MutationPath(
                    truthLevel = TruthLevel.DISTORTED,
                    type = MutationType.MYTHOLOGIZE,
                    textTransform = "{playerName} rescued {npcName} from a dragon-sized spider, risking life and limb!",
                    parameterChanges = mapOf("danger" to "a dragon-sized spider")
                )
            ),
            reputationEffects = listOf(
                ReputationEffect("buttonburgh_citizens", 15, "Heard rumor of heroic rescue")
            ),
            spreadProbability = 0.8,  // Heroic rescues spread quickly
            mutationChance = 0.2
        )
    }
    
    /**
     * Rumor: Player won champion duel.
     * 
     * ACCURATE: "Jalmar challenged the arena champion and won!"
     * EXAGGERATED: "Jalmar challenged the arena champion and won in under 10 seconds!"
     * DISTORTED: "Jalmar challenged the legendary arena champion and won in under 5 seconds!"
     * MYTHICAL: "Jalmar challenged the immortal arena champion and won with a single blow!"
     */
    private fun createChampionDuelRumor(): RumorTemplate {
        return RumorTemplate(
            templateId = "rumor_champion_duel",
            category = RumorCategory.HEROIC_DEED,
            baseText = "{playerName} challenged the arena champion and won!",
            mutationPaths = listOf(
                MutationPath(
                    truthLevel = TruthLevel.ACCURATE,
                    type = MutationType.EXAGGERATE,
                    textTransform = "{playerName} challenged the arena champion and won in under 10 seconds!",
                    parameterChanges = emptyMap()
                ),
                MutationPath(
                    truthLevel = TruthLevel.EXAGGERATED,
                    type = MutationType.EMBELLISH,
                    textTransform = "{playerName} challenged the legendary arena champion and won in under 5 seconds!",
                    parameterChanges = emptyMap()
                ),
                MutationPath(
                    truthLevel = TruthLevel.DISTORTED,
                    type = MutationType.MYTHOLOGIZE,
                    textTransform = "{playerName} challenged the immortal arena champion and won with a single blow!",
                    parameterChanges = emptyMap()
                )
            ),
            reputationEffects = listOf(
                ReputationEffect("buttonburgh_citizens", 20, "Heard rumor of legendary combat prowess")
            ),
            spreadProbability = 0.9,  // Legendary victories spread like wildfire
            mutationChance = 0.3
        )
    }
    
    // ============================================
    // CRIME RUMORS
    // ============================================
    
    /**
     * Rumor: Player stole from NPC.
     * 
     * ACCURATE: "Jalmar stole 5 seeds from Merchant Beetle."
     * EXAGGERATED: "Jalmar stole 10 seeds from Merchant Beetle in broad daylight!"
     * DISTORTED: "Jalmar stole 20 seeds from Merchant Beetle's entire inventory in broad daylight!"
     * MYTHICAL: "Jalmar stole Merchant Beetle's entire fortune of 40 seeds in broad daylight!"
     */
    private fun createStoleFromNPCRumor(): RumorTemplate {
        return RumorTemplate(
            templateId = "rumor_stole_from_npc",
            category = RumorCategory.CRIME,
            baseText = "{playerName} stole {itemCount} {itemType} from {npcName}.",
            mutationPaths = listOf(
                MutationPath(
                    truthLevel = TruthLevel.ACCURATE,
                    type = MutationType.EXAGGERATE,
                    textTransform = "{playerName} stole {itemCount} {itemType} from {npcName} in broad daylight!",
                    parameterChanges = mapOf("itemCount" to "multiply:2")
                ),
                MutationPath(
                    truthLevel = TruthLevel.EXAGGERATED,
                    type = MutationType.EMBELLISH,
                    textTransform = "{playerName} stole {itemCount} {itemType} from {npcName}'s entire inventory in broad daylight!",
                    parameterChanges = mapOf("itemCount" to "multiply:2")
                ),
                MutationPath(
                    truthLevel = TruthLevel.DISTORTED,
                    type = MutationType.MYTHOLOGIZE,
                    textTransform = "{playerName} stole {npcName}'s entire fortune of {itemCount} {itemType} in broad daylight!",
                    parameterChanges = mapOf("itemCount" to "multiply:2")
                )
            ),
            reputationEffects = listOf(
                ReputationEffect("buttonburgh_citizens", -15, "Heard rumor of theft")
            ),
            spreadProbability = 0.6,  // Bad news spreads moderately
            mutationChance = 0.25
        )
    }
    
    // ============================================
    // FAILURE RUMORS
    // ============================================
    
    /**
     * Rumor: Player fled from combat.
     * 
     * ACCURATE: "Jalmar fled from 2 beetles near the swamp."
     * EXAGGERATED: "Jalmar fled from 4 beetles near the swamp, abandoning the quest!"
     * DISTORTED: "Jalmar fled from 8 giant beetles near the swamp, abandoning the quest!"
     * MYTHICAL: "Jalmar fled from 16 dragon-beetles near the swamp, abandoning the quest and the town!"
     */
    private fun createFledCombatRumor(): RumorTemplate {
        return RumorTemplate(
            templateId = "rumor_fled_combat",
            category = RumorCategory.FAILURE,
            baseText = "{playerName} fled from {enemyCount} {enemyType} near {location}.",
            mutationPaths = listOf(
                MutationPath(
                    truthLevel = TruthLevel.ACCURATE,
                    type = MutationType.EXAGGERATE,
                    textTransform = "{playerName} fled from {enemyCount} {enemyType} near {location}, abandoning the quest!",
                    parameterChanges = mapOf("enemyCount" to "multiply:2")
                ),
                MutationPath(
                    truthLevel = TruthLevel.EXAGGERATED,
                    type = MutationType.DISTORT,
                    textTransform = "{playerName} fled from {enemyCount} giant beetles near {location}, abandoning the quest!",
                    parameterChanges = mapOf("enemyCount" to "multiply:2", "enemyType" to "giant beetles")
                ),
                MutationPath(
                    truthLevel = TruthLevel.DISTORTED,
                    type = MutationType.MYTHOLOGIZE,
                    textTransform = "{playerName} fled from {enemyCount} dragon-beetles near {location}, abandoning the quest and the town!",
                    parameterChanges = mapOf("enemyCount" to "multiply:2", "enemyType" to "dragon-beetles")
                )
            ),
            reputationEffects = listOf(
                ReputationEffect("buttonburgh_citizens", -10, "Heard rumor of cowardice")
            ),
            spreadProbability = 0.5,  // Embarrassing failures spread at moderate rate
            mutationChance = 0.3
        )
    }
    
    /**
     * Rumor: Player failed quest.
     * 
     * ACCURATE: "Jalmar failed to deliver Grumble's package on time."
     * EXAGGERATED: "Jalmar failed to deliver Grumble's package on time, losing half the contents!"
     * DISTORTED: "Jalmar failed to deliver Grumble's package on time, losing the entire shipment!"
     * MYTHICAL: "Jalmar failed to deliver Grumble's package, destroying Grumble's entire business!"
     */
    private fun createQuestFailureRumor(): RumorTemplate {
        return RumorTemplate(
            templateId = "rumor_quest_failure",
            category = RumorCategory.FAILURE,
            baseText = "{playerName} failed to deliver {npcName}'s package on time.",
            mutationPaths = listOf(
                MutationPath(
                    truthLevel = TruthLevel.ACCURATE,
                    type = MutationType.EMBELLISH,
                    textTransform = "{playerName} failed to deliver {npcName}'s package on time, losing half the contents!",
                    parameterChanges = emptyMap()
                ),
                MutationPath(
                    truthLevel = TruthLevel.EXAGGERATED,
                    type = MutationType.EXAGGERATE,
                    textTransform = "{playerName} failed to deliver {npcName}'s package on time, losing the entire shipment!",
                    parameterChanges = emptyMap()
                ),
                MutationPath(
                    truthLevel = TruthLevel.DISTORTED,
                    type = MutationType.MYTHOLOGIZE,
                    textTransform = "{playerName} failed to deliver {npcName}'s package, destroying {npcName}'s entire business!",
                    parameterChanges = emptyMap()
                )
            ),
            reputationEffects = listOf(
                ReputationEffect("buttonburgh_citizens", -8, "Heard rumor of quest failure")
            ),
            spreadProbability = 0.4,
            mutationChance = 0.25
        )
    }
    
    // ============================================
    // NPC GOSSIP RUMORS
    // ============================================
    
    /**
     * Rumor: NPC romance.
     * 
     * ACCURATE: "Grumble Forgepaw and Garden Snail were seen sharing seeds."
     * EXAGGERATED: "Grumble Forgepaw and Garden Snail were seen sharing seeds under the moonlight!"
     * DISTORTED: "Grumble Forgepaw and Garden Snail are planning to build a nest together under the moonlight!"
     * MYTHICAL: "Grumble Forgepaw and Garden Snail are engaged in a forbidden romance that will unite the clans!"
     */
    private fun createNPCRomanceRumor(): RumorTemplate {
        return RumorTemplate(
            templateId = "rumor_npc_romance",
            category = RumorCategory.NPC_GOSSIP,
            baseText = "{npcName1} and {npcName2} were seen sharing seeds.",
            mutationPaths = listOf(
                MutationPath(
                    truthLevel = TruthLevel.ACCURATE,
                    type = MutationType.EMBELLISH,
                    textTransform = "{npcName1} and {npcName2} were seen sharing seeds under the moonlight!",
                    parameterChanges = emptyMap()
                ),
                MutationPath(
                    truthLevel = TruthLevel.EXAGGERATED,
                    type = MutationType.DISTORT,
                    textTransform = "{npcName1} and {npcName2} are planning to build a nest together under the moonlight!",
                    parameterChanges = emptyMap()
                ),
                MutationPath(
                    truthLevel = TruthLevel.DISTORTED,
                    type = MutationType.MYTHOLOGIZE,
                    textTransform = "{npcName1} and {npcName2} are engaged in a forbidden romance that will unite the clans!",
                    parameterChanges = emptyMap()
                )
            ),
            reputationEffects = emptyList(),  // Neutral gossip
            spreadProbability = 0.9,  // Juicy gossip spreads like wildfire!
            mutationChance = 0.35
        )
    }
    
    // ============================================
    // WORLD EVENT RUMORS
    // ============================================
    
    /**
     * Rumor: Weather disaster.
     * 
     * ACCURATE: "A rainstorm flooded the meadow path yesterday."
     * EXAGGERATED: "A massive rainstorm flooded the meadow path yesterday, washing away nests!"
     * DISTORTED: "A massive rainstorm flooded half of Buttonburgh yesterday, washing away nests!"
     * MYTHICAL: "A biblical flood destroyed half of Buttonburgh yesterday, washing away entire districts!"
     */
    private fun createWeatherDisasterRumor(): RumorTemplate {
        return RumorTemplate(
            templateId = "rumor_weather_disaster",
            category = RumorCategory.WORLD_EVENT,
            baseText = "A rainstorm flooded {location} yesterday.",
            mutationPaths = listOf(
                MutationPath(
                    truthLevel = TruthLevel.ACCURATE,
                    type = MutationType.EMBELLISH,
                    textTransform = "A massive rainstorm flooded {location} yesterday, washing away nests!",
                    parameterChanges = emptyMap()
                ),
                MutationPath(
                    truthLevel = TruthLevel.EXAGGERATED,
                    type = MutationType.EXAGGERATE,
                    textTransform = "A massive rainstorm flooded half of Buttonburgh yesterday, washing away nests!",
                    parameterChanges = mapOf("location" to "half of Buttonburgh")
                ),
                MutationPath(
                    truthLevel = TruthLevel.DISTORTED,
                    type = MutationType.MYTHOLOGIZE,
                    textTransform = "A biblical flood destroyed half of Buttonburgh yesterday, washing away entire districts!",
                    parameterChanges = emptyMap()
                )
            ),
            reputationEffects = emptyList(),
            spreadProbability = 0.7,
            mutationChance = 0.3
        )
    }
    
    /**
     * Rumor: Mysterious light sighting.
     * 
     * ACCURATE: "Strange lights were seen near the old oak tree last night."
     * EXAGGERATED: "Strange glowing lights were seen near the old oak tree last night, moving in patterns!"
     * DISTORTED: "Strange glowing lights were seen near the old oak tree last night, spelling out ancient runes!"
     * MYTHICAL: "Otherworldly beings of pure light appeared near the old oak tree, spelling out prophecies!"
     */
    private fun createMysteriousLightRumor(): RumorTemplate {
        return RumorTemplate(
            templateId = "rumor_mysterious_light",
            category = RumorCategory.WORLD_EVENT,
            baseText = "Strange lights were seen near {location} last night.",
            mutationPaths = listOf(
                MutationPath(
                    truthLevel = TruthLevel.ACCURATE,
                    type = MutationType.EMBELLISH,
                    textTransform = "Strange glowing lights were seen near {location} last night, moving in patterns!",
                    parameterChanges = emptyMap()
                ),
                MutationPath(
                    truthLevel = TruthLevel.EXAGGERATED,
                    type = MutationType.DISTORT,
                    textTransform = "Strange glowing lights were seen near {location} last night, spelling out ancient runes!",
                    parameterChanges = emptyMap()
                ),
                MutationPath(
                    truthLevel = TruthLevel.DISTORTED,
                    type = MutationType.MYTHOLOGIZE,
                    textTransform = "Otherworldly beings of pure light appeared near {location}, spelling out prophecies!",
                    parameterChanges = mapOf("location" to "{location}")
                )
            ),
            reputationEffects = emptyList(),
            spreadProbability = 0.8,  // Mysteries spread quickly
            mutationChance = 0.4
        )
    }
    
    // ============================================
    // DISCOVERY RUMORS
    // ============================================
    
    /**
     * Rumor: Player found treasure.
     * 
     * ACCURATE: "Jalmar found a cache of 20 seeds hidden under a leaf."
     * EXAGGERATED: "Jalmar found a cache of 40 seeds hidden under a leaf, enough to feed a family!"
     * DISTORTED: "Jalmar found a legendary cache of 80 seeds hidden under a leaf, enough to feed a family for months!"
     * MYTHICAL: "Jalmar discovered an ancient treasure hoard of 160 seeds, enough to make them the richest quail alive!"
     */
    private fun createFoundTreasureRumor(): RumorTemplate {
        return RumorTemplate(
            templateId = "rumor_found_treasure",
            category = RumorCategory.DISCOVERY,
            baseText = "{playerName} found a cache of {itemCount} seeds hidden under a leaf.",
            mutationPaths = listOf(
                MutationPath(
                    truthLevel = TruthLevel.ACCURATE,
                    type = MutationType.EXAGGERATE,
                    textTransform = "{playerName} found a cache of {itemCount} seeds hidden under a leaf, enough to feed a family!",
                    parameterChanges = mapOf("itemCount" to "multiply:2")
                ),
                MutationPath(
                    truthLevel = TruthLevel.EXAGGERATED,
                    type = MutationType.EMBELLISH,
                    textTransform = "{playerName} found a legendary cache of {itemCount} seeds hidden under a leaf, enough to feed a family for months!",
                    parameterChanges = mapOf("itemCount" to "multiply:2")
                ),
                MutationPath(
                    truthLevel = TruthLevel.DISTORTED,
                    type = MutationType.MYTHOLOGIZE,
                    textTransform = "{playerName} discovered an ancient treasure hoard of {itemCount} seeds, enough to make them the richest quail alive!",
                    parameterChanges = mapOf("itemCount" to "multiply:2")
                )
            ),
            reputationEffects = listOf(
                ReputationEffect("buttonburgh_citizens", 5, "Heard rumor of discovery")
            ),
            spreadProbability = 0.6,
            mutationChance = 0.3
        )
    }
}
