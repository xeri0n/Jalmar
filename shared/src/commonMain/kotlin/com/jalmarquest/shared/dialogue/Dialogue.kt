package com.jalmarquest.shared.dialogue

import kotlinx.serialization.Serializable

/**
 * A single node in a dialogue tree representing an NPC's speech.
 * 
 * @property id Unique identifier for this node
 * @property npcId NPC who is speaking
 * @property text What the NPC says
 * @property choices Available player response options
 * @property questActions Quest-related actions (accept, turn-in) triggered by this node
 * @property flagsSet Game flags to set when this node is displayed
 * @property onceOnly If true, this node can only be seen once (tracked in DialogueMemory)
 */
@Serializable
data class DialogueNode(
    val id: String,
    val npcId: String,
    val text: String,
    val choices: List<DialogueChoice> = emptyList(),
    val questActions: List<QuestAction> = emptyList(),
    val flagsSet: Map<String, Boolean> = emptyMap(),
    val onceOnly: Boolean = false
) {
    init {
        require(id.isNotBlank()) { "DialogueNode ID cannot be blank" }
        require(npcId.isNotBlank()) { "DialogueNode must have an NPC ID" }
        require(text.isNotBlank()) { "DialogueNode text cannot be blank" }
    }
}

/**
 * A player choice in dialogue with conditions and effects.
 * 
 * @property id Unique identifier for this choice
 * @property text What the player says
 * @property nextNodeId Next dialogue node ID (null ends conversation)
 * @property conditions Requirements to display this choice
 * @property effects Side effects when this choice is selected
 * @property hidden If true, choice is completely hidden when conditions not met (vs greyed out)
 */
@Serializable
data class DialogueChoice(
    val id: String,
    val text: String,
    val nextNodeId: String? = null,
    val conditions: List<DialogueCondition> = emptyList(),
    val effects: DialogueEffects = DialogueEffects(),
    val hidden: Boolean = true
) {
    init {
        require(id.isNotBlank()) { "DialogueChoice ID cannot be blank" }
        require(text.isNotBlank()) { "DialogueChoice text cannot be blank" }
    }
}

/**
 * Condition that must be met to display a dialogue choice or node.
 */
@Serializable
sealed class DialogueCondition {
    /**
     * Quest must be active.
     */
    @Serializable
    data class QuestActive(val questId: String) : DialogueCondition()
    
    /**
     * Quest must be completed (and turned in).
     */
    @Serializable
    data class QuestCompleted(val questId: String) : DialogueCondition()
    
    /**
     * Quest must not be started yet.
     */
    @Serializable
    data class QuestNotStarted(val questId: String) : DialogueCondition()
    
    /**
     * Player must be at or above minimum level.
     */
    @Serializable
    data class PlayerLevel(val minLevel: Int) : DialogueCondition() {
        init {
            require(minLevel in 1..50) { "Player level must be 1-50" }
        }
    }
    
    /**
     * Player must have item in inventory.
     */
    @Serializable
    data class HasItem(val itemId: String, val quantity: Int = 1) : DialogueCondition() {
        init {
            require(quantity > 0) { "Item quantity must be positive" }
        }
    }
    
    /**
     * Game flag must be set to specified value.
     */
    @Serializable
    data class FlagSet(val flagId: String, val value: Boolean = true) : DialogueCondition()
    
    /**
     * Player must have minimum currency.
     */
    @Serializable
    data class CurrencyAmount(val minSeeds: Long = 0, val minGlimmerShards: Long = 0) : DialogueCondition() {
        init {
            require(minSeeds >= 0) { "Seed amount cannot be negative" }
            require(minGlimmerShards >= 0) { "Glimmer Shard amount cannot be negative" }
        }
    }
    
    /**
     * Player must have seen specific dialogue node.
     */
    @Serializable
    data class NodeSeen(val nodeId: String) : DialogueCondition()
    
    /**
     * Player must NOT have seen specific dialogue node.
     */
    @Serializable
    data class NodeNotSeen(val nodeId: String) : DialogueCondition()
}

/**
 * Effects applied when a dialogue choice is selected.
 * 
 * @property setFlags Game flags to set (tracked in GameState.flags)
 * @property giveItems Item IDs to add to player inventory
 * @property takeItems Item IDs to remove from inventory (with quantities)
 * @property giveCurrency Currency to grant (seeds, glimmerShards)
 * @property takeCurrency Currency to deduct
 * @property relationshipChange Change to NPC relationship score (future feature)
 */
@Serializable
data class DialogueEffects(
    val setFlags: Map<String, Boolean> = emptyMap(),
    val giveItems: List<String> = emptyList(),
    val takeItems: Map<String, Int> = emptyMap(),
    val giveCurrency: CurrencyReward = CurrencyReward(),
    val takeCurrency: CurrencyReward = CurrencyReward(),
    val relationshipChange: Int = 0
) {
    /**
     * Returns true if this effects object has any effects.
     */
    fun hasEffects(): Boolean {
        return setFlags.isNotEmpty() ||
               giveItems.isNotEmpty() ||
               takeItems.isNotEmpty() ||
               giveCurrency.hasRewards() ||
               takeCurrency.hasRewards() ||
               relationshipChange != 0
    }
}

/**
 * Currency reward/cost.
 */
@Serializable
data class CurrencyReward(
    val seeds: Long = 0,
    val glimmerShards: Long = 0
) {
    init {
        require(seeds >= 0) { "Seeds cannot be negative" }
        require(glimmerShards >= 0) { "Glimmer Shards cannot be negative" }
    }
    
    fun hasRewards(): Boolean = seeds > 0 || glimmerShards > 0
}

/**
 * Quest-related action triggered during dialogue.
 */
@Serializable
sealed class QuestAction {
    /**
     * Accept a quest.
     */
    @Serializable
    data class AcceptQuest(val questId: String) : QuestAction()
    
    /**
     * Turn in a completed quest.
     */
    @Serializable
    data class TurnInQuest(val questId: String) : QuestAction()
}

/**
 * A complete dialogue tree for an NPC or interaction.
 * 
 * @property id Unique tree identifier
 * @property npcId NPC this tree belongs to
 * @property name Human-readable tree name
 * @property description Tree description (for debugging/catalog)
 * @property entryNodeId Starting node ID
 * @property nodes All nodes in this tree
 */
@Serializable
data class DialogueTree(
    val id: String,
    val npcId: String,
    val name: String,
    val description: String = "",
    val entryNodeId: String,
    val nodes: Map<String, DialogueNode>
) {
    init {
        require(id.isNotBlank()) { "DialogueTree ID cannot be blank" }
        require(npcId.isNotBlank()) { "DialogueTree must have an NPC ID" }
        require(name.isNotBlank()) { "DialogueTree name cannot be blank" }
        require(entryNodeId.isNotBlank()) { "DialogueTree must have an entry node ID" }
        require(nodes.isNotEmpty()) { "DialogueTree must have at least one node" }
        require(nodes.containsKey(entryNodeId)) { "Entry node '$entryNodeId' not found in tree" }
    }
    
    /**
     * Retrieves a node by ID.
     */
    fun getNode(nodeId: String): DialogueNode? = nodes[nodeId]
    
    /**
     * Validates that all nextNodeIds reference valid nodes.
     * Throws IllegalStateException if invalid references found.
     */
    fun validate() {
        nodes.values.forEach { node ->
            node.choices.forEach { choice ->
                choice.nextNodeId?.let { nextId ->
                    if (!nodes.containsKey(nextId)) {
                        throw IllegalStateException(
                            "Node '${node.id}' choice '${choice.id}' references invalid node '$nextId'"
                        )
                    }
                }
            }
        }
    }
}

/**
 * Player's dialogue history and memory.
 * Tracks what nodes have been seen and what choices were made.
 * 
 * @property seenNodes Set of node IDs the player has encountered
 * @property choicesMade Map of nodeId -> choiceId tracking player decisions
 * @property npcRelationships Map of npcId -> relationship score (future feature)
 */
@Serializable
data class DialogueMemory(
    val seenNodes: Set<String> = emptySet(),
    val choicesMade: Map<String, String> = emptyMap(),
    val npcRelationships: Map<String, Int> = emptyMap()
) {
    /**
     * Returns true if player has seen a specific node.
     */
    fun hasSeenNode(nodeId: String): Boolean = seenNodes.contains(nodeId)
    
    /**
     * Returns the choice made at a specific node, or null if not visited.
     */
    fun getChoiceMade(nodeId: String): String? = choicesMade[nodeId]
    
    /**
     * Records that a node was seen.
     */
    fun markNodeSeen(nodeId: String): DialogueMemory {
        return copy(seenNodes = seenNodes + nodeId)
    }
    
    /**
     * Records a choice made at a node.
     */
    fun recordChoice(nodeId: String, choiceId: String): DialogueMemory {
        return copy(choicesMade = choicesMade + (nodeId to choiceId))
    }
    
    /**
     * Updates NPC relationship score.
     */
    fun updateRelationship(npcId: String, change: Int): DialogueMemory {
        val currentScore = npcRelationships[npcId] ?: 0
        return copy(npcRelationships = npcRelationships + (npcId to currentScore + change))
    }
}
