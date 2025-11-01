package com.jalmarquest.shared.equipment

import kotlinx.serialization.Serializable

/**
 * Defines the 7 equipment slots available to the player.
 * Each slot can hold one piece of equipment at a time.
 */
@Serializable
enum class EquipmentSlot {
    /** Headwear (helmets, hats, crowns) */
    HEAD,
    
    /** Necklaces, amulets, collars */
    NECK,
    
    /** Body armor (chest pieces, cloaks, robes) */
    BODY,
    
    /** Leg armor (leggings, greaves) */
    LEGS,
    
    /** Footwear (boots, sandals, talons) */
    FEET,
    
    /** Primary weapon (spears, swords, staves) */
    WEAPON,
    
    /** Accessories (rings, charms, trinkets) */
    ACCESSORY
}
