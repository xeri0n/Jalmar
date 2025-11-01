package com.jalmarquest.shared.inventory

import kotlinx.serialization.Serializable

/**
 * Categories of items in JalmarQuest.
 * Each type has different behaviors and usage patterns.
 */
@Serializable
enum class ItemType {
    /** Food, potions, and other single-use items */
    CONSUMABLE,
    
    /** Weapons, armor, and wearable items */
    EQUIPMENT,
    
    /** Crafting components and raw materials */
    MATERIAL,
    
    /** Quest-specific items (often non-stackable, non-droppable) */
    QUEST,
    
    /** Unique or miscellaneous items */
    SPECIAL,
    
    /** Currency items (Seeds, though handled separately in CurrencyManager) */
    CURRENCY
}
