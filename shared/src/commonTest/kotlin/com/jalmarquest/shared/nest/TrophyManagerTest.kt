package com.jalmarquest.shared.nest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TrophyManagerTest {
    
    private val trophyManager = TrophyManager()
    
    // Test trophy room with 20 slots
    private val testTrophyRoom = TrophyRoom(
        maxDisplaySlots = 20,
        displayedTrophies = emptyList(),
        totalPrestige = 0
    )
    
    // Test unlocked trophies set
    private val testUnlockedTrophies = setOf(
        "trophy_first_quest",
        "trophy_level_10",
        "trophy_giant_spider_defeated",
        "trophy_all_locations",
        "trophy_level_50",
        "trophy_beetle_king_defeated"
    )
    
    // ========== DISPLAY OPERATIONS ==========
    
    @Test
    fun `displayTrophy should succeed with valid inputs`() {
        val result = trophyManager.displayTrophy(
            trophyRoom = testTrophyRoom,
            unlockedTrophies = testUnlockedTrophies,
            trophyId = "trophy_first_quest",
            slotIndex = 0
        )
        
        assertIs<TrophyDisplayResult.Success>(result)
        assertEquals(1, result.newTrophyRoom.displayedTrophies.size)
        assertEquals("trophy_first_quest", result.newTrophyRoom.displayedTrophies[0].trophyId)
        assertTrue(result.prestigeGained > 0)
    }
    
    @Test
    fun `displayTrophy should fail when trophy not found`() {
        val result = trophyManager.displayTrophy(
            trophyRoom = testTrophyRoom,
            unlockedTrophies = testUnlockedTrophies,
            trophyId = "nonexistent_trophy",
            slotIndex = 0
        )
        
        assertIs<TrophyDisplayResult.Failure>(result)
        assertEquals(TrophyDisplayFailure.TROPHY_NOT_FOUND, result.reason)
    }
    
    @Test
    fun `displayTrophy should fail when trophy is locked`() {
        val result = trophyManager.displayTrophy(
            trophyRoom = testTrophyRoom,
            unlockedTrophies = emptySet(),  // No trophies unlocked
            trophyId = "trophy_first_quest",
            slotIndex = 0
        )
        
        assertIs<TrophyDisplayResult.Failure>(result)
        assertEquals(TrophyDisplayFailure.TROPHY_LOCKED, result.reason)
    }
    
    @Test
    fun `displayTrophy should fail when trophy already displayed`() {
        // First display succeeds
        val firstResult = trophyManager.displayTrophy(
            trophyRoom = testTrophyRoom,
            unlockedTrophies = testUnlockedTrophies,
            trophyId = "trophy_first_quest",
            slotIndex = 0
        )
        assertIs<TrophyDisplayResult.Success>(firstResult)
        
        // Second display should fail
        val secondResult = trophyManager.displayTrophy(
            trophyRoom = firstResult.newTrophyRoom,
            unlockedTrophies = testUnlockedTrophies,
            trophyId = "trophy_first_quest",
            slotIndex = 5
        )
        assertIs<TrophyDisplayResult.Failure>(secondResult)
        assertEquals(TrophyDisplayFailure.TROPHY_ALREADY_DISPLAYED, secondResult.reason)
    }
    
    @Test
    fun `displayTrophy should fail with invalid slot index`() {
        val result = trophyManager.displayTrophy(
            trophyRoom = testTrophyRoom,
            unlockedTrophies = testUnlockedTrophies,
            trophyId = "trophy_first_quest",
            slotIndex = 99  // Beyond max slots
        )
        
        assertIs<TrophyDisplayResult.Failure>(result)
        assertEquals(TrophyDisplayFailure.INVALID_SLOT_INDEX, result.reason)
    }
    
    @Test
    fun `displayTrophy should handle multi-slot trophies`() {
        // LARGE trophy requires 4 slots
        val trophy = TrophyCatalog.getTrophyById("trophy_giant_spider_defeated")
        assertNotNull(trophy)
        assertEquals(TrophySize.LARGE, trophy.size)
        
        val result = trophyManager.displayTrophy(
            trophyRoom = testTrophyRoom,
            unlockedTrophies = testUnlockedTrophies,
            trophyId = "trophy_giant_spider_defeated",
            slotIndex = 0
        )
        
        assertIs<TrophyDisplayResult.Success>(result)
        assertEquals(1, result.newTrophyRoom.displayedTrophies.size)
    }
    
    @Test
    fun `displayTrophy should prevent slot overlap`() {
        // Display MEDIUM trophy (2 slots) at slot 0 (occupies slots 0-1)
        val firstResult = trophyManager.displayTrophy(
            trophyRoom = testTrophyRoom,
            unlockedTrophies = testUnlockedTrophies,
            trophyId = "trophy_beetle_king_defeated",  // MEDIUM = 2 slots
            slotIndex = 0
        )
        assertIs<TrophyDisplayResult.Success>(firstResult)
        
        // Try to display SMALL trophy at slot 1 (should overlap)
        val secondResult = trophyManager.displayTrophy(
            trophyRoom = firstResult.newTrophyRoom,
            unlockedTrophies = testUnlockedTrophies,
            trophyId = "trophy_first_quest",  // SMALL = 1 slot
            slotIndex = 1
        )
        assertIs<TrophyDisplayResult.Failure>(secondResult)
        assertEquals(TrophyDisplayFailure.INVALID_SLOT_INDEX, secondResult.reason)
        
        // Display at slot 2 should succeed (no overlap)
        val thirdResult = trophyManager.displayTrophy(
            trophyRoom = firstResult.newTrophyRoom,
            unlockedTrophies = testUnlockedTrophies,
            trophyId = "trophy_first_quest",
            slotIndex = 2
        )
        assertIs<TrophyDisplayResult.Success>(thirdResult)
    }

    
    @Test
    fun `displayTrophy should fail when not enough slots remaining`() {
        // Create small trophy room with only 2 slots
        val smallRoom = TrophyRoom(maxDisplaySlots = 2, displayedTrophies = emptyList(), totalPrestige = 0)
        
        // Try to display LARGE trophy (requires 4 slots)
        val result = trophyManager.displayTrophy(
            trophyRoom = smallRoom,
            unlockedTrophies = testUnlockedTrophies,
            trophyId = "trophy_giant_spider_defeated",  // LARGE = 4 slots
            slotIndex = 0
        )
        
        assertIs<TrophyDisplayResult.Failure>(result)
        assertEquals(TrophyDisplayFailure.NOT_ENOUGH_SLOTS, result.reason)
    }
    
    // ========== REMOVAL OPERATIONS ==========
    
    @Test
    fun `removeTrophy should succeed with valid slot`() {
        // Display a trophy first
        val displayResult = trophyManager.displayTrophy(
            trophyRoom = testTrophyRoom,
            unlockedTrophies = testUnlockedTrophies,
            trophyId = "trophy_first_quest",
            slotIndex = 0
        )
        assertIs<TrophyDisplayResult.Success>(displayResult)
        
        // Remove it
        val removeResult = trophyManager.removeTrophy(
            trophyRoom = displayResult.newTrophyRoom,
            slotIndex = 0
        )
        
        assertIs<TrophyRemovalResult.Success>(removeResult)
        assertEquals(0, removeResult.newTrophyRoom.displayedTrophies.size)
        assertTrue(removeResult.prestigeLost > 0)
    }
    
    @Test
    fun `removeTrophy should fail when slot is empty`() {
        val result = trophyManager.removeTrophy(
            trophyRoom = testTrophyRoom,
            slotIndex = 0
        )
        
        assertIs<TrophyRemovalResult.Failure>(result)
        assertEquals(TrophyRemovalFailure.TROPHY_NOT_DISPLAYED, result.reason)
    }
    
    @Test
    fun `removeTrophy should correctly update prestige`() {
        // Display trophy
        val displayResult = trophyManager.displayTrophy(
            trophyRoom = testTrophyRoom,
            unlockedTrophies = testUnlockedTrophies,
            trophyId = "trophy_level_10",
            slotIndex = 0
        )
        assertIs<TrophyDisplayResult.Success>(displayResult)
        val initialPrestige = displayResult.newTrophyRoom.totalPrestige
        
        // Remove trophy
        val removeResult = trophyManager.removeTrophy(
            trophyRoom = displayResult.newTrophyRoom,
            slotIndex = 0
        )
        assertIs<TrophyRemovalResult.Success>(removeResult)
        
        assertEquals(0, removeResult.newTrophyRoom.totalPrestige)
        assertEquals(initialPrestige, removeResult.prestigeLost)
    }
    
    // ========== PRESTIGE CALCULATION ==========
    
    @Test
    fun `calculateTotalPrestige should return zero for empty room`() {
        val prestige = trophyManager.calculateTotalPrestige(testTrophyRoom)
        assertEquals(0, prestige)
    }
    
    @Test
    fun `calculateTotalPrestige should sum all trophy prestige values`() {
        // Display multiple trophies
        var room = testTrophyRoom
        
        val result1 = trophyManager.displayTrophy(room, testUnlockedTrophies, "trophy_first_quest", 0)
        assertIs<TrophyDisplayResult.Success>(result1)
        room = result1.newTrophyRoom
        
        val result2 = trophyManager.displayTrophy(room, testUnlockedTrophies, "trophy_level_10", 2)
        assertIs<TrophyDisplayResult.Success>(result2)
        room = result2.newTrophyRoom
        
        val calculatedPrestige = trophyManager.calculateTotalPrestige(room)
        assertEquals(room.totalPrestige, calculatedPrestige)
        assertTrue(calculatedPrestige > 0)
    }
    
    @Test
    fun `calculateTotalPrestige should match cached value in TrophyRoom`() {
        // Display 3 trophies
        var room = testTrophyRoom
        val trophyIds = listOf("trophy_first_quest", "trophy_level_10", "trophy_all_locations")
        var slotIndex = 0
        
        trophyIds.forEach { trophyId ->
            val result = trophyManager.displayTrophy(room, testUnlockedTrophies, trophyId, slotIndex)
            assertIs<TrophyDisplayResult.Success>(result)
            room = result.newTrophyRoom
            slotIndex += 3  // Skip slots to avoid overlap
        }
        
        val cachedPrestige = room.totalPrestige
        val recalculatedPrestige = trophyManager.calculateTotalPrestige(room)
        assertEquals(cachedPrestige, recalculatedPrestige)
    }
    
    // ========== NPC REACTIONS ==========
    
    @Test
    fun `getNPCReaction should return Admiring for legendary trophy with high relationship`() {
        val trophy = TrophyCatalog.getTrophyById("trophy_level_50")
        assertNotNull(trophy)
        assertEquals(TrophyRarity.LEGENDARY, trophy.rarity)
        
        val reaction = trophyManager.getNPCReaction(
            trophy = trophy,
            npcId = "elder_quail",
            relationshipLevel = 80
        )
        
        assertIs<VisitorReaction.Admiring>(reaction)
        assertTrue(reaction.relationshipBonus > 0)
    }
    
    @Test
    fun `getNPCReaction should return Envious for legendary trophy with low relationship`() {
        val trophy = TrophyCatalog.getTrophyById("trophy_level_50")
        assertNotNull(trophy)
        
        val reaction = trophyManager.getNPCReaction(
            trophy = trophy,
            npcId = "rival_npc",
            relationshipLevel = 20
        )
        
        assertIs<VisitorReaction.Envious>(reaction)
        assertTrue(reaction.relationshipPenalty < 0)
    }
    
    @Test
    fun `getNPCReaction should return Storytelling for boss trophy with high relationship`() {
        val trophy = TrophyCatalog.getTrophyById("trophy_giant_spider_defeated")
        assertNotNull(trophy)
        assertEquals(TrophyType.BOSS_DEFEATED, trophy.type)
        
        val reaction = trophyManager.getNPCReaction(
            trophy = trophy,
            npcId = "old_veteran",
            relationshipLevel = 70
        )
        
        assertIs<VisitorReaction.Storytelling>(reaction)
        assertNotNull(reaction.dialogueLine)
    }
    
    @Test
    fun `getNPCReaction should return Indifferent for common trophy with low relationship`() {
        val trophy = TrophyCatalog.getTrophyById("trophy_first_quest")
        assertNotNull(trophy)
        assertEquals(TrophyRarity.COMMON, trophy.rarity)
        
        val reaction = trophyManager.getNPCReaction(
            trophy = trophy,
            npcId = "stranger_npc",
            relationshipLevel = 10
        )
        
        assertIs<VisitorReaction.Indifferent>(reaction)
    }
    
    // ========== HELPER METHODS ==========
    
    @Test
    fun `getTrophiesForAchievement should return matching trophies`() {
        val trophies = trophyManager.getTrophiesForAchievement("achievement_reach_level_10")
        
        assertTrue(trophies.isNotEmpty())
        assertTrue(trophies.all { it.unlockAchievementId == "achievement_reach_level_10" })
    }
    
    @Test
    fun `getTrophiesForAchievement should return empty list for nonexistent achievement`() {
        val trophies = trophyManager.getTrophiesForAchievement("nonexistent_achievement")
        assertTrue(trophies.isEmpty())
    }
    
    @Test
    fun `getHighestPrestigeTrophy should return trophy with max prestige`() {
        // Display multiple trophies with different prestige values
        var room = testTrophyRoom
        
        val result1 = trophyManager.displayTrophy(room, testUnlockedTrophies, "trophy_first_quest", 0)  // Common, low prestige
        assertIs<TrophyDisplayResult.Success>(result1)
        room = result1.newTrophyRoom
        
        val result2 = trophyManager.displayTrophy(room, testUnlockedTrophies, "trophy_level_50", 2)  // Legendary, high prestige
        assertIs<TrophyDisplayResult.Success>(result2)
        room = result2.newTrophyRoom
        
        val highestTrophy = trophyManager.getHighestPrestigeTrophy(room)
        assertNotNull(highestTrophy)
        assertEquals("trophy_level_50", highestTrophy.id)
        assertEquals(TrophyRarity.LEGENDARY, highestTrophy.rarity)
    }
    
    @Test
    fun `getHighestPrestigeTrophy should return null for empty room`() {
        val highestTrophy = trophyManager.getHighestPrestigeTrophy(testTrophyRoom)
        assertEquals(null, highestTrophy)
    }
    
    @Test
    fun `getDisplayedTrophyCountByRarity should count correctly`() {
        // Display 2 uncommon trophies and 1 legendary
        var room = testTrophyRoom
        
        val result1 = trophyManager.displayTrophy(room, testUnlockedTrophies, "trophy_level_10", 0)  // Uncommon
        assertIs<TrophyDisplayResult.Success>(result1)
        room = result1.newTrophyRoom
        
        val result2 = trophyManager.displayTrophy(room, testUnlockedTrophies, "trophy_level_50", 3)  // Legendary
        assertIs<TrophyDisplayResult.Success>(result2)
        room = result2.newTrophyRoom
        
        assertEquals(1, trophyManager.getDisplayedTrophyCountByRarity(room, TrophyRarity.UNCOMMON))
        assertEquals(1, trophyManager.getDisplayedTrophyCountByRarity(room, TrophyRarity.LEGENDARY))
        assertEquals(0, trophyManager.getDisplayedTrophyCountByRarity(room, TrophyRarity.COMMON))
    }
    
    // ========== EDGE CASES ==========
    
    @Test
    fun `trophy room should handle maximum capacity correctly`() {
        // Fill room to capacity with SMALL trophies (1 slot each)
        var room = TrophyRoom(maxDisplaySlots = 3, displayedTrophies = emptyList(), totalPrestige = 0)
        val unlockedAll = setOf("trophy_first_quest", "trophy_ten_quests", "trophy_combo_master")
        
        val result1 = trophyManager.displayTrophy(room, unlockedAll, "trophy_first_quest", 0)
        assertIs<TrophyDisplayResult.Success>(result1)
        room = result1.newTrophyRoom
        
        val result2 = trophyManager.displayTrophy(room, unlockedAll, "trophy_ten_quests", 1)
        assertIs<TrophyDisplayResult.Success>(result2)
        room = result2.newTrophyRoom
        
        val result3 = trophyManager.displayTrophy(room, unlockedAll, "trophy_combo_master", 2)
        assertIs<TrophyDisplayResult.Success>(result3)
        room = result3.newTrophyRoom
        
        assertEquals(3, room.displayedTrophies.size)
        assertEquals(0, room.getRemainingSlots(TrophyCatalog.getAllTrophies()))
    }
    
    @Test
    fun `trophy prestige multipliers should scale correctly by rarity`() {
        val commonTrophy = TrophyCatalog.getTrophyById("trophy_first_quest")
        val legendaryTrophy = TrophyCatalog.getTrophyById("trophy_level_50")
        
        assertNotNull(commonTrophy)
        assertNotNull(legendaryTrophy)
        
        val commonPrestige = commonTrophy.calculatePrestige()
        val legendaryPrestige = legendaryTrophy.calculatePrestige()
        
        // Legendary should have significantly more prestige due to 5.0x multiplier
        assertTrue(legendaryPrestige > commonPrestige * 3)
    }
}
