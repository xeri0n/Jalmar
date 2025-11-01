package com.jalmarquest.shared.weather

import kotlinx.serialization.Serializable

/**
 * Weather conditions that can occur in the game world.
 * 
 * Each weather type has gameplay implications:
 * - Visibility changes (fog reduces sight range)
 * - Movement modifiers (snow/mud slow travel)
 * - Combat effects (rain weakens fire abilities)
 * - Atmospheric storytelling (thunderstorms create tension)
 * 
 * Weather is seasonal - certain types only appear in specific seasons.
 */
@Serializable
enum class WeatherType {
    /** Clear skies, no weather effects */
    CLEAR,
    
    /** Light rain, minor visibility reduction */
    DRIZZLE,
    
    /** Moderate to heavy rainfall */
    RAIN,
    
    /** Heavy rain with lightning and thunder */
    THUNDERSTORM,
    
    /** Thick fog, major visibility reduction */
    FOG,
    
    /** Light snowfall (Winter only) */
    SNOW,
    
    /** Heavy snowfall with wind (Winter only) */
    BLIZZARD,
    
    /** Strong winds, affects ranged attacks */
    WIND,
    
    /** Dangerous weather with multiple effects (Summer) */
    STORM,
    
    /** Extreme heat, increases stamina costs (Summer only) */
    HEATWAVE,
    
    /** Light cloud cover, minimal effects */
    CLOUDY,
    
    /** Morning/evening mist, light visibility reduction */
    MIST;
    
    /**
     * Returns a player-friendly display name for the weather.
     */
    fun displayName(): String = when (this) {
        CLEAR -> "Clear Skies"
        DRIZZLE -> "Light Drizzle"
        RAIN -> "Rainfall"
        THUNDERSTORM -> "Thunderstorm"
        FOG -> "Dense Fog"
        SNOW -> "Snowfall"
        BLIZZARD -> "Blizzard"
        WIND -> "Strong Winds"
        STORM -> "Storm"
        HEATWAVE -> "Heatwave"
        CLOUDY -> "Cloudy"
        MIST -> "Mist"
    }
    
    /**
     * Returns true if this weather type is severe/dangerous.
     */
    fun isSevere(): Boolean = when (this) {
        THUNDERSTORM, BLIZZARD, STORM, HEATWAVE -> true
        else -> false
    }
    
    /**
     * Returns visibility modifier (1.0 = normal, 0.5 = half visibility, 0.0 = none).
     */
    fun visibilityModifier(): Double = when (this) {
        CLEAR -> 1.0
        CLOUDY, DRIZZLE -> 0.95
        MIST -> 0.8
        RAIN, WIND -> 0.85
        SNOW -> 0.7
        FOG -> 0.4
        THUNDERSTORM, STORM -> 0.6
        BLIZZARD -> 0.3
        HEATWAVE -> 0.9
    }
    
    /**
     * Returns movement speed modifier (1.0 = normal, 0.5 = half speed).
     */
    fun movementModifier(): Double = when (this) {
        CLEAR, CLOUDY, MIST -> 1.0
        DRIZZLE, WIND -> 0.95
        RAIN -> 0.9
        FOG -> 0.85
        SNOW, HEATWAVE -> 0.8
        THUNDERSTORM, STORM -> 0.75
        BLIZZARD -> 0.6
    }
}
