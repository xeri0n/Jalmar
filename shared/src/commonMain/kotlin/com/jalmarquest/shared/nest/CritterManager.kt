package com.jalmarquest.shared.nest

/**
 * Manager for critter operations in the nest.
 * Handles satisfaction tracking, feeding, adoption, and bonus calculations.
 * 
 * Stateless functional approach - returns new instances.
 */
class CritterManager {
    
    companion object {
        /** Maximum number of critters that can live in a nest */
        const val MAX_CRITTERS_PER_NEST = 5
        
        /** Satisfaction gained from feeding */
        const val FEED_SATISFACTION_GAIN = 20
        
        /** Satisfaction gained from nest tier matching preference */
        const val TIER_MATCH_SATISFACTION = 15
    }
    
    /**
     * Calculate satisfaction for a critter based on nest state.
     * Starts from current satisfaction and applies modifiers.
     * 
     * @param critter The critter definition
     * @param nestCritter The critter instance in the nest
     * @param nestTier Current nest tier
     * @param placedCosmetics List of cosmetics in the nest
     * @return Updated satisfaction value (clamped to 0-maxSatisfaction)
     */
    fun calculateSatisfaction(
        critter: Critter,
        nestCritter: NestCritter,
        nestTier: NestTier,
        placedCosmetics: List<PlacedCosmetic>
    ): Int {
        // Start from current satisfaction
        var satisfaction = nestCritter.currentSatisfaction
        
        // Apply daily decay penalty
        satisfaction -= critter.satisfactionDecayPerDay * nestCritter.daysSinceFed
        
        // Bonus for matching preferred nest tier
        if (critter.preferredNestTier == nestTier) {
            satisfaction += TIER_MATCH_SATISFACTION
        }
        
        // Bonus from cosmetic preferences
        val cosmeticTypeCounts = placedCosmetics
            .mapNotNull { placed -> CosmeticCatalog.getCosmeticById(placed.cosmeticId)?.type }
            .groupingBy { it }
            .eachCount()
        
        critter.cosmeticPreferences.forEach { preference ->
            val count = cosmeticTypeCounts[preference.cosmeticType] ?: 0
            satisfaction += count * preference.satisfactionPerItem
        }
        
        // Clamp to valid range
        return satisfaction.coerceIn(0, critter.maxSatisfaction)
    }
    
    /**
     * Feed a critter to restore satisfaction and reset hunger.
     * 
     * @param nestCritter Current critter state
     * @return Pair of (updated critter, feed result)
     */
    fun feedCritter(nestCritter: NestCritter): Pair<NestCritter, FeedResult> {
        val critter = CritterCatalog.getCritterById(nestCritter.critterId)
            ?: return Pair(nestCritter, FeedResult.Failure("Critter not found in catalog"))
        
        val newSatisfaction = (nestCritter.currentSatisfaction + FEED_SATISFACTION_GAIN)
            .coerceAtMost(critter.maxSatisfaction)
        
        val updatedCritter = nestCritter.copy(
            currentSatisfaction = newSatisfaction,
            daysSinceFed = 0
        )
        
        return Pair(updatedCritter, FeedResult.Success(FEED_SATISFACTION_GAIN))
    }
    
    /**
     * Adopt a new critter into the nest.
     * 
     * @param currentCritters Current critters in nest
     * @param critterId ID of critter to adopt
     * @param nestTier Current nest tier
     * @return Pair of (updated critter list, adopt result)
     */
    fun adoptCritter(
        currentCritters: List<NestCritter>,
        critterId: String,
        nestTier: NestTier
    ): Pair<List<NestCritter>, AdoptResult> {
        // Check if nest is full
        if (currentCritters.size >= MAX_CRITTERS_PER_NEST) {
            return Pair(currentCritters, AdoptResult.Failure(AdoptFailureReason.NEST_FULL))
        }
        
        // Check if critter exists
        val critter = CritterCatalog.getCritterById(critterId)
            ?: return Pair(currentCritters, AdoptResult.Failure(AdoptFailureReason.CRITTER_NOT_FOUND))
        
        // Check if already adopted
        if (currentCritters.any { it.critterId == critterId }) {
            return Pair(currentCritters, AdoptResult.Failure(AdoptFailureReason.ALREADY_ADOPTED))
        }
        
        // Check nest tier requirement for rare critters
        if (critter.preferredNestTier != null && nestTier.ordinal < critter.preferredNestTier.ordinal) {
            return Pair(currentCritters, AdoptResult.Failure(AdoptFailureReason.NEST_TIER_TOO_LOW))
        }
        
        // Create new critter instance
        val newCritter = NestCritter(
            critterId = critterId,
            customName = null,
            currentSatisfaction = critter.maxSatisfaction / 2,  // Start at 50%
            daysSinceFed = 0,
            totalDaysInNest = 0
        )
        
        val updatedCritters = currentCritters + newCritter
        return Pair(updatedCritters, AdoptResult.Success(newCritter))
    }
    
    /**
     * Release a critter from the nest.
     * 
     * @param currentCritters Current critters in nest
     * @param critterId ID of critter to release
     * @return Updated critter list
     */
    fun releaseCritter(
        currentCritters: List<NestCritter>,
        critterId: String
    ): List<NestCritter> {
        return currentCritters.filterNot { it.critterId == critterId }
    }
    
    /**
     * Rename a critter.
     * 
     * @param currentCritters Current critters in nest
     * @param critterId ID of critter to rename
     * @param newName New custom name (null to clear)
     * @return Updated critter list
     */
    fun renameCritter(
        currentCritters: List<NestCritter>,
        critterId: String,
        newName: String?
    ): List<NestCritter> {
        return currentCritters.map { critter ->
            if (critter.critterId == critterId) {
                critter.copy(customName = newName)
            } else {
                critter
            }
        }
    }
    
    /**
     * Calculate total bonus from all critters.
     * 
     * @param critters List of critters in nest
     * @param bonusType Type of bonus to calculate
     * @return Total bonus value
     */
    fun calculateTotalBonus(
        critters: List<NestCritter>,
        bonusType: CritterBonusType
    ): Int {
        return critters.sumOf { nestCritter ->
            val critter = CritterCatalog.getCritterById(nestCritter.critterId) ?: return@sumOf 0
            
            if (critter.bonusType != bonusType) return@sumOf 0
            
            val satisfactionLevel = nestCritter.getSatisfactionLevel(critter.maxSatisfaction)
            val rarityMultiplier = critter.getRarityMultiplier()
            
            (critter.baseBonusValue * rarityMultiplier * satisfactionLevel.multiplier).toInt()
        }
    }
    
    /**
     * Get all active bonuses from critters.
     * 
     * @param critters List of critters in nest
     * @return Map of bonus type to total value
     */
    fun getAllBonuses(critters: List<NestCritter>): Map<CritterBonusType, Int> {
        return CritterBonusType.values()
            .associateWith { bonusType -> calculateTotalBonus(critters, bonusType) }
            .filterValues { it > 0 }
    }
    
    /**
     * Advance time for all critters (daily update).
     * 
     * @param currentCritters Current critters in nest
     * @param nestTier Current nest tier
     * @param placedCosmetics Current cosmetics
     * @return Updated critter list
     */
    fun advanceDay(
        currentCritters: List<NestCritter>,
        nestTier: NestTier,
        placedCosmetics: List<PlacedCosmetic>
    ): List<NestCritter> {
        return currentCritters.mapNotNull { nestCritter ->
            val critter = CritterCatalog.getCritterById(nestCritter.critterId) ?: return@mapNotNull null
            
            // Increment days
            val updated = nestCritter.copy(
                daysSinceFed = nestCritter.daysSinceFed + 1,
                totalDaysInNest = nestCritter.totalDaysInNest + 1
            )
            
            // Recalculate satisfaction
            val newSatisfaction = calculateSatisfaction(critter, updated, nestTier, placedCosmetics)
            val withSatisfaction = updated.copy(currentSatisfaction = newSatisfaction)
            
            // Check if critter leaves due to low satisfaction
            if (withSatisfaction.mightLeave() && withSatisfaction.totalDaysInNest > 7) {
                null  // Critter has left
            } else {
                withSatisfaction
            }
        }
    }
    
    /**
     * Check if a specific critter can be adopted.
     * 
     * @param currentCritters Current critters in nest
     * @param critterId ID of critter to check
     * @param nestTier Current nest tier
     * @return True if critter can be adopted
     */
    fun canAdopt(
        currentCritters: List<NestCritter>,
        critterId: String,
        nestTier: NestTier
    ): Boolean {
        if (currentCritters.size >= MAX_CRITTERS_PER_NEST) return false
        
        val critter = CritterCatalog.getCritterById(critterId) ?: return false
        
        if (currentCritters.any { it.critterId == critterId }) return false
        
        if (critter.preferredNestTier != null && nestTier.ordinal < critter.preferredNestTier.ordinal) {
            return false
        }
        
        return true
    }
}
