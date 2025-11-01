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
        ),
        
        // ========== GRASSLAND (Level 6-10) ==========
        
        Enemy(
            id = "bumblebee",
            name = "Fuzzy Bomber",
            description = "A massive bumblebee with a deafening buzz. Its stinger delivers a paralyzing venom.",
            maxHp = 55,
            strength = 11,
            agility = 9,
            vitality = 8,
            intelligence = 6,
            luck = 7,
            baseDamage = 9,
            defense = 3,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("honey_drop", minQuantity = 1, maxQuantity = 3, dropChance = 0.7f),
                    LootDrop("bee_stinger", minQuantity = 1, maxQuantity = 1, dropChance = 0.6f),
                    LootDrop("honeycomb", minQuantity = 1, maxQuantity = 1, dropChance = 0.4f)
                )
            ),
            xpReward = 50,
            level = 6
        ),
        
        Enemy(
            id = "dragonfly",
            name = "Sky Hunter",
            description = "A lightning-fast dragonfly with iridescent wings. Strikes from the air with deadly precision.",
            maxHp = 48,
            strength = 9,
            agility = 18,
            vitality = 6,
            intelligence = 7,
            luck = 11,
            baseDamage = 8,
            defense = 2,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("dragonfly_wing", minQuantity = 1, maxQuantity = 2, dropChance = 0.8f),
                    LootDrop("crystal_shard", minQuantity = 1, maxQuantity = 1, dropChance = 0.3f)
                )
            ),
            xpReward = 55,
            level = 7
        ),
        
        Enemy(
            id = "caterpillar",
            name = "Leaf Devourer",
            description = "An armored caterpillar with relentless appetite. Its silk binds prey before consuming.",
            maxHp = 70,
            strength = 7,
            agility = 4,
            vitality = 14,
            intelligence = 3,
            luck = 5,
            baseDamage = 7,
            defense = 6,
            behaviorType = EnemyBehaviorType.DEFENSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("silk", minQuantity = 2, maxQuantity = 4, dropChance = 0.9f),
                    LootDrop("leaf", minQuantity = 1, maxQuantity = 3, dropChance = 0.6f)
                )
            ),
            xpReward = 60,
            level = 8
        ),
        
        // ========== FOREST (Level 6-15) ==========
        
        Enemy(
            id = "snail",
            name = "Shell Fortress",
            description = "A colossal snail with an impenetrable shell. Leaves a trail of corrosive slime.",
            maxHp = 80,
            strength = 6,
            agility = 2,
            vitality = 16,
            intelligence = 4,
            luck = 3,
            baseDamage = 6,
            defense = 8,
            behaviorType = EnemyBehaviorType.DEFENSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("snail_shell", minQuantity = 1, maxQuantity = 1, dropChance = 0.9f),
                    LootDrop("slime", minQuantity = 1, maxQuantity = 2, dropChance = 0.7f),
                    LootDrop("salt", minQuantity = 1, maxQuantity = 1, dropChance = 0.3f)
                )
            ),
            xpReward = 65,
            level = 8
        ),
        
        Enemy(
            id = "mantis",
            name = "Praying Hunter",
            description = "A praying mantis with scythe-like forearms. Waits motionless before striking with lethal precision.",
            maxHp = 60,
            strength = 14,
            agility = 12,
            vitality = 8,
            intelligence = 9,
            luck = 8,
            baseDamage = 12,
            defense = 4,
            behaviorType = EnemyBehaviorType.DEFENSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("mantis_claw", minQuantity = 1, maxQuantity = 2, dropChance = 0.7f),
                    LootDrop("chitin", minQuantity = 1, maxQuantity = 2, dropChance = 0.5f)
                )
            ),
            xpReward = 70,
            level = 9
        ),
        
        Enemy(
            id = "stick_insect",
            name = "Living Twig",
            description = "A master of camouflage that blends perfectly with branches. Ambushes the unwary.",
            maxHp = 45,
            strength = 8,
            agility = 10,
            vitality = 7,
            intelligence = 11,
            luck = 13,
            baseDamage = 9,
            defense = 3,
            behaviorType = EnemyBehaviorType.DEFENSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("twig", minQuantity = 2, maxQuantity = 5, dropChance = 0.9f),
                    LootDrop("bark", minQuantity = 1, maxQuantity = 2, dropChance = 0.5f)
                )
            ),
            xpReward = 65,
            level = 9
        ),
        
        Enemy(
            id = "slug",
            name = "Slime Trail",
            description = "A giant slug that leaves hazardous mucus everywhere. Slow but incredibly resilient.",
            maxHp = 90,
            strength = 5,
            agility = 1,
            vitality = 18,
            intelligence = 3,
            luck = 2,
            baseDamage = 5,
            defense = 7,
            behaviorType = EnemyBehaviorType.DEFENSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("slime", minQuantity = 3, maxQuantity = 6, dropChance = 0.95f),
                    LootDrop("salt", minQuantity = 1, maxQuantity = 1, dropChance = 0.2f)
                )
            ),
            xpReward = 75,
            level = 10
        ),
        
        Enemy(
            id = "stag_beetle",
            name = "Antler Crusher",
            description = "A magnificent stag beetle with massive mandibles. Its crushing grip can shatter armor.",
            maxHp = 95,
            strength = 16,
            agility = 7,
            vitality = 14,
            intelligence = 5,
            luck = 6,
            baseDamage = 14,
            defense = 7,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("beetle_shell", minQuantity = 2, maxQuantity = 3, dropChance = 0.9f),
                    LootDrop("stag_horn", minQuantity = 1, maxQuantity = 2, dropChance = 0.7f),
                    LootDrop("chitin", minQuantity = 1, maxQuantity = 3, dropChance = 0.6f)
                )
            ),
            xpReward = 85,
            level = 12
        ),
        
        Enemy(
            id = "hornet",
            name = "Venom Fury",
            description = "An enraged hornet with a relentless stinger. Its venom burns like liquid fire.",
            maxHp = 70,
            strength = 13,
            agility = 16,
            vitality = 9,
            intelligence = 8,
            luck = 9,
            baseDamage = 13,
            defense = 4,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("wasp_stinger", minQuantity = 1, maxQuantity = 2, dropChance = 0.8f),
                    LootDrop("venom_sac", minQuantity = 1, maxQuantity = 1, dropChance = 0.5f),
                    LootDrop("honey_drop", minQuantity = 1, maxQuantity = 1, dropChance = 0.3f)
                )
            ),
            xpReward = 95,
            level = 13
        ),
        
        Enemy(
            id = "earthworm",
            name = "Tunneling Maw",
            description = "A colossal earthworm that burrows through soil. Emerges without warning to swallow prey whole.",
            maxHp = 110,
            strength = 12,
            agility = 5,
            vitality = 20,
            intelligence = 2,
            luck = 4,
            baseDamage = 11,
            defense = 6,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("worm_segment", minQuantity = 2, maxQuantity = 5, dropChance = 0.9f),
                    LootDrop("rich_soil", minQuantity = 1, maxQuantity = 3, dropChance = 0.7f),
                    LootDrop("mud", minQuantity = 1, maxQuantity = 2, dropChance = 0.5f)
                )
            ),
            xpReward = 100,
            level = 14
        ),
        
        // ========== SWAMP (Level 12-20) ==========
        
        Enemy(
            id = "mosquito",
            name = "Blood Cloud",
            description = "A swarm of mosquitoes that drains life force. Their incessant buzzing drives victims mad.",
            maxHp = 65,
            strength = 10,
            agility = 17,
            vitality = 7,
            intelligence = 6,
            luck = 10,
            baseDamage = 10,
            defense = 2,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("blood_drop", minQuantity = 1, maxQuantity = 3, dropChance = 0.7f),
                    LootDrop("proboscis", minQuantity = 1, maxQuantity = 1, dropChance = 0.4f)
                )
            ),
            xpReward = 90,
            level = 12
        ),
        
        Enemy(
            id = "leech",
            name = "Creeping Drainer",
            description = "A bloated leech that latches onto victims. Drains stamina and health with relentless persistence.",
            maxHp = 85,
            strength = 9,
            agility = 6,
            vitality = 15,
            intelligence = 4,
            luck = 5,
            baseDamage = 9,
            defense = 5,
            behaviorType = EnemyBehaviorType.DEFENSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("leech_extract", minQuantity = 1, maxQuantity = 2, dropChance = 0.8f),
                    LootDrop("blood_drop", minQuantity = 2, maxQuantity = 4, dropChance = 0.6f)
                )
            ),
            xpReward = 95,
            level = 13
        ),
        
        Enemy(
            id = "swamp_toad",
            name = "Croaking Behemoth",
            description = "A massive toad with toxic skin. Its tongue lashes out to capture prey in an instant.",
            maxHp = 140,
            strength = 15,
            agility = 8,
            vitality = 22,
            intelligence = 5,
            luck = 6,
            baseDamage = 16,
            defense = 8,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("toad_skin", minQuantity = 1, maxQuantity = 2, dropChance = 0.9f),
                    LootDrop("poison_gland", minQuantity = 1, maxQuantity = 1, dropChance = 0.6f),
                    LootDrop("slime", minQuantity = 2, maxQuantity = 3, dropChance = 0.7f)
                )
            ),
            xpReward = 120,
            level = 16
        ),
        
        Enemy(
            id = "swamp_gas",
            name = "Miasma Wraith",
            description = "A sentient cloud of toxic swamp gas. Burns lungs and corrodes equipment.",
            maxHp = 75,
            strength = 8,
            agility = 14,
            vitality = 10,
            intelligence = 12,
            luck = 15,
            baseDamage = 14,
            defense = 3,
            behaviorType = EnemyBehaviorType.FLEEING,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("sulfur", minQuantity = 2, maxQuantity = 4, dropChance = 0.8f),
                    LootDrop("swamp_essence", minQuantity = 1, maxQuantity = 1, dropChance = 0.5f)
                )
            ),
            xpReward = 110,
            level = 17
        ),
        
        Enemy(
            id = "bog_horror",
            name = "Rotting Abomination",
            description = "A nightmare creature born from swamp decay. Its touch spreads disease and corruption.",
            maxHp = 160,
            strength = 18,
            agility = 9,
            vitality = 24,
            intelligence = 8,
            luck = 7,
            baseDamage = 18,
            defense = 9,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("cursed_bone", minQuantity = 1, maxQuantity = 2, dropChance = 0.7f),
                    LootDrop("rot_essence", minQuantity = 1, maxQuantity = 1, dropChance = 0.5f),
                    LootDrop("swamp_moss", minQuantity = 2, maxQuantity = 4, dropChance = 0.6f)
                )
            ),
            xpReward = 140,
            level = 20
        ),
        
        // ========== MOUNTAIN (Level 15-25) ==========
        
        Enemy(
            id = "scorpion",
            name = "Stinger Sentinel",
            description = "A desert scorpion adapted to mountain rocks. Its tail delivers paralyzing neurotoxin.",
            maxHp = 120,
            strength = 16,
            agility = 13,
            vitality = 18,
            intelligence = 7,
            luck = 8,
            baseDamage = 17,
            defense = 7,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("scorpion_stinger", minQuantity = 1, maxQuantity = 1, dropChance = 0.8f),
                    LootDrop("venom_sac", minQuantity = 1, maxQuantity = 2, dropChance = 0.7f),
                    LootDrop("chitin", minQuantity = 2, maxQuantity = 3, dropChance = 0.6f)
                )
            ),
            xpReward = 130,
            level = 17
        ),
        
        Enemy(
            id = "mountain_hawk",
            name = "Sky Tyrant",
            description = "A massive hawk that rules the mountain skies. Its talons can pierce steel.",
            maxHp = 100,
            strength = 19,
            agility = 22,
            vitality = 14,
            intelligence = 11,
            luck = 14,
            baseDamage = 20,
            defense = 5,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("hawk_talon", minQuantity = 1, maxQuantity = 2, dropChance = 0.8f),
                    LootDrop("feather", minQuantity = 3, maxQuantity = 6, dropChance = 0.9f),
                    LootDrop("bird_beak", minQuantity = 1, maxQuantity = 1, dropChance = 0.5f)
                )
            ),
            xpReward = 150,
            level = 19
        ),
        
        Enemy(
            id = "mountain_goat",
            name = "Cliff Charger",
            description = "A fearless goat with crushing horns. Charges without hesitation, knocking foes off cliffs.",
            maxHp = 180,
            strength = 20,
            agility = 11,
            vitality = 25,
            intelligence = 6,
            luck = 7,
            baseDamage = 19,
            defense = 10,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("horn", minQuantity = 1, maxQuantity = 2, dropChance = 0.9f),
                    LootDrop("wool", minQuantity = 2, maxQuantity = 4, dropChance = 0.8f),
                    LootDrop("meat", minQuantity = 1, maxQuantity = 2, dropChance = 0.6f)
                )
            ),
            xpReward = 160,
            level = 21
        ),
        
        Enemy(
            id = "cave_cricket",
            name = "Echo Stalker",
            description = "A blind cave cricket that hunts by sound. Its chirps disorient and confuse prey.",
            maxHp = 95,
            strength = 14,
            agility = 19,
            vitality = 12,
            intelligence = 13,
            luck = 16,
            baseDamage = 16,
            defense = 4,
            behaviorType = EnemyBehaviorType.DEFENSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("cricket_leg", minQuantity = 2, maxQuantity = 4, dropChance = 0.8f),
                    LootDrop("echo_crystal", minQuantity = 1, maxQuantity = 1, dropChance = 0.4f)
                )
            ),
            xpReward = 145,
            level = 22
        ),
        
        Enemy(
            id = "rock_beetle",
            name = "Stone Titan",
            description = "A gargantuan beetle with a shell like granite. Nearly invincible but slow to react.",
            maxHp = 250,
            strength = 22,
            agility = 5,
            vitality = 30,
            intelligence = 4,
            luck = 5,
            baseDamage = 21,
            defense = 15,
            behaviorType = EnemyBehaviorType.DEFENSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("stone_shell", minQuantity = 1, maxQuantity = 2, dropChance = 0.9f),
                    LootDrop("granite_chunk", minQuantity = 2, maxQuantity = 4, dropChance = 0.8f),
                    LootDrop("obsidian", minQuantity = 1, maxQuantity = 1, dropChance = 0.3f)
                )
            ),
            xpReward = 180,
            level = 25
        ),
        
        // ========== DESERT (Level 20-30) ==========
        
        Enemy(
            id = "ant_lion",
            name = "Pit Lurker",
            description = "An ant lion that creates deadly sand traps. Drags victims into its crushing mandibles.",
            maxHp = 150,
            strength = 18,
            agility = 10,
            vitality = 20,
            intelligence = 9,
            luck = 8,
            baseDamage = 18,
            defense = 8,
            behaviorType = EnemyBehaviorType.DEFENSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("ant_mandible", minQuantity = 1, maxQuantity = 2, dropChance = 0.8f),
                    LootDrop("sand", minQuantity = 3, maxQuantity = 6, dropChance = 0.9f),
                    LootDrop("chitin", minQuantity = 2, maxQuantity = 3, dropChance = 0.6f)
                )
            ),
            xpReward = 170,
            level = 22
        ),
        
        Enemy(
            id = "desert_lizard",
            name = "Sand Sprinter",
            description = "A lightning-fast lizard with razor-sharp claws. Blends into dunes before striking.",
            maxHp = 130,
            strength = 17,
            agility = 21,
            vitality = 16,
            intelligence = 10,
            luck = 12,
            baseDamage = 19,
            defense = 6,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("lizard_scale", minQuantity = 2, maxQuantity = 4, dropChance = 0.9f),
                    LootDrop("claw", minQuantity = 1, maxQuantity = 2, dropChance = 0.7f),
                    LootDrop("tail", minQuantity = 1, maxQuantity = 1, dropChance = 0.4f)
                )
            ),
            xpReward = 190,
            level = 24
        ),
        
        Enemy(
            id = "tarantula",
            name = "Silk Nightmare",
            description = "A colossal tarantula with fangs like daggers. Its venom induces terrifying hallucinations.",
            maxHp = 170,
            strength = 20,
            agility = 15,
            vitality = 22,
            intelligence = 12,
            luck = 10,
            baseDamage = 22,
            defense = 9,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("spider_fang", minQuantity = 1, maxQuantity = 2, dropChance = 0.8f),
                    LootDrop("spider_silk", minQuantity = 3, maxQuantity = 6, dropChance = 0.9f),
                    LootDrop("venom_sac", minQuantity = 1, maxQuantity = 2, dropChance = 0.7f)
                )
            ),
            xpReward = 210,
            level = 27
        ),
        
        Enemy(
            id = "heat_mirage",
            name = "Shimmer Phantom",
            description = "A sentient heat mirage that confuses and burns. Reality bends in its presence.",
            maxHp = 110,
            strength = 15,
            agility = 24,
            vitality = 14,
            intelligence = 18,
            luck = 20,
            baseDamage = 20,
            defense = 4,
            behaviorType = EnemyBehaviorType.RANDOM,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("heat_essence", minQuantity = 1, maxQuantity = 2, dropChance = 0.7f),
                    LootDrop("mirage_crystal", minQuantity = 1, maxQuantity = 1, dropChance = 0.4f),
                    LootDrop("star_dust", minQuantity = 1, maxQuantity = 1, dropChance = 0.3f)
                )
            ),
            xpReward = 220,
            level = 28
        ),
        
        Enemy(
            id = "cactus_guardian",
            name = "Thorn Colossus",
            description = "A living cactus animated by desert magic. Its spines pierce armor like paper.",
            maxHp = 280,
            strength = 24,
            agility = 7,
            vitality = 32,
            intelligence = 8,
            luck = 6,
            baseDamage = 23,
            defense = 14,
            behaviorType = EnemyBehaviorType.DEFENSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("cactus_needle", minQuantity = 4, maxQuantity = 8, dropChance = 0.95f),
                    LootDrop("desert_bloom", minQuantity = 1, maxQuantity = 2, dropChance = 0.6f),
                    LootDrop("life_sap", minQuantity = 1, maxQuantity = 1, dropChance = 0.4f)
                )
            ),
            xpReward = 250,
            level = 30
        ),
        
        // ========== COASTAL (Level 25-35) ==========
        
        Enemy(
            id = "hermit_crab",
            name = "Shell Crusher",
            description = "A massive hermit crab with a fortress shell. Its pincers can shatter bone.",
            maxHp = 200,
            strength = 21,
            agility = 12,
            vitality = 26,
            intelligence = 9,
            luck = 8,
            baseDamage = 21,
            defense = 12,
            behaviorType = EnemyBehaviorType.DEFENSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("crab_claw", minQuantity = 1, maxQuantity = 2, dropChance = 0.8f),
                    LootDrop("shell", minQuantity = 1, maxQuantity = 1, dropChance = 0.9f),
                    LootDrop("pearl", minQuantity = 1, maxQuantity = 1, dropChance = 0.2f)
                )
            ),
            xpReward = 240,
            level = 28
        ),
        
        Enemy(
            id = "seagull",
            name = "Coastal Scavenger",
            description = "A cunning seagull with a vicious beak. Dive-bombs unsuspecting prey.",
            maxHp = 140,
            strength = 18,
            agility = 25,
            vitality = 18,
            intelligence = 14,
            luck = 16,
            baseDamage = 20,
            defense = 6,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("feather", minQuantity = 4, maxQuantity = 8, dropChance = 0.95f),
                    LootDrop("bird_beak", minQuantity = 1, maxQuantity = 1, dropChance = 0.7f)
                )
            ),
            xpReward = 260,
            level = 31
        ),
        
        Enemy(
            id = "jellyfish",
            name = "Drifting Agony",
            description = "A translucent jellyfish with paralyzing tentacles. Beautiful but deadly.",
            maxHp = 160,
            strength = 16,
            agility = 18,
            vitality = 20,
            intelligence = 15,
            luck = 18,
            baseDamage = 24,
            defense = 5,
            behaviorType = EnemyBehaviorType.DEFENSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("jellyfish_tentacle", minQuantity = 2, maxQuantity = 4, dropChance = 0.9f),
                    LootDrop("bioluminescent_gel", minQuantity = 1, maxQuantity = 2, dropChance = 0.7f),
                    LootDrop("water_essence", minQuantity = 1, maxQuantity = 1, dropChance = 0.5f)
                )
            ),
            xpReward = 280,
            level = 33
        ),
        
        // ========== CAVE/TUNDRA/ENDGAME (Level 30-40) ==========
        
        Enemy(
            id = "cave_bat",
            name = "Echo Hunter",
            description = "A massive bat with ultrasonic screech. Blinds prey before draining their blood.",
            maxHp = 180,
            strength = 19,
            agility = 26,
            vitality = 22,
            intelligence = 16,
            luck = 17,
            baseDamage = 23,
            defense = 7,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("bat_wing", minQuantity = 1, maxQuantity = 2, dropChance = 0.9f),
                    LootDrop("blood_drop", minQuantity = 2, maxQuantity = 4, dropChance = 0.8f),
                    LootDrop("echo_crystal", minQuantity = 1, maxQuantity = 1, dropChance = 0.5f)
                )
            ),
            xpReward = 300,
            level = 35
        ),
        
        Enemy(
            id = "frost_moth",
            name = "Winter's Whisper",
            description = "A crystalline moth from frozen wastelands. Its wings spread deadly frost.",
            maxHp = 220,
            strength = 22,
            agility = 20,
            vitality = 28,
            intelligence = 20,
            luck = 22,
            baseDamage = 26,
            defense = 10,
            behaviorType = EnemyBehaviorType.FLEEING,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("frost_wing", minQuantity = 1, maxQuantity = 2, dropChance = 0.85f),
                    LootDrop("ice_crystal", minQuantity = 2, maxQuantity = 4, dropChance = 0.8f),
                    LootDrop("frozen_essence", minQuantity = 1, maxQuantity = 1, dropChance = 0.6f)
                )
            ),
            xpReward = 350,
            level = 38
        ),
        
        Enemy(
            id = "shadow_lurker",
            name = "Void Stalker",
            description = "A nightmare creature born from pure darkness. Consumes light and hope.",
            maxHp = 300,
            strength = 28,
            agility = 22,
            vitality = 35,
            intelligence = 24,
            luck = 20,
            baseDamage = 30,
            defense = 13,
            behaviorType = EnemyBehaviorType.AGGRESSIVE,
            lootTable = LootTable(
                drops = listOf(
                    LootDrop("shadow_essence", minQuantity = 1, maxQuantity = 2, dropChance = 0.9f),
                    LootDrop("void_crystal", minQuantity = 1, maxQuantity = 1, dropChance = 0.6f),
                    LootDrop("cursed_bone", minQuantity = 2, maxQuantity = 3, dropChance = 0.7f)
                )
            ),
            xpReward = 400,
            level = 40
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
