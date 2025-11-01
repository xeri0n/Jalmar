package com.jalmarquest.shared.skills

import kotlinx.serialization.Serializable

/**
 * Skill progression tiers with level and point cost requirements.
 */
@Serializable
enum class SkillTier(val requiredLevel: Int, val skillPointCost: Int) {
    /** Basic skills available from level 1 */
    TIER_1(requiredLevel = 1, skillPointCost = 1),
    
    /** Intermediate skills unlocked at level 10 */
    TIER_2(requiredLevel = 10, skillPointCost = 2),
    
    /** Advanced skills unlocked at level 20 */
    TIER_3(requiredLevel = 20, skillPointCost = 3),
    
    /** Expert skills unlocked at level 30 */
    TIER_4(requiredLevel = 30, skillPointCost = 4),
    
    /** Ultimate skills unlocked at level 40 */
    ULTIMATE(requiredLevel = 40, skillPointCost = 5);
    
    fun displayName(): String = when (this) {
        TIER_1 -> "Tier 1"
        TIER_2 -> "Tier 2"
        TIER_3 -> "Tier 3"
        TIER_4 -> "Tier 4"
        ULTIMATE -> "Ultimate"
    }
}
