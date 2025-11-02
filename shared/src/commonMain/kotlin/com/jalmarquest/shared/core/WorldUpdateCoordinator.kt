package com.jalmarquest.shared.core

import com.jalmarquest.shared.state.GameStateManager
import com.jalmarquest.shared.time.TimeManager
import com.jalmarquest.shared.weather.WeatherManager
import com.jalmarquest.shared.world.BiomeType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Central coordinator for all world systems that need regular updates.
 * Manages the game loop and delegates updates to various managers.
 */
class WorldUpdateCoordinator(
    private val gameStateManager: GameStateManager,
    private val timeManager: TimeManager,
    private val weatherManager: WeatherManager,
    private val autosaveManager: AutosaveManager,
    private val scope: CoroutineScope
) {
    private var updateJob: Job? = null
    private var _isRunning = false
    private var _ticksPerSecond = DEFAULT_TICKS_PER_SECOND
    
    private val _updateCount = MutableStateFlow(0L)
    val updateCount: StateFlow<Long> = _updateCount.asStateFlow()
    
    private val _actualTickRate = MutableStateFlow(0.0)
    val actualTickRate: StateFlow<Double> = _actualTickRate.asStateFlow()
    
    companion object {
        const val DEFAULT_TICKS_PER_SECOND = 20 // 20 TPS is common for games
        const val MIN_TICKS_PER_SECOND = 1
        const val MAX_TICKS_PER_SECOND = 120
        
        // Stamina regeneration (ensures player never stuck waiting)
        const val STAMINA_REGEN_PER_TICK = 0.5 // At 20 TPS = 10 stamina/second (full bar in 10 seconds)
    }
    
    /**
     * Whether the world update loop is currently running.
     */
    val isRunning: Boolean
        get() = _isRunning
    
    /**
     * Target ticks per second.
     */
    val ticksPerSecond: Int
        get() = _ticksPerSecond
    
    /**
     * Start the world update loop.
     */
    fun start() {
        if (_isRunning) return
        
        _isRunning = true
        autosaveManager.start()
        
        updateJob = scope.launch {
            val tickIntervalMs = 1000L / _ticksPerSecond
            var lastTickTime = System.currentTimeMillis()
            var tickCount = 0L
            var lastRateCalcTime = System.currentTimeMillis()
            
            while (isActive && _isRunning) {
                val frameStart = System.currentTimeMillis()
                
                // Perform update tick
                performUpdate()
                tickCount++
                _updateCount.value = tickCount
                
                // Calculate actual tick rate every second
                val now = System.currentTimeMillis()
                val timeSinceLastCalc = now - lastRateCalcTime
                if (timeSinceLastCalc >= 1000) {
                    val ticksSinceLastCalc = tickCount - (tickCount - (timeSinceLastCalc / tickIntervalMs))
                    _actualTickRate.value = ticksSinceLastCalc * 1000.0 / timeSinceLastCalc
                    lastRateCalcTime = now
                }
                
                // Sleep for remaining time to maintain tick rate
                val frameTime = System.currentTimeMillis() - frameStart
                val sleepTime = (tickIntervalMs - frameTime).coerceAtLeast(0)
                
                if (sleepTime > 0) {
                    delay(sleepTime)
                }
                
                lastTickTime = System.currentTimeMillis()
            }
        }
    }
    
    /**
     * Stop the world update loop.
     */
    fun stop() {
        _isRunning = false
        autosaveManager.stop()
        updateJob?.cancel()
        updateJob = null
    }
    
    /**
     * Set the target tick rate.
     * @param tps Ticks per second (must be between MIN and MAX)
     */
    fun setTickRate(tps: Int) {
        require(tps in MIN_TICKS_PER_SECOND..MAX_TICKS_PER_SECOND) {
            "Tick rate must be between $MIN_TICKS_PER_SECOND and $MAX_TICKS_PER_SECOND"
        }
        
        val wasRunning = _isRunning
        if (wasRunning) {
            stop()
        }
        
        _ticksPerSecond = tps
        
        if (wasRunning) {
            start()
        }
    }
    
    /**
     * Perform a single update tick.
     * This is where all time-based systems get updated.
     */
    private suspend fun performUpdate() {
        // Update time system
        val newTime = timeManager.tick()
        
        // Get current game state for weather updates
        val currentState = gameStateManager.gameState.value ?: return
        
        // Update weather based on time progression
        // Weather advances by 1 in-game minute per tick (at 20 TPS, 60 ticks = 1 minute)
        // TODO: Get biome from tile-based world system
        val newWeather = weatherManager.advanceTime(
            minutes = 1,
            season = newTime.season,
            biome = BiomeType.GRASSLAND // Default for now
        )
        
        // Update game state with new time, weather, and regenerate stamina
        gameStateManager.updateState { state ->
            // Regenerate stamina (capped at max)
            val currentStamina = state.player.stats.currentStamina
            val maxStamina = state.player.stats.maxStamina
            val newStamina = (currentStamina + STAMINA_REGEN_PER_TICK).toInt().coerceAtMost(maxStamina)
            
            state.copy(
                worldTime = newTime,
                weather = newWeather,
                player = state.player.copy(
                    playTimeSeconds = state.player.playTimeSeconds + 1,
                    stats = state.player.stats.copy(
                        currentStamina = newStamina
                    )
                )
            )
        }
        
        // Future: This is where other systems will be updated
        // - NPC schedules
        // - Resource respawn
        // - Random events
        // - Enemy spawns
        // etc.
    }
    
    /**
     * Pause all world updates (including time).
     */
    fun pauseWorld() {
        timeManager.pause()
    }
    
    /**
     * Resume all world updates.
     */
    fun resumeWorld() {
        timeManager.resume()
    }
    
    /**
     * Toggle world pause state.
     */
    fun toggleWorldPause() {
        timeManager.togglePause()
    }
    
    /**
     * Get diagnostic information about the update loop.
     */
    fun getDiagnostics(): UpdateDiagnostics {
        return UpdateDiagnostics(
            isRunning = _isRunning,
            targetTickRate = _ticksPerSecond,
            actualTickRate = _actualTickRate.value,
            totalUpdates = _updateCount.value,
            timePaused = timeManager.isPaused,
            timeSpeed = timeManager.timeSpeed,
            autosaveEnabled = autosaveManager.isEnabled,
            secondsSinceAutosave = autosaveManager.secondsSinceLastAutosave()
        )
    }
}

/**
 * Diagnostic information about the world update loop.
 */
data class UpdateDiagnostics(
    val isRunning: Boolean,
    val targetTickRate: Int,
    val actualTickRate: Double,
    val totalUpdates: Long,
    val timePaused: Boolean,
    val timeSpeed: Double,
    val autosaveEnabled: Boolean,
    val secondsSinceAutosave: Long
)
