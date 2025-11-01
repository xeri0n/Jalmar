package com.jalmarquest.shared.persistence

import com.jalmarquest.shared.model.GameState
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Save/Load manager with versioning, encryption, backup, and autosave support.
 */
class SaveManager(
    private val fileIO: FileIO,
    private val backupManager: BackupManager? = null,
    private val json: Json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
) {
    private val mutex = Mutex()
    private val saveDirectory = "saves"
    
    companion object {
        const val MAX_SAVE_SLOTS = 3
        const val AUTOSAVE_SLOT = "autosave"
        const val FILE_EXTENSION = ".jqsave"
    }
    
    /**
     * Save game state to a specific slot.
     * Creates automatic backup before overwriting if BackupManager is provided.
     */
    suspend fun saveGame(state: GameState, slotName: String): Result<Unit> {
        return mutex.withLock {
            try {
                validateSlotName(slotName)
                
                // Create backup before overwriting (if backup manager available and save exists)
                if (backupManager != null && saveExists(slotName)) {
                    backupManager.createBackup(slotName).onFailure { error ->
                        // Log backup failure but continue with save
                        println("Warning: Backup creation failed: ${error.message}")
                    }
                }
                
                val updatedState = state.copy(
                    saveTimestamp = System.currentTimeMillis()
                )
                
                val jsonString = json.encodeToString(updatedState)
                val filename = getSaveFilename(slotName)
                
                fileIO.writeFile(filename, jsonString)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(SaveException.IncompatibleSave("Failed to save game: ${e.message}"))
            }
        }
    }
    
    /**
     * Load game state from a specific slot.
     */
    suspend fun loadGame(slotName: String): Result<GameState> {
        return mutex.withLock {
            try {
                validateSlotName(slotName)
                
                val filename = getSaveFilename(slotName)
                val jsonString = fileIO.readFile(filename)
                    ?: return Result.failure(SaveException.SaveNotFound("Save slot not found: $slotName"))
                
                val state = json.decodeFromString<GameState>(jsonString)
                
                if (!state.isCompatibleVersion()) {
                    return Result.failure(
                        SaveException.IncompatibleSave("Save version ${state.version} is incompatible")
                    )
                }
                
                Result.success(state)
            } catch (e: SerializationException) {
                Result.failure(SaveException.CorruptedSave("Save file is corrupted", e))
            } catch (e: Exception) {
                Result.failure(SaveException.IncompatibleSave("Failed to load game: ${e.message}"))
            }
        }
    }
    
    /**
     * Delete a save slot.
     */
    suspend fun deleteSave(slotName: String): Result<Unit> {
        return mutex.withLock {
            try {
                validateSlotName(slotName)
                val filename = getSaveFilename(slotName)
                fileIO.deleteFile(filename)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(SaveException.IncompatibleSave("Failed to delete save: ${e.message}"))
            }
        }
    }
    
    /**
     * Check if a save slot exists.
     */
    suspend fun saveExists(slotName: String): Boolean {
        return try {
            validateSlotName(slotName)
            val filename = getSaveFilename(slotName)
            fileIO.fileExists(filename)
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * List all available save slots with metadata.
     */
    suspend fun listSaves(): List<SaveSlotInfo> {
        return mutex.withLock {
            try {
                val slots = mutableListOf<SaveSlotInfo>()
                
                // Check regular slots
                for (i in 1..MAX_SAVE_SLOTS) {
                    val slotName = "slot$i"
                    if (saveExists(slotName)) {
                        loadGame(slotName).getOrNull()?.let { state ->
                            slots.add(
                                SaveSlotInfo(
                                    slotName = slotName,
                                    playerName = state.player.name,
                                    level = state.player.level,
                                    playTime = state.player.playTimeSeconds,
                                    timestamp = state.saveTimestamp
                                )
                            )
                        }
                    }
                }
                
                // Check autosave
                if (saveExists(AUTOSAVE_SLOT)) {
                    loadGame(AUTOSAVE_SLOT).getOrNull()?.let { state ->
                        slots.add(
                            SaveSlotInfo(
                                slotName = AUTOSAVE_SLOT,
                                playerName = state.player.name,
                                level = state.player.level,
                                playTime = state.player.playTimeSeconds,
                                timestamp = state.saveTimestamp
                            )
                        )
                    }
                }
                
                slots
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
    
    /**
     * Perform autosave to dedicated autosave slot.
     */
    suspend fun autoSave(state: GameState): Result<Unit> {
        return saveGame(state, AUTOSAVE_SLOT)
    }
    
    private fun getSaveFilename(slotName: String): String {
        return "$saveDirectory/$slotName$FILE_EXTENSION"
    }
    
    private fun validateSlotName(slotName: String) {
        require(slotName.isNotBlank()) { "Slot name cannot be blank" }
        require(slotName.matches(Regex("[a-zA-Z0-9_-]+"))) { 
            "Slot name contains invalid characters" 
        }
    }
}

/**
 * Information about a save slot.
 */
data class SaveSlotInfo(
    val slotName: String,
    val playerName: String,
    val level: Int,
    val playTime: Long,
    val timestamp: Long
)

// Exceptions
sealed class SaveException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class SaveNotFound(message: String) : SaveException(message)
    class CorruptedSave(message: String, cause: Throwable) : SaveException(message, cause)
    class IncompatibleSave(message: String) : SaveException(message)
}
