package com.jalmarquest.shared.currency

import kotlinx.serialization.Serializable

/**
 * Types of currency in JalmarQuest.
 */
@Serializable
enum class CurrencyType {
    /** Common currency earned through gameplay (Seeds) */
    SEEDS,
    
    /** Premium currency for special items and features (Glimmer Shards) */
    GLIMMER_SHARDS
}
