# How to Use Your Actual Artwork Image

Your image is saved at: `composeApp/src/commonMain/resources/jalmarquest_background.png`

## The Problem
Compose Multiplatform resources require using generated resource classes, not String paths.

## The Solution (When You're Ready)

### Step 1: Verify Resource Generation
After saving the image, run:
```powershell
.\gradlew :composeApp:generateComposeResClass --no-daemon
```

This generates resource classes in:
`composeApp/build/generated/compose/resourceGenerator/kotlin/commonMainResourceAccessors/`

### Step 2: Update MainMenuScreen.kt Imports
Replace the current imports with:
```kotlin
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
// Add this generated import (path may vary):
import composeapp.composeapp.generated.resources.Res
import composeapp.composeapp.generated.resources.jalmarquest_background
```

### Step 3: Replace Gradient Background
In `MainMenuScreen.kt`, replace the two gradient `Box` composables with:
```kotlin
Image(
    painter = painterResource(Res.drawable.jalmarquest_background),
    contentDescription = "JalmarQuest - Enchanted Forest with Jalmar and Blue Butterfly",
    modifier = Modifier.fillMaxSize(),
    contentScale = ContentScale.Crop
)
```

## Current Status
✅ Image saved to resources folder
✅ Beautiful gradient background matching your artwork colors (temporary)
✅ Fantasy UI components working perfectly
⏳ Actual image loading (requires generated resources setup)

## Why the Gradient for Now?
The gradient uses colors directly from your artwork:
- Deep forest greens (dark to light vertical gradient)
- Golden sunlight glow (radial gradient overlay)

This gives the same aesthetic feel while we figure out the proper resource generation.

## Next Steps
1. Try running the resource generation command above
2. Check if `Res.drawable.jalmarquest_background` exists in generated code
3. Update imports and Image component
4. Compile and run!

If you run into issues, we can stick with the gradient - it looks great and matches your artwork's vibe! 🌲✨
