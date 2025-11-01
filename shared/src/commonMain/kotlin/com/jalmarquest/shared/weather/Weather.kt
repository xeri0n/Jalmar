package com.jalmarquest.shared.weather

import com.jalmarquest.shared.model.Season
import kotlinx.serialization.Serializable

/**
 * Current weather state with intensity and duration.
 * 
 * Weather changes over time based on:
 * - Season (snow in winter, heatwaves in summer)
 * - Biome (deserts rarely have rain, swamps often foggy)
 * - Randomness (dynamic world feel)
 * 
 * @property type The type of weather occurring
 * @property intensity How severe the weather is (0.0 to 1.0)
 * @property durationMinutes How long this weather will last (in-game minutes)
 * @property minutesElapsed How many minutes have passed since weather started
 */
@Serializable
data class Weather(
    val type: WeatherType = WeatherType.CLEAR,
    val intensity: Double = 0.5,
    val durationMinutes: Int = 60,
    val minutesElapsed: Int = 0
) {
    init {
        require(intensity in 0.0..1.0) { "Intensity must be between 0.0 and 1.0, got $intensity" }
        require(durationMinutes > 0) { "Duration must be positive, got $durationMinutes" }
        require(minutesElapsed >= 0) { "Elapsed time cannot be negative, got $minutesElapsed" }
        require(minutesElapsed <= durationMinutes) { "Elapsed time ($minutesElapsed) cannot exceed duration ($durationMinutes)" }
    }
    
    /**
     * Returns true if this weather has finished its duration.
     */
    fun isComplete(): Boolean = minutesElapsed >= durationMinutes
    
    /**
     * Returns the remaining duration in minutes.
     */
    fun remainingMinutes(): Int = durationMinutes - minutesElapsed
    
    /**
     * Advances the weather by the specified number of minutes.
     * Returns a new Weather instance with updated elapsed time.
     */
    fun advance(minutes: Int): Weather {
        require(minutes >= 0) { "Cannot advance by negative minutes: $minutes" }
        return copy(minutesElapsed = (minutesElapsed + minutes).coerceAtMost(durationMinutes))
    }
    
    /**
     * Returns effective visibility modifier accounting for intensity.
     * Example: FOG at 0.5 intensity = 0.7 visibility (between normal 1.0 and FOG base 0.4)
     */
    fun effectiveVisibility(): Double {
        val baseModifier = type.visibilityModifier()
        // Interpolate between 1.0 (no effect) and base modifier based on intensity
        return 1.0 - (intensity * (1.0 - baseModifier))
    }
    
    /**
     * Returns effective movement modifier accounting for intensity.
     * Example: SNOW at 0.3 intensity = 0.94 speed (light snow, minimal slowdown)
     */
    fun effectiveMovementModifier(): Double {
        val baseModifier = type.movementModifier()
        // Interpolate between 1.0 (no effect) and base modifier based on intensity
        return 1.0 - (intensity * (1.0 - baseModifier))
    }
    
    /**
     * Returns a descriptive string for the current weather state.
     * Example: "Light Drizzle (clearing soon)" or "Heavy Blizzard (raging)"
     */
    fun describe(): String {
        val intensityDesc = when {
            intensity < 0.3 -> "Light"
            intensity < 0.7 -> "Moderate"
            else -> "Heavy"
        }
        
        val durationDesc = when {
            remainingMinutes() < 10 -> "clearing soon"
            remainingMinutes() < 30 -> "temporary"
            else -> "ongoing"
        }
        
        return "$intensityDesc ${type.displayName()} ($durationDesc)"
    }
    
    companion object {
        /** Standard clear weather (default state) */
        val CLEAR_SKY = Weather(
            type = WeatherType.CLEAR,
            intensity = 0.0,
            durationMinutes = 120
        )
        
        /** Light morning mist */
        val MORNING_MIST = Weather(
            type = WeatherType.MIST,
            intensity = 0.4,
            durationMinutes = 45
        )
        
        /** Afternoon rain shower */
        val RAIN_SHOWER = Weather(
            type = WeatherType.RAIN,
            intensity = 0.6,
            durationMinutes = 30
        )
        
        /** Heavy thunderstorm */
        val HEAVY_STORM = Weather(
            type = WeatherType.THUNDERSTORM,
            intensity = 0.9,
            durationMinutes = 20
        )
        
        /** Winter blizzard */
        val WINTER_BLIZZARD = Weather(
            type = WeatherType.BLIZZARD,
            intensity = 0.8,
            durationMinutes = 60
        )
        
        /** Summer heatwave */
        val SUMMER_HEAT = Weather(
            type = WeatherType.HEATWAVE,
            intensity = 0.7,
            durationMinutes = 240
        )
    }
}
