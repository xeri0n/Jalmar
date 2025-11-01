package com.jalmarquest.shared.state

import com.jalmarquest.shared.model.Position
import com.jalmarquest.shared.model.Season
import com.jalmarquest.shared.model.WorldTime
import com.jalmarquest.shared.movement.MovementManager
import com.jalmarquest.shared.movement.MovementResult
import com.jalmarquest.shared.world.Direction
import com.jalmarquest.shared.world.LocationManager
import kotlinx.coroutines.test.runTest
import kotlin.test.*

/**
 * Integration tests for time advancement during movement.
 * Verifies that movement properly advances world time.
 */
class TimeIntegrationTest {
    
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
    fun `movement advances world time by time cost`() = runTest {
        // Setup: Create game at starting village
        gameStateManager.createNewGame("TestHero")
        gameStateManager.updatePlayerPosition(Position(0, 0, "starting_village"))
        
        val initialTime = gameStateManager.gameState.value!!.worldTime
        assertEquals(0, initialTime.minute)
        assertEquals(6, initialTime.hour)
        
        // Execute movement
        val player = gameStateManager.gameState.value!!.player
        val moveResult = movementManager.move(player, Direction.NORTH)
        
        assertTrue(moveResult is MovementResult.Success)
        val successResult = moveResult as MovementResult.Success
        
        // Apply movement
        gameStateManager.executeMove(successResult)
        
        // Verify time advanced
        val newTime = gameStateManager.gameState.value!!.worldTime
        val minutesPassed = successResult.timeCost
        
        assertTrue(minutesPassed > 0, "Movement should have non-zero time cost")
        
        // Time should have advanced by timeCost minutes
        val expectedMinute = (initialTime.minute + minutesPassed) % 60
        assertEquals(expectedMinute, newTime.minute)
    }
    
    @Test
    fun `time advancement crosses hour boundary`() = runTest {
        // Setup: Start at 6:58 AM, move takes 5 minutes -> should be 7:03 AM
        gameStateManager.createNewGame("TestHero")
        gameStateManager.updatePlayerPosition(Position(0, 0, "starting_village"))
        
        // Manually set time to 6:58
        gameStateManager.updateState { state ->
            state.copy(worldTime = WorldTime(
                totalTicks = 0,
                season = Season.SPRING,
                day = 1,
                hour = 6,
                minute = 58
            ))
        }
        
        val initialTime = gameStateManager.gameState.value!!.worldTime
        assertEquals(58, initialTime.minute)
        assertEquals(6, initialTime.hour)
        
        // Create movement result with 5-minute time cost
        val moveResult = MovementResult.Success(
            newLocationId = "meadow_path",
            staminaCost = 1,
            timeCost = 5
        )
        
        gameStateManager.executeMove(moveResult)
        
        val newTime = gameStateManager.gameState.value!!.worldTime
        assertEquals(3, newTime.minute) // 58 + 5 = 63 -> 3 minutes past hour
        assertEquals(7, newTime.hour)   // Hour incremented
    }
    
    @Test
    fun `time advancement crosses day boundary`() = runTest {
        // Setup: Start at 23:58, move takes 5 minutes -> should be 00:03 next day
        gameStateManager.createNewGame("TestHero")
        gameStateManager.updatePlayerPosition(Position(0, 0, "starting_village"))
        
        gameStateManager.updateState { state ->
            state.copy(worldTime = WorldTime(
                totalTicks = 0,
                season = Season.SPRING,
                day = 1,
                hour = 23,
                minute = 58
            ))
        }
        
        val initialTime = gameStateManager.gameState.value!!.worldTime
        assertEquals(1, initialTime.day)
        
        val moveResult = MovementResult.Success(
            newLocationId = "meadow_path",
            staminaCost = 1,
            timeCost = 5
        )
        
        gameStateManager.executeMove(moveResult)
        
        val newTime = gameStateManager.gameState.value!!.worldTime
        assertEquals(3, newTime.minute)
        assertEquals(0, newTime.hour)   // Midnight
        assertEquals(2, newTime.day)    // Next day
    }
    
    @Test
    fun `time advancement crosses season boundary`() = runTest {
        // Setup: Last day of Spring (day 30), 23:58, move 5 minutes -> Summer day 1
        gameStateManager.createNewGame("TestHero")
        gameStateManager.updatePlayerPosition(Position(0, 0, "starting_village"))
        
        gameStateManager.updateState { state ->
            state.copy(worldTime = WorldTime(
                totalTicks = 0,
                season = Season.SPRING,
                day = 30,
                hour = 23,
                minute = 58
            ))
        }
        
        val initialTime = gameStateManager.gameState.value!!.worldTime
        assertEquals(Season.SPRING, initialTime.season)
        assertEquals(30, initialTime.day)
        
        val moveResult = MovementResult.Success(
            newLocationId = "meadow_path",
            staminaCost = 1,
            timeCost = 5
        )
        
        gameStateManager.executeMove(moveResult)
        
        val newTime = gameStateManager.gameState.value!!.worldTime
        assertEquals(Season.SUMMER, newTime.season)
        assertEquals(1, newTime.day)
    }
    
    @Test
    fun `multiple movements accumulate time correctly`() = runTest {
        gameStateManager.createNewGame("TestHero")
        gameStateManager.updatePlayerPosition(Position(0, 0, "starting_village"))
        
        val initialTime = gameStateManager.gameState.value!!.worldTime
        
        // Move 1: 2 minutes
        gameStateManager.executeMove(MovementResult.Success(
            newLocationId = "meadow_path",
            staminaCost = 1,
            timeCost = 2
        ))
        
        var currentTime = gameStateManager.gameState.value!!.worldTime
        assertEquals(2, currentTime.minute)
        
        // Move 2: 3 minutes
        gameStateManager.executeMove(MovementResult.Success(
            newLocationId = "forest_edge",
            staminaCost = 1,
            timeCost = 3
        ))
        
        currentTime = gameStateManager.gameState.value!!.worldTime
        assertEquals(5, currentTime.minute)
        
        // Move 3: 10 minutes
        gameStateManager.executeMove(MovementResult.Success(
            newLocationId = "deep_forest",
            staminaCost = 2,
            timeCost = 10
        ))
        
        currentTime = gameStateManager.gameState.value!!.worldTime
        assertEquals(15, currentTime.minute) // 2 + 3 + 10 = 15
        assertEquals(initialTime.hour, currentTime.hour) // Still same hour
    }
    
    @Test
    fun `zero time cost movement does not advance time`() = runTest {
        gameStateManager.createNewGame("TestHero")
        gameStateManager.updatePlayerPosition(Position(0, 0, "starting_village"))
        
        val initialTime = gameStateManager.gameState.value!!.worldTime
        
        val moveResult = MovementResult.Success(
            newLocationId = "meadow_path",
            staminaCost = 0,
            timeCost = 0
        )
        
        gameStateManager.executeMove(moveResult)
        
        val newTime = gameStateManager.gameState.value!!.worldTime
        assertEquals(initialTime.minute, newTime.minute)
        assertEquals(initialTime.hour, newTime.hour)
        assertEquals(initialTime.day, newTime.day)
        assertEquals(initialTime.totalTicks, newTime.totalTicks)
    }
    
    @Test
    fun `total ticks increase correctly with time advancement`() = runTest {
        gameStateManager.createNewGame("TestHero")
        gameStateManager.updatePlayerPosition(Position(0, 0, "starting_village"))
        
        val initialTicks = gameStateManager.gameState.value!!.worldTime.totalTicks
        
        // Move with 5-minute cost
        val moveResult = MovementResult.Success(
            newLocationId = "meadow_path",
            staminaCost = 1,
            timeCost = 5
        )
        
        gameStateManager.executeMove(moveResult)
        
        val newTicks = gameStateManager.gameState.value!!.worldTime.totalTicks
        val ticksPassed = newTicks - initialTicks
        
        // 5 minutes = 5 * 60 ticks = 300 ticks
        assertEquals(5 * WorldTime.TICKS_PER_MINUTE, ticksPassed.toInt())
    }
    
    @Test
    fun `time advancement preserves time of day transitions`() = runTest {
        // Setup: Start in morning (6 AM), advance to afternoon
        gameStateManager.createNewGame("TestHero")
        gameStateManager.updatePlayerPosition(Position(0, 0, "starting_village"))
        
        val initialTime = gameStateManager.gameState.value!!.worldTime
        assertTrue(initialTime.isDay())
        assertEquals(6, initialTime.hour)
        
        // Move with large time cost (400 minutes = 6 hours 40 minutes)
        // 6:00 + 6:40 = 12:40 PM
        val moveResult = MovementResult.Success(
            newLocationId = "far_location",
            staminaCost = 5,
            timeCost = 400
        )
        
        gameStateManager.executeMove(moveResult)
        
        val newTime = gameStateManager.gameState.value!!.worldTime
        assertEquals(12, newTime.hour)
        assertEquals(40, newTime.minute)
        assertTrue(newTime.isDay())
    }
}
