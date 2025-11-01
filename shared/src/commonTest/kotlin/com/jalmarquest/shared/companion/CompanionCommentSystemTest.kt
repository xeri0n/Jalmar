package com.jalmarquest.shared.companion

import com.jalmarquest.shared.world.BiomeType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests for CompanionCommentSystem.
 * Verifies context-aware companion comments are generated correctly.
 */
class CompanionCommentSystemTest {
    
    // ========== COMMENT GENERATION TESTS ==========
    
    @Test
    fun `generateComment returns NoComment when cooldown not met`() {
        val context = CompanionCommentSystem.CommentContext(
            companionId = "pip_young_quail",
            loyaltyScore = 50,
            timeSinceLastComment = 2 // Less than 5 minutes
        )
        
        val result = CompanionCommentSystem.generateComment(context)
        
        assertTrue(result is CompanionCommentSystem.CommentResult.NoComment)
    }
    
    @Test
    fun `generateComment returns NoComment when companion not found`() {
        val context = CompanionCommentSystem.CommentContext(
            companionId = "nonexistent_companion",
            loyaltyScore = 50,
            timeSinceLastComment = 10
        )
        
        val result = CompanionCommentSystem.generateComment(context)
        
        assertTrue(result is CompanionCommentSystem.CommentResult.NoComment)
    }
    
    @Test
    fun `generateComment returns combat comment when entering combat`() {
        val context = CompanionCommentSystem.CommentContext(
            companionId = "pip_young_quail",
            loyaltyScore = 75, // Loyal tier
            recentEvent = CompanionCommentSystem.CompanionEvent.ENTERED_COMBAT,
            timeSinceLastComment = 10
        )
        
        val result = CompanionCommentSystem.generateComment(context)
        
        assertTrue(result is CompanionCommentSystem.CommentResult.Comment)
        val comment = result as CompanionCommentSystem.CommentResult.Comment
        assertEquals("pip_young_quail", comment.companionId)
        assertEquals("Pip", comment.companionName)
        assertTrue(comment.text.isNotBlank())
    }
    
    @Test
    fun `generateComment returns victory comment when combat won`() {
        val context = CompanionCommentSystem.CommentContext(
            companionId = "pip_young_quail",
            loyaltyScore = 50,
            recentEvent = CompanionCommentSystem.CompanionEvent.WON_COMBAT,
            timeSinceLastComment = 10
        )
        
        val result = CompanionCommentSystem.generateComment(context)
        
        assertTrue(result is CompanionCommentSystem.CommentResult.Comment)
        val comment = result as CompanionCommentSystem.CommentResult.Comment
        assertEquals("We did it! That was amazing!", comment.text)
    }
    
    @Test
    fun `generateComment returns biome comment for grassland`() {
        val context = CompanionCommentSystem.CommentContext(
            companionId = "pip_young_quail",
            loyaltyScore = 60, // Friendly tier
            currentBiome = BiomeType.GRASSLAND,
            timeSinceLastComment = 10
        )
        
        val result = CompanionCommentSystem.generateComment(context)
        
        assertTrue(result is CompanionCommentSystem.CommentResult.Comment)
        val comment = result as CompanionCommentSystem.CommentResult.Comment
        assertTrue(comment.text.contains("grass") || comment.text.contains("green"))
    }
    
    @Test
    fun `generateComment returns NoComment for distrustful companion with biome`() {
        val context = CompanionCommentSystem.CommentContext(
            companionId = "pip_young_quail",
            loyaltyScore = 10, // Distrustful tier
            currentBiome = BiomeType.GRASSLAND,
            timeSinceLastComment = 10
        )
        
        val result = CompanionCommentSystem.generateComment(context)
        
        // Distrustful companions don't comment on biomes
        assertTrue(result is CompanionCommentSystem.CommentResult.NoComment)
    }
    
    // ========== LOYALTY-BASED COMMENT VARIATION TESTS ==========
    
    @Test
    fun `Pip comments reflect loyalty level on combat entry`() {
        // Distrustful
        val distrustfulContext = CompanionCommentSystem.CommentContext(
            companionId = "pip_young_quail",
            loyaltyScore = 15,
            recentEvent = CompanionCommentSystem.CompanionEvent.ENTERED_COMBAT,
            timeSinceLastComment = 10
        )
        
        val distrustfulResult = CompanionCommentSystem.generateComment(distrustfulContext)
        assertTrue(distrustfulResult is CompanionCommentSystem.CommentResult.Comment)
        assertTrue((distrustfulResult as CompanionCommentSystem.CommentResult.Comment).text.contains("guess"))
        
        // Devoted
        val devotedContext = CompanionCommentSystem.CommentContext(
            companionId = "pip_young_quail",
            loyaltyScore = 100,
            recentEvent = CompanionCommentSystem.CompanionEvent.ENTERED_COMBAT,
            timeSinceLastComment = 10
        )
        
        val devotedResult = CompanionCommentSystem.generateComment(devotedContext)
        assertTrue(devotedResult is CompanionCommentSystem.CommentResult.Comment)
        assertTrue((devotedResult as CompanionCommentSystem.CommentResult.Comment).text.contains("unstoppable"))
    }
    
    @Test
    fun `Grumble comments reflect personality`() {
        val context = CompanionCommentSystem.CommentContext(
            companionId = "grumble_forgepaw",
            loyaltyScore = 50,
            recentEvent = CompanionCommentSystem.CompanionEvent.WON_COMBAT,
            timeSinceLastComment = 10
        )
        
        val result = CompanionCommentSystem.generateComment(context)
        
        assertTrue(result is CompanionCommentSystem.CommentResult.Comment)
        val comment = result as CompanionCommentSystem.CommentResult.Comment
        assertEquals("Adequate work.", comment.text) // Grumble is succinct
    }
    
    @Test
    fun `Skitter comments are energetic`() {
        val context = CompanionCommentSystem.CommentContext(
            companionId = "skitter_beetle",
            loyaltyScore = 60,
            recentEvent = CompanionCommentSystem.CompanionEvent.WON_COMBAT,
            timeSinceLastComment = 10
        )
        
        val result = CompanionCommentSystem.generateComment(context)
        
        assertTrue(result is CompanionCommentSystem.CommentResult.Comment)
        val comment = result as CompanionCommentSystem.CommentResult.Comment
        assertEquals("That was FUN! Can we do it again?", comment.text)
    }
    
    // ========== BIOME-SPECIFIC COMMENT TESTS ==========
    
    @Test
    fun `Whisker provides tactical biome comments`() {
        val grasslandContext = CompanionCommentSystem.CommentContext(
            companionId = "whisker_explorer",
            loyaltyScore = 70,
            currentBiome = BiomeType.GRASSLAND,
            timeSinceLastComment = 10
        )
        
        val grasslandResult = CompanionCommentSystem.generateComment(grasslandContext)
        assertTrue(grasslandResult is CompanionCommentSystem.CommentResult.Comment)
        val grasslandComment = (grasslandResult as CompanionCommentSystem.CommentResult.Comment).text
        assertEquals("Good sightlines here. I can see danger coming.", grasslandComment)
        
        val forestContext = CompanionCommentSystem.CommentContext(
            companionId = "whisker_explorer",
            loyaltyScore = 70,
            currentBiome = BiomeType.FOREST,
            timeSinceLastComment = 10
        )
        
        val forestResult = CompanionCommentSystem.generateComment(forestContext)
        assertTrue(forestResult is CompanionCommentSystem.CommentResult.Comment)
        val forestComment = (forestResult as CompanionCommentSystem.CommentResult.Comment).text
        assertEquals("Lots of cover. Could be ambush territory.", forestComment)
    }
    
    @Test
    fun `Ember comments on heat-related biomes`() {
        val desertContext = CompanionCommentSystem.CommentContext(
            companionId = "ember_firefly",
            loyaltyScore = 65,
            currentBiome = BiomeType.DESERT,
            timeSinceLastComment = 10
        )
        
        val result = CompanionCommentSystem.generateComment(desertContext)
        
        assertTrue(result is CompanionCommentSystem.CommentResult.Comment)
        val comment = result as CompanionCommentSystem.CommentResult.Comment
        assertEquals("*glows warmly* The heat here feels like home.", comment.text)
    }
    
    @Test
    fun `Grumble comments on cave biome familiarity`() {
        val context = CompanionCommentSystem.CommentContext(
            companionId = "grumble_forgepaw",
            loyaltyScore = 55,
            currentBiome = BiomeType.CAVE,
            timeSinceLastComment = 10
        )
        
        val result = CompanionCommentSystem.generateComment(context)
        
        assertTrue(result is CompanionCommentSystem.CommentResult.Comment)
        val comment = result as CompanionCommentSystem.CommentResult.Comment
        assertTrue(comment.text.contains("earth") || comment.text.contains("home"))
    }
    
    // ========== EVENT-SPECIFIC COMMENT TESTS ==========
    
    @Test
    fun `All companions have combat entry comments with variety`() {
        val companionIds = listOf(
            "pip_young_quail",
            "grumble_forgepaw",
            "whisker_explorer",
            "ember_firefly",
            "skitter_beetle",
            "swoop_sparrow",
            "shimmer_dew_spirit",
            "thorn_hedgehog",
            "clover_ladybug",
            "rumble_toad_sage"
        )
        
        val comments = companionIds.mapNotNull { id ->
            val context = CompanionCommentSystem.CommentContext(
                companionId = id,
                loyaltyScore = 50,
                recentEvent = CompanionCommentSystem.CompanionEvent.ENTERED_COMBAT,
                timeSinceLastComment = 10
            )
            
            val result = CompanionCommentSystem.generateComment(context)
            if (result is CompanionCommentSystem.CommentResult.Comment) {
                result.text
            } else {
                null
            }
        }
        
        // Most companions should have combat entry comments
        assertTrue(comments.size >= 8, "At least 8 companions should comment on combat entry, got ${comments.size}")
        
        // Comments should be distinct
        val uniqueComments = comments.toSet()
        assertEquals(comments.size, uniqueComments.size, "Each companion should have unique combat entry comment")
    }
    
    @Test
    fun `Shimmer focuses on healing in low HP events`() {
        val context = CompanionCommentSystem.CommentContext(
            companionId = "shimmer_dew_spirit",
            loyaltyScore = 60,
            recentEvent = CompanionCommentSystem.CompanionEvent.PLAYER_LOW_HP,
            timeSinceLastComment = 10
        )
        
        val result = CompanionCommentSystem.generateComment(context)
        
        assertTrue(result is CompanionCommentSystem.CommentResult.Comment)
        val comment = result as CompanionCommentSystem.CommentResult.Comment
        assertEquals("*wings glow* Let me help heal you.", comment.text)
    }
    
    @Test
    fun `Thorn is protective when player low HP`() {
        val context = CompanionCommentSystem.CommentContext(
            companionId = "thorn_hedgehog",
            loyaltyScore = 70,
            recentEvent = CompanionCommentSystem.CompanionEvent.PLAYER_LOW_HP,
            timeSinceLastComment = 10
        )
        
        val result = CompanionCommentSystem.generateComment(context)
        
        assertTrue(result is CompanionCommentSystem.CommentResult.Comment)
        val comment = result as CompanionCommentSystem.CommentResult.Comment
        assertTrue(comment.text.contains("behind") || comment.text.contains("protect"))
    }
    
    @Test
    fun `Clover maintains optimism even when fleeing`() {
        val context = CompanionCommentSystem.CommentContext(
            companionId = "clover_ladybug",
            loyaltyScore = 55,
            recentEvent = CompanionCommentSystem.CompanionEvent.FLED_COMBAT,
            timeSinceLastComment = 10
        )
        
        val result = CompanionCommentSystem.generateComment(context)
        
        assertTrue(result is CompanionCommentSystem.CommentResult.Comment)
        val comment = result as CompanionCommentSystem.CommentResult.Comment
        assertTrue(comment.text.contains("lucky") || comment.text.contains("luckiest"))
    }
}
