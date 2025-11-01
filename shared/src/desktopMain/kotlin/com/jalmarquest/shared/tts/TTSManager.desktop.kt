package com.jalmarquest.shared.tts

/**
 * Desktop (JVM) implementation of TTSManager.
 * 
 * Current Implementation: Stub (no-op).
 * Future implementations could use:
 * - FreeTTS (Java TTS library)
 * - Mary TTS (open-source TTS)
 * - External system TTS (Windows SAPI, macOS say command)
 * - Cloud TTS APIs (Google, AWS Polly)
 * 
 * For Milestone 2, TTS is optional and stubbed.
 * Full implementation planned for later milestones.
 */
actual class TTSManager {
    private var currentSpeed: Float = 1.0f
    
    actual fun speak(text: String) {
        // Stub: Log to console instead of speaking
        println("[TTS Desktop Stub] Would speak: $text")
    }
    
    actual fun stop() {
        println("[TTS Desktop Stub] Stop requested")
    }
    
    actual fun setSpeed(speed: Float) {
        require(speed in 0.5f..2.0f) { "TTS speed must be 0.5-2.0" }
        currentSpeed = speed
        println("[TTS Desktop Stub] Speed set to ${speed}x")
    }
    
    actual fun isSpeaking(): Boolean {
        return false // Stub always returns false
    }
    
    actual fun shutdown() {
        println("[TTS Desktop Stub] Shutdown called")
    }
}
