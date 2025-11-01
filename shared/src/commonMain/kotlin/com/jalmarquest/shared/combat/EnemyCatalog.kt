package com.jalmarquest.shared.combat

/**
 * Catalog of all enemy types in JalmarQuest.
 * Quail-scale creatures from a button quail's perspective.
 */
object EnemyCatalog {
    
    /**
     * All available enemies in the game.
     * Initial catalog: 10 enemies (expandable to 40+)
     */
    val allEnemies: List<Enemy> = listOf(
        // ========== Level 1-3 Enemies (Starting Area) ==========
        
        Enemy(
            id = "grasshopper",
            name = "The Hopper",
            description = "A towering grasshopper that leaps unpredictably. Its powerful legs make it difficult to pin down.",
            maxHp = 25,
            strength = 4,
            agility = 14,
            vitality = 3,
            intelligence = 2,
            luck = 6,
            baseDamage = 3,
            defense = 1,
            behaviorType = EnemyBehaviorType.FLEEING,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("twig", minQuantity = 1, maxQuantity = 2, dropChance = 0.7f),
                    LootDrop("grass_blade", minQuantity = 1, maxQuantity = 1, dropChance = 0.4f)
                )
            ),
            xpReward = 15,
            level = 1
        ),
        
        Enemy(
            id = "beetle",
            name = "Armored Titan",
            description = "A massive beetle with an impenetrable shell. Its slow but devastating charges are feared by all.",
            maxHp = 45,
            strength = 8,
            agility = 4,
            vitality = 12,
            intelligence = 2,
            luck = 3,
            baseDamage = 6,
            defense = 5,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("beetle_shell", minQuantity = 1, maxQuantity = 1, dropChance = 0.9f),
                    LootDrop("twig", minQuantity = 1, maxQuantity = 3, dropChance = 0.5f)
                )
            ),
            xpReward = 25,
            level = 2
        ),
        
        Enemy(
            id = "ant",
            name = "Colony Soldier",
            description = "A relentless ant warrior. Never fights alone, but this one seems separated from its colony.",
            maxHp = 20,
            strength = 6,
            agility = 8,
            vitality = 5,
            intelligence = 3,
            luck = 4,
            baseDamage = 4,
            defense = 2,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("seed", minQuantity = 1, maxQuantity = 3, dropChance = 0.6f),
                    LootDrop("grass_blade", minQuantity = 1, maxQuantity = 2, dropChance = 0.5f)
                )
            ),
            xpReward = 12,
            level = 1
        ),
        
        Enemy(
            id = "ladybug",
            name = "Spotted Guardian",
            description = "A gentle-looking ladybug with deceptively strong mandibles. Protects its territory fiercely.",
            maxHp = 30,
            strength = 5,
            agility = 7,
            vitality = 6,
            intelligence = 4,
            luck = 8,
            baseDamage = 4,
            defense = 3,
            behaviorType = EnemyBehaviorType.DEFENSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("berry", minQuantity = 1, maxQuantity = 2, dropChance = 0.7f),
                    LootDrop("dried_leaf", minQuantity = 1, maxQuantity = 1, dropChance = 0.4f)
                )
            ),
            xpReward = 18,
            level = 2
        ),
        
        // ========== Level 3-5 Enemies (Intermediate) ==========
        
        Enemy(
            id = "spider",
            name = "Web Spinner",
            description = "A cunning spider that waits in ambush. Its venomous bite weakens even the mightiest foes.",
            maxHp = 35,
            strength = 7,
            agility = 10,
            vitality = 5,
            intelligence = 6,
            luck = 5,
            baseDamage = 5,
            defense = 2,
            behaviorType = EnemyBehaviorType.DEFENSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("spider_silk", minQuantity = 1, maxQuantity = 2, dropChance = 0.8f),
                    LootDrop("twig", minQuantity = 1, maxQuantity = 1, dropChance = 0.3f)
                )
            ),
            xpReward = 30,
            level = 3
        ),
        
        Enemy(
            id = "moth",
            name = "Dust Cloud",
            description = "A dusty moth whose wing scales create blinding clouds. Prefers to flee rather than fight.",
            maxHp = 22,
            strength = 3,
            agility = 12,
            vitality = 4,
            intelligence = 5,
            luck = 7,
            baseDamage = 3,
            defense = 1,
            behaviorType = EnemyBehaviorType.FLEEING,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("moth_dust", minQuantity = 1, maxQuantity = 1, dropChance = 0.6f),
                    LootDrop("feather", minQuantity = 1, maxQuantity = 1, dropChance = 0.3f)
                )
            ),
            xpReward = 20,
            level = 2
        ),
        
        Enemy(
            id = "cricket",
            name = "Chirping Terror",
            description = "Its deafening chirps disorient prey. Unpredictable movements make it hard to counter.",
            maxHp = 28,
            strength = 6,
            agility = 13,
            vitality = 5,
            intelligence = 4,
            luck = 9,
            baseDamage = 5,
            defense = 2,
            behaviorType = EnemyBehaviorType.RANDOM,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("grass_blade", minQuantity = 2, maxQuantity = 3, dropChance = 0.7f),
                    LootDrop("seed", minQuantity = 1, maxQuantity = 2, dropChance = 0.5f)
                )
            ),
            xpReward = 22,
            level = 3
        ),
        
        Enemy(
            id = "centipede",
            name = "Segment Serpent",
            description = "A writhing centipede with countless legs. Its venomous pincers inflict lasting pain.",
            maxHp = 40,
            strength = 9,
            agility = 6,
            vitality = 7,
            intelligence = 3,
            luck = 4,
            baseDamage = 7,
            defense = 3,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("centipede_segment", minQuantity = 1, maxQuantity = 3, dropChance = 0.8f),
                    LootDrop("grass_blade", minQuantity = 1, maxQuantity = 1, dropChance = 0.4f)
                )
            ),
            xpReward = 35,
            level = 4
        ),
        
        // ========== Level 5+ Enemies (Advanced) ==========
        
        Enemy(
            id = "earwig",
            name = "Pincer Beast",
            description = "A defensive earwig with formidable pincers. Balances offense and defense masterfully.",
            maxHp = 50,
            strength = 10,
            agility = 8,
            vitality = 9,
            intelligence = 5,
            luck = 6,
            baseDamage = 8,
            defense = 4,
            behaviorType = EnemyBehaviorType.DEFENSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("earwig_pincer", minQuantity = 1, maxQuantity = 2, dropChance = 0.7f),
                    LootDrop("beetle_shell", minQuantity = 1, maxQuantity = 1, dropChance = 0.3f)
                )
            ),
            xpReward = 45,
            level = 5
        ),
        
        Enemy(
            id = "firefly",
            name = "Glowing Phantom",
            description = "A mystical firefly whose bioluminescent glow mesmerizes. Burns foes with searing light.",
            maxHp = 32,
            strength = 5,
            agility = 15,
            vitality = 4,
            intelligence = 8,
            luck = 10,
            baseDamage = 6,
            defense = 2,
            behaviorType = EnemyBehaviorType.FLEEING,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("firefly_lantern", minQuantity = 1, maxQuantity = 1, dropChance = 0.5f),
                    LootDrop("glowing_ember", minQuantity = 1, maxQuantity = 2, dropChance = 0.6f)
                )
            ),
            xpReward = 40,
            level = 5
        )
    )
    
    /**
     * Gets an enemy by ID.
     */
    fun getEnemy(enemyId: String): Enemy? = allEnemies.find { it.id == enemyId }
    
    /**
     * Gets all enemies of a specific level.
     */
    fun getEnemiesByLevel(level: Int): List<Enemy> = allEnemies.filter { it.level == level }
    
    /**
     * Gets all enemies within a level range.
     */
    fun getEnemiesByLevelRange(minLevel: Int, maxLevel: Int): List<Enemy> = 
        allEnemies.filter { it.level in minLevel..maxLevel }
    
    /**
     * Gets all enemies with a specific behavior type.
     */
    fun getEnemiesByBehavior(behaviorType: EnemyBehaviorType): List<Enemy> = 
        allEnemies.filter { it.behaviorType == behaviorType }
    
    /**
     * Gets enemies appropriate for a location based on recommended level.
     * Returns enemies within ±1 level of the location's recommended level.
     * 
     * @param recommendedLevel The location's recommended player level
     * @return List of enemies suitable for this location (may be empty)
     */
    fun getEnemiesForLocation(recommendedLevel: Int): List<Enemy> {
        val minLevel = (recommendedLevel - 1).coerceAtLeast(1)
        val maxLevel = recommendedLevel + 1
        return getEnemiesByLevelRange(minLevel, maxLevel)
    }
    
    /**
     * Gets a random enemy for a location.
     * Returns null if no suitable enemies exist.
     */
    fun getRandomEnemyForLocation(recommendedLevel: Int): Enemy? {
        val enemies = getEnemiesForLocation(recommendedLevel)
        return enemies.randomOrNull()
    }
}
