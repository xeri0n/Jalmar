package com.jalmarquest.shared.world

import kotlinx.serialization.Serializable

/**
 * Biome types that define the environmental characteristics of locations.
 */
@Serializable
enum class BiomeType {
    FOREST,      // Dense woods with trees and wildlife
    MOUNTAIN,    // Rocky peaks and cliffs
    GRASSLAND,   // Open plains and meadows
    DESERT,      // Arid sandy regions
    SWAMP,       // Wetlands and marshes
    TUNDRA,      // Frozen wastelands
    COASTAL,     // Beaches and shorelines
    CAVE         // Underground caverns
}

/**
 * Properties and modifiers for each biome type.
 */
@Serializable
data class BiomeProperties(
    val type: BiomeType,
    val movementCostMultiplier: Double = 1.0,
    val resourceAbundance: Double = 1.0,
    val dangerLevel: Int = 1,
    val weatherSensitivity: Double = 1.0,
    val description: String = ""
) {
    companion object {
        fun getDefaultProperties(type: BiomeType): BiomeProperties {
            return when (type) {
                BiomeType.FOREST -> BiomeProperties(
                    type = BiomeType.FOREST,
                    movementCostMultiplier = 1.2,
                    resourceAbundance = 1.5,
                    dangerLevel = 2,
                    weatherSensitivity = 0.8,
                    description = "Dense forests with abundant resources but limited visibility"
                )
                BiomeType.MOUNTAIN -> BiomeProperties(
                    type = BiomeType.MOUNTAIN,
                    movementCostMultiplier = 1.8,
                    resourceAbundance = 0.8,
                    dangerLevel = 3,
                    weatherSensitivity = 1.5,
                    description = "Treacherous rocky terrain with harsh weather"
                )
                BiomeType.GRASSLAND -> BiomeProperties(
                    type = BiomeType.GRASSLAND,
                    movementCostMultiplier = 0.8,
                    resourceAbundance = 1.0,
                    dangerLevel = 1,
                    weatherSensitivity = 1.0,
                    description = "Open plains with good visibility and easy travel"
                )
                BiomeType.DESERT -> BiomeProperties(
                    type = BiomeType.DESERT,
                    movementCostMultiplier = 1.5,
                    resourceAbundance = 0.5,
                    dangerLevel = 2,
                    weatherSensitivity = 1.3,
                    description = "Arid wasteland with scarce resources and extreme temperatures"
                )
                BiomeType.SWAMP -> BiomeProperties(
                    type = BiomeType.SWAMP,
                    movementCostMultiplier = 2.0,
                    resourceAbundance = 1.3,
                    dangerLevel = 3,
                    weatherSensitivity = 0.7,
                    description = "Murky wetlands with hidden dangers and rare resources"
                )
                BiomeType.TUNDRA -> BiomeProperties(
                    type = BiomeType.TUNDRA,
                    movementCostMultiplier = 1.6,
                    resourceAbundance = 0.6,
                    dangerLevel = 3,
                    weatherSensitivity = 1.8,
                    description = "Frozen wasteland with extreme cold and sparse life"
                )
                BiomeType.COASTAL -> BiomeProperties(
                    type = BiomeType.COASTAL,
                    movementCostMultiplier = 1.0,
                    resourceAbundance = 1.4,
                    dangerLevel = 1,
                    weatherSensitivity = 1.2,
                    description = "Coastal areas with maritime resources and moderate weather"
                )
                BiomeType.CAVE -> BiomeProperties(
                    type = BiomeType.CAVE,
                    movementCostMultiplier = 1.3,
                    resourceAbundance = 1.2,
                    dangerLevel = 4,
                    weatherSensitivity = 0.0,
                    description = "Dark underground passages with valuable minerals but high danger"
                )
            }
        }
    }
}
