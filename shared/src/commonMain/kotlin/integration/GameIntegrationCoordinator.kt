package integration

import combat.CombatManager
import companion.CompanionManager
import concoctions.ConcoctionsManager
import crafting.CraftingManager
import currency.CurrencyManager
import dialogue.DialogueManager
import dungeon.DungeonManager
import equipment.EquipmentManager
import event.EventBus
import event.GameEvent
import faction.FactionManager
import inventory.InventoryManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import movement.MovementManager
import nest.NestManager
import npc.NPCManager
import persistence.SaveManager
import quest.QuestManager
import resource.ResourceNodeManager
import skill.SkillManager
import state.GameStateManager
import thoughtcabinet.ThoughtCabinetManager
import time.TimeManager
import world.LocationManager
import world.ai.*

/**
 * Master coordinator that wires all game systems together via the event bus.
 * This is the central nervous system of the game.
 */
class GameIntegrationCoordinator(
    private val gameStateManager: GameStateManager,
    private val saveManager: SaveManager,
    private val timeManager: TimeManager,
    private val locationManager: LocationManager,
    private val movementManager: MovementManager,
    private val inventoryManager: InventoryManager,
    private val currencyManager: CurrencyManager,
    private val equipmentManager: EquipmentManager,
    private val craftingManager: CraftingManager,
    private val combatManager: CombatManager,
    private val questManager: QuestManager,
    private val dialogueManager: DialogueManager,
    private val npcManager: NPCManager,
    private val factionManager: FactionManager,
    private val skillManager: SkillManager,
    private val nestManager: NestManager,
    private val companionManager: CompanionManager,
    private val dungeonManager: DungeonManager,
    private val resourceNodeManager: ResourceNodeManager,
    private val thoughtCabinetManager: ThoughtCabinetManager,
    private val concoctionsManager: ConcoctionsManager,
    private val butterflyEffectManager: ButterflyEffectManager,
    private val aiDirector: AIDirector,
    private val scope: CoroutineScope
) {
    
    fun initialize() {
        // Subscribe to all events and route them to appropriate handlers
        EventBus.events.onEach { event ->
            handleEvent(event)
        }.launchIn(scope)
        
        // Initialize periodic tasks
        initializePeriodicTasks()
        
        // Wire up save system to include all managers
        wireSaveSystem()
    }
    
    private suspend fun handleEvent(event: GameEvent) {
        when (event) {
            // Player movement triggers multiple systems
            is GameEvent.PlayerMoved -> {
                locationManager.markLocationDiscovered(event.newLocationId)
                questManager.checkLocationObjectives(event.newLocationId)
                npcManager.updateNearbyNPCs(event.newLocationId)
                resourceNodeManager.checkAvailableNodes(event.newLocationId)
                dungeonManager.checkDungeonEntrance(event.newLocationId)
                aiDirector.onPlayerMoved(event.newLocationId)
                butterflyEffectManager.logChoice("MOVED_TO", event.newLocationId)
            }
            
            // Combat victory cascades through many systems
            is GameEvent.CombatEnded -> {
                if (event.victory) {
                    event.rewards?.let { rewards ->
                        gameStateManager.addExperience(rewards.experience)
                        currencyManager.addSeeds(rewards.seeds)
                        rewards.items.forEach { inventoryManager.addItem(it) }
                        rewards.thoughts?.forEach { thoughtCabinetManager.unlockThought(it) }
                    }
                }
                questManager.checkCombatObjectives()
                skillManager.addCombatExperience()
            }
            
            // Quest completion affects multiple systems
            is GameEvent.QuestCompleted -> {
                val quest = questManager.getQuest(event.questId)
                quest?.rewards?.forEach { reward ->
                    when (reward.type) {
                        "SEEDS" -> currencyManager.addSeeds(reward.amount)
                        "ITEM" -> inventoryManager.addItem(reward.id)
                        "EXPERIENCE" -> gameStateManager.addExperience(reward.amount)
                        "FACTION_REP" -> factionManager.modifyReputation(reward.id, reward.amount)
                        "THOUGHT" -> thoughtCabinetManager.unlockThought(reward.id)
                        "RECIPE" -> craftingManager.unlockRecipe(reward.id)
                        "COMPANION" -> companionManager.unlockCompanion(reward.id)
                        "ARCHETYPE_TALENT" -> gameStateManager.addTalentPoint(reward.id)
                    }
                }
                event.choiceMade?.let { 
                    butterflyEffectManager.logChoice("QUEST_${event.questId}", it)
                }
                aiDirector.onQuestCompleted(event.questId)
            }
            
            // Item crafting updates multiple systems
            is GameEvent.ItemCrafted -> {
                questManager.checkCraftingObjectives(event.recipeId)
                skillManager.addCraftingExperience(event.recipeId)
                butterflyEffectManager.logChoice("CRAFTED", event.itemId)
            }
            
            // NPC interactions affect relationships and quests
            is GameEvent.NPCInteraction -> {
                dialogueManager.processInteraction(event.npcId, event.interactionType)
                questManager.checkNPCObjectives(event.npcId)
                if (event.interactionType == "GIFT") {
                    companionManager.checkCompanionUnlock(event.npcId)
                }
            }
            
            // Time changes trigger scheduled events
            is GameEvent.TimeChanged -> {
                npcManager.updateSchedules(event.hour)
                resourceNodeManager.respawnNodes()
                if (event.hour == 0) { // New day
                    questManager.refreshDailyQuests()
                    currencyManager.processDailyIncome()
                }
                if (event.season != timeManager.currentSeason) {
                    concoctionsManager.updateSeasonalIngredients(event.season)
                    aiDirector.onSeasonChange(event.season)
                }
            }
            
            // Currency changes may trigger shop unlocks
            is GameEvent.CurrencyChanged -> {
                if (event.currencyType == "GLIMMER_SHARDS") {
                    // Check for premium shop unlocks
                    checkPremiumUnlocks(event.amount)
                }
            }
            
            // Faction reputation affects available content
            is GameEvent.FactionReputationChanged -> {
                questManager.updateFactionQuests(event.factionId, event.newRep)
                dialogueManager.updateFactionDialogues(event.factionId, event.newRep)
                if (event.newRep >= 80) {
                    EventBus.emit(GameEvent.AchievementUnlocked("faction_${event.factionId}_revered"))
                }
            }
            
            // Nest upgrades affect player stats
            is GameEvent.NestUpgraded -> {
                nestManager.recalculateBonuses()
                gameStateManager.updatePlayerStats()
                companionManager.updateCritterSatisfaction()
            }
            
            // Thought internalization affects dialogue and abilities
            is GameEvent.ThoughtInternalized -> {
                val thought = thoughtCabinetManager.getThought(event.thoughtId)
                thought?.effects?.forEach { effect ->
                    when (effect.type) {
                        "STAT_BOOST" -> gameStateManager.addStatModifier(effect)
                        "DIALOGUE_UNLOCK" -> dialogueManager.unlockDialoguePath(effect.id)
                        "ABILITY_UNLOCK" -> skillManager.unlockAbility(effect.id)
                    }
                }
            }
            
            // AI Director world events
            is GameEvent.WorldEventTriggered -> {
                // Display event to player and process consequences
                processWorldEvent(event.eventType, event.description)
            }
            
            // Radiant quest generation
            is GameEvent.RadiantQuestGenerated -> {
                questManager.addRadiantQuest(event.questTemplate)
            }
            
            // Handle all other events with default logging
            else -> {
                // Log event for butterfly effect tracking
                butterflyEffectManager.logEvent(event)
            }
        }
    }
    
    private fun initializePeriodicTasks() {
        // 1 minute updates
        scope.launch {
            while (isActive) {
                delay(60_000)
                resourceNodeManager.processRespawns()
            }
        }
        
        // 5 minute updates
        scope.launch {
            while (isActive) {
                delay(300_000)
                saveManager.autosave()
                npcManager.updateAIGoals()
                aiDirector.evaluateWorldState()
            }
        }
        
        // 30 minute updates
        scope.launch {
            while (isActive) {
                delay(1_800_000)
                aiDirector.generateRadiantQuest()
                butterflyEffectManager.processConsequences()
            }
        }
        
        // 1 hour updates
        scope.launch {
            while (isActive) {
                delay(3_600_000)
                timeManager.advanceSeason()
            }
        }
    }
    
    private fun wireSaveSystem() {
        // Ensure all managers register their save data with SaveManager
        saveManager.registerSaveableComponent("inventory", inventoryManager)
        saveManager.registerSaveableComponent("currency", currencyManager)
        saveManager.registerSaveableComponent("equipment", equipmentManager)
        saveManager.registerSaveableComponent("crafting", craftingManager)
        saveManager.registerSaveableComponent("combat", combatManager)
        saveManager.registerSaveableComponent("quest", questManager)
        saveManager.registerSaveableComponent("dialogue", dialogueManager)
        saveManager.registerSaveableComponent("npc", npcManager)
        saveManager.registerSaveableComponent("faction", factionManager)
        saveManager.registerSaveableComponent("skill", skillManager)
        saveManager.registerSaveableComponent("nest", nestManager)
        saveManager.registerSaveableComponent("companion", companionManager)
        saveManager.registerSaveableComponent("dungeon", dungeonManager)
        saveManager.registerSaveableComponent("resource", resourceNodeManager)
        saveManager.registerSaveableComponent("thought", thoughtCabinetManager)
        saveManager.registerSaveableComponent("concoctions", concoctionsManager)
        saveManager.registerSaveableComponent("butterfly", butterflyEffectManager)
        saveManager.registerSaveableComponent("ai_director", aiDirector)
    }
    
    private suspend fun checkPremiumUnlocks(shardBalance: Int) {
        // Check for premium cosmetics, character slots, etc.
        if (shardBalance >= 100) {
            // Unlock premium content tiers
        }
    }
    
    private suspend fun processWorldEvent(eventType: String, description: String) {
        // Process AI-generated world events
        when (eventType) {
            "MERCHANT_ARRIVAL" -> {
                // Spawn special merchant NPC
            }
            "WEATHER_EXTREME" -> {
                // Apply weather modifiers
            }
            "FACTION_CONFLICT" -> {
                // Update faction tensions
            }
        }
    }
}
