package com.jalmarquest.shared.nest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class CritterTest {
    
    private val critterManager = CritterManager()
    
    // =============== Adoption Tests ===============
    
    @Test
    fun `adoptCritter should succeed with valid inputs`() {
        val (critters, result) = critterManager.adoptCritter(
            currentCritters = emptyList(),
            critterId = "ladybug_common",
            nestTier = NestTier.BASIC
        )
        
        assertTrue(result is AdoptResult.Success)
        assertEquals(1, critters.size)
        assertEquals("ladybug_common", critters[0].critterId)
        assertEquals(50, critters[0].currentSatisfaction) // 50% of max 100
    }
    
    @Test
    fun `adoptCritter should fail when nest is full`() {
        val fullNest = List(CritterManager.MAX_CRITTERS_PER_NEST) { index ->
            NestCritter(
                critterId = "ladybug_common",
                customName = "Bug$index",
                currentSatisfaction = 50,
                daysSinceFed = 0,
                totalDaysInNest = 0
            )
        }
        
        val (critters, result) = critterManager.adoptCritter(
            currentCritters = fullNest,
            critterId = "worm_earthworm",
            nestTier = NestTier.BASIC
        )
        
        assertTrue(result is AdoptResult.Failure)
        assertEquals(AdoptFailureReason.NEST_FULL, (result as AdoptResult.Failure).reason)
        assertEquals(fullNest.size, critters.size)
    }
    
    @Test
    fun `adoptCritter should fail when critter not found`() {
        val (critters, result) = critterManager.adoptCritter(
            currentCritters = emptyList(),
            critterId = "nonexistent_critter",
            nestTier = NestTier.BASIC
        )
        
        assertTrue(result is AdoptResult.Failure)
        assertEquals(AdoptFailureReason.CRITTER_NOT_FOUND, (result as AdoptResult.Failure).reason)
        assertEquals(0, critters.size)
    }
    
    @Test
    fun `adoptCritter should fail when already adopted`() {
        val existingCritters = listOf(
            NestCritter(
                critterId = "ladybug_common",
                customName = null,
                currentSatisfaction = 50,
                daysSinceFed = 0,
                totalDaysInNest = 5
            )
        )
        
        val (critters, result) = critterManager.adoptCritter(
            currentCritters = existingCritters,
            critterId = "ladybug_common",
            nestTier = NestTier.BASIC
        )
        
        assertTrue(result is AdoptResult.Failure)
        assertEquals(AdoptFailureReason.ALREADY_ADOPTED, (result as AdoptResult.Failure).reason)
        assertEquals(1, critters.size)
    }
    
    @Test
    fun `adoptCritter should fail when nest tier too low for rare critter`() {
        val (critters, result) = critterManager.adoptCritter(
            currentCritters = emptyList(),
            critterId = "butterfly_monarch", // Requires LUXURIOUS
            nestTier = NestTier.BASIC
        )
        
        assertTrue(result is AdoptResult.Failure)
        assertEquals(AdoptFailureReason.NEST_TIER_TOO_LOW, (result as AdoptResult.Failure).reason)
        assertEquals(0, critters.size)
    }
    
    @Test
    fun `adoptCritter should succeed when nest tier matches requirement`() {
        val (critters, result) = critterManager.adoptCritter(
            currentCritters = emptyList(),
            critterId = "butterfly_monarch", // Requires LUXURIOUS
            nestTier = NestTier.LUXURIOUS
        )
        
        assertTrue(result is AdoptResult.Success)
        assertEquals(1, critters.size)
        assertEquals("butterfly_monarch", critters[0].critterId)
    }
    
    // =============== Feeding Tests ===============
    
    @Test
    fun `feedCritter should increase satisfaction and reset hunger`() {
        val critter = NestCritter(
            critterId = "ladybug_common",
            customName = null,
            currentSatisfaction = 30,
            daysSinceFed = 5,
            totalDaysInNest = 10
        )
        
        val (updated, result) = critterManager.feedCritter(critter)
        
        assertTrue(result is FeedResult.Success)
        assertEquals(50, updated.currentSatisfaction) // 30 + 20
        assertEquals(0, updated.daysSinceFed)
        assertEquals(10, updated.totalDaysInNest) // Unchanged
    }
    
    @Test
    fun `feedCritter should cap satisfaction at max`() {
        val critter = NestCritter(
            critterId = "ladybug_common", // Max satisfaction = 100
            customName = null,
            currentSatisfaction = 95,
            daysSinceFed = 3,
            totalDaysInNest = 5
        )
        
        val (updated, result) = critterManager.feedCritter(critter)
        
        assertTrue(result is FeedResult.Success)
        assertEquals(100, updated.currentSatisfaction) // Capped at max
        assertEquals(0, updated.daysSinceFed)
    }
    
    @Test
    fun `feedCritter should fail for nonexistent critter`() {
        val critter = NestCritter(
            critterId = "nonexistent_critter",
            customName = null,
            currentSatisfaction = 50,
            daysSinceFed = 0,
            totalDaysInNest = 0
        )
        
        val (updated, result) = critterManager.feedCritter(critter)
        
        assertTrue(result is FeedResult.Failure)
        assertEquals(critter, updated) // Unchanged
    }
    
    // =============== Satisfaction Calculation Tests ===============
    
    @Test
    fun `calculateSatisfaction should apply daily decay`() {
        val critter = CritterCatalog.getCritterById("ladybug_common")!! // decay 3/day
        val nestCritter = NestCritter(
            critterId = "ladybug_common",
            customName = null,
            currentSatisfaction = 100,
            daysSinceFed = 5, // 5 days * 3 decay = -15 satisfaction
            totalDaysInNest = 10
        )
        
        val satisfaction = critterManager.calculateSatisfaction(
            critter = critter,
            nestCritter = nestCritter,
            nestTier = NestTier.BASIC,
            placedCosmetics = emptyList()
        )
        
        assertEquals(85, satisfaction) // 100 - (5 * 3)
    }
    
    @Test
    fun `calculateSatisfaction should add bonus for matching nest tier`() {
        val critter = CritterCatalog.getCritterById("worm_earthworm")!! // Prefers BASIC
        val nestCritter = NestCritter(
            critterId = "worm_earthworm",
            customName = null,
            currentSatisfaction = 50,
            daysSinceFed = 0,
            totalDaysInNest = 0
        )
        
        val satisfaction = critterManager.calculateSatisfaction(
            critter = critter,
            nestCritter = nestCritter,
            nestTier = NestTier.BASIC,
            placedCosmetics = emptyList()
        )
        
        assertEquals(65, satisfaction) // 50 + 15 (tier match)
    }
    
    @Test
    fun `calculateSatisfaction should add bonus from preferred cosmetics`() {
        val critter = CritterCatalog.getCritterById("ladybug_common")!! // Loves PLANT (+10/item)
        val nestCritter = NestCritter(
            critterId = "ladybug_common",
            customName = null,
            currentSatisfaction = 50,
            daysSinceFed = 0,
            totalDaysInNest = 0
        )
        
        val placedCosmetics = listOf(
            PlacedCosmetic("plant_tiny_fern", 0, 0), // PLANT
            PlacedCosmetic("plant_moss_patch", 1, 0)  // PLANT
        )
        
        val satisfaction = critterManager.calculateSatisfaction(
            critter = critter,
            nestCritter = nestCritter,
            nestTier = NestTier.BASIC,
            placedCosmetics = placedCosmetics
        )
        
        assertEquals(70, satisfaction) // 50 + (2 * 10)
    }
    
    @Test
    fun `calculateSatisfaction should combine multiple bonuses`() {
        val critter = CritterCatalog.getCritterById("worm_earthworm")!! // Prefers BASIC, loves FLOOR_ITEM (+8) & PLANT (+5)
        val nestCritter = NestCritter(
            critterId = "worm_earthworm",
            customName = null,
            currentSatisfaction = 50,
            daysSinceFed = 2, // -4 satisfaction (2 decay/day)
            totalDaysInNest = 10
        )
        
        val placedCosmetics = listOf(
            PlacedCosmetic("floor_leaf_rug", 0, 0),      // FLOOR_ITEM
            PlacedCosmetic("plant_tiny_fern", 1, 0),    // PLANT
            PlacedCosmetic("plant_moss_patch", 2, 0)     // PLANT
        )
        
        val satisfaction = critterManager.calculateSatisfaction(
            critter = critter,
            nestCritter = nestCritter,
            nestTier = NestTier.BASIC,
            placedCosmetics = placedCosmetics
        )
        
        // 50 (base) - 4 (decay) + 15 (tier) + 8 (1 FLOOR_ITEM) + 10 (2 PLANT) = 79
        assertEquals(79, satisfaction)
    }
    
    @Test
    fun `calculateSatisfaction should clamp to zero`() {
        val critter = CritterCatalog.getCritterById("ladybug_common")!!
        val nestCritter = NestCritter(
            critterId = "ladybug_common",
            customName = null,
            currentSatisfaction = 10,
            daysSinceFed = 20, // 20 * 2 = -40 satisfaction
            totalDaysInNest = 20
        )
        
        val satisfaction = critterManager.calculateSatisfaction(
            critter = critter,
            nestCritter = nestCritter,
            nestTier = NestTier.BASIC,
            placedCosmetics = emptyList()
        )
        
        assertEquals(0, satisfaction) // Clamped at 0
    }
    
    @Test
    fun `calculateSatisfaction should clamp to max satisfaction`() {
        val critter = CritterCatalog.getCritterById("ladybug_common")!! // Max = 100
        val nestCritter = NestCritter(
            critterId = "ladybug_common",
            customName = null,
            currentSatisfaction = 100,
            daysSinceFed = 0,
            totalDaysInNest = 0
        )
        
        val placedCosmetics = List(20) { index ->
            PlacedCosmetic("plant_tiny_fern", index, 0) // 20 PLANT items
        }
        
        val satisfaction = critterManager.calculateSatisfaction(
            critter = critter,
            nestCritter = nestCritter,
            nestTier = NestTier.BASIC,
            placedCosmetics = placedCosmetics
        )
        
        assertEquals(100, satisfaction) // Capped at max
    }
    
    // =============== Bonus Calculation Tests ===============
    
    @Test
    fun `calculateTotalBonus should return correct value for single critter`() {
        val critters = listOf(
            NestCritter(
                critterId = "ladybug_common", // LUCK +5, COMMON (1.0x), max 100
                customName = null,
                currentSatisfaction = 75, // 75% → CONTENT level (1.0x)
                daysSinceFed = 0,
                totalDaysInNest = 0
            )
        )
        
        val bonus = critterManager.calculateTotalBonus(critters, CritterBonusType.LUCK)
        
        assertEquals(5, bonus) // 5 * 1.0 * 1.0 = 5
    }
    
    @Test
    fun `calculateTotalBonus should sum bonuses from multiple critters`() {
        val critters = listOf(
            NestCritter(
                critterId = "ladybug_common", // LUCK +5, COMMON, max 100
                customName = null,
                currentSatisfaction = 75, // 75% → CONTENT (1.0x)
                daysSinceFed = 0,
                totalDaysInNest = 0
            ),
            NestCritter(
                critterId = "ant_worker", // ITEM_FIND +4, COMMON, max 90
                customName = null,
                currentSatisfaction = 67, // 67/90 = 74.4% → CONTENT (1.0x)
                daysSinceFed = 0,
                totalDaysInNest = 0
            )
        )
        
        val luckBonus = critterManager.calculateTotalBonus(critters, CritterBonusType.LUCK)
        val itemFindBonus = critterManager.calculateTotalBonus(critters, CritterBonusType.ITEM_FIND)
        
        assertEquals(5, luckBonus) // Only ladybug
        assertEquals(4, itemFindBonus) // Only ant
    }
    
    @Test
    fun `calculateTotalBonus should apply rarity multiplier`() {
        val critters = listOf(
            NestCritter(
                critterId = "firefly_rainbow", // STAMINA_REGEN +20, LEGENDARY (3.0x), max 200
                customName = null,
                currentSatisfaction = 160, // 160/200 = 80% → HAPPY level (1.5x)
                daysSinceFed = 0,
                totalDaysInNest = 0
            )
        )
        
        val bonus = critterManager.calculateTotalBonus(critters, CritterBonusType.STAMINA_REGEN)
        
        assertEquals(90, bonus) // 20 * 3.0 * 1.5 = 90
    }
    
    @Test
    fun `calculateTotalBonus should apply satisfaction multiplier`() {
        val critters = listOf(
            NestCritter(
                critterId = "ladybug_common", // LUCK +5, COMMON (1.0x)
                customName = null,
                currentSatisfaction = 90, // HAPPY level (1.5x)
                daysSinceFed = 0,
                totalDaysInNest = 0
            )
        )
        
        val bonus = critterManager.calculateTotalBonus(critters, CritterBonusType.LUCK)
        
        assertEquals(7, bonus) // 5 * 1.0 * 1.5 = 7.5 -> 7
    }
    
    @Test
    fun `calculateTotalBonus should return zero for miserable critters`() {
        val critters = listOf(
            NestCritter(
                critterId = "ladybug_common", // LUCK +5
                customName = null,
                currentSatisfaction = 5, // MISERABLE level (0.0x)
                daysSinceFed = 0,
                totalDaysInNest = 0
            )
        )
        
        val bonus = critterManager.calculateTotalBonus(critters, CritterBonusType.LUCK)
        
        assertEquals(0, bonus) // 5 * 1.0 * 0.0 = 0
    }
    
    @Test
    fun `getAllBonuses should return map of all active bonuses`() {
        val critters = listOf(
            NestCritter(
                critterId = "ladybug_common", // LUCK +5, max 100
                customName = null,
                currentSatisfaction = 75, // 75% → CONTENT (1.0x)
                daysSinceFed = 0,
                totalDaysInNest = 0
            ),
            NestCritter(
                critterId = "worm_earthworm", // HP_REGEN +3, max 80
                customName = null,
                currentSatisfaction = 60, // 60/80 = 75% → CONTENT (1.0x)
                daysSinceFed = 0,
                totalDaysInNest = 0
            )
        )
        
        val bonuses = critterManager.getAllBonuses(critters)
        
        assertEquals(2, bonuses.size)
        assertEquals(5, bonuses[CritterBonusType.LUCK])
        assertEquals(3, bonuses[CritterBonusType.HP_REGEN])
    }
    
    // =============== Daily Update Tests ===============
    
    @Test
    fun `advanceDay should increment days and recalculate satisfaction`() {
        val critters = listOf(
            NestCritter(
                critterId = "ladybug_common", // max 100, decay 3/day
                customName = null,
                currentSatisfaction = 100,
                daysSinceFed = 0,
                totalDaysInNest = 5
            )
        )
        
        val updated = critterManager.advanceDay(critters, NestTier.BASIC, emptyList())
        
        assertEquals(1, updated.size)
        assertEquals(1, updated[0].daysSinceFed)
        assertEquals(6, updated[0].totalDaysInNest)
        assertEquals(97, updated[0].currentSatisfaction) // 100 - (1 * 3 decay)
    }
    
    @Test
    fun `advanceDay should remove critters that leave due to low satisfaction`() {
        val critters = listOf(
            NestCritter(
                critterId = "ladybug_common",
                customName = null,
                currentSatisfaction = 5, // Will be MISERABLE
                daysSinceFed = 10,
                totalDaysInNest = 10 // More than 7 days
            )
        )
        
        val updated = critterManager.advanceDay(critters, NestTier.BASIC, emptyList())
        
        assertEquals(0, updated.size) // Critter left
    }
    
    @Test
    fun `advanceDay should keep critters under 7 days even if miserable`() {
        val critters = listOf(
            NestCritter(
                critterId = "ladybug_common",
                customName = null,
                currentSatisfaction = 5,
                daysSinceFed = 10,
                totalDaysInNest = 5 // Less than 7 days
            )
        )
        
        val updated = critterManager.advanceDay(critters, NestTier.BASIC, emptyList())
        
        assertEquals(1, updated.size) // Critter stays (grace period)
    }
    
    // =============== Utility Tests ===============
    
    @Test
    fun `releaseCritter should remove critter from list`() {
        val critters = listOf(
            NestCritter("ladybug_common", null, 50, 0, 5),
            NestCritter("worm_earthworm", null, 60, 1, 3),
            NestCritter("ant_worker", null, 70, 2, 10)
        )
        
        val updated = critterManager.releaseCritter(critters, "worm_earthworm")
        
        assertEquals(2, updated.size)
        assertFalse(updated.any { it.critterId == "worm_earthworm" })
        assertTrue(updated.any { it.critterId == "ladybug_common" })
        assertTrue(updated.any { it.critterId == "ant_worker" })
    }
    
    @Test
    fun `renameCritter should update custom name`() {
        val critters = listOf(
            NestCritter("ladybug_common", null, 50, 0, 5)
        )
        
        val updated = critterManager.renameCritter(critters, "ladybug_common", "Spotty")
        
        assertEquals("Spotty", updated[0].customName)
    }
    
    @Test
    fun `canAdopt should return true for valid adoption`() {
        val canAdopt = critterManager.canAdopt(
            currentCritters = emptyList(),
            critterId = "ladybug_common",
            nestTier = NestTier.BASIC
        )
        
        assertTrue(canAdopt)
    }
    
    @Test
    fun `canAdopt should return false when nest full`() {
        val fullNest = List(CritterManager.MAX_CRITTERS_PER_NEST) { index ->
            NestCritter("ladybug_common", "Bug$index", 50, 0, 0)
        }
        
        val canAdopt = critterManager.canAdopt(
            currentCritters = fullNest,
            critterId = "worm_earthworm",
            nestTier = NestTier.BASIC
        )
        
        assertFalse(canAdopt)
    }
    
    @Test
    fun `canAdopt should return false for tier requirement mismatch`() {
        val canAdopt = critterManager.canAdopt(
            currentCritters = emptyList(),
            critterId = "butterfly_monarch", // Requires LUXURIOUS
            nestTier = NestTier.BASIC
        )
        
        assertFalse(canAdopt)
    }
}
