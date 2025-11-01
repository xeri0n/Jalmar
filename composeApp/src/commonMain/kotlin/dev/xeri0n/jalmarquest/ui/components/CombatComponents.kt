package dev.xeri0n.jalmarquest.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jalmarquest.shared.skills.Skill
import dev.xeri0n.jalmarquest.ui.theme.*
import kotlin.math.abs

/**
 * AAA-Tier Combat Components
 * 
 * Contains advanced combat UI components with animations:
 * - SkillButton (cooldown overlays, pulse effects)
 * - DamageNumber (spring particle animation)
 * - StatusEffectIcon (tooltip + pulse)
 * - TurnIndicator (animated arrow)
 * - CombatHealthBar (damage shake effect)
 */

/**
 * Animated skill button with cooldown overlay and hover effects.
 */
@Composable
fun SkillButton(
    skill: Skill,
    isAvailable: Boolean,
    cooldownRemaining: Int = 0,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pressScale by animateFloatAsState(
        targetValue = if (isAvailable) 1f else 0.95f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "skill_press_scale"
    )
    
    // Pulse animation for available skills
    val pulseScale by rememberInfiniteTransition(label = "pulse_transition").animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    
    Box(
        modifier = modifier
            .size(80.dp)
            .scale(if (isAvailable) pressScale else 1f)
            .graphicsLayer {
                scaleX = if (isAvailable) pulseScale else 1f
                scaleY = if (isAvailable) pulseScale else 1f
            }
            .clip(RoundedCornerShape(CornerRadius.medium))
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isAvailable) {
                        listOf(GrassGreen, GrassGreenDark)
                    } else {
                        listOf(Color.Gray.copy(alpha = 0.5f), Color.DarkGray.copy(alpha = 0.5f))
                    }
                )
            )
            .border(
                width = 2.dp,
                color = if (isAvailable) SeedGold else Color.Gray,
                shape = RoundedCornerShape(CornerRadius.medium)
            )
            .clickable(enabled = isAvailable) { onClick() }
            .padding(Spacing.small),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Skill icon (emoji placeholder - replace with actual icons later)
            Text(
                text = getSkillIcon(skill.archetype.name),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.alpha(if (isAvailable) 1f else 0.4f)
            )
            
            // Skill name
            Text(
                text = skill.name.take(8),  // Truncate long names
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isAvailable) Color.White else Color.LightGray,
                    fontSize = 10.sp
                ),
                maxLines = 1
            )
        }
        
        // Cooldown overlay
        if (cooldownRemaining > 0) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cooldownRemaining.toString(),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    }
}

/**
 * Animated damage number that floats upward and fades out.
 * Spring physics for realistic movement.
 */
@Composable
fun DamageNumber(
    damage: Int,
    isCritical: Boolean = false,
    isHealing: Boolean = false,
    startPosition: Offset = Offset.Zero,
    onAnimationEnd: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(true) }
    
    // Upward float animation
    val offsetY by animateFloatAsState(
        targetValue = if (visible) -100f else -200f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "damage_offset_y",
        finishedListener = {
            onAnimationEnd()
        }
    )
    
    // Fade out animation
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(800, easing = LinearEasing),
        label = "damage_alpha"
    )
    
    // Scale animation (bigger for crits)
    val scale by animateFloatAsState(
        targetValue = if (isCritical) 1.5f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "damage_scale"
    )
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100)
        visible = false
    }
    
    Text(
        text = if (isHealing) "+$damage" else "-$damage",
        style = GameTypography.damageNumber.copy(
            color = when {
                isHealing -> SuccessGreen
                isCritical -> WarningAmber
                else -> DangerRed
            },
            fontWeight = if (isCritical) FontWeight.ExtraBold else FontWeight.Bold,
            fontSize = if (isCritical) 32.sp else 24.sp
        ),
        modifier = modifier
            .graphicsLayer {
                translationY = offsetY
                this.alpha = alpha
                scaleX = scale
                scaleY = scale
            }
    )
}

/**
 * Status effect icon with pulse animation and tooltip.
 */
@Composable
fun StatusEffectIcon(
    effectType: com.jalmarquest.shared.combat.StatusEffectType,
    duration: Int,
    modifier: Modifier = Modifier
) {
    // Pulse animation
    val pulseAlpha by rememberInfiniteTransition(label = "status_pulse").animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )
    
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(getStatusEffectColor(effectType).copy(alpha = pulseAlpha))
            .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = getStatusEffectIcon(effectType),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
            )
            if (duration > 0) {
                Text(
                    text = duration.toString(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    }
}

/**
 * Animated turn indicator arrow pointing at active participant.
 */
@Composable
fun TurnIndicator(
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    // Bounce animation
    val bounceOffset by rememberInfiniteTransition(label = "turn_bounce").animateFloat(
        initialValue = 0f,
        targetValue = if (isActive) 10f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce_offset"
    )
    
    if (isActive) {
        Text(
            text = "▶",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = SeedGold,
                fontWeight = FontWeight.Bold
            ),
            modifier = modifier.graphicsLayer {
                translationX = bounceOffset
            }
        )
    }
}

/**
 * Combat health bar with damage shake effect.
 */
@Composable
fun CombatHealthBar(
    current: Int,
    max: Int,
    label: String,
    showDamageShake: Boolean = false,
    modifier: Modifier = Modifier
) {
    // Damage shake animation
    val shakeOffset by animateFloatAsState(
        targetValue = if (showDamageShake) 10f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "shake_offset"
    )
    
    val healthPercent = (current.toFloat() / max.toFloat()).coerceIn(0f, 1f)
    
    Column(
        modifier = modifier.graphicsLayer {
            translationX = if (showDamageShake) {
                // Oscillate shake offset
                shakeOffset * kotlin.math.sin(System.currentTimeMillis() / 50.0).toFloat()
            } else {
                0f
            }
        }
    ) {
        // Label
        Text(
            text = "$label: $current / $max",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Health bar background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(CornerRadius.small))
                .background(Color.DarkGray)
        ) {
            // Health bar fill (gradient based on health percent)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(healthPercent)
                    .clip(RoundedCornerShape(CornerRadius.small))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = when {
                                healthPercent > 0.6f -> listOf(SuccessGreen, GrassGreen)
                                healthPercent > 0.3f -> listOf(WarningAmber, WarningAmber.copy(alpha = 0.7f))
                                else -> listOf(DangerRed, DangerRedDark)
                            }
                        )
                    )
            )
        }
    }
}

// Helper functions

private fun getSkillIcon(archetypeName: String): String {
    return when (archetypeName.uppercase()) {
        "FIGHTER" -> "⚔️"
        "DEFENDER" -> "🛡️"
        "SUPPORTER" -> "✨"
        else -> "🔮"
    }
}

private fun getStatusEffectIcon(effectType: com.jalmarquest.shared.combat.StatusEffectType): String {
    return when (effectType) {
        com.jalmarquest.shared.combat.StatusEffectType.POISON -> "☠️"
        com.jalmarquest.shared.combat.StatusEffectType.BURN -> "🔥"
        com.jalmarquest.shared.combat.StatusEffectType.STUN -> "💫"
        com.jalmarquest.shared.combat.StatusEffectType.REGENERATION -> "💚"
        com.jalmarquest.shared.combat.StatusEffectType.STRENGTHEN -> "⚡"
        com.jalmarquest.shared.combat.StatusEffectType.VULNERABLE -> "�"
        com.jalmarquest.shared.combat.StatusEffectType.WEAKEN -> "❌"
    }
}

private fun getStatusEffectColor(effectType: com.jalmarquest.shared.combat.StatusEffectType): Color {
    return when (effectType) {
        com.jalmarquest.shared.combat.StatusEffectType.POISON -> Color(0xFF8B00FF)  // Purple
        com.jalmarquest.shared.combat.StatusEffectType.BURN -> Color(0xFFFF4500)  // Orange-red
        com.jalmarquest.shared.combat.StatusEffectType.STUN -> Color(0xFFFFD700)  // Gold
        com.jalmarquest.shared.combat.StatusEffectType.REGENERATION -> Color(0xFF32CD32)  // Lime green
        com.jalmarquest.shared.combat.StatusEffectType.STRENGTHEN -> Color(0xFFFF6347)  // Tomato
        com.jalmarquest.shared.combat.StatusEffectType.VULNERABLE -> Color(0xFF696969)  // Dim gray
        com.jalmarquest.shared.combat.StatusEffectType.WEAKEN -> Color(0xFF8B0000)  // Dark red
    }
}

// Offset data class for damage number positioning
data class Offset(val x: Float, val y: Float) {
    companion object {
        val Zero = Offset(0f, 0f)
    }
}
