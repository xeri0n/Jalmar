package com.jalmarquest.shared.gossip

import com.jalmarquest.shared.model.GameState
import kotlin.random.Random

/**
 * Stateless manager for gossip and rumor spreading.
 * 
 * Core Operations:
 * - Start new rumors from player actions or NPC observations
 * - Spread rumors between NPCs with mutation chance
 * - Calculate spread probability based on NPC relationships
 * - Apply reputation effects when rumors reach faction members
 * - Track rumor statistics and spread history
 * 
 * All state changes return new GossipState instances (immutability).
 */
object GossipManager {
    
    // ============================================
    // RUMOR CREATION
    // ============================================
    
    /**
     * Start a new rumor based on player action or event.
     * 
     * @param state Current gossip state
     * @param rumorKey Template identifier
     * @param subjectId Who the rumor is about (player ID, NPC ID)
     * @param originNPCId NPC who started the rumor
     * @param originalText Initial accurate rumor text
     * @param category Rumor category
     * @param reputationEffects Base reputation changes
     * @param currentTimestamp Current game time in ticks
     * @param metadata Additional context (location, details)
     * @return StartRumorResult with updated state and rumor ID
     */
    fun startRumor(
        state: GossipState,
        rumorKey: String,
        subjectId: String,
        originNPCId: String,
        originalText: String,
        category: RumorCategory,
        reputationEffects: List<ReputationEffect> = emptyList(),
        currentTimestamp: Long,
        metadata: Map<String, String> = emptyMap()
    ): StartRumorResult {
        // Validation
        if (rumorKey.isBlank()) return StartRumorResult.Failure(StartRumorFailure.INVALID_TEMPLATE)
        if (subjectId.isBlank()) return StartRumorResult.Failure(StartRumorFailure.INVALID_SUBJECT)
        if (originNPCId.isBlank()) return StartRumorResult.Failure(StartRumorFailure.INVALID_ORIGIN_NPC)
        
        // Generate unique rumor ID
        val rumorId = "rumor_${currentTimestamp}_${Random.nextInt(1000, 9999)}"
        
        // Create rumor
        val rumor = Rumor(
            id = rumorId,
            rumorKey = rumorKey,
            category = category,
            originalText = originalText,
            currentText = originalText,
            truthLevel = TruthLevel.ACCURATE,
            mutationCount = 0,
            subjectId = subjectId,
            originNPCId = originNPCId,
            knownByNPCs = setOf(originNPCId),
            sourceMap = mapOf(originNPCId to "origin"),
            reputationEffects = reputationEffects,
            timestamp = currentTimestamp,
            metadata = metadata
        )
        
        // Update state
        val newActiveRumors = state.activeRumors + (rumorId to rumor)
        val newNpcMemory = state.npcGossipMemory.toMutableMap()
        newNpcMemory[originNPCId] = (newNpcMemory[originNPCId] ?: emptyList()) + rumorId
        
        val newStatistics = state.rumorStatistics + (rumorId to RumorStatistics(
            rumorId = rumorId,
            timesSpread = 0,
            totalReach = 1,
            mutationHistory = emptyList(),
            averageTruthLevel = 1.0
        ))
        
        val newState = state.copy(
            activeRumors = newActiveRumors,
            npcGossipMemory = newNpcMemory,
            rumorStatistics = newStatistics
        )
        
        return StartRumorResult.Success(newState, rumorId)
    }
    
    // ============================================
    // RUMOR SPREADING
    // ============================================
    
    /**
     * Spread rumor from source NPC to target NPCs.
     * 
     * Each target has independent spread probability and mutation chance.
     * Cooldowns prevent spam. Reputation effects applied to faction members.
     * 
     * @param state Current gossip state
     * @param rumorId Rumor to spread
     * @param sourceNPCId NPC sharing the rumor
     * @param targetNPCIds Potential recipients
     * @param gameState Current game state (for relationship lookups)
     * @param currentTimestamp Current game time in ticks
     * @param cooldownTicks How long before same NPC can receive again
     * @return SpreadRumorResult with updated state, spread targets, and mutations
     */
    fun spreadRumor(
        state: GossipState,
        rumorId: String,
        sourceNPCId: String,
        targetNPCIds: List<String>,
        gameState: GameState,
        currentTimestamp: Long,
        cooldownTicks: Long = 7200 // Default: 1 day
    ): SpreadRumorResult {
        // Get rumor
        val rumor = state.activeRumors[rumorId]
            ?: return SpreadRumorResult.Failure(SpreadFailure.RUMOR_NOT_FOUND)
        
        // Verify source knows rumor
        if (!rumor.isKnownBy(sourceNPCId)) {
            return SpreadRumorResult.Failure(SpreadFailure.SOURCE_NPC_DOESNT_KNOW_RUMOR)
        }
        
        // Filter valid targets (not on cooldown, don't already know)
        val validTargets = targetNPCIds.filter { targetId ->
            !rumor.isKnownBy(targetId) &&
            !state.isSpreadOnCooldown(rumorId, targetId, currentTimestamp)
        }
        
        if (validTargets.isEmpty()) {
            return SpreadRumorResult.Failure(SpreadFailure.NO_VALID_TARGETS)
        }
        
        // Spread to each target with probability check
        val spreadTo = mutableListOf<String>()
        val mutations = mutableListOf<RumorMutation>()
        var currentRumor = rumor
        var currentState = state
        
        for (targetId in validTargets) {
            val spreadProb = calculateSpreadProbability(sourceNPCId, targetId, gameState)
            
            if (Random.nextDouble() < spreadProb) {
                // Attempt mutation
                val mutationResult = attemptMutation(currentRumor)
                currentRumor = mutationResult.first
                mutationResult.second?.let { mutations.add(it) }
                
                // Add target to known list
                spreadTo.add(targetId)
            }
        }
        
        if (spreadTo.isEmpty()) {
            return SpreadRumorResult.Failure(SpreadFailure.SPREAD_PROBABILITY_FAILED)
        }
        
        // Update rumor with new knowledge
        val updatedRumor = currentRumor.copy(
            knownByNPCs = currentRumor.knownByNPCs + spreadTo,
            sourceMap = currentRumor.sourceMap + spreadTo.associateWith { sourceNPCId },
            truthLevel = currentRumor.calculateTruthLevel()
        )
        
        // Update state
        val newActiveRumors = currentState.activeRumors + (rumorId to updatedRumor)
        val newNpcMemory = currentState.npcGossipMemory.toMutableMap()
        for (targetId in spreadTo) {
            newNpcMemory[targetId] = (newNpcMemory[targetId] ?: emptyList()) + rumorId
        }
        
        // Set cooldowns
        val newCooldowns = currentState.spreadCooldowns.toMutableMap()
        for (targetId in spreadTo) {
            newCooldowns["${rumorId}_${targetId}"] = currentTimestamp + cooldownTicks
        }
        
        // Update statistics
        val stats = currentState.rumorStatistics[rumorId] ?: RumorStatistics(rumorId)
        val newStats = stats.copy(
            timesSpread = stats.timesSpread + spreadTo.size,
            totalReach = updatedRumor.knownByNPCs.size,
            mutationHistory = stats.mutationHistory + mutations,
            averageTruthLevel = calculateAverageTruthLevel(stats.mutationHistory + mutations)
        )
        
        // Apply reputation effects to faction members
        val newReputationHistory = if (updatedRumor.reputationEffects.isNotEmpty()) {
            applyReputationEffects(currentState, updatedRumor, spreadTo)
        } else {
            currentState.reputationHistory
        }
        
        val finalState = currentState.copy(
            activeRumors = newActiveRumors,
            npcGossipMemory = newNpcMemory,
            spreadCooldowns = newCooldowns,
            rumorStatistics = currentState.rumorStatistics + (rumorId to newStats),
            reputationHistory = newReputationHistory
        )
        
        return SpreadRumorResult.Success(finalState, spreadTo, mutations)
    }
    
    // ============================================
    // MUTATION LOGIC
    // ============================================
    
    /**
     * Attempt to mutate rumor with configured chance.
     * 
     * Mutation types progress: EXAGGERATE → EMBELLISH → DISTORT → MYTHOLOGIZE
     * Each mutation increments count and may change text.
     * 
     * @param rumor Current rumor
     * @param mutationChance Probability of mutation (default 20%)
     * @return Pair of (updated rumor, mutation applied or null)
     */
    private fun attemptMutation(
        rumor: Rumor,
        mutationChance: Double = 0.2
    ): Pair<Rumor, RumorMutation?> {
        if (Random.nextDouble() >= mutationChance) {
            return rumor to null
        }
        
        // Select mutation type based on current mutation count
        val mutationType = when (rumor.mutationCount) {
            in 0..1 -> MutationType.EXAGGERATE
            in 2..3 -> MutationType.EMBELLISH
            in 4..5 -> MutationType.DISTORT
            else -> MutationType.MYTHOLOGIZE
        }
        
        // Apply mutation (simplified - actual implementation would use templates)
        val mutation = RumorMutation(
            type = mutationType,
            description = "Rumor mutated via ${mutationType.name.lowercase()}",
            parameter = null,
            oldValue = null,
            newValue = null
        )
        
        val mutatedRumor = rumor.copy(
            currentText = applyMutationToText(rumor.currentText, mutationType),
            mutationCount = rumor.mutationCount + 1
        )
        
        return mutatedRumor to mutation
    }
    
    /**
     * Apply mutation transformation to rumor text.
     * 
     * This is a simplified version - actual implementation would use
     * RumorTemplate mutation paths for precise transformations.
     */
    private fun applyMutationToText(text: String, mutationType: MutationType): String {
        return when (mutationType) {
            MutationType.EXAGGERATE -> text.replace(Regex("\\d+")) { match ->
                (match.value.toIntOrNull()?.times(2) ?: match.value).toString()
            }
            MutationType.EMBELLISH -> "$text (single-handedly!)"
            MutationType.DISTORT -> text.replace("ant", "beetle")
                .replace("Ant", "Beetle")
            MutationType.MYTHOLOGIZE -> text.replace("defeated", "slew the legendary")
                .replace("Defeated", "Slew the legendary")
        }
    }
    
    // ============================================
    // SPREAD PROBABILITY
    // ============================================
    
    /**
     * Calculate probability of rumor spreading from source to target.
     * 
     * Factors:
     * - Base probability (50%)
     * - Relationship strength (placeholder - would query NPC system)
     * - Proximity (placeholder - would check location distance)
     * - Faction alignment (placeholder - would check faction standings)
     * 
     * @param sourceNPCId NPC sharing rumor
     * @param targetNPCId NPC receiving rumor
     * @param gameState Current game state
     * @return Probability 0.0-1.0
     */
    fun calculateSpreadProbability(
        sourceNPCId: String,
        targetNPCId: String,
        gameState: GameState
    ): Double {
        var probability = 0.5 // Base 50% chance
        
        // Relationship multiplier (placeholder - would query actual NPC relationships)
        // For now, use simple logic: same faction = higher chance
        val relationshipMultiplier = 1.0 // TODO: Query NPC relationship system
        
        // Proximity multiplier (placeholder - would check if NPCs are in same location)
        val proximityMultiplier = 1.0 // TODO: Check location proximity
        
        // Faction multiplier (placeholder - would check faction alignment)
        val factionMultiplier = 1.0 // TODO: Query faction standings
        
        probability *= relationshipMultiplier * proximityMultiplier * factionMultiplier
        
        return probability.coerceIn(0.0, 1.0)
    }
    
    // ============================================
    // REPUTATION EFFECTS
    // ============================================
    
    /**
     * Apply reputation effects when rumor reaches new NPCs.
     * 
     * Checks if target NPCs are faction members and applies
     * appropriate reputation changes.
     * 
     * @param state Current gossip state
     * @param rumor Rumor being spread
     * @param newNPCs NPCs who just learned the rumor
     * @return Updated reputation history
     */
    private fun applyReputationEffects(
        state: GossipState,
        rumor: Rumor,
        newNPCs: List<String>
    ): List<ReputationEffect> {
        val newEffects = mutableListOf<ReputationEffect>()
        
        for (effect in rumor.reputationEffects) {
            // Check if any new NPCs are faction members (placeholder)
            // TODO: Query NPC faction membership
            val factionMembersReached = newNPCs.filter { npcId ->
                // Placeholder: Assume all NPCs are in "buttonburgh_citizens" faction
                effect.factionId == "buttonburgh_citizens"
            }
            
            if (factionMembersReached.isNotEmpty()) {
                newEffects.add(effect.copy(
                    reason = "${effect.reason} (reached ${factionMembersReached.size} faction members)"
                ))
            }
        }
        
        return state.reputationHistory + newEffects
    }
    
    /**
     * Calculate average truth level from mutation history.
     */
    private fun calculateAverageTruthLevel(mutations: List<RumorMutation>): Double {
        if (mutations.isEmpty()) return 1.0
        
        val truthValues = mutations.mapIndexed { index, _ ->
            when {
                index < 2 -> 1.0  // ACCURATE
                index < 4 -> 2.0  // EXAGGERATED
                index < 6 -> 3.0  // DISTORTED
                else -> 4.0       // MYTHICAL
            }
        }
        
        return truthValues.average()
    }
    
    // ============================================
    // QUERY OPERATIONS
    // ============================================
    
    /**
     * Get all rumors known by specific NPC.
     */
    fun getRumorsKnownBy(state: GossipState, npcId: String): List<Rumor> {
        return state.getRumorsKnownBy(npcId)
    }
    
    /**
     * Get rumors by category.
     */
    fun getRumorsByCategory(state: GossipState, category: RumorCategory): List<Rumor> {
        return state.activeRumors.values.filter { it.category == category }
    }
    
    /**
     * Get rumors by truth level.
     */
    fun getRumorsByTruthLevel(state: GossipState, truthLevel: TruthLevel): List<Rumor> {
        return state.activeRumors.values.filter { it.truthLevel == truthLevel }
    }
    
    /**
     * Get rumor statistics.
     */
    fun getRumorStatistics(state: GossipState, rumorId: String): RumorStatistics? {
        return state.rumorStatistics[rumorId]
    }
    
    /**
     * Get most spread rumor.
     */
    fun getMostSpreadRumor(state: GossipState): Rumor? {
        val mostSpreadId = state.rumorStatistics.values
            .maxByOrNull { it.totalReach }
            ?.rumorId
            ?: return null
        
        return state.activeRumors[mostSpreadId]
    }
    
    /**
     * Get most mutated rumor.
     */
    fun getMostMutatedRumor(state: GossipState): Rumor? {
        return state.activeRumors.values.maxByOrNull { it.mutationCount }
    }
}
