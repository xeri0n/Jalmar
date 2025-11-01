package com.jalmarquest.shared.core

import com.jalmarquest.shared.model.GameState
import com.jalmarquest.shared.persistence.SaveManager
import com.jalmarquest.shared.state.GameStateManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages automatic saving of game state at configurable intervals.
 */
class AutosaveManager(
    private val gameStateManager: GameStateManager,
    private val saveManager: SaveManager,
    private val scope: CoroutineScope
) {
    private var autosaveJob: Job? = null
    private var _isEnabled = false
    private var _intervalSeconds = DEFAULT_INTERVAL_SECONDS
    
    private val _lastAutosaveTime = MutableStateFlow(0L)
    val lastAutosaveTime: StateFlow<Long> = _lastAutosaveTime.asStateFlow()
    
    private val _autosaveCount = MutableStateFlow(0)
    val autosaveCount: StateFlow<Int> = _autosaveCount.asStateFlow()
    
    companion object {
        const val DEFAULT_INTERVAL_SECONDS = 300 // 5 minutes
        const val MIN_INTERVAL_SECONDS = 60 // 1 minute
        const val MAX_INTERVAL_SECONDS = 3600 // 1 hour
    }
    
    /**
     * Whether autosave is currently enabled.
     */
    val isEnabled: Boolean
        get() = _isEnabled
    
    /**
     * Current autosave interval in seconds.
     */
    val intervalSeconds: Int
        get() = _intervalSeconds
    
    /**
     * Start autosaving at the configured interval.
     */
    fun start() {
        if (_isEnabled) return
        
        _isEnabled = true
        autosaveJob = scope.launch {
            while (isActive && _isEnabled) {
                delay(_intervalSeconds * 1000L)
                performAutosave()
            }
        }
    }
    
    /**
     * Stop autosaving.
     */
    fun stop() {
        _isEnabled = false
        autosaveJob?.cancel()
        autosaveJob = null
    }
    
    /**
     * Set the autosave interval.
     * @param seconds Interval in seconds (must be between MIN and MAX)
     */
    fun setInterval(seconds: Int) {
        require(seconds in MIN_INTERVAL_SECONDS..MAX_INTERVAL_SECONDS) {
            "Interval must be between $MIN_INTERVAL_SECONDS and $MAX_INTERVAL_SECONDS seconds"
        }
        
        val wasEnabled = _isEnabled
        if (wasEnabled) {
            stop()
        }
        
        _intervalSeconds = seconds
        
        if (wasEnabled) {
            start()
        }
    }
    
    /**
     * Manually trigger an autosave.
     */
    suspend fun performAutosave() {
        val currentState = gameStateManager.gameState.value ?: return
        
        saveManager.autoSave(currentState).fold(
            onSuccess = {
                _lastAutosaveTime.value = System.currentTimeMillis()
                _autosaveCount.value += 1
            },
            onFailure = { error ->
                // Log error but don't crash
                println("Autosave failed: ${error.message}")
            }
        )
    }
    
    /**
     * Get time since last autosave in seconds.
     */
    fun secondsSinceLastAutosave(): Long {
        val lastSave = _lastAutosaveTime.value
        if (lastSave == 0L) return Long.MAX_VALUE
        return (System.currentTimeMillis() - lastSave) / 1000
    }
}
