package com.jalmarquest.shared.npc

/**
 * Static catalog of all factions in JalmarQuest.
 * 
 * Factions represent groups with shared interests and relationships.
 */
object FactionCatalog {
    
    /**
     * Buttonburgh Council - Village leadership
     */
    val buttonburghCouncil = Faction(
        id = "buttonburgh_council",
        name = "Buttonburgh Council",
        description = "The wise elders who govern Buttonburgh with fairness and wisdom.",
        homeLocationId = "buttonburgh_village",
        allyFactionIds = listOf(
            "buttonburgh_citizens",
            "buttonburgh_craftsmen",
            "buttonburgh_merchants",
            "buttonburgh_guard",
            "buttonburgh_scholars"
        ),
        enemyFactionIds = listOf("sparrow_raiders")
    )
    
    /**
     * Buttonburgh Citizens - General villagers
     */
    val buttonburghCitizens = Faction(
        id = "buttonburgh_citizens",
        name = "Buttonburgh Citizens",
        description = "The friendly quail folk who call Buttonburgh home.",
        homeLocationId = "buttonburgh_village",
        allyFactionIds = listOf(
            "buttonburgh_council",
            "buttonburgh_craftsmen",
            "buttonburgh_merchants",
            "buttonburgh_guard"
        ),
        enemyFactionIds = listOf("sparrow_raiders")
    )
    
    /**
     * Buttonburgh Craftsmen - Artisans and smiths
     */
    val buttonburghCraftsmen = Faction(
        id = "buttonburgh_craftsmen",
        name = "Buttonburgh Craftsmen",
        description = "Skilled artisans who craft weapons and armor from natural materials.",
        homeLocationId = "the_quailsmith",
        allyFactionIds = listOf(
            "buttonburgh_council",
            "buttonburgh_citizens",
            "buttonburgh_merchants"
        ),
        enemyFactionIds = listOf("sparrow_raiders")
    )
    
    /**
     * Buttonburgh Merchants - Traders and innkeepers
     */
    val buttonburghMerchants = Faction(
        id = "buttonburgh_merchants",
        name = "Buttonburgh Merchants",
        description = "Enterprising traders who keep the village's economy thriving.",
        homeLocationId = "buttonburgh_village",
        allyFactionIds = listOf(
            "buttonburgh_council",
            "buttonburgh_citizens",
            "buttonburgh_craftsmen",
            "beetle_traders"
        ),
        enemyFactionIds = emptyList()
    )
    
    /**
     * Buttonburgh Guard - Village defenders
     */
    val buttonburghGuard = Faction(
        id = "buttonburgh_guard",
        name = "Buttonburgh Guard",
        description = "Brave warriors who protect Buttonburgh from threats.",
        homeLocationId = "buttonburgh_village",
        allyFactionIds = listOf(
            "buttonburgh_council",
            "buttonburgh_citizens",
            "buttonburgh_explorers"
        ),
        enemyFactionIds = listOf("sparrow_raiders")
    )
    
    /**
     * Buttonburgh Scholars - Researchers and healers
     */
    val buttonburghScholars = Faction(
        id = "buttonburgh_scholars",
        name = "Buttonburgh Scholars",
        description = "Learned quail who study the world and preserve knowledge.",
        homeLocationId = "old_quills_study",
        allyFactionIds = listOf(
            "buttonburgh_council",
            "firefly_circle"
        ),
        enemyFactionIds = emptyList()
    )
    
    /**
     * Buttonburgh Healers - Medical practitioners
     */
    val buttonburghHealers = Faction(
        id = "buttonburgh_healers",
        name = "Buttonburgh Healers",
        description = "Compassionate healers who tend to the sick and injured.",
        homeLocationId = "buttonburgh_village",
        allyFactionIds = listOf(
            "buttonburgh_council",
            "buttonburgh_citizens",
            "buttonburgh_scholars"
        ),
        enemyFactionIds = emptyList()
    )
    
    /**
     * Buttonburgh Explorers - Dungeon scouts
     */
    val buttonburghExplorers = Faction(
        id = "buttonburgh_explorers",
        name = "Buttonburgh Explorers",
        description = "Brave adventurers who map dangerous territories.",
        homeLocationId = "buttonburgh_village",
        allyFactionIds = listOf(
            "buttonburgh_council",
            "buttonburgh_guard"
        ),
        enemyFactionIds = listOf("sparrow_raiders")
    )
    
    /**
     * Firefly Circle - Mystical firefly group
     */
    val fireflyCircle = Faction(
        id = "firefly_circle",
        name = "Firefly Circle",
        description = "Mysterious fireflies who guard ancient secrets.",
        homeLocationId = "firefly_glade",
        allyFactionIds = listOf("buttonburgh_scholars"),
        enemyFactionIds = emptyList()
    )
    
    /**
     * Beetle Traders - Friendly beetle merchants
     */
    val beetleTraders = Faction(
        id = "beetle_traders",
        name = "Beetle Traders",
        description = "Industrious beetle merchants who trade exotic goods.",
        homeLocationId = "buttonburgh_village",
        allyFactionIds = listOf("buttonburgh_merchants"),
        enemyFactionIds = emptyList()
    )
    
    /**
     * Sparrow Raiders - Hostile sparrow faction
     */
    val sparrowRaiders = Faction(
        id = "sparrow_raiders",
        name = "Sparrow Raiders",
        description = "Aggressive sparrows who threaten the peaceful quail folk.",
        homeLocationId = "forest_edge",
        allyFactionIds = emptyList(),
        enemyFactionIds = listOf(
            "buttonburgh_council",
            "buttonburgh_citizens",
            "buttonburgh_craftsmen",
            "buttonburgh_guard",
            "buttonburgh_explorers"
        )
    )
    
    // ===== CATALOG METHODS =====
    
    /**
     * All factions in the game.
     */
    val allFactions: List<Faction> = listOf(
        buttonburghCouncil,
        buttonburghCitizens,
        buttonburghCraftsmen,
        buttonburghMerchants,
        buttonburghGuard,
        buttonburghScholars,
        buttonburghHealers,
        buttonburghExplorers,
        fireflyCircle,
        beetleTraders,
        sparrowRaiders
    )
    
    /**
     * Gets a faction by ID.
     */
    fun getFaction(id: String): Faction? {
        return allFactions.find { it.id == id }
    }
    
    /**
     * Gets all allied factions for a given faction.
     */
    fun getAlliedFactions(factionId: String): List<Faction> {
        val faction = getFaction(factionId) ?: return emptyList()
        return faction.allyFactionIds.mapNotNull { getFaction(it) }
    }
    
    /**
     * Gets all enemy factions for a given faction.
     */
    fun getEnemyFactions(factionId: String): List<Faction> {
        val faction = getFaction(factionId) ?: return emptyList()
        return faction.enemyFactionIds.mapNotNull { getFaction(it) }
    }
    
    /**
     * Checks if two factions are allies.
     */
    fun areAllies(factionId1: String, factionId2: String): Boolean {
        val faction1 = getFaction(factionId1) ?: return false
        return faction1.isAlly(factionId2)
    }
    
    /**
     * Checks if two factions are enemies.
     */
    fun areEnemies(factionId1: String, factionId2: String): Boolean {
        val faction1 = getFaction(factionId1) ?: return false
        return faction1.isEnemy(factionId2)
    }
    
    /**
     * Returns total faction count.
     */
    fun getTotalFactionCount(): Int = allFactions.size
    
    /**
     * Validates all factions have unique IDs.
     */
    fun validateCatalog(): Boolean {
        // Check unique IDs
        val ids = allFactions.map { it.id }
        if (ids.size != ids.distinct().size) {
            return false
        }
        
        // Check all ally/enemy references are valid
        allFactions.forEach { faction ->
            faction.allyFactionIds.forEach { allyId ->
                if (getFaction(allyId) == null) return false
            }
            faction.enemyFactionIds.forEach { enemyId ->
                if (getFaction(enemyId) == null) return false
            }
        }
        
        return true
    }
}
