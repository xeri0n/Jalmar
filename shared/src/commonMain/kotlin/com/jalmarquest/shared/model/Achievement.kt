package com.jalmarquest.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class AchievementDefinition(
    val id: String,
    val name: String,
    val description: String,
    val points: Int = 10,
    val hidden: Boolean = false
) {
    init {
        require(id.isNotBlank()) { "Achievement id cannot be blank" }
        require(name.isNotBlank()) { "Achievement name cannot be blank" }
        require(points >= 0) { "Achievement points cannot be negative" }
    }
}

@Serializable
data class AchievementProgress(
    val id: String,
    val unlocked: Boolean = false,
    val unlockedAt: Long? = null
) {
    init {
        require(id.isNotBlank()) { "Achievement id cannot be blank" }
        if (unlocked) require(unlockedAt != null) { "unlockedAt required when unlocked" }
    }
}

object AchievementsCatalog {
    // Minimal starter set; expand in future phases
    val all: List<AchievementDefinition> = listOf(
        AchievementDefinition(
            id = "first_steps",
            name = "First Steps",
            description = "Take your first steps beyond Buttonburgh.",
            points = 5
        ),
        AchievementDefinition(
            id = "twig_spear_crafted",
            name = "Twig Spear",
            description = "Craft your first Twig Spear.",
            points = 10
        ),
        AchievementDefinition(
            id = "puddle_conqueror",
            name = "Puddle Conqueror",
            description = "Brave the great lake (puddle) for the first time.",
            points = 15
        )
    )

    fun get(id: String): AchievementDefinition? = all.find { it.id == id }
}
