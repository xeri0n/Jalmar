package com.jalmarquest.shared.weather

import com.jalmarquest.shared.world.BiomeType
import com.jalmarquest.shared.model.Season
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlin.random.Random

/**
 * Manages dynamic weather transitions based on seasons, biomes, and time.
 * 
 * Weather changes occur naturally over time with:
 * - Season-appropriate weather types (no snow in summer)
 * - Biome-specific probabilities (deserts rarely rain, swamps often fog)
 * - Random variation for dynamic world feel
 * - Smooth transitions between weather states
 * 
 * Thread-safe for concurrent access from game loop and UI.
 */
class WeatherManager(
    private val random: Random = Random(Clock.System.now().toEpochMilliseconds())
) {
    private val mutex = Mutex()
    private var currentWeather: Weather = Weather.CLEAR_SKY
    
    /**
     * Gets the current weather state (thread-safe).
     */
    suspend fun getCurrentWeather(): Weather = mutex.withLock {
        currentWeather
    }
    
    /**
     * Advances weather time by specified in-game minutes.
     * If current weather completes, transitions to new weather.
     * 
     * @param minutes In-game minutes to advance
     * @param season Current season (affects weather type selection)
     * @param biome Current biome (affects weather probabilities)
     * @return The new weather state after advancement
     */
    suspend fun advanceTime(minutes: Int, season: Season, biome: BiomeType): Weather = mutex.withLock {
        require(minutes >= 0) { "Cannot advance by negative minutes: $minutes" }
        
        currentWeather = currentWeather.advance(minutes)
        
        // If weather has completed, transition to new weather
        if (currentWeather.isComplete()) {
            currentWeather = generateNextWeather(season, biome)
        }
        
        currentWeather
    }
    
    /**
     * Manually sets the weather (useful for scripted events, testing).
     */
    suspend fun setWeather(weather: Weather) = mutex.withLock {
        currentWeather = weather
    }
    
    /**
     * Generates the next weather state based on season and biome.
     * 
     * Season constraints:
     * - WINTER: Can have SNOW, BLIZZARD
     * - SUMMER: Can have HEATWAVE
     * - SPRING/AUTUMN: More rain, fog, mild weather
     * 
     * Biome modifiers:
     * - DESERT: Rarely rains, often clear or heatwave
     * - SWAMP: Often foggy or rainy
     * - MOUNTAIN: High chance of snow in winter, wind year-round
     * - FOREST: Moderate rain, occasional mist
     */
    private fun generateNextWeather(season: Season, biome: BiomeType): Weather {
        val allowedTypes = getAllowedWeatherTypes(season, biome)
        val type = selectWeatherType(allowedTypes, biome)
        val intensity = generateIntensity(type)
        val duration = generateDuration(type)
        
        return Weather(
            type = type,
            intensity = intensity,
            durationMinutes = duration,
            minutesElapsed = 0
        )
    }
    
    /**
     * Returns weather types allowed for the given season and biome.
     */
    private fun getAllowedWeatherTypes(season: Season, biome: BiomeType): List<WeatherType> {
        val base = when (season) {
            Season.WINTER -> listOf(
                WeatherType.CLEAR, WeatherType.CLOUDY, WeatherType.SNOW,
                WeatherType.BLIZZARD, WeatherType.FOG, WeatherType.WIND
            )
            Season.SUMMER -> listOf(
                WeatherType.CLEAR, WeatherType.CLOUDY, WeatherType.HEATWAVE,
                WeatherType.DRIZZLE, WeatherType.RAIN, WeatherType.THUNDERSTORM,
                WeatherType.WIND
            )
            Season.SPRING, Season.AUTUMN -> listOf(
                WeatherType.CLEAR, WeatherType.CLOUDY, WeatherType.MIST,
                WeatherType.DRIZZLE, WeatherType.RAIN, WeatherType.THUNDERSTORM,
                WeatherType.FOG, WeatherType.WIND
            )
        }
        
        // Biome filtering
        return when (biome) {
            BiomeType.DESERT -> base.filter { it !in listOf(WeatherType.RAIN, WeatherType.THUNDERSTORM, WeatherType.SNOW, WeatherType.BLIZZARD) }
            BiomeType.CAVE -> listOf(WeatherType.CLEAR) // Caves don't experience weather
            else -> base
        }
    }
    
    /**
     * Selects a weather type from allowed types based on biome probabilities.
     */
    private fun selectWeatherType(allowedTypes: List<WeatherType>, biome: BiomeType): WeatherType {
        if (allowedTypes.isEmpty()) return WeatherType.CLEAR
        
        // Weighted selection based on biome
        val weights = allowedTypes.map { type -> getWeatherWeight(type, biome) }
        val totalWeight = weights.sum()
        val roll = random.nextDouble() * totalWeight
        
        var cumulative = 0.0
        for ((index, weight) in weights.withIndex()) {
            cumulative += weight
            if (roll <= cumulative) {
                return allowedTypes[index]
            }
        }
        
        return allowedTypes.last() // Fallback
    }
    
    /**
     * Returns the probability weight for a weather type in a given biome.
     * Higher weight = more likely to occur.
     */
    private fun getWeatherWeight(type: WeatherType, biome: BiomeType): Double = when (biome) {
        BiomeType.DESERT -> when (type) {
            WeatherType.CLEAR -> 5.0
            WeatherType.HEATWAVE -> 3.0
            WeatherType.WIND -> 2.0
            WeatherType.CLOUDY -> 1.0
            else -> 0.5
        }
        BiomeType.SWAMP -> when (type) {
            WeatherType.FOG -> 4.0
            WeatherType.RAIN -> 3.0
            WeatherType.MIST -> 2.5
            WeatherType.CLEAR -> 1.5
            else -> 1.0
        }
        BiomeType.MOUNTAIN -> when (type) {
            WeatherType.WIND -> 4.0
            WeatherType.SNOW -> 3.0
            WeatherType.BLIZZARD -> 2.0
            WeatherType.CLEAR -> 2.0
            else -> 1.0
        }
        BiomeType.FOREST -> when (type) {
            WeatherType.RAIN -> 3.0
            WeatherType.MIST -> 2.5
            WeatherType.CLEAR -> 2.0
            WeatherType.CLOUDY -> 2.0
            else -> 1.0
        }
        BiomeType.GRASSLAND, BiomeType.COASTAL -> when (type) {
            WeatherType.CLEAR -> 3.0
            WeatherType.CLOUDY -> 2.5
            WeatherType.RAIN -> 2.0
            else -> 1.0
        }
        BiomeType.CAVE -> 0.0 // Caves handled above
        BiomeType.TUNDRA -> when (type) {
            WeatherType.SNOW, WeatherType.BLIZZARD -> 4.0
            WeatherType.WIND -> 3.0
            WeatherType.CLEAR -> 1.5
            else -> 0.5
        }
    }
    
    /**
     * Generates intensity for a weather type (0.0 to 1.0).
     */
    private fun generateIntensity(type: WeatherType): Double = when (type) {
        WeatherType.CLEAR, WeatherType.CLOUDY -> 0.0
        WeatherType.MIST, WeatherType.DRIZZLE -> random.nextDouble(0.2, 0.5)
        WeatherType.RAIN, WeatherType.FOG, WeatherType.WIND -> random.nextDouble(0.4, 0.8)
        WeatherType.SNOW, WeatherType.HEATWAVE -> random.nextDouble(0.5, 0.9)
        WeatherType.THUNDERSTORM, WeatherType.STORM, WeatherType.BLIZZARD -> random.nextDouble(0.7, 1.0)
    }
    
    /**
     * Generates duration for a weather type in in-game minutes.
     */
    private fun generateDuration(type: WeatherType): Int = when (type) {
        WeatherType.CLEAR -> random.nextInt(60, 180) // 1-3 hours
        WeatherType.CLOUDY -> random.nextInt(45, 120)
        WeatherType.MIST -> random.nextInt(30, 60)
        WeatherType.DRIZZLE -> random.nextInt(20, 45)
        WeatherType.RAIN -> random.nextInt(25, 60)
        WeatherType.FOG -> random.nextInt(40, 90)
        WeatherType.WIND -> random.nextInt(30, 90)
        WeatherType.SNOW -> random.nextInt(45, 120)
        WeatherType.HEATWAVE -> random.nextInt(120, 300) // 2-5 hours
        WeatherType.THUNDERSTORM, WeatherType.STORM -> random.nextInt(15, 35)
        WeatherType.BLIZZARD -> random.nextInt(40, 90)
    }
}
