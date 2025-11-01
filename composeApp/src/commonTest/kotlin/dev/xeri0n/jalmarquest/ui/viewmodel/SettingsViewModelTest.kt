package dev.xeri0n.jalmarquest.ui.viewmodel

import com.jalmarquest.shared.model.UserPreferences
import com.jalmarquest.shared.model.TextSize
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.test.assertFalse

/**
 * Comprehensive tests for SettingsViewModel.
 * 
 * Tests cover:
 * - Volume controls (master, music, SFX)
 * - Text size selection
 * - TTS configuration (enable, speed)
 * - Accessibility options (high contrast, reduced motion)
 * - Autosave settings
 * - Validation and edge cases
 * - State persistence
 */
class SettingsViewModelTest {
    
    // ==================== Volume Tests ====================
    
    @Test
    fun `setMasterVolume should update master volume`() = runTest {
        val viewModel = SettingsViewModel()
        
        viewModel.setMasterVolume(0.5f)
        
        assertEquals(0.5f, viewModel.preferences.first().masterVolume)
    }
    
    @Test
    fun `setMasterVolume should reject values below 0`() {
        val viewModel = SettingsViewModel()
        
        assertFailsWith<IllegalArgumentException> {
            viewModel.setMasterVolume(-0.1f)
        }
    }
    
    @Test
    fun `setMasterVolume should reject values above 1`() {
        val viewModel = SettingsViewModel()
        
        assertFailsWith<IllegalArgumentException> {
            viewModel.setMasterVolume(1.1f)
        }
    }
    
    @Test
    fun `setMusicVolume should update music volume`() = runTest {
        val viewModel = SettingsViewModel()
        
        viewModel.setMusicVolume(0.6f)
        
        assertEquals(0.6f, viewModel.preferences.first().musicVolume)
    }
    
    @Test
    fun `setSfxVolume should update SFX volume`() = runTest {
        val viewModel = SettingsViewModel()
        
        viewModel.setSfxVolume(0.7f)
        
        assertEquals(0.7f, viewModel.preferences.first().sfxVolume)
    }
    
    @Test
    fun `volume settings should accept boundary values`() = runTest {
        val viewModel = SettingsViewModel()
        
        viewModel.setMasterVolume(0.0f)
        viewModel.setMusicVolume(1.0f)
        viewModel.setSfxVolume(0.5f)
        
        val prefs = viewModel.preferences.first()
        assertEquals(0.0f, prefs.masterVolume)
        assertEquals(1.0f, prefs.musicVolume)
        assertEquals(0.5f, prefs.sfxVolume)
    }
    
    // ==================== Text Size Tests ====================
    
    @Test
    fun `setTextSize should update text size`() = runTest {
        val viewModel = SettingsViewModel()
        
        viewModel.setTextSize(TextSize.LARGE)
        
        assertEquals(TextSize.LARGE, viewModel.preferences.first().textSize)
    }
    
    @Test
    fun `setTextSize should handle all text sizes`() = runTest {
        val viewModel = SettingsViewModel()
        
        for (size in TextSize.entries) {
            viewModel.setTextSize(size)
            assertEquals(size, viewModel.preferences.first().textSize)
        }
    }
    
    // ==================== TTS Tests ====================
    
    @Test
    fun `setTtsEnabled should toggle TTS`() = runTest {
        val viewModel = SettingsViewModel()
        
        viewModel.setTtsEnabled(true)
        assertTrue(viewModel.preferences.first().ttsEnabled)
        
        viewModel.setTtsEnabled(false)
        assertFalse(viewModel.preferences.first().ttsEnabled)
    }
    
    @Test
    fun `setTtsSpeed should update TTS speed`() = runTest {
        val viewModel = SettingsViewModel()
        
        viewModel.setTtsSpeed(1.5f)
        
        assertEquals(1.5f, viewModel.preferences.first().ttsSpeed)
    }
    
    @Test
    fun `setTtsSpeed should reject values below 0_5`() {
        val viewModel = SettingsViewModel()
        
        assertFailsWith<IllegalArgumentException> {
            viewModel.setTtsSpeed(0.4f)
        }
    }
    
    @Test
    fun `setTtsSpeed should reject values above 2_0`() {
        val viewModel = SettingsViewModel()
        
        assertFailsWith<IllegalArgumentException> {
            viewModel.setTtsSpeed(2.1f)
        }
    }
    
    @Test
    fun `setTtsSpeed should accept boundary values`() = runTest {
        val viewModel = SettingsViewModel()
        
        viewModel.setTtsSpeed(0.5f)
        assertEquals(0.5f, viewModel.preferences.first().ttsSpeed)
        
        viewModel.setTtsSpeed(2.0f)
        assertEquals(2.0f, viewModel.preferences.first().ttsSpeed)
    }
    
    // ==================== Accessibility Tests ====================
    
    @Test
    fun `setHighContrastMode should toggle high contrast`() = runTest {
        val viewModel = SettingsViewModel()
        
        viewModel.setHighContrastMode(true)
        assertTrue(viewModel.preferences.first().highContrastMode)
        
        viewModel.setHighContrastMode(false)
        assertFalse(viewModel.preferences.first().highContrastMode)
    }
    
    @Test
    fun `setReducedMotion should toggle reduced motion`() = runTest {
        val viewModel = SettingsViewModel()
        
        viewModel.setReducedMotion(true)
        assertTrue(viewModel.preferences.first().reducedMotion)
        
        viewModel.setReducedMotion(false)
        assertFalse(viewModel.preferences.first().reducedMotion)
    }
    
    // ==================== Autosave Tests ====================
    
    @Test
    fun `setAutoSaveEnabled should toggle autosave`() = runTest {
        val viewModel = SettingsViewModel()
        
        viewModel.setAutoSaveEnabled(false)
        assertFalse(viewModel.preferences.first().autoSaveEnabled)
        
        viewModel.setAutoSaveEnabled(true)
        assertTrue(viewModel.preferences.first().autoSaveEnabled)
    }
    
    @Test
    fun `setAutoSaveInterval should update interval`() = runTest {
        val viewModel = SettingsViewModel()
        
        viewModel.setAutoSaveInterval(10)
        
        assertEquals(10, viewModel.preferences.first().autoSaveIntervalMinutes)
    }
    
    @Test
    fun `setAutoSaveInterval should reject values below 1`() {
        val viewModel = SettingsViewModel()
        
        assertFailsWith<IllegalArgumentException> {
            viewModel.setAutoSaveInterval(0)
        }
    }
    
    @Test
    fun `setAutoSaveInterval should reject values above 60`() {
        val viewModel = SettingsViewModel()
        
        assertFailsWith<IllegalArgumentException> {
            viewModel.setAutoSaveInterval(61)
        }
    }
    
    @Test
    fun `setAutoSaveInterval should accept boundary values`() = runTest {
        val viewModel = SettingsViewModel()
        
        viewModel.setAutoSaveInterval(1)
        assertEquals(1, viewModel.preferences.first().autoSaveIntervalMinutes)
        
        viewModel.setAutoSaveInterval(60)
        assertEquals(60, viewModel.preferences.first().autoSaveIntervalMinutes)
    }
    
    // ==================== State Management Tests ====================
    
    @Test
    fun `loadPreferences should update all settings`() = runTest {
        val viewModel = SettingsViewModel()
        
        val customPrefs = UserPreferences(
            masterVolume = 0.8f,
            musicVolume = 0.6f,
            sfxVolume = 0.7f,
            textSize = TextSize.LARGE,
            ttsEnabled = true,
            ttsSpeed = 1.5f,
            highContrastMode = true,
            reducedMotion = true,
            autoSaveEnabled = false,
            autoSaveIntervalMinutes = 15
        )
        
        viewModel.loadPreferences(customPrefs)
        
        val loaded = viewModel.preferences.first()
        assertEquals(0.8f, loaded.masterVolume)
        assertEquals(0.6f, loaded.musicVolume)
        assertEquals(0.7f, loaded.sfxVolume)
        assertEquals(TextSize.LARGE, loaded.textSize)
        assertTrue(loaded.ttsEnabled)
        assertEquals(1.5f, loaded.ttsSpeed)
        assertTrue(loaded.highContrastMode)
        assertTrue(loaded.reducedMotion)
        assertFalse(loaded.autoSaveEnabled)
        assertEquals(15, loaded.autoSaveIntervalMinutes)
    }
    
    @Test
    fun `resetToDefaults should restore default preferences`() = runTest {
        val viewModel = SettingsViewModel()
        
        // Change all settings
        viewModel.setMasterVolume(0.5f)
        viewModel.setTextSize(TextSize.LARGE)
        viewModel.setTtsEnabled(true)
        viewModel.setAutoSaveEnabled(false)
        
        // Reset
        viewModel.resetToDefaults()
        
        val prefs = viewModel.preferences.first()
        assertEquals(1.0f, prefs.masterVolume)
        assertEquals(0.8f, prefs.musicVolume)
        assertEquals(0.9f, prefs.sfxVolume)
        assertEquals(TextSize.MEDIUM, prefs.textSize)
        assertFalse(prefs.ttsEnabled)
        assertEquals(1.0f, prefs.ttsSpeed)
        assertFalse(prefs.highContrastMode)
        assertFalse(prefs.reducedMotion)
        assertTrue(prefs.autoSaveEnabled)
        assertEquals(5, prefs.autoSaveIntervalMinutes)
    }
    
    @Test
    fun `multiple setting changes should maintain state correctly`() = runTest {
        val viewModel = SettingsViewModel()
        
        viewModel.setMasterVolume(0.7f)
        viewModel.setTextSize(TextSize.EXTRA_LARGE)
        viewModel.setTtsEnabled(true)
        viewModel.setTtsSpeed(1.8f)
        viewModel.setHighContrastMode(true)
        viewModel.setAutoSaveInterval(20)
        
        val prefs = viewModel.preferences.first()
        assertEquals(0.7f, prefs.masterVolume)
        assertEquals(TextSize.EXTRA_LARGE, prefs.textSize)
        assertTrue(prefs.ttsEnabled)
        assertEquals(1.8f, prefs.ttsSpeed)
        assertTrue(prefs.highContrastMode)
        assertEquals(20, prefs.autoSaveIntervalMinutes)
    }
    
    // ==================== UserPreferences Validation Tests ====================
    
    @Test
    fun `UserPreferences should reject invalid master volume in init`() {
        assertFailsWith<IllegalArgumentException> {
            UserPreferences(masterVolume = 1.5f)
        }
    }
    
    @Test
    fun `UserPreferences should reject invalid music volume in init`() {
        assertFailsWith<IllegalArgumentException> {
            UserPreferences(musicVolume = -0.1f)
        }
    }
    
    @Test
    fun `UserPreferences should reject invalid TTS speed in init`() {
        assertFailsWith<IllegalArgumentException> {
            UserPreferences(ttsSpeed = 3.0f)
        }
    }
    
    @Test
    fun `UserPreferences should reject invalid autosave interval in init`() {
        assertFailsWith<IllegalArgumentException> {
            UserPreferences(autoSaveIntervalMinutes = 0)
        }
    }
    
    @Test
    fun `TextSize enum should have correct scale factors`() {
        assertEquals(0.85f, TextSize.SMALL.scaleFactor)
        assertEquals(1.0f, TextSize.MEDIUM.scaleFactor)
        assertEquals(1.15f, TextSize.LARGE.scaleFactor)
        assertEquals(1.3f, TextSize.EXTRA_LARGE.scaleFactor)
    }
}
