package com.jalmarquest.shared.encounter

import com.jalmarquest.shared.model.TimeOfDay
import com.jalmarquest.shared.world.BiomeType
import kotlinx.serialization.Serializable

/**
 * Manages encounter calculations based on time of day, location, and biome.
 * Foundation for Phase 3.1 Enemy System.
 * 
 * This manager calculates effective encounter rates by combining:
 * - Base location encounter rate
 * - Time-of-day multipliers (from EncounterRate)
 * - Biome-specific modifiers
 * 
 * Usage:
 * ```
 * val manager = EncounterManager()
 * val effectiveRate = manager.calculateEncounterRate(
 *     baseRate = 0.8,
 *     timeOfDay = TimeOfDay.NIGHT,
 *     encounterRate = EncounterRate.NOCTURNAL
 * )
 * // effectiveRate = 0.8 * 1.8 = 1.44
 * ```
 */
class EncounterManager {
    
    /**
     * Calculate the effective encounter rate for current conditions.
     * 
     * @param baseRate The location's base encounter rate (from Location.encounterRate)
     * @param timeOfDay Current time of day
     * @param encounterRate Time-based multipliers for this encounter type
     * @param biomeModifier Optional biome-specific modifier (default 1.0)
     * @return Effective encounter rate (typically 0.0 to 2.0+)
     */
    fun calculateEncounterRate(
        baseRate: Double,
        timeOfDay: TimeOfDay,
        encounterRate: EncounterRate = EncounterRate.STANDARD,
        biomeModifier: Double = 1.0
    ): Double {
        require(baseRate >= 0.0) { "Base rate must be >= 0.0" }
        require(biomeModifier >= 0.0) { "Biome modifier must be >= 0.0" }
        
        val timeMultiplier = encounterRate.getMultiplier(timeOfDay)
        return baseRate * timeMultiplier * biomeModifier
    }
    
    /**
     * Determine if an encounter should occur based on effective rate.
     * Uses a simple probability check.
     * 
     * @param effectiveRate The calculated encounter rate
     * @return true if encounter should occur
     */
    fun shouldEncounter(effectiveRate: Double): Boolean {
        require(effectiveRate >= 0.0) { "Effective rate must be >= 0.0" }
        
        // For rates > 1.0, guarantee encounter + chance of multiple
        if (effectiveRate >= 1.0) return true
        
        // For rates < 1.0, use as probability
        return Math.random() < effectiveRate
    }
    
    /**
     * Get biome-specific encounter modifier.
     * Some biomes are inherently more dangerous than others.
     * 
     * @param biome The biome type
     * @return Multiplier for encounter rate (0.5 to 1.5)
     */
    fun getBiomeModifier(biome: BiomeType): Double {
        return when (biome) {
            BiomeType.GRASSLAND -> 0.8  // Relatively safe
            BiomeType.FOREST -> 1.0     // Standard
            BiomeType.MOUNTAIN -> 1.2   // Dangerous terrain
            BiomeType.DESERT -> 1.1     // Harsh environment
            BiomeType.SWAMP -> 1.3      // Very dangerous
            BiomeType.TUNDRA -> 1.2     // Harsh + predators
            BiomeType.COASTAL -> 0.9    // Moderate danger
            BiomeType.CAVE -> 1.5       // Very dangerous, confined
        }
    }
    
    /**
     * Get the appropriate EncounterRate for a biome.
     * Different biomes tend to have different creature activity patterns.
     * 
     * @param biome The biome type
     * @return Recommended EncounterRate for this biome
     */
    fun getDefaultEncounterRateForBiome(biome: BiomeType): EncounterRate {
        return when (biome) {
            BiomeType.GRASSLAND -> EncounterRate.DIURNAL       // Daytime herbivores
            BiomeType.FOREST -> EncounterRate.CREPUSCULAR      // Dawn/dusk predators
            BiomeType.MOUNTAIN -> EncounterRate.DIURNAL        // Eagles, mountain goats
            BiomeType.DESERT -> EncounterRate.NOCTURNAL        // Avoid heat
            BiomeType.SWAMP -> EncounterRate.STANDARD          // Active all times
            BiomeType.TUNDRA -> EncounterRate.DIURNAL          // Limited daylight
            BiomeType.COASTAL -> EncounterRate.STANDARD        // Tidal cycles
            BiomeType.CAVE -> EncounterRate.NOCTURNAL          // Dark environment
        }
    }
    
    /**
     * Calculate full encounter probability with all modifiers.
     * Convenience method combining all factors.
     * 
     * @param baseRate Location's base encounter rate
     * @param timeOfDay Current time of day
     * @param biome Location's biome
     * @param encounterRate Optional custom encounter rate (uses biome default if null)
     * @return Effective encounter rate
     */
    fun calculateFullEncounterRate(
        baseRate: Double,
        timeOfDay: TimeOfDay,
        biome: BiomeType,
        encounterRate: EncounterRate? = null
    ): Double {
        val rate = encounterRate ?: getDefaultEncounterRateForBiome(biome)
        val biomeModifier = getBiomeModifier(biome)
        return calculateEncounterRate(baseRate, timeOfDay, rate, biomeModifier)
    }
}

/**
 * Encounter probability tier for UI display.
 */
enum class EncounterTier {
    NONE,       // 0.0
    VERY_LOW,   // 0.0 - 0.3
    LOW,        // 0.3 - 0.6
    MODERATE,   // 0.6 - 1.0
    HIGH,       // 1.0 - 1.5
    VERY_HIGH,  // 1.5 - 2.0
    EXTREME;    // 2.0+
    
    companion object {
        fun fromRate(rate: Double): EncounterTier {
            return when {
                rate <= 0.0 -> NONE
                rate < 0.3 -> VERY_LOW
                rate < 0.6 -> LOW
                rate < 1.0 -> MODERATE
                rate < 1.5 -> HIGH
                rate < 2.0 -> VERY_HIGH
                else -> EXTREME
            }
        }
    }
}
