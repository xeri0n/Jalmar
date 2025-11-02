package com.jalmar.quest.npc

import com.jalmarquest.shared.npc.NPC
import com.jalmarquest.shared.npc.NPCOccupation
import com.jalmarquest.shared.npc.NPCPersonality
import com.jalmarquest.shared.npc.NPCSchedule
import com.jalmarquest.shared.npc.NPCSpecies
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Manages NPC instances and interactions.
 * Thread-safe with Mutex for concurrent access.
 */
class NPCManager {
    private val mutex = Mutex()
    private val npcRegistry = mutableMapOf<String, NPC>()
    private val relationshipScores = mutableMapOf<String, Int>() // NPC ID -> relationship points
    
    init {
        // Register default NPCs from Buttonburgh
        registerDefaultNPCs()
    }
    
    /**
     * Get an NPC by ID.
     */
    suspend fun getNPC(npcId: String): NPC? {
        return mutex.withLock {
            npcRegistry[npcId]
        }
    }
    
    /**
     * Get all registered NPCs.
     */
    suspend fun getAllNPCs(): List<NPC> {
        return mutex.withLock {
            npcRegistry.values.toList()
        }
    }
    
    /**
     * Register a new NPC.
     */
    suspend fun registerNPC(npc: NPC) {
        mutex.withLock {
            npcRegistry[npc.id] = npc
            if (!relationshipScores.containsKey(npc.id)) {
                relationshipScores[npc.id] = 0 // Neutral relationship
            }
        }
    }
    
    /**
     * Get relationship score with an NPC.
     * Returns 0 (neutral) if NPC not found.
     */
    suspend fun getRelationship(npcId: String): Int {
        return mutex.withLock {
            relationshipScores[npcId] ?: 0
        }
    }
    
    /**
     * Modify relationship score with an NPC.
     */
    suspend fun modifyRelationship(npcId: String, delta: Int) {
        mutex.withLock {
            val current = relationshipScores[npcId] ?: 0
            relationshipScores[npcId] = (current + delta).coerceIn(-100, 100)
        }
    }
    
    /**
     * Get relationship level description.
     */
    fun getRelationshipLevel(score: Int): String {
        return when {
            score >= 75 -> "Best Friends"
            score >= 50 -> "Close Friend"
            score >= 25 -> "Friend"
            score >= 10 -> "Friendly"
            score >= -10 -> "Neutral"
            score >= -25 -> "Unfriendly"
            score >= -50 -> "Hostile"
            else -> "Enemy"
        }
    }
    
    private fun registerDefaultNPCs() {
        // Innkeeper Bertha - The Gilded Seed Inn
        val bertha = NPC(
            id = "innkeeper_bertha",
            name = "Bertha the Innkeeper",
            species = NPCSpecies.BUTTON_QUAIL,
            personality = NPCPersonality(
                friendliness = 9,
                courage = 5,
                wisdom = 7,
                humor = 8,
                traits = listOf("Cheerful", "Welcoming", "Gossip")
            ),
            homeLocationId = "the_gilded_seed_inn",
            occupation = NPCOccupation.INNKEEPER,
            factionId = "buttonburgh_citizens",
            defaultDialogueTreeId = "bertha_greeting",
            schedule = NPCSchedule(listOf(
                com.jalmarquest.shared.npc.ScheduleEntry(
                    startHour = 0,
                    endHour = 24,
                    locationId = "the_gilded_seed_inn",
                    activity = "tending_inn"
                )
            ))
        )
        
        // Grumble Forgepaw - The Quailsmith
        val grumble = NPC(
            id = "grumble_forgepaw",
            name = "Grumble Forgepaw",
            species = NPCSpecies.MOLE,
            personality = NPCPersonality(
                friendliness = 4,
                courage = 8,
                wisdom = 9,
                humor = 3,
                traits = listOf("Grumpy", "Skilled", "Perfectionist")
            ),
            homeLocationId = "the_quailsmith",
            occupation = NPCOccupation.CRAFTSMAN,
            factionId = "buttonburgh_citizens",
            defaultDialogueTreeId = "grumble_greeting",
            merchantInventory = listOf("twig_spear", "acorn_helmet", "pebble_shield"),
            schedule = NPCSchedule(listOf(
                com.jalmarquest.shared.npc.ScheduleEntry(
                    startHour = 0,
                    endHour = 24,
                    locationId = "the_quailsmith",
                    activity = "smithing"
                )
            ))
        )
        
        // Old Quill - Scholar
        val oldQuill = NPC(
            id = "old_quill",
            name = "Old Quill",
            species = NPCSpecies.SPARROW,
            personality = NPCPersonality(
                friendliness = 7,
                courage = 4,
                wisdom = 10,
                humor = 6,
                traits = listOf("Wise", "Patient", "Mysterious")
            ),
            homeLocationId = "old_quills_study",
            occupation = NPCOccupation.SCHOLAR,
            factionId = "buttonburgh_citizens",
            defaultDialogueTreeId = "quill_greeting",
            schedule = NPCSchedule(listOf(
                com.jalmarquest.shared.npc.ScheduleEntry(
                    startHour = 0,
                    endHour = 24,
                    locationId = "old_quills_study",
                    activity = "studying"
                )
            ))
        )
        
        npcRegistry["innkeeper_bertha"] = bertha
        npcRegistry["grumble_forgepaw"] = grumble
        npcRegistry["old_quill"] = oldQuill
        
        // Initialize neutral relationships
        relationshipScores["innkeeper_bertha"] = 0
        relationshipScores["grumble_forgepaw"] = 0
        relationshipScores["old_quill"] = 0
    }
}
