package dev.xeri0n.jalmarquest.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Dark color scheme for JalmarQuest
 * 
 * Designed for nighttime play with reduced eye strain
 * Uses warm earth tones to maintain quail aesthetic
 */
private val DarkColorScheme = darkColorScheme(
    // Primary colors (main UI elements)
    primary = QuailBrown,
    onPrimary = Color.White,
    primaryContainer = QuailBrownDark,
    onPrimaryContainer = QuailBrownLight,
    
    // Secondary colors (accents, secondary actions)
    secondary = GrassGreen,
    onSecondary = Color.White,
    secondaryContainer = GrassGreenDark,
    onSecondaryContainer = GrassGreenLight,
    
    // Tertiary colors (additional accents)
    tertiary = SeedGold,
    onTertiary = Color.Black,
    tertiaryContainer = SeedGoldDark,
    onTertiaryContainer = SeedGoldLight,
    
    // Error colors
    error = DangerRed,
    onError = Color.White,
    errorContainer = DangerRedDark,
    onErrorContainer = DangerRedLight,
    
    // Background colors
    background = BackgroundDark,
    onBackground = Color(0xFFE0E0E0),
    
    // Surface colors (cards, sheets, dialogs)
    surface = SurfaceDark,
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFCCCCCC),
    
    // Outline colors (borders, dividers)
    outline = Color(0xFF4A4A4A),
    outlineVariant = Color(0xFF333333),
    
    // Scrim (overlay for modals)
    scrim = OverlayDark,
    
    // Inverse colors (for snackbars, tooltips)
    inverseSurface = Color(0xFFE0E0E0),
    inverseOnSurface = Color(0xFF1E1E1E),
    inversePrimary = QuailBrownLight,
    
    // Surface tints
    surfaceTint = QuailBrown
)

/**
 * Light color scheme for JalmarQuest
 * 
 * Designed for daytime play with bright, natural colors
 * Maintains quail-scale aesthetic with earthy palette
 */
private val LightColorScheme = lightColorScheme(
    // Primary colors
    primary = QuailBrownDark,
    onPrimary = Color.White,
    primaryContainer = QuailBrownLight,
    onPrimaryContainer = QuailBrownDark,
    
    // Secondary colors
    secondary = GrassGreenDark,
    onSecondary = Color.White,
    secondaryContainer = GrassGreenLight,
    onSecondaryContainer = GrassGreenDark,
    
    // Tertiary colors
    tertiary = SeedGoldDark,
    onTertiary = Color.White,
    tertiaryContainer = SeedGoldLight,
    onTertiaryContainer = SeedGoldDark,
    
    // Error colors
    error = DangerRed,
    onError = Color.White,
    errorContainer = DangerRedLight,
    onErrorContainer = DangerRedDark,
    
    // Background colors
    background = BackgroundLight,
    onBackground = Color(0xFF1C1C1C),
    
    // Surface colors
    surface = SurfaceLight,
    onSurface = Color(0xFF1C1C1C),
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF4A4A4A),
    
    // Outline colors
    outline = Color(0xFFBDBDBD),
    outlineVariant = Color(0xFFE0E0E0),
    
    // Scrim
    scrim = OverlayLight,
    
    // Inverse colors
    inverseSurface = Color(0xFF2C2C2C),
    inverseOnSurface = Color(0xFFF5F5F5),
    inversePrimary = QuailBrown,
    
    // Surface tints
    surfaceTint = QuailBrownDark
)

/**
 * JalmarQuest Theme
 * 
 * Main theme composable that applies colors, typography, and shapes
 * Supports dynamic dark/light mode based on system settings
 * 
 * @param darkTheme Whether to use dark theme (defaults to system preference)
 * @param content The composable content to theme
 */
@Composable
fun JalmarQuestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = JalmarQuestTypography,
        content = content
    )
}

/**
 * Preview helper for light theme
 */
@Composable
fun JalmarQuestLightTheme(content: @Composable () -> Unit) {
    JalmarQuestTheme(darkTheme = false, content = content)
}

/**
 * Preview helper for dark theme
 */
@Composable
fun JalmarQuestDarkTheme(content: @Composable () -> Unit) {
    JalmarQuestTheme(darkTheme = true, content = content)
}
