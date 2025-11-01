package com.jalmarquest.shared.companion

import com.jalmarquest.shared.model.GameState

/**
 * Stateless manager for companion operations.
 * All state mutations are performed through GameStateManager for thread-safety.
 * 
 * Responsibilities:
 * - Recruit and dismiss companions
 * - Manage active companion
 * - Track loyalty and apply changes
 * - Validate ability usage
 * - Convert companions to combat data
 * 
 * Thread-Safety: This manager is stateless. All mutations must go through GameStateManager.
 */
class CompanionManager {
    
    /**
     * Get a companion by ID from the catalog.
     */
    fun getCompanionById(companionId: String): Companion? {
        return CompanionCatalog.getCompanionById(companionId)
    }
    
    /**
     * Get all companions from the catalog.
     */
    fun getAllCompanions(): List<Companion> {
        return CompanionCatalog.ALL_COMPANIONS
    }
    
    /**
     * Check if a companion is recruited in the current game state.
     */
    fun isRecruited(gameState: GameState, companionId: String): Boolean {
        return gameState.recruitedCompanions.contains(companionId)
    }
    
    /**
     * Get the active companion, if any.
     */
    fun getActiveCompanion(gameState: GameState): Companion? {
        val activeId = gameState.activeCompanionId ?: return null
        return getCompanionById(activeId)
    }
    
    /**
     * Get the active companion's progress data.
     */
    fun getActiveCompanionProgress(gameState: GameState): CompanionProgress? {
        val activeId = gameState.activeCompanionId ?: return null
        return gameState.companionProgress[activeId]
    }
    
    /**
     * Get all recruited companions.
     */
    fun getRecruitedCompanions(gameState: GameState): List<Companion> {
        return gameState.recruitedCompanions.mapNotNull { getCompanionById(it) }
    }
    
    /**
     * Get the loyalty score for a specific companion.
     */
    fun getLoyalty(gameState: GameState, companionId: String): Int {
        return gameState.companionProgress[companionId]?.loyaltyScore ?: 50 // Default neutral
    }
    
    /**
     * Get the loyalty status tier for a companion.
     */
    fun getLoyaltyStatus(gameState: GameState, companionId: String): CompanionLoyaltyStatus {
        val loyalty = getLoyalty(gameState, companionId)
        return CompanionLoyaltyStatus.fromScore(loyalty)
    }
    
    /**
     * Check if a companion can use a specific ability based on loyalty.
     */
    fun canUseAbility(gameState: GameState, companionId: String, abilityId: String): CanUseAbilityResult {
        // Check if companion exists
        val companion = getCompanionById(companionId)
            ?: return CanUseAbilityResult.Failure(CanUseAbilityFailure.COMPANION_NOT_FOUND)
        
        // Check if companion is recruited
        if (!isRecruited(gameState, companionId)) {
            return CanUseAbilityResult.Failure(CanUseAbilityFailure.COMPANION_NOT_RECRUITED)
        }
        
        // Find the ability
        val ability = companion.abilities.find { it.id == abilityId }
            ?: return CanUseAbilityResult.Failure(CanUseAbilityFailure.ABILITY_NOT_FOUND)
        
        // Check loyalty requirement
        val currentLoyalty = getLoyalty(gameState, companionId)
        if (currentLoyalty < ability.loyaltyRequired) {
            return CanUseAbilityResult.Failure(CanUseAbilityFailure.INSUFFICIENT_LOYALTY)
        }
        
        // Check cooldown
        val progress = gameState.companionProgress[companionId]
        if (progress != null) {
            val remainingCooldown = progress.abilityCooldowns[abilityId] ?: 0
            if (remainingCooldown > 0) {
                return CanUseAbilityResult.Failure(
                    CanUseAbilityFailure.ON_COOLDOWN,
                    remainingCooldown = remainingCooldown
                )
            }
        }
        
        return CanUseAbilityResult.Success(ability)
    }
    
    /**
     * Get available abilities for a companion based on current loyalty.
     */
    fun getAvailableAbilities(gameState: GameState, companionId: String): List<CompanionAbility> {
        val companion = getCompanionById(companionId) ?: return emptyList()
        if (!isRecruited(gameState, companionId)) return emptyList()
        
        val currentLoyalty = getLoyalty(gameState, companionId)
        return companion.abilities.filter { it.loyaltyRequired <= currentLoyalty }
    }
    
    /**
     * Get abilities that are locked due to insufficient loyalty.
     */
    fun getLockedAbilities(gameState: GameState, companionId: String): List<CompanionAbility> {
        val companion = getCompanionById(companionId) ?: return emptyList()
        if (!isRecruited(gameState, companionId)) return companion.abilities
        
        val currentLoyalty = getLoyalty(gameState, companionId)
        return companion.abilities.filter { it.loyaltyRequired > currentLoyalty }
    }
    
    /**
     * Create a new game state with a recruited companion.
     * Returns updated GameState.
     */
    fun recruitCompanion(gameState: GameState, companionId: String, currentTime: Long): RecruitResult {
        // Validate companion exists
        val companion = getCompanionById(companionId)
            ?: return RecruitResult.Failure(RecruitFailure.COMPANION_NOT_FOUND)
        
        // Check if already recruited
        if (isRecruited(gameState, companionId)) {
            return RecruitResult.Failure(RecruitFailure.ALREADY_RECRUITED)
        }
        
        // Create initial progress
        val initialProgress = CompanionProgress(
            companionId = companionId,
            loyaltyScore = 50, // Start at neutral
            abilityCooldowns = emptyMap(),
            lastInteractionTimestamp = currentTime
        )
        
        // Update game state
        val updatedState = gameState.copy(
            recruitedCompanions = gameState.recruitedCompanions + companionId,
            companionProgress = gameState.companionProgress + (companionId to initialProgress)
        )
        
        return RecruitResult.Success(updatedState, companion)
    }
    
    /**
     * Create a new game state with a dismissed companion.
     * Returns updated GameState.
     */
    fun dismissCompanion(gameState: GameState, companionId: String): DismissResult {
        // Check if recruited
        if (!isRecruited(gameState, companionId)) {
            return DismissResult.Failure(DismissFailure.NOT_RECRUITED)
        }
        
        val companion = getCompanionById(companionId)
            ?: return DismissResult.Failure(DismissFailure.COMPANION_NOT_FOUND)
        
        // Clear active companion if dismissing the active one
        val newActiveId = if (gameState.activeCompanionId == companionId) null else gameState.activeCompanionId
        
        // Update game state (keep progress for potential re-recruitment)
        val updatedState = gameState.copy(
            recruitedCompanions = gameState.recruitedCompanions - companionId,
            activeCompanionId = newActiveId
        )
        
        return DismissResult.Success(updatedState, companion)
    }
    
    /**
     * Set the active companion (the one following the player).
     * Returns updated GameState.
     */
    fun setActiveCompanion(gameState: GameState, companionId: String?): SetActiveResult {
        // If setting to null, just clear active companion
        if (companionId == null) {
            return SetActiveResult.Success(gameState.copy(activeCompanionId = null), null)
        }
        
        // Validate companion is recruited
        if (!isRecruited(gameState, companionId)) {
            return SetActiveResult.Failure(SetActiveFailure.NOT_RECRUITED)
        }
        
        val companion = getCompanionById(companionId)
            ?: return SetActiveResult.Failure(SetActiveFailure.COMPANION_NOT_FOUND)
        
        val updatedState = gameState.copy(activeCompanionId = companionId)
        return SetActiveResult.Success(updatedState, companion)
    }
    
    /**
     * Modify a companion's loyalty score.
     * Returns updated GameState with loyalty change tracked for Butterfly Effect.
     */
    fun modifyLoyalty(
        gameState: GameState,
        companionId: String,
        change: Int,
        trigger: LoyaltyChangeTrigger,
        currentTime: Long,
        context: String? = null
    ): ModifyLoyaltyResult {
        // Validate companion is recruited
        if (!isRecruited(gameState, companionId)) {
            return ModifyLoyaltyResult.Failure(ModifyLoyaltyFailure.NOT_RECRUITED)
        }
        
        val companion = getCompanionById(companionId)
            ?: return ModifyLoyaltyResult.Failure(ModifyLoyaltyFailure.COMPANION_NOT_FOUND)
        
        // Get current progress
        val currentProgress = gameState.companionProgress[companionId]
            ?: CompanionProgress(
                companionId = companionId,
                loyaltyScore = 50,
                abilityCooldowns = emptyMap(),
                lastInteractionTimestamp = currentTime
            )
        
        val previousLoyalty = currentProgress.loyaltyScore
        val newLoyalty = LoyaltyChangeHelper.calculateNewLoyalty(previousLoyalty, change)
        
        // Check if threshold crossed
        val thresholdCrossed = LoyaltyChangeHelper.crossedThreshold(previousLoyalty, newLoyalty)
        
        // Create loyalty change event for Butterfly Effect tracking
        val event = LoyaltyChangeEvent(
            eventId = "loyalty_${companionId}_${currentTime}",
            companionId = companionId,
            trigger = trigger,
            loyaltyChange = change,
            previousLoyalty = previousLoyalty,
            newLoyalty = newLoyalty,
            timestamp = currentTime,
            context = context
        )
        
        // Update progress
        val updatedProgress = currentProgress.modifyLoyalty(change)
        
        // Update game state
        val updatedState = gameState.copy(
            companionProgress = gameState.companionProgress + (companionId to updatedProgress)
        )
        
        return ModifyLoyaltyResult.Success(
            newState = updatedState,
            companion = companion,
            event = event,
            previousLoyalty = previousLoyalty,
            newLoyalty = newLoyalty,
            thresholdCrossed = thresholdCrossed
        )
    }
    
    /**
     * Convert the active companion to combat data for battle.
     */
    fun getActiveCompanionCombatData(gameState: GameState, combatId: String): CompanionCombatData? {
        val companion = getActiveCompanion(gameState) ?: return null
        val progress = getActiveCompanionProgress(gameState) ?: return null
        
        return companion.toCombatData(
            combatId = combatId,
            currentHp = companion.maxHp, // Start at full HP for new combat
            loyaltyScore = progress.loyaltyScore
        )
    }
    
    /**
     * Update companion interaction timestamp.
     * Used to track neglect for loyalty decay.
     */
    fun updateInteractionTime(
        gameState: GameState,
        companionId: String
    ): GameState? {
        val progress = gameState.companionProgress[companionId] ?: return null
        val updatedProgress = progress.updateInteraction()
        
        return gameState.copy(
            companionProgress = gameState.companionProgress + (companionId to updatedProgress)
        )
    }
    
    /**
     * Decrement all ability cooldowns for a companion.
     * Called after combat rounds or time passage.
     */
    fun decrementCooldowns(gameState: GameState, companionId: String): GameState? {
        val progress = gameState.companionProgress[companionId] ?: return null
        val updatedProgress = progress.decrementCooldowns()
        
        return gameState.copy(
            companionProgress = gameState.companionProgress + (companionId to updatedProgress)
        )
    }
    
    /**
     * Set an ability on cooldown after use.
     */
    fun setAbilityCooldown(
        gameState: GameState,
        companionId: String,
        abilityId: String,
        cooldownRounds: Int
    ): GameState? {
        val progress = gameState.companionProgress[companionId] ?: return null
        val updatedProgress = progress.setCooldown(abilityId, cooldownRounds)
        
        return gameState.copy(
            companionProgress = gameState.companionProgress + (companionId to updatedProgress)
        )
    }
}

/**
 * Result types for companion operations.
 */
sealed class RecruitResult {
    data class Success(val newState: GameState, val companion: Companion) : RecruitResult()
    data class Failure(val reason: RecruitFailure) : RecruitResult()
}

enum class RecruitFailure {
    COMPANION_NOT_FOUND,
    ALREADY_RECRUITED
}

sealed class DismissResult {
    data class Success(val newState: GameState, val companion: Companion) : DismissResult()
    data class Failure(val reason: DismissFailure) : DismissResult()
}

enum class DismissFailure {
    COMPANION_NOT_FOUND,
    NOT_RECRUITED
}

sealed class SetActiveResult {
    data class Success(val newState: GameState, val companion: Companion?) : SetActiveResult()
    data class Failure(val reason: SetActiveFailure) : SetActiveResult()
}

enum class SetActiveFailure {
    COMPANION_NOT_FOUND,
    NOT_RECRUITED
}

sealed class ModifyLoyaltyResult {
    data class Success(
        val newState: GameState,
        val companion: Companion,
        val event: LoyaltyChangeEvent,
        val previousLoyalty: Int,
        val newLoyalty: Int,
        val thresholdCrossed: Boolean
    ) : ModifyLoyaltyResult()
    
    data class Failure(val reason: ModifyLoyaltyFailure) : ModifyLoyaltyResult()
}

enum class ModifyLoyaltyFailure {
    COMPANION_NOT_FOUND,
    NOT_RECRUITED
}

sealed class CanUseAbilityResult {
    data class Success(val ability: CompanionAbility) : CanUseAbilityResult()
    data class Failure(
        val reason: CanUseAbilityFailure,
        val remainingCooldown: Int = 0
    ) : CanUseAbilityResult()
}

enum class CanUseAbilityFailure {
    COMPANION_NOT_FOUND,
    COMPANION_NOT_RECRUITED,
    ABILITY_NOT_FOUND,
    INSUFFICIENT_LOYALTY,
    ON_COOLDOWN
}
