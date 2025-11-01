package com.jalmarquest.shared.world

import com.jalmarquest.shared.model.Season
import kotlinx.serialization.Serializable

/**
 * Location description with optional seasonal variants.
 * 
 * Supports dynamic descriptions that change based on the current season,
 * creating an immersive world that evolves throughout the year.
 * 
 * Design Philosophy:
 * - Base description is always available (fallback)
 * - Seasonal variants are optional (use base if not specified)
 * - Maintains authenticity of "tiny hero, big world" theme
 */
@Serializable
data class LocationDescription(
    val base: String,
    val spring: String? = null,
    val summer: String? = null,
    val autumn: String? = null,
    val winter: String? = null
) {
    init {
        require(base.isNotBlank()) { "Base description cannot be blank" }
    }
    
    /**
     * Get the description for the specified season.
     * Falls back to base description if seasonal variant is not defined.
     */
    fun getSeasonalDescription(season: Season): String {
        return when (season) {
            Season.SPRING -> spring ?: base
            Season.SUMMER -> summer ?: base
            Season.AUTUMN -> autumn ?: base
            Season.WINTER -> winter ?: base
        }
    }
    
    /**
     * Check if this location has seasonal variants defined.
     */
    fun hasSeasonalVariants(): Boolean {
        return spring != null || summer != null || autumn != null || winter != null
    }
    
    /**
     * Get the number of defined seasonal variants.
     */
    fun getVariantCount(): Int {
        return listOfNotNull(spring, summer, autumn, winter).size
    }
    
    companion object {
        /**
         * Create a simple location description with no seasonal variants.
         */
        fun simple(description: String): LocationDescription {
            return LocationDescription(base = description)
        }
        
        /**
         * Create a location description with all four seasons defined.
         */
        fun withAllSeasons(
            spring: String,
            summer: String,
            autumn: String,
            winter: String
        ): LocationDescription {
            return LocationDescription(
                base = spring, // Use spring as base
                spring = spring,
                summer = summer,
                autumn = autumn,
                winter = winter
            )
        }
    }
}
