package com.jalmarquest.shared.nest

import kotlinx.serialization.Serializable

/**
 * Represents the player's nest and its current upgrade level.
 * The nest provides stat bonuses that improve with each tier upgrade.
 * 
 * @property id Unique identifier for the nest
 * @property tier Current upgrade tier (BASIC, COMFORTABLE, LUXURIOUS)
 * @property customName Optional custom name given by player (null uses default tier name)
 * @property unlockedCosmetics Set of cosmetic IDs that have been unlocked
 * @property placedCosmetics List of cosmetics currently placed in the nest
 * @property critters List of critters currently living in the nest
 */
@Serializable
data class Nest(
    val id: String,
    val tier: NestTier = NestTier.BASIC,
    val customName: String? = null,
    val unlockedCosmetics: Set<String> = emptySet(),
    val placedCosmetics: List<PlacedCosmetic> = emptyList(),
    val critters: List<NestCritter> = emptyList()
) {
    init {
        require(id.isNotBlank()) { "Nest ID cannot be blank" }
        if (customName != null) {
            require(customName.isNotBlank()) { "Custom name cannot be blank if provided" }
            require(customName.length <= 50) { "Custom name cannot exceed 50 characters" }
        }
        require(critters.size <= CritterManager.MAX_CRITTERS_PER_NEST) { 
            "Nest cannot have more than ${CritterManager.MAX_CRITTERS_PER_NEST} critters" 
        }
    }
    
    /**
     * Gets the display name for this nest.
     * Returns custom name if set, otherwise tier's default name.
     */
    fun getDisplayName(): String = customName ?: tier.defaultName
    
    /**
     * Gets the total prestige value from all placed cosmetics.
     */
    fun getTotalPrestige(): Int {
        return placedCosmetics.sumOf { placed ->
            CosmeticCatalog.getCosmeticById(placed.cosmeticId)?.prestigeValue ?: 0
        }
    }
    
    /**
     * Gets the total bonus of a specific type from all critters.
     * Requires a CritterManager instance to calculate.
     */
    fun getCritterBonus(bonusType: CritterBonusType, critterManager: CritterManager): Int {
        return critterManager.calculateTotalBonus(critters, bonusType)
    }
    
    /**
     * Gets all active bonuses from critters as a map.
     * Requires a CritterManager instance to calculate.
     */
    fun getAllCritterBonuses(critterManager: CritterManager): Map<CritterBonusType, Int> {
        return critterManager.getAllBonuses(critters)
    }
}

/**
 * Nest upgrade tiers with linear progression.
 * Each tier provides increasing stat bonuses to the player.
 */
@Serializable
enum class NestTier(
    val defaultName: String,
    val description: String,
    val requiredLevel: Int,
    val hpRegenBonus: Float,
    val staminaRegenBonus: Float,
    val xpBonus: Float
) {
    /** Starting nest tier, no stat bonuses */
    BASIC(
        defaultName = "Simple Nest",
        description = "A basic nest with minimal comfort. Better than sleeping on the ground, at least.",
        requiredLevel = 1,
        hpRegenBonus = 0.0f,
        staminaRegenBonus = 0.0f,
        xpBonus = 0.0f
    ),
    
    /** Mid-tier nest with moderate stat bonuses */
    COMFORTABLE(
        defaultName = "Comfortable Nest",
        description = "A cozy nest lined with soft materials. Rest here brings better recovery.",
        requiredLevel = 5,
        hpRegenBonus = 0.10f,  // +10% HP regen
        staminaRegenBonus = 0.05f,  // +5% stamina regen
        xpBonus = 0.0f
    ),
    
    /** Top-tier nest with maximum stat bonuses */
    LUXURIOUS(
        defaultName = "Luxurious Nest",
        description = "An exquisite nest of the finest materials. True sanctuary for a tiny hero.",
        requiredLevel = 10,
        hpRegenBonus = 0.20f,  // +20% HP regen
        staminaRegenBonus = 0.10f,  // +10% stamina regen
        xpBonus = 0.05f  // +5% XP gain
    );
    
    /**
     * Gets the next tier in progression, or null if already at max tier.
     */
    fun getNextTier(): NestTier? = when (this) {
        BASIC -> COMFORTABLE
        COMFORTABLE -> LUXURIOUS
        LUXURIOUS -> null
    }
    
    /**
     * Checks if this tier can be upgraded to the target tier.
     * Only allows linear progression (cannot skip tiers).
     */
    fun canUpgradeTo(target: NestTier): Boolean {
        return target == getNextTier()
    }
}

/**
 * Material requirements for upgrading a nest tier.
 * Each upgrade tier requires specific materials from the player's inventory.
 * 
 * @property targetTier The tier this upgrade leads to
 * @property requiredMaterials Map of item IDs to quantities needed
 */
@Serializable
data class NestUpgradeRequirements(
    val targetTier: NestTier,
    val requiredMaterials: Map<String, Int>
) {
    init {
        require(requiredMaterials.isNotEmpty()) { "Upgrade must require at least one material" }
        requiredMaterials.forEach { (itemId, quantity) ->
            require(itemId.isNotBlank()) { "Material item ID cannot be blank" }
            require(quantity > 0) { "Material quantity must be positive: $itemId requires $quantity" }
        }
    }
    
    companion object {
        /**
         * Gets the material requirements for upgrading from current tier to next.
         * Returns null if already at max tier.
         * 
         * Uses existing ItemCatalog items:
         * - "twig" (common material from ItemCatalog)
         * - "dried_leaf" (common material from ItemCatalog)
         * - "grass_blade" (common material from ItemCatalog, used for soft nesting)
         * - "feather" (common material from ItemCatalog, soft padding)
         * - "spider_silk" (now added to ItemCatalog - uncommon material for luxury nests)
         */
        fun forTier(currentTier: NestTier): NestUpgradeRequirements? {
            val targetTier = currentTier.getNextTier() ?: return null
            
            return when (targetTier) {
                NestTier.COMFORTABLE -> NestUpgradeRequirements(
                    targetTier = NestTier.COMFORTABLE,
                    requiredMaterials = mapOf(
                        "twig" to 20,
                        "dried_leaf" to 30,
                        "grass_blade" to 10
                    )
                )
                
                NestTier.LUXURIOUS -> NestUpgradeRequirements(
                    targetTier = NestTier.LUXURIOUS,
                    requiredMaterials = mapOf(
                        "twig" to 50,
                        "dried_leaf" to 40,
                        "grass_blade" to 20,
                        "spider_silk" to 10,
                        "feather" to 5
                    )
                )
                
                NestTier.BASIC -> null  // Cannot downgrade to BASIC
            }
        }
    }
}

/**
 * Stat modifiers applied to the player based on their nest tier.
 * These bonuses are applied when resting or as passive effects.
 * 
 * @property hpRegenMultiplier HP regeneration rate multiplier (1.0 = 100%, 1.1 = 110%)
 * @property staminaRegenMultiplier Stamina regeneration rate multiplier
 * @property xpGainMultiplier XP gain multiplier when earning experience
 */
@Serializable
data class NestStatModifiers(
    val hpRegenMultiplier: Float = 1.0f,
    val staminaRegenMultiplier: Float = 1.0f,
    val xpGainMultiplier: Float = 1.0f
) {
    init {
        require(hpRegenMultiplier >= 1.0f) { "HP regen multiplier cannot be less than 1.0: $hpRegenMultiplier" }
        require(staminaRegenMultiplier >= 1.0f) { "Stamina regen multiplier cannot be less than 1.0: $staminaRegenMultiplier" }
        require(xpGainMultiplier >= 1.0f) { "XP gain multiplier cannot be less than 1.0: $xpGainMultiplier" }
    }
    
    companion object {
        /**
         * Creates stat modifiers for a given nest tier.
         */
        fun fromTier(tier: NestTier): NestStatModifiers {
            return NestStatModifiers(
                hpRegenMultiplier = 1.0f + tier.hpRegenBonus,
                staminaRegenMultiplier = 1.0f + tier.staminaRegenBonus,
                xpGainMultiplier = 1.0f + tier.xpBonus
            )
        }
    }
}

/**
 * Visual state data for displaying the nest in the UI.
 * Contains tier-specific descriptions and ASCII art patterns.
 * 
 * @property tier The nest tier this visual state represents
 * @property asciiArt Multi-line ASCII art representation
 * @property flavorText Short flavor text describing the nest's appearance
 */
@Serializable
data class NestVisualState(
    val tier: NestTier,
    val asciiArt: String,
    val flavorText: String
) {
    init {
        require(asciiArt.isNotBlank()) { "ASCII art cannot be blank" }
        require(flavorText.isNotBlank()) { "Flavor text cannot be blank" }
    }
    
    companion object {
        /**
         * Gets the visual state for a given nest tier.
         */
        fun forTier(tier: NestTier): NestVisualState {
            return when (tier) {
                NestTier.BASIC -> NestVisualState(
                    tier = NestTier.BASIC,
                    asciiArt = """
                        |    ___
                        |   /   \
                        |  |  o  |  <- You
                        |   \___/
                        |  ~~~~~~~
                    """.trimMargin(),
                    flavorText = "A hastily assembled pile of twigs and leaves. It's a start."
                )
                
                NestTier.COMFORTABLE -> NestVisualState(
                    tier = NestTier.COMFORTABLE,
                    asciiArt = """
                        |     ____
                        |   /      \
                        |  |   o    |  <- Cozy!
                        |   \______/
                        |  ~~~~~~~~~
                        |  Soft Grass
                    """.trimMargin(),
                    flavorText = "Lined with soft grass and carefully arranged. You could get used to this."
                )
                
                NestTier.LUXURIOUS -> NestVisualState(
                    tier = NestTier.LUXURIOUS,
                    asciiArt = """
                        |      _____
                        |    /       \
                        |   |    o    |  <- Paradise!
                        |    \_______/
                        |   ~~~~~~~~~~~
                        |  Feathers & Silk
                        |    (Premium)
                    """.trimMargin(),
                    flavorText = "Woven with spider silk and lined with feathers. A masterpiece of quail engineering."
                )
            }
        }
    }
}
