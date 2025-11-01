package com.jalmarquest.shared.encounter

import com.jalmarquest.shared.model.TimeOfDay
import com.jalmarquest.shared.world.BiomeType
import kotlin.test.*

class EncounterRateTest {
    
    @Test
    fun `EncounterRate should validate multipliers are non-negative`() {
        assertFailsWith<IllegalArgumentException> {
            EncounterRate(morningMultiplier = -1.0)
        }
        assertFailsWith<IllegalArgumentException> {
            EncounterRate(afternoonMultiplier = -0.5)
        }
        assertFailsWith<IllegalArgumentException> {
            EncounterRate(eveningMultiplier = -1.0)
        }
        assertFailsWith<IllegalArgumentException> {
            EncounterRate(nightMultiplier = -2.0)
        }
    }
    
    @Test
    fun `EncounterRate should accept zero multipliers`() {
        val rate = EncounterRate(
            morningMultiplier = 0.0,
            afternoonMultiplier = 0.0,
            eveningMultiplier = 0.0,
            nightMultiplier = 0.0
        )
        assertEquals(0.0, rate.morningMultiplier)
        assertEquals(0.0, rate.nightMultiplier)
    }
    
    @Test
    fun `getMultiplier should return correct value for each TimeOfDay`() {
        val rate = EncounterRate(
            morningMultiplier = 0.5,
            afternoonMultiplier = 1.0,
            eveningMultiplier = 1.5,
            nightMultiplier = 2.0
        )
        
        assertEquals(0.5, rate.getMultiplier(TimeOfDay.MORNING))
        assertEquals(1.0, rate.getMultiplier(TimeOfDay.AFTERNOON))
        assertEquals(1.5, rate.getMultiplier(TimeOfDay.EVENING))
        assertEquals(2.0, rate.getMultiplier(TimeOfDay.NIGHT))
    }
    
    @Test
    fun `STANDARD should have uniform multipliers`() {
        val standard = EncounterRate.STANDARD
        assertEquals(1.0, standard.morningMultiplier)
        assertEquals(1.0, standard.afternoonMultiplier)
        assertEquals(1.0, standard.eveningMultiplier)
        assertEquals(1.0, standard.nightMultiplier)
    }
    
    @Test
    fun `DIURNAL should favor daytime`() {
        val diurnal = EncounterRate.DIURNAL
        assertTrue(diurnal.afternoonMultiplier > diurnal.nightMultiplier)
        assertTrue(diurnal.afternoonMultiplier > diurnal.morningMultiplier)
        assertTrue(diurnal.afternoonMultiplier > diurnal.eveningMultiplier)
    }
    
    @Test
    fun `NOCTURNAL should favor nighttime`() {
        val nocturnal = EncounterRate.NOCTURNAL
        assertTrue(nocturnal.nightMultiplier > nocturnal.afternoonMultiplier)
        assertTrue(nocturnal.nightMultiplier > nocturnal.morningMultiplier)
        assertTrue(nocturnal.eveningMultiplier > nocturnal.afternoonMultiplier)
    }
    
    @Test
    fun `CREPUSCULAR should favor morning and evening`() {
        val crepuscular = EncounterRate.CREPUSCULAR
        assertTrue(crepuscular.morningMultiplier > crepuscular.afternoonMultiplier)
        assertTrue(crepuscular.eveningMultiplier > crepuscular.afternoonMultiplier)
        assertTrue(crepuscular.morningMultiplier > crepuscular.nightMultiplier)
        assertTrue(crepuscular.eveningMultiplier > crepuscular.nightMultiplier)
    }
    
    @Test
    fun `PREDATOR should have elevated evening and night rates`() {
        val predator = EncounterRate.PREDATOR
        assertTrue(predator.eveningMultiplier > predator.afternoonMultiplier)
        assertTrue(predator.nightMultiplier > predator.afternoonMultiplier)
    }
}

class EncounterManagerTest {
    
    private val manager = EncounterManager()
    
    @Test
    fun `calculateEncounterRate should validate inputs`() {
        assertFailsWith<IllegalArgumentException> {
            manager.calculateEncounterRate(
                baseRate = -1.0,
                timeOfDay = TimeOfDay.AFTERNOON
            )
        }
        assertFailsWith<IllegalArgumentException> {
            manager.calculateEncounterRate(
                baseRate = 1.0,
                timeOfDay = TimeOfDay.AFTERNOON,
                biomeModifier = -0.5
            )
        }
    }
    
    @Test
    fun `calculateEncounterRate should multiply base rate by time multiplier`() {
        val result = manager.calculateEncounterRate(
            baseRate = 0.8,
            timeOfDay = TimeOfDay.NIGHT,
            encounterRate = EncounterRate(nightMultiplier = 2.0)
        )
        assertEquals(1.6, result, 0.01)
    }
    
    @Test
    fun `calculateEncounterRate should apply biome modifier`() {
        val result = manager.calculateEncounterRate(
            baseRate = 1.0,
            timeOfDay = TimeOfDay.AFTERNOON,
            encounterRate = EncounterRate.STANDARD,
            biomeModifier = 1.5
        )
        assertEquals(1.5, result, 0.01)
    }
    
    @Test
    fun `calculateEncounterRate should combine all modifiers`() {
        // Base: 0.5, Time: 2.0, Biome: 1.2
        // Expected: 0.5 * 2.0 * 1.2 = 1.2
        val result = manager.calculateEncounterRate(
            baseRate = 0.5,
            timeOfDay = TimeOfDay.NIGHT,
            encounterRate = EncounterRate(nightMultiplier = 2.0),
            biomeModifier = 1.2
        )
        assertEquals(1.2, result, 0.01)
    }
    
    @Test
    fun `calculateEncounterRate with zero base rate should return zero`() {
        val result = manager.calculateEncounterRate(
            baseRate = 0.0,
            timeOfDay = TimeOfDay.NIGHT,
            encounterRate = EncounterRate.NOCTURNAL,
            biomeModifier = 1.5
        )
        assertEquals(0.0, result)
    }
    
    @Test
    fun `shouldEncounter should validate effective rate`() {
        assertFailsWith<IllegalArgumentException> {
            manager.shouldEncounter(-0.5)
        }
    }
    
    @Test
    fun `shouldEncounter with rate greater than or equal to 1 should always return true`() {
        assertTrue(manager.shouldEncounter(1.0))
        assertTrue(manager.shouldEncounter(1.5))
        assertTrue(manager.shouldEncounter(2.0))
        assertTrue(manager.shouldEncounter(100.0))
    }
    
    @Test
    fun `shouldEncounter with rate 0 should always return false`() {
        assertFalse(manager.shouldEncounter(0.0))
    }
    
    @Test
    fun `shouldEncounter with fractional rate should be probabilistic`() {
        // Test multiple times to verify probability behavior
        var encounters = 0
        val trials = 1000
        val expectedRate = 0.5
        
        repeat(trials) {
            if (manager.shouldEncounter(expectedRate)) {
                encounters++
            }
        }
        
        // Should be roughly 50% (allow 10% margin)
        val actualRate = encounters.toDouble() / trials
        assertTrue(actualRate > 0.4 && actualRate < 0.6,
            "Expected ~0.5, got $actualRate")
    }
    
    @Test
    fun `getBiomeModifier should return valid modifiers for all biomes`() {
        val grassland = manager.getBiomeModifier(BiomeType.GRASSLAND)
        val forest = manager.getBiomeModifier(BiomeType.FOREST)
        val mountain = manager.getBiomeModifier(BiomeType.MOUNTAIN)
        val desert = manager.getBiomeModifier(BiomeType.DESERT)
        val swamp = manager.getBiomeModifier(BiomeType.SWAMP)
        val tundra = manager.getBiomeModifier(BiomeType.TUNDRA)
        val coastal = manager.getBiomeModifier(BiomeType.COASTAL)
        val cave = manager.getBiomeModifier(BiomeType.CAVE)
        
        // All should be positive
        assertTrue(grassland > 0.0)
        assertTrue(forest > 0.0)
        assertTrue(mountain > 0.0)
        assertTrue(desert > 0.0)
        assertTrue(swamp > 0.0)
        assertTrue(tundra > 0.0)
        assertTrue(coastal > 0.0)
        assertTrue(cave > 0.0)
        
        // Dangerous biomes should have higher modifiers
        assertTrue(swamp > grassland)
        assertTrue(cave > forest)
    }
    
    @Test
    fun `getDefaultEncounterRateForBiome should return appropriate patterns`() {
        val grassland = manager.getDefaultEncounterRateForBiome(BiomeType.GRASSLAND)
        val desert = manager.getDefaultEncounterRateForBiome(BiomeType.DESERT)
        val forest = manager.getDefaultEncounterRateForBiome(BiomeType.FOREST)
        val cave = manager.getDefaultEncounterRateForBiome(BiomeType.CAVE)
        
        // Grassland should be diurnal (daytime active)
        assertTrue(grassland.afternoonMultiplier > grassland.nightMultiplier)
        
        // Desert should be nocturnal (avoid heat)
        assertTrue(desert.nightMultiplier > desert.afternoonMultiplier)
        
        // Forest should be crepuscular (morning/evening)
        assertTrue(forest.morningMultiplier > forest.afternoonMultiplier)
        
        // Cave should be nocturnal (dark environment)
        assertTrue(cave.nightMultiplier > cave.afternoonMultiplier)
    }
    
    @Test
    fun `calculateFullEncounterRate should use biome default when encounterRate is null`() {
        val result = manager.calculateFullEncounterRate(
            baseRate = 1.0,
            timeOfDay = TimeOfDay.NIGHT,
            biome = BiomeType.DESERT
        )
        
        // Desert default is NOCTURNAL, night multiplier is high
        val expected = manager.calculateEncounterRate(
            baseRate = 1.0,
            timeOfDay = TimeOfDay.NIGHT,
            encounterRate = EncounterRate.NOCTURNAL,
            biomeModifier = manager.getBiomeModifier(BiomeType.DESERT)
        )
        
        assertEquals(expected, result, 0.01)
    }
    
    @Test
    fun `calculateFullEncounterRate should use custom encounterRate when provided`() {
        val customRate = EncounterRate(nightMultiplier = 5.0)
        val result = manager.calculateFullEncounterRate(
            baseRate = 1.0,
            timeOfDay = TimeOfDay.NIGHT,
            biome = BiomeType.GRASSLAND,
            encounterRate = customRate
        )
        
        val expected = manager.calculateEncounterRate(
            baseRate = 1.0,
            timeOfDay = TimeOfDay.NIGHT,
            encounterRate = customRate,
            biomeModifier = manager.getBiomeModifier(BiomeType.GRASSLAND)
        )
        
        assertEquals(expected, result, 0.01)
    }
}

class EncounterTierTest {
    
    @Test
    fun `fromRate should return NONE for zero rate`() {
        assertEquals(EncounterTier.NONE, EncounterTier.fromRate(0.0))
    }
    
    @Test
    fun `fromRate should return VERY_LOW for rates below 0_3`() {
        assertEquals(EncounterTier.VERY_LOW, EncounterTier.fromRate(0.1))
        assertEquals(EncounterTier.VERY_LOW, EncounterTier.fromRate(0.29))
    }
    
    @Test
    fun `fromRate should return LOW for rates 0_3 to 0_6`() {
        assertEquals(EncounterTier.LOW, EncounterTier.fromRate(0.3))
        assertEquals(EncounterTier.LOW, EncounterTier.fromRate(0.5))
    }
    
    @Test
    fun `fromRate should return MODERATE for rates 0_6 to 1_0`() {
        assertEquals(EncounterTier.MODERATE, EncounterTier.fromRate(0.6))
        assertEquals(EncounterTier.MODERATE, EncounterTier.fromRate(0.9))
    }
    
    @Test
    fun `fromRate should return HIGH for rates 1_0 to 1_5`() {
        assertEquals(EncounterTier.HIGH, EncounterTier.fromRate(1.0))
        assertEquals(EncounterTier.HIGH, EncounterTier.fromRate(1.4))
    }
    
    @Test
    fun `fromRate should return VERY_HIGH for rates 1_5 to 2_0`() {
        assertEquals(EncounterTier.VERY_HIGH, EncounterTier.fromRate(1.5))
        assertEquals(EncounterTier.VERY_HIGH, EncounterTier.fromRate(1.9))
    }
    
    @Test
    fun `fromRate should return EXTREME for rates above 2_0`() {
        assertEquals(EncounterTier.EXTREME, EncounterTier.fromRate(2.0))
        assertEquals(EncounterTier.EXTREME, EncounterTier.fromRate(5.0))
    }
}
