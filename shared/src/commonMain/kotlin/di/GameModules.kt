package di

import integration.GameIntegrationCoordinator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module
import stub.*

val integrationModule = module {
    // Core integration coordinator
    single {
        GameIntegrationCoordinator(
            gameStateManager = get(),
            saveManager = get(),
            timeManager = get(),
            locationManager = get(),
            movementManager = get(),
            inventoryManager = get(),
            currencyManager = get(),
            equipmentManager = get(),
            craftingManager = get(),
            combatManager = get(),
            questManager = get(),
            dialogueManager = get(),
            npcManager = get(),
            factionManager = get(),
            skillManager = get(),
            nestManager = get(),
            companionManager = get(),
            dungeonManager = get(),
            resourceNodeManager = get(),
            thoughtCabinetManager = get(),
            concoctionsManager = get(),
            butterflyEffectManager = get(),
            aiDirector = get(),
            scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        )
    }
    
    // Stub managers for future features
    single { StubTradingPostManager() }
    single { StubGuildManager() }
    single { StubLeaderboardManager() }
    single { StubDynamicEventManager() }
    single { StubGossipManager() }
    single { StubDynamicDifficultyManager() }
    single { StubAudioManager() }
    single { StubLiveServiceManager() }
    single { StubPerformanceOptimizer() }
}

// Add to appModule list
val gameModules = listOf(
    // ...existing modules...
    integrationModule
)