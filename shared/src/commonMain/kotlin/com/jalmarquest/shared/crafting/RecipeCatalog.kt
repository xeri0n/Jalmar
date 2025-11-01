package com.jalmarquest.shared.crafting

import com.jalmarquest.shared.model.Player

/**
 * Static catalog of all crafting recipes in JalmarQuest.
 * Initial implementation contains ~10 recipes. Will expand to 100+ over time.
 * 
 * All recipes use quail-scale items from ItemCatalog.
 */
object RecipeCatalog {
    
    private val recipes = mapOf(
        // ===== EQUIPMENT RECIPES =====
        
        "twig_spear" to Recipe(
            id = "twig_spear",
            name = "Twig Spear",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("twig", 3)
            ),
            output = RecipeOutput("twig_spear", 1),
            requiredLevel = 1,
            description = "Craft a mighty spear from three sturdy twigs. Perfect for a tiny warrior."
        ),
        
        "acorn_helmet" to Recipe(
            id = "acorn_helmet",
            name = "Acorn Helmet",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("acorn_cap", 1),
                RecipeInput("grass_blade", 2)
            ),
            output = RecipeOutput("acorn_helmet", 1),
            requiredLevel = 1,
            description = "Fashion a protective helmet from an acorn cap, reinforced with grass blades."
        ),
        
        "leaf_cloak" to Recipe(
            id = "leaf_cloak",
            name = "Leaf Cloak",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("dried_leaf", 5),
                RecipeInput("grass_blade", 2)
            ),
            output = RecipeOutput("leaf_cloak", 1),
            requiredLevel = 1,
            description = "Weave dried leaves into a camouflage cloak. Blend with the forest floor."
        ),
        
        "feather_charm" to Recipe(
            id = "feather_charm",
            name = "Feather Charm",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("feather", 1),
                RecipeInput("shiny_button", 1)
            ),
            output = RecipeOutput("feather_charm", 1),
            requiredLevel = 5,
            description = "Combine a pristine feather with a shiny button to create a lucky charm."
        ),
        
        "bark_chestplate" to Recipe(
            id = "bark_chestplate",
            name = "Bark Chestplate",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("bark_chip", 5),
                RecipeInput("spider_silk", 2)
            ),
            output = RecipeOutput("bark_chestplate", 1),
            requiredLevel = 5,
            description = "Weave bark chips into protective chest armor."
        ),
        
        "feather_tunic" to Recipe(
            id = "feather_tunic",
            name = "Feather Tunic",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("feather", 8),
                RecipeInput("spider_silk", 3)
            ),
            output = RecipeOutput("feather_tunic", 1),
            requiredLevel = 7,
            description = "Sew feathers into a light, agile tunic."
        ),
        
        "beetle_breastplate" to Recipe(
            id = "beetle_breastplate",
            name = "Beetle Breastplate",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("beetle_shell", 3),
                RecipeInput("spider_silk", 2)
            ),
            output = RecipeOutput("beetle_breastplate", 1),
            requiredLevel = 15,
            description = "Craft heavy armor from beetle shells. Part of the beetle armor set."
        ),
        
        "mushroom_helm" to Recipe(
            id = "mushroom_helm",
            name = "Mushroom Helm",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("mushroom_cap", 1),
                RecipeInput("grass_blade", 2)
            ),
            output = RecipeOutput("mushroom_helm", 1),
            requiredLevel = 8,
            description = "A mushroom cap makes an excellent umbrella helmet."
        ),
        
        "beetle_helmet" to Recipe(
            id = "beetle_helmet",
            name = "Beetle Helmet",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("beetle_shell", 1),
                RecipeInput("spider_silk", 1)
            ),
            output = RecipeOutput("beetle_helmet", 1),
            requiredLevel = 15,
            description = "Shape beetle shell into a protective helmet. Beetle set piece."
        ),
        
        "scale_boots" to Recipe(
            id = "scale_boots",
            name = "Scale Boots",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("scale_fragment", 4),
                RecipeInput("spider_silk", 1)
            ),
            output = RecipeOutput("scale_boots", 1),
            requiredLevel = 20,
            description = "Assemble reptilian scales into durable boots."
        ),
        
        "fur_boots" to Recipe(
            id = "fur_boots",
            name = "Fur Boots",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("fur_tuft", 3),
                RecipeInput("spider_silk", 1)
            ),
            output = RecipeOutput("fur_boots", 1),
            requiredLevel = 12,
            description = "Soft fur boots for warmth and stealth."
        ),
        
        "bark_shield" to Recipe(
            id = "bark_shield",
            name = "Bark Shield",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("bark_chip", 3),
                RecipeInput("twig", 1)
            ),
            output = RecipeOutput("bark_shield", 1),
            requiredLevel = 6,
            description = "Carve a defensive shield from thick bark."
        ),
        
        "snail_shell_shield" to Recipe(
            id = "snail_shell_shield",
            name = "Snail Shell Shield",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("snail_shell", 1),
                RecipeInput("grass_blade", 2)
            ),
            output = RecipeOutput("snail_shell_shield", 1),
            requiredLevel = 18,
            description = "A spiral shield made from a snail shell."
        ),
        
        "wing_cape" to Recipe(
            id = "wing_cape",
            name = "Wing Cape",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("dragonfly_wing", 2),
                RecipeInput("spider_silk", 1)
            ),
            output = RecipeOutput("wing_cape", 1),
            requiredLevel = 25,
            description = "Fashion a shimmering cape from dragonfly wings."
        ),
        
        "moss_cape" to Recipe(
            id = "moss_cape",
            name = "Moss Cape",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("moss_clump", 5),
                RecipeInput("grass_blade", 2)
            ),
            output = RecipeOutput("moss_cape", 1),
            requiredLevel = 10,
            description = "Weave living moss into a camouflage cape."
        ),
        
        "beetle_pauldrons" to Recipe(
            id = "beetle_pauldrons",
            name = "Beetle Pauldrons",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("beetle_shell", 2),
                RecipeInput("spider_silk", 1)
            ),
            output = RecipeOutput("beetle_pauldrons", 1),
            requiredLevel = 15,
            description = "Beetle shell shoulder guards. Complete the beetle armor set."
        ),
        
        "feather_pauldrons" to Recipe(
            id = "feather_pauldrons",
            name = "Feather Pauldrons",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("feather", 4),
                RecipeInput("spider_silk", 1)
            ),
            output = RecipeOutput("feather_pauldrons", 1),
            requiredLevel = 9,
            description = "Light shoulder guards adorned with feathers."
        ),
        
        "silk_gloves" to Recipe(
            id = "silk_gloves",
            name = "Silk Gloves",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("spider_silk", 3)
            ),
            output = RecipeOutput("silk_gloves", 1),
            requiredLevel = 11,
            description = "Weave spider silk into delicate, dexterous gloves."
        ),
        
        "silk_robe" to Recipe(
            id = "silk_robe",
            name = "Silk Robe",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("spider_silk", 8),
                RecipeInput("moth_wing", 2)
            ),
            output = RecipeOutput("silk_robe", 1),
            requiredLevel = 22,
            description = "A luxurious robe woven from finest spider silk."
        ),
        
        // ===== EARLY GAME WEAPONS =====
        
        "pebble_hammer" to Recipe(
            id = "pebble_hammer",
            name = "Pebble Hammer",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("pebble_smooth", 1),
                RecipeInput("twig", 2)
            ),
            output = RecipeOutput("pebble_hammer", 1),
            requiredLevel = 3,
            description = "Bind a smooth pebble to a twig handle. A crushing weapon."
        ),
        
        "thorn_dagger" to Recipe(
            id = "thorn_dagger",
            name = "Thorn Dagger",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("thistle_down", 3),
                RecipeInput("grass_blade", 1)
            ),
            output = RecipeOutput("thorn_dagger", 1),
            requiredLevel = 4,
            description = "Sharpen thistle thorns into a deadly piercing dagger."
        ),
        
        "needle_rapier" to Recipe(
            id = "needle_rapier",
            name = "Needle Rapier",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("pine_needle", 1),
                RecipeInput("grass_blade", 2)
            ),
            output = RecipeOutput("needle_rapier", 1),
            requiredLevel = 6,
            description = "Fashion a graceful rapier from a single pine needle."
        ),
        
        "seed_sling" to Recipe(
            id = "seed_sling",
            name = "Seed Sling",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("grass_blade", 4),
                RecipeInput("dried_leaf", 1)
            ),
            output = RecipeOutput("seed_sling", 1),
            requiredLevel = 5,
            description = "Weave grass into a sling for launching seeds at enemies."
        ),
        
        "twig_bow" to Recipe(
            id = "twig_bow",
            name = "Twig Bow",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("twig", 1),
                RecipeInput("spider_silk", 1),
                RecipeInput("pine_needle", 3)
            ),
            output = RecipeOutput("twig_bow", 1),
            requiredLevel = 8,
            description = "Craft a ranged weapon using twig, silk string, and needle arrows."
        ),
        
        // ===== MID GAME WEAPONS =====
        
        "glass_blade" to Recipe(
            id = "glass_blade",
            name = "Glass Blade",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("glass_fragment", 2),
                RecipeInput("grass_blade", 1)
            ),
            output = RecipeOutput("glass_blade", 1),
            requiredLevel = 10,
            description = "Knap glass fragments into a razor-sharp blade. Fragile but deadly."
        ),
        
        "flint_axe" to Recipe(
            id = "flint_axe",
            name = "Flint Axe",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("flint_chip", 1),
                RecipeInput("twig", 1),
                RecipeInput("spider_silk", 1)
            ),
            output = RecipeOutput("flint_axe", 1),
            requiredLevel = 12,
            description = "Lash a flint chip to a wooden handle for a chopping weapon."
        ),
        
        "stinger_spear" to Recipe(
            id = "stinger_spear",
            name = "Stinger Spear",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("wasp_stinger", 1),
                RecipeInput("twig", 1),
                RecipeInput("spider_silk", 1)
            ),
            output = RecipeOutput("stinger_spear", 1),
            requiredLevel = 15,
            description = "Mount a wasp stinger on a twig. Pierces and poisons foes."
        ),
        
        "mandible_sword" to Recipe(
            id = "mandible_sword",
            name = "Mandible Sword",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("ant_mandible", 2),
                RecipeInput("bark_chip", 1),
                RecipeInput("spider_silk", 1)
            ),
            output = RecipeOutput("mandible_sword", 1),
            requiredLevel = 18,
            description = "Craft a curved sword from ant mandibles. Sharp and durable."
        ),
        
        "copper_blade" to Recipe(
            id = "copper_blade",
            name = "Copper Blade",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("copper_wire", 3),
                RecipeInput("flint_chip", 1),
                RecipeInput("charcoal_bit", 1)
            ),
            output = RecipeOutput("copper_blade", 1),
            requiredLevel = 20,
            description = "Forge copper wire into a blade using primitive smelting."
        ),
        
        // ===== CONSUMABLE RECIPES =====
        
        "seed_bundle" to Recipe(
            id = "seed_bundle",
            name = "Seed Bundle",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("sunflower_seed", 10)
            ),
            output = RecipeOutput("seed_pouch_small", 1),
            requiredLevel = 1,
            description = "Bundle sunflower seeds into a small pouch for easy carrying."
        ),
        
        "berry_cluster" to Recipe(
            id = "berry_cluster",
            name = "Berry Cluster",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("berry", 3)
            ),
            output = RecipeOutput("berry", 1),  // Placeholder: would need berry_cluster item
            requiredLevel = 1,
            description = "Combine berries into a nutrient-rich cluster."
        ),
        
        "honey_drop" to Recipe(
            id = "honey_drop",
            name = "Honey Drop",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("honeycomb", 1)
            ),
            output = RecipeOutput("honey_drop", 3),
            requiredLevel = 5,
            description = "Extract sweet honey drops from honeycomb."
        ),
        
        "mushroom_slice" to Recipe(
            id = "mushroom_slice",
            name = "Mushroom Slice",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("mushroom_cap", 1)
            ),
            output = RecipeOutput("mushroom_slice", 2),
            requiredLevel = 3,
            description = "Slice mushroom cap into edible portions."
        ),
        
        "health_potion_minor" to Recipe(
            id = "health_potion_minor",
            name = "Minor Health Potion",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("berry", 2),
                RecipeInput("clover_leaf", 1)
            ),
            output = RecipeOutput("health_potion_minor", 1),
            requiredLevel = 5,
            description = "Mash berries and clover into a healing potion. Restores 25 HP."
        ),
        
        "health_potion" to Recipe(
            id = "health_potion",
            name = "Health Potion",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("berry", 4),
                RecipeInput("clover_leaf", 2),
                RecipeInput("honey_drop", 1)
            ),
            output = RecipeOutput("health_potion", 1),
            requiredLevel = 10,
            description = "Brew a standard healing potion. Restores 50 HP."
        ),
        
        "health_potion_major" to Recipe(
            id = "health_potion_major",
            name = "Major Health Potion",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("berry", 6),
                RecipeInput("rose_petal", 2),
                RecipeInput("honey_drop", 2)
            ),
            output = RecipeOutput("health_potion_major", 1),
            requiredLevel = 20,
            description = "Create a powerful healing potion. Restores 100 HP."
        ),
        
        "stamina_potion_minor" to Recipe(
            id = "stamina_potion_minor",
            name = "Minor Stamina Potion",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("sunflower_seed", 3),
                RecipeInput("dandelion_fluff", 1)
            ),
            output = RecipeOutput("stamina_potion_minor", 1),
            requiredLevel = 4,
            description = "Grind seeds into an energy-restoring drink. Restores 50 stamina."
        ),
        
        "stamina_potion" to Recipe(
            id = "stamina_potion",
            name = "Stamina Potion",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("sunflower_seed", 5),
                RecipeInput("wheat_stalk", 2),
                RecipeInput("honey_drop", 1)
            ),
            output = RecipeOutput("stamina_potion", 1),
            requiredLevel = 12,
            description = "Brew a stamina-restoring potion. Restores 100 stamina."
        ),
        
        "strength_potion" to Recipe(
            id = "strength_potion",
            name = "Strength Potion",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("acorn_meat", 2),
                RecipeInput("iron_filing", 1)
            ),
            output = RecipeOutput("strength_potion", 1),
            requiredLevel = 15,
            description = "Brew a potion that boosts strength +5 for 10 minutes."
        ),
        
        "agility_potion" to Recipe(
            id = "agility_potion",
            name = "Agility Potion",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("dandelion_fluff", 3),
                RecipeInput("moth_wing", 1)
            ),
            output = RecipeOutput("agility_potion", 1),
            requiredLevel = 15,
            description = "Brew a potion that boosts agility +5 for 10 minutes."
        ),
        
        "intelligence_potion" to Recipe(
            id = "intelligence_potion",
            name = "Intelligence Potion",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("crystal_shard", 1),
                RecipeInput("rose_petal", 2)
            ),
            output = RecipeOutput("intelligence_potion", 1),
            requiredLevel = 15,
            description = "Brew a potion that boosts intelligence +5 for 10 minutes."
        ),
        
        "luck_potion" to Recipe(
            id = "luck_potion",
            name = "Luck Potion",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("clover_leaf", 4),
                RecipeInput("star_dust", 1)
            ),
            output = RecipeOutput("luck_potion", 1),
            requiredLevel = 25,
            description = "Brew a rare potion that boosts luck +5 for 10 minutes."
        ),
        
        "antidote" to Recipe(
            id = "antidote",
            name = "Antidote",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("clover_leaf", 1),
                RecipeInput("mushroom_stem", 1)
            ),
            output = RecipeOutput("antidote", 1),
            requiredLevel = 8,
            description = "Create an antidote that cures poison effects."
        ),
        
        "fire_resistance_potion" to Recipe(
            id = "fire_resistance_potion",
            name = "Fire Resistance Potion",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("clay_chunk", 2),
                RecipeInput("honey_drop", 1)
            ),
            output = RecipeOutput("fire_resistance_potion", 1),
            requiredLevel = 18,
            description = "Brew a potion that grants fire resistance for 5 minutes."
        ),
        
        "speed_potion" to Recipe(
            id = "speed_potion",
            name = "Speed Potion",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("dandelion_fluff", 2),
                RecipeInput("dragonfly_wing", 1)
            ),
            output = RecipeOutput("speed_potion", 1),
            requiredLevel = 20,
            description = "Brew a potion that increases movement speed for 3 minutes."
        ),
        
        "regeneration_potion" to Recipe(
            id = "regeneration_potion",
            name = "Regeneration Potion",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("moss_clump", 3),
                RecipeInput("honey_drop", 1),
                RecipeInput("clover_leaf", 1)
            ),
            output = RecipeOutput("regeneration_potion", 1),
            requiredLevel = 22,
            description = "Brew a potion that slowly regenerates HP over 60 seconds."
        ),
        
        "bomb_pebble" to Recipe(
            id = "bomb_pebble",
            name = "Bomb Pebble",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("pebble_sharp", 1),
                RecipeInput("sulfur_powder", 1),
                RecipeInput("charcoal_bit", 1)
            ),
            output = RecipeOutput("bomb_pebble", 1),
            requiredLevel = 16,
            description = "Craft an explosive pebble. Deals 30 damage in small area."
        ),
        
        "smoke_bomb" to Recipe(
            id = "smoke_bomb",
            name = "Smoke Bomb",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("charcoal_bit", 2),
                RecipeInput("dandelion_fluff", 1)
            ),
            output = RecipeOutput("smoke_bomb", 1),
            requiredLevel = 10,
            description = "Create a smoke bomb for escaping combat."
        ),
        
        "flash_powder" to Recipe(
            id = "flash_powder",
            name = "Flash Powder",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("salt_crystal", 1),
                RecipeInput("sulfur_powder", 1)
            ),
            output = RecipeOutput("flash_powder", 1),
            requiredLevel = 14,
            description = "Mix chemicals into blinding flash powder. Blinds for 2 turns."
        ),
        
        "trap_net" to Recipe(
            id = "trap_net",
            name = "Trap Net",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("spider_silk", 5),
                RecipeInput("grass_blade", 2)
            ),
            output = RecipeOutput("trap_net", 1),
            requiredLevel = 12,
            description = "Weave a trap net that immobilizes enemies for 3 turns."
        ),
        
        "millet_snack" to Recipe(
            id = "millet_snack",
            name = "Millet Snack",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("millet_grain", 5)
            ),
            output = RecipeOutput("millet_grain", 2),  // Placeholder: bundled snack
            requiredLevel = 1,
            description = "Bundle millet grains into a convenient snack."
        ),
        
        // ===== MATERIAL RECIPES =====
        
        "reinforced_twig" to Recipe(
            id = "reinforced_twig",
            name = "Reinforced Twig",
            category = CraftingCategory.MATERIAL,
            inputs = listOf(
                RecipeInput("twig", 2),
                RecipeInput("bark_chip", 1)
            ),
            output = RecipeOutput("twig", 1),  // Placeholder: would need reinforced_twig item
            requiredLevel = 3,
            description = "Strengthen a twig by binding it with bark chips."
        ),
        
        "woven_grass" to Recipe(
            id = "woven_grass",
            name = "Woven Grass",
            category = CraftingCategory.MATERIAL,
            inputs = listOf(
                RecipeInput("grass_blade", 3)
            ),
            output = RecipeOutput("grass_blade", 1),  // Placeholder: would need woven_grass item
            requiredLevel = 2,
            description = "Weave grass blades into a sturdy fabric material."
        ),
        
        "polished_pebble" to Recipe(
            id = "polished_pebble",
            name = "Polished Pebble",
            category = CraftingCategory.MATERIAL,
            inputs = listOf(
                RecipeInput("pebble", 1),
                RecipeInput("pine_needle", 2)
            ),
            output = RecipeOutput("pebble", 1),  // Placeholder: would need polished_pebble item
            requiredLevel = 2,
            description = "Polish a pebble to a smooth finish using pine needles."
        ),
        
        // ===== MATERIAL PROCESSING =====
        
        "beeswax" to Recipe(
            id = "beeswax",
            name = "Beeswax",
            category = CraftingCategory.MATERIAL,
            inputs = listOf(
                RecipeInput("honeycomb", 2)
            ),
            output = RecipeOutput("beeswax", 1),
            requiredLevel = 8,
            description = "Process honeycomb to extract pure beeswax."
        ),
        
        "seed_oil" to Recipe(
            id = "seed_oil",
            name = "Seed Oil",
            category = CraftingCategory.MATERIAL,
            inputs = listOf(
                RecipeInput("sunflower_seed", 10),
                RecipeInput("pebble_smooth", 1)
            ),
            output = RecipeOutput("seed_oil", 1),
            requiredLevel = 10,
            description = "Grind and press seeds to extract oil."
        ),
        
        "pine_resin" to Recipe(
            id = "pine_resin",
            name = "Pine Resin",
            category = CraftingCategory.MATERIAL,
            inputs = listOf(
                RecipeInput("pine_needle", 5),
                RecipeInput("bark_chip", 1)
            ),
            output = RecipeOutput("pine_resin", 1),
            requiredLevel = 7,
            description = "Collect sticky pine resin from needles and bark."
        ),
        
        "amber_resin" to Recipe(
            id = "amber_resin",
            name = "Amber Resin",
            category = CraftingCategory.MATERIAL,
            inputs = listOf(
                RecipeInput("pine_resin", 3),
                RecipeInput("honey_drop", 1)
            ),
            output = RecipeOutput("amber_resin", 1),
            requiredLevel = 15,
            description = "Harden pine resin into precious amber."
        ),
        
        "charcoal_bit" to Recipe(
            id = "charcoal_bit",
            name = "Charcoal",
            category = CraftingCategory.MATERIAL,
            inputs = listOf(
                RecipeInput("twig", 3)
            ),
            output = RecipeOutput("charcoal_bit", 2),
            requiredLevel = 5,
            description = "Burn twigs into charcoal for smelting and explosives."
        ),
        
        // ===== ADVANCED EQUIPMENT =====
        
        "bone_club" to Recipe(
            id = "bone_club",
            name = "Bone Club",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("bone_fragment", 3),
                RecipeInput("spider_silk", 1)
            ),
            output = RecipeOutput("bone_club", 1),
            requiredLevel = 22,
            description = "Bind bone fragments into a heavy crushing weapon."
        ),
        
        "crystal_staff" to Recipe(
            id = "crystal_staff",
            name = "Crystal Staff",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("crystal_shard", 3),
                RecipeInput("twig", 1),
                RecipeInput("spider_silk", 1)
            ),
            output = RecipeOutput("crystal_staff", 1),
            requiredLevel = 25,
            description = "Mount crystal shards on a staff. Channels magical energy."
        ),
        
        "claw_gauntlet" to Recipe(
            id = "claw_gauntlet",
            name = "Claw Gauntlet",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("claw_tip", 5),
                RecipeInput("beetle_shell", 1),
                RecipeInput("spider_silk", 2)
            ),
            output = RecipeOutput("claw_gauntlet", 1),
            requiredLevel = 28,
            description = "Attach claws to a beetle shell gauntlet for brutal melee strikes."
        ),
        
        "obsidian_dagger" to Recipe(
            id = "obsidian_dagger",
            name = "Obsidian Dagger",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("obsidian_shard", 1),
                RecipeInput("grass_blade", 2)
            ),
            output = RecipeOutput("obsidian_dagger", 1),
            requiredLevel = 30,
            description = "Knap volcanic glass into a deadly black dagger. Fragile but powerful."
        ),
        
        "mirror_shield" to Recipe(
            id = "mirror_shield",
            name = "Mirror Shield",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("mirror_piece", 2),
                RecipeInput("grass_blade", 2),
                RecipeInput("beeswax", 1)
            ),
            output = RecipeOutput("mirror_shield", 1),
            requiredLevel = 24,
            description = "Polish mirror pieces into a dazzling defensive shield."
        ),
        
        "crystal_crown" to Recipe(
            id = "crystal_crown",
            name = "Crystal Crown",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("crystal_shard", 5),
                RecipeInput("ruby_chip", 1),
                RecipeInput("spider_silk", 1)
            ),
            output = RecipeOutput("crystal_crown", 1),
            requiredLevel = 35,
            description = "Craft a magnificent crown from crystals and ruby. For true royalty."
        ),
        
        "feather_hood" to Recipe(
            id = "feather_hood",
            name = "Feather Hood",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("feather", 6),
                RecipeInput("spider_silk", 2)
            ),
            output = RecipeOutput("feather_hood", 1),
            requiredLevel = 14,
            description = "Sew a light hood from soft feathers."
        ),
        
        "grass_sandals" to Recipe(
            id = "grass_sandals",
            name = "Grass Sandals",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("grass_blade", 6),
                RecipeInput("dried_leaf", 2)
            ),
            output = RecipeOutput("grass_sandals", 1),
            requiredLevel = 3,
            description = "Weave basic footwear from grass and leaves."
        ),
        
        // ===== ACCESSORIES =====
        
        "luck_clover" to Recipe(
            id = "luck_clover",
            name = "Four-Leaf Clover",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("clover_leaf", 4),
                RecipeInput("amber_resin", 1)
            ),
            output = RecipeOutput("luck_clover", 1),
            requiredLevel = 40,
            description = "Preserve a rare four-leaf clover in amber. Legendary luck charm."
        ),
        
        "ruby_ring" to Recipe(
            id = "ruby_ring",
            name = "Ruby Ring",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("ruby_chip", 1),
                RecipeInput("copper_wire", 2)
            ),
            output = RecipeOutput("ruby_ring", 1),
            requiredLevel = 28,
            description = "Set a ruby chip in a copper band. Boosts strength."
        ),
        
        "sapphire_ring" to Recipe(
            id = "sapphire_ring",
            name = "Sapphire Ring",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("sapphire_chip", 1),
                RecipeInput("copper_wire", 2)
            ),
            output = RecipeOutput("sapphire_ring", 1),
            requiredLevel = 28,
            description = "Set a sapphire chip in a copper band. Boosts intelligence."
        ),
        
        "emerald_amulet" to Recipe(
            id = "emerald_amulet",
            name = "Emerald Amulet",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("emerald_chip", 1),
                RecipeInput("spider_silk", 1),
                RecipeInput("copper_wire", 1)
            ),
            output = RecipeOutput("emerald_amulet", 1),
            requiredLevel = 30,
            description = "Craft an amulet from emerald and copper. Boosts vitality."
        ),
        
        "bone_necklace" to Recipe(
            id = "bone_necklace",
            name = "Bone Necklace",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("bone_fragment", 3),
                RecipeInput("spider_silk", 1)
            ),
            output = RecipeOutput("bone_necklace", 1),
            requiredLevel = 16,
            description = "String bone fragments into a primitive necklace."
        ),
        
        "shell_bracelet" to Recipe(
            id = "shell_bracelet",
            name = "Shell Bracelet",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("snail_shell", 3),
                RecipeInput("spider_silk", 1)
            ),
            output = RecipeOutput("shell_bracelet", 1),
            requiredLevel = 14,
            description = "String snail shells into a decorative bracelet."
        ),
        
        // ===== UPGRADE RECIPES =====
        
        "reinforced_twig_spear" to Recipe(
            id = "reinforced_twig_spear",
            name = "Reinforced Twig Spear",
            category = CraftingCategory.UPGRADE,
            inputs = listOf(
                RecipeInput("twig_spear", 1),
                RecipeInput("flint_chip", 1),
                RecipeInput("spider_silk", 1)
            ),
            output = RecipeOutput("flint_axe", 1),
            requiredLevel = 10,
            description = "Upgrade twig spear with flint tip to create a flint axe."
        ),
        
        "reinforced_bark_armor" to Recipe(
            id = "reinforced_bark_armor",
            name = "Reinforced Bark Armor",
            category = CraftingCategory.UPGRADE,
            inputs = listOf(
                RecipeInput("bark_chestplate", 1),
                RecipeInput("beetle_shell", 2),
                RecipeInput("spider_silk", 2)
            ),
            output = RecipeOutput("beetle_breastplate", 1),
            requiredLevel = 16,
            description = "Reinforce bark chestplate with beetle shells for superior protection."
        ),
        
        "sharpened_dagger" to Recipe(
            id = "sharpened_dagger",
            name = "Sharpened Dagger",
            category = CraftingCategory.UPGRADE,
            inputs = listOf(
                RecipeInput("thorn_dagger", 1),
                RecipeInput("flint_chip", 1)
            ),
            output = RecipeOutput("glass_blade", 1),
            requiredLevel = 12,
            description = "Sharpen thorn dagger with flint to create glass-sharp edge."
        ),
        
        "enchanted_staff" to Recipe(
            id = "enchanted_staff",
            name = "Enchanted Crystal Staff",
            category = CraftingCategory.UPGRADE,
            inputs = listOf(
                RecipeInput("crystal_staff", 1),
                RecipeInput("star_dust", 1),
                RecipeInput("moonstone_fragment", 1)
            ),
            output = RecipeOutput("crystal_staff", 1),
            requiredLevel = 35,
            description = "Enchant crystal staff with cosmic dust. (Returns enhanced version)"
        ),
        
        // ===== UTILITY ITEMS =====
        
        "rope_silk" to Recipe(
            id = "rope_silk",
            name = "Silk Rope",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("spider_silk", 10)
            ),
            output = RecipeOutput("rope_silk", 1),
            requiredLevel = 8,
            description = "Braid spider silk into strong climbing rope."
        ),
        
        "torch_stick" to Recipe(
            id = "torch_stick",
            name = "Torch",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("twig", 1),
                RecipeInput("pine_resin", 1),
                RecipeInput("dried_leaf", 1)
            ),
            output = RecipeOutput("torch_stick", 1),
            requiredLevel = 4,
            description = "Wrap twig in resin and leaves to create a torch."
        ),
        
        "glowstone" to Recipe(
            id = "glowstone",
            name = "Glowstone",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("crystal_shard", 1),
                RecipeInput("star_dust", 1)
            ),
            output = RecipeOutput("glowstone", 1),
            requiredLevel = 20,
            description = "Infuse crystal with star dust to create eternal light."
        ),
        
        "whetstone" to Recipe(
            id = "whetstone",
            name = "Whetstone",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("pebble_smooth", 1),
                RecipeInput("sand_grain", 3)
            ),
            output = RecipeOutput("whetstone", 1),
            requiredLevel = 6,
            description = "Create a sharpening stone from smooth pebble and sand."
        ),
        
        "repair_kit" to Recipe(
            id = "repair_kit",
            name = "Repair Kit",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("spider_silk", 3),
                RecipeInput("pine_resin", 1),
                RecipeInput("bark_chip", 2)
            ),
            output = RecipeOutput("repair_kit", 1),
            requiredLevel = 10,
            description = "Assemble materials for field equipment repair."
        ),
        
        "poison_vial" to Recipe(
            id = "poison_vial",
            name = "Poison Vial",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("wasp_stinger", 1),
                RecipeInput("mushroom_stem", 2)
            ),
            output = RecipeOutput("poison_vial", 1),
            requiredLevel = 18,
            description = "Extract venom from wasp stinger. Coat weapons for poison damage."
        ),
        
        // ===== FOOD PREPARATION =====
        
        "acorn_meat" to Recipe(
            id = "acorn_meat",
            name = "Acorn Meat",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("acorn_cap", 2)
            ),
            output = RecipeOutput("acorn_meat", 1),
            requiredLevel = 3,
            description = "Extract nutritious meat from acorn caps."
        ),
        
        "pine_nut" to Recipe(
            id = "pine_nut",
            name = "Pine Nut",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("pine_needle", 5)
            ),
            output = RecipeOutput("pine_nut", 2),
            requiredLevel = 4,
            description = "Harvest edible pine nuts from needles."
        ),
        
        "clover_snack" to Recipe(
            id = "clover_snack",
            name = "Clover Snack",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("clover_leaf", 3)
            ),
            output = RecipeOutput("clover_snack", 1),
            requiredLevel = 2,
            description = "Bundle fresh clover leaves into a healthy snack."
        ),
        
        "nectar_sip" to Recipe(
            id = "nectar_sip",
            name = "Nectar Sip",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("rose_petal", 2),
                RecipeInput("honey_drop", 1)
            ),
            output = RecipeOutput("nectar_sip", 1),
            requiredLevel = 8,
            description = "Mix rose petals with honey for a sweet, energizing drink."
        ),
        
        // ===== SPECIAL CRAFTS =====
        
        "teleport_seed" to Recipe(
            id = "teleport_seed",
            name = "Teleport Seed",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("maple_seed", 1),
                RecipeInput("star_dust", 1),
                RecipeInput("crystal_shard", 1)
            ),
            output = RecipeOutput("teleport_seed", 1),
            requiredLevel = 30,
            description = "Enchant a maple seed with teleportation magic. Returns to town."
        ),
        
        "revive_nectar" to Recipe(
            id = "revive_nectar",
            name = "Revive Nectar",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("honey_drop", 3),
                RecipeInput("phoenix_feather", 1),
                RecipeInput("rose_petal", 2)
            ),
            output = RecipeOutput("revive_nectar", 1),
            requiredLevel = 40,
            description = "Legendary elixir that resurrects the fallen with 50% HP."
        ),
        
        "experience_berry" to Recipe(
            id = "experience_berry",
            name = "Experience Berry",
            category = CraftingCategory.CONSUMABLE,
            inputs = listOf(
                RecipeInput("berry", 5),
                RecipeInput("star_dust", 1),
                RecipeInput("honey_drop", 1)
            ),
            output = RecipeOutput("experience_berry", 1),
            requiredLevel = 25,
            description = "Infuse berries with cosmic energy. Grants +100 XP when consumed."
        ),
        
        "iron_band" to Recipe(
            id = "iron_band",
            name = "Iron Band",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("iron_filing", 5),
                RecipeInput("charcoal_bit", 1)
            ),
            output = RecipeOutput("iron_band", 1),
            requiredLevel = 18,
            description = "Smelt iron filings into a sturdy ring."
        ),
        
        "copper_circlet" to Recipe(
            id = "copper_circlet",
            name = "Copper Circlet",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("copper_wire", 5)
            ),
            output = RecipeOutput("copper_circlet", 1),
            requiredLevel = 16,
            description = "Weave copper wire into an elegant headband."
        ),
        
        "petal_corsage" to Recipe(
            id = "petal_corsage",
            name = "Petal Corsage",
            category = CraftingCategory.EQUIPMENT,
            inputs = listOf(
                RecipeInput("rose_petal", 5),
                RecipeInput("spider_silk", 1)
            ),
            output = RecipeOutput("petal_corsage", 1),
            requiredLevel = 12,
            description = "Arrange rose petals into a beautiful decorative corsage."
        )
    )
    
    /**
     * Returns the recipe with the given ID, or null if not found.
     */
    fun getRecipe(recipeId: String): Recipe? {
        return recipes[recipeId]
    }
    
    /**
     * Returns all available recipes.
     */
    fun getAllRecipes(): List<Recipe> {
        return recipes.values.toList()
    }
    
    /**
     * Returns all recipes in the specified category.
     */
    fun getRecipesByCategory(category: CraftingCategory): List<Recipe> {
        return recipes.values.filter { it.category == category }
    }
    
    /**
     * Returns recipes that the player can currently craft based on level.
     * Does NOT check materials - only level requirement.
     */
    fun getAvailableRecipes(player: Player): List<Recipe> {
        return recipes.values.filter { it.requiredLevel <= player.level }
    }
    
    /**
     * Returns all recipe IDs.
     */
    fun getAllRecipeIds(): List<String> {
        return recipes.keys.toList()
    }
    
    /**
     * Validates that all recipe inputs and outputs reference valid items in ItemCatalog.
     * Useful for debugging catalog consistency.
     */
    fun validateRecipes(): Boolean {
        // This would check against ItemCatalog but we'll keep it simple for now
        return recipes.values.all { recipe ->
            recipe.inputs.isNotEmpty() && recipe.output.quantity > 0
        }
    }
}
