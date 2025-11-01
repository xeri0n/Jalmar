package com.jalmarquest.shared.gossip

import kotlinx.serialization.Serializable
import kotlin.random.Random

/**
 * Gossip & Rumor System - AI-powered information spread with telephone-game mutations.
 * 
 * Core Mechanics:
 * - Rumors start from player actions or NPC observations
 * - NPCs spread rumors based on relationships, proximity, and personality
 * - Each spread has mutation chance (telephone game effect)
 * - Truth levels: ACCURATE → EXAGGERATED → DISTORTED → MYTHICAL
 * - Reputation effects ripple through factions
 * 
 * Design Philosophy:
 * - Emergent storytelling through organic information flow
 * - Player actions become legends (or infamy)
 * - Butterfly Effect integration for long-term consequences
 * - Quail-scale authenticity (gossip about tiny heroics)
 */

/**
 * A rumor circulating among NPCs.
 * Tracks original truth, current mutated version, and spread history.
 */
@Serializable
data class Rumor(
    val id: String,                                 // Unique UUID: "rumor_12345678"
    val rumorKey: String,                           // Template ID: "rumor_defeated_ant_colony"
    val category: RumorCategory,                    // HEROIC_DEED, CRIME, FAILURE, etc.
    val originalText: String,                       // Initial accurate version
    val currentText: String,                        // Mutated version (may differ greatly)
    val truthLevel: TruthLevel,                     // ACCURATE, EXAGGERATED, DISTORTED, MYTHICAL
    val mutationCount: Int = 0,                     // Number of mutations applied
    val subjectId: String,                          // Who rumor is about (player ID, NPC ID)
    val originNPCId: String,                        // Who started the rumor
    val knownByNPCs: Set<String> = emptySet(),      // NPCs who have heard this rumor
    val sourceMap: Map<String, String> = emptyMap(), // npcId → who told them
    val reputationEffects: List<ReputationEffect> = emptyList(), // Faction standing changes
    val timestamp: Long,                            // When rumor started (game ticks)
    val metadata: Map<String, String> = emptyMap()  // Additional context (location, details)
) {
    init {
        require(id.isNotBlank()) { "Rumor ID cannot be blank" }
        require(rumorKey.isNotBlank()) { "Rumor key cannot be blank" }
        require(originalText.isNotBlank()) { "Original text cannot be blank" }
        require(currentText.isNotBlank()) { "Current text cannot be blank" }
        require(mutationCount >= 0) { "Mutation count cannot be negative" }
        require(timestamp >= 0) { "Timestamp cannot be negative" }
    }
    
    /**
     * Calculate truth level based on mutation count.
     * 0-1: ACCURATE, 2-3: EXAGGERATED, 4-5: DISTORTED, 6+: MYTHICAL
     */
    fun calculateTruthLevel(): TruthLevel {
        return when (mutationCount) {
            in 0..1 -> TruthLevel.ACCURATE
            in 2..3 -> TruthLevel.EXAGGERATED
            in 4..5 -> TruthLevel.DISTORTED
            else -> TruthLevel.MYTHICAL
        }
    }
    
    /**
     * Check if rumor has reached specific NPC.
     */
    fun isKnownBy(npcId: String): Boolean = npcId in knownByNPCs
    
    /**
     * Get who told this rumor to specific NPC.
     */
    fun getSource(npcId: String): String? = sourceMap[npcId]
}

/**
 * Categories of rumors for different types of information.
 */
@Serializable
enum class RumorCategory {
    HEROIC_DEED,    // Player defeated dangerous enemy, saved NPC, completed quest
    CRIME,          // Player stole, attacked NPC, failed to help
    FAILURE,        // Player fled combat, got lost, embarrassing moment
    NPC_GOSSIP,     // NPC relationships, drama, scandals
    WORLD_EVENT,    // Weather disasters, mysterious phenomena
    DISCOVERY       // Player found location, item, secret
}

/**
 * Truth levels indicating how much rumor has mutated.
 */
@Serializable
enum class TruthLevel {
    ACCURATE,       // 0-1 mutations: Close to original truth
    EXAGGERATED,    // 2-3 mutations: Numbers inflated, details enhanced
    DISTORTED,      // 4-5 mutations: Facts changed, major alterations
    MYTHICAL        // 6+ mutations: Supernatural, legendary status
}

/**
 * A mutation applied to a rumor during spreading.
 */
@Serializable
data class RumorMutation(
    val type: MutationType,                 // EXAGGERATE, EMBELLISH, DISTORT, MYTHOLOGIZE
    val description: String,                // Human-readable: "Enemy count increased from 5 to 20"
    val parameter: String? = null,          // Which parameter changed: "enemyCount"
    val oldValue: String? = null,           // Before mutation: "5"
    val newValue: String? = null            // After mutation: "20"
) {
    init {
        require(description.isNotBlank()) { "Mutation description cannot be blank" }
    }
}

/**
 * Types of mutations that can occur during rumor spreading.
 */
@Serializable
enum class MutationType {
    EXAGGERATE,     // Numbers increase (defeated 5 ants → defeated 20 ants)
    EMBELLISH,      // Add details (defeated ants → defeated ants single-handedly)
    DISTORT,        // Change facts (defeated ants → defeated beetle army)
    MYTHOLOGIZE     // Add supernatural (defeated ants → slew dragon-sized ant queen)
}

/**
 * Reputation change applied when rumor spreads to faction members.
 */
@Serializable
data class ReputationEffect(
    val factionId: String,                  // Which faction affected: "buttonburgh_citizens"
    val reputationChange: Int,              // ±reputation points
    val reason: String                      // "Heard rumor of heroic deed"
) {
    init {
        require(factionId.isNotBlank()) { "Faction ID cannot be blank" }
        require(reason.isNotBlank()) { "Reason cannot be blank" }
    }
}

/**
 * Template for generating rumors from player actions.
 */
@Serializable
data class RumorTemplate(
    val templateId: String,                         // "rumor_defeated_enemy"
    val category: RumorCategory,                    // HEROIC_DEED
    val baseText: String,                           // "{playerName} defeated {enemyCount} {enemyType}"
    val mutationPaths: List<MutationPath>,          // How rumor can mutate
    val reputationEffects: List<ReputationEffect>,  // Base reputation changes
    val spreadProbability: Double = 0.5,            // Base chance to spread (0.0-1.0)
    val mutationChance: Double = 0.2                // Chance to mutate per spread (0.0-1.0)
) {
    init {
        require(templateId.isNotBlank()) { "Template ID cannot be blank" }
        require(baseText.isNotBlank()) { "Base text cannot be blank" }
        require(spreadProbability in 0.0..1.0) { "Spread probability must be 0.0-1.0" }
        require(mutationChance in 0.0..1.0) { "Mutation chance must be 0.0-1.0" }
    }
}

/**
 * Defines how a rumor can mutate at different truth levels.
 */
@Serializable
data class MutationPath(
    val truthLevel: TruthLevel,             // When this mutation applies
    val type: MutationType,                 // Type of mutation
    val textTransform: String,              // New text pattern
    val parameterChanges: Map<String, String> = emptyMap() // parameter → transformation rule
)

/**
 * Gossip system state tracking all active rumors and NPC knowledge.
 */
@Serializable
data class GossipState(
    val activeRumors: Map<String, Rumor> = emptyMap(),              // rumorId → Rumor
    val npcGossipMemory: Map<String, List<String>> = emptyMap(),    // npcId → List<rumorId>
    val spreadCooldowns: Map<String, Long> = emptyMap(),            // "rumorId_npcId" → expiry timestamp
    val reputationHistory: List<ReputationEffect> = emptyList(),    // All reputation changes from gossip
    val rumorStatistics: Map<String, RumorStatistics> = emptyMap()  // rumorId → statistics
) {
    /**
     * Get all rumors known by specific NPC.
     */
    fun getRumorsKnownBy(npcId: String): List<Rumor> {
        val rumorIds = npcGossipMemory[npcId] ?: return emptyList()
        return rumorIds.mapNotNull { activeRumors[it] }
    }
    
    /**
     * Check if NPC knows a specific rumor.
     */
    fun npcKnowsRumor(npcId: String, rumorId: String): Boolean {
        return rumorId in (npcGossipMemory[npcId] ?: emptyList())
    }
    
    /**
     * Check if spread is on cooldown for specific NPC.
     */
    fun isSpreadOnCooldown(rumorId: String, npcId: String, currentTimestamp: Long): Boolean {
        val cooldownKey = "${rumorId}_${npcId}"
        val expiryTime = spreadCooldowns[cooldownKey] ?: return false
        return currentTimestamp < expiryTime
    }
    
    /**
     * Get total reputation changes for specific faction.
     */
    fun getTotalReputationChange(factionId: String): Int {
        return reputationHistory
            .filter { it.factionId == factionId }
            .sumOf { it.reputationChange }
    }
}

/**
 * Statistics tracking for individual rumor spread.
 */
@Serializable
data class RumorStatistics(
    val rumorId: String,                    // Which rumor
    val timesSpread: Int = 0,               // How many times shared
    val totalReach: Int = 0,                // Total NPCs who heard it
    val mutationHistory: List<RumorMutation> = emptyList(), // All mutations applied
    val averageTruthLevel: Double = 1.0     // Average truth level (1.0-4.0)
)

/**
 * Result of starting a new rumor.
 */
sealed class StartRumorResult {
    data class Success(
        val state: GossipState,
        val rumorId: String
    ) : StartRumorResult()
    
    data class Failure(val reason: StartRumorFailure) : StartRumorResult()
}

enum class StartRumorFailure {
    INVALID_TEMPLATE,
    INVALID_SUBJECT,
    INVALID_ORIGIN_NPC
}

/**
 * Result of spreading a rumor to NPCs.
 */
sealed class SpreadRumorResult {
    data class Success(
        val state: GossipState,
        val spreadToNPCs: List<String>,         // NPCs who received rumor
        val mutations: List<RumorMutation>       // Mutations that occurred
    ) : SpreadRumorResult()
    
    data class Failure(val reason: SpreadFailure) : SpreadRumorResult()
}

enum class SpreadFailure {
    RUMOR_NOT_FOUND,
    SOURCE_NPC_DOESNT_KNOW_RUMOR,
    NO_VALID_TARGETS,
    ALL_ON_COOLDOWN,
    SPREAD_PROBABILITY_FAILED
}

/**
 * Context validation result for rumor spreading.
 */
sealed class SpreadValidationResult {
    object Valid : SpreadValidationResult()
    data class Invalid(val reason: String) : SpreadValidationResult()
}
