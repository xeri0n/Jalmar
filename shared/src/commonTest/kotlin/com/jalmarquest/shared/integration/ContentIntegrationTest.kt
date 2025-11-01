package com.jalmarquest.shared.integration

import com.jalmarquest.shared.combat.EnemyCatalog
import com.jalmarquest.shared.inventory.ItemCatalog
import com.jalmarquest.shared.npc.NPCCatalog
import com.jalmarquest.shared.quest.QuestCatalog
import com.jalmarquest.shared.crafting.RecipeCatalog
import com.jalmarquest.shared.skills.SkillCatalog
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Comprehensive content integration validation tests.
 * Validates cross-references between all game catalogs.
 */
class ContentIntegrationTest {
    
    // ===== CATALOG LOADING TESTS =====
    
    @Test
    fun `all catalogs should load without errors`() {
        // This test passes if catalog initialization doesn't throw
        assertTrue(ItemCatalog.getAllItems().isNotEmpty())
        assertTrue(RecipeCatalog.getAllRecipes().isNotEmpty())
        assertTrue(EnemyCatalog.allEnemies.isNotEmpty())
        assertTrue(SkillCatalog.allSkills.isNotEmpty())
        assertTrue(QuestCatalog.allQuests.isNotEmpty())
        assertTrue(NPCCatalog.allNPCs.isNotEmpty())
    }
    
    @Test
    fun `catalogs should have expected counts`() {
        assertTrue(ItemCatalog.getAllItems().size >= 215, "ItemCatalog should have 215+ items, has ${ItemCatalog.getAllItems().size}")
        assertTrue(RecipeCatalog.getAllRecipes().size >= 93, "RecipeCatalog should have 93+ recipes, has ${RecipeCatalog.getAllRecipes().size}")
        assertTrue(EnemyCatalog.allEnemies.size >= 40, "EnemyCatalog should have 40+ enemies, has ${EnemyCatalog.allEnemies.size}")
        assertTrue(SkillCatalog.allSkills.size >= 57, "SkillCatalog should have 57+ skills, has ${SkillCatalog.allSkills.size}")
        assertTrue(QuestCatalog.allQuests.size >= 55, "QuestCatalog should have 55+ quests, has ${QuestCatalog.allQuests.size}")
        assertTrue(NPCCatalog.allNPCs.size >= 52, "NPCCatalog should have 52+ NPCs, has ${NPCCatalog.allNPCs.size}")
    }
    
    // ===== RECIPE → ITEM VALIDATION =====
    
    @Test
    fun `all recipe inputs should reference valid items`() {
        val invalidRecipes = mutableListOf<String>()
        
        RecipeCatalog.getAllRecipes().forEach { recipe ->
            recipe.inputs.forEach { input ->
                if (ItemCatalog.getItem(input.itemId) == null) {
                    invalidRecipes.add("Recipe '${recipe.id}' references non-existent input item '${input.itemId}'")
                }
            }
        }
        
        assertTrue(
            invalidRecipes.isEmpty(),
            "Found ${invalidRecipes.size} recipe input errors:\n${invalidRecipes.take(10).joinToString("\n")}"
        )
    }
    
    @Test
    fun `all recipe outputs should reference valid items`() {
        val invalidRecipes = mutableListOf<String>()
        
        RecipeCatalog.getAllRecipes().forEach { recipe ->
            if (ItemCatalog.getItem(recipe.output.itemId) == null) {
                invalidRecipes.add("Recipe '${recipe.id}' outputs non-existent item '${recipe.output.itemId}'")
            }
        }
        
        assertTrue(
            invalidRecipes.isEmpty(),
            "Found ${invalidRecipes.size} recipe output errors:\n${invalidRecipes.joinToString("\n")}"
        )
    }
    
    // ===== ENEMY → ITEM VALIDATION =====
    
    @Test
    fun `all enemy loot drops should reference valid items`() {
        val invalidEnemies = mutableListOf<String>()
        
        EnemyCatalog.allEnemies.forEach { enemy ->
            enemy.lootTable.drops.forEach { lootDrop ->
                if (ItemCatalog.getItem(lootDrop.itemId) == null) {
                    invalidEnemies.add("Enemy '${enemy.id}' drops non-existent item '${lootDrop.itemId}'")
                }
            }
        }
        
        assertTrue(
            invalidEnemies.isEmpty(),
            "Found ${invalidEnemies.size} enemy loot errors:\n${invalidEnemies.take(10).joinToString("\n")}"
        )
    }
    
    // ===== QUEST → NPC VALIDATION =====
    
    @Test
    fun `all quest givers should be valid NPCs`() {
        val invalidQuests = mutableListOf<String>()
        
        QuestCatalog.allQuests.forEach { quest ->
            quest.giver?.let { giver ->
                if (NPCCatalog.getNPC(giver) == null) {
                    invalidQuests.add("Quest '${quest.id}' has non-existent giver '${giver}'")
                }
            }
        }
        
        assertTrue(
            invalidQuests.isEmpty(),
            "Found ${invalidQuests.size} quest giver errors:\n${invalidQuests.joinToString("\n")}"
        )
    }
    
    @Test
    fun `all NPC questGiverIds should reference valid quests`() {
        val invalidNPCs = mutableListOf<String>()
        
        NPCCatalog.allNPCs.forEach { npc ->
            npc.questGiverIds.forEach { questId ->
                if (QuestCatalog.getQuest(questId) == null) {
                    invalidNPCs.add("NPC '${npc.id}' gives non-existent quest '${questId}'")
                }
            }
        }
        
        assertTrue(
            invalidNPCs.isEmpty(),
            "Found ${invalidNPCs.size} NPC quest giver errors:\n${invalidNPCs.take(10).joinToString("\n")}"
        )
    }
    
    // ===== QUEST → ITEM VALIDATION =====
    
    @Test
    fun `all quest item rewards should reference valid items`() {
        val invalidQuests = mutableListOf<String>()
        
        QuestCatalog.allQuests.forEach { quest ->
            quest.rewards.items.forEach { itemId ->
                if (ItemCatalog.getItem(itemId) == null) {
                    invalidQuests.add("Quest '${quest.id}' rewards non-existent item '${itemId}'")
                }
            }
        }
        
        assertTrue(
            invalidQuests.isEmpty(),
            "Found ${invalidQuests.size} quest reward errors:\n${invalidQuests.take(10).joinToString("\n")}"
        )
    }
    
    @Test
    fun `all quest recipe unlocks should reference valid recipes`() {
        val invalidQuests = mutableListOf<String>()
        
        QuestCatalog.allQuests.forEach { quest ->
            quest.rewards.unlockRecipeIds.forEach { recipeId ->
                if (RecipeCatalog.getRecipe(recipeId) == null) {
                    invalidQuests.add("Quest '${quest.id}' unlocks non-existent recipe '${recipeId}'")
                }
            }
        }
        
        assertTrue(
            invalidQuests.isEmpty(),
            "Found ${invalidQuests.size} quest recipe unlock errors:\n${invalidQuests.joinToString("\n")}"
        )
    }
    
    // ===== QUEST → ENEMY VALIDATION =====
    
    @Test
    fun `all quest KILL objectives should reference valid enemies`() {
        val invalidQuests = mutableListOf<String>()
        
        QuestCatalog.allQuests.forEach { quest ->
            quest.objectives.forEach { objective ->
                if (objective.type.name == "KILL") {
                    objective.targetId?.let { targetId ->
                        if (EnemyCatalog.getEnemy(targetId) == null) {
                            invalidQuests.add("Quest '${quest.id}' KILL objective references non-existent enemy '${targetId}'")
                        }
                    }
                }
            }
        }
        
        assertTrue(
            invalidQuests.isEmpty(),
            "Found ${invalidQuests.size} quest KILL objective errors:\n${invalidQuests.take(10).joinToString("\n")}"
        )
    }
    
    // ===== NPC → ITEM VALIDATION =====
    
    @Test
    fun `all NPC merchant inventories should reference valid items`() {
        val invalidNPCs = mutableListOf<String>()
        
        NPCCatalog.allNPCs.forEach { npc ->
            npc.merchantInventory.forEach { itemId ->
                if (ItemCatalog.getItem(itemId) == null) {
                    invalidNPCs.add("NPC '${npc.id}' sells non-existent item '${itemId}'")
                }
            }
        }
        
        assertTrue(
            invalidNPCs.isEmpty(),
            "Found ${invalidNPCs.size} NPC merchant inventory errors:\n${invalidNPCs.take(10).joinToString("\n")}"
        )
    }
    
    // ===== CONTENT STATISTICS =====
    
    @Test
    fun `content statistics report`() {
        println("\n=== CONTENT SPRINT FINAL STATISTICS ===")
        println("Items: ${ItemCatalog.getAllItems().size}")
        println("Recipes: ${RecipeCatalog.getAllRecipes().size}")
        println("Enemies: ${EnemyCatalog.allEnemies.size}")
        println("Skills: ${SkillCatalog.allSkills.size}")
        println("Quests: ${QuestCatalog.allQuests.size}")
        println("NPCs: ${NPCCatalog.allNPCs.size}")
        println("TOTAL ASSETS: ${ItemCatalog.getAllItems().size + RecipeCatalog.getAllRecipes().size + EnemyCatalog.allEnemies.size + SkillCatalog.allSkills.size + QuestCatalog.allQuests.size + NPCCatalog.allNPCs.size}")
        
        // Quest giver NPCs
        val questGiverNPCs = NPCCatalog.allNPCs.filter { it.questGiverIds.isNotEmpty() }
        println("\nQuest Giver NPCs: ${questGiverNPCs.size}")
        
        // Merchant NPCs
        val merchantNPCs = NPCCatalog.allNPCs.filter { it.merchantInventory.isNotEmpty() }
        println("Merchant NPCs: ${merchantNPCs.size}")
        
        // Craftable items
        val craftableItems = RecipeCatalog.getAllRecipes().map { it.output.itemId }.distinct()
        println("\nCraftable Items: ${craftableItems.size}")
        
        // Quest rewards
        val questRewardItems = QuestCatalog.allQuests.flatMap { it.rewards.items }.distinct()
        println("Quest Reward Items: ${questRewardItems.size}")
        
        // Enemy loot
        val enemyLootItems = EnemyCatalog.allEnemies.flatMap { it.lootTable.drops.map { drop -> drop.itemId } }.distinct()
        println("Enemy Loot Items: ${enemyLootItems.size}")
        
        assertTrue(true) // Always pass, this is just a report
    }
}
