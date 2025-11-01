package com.jalmarquest.shared.nest

import kotlinx.serialization.json.Json
import kotlin.test.*

class NestTest {
    
    @Test
    fun `default nest should be BASIC tier`() {
        val nest = Nest(id = "test_nest")
        
        assertEquals(NestTier.BASIC, nest.tier)
        assertEquals("Simple Nest", nest.getDisplayName())
    }
    
    @Test
    fun `nest ID cannot be blank`() {
        assertFails {
            Nest(id = "")
        }
        
        assertFails {
            Nest(id = "   ")
        }
    }
    
    @Test
    fun `custom name should be returned if set`() {
        val nest = Nest(id = "test_nest", tier = NestTier.COMFORTABLE, customName = "My Cozy Home")
        
        assertEquals("My Cozy Home", nest.getDisplayName())
    }
    
    @Test
    fun `custom name cannot be blank if provided`() {
        assertFails {
            Nest(id = "test_nest", customName = "")
        }
        
        assertFails {
            Nest(id = "test_nest", customName = "   ")
        }
    }
    
    @Test
    fun `custom name cannot exceed 50 characters`() {
        val validName = "A".repeat(50)
        val nest = Nest(id = "test_nest", customName = validName)
        assertEquals(validName, nest.customName)
        
        assertFails {
            Nest(id = "test_nest", customName = "A".repeat(51))
        }
    }
    
    @Test
    fun `nest should serialize correctly`() {
        val nest = Nest(id = "player_nest", tier = NestTier.LUXURIOUS, customName = "The Gilded Perch")
        
        val json = Json.encodeToString(Nest.serializer(), nest)
        val deserialized = Json.decodeFromString(Nest.serializer(), json)
        
        assertEquals(nest, deserialized)
    }
}

class NestTierTest {
    
    @Test
    fun `BASIC tier should have no bonuses`() {
        val tier = NestTier.BASIC
        
        assertEquals(0.0f, tier.hpRegenBonus)
        assertEquals(0.0f, tier.staminaRegenBonus)
        assertEquals(0.0f, tier.xpBonus)
    }
    
    @Test
    fun `COMFORTABLE tier should have correct bonuses`() {
        val tier = NestTier.COMFORTABLE
        
        assertEquals(0.10f, tier.hpRegenBonus)
        assertEquals(0.05f, tier.staminaRegenBonus)
        assertEquals(0.0f, tier.xpBonus)
    }
    
    @Test
    fun `LUXURIOUS tier should have maximum bonuses`() {
        val tier = NestTier.LUXURIOUS
        
        assertEquals(0.20f, tier.hpRegenBonus)
        assertEquals(0.10f, tier.staminaRegenBonus)
        assertEquals(0.05f, tier.xpBonus)
    }
    
    @Test
    fun `BASIC tier should have level 1 requirement`() {
        assertEquals(1, NestTier.BASIC.requiredLevel)
    }
    
    @Test
    fun `COMFORTABLE tier should have level 5 requirement`() {
        assertEquals(5, NestTier.COMFORTABLE.requiredLevel)
    }
    
    @Test
    fun `LUXURIOUS tier should have level 10 requirement`() {
        assertEquals(10, NestTier.LUXURIOUS.requiredLevel)
    }
    
    @Test
    fun `getNextTier should return correct progression`() {
        assertEquals(NestTier.COMFORTABLE, NestTier.BASIC.getNextTier())
        assertEquals(NestTier.LUXURIOUS, NestTier.COMFORTABLE.getNextTier())
        assertNull(NestTier.LUXURIOUS.getNextTier())
    }
    
    @Test
    fun `canUpgradeTo should allow linear progression only`() {
        // Valid upgrades
        assertTrue(NestTier.BASIC.canUpgradeTo(NestTier.COMFORTABLE))
        assertTrue(NestTier.COMFORTABLE.canUpgradeTo(NestTier.LUXURIOUS))
        
        // Invalid: skipping tiers
        assertFalse(NestTier.BASIC.canUpgradeTo(NestTier.LUXURIOUS))
        
        // Invalid: downgrading
        assertFalse(NestTier.COMFORTABLE.canUpgradeTo(NestTier.BASIC))
        assertFalse(NestTier.LUXURIOUS.canUpgradeTo(NestTier.BASIC))
        assertFalse(NestTier.LUXURIOUS.canUpgradeTo(NestTier.COMFORTABLE))
        
        // Invalid: upgrading from max tier
        assertFalse(NestTier.LUXURIOUS.canUpgradeTo(NestTier.LUXURIOUS))
    }
    
    @Test
    fun `tier should have descriptive names`() {
        assertEquals("Simple Nest", NestTier.BASIC.defaultName)
        assertEquals("Comfortable Nest", NestTier.COMFORTABLE.defaultName)
        assertEquals("Luxurious Nest", NestTier.LUXURIOUS.defaultName)
    }
}

class NestUpgradeRequirementsTest {
    
    @Test
    fun `COMFORTABLE upgrade should require correct materials`() {
        val requirements = NestUpgradeRequirements.forTier(NestTier.BASIC)!!
        
        assertEquals(NestTier.COMFORTABLE, requirements.targetTier)
        assertEquals(20, requirements.requiredMaterials["twig"])
        assertEquals(30, requirements.requiredMaterials["dried_leaf"])
        assertEquals(10, requirements.requiredMaterials["grass_blade"])
        assertEquals(3, requirements.requiredMaterials.size)
    }
    
    @Test
    fun `LUXURIOUS upgrade should require correct materials`() {
        val requirements = NestUpgradeRequirements.forTier(NestTier.COMFORTABLE)!!
        
        assertEquals(NestTier.LUXURIOUS, requirements.targetTier)
        assertEquals(50, requirements.requiredMaterials["twig"])
        assertEquals(40, requirements.requiredMaterials["dried_leaf"])
        assertEquals(20, requirements.requiredMaterials["grass_blade"])
        assertEquals(10, requirements.requiredMaterials["spider_silk"])
        assertEquals(5, requirements.requiredMaterials["feather"])
        assertEquals(5, requirements.requiredMaterials.size)
    }
    
    @Test
    fun `LUXURIOUS tier should return null requirements (max tier)`() {
        val requirements = NestUpgradeRequirements.forTier(NestTier.LUXURIOUS)
        
        assertNull(requirements)
    }
    
    @Test
    fun `upgrade requirements cannot be empty`() {
        assertFails {
            NestUpgradeRequirements(
                targetTier = NestTier.COMFORTABLE,
                requiredMaterials = emptyMap()
            )
        }
    }
    
    @Test
    fun `material item ID cannot be blank`() {
        assertFails {
            NestUpgradeRequirements(
                targetTier = NestTier.COMFORTABLE,
                requiredMaterials = mapOf("" to 10)
            )
        }
    }
    
    @Test
    fun `material quantity must be positive`() {
        assertFails {
            NestUpgradeRequirements(
                targetTier = NestTier.COMFORTABLE,
                requiredMaterials = mapOf("material_twig" to 0)
            )
        }
        
        assertFails {
            NestUpgradeRequirements(
                targetTier = NestTier.COMFORTABLE,
                requiredMaterials = mapOf("material_twig" to -5)
            )
        }
    }
    
    @Test
    fun `upgrade requirements should serialize correctly`() {
        val requirements = NestUpgradeRequirements.forTier(NestTier.BASIC)!!
        
        val json = Json.encodeToString(NestUpgradeRequirements.serializer(), requirements)
        val deserialized = Json.decodeFromString(NestUpgradeRequirements.serializer(), json)
        
        assertEquals(requirements, deserialized)
    }
}

class NestStatModifiersTest {
    
    @Test
    fun `default modifiers should be 1_0x (no bonus)`() {
        val modifiers = NestStatModifiers()
        
        assertEquals(1.0f, modifiers.hpRegenMultiplier)
        assertEquals(1.0f, modifiers.staminaRegenMultiplier)
        assertEquals(1.0f, modifiers.xpGainMultiplier)
    }
    
    @Test
    fun `fromTier should create correct modifiers for BASIC`() {
        val modifiers = NestStatModifiers.fromTier(NestTier.BASIC)
        
        assertEquals(1.0f, modifiers.hpRegenMultiplier)
        assertEquals(1.0f, modifiers.staminaRegenMultiplier)
        assertEquals(1.0f, modifiers.xpGainMultiplier)
    }
    
    @Test
    fun `fromTier should create correct modifiers for COMFORTABLE`() {
        val modifiers = NestStatModifiers.fromTier(NestTier.COMFORTABLE)
        
        assertEquals(1.10f, modifiers.hpRegenMultiplier)
        assertEquals(1.05f, modifiers.staminaRegenMultiplier)
        assertEquals(1.0f, modifiers.xpGainMultiplier)
    }
    
    @Test
    fun `fromTier should create correct modifiers for LUXURIOUS`() {
        val modifiers = NestStatModifiers.fromTier(NestTier.LUXURIOUS)
        
        assertEquals(1.20f, modifiers.hpRegenMultiplier)
        assertEquals(1.10f, modifiers.staminaRegenMultiplier)
        assertEquals(1.05f, modifiers.xpGainMultiplier)
    }
    
    @Test
    fun `multipliers cannot be less than 1_0`() {
        assertFails {
            NestStatModifiers(hpRegenMultiplier = 0.9f)
        }
        
        assertFails {
            NestStatModifiers(staminaRegenMultiplier = 0.5f)
        }
        
        assertFails {
            NestStatModifiers(xpGainMultiplier = 0.0f)
        }
    }
    
    @Test
    fun `modifiers should serialize correctly`() {
        val modifiers = NestStatModifiers.fromTier(NestTier.LUXURIOUS)
        
        val json = Json.encodeToString(NestStatModifiers.serializer(), modifiers)
        val deserialized = Json.decodeFromString(NestStatModifiers.serializer(), json)
        
        assertEquals(modifiers, deserialized)
    }
}

class NestVisualStateTest {
    
    @Test
    fun `BASIC tier should have visual state`() {
        val visual = NestVisualState.forTier(NestTier.BASIC)
        
        assertEquals(NestTier.BASIC, visual.tier)
        assertTrue(visual.asciiArt.contains("o"))  // Player representation
        assertTrue(visual.flavorText.isNotBlank())
    }
    
    @Test
    fun `COMFORTABLE tier should have visual state`() {
        val visual = NestVisualState.forTier(NestTier.COMFORTABLE)
        
        assertEquals(NestTier.COMFORTABLE, visual.tier)
        assertTrue(visual.asciiArt.contains("Cozy"))
        assertTrue(visual.asciiArt.contains("Soft Grass"))
        assertTrue(visual.flavorText.isNotBlank())
    }
    
    @Test
    fun `LUXURIOUS tier should have visual state`() {
        val visual = NestVisualState.forTier(NestTier.LUXURIOUS)
        
        assertEquals(NestTier.LUXURIOUS, visual.tier)
        assertTrue(visual.asciiArt.contains("Paradise"))
        assertTrue(visual.asciiArt.contains("Feathers & Silk"))
        assertTrue(visual.flavorText.contains("spider silk"))
    }
    
    @Test
    fun `ASCII art cannot be blank`() {
        assertFails {
            NestVisualState(
                tier = NestTier.BASIC,
                asciiArt = "",
                flavorText = "Some text"
            )
        }
    }
    
    @Test
    fun `flavor text cannot be blank`() {
        assertFails {
            NestVisualState(
                tier = NestTier.BASIC,
                asciiArt = "Some art",
                flavorText = ""
            )
        }
    }
    
    @Test
    fun `visual state should serialize correctly`() {
        val visual = NestVisualState.forTier(NestTier.COMFORTABLE)
        
        val json = Json.encodeToString(NestVisualState.serializer(), visual)
        val deserialized = Json.decodeFromString(NestVisualState.serializer(), json)
        
        assertEquals(visual, deserialized)
    }
}
