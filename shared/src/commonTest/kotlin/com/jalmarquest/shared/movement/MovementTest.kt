package com.jalmarquest.shared.movement

import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.model.PlayerStats
import com.jalmarquest.shared.model.Position
import com.jalmarquest.shared.world.BiomeType
import com.jalmarquest.shared.world.Direction
import com.jalmarquest.shared.world.LocationManager
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class MovementManagerTest {
    
    private lateinit var locationManager: LocationManager
    private lateinit var movementManager: MovementManager
    private lateinit var testPlayer: Player
    
    @BeforeTest
    fun setup() {
        locationManager = LocationManager()
        movementManager = MovementManager(locationManager)
        
        testPlayer = Player(
            id = "test_player",
            name = "TestHero",
            level = 5,
            experience = 0,
            stats = PlayerStats(currentStamina = 50, maxStamina = 100),
            position = Position(0, 0, "starting_village")
        )
    }
    
    @Test
    fun `movement cost should vary by biome`() {
        val grasslandCost = MovementCost.forBiome(BiomeType.GRASSLAND)
        val swampCost = MovementCost.forBiome(BiomeType.SWAMP)
        val mountainCost = MovementCost.forBiome(BiomeType.MOUNTAIN)
        
        assertTrue(grasslandCost.baseStaminaCost < swampCost.baseStaminaCost)
        assertTrue(grasslandCost.baseStaminaCost < mountainCost.baseStaminaCost)
        assertTrue(grasslandCost.timeMultiplier < swampCost.timeMultiplier)
    }
    
    @Test
    fun `move should succeed with valid direction and stamina`() = runTest {
        // Debug: verify player setup
        val currentLoc = locationManager.getLocation(testPlayer.position.locationId)
        assertNotNull(currentLoc, "Current location should exist")
        assertEquals("starting_village", currentLoc.id)
        
        // Debug: verify north connection exists
        val northConnection = currentLoc.connections.find { it.direction == Direction.NORTH }
        assertNotNull(northConnection, "North connection should exist")
        assertEquals("meadow_path", northConnection.targetLocationId)
        
        // Now try the move
        val result = movementManager.move(testPlayer, Direction.NORTH)
        
        if (result !is MovementResult.Success) {
            fail("Expected Success but got: $result")
        }
        
        assertEquals("meadow_path", result.newLocationId)
        assertTrue(result.staminaCost > 0)
        assertTrue(result.timeCost > 0)
    }
    
    @Test
    fun `move should fail with invalid direction`() = runTest {
        // Starting village doesn't have a SOUTH exit
        val result = movementManager.move(testPlayer, Direction.SOUTH)
        
        assertTrue(result is MovementResult.Failure)
        val failure = result as MovementResult.Failure
        assertEquals(MovementFailureReason.INVALID_DIRECTION, failure.reason)
    }
    
    @Test
    fun `move should fail with insufficient stamina`() = runTest {
        val lowStaminaPlayer = testPlayer.copy(
            stats = testPlayer.stats.copy(currentStamina = 0)
        )
        
        val result = movementManager.move(lowStaminaPlayer, Direction.NORTH)
        
        assertTrue(result is MovementResult.Failure)
        val failure = result as MovementResult.Failure
        assertEquals(MovementFailureReason.INSUFFICIENT_STAMINA, failure.reason)
    }
    
    @Test
    fun `findPath should return valid path between connected locations`() {
        val path = movementManager.findPath(
            startLocationId = "starting_village",
            goalLocationId = "elderwood",
            playerLevel = 10,
            unlockedFlags = emptySet()
        )
        
        assertNotNull(path)
        assertTrue(path.isNotEmpty())
        assertEquals("starting_village", path.first())
        assertEquals("elderwood", path.last())
    }
    
    @Test
    fun `calculatePathStaminaCost should sum all location costs`() {
        val path = listOf("starting_village", "meadow_path", "elderwood")
        val cost = movementManager.calculatePathStaminaCost(path)
        
        assertTrue(cost > 0)
    }
    
    @Test
    fun `canMoveTo should check all requirements`() {
        val canMove = movementManager.canMoveTo(
            fromLocationId = "starting_village",
            toLocationId = "meadow_path",
            playerLevel = 1,
            playerStamina = 10,
            unlockedFlags = emptySet()
        )
        
        assertTrue(canMove)
    }
    
    @Test
    fun `canMoveTo should return false without stamina`() {
        val canMove = movementManager.canMoveTo(
            fromLocationId = "starting_village",
            toLocationId = "meadow_path",
            playerLevel = 1,
            playerStamina = 0,
            unlockedFlags = emptySet()
        )
        
        assertFalse(canMove)
    }
    
    @Test
    fun `getReachableLocations should find stamina-accessible locations`() {
        val reachable = movementManager.getReachableLocations(
            currentLocationId = "starting_village",
            playerLevel = 10,
            currentStamina = 20,
            unlockedFlags = emptySet()
        )
        
        assertTrue(reachable.isNotEmpty())
        assertTrue(reachable.contains("starting_village"))
        assertTrue(reachable.contains("meadow_path"))
    }
}

class MovementCostTest {
    
    @Test
    fun `all biome types should have movement costs defined`() {
        BiomeType.values().forEach { biomeType ->
            val cost = MovementCost.forBiome(biomeType)
            assertTrue(cost.baseStaminaCost > 0)
            assertTrue(cost.timeMultiplier > 0)
        }
    }
    
    @Test
    fun `grassland should be easiest terrain`() {
        val grassland = MovementCost.forBiome(BiomeType.GRASSLAND)
        val biomes = BiomeType.values().map { MovementCost.forBiome(it) }
        
        val cheaperBiomes = biomes.count { it.baseStaminaCost < grassland.baseStaminaCost }
        assertTrue(cheaperBiomes <= 1)
    }
    
    @Test
    fun `swamp should be difficult terrain`() {
        val swamp = MovementCost.forBiome(BiomeType.SWAMP)
        val grassland = MovementCost.forBiome(BiomeType.GRASSLAND)
        
        assertTrue(swamp.baseStaminaCost > grassland.baseStaminaCost * 2)
        assertTrue(swamp.timeMultiplier > grassland.timeMultiplier * 2)
    }
}
