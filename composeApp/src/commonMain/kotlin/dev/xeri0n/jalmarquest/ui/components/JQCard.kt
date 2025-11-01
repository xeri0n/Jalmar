package dev.xeri0n.jalmarquest.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.xeri0n.jalmarquest.ui.theme.*

/**
 * JalmarQuest Card Component
 * 
 * Glassmorphism-style card with:
 * - Elevation
 * - Border options
 * - Clickable variant
 * - Custom colors
 * - Content padding
 * 
 * @param modifier Modifier for customization
 * @param onClick Optional click callback (makes card clickable)
 * @param backgroundColor Card background color
 * @param borderColor Optional border color
 * @param borderWidth Border thickness
 * @param elevation Shadow elevation
 * @param padding Content padding
 * @param content Card content
 */
@Composable
fun JQCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color? = null,
    borderWidth: Dp = BorderWidth.thin,
    elevation: Dp = Elevation.level2,
    padding: Dp = Spacing.large,
    content: @Composable ColumnScope.() -> Unit
) {
    val cardModifier = modifier
        .clip(RoundedCornerShape(CornerRadius.medium))
        .then(
            if (borderColor != null) {
                Modifier.border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(CornerRadius.medium)
                )
            } else {
                Modifier
            }
        )
    
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = cardModifier,
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation)
        ) {
            Column(
                modifier = Modifier.padding(padding),
                content = content
            )
        }
    } else {
        Card(
            modifier = cardModifier,
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation)
        ) {
            Column(
                modifier = Modifier.padding(padding),
                content = content
            )
        }
    }
}

/**
 * JalmarQuest Progress Bar Component
 * 
 * Animated progress bar with:
 * - Gradient support
 * - Custom colors
 * - Smooth animations
 * - Label overlay
 * - Height variants
 * 
 * @param progress Current progress (0.0 to 1.0)
 * @param modifier Modifier for customization
 * @param foregroundColor Progress bar fill color
 * @param backgroundColor Background color
 * @param gradient Optional gradient brush (overrides foregroundColor)
 * @param showLabel Whether to show progress percentage
 * @param label Custom label text (overrides percentage)
 * @param height Bar height
 */
@Composable
fun JQProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    foregroundColor: Color = GrassGreen,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    gradient: Brush? = null,
    showLabel: Boolean = false,
    label: String? = null,
    height: Dp = ComponentSize.progressBarHeight
) {
    // Animate progress changes
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(
            durationMillis = AnimationDuration.normal,
            easing = FastOutSlowInEasing
        ),
        label = "progress_animation"
    )
    
    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(CornerRadius.pill))
            .background(backgroundColor)
    ) {
        // Progress fill
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .clip(RoundedCornerShape(CornerRadius.pill))
                .background(
                    brush = gradient ?: Brush.horizontalGradient(
                        colors = listOf(foregroundColor, foregroundColor)
                    )
                )
        )
        
        // Label overlay
        if (showLabel) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label ?: "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (progress > 0.5f) Color.White else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * HP Bar Component (Red gradient)
 */
@Composable
fun JQHealthBar(
    currentHP: Int,
    maxHP: Int,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true
) {
    val progress = if (maxHP > 0) currentHP.toFloat() / maxHP.toFloat() else 0f
    
    JQProgressBar(
        progress = progress,
        modifier = modifier,
        gradient = Brush.horizontalGradient(
            colors = listOf(HPBarRedDark, HPBarRed, HPBarRedLight)
        ),
        backgroundColor = Color.Black.copy(alpha = 0.3f),
        showLabel = showLabel,
        label = "$currentHP / $maxHP HP",
        height = ComponentSize.progressBarHeightThick
    )
}

/**
 * Stamina Bar Component (Green gradient)
 */
@Composable
fun JQStaminaBar(
    currentStamina: Int,
    maxStamina: Int,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true
) {
    val progress = if (maxStamina > 0) currentStamina.toFloat() / maxStamina.toFloat() else 0f
    
    JQProgressBar(
        progress = progress,
        modifier = modifier,
        gradient = Brush.horizontalGradient(
            colors = listOf(StaminaBarGreenDark, StaminaBarGreen, StaminaBarGreenLight)
        ),
        backgroundColor = Color.Black.copy(alpha = 0.3f),
        showLabel = showLabel,
        label = "$currentStamina / $maxStamina",
        height = ComponentSize.progressBarHeightThick
    )
}

/**
 * XP Bar Component (Purple gradient)
 */
@Composable
fun JQXPBar(
    currentXP: Long,
    requiredXP: Long,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true
) {
    val progress = if (requiredXP > 0) (currentXP.toFloat() / requiredXP.toFloat()).coerceIn(0f, 1f) else 0f
    
    JQProgressBar(
        progress = progress,
        modifier = modifier,
        gradient = Brush.horizontalGradient(
            colors = listOf(XPBarPurpleDark, XPBarPurple, XPBarPurpleLight)
        ),
        backgroundColor = Color.Black.copy(alpha = 0.3f),
        showLabel = showLabel,
        label = "$currentXP / $requiredXP XP",
        height = ComponentSize.progressBarHeight
    )
}

/**
 * Generic Circular Progress Indicator with percentage
 */
@Composable
fun JQCircularProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    strokeWidth: Dp = 8.dp,
    showPercentage: Boolean = true
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(
            durationMillis = AnimationDuration.normal,
            easing = FastOutSlowInEasing
        ),
        label = "circular_progress"
    )
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Background circle
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxSize(),
            color = backgroundColor,
            strokeWidth = strokeWidth
        )
        
        // Progress circle
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxSize(),
            color = color,
            strokeWidth = strokeWidth
        )
        
        // Percentage text
        if (showPercentage) {
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Shimmer Effect Progress Bar (for loading states)
 */
@Composable
fun JQShimmerProgressBar(
    modifier: Modifier = Modifier,
    height: Dp = ComponentSize.progressBarHeight
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )
    
    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(CornerRadius.pill))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        NeutralGrayLight.copy(alpha = 0.3f),
                        NeutralGrayLight.copy(alpha = 0.6f),
                        NeutralGrayLight.copy(alpha = 0.3f)
                    ),
                    startX = shimmerOffset * 1000f,
                    endX = (shimmerOffset + 1f) * 1000f
                )
            )
    )
}
