package com.jalmarquest.shared.inventory

import com.jalmarquest.shared.equipment.EquipmentSlot
import com.jalmarquest.shared.equipment.StatModifier

/**
 * Static catalog of all items in JalmarQuest.
 * Follows the same pattern as LocationCatalog for static game data.
 * 
 * Initial implementation contains ~30 items. Will expand to 200+ over time.
 * All items follow quail-scale realism with weights in milligrams.
 */
object ItemCatalog {
    
    private val items: Map<String, Item> = buildMap {
        // ===== MATERIALS (Mundane → Epic Transformation) =====
        
        put("twig", Item(
            id = "twig",
            name = "Twig",
            description = "A small fallen branch. Unremarkable to humans, but the stuff of legends to a quail.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 1,
            weight = 500,  // 0.5g - realistic small twig
            stackable = true,
            maxStack = 99
        ))
        
        put("acorn_cap", Item(
            id = "acorn_cap",
            name = "Acorn Cap",
            description = "The protective cap of an acorn. Perfect for crafting armor in the tiny world.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 2,
            weight = 300,  // 0.3g - realistic acorn cap
            stackable = true,
            maxStack = 99
        ))
        
        put("pebble", Item(
            id = "pebble",
            name = "Pebble",
            description = "A small smooth stone. Heavy, but useful for certain crafts.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 1,
            weight = 2000,  // 2g - hefty for a quail
            stackable = true,
            maxStack = 50  // Reduced stack due to weight
        ))
        
        put("feather", Item(
            id = "feather",
            name = "Feather",
            description = "A soft down feather. Light as air, perfect for padding.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 3,
            weight = 50,  // 0.05g - realistic feather
            stackable = true,
            maxStack = 99
        ))
        
        put("dried_leaf", Item(
            id = "dried_leaf",
            name = "Dried Leaf",
            description = "A crispy autumn leaf. Brittle but useful for crafting.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 1,
            weight = 100,  // 0.1g - lightweight
            stackable = true,
            maxStack = 99
        ))
        
        put("grass_blade", Item(
            id = "grass_blade",
            name = "Grass Blade",
            description = "A long blade of grass. Flexible and surprisingly strong.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 1,
            weight = 80,  // 0.08g
            stackable = true,
            maxStack = 99
        ))
        
        put("bark_chip", Item(
            id = "bark_chip",
            name = "Bark Chip",
            description = "A piece of tree bark. Rough and sturdy.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 2,
            weight = 400,  // 0.4g
            stackable = true,
            maxStack = 99
        ))
        
        put("pine_needle", Item(
            id = "pine_needle",
            name = "Pine Needle",
            description = "A sharp evergreen needle. Pointy at both ends.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 1,
            weight = 60,  // 0.06g
            stackable = true,
            maxStack = 99
        ))
        
        put("spider_silk", Item(
            id = "spider_silk",
            name = "Spider Silk",
            description = "Delicate silken thread spun by spiders. Incredibly strong for its weight. Perfect for luxury nest padding.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 8,
            weight = 20,  // 0.02g - extremely lightweight
            stackable = true,
            maxStack = 99
        ))
        
        put("moss_clump", Item(
            id = "moss_clump",
            name = "Moss Clump",
            description = "A soft, damp handful of moss. Perfect for padding and moisture retention.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 1,
            weight = 200,  // 0.2g
            stackable = true,
            maxStack = 99
        ))
        
        put("lichen_scrap", Item(
            id = "lichen_scrap",
            name = "Lichen Scrap",
            description = "A piece of hardy lichen. Resilient and weather-resistant.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 2,
            weight = 150,  // 0.15g
            stackable = true,
            maxStack = 99
        ))
        
        put("dandelion_fluff", Item(
            id = "dandelion_fluff",
            name = "Dandelion Fluff",
            description = "Silky white seeds from a dandelion. Floats on the slightest breeze.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 1,
            weight = 10,  // 0.01g - ultra-light
            stackable = true,
            maxStack = 99
        ))
        
        put("rose_petal", Item(
            id = "rose_petal",
            name = "Rose Petal",
            description = "A delicate petal from a wild rose. Fragrant and colorful.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 3,
            weight = 120,  // 0.12g
            stackable = true,
            maxStack = 99
        ))
        
        put("clover_leaf", Item(
            id = "clover_leaf",
            name = "Clover Leaf",
            description = "A three-leaf clover. Some say a four-leaf brings luck...",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 1,
            weight = 60,  // 0.06g
            stackable = true,
            maxStack = 99
        ))
        
        put("wheat_stalk", Item(
            id = "wheat_stalk",
            name = "Wheat Stalk",
            description = "A hollow stalk of wheat. Surprisingly sturdy for its weight.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 2,
            weight = 300,  // 0.3g
            stackable = true,
            maxStack = 99
        ))
        
        put("corn_husk", Item(
            id = "corn_husk",
            name = "Corn Husk",
            description = "A dried leaf from an ear of corn. Tough and fibrous.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 2,
            weight = 250,  // 0.25g
            stackable = true,
            maxStack = 99
        ))
        
        put("maple_seed", Item(
            id = "maple_seed",
            name = "Maple Seed",
            description = "A 'helicopter' seed with delicate wings. Spins as it falls.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 1,
            weight = 90,  // 0.09g
            stackable = true,
            maxStack = 99
        ))
        
        put("oak_seed", Item(
            id = "oak_seed",
            name = "Acorn",
            description = "A whole acorn, cap still attached. The size of a boulder to a quail.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 3,
            weight = 3000,  // 3g - very heavy!
            stackable = true,
            maxStack = 20  // Heavy, reduced stack
        ))
        
        put("thistle_down", Item(
            id = "thistle_down",
            name = "Thistle Down",
            description = "Fluffy white seeds from a thistle plant. Soft but clingy.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 1,
            weight = 15,  // 0.015g
            stackable = true,
            maxStack = 99
        ))
        
        put("reed_segment", Item(
            id = "reed_segment",
            name = "Reed Segment",
            description = "A hollow section of cattail reed. Could be used as a tube.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 2,
            weight = 400,  // 0.4g
            stackable = true,
            maxStack = 99
        ))
        
        put("birch_bark", Item(
            id = "birch_bark",
            name = "Birch Bark",
            description = "A papery strip of white birch bark. Peels off easily.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 2,
            weight = 350,  // 0.35g
            stackable = true,
            maxStack = 99
        ))
        
        put("fern_frond", Item(
            id = "fern_frond",
            name = "Fern Frond",
            description = "A curled frond from a young fern. Delicate and green.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 2,
            weight = 180,  // 0.18g
            stackable = true,
            maxStack = 99
        ))
        
        put("mushroom_cap", Item(
            id = "mushroom_cap",
            name = "Mushroom Cap",
            description = "The cap of a small mushroom. Could be used as an umbrella or shield.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 3,
            weight = 800,  // 0.8g
            stackable = true,
            maxStack = 50
        ))
        
        put("mushroom_stem", Item(
            id = "mushroom_stem",
            name = "Mushroom Stem",
            description = "The fibrous stem of a mushroom. Spongy but sturdy.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 2,
            weight = 500,  // 0.5g
            stackable = true,
            maxStack = 99
        ))
        
        put("sand_grain", Item(
            id = "sand_grain",
            name = "Sand Grain",
            description = "A single grain of sand. Tiny to humans, but a substantial pebble to a quail.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 1,
            weight = 50,  // 0.05g
            stackable = true,
            maxStack = 99
        ))
        
        put("clay_chunk", Item(
            id = "clay_chunk",
            name = "Clay Chunk",
            description = "A malleable lump of wet clay. Can be molded and dried.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 3,
            weight = 1500,  // 1.5g - dense and heavy
            stackable = true,
            maxStack = 30
        ))
        
        put("pebble_smooth", Item(
            id = "pebble_smooth",
            name = "Smooth Pebble",
            description = "A water-worn pebble, perfectly smooth. Pleasing to hold.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 2,
            weight = 2500,  // 2.5g - heavy
            stackable = true,
            maxStack = 40
        ))
        
        put("pebble_sharp", Item(
            id = "pebble_sharp",
            name = "Sharp Pebble",
            description = "A jagged stone with sharp edges. Dangerous but useful.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 4,
            weight = 1800,  // 1.8g
            stackable = true,
            maxStack = 50
        ))
        
        put("charcoal_bit", Item(
            id = "charcoal_bit",
            name = "Charcoal Bit",
            description = "A small piece of charred wood. Leaves black marks.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.COMMON,
            value = 2,
            weight = 100,  // 0.1g - very light
            stackable = true,
            maxStack = 99
        ))
        
        // ===== UNCOMMON MATERIALS =====
        
        put("beetle_shell", Item(
            id = "beetle_shell",
            name = "Beetle Shell",
            description = "The hard carapace of a beetle. Excellent for crafting sturdy armor.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 12,
            weight = 1200,  // 1.2g
            stackable = true,
            maxStack = 50
        ))
        
        put("crystal_shard", Item(
            id = "crystal_shard",
            name = "Crystal Shard",
            description = "A fragment of quartz crystal. Catches and refracts light beautifully.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 20,
            weight = 800,  // 0.8g
            stackable = true,
            maxStack = 50
        ))
        
        put("iron_filing", Item(
            id = "iron_filing",
            name = "Iron Filing",
            description = "Tiny shavings of iron metal. Magnetic and dense.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 15,
            weight = 300,  // 0.3g - metal is dense
            stackable = true,
            maxStack = 99
        ))
        
        put("copper_wire", Item(
            id = "copper_wire",
            name = "Copper Wire",
            description = "A thin strand of copper wire. Flexible and conductive.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 18,
            weight = 250,  // 0.25g
            stackable = true,
            maxStack = 99
        ))
        
        put("glass_fragment", Item(
            id = "glass_fragment",
            name = "Glass Fragment",
            description = "A tiny shard of broken glass. Sharp and transparent.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 10,
            weight = 400,  // 0.4g
            stackable = true,
            maxStack = 50
        ))
        
        put("mirror_piece", Item(
            id = "mirror_piece",
            name = "Mirror Piece",
            description = "A reflective piece of mirror. Could be used to signal or dazzle.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 25,
            weight = 600,  // 0.6g
            stackable = true,
            maxStack = 30
        ))
        
        put("wasp_stinger", Item(
            id = "wasp_stinger",
            name = "Wasp Stinger",
            description = "The barbed stinger of a wasp. Deadly sharp and slightly venomous.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 22,
            weight = 30,  // 0.03g
            stackable = true,
            maxStack = 99
        ))
        
        put("ant_mandible", Item(
            id = "ant_mandible",
            name = "Ant Mandible",
            description = "The powerful jaw of an ant. Serrated and sharp.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 14,
            weight = 80,  // 0.08g
            stackable = true,
            maxStack = 99
        ))
        
        put("dragonfly_wing", Item(
            id = "dragonfly_wing",
            name = "Dragonfly Wing",
            description = "A gossamer wing from a dragonfly. Iridescent and delicate.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 30,
            weight = 25,  // 0.025g
            stackable = true,
            maxStack = 99
        ))
        
        put("moth_wing", Item(
            id = "moth_wing",
            name = "Moth Wing",
            description = "A powdery wing from a large moth. Patterned and soft.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 16,
            weight = 40,  // 0.04g
            stackable = true,
            maxStack = 99
        ))
        
        put("snail_shell", Item(
            id = "snail_shell",
            name = "Snail Shell",
            description = "The spiral shell of a garden snail. Hard and decorative.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 18,
            weight = 2000,  // 2g - calcium carbonate, heavy
            stackable = true,
            maxStack = 30
        ))
        
        put("amber_resin", Item(
            id = "amber_resin",
            name = "Amber Resin",
            description = "Fossilized tree sap, golden and translucent. Ancient and precious.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 40,
            weight = 500,  // 0.5g
            stackable = true,
            maxStack = 50
        ))
        
        put("beeswax", Item(
            id = "beeswax",
            name = "Beeswax",
            description = "Pure wax from a beehive. Waterproof and moldable.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 28,
            weight = 700,  // 0.7g
            stackable = true,
            maxStack = 50
        ))
        
        put("honeycomb", Item(
            id = "honeycomb",
            name = "Honeycomb",
            description = "A section of hexagonal honeycomb. Structural perfection.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 35,
            weight = 900,  // 0.9g
            stackable = true,
            maxStack = 40
        ))
        
        put("bone_fragment", Item(
            id = "bone_fragment",
            name = "Bone Fragment",
            description = "A tiny piece of bone from a small creature. Hard and white.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 12,
            weight = 300,  // 0.3g
            stackable = true,
            maxStack = 99
        ))
        
        put("tooth_shard", Item(
            id = "tooth_shard",
            name = "Tooth Shard",
            description = "A fragment of a tooth. Sharp enough to use as a blade.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 18,
            weight = 200,  // 0.2g
            stackable = true,
            maxStack = 99
        ))
        
        put("claw_tip", Item(
            id = "claw_tip",
            name = "Claw Tip",
            description = "The pointed tip of a creature's claw. Curved and deadly.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 20,
            weight = 150,  // 0.15g
            stackable = true,
            maxStack = 99
        ))
        
        put("scale_fragment", Item(
            id = "scale_fragment",
            name = "Scale Fragment",
            description = "A small scale from a fish or reptile. Shiny and protective.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 14,
            weight = 100,  // 0.1g
            stackable = true,
            maxStack = 99
        ))
        
        put("fur_tuft", Item(
            id = "fur_tuft",
            name = "Fur Tuft",
            description = "Soft fur from a mammal. Warm and insulating.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 10,
            weight = 50,  // 0.05g
            stackable = true,
            maxStack = 99
        ))
        
        put("seed_oil", Item(
            id = "seed_oil",
            name = "Seed Oil",
            description = "Oil extracted from sunflower seeds. Slick and useful.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 25,
            weight = 100,  // 0.1g - oil is light
            stackable = true,
            maxStack = 50
        ))
        
        put("pine_resin", Item(
            id = "pine_resin",
            name = "Pine Resin",
            description = "Sticky sap from a pine tree. Adhesive and aromatic.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 16,
            weight = 400,  // 0.4g
            stackable = true,
            maxStack = 50
        ))
        
        put("flint_chip", Item(
            id = "flint_chip",
            name = "Flint Chip",
            description = "A piece of flint stone. Strikes sparks when hit with metal.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 22,
            weight = 1000,  // 1g
            stackable = true,
            maxStack = 50
        ))
        
        put("coal_nugget", Item(
            id = "coal_nugget",
            name = "Coal Nugget",
            description = "A small lump of coal. Burns hot and long.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 18,
            weight = 800,  // 0.8g
            stackable = true,
            maxStack = 50
        ))
        
        put("sulfur_powder", Item(
            id = "sulfur_powder",
            name = "Sulfur Powder",
            description = "Yellow sulfur dust. Smells terrible but useful in alchemy.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 30,
            weight = 200,  // 0.2g
            stackable = true,
            maxStack = 99
        ))
        
        put("salt_crystal", Item(
            id = "salt_crystal",
            name = "Salt Crystal",
            description = "A cube of rock salt. Preservative and flavor enhancer.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.UNCOMMON,
            value = 12,
            weight = 600,  // 0.6g
            stackable = true,
            maxStack = 99
        ))
        
        // ===== RARE MATERIALS =====
        
        put("diamond_dust", Item(
            id = "diamond_dust",
            name = "Diamond Dust",
            description = "Microscopic diamond particles. Sparkles with unmatched brilliance.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.RARE,
            value = 150,
            weight = 50,  // 0.05g - very dense
            stackable = true,
            maxStack = 50
        ))
        
        put("ruby_chip", Item(
            id = "ruby_chip",
            name = "Ruby Chip",
            description = "A tiny fragment of ruby gemstone. Deep crimson and radiant.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.RARE,
            value = 200,
            weight = 300,  // 0.3g
            stackable = true,
            maxStack = 50
        ))
        
        put("sapphire_chip", Item(
            id = "sapphire_chip",
            name = "Sapphire Chip",
            description = "A small piece of sapphire. Deep blue and mesmerizing.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.RARE,
            value = 210,
            weight = 320,  // 0.32g
            stackable = true,
            maxStack = 50
        ))
        
        put("emerald_chip", Item(
            id = "emerald_chip",
            name = "Emerald Chip",
            description = "A fragment of emerald gem. Vivid green and precious.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.RARE,
            value = 220,
            weight = 310,  // 0.31g
            stackable = true,
            maxStack = 50
        ))
        
        put("moonstone_fragment", Item(
            id = "moonstone_fragment",
            name = "Moonstone Fragment",
            description = "A piece of moonstone with ethereal inner glow. Said to hold moon magic.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.RARE,
            value = 180,
            weight = 400,  // 0.4g
            stackable = true,
            maxStack = 50
        ))
        
        put("obsidian_shard", Item(
            id = "obsidian_shard",
            name = "Obsidian Shard",
            description = "Volcanic glass, sharper than any metal. Black and deadly.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.RARE,
            value = 120,
            weight = 700,  // 0.7g
            stackable = true,
            maxStack = 50
        ))
        
        put("meteorite_fragment", Item(
            id = "meteorite_fragment",
            name = "Meteorite Fragment",
            description = "A piece of stone that fell from the sky. Impossibly hard and strange.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.RARE,
            value = 250,
            weight = 1500,  // 1.5g - very dense
            stackable = true,
            maxStack = 30
        ))
        
        put("dragon_scale", Item(
            id = "dragon_scale",
            name = "Dragon Scale",
            description = "A scale from a 'dragon' (actually a large lizard, but terrifying to a quail). Iridescent and tough.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.RARE,
            value = 280,
            weight = 900,  // 0.9g
            stackable = true,
            maxStack = 30
        ))
        
        put("phoenix_feather", Item(
            id = "phoenix_feather",
            name = "Phoenix Feather",
            description = "A feather from a rare red bird. Warm to the touch, never burns.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.RARE,
            value = 300,
            weight = 30,  // 0.03g
            stackable = true,
            maxStack = 20
        ))
        
        put("unicorn_hair", Item(
            id = "unicorn_hair",
            name = "Unicorn Hair",
            description = "A single strand of silvery hair from a white horse (called 'unicorn' by quails). Magical properties.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.RARE,
            value = 260,
            weight = 10,  // 0.01g
            stackable = true,
            maxStack = 50
        ))
        
        put("ghost_silk", Item(
            id = "ghost_silk",
            name = "Ghost Silk",
            description = "Ethereal spider silk that glows faintly in darkness. Nearly invisible.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.RARE,
            value = 200,
            weight = 5,  // 0.005g - ultra-light
            stackable = true,
            maxStack = 99
        ))
        
        put("void_essence", Item(
            id = "void_essence",
            name = "Void Essence",
            description = "A dark, swirling liquid that seems to absorb light. Origin unknown.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.RARE,
            value = 320,
            weight = 150,  // 0.15g
            stackable = true,
            maxStack = 20
        ))
        
        put("star_dust", Item(
            id = "star_dust",
            name = "Star Dust",
            description = "Glittering particles that shimmer with all colors. Falls from the night sky.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.RARE,
            value = 240,
            weight = 20,  // 0.02g
            stackable = true,
            maxStack = 50
        ))
        
        put("mithril_shaving", Item(
            id = "mithril_shaving",
            name = "Mithril Shaving",
            description = "A sliver of legendary mithril metal. Lighter than feather, stronger than steel.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.RARE,
            value = 400,
            weight = 15,  // 0.015g - impossibly light for metal
            stackable = true,
            maxStack = 50
        ))
        
        put("ancient_scroll_fragment", Item(
            id = "ancient_scroll_fragment",
            name = "Ancient Scroll Fragment",
            description = "A piece of parchment covered in indecipherable runes. Hums with power.",
            type = ItemType.MATERIAL,
            rarity = ItemRarity.RARE,
            value = 180,
            weight = 80,  // 0.08g
            stackable = true,
            maxStack = 30
        ))
        
        // ===== EQUIPMENT (Crafted Items) =====
        
        put("twig_spear", Item(
            id = "twig_spear",
            name = "Twig Spear",
            description = "A mighty weapon forged from the finest twig. Grumble Forgepaw's craftsmanship shines through.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 50,
            weight = 800,  // 0.8g - crafted, slightly heavier
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.WEAPON,
            stats = StatModifier(strength = 3, agility = 1),
            maxDurability = 100
        ))
        
        put("acorn_helmet", Item(
            id = "acorn_helmet",
            name = "Acorn Helmet",
            description = "A sturdy helmet fashioned from an acorn cap. Protection worthy of a tiny hero.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 60,
            weight = 600,  // 0.6g - hollowed out, lighter than raw cap
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.HEAD,
            stats = StatModifier(vitality = 2),
            maxDurability = 80,
            setId = "acorn_armor_set"
        ))
        
        put("leaf_cloak", Item(
            id = "leaf_cloak",
            name = "Leaf Cloak",
            description = "A camouflage cloak woven from dried leaves. Blends perfectly with the forest floor.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 40,
            weight = 200,  // 0.2g - lightweight armor
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.BODY,
            stats = StatModifier(agility = 2, luck = 1),
            maxDurability = 60
        ))
        
        put("feather_charm", Item(
            id = "feather_charm",
            name = "Feather Charm",
            description = "A delicate charm made from a pristine feather. Said to bring luck.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 100,
            weight = 80,  // 0.08g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(luck = 3, agility = 1),
            maxDurability = 50
        ))
        
        // ===== WEAPONS =====
        
        put("pebble_hammer", Item(
            id = "pebble_hammer",
            name = "Pebble Hammer",
            description = "A smooth pebble lashed to a twig. Devastating impact weapon.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.COMMON,
            value = 45,
            weight = 2800,  // 2.8g - heavy weapon
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.WEAPON,
            stats = StatModifier(strength = 5),  // Heavy, so no agility bonus
            maxDurability = 120
        ))
        
        put("thorn_dagger", Item(
            id = "thorn_dagger",
            name = "Thorn Dagger",
            description = "A razor-sharp rose thorn mounted on a grass handle. Quick and deadly.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 55,
            weight = 150,  // 0.15g - lightweight
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.WEAPON,
            stats = StatModifier(strength = 2, agility = 4),
            maxDurability = 60
        ))
        
        put("needle_rapier", Item(
            id = "needle_rapier",
            name = "Needle Rapier",
            description = "A pine needle fashioned into an elegant piercing sword.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 60,
            weight = 90,  // 0.09g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.WEAPON,
            stats = StatModifier(strength = 3, agility = 3),
            maxDurability = 70
        ))
        
        put("glass_blade", Item(
            id = "glass_blade",
            name = "Glass Blade",
            description = "A weapon forged from sharpened glass fragment. Incredibly sharp but fragile.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 120,
            weight = 600,  // 0.6g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.WEAPON,
            stats = StatModifier(strength = 8, agility = 2),
            maxDurability = 40  // Fragile
        ))
        
        put("flint_axe", Item(
            id = "flint_axe",
            name = "Flint Axe",
            description = "A crude but effective axe made from chipped flint.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 70,
            weight = 1200,  // 1.2g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.WEAPON,
            stats = StatModifier(strength = 6, vitality = 1),
            maxDurability = 100
        ))
        
        put("stinger_spear", Item(
            id = "stinger_spear",
            name = "Stinger Spear",
            description = "A wasp stinger mounted on a reed shaft. Venomous tip included.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 140,
            weight = 450,  // 0.45g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.WEAPON,
            stats = StatModifier(strength = 7, intelligence = 2),
            maxDurability = 80
        ))
        
        put("mandible_sword", Item(
            id = "mandible_sword",
            name = "Mandible Sword",
            description = "An ant's mandible crafted into a serrated sword. Nature's chainsaw.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 150,
            weight = 180,  // 0.18g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.WEAPON,
            stats = StatModifier(strength = 8, agility = 1),
            maxDurability = 90
        ))
        
        put("obsidian_dagger", Item(
            id = "obsidian_dagger",
            name = "Obsidian Dagger",
            description = "A blade of volcanic glass, sharper than any metal. Legendary sharpness.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.EPIC,
            value = 300,
            weight = 800,  // 0.8g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.WEAPON,
            stats = StatModifier(strength = 12, agility = 3),
            maxDurability = 50  // Sharp but fragile
        ))
        
        put("mithril_spear", Item(
            id = "mithril_spear",
            name = "Mithril Spear",
            description = "A legendary spear forged from mithril. Light as air, strong as steel.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.EPIC,
            value = 500,
            weight = 100,  // 0.1g - impossibly light
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.WEAPON,
            stats = StatModifier(strength = 10, agility = 8),
            maxDurability = 200
        ))
        
        put("claw_gauntlet", Item(
            id = "claw_gauntlet",
            name = "Claw Gauntlet",
            description = "Claws mounted on a feather base. Extends your natural attacks.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 65,
            weight = 200,  // 0.2g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.WEAPON,
            stats = StatModifier(strength = 4, agility = 3),
            maxDurability = 75
        ))
        
        put("seed_sling", Item(
            id = "seed_sling",
            name = "Seed Sling",
            description = "A sling made from spider silk. Launch pebbles at range.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.COMMON,
            value = 40,
            weight = 80,  // 0.08g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.WEAPON,
            stats = StatModifier(strength = 2, agility = 5, luck = 1),
            maxDurability = 50
        ))
        
        put("twig_bow", Item(
            id = "twig_bow",
            name = "Twig Bow",
            description = "A curved twig with spider silk string. Shoots grass blade arrows.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 75,
            weight = 600,  // 0.6g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.WEAPON,
            stats = StatModifier(strength = 3, agility = 6),
            maxDurability = 80
        ))
        
        put("copper_blade", Item(
            id = "copper_blade",
            name = "Copper Blade",
            description = "A sword forged from copper wire. Conducts magic well.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 160,
            weight = 400,  // 0.4g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.WEAPON,
            stats = StatModifier(strength = 7, intelligence = 4),
            maxDurability = 100
        ))
        
        put("bone_club", Item(
            id = "bone_club",
            name = "Bone Club",
            description = "A crude club fashioned from bone fragments. Heavy and brutal.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 50,
            weight = 900,  // 0.9g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.WEAPON,
            stats = StatModifier(strength = 6, vitality = 2),
            maxDurability = 110
        ))
        
        put("crystal_staff", Item(
            id = "crystal_staff",
            name = "Crystal Staff",
            description = "A twig topped with a crystal shard. Channels magical energy.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 180,
            weight = 1100,  // 1.1g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.WEAPON,
            stats = StatModifier(intelligence = 8, luck = 3),
            maxDurability = 70
        ))
        
        put("dragon_fang", Item(
            id = "dragon_fang",
            name = "Dragon Fang Blade",
            description = "A blade crafted from a lizard's tooth. Called 'dragon fang' by quails.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.EPIC,
            value = 400,
            weight = 700,  // 0.7g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.WEAPON,
            stats = StatModifier(strength = 14, vitality = 2),
            maxDurability = 150
        ))
        
        // ===== ARMOR =====
        
        put("bark_chestplate", Item(
            id = "bark_chestplate",
            name = "Bark Chestplate",
            description = "Sturdy armor made from layered bark chips. Natural protection.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.COMMON,
            value = 50,
            weight = 1200,  // 1.2g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.BODY,
            stats = StatModifier(vitality = 4),  // Sturdy but not heavy enough to reduce agility
            maxDurability = 100
        ))
        
        put("feather_tunic", Item(
            id = "feather_tunic",
            name = "Feather Tunic",
            description = "Light armor woven from soft feathers. Barely felt when worn.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 65,
            weight = 150,  // 0.15g - ultra-light
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.BODY,
            stats = StatModifier(agility = 5, luck = 2),
            maxDurability = 60
        ))
        
        put("beetle_breastplate", Item(
            id = "beetle_breastplate",
            name = "Beetle Breastplate",
            description = "Heavy armor crafted from beetle shell. Nearly impenetrable.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 200,
            weight = 1500,  // 1.5g - heavy armor
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.BODY,
            stats = StatModifier(vitality = 10),  // Heavy defense, no agility bonus
            maxDurability = 180
        ))
        
        put("silk_robe", Item(
            id = "silk_robe",
            name = "Silk Robe",
            description = "Flowing robes woven from spider silk. Perfect for spellcasters.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 160,
            weight = 80,  // 0.08g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.BODY,
            stats = StatModifier(intelligence = 6, luck = 3),
            maxDurability = 50
        ))
        
        put("mushroom_helm", Item(
            id = "mushroom_helm",
            name = "Mushroom Helm",
            description = "A helmet fashioned from a mushroom cap. Provides shade and protection.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.COMMON,
            value = 45,
            weight = 900,  // 0.9g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.HEAD,
            stats = StatModifier(vitality = 3),
            maxDurability = 70
        ))
        
        put("beetle_helmet", Item(
            id = "beetle_helmet",
            name = "Beetle Helmet",
            description = "A helmet made from beetle shell. Matches the beetle breastplate.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 180,
            weight = 800,  // 0.8g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.HEAD,
            stats = StatModifier(vitality = 7),
            maxDurability = 160,
            setId = "beetle_armor_set"
        ))
        
        put("feather_hood", Item(
            id = "feather_hood",
            name = "Feather Hood",
            description = "A soft hood lined with down feathers. Warm and comfortable.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 55,
            weight = 100,  // 0.1g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.HEAD,
            stats = StatModifier(agility = 3, luck = 2),
            maxDurability = 50
        ))
        
        put("crystal_crown", Item(
            id = "crystal_crown",
            name = "Crystal Crown",
            description = "A crown made from multiple crystal shards. Radiates power.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.EPIC,
            value = 350,
            weight = 1200,  // 1.2g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.HEAD,
            stats = StatModifier(intelligence = 10, luck = 5),
            maxDurability = 100
        ))
        
        put("scale_boots", Item(
            id = "scale_boots",
            name = "Scale Boots",
            description = "Boots covered in overlapping fish scales. Flexible and durable.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 60,
            weight = 250,  // 0.25g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.FEET,
            stats = StatModifier(agility = 4, vitality = 1),
            maxDurability = 80
        ))
        
        put("fur_boots", Item(
            id = "fur_boots",
            name = "Fur Boots",
            description = "Warm boots lined with soft fur. Perfect for cold climates.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.COMMON,
            value = 40,
            weight = 180,  // 0.18g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.FEET,
            stats = StatModifier(vitality = 2, agility = 1),
            maxDurability = 60
        ))
        
        put("grass_sandals", Item(
            id = "grass_sandals",
            name = "Grass Sandals",
            description = "Simple sandals woven from grass blades. Light and breathable.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.COMMON,
            value = 25,
            weight = 60,  // 0.06g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.FEET,
            stats = StatModifier(agility = 2),
            maxDurability = 40
        ))
        
        put("bark_shield", Item(
            id = "bark_shield",
            name = "Bark Shield",
            description = "A defensive shield carved from thick bark. Blocks attacks effectively.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 70,
            weight = 1400,  // 1.4g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(vitality = 5, strength = 1),
            maxDurability = 120
        ))
        
        put("button_shield", Item(
            id = "button_shield",
            name = "Button Shield",
            description = "A human's lost button repurposed as a massive shield. Enormous and shiny.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.EPIC,
            value = 400,
            weight = 6000,  // 6g - very heavy!
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(vitality = 15),  // Maximum defense, no agility
            maxDurability = 300
        ))
        
        put("snail_shell_shield", Item(
            id = "snail_shell_shield",
            name = "Snail Shell Shield",
            description = "A spiral shield made from a snail shell. Natural curves deflect blows.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 150,
            weight = 2200,  // 2.2g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(vitality = 8, luck = 2),
            maxDurability = 140
        ))
        
        put("mirror_shield", Item(
            id = "mirror_shield",
            name = "Mirror Shield",
            description = "A shield made from reflective mirror. Dazzles enemies with reflected light.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 180,
            weight = 800,  // 0.8g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(vitality = 6, intelligence = 4, luck = 3),
            maxDurability = 60  // Glass is fragile
        ))
        
        put("wing_cape", Item(
            id = "wing_cape",
            name = "Wing Cape",
            description = "A cape made from dragonfly wings. Shimmers with iridescent light.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.EPIC,
            value = 450,
            weight = 80,  // 0.08g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.BODY,
            stats = StatModifier(agility = 10, luck = 5),
            maxDurability = 50  // Fragile but powerful
        ))
        
        put("moss_cape", Item(
            id = "moss_cape",
            name = "Moss Cape",
            description = "A living cape of moss. Blends perfectly with forest environments.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 70,
            weight = 400,  // 0.4g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.BODY,
            stats = StatModifier(agility = 3, vitality = 2),
            maxDurability = 70
        ))
        
        put("beetle_pauldrons", Item(
            id = "beetle_pauldrons",
            name = "Beetle Pauldrons",
            description = "Shoulder guards made from beetle shell segments. Part of the beetle armor set.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 120,
            weight = 600,  // 0.6g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.BODY,
            stats = StatModifier(vitality = 4, strength = 2),
            maxDurability = 140,
            setId = "beetle_armor_set"
        ))
        
        put("feather_pauldrons", Item(
            id = "feather_pauldrons",
            name = "Feather Pauldrons",
            description = "Light shoulder guards adorned with feathers. Barely restricts movement.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 50,
            weight = 80,  // 0.08g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.BODY,
            stats = StatModifier(agility = 4, luck = 1),
            maxDurability = 50
        ))
        
        put("silk_gloves", Item(
            id = "silk_gloves",
            name = "Silk Gloves",
            description = "Delicate gloves woven from spider silk. Enhances dexterity.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 55,
            weight = 30,  // 0.03g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(agility = 5, intelligence = 2),
            maxDurability = 40
        ))
        
        // ===== ACCESSORIES =====
        
        put("luck_clover", Item(
            id = "luck_clover",
            name = "Four-Leaf Clover",
            description = "A rare four-leaf clover preserved in amber. Brings extraordinary luck.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.EPIC,
            value = 500,
            weight = 80,  // 0.08g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(luck = 10),
            maxDurability = 999  // Nearly indestructible
        ))
        
        put("ruby_ring", Item(
            id = "ruby_ring",
            name = "Ruby Ring",
            description = "A ring set with a tiny ruby chip. Radiates power and prestige.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 250,
            weight = 400,  // 0.4g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(strength = 5, vitality = 3),
            maxDurability = 200
        ))
        
        put("sapphire_ring", Item(
            id = "sapphire_ring",
            name = "Sapphire Ring",
            description = "A ring with embedded sapphire. Enhances magical abilities.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 260,
            weight = 420,  // 0.42g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(intelligence = 6, luck = 2),
            maxDurability = 200
        ))
        
        put("emerald_amulet", Item(
            id = "emerald_amulet",
            name = "Emerald Amulet",
            description = "An amulet containing an emerald chip. Grants vitality and life force.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 270,
            weight = 350,  // 0.35g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(vitality = 6, intelligence = 3),
            maxDurability = 200
        ))
        
        put("moonstone_pendant", Item(
            id = "moonstone_pendant",
            name = "Moonstone Pendant",
            description = "A pendant with glowing moonstone. Holds the power of the moon.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.EPIC,
            value = 400,
            weight = 450,  // 0.45g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(intelligence = 8, luck = 6),
            maxDurability = 250
        ))
        
        put("phoenix_brooch", Item(
            id = "phoenix_brooch",
            name = "Phoenix Brooch",
            description = "A brooch adorned with a phoenix feather. Grants resistance to fire.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.EPIC,
            value = 450,
            weight = 100,  // 0.1g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(vitality = 8, intelligence = 5),
            maxDurability = 300
        ))
        
        put("dragon_scale_belt", Item(
            id = "dragon_scale_belt",
            name = "Dragon Scale Belt",
            description = "A belt made from interlocking lizard scales. Symbol of strength.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 220,
            weight = 1100,  // 1.1g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(strength = 6, vitality = 4),
            maxDurability = 180
        ))
        
        put("speed_anklet", Item(
            id = "speed_anklet",
            name = "Speed Anklet",
            description = "An anklet woven from grasshopper legs. Grants incredible speed.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 200,
            weight = 120,  // 0.12g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(agility = 8, luck = 2),
            maxDurability = 100
        ))
        
        put("wisdom_monocle", Item(
            id = "wisdom_monocle",
            name = "Wisdom Monocle",
            description = "A tiny lens from a broken telescope. Enhances perception and knowledge.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.RARE,
            value = 180,
            weight = 300,  // 0.3g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(intelligence = 7, luck = 3),
            maxDurability = 60  // Glass is fragile
        ))
        
        put("iron_band", Item(
            id = "iron_band",
            name = "Iron Band",
            description = "A ring forged from iron filings. Simple but sturdy.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 90,
            weight = 400,  // 0.4g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(strength = 4, vitality = 3),
            maxDurability = 150
        ))
        
        put("copper_circlet", Item(
            id = "copper_circlet",
            name = "Copper Circlet",
            description = "A headband made from copper wire. Conducts magical energy.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 100,
            weight = 280,  // 0.28g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(intelligence = 5, luck = 2),
            maxDurability = 120
        ))
        
        put("bone_necklace", Item(
            id = "bone_necklace",
            name = "Bone Necklace",
            description = "A necklace strung with small bones. Intimidating and powerful.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 80,
            weight = 350,  // 0.35g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(strength = 4, vitality = 2),
            maxDurability = 100
        ))
        
        put("shell_bracelet", Item(
            id = "shell_bracelet",
            name = "Shell Bracelet",
            description = "A bracelet made from tiny shell fragments. Each piece unique.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.COMMON,
            value = 50,
            weight = 200,  // 0.2g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(vitality = 2, luck = 2),
            maxDurability = 80
        ))
        
        put("petal_corsage", Item(
            id = "petal_corsage",
            name = "Petal Corsage",
            description = "A decorative flower corsage. Beautiful and fragrant.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.UNCOMMON,
            value = 70,
            weight = 150,  // 0.15g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(luck = 4, agility = 2),
            maxDurability = 40  // Flowers wilt
        ))
        
        put("star_charm", Item(
            id = "star_charm",
            name = "Star Charm",
            description = "A charm made from crystallized star dust. Glimmers with cosmic power.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.EPIC,
            value = 550,
            weight = 60,  // 0.06g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(intelligence = 10, luck = 8),
            maxDurability = 999
        ))
        
        put("void_amulet", Item(
            id = "void_amulet",
            name = "Void Amulet",
            description = "An amulet containing void essence. Absorbs light and energy.",
            type = ItemType.EQUIPMENT,
            rarity = ItemRarity.EPIC,
            value = 600,
            weight = 200,  // 0.2g
            stackable = false,
            maxStack = 1,
            equipmentSlot = EquipmentSlot.ACCESSORY,
            stats = StatModifier(intelligence = 12),  // Pure magic focus, no vitality bonus
            maxDurability = 500
        ))
        
        // ===== CONSUMABLES (Food & Potions) =====
        
        put("sunflower_seed", Item(
            id = "sunflower_seed",
            name = "Sunflower Seed",
            description = "A plump sunflower seed. Delicious and nutritious! Restores 10 stamina.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.COMMON,
            value = 5,
            weight = 10,  // 0.01g - realistic seed weight
            stackable = true,
            maxStack = 99,
            usable = true,
            consumable = true
        ))
        
        put("millet_grain", Item(
            id = "millet_grain",
            name = "Millet Grain",
            description = "A tiny grain of millet. A quail's favorite snack! Restores 5 stamina.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.COMMON,
            value = 3,
            weight = 5,  // 0.005g
            stackable = true,
            maxStack = 99,
            usable = true,
            consumable = true
        ))
        
        put("dewdrop", Item(
            id = "dewdrop",
            name = "Dewdrop",
            description = "A crystal-clear drop of morning dew. Refreshing! Restores 20 stamina.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.UNCOMMON,
            value = 15,
            weight = 50,  // 0.05g - water droplet
            stackable = true,
            maxStack = 20,  // Limited stack - fragile
            usable = true,
            consumable = true
        ))
        
        put("berry", Item(
            id = "berry",
            name = "Wild Berry",
            description = "A small wild berry. Sweet and tangy. Restores 15 stamina.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.COMMON,
            value = 8,
            weight = 300,  // 0.3g - realistic small berry
            stackable = true,
            maxStack = 50,
            usable = true,
            consumable = true
        ))
        
        put("honey_drop", Item(
            id = "honey_drop",
            name = "Honey Drop",
            description = "A globule of pure honey. Incredibly sweet and energizing. Restores 30 stamina.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.UNCOMMON,
            value = 25,
            weight = 150,  // 0.15g
            stackable = true,
            maxStack = 30,
            usable = true,
            consumable = true
        ))
        
        put("nectar_sip", Item(
            id = "nectar_sip",
            name = "Nectar Sip",
            description = "Flower nectar in liquid form. Refreshing and magical. Restores 40 stamina + 10 HP.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.RARE,
            value = 50,
            weight = 80,  // 0.08g
            stackable = true,
            maxStack = 20,
            usable = true,
            consumable = true
        ))
        
        put("mushroom_slice", Item(
            id = "mushroom_slice",
            name = "Mushroom Slice",
            description = "A slice of edible mushroom. Earthy flavor. Restores 20 stamina.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.COMMON,
            value = 12,
            weight = 400,  // 0.4g
            stackable = true,
            maxStack = 50,
            usable = true,
            consumable = true
        ))
        
        put("acorn_meat", Item(
            id = "acorn_meat",
            name = "Acorn Meat",
            description = "The nutritious interior of an acorn. Rich and filling. Restores 25 stamina.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.COMMON,
            value = 15,
            weight = 800,  // 0.8g
            stackable = true,
            maxStack = 40,
            usable = true,
            consumable = true
        ))
        
        put("pine_nut", Item(
            id = "pine_nut",
            name = "Pine Nut",
            description = "A small pine nut. Crunchy and nutritious. Restores 18 stamina.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.COMMON,
            value = 10,
            weight = 200,  // 0.2g
            stackable = true,
            maxStack = 99,
            usable = true,
            consumable = true
        ))
        
        put("clover_snack", Item(
            id = "clover_snack",
            name = "Clover Snack",
            description = "Fresh clover leaves. Crisp and green. Restores 12 stamina.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.COMMON,
            value = 6,
            weight = 100,  // 0.1g
            stackable = true,
            maxStack = 99,
            usable = true,
            consumable = true
        ))
        
        put("bread_crumb", Item(
            id = "bread_crumb",
            name = "Bread Crumb",
            description = "A crumb from human bread. A feast for a quail! Restores 35 stamina.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.UNCOMMON,
            value = 20,
            weight = 600,  // 0.6g
            stackable = true,
            maxStack = 50,
            usable = true,
            consumable = true
        ))
        
        put("cheese_morsel", Item(
            id = "cheese_morsel",
            name = "Cheese Morsel",
            description = "A tiny piece of cheese. Rich and creamy. Restores 30 stamina + 5 HP.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.UNCOMMON,
            value = 28,
            weight = 500,  // 0.5g
            stackable = true,
            maxStack = 40,
            usable = true,
            consumable = true
        ))
        
        put("health_potion_minor", Item(
            id = "health_potion_minor",
            name = "Minor Health Potion",
            description = "A dewdrop infused with healing herbs. Restores 25 HP.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.UNCOMMON,
            value = 30,
            weight = 60,  // 0.06g
            stackable = true,
            maxStack = 20,
            usable = true,
            consumable = true
        ))
        
        put("health_potion", Item(
            id = "health_potion",
            name = "Health Potion",
            description = "A potent healing elixir made from rare flowers. Restores 50 HP.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.RARE,
            value = 75,
            weight = 80,  // 0.08g
            stackable = true,
            maxStack = 20,
            usable = true,
            consumable = true
        ))
        
        put("health_potion_major", Item(
            id = "health_potion_major",
            name = "Major Health Potion",
            description = "An incredibly powerful healing draught. Restores 100 HP.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.EPIC,
            value = 150,
            weight = 100,  // 0.1g
            stackable = true,
            maxStack = 10,
            usable = true,
            consumable = true
        ))
        
        put("stamina_potion_minor", Item(
            id = "stamina_potion_minor",
            name = "Minor Stamina Potion",
            description = "A refreshing tonic. Restores 50 stamina.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.UNCOMMON,
            value = 25,
            weight = 70,  // 0.07g
            stackable = true,
            maxStack = 20,
            usable = true,
            consumable = true
        ))
        
        put("stamina_potion", Item(
            id = "stamina_potion",
            name = "Stamina Potion",
            description = "A potent energy elixir. Restores 100 stamina.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.RARE,
            value = 60,
            weight = 90,  // 0.09g
            stackable = true,
            maxStack = 20,
            usable = true,
            consumable = true
        ))
        
        put("strength_potion", Item(
            id = "strength_potion",
            name = "Strength Potion",
            description = "A red liquid that bulges your muscles. +5 Strength for 10 minutes.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.RARE,
            value = 80,
            weight = 75,  // 0.075g
            stackable = true,
            maxStack = 10,
            usable = true,
            consumable = true
        ))
        
        put("agility_potion", Item(
            id = "agility_potion",
            name = "Agility Potion",
            description = "A swift liquid that quickens your reflexes. +5 Agility for 10 minutes.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.RARE,
            value = 80,
            weight = 70,  // 0.07g
            stackable = true,
            maxStack = 10,
            usable = true,
            consumable = true
        ))
        
        put("intelligence_potion", Item(
            id = "intelligence_potion",
            name = "Intelligence Potion",
            description = "A glowing blue elixir that sharpens the mind. +5 Intelligence for 10 minutes.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.RARE,
            value = 80,
            weight = 65,  // 0.065g
            stackable = true,
            maxStack = 10,
            usable = true,
            consumable = true
        ))
        
        put("luck_potion", Item(
            id = "luck_potion",
            name = "Luck Potion",
            description = "A shimmering potion that bends probability. +5 Luck for 10 minutes.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.RARE,
            value = 90,
            weight = 60,  // 0.06g
            stackable = true,
            maxStack = 10,
            usable = true,
            consumable = true
        ))
        
        put("antidote", Item(
            id = "antidote",
            name = "Antidote",
            description = "Cures poison and venom. Essential for surviving dangerous encounters.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.UNCOMMON,
            value = 45,
            weight = 50,  // 0.05g
            stackable = true,
            maxStack = 20,
            usable = true,
            consumable = true
        ))
        
        put("fire_resistance_potion", Item(
            id = "fire_resistance_potion",
            name = "Fire Resistance Potion",
            description = "Grants immunity to fire damage for 5 minutes.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.RARE,
            value = 100,
            weight = 80,  // 0.08g
            stackable = true,
            maxStack = 10,
            usable = true,
            consumable = true
        ))
        
        put("invisibility_potion", Item(
            id = "invisibility_potion",
            name = "Invisibility Potion",
            description = "Renders you invisible for 30 seconds. Perfect for stealth.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.EPIC,
            value = 200,
            weight = 70,  // 0.07g
            stackable = true,
            maxStack = 5,
            usable = true,
            consumable = true
        ))
        
        put("speed_potion", Item(
            id = "speed_potion",
            name = "Speed Potion",
            description = "Doubles movement speed for 1 minute. Run like the wind!",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.RARE,
            value = 85,
            weight = 75,  // 0.075g
            stackable = true,
            maxStack = 10,
            usable = true,
            consumable = true
        ))
        
        put("regeneration_potion", Item(
            id = "regeneration_potion",
            name = "Regeneration Potion",
            description = "Gradually restores 50 HP over 1 minute.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.RARE,
            value = 95,
            weight = 85,  // 0.085g
            stackable = true,
            maxStack = 10,
            usable = true,
            consumable = true
        ))
        
        put("bomb_pebble", Item(
            id = "bomb_pebble",
            name = "Bomb Pebble",
            description = "A pebble coated in explosive powder. Deals 30 damage in small area.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.UNCOMMON,
            value = 40,
            weight = 2200,  // 2.2g - heavy
            stackable = true,
            maxStack = 10,
            usable = true,
            consumable = true
        ))
        
        put("smoke_bomb", Item(
            id = "smoke_bomb",
            name = "Smoke Bomb",
            description = "Creates a cloud of smoke for quick escapes. No damage.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.UNCOMMON,
            value = 35,
            weight = 300,  // 0.3g
            stackable = true,
            maxStack = 15,
            usable = true,
            consumable = true
        ))
        
        put("flash_powder", Item(
            id = "flash_powder",
            name = "Flash Powder",
            description = "Blinds enemies with brilliant light. Stuns for 2 turns.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.RARE,
            value = 70,
            weight = 150,  // 0.15g
            stackable = true,
            maxStack = 10,
            usable = true,
            consumable = true
        ))
        
        put("trap_net", Item(
            id = "trap_net",
            name = "Trap Net",
            description = "A spider silk net. Can immobilize one enemy for 3 turns.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.UNCOMMON,
            value = 50,
            weight = 100,  // 0.1g
            stackable = true,
            maxStack = 10,
            usable = true,
            consumable = true
        ))
        
        put("poison_vial", Item(
            id = "poison_vial",
            name = "Poison Vial",
            description = "Concentrated venom. Apply to weapon or throw. Deals 5 damage/turn for 5 turns.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.RARE,
            value = 90,
            weight = 40,  // 0.04g
            stackable = true,
            maxStack = 10,
            usable = true,
            consumable = true
        ))
        
        put("rope_silk", Item(
            id = "rope_silk",
            name = "Silk Rope",
            description = "Strong spider silk rope. Can be used to climb or tie things.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.COMMON,
            value = 20,
            weight = 80,  // 0.08g
            stackable = true,
            maxStack = 20,
            usable = true,
            consumable = true
        ))
        
        put("torch_stick", Item(
            id = "torch_stick",
            name = "Torch Stick",
            description = "A twig dipped in pine resin. Burns for 10 minutes. Lights dark areas.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.COMMON,
            value = 15,
            weight = 600,  // 0.6g
            stackable = true,
            maxStack = 20,
            usable = true,
            consumable = true
        ))
        
        put("glowstone", Item(
            id = "glowstone",
            name = "Glowstone",
            description = "A pebble that emits soft light. Reusable light source.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.UNCOMMON,
            value = 60,
            weight = 1800,  // 1.8g
            stackable = true,
            maxStack = 5,
            usable = true,
            consumable = false  // Reusable
        ))
        
        put("whetstone", Item(
            id = "whetstone",
            name = "Whetstone",
            description = "A small sharpening stone. Restores 20 durability to weapons.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.COMMON,
            value = 25,
            weight = 1500,  // 1.5g
            stackable = true,
            maxStack = 10,
            usable = true,
            consumable = true
        ))
        
        put("repair_kit", Item(
            id = "repair_kit",
            name = "Repair Kit",
            description = "Tools and materials for field repairs. Restores 30 durability to armor.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.UNCOMMON,
            value = 40,
            weight = 800,  // 0.8g
            stackable = true,
            maxStack = 10,
            usable = true,
            consumable = true
        ))
        
        put("teleport_seed", Item(
            id = "teleport_seed",
            name = "Teleport Seed",
            description = "A magical seed that instantly transports you to Buttonburgh.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.RARE,
            value = 150,
            weight = 20,  // 0.02g
            stackable = true,
            maxStack = 5,
            usable = true,
            consumable = true
        ))
        
        put("revive_nectar", Item(
            id = "revive_nectar",
            name = "Revive Nectar",
            description = "Sacred nectar that can revive a fallen companion with 50% HP.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.EPIC,
            value = 300,
            weight = 100,  // 0.1g
            stackable = true,
            maxStack = 3,
            usable = true,
            consumable = true
        ))
        
        put("experience_berry", Item(
            id = "experience_berry",
            name = "Experience Berry",
            description = "A rare berry infused with knowledge. Grants 100 bonus XP.",
            type = ItemType.CONSUMABLE,
            rarity = ItemRarity.EPIC,
            value = 250,
            weight = 400,  // 0.4g
            stackable = true,
            maxStack = 10,
            usable = true,
            consumable = true
        ))
        
        // ===== QUEST ITEMS =====
        
        put("glowing_pebble", Item(
            id = "glowing_pebble",
            name = "Glowing Pebble",
            description = "An unusual pebble that emits a faint, mysterious glow. Feels warm to the touch.",
            type = ItemType.QUEST,
            rarity = ItemRarity.RARE,
            value = 0,  // Quest items typically have no vendor value
            weight = 2200,  // 2.2g - slightly heavier due to magical properties
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("old_quill_note", Item(
            id = "old_quill_note",
            name = "Old Quill's Note",
            description = "A cryptic note written in elegant script. The handwriting belongs to Old Quill.",
            type = ItemType.QUEST,
            rarity = ItemRarity.UNCOMMON,
            value = 0,
            weight = 20,  // 0.02g - paper scrap
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("buttonburgh_map", Item(
            id = "buttonburgh_map",
            name = "Map to Buttonburgh",
            description = "An aged parchment map showing the route back to Buttonburgh. The ink is faded but readable, with intricate calligraphy marking major landmarks.",
            type = ItemType.QUEST,
            rarity = ItemRarity.UNCOMMON,
            value = 50,
            weight = 150,  // 0.15g - parchment paper
            stackable = false,
            maxStack = 1,
            usable = true,  // Can be used from inventory to open map screen
            consumable = false,  // Map is reusable
            questItem = false  // Can be dropped/sold if player chooses
        ))
        
        put("ancient_key", Item(
            id = "ancient_key",
            name = "Ancient Key",
            description = "A safety pin repurposed as a key. Opens the Ancient Gate in the forest.",
            type = ItemType.QUEST,
            rarity = ItemRarity.RARE,
            value = 0,
            weight = 400,  // 0.4g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("crystal_key", Item(
            id = "crystal_key",
            name = "Crystal Key",
            description = "A key carved from pure crystal. Required to access the Crystal Caverns.",
            type = ItemType.QUEST,
            rarity = ItemRarity.RARE,
            value = 0,
            weight = 600,  // 0.6g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("rusty_key", Item(
            id = "rusty_key",
            name = "Rusty Key",
            description = "An old, corroded key. Opens the abandoned shack in the swamp.",
            type = ItemType.QUEST,
            rarity = ItemRarity.UNCOMMON,
            value = 0,
            weight = 350,  // 0.35g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("golden_key", Item(
            id = "golden_key",
            name = "Golden Key",
            description = "A shimmering key made from a paper clip. Opens the Gilded Treasury.",
            type = ItemType.QUEST,
            rarity = ItemRarity.EPIC,
            value = 0,
            weight = 500,  // 0.5g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("forest_map", Item(
            id = "forest_map",
            name = "Forest Map",
            description = "A detailed map of the Whispering Woods. Reveals hidden paths.",
            type = ItemType.QUEST,
            rarity = ItemRarity.UNCOMMON,
            value = 40,
            weight = 140,  // 0.14g
            stackable = false,
            maxStack = 1,
            usable = true,
            consumable = false,
            questItem = false
        ))
        
        put("swamp_map", Item(
            id = "swamp_map",
            name = "Swamp Map",
            description = "A soggy map of the Murky Swamp. Barely legible but helpful.",
            type = ItemType.QUEST,
            rarity = ItemRarity.UNCOMMON,
            value = 35,
            weight = 160,  // 0.16g - water damaged
            stackable = false,
            maxStack = 1,
            usable = true,
            consumable = false,
            questItem = false
        ))
        
        put("mountain_map", Item(
            id = "mountain_map",
            name = "Mountain Map",
            description = "A map of the Peaked Heights. Shows dangerous cliffs and safe paths.",
            type = ItemType.QUEST,
            rarity = ItemRarity.RARE,
            value = 60,
            weight = 135,  // 0.135g
            stackable = false,
            maxStack = 1,
            usable = true,
            consumable = false,
            questItem = false
        ))
        
        put("desert_map", Item(
            id = "desert_map",
            name = "Desert Map",
            description = "A sun-bleached map of the Scorched Dunes. Marks water sources.",
            type = ItemType.QUEST,
            rarity = ItemRarity.RARE,
            value = 65,
            weight = 130,  // 0.13g
            stackable = false,
            maxStack = 1,
            usable = true,
            consumable = false,
            questItem = false
        ))
        
        put("grumble_note", Item(
            id = "grumble_note",
            name = "Grumble's Special Order",
            description = "A note from Grumble Forgepaw requesting rare crafting materials.",
            type = ItemType.QUEST,
            rarity = ItemRarity.UNCOMMON,
            value = 0,
            weight = 25,  // 0.025g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("mysterious_egg", Item(
            id = "mysterious_egg",
            name = "Mysterious Egg",
            description = "A quail egg with strange markings. Warm to the touch. What will hatch?",
            type = ItemType.QUEST,
            rarity = ItemRarity.EPIC,
            value = 0,
            weight = 8000,  // 8g - standard quail egg weight
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("royal_seal", Item(
            id = "royal_seal",
            name = "Royal Seal",
            description = "An official stamp bearing the mark of the Quail King. Grants passage to restricted areas.",
            type = ItemType.QUEST,
            rarity = ItemRarity.EPIC,
            value = 0,
            weight = 300,  // 0.3g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("wanted_poster", Item(
            id = "wanted_poster",
            name = "Wanted Poster",
            description = "A bounty poster seeking a notorious beetle bandit. Reward: 1000 seeds.",
            type = ItemType.QUEST,
            rarity = ItemRarity.UNCOMMON,
            value = 0,
            weight = 30,  // 0.03g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("love_letter", Item(
            id = "love_letter",
            name = "Love Letter",
            description = "A heartfelt letter written by a lovestruck quail. Deliver to their intended.",
            type = ItemType.QUEST,
            rarity = ItemRarity.COMMON,
            value = 0,
            weight = 20,  // 0.02g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("recipe_scroll", Item(
            id = "recipe_scroll",
            name = "Ancient Recipe Scroll",
            description = "A scroll containing a lost crafting recipe. Grumble would love this.",
            type = ItemType.QUEST,
            rarity = ItemRarity.RARE,
            value = 0,
            weight = 90,  // 0.09g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("compass_piece_north", Item(
            id = "compass_piece_north",
            name = "Compass Piece: North",
            description = "One quarter of a broken compass. Shows the north direction.",
            type = ItemType.QUEST,
            rarity = ItemRarity.RARE,
            value = 0,
            weight = 250,  // 0.25g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("compass_piece_south", Item(
            id = "compass_piece_south",
            name = "Compass Piece: South",
            description = "One quarter of a broken compass. Shows the south direction.",
            type = ItemType.QUEST,
            rarity = ItemRarity.RARE,
            value = 0,
            weight = 250,  // 0.25g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("compass_piece_east", Item(
            id = "compass_piece_east",
            name = "Compass Piece: East",
            description = "One quarter of a broken compass. Shows the east direction.",
            type = ItemType.QUEST,
            rarity = ItemRarity.RARE,
            value = 0,
            weight = 250,  // 0.25g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("compass_piece_west", Item(
            id = "compass_piece_west",
            name = "Compass Piece: West",
            description = "One quarter of a broken compass. Shows the west direction.",
            type = ItemType.QUEST,
            rarity = ItemRarity.RARE,
            value = 0,
            weight = 250,  // 0.25g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("secret_password", Item(
            id = "secret_password",
            name = "Secret Password",
            description = "A scrap of paper with a password written on it. Opens the secret society door.",
            type = ItemType.QUEST,
            rarity = ItemRarity.RARE,
            value = 0,
            weight = 15,  // 0.015g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("mushroom_sample", Item(
            id = "mushroom_sample",
            name = "Mushroom Sample",
            description = "A rare mushroom specimen requested by the local mycologist.",
            type = ItemType.QUEST,
            rarity = ItemRarity.UNCOMMON,
            value = 0,
            weight = 400,  // 0.4g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("bandit_mask", Item(
            id = "bandit_mask",
            name = "Bandit Mask",
            description = "A mask worn by the beetle bandits. Proof of their defeat.",
            type = ItemType.QUEST,
            rarity = ItemRarity.UNCOMMON,
            value = 0,
            weight = 150,  // 0.15g
            stackable = true,
            maxStack = 10,
            questItem = true
        ))
        
        put("family_heirloom", Item(
            id = "family_heirloom",
            name = "Family Heirloom",
            description = "A precious heirloom stolen by bandits. Return it to its rightful owner.",
            type = ItemType.QUEST,
            rarity = ItemRarity.RARE,
            value = 0,
            weight = 500,  // 0.5g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("medicine_bundle", Item(
            id = "medicine_bundle",
            name = "Medicine Bundle",
            description = "Healing herbs wrapped in a leaf. Deliver to the sick patient urgently.",
            type = ItemType.QUEST,
            rarity = ItemRarity.COMMON,
            value = 0,
            weight = 300,  // 0.3g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("gem_of_power", Item(
            id = "gem_of_power",
            name = "Gem of Power",
            description = "A legendary gem said to grant immense strength. Glows with inner fire.",
            type = ItemType.QUEST,
            rarity = ItemRarity.EPIC,
            value = 0,
            weight = 800,  // 0.8g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("gnome_fragment_1", Item(
            id = "gnome_fragment_1",
            name = "Gnome Fragment: Head",
            description = "A piece of the shattered Garden Gnome. One of four fragments needed for the final confrontation.",
            type = ItemType.QUEST,
            rarity = ItemRarity.EPIC,
            value = 0,
            weight = 5000,  // 5g - heavy ceramic piece
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("gnome_fragment_2", Item(
            id = "gnome_fragment_2",
            name = "Gnome Fragment: Torso",
            description = "A piece of the shattered Garden Gnome. One of four fragments needed for the final confrontation.",
            type = ItemType.QUEST,
            rarity = ItemRarity.EPIC,
            value = 0,
            weight = 7000,  // 7g - heaviest piece
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("gnome_fragment_3", Item(
            id = "gnome_fragment_3",
            name = "Gnome Fragment: Legs",
            description = "A piece of the shattered Garden Gnome. One of four fragments needed for the final confrontation.",
            type = ItemType.QUEST,
            rarity = ItemRarity.EPIC,
            value = 0,
            weight = 6000,  // 6g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        put("gnome_fragment_4", Item(
            id = "gnome_fragment_4",
            name = "Gnome Fragment: Base",
            description = "A piece of the shattered Garden Gnome. One of four fragments needed for the final confrontation.",
            type = ItemType.QUEST,
            rarity = ItemRarity.EPIC,
            value = 0,
            weight = 4000,  // 4g
            stackable = false,
            maxStack = 1,
            questItem = true
        ))
        
        // ===== SPECIAL ITEMS =====
        
        put("lore_fragment_buttonburgh", Item(
            id = "lore_fragment_buttonburgh",
            name = "Lore Fragment: Buttonburgh",
            description = "An ancient inscription fragment detailing the founding of Buttonburgh.",
            type = ItemType.SPECIAL,
            rarity = ItemRarity.RARE,
            value = 0,  // Priceless to collectors
            weight = 150,  // 0.15g - stone fragment
            stackable = false,
            maxStack = 1
        ))
        
        put("shiny_button", Item(
            id = "shiny_button",
            name = "Shiny Button",
            description = "A human's lost button. Massive and shiny! Could be used as a shield or decoration.",
            type = ItemType.SPECIAL,
            rarity = ItemRarity.EPIC,
            value = 200,
            weight = 5000,  // 5g - very heavy for a quail!
            stackable = false,
            maxStack = 1
        ))
        
        put("lore_fragment_forest", Item(
            id = "lore_fragment_forest",
            name = "Lore Fragment: Whispering Woods",
            description = "An ancient text describing the origin of the Whispering Woods.",
            type = ItemType.SPECIAL,
            rarity = ItemRarity.RARE,
            value = 0,
            weight = 140,  // 0.14g
            stackable = false,
            maxStack = 1
        ))
        
        put("lore_fragment_swamp", Item(
            id = "lore_fragment_swamp",
            name = "Lore Fragment: Murky Swamp",
            description = "A moldy inscription about the dark history of the Murky Swamp.",
            type = ItemType.SPECIAL,
            rarity = ItemRarity.RARE,
            value = 0,
            weight = 160,  // 0.16g
            stackable = false,
            maxStack = 1
        ))
        
        put("lore_fragment_mountain", Item(
            id = "lore_fragment_mountain",
            name = "Lore Fragment: Peaked Heights",
            description = "A stone tablet detailing the legends of the Peaked Heights.",
            type = ItemType.SPECIAL,
            rarity = ItemRarity.RARE,
            value = 0,
            weight = 1200,  // 1.2g - stone is heavy
            stackable = false,
            maxStack = 1
        ))
        
        put("lore_fragment_desert", Item(
            id = "lore_fragment_desert",
            name = "Lore Fragment: Scorched Dunes",
            description = "A sun-bleached scroll describing the ancient desert civilization.",
            type = ItemType.SPECIAL,
            rarity = ItemRarity.RARE,
            value = 0,
            weight = 120,  // 0.12g
            stackable = false,
            maxStack = 1
        ))
        
        put("lore_fragment_gnome", Item(
            id = "lore_fragment_gnome",
            name = "Lore Fragment: The Garden Gnome",
            description = "A chilling account of the Terrifying Titan and its reign of fear.",
            type = ItemType.SPECIAL,
            rarity = ItemRarity.EPIC,
            value = 0,
            weight = 180,  // 0.18g
            stackable = false,
            maxStack = 1
        ))
        
        put("lore_fragment_old_quill", Item(
            id = "lore_fragment_old_quill",
            name = "Lore Fragment: Old Quill",
            description = "Personal notes revealing the true identity and past of Old Quill.",
            type = ItemType.SPECIAL,
            rarity = ItemRarity.EPIC,
            value = 0,
            weight = 100,  // 0.1g
            stackable = false,
            maxStack = 1
        ))
        
        put("lore_fragment_jalmar", Item(
            id = "lore_fragment_jalmar",
            name = "Lore Fragment: The Chosen One",
            description = "An ancient prophecy about a button quail destined to save the realm.",
            type = ItemType.SPECIAL,
            rarity = ItemRarity.EPIC,
            value = 0,
            weight = 90,  // 0.09g
            stackable = false,
            maxStack = 1
        ))
        
        // ===== CURRENCY ITEMS =====
        
        put("seed_pouch_small", Item(
            id = "seed_pouch_small",
            name = "Small Seed Pouch",
            description = "A tiny pouch containing 100 Seeds. The standard currency of the quail kingdom.",
            type = ItemType.CURRENCY,
            rarity = ItemRarity.COMMON,
            value = 100,
            weight = 1000,  // 1g - 100 seeds × 10mg each
            stackable = true,
            maxStack = 10,  // Limited stacks - convert to currency on pickup
            usable = true,
            consumable = true  // Converts to currency when used
        ))
        
        put("glimmer_shard", Item(
            id = "glimmer_shard",
            name = "Glimmer Shard",
            description = "A fragment of crystallized magic. Shimmers with ethereal light. Premium currency.",
            type = ItemType.CURRENCY,
            rarity = ItemRarity.RARE,
            value = 0,  // Not sellable
            weight = 100,  // 0.1g - magical crystal
            stackable = true,
            maxStack = 99,
            usable = true,
            consumable = true  // Converts to premium currency
        ))
    }
    
    /**
     * Retrieves an item by its unique ID.
     * Returns null if the item doesn't exist.
     */
    fun getItem(id: String): Item? = items[id]
    
    /**
     * Returns all items in the catalog.
     */
    fun getAllItems(): List<Item> = items.values.toList()
    
    /**
     * Returns all items of a specific type.
     */
    fun getItemsByType(type: ItemType): List<Item> = 
        items.values.filter { it.type == type }
    
    /**
     * Returns all items of a specific rarity.
     */
    fun getItemsByRarity(rarity: ItemRarity): List<Item> = 
        items.values.filter { it.rarity == rarity }
    
    /**
     * Returns all stackable items.
     */
    fun getStackableItems(): List<Item> = 
        items.values.filter { it.stackable }
    
    /**
     * Returns all equipment items.
     */
    fun getEquipment(): List<Item> = getItemsByType(ItemType.EQUIPMENT)
    
    /**
     * Returns all consumable items.
     */
    fun getConsumables(): List<Item> = getItemsByType(ItemType.CONSUMABLE)
    
    /**
     * Returns all crafting materials.
     */
    fun getMaterials(): List<Item> = getItemsByType(ItemType.MATERIAL)
    
    /**
     * Validates that the catalog is properly configured.
     * Throws IllegalStateException if any issues are found.
     */
    fun validate() {
        val duplicateIds = items.keys.groupingBy { it }.eachCount().filter { it.value > 1 }
        require(duplicateIds.isEmpty()) { "Duplicate item IDs found: ${duplicateIds.keys}" }
        
        items.values.forEach { item ->
            require(item.id in items) { "Item ${item.id} references itself but isn't in catalog" }
        }
    }
}
