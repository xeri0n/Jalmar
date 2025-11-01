package com.jalmarquest.shared.nest

/**
 * Catalog of all critter species available in the game.
 * Contains 10+ species with unique preferences and bonuses.
 */
object CritterCatalog {
    
    private val critters = listOf(
        // COMMON CRITTERS (Easy to satisfy, small bonuses)
        Critter(
            id = "ladybug_common",
            name = "Common Ladybug",
            description = "A cheerful red ladybug. Loves flowers and brings good fortune!",
            type = CritterType.LADYBUG,
            rarity = CritterRarity.COMMON,
            baseBonusValue = 5,
            bonusType = CritterBonusType.LUCK,
            cosmeticPreferences = listOf(
                CosmeticPreference(CosmeticType.PLANT, satisfactionPerItem = 10)
            ),
            preferredNestTier = null,  // Happy in any nest
            maxSatisfaction = 100,
            satisfactionDecayPerDay = 3
        ),
        
        Critter(
            id = "worm_earthworm",
            name = "Earthworm",
            description = "A helpful earthworm. Loves natural materials and promotes health.",
            type = CritterType.WORM,
            rarity = CritterRarity.COMMON,
            baseBonusValue = 3,
            bonusType = CritterBonusType.HP_REGEN,
            cosmeticPreferences = listOf(
                CosmeticPreference(CosmeticType.FLOOR_ITEM, satisfactionPerItem = 8),
                CosmeticPreference(CosmeticType.PLANT, satisfactionPerItem = 5)
            ),
            preferredNestTier = NestTier.BASIC,
            maxSatisfaction = 80,
            satisfactionDecayPerDay = 2
        ),
        
        Critter(
            id = "ant_worker",
            name = "Worker Ant",
            description = "An industrious ant. Appreciates organization and finds hidden treasures.",
            type = CritterType.ANT,
            rarity = CritterRarity.COMMON,
            baseBonusValue = 4,
            bonusType = CritterBonusType.ITEM_FIND,
            cosmeticPreferences = listOf(
                CosmeticPreference(CosmeticType.FURNITURE, satisfactionPerItem = 10)
            ),
            preferredNestTier = null,
            maxSatisfaction = 90,
            satisfactionDecayPerDay = 4
        ),
        
        // UNCOMMON CRITTERS (Moderate requirements, decent bonuses)
        Critter(
            id = "firefly_common",
            name = "Common Firefly",
            description = "A glowing firefly. Loves light sources and boosts energy recovery.",
            type = CritterType.FIREFLY,
            rarity = CritterRarity.UNCOMMON,
            baseBonusValue = 6,
            bonusType = CritterBonusType.STAMINA_REGEN,
            cosmeticPreferences = listOf(
                CosmeticPreference(CosmeticType.LIGHTING, satisfactionPerItem = 15)
            ),
            preferredNestTier = NestTier.COMFORTABLE,
            maxSatisfaction = 100,
            satisfactionDecayPerDay = 5
        ),
        
        Critter(
            id = "snail_garden",
            name = "Garden Snail",
            description = "A patient snail. Enjoys moisture and calm. Promotes thoughtful learning.",
            type = CritterType.SNAIL,
            rarity = CritterRarity.UNCOMMON,
            baseBonusValue = 5,
            bonusType = CritterBonusType.XP_GAIN,
            cosmeticPreferences = listOf(
                CosmeticPreference(CosmeticType.PLANT, satisfactionPerItem = 10),
                CosmeticPreference(CosmeticType.FLOOR_ITEM, satisfactionPerItem = 5)
            ),
            preferredNestTier = null,
            maxSatisfaction = 110,
            satisfactionDecayPerDay = 3
        ),
        
        Critter(
            id = "moth_silk",
            name = "Silk Moth",
            description = "A delicate moth. Loves soft materials and grants silent movement.",
            type = CritterType.MOTH,
            rarity = CritterRarity.UNCOMMON,
            baseBonusValue = 7,
            bonusType = CritterBonusType.STEALTH,
            cosmeticPreferences = listOf(
                CosmeticPreference(CosmeticType.FLOOR_ITEM, satisfactionPerItem = 12),
                CosmeticPreference(CosmeticType.WALL_DECORATION, satisfactionPerItem = 6)
            ),
            preferredNestTier = NestTier.COMFORTABLE,
            maxSatisfaction = 100,
            satisfactionDecayPerDay = 5
        ),
        
        // RARE CRITTERS (Picky eaters, strong bonuses)
        Critter(
            id = "beetle_stag",
            name = "Stag Beetle",
            description = "A mighty stag beetle. Admires trophies and provides stalwart defense.",
            type = CritterType.BEETLE,
            rarity = CritterRarity.RARE,
            baseBonusValue = 8,
            bonusType = CritterBonusType.DEFENSE,
            cosmeticPreferences = listOf(
                CosmeticPreference(CosmeticType.TROPHY, satisfactionPerItem = 20),
                CosmeticPreference(CosmeticType.FURNITURE, satisfactionPerItem = 8)
            ),
            preferredNestTier = NestTier.LUXURIOUS,
            maxSatisfaction = 120,
            satisfactionDecayPerDay = 6
        ),
        
        Critter(
            id = "grasshopper_spring",
            name = "Spring Grasshopper",
            description = "An energetic grasshopper. Loves open space and boosts movement.",
            type = CritterType.GRASSHOPPER,
            rarity = CritterRarity.RARE,
            baseBonusValue = 10,
            bonusType = CritterBonusType.MOVEMENT_SPEED,
            cosmeticPreferences = listOf(
                CosmeticPreference(CosmeticType.PLANT, satisfactionPerItem = 12)
                // Prefers LESS cosmetics (more open space)
            ),
            preferredNestTier = null,
            maxSatisfaction = 100,
            satisfactionDecayPerDay = 7
        ),
        
        Critter(
            id = "spider_jumping",
            name = "Jumping Spider",
            description = "A clever jumping spider. Loves corners and shadows. Enhances precision.",
            type = CritterType.SPIDER,
            rarity = CritterRarity.RARE,
            baseBonusValue = 9,
            bonusType = CritterBonusType.CRITICAL_CHANCE,
            cosmeticPreferences = listOf(
                CosmeticPreference(CosmeticType.WALL_DECORATION, satisfactionPerItem = 15),
                CosmeticPreference(CosmeticType.FURNITURE, satisfactionPerItem = 10)
            ),
            preferredNestTier = NestTier.COMFORTABLE,
            maxSatisfaction = 110,
            satisfactionDecayPerDay = 6
        ),
        
        // EPIC CRITTERS (Very demanding, powerful bonuses)
        Critter(
            id = "butterfly_monarch",
            name = "Monarch Butterfly",
            description = "A majestic monarch butterfly. Seeks beauty and prestige. Radiates joy.",
            type = CritterType.BUTTERFLY,
            rarity = CritterRarity.EPIC,
            baseBonusValue = 15,
            bonusType = CritterBonusType.HAPPINESS,
            cosmeticPreferences = listOf(
                CosmeticPreference(CosmeticType.PLANT, satisfactionPerItem = 20),
                CosmeticPreference(CosmeticType.WALL_DECORATION, satisfactionPerItem = 15),
                CosmeticPreference(CosmeticType.LIGHTING, satisfactionPerItem = 10)
            ),
            preferredNestTier = NestTier.LUXURIOUS,
            maxSatisfaction = 150,
            satisfactionDecayPerDay = 8
        ),
        
        // LEGENDARY CRITTERS (Extremely rare and powerful)
        Critter(
            id = "firefly_rainbow",
            name = "Rainbow Firefly",
            description = "A legendary firefly with rainbow glow. Grants immense vitality to those it blesses.",
            type = CritterType.FIREFLY,
            rarity = CritterRarity.LEGENDARY,
            baseBonusValue = 20,
            bonusType = CritterBonusType.STAMINA_REGEN,
            cosmeticPreferences = listOf(
                CosmeticPreference(CosmeticType.LIGHTING, satisfactionPerItem = 30),
                CosmeticPreference(CosmeticType.PLANT, satisfactionPerItem = 20),
                CosmeticPreference(CosmeticType.SPECIAL, satisfactionPerItem = 25)
            ),
            preferredNestTier = NestTier.LUXURIOUS,
            maxSatisfaction = 200,
            satisfactionDecayPerDay = 10
        )
    )
    
    /**
     * Total number of critters in catalog.
     */
    val totalCount: Int = critters.size  // 11 critters
    
    /**
     * Get all critters.
     */
    val allCritters: List<Critter> = critters
    
    /**
     * Get critter by ID.
     */
    fun getCritterById(id: String): Critter? = critters.find { it.id == id }
    
    /**
     * Get all critters of a specific type.
     */
    fun getCrittersByType(type: CritterType): List<Critter> = 
        critters.filter { it.type == type }
    
    /**
     * Get all critters of a specific rarity.
     */
    fun getCrittersByRarity(rarity: CritterRarity): List<Critter> = 
        critters.filter { it.rarity == rarity }
    
    /**
     * Get all critters that prefer a specific nest tier.
     */
    fun getCrittersByPreferredTier(tier: NestTier): List<Critter> = 
        critters.filter { it.preferredNestTier == tier }
    
    /**
     * Get all critters that provide a specific bonus type.
     */
    fun getCrittersByBonusType(bonusType: CritterBonusType): List<Critter> = 
        critters.filter { it.bonusType == bonusType }
    
    /**
     * Validate catalog for duplicate IDs.
     */
    fun validateCatalog(): Result<Unit> {
        val duplicateIds = critters
            .groupBy { it.id }
            .filter { it.value.size > 1 }
            .keys
        
        return if (duplicateIds.isEmpty()) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalStateException("Duplicate critter IDs found: $duplicateIds"))
        }
    }
}
