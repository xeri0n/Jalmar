package com.jalmarquest.shared.companion

import com.jalmarquest.shared.npc.NPCPersonality
import com.jalmarquest.shared.npc.NPCSpecies
import com.jalmarquest.shared.skills.SkillEffect

/**
 * Catalog of all available companions in JalmarQuest.
 * Each companion has unique abilities, personality, and recruitment requirements.
 * Companions are based on the quail-scale world where mundane creatures become epic allies.
 */
object CompanionCatalog {
    
    /**
     * Pip - The Young Quail (Tutorial Companion)
     * 
     * A young button quail who looks up to Jalmar. Pip is eager but inexperienced,
     * representing the player's first companion and introduction to the system.
     * Recruitment: Complete "First Flight" quest in Buttonburgh.
     */
    val PIP = Companion(
        id = "pip_young_quail",
        name = "Pip",
        species = NPCSpecies.BUTTON_QUAIL,
        personality = NPCPersonality(
            friendliness = 9,
            courage = 5,
            wisdom = 4,
            humor = 7,
            traits = listOf("eager", "optimistic", "inexperienced")
        ),
        backstory = "A young button quail who hatched in Buttonburgh and dreams of adventure. " +
                "Pip sees Jalmar as a mentor and hero, despite being only slightly older.",
        recruitmentQuestId = "quest_first_flight",
        maxHp = 40,
        strength = 8,
        agility = 12,
        vitality = 10,
        intelligence = 7,
        luck = 10,
        combatBehavior = CompanionBehavior.SUPPORTIVE,
        defaultDialogueTreeId = "dialogue_pip",
        abilities = listOf(
            CompanionAbility(
                id = "pips_courage",
                name = "Pip's Courage",
                description = "Pip's optimism grants a flee bonus when things look dire.",
                loyaltyRequired = 0,
                effects = listOf(SkillEffect.FleeBonus(successBonus = 0.15f)),
                cooldownRounds = 5
            ),
            CompanionAbility(
                id = "cheerful_chirp",
                name = "Cheerful Chirp",
                description = "Pip's encouraging chirp heals a small amount to all allies.",
                loyaltyRequired = 50,
                effects = listOf(SkillEffect.AoEHeal(baseHealing = 8)),
                cooldownRounds = 4
            ),
            CompanionAbility(
                id = "fledgling_fury",
                name = "Fledgling Fury",
                description = "Pip's determination grants a temporary attack boost to self.",
                loyaltyRequired = 100,
                effects = listOf(SkillEffect.BuffAttack(attackBonus = 6, duration = 3)),
                cooldownRounds = 6
            )
        ),
        favoriteItems = listOf("item_seeds", "item_fresh_water", "item_soft_feather"),
        dislikedItems = listOf("item_sharp_thorn", "item_scary_mask")
    )
    
    /**
     * Grumble Forgepaw - The Mole Craftsman
     * 
     * A skilled mole blacksmith from The Quailsmith in Buttonburgh. Grumpy but loyal,
     * Grumble is a defensive powerhouse who can enhance equipment.
     * Recruitment: Complete "The Mole's Trust" quest after crafting 3 items.
     */
    val GRUMBLE = Companion(
        id = "grumble_forgepaw",
        name = "Grumble Forgepaw",
        species = NPCSpecies.MOLE,
        personality = NPCPersonality(
            friendliness = 3,
            courage = 8,
            wisdom = 9,
            humor = 2,
            traits = listOf("grumpy", "reliable", "perfectionist", "skilled")
        ),
        backstory = "The master craftsman of Buttonburgh's Quailsmith. Grumble initially " +
                "distrusts adventurers but respects those who value quality craftsmanship.",
        recruitmentQuestId = "quest_moles_trust",
        maxHp = 65,
        strength = 14,
        agility = 6,
        vitality = 16,
        intelligence = 12,
        luck = 5,
        combatBehavior = CompanionBehavior.DEFENSIVE,
        defaultDialogueTreeId = "dialogue_grumble",
        abilities = listOf(
            CompanionAbility(
                id = "forged_resilience",
                name = "Forged Resilience",
                description = "Grumble's craftsmanship grants damage reduction to an ally.",
                loyaltyRequired = 0,
                effects = listOf(SkillEffect.DamageReduction(reductionPercent = 0.3f, duration = 3)),
                cooldownRounds = 5
            ),
            CompanionAbility(
                id = "forge_blessing",
                name = "Forge Blessing",
                description = "Enhances an ally's weapon, increasing attack temporarily.",
                loyaltyRequired = 50,
                effects = listOf(SkillEffect.BuffAttack(attackBonus = 10, duration = 4)),
                cooldownRounds = 6
            ),
            CompanionAbility(
                id = "moles_endurance",
                name = "Mole's Endurance",
                description = "Grumble's legendary toughness grants massive defense boost.",
                loyaltyRequired = 75,
                effects = listOf(SkillEffect.BuffDefense(defenseBonus = 15, duration = 5)),
                cooldownRounds = 8
            ),
            CompanionAbility(
                id = "master_crafters_wrath",
                name = "Master Crafter's Wrath",
                description = "Grumble unleashes a powerful hammer strike, ignoring defense.",
                loyaltyRequired = 100,
                effects = listOf(
                    SkillEffect.Damage(baseDamage = 30, statScaling = 0.8f),
                    SkillEffect.IgnoreDefense
                ),
                cooldownRounds = 7
            )
        ),
        favoriteItems = listOf("item_ore", "item_fine_tool", "item_quality_metal"),
        dislikedItems = listOf("item_shoddy_item", "item_rust", "item_broken_tool")
    )
    
    /**
     * Whisker - The Mouse Explorer
     * 
     * A curious mouse cartographer mapping the quail-scale world. Fast and agile,
     * Whisker excels at reconnaissance and quick strikes.
     * Recruitment: Rescue from "Lost in the Garden Maze" quest.
     */
    val WHISKER = Companion(
        id = "whisker_explorer",
        name = "Whisker",
        species = NPCSpecies.MOUSE,
        personality = NPCPersonality(
            friendliness = 7,
            courage = 6,
            wisdom = 8,
            humor = 8,
            traits = listOf("curious", "clever", "cautious", "knowledgeable")
        ),
        backstory = "A mouse explorer dedicated to mapping every corner of the garden. " +
                "Whisker's maps have saved many lives, though they nearly cost their own.",
        recruitmentQuestId = "quest_lost_in_maze",
        maxHp = 45,
        strength = 9,
        agility = 16,
        vitality = 11,
        intelligence = 14,
        luck = 12,
        combatBehavior = CompanionBehavior.AGGRESSIVE,
        defaultDialogueTreeId = "dialogue_whisker",
        abilities = listOf(
            CompanionAbility(
                id = "swift_strike",
                name = "Swift Strike",
                description = "Whisker's speed allows a rapid multi-hit attack.",
                loyaltyRequired = 0,
                effects = listOf(SkillEffect.MultiHit(hits = 3, damagePerHit = 6, statScaling = 0.2f)),
                cooldownRounds = 4
            ),
            CompanionAbility(
                id = "explorers_insight",
                name = "Explorer's Insight",
                description = "Whisker's knowledge grants a speed boost to all allies.",
                loyaltyRequired = 50,
                effects = listOf(SkillEffect.BuffSpeed(speedBonus = 8, duration = 3)),
                cooldownRounds = 5
            ),
            CompanionAbility(
                id = "cartographers_precision",
                name = "Cartographer's Precision",
                description = "Whisker strikes a vital point, guaranteeing a critical hit.",
                loyaltyRequired = 100,
                effects = listOf(
                    SkillEffect.Damage(baseDamage = 20, statScaling = 0.6f),
                    SkillEffect.GuaranteedCrit
                ),
                cooldownRounds = 6
            )
        ),
        favoriteItems = listOf("item_map_scrap", "item_compass", "item_cheese"),
        dislikedItems = listOf("item_trap", "item_cat_toy")
    )
    
    /**
     * Ember - The Firefly Guide
     * 
     * A mystical firefly who illuminates dark paths. Ember provides magical support
     * and healing, representing the wonder of the quail-scale world.
     * Recruitment: Complete "Light in the Darkness" quest in the Deep Garden.
     */
    val EMBER = Companion(
        id = "ember_firefly",
        name = "Ember",
        species = NPCSpecies.FIREFLY,
        personality = NPCPersonality(
            friendliness = 8,
            courage = 7,
            wisdom = 10,
            humor = 5,
            traits = listOf("mystical", "serene", "patient", "ancient")
        ),
        backstory = "An ancient firefly who has witnessed countless generations. " +
                "Ember's light has guided many lost travelers to safety.",
        recruitmentQuestId = "quest_light_darkness",
        maxHp = 35,
        strength = 5,
        agility = 14,
        vitality = 8,
        intelligence = 16,
        luck = 14,
        combatBehavior = CompanionBehavior.SUPPORTIVE,
        defaultDialogueTreeId = "dialogue_ember",
        abilities = listOf(
            CompanionAbility(
                id = "healing_glow",
                name = "Healing Glow",
                description = "Ember's gentle light restores health to an ally.",
                loyaltyRequired = 0,
                effects = listOf(SkillEffect.Heal(baseHealing = 15)),
                cooldownRounds = 3
            ),
            CompanionAbility(
                id = "protective_light",
                name = "Protective Light",
                description = "Ember's radiance grants defense to all allies.",
                loyaltyRequired = 50,
                effects = listOf(SkillEffect.BuffDefense(defenseBonus = 8, duration = 4)),
                cooldownRounds = 5
            ),
            CompanionAbility(
                id = "blinding_flash",
                name = "Blinding Flash",
                description = "Ember's intense flash reduces enemy attack power.",
                loyaltyRequired = 75,
                effects = listOf(SkillEffect.DebuffAttack(attackPenalty = 10, duration = 3)),
                cooldownRounds = 6
            ),
            CompanionAbility(
                id = "ancient_radiance",
                name = "Ancient Radiance",
                description = "Ember channels ancient magic, healing all allies significantly.",
                loyaltyRequired = 100,
                effects = listOf(SkillEffect.AoEHeal(baseHealing = 20)),
                cooldownRounds = 7
            )
        ),
        favoriteItems = listOf("item_nectar", "item_glowing_moss", "item_moonstone"),
        dislikedItems = listOf("item_jar", "item_darkness_shard")
    )
    
    /**
     * Skitter - The Beetle Warrior
     * 
     * A brave beetle warrior who defends the garden from threats. Heavily armored
     * and strong, Skitter is a frontline tank companion.
     * Recruitment: Complete "The Beetle's Honor" quest defending a village.
     */
    val SKITTER = Companion(
        id = "skitter_beetle",
        name = "Skitter",
        species = NPCSpecies.BEETLE,
        personality = NPCPersonality(
            friendliness = 6,
            courage = 10,
            wisdom = 6,
            humor = 4,
            traits = listOf("honorable", "protective", "disciplined", "brave")
        ),
        backstory = "A beetle warrior from the Garden Guard. Skitter has sworn an oath " +
                "to protect the innocent and uphold justice in the garden realm.",
        recruitmentQuestId = "quest_beetles_honor",
        maxHp = 70,
        strength = 15,
        agility = 8,
        vitality = 18,
        intelligence = 8,
        luck = 6,
        combatBehavior = CompanionBehavior.DEFENSIVE,
        defaultDialogueTreeId = "dialogue_skitter",
        abilities = listOf(
            CompanionAbility(
                id = "shell_shield",
                name = "Shell Shield",
                description = "Skitter's armored shell reflects damage back to attackers.",
                loyaltyRequired = 0,
                effects = listOf(SkillEffect.ReflectDamage(reflectPercent = 0.4f, duration = 3)),
                cooldownRounds = 5
            ),
            CompanionAbility(
                id = "beetle_charge",
                name = "Beetle Charge",
                description = "Skitter charges forward with a powerful strike.",
                loyaltyRequired = 50,
                effects = listOf(SkillEffect.Damage(baseDamage = 25, statScaling = 0.7f)),
                cooldownRounds = 4
            ),
            CompanionAbility(
                id = "armored_stance",
                name = "Armored Stance",
                description = "Skitter's legendary defense protects all allies.",
                loyaltyRequired = 100,
                effects = listOf(SkillEffect.BuffDefense(defenseBonus = 12, duration = 5)),
                cooldownRounds = 7
            )
        ),
        favoriteItems = listOf("item_polish", "item_armor_piece", "item_medal"),
        dislikedItems = listOf("item_insecticide", "item_dirt")
    )
    
    /**
     * Swoop - The Redeemed Sparrow
     * 
     * A former enemy turned ally. Once a scout for hostile sparrows, Swoop now
     * fights alongside Jalmar after being shown mercy. Aggressive aerial combatant.
     * Recruitment: Spare Swoop during "Aerial Ambush" quest, then complete "Redemption."
     */
    val SWOOP = Companion(
        id = "swoop_sparrow",
        name = "Swoop",
        species = NPCSpecies.SPARROW,
        personality = NPCPersonality(
            friendliness = 5,
            courage = 9,
            wisdom = 7,
            humor = 6,
            traits = listOf("conflicted", "fierce", "reformed", "loyal")
        ),
        backstory = "A sparrow scout who attacked Jalmar but was spared. This mercy " +
                "changed Swoop's worldview, leading them to abandon their hostile flock.",
        recruitmentQuestId = "quest_redemption",
        maxHp = 50,
        strength = 13,
        agility = 15,
        vitality = 12,
        intelligence = 10,
        luck = 11,
        combatBehavior = CompanionBehavior.AGGRESSIVE,
        defaultDialogueTreeId = "dialogue_swoop",
        abilities = listOf(
            CompanionAbility(
                id = "dive_attack",
                name = "Dive Attack",
                description = "Swoop dives from above, dealing heavy damage to one target.",
                loyaltyRequired = 0,
                effects = listOf(SkillEffect.Damage(baseDamage = 22, statScaling = 0.6f)),
                cooldownRounds = 4
            ),
            CompanionAbility(
                id = "aerial_strike",
                name = "Aerial Strike",
                description = "Swoop's sweeping attack hits all enemies from above.",
                loyaltyRequired = 50,
                effects = listOf(SkillEffect.AoEDamage(baseDamage = 15, statScaling = 0.4f)),
                cooldownRounds = 6
            ),
            CompanionAbility(
                id = "talons_of_redemption",
                name = "Talons of Redemption",
                description = "Swoop's guilt-fueled fury unleashes a devastating critical strike.",
                loyaltyRequired = 75,
                effects = listOf(
                    SkillEffect.Damage(baseDamage = 28, statScaling = 0.8f),
                    SkillEffect.GuaranteedCrit
                ),
                cooldownRounds = 7
            ),
            CompanionAbility(
                id = "sky_guardians_oath",
                name = "Sky Guardian's Oath",
                description = "Swoop vows protection, granting attack and defense to all allies.",
                loyaltyRequired = 100,
                effects = listOf(
                    SkillEffect.BuffAttack(attackBonus = 7, duration = 4),
                    SkillEffect.BuffDefense(defenseBonus = 7, duration = 4)
                ),
                cooldownRounds = 8
            )
        ),
        favoriteItems = listOf("item_feather", "item_nest_material", "item_forgiveness_token"),
        dislikedItems = listOf("item_cage", "item_net", "item_reminder_of_past")
    )
    
    /**
     * Shimmer - The Dew Drop Spirit
     * 
     * A mystical entity born from morning dew. Shimmer represents the magical
     * side of the garden, providing healing and elemental support.
     * Recruitment: Complete "Morning's Gift" quest during a specific season.
     */
    val SHIMMER = Companion(
        id = "shimmer_dew_spirit",
        name = "Shimmer",
        species = NPCSpecies.FIREFLY, // Using FIREFLY as closest mystical species
        personality = NPCPersonality(
            friendliness = 9,
            courage = 5,
            wisdom = 9,
            humor = 8,
            traits = listOf("playful", "ephemeral", "pure", "innocent")
        ),
        backstory = "A spirit of morning dew who forms only when conditions are perfect. " +
                "Shimmer is playful and innocent, seeing the world with childlike wonder.",
        recruitmentQuestId = "quest_mornings_gift",
        maxHp = 38,
        strength = 6,
        agility = 13,
        vitality = 9,
        intelligence = 15,
        luck = 16,
        combatBehavior = CompanionBehavior.SUPPORTIVE,
        defaultDialogueTreeId = "dialogue_shimmer",
        abilities = listOf(
            CompanionAbility(
                id = "dew_refresh",
                name = "Dew Refresh",
                description = "Shimmer's essence restores health and removes status effects.",
                loyaltyRequired = 0,
                effects = listOf(SkillEffect.Heal(baseHealing = 12)),
                cooldownRounds = 3
            ),
            CompanionAbility(
                id = "morning_blessing",
                name = "Morning Blessing",
                description = "Shimmer's purity grants temporary attack boost to allies.",
                loyaltyRequired = 50,
                effects = listOf(SkillEffect.BuffAttack(attackBonus = 8, duration = 3)),
                cooldownRounds = 5
            ),
            CompanionAbility(
                id = "crystal_cascade",
                name = "Crystal Cascade",
                description = "Shimmer unleashes water droplets, healing all allies.",
                loyaltyRequired = 100,
                effects = listOf(SkillEffect.AoEHeal(baseHealing = 18)),
                cooldownRounds = 6
            )
        ),
        favoriteItems = listOf("item_morning_dew", "item_flower_petal", "item_rainbow_shard"),
        dislikedItems = listOf("item_salt", "item_heat_stone")
    )
    
    /**
     * Thorn - The Hedgehog Mercenary
     * 
     * A gruff hedgehog mercenary who fights for payment but develops genuine loyalty.
     * Balanced offensive and defensive capabilities with a rough exterior.
     * Recruitment: Pay to recruit, then complete "More Than Gold" quest for true loyalty.
     */
    val THORN = Companion(
        id = "thorn_hedgehog",
        name = "Thorn",
        species = NPCSpecies.BEETLE, // Using BEETLE as closest armored species
        personality = NPCPersonality(
            friendliness = 4,
            courage = 8,
            wisdom = 7,
            humor = 5,
            traits = listOf("mercenary", "pragmatic", "secretly kind", "professional")
        ),
        backstory = "A battle-scarred hedgehog who claims to only fight for coin. " +
                "Despite the tough exterior, Thorn has a code of honor and hidden compassion.",
        recruitmentQuestId = "quest_more_than_gold",
        maxHp = 60,
        strength = 13,
        agility = 10,
        vitality = 15,
        intelligence = 11,
        luck = 8,
        combatBehavior = CompanionBehavior.AGGRESSIVE,
        defaultDialogueTreeId = "dialogue_thorn",
        abilities = listOf(
            CompanionAbility(
                id = "spike_defense",
                name = "Spike Defense",
                description = "Thorn's spines reflect a portion of damage back to attackers.",
                loyaltyRequired = 0,
                effects = listOf(SkillEffect.ReflectDamage(reflectPercent = 0.35f, duration = 3)),
                cooldownRounds = 4
            ),
            CompanionAbility(
                id = "mercenary_strike",
                name = "Mercenary Strike",
                description = "Thorn's professional precision deals reliable damage.",
                loyaltyRequired = 50,
                effects = listOf(SkillEffect.Damage(baseDamage = 20, statScaling = 0.65f)),
                cooldownRounds = 4
            ),
            CompanionAbility(
                id = "spine_barrage",
                name = "Spine Barrage",
                description = "Thorn launches spines at all enemies, hitting multiple times.",
                loyaltyRequired = 75,
                effects = listOf(SkillEffect.MultiHit(hits = 4, damagePerHit = 8, statScaling = 0.3f)),
                cooldownRounds = 6
            ),
            CompanionAbility(
                id = "warriors_respect",
                name = "Warrior's Respect",
                description = "Thorn's earned respect grants attack and defense to all allies.",
                loyaltyRequired = 100,
                effects = listOf(
                    SkillEffect.BuffAttack(attackBonus = 9, duration = 4),
                    SkillEffect.BuffDefense(defenseBonus = 9, duration = 4)
                ),
                cooldownRounds = 7
            )
        ),
        favoriteItems = listOf("item_gold_coin", "item_quality_weapon", "item_respect_token"),
        dislikedItems = listOf("item_cheap_trinket", "item_charity", "item_pity")
    )
    
    /**
     * Clover - The Lucky Ladybug
     * 
     * An incredibly lucky ladybug who brings fortune to allies. Clover's abilities
     * focus on luck manipulation and surprising outcomes.
     * Recruitment: Random encounter, complete "Four-Leaf Fortune" quest.
     */
    val CLOVER = Companion(
        id = "clover_ladybug",
        name = "Clover",
        species = NPCSpecies.BEETLE,
        personality = NPCPersonality(
            friendliness = 10,
            courage = 6,
            wisdom = 6,
            humor = 9,
            traits = listOf("cheerful", "carefree", "lucky", "optimistic")
        ),
        backstory = "A ladybug with improbable luck who always seems to be in the right " +
                "place at the right time. Clover believes everything happens for a reason.",
        recruitmentQuestId = "quest_four_leaf_fortune",
        maxHp = 42,
        strength = 9,
        agility = 12,
        vitality = 10,
        intelligence = 11,
        luck = 18,  // Exceptionally high luck
        combatBehavior = CompanionBehavior.SUPPORTIVE,
        defaultDialogueTreeId = "dialogue_clover",
        abilities = listOf(
            CompanionAbility(
                id = "lucky_charm",
                name = "Lucky Charm",
                description = "Clover's presence increases critical hit chance for allies.",
                loyaltyRequired = 0,
                effects = listOf(SkillEffect.BuffAttack(attackBonus = 5, duration = 4)),
                cooldownRounds = 5
            ),
            CompanionAbility(
                id = "fortunate_strike",
                name = "Fortunate Strike",
                description = "Clover's attack always finds a weak point, guaranteeing a crit.",
                loyaltyRequired = 50,
                effects = listOf(
                    SkillEffect.Damage(baseDamage = 18, statScaling = 0.5f),
                    SkillEffect.GuaranteedCrit
                ),
                cooldownRounds = 5
            ),
            CompanionAbility(
                id = "serendipity",
                name = "Serendipity",
                description = "Clover's incredible luck heals allies and boosts their speed.",
                loyaltyRequired = 100,
                effects = listOf(
                    SkillEffect.AoEHeal(baseHealing = 12),
                    SkillEffect.BuffSpeed(speedBonus = 10, duration = 3)
                ),
                cooldownRounds = 6
            )
        ),
        favoriteItems = listOf("item_four_leaf_clover", "item_shiny_object", "item_lucky_coin"),
        dislikedItems = listOf("item_black_cat_fur", "item_broken_mirror", "item_unlucky_charm")
    )
    
    /**
     * Rumble - The Toad Sage
     * 
     * An ancient toad who has mastered the garden's secrets. Rumble provides
     * wisdom, powerful magic, and strategic support.
     * Recruitment: Complete "The Sage's Test" quest proving worthiness.
     */
    val RUMBLE = Companion(
        id = "rumble_toad_sage",
        name = "Rumble",
        species = NPCSpecies.BEETLE, // Using BEETLE as placeholder for toad
        personality = NPCPersonality(
            friendliness = 6,
            courage = 7,
            wisdom = 10,
            humor = 3,
            traits = listOf("ancient", "wise", "cryptic", "powerful", "patient")
        ),
        backstory = "An ancient toad sage who has lived for countless seasons. " +
                "Rumble speaks in riddles but possesses vast knowledge of the garden's magic.",
        recruitmentQuestId = "quest_sages_test",
        maxHp = 55,
        strength = 11,
        agility = 7,
        vitality = 14,
        intelligence = 18,
        luck = 13,
        combatBehavior = CompanionBehavior.SUPPORTIVE,
        defaultDialogueTreeId = "dialogue_rumble",
        abilities = listOf(
            CompanionAbility(
                id = "sages_wisdom",
                name = "Sage's Wisdom",
                description = "Rumble's knowledge grants intelligence boost to allies.",
                loyaltyRequired = 0,
                effects = listOf(SkillEffect.BuffAttack(attackBonus = 6, duration = 4)),
                cooldownRounds = 5
            ),
            CompanionAbility(
                id = "toads_tongue",
                name = "Toad's Tongue",
                description = "Rumble's long tongue strike reduces enemy attack power.",
                loyaltyRequired = 50,
                effects = listOf(SkillEffect.DebuffAttack(attackPenalty = 12, duration = 4)),
                cooldownRounds = 5
            ),
            CompanionAbility(
                id = "ancient_barrier",
                name = "Ancient Barrier",
                description = "Rumble creates a mystical barrier, granting massive damage reduction.",
                loyaltyRequired = 75,
                effects = listOf(SkillEffect.DamageReduction(reductionPercent = 0.5f, duration = 4)),
                cooldownRounds = 7
            ),
            CompanionAbility(
                id = "gardens_blessing",
                name = "Garden's Blessing",
                description = "Rumble channels the garden's energy, healing and buffing all allies.",
                loyaltyRequired = 100,
                effects = listOf(
                    SkillEffect.AoEHeal(baseHealing = 15),
                    SkillEffect.BuffAttack(attackBonus = 8, duration = 4),
                    SkillEffect.BuffDefense(defenseBonus = 8, duration = 4)
                ),
                cooldownRounds = 10
            )
        ),
        favoriteItems = listOf("item_ancient_scroll", "item_meditation_stone", "item_sacred_herb"),
        dislikedItems = listOf("item_loud_noise", "item_disrespect", "item_poison")
    )
    
    /**
     * All available companions in the game.
     */
    val ALL_COMPANIONS = listOf(
        PIP, GRUMBLE, WHISKER, EMBER, SKITTER,
        SWOOP, SHIMMER, THORN, CLOVER, RUMBLE
    )
    
    /**
     * Get a companion by ID.
     */
    fun getCompanionById(id: String): Companion? {
        return ALL_COMPANIONS.find { it.id == id }
    }
    
    /**
     * Get companions by species.
     */
    fun getCompanionsBySpecies(species: NPCSpecies): List<Companion> {
        return ALL_COMPANIONS.filter { it.species == species }
    }
    
    /**
     * Get companions by combat behavior.
     */
    fun getCompanionsByBehavior(behavior: CompanionBehavior): List<Companion> {
        return ALL_COMPANIONS.filter { it.combatBehavior == behavior }
    }
    
    /**
     * Get companions that can be recruited via a specific quest.
     */
    fun getCompanionByRecruitmentQuest(questId: String): Companion? {
        return ALL_COMPANIONS.find { it.recruitmentQuestId == questId }
    }
}
