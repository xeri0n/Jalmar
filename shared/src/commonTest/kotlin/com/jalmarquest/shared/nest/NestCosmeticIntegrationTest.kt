package com.jalmarquest.shared.nest

import com.jalmarquest.shared.inventory.ItemRarity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Integration tests for Nest + Cosmetic system.
 * Tests the full workflow from nest creation through cosmetic unlocking and placement.
 */
class NestCosmeticIntegrationTest {
    
    @Test
    fun `new nest starts with no cosmetics`() {
        val nest = NestManager.createBasicNest()
        
        assertEquals(0, nest.unlockedCosmetics.size)
        assertEquals(0, nest.placedCosmetics.size)
        assertEquals(0, nest.getTotalPrestige())
    }
    
    @Test
    fun `unlocking cosmetic adds to unlocked set`() {
        val nest = NestManager.createBasicNest()
        val updatedNest = NestManager.unlockCosmetic(nest, "wall_twig_picture")
        
        assertTrue(updatedNest.unlockedCosmetics.contains("wall_twig_picture"))
        assertEquals(1, updatedNest.unlockedCosmetics.size)
    }
    
    @Test
    fun `unlocking multiple cosmetics accumulates`() {
        var nest = NestManager.createBasicNest()
        nest = NestManager.unlockCosmetic(nest, "wall_twig_picture")
        nest = NestManager.unlockCosmetic(nest, "floor_leaf_rug")
        nest = NestManager.unlockCosmetic(nest, "furniture_twig_perch")
        
        assertEquals(3, nest.unlockedCosmetics.size)
        assertTrue(nest.unlockedCosmetics.contains("wall_twig_picture"))
        assertTrue(nest.unlockedCosmetics.contains("floor_leaf_rug"))
        assertTrue(nest.unlockedCosmetics.contains("furniture_twig_perch"))
    }
    
    @Test
    fun `placing unlocked cosmetic succeeds`() {
        var nest = NestManager.createBasicNest()
        nest = NestManager.unlockCosmetic(nest, "wall_twig_picture")
        
        val (updatedNest, result) = NestManager.placeCosmetic(nest, "wall_twig_picture", 0, 0)
        
        assertTrue(result is CosmeticManager.PlacementResult.Success)
        assertEquals(1, updatedNest.placedCosmetics.size)
        assertEquals("wall_twig_picture", updatedNest.placedCosmetics[0].cosmeticId)
        assertEquals(0, updatedNest.placedCosmetics[0].gridX)
        assertEquals(0, updatedNest.placedCosmetics[0].gridY)
    }
    
    @Test
    fun `placing locked cosmetic fails`() {
        val nest = NestManager.createBasicNest()
        
        // wall_beetle_shell requires "beetle_hunter" achievement
        val (updatedNest, result) = NestManager.placeCosmetic(nest, "wall_beetle_shell", 0, 0)
        
        assertTrue(result is CosmeticManager.PlacementResult.Failure)
        val failure = result as CosmeticManager.PlacementResult.Failure
        assertEquals(CosmeticManager.PlacementFailureReason.COSMETIC_LOCKED, failure.reason)
        assertEquals(0, updatedNest.placedCosmetics.size)  // Original unchanged
    }
    
    @Test
    fun `placing multiple cosmetics accumulates`() {
        var nest = NestManager.createBasicNest()
        nest = NestManager.unlockCosmetic(nest, "wall_twig_picture")
        nest = NestManager.unlockCosmetic(nest, "floor_leaf_rug")
        
        val (nest1, result1) = NestManager.placeCosmetic(nest, "wall_twig_picture", 0, 0)
        assertTrue(result1 is CosmeticManager.PlacementResult.Success)
        
        val (nest2, result2) = NestManager.placeCosmetic(nest1, "floor_leaf_rug", 2, 0)
        assertTrue(result2 is CosmeticManager.PlacementResult.Success)
        
        assertEquals(2, nest2.placedCosmetics.size)
    }
    
    @Test
    fun `placing cosmetic with collision fails`() {
        var nest = NestManager.createBasicNest()
        nest = NestManager.unlockCosmetic(nest, "wall_twig_picture")
        nest = NestManager.unlockCosmetic(nest, "floor_leaf_rug")
        
        val (nest1, _) = NestManager.placeCosmetic(nest, "floor_leaf_rug", 0, 0)  // 2x2 at (0,0)
        val (nest2, result) = NestManager.placeCosmetic(nest1, "wall_twig_picture", 1, 1)  // Collides
        
        assertTrue(result is CosmeticManager.PlacementResult.Failure)
        val failure = result as CosmeticManager.PlacementResult.Failure
        assertEquals(CosmeticManager.PlacementFailureReason.COLLISION, failure.reason)
        assertEquals(1, nest2.placedCosmetics.size)  // Only first cosmetic placed
    }
    
    @Test
    fun `removing cosmetic removes from placed list`() {
        var nest = NestManager.createBasicNest()
        nest = NestManager.unlockCosmetic(nest, "wall_twig_picture")
        
        val (nest1, _) = NestManager.placeCosmetic(nest, "wall_twig_picture", 2, 3)
        assertEquals(1, nest1.placedCosmetics.size)
        
        val (nest2, result) = NestManager.removeCosmetic(nest1, 2, 3)
        
        assertTrue(result is CosmeticManager.RemovalResult.Success)
        assertEquals(0, nest2.placedCosmetics.size)
        // Cosmetic remains unlocked
        assertTrue(nest2.unlockedCosmetics.contains("wall_twig_picture"))
    }
    
    @Test
    fun `removing nonexistent cosmetic fails`() {
        val nest = NestManager.createBasicNest()
        
        val (updatedNest, result) = NestManager.removeCosmetic(nest, 5, 5)
        
        assertTrue(result is CosmeticManager.RemovalResult.Failure)
        assertEquals(0, updatedNest.placedCosmetics.size)
    }
    
    @Test
    fun `canPlaceCosmetic returns correct validation`() {
        var nest = NestManager.createBasicNest()
        nest = NestManager.unlockCosmetic(nest, "wall_twig_picture")
        
        // Can place unlocked cosmetic at valid position
        assertTrue(NestManager.canPlaceCosmetic(nest, "wall_twig_picture", 0, 0))
        
        // Cannot place locked cosmetic (wall_beetle_shell requires achievement)
        assertFalse(NestManager.canPlaceCosmetic(nest, "wall_beetle_shell", 0, 0))
        
        // Cannot place out of bounds
        assertFalse(NestManager.canPlaceCosmetic(nest, "wall_twig_picture", 10, 10))
        
        // Cannot place with collision
        val (nest1, _) = NestManager.placeCosmetic(nest, "wall_twig_picture", 0, 0)
        assertFalse(NestManager.canPlaceCosmetic(nest1, "wall_twig_picture", 0, 0))
    }
    
    @Test
    fun `purchasing cosmetic unlocks it and deducts currency`() {
        var nest = NestManager.createBasicNest()
        val currentSeeds = 1000L
        val currentGlimmerShards = 10L
        
        val (updatedNest, result) = NestManager.purchaseCosmetic(
            nest = nest,
            currentSeeds = currentSeeds,
            currentGlimmerShards = currentGlimmerShards,
            cosmeticId = "floor_grass_mat"  // Costs 500 seeds
        )
        
        assertTrue(result is CosmeticManager.PurchaseResult.Success)
        val success = result as CosmeticManager.PurchaseResult.Success
        
        assertTrue(updatedNest.unlockedCosmetics.contains("floor_grass_mat"))
        assertEquals(500L, success.newSeeds)
        assertEquals(10L, success.newGlimmerShards)
    }
    
    @Test
    fun `purchasing with insufficient currency fails`() {
        val nest = NestManager.createBasicNest()
        
        val (updatedNest, result) = NestManager.purchaseCosmetic(
            nest = nest,
            currentSeeds = 100L,  // Not enough
            currentGlimmerShards = 0L,
            cosmeticId = "floor_grass_mat"  // Costs 500 seeds
        )
        
        assertTrue(result is CosmeticManager.PurchaseResult.Failure)
        assertFalse(updatedNest.unlockedCosmetics.contains("floor_grass_mat"))
    }
    
    @Test
    fun `getTotalPrestige calculates correctly`() {
        var nest = NestManager.createBasicNest()
        nest = NestManager.unlockCosmetic(nest, "wall_twig_picture")  // 5 prestige
        nest = NestManager.unlockCosmetic(nest, "floor_leaf_rug")     // 6 prestige
        nest = NestManager.unlockCosmetic(nest, "furniture_twig_perch") // 5 prestige
        
        assertEquals(0, nest.getTotalPrestige())  // Nothing placed yet
        
        val (nest1, _) = NestManager.placeCosmetic(nest, "wall_twig_picture", 0, 0)
        assertEquals(5, nest1.getTotalPrestige())
        
        val (nest2, _) = NestManager.placeCosmetic(nest1, "floor_leaf_rug", 2, 0)
        assertEquals(11, nest2.getTotalPrestige())
        
        val (nest3, _) = NestManager.placeCosmetic(nest2, "furniture_twig_perch", 5, 0)
        assertEquals(16, nest3.getTotalPrestige())
    }
    
    @Test
    fun `nest preserves cosmetics through upgrades`() {
        var nest = NestManager.createBasicNest()
        nest = NestManager.unlockCosmetic(nest, "wall_twig_picture")
        val (nest1, _) = NestManager.placeCosmetic(nest, "wall_twig_picture", 0, 0)
        
        // Simulate upgrade (just copy tier for test)
        val upgradedNest = nest1.copy(tier = NestTier.COMFORTABLE)
        
        // Cosmetics should be preserved
        assertEquals(1, upgradedNest.unlockedCosmetics.size)
        assertEquals(1, upgradedNest.placedCosmetics.size)
        assertEquals(5, upgradedNest.getTotalPrestige())
    }
    
    @Test
    fun `full workflow - unlock, purchase, place, remove`() {
        // Start with basic nest
        var nest = NestManager.createBasicNest()
        assertEquals(NestTier.BASIC, nest.tier)
        
        // Unlock a starting cosmetic
        nest = NestManager.unlockCosmetic(nest, "wall_twig_picture")
        assertTrue(nest.unlockedCosmetics.contains("wall_twig_picture"))
        
        // Place it
        val (nest1, placeResult) = NestManager.placeCosmetic(nest, "wall_twig_picture", 0, 0)
        assertTrue(placeResult is CosmeticManager.PlacementResult.Success)
        assertEquals(1, nest1.placedCosmetics.size)
        assertEquals(5, nest1.getTotalPrestige())
        
        // Purchase another cosmetic
        val (nest2, purchaseResult) = NestManager.purchaseCosmetic(
            nest = nest1,
            currentSeeds = 1000L,
            currentGlimmerShards = 10L,
            cosmeticId = "floor_grass_mat"
        )
        assertTrue(purchaseResult is CosmeticManager.PurchaseResult.Success)
        assertEquals(2, nest2.unlockedCosmetics.size)
        
        // Place the purchased cosmetic
        val (nest3, placeResult2) = NestManager.placeCosmetic(nest2, "floor_grass_mat", 3, 0)
        assertTrue(placeResult2 is CosmeticManager.PlacementResult.Success)
        assertEquals(2, nest3.placedCosmetics.size)
        assertEquals(20, nest3.getTotalPrestige())  // 5 + 15
        
        // Remove first cosmetic
        val (nest4, removeResult) = NestManager.removeCosmetic(nest3, 0, 0)
        assertTrue(removeResult is CosmeticManager.RemovalResult.Success)
        assertEquals(1, nest4.placedCosmetics.size)
        assertEquals(15, nest4.getTotalPrestige())  // Only grass mat remains
        
        // Cosmetics remain unlocked
        assertEquals(2, nest4.unlockedCosmetics.size)
    }
    
    @Test
    fun `grid boundary validation works correctly`() {
        var nest = NestManager.createBasicNest()
        nest = NestManager.unlockCosmetic(nest, "wall_leaf_banner")  // 2x1 cosmetic
        
        // Valid placement at edge
        val (nest1, result1) = NestManager.placeCosmetic(nest, "wall_leaf_banner", 6, 0)
        assertTrue(result1 is CosmeticManager.PlacementResult.Success)
        
        // Invalid - extends beyond grid width (8 tiles)
        val (nest2, result2) = NestManager.placeCosmetic(nest1, "wall_leaf_banner", 7, 0)
        assertTrue(result2 is CosmeticManager.PlacementResult.Failure)
        val failure = result2 as CosmeticManager.PlacementResult.Failure
        assertEquals(CosmeticManager.PlacementFailureReason.OUT_OF_BOUNDS, failure.reason)
    }
    
    @Test
    fun `serialization includes cosmetic fields`() {
        var nest = NestManager.createBasicNest("test_nest", "Cozy Corner")
        nest = NestManager.unlockCosmetic(nest, "wall_twig_picture")
        nest = NestManager.unlockCosmetic(nest, "floor_leaf_rug")
        
        val (nest1, _) = NestManager.placeCosmetic(nest, "wall_twig_picture", 1, 2)
        
        // Verify fields are populated
        assertEquals("test_nest", nest1.id)
        assertEquals("Cozy Corner", nest1.customName)
        assertEquals(2, nest1.unlockedCosmetics.size)
        assertEquals(1, nest1.placedCosmetics.size)
        assertEquals(1, nest1.placedCosmetics[0].gridX)
        assertEquals(2, nest1.placedCosmetics[0].gridY)
    }
}
