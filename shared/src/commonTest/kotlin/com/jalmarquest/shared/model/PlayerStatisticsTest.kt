package com.jalmarquest.shared.model

import kotlinx.serialization.json.Json
import kotlin.test.*

class PlayerStatisticsTest {
    @Test
    fun `defaults are zero and non-negative`() {
        val stats = PlayerStatistics()
        assertEquals(0, stats.deaths)
        assertEquals(0, stats.questsCompleted)
        assertEquals(0L, stats.stepsTaken)
    }

    @Test
    fun `validation prevents negatives`() {
        assertFails { PlayerStatistics(deaths = -1) }
        assertFails { PlayerStatistics(stepsTaken = -1) }
        assertFails { PlayerStatistics(damageDealt = -5) }
    }

    @Test
    fun `add combines counters`() {
        val a = PlayerStatistics(stepsTaken = 10, enemiesDefeated = 2, deaths = 1)
        val b = PlayerStatistics(stepsTaken = 5, enemiesDefeated = 3)
        val c = a.add(b)
        assertEquals(15, c.stepsTaken)
        assertEquals(5, c.enemiesDefeated)
        assertEquals(1, c.deaths)
    }

    @Test
    fun `serialization roundtrip`() {
        val json = Json { encodeDefaults = true }
        val stats = PlayerStatistics(stepsTaken = 42, puddlesCrossed = 3)
        val text = json.encodeToString(PlayerStatistics.serializer(), stats)
        val back = json.decodeFromString(PlayerStatistics.serializer(), text)
        assertEquals(stats, back)
    }
}
