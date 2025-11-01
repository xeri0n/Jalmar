package dev.xeri0n.jalmarquest.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Settings ViewModel
 * 
 * Manages user preferences and settings state
 */
class SettingsViewModel {
    // ============= Audio Settings =============
    
    private val _musicVolume = MutableStateFlow(0.7f)
    val musicVolume: StateFlow<Float> = _musicVolume.asStateFlow()
    
    private val _sfxVolume = MutableStateFlow(0.8f)
    val sfxVolume: StateFlow<Float> = _sfxVolume.asStateFlow()
    
    private val _ttsVolume = MutableStateFlow(1.0f)
    val ttsVolume: StateFlow<Float> = _ttsVolume.asStateFlow()
    
    private val _ttsEnabled = MutableStateFlow(true)
    val ttsEnabled: StateFlow<Boolean> = _ttsEnabled.asStateFlow()
    
    // ============= Display Settings =============
    
    private val _darkMode = MutableStateFlow(true)
    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()
    
    private val _textSize = MutableStateFlow(TextSize.MEDIUM)
    val textSize: StateFlow<TextSize> = _textSize.asStateFlow()
    
    private val _highContrastMode = MutableStateFlow(false)
    val highContrastMode: StateFlow<Boolean> = _highContrastMode.asStateFlow()
    
    private val _animationsEnabled = MutableStateFlow(true)
    val animationsEnabled: StateFlow<Boolean> = _animationsEnabled.asStateFlow()
    
    private val _particleEffects = MutableStateFlow(true)
    val particleEffects: StateFlow<Boolean> = _particleEffects.asStateFlow()
    
    // ============= Gameplay Settings =============
    
    private val _autosaveInterval = MutableStateFlow(5)
    val autosaveInterval: StateFlow<Int> = _autosaveInterval.asStateFlow()
    
    private val _showTutorials = MutableStateFlow(true)
    val showTutorials: StateFlow<Boolean> = _showTutorials.asStateFlow()
    
    private val _confirmDestructiveActions = MutableStateFlow(true)
    val confirmDestructiveActions: StateFlow<Boolean> = _confirmDestructiveActions.asStateFlow()
    
    private val _showDamageNumbers = MutableStateFlow(true)
    val showDamageNumbers: StateFlow<Boolean> = _showDamageNumbers.asStateFlow()
    
    private val _autoLootItems = MutableStateFlow(false)
    val autoLootItems: StateFlow<Boolean> = _autoLootItems.asStateFlow()
    
    // ============= Controls Settings =============
    
    private val _mouseSensitivity = MutableStateFlow(0.5f)
    val mouseSensitivity: StateFlow<Float> = _mouseSensitivity.asStateFlow()
    
    private val _hapticFeedback = MutableStateFlow(true)
    val hapticFeedback: StateFlow<Boolean> = _hapticFeedback.asStateFlow()
    
    // ============= Setter Methods =============
    
    fun setMusicVolume(volume: Float) {
        _musicVolume.value = volume.coerceIn(0f, 1f)
    }
    
    fun setSfxVolume(volume: Float) {
        _sfxVolume.value = volume.coerceIn(0f, 1f)
    }
    
    fun setTtsVolume(volume: Float) {
        _ttsVolume.value = volume.coerceIn(0f, 1f)
    }
    
    fun setTtsEnabled(enabled: Boolean) {
        _ttsEnabled.value = enabled
    }
    
    fun setDarkMode(enabled: Boolean) {
        _darkMode.value = enabled
    }
    
    fun setTextSize(size: TextSize) {
        _textSize.value = size
    }
    
    fun setHighContrastMode(enabled: Boolean) {
        _highContrastMode.value = enabled
    }
    
    fun setAnimationsEnabled(enabled: Boolean) {
        _animationsEnabled.value = enabled
    }
    
    fun setParticleEffects(enabled: Boolean) {
        _particleEffects.value = enabled
    }
    
    fun setAutosaveInterval(minutes: Int) {
        _autosaveInterval.value = minutes.coerceIn(1, 30)
    }
    
    fun setShowTutorials(show: Boolean) {
        _showTutorials.value = show
    }
    
    fun setConfirmDestructiveActions(confirm: Boolean) {
        _confirmDestructiveActions.value = confirm
    }
    
    fun setShowDamageNumbers(show: Boolean) {
        _showDamageNumbers.value = show
    }
    
    fun setAutoLootItems(autoLoot: Boolean) {
        _autoLootItems.value = autoLoot
    }
    
    fun setMouseSensitivity(sensitivity: Float) {
        _mouseSensitivity.value = sensitivity.coerceIn(0f, 1f)
    }
    
    fun setHapticFeedback(enabled: Boolean) {
        _hapticFeedback.value = enabled
    }
    
    fun resetToDefaults() {
        _musicVolume.value = 0.7f
        _sfxVolume.value = 0.8f
        _ttsVolume.value = 1.0f
        _ttsEnabled.value = true
        _darkMode.value = true
        _textSize.value = TextSize.MEDIUM
        _highContrastMode.value = false
        _animationsEnabled.value = true
        _particleEffects.value = true
        _autosaveInterval.value = 5
        _showTutorials.value = true
        _confirmDestructiveActions.value = true
        _showDamageNumbers.value = true
        _autoLootItems.value = false
        _mouseSensitivity.value = 0.5f
        _hapticFeedback.value = true
    }
}

enum class TextSize {
    SMALL,
    MEDIUM,
    LARGE,
    EXTRA_LARGE
}
