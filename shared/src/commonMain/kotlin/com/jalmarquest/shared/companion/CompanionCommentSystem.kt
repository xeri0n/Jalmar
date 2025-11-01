package com.jalmarquest.shared.companion

import com.jalmarquest.shared.world.BiomeType
import kotlinx.serialization.Serializable

/**
 * System for generating context-aware companion comments during gameplay.
 * Companions will interject with relevant dialogue based on:
 * - Current location/biome
 * - Recent combat events
 * - Player actions
 * - Loyalty level
 * 
 * Design Philosophy:
 * - Comments add personality without being intrusive
 * - Frequency limited to avoid spam
 * - Context-aware based on game state
 * - Loyalty affects tone and depth of comments
 */
object CompanionCommentSystem {
    
    /**
     * Context for generating companion comments.
     */
    @Serializable
    data class CommentContext(
        val companionId: String,
        val loyaltyScore: Int,
        val currentBiome: BiomeType? = null,
        val recentEvent: CompanionEvent? = null,
        val timeSinceLastComment: Int = 0 // in game minutes
    )
    
    /**
     * Events that can trigger companion comments.
     */
    enum class CompanionEvent {
        ENTERED_COMBAT,
        WON_COMBAT,
        FLED_COMBAT,
        PLAYER_LOW_HP,
        COMPANION_LOW_HP,
        DISCOVERED_LOCATION,
        BIOME_TRANSITION,
        LEVEL_UP,
        QUEST_ACCEPTED,
        QUEST_COMPLETED,
        ITEM_FOUND,
        REST_STARTED,
        LONG_JOURNEY
    }
    
    /**
     * Result of comment generation.
     */
    sealed class CommentResult {
        /**
         * Comment generated successfully.
         */
        data class Comment(
            val text: String,
            val companionId: String,
            val companionName: String
        ) : CommentResult()
        
        /**
         * No comment generated (too soon, no relevant context, etc.)
         */
        object NoComment : CommentResult()
    }
    
    /**
     * Generates a companion comment based on context.
     * Returns null if no comment should be shown (cooldown, no relevant comment, etc.)
     * 
     * @param context Current game context
     * @return Comment if one should be displayed
     */
    fun generateComment(context: CommentContext): CommentResult {
        // Cooldown: Don't comment more than once every 5 minutes
        if (context.timeSinceLastComment < 5) {
            return CommentResult.NoComment
        }
        
        // Get companion data
        val companion = CompanionCatalog.getCompanionById(context.companionId)
            ?: return CommentResult.NoComment
        
        // Priority 1: React to events
        context.recentEvent?.let { event ->
            getEventComment(companion, event, context.loyaltyScore)?.let { comment ->
                return CommentResult.Comment(comment, companion.id, companion.name)
            }
        }
        
        // Priority 2: React to biome
        context.currentBiome?.let { biome ->
            getBiomeComment(companion, biome, context.loyaltyScore)?.let { comment ->
                return CommentResult.Comment(comment, companion.id, companion.name)
            }
        }
        
        return CommentResult.NoComment
    }
    
    /**
     * Gets a comment for a specific event.
     */
    private fun getEventComment(
        companion: Companion,
        event: CompanionEvent,
        loyaltyScore: Int
    ): String? {
        // Comments vary by companion personality and loyalty
        val loyaltyTier = CompanionLoyaltyStatus.fromScore(loyaltyScore)
        
        return when (companion.id) {
            "pip_young_quail" -> getPipEventComment(event, loyaltyTier)
            "grumble_forgepaw" -> getGrumbleEventComment(event, loyaltyTier)
            "whisker_explorer" -> getWhiskerEventComment(event, loyaltyTier)
            "ember_firefly" -> getEmberEventComment(event, loyaltyTier)
            "skitter_beetle" -> getSkitterEventComment(event, loyaltyTier)
            "swoop_sparrow" -> getSwoopEventComment(event, loyaltyTier)
            "shimmer_dew_spirit" -> getShimmerEventComment(event, loyaltyTier)
            "thorn_hedgehog" -> getThornEventComment(event, loyaltyTier)
            "clover_ladybug" -> getCloverEventComment(event, loyaltyTier)
            "rumble_toad_sage" -> getRumbleEventComment(event, loyaltyTier)
            else -> null
        }
    }
    
    /**
     * Gets a comment for entering a specific biome.
     */
    private fun getBiomeComment(
        companion: Companion,
        biome: BiomeType,
        loyaltyScore: Int
    ): String? {
        val loyaltyTier = CompanionLoyaltyStatus.fromScore(loyaltyScore)
        
        // Only comment on biome changes if loyalty is at least Neutral
        if (loyaltyTier == CompanionLoyaltyStatus.DISTRUSTFUL || loyaltyTier == CompanionLoyaltyStatus.NEUTRAL) {
            return null
        }
        
        return when (companion.id) {
            "pip_young_quail" -> getPipBiomeComment(biome)
            "grumble_forgepaw" -> getGrumbleBiomeComment(biome)
            "whisker_explorer" -> getWhiskerBiomeComment(biome)
            "ember_firefly" -> getEmberBiomeComment(biome)
            "skitter_beetle" -> getSkitterBiomeComment(biome)
            "swoop_sparrow" -> getSwoopBiomeComment(biome)
            "shimmer_dew_spirit" -> getShimmerBiomeComment(biome)
            "thorn_hedgehog" -> getThornBiomeComment(biome)
            "clover_ladybug" -> getCloverBiomeComment(biome)
            "rumble_toad_sage" -> getRumbleBiomeComment(biome)
            else -> null
        }
    }
    
    // ========== PIP EVENT COMMENTS ==========
    
    private fun getPipEventComment(event: CompanionEvent, tier: CompanionLoyaltyStatus): String? {
        return when (event) {
            CompanionEvent.ENTERED_COMBAT -> when (tier) {
                CompanionLoyaltyStatus.DISTRUSTFUL -> "I... I guess we're fighting?"
                CompanionLoyaltyStatus.NEUTRAL -> "Okay, let's do this!"
                CompanionLoyaltyStatus.FRIENDLY -> "I've got your back!"
                CompanionLoyaltyStatus.LOYAL, CompanionLoyaltyStatus.DEVOTED -> "Together we're unstoppable!"
            }
            CompanionEvent.WON_COMBAT -> "We did it! That was amazing!"
            CompanionEvent.PLAYER_LOW_HP -> "Are you okay?! Please be careful!"
            CompanionEvent.DISCOVERED_LOCATION -> "Wow! I've never been here before!"
            CompanionEvent.LEVEL_UP -> "You're getting so strong! I hope I can keep up!"
            else -> null
        }
    }
    
    private fun getPipBiomeComment(biome: BiomeType): String? {
        return when (biome) {
            BiomeType.GRASSLAND -> "The grass is so tall here! Like a forest of green!"
            BiomeType.FOREST -> "It's a bit dark under these trees..."
            BiomeType.MOUNTAIN -> "This climb is tough, but the view is worth it!"
            BiomeType.SWAMP -> "Ugh, my feet are getting all muddy."
            else -> null
        }
    }
    
    // ========== GRUMBLE EVENT COMMENTS ==========
    
    private fun getGrumbleEventComment(event: CompanionEvent, tier: CompanionLoyaltyStatus): String? {
        return when (event) {
            CompanionEvent.ENTERED_COMBAT -> when (tier) {
                CompanionLoyaltyStatus.DISTRUSTFUL -> "*sighs* Fine. Let's get this over with."
                CompanionLoyaltyStatus.NEUTRAL -> "Keep your guard up."
                CompanionLoyaltyStatus.FRIENDLY -> "Stand behind me if you need to."
                CompanionLoyaltyStatus.LOYAL, CompanionLoyaltyStatus.DEVOTED -> "For you, I'll fight a hundred battles."
            }
            CompanionEvent.WON_COMBAT -> "Adequate work."
            CompanionEvent.ITEM_FOUND -> "Hmm, that could be useful for crafting."
            CompanionEvent.LONG_JOURNEY -> "You're tougher than you look."
            else -> null
        }
    }
    
    private fun getGrumbleBiomeComment(biome: BiomeType): String? {
        return when (biome) {
            BiomeType.CAVE -> "*sniffs* Good earth here. Reminds me of home."
            BiomeType.MOUNTAIN -> "Rocky terrain. Watch your step."
            BiomeType.SWAMP -> "Terrible conditions for metalwork. Too humid."
            else -> null
        }
    }
    
    // ========== WHISKER EVENT COMMENTS ==========
    
    private fun getWhiskerEventComment(event: CompanionEvent, tier: CompanionLoyaltyStatus): String? {
        return when (event) {
            CompanionEvent.ENTERED_COMBAT -> "*whiskers twitch* Stay sharp."
            CompanionEvent.DISCOVERED_LOCATION -> "I'll scout ahead. Wait for my signal."
            CompanionEvent.BIOME_TRANSITION -> "Terrain's changing. Stay alert."
            CompanionEvent.FLED_COMBAT -> "Smart move. Living to fight another day."
            else -> null
        }
    }
    
    private fun getWhiskerBiomeComment(biome: BiomeType): String? {
        return when (biome) {
            BiomeType.GRASSLAND -> "Good sightlines here. I can see danger coming."
            BiomeType.FOREST -> "Lots of cover. Could be ambush territory."
            BiomeType.DESERT -> "Open spaces. Nowhere to hide."
            else -> null
        }
    }
    
    // ========== EMBER EVENT COMMENTS ==========
    
    private fun getEmberEventComment(event: CompanionEvent, tier: CompanionLoyaltyStatus): String? {
        return when (event) {
            CompanionEvent.ENTERED_COMBAT -> "*glow intensifies* The fire within is ready."
            CompanionEvent.WON_COMBAT -> "We burned bright today."
            CompanionEvent.REST_STARTED -> "Rest well. The fire will keep watch."
            CompanionEvent.LEVEL_UP -> "Your inner flame grows stronger."
            else -> null
        }
    }
    
    private fun getEmberBiomeComment(biome: BiomeType): String? {
        return when (biome) {
            BiomeType.DESERT -> "*glows warmly* The heat here feels like home."
            BiomeType.SWAMP -> "The dampness... it dims the light."
            BiomeType.TUNDRA -> "Even here, warmth persists. As it must."
            else -> null
        }
    }
    
    // ========== SKITTER EVENT COMMENTS ==========
    
    private fun getSkitterEventComment(event: CompanionEvent, tier: CompanionLoyaltyStatus): String? {
        return when (event) {
            CompanionEvent.ENTERED_COMBAT -> "*bounces* Time to jump into action!"
            CompanionEvent.WON_COMBAT -> "That was FUN! Can we do it again?"
            CompanionEvent.DISCOVERED_LOCATION -> "NEW PLACE! So many things to see!"
            CompanionEvent.ITEM_FOUND -> "Ooh ooh, what's that?! Can I see?"
            CompanionEvent.LONG_JOURNEY -> "Are we there yet? How about now? Now?"
            else -> null
        }
    }
    
    private fun getSkitterBiomeComment(biome: BiomeType): String? {
        return when (biome) {
            BiomeType.GRASSLAND -> "SO much grass to jump through! Wheee!"
            BiomeType.FOREST -> "Look at all these leaves! *crunch crunch*"
            BiomeType.MOUNTAIN -> "Jumping up hills is EXTRA fun!"
            else -> null
        }
    }
    
    // ========== SWOOP EVENT COMMENTS ==========
    
    private fun getSwoopEventComment(event: CompanionEvent, tier: CompanionLoyaltyStatus): String? {
        return when (event) {
            CompanionEvent.ENTERED_COMBAT -> "*hovers above* I'll watch from the air."
            CompanionEvent.DISCOVERED_LOCATION -> "From above, I can see the layout. Follow me."
            CompanionEvent.BIOME_TRANSITION -> "The winds are changing."
            else -> null
        }
    }
    
    private fun getSwoopBiomeComment(biome: BiomeType): String? {
        return when (biome) {
            BiomeType.COASTAL -> "The sea breeze makes flying effortless."
            BiomeType.MOUNTAIN -> "The updrafts here are powerful."
            BiomeType.SWAMP -> "Heavy air. Hard to fly."
            else -> null
        }
    }
    
    // ========== SHIMMER EVENT COMMENTS ==========
    
    private fun getShimmerEventComment(event: CompanionEvent, tier: CompanionLoyaltyStatus): String? {
        return when (event) {
            CompanionEvent.PLAYER_LOW_HP -> "*wings glow* Let me help heal you."
            CompanionEvent.COMPANION_LOW_HP -> "I'll be alright. Don't worry."
            CompanionEvent.REST_STARTED -> "Rest brings renewal."
            CompanionEvent.WON_COMBAT -> "Balance is restored."
            else -> null
        }
    }
    
    private fun getShimmerBiomeComment(biome: BiomeType): String? {
        return when (biome) {
            BiomeType.FOREST -> "*flutters between flowers* Such beauty here."
            BiomeType.GRASSLAND -> "The wildflowers are in bloom."
            BiomeType.SWAMP -> "Even in murky places, life persists."
            else -> null
        }
    }
    
    // ========== THORN EVENT COMMENTS ==========
    
    private fun getThornEventComment(event: CompanionEvent, tier: CompanionLoyaltyStatus): String? {
        return when (event) {
            CompanionEvent.ENTERED_COMBAT -> "*curls defensively* I'll protect you."
            CompanionEvent.PLAYER_LOW_HP -> "Get behind me! Now!"
            CompanionEvent.WON_COMBAT -> "They won't hurt you. Not while I'm here."
            else -> null
        }
    }
    
    private fun getThornBiomeComment(biome: BiomeType): String? {
        return when (biome) {
            BiomeType.FOREST -> "Good cover here. Defensible."
            BiomeType.DESERT -> "Too open. I don't like it."
            BiomeType.CAVE -> "Confined space. Easy to defend."
            else -> null
        }
    }
    
    // ========== CLOVER EVENT COMMENTS ==========
    
    private fun getCloverEventComment(event: CompanionEvent, tier: CompanionLoyaltyStatus): String? {
        return when (event) {
            CompanionEvent.ENTERED_COMBAT -> "Don't worry! Our luck will see us through!"
            CompanionEvent.WON_COMBAT -> "See? I told you we'd be fine!"
            CompanionEvent.ITEM_FOUND -> "Ooh, lucky find!"
            CompanionEvent.DISCOVERED_LOCATION -> "What a delightful place!"
            CompanionEvent.FLED_COMBAT -> "Sometimes the luckiest thing is knowing when to run!"
            else -> null
        }
    }
    
    private fun getCloverBiomeComment(biome: BiomeType): String? {
        return when (biome) {
            BiomeType.GRASSLAND -> "*hums cheerfully* Perfect day for an adventure!"
            BiomeType.FOREST -> "I love the shade of the trees!"
            BiomeType.SWAMP -> "A bit muddy, but that's part of the fun!"
            else -> null
        }
    }
    
    // ========== RUMBLE EVENT COMMENTS ==========
    
    private fun getRumbleEventComment(event: CompanionEvent, tier: CompanionLoyaltyStatus): String? {
        return when (event) {
            CompanionEvent.ENTERED_COMBAT -> "*rumbles* Let's roll."
            CompanionEvent.WON_COMBAT -> "Good work."
            CompanionEvent.LONG_JOURNEY -> "Long journey builds strength."
            CompanionEvent.ITEM_FOUND -> "*sniffs* Useful."
            else -> null
        }
    }
    
    private fun getRumbleBiomeComment(biome: BiomeType): String? {
        return when (biome) {
            BiomeType.GRASSLAND -> "Good rolling terrain."
            BiomeType.MOUNTAIN -> "Uphill. Slow but steady."
            BiomeType.SWAMP -> "Ground's too soft for rolling."
            else -> null
        }
    }
}
