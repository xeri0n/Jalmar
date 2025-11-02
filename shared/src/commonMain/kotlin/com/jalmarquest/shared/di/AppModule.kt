package com.jalmarquest.shared.di

import com.jalmar.quest.movement.TileMovementManager
import com.jalmar.quest.tilemap.MapTriggerManager
import com.jalmar.quest.tilemap.TileMapManager
import com.jalmarquest.shared.core.AutosaveManager
import com.jalmarquest.shared.core.WorldUpdateCoordinator
import com.jalmarquest.shared.persistence.FileIO
import com.jalmarquest.shared.persistence.FileIOAdapter
import com.jalmarquest.shared.persistence.BackupManager
import com.jalmarquest.shared.persistence.SaveManager
import com.jalmarquest.shared.state.GameStateManager
import com.jalmarquest.shared.time.TimeManager
import com.jalmarquest.shared.weather.WeatherManager
import com.jalmarquest.shared.progression.StatsAchievementsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin dependency injection module for shared game logic.
 */
val sharedModule = module {
    // Coroutine Scope
    single<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
    
    // State Management
    single { GameStateManager() }
    
    // Tile System
    single { TileMapManager() }
    single { MapTriggerManager(get()) } // Depends on TileMapManager
    single { TileMovementManager(get()) } // Depends on TileMapManager
    
    // Time System
    single { TimeManager() }
    
    // Weather System
    single { WeatherManager() }
    
    // Persistence - FileIO is platform-specific and injected separately
    single { BackupManager(FileIOAdapter(get())) }
    single { SaveManager(get(), get()) }
    
    // Autosave
    single { AutosaveManager(get(), get(), get()) }
    
    // World Coordinator
    single { WorldUpdateCoordinator(get(), get(), get(), get(), get()) }
    
    // Progression (Stats + Achievements)
    single { StatsAchievementsManager(get()) }
}

/**
 * Platform-specific modules must provide FileIO implementation.
 */
expect val platformModule: Module
