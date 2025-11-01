package com.jalmarquest.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerStatistics(
    val stepsTaken: Long = 0,
    val enemiesDefeated: Long = 0,
    val deaths: Int = 0,
    val seedsCollected: Long = 0,
    val itemsCrafted: Long = 0,
    val questsCompleted: Int = 0,
    val damageDealt: Long = 0,
    val damageTaken: Long = 0,
    val puddlesCrossed: Int = 0,
    val gnomesSpotted: Int = 0
) {
    init {
        require(stepsTaken >= 0) { "stepsTaken cannot be negative" }
        require(enemiesDefeated >= 0) { "enemiesDefeated cannot be negative" }
        require(deaths >= 0) { "deaths cannot be negative" }
        require(seedsCollected >= 0) { "seedsCollected cannot be negative" }
        require(itemsCrafted >= 0) { "itemsCrafted cannot be negative" }
        require(questsCompleted >= 0) { "questsCompleted cannot be negative" }
        require(damageDealt >= 0) { "damageDealt cannot be negative" }
        require(damageTaken >= 0) { "damageTaken cannot be negative" }
        require(puddlesCrossed >= 0) { "puddlesCrossed cannot be negative" }
        require(gnomesSpotted >= 0) { "gnomesSpotted cannot be negative" }
    }

    fun add(other: PlayerStatistics): PlayerStatistics = copy(
        stepsTaken = stepsTaken + other.stepsTaken,
        enemiesDefeated = enemiesDefeated + other.enemiesDefeated,
        deaths = deaths + other.deaths,
        seedsCollected = seedsCollected + other.seedsCollected,
        itemsCrafted = itemsCrafted + other.itemsCrafted,
        questsCompleted = questsCompleted + other.questsCompleted,
        damageDealt = damageDealt + other.damageDealt,
        damageTaken = damageTaken + other.damageTaken,
        puddlesCrossed = puddlesCrossed + other.puddlesCrossed,
        gnomesSpotted = gnomesSpotted + other.gnomesSpotted
    )
}
