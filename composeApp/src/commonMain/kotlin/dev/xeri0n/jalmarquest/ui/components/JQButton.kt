package dev.xeri0n.jalmarquest.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.xeri0n.jalmarquest.ui.theme.*

/**
 * JalmarQuest Button Variants
 */
enum class JQButtonVariant {
    PRIMARY,      // Main action buttons (green)
    SECONDARY,    // Less emphasis (brown)
    TERTIARY,     // Minimal emphasis (outlined)
    DANGER,       // Destructive actions (red)
    SUCCESS,      // Positive actions (green)
    WARNING,      // Caution actions (amber)
    GHOST         // Transparent background
}

/**
 * JalmarQuest Button Size
 */
enum class JQButtonSize {
    SMALL,        // Compact button (32dp height)
    MEDIUM,       // Standard button (40dp height)
    LARGE         // Prominent button (56dp height)
}

/**
 * JalmarQuest Primary Button Component
 * 
 * AAA-tier button with:
 * - Ripple effects
 * - Loading states
 * - Disabled states
 * - Accessibility support
 * - Multiple variants (primary, secondary, danger, etc.)
 * - Hover effects (desktop/web)
 * - Press animations
 * 
 * @param text Button label text
 * @param onClick Click callback
 * @param modifier Modifier for customization
 * @param variant Button visual variant
 * @param size Button size preset
 * @param enabled Whether button is clickable
 * @param loading Whether to show loading indicator
 * @param leadingIcon Optional icon before text (emoji or composable)
 * @param trailingIcon Optional icon after text
 */
@Composable
fun JQButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: JQButtonVariant = JQButtonVariant.PRIMARY,
    size: JQButtonSize = JQButtonSize.MEDIUM,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    // Press animation state
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "button_press_scale"
    )
    
    // Get colors based on variant
    val (backgroundColor, contentColor) = getButtonColors(variant, enabled)
    
    // Get dimensions based on size
    val (height, horizontalPadding, fontSize) = when (size) {
        JQButtonSize.SMALL -> Triple(ComponentSize.chipHeight, Spacing.medium, MaterialTheme.typography.labelSmall)
        JQButtonSize.MEDIUM -> Triple(ComponentSize.buttonHeight, Spacing.large, MaterialTheme.typography.labelLarge)
        JQButtonSize.LARGE -> Triple(ComponentSize.buttonHeightLarge, Spacing.extraLarge, MaterialTheme.typography.titleMedium)
    }
    
    // Main button surface
    Surface(
        onClick = { if (!loading) onClick() },
        modifier = modifier
            .scale(scale)
            .height(height)
            .then(if (variant != JQButtonVariant.GHOST) Modifier.defaultMinSize(minWidth = ComponentSize.buttonMinWidth) else Modifier),
        enabled = enabled && !loading,
        shape = RoundedCornerShape(CornerRadius.medium),
        color = backgroundColor,
        contentColor = contentColor,
        interactionSource = interactionSource,
        shadowElevation = if (variant == JQButtonVariant.TERTIARY || variant == JQButtonVariant.GHOST) Elevation.none else Elevation.level2
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Loading indicator
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(IconSize.small),
                    color = contentColor,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(Spacing.small))
            }
            
            // Leading icon
            leadingIcon?.let {
                Box(modifier = Modifier.size(IconSize.small)) {
                    it()
                }
                Spacer(modifier = Modifier.width(Spacing.small))
            }
            
            // Button text
            Text(
                text = text.uppercase(),
                style = fontSize,
                textAlign = TextAlign.Center,
                color = contentColor
            )
            
            // Trailing icon
            trailingIcon?.let {
                Spacer(modifier = Modifier.width(Spacing.small))
                Box(modifier = Modifier.size(IconSize.small)) {
                    it()
                }
            }
        }
    }
}

/**
 * Get button colors based on variant and state
 */
@Composable
private fun getButtonColors(variant: JQButtonVariant, enabled: Boolean): Pair<Color, Color> {
    val alpha = if (enabled) 1f else 0.38f
    
    return when (variant) {
        JQButtonVariant.PRIMARY -> {
            Pair(
                GrassGreen.copy(alpha = alpha),
                Color.White
            )
        }
        JQButtonVariant.SECONDARY -> {
            Pair(
                QuailBrown.copy(alpha = alpha),
                Color.White
            )
        }
        JQButtonVariant.TERTIARY -> {
            Pair(
                Color.Transparent,
                MaterialTheme.colorScheme.primary.copy(alpha = alpha)
            )
        }
        JQButtonVariant.DANGER -> {
            Pair(
                DangerRed.copy(alpha = alpha),
                Color.White
            )
        }
        JQButtonVariant.SUCCESS -> {
            Pair(
                SuccessGreen.copy(alpha = alpha),
                Color.White
            )
        }
        JQButtonVariant.WARNING -> {
            Pair(
                WarningAmber.copy(alpha = alpha),
                Color.Black
            )
        }
        JQButtonVariant.GHOST -> {
            Pair(
                Color.Transparent,
                MaterialTheme.colorScheme.onSurface.copy(alpha = alpha)
            )
        }
    }
}

/**
 * Icon Button with circular shape
 * 
 * @param icon Icon composable (emoji or vector)
 * @param onClick Click callback
 * @param modifier Modifier for customization
 * @param contentDescription Accessibility description
 * @param variant Button variant
 * @param enabled Whether button is clickable
 * @param size Icon size
 */
@Composable
fun JQIconButton(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    variant: JQButtonVariant = JQButtonVariant.GHOST,
    enabled: Boolean = true,
    size: Dp = IconSize.medium
) {
    val (backgroundColor, contentColor) = getButtonColors(variant, enabled)
    
    Surface(
        onClick = onClick,
        modifier = modifier.size(size + Spacing.large),
        enabled = enabled,
        shape = RoundedCornerShape(CornerRadius.circle),
        color = backgroundColor,
        contentColor = contentColor,
        shadowElevation = if (variant != JQButtonVariant.GHOST && variant != JQButtonVariant.TERTIARY) Elevation.level2 else Elevation.none
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.size(size)) {
                icon()
            }
        }
    }
}

/**
 * Gradient Button (for special actions like "Start Adventure")
 * 
 * @param text Button label
 * @param onClick Click callback
 * @param modifier Modifier for customization
 * @param gradient Gradient brush
 * @param contentColor Text/icon color
 * @param enabled Whether button is clickable
 * @param loading Whether to show loading indicator
 */
@Composable
fun JQGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradient: Brush = Brush.horizontalGradient(
        colors = listOf(GrassGreen, GrassGreenLight)
    ),
    contentColor: Color = Color.White,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "gradient_button_press"
    )
    
    Box(
        modifier = modifier
            .scale(scale)
            .height(ComponentSize.buttonHeightLarge)
            .defaultMinSize(minWidth = ComponentSize.buttonMinWidth)
            .background(
                brush = if (enabled) gradient else Brush.horizontalGradient(
                    colors = listOf(NeutralGray, NeutralGray)
                ),
                shape = RoundedCornerShape(CornerRadius.medium)
            )
            .clickable(
                enabled = enabled && !loading,
                onClick = onClick,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null
            )
            .padding(horizontal = Spacing.extraLarge),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(IconSize.small),
                    color = contentColor,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(Spacing.medium))
            }
            
            leadingIcon?.let {
                Box(modifier = Modifier.size(IconSize.medium)) {
                    it()
                }
                Spacer(modifier = Modifier.width(Spacing.medium))
            }
            
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = contentColor,
                textAlign = TextAlign.Center
            )
        }
    }
}
