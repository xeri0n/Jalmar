package com.jalmarquest.shared.integration

import com.jalmarquest.shared.combat.EnemyCatalog
import com.jalmarquest.shared.inventory.ItemCatalog
import com.jalmarquest.shared.npc.NPCCatalog
import com.jalmarquest.shared.quest.QuestCatalog
import com.jalmarquest.shared.crafting.RecipeCatalog
import kotlin.test.Test

/**
 * Quick validation to find cross-reference issues.
 */
class QuickValidationTest {
    
    @Test
    fun `check quest givers`() {
        println("\n=== QUEST GIVER VALIDATION ===")
        QuestCatalog.allQuests.forEach { quest ->
            quest.giver?.let { giver ->
                val npc = NPCCatalog.getNPC(giver)
                if (npc == null) {
                    println("ERROR: Quest '${quest.id}' (${quest.name}) has invalid giver '${giver}'")
                }
            }
        }
        println("Quest giver check complete")
    }
    
    @Test
    fun `check NPC quest IDs`() {
        println("\n=== NPC QUEST ID VALIDATION ===")
        NPCCatalog.allNPCs.forEach { npc ->
            npc.questGiverIds.forEach { questId ->
                val quest = QuestCatalog.getQuest(questId)
                if (quest == null) {
                    println("ERROR: NPC '${npc.id}' (${npc.name}) references invalid quest '${questId}'")
                }
            }
        }
        println("NPC quest ID check complete")
    }
    
    @Test
    fun `check quest KILL objectives`() {
        println("\n=== QUEST KILL OBJECTIVE VALIDATION ===")
        QuestCatalog.allQuests.forEach { quest ->
            quest.objectives.forEach { objective ->
                if (objective.type.name == "KILL") {
                    objective.targetId?.let { targetId ->
                        val enemy = EnemyCatalog.getEnemy(targetId)
                        if (enemy == null) {
                            println("ERROR: Quest '${quest.id}' KILL objective references invalid enemy '${targetId}'")
                        }
                    }
                }
            }
        }
        println("Quest KILL objective check complete")
    }
    
    @Test
    fun `check quest recipe unlocks`() {
        println("\n=== QUEST RECIPE UNLOCK VALIDATION ===")
        QuestCatalog.allQuests.forEach { quest ->
            quest.rewards.unlockRecipeIds.forEach { recipeId ->
                val recipe = RecipeCatalog.getRecipe(recipeId)
                if (recipe == null) {
                    println("ERROR: Quest '${quest.id}' unlocks invalid recipe '${recipeId}'")
                }
            }
        }
        println("Quest recipe unlock check complete")
    }
}
