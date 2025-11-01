package dev.xeri0n.jalmarquest.ui.viewmodel

import dev.xeri0n.jalmarquest.core.GameCommand
import dev.xeri0n.jalmarquest.core.WorldUpdateCoordinator
import dev.xeri0n.jalmarquest.model.GameState
import dev.xeri0n.jalmarquest.model.Location
import dev.xeri0n.jalmarquest.movement.Direction
import dev.xeri0n.jalmarquest.persistence.SaveSlotManager
import dev.xeri0n.jalmarquest.state.GameStateManager
import dev.xeri0n.jalmarquest.time.TimeEventManager
import dev.xeri0n.jalmarquest.ui.components.CommandResult
import dev.xeri0n.jalmarquest.world.LocationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * View model for the main game screen.
 * Bridges UI with game logic managers.
 */
class GameViewModel(
    private val gameStateManager: GameStateManager,
    private val worldUpdateCoordinator: WorldUpdateCoordinator,
    private val locationManager: LocationManager,
    private val timeEventManager: TimeEventManager,
    private val saveSlotManager: SaveSlotManager,
    private val scope: CoroutineScope
) {
    // Create command processor
    private val commandProcessor = CommandProcessor(
        worldUpdateCoordinator,
        gameStateManager,
        locationManager,
        saveSlotManager
    )
    
    // Game state flows
    val gameState: StateFlow<GameState?> = gameStateManager.gameState
    
    val currentLocation: StateFlow<Location?> = gameState
        .map { state ->
            state?.let { locationManager.getLocation(it.player.position.locationId) }
        }
        .stateIn(scope, SharingStarted.Eagerly, null)
    
    val timeDisplay: StateFlow<String> = gameState
        .map { state ->
            state?.currentTime?.let { time ->
                "${time.format()} - ${time.season.name}"
            } ?: "Unknown Time"
        }
        .stateIn(scope, SharingStarted.Eagerly, "Unknown Time")
    
    val activeEvents: StateFlow<List<String>> = timeEventManager.activeEvents
        .map { events -> events.map { it.name } }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())
    
    // Command history for text-based interaction
    private val _commandHistory = MutableStateFlow<List<String>>(emptyList())
    val commandHistory: StateFlow<List<String>> = _commandHistory.asStateFlow()
    
    init {
        // Start the game loop when view model is created
        scope.launch {
            worldUpdateCoordinator.startGameLoop()
        }
    }
    
    /**
     * Move the player in a direction.
     */
    suspend fun move(direction: Direction) {
        worldUpdateCoordinator.handleCommand(GameCommand.Move(direction))
    }
    
    /**
     * Wait for a specified number of minutes.
     */
    suspend fun wait(minutes: Int) {
        worldUpdateCoordinator.handleCommand(GameCommand.Wait(minutes))
    }
    
    /**
     * Rest until stamina is full.
     */
    suspend fun rest() {
        worldUpdateCoordinator.handleCommand(GameCommand.Rest)
    }
    
    /**
     * Quick save the game.
     */
    suspend fun quickSave() {
        val state = gameState.value ?: return
        saveSlotManager.quickSave(state)
    }
    
    /**
     * Quick load the game.
     */
    suspend fun quickLoad() {
        val result = saveSlotManager.quickLoad()
        if (result is dev.xeri0n.jalmarquest.persistence.LoadResult.Success) {
            // Game state will be updated through GameStateManager
        }
    }
    
    /**
     * Create a new game.
     */
    suspend fun newGame(playerName: String) {
        gameStateManager.createNewGame(playerName, System.currentTimeMillis())
    }
    
    /**
     * Process a text command from the player.
     */
    suspend fun processTextCommand(command: String): CommandResult {
        return commandProcessor.processCommand(command)
    }
    
    /**
     * Add a response to the command history.
     */
    private fun addCommandResponse(response: String) {
        _commandHistory.value = (_commandHistory.value + response).takeLast(20)
    }
    
    /**
     * Show help information.
     */
    private fun showHelp() {
        val helpText = listOf(
            "=== Available Commands ===",
            "Movement: north/n, south/s, east/e, west/w",
            "Actions: look/l, rest/r, wait [minutes]",
            "Save/Load: save, load, quicksave/qs",
            "Info: stats, time, help",
            "Quail: chirp, preen, dust bath",
            "Tip: You can use natural phrases like 'go north' or 'look around'"
        )
        helpText.forEach { addCommandResponse(it) }
    }
    
    /**
     * Show player stats.
     */
    private fun showStats() {
        val state = gameState.value ?: return
        val stats = listOf(
            "=== ${state.player.name} ===",
            "Level: ${state.player.level}",
            "Stamina: ${state.player.stats.currentStamina}/${state.player.stats.maxStamina}",
            "Health: ${state.player.stats.currentHealth}/${state.player.stats.maxHealth}",
            "Location: ${currentLocation.value?.name ?: "Unknown"}"
        )
        stats.forEach { addCommandResponse(it) }
    }
    
    /**
     * Show current time.
     */
    private fun showTime() {
        val state = gameState.value ?: return
        state.currentTime?.let { time ->
            addCommandResponse("=== Current Time ===")
            addCommandResponse("Time: ${time.format()}")
            addCommandResponse("Day: ${time.day}")
            addCommandResponse("Season: ${time.season.name}")
        }
    }
    
    /**
     * Extract a number from a command string.
     */
    private fun extractNumber(command: String): Int? {
        return command.split(" ")
            .mapNotNull { it.toIntOrNull() }
            .firstOrNull()
    }
    
    /**
     * Get a random quail sound for fun.
     */
    private fun getQuailSound(): String {
        val sounds = listOf(
            "Peep peep!",
            "Chirrup!",
            "Pip pip!",
            "Twee twee!",
            "*rustles feathers*"
        )
        return sounds.random()
    }
}