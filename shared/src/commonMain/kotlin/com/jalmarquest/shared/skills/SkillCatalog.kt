package com.jalmarquest.shared.skills

import com.jalmarquest.shared.combat.StatusEffectType

/**
 * Catalog of all 57 learnable skills across 3 archetypes.
 * Skills are organized by archetype and tier for progression.
 */
object SkillCatalog {
    
    // ==================== FIGHTER ARCHETYPE (19 skills) ====================
    
    // FIGHTER TIER 1 (5 skills)
    private val fighterTier1 = listOf(
        Skill(
            id = "fighter_twig_strike",
            name = "Twig Strike",
            description = "A basic melee attack with your twig weapon, dealing moderate damage.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_1,
            effects = listOf(SkillEffect.Damage(baseDamage = 10, statScaling = 0.5f)),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "fighter_power_stance",
            name = "Power Stance",
            description = "Increase your attack power for 3 turns, enhancing physical damage.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_1,
            effects = listOf(SkillEffect.BuffAttack(attackBonus = 2, duration = 3)),
            targetType = SkillTargetType.SELF
        ),
        Skill(
            id = "fighter_pebble_toss",
            name = "Pebble Toss",
            description = "Hurl a pebble at an enemy from range, dealing light damage.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_1,
            effects = listOf(SkillEffect.Damage(baseDamage = 8, statScaling = 0.4f)),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "fighter_headbutt",
            name = "Headbutt",
            description = "Charge headfirst into an enemy, dealing damage with a chance to stun.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_1,
            effects = listOf(
                SkillEffect.Damage(baseDamage = 7, statScaling = 0.4f),
                SkillEffect.ApplyStatus(StatusEffectType.STUN, duration = 1, intensity = 0.5f)
            ),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "fighter_endurance",
            name = "Endurance",
            description = "Passive: Permanently increases your maximum health.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_1,
            effects = listOf(SkillEffect.PassiveStats(healthBonus = 5)),
            isPassive = true,
            targetType = SkillTargetType.NONE
        )
    )
    
    // FIGHTER TIER 2 (5 skills)
    private val fighterTier2 = listOf(
        Skill(
            id = "fighter_whirlwind_slash",
            name = "Whirlwind Slash",
            description = "Spin in a circle, striking all nearby enemies with your weapon.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_2,
            prerequisiteSkills = listOf("fighter_twig_strike"),
            effects = listOf(SkillEffect.AoEDamage(baseDamage = 12, statScaling = 0.4f)),
            targetType = SkillTargetType.ALL_ENEMIES
        ),
        Skill(
            id = "fighter_double_strike",
            name = "Double Strike",
            description = "Strike twice in rapid succession, hitting the same target twice.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_2,
            prerequisiteSkills = listOf("fighter_twig_strike"),
            effects = listOf(SkillEffect.MultiHit(hits = 2, damagePerHit = 8, statScaling = 0.4f)),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "fighter_rage",
            name = "Rage",
            description = "Enter a berserker rage, boosting attack but reducing defense for 3 turns.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_2,
            prerequisiteSkills = listOf("fighter_power_stance"),
            effects = listOf(
                SkillEffect.BuffAttack(attackBonus = 5, duration = 3),
                SkillEffect.DebuffDefense(defensePenalty = 3, duration = 3)
            ),
            targetType = SkillTargetType.SELF
        ),
        Skill(
            id = "fighter_stone_fist",
            name = "Stone Fist",
            description = "A devastating punch enhanced with pebble-like hardness.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_2,
            prerequisiteSkills = listOf("fighter_pebble_toss"),
            effects = listOf(SkillEffect.Damage(baseDamage = 20, statScaling = 0.6f)),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "fighter_unyielding",
            name = "Unyielding",
            description = "Passive: Permanently increases your maximum health.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_2,
            prerequisiteSkills = listOf("fighter_endurance"),
            effects = listOf(SkillEffect.PassiveStats(healthBonus = 10)),
            isPassive = true,
            targetType = SkillTargetType.NONE
        )
    )
    
    // FIGHTER TIER 3 (5 skills)
    private val fighterTier3 = listOf(
        Skill(
            id = "fighter_crushing_blow",
            name = "Crushing Blow",
            description = "A powerful strike that ignores armor, dealing massive damage.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_3,
            prerequisiteSkills = listOf("fighter_stone_fist"),
            effects = listOf(
                SkillEffect.Damage(baseDamage = 25, statScaling = 0.7f),
                SkillEffect.IgnoreDefense
            ),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "fighter_berserker_fury",
            name = "Berserker Fury",
            description = "Attack with reckless abandon, striking 3 times in quick succession.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_3,
            prerequisiteSkills = listOf("fighter_double_strike", "fighter_rage"),
            effects = listOf(SkillEffect.MultiHit(hits = 3, damagePerHit = 10, statScaling = 0.4f)),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "fighter_earthquake_stomp",
            name = "Earthquake Stomp",
            description = "Stomp the ground with tremendous force, stunning all enemies.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_3,
            prerequisiteSkills = listOf("fighter_whirlwind_slash"),
            effects = listOf(
                SkillEffect.AoEDamage(baseDamage = 8, statScaling = 0.3f),
                SkillEffect.ApplyStatus(StatusEffectType.STUN, duration = 1, intensity = 1.0f)
            ),
            targetType = SkillTargetType.ALL_ENEMIES
        ),
        Skill(
            id = "fighter_iron_will",
            name = "Iron Will",
            description = "Steel your resolve, becoming immune to stun for 2 turns.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_3,
            prerequisiteSkills = listOf("fighter_power_stance"),
            effects = listOf(SkillEffect.BuffDefense(defenseBonus = 5, duration = 2)),
            targetType = SkillTargetType.SELF
        ),
        Skill(
            id = "fighter_titans_grasp",
            name = "Titan's Grasp",
            description = "Passive: Permanently increases your attack power.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_3,
            prerequisiteSkills = listOf("fighter_unyielding"),
            effects = listOf(SkillEffect.PassiveStats(attackBonus = 5)),
            isPassive = true,
            targetType = SkillTargetType.NONE
        )
    )
    
    // FIGHTER TIER 4 (3 skills)
    private val fighterTier4 = listOf(
        Skill(
            id = "fighter_devastate",
            name = "Devastate",
            description = "Unleash all your strength in one devastating blow.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_4,
            prerequisiteSkills = listOf("fighter_crushing_blow"),
            effects = listOf(SkillEffect.Damage(baseDamage = 40, statScaling = 1.0f)),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "fighter_unstoppable_force",
            name = "Unstoppable Force",
            description = "A strike so powerful it cannot be defended against.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_4,
            prerequisiteSkills = listOf("fighter_berserker_fury"),
            effects = listOf(
                SkillEffect.Damage(baseDamage = 35, statScaling = 0.9f),
                SkillEffect.IgnoreDefense
            ),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "fighter_warlords_might",
            name = "Warlord's Might",
            description = "Passive: Permanently increases all combat stats.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.TIER_4,
            prerequisiteSkills = listOf("fighter_titans_grasp"),
            effects = listOf(SkillEffect.PassiveStats(healthBonus = 10, attackBonus = 3, defenseBonus = 2)),
            isPassive = true,
            targetType = SkillTargetType.NONE
        )
    )
    
    // FIGHTER ULTIMATE (1 skill)
    private val fighterUltimate = listOf(
        Skill(
            id = "fighter_quails_wrath",
            name = "Quail's Wrath",
            description = "Channel the fury of all quails, unleashing devastating area damage with burning and weakness.",
            archetype = SkillArchetype.FIGHTER,
            tier = SkillTier.ULTIMATE,
            prerequisiteSkills = listOf("fighter_devastate", "fighter_unstoppable_force"),
            effects = listOf(
                SkillEffect.AoEDamage(baseDamage = 30, statScaling = 0.8f),
                SkillEffect.ApplyStatus(StatusEffectType.BURN, duration = 3, intensity = 1.0f),
                SkillEffect.ApplyStatus(StatusEffectType.WEAKEN, duration = 2, intensity = 1.0f)
            ),
            targetType = SkillTargetType.ALL_ENEMIES
        )
    )
    
    // ==================== RANGER ARCHETYPE (19 skills) ====================
    
    // RANGER TIER 1 (5 skills)
    private val rangerTier1 = listOf(
        Skill(
            id = "ranger_seed_shot",
            name = "Seed Shot",
            description = "Fire a seed projectile at an enemy from range.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_1,
            effects = listOf(SkillEffect.Damage(baseDamage = 8, statScaling = 0.4f)),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "ranger_quick_step",
            name = "Quick Step",
            description = "Increase your speed for 3 turns, acting faster in combat.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_1,
            effects = listOf(SkillEffect.BuffSpeed(speedBonus = 2, duration = 3)),
            targetType = SkillTargetType.SELF
        ),
        Skill(
            id = "ranger_feather_dart",
            name = "Feather Dart",
            description = "Launch 2 feather darts in rapid succession.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_1,
            effects = listOf(SkillEffect.MultiHit(hits = 2, damagePerHit = 5, statScaling = 0.3f)),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "ranger_camouflage",
            name = "Camouflage",
            description = "Blend into surroundings, gaining defense for 2 turns.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_1,
            effects = listOf(SkillEffect.BuffDefense(defenseBonus = 3, duration = 2)),
            targetType = SkillTargetType.SELF
        ),
        Skill(
            id = "ranger_keen_eye",
            name = "Keen Eye",
            description = "Passive: Permanently increases your attack power (critical chance).",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_1,
            effects = listOf(SkillEffect.PassiveStats(attackBonus = 1)),
            isPassive = true,
            targetType = SkillTargetType.NONE
        )
    )
    
    // RANGER TIER 2 (5 skills)
    private val rangerTier2 = listOf(
        Skill(
            id = "ranger_rapid_fire",
            name = "Rapid Fire",
            description = "Fire 3 projectiles in quick succession at a single target.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_2,
            prerequisiteSkills = listOf("ranger_feather_dart"),
            effects = listOf(SkillEffect.MultiHit(hits = 3, damagePerHit = 6, statScaling = 0.3f)),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "ranger_poisoned_needle",
            name = "Poisoned Needle",
            description = "Strike with a poisoned thorn, applying poison for 3 turns.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_2,
            prerequisiteSkills = listOf("ranger_seed_shot"),
            effects = listOf(
                SkillEffect.Damage(baseDamage = 10, statScaling = 0.4f),
                SkillEffect.ApplyStatus(StatusEffectType.POISON, duration = 3, intensity = 1.0f)
            ),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "ranger_disengage",
            name = "Disengage",
            description = "Quickly retreat, increasing flee success chance.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_2,
            prerequisiteSkills = listOf("ranger_quick_step"),
            effects = listOf(SkillEffect.FleeBonus(successBonus = 0.3f)),
            targetType = SkillTargetType.SELF
        ),
        Skill(
            id = "ranger_precision_shot",
            name = "Precision Shot",
            description = "A carefully aimed shot that deals bonus damage.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_2,
            prerequisiteSkills = listOf("ranger_seed_shot"),
            effects = listOf(SkillEffect.Damage(baseDamage = 18, statScaling = 0.6f)),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "ranger_fleet_footed",
            name = "Fleet Footed",
            description = "Passive: Permanently increases your speed.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_2,
            prerequisiteSkills = listOf("ranger_keen_eye"),
            effects = listOf(SkillEffect.PassiveStats(speedBonus = 2)),
            isPassive = true,
            targetType = SkillTargetType.NONE
        )
    )
    
    // RANGER TIER 3 (5 skills)
    private val rangerTier3 = listOf(
        Skill(
            id = "ranger_volley",
            name = "Volley",
            description = "Fire a barrage of seeds at all enemies.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_3,
            prerequisiteSkills = listOf("ranger_rapid_fire"),
            effects = listOf(SkillEffect.AoEDamage(baseDamage = 12, statScaling = 0.4f)),
            targetType = SkillTargetType.ALL_ENEMIES
        ),
        Skill(
            id = "ranger_shadow_strike",
            name = "Shadow Strike",
            description = "Strike from the shadows, ignoring enemy defenses.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_3,
            prerequisiteSkills = listOf("ranger_precision_shot"),
            effects = listOf(
                SkillEffect.Damage(baseDamage = 22, statScaling = 0.7f),
                SkillEffect.IgnoreDefense
            ),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "ranger_hunters_mark",
            name = "Hunter's Mark",
            description = "Mark an enemy, making them vulnerable to additional damage for 3 turns.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_3,
            prerequisiteSkills = listOf("ranger_poisoned_needle"),
            effects = listOf(SkillEffect.ApplyStatus(StatusEffectType.VULNERABLE, duration = 3, intensity = 1.0f)),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "ranger_evasion_master",
            name = "Evasion Master",
            description = "Enhance your dodging ability, increasing defense for 3 turns.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_3,
            prerequisiteSkills = listOf("ranger_camouflage"),
            effects = listOf(SkillEffect.BuffDefense(defenseBonus = 5, duration = 3)),
            targetType = SkillTargetType.SELF
        ),
        Skill(
            id = "ranger_swift_hunter",
            name = "Swift Hunter",
            description = "Passive: Permanently increases your speed.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_3,
            prerequisiteSkills = listOf("ranger_fleet_footed"),
            effects = listOf(SkillEffect.PassiveStats(speedBonus = 3)),
            isPassive = true,
            targetType = SkillTargetType.NONE
        )
    )
    
    // RANGER TIER 4 (3 skills)
    private val rangerTier4 = listOf(
        Skill(
            id = "ranger_deadly_aim",
            name = "Deadly Aim",
            description = "A perfectly aimed shot guaranteed to critically strike.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_4,
            prerequisiteSkills = listOf("ranger_shadow_strike"),
            effects = listOf(
                SkillEffect.Damage(baseDamage = 30, statScaling = 0.8f),
                SkillEffect.GuaranteedCrit
            ),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "ranger_phantom_dance",
            name = "Phantom Dance",
            description = "Move with supernatural speed, gaining massive defense for 2 turns.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_4,
            prerequisiteSkills = listOf("ranger_evasion_master"),
            effects = listOf(SkillEffect.BuffDefense(defenseBonus = 10, duration = 2)),
            targetType = SkillTargetType.SELF
        ),
        Skill(
            id = "ranger_master_forager",
            name = "Master Forager",
            description = "Passive: Permanently increases all ranged combat stats.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.TIER_4,
            prerequisiteSkills = listOf("ranger_swift_hunter"),
            effects = listOf(SkillEffect.PassiveStats(attackBonus = 3, speedBonus = 2)),
            isPassive = true,
            targetType = SkillTargetType.NONE
        )
    )
    
    // RANGER ULTIMATE (1 skill)
    private val rangerUltimate = listOf(
        Skill(
            id = "ranger_storm_of_feathers",
            name = "Storm of Feathers",
            description = "Summon a whirlwind of feather darts, striking all enemies multiple times.",
            archetype = SkillArchetype.RANGER,
            tier = SkillTier.ULTIMATE,
            prerequisiteSkills = listOf("ranger_deadly_aim", "ranger_volley"),
            effects = listOf(
                SkillEffect.AoEDamage(baseDamage = 25, statScaling = 0.7f),
                SkillEffect.ApplyStatus(StatusEffectType.WEAKEN, duration = 2, intensity = 1.0f)
            ),
            targetType = SkillTargetType.ALL_ENEMIES
        )
    )
    
    // ==================== GUARDIAN ARCHETYPE (19 skills) ====================
    
    // GUARDIAN TIER 1 (5 skills)
    private val guardianTier1 = listOf(
        Skill(
            id = "guardian_acorn_shield",
            name = "Acorn Shield",
            description = "Raise your acorn cap as a shield, increasing defense for 3 turns.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_1,
            effects = listOf(SkillEffect.BuffDefense(defenseBonus = 2, duration = 3)),
            targetType = SkillTargetType.SELF
        ),
        Skill(
            id = "guardian_bark_armor",
            name = "Bark Armor",
            description = "Restore health with regenerative bark sap.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_1,
            effects = listOf(SkillEffect.Heal(baseHealing = 10)),
            targetType = SkillTargetType.SELF
        ),
        Skill(
            id = "guardian_taunt",
            name = "Taunt",
            description = "Force an enemy to focus attacks on you for 2 turns.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_1,
            effects = listOf(SkillEffect.DebuffAttack(attackPenalty = 1, duration = 2)),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "guardian_rally",
            name = "Rally",
            description = "Inspire an ally, increasing their attack for 2 turns.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_1,
            effects = listOf(SkillEffect.BuffAttack(attackBonus = 2, duration = 2)),
            targetType = SkillTargetType.SINGLE_ALLY
        ),
        Skill(
            id = "guardian_fortitude",
            name = "Fortitude",
            description = "Passive: Permanently increases your maximum health.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_1,
            effects = listOf(SkillEffect.PassiveStats(healthBonus = 5)),
            isPassive = true,
            targetType = SkillTargetType.NONE
        )
    )
    
    // GUARDIAN TIER 2 (5 skills)
    private val guardianTier2 = listOf(
        Skill(
            id = "guardian_shield_bash",
            name = "Shield Bash",
            description = "Smash an enemy with your shield, dealing damage and stunning.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_2,
            prerequisiteSkills = listOf("guardian_acorn_shield"),
            effects = listOf(
                SkillEffect.Damage(baseDamage = 12, statScaling = 0.5f),
                SkillEffect.ApplyStatus(StatusEffectType.STUN, duration = 1, intensity = 1.0f)
            ),
            targetType = SkillTargetType.SINGLE_ENEMY
        ),
        Skill(
            id = "guardian_healing_leaf",
            name = "Healing Leaf",
            description = "Apply a medicinal leaf, restoring significant health.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_2,
            prerequisiteSkills = listOf("guardian_bark_armor"),
            effects = listOf(SkillEffect.Heal(baseHealing = 20)),
            targetType = SkillTargetType.SELF
        ),
        Skill(
            id = "guardian_protective_stance",
            name = "Protective Stance",
            description = "Reduce damage taken by 50% for 2 turns.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_2,
            prerequisiteSkills = listOf("guardian_acorn_shield"),
            effects = listOf(SkillEffect.DamageReduction(reductionPercent = 0.5f, duration = 2)),
            targetType = SkillTargetType.SELF
        ),
        Skill(
            id = "guardian_counter_strike",
            name = "Counter Strike",
            description = "Reflect 30% of damage back to attackers for 2 turns.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_2,
            prerequisiteSkills = listOf("guardian_taunt"),
            effects = listOf(SkillEffect.ReflectDamage(reflectPercent = 0.3f, duration = 2)),
            targetType = SkillTargetType.SELF
        ),
        Skill(
            id = "guardian_stalwart",
            name = "Stalwart",
            description = "Passive: Permanently increases your defense.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_2,
            prerequisiteSkills = listOf("guardian_fortitude"),
            effects = listOf(SkillEffect.PassiveStats(defenseBonus = 3)),
            isPassive = true,
            targetType = SkillTargetType.NONE
        )
    )
    
    // GUARDIAN TIER 3 (5 skills)
    private val guardianTier3 = listOf(
        Skill(
            id = "guardian_blessing",
            name = "Guardian's Blessing",
            description = "Heal all allies with restorative energy.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_3,
            prerequisiteSkills = listOf("guardian_healing_leaf"),
            effects = listOf(SkillEffect.AoEHeal(baseHealing = 15)),
            targetType = SkillTargetType.ALL_ALLIES
        ),
        Skill(
            id = "guardian_thornmail",
            name = "Thornmail",
            description = "Grow thorny bark, reflecting 50% of damage for 3 turns.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_3,
            prerequisiteSkills = listOf("guardian_counter_strike"),
            effects = listOf(SkillEffect.ReflectDamage(reflectPercent = 0.5f, duration = 3)),
            targetType = SkillTargetType.SELF
        ),
        Skill(
            id = "guardian_bastion",
            name = "Bastion",
            description = "Become an immovable fortress, reducing damage by 70% for 1 turn.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_3,
            prerequisiteSkills = listOf("guardian_protective_stance"),
            effects = listOf(SkillEffect.DamageReduction(reductionPercent = 0.7f, duration = 1)),
            targetType = SkillTargetType.SELF
        ),
        Skill(
            id = "guardian_life_bond",
            name = "Life Bond",
            description = "Create a bond with an ally, sharing healing effects for 2 turns.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_3,
            prerequisiteSkills = listOf("guardian_rally"),
            effects = listOf(SkillEffect.Heal(baseHealing = 15)),
            targetType = SkillTargetType.SINGLE_ALLY
        ),
        Skill(
            id = "guardian_iron_shell",
            name = "Iron Shell",
            description = "Passive: Permanently increases your defense.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_3,
            prerequisiteSkills = listOf("guardian_stalwart"),
            effects = listOf(SkillEffect.PassiveStats(defenseBonus = 5)),
            isPassive = true,
            targetType = SkillTargetType.NONE
        )
    )
    
    // GUARDIAN TIER 4 (3 skills)
    private val guardianTier4 = listOf(
        Skill(
            id = "guardian_divine_shield",
            name = "Divine Shield",
            description = "Become invulnerable, reducing all damage by 90% for 1 turn.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_4,
            prerequisiteSkills = listOf("guardian_bastion"),
            effects = listOf(SkillEffect.DamageReduction(reductionPercent = 0.9f, duration = 1)),
            targetType = SkillTargetType.SELF
        ),
        Skill(
            id = "guardian_mass_heal",
            name = "Mass Heal",
            description = "Channel powerful restoration, healing all allies significantly.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_4,
            prerequisiteSkills = listOf("guardian_blessing"),
            effects = listOf(SkillEffect.AoEHeal(baseHealing = 25)),
            targetType = SkillTargetType.ALL_ALLIES
        ),
        Skill(
            id = "guardian_unbreakable",
            name = "Unbreakable",
            description = "Passive: Permanently increases all defensive stats.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.TIER_4,
            prerequisiteSkills = listOf("guardian_iron_shell"),
            effects = listOf(SkillEffect.PassiveStats(healthBonus = 15, defenseBonus = 5)),
            isPassive = true,
            targetType = SkillTargetType.NONE
        )
    )
    
    // GUARDIAN ULTIMATE (1 skill)
    private val guardianUltimate = listOf(
        Skill(
            id = "guardian_sanctuary",
            name = "Sanctuary",
            description = "Create a protective sanctuary, reducing all ally damage by 80% and healing for 2 turns.",
            archetype = SkillArchetype.GUARDIAN,
            tier = SkillTier.ULTIMATE,
            prerequisiteSkills = listOf("guardian_divine_shield", "guardian_mass_heal"),
            effects = listOf(
                SkillEffect.AoEHeal(baseHealing = 30),
                SkillEffect.DamageReduction(reductionPercent = 0.8f, duration = 2)
            ),
            targetType = SkillTargetType.ALL_ALLIES
        )
    )
    
    // ==================== CATALOG AGGREGATION ====================
    
    /**
     * All 57 skills across all archetypes and tiers.
     */
    val allSkills: List<Skill> = fighterTier1 + fighterTier2 + fighterTier3 + fighterTier4 + fighterUltimate +
            rangerTier1 + rangerTier2 + rangerTier3 + rangerTier4 + rangerUltimate +
            guardianTier1 + guardianTier2 + guardianTier3 + guardianTier4 + guardianUltimate
    
    init {
        // Validate no duplicate IDs
        val ids = allSkills.map { it.id }
        require(ids.size == ids.toSet().size) { "Duplicate skill IDs detected in catalog" }
        require(allSkills.size == 57) { "Expected 57 skills, found ${allSkills.size}" }
    }
    
    /**
     * Get a skill by ID.
     */
    fun getSkill(id: String): Skill? = allSkills.find { it.id == id }
    
    /**
     * Get all skills for a specific archetype.
     */
    fun getSkillsByArchetype(archetype: SkillArchetype): List<Skill> =
        allSkills.filter { it.archetype == archetype }
    
    /**
     * Get all skills of a specific tier.
     */
    fun getSkillsByTier(tier: SkillTier): List<Skill> =
        allSkills.filter { it.tier == tier }
    
    /**
     * Get all passive skills.
     */
    fun getPassiveSkills(): List<Skill> =
        allSkills.filter { it.isPassive }
    
    /**
     * Get all active (non-passive) skills.
     */
    fun getActiveSkills(): List<Skill> =
        allSkills.filter { !it.isPassive }
}
