package com.jalmarquest.shared.dialogue

import com.jalmarquest.shared.inventory.InventoryManager
import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.quest.QuestManager

/**
 * Manages dialogue conversations, conditions, and effects.
 * 
 * Follows stateless functional pattern:
 * - All methods return new GameState instances
 * - No internal mutable state
 * - Thread-safe by design
 * 
 * Integration points:
 * - QuestManager: Accept/turn-in quests via dialogue
 * - InventoryManager: Give/take items in conversations
 * - GameState.flags: Track conversation state
 * - DialogueMemory: Remember past conversations (Butterfly Effect)
 */
class DialogueManager {
    private val inventoryManager = InventoryManager
    private val questManager = QuestManager()
    
    /**
     * Result of starting or progressing dialogue.
     */
    sealed class DialogueResult {
        /**
         * Dialogue successfully started/progressed.
         * 
         * @property gameState Updated game state
         * @property currentNode Current dialogue node
         * @property availableChoices Choices player can make (filtered by conditions)
         */
        data class Success(
            val gameState: GameState,
            val currentNode: DialogueNode,
            val availableChoices: List<DialogueChoice>
        ) : DialogueResult()
        
        /**
         * Dialogue ended normally.
         * 
         * @property gameState Final game state after dialogue
         */
        data class End(val gameState: GameState) : DialogueResult()
        
        /**
         * Dialogue failed to start/progress.
         */
        data class Failure(val reason: DialogueFailure) : DialogueResult()
    }
    
    enum class DialogueFailure {
        TREE_NOT_FOUND,
        NODE_NOT_FOUND,
        CHOICE_NOT_FOUND,
        CHOICE_UNAVAILABLE,
        ALREADY_SEEN_ONCE_ONLY
    }
    
    /**
     * Starts a conversation with an NPC.
     * 
     * @param gameState Current game state
     * @param tree Dialogue tree to start
     * @return DialogueResult with first node and available choices
     */
    fun startDialogue(gameState: GameState, tree: DialogueTree): DialogueResult {
        val entryNode = tree.getNode(tree.entryNodeId)
            ?: return DialogueResult.Failure(DialogueFailure.NODE_NOT_FOUND)
        
        // Check if this is a onceOnly node that's already been seen
        if (entryNode.onceOnly && gameState.dialogueMemory.hasSeenNode(entryNode.id)) {
            return DialogueResult.Failure(DialogueFailure.ALREADY_SEEN_ONCE_ONLY)
        }
        
        // Mark node as seen and apply initial effects
        var updatedGameState = markNodeSeen(gameState, entryNode)
        updatedGameState = applyNodeEffects(updatedGameState, entryNode)
        updatedGameState = executeQuestActions(updatedGameState, entryNode.questActions)
        
        // Get available choices
        val availableChoices = getAvailableChoices(updatedGameState, entryNode)
        
        return DialogueResult.Success(updatedGameState, entryNode, availableChoices)
    }
    
    /**
     * Makes a dialogue choice and progresses to the next node.
     * 
     * @param gameState Current game state
     * @param tree Current dialogue tree
     * @param currentNodeId Current node ID
     * @param choiceId Choice player selected
     * @return DialogueResult with next node or end of conversation
     */
    fun makeChoice(
        gameState: GameState,
        tree: DialogueTree,
        currentNodeId: String,
        choiceId: String
    ): DialogueResult {
        val currentNode = tree.getNode(currentNodeId)
            ?: return DialogueResult.Failure(DialogueFailure.NODE_NOT_FOUND)
        
        val choice = currentNode.choices.find { it.id == choiceId }
            ?: return DialogueResult.Failure(DialogueFailure.CHOICE_NOT_FOUND)
        
        // Verify choice is available (conditions met)
        if (!checkConditions(gameState, choice.conditions)) {
            return DialogueResult.Failure(DialogueFailure.CHOICE_UNAVAILABLE)
        }
        
        // Record choice in memory (Butterfly Effect tracking)
        var updatedGameState = gameState.copy(
            dialogueMemory = gameState.dialogueMemory.recordChoice(currentNodeId, choiceId)
        )
        
        // Apply choice effects
        updatedGameState = applyEffects(updatedGameState, choice.effects)
        
        // Check if conversation ends
        val nextNodeId = choice.nextNodeId
        if (nextNodeId == null) {
            return DialogueResult.End(updatedGameState)
        }
        
        // Progress to next node
        val nextNode = tree.getNode(nextNodeId)
            ?: return DialogueResult.Failure(DialogueFailure.NODE_NOT_FOUND)
        
        // Check onceOnly constraint
        if (nextNode.onceOnly && updatedGameState.dialogueMemory.hasSeenNode(nextNode.id)) {
            return DialogueResult.Failure(DialogueFailure.ALREADY_SEEN_ONCE_ONLY)
        }
        
        // Mark next node as seen and apply effects
        updatedGameState = markNodeSeen(updatedGameState, nextNode)
        updatedGameState = applyNodeEffects(updatedGameState, nextNode)
        updatedGameState = executeQuestActions(updatedGameState, nextNode.questActions)
        
        // Get available choices for next node
        val availableChoices = getAvailableChoices(updatedGameState, nextNode)
        
        return DialogueResult.Success(updatedGameState, nextNode, availableChoices)
    }
    
    /**
     * Gets available choices for a node (filtered by conditions).
     * 
     * @param gameState Current game state
     * @param node Dialogue node to check
     * @return List of choices player can select
     */
    fun getAvailableChoices(gameState: GameState, node: DialogueNode): List<DialogueChoice> {
        return node.choices.filter { choice ->
            checkConditions(gameState, choice.conditions)
        }
    }
    
    /**
     * Checks if all conditions are met.
     * 
     * @param gameState Current game state
     * @param conditions List of conditions to check
     * @return True if all conditions satisfied
     */
    fun checkConditions(gameState: GameState, conditions: List<DialogueCondition>): Boolean {
        return conditions.all { condition ->
            when (condition) {
                is DialogueCondition.QuestActive -> {
                    gameState.activeQuests.contains(condition.questId)
                }
                is DialogueCondition.QuestCompleted -> {
                    gameState.completedQuests.contains(condition.questId)
                }
                is DialogueCondition.QuestNotStarted -> {
                    !gameState.activeQuests.contains(condition.questId) &&
                    !gameState.completedQuests.contains(condition.questId)
                }
                is DialogueCondition.PlayerLevel -> {
                    gameState.player.level >= condition.minLevel
                }
                is DialogueCondition.HasItem -> {
                    gameState.player.inventory.getItemQuantity(condition.itemId) >= condition.quantity
                }
                is DialogueCondition.FlagSet -> {
                    gameState.flags[condition.flagId] == condition.value
                }
                is DialogueCondition.CurrencyAmount -> {
                    gameState.player.seeds >= condition.minSeeds &&
                    gameState.player.glimmerShards >= condition.minGlimmerShards
                }
                is DialogueCondition.NodeSeen -> {
                    gameState.dialogueMemory.hasSeenNode(condition.nodeId)
                }
                is DialogueCondition.NodeNotSeen -> {
                    !gameState.dialogueMemory.hasSeenNode(condition.nodeId)
                }
            }
        }
    }
    
    /**
     * Marks a node as seen in dialogue memory.
     */
    private fun markNodeSeen(gameState: GameState, node: DialogueNode): GameState {
        return gameState.copy(
            dialogueMemory = gameState.dialogueMemory.markNodeSeen(node.id)
        )
    }
    
    /**
     * Applies effects from a node (flags).
     */
    private fun applyNodeEffects(gameState: GameState, node: DialogueNode): GameState {
        if (node.flagsSet.isEmpty()) return gameState
        
        return gameState.copy(
            flags = gameState.flags + node.flagsSet
        )
    }
    
    /**
     * Applies effects from a dialogue choice.
     * 
     * @param gameState Current game state
     * @param effects Effects to apply
     * @return Updated game state
     */
    private fun applyEffects(gameState: GameState, effects: DialogueEffects): GameState {
        if (!effects.hasEffects()) return gameState
        
        var updatedGameState = gameState
        var updatedPlayer = gameState.player
        
        // Set flags
        if (effects.setFlags.isNotEmpty()) {
            updatedGameState = updatedGameState.copy(
                flags = updatedGameState.flags + effects.setFlags
            )
        }
        
        // Give items
        if (effects.giveItems.isNotEmpty()) {
            var currentInventory = updatedPlayer.inventory
            effects.giveItems.forEach { itemId ->
                val (newInventory, _) = inventoryManager.addItem(currentInventory, itemId, 1)
                currentInventory = newInventory
            }
            updatedPlayer = updatedPlayer.copy(inventory = currentInventory)
        }
        
        // Take items
        if (effects.takeItems.isNotEmpty()) {
            var currentInventory = updatedPlayer.inventory
            effects.takeItems.forEach { (itemId, quantity) ->
                val (newInventory, _) = inventoryManager.removeItem(currentInventory, itemId, quantity)
                currentInventory = newInventory
            }
            updatedPlayer = updatedPlayer.copy(inventory = currentInventory)
        }
        
        // Give currency
        if (effects.giveCurrency.hasRewards()) {
            updatedPlayer = updatedPlayer.copy(
                seeds = updatedPlayer.seeds + effects.giveCurrency.seeds,
                glimmerShards = updatedPlayer.glimmerShards + effects.giveCurrency.glimmerShards
            )
        }
        
        // Take currency
        if (effects.takeCurrency.hasRewards()) {
            updatedPlayer = updatedPlayer.copy(
                seeds = (updatedPlayer.seeds - effects.takeCurrency.seeds).coerceAtLeast(0),
                glimmerShards = (updatedPlayer.glimmerShards - effects.takeCurrency.glimmerShards).coerceAtLeast(0)
            )
        }
        
        // Update relationship (tracked in memory)
        if (effects.relationshipChange != 0) {
            // Find NPC ID from current node (would need to pass node context)
            // For now, relationship changes are tracked in DialogueMemory
            // This will be enhanced when NPC system is implemented
        }
        
        updatedGameState = updatedGameState.copy(player = updatedPlayer)
        
        return updatedGameState
    }
    
    /**
     * Executes quest-related actions (accept, turn-in).
     * 
     * @param gameState Current game state
     * @param actions Quest actions to execute
     * @return Updated game state
     */
    private fun executeQuestActions(gameState: GameState, actions: List<QuestAction>): GameState {
        var updatedGameState = gameState
        
        actions.forEach { action ->
            when (action) {
                is QuestAction.AcceptQuest -> {
                    val result = questManager.acceptQuest(updatedGameState, action.questId)
                    if (result is QuestManager.AcceptQuestResult.Success) {
                        updatedGameState = result.gameState
                    }
                }
                is QuestAction.TurnInQuest -> {
                    val result = questManager.turnInQuest(updatedGameState, action.questId)
                    if (result is QuestManager.TurnInQuestResult.Success) {
                        updatedGameState = result.gameState
                    }
                }
            }
        }
        
        return updatedGameState
    }
}
