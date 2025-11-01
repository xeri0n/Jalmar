package com.jalmarquest.shared.model

import kotlinx.serialization.json.Json
import kotlin.test.*

class AchievementModelTest {
    @Test
    fun `definition validation`() {
        assertFails { AchievementDefinition(id = "", name = "A", description = "d") }
        assertFails { AchievementDefinition(id = "id", name = "", description = "d") }
        assertFails { AchievementDefinition(id = "id", name = "A", description = "d", points = -1) }
        val ok = AchievementDefinition(id = "id", name = "A", description = "d")
        assertEquals("id", ok.id)
    }

    @Test
    fun `progress validation`() {
        assertFails { AchievementProgress(id = "") }
        assertFails { AchievementProgress(id = "id", unlocked = true, unlockedAt = null) }
        val ok = AchievementProgress(id = "id", unlocked = true, unlockedAt = 1L)
        assertTrue(ok.unlocked)
    }

    @Test
    fun `catalog ids unique`() {
        val ids = AchievementsCatalog.all.map { it.id }
        assertEquals(ids.distinct().size, ids.size)
    }

    @Test
    fun `serialization roundtrip`() {
        val json = Json { encodeDefaults = true }
        val def = AchievementDefinition("x", "Name", "Desc", 10, hidden = true)
        val text = json.encodeToString(AchievementDefinition.serializer(), def)
        val back = json.decodeFromString(AchievementDefinition.serializer(), text)
        assertEquals(def, back)
    }
}
