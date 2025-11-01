package com.jalmarquest.shared.equipment

import kotlinx.serialization.Serializable

/**
 * Defines a set bonus that activates when multiple items from the same set are equipped.
 */
@Serializable
data class SetBonus(
    /** Unique identifier for this set (e.g., "acorn_armor_set") */
    val setId: String,
    
    /** Number of set pieces required to activate this bonus */
    val requiredPieces: Int,
    
    /** Stat bonuses granted when set is active */
    val bonusStats: StatModifier,
    
    /** Human-readable name of the set (e.g., "Acorn Armor Set") */
    val name: String,
    
    /** Description of the bonus effect */
    val description: String
) {
    init {
        require(setId.isNotBlank()) { "Set ID cannot be blank" }
        require(requiredPieces in 2..7) { "Required pieces must be 2-7: $requiredPieces" }
        require(name.isNotBlank()) { "Set name cannot be blank" }
        require(!bonusStats.isEmpty()) { "Set bonus must provide at least one stat" }
    }
}
