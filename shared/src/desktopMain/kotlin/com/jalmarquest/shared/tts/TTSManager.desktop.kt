package com.jalmarquest.shared.tts

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Desktop (JVM) implementation of TTSManager.
 * 
 * Uses platform-native TTS:
 * - **Windows**: PowerShell SAPI via Add-Type System.Speech
 * - **macOS**: `say` command
 * - **Linux**: `espeak` or `spd-say` (Speech Dispatcher)
 * 
 * Implementation strategy:
 * - Detects OS at runtime
 * - Uses ProcessBuilder to invoke system TTS
 * - Queues speech requests (one at a time)
 * - Supports speed control via platform-specific flags
 * 
 * Limitations:
 * - Windows: Requires PowerShell 5.1+ (bundled with Windows 10+)
 * - macOS: `say` command available by default
 * - Linux: Requires `espeak` or `speech-dispatcher` installed
 */
actual class TTSManager {
    private val mutex = Mutex()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var currentSpeed: Float = 1.0f
    private var currentProcess: Process? = null
    private var isCurrentlySpeaking = false
    
    private val osName = System.getProperty("os.name").lowercase()
    private val isWindows = osName.contains("win")
    private val isMac = osName.contains("mac")
    private val isLinux = osName.contains("nux") || osName.contains("nix")
    
    actual fun speak(text: String) {
        if (text.isBlank()) return
        
        scope.launch {
            mutex.withLock {
                try {
                    isCurrentlySpeaking = true
                    
                    // Sanitize text for shell safety (escape quotes)
                    val sanitized = text.replace("\"", "\\\"").replace("'", "\\'")
                    
                    val process = when {
                        isWindows -> speakWindows(sanitized)
                        isMac -> speakMac(sanitized)
                        isLinux -> speakLinux(sanitized)
                        else -> {
                            println("[TTS Desktop] Unsupported OS: $osName")
                            null
                        }
                    }
                    
                    currentProcess = process
                    process?.waitFor() // Block until speech completes
                    
                } catch (e: Exception) {
                    println("[TTS Desktop] Error: ${e.message}")
                } finally {
                    isCurrentlySpeaking = false
                    currentProcess = null
                }
            }
        }
    }
    
    actual fun stop() {
        scope.launch {
            mutex.withLock {
                currentProcess?.destroy()
                currentProcess = null
                isCurrentlySpeaking = false
            }
        }
    }
    
    actual fun setSpeed(speed: Float) {
        require(speed in 0.5f..2.0f) { "TTS speed must be 0.5-2.0" }
        currentSpeed = speed
    }
    
    actual fun isSpeaking(): Boolean {
        return isCurrentlySpeaking
    }
    
    actual fun shutdown() {
        stop()
    }
    
    // Platform-specific TTS implementations
    
    private fun speakWindows(text: String): Process? {
        // Windows: Use PowerShell SAPI (System.Speech.Synthesis)
        // Rate: -10 (slow) to 10 (fast), 0 = normal
        // Map our 0.5-2.0 scale to -10 to 10
        val rate = ((currentSpeed - 1.0f) * 10).toInt().coerceIn(-10, 10)
        
        // Build PowerShell command (use ${'$'} to escape dollar signs in Kotlin strings)
        val command = """
            Add-Type -AssemblyName System.Speech;
            ${'$'}synth = New-Object System.Speech.Synthesis.SpeechSynthesizer;
            ${'$'}synth.Rate = $rate;
            ${'$'}synth.Speak('$text')
        """.trimIndent()
        
        return ProcessBuilder(
            "powershell.exe",
            "-NoProfile",
            "-NonInteractive",
            "-Command",
            command
        ).start()
    }
    
    private fun speakMac(text: String): Process? {
        // macOS: Use `say` command
        // Rate: words per minute (default ~200)
        // Map our 0.5-2.0 scale to 100-400 WPM
        val rate = (200 * currentSpeed).toInt()
        
        return ProcessBuilder(
            "say",
            "-r", rate.toString(),
            text
        ).start()
    }
    
    private fun speakLinux(text: String): Process? {
        // Linux: Try espeak first, fall back to spd-say
        // espeak speed: 80-450 WPM (default 175)
        val speed = (175 * currentSpeed).toInt()
        
        return try {
            // Try espeak (more control over speed)
            ProcessBuilder(
                "espeak",
                "-s", speed.toString(),
                text
            ).start()
        } catch (e: Exception) {
            try {
                // Fall back to spd-say (Speech Dispatcher)
                ProcessBuilder(
                    "spd-say",
                    "-r", ((currentSpeed - 1.0f) * 100).toInt().toString(), // -100 to 100
                    text
                ).start()
            } catch (e2: Exception) {
                println("[TTS Desktop Linux] Neither espeak nor spd-say found")
                null
            }
        }
    }
}
