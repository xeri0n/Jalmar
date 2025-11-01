package dev.xeri0n.jalmarquest.ui.navigation

/**
 * Navigation routes for JalmarQuest
 * 
 * Defines all available screens in the app with type-safe routing
 * Each screen has a unique route string for NavHost navigation
 */
sealed class Screen(val route: String) {
    // ============= Main Flow =============
    
    /** Splash screen (app launch) */
    data object Splash : Screen("splash")
    
    /** Main menu (start screen with New Game, Load Game, Settings, Quit) */
    data object MainMenu : Screen("main_menu")
    
    /** Character creation (new game flow) */
    data object CharacterCreation : Screen("character_creation")
    
    /** Main game screen (HUD with tabs for different game sections) */
    data object Game : Screen("game")
    
    /** Settings screen (user preferences) */
    data object Settings : Screen("settings")
    
    /** Save/Load screen (game slot management) */
    data object SaveLoad : Screen("save_load")
    
    // ============= Game Tabs (Bottom Navigation) =============
    
    /** World map & exploration */
    data object Map : Screen("map")
    
    /** Inventory & equipment management */
    data object Inventory : Screen("inventory")
    
    /** Quest log & active quests */
    data object Quests : Screen("quests")
    
    /** Nest management & customization */
    data object Nest : Screen("nest")
    
    /** Character stats & skills */
    data object Character : Screen("character")
    
    // ============= Sub-Screens =============
    
    /** Equipment screen (separate from inventory) */
    data object Equipment : Screen("equipment")
    
    /** Crafting screen */
    data object Crafting : Screen("crafting")
    
    /** Shop screen (NPC merchants) */
    data object Shop : Screen("shop")
    
    /** Combat screen (battle interface) */
    data object Combat : Screen("combat")
    
    /** Dialogue screen (NPC conversations) */
    data object Dialogue : Screen("dialogue")
    
    /** Dungeon map (floor layout) */
    data object DungeonMap : Screen("dungeon_map")
    
    /** Cosmetic shop (nest decorations) */
    data object CosmeticShop : Screen("cosmetic_shop")
    
    /** Critter management (adopt, feed, view) */
    data object Critters : Screen("critters")
    
    /** Trophy room (achievements, boss trophies) */
    data object TrophyRoom : Screen("trophy_room")
    
    /** Hoard screen (rare item collection) */
    data object Hoard : Screen("hoard")
    
    /** Companion management */
    data object Companions : Screen("companions")
    
    /** Skill tree screen */
    data object SkillTree : Screen("skill_tree")
    
    /** Leaderboards */
    data object Leaderboards : Screen("leaderboards")
    
    /** Achievements screen */
    data object Achievements : Screen("achievements")
    
    /** Lore/Codex screen */
    data object Codex : Screen("codex")
    
    // ============= Helper Functions =============
    
    companion object {
        /**
         * Get all main game tabs for bottom navigation
         */
        fun getMainTabs(): List<Screen> = listOf(
            Map,
            Inventory,
            Quests,
            Nest,
            Character
        )
        
        /**
         * Check if route is a main tab
         */
        fun isMainTab(route: String): Boolean {
            return getMainTabs().any { it.route == route }
        }
        
        /**
         * Get screen from route string
         */
        fun fromRoute(route: String): Screen? {
            return when (route) {
                Splash.route -> Splash
                MainMenu.route -> MainMenu
                CharacterCreation.route -> CharacterCreation
                Game.route -> Game
                Settings.route -> Settings
                SaveLoad.route -> SaveLoad
                Map.route -> Map
                Inventory.route -> Inventory
                Quests.route -> Quests
                Nest.route -> Nest
                Character.route -> Character
                Equipment.route -> Equipment
                Crafting.route -> Crafting
                Shop.route -> Shop
                Combat.route -> Combat
                Dialogue.route -> Dialogue
                DungeonMap.route -> DungeonMap
                CosmeticShop.route -> CosmeticShop
                Critters.route -> Critters
                TrophyRoom.route -> TrophyRoom
                Hoard.route -> Hoard
                Companions.route -> Companions
                SkillTree.route -> SkillTree
                Leaderboards.route -> Leaderboards
                Achievements.route -> Achievements
                Codex.route -> Codex
                else -> null
            }
        }
    }
}

/**
 * Bottom navigation tab data
 * 
 * Represents a single tab in the main game bottom navigation
 */
data class NavigationTab(
    val screen: Screen,
    val label: String,
    val icon: String  // Using emoji icons for now (can replace with vector assets)
)

/**
 * Main game navigation tabs
 */
object GameTabs {
    val Map = NavigationTab(
        screen = Screen.Map,
        label = "Map",
        icon = "🗺️"
    )
    
    val Inventory = NavigationTab(
        screen = Screen.Inventory,
        label = "Inventory",
        icon = "🎒"
    )
    
    val Quests = NavigationTab(
        screen = Screen.Quests,
        label = "Quests",
        icon = "📜"
    )
    
    val Nest = NavigationTab(
        screen = Screen.Nest,
        label = "Nest",
        icon = "🏠"
    )
    
    val Character = NavigationTab(
        screen = Screen.Character,
        label = "Character",
        icon = "🐦"
    )
    
    /** Get all tabs in order */
    fun all(): List<NavigationTab> = listOf(
        Map,
        Inventory,
        Quests,
        Nest,
        Character
    )
}
