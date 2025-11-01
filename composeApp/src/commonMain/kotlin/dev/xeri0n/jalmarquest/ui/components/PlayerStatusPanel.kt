package dev.xeri0n.jalmarquest.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jalmarquest.shared.model.Player
import dev.xeri0n.jalmarquest.ui.theme.*

/**
 * Floating status panel that displays player stats in a compact, always-visible format.
 * Can be minimized to just show character icon and HP.
 * Designed to be shown in the corner of the main gameplay screen.
 */
@Composable
fun PlayerStatusPanel(
    player: Player,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }
    
    val healthPercent = (player.stats.currentHealth.toFloat() / player.stats.maxHealth.toFloat()).coerceIn(0f, 1f)
    val staminaPercent = (player.stats.currentStamina.toFloat() / player.stats.maxStamina.toFloat()).coerceIn(0f, 1f)
    
    // Pulse animation for low health warning
    val lowHealthPulse by rememberInfiniteTransition(label = "low_health_pulse").animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    val isLowHealth = healthPercent < 0.3f
    
    if (isExpanded) {
        // Full expanded panel
        JQCard(
            modifier = modifier.width(220.dp),
            backgroundColor = SurfaceDark.copy(alpha = 0.95f),
            borderColor = if (isLowHealth) DangerRed.copy(alpha = lowHealthPulse) else QuailBrown
        ) {
            Column(
                modifier = Modifier
                    .padding(Spacing.medium)
                    .clickable { isExpanded = false }
            ) {
                // Player name and level
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Quail icon
                        Text(
                            text = "🐦",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.width(Spacing.small))
                        Column {
                            Text(
                                text = player.name,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SeedGold
                                )
                            )
                            Text(
                                text = "Level ${player.level}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            )
                        }
                    }
                    
                    // XP to next level indicator
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(GrassGreenDark)
                            .border(2.dp, SeedGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${player.level}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(Spacing.medium))
                
                // Health bar
                StatBar(
                    label = "HP",
                    current = player.stats.currentHealth,
                    max = player.stats.maxHealth,
                    percentage = healthPercent,
                    color = when {
                        healthPercent > 0.6f -> SuccessGreen
                        healthPercent > 0.3f -> WarningAmber
                        else -> DangerRed
                    },
                    icon = "❤️"
                )
                
                Spacer(modifier = Modifier.height(Spacing.small))
                
                // Stamina bar
                StatBar(
                    label = "Stamina",
                    current = player.stats.currentStamina,
                    max = player.stats.maxStamina,
                    percentage = staminaPercent,
                    color = Color(0xFF4FC3F7),  // Light blue
                    icon = "⚡"
                )
                
                Spacer(modifier = Modifier.height(Spacing.small))
                
                // Combat stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CompactStat(icon = "⚔️", value = player.stats.attack.toString())
                    CompactStat(icon = "🛡️", value = player.stats.defense.toString())
                    CompactStat(icon = "💨", value = player.stats.speed.toString())
                }
            }
        }
    } else {
        // Minimized panel - just icon and HP
        JQCard(
            modifier = modifier.width(80.dp),
            backgroundColor = SurfaceDark.copy(alpha = 0.95f),
            borderColor = if (isLowHealth) DangerRed.copy(alpha = lowHealthPulse) else QuailBrown
        ) {
            Column(
                modifier = Modifier
                    .padding(Spacing.small)
                    .clickable { isExpanded = true },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Quail icon
                Text(
                    text = "🐦",
                    style = MaterialTheme.typography.headlineMedium
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // HP value
                Text(
                    text = "${player.stats.currentHealth}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = when {
                            healthPercent > 0.6f -> SuccessGreen
                            healthPercent > 0.3f -> WarningAmber
                            else -> DangerRed
                        }
                    )
                )
                
                // Small HP bar
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(CornerRadius.small))
                        .background(Color.DarkGray)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(healthPercent)
                            .clip(RoundedCornerShape(CornerRadius.small))
                            .background(
                                when {
                                    healthPercent > 0.6f -> SuccessGreen
                                    healthPercent > 0.3f -> WarningAmber
                                    else -> DangerRed
                                }
                            )
                    )
                }
            }
        }
    }
}

/**
 * Stat bar component for HP and Stamina.
 */
@Composable
private fun StatBar(
    label: String,
    current: Int,
    max: Int,
    percentage: Float,
    color: Color,
    icon: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Label row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = icon,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
            Text(
                text = "$current / $max",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(CornerRadius.small))
                .background(Color.DarkGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(percentage)
                    .clip(RoundedCornerShape(CornerRadius.small))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(color, color.copy(alpha = 0.7f))
                        )
                    )
            )
        }
    }
}

/**
 * Compact stat display for attack, defense, speed.
 */
@Composable
private fun CompactStat(
    icon: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = SeedGold
            )
        )
    }
}
