package model

import kotlinx.serialization.Serializable

@Serializable
data class StatusEffect(
    val id: String,
    val name: String,
    val description: String,
    val type: Type,
    val duration: Int, // in turns
    val statModifiers: Map<String, Int> = emptyMap()
) {
    enum class Type {
        BUFF,
        DEBUFF,
        NEUTRAL
    }
}
