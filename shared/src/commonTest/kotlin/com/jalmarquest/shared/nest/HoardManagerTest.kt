package com.jalmarquest.shared.nest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class HoardManagerTest {
    
    private val hoardManager = HoardManager()
    
    // Empty hoard for testing
    private val emptyHoard = Hoard(
        items = emptyList(),
        totalValue = 0,
        prestigeBonus = 0,
        completedSets = emptySet()
    )
    
    // ========== ADD OPERATIONS ==========
    
    @Test
    fun `addToHoard should succeed with valid item`() {
        val result = hoardManager.addToHoard(
            hoard = emptyHoard,
            itemId = "button_red_ruby",
            condition = HoardCondition.GOOD
        )
        
        assertIs<HoardResult.Success>(result)
        assertEquals(1, result.newHoard.items.size)
        assertTrue(result.valueChange > 0)
    }
    
    @Test
    fun `addToHoard should fail when item not found`() {
        val result = hoardManager.addToHoard(
            hoard = emptyHoard,
            itemId = "nonexistent_item"
        )
        
        assertIs<HoardResult.Failure>(result)
        assertEquals(HoardFailure.ITEM_NOT_FOUND, result.reason)
    }
    
    @Test
    fun `addToHoard should fail when item already in hoard`() {
        // Add item first time
        val firstResult = hoardManager.addToHoard(emptyHoard, "button_red_ruby")
        assertIs<HoardResult.Success>(firstResult)
        
        // Try to add same item again
        val secondResult = hoardManager.addToHoard(firstResult.newHoard, "button_red_ruby")
        assertIs<HoardResult.Failure>(secondResult)
        assertEquals(HoardFailure.ITEM_ALREADY_IN_HOARD, secondResult.reason)
    }
    
    @Test
    fun `addToHoard should apply condition multiplier correctly`() {
        val pristineResult = hoardManager.addToHoard(
            hoard = emptyHoard,
            itemId = "button_red_ruby",
            condition = HoardCondition.PRISTINE
        )
        assertIs<HoardResult.Success>(pristineResult)
        
        val poorResult = hoardManager.addToHoard(
            hoard = Hoard(),
            itemId = "button_red_ruby",
            condition = HoardCondition.POOR
        )
        assertIs<HoardResult.Success>(poorResult)
        
        // Pristine should be worth more than poor
        assertTrue(pristineResult.newHoard.totalValue > poorResult.newHoard.totalValue)
    }
    
    // ========== REMOVE OPERATIONS ==========
    
    @Test
    fun `removeFromHoard should succeed with valid item`() {
        // Add item first
        val addResult = hoardManager.addToHoard(emptyHoard, "button_red_ruby")
        assertIs<HoardResult.Success>(addResult)
        
        // Remove it
        val removeResult = hoardManager.removeFromHoard(addResult.newHoard, "button_red_ruby")
        assertIs<HoardResult.Success>(removeResult)
        assertEquals(0, removeResult.newHoard.items.size)
        assertTrue(removeResult.valueChange < 0)
    }
    
    @Test
    fun `removeFromHoard should fail when item not in hoard`() {
        val result = hoardManager.removeFromHoard(emptyHoard, "button_red_ruby")
        
        assertIs<HoardResult.Failure>(result)
        assertEquals(HoardFailure.ITEM_NOT_IN_HOARD, result.reason)
    }
    
    // ========== VALUE CALCULATION ==========
    
    @Test
    fun `calculateTotalValue should return zero for empty hoard`() {
        val totalValue = hoardManager.calculateTotalValue(emptyHoard)
        assertEquals(0, totalValue)
    }
    
    @Test
    fun `calculateTotalValue should sum all item values`() {
        var hoard = emptyHoard
        
        // Add multiple items
        val items = listOf("button_red_ruby", "button_orange_amber", "crystal_quartz_clear")
        items.forEach { itemId ->
            val result = hoardManager.addToHoard(hoard, itemId)
            assertIs<HoardResult.Success>(result)
            hoard = result.newHoard
        }
        
        val calculatedValue = hoardManager.calculateTotalValue(hoard)
        assertEquals(hoard.totalValue, calculatedValue)
        assertTrue(calculatedValue > 0)
    }
    
    @Test
    fun `mythical items should have massive value`() {
        val result = hoardManager.addToHoard(
            hoard = emptyHoard,
            itemId = "special_fallen_star",  // Mythical rarity
            condition = HoardCondition.PRISTINE
        )
        assertIs<HoardResult.Success>(result)
        
        // Mythical (50x multiplier) + Pristine (2x) = 100x base value!
        assertTrue(result.newHoard.totalValue > 200000)  // Should be huge
    }
    
    // ========== RANKING SYSTEM ==========
    
    @Test
    fun `getRank should return NOVICE_COLLECTOR for zero value`() {
        val rank = hoardManager.getRank(0)
        assertEquals(HoardRank.NOVICE_COLLECTOR, rank)
    }
    
    @Test
    fun `getRank should return correct tier for each threshold`() {
        assertEquals(HoardRank.AMATEUR_HOARDER, hoardManager.getRank(1000))
        assertEquals(HoardRank.SKILLED_COLLECTOR, hoardManager.getRank(5000))
        assertEquals(HoardRank.EXPERT_HOARDER, hoardManager.getRank(15000))
        assertEquals(HoardRank.MASTER_COLLECTOR, hoardManager.getRank(40000))
        assertEquals(HoardRank.LEGENDARY_HOARDER, hoardManager.getRank(100000))
        assertEquals(HoardRank.MYTHICAL_DRAGON, hoardManager.getRank(250000))
    }
    
    @Test
    fun `getRank should use highest qualifying tier`() {
        val rank = hoardManager.getRank(10000)
        assertEquals(HoardRank.SKILLED_COLLECTOR, rank)  // Not EXPERT (needs 15k)
    }
    
    // ========== LEADERBOARD ==========
    
    @Test
    fun `createLeaderboardEntry should include correct data`() {
        var hoard = emptyHoard
        
        // Add some items including rare ones
        val addResult1 = hoardManager.addToHoard(hoard, "gem_diamond_tiny")  // Legendary
        assertIs<HoardResult.Success>(addResult1)
        hoard = addResult1.newHoard
        
        val addResult2 = hoardManager.addToHoard(hoard, "button_red_ruby")  // Uncommon
        assertIs<HoardResult.Success>(addResult2)
        hoard = addResult2.newHoard
        
        val entry = hoardManager.createLeaderboardEntry("TestPlayer", hoard)
        
        assertEquals("TestPlayer", entry.playerName)
        assertEquals(2, entry.itemCount)
        assertEquals(1, entry.rareItemCount)  // Only the legendary diamond
        assertTrue(entry.totalValue > 0)
        assertNotNull(entry.rank)
    }
    
    // ========== COLLECTION SETS ==========
    
    @Test
    fun `adding all rainbow buttons should complete the set`() {
        var hoard = emptyHoard
        
        // Add all 7 rainbow buttons
        val rainbowButtons = listOf(
            "button_red_ruby",
            "button_orange_amber",
            "button_yellow_gold",
            "button_green_emerald",
            "button_blue_sapphire",
            "button_indigo_midnight",
            "button_violet_amethyst"
        )
        
        rainbowButtons.forEach { buttonId ->
            val result = hoardManager.addToHoard(hoard, buttonId)
            assertIs<HoardResult.Success>(result)
            hoard = result.newHoard
        }
        
        assertTrue(hoard.completedSets.contains("rainbow_buttons"))
        assertTrue(hoard.prestigeBonus > 0)  // Should have set bonus prestige
    }
    
    @Test
    fun `getSetCompletionPercentage should calculate correctly`() {
        var hoard = emptyHoard
        
        // Add 3 out of 7 rainbow buttons (42.8% ≈ 42%)
        val result1 = hoardManager.addToHoard(hoard, "button_red_ruby")
        assertIs<HoardResult.Success>(result1)
        hoard = result1.newHoard
        
        val result2 = hoardManager.addToHoard(hoard, "button_orange_amber")
        assertIs<HoardResult.Success>(result2)
        hoard = result2.newHoard
        
        val result3 = hoardManager.addToHoard(hoard, "button_yellow_gold")
        assertIs<HoardResult.Success>(result3)
        hoard = result3.newHoard
        
        val percentage = hoardManager.getSetCompletionPercentage(hoard, "rainbow_buttons")
        assertEquals(42, percentage)  // 3/7 = 42%
    }
    
    @Test
    fun `getMissingSetItems should return incomplete items`() {
        var hoard = emptyHoard
        
        // Add only one rainbow button
        val result = hoardManager.addToHoard(hoard, "button_red_ruby")
        assertIs<HoardResult.Success>(result)
        hoard = result.newHoard
        
        val missing = hoardManager.getMissingSetItems(hoard, "rainbow_buttons")
        assertEquals(6, missing.size)  // 7 total - 1 owned = 6 missing
        assertFalse(missing.any { it.id == "button_red_ruby" })  // Should not include owned item
    }
    
    // ========== PRESTIGE BONUS ==========
    
    @Test
    fun `epic items should grant prestige bonus`() {
        val result = hoardManager.addToHoard(
            hoard = emptyHoard,
            itemId = "crystal_amethyst_cluster"  // Epic rarity
        )
        assertIs<HoardResult.Success>(result)
        
        assertTrue(result.newHoard.prestigeBonus >= 10)  // Epic = 10 prestige
    }
    
    @Test
    fun `legendary items should grant higher prestige bonus`() {
        val result = hoardManager.addToHoard(
            hoard = emptyHoard,
            itemId = "gem_diamond_tiny"  // Legendary rarity
        )
        assertIs<HoardResult.Success>(result)
        
        assertTrue(result.newHoard.prestigeBonus >= 25)  // Legendary = 25 prestige
    }
    
    @Test
    fun `mythical items should grant massive prestige bonus`() {
        val result = hoardManager.addToHoard(
            hoard = emptyHoard,
            itemId = "special_fallen_star"  // Mythical rarity
        )
        assertIs<HoardResult.Success>(result)
        
        assertTrue(result.newHoard.prestigeBonus >= 100)  // Mythical = 100 prestige
    }
    
    // ========== CONDITION UPDATES ==========
    
    @Test
    fun `updateItemCondition should change value`() {
        // Add item in GOOD condition
        val addResult = hoardManager.addToHoard(
            hoard = emptyHoard,
            itemId = "button_red_ruby",
            condition = HoardCondition.GOOD
        )
        assertIs<HoardResult.Success>(addResult)
        val initialValue = addResult.newHoard.totalValue
        
        // Upgrade to PRISTINE
        val updateResult = hoardManager.updateItemCondition(
            hoard = addResult.newHoard,
            itemId = "button_red_ruby",
            newCondition = HoardCondition.PRISTINE
        )
        assertIs<HoardResult.Success>(updateResult)
        
        assertTrue(updateResult.newHoard.totalValue > initialValue)
        assertTrue(updateResult.valueChange > 0)
    }
    
    @Test
    fun `updateItemCondition should fail for item not in hoard`() {
        val result = hoardManager.updateItemCondition(
            hoard = emptyHoard,
            itemId = "button_red_ruby",
            newCondition = HoardCondition.PRISTINE
        )
        
        assertIs<HoardResult.Failure>(result)
        assertEquals(HoardFailure.ITEM_NOT_IN_HOARD, result.reason)
    }
    
    // ========== HELPER METHODS ==========
    
    @Test
    fun `getItemCountByType should count correctly`() {
        var hoard = emptyHoard
        
        // Add 2 buttons and 1 crystal
        val result1 = hoardManager.addToHoard(hoard, "button_red_ruby")
        assertIs<HoardResult.Success>(result1)
        hoard = result1.newHoard
        
        val result2 = hoardManager.addToHoard(hoard, "button_orange_amber")
        assertIs<HoardResult.Success>(result2)
        hoard = result2.newHoard
        
        val result3 = hoardManager.addToHoard(hoard, "crystal_quartz_clear")
        assertIs<HoardResult.Success>(result3)
        hoard = result3.newHoard
        
        assertEquals(2, hoardManager.getItemCountByType(hoard, HoardItemType.BUTTON))
        assertEquals(1, hoardManager.getItemCountByType(hoard, HoardItemType.CRYSTAL))
        assertEquals(0, hoardManager.getItemCountByType(hoard, HoardItemType.COIN))
    }
    
    @Test
    fun `getItemCountByRarity should count correctly`() {
        var hoard = emptyHoard
        
        // Add items of different rarities
        val result1 = hoardManager.addToHoard(hoard, "button_red_ruby")  // Uncommon
        assertIs<HoardResult.Success>(result1)
        hoard = result1.newHoard
        
        val result2 = hoardManager.addToHoard(hoard, "crystal_quartz_clear")  // Rare
        assertIs<HoardResult.Success>(result2)
        hoard = result2.newHoard
        
        val result3 = hoardManager.addToHoard(hoard, "gem_diamond_tiny")  // Legendary
        assertIs<HoardResult.Success>(result3)
        hoard = result3.newHoard
        
        assertEquals(1, hoardManager.getItemCountByRarity(hoard, HoardRarity.UNCOMMON))
        assertEquals(1, hoardManager.getItemCountByRarity(hoard, HoardRarity.RARE))
        assertEquals(1, hoardManager.getItemCountByRarity(hoard, HoardRarity.LEGENDARY))
        assertEquals(0, hoardManager.getItemCountByRarity(hoard, HoardRarity.MYTHICAL))
    }
}
