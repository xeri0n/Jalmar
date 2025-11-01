package com.jalmarquest.shared.core

import com.jalmarquest.shared.state.GameStateManager
import kotlinx.coroutines.test.runTest
import kotlin.test.*

/**
 * Tests for stamina regeneration system.
 * Ensures player is never stuck waiting for stamina.
 * 
 * DESIGN PHILOSOPHY:
 * Movement costs are strategic flavor, NOT blocking mechanics.
 * Stamina regeneration at 10/second ensures worst-case recovery (swamp move = 5 stamina) 
 * takes only 0.5 seconds - fast enough to never be a "wait or pay" hinder.
 */
class StaminaRegenerationTest {
    
    private lateinit var gameStateManager: GameStateManager
    
    @BeforeTest
    fun setup() {
        gameStateManager = GameStateManager()
    }
    
    @Test
    fun `stamina regeneration constant is set correctly`() {
        // At 20 TPS with 0.5 regen per tick = 10 stamina/second
        assertEquals(0.5, WorldUpdateCoordinator.STAMINA_REGEN_PER_TICK)
    }
    
    @Test
    fun `stamina regeneration rate ensures fast recovery`() {
        // At 0.5 per tick, 20 TPS:
        // 1 second = 20 ticks * 0.5 = 10 stamina
        // Full bar (100 stamina) = 10 seconds
        // Swamp move (5 stamina) = 0.5 seconds to recover
        
        val regenPerSecond = WorldUpdateCoordinator.STAMINA_REGEN_PER_TICK * WorldUpdateCoordinator.DEFAULT_TICKS_PER_SECOND
        assertEquals(10.0, regenPerSecond)
        
        // Time to recover from max cost move (5 stamina)
        val swampCost = 5
        val secondsToRecover = swampCost / regenPerSecond
        assertTrue(secondsToRecover <= 1.0, "Should recover from swamp move in under 1 second, takes $secondsToRecover")
    }
    
    @Test
    fun `stamina update caps at max stamina`() = runTest {
        gameStateManager.createNewGame("TestHero")
        gameStateManager.updatePlayerStats { stats ->
            stats.copy(currentStamina = 99, maxStamina = 100)
        }
        
        // Simulate stamina regeneration logic
        val currentStamina = gameStateManager.gameState.value!!.player.stats.currentStamina
        val maxStamina = gameStateManager.gameState.value!!.player.stats.maxStamina
        
        // Important: Track as float for accumulation, then convert to int
        val regenAmount = WorldUpdateCoordinator.STAMINA_REGEN_PER_TICK
        val afterRegen = currentStamina + regenAmount // 99 + 0.5 = 99.5
        val newStamina = afterRegen.toInt().coerceAtMost(maxStamina) // 99.5.toInt() = 99
        
        // After ONE tick from 99, we get 99 (0.5 is lost to int truncation)
        // This is expected behavior - need 2 ticks to go from 99 to 100
        assertEquals(99, newStamina)
        
        // After SECOND tick
        val afterSecondTick = afterRegen + regenAmount // 99.5 + 0.5 = 100.0
        val finalStamina = afterSecondTick.toInt().coerceAtMost(maxStamina)
        assertEquals(100, finalStamina) // Now we hit max
    }
    
    @Test
    fun `stamina regeneration works from zero`() {
        // Test the math from absolute zero
        val regenAmount = WorldUpdateCoordinator.STAMINA_REGEN_PER_TICK
        
        // Track as float to accumulate fractional stamina
        var current = 0.0
        
        // First tick from 0
        current += regenAmount // 0 + 0.5 = 0.5
        assertEquals(0, current.toInt()) // 0.5.toInt() = 0
        
        // Second tick
        current += regenAmount // 0.5 + 0.5 = 1.0
        assertEquals(1, current.toInt()) // 1.0.toInt() = 1
        
        // Verify regeneration works properly when tracked as float
        assertTrue(current > 0.0, "Should regenerate from 0 after second tick")
    }
    
    @Test
    fun `multiple ticks regenerate full stamina bar in reasonable time`() {
        val fullStamina = 100
        val regenPerTick = WorldUpdateCoordinator.STAMINA_REGEN_PER_TICK
        val ticksPerSecond = WorldUpdateCoordinator.DEFAULT_TICKS_PER_SECOND
        
        // How many ticks to regenerate full bar?
        val ticksNeeded = (fullStamina / regenPerTick).toInt()
        val secondsNeeded = ticksNeeded / ticksPerSecond
        
        // Should take ~10 seconds
        assertTrue(secondsNeeded <= 15, "Should regenerate full bar in under 15 seconds, takes $secondsNeeded")
    }
    
    @Test
    fun `regeneration prevents player from getting stuck`() {
        // CRITICAL TEST: Movement cost should never be a hinder for the player
        // where they have to wait or pay to continue playing.
        // 
        // Worst case: Player at 0 stamina, needs 5 stamina for swamp move
        // At 10 stamina/second, this takes 0.5 seconds
        // This is fast enough to never be a blocking mechanic
        
        val maxMoveCost = 5 // Swamp biome - highest cost
        val regenPerSecond = WorldUpdateCoordinator.STAMINA_REGEN_PER_TICK * WorldUpdateCoordinator.DEFAULT_TICKS_PER_SECOND
        
        val waitTime = maxMoveCost / regenPerSecond
        
        assertTrue(waitTime < 1.0, "Wait time should be under 1 second, is $waitTime")
        assertEquals(0.5, waitTime, "Swamp move recovery should be exactly 0.5 seconds")
    }
}
