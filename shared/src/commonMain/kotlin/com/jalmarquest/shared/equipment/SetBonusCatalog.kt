package com.jalmarquest.shared.equipment

/**
 * Static catalog of all set bonuses in JalmarQuest.
 * Set bonuses activate when a player equips multiple items from the same set.
 */
object SetBonusCatalog {
    
    private val bonuses = mapOf(
        "acorn_armor_set" to SetBonus(
            setId = "acorn_armor_set",
            requiredPieces = 2,
            bonusStats = StatModifier(vitality = 2, strength = 1),
            name = "Acorn Armor Set",
            description = "Wearing multiple pieces of Acorn Armor grants +2 Vitality, +1 Strength"
        )
    )
    
    /**
     * Returns the set bonus for the given set ID, or null if not found.
     */
    fun getSetBonus(setId: String): SetBonus? {
        return bonuses[setId]
    }
    
    /**
     * Returns all available set bonuses.
     */
    fun getAllSetBonuses(): List<SetBonus> {
        return bonuses.values.toList()
    }
    
    /**
     * Returns all set IDs.
     */
    fun getAllSetIds(): List<String> {
        return bonuses.keys.toList()
    }
}
