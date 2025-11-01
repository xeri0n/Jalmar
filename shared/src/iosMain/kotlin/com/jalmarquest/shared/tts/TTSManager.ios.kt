package com.jalmarquest.shared.tts

/**
 * iOS implementation of TTSManager.
 * 
 * Current Implementation: Stub (no-op).
 * Future implementation will use AVSpeechSynthesizer.
 * 
 * Implementation requires:
 * - AVFoundation framework
 * - AVSpeechSynthesizer instance
 * - AVSpeechUtterance for text configuration
 * - Voice selection (language/accent)
 * 
 * For Milestone 2, TTS is optional and stubbed.
 * Full implementation planned for later milestones.
 */
actual class TTSManager {
    private var currentSpeed: Float = 1.0f
    
    actual fun speak(text: String) {
        // Stub: Would use AVSpeechSynthesizer
        println("[TTS iOS Stub] Would speak: $text")
    }
    
    actual fun stop() {
        // Stub: Would use AVSpeechSynthesizer.stopSpeaking()
        println("[TTS iOS Stub] Stop requested")
    }
    
    actual fun setSpeed(speed: Float) {
        require(speed in 0.5f..2.0f) { "TTS speed must be 0.5-2.0" }
        currentSpeed = speed
        // Stub: Would set AVSpeechUtterance.rate
        println("[TTS iOS Stub] Speed set to ${speed}x")
    }
    
    actual fun isSpeaking(): Boolean {
        // Stub: Would use AVSpeechSynthesizer.isSpeaking
        return false
    }
    
    actual fun shutdown() {
        // Stub: Would stop synthesizer and release resources
        println("[TTS iOS Stub] Shutdown called")
    }
}
