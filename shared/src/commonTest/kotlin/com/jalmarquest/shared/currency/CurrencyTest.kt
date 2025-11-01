package com.jalmarquest.shared.currency

import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.model.PlayerStats
import com.jalmarquest.shared.model.Position
import kotlin.test.*

/**
 * Tests for the Currency system including CurrencyManager operations,
 * overflow protection, validation, and transaction safety.
 */
class CurrencyTest {
    
    private fun createTestPlayer(
        seeds: Long = 0,
        glimmerShards: Long = 0
    ): Player {
        return Player(
            id = "test_player",
            name = "Test Hero",
            level = 1,
            stats = PlayerStats(),
            position = Position(0, 0, "starting_village"),
            seeds = seeds,
            glimmerShards = glimmerShards
        )
    }
    
    // ==== SEEDS ADD TESTS ====
    
    @Test
    fun `addSeeds should add currency correctly`() {
        val player = createTestPlayer(seeds = 100)
        val (newPlayer, result) = CurrencyManager.addSeeds(player, 50)
        
        assertTrue(result is CurrencyResult.Success)
        assertEquals(150, newPlayer.seeds)
        assertEquals(150, (result as CurrencyResult.Success).newBalance)
        assertEquals(50, result.amountChanged)
    }
    
    @Test
    fun `addSeeds should handle zero balance`() {
        val player = createTestPlayer(seeds = 0)
        val (newPlayer, result) = CurrencyManager.addSeeds(player, 100)
        
        assertTrue(result is CurrencyResult.Success)
        assertEquals(100, newPlayer.seeds)
    }
    
    @Test
    fun `addSeeds should handle large amounts`() {
        val player = createTestPlayer(seeds = 1_000_000)
        val (newPlayer, result) = CurrencyManager.addSeeds(player, 5_000_000)
        
        assertTrue(result is CurrencyResult.Success)
        assertEquals(6_000_000, newPlayer.seeds)
    }
    
    @Test
    fun `addSeeds should reject negative amounts`() {
        val player = createTestPlayer(seeds = 100)
        val (newPlayer, result) = CurrencyManager.addSeeds(player, -50)
        
        assertTrue(result is CurrencyResult.Failure.InvalidAmount)
        assertEquals(100, newPlayer.seeds)  // Unchanged
    }
    
    @Test
    fun `addSeeds should reject zero amount`() {
        val player = createTestPlayer(seeds = 100)
        val (newPlayer, result) = CurrencyManager.addSeeds(player, 0)
        
        assertTrue(result is CurrencyResult.Failure.InvalidAmount)
        assertEquals(100, newPlayer.seeds)
    }
    
    @Test
    fun `addSeeds should detect overflow risk`() {
        val player = createTestPlayer(seeds = Long.MAX_VALUE - 10)
        val (newPlayer, result) = CurrencyManager.addSeeds(player, 100)
        
        assertTrue(result is CurrencyResult.Failure.OverflowRisk)
        assertEquals(Long.MAX_VALUE - 10, newPlayer.seeds)  // Unchanged
    }
    
    @Test
    fun `addSeeds should allow adding to max value - 1`() {
        val player = createTestPlayer(seeds = Long.MAX_VALUE - 100)
        val (newPlayer, result) = CurrencyManager.addSeeds(player, 50)
        
        assertTrue(result is CurrencyResult.Success)
        assertEquals(Long.MAX_VALUE - 50, newPlayer.seeds)
    }
    
    // ==== SEEDS REMOVE TESTS ====
    
    @Test
    fun `removeSeeds should remove currency correctly`() {
        val player = createTestPlayer(seeds = 100)
        val (newPlayer, result) = CurrencyManager.removeSeeds(player, 30)
        
        assertTrue(result is CurrencyResult.Success)
        assertEquals(70, newPlayer.seeds)
        assertEquals(70, (result as CurrencyResult.Success).newBalance)
        assertEquals(30, result.amountChanged)
    }
    
    @Test
    fun `removeSeeds should allow removing entire balance`() {
        val player = createTestPlayer(seeds = 100)
        val (newPlayer, result) = CurrencyManager.removeSeeds(player, 100)
        
        assertTrue(result is CurrencyResult.Success)
        assertEquals(0, newPlayer.seeds)
    }
    
    @Test
    fun `removeSeeds should fail when insufficient funds`() {
        val player = createTestPlayer(seeds = 50)
        val (newPlayer, result) = CurrencyManager.removeSeeds(player, 100)
        
        assertTrue(result is CurrencyResult.Failure.InsufficientFunds)
        assertEquals(50, newPlayer.seeds)  // Unchanged
    }
    
    @Test
    fun `removeSeeds should fail when balance is zero`() {
        val player = createTestPlayer(seeds = 0)
        val (newPlayer, result) = CurrencyManager.removeSeeds(player, 1)
        
        assertTrue(result is CurrencyResult.Failure.InsufficientFunds)
        assertEquals(0, newPlayer.seeds)
    }
    
    @Test
    fun `removeSeeds should reject negative amounts`() {
        val player = createTestPlayer(seeds = 100)
        val (newPlayer, result) = CurrencyManager.removeSeeds(player, -10)
        
        assertTrue(result is CurrencyResult.Failure.InvalidAmount)
        assertEquals(100, newPlayer.seeds)
    }
    
    @Test
    fun `removeSeeds should reject zero amount`() {
        val player = createTestPlayer(seeds = 100)
        val (newPlayer, result) = CurrencyManager.removeSeeds(player, 0)
        
        assertTrue(result is CurrencyResult.Failure.InvalidAmount)
        assertEquals(100, newPlayer.seeds)
    }
    
    // ==== GLIMMER SHARDS ADD TESTS ====
    
    @Test
    fun `addGlimmerShards should add currency correctly`() {
        val player = createTestPlayer(glimmerShards = 10)
        val (newPlayer, result) = CurrencyManager.addGlimmerShards(player, 5)
        
        assertTrue(result is CurrencyResult.Success)
        assertEquals(15, newPlayer.glimmerShards)
        assertEquals(15, (result as CurrencyResult.Success).newBalance)
        assertEquals(5, result.amountChanged)
    }
    
    @Test
    fun `addGlimmerShards should handle zero balance`() {
        val player = createTestPlayer(glimmerShards = 0)
        val (newPlayer, result) = CurrencyManager.addGlimmerShards(player, 20)
        
        assertTrue(result is CurrencyResult.Success)
        assertEquals(20, newPlayer.glimmerShards)
    }
    
    @Test
    fun `addGlimmerShards should reject negative amounts`() {
        val player = createTestPlayer(glimmerShards = 10)
        val (newPlayer, result) = CurrencyManager.addGlimmerShards(player, -5)
        
        assertTrue(result is CurrencyResult.Failure.InvalidAmount)
        assertEquals(10, newPlayer.glimmerShards)
    }
    
    @Test
    fun `addGlimmerShards should detect overflow risk`() {
        val player = createTestPlayer(glimmerShards = Long.MAX_VALUE - 5)
        val (newPlayer, result) = CurrencyManager.addGlimmerShards(player, 10)
        
        assertTrue(result is CurrencyResult.Failure.OverflowRisk)
        assertEquals(Long.MAX_VALUE - 5, newPlayer.glimmerShards)
    }
    
    // ==== GLIMMER SHARDS REMOVE TESTS ====
    
    @Test
    fun `removeGlimmerShards should remove currency correctly`() {
        val player = createTestPlayer(glimmerShards = 50)
        val (newPlayer, result) = CurrencyManager.removeGlimmerShards(player, 20)
        
        assertTrue(result is CurrencyResult.Success)
        assertEquals(30, newPlayer.glimmerShards)
        assertEquals(30, (result as CurrencyResult.Success).newBalance)
        assertEquals(20, result.amountChanged)
    }
    
    @Test
    fun `removeGlimmerShards should fail when insufficient funds`() {
        val player = createTestPlayer(glimmerShards = 5)
        val (newPlayer, result) = CurrencyManager.removeGlimmerShards(player, 10)
        
        assertTrue(result is CurrencyResult.Failure.InsufficientFunds)
        assertEquals(5, newPlayer.glimmerShards)
    }
    
    @Test
    fun `removeGlimmerShards should reject negative amounts`() {
        val player = createTestPlayer(glimmerShards = 10)
        val (newPlayer, result) = CurrencyManager.removeGlimmerShards(player, -3)
        
        assertTrue(result is CurrencyResult.Failure.InvalidAmount)
        assertEquals(10, newPlayer.glimmerShards)
    }
    
    // ==== CAN AFFORD TESTS ====
    
    @Test
    fun `canAfford should return true when player has exact amount`() {
        val player = createTestPlayer(seeds = 100, glimmerShards = 10)
        
        assertTrue(CurrencyManager.canAfford(player, seedsCost = 100, glimmerShardsCost = 10))
    }
    
    @Test
    fun `canAfford should return true when player has more than needed`() {
        val player = createTestPlayer(seeds = 200, glimmerShards = 20)
        
        assertTrue(CurrencyManager.canAfford(player, seedsCost = 100, glimmerShardsCost = 10))
    }
    
    @Test
    fun `canAfford should return false when seeds insufficient`() {
        val player = createTestPlayer(seeds = 50, glimmerShards = 20)
        
        assertFalse(CurrencyManager.canAfford(player, seedsCost = 100, glimmerShardsCost = 10))
    }
    
    @Test
    fun `canAfford should return false when glimmer shards insufficient`() {
        val player = createTestPlayer(seeds = 200, glimmerShards = 5)
        
        assertFalse(CurrencyManager.canAfford(player, seedsCost = 100, glimmerShardsCost = 10))
    }
    
    @Test
    fun `canAfford should handle zero costs`() {
        val player = createTestPlayer(seeds = 0, glimmerShards = 0)
        
        assertTrue(CurrencyManager.canAfford(player, seedsCost = 0, glimmerShardsCost = 0))
    }
    
    @Test
    fun `canAfford should handle single currency check`() {
        val player = createTestPlayer(seeds = 100, glimmerShards = 0)
        
        assertTrue(CurrencyManager.canAfford(player, seedsCost = 50))
        assertFalse(CurrencyManager.canAfford(player, glimmerShardsCost = 1))
    }
    
    // ==== PURCHASE TESTS ====
    
    @Test
    fun `purchase should deduct both currencies atomically`() {
        val player = createTestPlayer(seeds = 200, glimmerShards = 20)
        val (newPlayer, success) = CurrencyManager.purchase(player, seedsCost = 100, glimmerShardsCost = 10)
        
        assertTrue(success)
        assertEquals(100, newPlayer.seeds)
        assertEquals(10, newPlayer.glimmerShards)
    }
    
    @Test
    fun `purchase should handle seeds-only cost`() {
        val player = createTestPlayer(seeds = 200, glimmerShards = 20)
        val (newPlayer, success) = CurrencyManager.purchase(player, seedsCost = 150)
        
        assertTrue(success)
        assertEquals(50, newPlayer.seeds)
        assertEquals(20, newPlayer.glimmerShards)  // Unchanged
    }
    
    @Test
    fun `purchase should handle glimmer-shards-only cost`() {
        val player = createTestPlayer(seeds = 200, glimmerShards = 20)
        val (newPlayer, success) = CurrencyManager.purchase(player, glimmerShardsCost = 15)
        
        assertTrue(success)
        assertEquals(200, newPlayer.seeds)  // Unchanged
        assertEquals(5, newPlayer.glimmerShards)
    }
    
    @Test
    fun `purchase should fail when seeds insufficient`() {
        val player = createTestPlayer(seeds = 50, glimmerShards = 20)
        val (newPlayer, success) = CurrencyManager.purchase(player, seedsCost = 100, glimmerShardsCost = 10)
        
        assertFalse(success)
        assertEquals(50, newPlayer.seeds)  // Unchanged
        assertEquals(20, newPlayer.glimmerShards)  // Unchanged
    }
    
    @Test
    fun `purchase should fail when glimmer shards insufficient`() {
        val player = createTestPlayer(seeds = 200, glimmerShards = 5)
        val (newPlayer, success) = CurrencyManager.purchase(player, seedsCost = 100, glimmerShardsCost = 10)
        
        assertFalse(success)
        assertEquals(200, newPlayer.seeds)  // Unchanged
        assertEquals(5, newPlayer.glimmerShards)  // Unchanged
    }
    
    @Test
    fun `purchase should reject negative costs`() {
        val player = createTestPlayer(seeds = 200, glimmerShards = 20)
        val (newPlayer, success) = CurrencyManager.purchase(player, seedsCost = -10, glimmerShardsCost = 5)
        
        assertFalse(success)
        assertEquals(200, newPlayer.seeds)
        assertEquals(20, newPlayer.glimmerShards)
    }
    
    @Test
    fun `purchase should handle zero-cost transaction`() {
        val player = createTestPlayer(seeds = 200, glimmerShards = 20)
        val (newPlayer, success) = CurrencyManager.purchase(player, seedsCost = 0, glimmerShardsCost = 0)
        
        assertTrue(success)
        assertEquals(200, newPlayer.seeds)
        assertEquals(20, newPlayer.glimmerShards)
    }
    
    // ==== FORMATTING TESTS ====
    
    @Test
    fun `formatSeeds should format with thousand separators`() {
        assertEquals("1,000 Seeds", CurrencyManager.formatSeeds(1000))
        assertEquals("1,000,000 Seeds", CurrencyManager.formatSeeds(1_000_000))
        assertEquals("999 Seeds", CurrencyManager.formatSeeds(999))
        assertEquals("0 Seeds", CurrencyManager.formatSeeds(0))
    }
    
    @Test
    fun `formatGlimmerShards should format with thousand separators`() {
        assertEquals("50 Glimmer Shards", CurrencyManager.formatGlimmerShards(50))
        assertEquals("1,000 Glimmer Shards", CurrencyManager.formatGlimmerShards(1000))
        assertEquals("10,000,000 Glimmer Shards", CurrencyManager.formatGlimmerShards(10_000_000))
    }
    
    // ==== SERIALIZATION TEST ====
    
    @Test
    fun `Player with currency should serialize correctly`() {
        val player = createTestPlayer(seeds = 123456, glimmerShards = 789)
        
        val json = kotlinx.serialization.json.Json
        val serialized = json.encodeToString(Player.serializer(), player)
        val deserialized = json.decodeFromString(Player.serializer(), serialized)
        
        assertEquals(player.seeds, deserialized.seeds)
        assertEquals(player.glimmerShards, deserialized.glimmerShards)
    }
    
    // ==== BOUNDARY VALUE TESTS ====
    
    @Test
    fun `addSeeds should handle max safe addition`() {
        val player = createTestPlayer(seeds = Long.MAX_VALUE - 1000)
        val (newPlayer, result) = CurrencyManager.addSeeds(player, 1000)
        
        assertTrue(result is CurrencyResult.Success)
        assertEquals(Long.MAX_VALUE, newPlayer.seeds)
    }
    
    @Test
    fun `operations should not affect other currency`() {
        val player = createTestPlayer(seeds = 100, glimmerShards = 50)
        
        val (p1, _) = CurrencyManager.addSeeds(player, 100)
        assertEquals(50, p1.glimmerShards)  // Glimmer Shards unchanged
        
        val (p2, _) = CurrencyManager.removeSeeds(p1, 50)
        assertEquals(50, p2.glimmerShards)  // Still unchanged
        
        val (p3, _) = CurrencyManager.addGlimmerShards(p2, 25)
        assertEquals(150, p3.seeds)  // Seeds unchanged
    }
}
