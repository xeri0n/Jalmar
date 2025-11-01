package dev.xeri0n.jalmarquest.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * JalmarQuest Color Palette
 * 
 * Inspired by quail natural habitat: earthy tones, grass greens, seed golds
 * Designed for both dark and light themes with accessibility (WCAG AA contrast)
 */

// Primary - Earthy Brown (quail plumage colors)
val QuailBrown = Color(0xFF8B6F47)
val QuailBrownLight = Color(0xFFB89968)
val QuailBrownDark = Color(0xFF5D4A2F)

// Secondary - Grass Green (natural habitat)
val GrassGreen = Color(0xFF6B8E23)
val GrassGreenLight = Color(0xFF8BAA42)
val GrassGreenDark = Color(0xFF4A6118)

// Accent - Golden Seeds (currency, highlights)
val SeedGold = Color(0xFFFFD700)
val SeedGoldLight = Color(0xFFFFE55C)
val SeedGoldDark = Color(0xFFB8960A)

// Glimmer Shards - Premium Currency (mystical blue)
val GlimmerBlue = Color(0xFF4FC3F7)
val GlimmerBlueLight = Color(0xFF88D5F7)
val GlimmerBlueDark = Color(0xFF0093C4)

// Status Colors
val DangerRed = Color(0xFFD32F2F)
val DangerRedLight = Color(0xFFEF5350)
val DangerRedDark = Color(0xFF9A0007)

val SuccessGreen = Color(0xFF388E3C)
val SuccessGreenLight = Color(0xFF66BB6A)
val SuccessGreenDark = Color(0xFF00600F)

val WarningAmber = Color(0xFFFFA726)
val WarningAmberLight = Color(0xFFFFB74D)
val WarningAmberDark = Color(0xFFF57C00)

val InfoBlue = Color(0xFF1976D2)
val InfoBlueLight = Color(0xFF42A5F5)
val InfoBlueDark = Color(0xFF004BA0)

// Neutral Grays
val NeutralGray = Color(0xFF9E9E9E)
val NeutralGrayLight = Color(0xFFBDBDBD)
val NeutralGrayDark = Color(0xFF616161)

// Rarity Colors (for items, equipment, cosmetics)
val RarityCommon = Color(0xFFB0BEC5)      // Gray
val RarityUncommon = Color(0xFF81C784)     // Green
val RarityRare = Color(0xFF64B5F6)         // Blue
val RarityEpic = Color(0xFFBA68C8)         // Purple
val RarityLegendary = Color(0xFFFFB74D)    // Orange/Gold
val RarityMythic = Color(0xFFE91E63)       // Pink/Red

// HP & Stamina Bar Colors
val HPBarRed = Color(0xFFE53935)
val HPBarRedLight = Color(0xFFEF5350)
val HPBarRedDark = Color(0xFFC62828)

val StaminaBarGreen = Color(0xFF43A047)
val StaminaBarGreenLight = Color(0xFF66BB6A)
val StaminaBarGreenDark = Color(0xFF2E7D32)

val ManaBarBlue = Color(0xFF1E88E5)        // Future magic system
val ManaBarBlueLight = Color(0xFF42A5F5)
val ManaBarBlueDark = Color(0xFF1565C0)

// XP Bar Color
val XPBarPurple = Color(0xFF7E57C2)
val XPBarPurpleLight = Color(0xFF9575CD)
val XPBarPurpleDark = Color(0xFF5E35B1)

// Background Colors (for glassmorphism effects)
val BackgroundDark = Color(0xFF121212)
val BackgroundLight = Color(0xFFFAF9F6)
val SurfaceDark = Color(0xFF1E1E1E)
val SurfaceLight = Color(0xFFFFFFFF)

// Overlay Colors (for dialogs, modals)
val OverlayDark = Color(0xCC000000)        // 80% black
val OverlayLight = Color(0x80FFFFFF)       // 50% white

// Biome-Specific Colors (for location theming)
val BiomeGrassland = Color(0xFF7CB342)
val BiomeForest = Color(0xFF558B2F)
val BiomeDesert = Color(0xFFEF6C00)
val BiomeCave = Color(0xFF424242)
val BiomeSwamp = Color(0xFF689F38)
val BiomeMountain = Color(0xFF78909C)
val BiomeTundra = Color(0xFFB0BEC5)
val BiomeCoastal = Color(0xFF0288D1)

// Weather Colors (for atmospheric effects)
val WeatherClear = Color(0xFFFFEB3B)       // Sunny yellow
val WeatherRainy = Color(0xFF607D8B)       // Stormy gray
val WeatherStormy = Color(0xFF455A64)      // Dark gray
val WeatherFoggy = Color(0xFFCFD8DC)       // Light gray
val WeatherSnowy = Color(0xFFECEFF1)       // White-ish
val WeatherWindy = Color(0xFF90A4AE)       // Windy gray

// Special Effect Colors
val GlowEffect = Color(0xFFFFEB3B)         // Golden glow for special items
val ShadowDark = Color(0x66000000)         // 40% black for shadows
val HighlightWhite = Color(0x33FFFFFF)     // 20% white for highlights

// Companion Loyalty Colors (gradient from hostile to devoted)
val LoyaltyHostile = Color(0xFFD32F2F)     // Red
val LoyaltyUnfriendly = Color(0xFFFF6F00)  // Orange
val LoyaltyNeutral = Color(0xFFFDD835)     // Yellow
val LoyaltyFriendly = Color(0xFF7CB342)    // Light Green
val LoyaltyLoyal = Color(0xFF43A047)       // Green
val LoyaltyDevoted = Color(0xFF1E88E5)     // Blue

// Nest Prestige Colors
val PrestigeBronze = Color(0xFFCD7F32)
val PrestigeSilver = Color(0xFFC0C0C0)
val PrestigeGold = Color(0xFFFFD700)
val PrestigePlatinum = Color(0xFFE5E4E2)

/**
 * Extension functions for color manipulation
 */
fun Color.withAlpha(alpha: Float): Color {
    return this.copy(alpha = alpha)
}

fun Color.brighten(factor: Float = 0.2f): Color {
    return Color(
        red = (red + (1f - red) * factor).coerceIn(0f, 1f),
        green = (green + (1f - green) * factor).coerceIn(0f, 1f),
        blue = (blue + (1f - blue) * factor).coerceIn(0f, 1f),
        alpha = alpha
    )
}

fun Color.darken(factor: Float = 0.2f): Color {
    return Color(
        red = (red * (1f - factor)).coerceIn(0f, 1f),
        green = (green * (1f - factor)).coerceIn(0f, 1f),
        blue = (blue * (1f - factor)).coerceIn(0f, 1f),
        alpha = alpha
    )
}
