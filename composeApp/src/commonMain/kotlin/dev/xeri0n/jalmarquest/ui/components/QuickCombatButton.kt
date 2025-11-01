package dev.xeri0n.jalmarquest.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.xeri0n.jalmarquest.ui.theme.*

/**
 * Floating action button for quick combat initiation.
 * One-click attack button that appears when enemies are nearby.
 */
@Composable
fun QuickCombatButton(
    enemyName: String,
    winRatePercent: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    // Pulse animation
    val pulseScale by rememberInfiniteTransition(label = "pulse_transition").animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    
    // Press animation
    var isPressed by remember { mutableStateOf(false) }
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "press_scale"
    )
    
    // Determine color based on win rate
    val buttonColor = when {
        winRatePercent >= 70 -> SuccessGreen
        winRatePercent >= 40 -> WarningAmber
        else -> DangerRed
    }
    
    Box(
        modifier = modifier
            .size(72.dp)
            .scale(pulseScale * pressScale)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = if (enabled) {
                        listOf(buttonColor, buttonColor.copy(alpha = 0.7f))
                    } else {
                        listOf(Color.Gray, Color.DarkGray)
                    }
                )
            )
            .border(3.dp, SeedGold, CircleShape)
            .clickable(enabled = enabled) {
                isPressed = true
                onClick()
                // Reset press state after animation
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Attack icon
            Text(
                text = "⚔️",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 28.sp
                )
            )
            
            // Win rate indicator
            Text(
                text = "$winRatePercent%",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 10.sp
                )
            )
        }
    }
}

/**
 * Compact floating action button for quick attack.
 * Shows just the sword icon with a subtle indicator.
 */
@Composable
fun QuickAttackFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showIndicator: Boolean = false
) {
    // Pulse animation when indicator is shown
    val pulseScale by rememberInfiniteTransition(label = "fab_pulse").animateFloat(
        initialValue = 1f,
        targetValue = if (showIndicator) 1.15f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    
    Box(
        modifier = modifier
            .size(64.dp)
            .scale(pulseScale)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = if (enabled) {
                        listOf(DangerRed, DangerRedDark)
                    } else {
                        listOf(Color.Gray, Color.DarkGray)
                    }
                )
            )
            .border(
                width = 3.dp,
                color = if (showIndicator) SeedGold else QuailBrown,
                shape = CircleShape
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "⚔",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 32.sp,
                color = Color.White
            )
        )
        
        // Notification indicator (red dot)
        if (showIndicator) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(WarningAmber)
                    .border(2.dp, Color.White, CircleShape)
            )
        }
    }
}
