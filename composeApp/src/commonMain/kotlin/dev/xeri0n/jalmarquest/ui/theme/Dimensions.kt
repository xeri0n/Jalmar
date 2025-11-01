package dev.xeri0n.jalmarquest.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * JalmarQuest Spacing System
 * 
 * Consistent spacing tokens following 4dp baseline grid
 * Used throughout the app for margins, padding, gaps
 */
object Spacing {
    /** 0dp - No spacing */
    val none: Dp = 0.dp
    
    /** 2dp - Minimal spacing (icon padding, chip padding) */
    val extraSmall: Dp = 2.dp
    
    /** 4dp - Tight spacing (between related elements) */
    val small: Dp = 4.dp
    
    /** 8dp - Default spacing (most common use case) */
    val medium: Dp = 8.dp
    
    /** 12dp - Comfortable spacing (between sections) */
    val mediumLarge: Dp = 12.dp
    
    /** 16dp - Standard spacing (card padding, screen margins) */
    val large: Dp = 16.dp
    
    /** 24dp - Generous spacing (between major sections) */
    val extraLarge: Dp = 24.dp
    
    /** 32dp - Large spacing (screen top/bottom padding) */
    val huge: Dp = 32.dp
    
    /** 48dp - Extra large spacing (splash screen elements) */
    val gigantic: Dp = 48.dp
    
    /** 64dp - Massive spacing (special use cases) */
    val massive: Dp = 64.dp
}

/**
 * Corner Radius System
 */
object CornerRadius {
    /** 0dp - No rounding (sharp corners) */
    val none: Dp = 0.dp
    
    /** 4dp - Subtle rounding (buttons, chips) */
    val small: Dp = 4.dp
    
    /** 8dp - Standard rounding (cards, dialogs) */
    val medium: Dp = 8.dp
    
    /** 12dp - Comfortable rounding (featured cards) */
    val large: Dp = 12.dp
    
    /** 16dp - Prominent rounding (modals, overlays) */
    val extraLarge: Dp = 16.dp
    
    /** 24dp - Pill shape (circular buttons) */
    val pill: Dp = 24.dp
    
    /** 50% - Full circle (avatar, icons) */
    val circle: Dp = 999.dp  // Large value for circular clipping
}

/**
 * Border Width System
 */
object BorderWidth {
    /** 0dp - No border */
    val none: Dp = 0.dp
    
    /** 1dp - Thin border (default) */
    val thin: Dp = 1.dp
    
    /** 2dp - Medium border (emphasis) */
    val medium: Dp = 2.dp
    
    /** 4dp - Thick border (strong emphasis, selected state) */
    val thick: Dp = 4.dp
}

/**
 * Elevation System (for Material3 shadows)
 */
object Elevation {
    /** 0dp - Flat (no shadow) */
    val none: Dp = 0.dp
    
    /** 1dp - Slight lift (subtle shadows) */
    val level1: Dp = 1.dp
    
    /** 2dp - Low elevation (buttons at rest) */
    val level2: Dp = 2.dp
    
    /** 4dp - Medium elevation (cards, buttons on hover) */
    val level3: Dp = 4.dp
    
    /** 8dp - High elevation (dialogs, dropdowns) */
    val level4: Dp = 8.dp
    
    /** 12dp - Very high elevation (modals, tooltips) */
    val level5: Dp = 12.dp
}

/**
 * Icon Sizes
 */
object IconSize {
    /** 12dp - Extra small icons (inline with text) */
    val extraSmall: Dp = 12.dp
    
    /** 16dp - Small icons (buttons, chips) */
    val small: Dp = 16.dp
    
    /** 24dp - Standard icons (most UI elements) */
    val medium: Dp = 24.dp
    
    /** 32dp - Large icons (prominent buttons) */
    val large: Dp = 32.dp
    
    /** 48dp - Extra large icons (feature highlights) */
    val extraLarge: Dp = 48.dp
    
    /** 64dp - Huge icons (splash screens, empty states) */
    val huge: Dp = 64.dp
}

/**
 * Component-Specific Sizes
 */
object ComponentSize {
    /** Button minimum width */
    val buttonMinWidth: Dp = 88.dp
    
    /** Button minimum height */
    val buttonHeight: Dp = 40.dp
    
    /** Large button height */
    val buttonHeightLarge: Dp = 56.dp
    
    /** Chip height */
    val chipHeight: Dp = 32.dp
    
    /** Dialog minimum width */
    val dialogMinWidth: Dp = 280.dp
    
    /** Dialog maximum width */
    val dialogMaxWidth: Dp = 560.dp
    
    /** Card minimum height */
    val cardMinHeight: Dp = 120.dp
    
    /** Avatar size (small) */
    val avatarSmall: Dp = 40.dp
    
    /** Avatar size (medium) */
    val avatarMedium: Dp = 56.dp
    
    /** Avatar size (large) */
    val avatarLarge: Dp = 72.dp
    
    /** Progress bar height */
    val progressBarHeight: Dp = 8.dp
    
    /** Progress bar height (thick) */
    val progressBarHeightThick: Dp = 12.dp
    
    /** Divider thickness */
    val dividerThickness: Dp = 1.dp
    
    /** Bottom navigation height */
    val bottomNavHeight: Dp = 56.dp
    
    /** Top app bar height */
    val topAppBarHeight: Dp = 64.dp
}

/**
 * Grid & Layout Sizes
 */
object GridSize {
    /** Item grid columns (portrait) */
    const val inventoryColumnsPortrait = 4
    
    /** Item grid columns (landscape) */
    const val inventoryColumnsLandscape = 6
    
    /** Nest cosmetic grid width */
    const val nestGridWidth = 8
    
    /** Nest cosmetic grid height */
    const val nestGridHeight = 6
    
    /** Item card width */
    val itemCardWidth: Dp = 80.dp
    
    /** Item card height */
    val itemCardHeight: Dp = 96.dp
    
    /** Cosmetic card width */
    val cosmeticCardWidth: Dp = 64.dp
    
    /** Cosmetic card height */
    val cosmeticCardHeight: Dp = 64.dp
}

/**
 * Animation Durations (milliseconds)
 */
object AnimationDuration {
    /** Instant (no animation) */
    const val instant = 0
    
    /** Very fast (micro-interactions) */
    const val veryFast = 100
    
    /** Fast (button press, ripple) */
    const val fast = 200
    
    /** Normal (default transitions) */
    const val normal = 300
    
    /** Slow (page transitions, modals) */
    const val slow = 500
    
    /** Very slow (splash screens, special effects) */
    const val verySlow = 800
}
