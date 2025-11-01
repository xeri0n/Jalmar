package com.jalmarquest.model

import kotlinx.serialization.Serializable

/**
 * Represents a single node in a dialogue tree.
 * Each node contains speaker text and up to 4 player choices.
 */
@Serializable
data class DialogueNode(
    val id: String,
    val speakerName: String,
    val text: String,
    val choices: List<DialogueChoice> = emptyList(),
    val isEndNode: Boolean = false,
    val consequences: List<DialogueConsequence> = emptyList(),
    val conditions: List<DialogueCondition> = emptyList()
) {
    init {
        require(id.isNotBlank()) { "DialogueNode id cannot be blank" }
        require(speakerName.isNotBlank()) { "DialogueNode speakerName cannot be blank" }
        require(text.isNotBlank()) { "DialogueNode text cannot be blank" }
        require(choices.size <= 4) { "DialogueNode cannot have more than 4 choices" }
        if (isEndNode) {
            require(choices.isEmpty()) { "End nodes cannot have choices" }
        }
    }
}

/**
 * Represents a player dialogue choice.
 * Can lead to another node or trigger consequences.
 */
@Serializable
data class DialogueChoice(
    val text: String,
    val nextNodeId: String?,
    val requirements: List<DialogueCondition> = emptyList(),
    val consequences: List<DialogueConsequence> = emptyList(),
    val isHidden: Boolean = false
) {
    init {
        require(text.isNotBlank()) { "DialogueChoice text cannot be blank" }
    }
}

/**
 * Conditions that must be met for a choice to appear or node to be accessible.
 */
@Serializable
sealed class DialogueCondition {
    @Serializable
    data class QuestStatus(val questId: String, val status: String) : DialogueCondition()
    
    @Serializable
    data class MinLevel(val level: Int) : DialogueCondition()
    
    @Serializable
    data class StatRequirement(val statName: String, val minValue: Int) : DialogueCondition()
    
    @Serializable
    data class PreviousDialogue(val dialogueTreeId: String, val nodeId: String) : DialogueCondition()
    
    @Serializable
    data class HasCompletedQuest(val questId: String) : DialogueCondition()
    
    @Serializable
    data class RelationshipLevel(val npcId: String, val minLevel: Int) : DialogueCondition()
}

/**
 * Consequences that occur when a choice is selected or node is visited.
 */
@Serializable
sealed class DialogueConsequence {
    @Serializable
    data class StartQuest(val questId: String) : DialogueConsequence()
    
    @Serializable
    data class ProgressQuest(val questId: String, val objectiveId: String) : DialogueConsequence()
    
    @Serializable
    data class CompleteQuest(val questId: String) : DialogueConsequence()
    
    @Serializable
    data class ModifyRelationship(val npcId: String, val deltaPoints: Int) : DialogueConsequence()
    
    @Serializable
    data class GrantXP(val amount: Int) : DialogueConsequence()
    
    @Serializable
    data class SetFlag(val flagKey: String, val flagValue: String) : DialogueConsequence()
    
    @Serializable
    data class UnlockDialogue(val dialogueTreeId: String) : DialogueConsequence()
}

/**
 * Complete dialogue tree with all nodes.
 */
@Serializable
data class DialogueTree(
    val id: String,
    val npcId: String,
    val rootNodeId: String,
    val nodes: Map<String, DialogueNode>,
    val requiredConditions: List<DialogueCondition> = emptyList(),
    val isRepeatable: Boolean = false
) {
    init {
        require(id.isNotBlank()) { "DialogueTree id cannot be blank" }
        require(npcId.isNotBlank()) { "DialogueTree npcId cannot be blank" }
        require(rootNodeId.isNotBlank()) { "DialogueTree rootNodeId cannot be blank" }
        require(nodes.containsKey(rootNodeId)) { "Root node must exist in nodes map" }
        
        // Validate all choice nextNodeIds exist
        nodes.values.forEach { node ->
            node.choices.forEach { choice ->
                if (choice.nextNodeId != null) {
                    require(nodes.containsKey(choice.nextNodeId)) {
                        "Choice nextNodeId '${choice.nextNodeId}' must exist in nodes map"
                    }
                }
            }
        }
    }
}

/**
 * Tracks player's dialogue history for memory and condition checking.
 */
@Serializable
data class DialogueHistory(
    val visitedNodes: Map<String, List<String>> = emptyMap(), // dialogueTreeId -> list of visited nodeIds
    val completedTrees: List<String> = emptyList(),
    val dialogueFlags: Map<String, String> = emptyMap() // Custom flags set by consequences
) {
    fun hasVisitedNode(treeId: String, nodeId: String): Boolean {
        return visitedNodes[treeId]?.contains(nodeId) ?: false
    }
    
    fun hasCompletedTree(treeId: String): Boolean {
        return completedTrees.contains(treeId)
    }
    
    fun getFlag(key: String): String? {
        return dialogueFlags[key]
    }
}
