# JalmarQuest - Quick Start Guide

## 🚀 Getting Started

### First Time Setup
1. Ensure JDK 17+ is installed
2. Clone/open the project
3. Wait for Gradle sync to complete

### Running the Project

#### Desktop (Easiest)
```bash
./gradlew :composeApp:run
```

#### Android
1. Open in Android Studio
2. Select Android device/emulator
3. Click Run (or use `./gradlew :composeApp:installDebug`)

#### Run Tests
```bash
./gradlew :shared:allTests
```

---

## 📁 Project Structure

```
shared/              → Core game logic (KMP)
  ├── model/         → Data classes (@Serializable)
  ├── state/         → State management (StateFlow)
  ├── persistence/   → Save/Load system
  └── di/            → Dependency injection

composeApp/          → UI application
  ├── commonMain/    → Shared UI code
  ├── androidMain/   → Android app
  └── desktopMain/   → Desktop app
```

---

## 🎮 Core Systems

### GameStateManager
Centralized state management with thread-safety.

```kotlin
val gameStateManager: GameStateManager = get()

// Create new game
gameStateManager.createNewGame("PlayerName")

// Update state
gameStateManager.addExperience(100)
gameStateManager.addSeeds(50)
gameStateManager.updatePlayerPosition(Position(10, 20, "forest"))

// Observe state
gameStateManager.gameState.collectAsState()
```

### SaveManager
Multi-slot save system with autosave.

```kotlin
val saveManager: SaveManager = get()

// Save game
saveManager.saveGame(gameState, "slot1")

// Load game
val result = saveManager.loadGame("slot1")
result.onSuccess { state -> /* ... */ }

// Autosave
saveManager.autoSave(gameState)

// List saves
val saves = saveManager.listSaves()
```

---

## 🔧 Development Commands

### Build
```bash
./gradlew build
```

### Clean Build
```bash
./gradlew clean build
```

### Run Tests
```bash
./gradlew :shared:allTests
```

### Desktop Run
```bash
./gradlew :composeApp:run
```

### Android Install
```bash
./gradlew :composeApp:installDebug
```

---

## 📊 Current Status

**Milestone 1, Phase 1.1:** ✅ COMPLETE
- KMP architecture ✅
- State management ✅
- Save/Load system ✅
- Platform builds ✅
- Test suite ✅

**Next:** Phase 1.2 - Time System

---

## 🧪 Testing

All tests are in `shared/src/commonTest/`

**Current Coverage:**
- GameStateManager (12+ tests)
- Player models (8+ tests)
- Thread-safety tests
- Validation tests

---

## 🏗️ Adding New Features

### 1. Add Data Model
```kotlin
// shared/src/commonMain/kotlin/.../model/
@Serializable
data class NewFeature(val id: String, val data: Int)
```

### 2. Add to GameState
```kotlin
data class GameState(
    // ...
    val newFeatures: List<NewFeature> = emptyList()
)
```

### 3. Add Manager Functions
```kotlin
// In GameStateManager
suspend fun updateNewFeature(feature: NewFeature) {
    updateState { state ->
        state.copy(newFeatures = state.newFeatures + feature)
    }
}
```

### 4. Write Tests
```kotlin
@Test
fun `test new feature`() = runTest {
    // Test implementation
}
```

---

## 🐛 Troubleshooting

### Gradle Sync Issues
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

### Android Build Issues
- Check Android SDK is installed
- Verify ANDROID_HOME is set
- Update Android Studio

### Desktop Issues
- Verify JDK 17+ is installed
- Check JAVA_HOME points to correct JDK

---

## 📖 Key Files

| File | Purpose |
|------|---------|
| `build.gradle.kts` | Project dependencies |
| `settings.gradle.kts` | Module configuration |
| `shared/build.gradle.kts` | Shared module config |
| `composeApp/build.gradle.kts` | UI app config |

---

## 🔗 Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| Kotlin | 1.9.21 | Language |
| Compose | 1.5.11 | UI Framework |
| Koin | 3.5.3 | DI |
| Serialization | 1.6.2 | JSON |
| Coroutines | 1.7.3 | Async |

---

## 💡 Tips

1. **Use StateFlow**: Always observe `gameState` for reactive UI
2. **Thread-Safe**: Use `GameStateManager` for all mutations
3. **Test Early**: Write tests as you add features
4. **Platform-Specific**: Use expect/actual for platform code
5. **Serializable**: Always annotate data models with `@Serializable`

---

## 📞 Support

- Check README.md for detailed info
- Review PHASE_1.1_COMPLETE.md for architecture
- See JalmarQuest_Roadmap.md for full plan

---

**Last Updated:** October 31, 2025  
**Version:** 1.0.0
