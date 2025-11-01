package com.jalmarquest.shared.nest

/**
 * Manager for trophy display and prestige calculations in the nest trophy room.
 * 
 * Stateless functional approach - all methods return new instances.
 * Thread-safety delegated to GameStateManager's Mutex.
 */
class TrophyManager {
    
    /**
     * Display a trophy in the trophy room at a specific slot.
     * 
     * @param trophyRoom Current trophy room state
     * @param unlockedTrophies Set of trophy IDs the player has unlocked
     * @param trophyId ID of trophy to display
     * @param slotIndex Display slot index (0-based)
     * @return TrophyDisplayResult with updated room or failure reason
     */
    fun displayTrophy(
        trophyRoom: TrophyRoom,
        unlockedTrophies: Set<String>,
        trophyId: String,
        slotIndex: Int
    ): TrophyDisplayResult {
        // Validate trophy exists in catalog
        val trophy = TrophyCatalog.getTrophyById(trophyId)
            ?: return TrophyDisplayResult.Failure(TrophyDisplayFailure.TROPHY_NOT_FOUND)
        
        // Check if trophy is unlocked
        if (!unlockedTrophies.contains(trophyId)) {
            return TrophyDisplayResult.Failure(TrophyDisplayFailure.TROPHY_LOCKED)
        }
        
        // Check if already displayed
        if (trophyRoom.displayedTrophies.any { it.trophyId == trophyId }) {
            return TrophyDisplayResult.Failure(TrophyDisplayFailure.TROPHY_ALREADY_DISPLAYED)
        }
        
        // Validate slot index
        if (slotIndex < 0 || slotIndex >= trophyRoom.maxDisplaySlots) {
            return TrophyDisplayResult.Failure(TrophyDisplayFailure.INVALID_SLOT_INDEX)
        }
        
        // Check if enough slots available (accounting for trophy size)
        val allTrophies = TrophyCatalog.getAllTrophies()
        if (!trophyRoom.canFitTrophy(trophy, allTrophies)) {
            return TrophyDisplayResult.Failure(TrophyDisplayFailure.NOT_ENOUGH_SLOTS)
        }
        
        // Check if slot is already occupied (slot overlap validation)
        val occupiedSlots = getOccupiedSlotIndices(trophyRoom, allTrophies)
        val newTrophySlots = (slotIndex until slotIndex + trophy.size.slotsRequired).toSet()
        if (occupiedSlots.intersect(newTrophySlots).isNotEmpty()) {
            return TrophyDisplayResult.Failure(TrophyDisplayFailure.INVALID_SLOT_INDEX)
        }
        
        // Add trophy to displayed list
        val newDisplayedTrophy = DisplayedTrophy(trophyId, slotIndex)
        val updatedDisplayedTrophies = trophyRoom.displayedTrophies + newDisplayedTrophy
        
        // Recalculate total prestige
        val prestigeGained = trophy.calculatePrestige()
        val newTotalPrestige = trophyRoom.totalPrestige + prestigeGained
        
        val updatedTrophyRoom = trophyRoom.copy(
            displayedTrophies = updatedDisplayedTrophies,
            totalPrestige = newTotalPrestige
        )
        
        return TrophyDisplayResult.Success(updatedTrophyRoom, prestigeGained)
    }
    
    /**
     * Remove a trophy from display.
     * 
     * @param trophyRoom Current trophy room state
     * @param slotIndex Slot index to remove trophy from
     * @return TrophyRemovalResult with updated room or failure reason
     */
    fun removeTrophy(
        trophyRoom: TrophyRoom,
        slotIndex: Int
    ): TrophyRemovalResult {
        // Find trophy at this slot
        val displayedTrophy = trophyRoom.displayedTrophies.find { it.slotIndex == slotIndex }
            ?: return TrophyRemovalResult.Failure(TrophyRemovalFailure.TROPHY_NOT_DISPLAYED)
        
        // Get trophy details for prestige calculation
        val trophy = TrophyCatalog.getTrophyById(displayedTrophy.trophyId)
            ?: return TrophyRemovalResult.Failure(TrophyRemovalFailure.TROPHY_NOT_DISPLAYED)
        
        // Remove trophy from list
        val updatedDisplayedTrophies = trophyRoom.displayedTrophies.filter { it != displayedTrophy }
        
        // Recalculate prestige
        val prestigeLost = trophy.calculatePrestige()
        val newTotalPrestige = (trophyRoom.totalPrestige - prestigeLost).coerceAtLeast(0)
        
        val updatedTrophyRoom = trophyRoom.copy(
            displayedTrophies = updatedDisplayedTrophies,
            totalPrestige = newTotalPrestige
        )
        
        return TrophyRemovalResult.Success(updatedTrophyRoom, prestigeLost)
    }
    
    /**
     * Calculate total prestige from all displayed trophies.
     * This recalculates from scratch (useful for validation/migration).
     * 
     * @param trophyRoom Current trophy room state
     * @return Total prestige value
     */
    fun calculateTotalPrestige(trophyRoom: TrophyRoom): Int {
        return trophyRoom.displayedTrophies.sumOf { displayedTrophy ->
            TrophyCatalog.getTrophyById(displayedTrophy.trophyId)?.calculatePrestige() ?: 0
        }
    }
    
    /**
     * Get NPC reaction to a displayed trophy.
     * Different NPCs react differently based on relationship and personality.
     * 
     * @param trophy Trophy being viewed
     * @param npcId NPC viewing the trophy
     * @param relationshipLevel Player's relationship level with NPC (0-100)
     * @return VisitorReaction for this NPC-trophy combination
     */
    fun getNPCReaction(
        trophy: Trophy,
        npcId: String,
        relationshipLevel: Int
    ): VisitorReaction {
        // Boss defeated trophies trigger stories from NPCs (priority over rarity)
        if (trophy.type == TrophyType.BOSS_DEFEATED && relationshipLevel >= 50) {
            return VisitorReaction.Storytelling(
                trophyId = trophy.id,
                dialogueLine = "I remember when that beast roamed the garden. You're braver than I ever was.",
                loreFragmentId = "lore_${trophy.id}_story"
            )
        }
        
        // Legendary trophies always impress
        if (trophy.rarity == TrophyRarity.LEGENDARY) {
            return when {
                relationshipLevel >= 75 -> VisitorReaction.Admiring(
                    trophyId = trophy.id,
                    dialogueLine = "By the Great Garden! A ${trophy.name}! This is truly legendary!",
                    relationshipBonus = 15
                )
                relationshipLevel >= 50 -> VisitorReaction.Impressed(
                    trophyId = trophy.id,
                    dialogueLine = "Incredible! I've heard tales of the ${trophy.name}, but never seen it!",
                    relationshipBonus = 10
                )
                else -> VisitorReaction.Envious(
                    trophyId = trophy.id,
                    dialogueLine = "*grumbles* Lucky you, getting a ${trophy.name}...",
                    relationshipPenalty = -5
                )
            }
        }
        
        // Epic and Rare trophies impress friends
        if (trophy.rarity == TrophyRarity.EPIC || trophy.rarity == TrophyRarity.RARE) {
            return when {
                relationshipLevel >= 60 -> VisitorReaction.Impressed(
                    trophyId = trophy.id,
                    dialogueLine = "That ${trophy.name} is impressive! You've earned this.",
                    relationshipBonus = 8
                )
                relationshipLevel >= 30 -> VisitorReaction.Impressed(
                    trophyId = trophy.id,
                    dialogueLine = "Nice ${trophy.name}. Looks like you've been busy.",
                    relationshipBonus = 5
                )
                else -> VisitorReaction.Indifferent(trophyId = trophy.id)
            }
        }
        
        // Default: indifferent or mildly impressed
        return when {
            relationshipLevel >= 70 -> VisitorReaction.Impressed(
                trophyId = trophy.id,
                dialogueLine = "You've got quite a collection here!",
                relationshipBonus = 3
            )
            else -> VisitorReaction.Indifferent(trophyId = trophy.id)
        }
    }
    
    /**
     * Get all trophies unlocked by a specific achievement.
     * 
     * @param achievementId Achievement ID
     * @return List of trophies unlocked by this achievement
     */
    fun getTrophiesForAchievement(achievementId: String): List<Trophy> {
        return TrophyCatalog.getAllTrophies().filter {
            it.unlockAchievementId == achievementId
        }
    }
    
    /**
     * Get highest prestige trophy currently displayed.
     * Useful for UI highlights and visitor reactions.
     * 
     * @param trophyRoom Current trophy room state
     * @return Trophy with highest prestige, or null if none displayed
     */
    fun getHighestPrestigeTrophy(trophyRoom: TrophyRoom): Trophy? {
        return trophyRoom.displayedTrophies
            .mapNotNull { TrophyCatalog.getTrophyById(it.trophyId) }
            .maxByOrNull { it.calculatePrestige() }
    }
    
    /**
     * Get count of displayed trophies by rarity.
     * Useful for achievements like "Display 5 Legendary Trophies".
     * 
     * @param trophyRoom Current trophy room state
     * @param rarity Rarity to count
     * @return Number of displayed trophies of this rarity
     */
    fun getDisplayedTrophyCountByRarity(trophyRoom: TrophyRoom, rarity: TrophyRarity): Int {
        return trophyRoom.displayedTrophies.count { displayedTrophy ->
            TrophyCatalog.getTrophyById(displayedTrophy.trophyId)?.rarity == rarity
        }
    }
    
    /**
     * Helper: Get all occupied slot indices (accounting for multi-slot trophies).
     */
    private fun getOccupiedSlotIndices(trophyRoom: TrophyRoom, allTrophies: List<Trophy>): Set<Int> {
        return trophyRoom.displayedTrophies.flatMap { displayedTrophy ->
            val trophy = allTrophies.find { it.id == displayedTrophy.trophyId }
            if (trophy != null) {
                (displayedTrophy.slotIndex until displayedTrophy.slotIndex + trophy.size.slotsRequired).toList()
            } else {
                emptyList()
            }
        }.toSet()
    }
}
