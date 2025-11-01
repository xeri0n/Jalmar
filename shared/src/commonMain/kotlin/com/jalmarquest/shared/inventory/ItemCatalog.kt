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
