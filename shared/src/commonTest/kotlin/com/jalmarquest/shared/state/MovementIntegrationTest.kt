package com.jalmarquest.shared.state

import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.model.PlayerStats
import com.jalmarquest.shared.model.Position
import com.jalmarquest.shared.movement.MovementManager
import com.jalmarquest.shared.movement.MovementResult
import com.jalmarquest.shared.world.Direction
import com.jalmarquest.shared.world.LocationManager
import kotlinx.coroutines.test.runTest
import kotlin.test.*

/**
 * Integration tests for MovementManager + GameStateManager.
 * Tests the complete flow: move validation -> state update -> stamina consumption.
 */
class MovementIntegrationTest {
    
    private lateinit var gameStateManager: GameStateManager
    private lateinit var locationManager: LocationManager
    private lateinit var movementManager: MovementManager
    
    @BeforeTest
    fun setup() {
        gameStateManager = GameStateManager()
        locationManager = LocationManager()
        movementManager = MovementManager(locationManager)
    }
    
    @Test
    fun `successful movement updates player position and consumes stamina`() = runTest {
        // Setup: Create game with player at starting village
        gameStateManager.createNewGame("TestHero")
        
        // Update player to start at starting_village
        gameStateManager.updatePlayerPosition(Position(0, 0, "starting_village"))
        
        val initialState = gameStateManager.gameState.value
        assertNotNull(initialState)
        assertEquals("starting_village", initialState.player.position.locationId)
        val initialStamina = initialState.player.stats.currentStamina
        
        // Action: Move north to meadow_path
        val moveResult = movementManager.move(initialState.player, Direction.NORTH)
        assertTrue(moveResult is MovementResult.Success)
        
        val newPosition = gameStateManager.executeMove(moveResult)
        
        // Verify: Position updated
        assertEquals("meadow_path", newPosition.locationId)
        
        // Verify: State reflects changes
        val updatedState = gameStateManager.gameState.value
        assertNotNull(updatedState)
        assertEquals("meadow_path", updatedState.player.position.locationId)
        
        // Verify: Stamina consumed
        val expectedStamina = initialStamina - moveResult.staminaCost
        assertEquals(expectedStamina, updatedState.player.stats.currentStamina)
    }
    
    @Test
    fun `executeMove throws when no game is loaded`() = runTest {
        val fakeResult = MovementResult.Success("meadow_path", 1, 1)
        
        val exception = assertFailsWith<IllegalStateException> {
            gameStateManager.executeMove(fakeResult)
        }
        assertEquals("No game loaded", exception.message)
    }
    
    @Test
    fun `executeMove validates stamina is sufficient`() = runTest {
        // Setup: Create game
        gameStateManager.createNewGame("TestHero")
        
        // Reduce stamina to 0
        gameStateManager.updatePlayerStats { stats ->
            stats.copy(currentStamina = 0)
        }
        
        val fakeResult = MovementResult.Success("meadow_path", 10, 1)
        
        // Action: Attempt to execute move with insufficient stamina
        val exception = assertFailsWith<IllegalArgumentException> {
            gameStateManager.executeMove(fakeResult)
        }
        assertTrue(exception.message!!.contains("Insufficient stamina"))
    }
    
    @Test
    fun `multiple consecutive moves update position correctly`() = runTest {
        // Setup
        gameStateManager.createNewGame("TestHero")
        gameStateManager.updatePlayerPosition(Position(0, 0, "starting_village"))
        val initialState = gameStateManager.gameState.value!!
        
        // Move 1: starting_village -> meadow_path (NORTH)
        val move1 = movementManager.move(initialState.player, Direction.NORTH)
        assertTrue(move1 is MovementResult.Success)
        gameStateManager.executeMove(move1)
        
        val state1 = gameStateManager.gameState.value!!
        assertEquals("meadow_path", state1.player.position.locationId)
        
        // Move 2: meadow_path -> elderwood (NORTH)
        val move2 = movementManager.move(state1.player, Direction.NORTH)
        assertTrue(move2 is MovementResult.Success)
        gameStateManager.executeMove(move2)
        
        val state2 = gameStateManager.gameState.value!!
        assertEquals("elderwood", state2.player.position.locationId)
        
        // Move 3: elderwood -> meadow_path (SOUTH)
        val move3 = movementManager.move(state2.player, Direction.SOUTH)
        assertTrue(move3 is MovementResult.Success)
        gameStateManager.executeMove(move3)
        
        val finalState = gameStateManager.gameState.value!!
        assertEquals("meadow_path", finalState.player.position.locationId)
    }
    
    @Test
    fun `stamina accumulates correctly across multiple moves`() = runTest {
        gameStateManager.createNewGame("TestHero")
        gameStateManager.updatePlayerPosition(Position(0, 0, "starting_village"))
        val initialStamina = gameStateManager.gameState.value!!.player.stats.currentStamina
        
        var totalStaminaCost = 0
        
        // Move 1
        val move1 = movementManager.move(gameStateManager.gameState.value!!.player, Direction.NORTH)
        assertTrue(move1 is MovementResult.Success)
        totalStaminaCost += move1.staminaCost
        gameStateManager.executeMove(move1)
        
        // Move 2
        val move2 = movementManager.move(gameStateManager.gameState.value!!.player, Direction.NORTH)
        assertTrue(move2 is MovementResult.Success)
        totalStaminaCost += move2.staminaCost
        gameStateManager.executeMove(move2)
        
        // Verify total stamina consumed
        val finalState = gameStateManager.gameState.value!!
        val expectedStamina = initialStamina - totalStaminaCost
        assertEquals(expectedStamina, finalState.player.stats.currentStamina)
    }
    
    @Test
    fun `failed movement does not update state or consume stamina`() = runTest {
        gameStateManager.createNewGame("TestHero")
        gameStateManager.updatePlayerPosition(Position(0, 0, "starting_village"))
        val initialState = gameStateManager.gameState.value!!
        val initialStamina = initialState.player.stats.currentStamina
        
        // Try to move SOUTH (invalid direction from starting_village)
        val moveResult = movementManager.move(initialState.player, Direction.SOUTH)
        assertTrue(moveResult is MovementResult.Failure)
        
        // Verify state unchanged
        val finalState = gameStateManager.gameState.value!!
        assertEquals(initialState.player.position.locationId, finalState.player.position.locationId)
        assertEquals(initialStamina, finalState.player.stats.currentStamina)
    }
    
    @Test
    fun `movement with zero stamina is blocked by MovementManager`() = runTest {
        gameStateManager.createNewGame("TestHero")
        gameStateManager.updatePlayerPosition(Position(0, 0, "starting_village"))
        
        // Reduce stamina to 0
        gameStateManager.updatePlayerStats { stats ->
            stats.copy(currentStamina = 0)
        }
        
        val playerWithNoStamina = gameStateManager.gameState.value!!.player
        
        // Try to move
        val moveResult = movementManager.move(playerWithNoStamina, Direction.NORTH)
        assertTrue(moveResult is MovementResult.Failure)
    }
    
    @Test
    fun `executeMove preserves grid coordinates when only changing location`() = runTest {
        gameStateManager.createNewGame("TestHero")
        gameStateManager.updatePlayerPosition(Position(0, 0, "starting_village"))
        
        val initialState = gameStateManager.gameState.value!!
        val initialX = initialState.player.position.x
        val initialY = initialState.player.position.y
        
        // Move
        val moveResult = movementManager.move(initialState.player, Direction.NORTH)
        assertTrue(moveResult is MovementResult.Success)
        
        val newPosition = gameStateManager.executeMove(moveResult)
        
        // Grid coordinates should be preserved (only locationId changes)
        assertEquals(initialX, newPosition.x)
        assertEquals(initialY, newPosition.y)
        assertNotEquals(initialState.player.position.locationId, newPosition.locationId)
    }
}
