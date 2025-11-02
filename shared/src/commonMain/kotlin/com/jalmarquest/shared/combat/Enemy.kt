package com.jalmarquest.shared.combat

import kotlinx.serialization.Serializable

/**
 * AI behavior patterns for enemies in combat.
 * Determines how an enemy chooses actions during its turn.
 */
@Serializable
enum class EnemyBehaviorType {
    /** Always attacks, prioritizes low-HP targets */
    AGGRESSIVE,
    
    /** Defends when HP < 50%, attacks otherwise */
    DEFENSIVE,
    
    /** Attempts to flee when HP < 30%, attacks when healthy */
    FLEEING,
    
    /** Chooses random action each turn (chaos enemy) */
    RANDOM,
    
    /** Future: Heals/buffs allies, attacks when alone */
    SUPPORTIVE
}

/**
 * A single item that can drop from an enemy.
 * @property itemId The ID of the item from ItemCatalog
 * @property minQuantity Minimum number to drop (>= 1)
 * @property maxQuantity Maximum number to drop (>= minQuantity)
 * @property dropChance Probability of this item dropping (0.0 to 1.0)
 */
@Serializable
data class LootDrop(
    val itemId: String,
    val minQuantity: Int,
    val maxQuantity: Int,
    val dropChance: Float
) {
    init {
        require(itemId.isNotBlank()) { "Item ID cannot be blank" }
        require(minQuantity >= 1) { "Min quantity must be at least 1, got $minQuantity" }
        require(maxQuantity >= minQuantity) { "Max quantity must be >= min quantity, got max=$maxQuantity, min=$minQuantity" }
        require(dropChance in 0.0..1.0) { "Drop chance must be 0.0-1.0, got $dropChance" }
    }
}

/**
 * Enemy loot table defining all possible item drops.
 * @property drops List of possible item drops
 */
@Serializable
data class LootTable(
    val drops: List<LootDrop> = emptyList()
) {
    /**
     * Returns a human-readable summary of the loot table.
     */
    fun summary(): String {
        if (drops.isEmpty()) return "No loot"
        return drops.joinToString(", ") { drop ->
            val qty = if (drop.minQuantity == drop.maxQuantity) "${drop.minQuantity}" else "${drop.minQuantity}-${drop.maxQuantity}"
            "$qty ${drop.itemId} (${(drop.dropChance * 100).toInt()}%)"
        }
    }
}

/**
 * Enemy data model for the enemy catalog.
 * Contains all information needed to create enemy encounters.
 * 
 * @property id Unique identifier for this enemy type
 * @property name Display name (e.g., "The Hopper", "Armored Titan")
 * @property description Flavor text describing the enemy
 * @property maxHp Maximum hit points
 * @property strength Physical damage stat
 * @property agility Initiative and dodge stat
 * @property vitality Defense stat
 * @property intelligence Magic damage stat (future)
 * @property luck Critical hit stat
 * @property baseDamage Base damage for attacks
 * @property defense Damage reduction
 * @property behaviorType AI behavior pattern
 * @property lootTable Items this enemy can drop
 * @property xpReward Experience points awarded on defeat
 * @property level Enemy level (for scaling)
 */
@Serializable
data class Enemy(
    val id: String,
    val name: String,
    val description: String,
    val maxHp: Int,
    val strength: Int,
    val agility: Int,
    val vitality: Int,
    val intelligence: Int,
    val luck: Int,
    val baseDamage: Int,
    val defense: Int,
    val behaviorType: EnemyBehaviorType,
    val lootTable: LootTable = LootTable(),
    val xpReward: Int = 0,
    val level: Int = 1
) {
    init {
        require(id.isNotBlank()) { "Enemy ID cannot be blank" }
        require(name.isNotBlank()) { "Enemy name cannot be blank" }
        require(maxHp > 0) { "Max HP must be positive, got $maxHp" }
        require(strength >= 0) { "Strength cannot be negative, got $strength" }
        require(agility >= 0) { "Agility cannot be negative, got $agility" }
        require(vitality >= 0) { "Vitality cannot be negative, got $vitality" }
        require(intelligence >= 0) { "Intelligence cannot be negative, got $intelligence" }
        require(luck >= 0) { "Luck cannot be negative, got $luck" }
        require(baseDamage >= 0) { "Base damage cannot be negative, got $baseDamage" }
        require(defense >= 0) { "Defense cannot be negative, got $defense" }
        require(xpReward >= 0) { "XP reward cannot be negative, got $xpReward" }
        require(level >= 1) { "Level must be at least 1, got $level" }
    }
    
    /**
     * Creates combat data from this enemy template.
     * @param instanceId Unique ID for this specific enemy instance in combat
     */
    fun toCombatData(instanceId: String): EnemyCombatData {
        return EnemyCombatData(
            id = instanceId,
            name = name,
            currentHp = maxHp,
            maxHp = maxHp,
            strength = strength,
            agility = agility,
            vitality = vitality,
            intelligence = intelligence,
            luck = luck,
            baseDamage = baseDamage,
            defense = defense,
            xpReward = xpReward,
            catalogId = id, // Store catalog ID for loot lookup
            activeStatusEffects = emptyList()
        )
    }
    
    /**
     * Returns a formatted stat block for display.
     */
    fun statBlock(): String = buildString {
        appendLine("$name (Level $level)")
        appendLine("HP: $maxHp | ATK: $baseDamage | DEF: $defense")
        appendLine("STR: $strength | AGI: $agility | VIT: $vitality | INT: $intelligence | LCK: $luck")
        appendLine("Behavior: $behaviorType")
        appendLine("XP: $xpReward")
        appendLine("Loot: ${lootTable.summary()}")
    }
}
