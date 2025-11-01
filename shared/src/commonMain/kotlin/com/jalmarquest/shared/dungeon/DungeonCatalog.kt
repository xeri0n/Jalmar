package com.jalmarquest.shared.dungeon

/**
 * Static catalog of all dungeons in the game.
 * Follows the established pattern from LocationCatalog, EnemyCatalog, ItemCatalog.
 */
object DungeonCatalog {
    
    /**
     * All available dungeons in the game.
     */
    val allDungeons: List<Dungeon> = listOf(
        // ========== EASY DIFFICULTY ==========
        
        Dungeon(
            id = "abandoned_burrow",
            name = "Abandoned Burrow",
            description = "A dark network of tunnels beneath the garden, once home to a family of moles. " +
                    "The musty air carries whispers of scuttling creatures and the faint scent of earth. " +
                    "Perfect for a fledgling adventurer.",
            theme = "Underground Tunnels",
            difficulty = DungeonDifficulty.EASY,
            baseLevel = 1,
            floorCount = 3,
            rewardXpBonus = 1.2f,
            guaranteedLoot = listOf("twig_spear", "beetle_shell")
        ),
        
        Dungeon(
            id = "compost_heap_depths",
            name = "Compost Heap Depths",
            description = "A decaying mound of organic matter teeming with life. The warm, rotting layers " +
                    "harbor insects of all kinds. The stench is overwhelming, but the rewards are worth it.",
            theme = "Decay & Decomposition",
            difficulty = DungeonDifficulty.EASY,
            baseLevel = 2,
            floorCount = 4,
            rewardXpBonus = 1.3f,
            guaranteedLoot = listOf("grub_jerky", "compost_fertilizer")
        ),
        
        // ========== MEDIUM DIFFICULTY ==========
        
        Dungeon(
            id = "garden_gnome_fortress",
            name = "Garden Gnome Fortress",
            description = "A towering statue of a ceramic gnome, its hollow interior converted into a " +
                    "multi-level fortress by territorial beetles. Narrow passages wind upward through " +
                    "the gnome's body, culminating in a throne room within its head.",
            theme = "Ceramic Stronghold",
            difficulty = DungeonDifficulty.MEDIUM,
            baseLevel = 6,
            floorCount = 5,
            rewardXpBonus = 1.5f,
            guaranteedLoot = listOf("gnome_hat_helmet", "ceramic_shard_dagger")
        ),
        
        Dungeon(
            id = "rainwater_gutter_maze",
            name = "Rainwater Gutter Maze",
            description = "A labyrinth of metal channels and pipes collecting rainwater from the roof. " +
                    "Water bugs patrol the slick surfaces, and the constant dripping echoes ominously. " +
                    "One wrong step could mean a deadly fall.",
            theme = "Aquatic Labyrinth",
            difficulty = DungeonDifficulty.MEDIUM,
            baseLevel = 8,
            floorCount = 5,
            rewardXpBonus = 1.6f,
            guaranteedLoot = listOf("water_bug_carapace", "rust_proof_armor")
        ),
        
        // ========== HARD DIFFICULTY ==========
        
        Dungeon(
            id = "old_tool_shed_ruins",
            name = "Old Tool Shed Ruins",
            description = "The crumbling remains of a once-great structure. Rusted tools tower like " +
                    "monuments, and the wooden beams groan under the weight of time. Centipedes and " +
                    "other predators have claimed this territory as their hunting ground.",
            theme = "Rusted Ruins",
            difficulty = DungeonDifficulty.HARD,
            baseLevel = 16,
            floorCount = 7,
            rewardXpBonus = 2.0f,
            guaranteedLoot = listOf("rusted_nail_sword", "tool_shed_key", "ancient_seed_cache")
        ),
        
        // Future dungeons (Phase 9+ content):
        // - "Spider's Web Cathedral" (EXPERT, level 31+)
        // - "The Great Firefly Lantern" (EXPERT, level 36+)
        // - "Titan's Footprint Crater" (LEGENDARY, level 46+, endgame raid)
    )
    
    /**
     * Retrieves a dungeon by its unique ID.
     * 
     * @param dungeonId The dungeon identifier
     * @return The dungeon, or null if not found
     */
    fun getDungeon(dungeonId: String): Dungeon? {
        return allDungeons.find { it.id == dungeonId }
    }
    
    /**
     * Retrieves all dungeons within a specific difficulty tier.
     * 
     * @param difficulty The difficulty tier to filter by
     * @return List of dungeons matching the difficulty
     */
    fun getDungeonsByDifficulty(difficulty: DungeonDifficulty): List<Dungeon> {
        return allDungeons.filter { it.difficulty == difficulty }
    }
    
    /**
     * Retrieves all dungeons suitable for a player level.
     * Returns dungeons where baseLevel <= playerLevel <= (baseLevel + floorCount).
     * 
     * @param playerLevel The player's current level
     * @return List of level-appropriate dungeons
     */
    fun getDungeonsForLevel(playerLevel: Int): List<Dungeon> {
        return allDungeons.filter { dungeon ->
            val minLevel = dungeon.baseLevel
            val maxLevel = dungeon.baseLevel + dungeon.floorCount
            playerLevel in minLevel..maxLevel
        }
    }
    
    /**
     * Retrieves all dungeons by theme keyword.
     * 
     * @param themeKeyword Keyword to search for (case-insensitive)
     * @return List of dungeons matching the theme
     */
    fun getDungeonsByTheme(themeKeyword: String): List<Dungeon> {
        val keyword = themeKeyword.lowercase()
        return allDungeons.filter { it.theme.lowercase().contains(keyword) }
    }
    
    /**
     * Returns total dungeon count (for validation/stats).
     */
    fun getTotalDungeonCount(): Int = allDungeons.size
    
    /**
     * Validates that all dungeons have unique IDs.
     * Throws IllegalStateException if duplicates found.
     */
    fun validateCatalog() {
        val ids = allDungeons.map { it.id }
        val duplicates = ids.groupingBy { it }.eachCount().filter { it.value > 1 }
        
        if (duplicates.isNotEmpty()) {
            throw IllegalStateException("Duplicate dungeon IDs found: ${duplicates.keys}")
        }
    }
}
