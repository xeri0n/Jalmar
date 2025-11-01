package com.jalmarquest.shared.encounter

import com.jalmarquest.shared.model.TimeOfDay
import kotlinx.serialization.Serializable

/**
 * Defines encounter rate multipliers for different times of day.
 * Base encounter rate from Location is multiplied by these values.
 * 
 * Example: Location has baseRate = 0.8
 * - During AFTERNOON with afternoonMultiplier = 1.0: effectiveRate = 0.8 * 1.0 = 0.8
 * - During NIGHT with nightMultiplier = 1.5: effectiveRate = 0.8 * 1.5 = 1.2
 * 
 * @property morningMultiplier Applied during MORNING (typically moderate activity)
 * @property afternoonMultiplier Applied during AFTERNOON (default activity)
 * @property eveningMultiplier Applied during EVENING (transition period, often higher)
 * @property nightMultiplier Applied during NIGHT (varies by creature type)
 */
@Serializable
data class EncounterRate(
    val morningMultiplier: Double = 1.0,
    val afternoonMultiplier: Double = 1.0,
    val eveningMultiplier: Double = 1.0,
    val nightMultiplier: Double = 1.0
) {
    init {
        require(morningMultiplier >= 0.0) { "Morning multiplier must be >= 0.0" }
        require(afternoonMultiplier >= 0.0) { "Afternoon multiplier must be >= 0.0" }
        require(eveningMultiplier >= 0.0) { "Evening multiplier must be >= 0.0" }
        require(nightMultiplier >= 0.0) { "Night multiplier must be >= 0.0" }
    }
    
    /**
     * Get the multiplier for a specific time of day.
     */
    fun getMultiplier(timeOfDay: TimeOfDay): Double {
        return when (timeOfDay) {
            TimeOfDay.MORNING -> morningMultiplier
            TimeOfDay.AFTERNOON -> afternoonMultiplier
            TimeOfDay.EVENING -> eveningMultiplier
            TimeOfDay.NIGHT -> nightMultiplier
        }
    }
    
    companion object {
        /**
         * Standard rate - no variation by time of day.
         */
        val STANDARD = EncounterRate(
            morningMultiplier = 1.0,
            afternoonMultiplier = 1.0,
            eveningMultiplier = 1.0,
            nightMultiplier = 1.0
        )
        
        /**
         * Diurnal creatures - active during day, less at night.
         * Examples: butterflies, songbirds, most herbivores
         */
        val DIURNAL = EncounterRate(
            morningMultiplier = 0.8,
            afternoonMultiplier = 1.2,
            eveningMultiplier = 0.8,
            nightMultiplier = 0.3
        )
        
        /**
         * Nocturnal creatures - active at night, rare during day.
         * Examples: owls, bats, nocturnal predators
         */
        val NOCTURNAL = EncounterRate(
            morningMultiplier = 1.2,
            afternoonMultiplier = 0.2,
            eveningMultiplier = 1.5,
            nightMultiplier = 1.8
        )
        
        /**
         * Crepuscular creatures - most active at morning/evening.
         * Examples: deer, rabbits, certain predators
         */
        val CREPUSCULAR = EncounterRate(
            morningMultiplier = 1.8,
            afternoonMultiplier = 0.6,
            eveningMultiplier = 1.8,
            nightMultiplier = 0.6
        )
        
        /**
         * Aggressive predators - higher activity during hunting times.
         */
        val PREDATOR = EncounterRate(
            morningMultiplier = 1.3,
            afternoonMultiplier = 0.8,
            eveningMultiplier = 1.5,
            nightMultiplier = 1.4
        )
    }
}
