package com.jalmarquest.shared.quest

import com.jalmarquest.shared.inventory.InventoryManager
import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.model.Player

/**
 * Manages quest acceptance, progression, completion, and rewards.
 * 
 * Follows stateless functional pattern:
 * - All methods return new Player/GameState instances
 * - No internal mutable state
 * - Thread-safe by design
 * 
 * Integration points:
 * - Combat system: updateObjective() for KILL objectives
 * - Inventory system: updateObjective() for COLLECT/CRAFT objectives
 * - Location system: updateObjective() for REACH objectives
 * - Experience system: updateObjective() for LEVEL objectives
 * - Dungeon system: updateObjective() for DUNGEON_CLEAR objectives
 */
class QuestManager {
    private val questCatalog = QuestCatalog
    private val inventoryManager = InventoryManager
    
    /**
     * Result of accepting a quest.
     */
    sealed class AcceptQuestResult {
        data class Success(val gameState: GameState) : AcceptQuestResult()
        data class Failure(val reason: AcceptQuestFailure) : AcceptQuestResult()
    }
    
    enum class AcceptQuestFailure {
        QUEST_NOT_FOUND,
        ALREADY_ACTIVE,
        ALREADY_COMPLETED,
        PREREQUISITES_NOT_MET,
        LEVEL_TOO_LOW
    }
    
    /**
     * Result of updating quest objectives.
     */
    sealed class UpdateObjectiveResult {
        data class Success(val gameState: GameState, val questsProgressed: List<String>) : UpdateObjectiveResult()
        data object NoChange : UpdateObjectiveResult()
    }
    
    /**
     * Result of completing a quest.
     */
    sealed class CompleteQuestResult {
        data class Success(val gameState: GameState) : CompleteQuestResult()
        data class Failure(val reason: CompleteQuestFailure) : CompleteQuestResult()
    }
    
    enum class CompleteQuestFailure {
        QUEST_NOT_FOUND,
        NOT_ACTIVE,
        OBJECTIVES_INCOMPLETE
    }
    
    /**
     * Result of turning in a quest for rewards.
     */
    sealed class TurnInQuestResult {
        data class Success(val gameState: GameState, val player: Player, val rewards: QuestReward) : TurnInQuestResult()
        data class Failure(val reason: TurnInQuestFailure) : TurnInQuestResult()
    }
    
    enum class TurnInQuestFailure {
        QUEST_NOT_FOUND,
        NOT_COMPLETED,
        ALREADY_TURNED_IN
    }
    
    /**
     * Accepts a new quest, adding it to active quests.
     * 
     * Validation:
     * - Quest must exist in catalog
     * - Quest must not already be active or completed
     * - Prerequisites must be completed
     * - Player level must meet minimum
     * 
     * @param gameState Current game state
     * @param questId Quest to accept
     * @return AcceptQuestResult with updated game state or failure reason
     */
    fun acceptQuest(gameState: GameState, questId: String): AcceptQuestResult {
        val quest = questCatalog.getQuest(questId)
            ?: return AcceptQuestResult.Failure(AcceptQuestFailure.QUEST_NOT_FOUND)
        
        // Check if already active
        if (gameState.activeQuests.contains(questId)) {
            return AcceptQuestResult.Failure(AcceptQuestFailure.ALREADY_ACTIVE)
        }
        
        // Check if already completed
        if (gameState.completedQuests.contains(questId)) {
            return AcceptQuestResult.Failure(AcceptQuestFailure.ALREADY_COMPLETED)
        }
        
        // Check level requirement
        if (gameState.player.level < quest.level) {
            return AcceptQuestResult.Failure(AcceptQuestFailure.LEVEL_TOO_LOW)
        }
        
        // Check prerequisites
        val unmetPrerequisites = quest.prerequisiteQuestIds.filter { prereqId ->
            !gameState.completedQuests.contains(prereqId)
        }
        if (unmetPrerequisites.isNotEmpty()) {
            return AcceptQuestResult.Failure(AcceptQuestFailure.PREREQUISITES_NOT_MET)
        }
        
        // Accept quest - add to active list
        val updatedGameState = gameState.copy(
            activeQuests = gameState.activeQuests + questId
        )
        
        return AcceptQuestResult.Success(updatedGameState)
    }
    
    /**
     * Updates quest objectives based on player actions.
     * 
     * This is the primary integration point for other systems:
     * - Combat: updateObjective(gameState, KILL, enemyId, 1)
     * - Inventory: updateObjective(gameState, COLLECT, itemId, amount)
     * - Crafting: updateObjective(gameState, CRAFT, itemId, 1)
     * - Location: updateObjective(gameState, REACH, locationId, 1)
     * - Leveling: updateObjective(gameState, LEVEL, "player_level", newLevel)
     * - Dungeons: updateObjective(gameState, DUNGEON_CLEAR, dungeonFloorId, 1)
     * 
     * @param gameState Current game state
     * @param objectiveType Type of objective to update
     * @param targetId Target identifier (enemy ID, item ID, location ID, etc.)
     * @param amount Amount to increment (default 1)
     * @return UpdateObjectiveResult with updated state and list of progressed quests
     */
    fun updateObjective(
        gameState: GameState,
        objectiveType: ObjectiveType,
        targetId: String,
        amount: Int = 1
    ): UpdateObjectiveResult {
        if (gameState.activeQuests.isEmpty()) {
            return UpdateObjectiveResult.NoChange
        }
        
        val questsProgressed = mutableListOf<String>()
        var currentGameState = gameState
        
        // Check each active quest for matching objectives
        gameState.activeQuests.forEach { questId ->
            val quest = questCatalog.getQuest(questId) ?: return@forEach
            
            // Find matching objectives
            val matchingObjectives = quest.objectives.filter { objective ->
                objective.type == objectiveType &&
                (objective.targetId.isEmpty() || objective.targetId == targetId) &&
                objective.currentProgress < objective.targetCount
            }
            
            if (matchingObjectives.isNotEmpty()) {
                // Update quest with progressed objectives
                val updatedObjectives = quest.objectives.map { objective ->
                    if (matchingObjectives.contains(objective)) {
                        val newProgress = (objective.currentProgress + amount).coerceAtMost(objective.targetCount)
                        objective.copy(currentProgress = newProgress)
                    } else {
                        objective
                    }
                }
                
                // Store updated quest (in real implementation, this would be in GameState.questProgress map)
                // For now, we track progress via the objectives themselves
                questsProgressed.add(questId)
                
                // Auto-complete if enabled and quest is complete
                val updatedQuest = quest.copy(objectives = updatedObjectives)
                if (updatedQuest.isComplete() && quest.autoComplete) {
                    currentGameState = completeQuestInternal(currentGameState, questId)
                }
            }
        }
        
        return if (questsProgressed.isNotEmpty()) {
            UpdateObjectiveResult.Success(currentGameState, questsProgressed)
        } else {
            UpdateObjectiveResult.NoChange
        }
    }
    
    /**
     * Marks a quest as complete (but not turned in).
     * Used for quests that require manual turn-in.
     * 
     * @param gameState Current game state
     * @param questId Quest to complete
     * @return CompleteQuestResult with updated state or failure reason
     */
    fun completeQuest(gameState: GameState, questId: String): CompleteQuestResult {
        val quest = questCatalog.getQuest(questId)
            ?: return CompleteQuestResult.Failure(CompleteQuestFailure.QUEST_NOT_FOUND)
        
        if (!gameState.activeQuests.contains(questId)) {
            return CompleteQuestResult.Failure(CompleteQuestFailure.NOT_ACTIVE)
        }
        
        if (!quest.isComplete()) {
            return CompleteQuestResult.Failure(CompleteQuestFailure.OBJECTIVES_INCOMPLETE)
        }
        
        val updatedGameState = completeQuestInternal(gameState, questId)
        return CompleteQuestResult.Success(updatedGameState)
    }
    
    /**
     * Internal helper to complete a quest.
     */
    private fun completeQuestInternal(gameState: GameState, questId: String): GameState {
        return gameState.copy(
            activeQuests = gameState.activeQuests - questId,
            completedQuests = gameState.completedQuests + questId
        )
    }
    
    /**
     * Turns in a completed quest, granting rewards to the player.
     * 
     * Rewards granted:
     * - XP (added to player experience)
     * - Items (added to inventory)
     * - Seeds (currency)
     * - Glimmer Shards (premium currency)
     * - Unlocked recipes (added to GameState.unlockedRecipes)
     * - Unlocked locations (added to GameState.discoveredLocations)
     * 
     * @param gameState Current game state
     * @param questId Quest to turn in
     * @return TurnInQuestResult with updated state/player and rewards
     */
    fun turnInQuest(gameState: GameState, questId: String): TurnInQuestResult {
        val quest = questCatalog.getQuest(questId)
            ?: return TurnInQuestResult.Failure(TurnInQuestFailure.QUEST_NOT_FOUND)
        
        if (!gameState.completedQuests.contains(questId)) {
            return TurnInQuestResult.Failure(TurnInQuestFailure.NOT_COMPLETED)
        }
        
        // Grant rewards
        var updatedPlayer = gameState.player
        var updatedGameState = gameState
        
        val rewards = quest.rewards
        
        // Grant XP
        if (rewards.xp > 0) {
            updatedPlayer = updatedPlayer.copy(
                experience = updatedPlayer.experience + rewards.xp
            )
        }
        
        // Grant currency
        if (rewards.seeds > 0) {
            updatedPlayer = updatedPlayer.copy(
                seeds = updatedPlayer.seeds + rewards.seeds
            )
        }
        
        if (rewards.glimmerShards > 0) {
            updatedPlayer = updatedPlayer.copy(
                glimmerShards = updatedPlayer.glimmerShards + rewards.glimmerShards
            )
        }
        
        // Grant items
        if (rewards.items.isNotEmpty()) {
            var currentInventory = updatedPlayer.inventory
            rewards.items.forEach { itemId ->
                // Add item to inventory using InventoryManager
                val (newInventory, _) = inventoryManager.addItem(currentInventory, itemId, 1)
                currentInventory = newInventory
            }
            updatedPlayer = updatedPlayer.copy(inventory = currentInventory)
        }
        
        // Unlock recipes
        if (rewards.unlockRecipeIds.isNotEmpty()) {
            updatedGameState = updatedGameState.copy(
                unlockedRecipes = updatedGameState.unlockedRecipes + rewards.unlockRecipeIds
            )
        }
        
        // Unlock locations
        if (rewards.unlockLocationIds.isNotEmpty()) {
            updatedGameState = updatedGameState.copy(
                discoveredLocations = updatedGameState.discoveredLocations + rewards.unlockLocationIds
            )
        }
        
        // Update game state with new player
        updatedGameState = updatedGameState.copy(player = updatedPlayer)
        
        return TurnInQuestResult.Success(updatedGameState, updatedPlayer, rewards)
    }
    
    /**
     * Retrieves all active quests for the player.
     */
    fun getActiveQuests(gameState: GameState): List<Quest> {
        return gameState.activeQuests.mapNotNull { questCatalog.getQuest(it) }
    }
    
    /**
     * Retrieves all completed quests for the player.
     */
    fun getCompletedQuests(gameState: GameState): List<Quest> {
        return gameState.completedQuests.mapNotNull { questCatalog.getQuest(it) }
    }
    
    /**
     * Retrieves all available quests (not active, not completed, prerequisites met).
     */
    fun getAvailableQuests(gameState: GameState): List<Quest> {
        return questCatalog.allQuests.filter { quest ->
            !gameState.activeQuests.contains(quest.id) &&
            !gameState.completedQuests.contains(quest.id) &&
            gameState.player.level >= quest.level &&
            quest.prerequisiteQuestIds.all { gameState.completedQuests.contains(it) }
        }
    }
    
    /**
     * Checks if a quest is available to accept.
     */
    fun isQuestAvailable(gameState: GameState, questId: String): Boolean {
        val quest = questCatalog.getQuest(questId) ?: return false
        
        return !gameState.activeQuests.contains(questId) &&
               !gameState.completedQuests.contains(questId) &&
               gameState.player.level >= quest.level &&
               quest.prerequisiteQuestIds.all { gameState.completedQuests.contains(it) }
    }
    
    /**
     * Gets quest progress percentage (0.0 to 1.0).
     */
    fun getQuestProgress(questId: String): Float {
        val quest = questCatalog.getQuest(questId) ?: return 0f
        return quest.progressPercentage()
    }
}
