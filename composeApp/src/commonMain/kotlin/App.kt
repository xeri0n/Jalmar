package composeApp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import integration.GameIntegrationCoordinator
import ui.navigation.MainNavigationHub
import org.koin.core.context.GlobalContext.get
import org.koin.core.parameter.parametersOf

@Composable
fun App() {
    val integrationCoordinator: GameIntegrationCoordinator = get { parametersOf() }
    
    LaunchedEffect(Unit) {
        // Initialize all system integrations on app start
        integrationCoordinator.initialize()
    }
    
    // Replace or enhance existing navigation with MainNavigationHub
    when (currentScreen) {
        Screen.MAIN_HUB -> MainNavigationHub(
            onNavigateToHub = { currentScreen = Screen.HUB },
            onNavigateToExplore = { currentScreen = Screen.EXPLORE },
            onNavigateToNest = { currentScreen = Screen.NEST },
            onNavigateToInventory = { currentScreen = Screen.INVENTORY },
            onNavigateToSkills = { currentScreen = Screen.SKILLS },
            onNavigateToCrafting = { currentScreen = Screen.CRAFTING },
            onNavigateToQuests = { currentScreen = Screen.QUESTS },
            onNavigateToThoughts = { currentScreen = Screen.THOUGHTS },
            onNavigateToConcoctions = { currentScreen = Screen.CONCOCTIONS },
            onNavigateToWorldInfo = { currentScreen = Screen.WORLD_INFO },
            onNavigateToChronicle = { currentScreen = Screen.SEASONAL_CHRONICLE },
            onNavigateToShop = { currentScreen = Screen.SHOP },
            onNavigateToCompanions = { currentScreen = Screen.COMPANIONS },
            onNavigateToFactions = { currentScreen = Screen.FACTIONS },
            onNavigateToSettings = { currentScreen = Screen.SETTINGS },
            onNavigateToSaveLoad = { currentScreen = Screen.SAVE_LOAD }
        )
        // ...existing screen cases...
    }
}