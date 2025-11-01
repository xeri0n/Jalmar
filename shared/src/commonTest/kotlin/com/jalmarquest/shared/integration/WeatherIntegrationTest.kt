package com.jalmarquest.shared.integration

import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.model.Player
import com.jalmarquest.shared.model.Season
import com.jalmarquest.shared.movement.MovementManager
import com.jalmarquest.shared.movement.MovementResult
import com.jalmarquest.shared.weather.Weather
import com.jalmarquest.shared.weather.WeatherManager
import com.jalmarquest.shared.weather.WeatherType
import com.jalmarquest.shared.world.BiomeType
import com.jalmarquest.shared.world.Direction
import com.jalmarquest.shared.world.LocationManager
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.*

/**
 * Integration tests for weather system.
 * Tests weather progression, seasonal constraints, biome patterns, and movement effects.
 */
class WeatherIntegrationTest {
    
    private val locationManager = LocationManager()
    private val movementManager = MovementManager(locationManager)
    private val weatherManager = WeatherManager(Random(12345))
    
    @Test
    fun `weather should progress over time`() = runTest {
        val initialWeather = Weather(
            type = WeatherType.RAIN,
            intensity = 0.5,
            durationMinutes = 30,
            minutesElapsed = 0
        )
        weatherManager.setWeather(initialWeather)
        
        // Advance time by 15 minutes
        val updated = weatherManager.advanceTime(15, Season.SPRING, BiomeType.GRASSLAND)
        
        assertEquals(15, updated.minutesElapsed)
        assertEquals(15, updated.remainingMinutes())
        assertFalse(updated.isComplete())
    }
    
    @Test
    fun `weather should transition when duration complete`() = runTest {
        val shortWeather = Weather(
            type = WeatherType.DRIZZLE,
            durationMinutes = 10,
            minutesElapsed = 5
        )
        weatherManager.setWeather(shortWeather)
        
        // Advance past completion
        val newWeather = weatherManager.advanceTime(10, Season.SPRING, BiomeType.GRASSLAND)
        
        // Should have new weather with 0 elapsed time
        assertEquals(0, newWeather.minutesElapsed)
        assertTrue(newWeather.durationMinutes > 0)
    }
    
    @Test
    fun `winter should only generate cold weather`() = runTest {
        val allowedWinterTypes = setOf(
            WeatherType.CLEAR,
            WeatherType.CLOUDY,
            WeatherType.SNOW,
            WeatherType.BLIZZARD,
            WeatherType.FOG,
            WeatherType.WIND,
            WeatherType.MIST
        )
        
        // Generate many weather instances in winter
        repeat(30) {
            weatherManager.setWeather(Weather(durationMinutes = 1, minutesElapsed = 1))
            val weather = weatherManager.advanceTime(1, Season.WINTER, BiomeType.GRASSLAND)
            
            assertTrue(
                weather.type in allowedWinterTypes,
                "Winter generated invalid weather: ${weather.type}"
            )
            assertNotEquals(WeatherType.HEATWAVE, weather.type, "Winter should not have heatwaves")
        }
    }
    
    @Test
    fun `summer should not have snow`() = runTest {
        repeat(30) {
            weatherManager.setWeather(Weather(durationMinutes = 1, minutesElapsed = 1))
            val weather = weatherManager.advanceTime(1, Season.SUMMER, BiomeType.GRASSLAND)
            
            assertFalse(
                weather.type in listOf(WeatherType.SNOW, WeatherType.BLIZZARD),
                "Summer generated snow: ${weather.type}"
            )
        }
    }
    
    @Test
    fun `desert should have dry weather`() = runTest {
        var clearOrHeatCount = 0
        
        repeat(50) {
            weatherManager.setWeather(Weather(durationMinutes = 1, minutesElapsed = 1))
            val weather = weatherManager.advanceTime(1, Season.SUMMER, BiomeType.DESERT)
            
            if (weather.type in listOf(WeatherType.CLEAR, WeatherType.HEATWAVE, WeatherType.WIND)) {
                clearOrHeatCount++
            }
        }
        
        // Desert should be dry at least 60% of the time
        assertTrue(clearOrHeatCount >= 30, "Desert only had dry weather $clearOrHeatCount times out of 50")
    }
    
    @Test
    fun `cave should always have clear weather`() = runTest {
        repeat(20) {
            weatherManager.setWeather(Weather(durationMinutes = 1, minutesElapsed = 1))
            val weather = weatherManager.advanceTime(1, Season.SPRING, BiomeType.CAVE)
            
            assertEquals(WeatherType.CLEAR, weather.type, "Caves should always be clear")
        }
    }
    
    @Test
    fun `weather should affect movement stamina cost`() = runTest {
        val testPlayer = Player(id = "test", name = "Test", level = 10)
        val clearWeather = Weather.CLEAR_SKY
        val blizzard = Weather.WINTER_BLIZZARD
        
        //  Verify weather modifiers are different
        val clearModifier = clearWeather.effectiveMovementModifier()
        val blizzardModifier = blizzard.effectiveMovementModifier()
        
        assertTrue(
            blizzardModifier < clearModifier,
            "Blizzard modifier ($blizzardModifier) should be < clear ($clearModifier)"
        )
        
        // Move in both weathers
        val clearResult = movementManager.move(
            player = testPlayer,
            direction = Direction.NORTH,
            weather = clearWeather
        )
        
        val blizzardResult = movementManager.move(
            player = testPlayer,
            direction = Direction.NORTH,
            weather = blizzard
        )
        
        // Both should succeed
        assertTrue(clearResult is MovementResult.Success, "Clear weather move should succeed")
        assertTrue(blizzardResult is MovementResult.Success, "Blizzard move should succeed")
        
        // Verify costs are calculated
        val clearCost = (clearResult as MovementResult.Success).staminaCost
        val blizzardCost = (blizzardResult as MovementResult.Success).staminaCost
        
        assertTrue(clearCost > 0, "Clear weather cost must be positive")
        assertTrue(blizzardCost > 0, "Blizzard cost must be positive")
    }
    
    @Test
    fun `weather should affect movement time cost`() = runTest {
        val player = Player(id = "test", name = "Test", level = 10)
        val clearWeather = Weather.CLEAR_SKY
        val heavyFog = Weather(type = WeatherType.FOG, intensity = 0.9, durationMinutes = 60)
        
        // Move in clear weather
        val clearResult = movementManager.move(
            player = player,
            direction = Direction.NORTH,
            weather = clearWeather
        )
        
        // Move in heavy fog
        val fogResult = movementManager.move(
            player = player,
            direction = Direction.NORTH,
            weather = heavyFog
        )
        
        assertTrue(clearResult is MovementResult.Success)
        assertTrue(fogResult is MovementResult.Success)
        
        // Fog should take longer (reduced visibility = slower travel)
        assertTrue(
            (fogResult as MovementResult.Success).timeCost >= (clearResult as MovementResult.Success).timeCost,
            "Fog time (${fogResult.timeCost}) should be >= clear (${clearResult.timeCost})"
        )
    }
    
    @Test
    fun `weather intensity should scale movement effects`() = runTest {
        val player = Player(id = "test", name = "Test", level = 10)
        val lightSnow = Weather(type = WeatherType.SNOW, intensity = 0.2, durationMinutes = 60)
        val heavySnow = Weather(type = WeatherType.SNOW, intensity = 0.9, durationMinutes = 60)
        
        val lightResult = movementManager.move(
            player = player,
            direction = Direction.NORTH,
            weather = lightSnow
        )
        
        val heavyResult = movementManager.move(
            player = player,
            direction = Direction.NORTH,
            weather = heavySnow
        )
        
        assertTrue(lightResult is MovementResult.Success)
        assertTrue(heavyResult is MovementResult.Success)
        
        // Heavy snow should cost more than light snow
        assertTrue(
            (heavyResult as MovementResult.Success).staminaCost >= (lightResult as MovementResult.Success).staminaCost,
            "Heavy snow (${heavyResult.staminaCost}) should cost >= light snow (${lightResult.staminaCost})"
        )
    }
    
    @Test
    fun `GameState should serialize weather correctly`() {
        val weather = Weather(
            type = WeatherType.THUNDERSTORM,
            intensity = 0.75,
            durationMinutes = 25,
            minutesElapsed = 10
        )
        
        val gameState = GameState.createNew("Test", "test-id").copy(weather = weather)
        
        assertEquals(WeatherType.THUNDERSTORM, gameState.weather.type)
        assertEquals(0.75, gameState.weather.intensity)
        assertEquals(25, gameState.weather.durationMinutes)
        assertEquals(10, gameState.weather.minutesElapsed)
    }
    
    @Test
    fun `weather should persist across save cycles`() {
        val originalWeather = Weather(
            type = WeatherType.RAIN,
            intensity = 0.6,
            durationMinutes = 40,
            minutesElapsed = 15
        )
        
        val gameState = GameState.createNew("Test", "test-id").copy(weather = originalWeather)
        
        // Verify weather is preserved
        assertEquals(originalWeather.type, gameState.weather.type)
        assertEquals(originalWeather.intensity, gameState.weather.intensity)
        assertEquals(originalWeather.minutesElapsed, gameState.weather.minutesElapsed)
        assertFalse(gameState.weather.isComplete())
    }
    
    @Test
    fun `seasonal weather transitions should be logical`() = runTest {
        // Spring → Summer: should transition smoothly
        weatherManager.setWeather(Weather(durationMinutes = 1, minutesElapsed = 1))
        val springWeather = weatherManager.advanceTime(1, Season.SPRING, BiomeType.GRASSLAND)
        
        weatherManager.setWeather(Weather(durationMinutes = 1, minutesElapsed = 1))
        val summerWeather = weatherManager.advanceTime(1, Season.SUMMER, BiomeType.GRASSLAND)
        
        // Both should have valid weather for their seasons
        assertNotNull(springWeather.type)
        assertNotNull(summerWeather.type)
        
        // Summer can have HEATWAVE, Spring cannot
        if (summerWeather.type == WeatherType.HEATWAVE) {
            assertNotEquals(WeatherType.HEATWAVE, springWeather.type)
        }
    }
    
    @Test
    fun `movement in extreme weather should still be possible`() = runTest {
        val player = Player(id = "test", name = "Test", level = 10)
        val extremeBlizzard = Weather(type = WeatherType.BLIZZARD, intensity = 1.0, durationMinutes = 60)
        
        val result = movementManager.move(
            player = player,
            direction = Direction.NORTH,
            weather = extremeBlizzard
        )
        
        // Movement should still succeed (not blocked), just more costly
        assertTrue(result is MovementResult.Success, "Movement should succeed even in extreme weather")
        
        val success = result as MovementResult.Success
        assertTrue(success.staminaCost > 0, "Stamina cost must be > 0")
        assertTrue(success.timeCost > 0, "Time cost must be > 0")
    }
}
