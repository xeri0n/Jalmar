package com.jalmarquest.shared.model

import kotlinx.serialization.Serializable

/**
 * User preferences for JalmarQuest.
 * 
 * All settings are persisted across app restarts via PreferencesManager.
 * This class is @Serializable to enable JSON persistence.
 */
@Serializable
data class UserPreferences(
    val masterVolume: Float = 1.0f,      // 0.0 to 1.0
    val musicVolume: Float = 0.8f,       // 0.0 to 1.0
    val sfxVolume: Float = 0.9f,         // 0.0 to 1.0
    val textSize: TextSize = TextSize.MEDIUM,
    val ttsEnabled: Boolean = false,     // Text-to-Speech narration
    val ttsSpeed: Float = 1.0f,          // 0.5 to 2.0
    val highContrastMode: Boolean = false,
    val reducedMotion: Boolean = false,
    val autoSaveEnabled: Boolean = true,
    val autoSaveIntervalMinutes: Int = 5
) {
    init {
        require(masterVolume in 0.0f..1.0f) { "Master volume must be 0.0-1.0" }
        require(musicVolume in 0.0f..1.0f) { "Music volume must be 0.0-1.0" }
        require(sfxVolume in 0.0f..1.0f) { "SFX volume must be 0.0-1.0" }
        require(ttsSpeed in 0.5f..2.0f) { "TTS speed must be 0.5-2.0" }
        require(autoSaveIntervalMinutes in 1..60) { "Autosave interval must be 1-60 minutes" }
    }
}

/**
 * Text size options for improved accessibility.
 */
@Serializable
enum class TextSize(val scaleFactor: Float) {
    SMALL(0.85f),
    MEDIUM(1.0f),
    LARGE(1.15f),
    EXTRA_LARGE(1.3f)
}
