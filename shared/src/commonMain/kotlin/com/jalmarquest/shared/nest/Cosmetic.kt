package com.jalmarquest.shared.nest

import com.jalmarquest.shared.inventory.ItemRarity
import kotlinx.serialization.Serializable

/**
 * Types of cosmetic items that can be placed in the nest.
 * Different types may have different placement rules or visual styles.
 */
@Serializable
enum class CosmeticType {
    /** Wall decorations (pictures, banners, shields) */
    WALL_DECORATION,
    
    /** Floor items (rugs, cushions, stones) */
    FLOOR_ITEM,
    
    /** Furniture (perches, feeders, nesting boxes) */
    FURNITURE,
    
    /** Lighting (firefly jars, glowing mushrooms) */
    LIGHTING,
    
    /** Plants (potted flowers, moss, vines) */
    PLANT,
    
    /** Trophy items (mounted beetles, feathers, shells) */
    TROPHY,
    
    /** Special/seasonal items (holiday decorations, unique finds) */
    SPECIAL
}

/**
 * Represents a cosmetic item that can be placed in the player's nest.
 * 
 * @property id Unique identifier
 * @property name Display name
 * @property description Flavor text
 * @property type Category of cosmetic
 * @property rarity Rarity tier (affects unlock difficulty and prestige value)
 * @property width Grid width (1-3 tiles)
 * @property height Grid height (1-3 tiles)
 * @property prestigeValue Contribution to nest prestige score
 * @property unlockCondition How this item is unlocked (null = available from start)
 */
@Serializable
data class Cosmetic(
    val id: String,
    val name: String,
    val description: String,
    val type: CosmeticType,
    val rarity: ItemRarity,
    val width: Int,
    val height: Int,
    val prestigeValue: Int,
    val unlockCondition: UnlockCondition? = null
) {
    init {
        require(id.isNotBlank()) { "Cosmetic ID cannot be blank" }
        require(name.isNotBlank()) { "Cosmetic name cannot be blank" }
        require(width in 1..3) { "Cosmetic width must be 1-3 tiles" }
        require(height in 1..3) { "Cosmetic height must be 1-3 tiles" }
        require(prestigeValue >= 0) { "Prestige value cannot be negative" }
    }
    
    /**
     * Calculate the total grid size occupied by this cosmetic.
     */
    fun getGridSize(): Int = width * height
    
    /**
     * Get a formatted size string for UI display.
     */
    fun getFormattedSize(): String = "${width}x${height}"
}

/**
 * Conditions that must be met to unlock a cosmetic item.
 */
@Serializable
sealed class UnlockCondition {
    /** Unlocked by completing a specific achievement */
    @Serializable
    data class Achievement(val achievementId: String) : UnlockCondition()
    
    /** Unlocked by reaching a specific player level */
    @Serializable
    data class Level(val requiredLevel: Int) : UnlockCondition() {
        init {
            require(requiredLevel >= 1) { "Required level must be at least 1" }
        }
    }
    
    /** Unlocked by purchasing from a shop */
    @Serializable
    data class Purchase(val seedsCost: Long, val glimmerShardsCost: Long = 0) : UnlockCondition() {
        init {
            require(seedsCost >= 0) { "Seeds cost cannot be negative" }
            require(glimmerShardsCost >= 0) { "Glimmer shards cost cannot be negative" }
            require(seedsCost > 0 || glimmerShardsCost > 0) { "At least one currency cost must be positive" }
        }
    }
    
    /** Unlocked by completing a specific quest */
    @Serializable
    data class Quest(val questId: String) : UnlockCondition()
    
    /** Unlocked by defeating a specific boss/enemy */
    @Serializable
    data class Boss(val enemyId: String) : UnlockCondition()
    
    /** Unlocked by finding in a specific location */
    @Serializable
    data class Discovery(val locationId: String) : UnlockCondition()
}

/**
 * Represents a placed cosmetic item in the nest grid.
 * 
 * @property cosmeticId Reference to the Cosmetic definition
 * @property gridX X position on nest grid (0-indexed)
 * @property gridY Y position on nest grid (0-indexed)
 */
@Serializable
data class PlacedCosmetic(
    val cosmeticId: String,
    val gridX: Int,
    val gridY: Int
) {
    init {
        require(cosmeticId.isNotBlank()) { "Cosmetic ID cannot be blank" }
        require(gridX >= 0) { "Grid X cannot be negative" }
        require(gridY >= 0) { "Grid Y cannot be negative" }
    }
}

/**
 * Configuration for the nest placement grid.
 * Defines the available space for placing cosmetics.
 */
object NestGridConfig {
    /** Grid width in tiles */
    const val GRID_WIDTH = 8
    
    /** Grid height in tiles */
    const val GRID_HEIGHT = 6
    
    /** Total tiles available */
    const val TOTAL_TILES = GRID_WIDTH * GRID_HEIGHT  // 48 tiles
    
    /**
     * Check if coordinates are within grid bounds.
     */
    fun isWithinBounds(x: Int, y: Int): Boolean {
        return x in 0 until GRID_WIDTH && y in 0 until GRID_HEIGHT
    }
    
    /**
     * Check if a rectangle fits within grid bounds.
     */
    fun fitsInBounds(x: Int, y: Int, width: Int, height: Int): Boolean {
        return x >= 0 && y >= 0 && (x + width) <= GRID_WIDTH && (y + height) <= GRID_HEIGHT
    }
}
