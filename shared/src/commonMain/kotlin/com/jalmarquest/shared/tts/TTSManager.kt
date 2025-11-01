package com.jalmarquest.shared.tts

/**
 * Text-to-Speech manager interface for JalmarQuest.
 * 
 * Provides platform-independent TTS narration for:
 * - Dialogue and NPC conversations
 * - Location descriptions
 * - Quest text
 * - Combat events
 * 
 * Platform-specific implementations handle:
 * - Desktop: javax.speech or external TTS library
 * - Android: android.speech.tts.TextToSpeech
 * - iOS: AVSpeechSynthesizer
 * 
 * Usage:
 * ```
 * val ttsManager = TTSManager()
 * ttsManager.speak("Welcome to Buttonburgh!")
 * ttsManager.setSpeed(1.5f) // 1.5x speed
 * ttsManager.stop() // Interrupt current speech
 * ```
 */
expect class TTSManager() {
    /**
     * Speak the given text aloud.
     * Queues speech if already speaking.
     * 
     * @param text The text to speak
     */
    fun speak(text: String)
    
    /**
     * Stop any current speech and clear the queue.
     */
    fun stop()
    
    /**
     * Set the playback speed.
     * 
     * @param speed Speed multiplier (0.5 = half speed, 2.0 = double speed)
     */
    fun setSpeed(speed: Float)
    
    /**
     * Check if TTS is currently speaking.
     * 
     * @return True if speaking, false otherwise
     */
    fun isSpeaking(): Boolean
    
    /**
     * Release TTS resources.
     * Call this when TTS is no longer needed (e.g., app shutdown).
     */
    fun shutdown()
}
