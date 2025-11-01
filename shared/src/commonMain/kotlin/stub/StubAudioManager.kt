package stub

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable

/**
 * Stub implementation for Audio System (Phase 10.3).
 * Foundation for sound effects and music.
 */
class StubAudioManager {
    private val _settings = MutableStateFlow(AudioSettings())
    val settings: StateFlow<AudioSettings> = _settings.asStateFlow()
    
    suspend fun playSound(soundId: String, volume: Float = 1.0f) {
        // Stub: Will trigger platform-specific sound playback
    }
    
    suspend fun playMusic(trackId: String, loop: Boolean = true) {
        // Stub: Will handle background music
    }
    
    suspend fun stopMusic() {
        // Stub: Will stop current music
    }
    
    suspend fun fadeMusic(duration: Long) {
        // Stub: Will fade out music over time
    }
    
    suspend fun playAmbient(ambientId: String, locationId: String) {
        // Stub: Will play location-specific ambient sounds
    }
    
    suspend fun updateVolume(category: AudioCategory, volume: Float) {
        // Stub: Will update volume settings
    }
}

@Serializable
data class AudioSettings(
    val masterVolume: Float = 1.0f,
    val musicVolume: Float = 1.0f,
    val sfxVolume: Float = 1.0f,
    val ambientVolume: Float = 1.0f,
    val voiceVolume: Float = 1.0f,
    val muteAll: Boolean = false
)

@Serializable
enum class AudioCategory {
    MASTER, MUSIC, SFX, AMBIENT, VOICE
}

// Platform-specific implementation will be added
expect class PlatformAudioPlayer() {
    fun playSound(assetPath: String, volume: Float)
    fun playMusic(assetPath: String, loop: Boolean)
    fun stopAll()
}
