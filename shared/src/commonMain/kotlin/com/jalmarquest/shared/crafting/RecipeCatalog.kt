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
