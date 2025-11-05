package com.jalmarquest.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jalmarquest.shared.npc.NPC
import com.jalmarquest.shared.tts.TTSManager
import com.jalmarquest.shared.model.UserPreferences
import com.jalmarquest.ui.theme.JQAnimations
import com.jalmarquest.ui.accessibility.keyboardNavigable
import com.jalmarquest.ui.accessibility.AutoFocus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Dialogue window for NPC conversations.
 * Shows NPC portrait, name, dialogue text, and player choices.
 * Features smooth scale-in/scale-out animations.
 * 
 * **Accessibility:** Narrates NPC dialogue via TTS if enabled in UserPreferences.
 */
@Composable
fun DialogueWindow(
    npc: NPC,
    relationshipScore: Int,
    onChoice: (Int) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    preferences: UserPreferences? = null, // Injected from parent or Koin
    ttsManager: TTSManager = koinInject()
) {
    val relationshipLevel = getRelationshipLevel(relationshipScore)
    var visible by remember { mutableStateOf(false) }
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    
    // Keyboard navigation: focus requesters for dialogue choices
    val focusRequesters = remember { List(3) { FocusRequester() } }
    
    // Sync TTS speed with user preferences
    LaunchedEffect(preferences?.ttsSpeed) {
        preferences?.ttsSpeed?.let { speed ->
            ttsManager.setSpeed(speed)
        }
    }
    
    // Trigger animation on first composition
    LaunchedEffect(Unit) {
        visible = true
    }
    
    // Auto-focus first dialogue choice for keyboard navigation
    AutoFocus(focusRequesters[0])
    
    // TTS: Narrate NPC dialogue on appearance
    LaunchedEffect(npc.id) {
        if (preferences?.ttsEnabled == true) {
            // Format narration: "Grumble says: [dialogue text]"
            // TODO: Get actual dialogue from DialogueManager once integrated
            val greeting = when (relationshipLevel) {
                "Stranger" -> "Hello there, traveler."
                "Acquaintance" -> "Good to see you again!"
                "Friend" -> "Ah, my friend! How goes it?"
                "Trusted Ally" -> "Welcome back, dear friend! I always have time for you."
                else -> "Hello."
            }
            val narration = "${npc.name} says: $greeting"
            ttsManager.speak(narration)
        }
    }
    
    // TTS: Stop narration when dialogue closes
    DisposableEffect(Unit) {
        onDispose {
            if (preferences?.ttsEnabled == true) {
                ttsManager.stop()
            }
        }
    }
    
    Dialog(onDismissRequest = {
        visible = false
        scope.launch {
            delay(200)
            onClose()
        }
    }) {
        AnimatedVisibility(
            visible = visible,
            enter = JQAnimations.dialogueEnter(),
            exit = JQAnimations.dialogueExit()
        ) {
            Box(
                modifier = modifier
                    .width(600.dp)
                    .background(Color(0xEE1a1a1a), RoundedCornerShape(12.dp))
                    .border(3.dp, Color(0xFFaa8844), RoundedCornerShape(12.dp))
                    .padding(24.dp)
            ) {
                Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // NPC Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = npc.name,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${npc.occupation.name.lowercase().replace('_', ' ')} • ${npc.species.name.lowercase().replace('_', ' ')}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF888888)
                        )
                    }
                    
                    // Relationship indicator
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = relationshipLevel,
                            style = MaterialTheme.typography.bodyMedium,
                            color = getRelationshipColor(relationshipScore),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "($relationshipScore)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF666666)
                        )
                    }
                }
                
                HorizontalDivider(color = Color(0xFF444444))
                
                // Dialogue text (placeholder for now)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color(0xFF0a0a0a), RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = getGreeting(npc, relationshipScore),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFFCCCCCC),
                        lineHeight = 24.sp
                    )
                }
                
                // Player choices (placeholder) with keyboard navigation
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    DialogueChoiceButton(
                        text = "Tell me about this place.",
                        onClick = { onChoice(0) },
                        focusRequester = focusRequesters[0],
                        contentDescription = "Dialogue choice: Tell me about this place"
                    )
                    DialogueChoiceButton(
                        text = "What do you know about the area?",
                        onClick = { onChoice(1) },
                        focusRequester = focusRequesters[1],
                        contentDescription = "Dialogue choice: What do you know about the area"
                    )
                    DialogueChoiceButton(
                        text = "I should get going. Farewell!",
                        onClick = {
                            visible = false
                            scope.launch {
                                delay(200)
                                onClose()
                            }
                        },
                        isExit = true,
                        focusRequester = focusRequesters[2],
                        contentDescription = "Exit dialogue"
                    )
                }
            } // End Column
        } // End Box
    } // End AnimatedVisibility
} // End Dialog
} // End DialogueWindow function

@Composable
private fun DialogueChoiceButton(
    text: String,
    onClick: () -> Unit,
    isExit: Boolean = false,
    focusRequester: FocusRequester? = null,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isExit) Color(0xFF663333) else Color(0xFF333366),
            contentColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .keyboardNavigable(
                enabled = true,
                contentDescription = contentDescription ?: text,
                onActivate = onClick,
                focusRequester = focusRequester
            ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isExit) "→" else "•",
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = text, fontSize = 15.sp)
        }
    }
}

private fun getGreeting(npc: NPC, relationshipScore: Int): String {
    val greeting = when {
        relationshipScore >= 50 -> when (npc.personality.friendliness) {
            in 8..10 -> "Ah, my dear friend! It's wonderful to see you again! How can I help you today?"
            in 5..7 -> "Good to see you, friend. What brings you by?"
            else -> "Oh, it's you. What do you need?"
        }
        relationshipScore >= 10 -> when (npc.personality.friendliness) {
            in 8..10 -> "Hello there! Always happy to see a friendly face. What can I do for you?"
            in 5..7 -> "Greetings. How may I assist you?"
            else -> "Yes? What is it?"
        }
        relationshipScore >= -10 -> when (npc.personality.friendliness) {
            in 8..10 -> "Hello! Welcome! How can I help you today?"
            in 5..7 -> "Greetings. What do you need?"
            else -> "What do you want?"
        }
        else -> "I don't have time for you. Move along."
    }
    
    return when (npc.id) {
        "innkeeper_bertha" -> "\"$greeting I always have warm food and a cozy nest for weary travelers!\""
        "grumble_forgepaw" -> "\"$greeting\" *grumbles while hammering a tiny shield*"
        "old_quill" -> "\"$greeting The winds of fate blow curiously today...\""
        else -> "\"$greeting\""
    }
}

private fun getRelationshipLevel(score: Int): String {
    return when {
        score >= 75 -> "Best Friends"
        score >= 50 -> "Close Friend"
        score >= 25 -> "Friend"
        score >= 10 -> "Friendly"
        score >= -10 -> "Neutral"
        score >= -25 -> "Unfriendly"
        score >= -50 -> "Hostile"
        else -> "Enemy"
    }
}

private fun getRelationshipColor(score: Int): Color {
    return when {
        score >= 50 -> Color(0xFF44ff44)  // Green
        score >= 10 -> Color(0xFF88ff88)  // Light green
        score >= -10 -> Color(0xFFcccccc) // Gray (neutral)
        score >= -50 -> Color(0xFFff8844) // Orange
        else -> Color(0xFFff4444)         // Red
    }
}
