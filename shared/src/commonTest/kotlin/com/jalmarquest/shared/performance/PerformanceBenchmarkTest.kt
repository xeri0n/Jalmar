package com.jalmarquest.shared.performance

import com.jalmarquest.shared.combat.EnemyCatalog
import com.jalmarquest.shared.crafting.RecipeCatalog
import com.jalmarquest.shared.inventory.ItemCatalog
import com.jalmarquest.shared.inventory.ItemType
import com.jalmarquest.shared.npc.NPCCatalog
import com.jalmarquest.shared.quest.QuestCatalog
import com.jalmarquest.shared.skills.SkillCatalog
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.measureTime

/**
 * Performance Benchmark Suite
 * 
 * Tests actual performance with full 512-asset content load:
 * - Items: 215
 * - Recipes: 93
 * - Enemies: 40
 * - Skills: 57
 * - Quests: 55
 * - NPCs: 52
 * 
 * Performance Targets:
 * - Catalog loading: <2s total
 * - Individual catalogs: <500ms each
 * - Memory footprint: <10MB for catalogs
 * - Lookup operations: <100μs per lookup
 * - Filtering operations: <50ms per search
 */
class PerformanceBenchmarkTest {

    @Test
    fun `benchmark catalog loading performance`() {
        println("\n=== CATALOG LOADING BENCHMARK ===")
        
        val itemTime = measureTime {
            ItemCatalog.getAllItems()
        }
        println("ItemCatalog (215 items): ${itemTime.inWholeMilliseconds}ms")
        
        val recipeTime = measureTime {
            RecipeCatalog.getAllRecipes()
        }
        println("RecipeCatalog (93 recipes): ${recipeTime.inWholeMilliseconds}ms")
        
        val enemyTime = measureTime {
            EnemyCatalog.allEnemies
        }
        println("EnemyCatalog (40 enemies): ${enemyTime.inWholeMilliseconds}ms")
        
        val skillTime = measureTime {
            SkillCatalog.allSkills
        }
        println("SkillCatalog (57 skills): ${skillTime.inWholeMilliseconds}ms")
        
        val questTime = measureTime {
            QuestCatalog.allQuests
        }
        println("QuestCatalog (55 quests): ${questTime.inWholeMilliseconds}ms")
        
        val npcTime = measureTime {
            NPCCatalog.allNPCs
        }
        println("NPCCatalog (52 NPCs): ${npcTime.inWholeMilliseconds}ms")
        
        val totalTime = itemTime + recipeTime + enemyTime + skillTime + questTime + npcTime
        println("\nTOTAL CATALOG LOAD TIME: ${totalTime.inWholeMilliseconds}ms")
        println("TARGET: <2000ms (2 seconds)")
        
        // Performance assertion - catalogs should load in under 2 seconds
        assertTrue(
            totalTime.inWholeMilliseconds < 2000,
            "Catalog loading took ${totalTime.inWholeMilliseconds}ms (target: <2000ms)"
        )
        
        // Individual catalog assertions - each should load in <500ms
        assertTrue(itemTime.inWholeMilliseconds < 500, "ItemCatalog took ${itemTime.inWholeMilliseconds}ms")
        assertTrue(recipeTime.inWholeMilliseconds < 500, "RecipeCatalog took ${recipeTime.inWholeMilliseconds}ms")
        assertTrue(enemyTime.inWholeMilliseconds < 500, "EnemyCatalog took ${enemyTime.inWholeMilliseconds}ms")
        assertTrue(skillTime.inWholeMilliseconds < 500, "SkillCatalog took ${skillTime.inWholeMilliseconds}ms")
        assertTrue(questTime.inWholeMilliseconds < 500, "QuestCatalog took ${questTime.inWholeMilliseconds}ms")
        assertTrue(npcTime.inWholeMilliseconds < 500, "NPCCatalog took ${npcTime.inWholeMilliseconds}ms")
    }

    @Test
    fun `benchmark catalog memory footprint`() {
        println("\n=== CATALOG MEMORY BENCHMARK ===")
        
        // Force garbage collection before measuring
        System.gc()
        Thread.sleep(100)
        
        val beforeMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
        
        // Load all catalogs
        val items = ItemCatalog.getAllItems()
        val recipes = RecipeCatalog.getAllRecipes()
        val enemies = EnemyCatalog.allEnemies
        val skills = SkillCatalog.allSkills
        val quests = QuestCatalog.allQuests
        val npcs = NPCCatalog.allNPCs
        
        val afterMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
        val memoryUsedBytes = afterMemory - beforeMemory
        val memoryUsedMB = memoryUsedBytes / (1024.0 * 1024.0)
        
        println("Memory used by catalogs: %.2f MB".format(memoryUsedMB))
        println("Asset counts: ${items.size} items, ${recipes.size} recipes, ${enemies.size} enemies, ${skills.size} skills, ${quests.size} quests, ${npcs.size} NPCs")
        println("TARGET: <10 MB")
        
        // Memory assertion - catalogs should use <10MB
        assertTrue(
            memoryUsedMB < 10.0,
            "Catalog memory usage: %.2f MB (target: <10 MB)".format(memoryUsedMB)
        )
    }

    @Test
    fun `benchmark catalog lookup performance`() {
        println("\n=== CATALOG LOOKUP BENCHMARK ===")
        
        // Test lookup performance for frequently accessed operations
        val lookupIterations = 1000
        
        // ItemCatalog lookup (most frequently accessed)
        val itemLookupTime = measureTime {
            repeat(lookupIterations) {
                ItemCatalog.getItem("twig_spear")
                ItemCatalog.getItem("acorn_helmet")
                ItemCatalog.getItem("seed_ration")
            }
        }
        val avgItemLookup = itemLookupTime.inWholeMicroseconds / (lookupIterations * 3.0)
        println("ItemCatalog average lookup: %.2f μs".format(avgItemLookup))
        
        // QuestCatalog lookup
        val questLookupTime = measureTime {
            repeat(lookupIterations) {
                QuestCatalog.getQuest("tutorial_first_steps")
                QuestCatalog.getQuest("main_the_great_escape")
                QuestCatalog.getQuest("side_gather_seeds")
            }
        }
        val avgQuestLookup = questLookupTime.inWholeMicroseconds / (lookupIterations * 3.0)
        println("QuestCatalog average lookup: %.2f μs".format(avgQuestLookup))
        
        // EnemyCatalog lookup
        val enemyLookupTime = measureTime {
            repeat(lookupIterations) {
                EnemyCatalog.getEnemy("garden_spider")
                EnemyCatalog.getEnemy("feral_cat")
                EnemyCatalog.getEnemy("house_sparrow")
            }
        }
        val avgEnemyLookup = enemyLookupTime.inWholeMicroseconds / (lookupIterations * 3.0)
        println("EnemyCatalog average lookup: %.2f μs".format(avgEnemyLookup))
        
        println("\nTARGET: <100 μs per lookup")
        
        // Performance assertions - lookups should be <100μs
        assertTrue(avgItemLookup < 100.0, "Item lookup: %.2f μs (target: <100 μs)".format(avgItemLookup))
        assertTrue(avgQuestLookup < 100.0, "Quest lookup: %.2f μs (target: <100 μs)".format(avgQuestLookup))
        assertTrue(avgEnemyLookup < 100.0, "Enemy lookup: %.2f μs (target: <100 μs)".format(avgEnemyLookup))
    }

    @Test
    fun `benchmark getAllItems performance with filtering`() {
        println("\n=== GET ALL ITEMS WITH FILTERING BENCHMARK ===")
        
        val filterIterations = 100
        
        // Common filtering operation: find all consumables
        val filterTime = measureTime {
            repeat(filterIterations) {
                ItemCatalog.getAllItems().filter { it.type == ItemType.CONSUMABLE }
            }
        }
        val avgFilterTime = filterTime.inWholeMilliseconds / filterIterations.toDouble()
        println("Average filter time (consumables from 215 items): %.2f ms".format(avgFilterTime))
        
        // Find all craftable items
        val craftableFilterTime = measureTime {
            repeat(filterIterations) {
                ItemCatalog.getAllItems().filter { item ->
                    RecipeCatalog.getAllRecipes().any { it.output.itemId == item.id }
                }
            }
        }
        val avgCraftableTime = craftableFilterTime.inWholeMilliseconds / filterIterations.toDouble()
        println("Average craftable filter time: %.2f ms".format(avgCraftableTime))
        
        println("\nTARGET: <50ms per filtered search")
        
        // Performance assertions
        assertTrue(avgFilterTime < 50.0, "Filter time: %.2f ms (target: <50ms)".format(avgFilterTime))
        assertTrue(avgCraftableTime < 50.0, "Craftable filter: %.2f ms (target: <50ms)".format(avgCraftableTime))
    }

    @Test
    fun `benchmark recipe validation performance`() {
        println("\n=== RECIPE VALIDATION BENCHMARK ===")
        
        // Measure time to validate all 93 recipes
        val validationTime = measureTime {
            try {
                val recipes = RecipeCatalog.getAllRecipes()
                println("Loaded ${recipes.size} recipes")
                recipes.forEach { recipe ->
                    println("Validating recipe: ${recipe.id}")
                    // Validate inputs exist
                    recipe.inputs.forEach { input ->
                        val item = ItemCatalog.getItem(input.itemId)
                        if (item == null) {
                            println("ERROR: Recipe ${recipe.id} references missing item: ${input.itemId}")
                        }
                    }
                    // Validate output exists
                    val outputItem = ItemCatalog.getItem(recipe.output.itemId)
                    if (outputItem == null) {
                        println("ERROR: Recipe ${recipe.id} has missing output item: ${recipe.output.itemId}")
                    }
                }
            } catch (e: Exception) {
                println("EXCEPTION during recipe validation: ${e.message}")
                e.printStackTrace()
                throw e
            }
        }
        
        println("Recipe validation time (93 recipes): ${validationTime.inWholeMilliseconds}ms")
        println("TARGET: <100ms")
        
        assertTrue(
            validationTime.inWholeMilliseconds < 100,
            "Recipe validation took ${validationTime.inWholeMilliseconds}ms (target: <100ms)"
        )
    }

    @Test
    fun `performance summary report`() {
        println("\n" + "=".repeat(60))
        println("PERFORMANCE BENCHMARK SUMMARY")
        println("=".repeat(60))
        println("Content Scale: 512 total assets")
        println("  - Items: 215")
        println("  - Recipes: 93")
        println("  - Enemies: 40")
        println("  - Skills: 57")
        println("  - Quests: 55")
        println("  - NPCs: 52")
        println("\nRun individual benchmark tests for detailed metrics.")
        println("All tests must pass performance targets.")
        println("=".repeat(60))
        
        // This test always passes - it's just a summary
        assertTrue(true)
    }
}
