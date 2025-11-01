package com.jalmarquest.shared.persistence

import com.jalmarquest.shared.model.UserPreferences
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Preferences manager for persisting user settings across app restarts.
 * 
 * Manages:
 * - Volume settings (master, music, SFX)
 * - Text size and accessibility options
 * - TTS configuration
 * - Autosave settings
 * 
 * Preferences are stored in a separate file from game saves to enable
 * settings persistence even when no game is loaded.
 * 
 * Thread-safe via Mutex, compatible with GameStateManager's concurrency model.
 */
class PreferencesManager(
    private val fileIO: FileIO,
    private val json: Json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
) {
    private val mutex = Mutex()
    private val preferencesFilename = "preferences.json"
    
    /**
     * Save user preferences to disk.
     * 
     * @param preferences The preferences to save
     * @return Result.success on successful save, Result.failure on error
     */
    suspend fun savePreferences(preferences: UserPreferences): Result<Unit> {
        return mutex.withLock {
            try {
                val jsonString = json.encodeToString(preferences)
                fileIO.writeFile(preferencesFilename, jsonString)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(Exception("Failed to save preferences: ${e.message}", e))
            }
        }
    }
    
    /**
     * Load user preferences from disk.
     * 
     * @return Result.success with loaded preferences, or Result.failure if file doesn't exist or is corrupted.
     *         On failure, caller should use default UserPreferences().
     */
    suspend fun loadPreferences(): Result<UserPreferences> {
        return mutex.withLock {
            try {
                val jsonString = fileIO.readFile(preferencesFilename)
                    ?: return Result.failure(Exception("Preferences file not found"))
                
                val preferences = json.decodeFromString<UserPreferences>(jsonString)
                Result.success(preferences)
            } catch (e: SerializationException) {
                Result.failure(Exception("Preferences file is corrupted: ${e.message}", e))
            } catch (e: Exception) {
                Result.failure(Exception("Failed to load preferences: ${e.message}", e))
            }
        }
    }
    
    /**
     * Delete preferences file (reset to defaults).
     * 
     * @return Result.success on successful deletion, Result.failure on error
     */
    suspend fun deletePreferences(): Result<Unit> {
        return mutex.withLock {
            try {
                fileIO.deleteFile(preferencesFilename)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(Exception("Failed to delete preferences: ${e.message}", e))
            }
        }
    }
    
    /**
     * Check if preferences file exists.
     * 
     * @return True if preferences file exists, false otherwise
     */
    suspend fun preferencesExist(): Boolean {
        return mutex.withLock {
            fileIO.readFile(preferencesFilename) != null
        }
    }
}
