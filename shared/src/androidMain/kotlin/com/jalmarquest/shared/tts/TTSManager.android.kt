package com.jalmarquest.shared.tts

/**
 * Android implementation of TTSManager.
 * 
 * Current Implementation: Stub (no-op).
 * Future implementation will use android.speech.tts.TextToSpeech.
 * 
 * Implementation requires:
 * - Context from Activity/Service
 * - Initialization listener for TTS engine ready state
 * - Language/locale selection
 * - Voice selection (male/female/variations)
 * 
 * For Milestone 2, TTS is optional and stubbed.
 * Full implementation planned for later milestones.
 */
actual class TTSManager {
    private var currentSpeed: Float = 1.0f
    
    actual fun speak(text: String) {
        // Stub: Would use android.speech.tts.TextToSpeech.speak()
        println("[TTS Android Stub] Would speak: $text")
    }
    
    actual fun stop() {
        // Stub: Would use TextToSpeech.stop()
        println("[TTS Android Stub] Stop requested")
    }
    
    actual fun setSpeed(speed: Float) {
        require(speed in 0.5f..2.0f) { "TTS speed must be 0.5-2.0" }
        currentSpeed = speed
        // Stub: Would use TextToSpeech.setSpeechRate(speed)
        println("[TTS Android Stub] Speed set to ${speed}x")
    }
    
    actual fun isSpeaking(): Boolean {
        // Stub: Would use TextToSpeech.isSpeaking()
        return false
    }
    
    actual fun shutdown() {
        // Stub: Would use TextToSpeech.shutdown()
        println("[TTS Android Stub] Shutdown called")
    }
}
