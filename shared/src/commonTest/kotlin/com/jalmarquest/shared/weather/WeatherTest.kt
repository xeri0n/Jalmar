package com.jalmarquest.shared.weather

import com.jalmarquest.shared.world.BiomeType
import com.jalmarquest.shared.model.Season
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.*

/**
 * Comprehensive tests for weather system.
 * 
 * Coverage:
 * - WeatherType properties and helpers
 * - Weather data class validation and progression
 * - WeatherManager seasonal constraints
 * - WeatherManager biome-specific weather
 * - Weather transitions and state machine
 */

class WeatherTypeTest {
    
    @Test
    fun `displayName should return readable names`() {
        assertEquals("Clear Skies", WeatherType.CLEAR.displayName())
        assertEquals("Thunderstorm", WeatherType.THUNDERSTORM.displayName())
        assertEquals("Dense Fog", WeatherType.FOG.displayName())
        assertEquals("Blizzard", WeatherType.BLIZZARD.displayName())
    }
    
    @Test
    fun `isSevere should identify dangerous weather`() {
        assertTrue(WeatherType.THUNDERSTORM.isSevere())
        assertTrue(WeatherType.BLIZZARD.isSevere())
        assertTrue(WeatherType.STORM.isSevere())
        assertTrue(WeatherType.HEATWAVE.isSevere())
        
        assertFalse(WeatherType.CLEAR.isSevere())
        assertFalse(WeatherType.RAIN.isSevere())
        assertFalse(WeatherType.MIST.isSevere())
    }
    
    @Test
    fun `visibilityModifier should return valid range`() {
        for (type in WeatherType.entries) {
            val modifier = type.visibilityModifier()
            assertTrue(modifier in 0.0..1.0, "$type visibility $modifier out of range")
        }
    }
    
    @Test
    fun `visibilityModifier should make fog lowest visibility`() {
        val fogVisibility = WeatherType.FOG.visibilityModifier()
        for (type in WeatherType.entries) {
            if (type != WeatherType.BLIZZARD) { // Blizzard can be lower
                assertTrue(
                    type.visibilityModifier() >= fogVisibility || type == WeatherType.BLIZZARD,
                    "$type visibility should be >= FOG or be BLIZZARD"
                )
            }
        }
    }
    
    @Test
    fun `movementModifier should return valid range`() {
        for (type in WeatherType.entries) {
            val modifier = type.movementModifier()
            assertTrue(modifier in 0.0..1.0, "$type movement $modifier out of range")
        }
    }
    
    @Test
    fun `movementModifier should make blizzard slowest`() {
        val blizzardSpeed = WeatherType.BLIZZARD.movementModifier()
        for (type in WeatherType.entries) {
            assertTrue(
                type.movementModifier() >= blizzardSpeed,
                "$type movement should be >= BLIZZARD"
            )
        }
    }
    
    @Test
    fun `clear weather should have no penalties`() {
        assertEquals(1.0, WeatherType.CLEAR.visibilityModifier())
        assertEquals(1.0, WeatherType.CLEAR.movementModifier())
    }
}

class WeatherTest {
    
    @Test
    fun `Weather should validate intensity range`() {
        assertFailsWith<IllegalArgumentException> {
            Weather(intensity = -0.1)
        }
        assertFailsWith<IllegalArgumentException> {
            Weather(intensity = 1.1)
        }
        
        // Valid boundaries
        Weather(intensity = 0.0)
        Weather(intensity = 1.0)
    }
    
    @Test
    fun `Weather should validate positive duration`() {
        assertFailsWith<IllegalArgumentException> {
            Weather(durationMinutes = 0)
        }
        assertFailsWith<IllegalArgumentException> {
            Weather(durationMinutes = -10)
        }
    }
    
    @Test
    fun `Weather should validate elapsed time does not exceed duration`() {
        assertFailsWith<IllegalArgumentException> {
            Weather(durationMinutes = 30, minutesElapsed = 31)
        }
        
        // Valid at exactly duration
        Weather(durationMinutes = 30, minutesElapsed = 30)
    }
    
    @Test
    fun `isComplete should return true when elapsed equals duration`() {
        val weather = Weather(durationMinutes = 60, minutesElapsed = 60)
        assertTrue(weather.isComplete())
    }
    
    @Test
    fun `isComplete should return false when time remains`() {
        val weather = Weather(durationMinutes = 60, minutesElapsed = 30)
        assertFalse(weather.isComplete())
    }
    
    @Test
    fun `remainingMinutes should calculate correctly`() {
        val weather = Weather(durationMinutes = 100, minutesElapsed = 35)
        assertEquals(65, weather.remainingMinutes())
    }
    
    @Test
    fun `advance should increase elapsed time`() {
        val weather = Weather(durationMinutes = 60, minutesElapsed = 10)
        val advanced = weather.advance(20)
        
        assertEquals(30, advanced.minutesElapsed)
        assertEquals(60, advanced.durationMinutes)
    }
    
    @Test
    fun `advance should cap at duration`() {
        val weather = Weather(durationMinutes = 60, minutesElapsed = 50)
        val advanced = weather.advance(20) // Would be 70, caps at 60
        
        assertEquals(60, advanced.minutesElapsed)
        assertTrue(advanced.isComplete())
    }
    
    @Test
    fun `advance should reject negative minutes`() {
        val weather = Weather()
        assertFailsWith<IllegalArgumentException> {
            weather.advance(-10)
        }
    }
    
    @Test
    fun `effectiveVisibility should scale with intensity`() {
        val lightFog = Weather(type = WeatherType.FOG, intensity = 0.3, durationMinutes = 60)
        val heavyFog = Weather(type = WeatherType.FOG, intensity = 0.9, durationMinutes = 60)
        
        // Heavy fog should have worse visibility than light fog
        assertTrue(heavyFog.effectiveVisibility() < lightFog.effectiveVisibility())
        
        // Both should be between 0 and 1
        assertTrue(lightFog.effectiveVisibility() in 0.0..1.0)
        assertTrue(heavyFog.effectiveVisibility() in 0.0..1.0)
    }
    
    @Test
    fun `effectiveMovementModifier should scale with intensity`() {
        val lightSnow = Weather(type = WeatherType.SNOW, intensity = 0.2, durationMinutes = 60)
        val heavySnow = Weather(type = WeatherType.SNOW, intensity = 0.8, durationMinutes = 60)
        
        // Heavy snow should slow more than light snow
        assertTrue(heavySnow.effectiveMovementModifier() < lightSnow.effectiveMovementModifier())
        
        // Both should be between 0 and 1
        assertTrue(lightSnow.effectiveMovementModifier() in 0.0..1.0)
        assertTrue(heavySnow.effectiveMovementModifier() in 0.0..1.0)
    }
    
    @Test
    fun `describe should include intensity and duration info`() {
        val weather = Weather(
            type = WeatherType.RAIN,
            intensity = 0.8,
            durationMinutes = 30,
            minutesElapsed = 0
        )
        
        val description = weather.describe()
        assertTrue(description.contains("Heavy") || description.contains("Moderate"))
        assertTrue(description.contains("Rainfall"))
    }
    
    @Test
    fun `companion presets should be valid`() {
        assertFalse(Weather.CLEAR_SKY.isComplete())
        assertFalse(Weather.MORNING_MIST.isComplete())
        assertFalse(Weather.RAIN_SHOWER.isComplete())
        assertFalse(Weather.HEAVY_STORM.isComplete())
        assertFalse(Weather.WINTER_BLIZZARD.isComplete())
        assertFalse(Weather.SUMMER_HEAT.isComplete())
    }
}

class WeatherManagerTest {
    
    @Test
    fun `getCurrentWeather should return current state`() = runTest {
        val manager = WeatherManager()
        val weather = manager.getCurrentWeather()
        
        assertNotNull(weather)
        assertEquals(WeatherType.CLEAR, weather.type)
    }
    
    @Test
    fun `setWeather should update current weather`() = runTest {
        val manager = WeatherManager()
        val newWeather = Weather.RAIN_SHOWER
        
        manager.setWeather(newWeather)
        val current = manager.getCurrentWeather()
        
        assertEquals(WeatherType.RAIN, current.type)
    }
    
    @Test
    fun `advanceTime should progress weather duration`() = runTest {
        val manager = WeatherManager()
        manager.setWeather(Weather(type = WeatherType.RAIN, durationMinutes = 60, minutesElapsed = 0))
        
        manager.advanceTime(15, Season.SPRING, BiomeType.GRASSLAND)
        val current = manager.getCurrentWeather()
        
        assertEquals(15, current.minutesElapsed)
    }
    
    @Test
    fun `advanceTime should transition when weather completes`() = runTest {
        val manager = WeatherManager()
        manager.setWeather(Weather(type = WeatherType.RAIN, durationMinutes = 30, minutesElapsed = 25))
        
        // Advance past completion
        val newWeather = manager.advanceTime(10, Season.SPRING, BiomeType.GRASSLAND)
        
        // Should have new weather (not the old rain)
        assertEquals(0, newWeather.minutesElapsed)
    }
    
    @Test
    fun `advanceTime should reject negative minutes`() = runTest {
        val manager = WeatherManager()
        
        assertFailsWith<IllegalArgumentException> {
            manager.advanceTime(-5, Season.SPRING, BiomeType.GRASSLAND)
        }
    }
    
    @Test
    fun `winter should not generate heatwave`() = runTest {
        val manager = WeatherManager(Random(12345))
        
        // Run many transitions to ensure no heatwaves in winter
        repeat(50) {
            manager.setWeather(Weather(durationMinutes = 1, minutesElapsed = 1))
            val weather = manager.advanceTime(1, Season.WINTER, BiomeType.GRASSLAND)
            assertNotEquals(WeatherType.HEATWAVE, weather.type, "Winter should not have heatwaves")
        }
    }
    
    @Test
    fun `summer should not generate snow or blizzard`() = runTest {
        val manager = WeatherManager(Random(54321))
        
        // Run many transitions
        repeat(50) {
            manager.setWeather(Weather(durationMinutes = 1, minutesElapsed = 1))
            val weather = manager.advanceTime(1, Season.SUMMER, BiomeType.GRASSLAND)
            assertFalse(weather.type in listOf(WeatherType.SNOW, WeatherType.BLIZZARD), "Summer should not have snow")
        }
    }
    
    @Test
    fun `desert should rarely have rain`() = runTest {
        val manager = WeatherManager(Random(99999))
        var rainCount = 0
        
        // Generate many weather instances
        repeat(100) {
            manager.setWeather(Weather(durationMinutes = 1, minutesElapsed = 1))
            val weather = manager.advanceTime(1, Season.SUMMER, BiomeType.DESERT)
            if (weather.type in listOf(WeatherType.RAIN, WeatherType.THUNDERSTORM)) {
                rainCount++
            }
        }
        
        // Desert should have rain less than 20% of the time
        assertTrue(rainCount < 20, "Desert had rain $rainCount times out of 100")
    }
    
    @Test
    fun `cave should always have clear weather`() = runTest {
        val manager = WeatherManager(Random(11111))
        
        repeat(20) {
            manager.setWeather(Weather(durationMinutes = 1, minutesElapsed = 1))
            val weather = manager.advanceTime(1, Season.SPRING, BiomeType.CAVE)
            assertEquals(WeatherType.CLEAR, weather.type, "Caves should always have clear weather")
        }
    }
    
    @Test
    fun `swamp should often have fog or rain`() = runTest {
        val manager = WeatherManager(Random(22222))
        var wetWeatherCount = 0
        
        repeat(50) {
            manager.setWeather(Weather(durationMinutes = 1, minutesElapsed = 1))
            val weather = manager.advanceTime(1, Season.SPRING, BiomeType.SWAMP)
            if (weather.type in listOf(WeatherType.FOG, WeatherType.RAIN, WeatherType.MIST)) {
                wetWeatherCount++
            }
        }
        
        // Swamps should be wet/foggy at least 40% of the time
        assertTrue(wetWeatherCount >= 20, "Swamp only had wet weather $wetWeatherCount times out of 50")
    }
    
    @Test
    fun `weather intensity should be in valid range`() = runTest {
        val manager = WeatherManager(Random(33333))
        
        repeat(30) {
            manager.setWeather(Weather(durationMinutes = 1, minutesElapsed = 1))
            val weather = manager.advanceTime(1, Season.AUTUMN, BiomeType.FOREST)
            assertTrue(weather.intensity in 0.0..1.0, "Intensity ${weather.intensity} out of range")
        }
    }
    
    @Test
    fun `weather duration should be positive`() = runTest {
        val manager = WeatherManager(Random(44444))
        
        repeat(30) {
            manager.setWeather(Weather(durationMinutes = 1, minutesElapsed = 1))
            val weather = manager.advanceTime(1, Season.SPRING, BiomeType.MOUNTAIN)
            assertTrue(weather.durationMinutes > 0, "Duration must be positive, got ${weather.durationMinutes}")
        }
    }
}
