package com.jalmarquest.shared.inventory

import kotlinx.serialization.Serializable

/**
 * Rarity tiers for items in JalmarQuest.
 * Affects visual presentation, vendor value, and drop rates.
 */
@Serializable
enum class ItemRarity {
    /** Common items - gray color, abundant */
    COMMON,
    
    /** Uncommon items - green color, moderately rare */
    UNCOMMON,
    
    /** Rare items - blue color, hard to find */
    RARE,
    
    /** Epic items - purple color, very rare */
    EPIC,
    
    /** Legendary items - gold color, extremely rare or unique */
    LEGENDARY;
    
    /**
     * Color hint for UI display (CSS-style hex colors).
     */
    fun displayColor(): String = when (this) {
        COMMON -> "#9D9D9D"      // Gray
        UNCOMMON -> "#1EFF00"    // Green
        RARE -> "#0070DD"        // Blue
        EPIC -> "#A335EE"        // Purple
        LEGENDARY -> "#FF8000"   // Orange/Gold
    }
    
    /**
     * Value multiplier for vendor prices.
     */
    fun valueMultiplier(): Double = when (this) {
        COMMON -> 1.0
        UNCOMMON -> 2.0
        RARE -> 5.0
        EPIC -> 10.0
        LEGENDARY -> 25.0
    }
}
