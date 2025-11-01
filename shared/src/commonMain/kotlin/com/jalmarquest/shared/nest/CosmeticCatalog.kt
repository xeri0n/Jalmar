package com.jalmarquest.shared.nest

import com.jalmarquest.shared.inventory.ItemRarity

/**
 * Catalog of all cosmetic items available in the game.
 * Contains 40+ decorative items for nest customization.
 */
object CosmeticCatalog {
    
    // WALL DECORATIONS (10 items)
    private val wallDecorations = listOf(
        Cosmetic(
            id = "wall_twig_picture",
            name = "Twig Picture Frame",
            description = "A simple frame made of twigs. Perfect for that feather you found.",
            type = CosmeticType.WALL_DECORATION,
            rarity = ItemRarity.COMMON,
            width = 1,
            height = 1,
            prestigeValue = 5,
            unlockCondition = null  // Available from start
        ),
        Cosmetic(
            id = "wall_leaf_banner",
            name = "Leaf Banner",
            description = "A dried leaf hung like a banner. Very rustic!",
            type = CosmeticType.WALL_DECORATION,
            rarity = ItemRarity.COMMON,
            width = 2,
            height = 1,
            prestigeValue = 8,
            unlockCondition = UnlockCondition.Level(3)
        ),
        Cosmetic(
            id = "wall_beetle_shell",
            name = "Mounted Beetle Shell",
            description = "Trophy from a hard-fought battle. Intimidating!",
            type = CosmeticType.WALL_DECORATION,
            rarity = ItemRarity.UNCOMMON,
            width = 2,
            height = 2,
            prestigeValue = 15,
            unlockCondition = UnlockCondition.Achievement("beetle_hunter")
        ),
        Cosmetic(
            id = "wall_spider_silk_tapestry",
            name = "Spider Silk Tapestry",
            description = "Delicate webbing stretched into art. Catches the light beautifully.",
            type = CosmeticType.WALL_DECORATION,
            rarity = ItemRarity.RARE,
            width = 3,
            height = 2,
            prestigeValue = 25,
            unlockCondition = UnlockCondition.Purchase(seedsCost = 1000)
        ),
        Cosmetic(
            id = "wall_firefly_jar_hanging",
            name = "Hanging Firefly Jar",
            description = "Fireflies in a jar, hung on the wall. Living night light!",
            type = CosmeticType.WALL_DECORATION,
            rarity = ItemRarity.RARE,
            width = 1,
            height = 2,
            prestigeValue = 30,
            unlockCondition = UnlockCondition.Quest("firefly_rescue")
        ),
        Cosmetic(
            id = "wall_feather_fan",
            name = "Feather Fan Display",
            description = "An arrangement of colorful feathers. Quite fancy!",
            type = CosmeticType.WALL_DECORATION,
            rarity = ItemRarity.UNCOMMON,
            width = 2,
            height = 1,
            prestigeValue = 12,
            unlockCondition = UnlockCondition.Level(7)
        ),
        Cosmetic(
            id = "wall_acorn_wreath",
            name = "Acorn Wreath",
            description = "A circle of acorn caps. Festive!",
            type = CosmeticType.WALL_DECORATION,
            rarity = ItemRarity.COMMON,
            width = 2,
            height = 2,
            prestigeValue = 10,
            unlockCondition = null
        ),
        Cosmetic(
            id = "wall_moss_hanging",
            name = "Hanging Moss",
            description = "Soft green moss draped elegantly. Brings nature inside.",
            type = CosmeticType.WALL_DECORATION,
            rarity = ItemRarity.UNCOMMON,
            width = 1,
            height = 2,
            prestigeValue = 14,
            unlockCondition = UnlockCondition.Discovery("swamp_heart")
        ),
        Cosmetic(
            id = "wall_gnome_map",
            name = "Gnome Fortress Map",
            description = "Detailed map of the Garden Gnome Fortress. Battle planning tool!",
            type = CosmeticType.WALL_DECORATION,
            rarity = ItemRarity.EPIC,
            width = 3,
            height = 2,
            prestigeValue = 40,
            unlockCondition = UnlockCondition.Boss("garden_gnome_king")
        ),
        Cosmetic(
            id = "wall_ancient_rune",
            name = "Ancient Rune Stone",
            description = "A mysterious stone with strange markings. What could it mean?",
            type = CosmeticType.WALL_DECORATION,
            rarity = ItemRarity.LEGENDARY,
            width = 2,
            height = 2,
            prestigeValue = 100,
            unlockCondition = UnlockCondition.Quest("ancient_secrets")
        )
    )
    
    // FLOOR ITEMS (10 items)
    private val floorItems = listOf(
        Cosmetic(
            id = "floor_leaf_rug",
            name = "Dried Leaf Rug",
            description = "Overlapping dried leaves form a comfortable rug.",
            type = CosmeticType.FLOOR_ITEM,
            rarity = ItemRarity.COMMON,
            width = 2,
            height = 2,
            prestigeValue = 6,
            unlockCondition = null
        ),
        Cosmetic(
            id = "floor_pebble_path",
            name = "Pebble Path",
            description = "Small pebbles arranged in a decorative pattern.",
            type = CosmeticType.FLOOR_ITEM,
            rarity = ItemRarity.COMMON,
            width = 3,
            height = 1,
            prestigeValue = 8,
            unlockCondition = null
        ),
        Cosmetic(
            id = "floor_moss_cushion",
            name = "Moss Cushion",
            description = "Soft, springy moss. Perfect for a nap!",
            type = CosmeticType.FLOOR_ITEM,
            rarity = ItemRarity.UNCOMMON,
            width = 1,
            height = 1,
            prestigeValue = 10,
            unlockCondition = UnlockCondition.Level(4)
        ),
        Cosmetic(
            id = "floor_grass_mat",
            name = "Woven Grass Mat",
            description = "Carefully woven grass blades. Took hours to make!",
            type = CosmeticType.FLOOR_ITEM,
            rarity = ItemRarity.UNCOMMON,
            width = 2,
            height = 2,
            prestigeValue = 15,
            unlockCondition = UnlockCondition.Purchase(seedsCost = 500)
        ),
        Cosmetic(
            id = "floor_feather_pillow",
            name = "Feather Pillow",
            description = "Luxuriously soft feather pillow. Peak comfort!",
            type = CosmeticType.FLOOR_ITEM,
            rarity = ItemRarity.RARE,
            width = 1,
            height = 1,
            prestigeValue = 20,
            unlockCondition = UnlockCondition.Level(10)
        ),
        Cosmetic(
            id = "floor_silk_rug",
            name = "Spider Silk Rug",
            description = "Woven spider silk. Impossibly soft and shimmering.",
            type = CosmeticType.FLOOR_ITEM,
            rarity = ItemRarity.EPIC,
            width = 3,
            height = 2,
            prestigeValue = 50,
            unlockCondition = UnlockCondition.Purchase(seedsCost = 2000, glimmerShardsCost = 5)
        ),
        Cosmetic(
            id = "floor_flower_petal_bed",
            name = "Flower Petal Bed",
            description = "Soft petals arranged into a cozy sleeping spot.",
            type = CosmeticType.FLOOR_ITEM,
            rarity = ItemRarity.RARE,
            width = 2,
            height = 1,
            prestigeValue = 22,
            unlockCondition = UnlockCondition.Discovery("flower_garden")
        ),
        Cosmetic(
            id = "floor_sand_circle",
            name = "Sand Bath Circle",
            description = "A designated area for dust bathing. Essential quail luxury!",
            type = CosmeticType.FLOOR_ITEM,
            rarity = ItemRarity.UNCOMMON,
            width = 2,
            height = 2,
            prestigeValue = 18,
            unlockCondition = UnlockCondition.Achievement("dust_bath_master")
        ),
        Cosmetic(
            id = "floor_bark_platform",
            name = "Bark Platform",
            description = "Flat piece of tree bark. Multi-purpose surface!",
            type = CosmeticType.FLOOR_ITEM,
            rarity = ItemRarity.COMMON,
            width = 2,
            height = 1,
            prestigeValue = 7,
            unlockCondition = null
        ),
        Cosmetic(
            id = "floor_crystal_shard",
            name = "Crystal Shard Display",
            description = "Glimmering crystal shard placed on floor. Catches the light!",
            type = CosmeticType.FLOOR_ITEM,
            rarity = ItemRarity.LEGENDARY,
            width = 1,
            height = 1,
            prestigeValue = 80,
            unlockCondition = UnlockCondition.Quest("crystal_caves")
        )
    )
    
    // FURNITURE (10 items)
    private val furniture = listOf(
        Cosmetic(
            id = "furniture_twig_perch",
            name = "Twig Perch",
            description = "A sturdy twig to perch on. Classic quail furniture.",
            type = CosmeticType.FURNITURE,
            rarity = ItemRarity.COMMON,
            width = 1,
            height = 1,
            prestigeValue = 5,
            unlockCondition = null
        ),
        Cosmetic(
            id = "furniture_seed_feeder",
            name = "Seed Feeder",
            description = "Acorn cap repurposed as a seed container. Functional and cute!",
            type = CosmeticType.FURNITURE,
            rarity = ItemRarity.COMMON,
            width = 1,
            height = 1,
            prestigeValue = 6,
            unlockCondition = null
        ),
        Cosmetic(
            id = "furniture_water_droplet_dish",
            name = "Dew Drop Dish",
            description = "Leaf that holds a perfect droplet of water. Refreshing!",
            type = CosmeticType.FURNITURE,
            rarity = ItemRarity.UNCOMMON,
            width = 1,
            height = 1,
            prestigeValue = 10,
            unlockCondition = UnlockCondition.Level(5)
        ),
        Cosmetic(
            id = "furniture_nesting_box",
            name = "Miniature Nesting Box",
            description = "A tiny nesting box within the nest. Nestception!",
            type = CosmeticType.FURNITURE,
            rarity = ItemRarity.RARE,
            width = 2,
            height = 2,
            prestigeValue = 25,
            unlockCondition = UnlockCondition.Purchase(seedsCost = 800)
        ),
        Cosmetic(
            id = "furniture_acorn_chair",
            name = "Acorn Shell Chair",
            description = "Hollowed acorn shell makes a perfect sitting spot.",
            type = CosmeticType.FURNITURE,
            rarity = ItemRarity.UNCOMMON,
            width = 1,
            height = 1,
            prestigeValue = 12,
            unlockCondition = UnlockCondition.Level(6)
        ),
        Cosmetic(
            id = "furniture_pebble_table",
            name = "Flat Pebble Table",
            description = "Smooth pebble serves as a tiny table. Functional!",
            type = CosmeticType.FURNITURE,
            rarity = ItemRarity.COMMON,
            width = 1,
            height = 1,
            prestigeValue = 8,
            unlockCondition = null
        ),
        Cosmetic(
            id = "furniture_shell_shelf",
            name = "Shell Shelf",
            description = "Beetle shell propped up as storage. Industrial chic!",
            type = CosmeticType.FURNITURE,
            rarity = ItemRarity.UNCOMMON,
            width = 2,
            height = 1,
            prestigeValue = 14,
            unlockCondition = UnlockCondition.Achievement("collector")
        ),
        Cosmetic(
            id = "furniture_twig_ladder",
            name = "Twig Ladder",
            description = "Tiny ladder made of twigs. For accessing high spots!",
            type = CosmeticType.FURNITURE,
            rarity = ItemRarity.UNCOMMON,
            width = 1,
            height = 2,
            prestigeValue = 16,
            unlockCondition = UnlockCondition.Level(8)
        ),
        Cosmetic(
            id = "furniture_royal_throne",
            name = "Royal Acorn Throne",
            description = "Majestic throne carved from the largest acorn. For a tiny king!",
            type = CosmeticType.FURNITURE,
            rarity = ItemRarity.EPIC,
            width = 2,
            height = 2,
            prestigeValue = 60,
            unlockCondition = UnlockCondition.Quest("king_of_buttonburgh")
        ),
        Cosmetic(
            id = "furniture_gnome_throne",
            name = "Captured Gnome Throne",
            description = "Piece of the Garden Gnome's throne. Ultimate trophy!",
            type = CosmeticType.FURNITURE,
            rarity = ItemRarity.LEGENDARY,
            width = 3,
            height = 2,
            prestigeValue = 120,
            unlockCondition = UnlockCondition.Boss("garden_gnome_king")
        )
    )
    
    // LIGHTING (5 items)
    private val lighting = listOf(
        Cosmetic(
            id = "light_firefly_jar",
            name = "Firefly Jar",
            description = "Captured fireflies provide gentle illumination.",
            type = CosmeticType.LIGHTING,
            rarity = ItemRarity.UNCOMMON,
            width = 1,
            height = 1,
            prestigeValue = 15,
            unlockCondition = UnlockCondition.Level(5)
        ),
        Cosmetic(
            id = "light_glowing_mushroom",
            name = "Glowing Mushroom",
            description = "Bioluminescent fungus. Natural night light!",
            type = CosmeticType.LIGHTING,
            rarity = ItemRarity.RARE,
            width = 1,
            height = 1,
            prestigeValue = 20,
            unlockCondition = UnlockCondition.Discovery("cave_depths")
        ),
        Cosmetic(
            id = "light_crystal_lamp",
            name = "Crystal Lamp",
            description = "Glowing crystal shard mounted on twig. Magical!",
            type = CosmeticType.LIGHTING,
            rarity = ItemRarity.EPIC,
            width = 1,
            height = 2,
            prestigeValue = 45,
            unlockCondition = UnlockCondition.Purchase(seedsCost = 1500, glimmerShardsCost = 3)
        ),
        Cosmetic(
            id = "light_ember_brazier",
            name = "Tiny Ember Brazier",
            description = "Small pile of glowing embers. Warm and cozy!",
            type = CosmeticType.LIGHTING,
            rarity = ItemRarity.RARE,
            width = 1,
            height = 1,
            prestigeValue = 22,
            unlockCondition = UnlockCondition.Quest("eternal_flame")
        ),
        Cosmetic(
            id = "light_star_shard",
            name = "Fallen Star Shard",
            description = "Fragment of a shooting star. Radiates gentle starlight.",
            type = CosmeticType.LIGHTING,
            rarity = ItemRarity.LEGENDARY,
            width = 1,
            height = 1,
            prestigeValue = 150,
            unlockCondition = UnlockCondition.Quest("wish_upon_star")
        )
    )
    
    // PLANTS (5 items)
    private val plants = listOf(
        Cosmetic(
            id = "plant_potted_clover",
            name = "Potted Clover",
            description = "Lucky four-leaf clover in an acorn cap pot.",
            type = CosmeticType.PLANT,
            rarity = ItemRarity.UNCOMMON,
            width = 1,
            height = 1,
            prestigeValue = 12,
            unlockCondition = UnlockCondition.Level(4)
        ),
        Cosmetic(
            id = "plant_moss_patch",
            name = "Moss Patch",
            description = "Living moss brings freshness to your nest.",
            type = CosmeticType.PLANT,
            rarity = ItemRarity.COMMON,
            width = 2,
            height = 1,
            prestigeValue = 8,
            unlockCondition = null
        ),
        Cosmetic(
            id = "plant_tiny_fern",
            name = "Miniature Fern",
            description = "Delicate fern frond. Adds greenery!",
            type = CosmeticType.PLANT,
            rarity = ItemRarity.UNCOMMON,
            width = 1,
            height = 2,
            prestigeValue = 14,
            unlockCondition = UnlockCondition.Discovery("forest_floor")
        ),
        Cosmetic(
            id = "plant_flowering_vine",
            name = "Flowering Vine",
            description = "Climbing vine with tiny flowers. Beautiful!",
            type = CosmeticType.PLANT,
            rarity = ItemRarity.RARE,
            width = 2,
            height = 3,
            prestigeValue = 30,
            unlockCondition = UnlockCondition.Purchase(seedsCost = 1200)
        ),
        Cosmetic(
            id = "plant_rainbow_flower",
            name = "Rainbow Bloom",
            description = "Legendary flower with petals of every color. Breathtaking!",
            type = CosmeticType.PLANT,
            rarity = ItemRarity.LEGENDARY,
            width = 1,
            height = 1,
            prestigeValue = 100,
            unlockCondition = UnlockCondition.Quest("rainbow_valley")
        )
    )
    
    /**
     * All cosmetics combined into a single list.
     */
    val allCosmetics: List<Cosmetic> = wallDecorations + floorItems + furniture + lighting + plants
    
    /**
     * Total number of cosmetics in the catalog.
     */
    val totalCount: Int = allCosmetics.size  // Should be 40
    
    /**
     * Get a cosmetic by ID.
     */
    fun getCosmeticById(id: String): Cosmetic? = allCosmetics.find { it.id == id }
    
    /**
     * Get all cosmetics of a specific type.
     */
    fun getCosmeticsByType(type: CosmeticType): List<Cosmetic> = 
        allCosmetics.filter { it.type == type }
    
    /**
     * Get all cosmetics of a specific rarity.
     */
    fun getCosmeticsByRarity(rarity: ItemRarity): List<Cosmetic> = 
        allCosmetics.filter { it.rarity == rarity }
    
    /**
     * Get all cosmetics that have no unlock condition (available from start).
     */
    fun getStartingCosmetics(): List<Cosmetic> = 
        allCosmetics.filter { it.unlockCondition == null }
    
    /**
     * Get all cosmetics unlocked by a specific achievement.
     */
    fun getCosmeticsUnlockedByAchievement(achievementId: String): List<Cosmetic> = 
        allCosmetics.filter {
            (it.unlockCondition as? UnlockCondition.Achievement)?.achievementId == achievementId
        }
    
    /**
     * Validate the catalog for duplicate IDs.
     */
    fun validateCatalog(): Result<Unit> {
        val duplicateIds = allCosmetics
            .groupBy { it.id }
            .filter { it.value.size > 1 }
            .keys
        
        return if (duplicateIds.isEmpty()) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalStateException("Duplicate cosmetic IDs found: $duplicateIds"))
        }
    }
}
