package com.jalmarquest.shared.crafting

import kotlinx.serialization.Serializable

/**
 * Categories for crafting recipes.
 * Determines the type of item produced.
 */
@Serializable
enum class CraftingCategory {
    /** Equipment items (weapons, armor, accessories) */
    EQUIPMENT,
    
    /** Consumable items (food, potions) */
    CONSUMABLE,
    
    /** Material items (refined materials, components) */
    MATERIAL,
    
    /** Upgrade recipes (improve existing equipment) */
    UPGRADE,
    
    /** Special/unique items */
    SPECIAL
}
