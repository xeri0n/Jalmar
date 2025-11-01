package com.jalmarquest.shared.nest

/**
 * Catalog of all hoard items (shiny collectibles) in the game.
 * Button quails are naturally attracted to shiny objects!
 */
object HoardCatalog {
    
    /**
     * Collection set bonuses.
     */
    private val setBonus = listOf(
        SetBonus(
            setId = "rainbow_buttons",
            setName = "Rainbow Button Collection",
            description = "All seven colors of the rainbow in pristine buttons",
            bonusPrestige = 50,
            bonusValueMultiplier = 1.3f
        ),
        SetBonus(
            setId = "ancient_coins",
            setName = "Ancient Coin Treasury",
            description = "Five coins from the Age of Giants",
            bonusPrestige = 100,
            bonusValueMultiplier = 1.5f
        ),
        SetBonus(
            setId = "crystal_garden",
            setName = "Crystal Garden Collection",
            description = "Six natural crystals representing garden minerals",
            bonusPrestige = 150,
            bonusValueMultiplier = 1.4f
        )
    )
    
    private val items = listOf(
        // ========== BUTTONS (Rainbow Set) ==========
        
        HoardItem(
            id = "button_red_ruby",
            name = "Ruby Red Button",
            description = "A brilliant red button that shines like a tiny ruby. Part of the Rainbow Collection.",
            type = HoardItemType.BUTTON,
            rarity = HoardRarity.UNCOMMON,
            baseValue = 100,
            weight = 150,  // 0.15g
            isSetItem = true,
            setId = "rainbow_buttons",
            unlockMethod = "Found in crimson flowers"
        ),
        
        HoardItem(
            id = "button_orange_amber",
            name = "Amber Orange Button",
            description = "A warm orange button with the glow of fossilized amber.",
            type = HoardItemType.BUTTON,
            rarity = HoardRarity.UNCOMMON,
            baseValue = 100,
            weight = 150,
            isSetItem = true,
            setId = "rainbow_buttons",
            unlockMethod = "Found near sunset flowers"
        ),
        
        HoardItem(
            id = "button_yellow_gold",
            name = "Golden Yellow Button",
            description = "A lustrous yellow button that gleams like pure gold.",
            type = HoardItemType.BUTTON,
            rarity = HoardRarity.RARE,
            baseValue = 200,
            weight = 200,  // 0.2g
            isSetItem = true,
            setId = "rainbow_buttons",
            unlockMethod = "Found in sunflower patches"
        ),
        
        HoardItem(
            id = "button_green_emerald",
            name = "Emerald Green Button",
            description = "A deep green button with the brilliance of an emerald.",
            type = HoardItemType.BUTTON,
            rarity = HoardRarity.UNCOMMON,
            baseValue = 100,
            weight = 150,
            isSetItem = true,
            setId = "rainbow_buttons",
            unlockMethod = "Found in moss beds"
        ),
        
        HoardItem(
            id = "button_blue_sapphire",
            name = "Sapphire Blue Button",
            description = "A stunning blue button that rivals a sapphire's depth.",
            type = HoardItemType.BUTTON,
            rarity = HoardRarity.RARE,
            baseValue = 200,
            weight = 150,
            isSetItem = true,
            setId = "rainbow_buttons",
            unlockMethod = "Found near water sources"
        ),
        
        HoardItem(
            id = "button_indigo_midnight",
            name = "Midnight Indigo Button",
            description = "A mysterious indigo button that captures the essence of twilight.",
            type = HoardItemType.BUTTON,
            rarity = HoardRarity.UNCOMMON,
            baseValue = 100,
            weight = 150,
            isSetItem = true,
            setId = "rainbow_buttons",
            unlockMethod = "Found during night exploration"
        ),
        
        HoardItem(
            id = "button_violet_amethyst",
            name = "Amethyst Violet Button",
            description = "A regal violet button with the clarity of an amethyst.",
            type = HoardItemType.BUTTON,
            rarity = HoardRarity.UNCOMMON,
            baseValue = 100,
            weight = 150,
            isSetItem = true,
            setId = "rainbow_buttons",
            unlockMethod = "Found in lavender fields"
        ),
        
        // ========== CRYSTALS (Garden Set) ==========
        
        HoardItem(
            id = "crystal_quartz_clear",
            name = "Clear Quartz Shard",
            description = "A perfectly transparent quartz crystal. Captures and refracts light beautifully.",
            type = HoardItemType.CRYSTAL,
            rarity = HoardRarity.RARE,
            baseValue = 250,
            weight = 300,  // 0.3g
            isSetItem = true,
            setId = "crystal_garden",
            unlockMethod = "Mined from underground caverns"
        ),
        
        HoardItem(
            id = "crystal_rose_quartz",
            name = "Rose Quartz Heart",
            description = "A delicate pink crystal shaped like a tiny heart. Radiates warmth.",
            type = HoardItemType.CRYSTAL,
            rarity = HoardRarity.RARE,
            baseValue = 250,
            weight = 250,
            isSetItem = true,
            setId = "crystal_garden",
            unlockMethod = "Found in rose gardens"
        ),
        
        HoardItem(
            id = "crystal_amethyst_cluster",
            name = "Amethyst Cluster",
            description = "A small cluster of purple amethyst crystals. Each point catches the light.",
            type = HoardItemType.CRYSTAL,
            rarity = HoardRarity.EPIC,
            baseValue = 400,
            weight = 350,
            isSetItem = true,
            setId = "crystal_garden",
            unlockMethod = "Rare drop from crystal beetles"
        ),
        
        HoardItem(
            id = "crystal_citrine_point",
            name = "Citrine Point",
            description = "A golden-yellow citrine crystal with a perfect natural point.",
            type = HoardItemType.CRYSTAL,
            rarity = HoardRarity.RARE,
            baseValue = 250,
            weight = 280,
            isSetItem = true,
            setId = "crystal_garden",
            unlockMethod = "Found in sunny clearings"
        ),
        
        HoardItem(
            id = "crystal_moonstone",
            name = "Moonstone Cabochon",
            description = "A milky white stone with an ethereal blue sheen. Glows faintly in moonlight.",
            type = HoardItemType.CRYSTAL,
            rarity = HoardRarity.EPIC,
            baseValue = 400,
            weight = 220,
            isSetItem = true,
            setId = "crystal_garden",
            unlockMethod = "Found during full moon nights"
        ),
        
        HoardItem(
            id = "crystal_obsidian_shard",
            name = "Obsidian Shard",
            description = "A jet-black volcanic glass shard. Sharp edges and mirror-like surface.",
            type = HoardItemType.CRYSTAL,
            rarity = HoardRarity.RARE,
            baseValue = 250,
            weight = 320,
            isSetItem = true,
            setId = "crystal_garden",
            unlockMethod = "Found near ancient fire pits"
        ),
        
        // ========== COINS (Ancient Set) ==========
        
        HoardItem(
            id = "coin_copper_giant",
            name = "Giant's Copper Penny",
            description = "An ancient copper coin from the Age of Giants. Bears a faded eagle emblem.",
            type = HoardItemType.COIN,
            rarity = HoardRarity.UNCOMMON,
            baseValue = 150,
            weight = 500,  // 0.5g (heavy!)
            isSetItem = true,
            setId = "ancient_coins",
            unlockMethod = "Buried in garden soil"
        ),
        
        HoardItem(
            id = "coin_silver_dime",
            name = "Silver Dime of Ages",
            description = "A tarnished silver coin with mysterious symbols. Still gleams when polished.",
            type = HoardItemType.COIN,
            rarity = HoardRarity.RARE,
            baseValue = 300,
            weight = 400,
            isSetItem = true,
            setId = "ancient_coins",
            unlockMethod = "Found in old stone ruins"
        ),
        
        HoardItem(
            id = "coin_gold_token",
            name = "Golden Arcade Token",
            description = "A golden token from a long-forgotten arcade. Has a star embossed on it.",
            type = HoardItemType.COIN,
            rarity = HoardRarity.EPIC,
            baseValue = 500,
            weight = 350,
            isSetItem = true,
            setId = "ancient_coins",
            unlockMethod = "Rare find in human settlements"
        ),
        
        HoardItem(
            id = "coin_ancient_nickel",
            name = "Ancient Nickel",
            description = "A weathered nickel from centuries past. The date is barely readable.",
            type = HoardItemType.COIN,
            rarity = HoardRarity.UNCOMMON,
            baseValue = 150,
            weight = 450,
            isSetItem = true,
            setId = "ancient_coins",
            unlockMethod = "Dug up from old pathways"
        ),
        
        HoardItem(
            id = "coin_platinum_commemorative",
            name = "Platinum Commemorative Coin",
            description = "An extremely rare platinum coin. Mint condition with intricate designs.",
            type = HoardItemType.COIN,
            rarity = HoardRarity.LEGENDARY,
            baseValue = 1000,
            weight = 600,
            isSetItem = true,
            setId = "ancient_coins",
            unlockMethod = "Ultimate treasure from boss battles"
        ),
        
        // ========== GEMS (Individual Treasures) ==========
        
        HoardItem(
            id = "gem_diamond_tiny",
            name = "Tiny Diamond",
            description = "A minuscule but genuine diamond. Perfect clarity and brilliance.",
            type = HoardItemType.GEM,
            rarity = HoardRarity.LEGENDARY,
            baseValue = 2000,
            weight = 50,  // 0.05g (very light but extremely valuable!)
            isSetItem = false,
            unlockMethod = "Extremely rare drop from elite enemies"
        ),
        
        HoardItem(
            id = "gem_ruby_chip",
            name = "Ruby Chip",
            description = "A tiny fragment of a genuine ruby. Deep red and flawless.",
            type = HoardItemType.GEM,
            rarity = HoardRarity.EPIC,
            baseValue = 600,
            weight = 80,
            isSetItem = false,
            unlockMethod = "Rare mining discovery"
        ),
        
        HoardItem(
            id = "gem_emerald_sliver",
            name = "Emerald Sliver",
            description = "A thin sliver of emerald with incredible green depth.",
            type = HoardItemType.GEM,
            rarity = HoardRarity.EPIC,
            baseValue = 600,
            weight = 70,
            isSetItem = false,
            unlockMethod = "Rare mining discovery"
        ),
        
        // ========== METAL (Reflective Objects) ==========
        
        HoardItem(
            id = "metal_aluminum_foil",
            name = "Crumpled Aluminum Star",
            description = "A piece of aluminum foil shaped like a star. Incredibly reflective!",
            type = HoardItemType.METAL,
            rarity = HoardRarity.COMMON,
            baseValue = 50,
            weight = 30,  // 0.03g (very light)
            isSetItem = false,
            unlockMethod = "Common find in garden debris"
        ),
        
        HoardItem(
            id = "metal_brass_washer",
            name = "Brass Washer",
            description = "A golden brass washer. Simple but satisfyingly shiny.",
            type = HoardItemType.METAL,
            rarity = HoardRarity.COMMON,
            baseValue = 40,
            weight = 200,
            isSetItem = false,
            unlockMethod = "Found near old machinery"
        ),
        
        HoardItem(
            id = "metal_copper_wire",
            name = "Copper Wire Coil",
            description = "A tiny coil of bright copper wire. Catches light beautifully when unraveled.",
            type = HoardItemType.METAL,
            rarity = HoardRarity.UNCOMMON,
            baseValue = 80,
            weight = 100,
            isSetItem = false,
            unlockMethod = "Salvaged from electrical components"
        ),
        
        // ========== GLASS (Colorful Fragments) ==========
        
        HoardItem(
            id = "glass_sea_green",
            name = "Sea Glass - Green",
            description = "A smooth piece of green sea glass. Frosted by time and tide.",
            type = HoardItemType.GLASS,
            rarity = HoardRarity.UNCOMMON,
            baseValue = 120,
            weight = 180,
            isSetItem = false,
            unlockMethod = "Found near water features"
        ),
        
        HoardItem(
            id = "glass_cobalt_blue",
            name = "Cobalt Blue Glass",
            description = "A vivid blue glass fragment. Deep and mesmerizing color.",
            type = HoardItemType.GLASS,
            rarity = HoardRarity.RARE,
            baseValue = 220,
            weight = 150,
            isSetItem = false,
            unlockMethod = "Rare find in old bottles"
        ),
        
        HoardItem(
            id = "glass_rainbow_prism",
            name = "Rainbow Prism Glass",
            description = "A faceted glass piece that splits light into rainbows. Absolutely mesmerizing!",
            type = HoardItemType.GLASS,
            rarity = HoardRarity.EPIC,
            baseValue = 450,
            weight = 200,
            isSetItem = false,
            unlockMethod = "Rare drop from crystal spiders"
        ),
        
        // ========== TRINKETS (Special Items) ==========
        
        HoardItem(
            id = "trinket_glitter_vial",
            name = "Vial of Eternal Glitter",
            description = "A tiny vial filled with sparkly glitter that never stops shimmering.",
            type = HoardItemType.TRINKET,
            rarity = HoardRarity.RARE,
            baseValue = 300,
            weight = 120,
            isSetItem = false,
            unlockMethod = "Gift from friendly NPCs"
        ),
        
        HoardItem(
            id = "trinket_music_box_key",
            name = "Tiny Music Box Key",
            description = "A miniature golden key from a music box. Still plays a faint tune when turned.",
            type = HoardItemType.TRINKET,
            rarity = HoardRarity.EPIC,
            baseValue = 550,
            weight = 90,
            isSetItem = false,
            unlockMethod = "Found in abandoned dollhouses"
        ),
        
        // ========== SPECIAL (Unique Event Items) ==========
        
        HoardItem(
            id = "special_fallen_star",
            name = "Fallen Star Fragment",
            description = "A piece of an actual fallen star. Glows with otherworldly light. The ultimate treasure!",
            type = HoardItemType.SPECIAL,
            rarity = HoardRarity.MYTHICAL,
            baseValue = 5000,
            weight = 100,
            isSetItem = false,
            unlockMethod = "Legendary event reward"
        ),
        
        HoardItem(
            id = "special_dragon_scale",
            name = "Miniature Dragon Scale",
            description = "A tiny iridescent scale from a legendary garden dragon. Pulses with magic.",
            type = HoardItemType.SPECIAL,
            rarity = HoardRarity.MYTHICAL,
            baseValue = 4500,
            weight = 150,
            isSetItem = false,
            unlockMethod = "Ultimate boss drop"
        )
    )
    
    /**
     * Get all hoard items in the catalog.
     */
    fun getAllItems(): List<HoardItem> = items
    
    /**
     * Get hoard item by ID.
     */
    fun getItemById(id: String): HoardItem? {
        return items.find { it.id == id }
    }
    
    /**
     * Get all items of a specific type.
     */
    fun getItemsByType(type: HoardItemType): List<HoardItem> {
        return items.filter { it.type == type }
    }
    
    /**
     * Get all items of a specific rarity.
     */
    fun getItemsByRarity(rarity: HoardRarity): List<HoardItem> {
        return items.filter { it.rarity == rarity }
    }
    
    /**
     * Get all items in a collection set.
     */
    fun getItemsBySet(setId: String): List<HoardItem> {
        return items.filter { it.setId == setId }
    }
    
    /**
     * Get all collection sets.
     */
    fun getAllSets(): List<SetBonus> = setBonus
    
    /**
     * Get set bonus for a specific set ID.
     */
    fun getSetBonus(setId: String): SetBonus? {
        return setBonus.find { it.setId == setId }
    }
}
