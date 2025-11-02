# JalmarQuest 🐦

A comprehensive indie RPG built with Kotlin Multiplatform, featuring 40+ interconnected systems and commercial-grade architecture.

## Project Status

**Current Milestone:** Milestone 1 - Core Architecture & Foundation (~70% complete)  
**Phase:** 1.2 - State Management System ✅ (Completed)  
**Next:** Phase 2.1 - Location System & World Map

## Recent Completions

### ✅ Phase 1.2 - State Management System (NEW)
- [x] TimeManager with seasons and day/night cycles
- [x] AutosaveManager with configurable intervals
- [x] WorldUpdateCoordinator (game loop)
- [x] 25+ time system tests
- [x] UI integration with time display

### ✅ Phase 1.1 - Project Setup & Architecture
- [x] Kotlin Multiplatform project structure
- [x] State management with StateFlow
- [x] Save/Load system
- [x] Platform-specific file I/O
- [x] Comprehensive test suite

## Architecture

### Technology Stack
- **Kotlin Multiplatform** - Cross-platform shared logic
- **Jetpack Compose Multiplatform** - Modern UI framework
- **Koin** - Dependency injection
- **kotlinx.serialization** - Data serialization
- **kotlinx.coroutines** - Async programming
- **StateFlow** - Reactive state management

### Module Structure
```
JalmarQuest/
├── shared/                      # Shared KMP module
│   ├── commonMain/
│   │   ├── model/              # Data models (@Serializable)
│   │   ├── state/              # State management
│   │   ├── persistence/        # Save/Load system
│   │   └── di/                 # Dependency injection
│   ├── androidMain/            # Android-specific
│   ├── iosMain/                # iOS-specific
│   └── desktopMain/            # Desktop-specific
└── composeApp/                 # UI application
    ├── commonMain/             # Shared UI
    ├── androidMain/            # Android app
    └── desktopMain/            # Desktop app
```

## Features Implemented

### ✅ Phase 1.2 Complete - State Management System
- [x] Time system with seasons and day/night cycles
- [x] 60 ticks = 1 minute progression
- [x] 4 seasons (Spring, Summer, Autumn, Winter)
- [x] Day/night detection and time of day tracking
- [x] Pause/resume and speed controls
- [x] Autosave manager (configurable intervals)
- [x] World update coordinator (game loop at 20 TPS)
- [x] Real-time diagnostics and monitoring
- [x] Reactive time display in UI
- [x] 25+ comprehensive tests

### ✅ Phase 1.1 Complete - Project Setup
- [x] Kotlin Multiplatform project structure
- [x] Android, iOS, Desktop target configuration
- [x] Koin dependency injection framework
- [x] Core data models with kotlinx.serialization
- [x] Thread-safe state management with Mutex + StateFlow
- [x] Save/Load system with versioning
- [x] Platform-specific file I/O (expect/actual)
- [x] Base player model with stats
- [x] Currency system (Seeds & Glimmer Shards)
- [x] Comprehensive test suite
- [x] Working Android and Desktop apps

### Core Systems
- **TimeManager**: Season cycles, day/night, pause/speed controls
- **WorldUpdateCoordinator**: Central game loop managing all timed systems
- **AutosaveManager**: Configurable automatic saving every 5 minutes
- **GameStateManager**: Centralized, thread-safe state management
- **SaveManager**: Robust save/load with multiple slots and autosave
- **Player System**: Stats, levels, experience, currency

### 🎮 Features

### Core Systems (Implemented)
- **TimeManager**: Season cycles, day/night, pause/speed controls
- **WorldUpdateCoordinator**: Central game loop managing all timed systems
- **AutosaveManager**: Configurable automatic saving every 5 minutes
- **GameStateManager**: Centralized, thread-safe state management
- **SaveManager**: Robust save/load with multiple slots and autosave
- **Player System**: Stats, levels, experience, currency

- **✨ NEW: Tile-Based Navigation System**
  - 2D grid-based world with visual rendering
  - Smart A* pathfinding around obstacles
  - Points of Interest (POIs) for quests, resources, and encounters
  - Fog of war with progressive discovery
  - Click-to-move and directional controls
  - Terrain types affecting movement costs
  - Full Butterfly Effect integration

### Visual Features (NEW!)
- **Tile Map Renderer**: Real-time world visualization
- **Mini-Map**: Overview with discovered areas
- **Smooth Animations**: 300ms movement transitions
- **POI Effects**: Pulsing markers for interactions
- **Authentic Quail Avatar**: Directional sprite with animations

## Building & Running

### Prerequisites
- JDK 17 or higher
- Android Studio (for Android builds)
- Xcode (for iOS builds, macOS only)

### Build Commands

#### Desktop
```bash
./gradlew :composeApp:run
```

#### Android
```bash
./gradlew :composeApp:installDebug
```

#### Run Tests
```bash
./gradlew :shared:allTests
```

## Project Roadmap

Following a 13-milestone development plan spanning 70-85 weeks:

1. **Milestone 1** - Core Architecture & Foundation (Current)
2. **Milestone 2** - World & Exploration Systems
3. **Milestone 3** - Inventory & Economy
4. **Milestone 4** - Combat & Progression
5. **Milestone 5** - Quests & Narrative
6. **Milestone 6** - Nest & Home Systems
7. **Milestone 7** - AI Director & Dynamic Systems
8. **Milestone 8** - Social & Multiplayer Features
9. **Milestone 9** - Monetization & IAP
10. **Milestone 10** - Polish & Optimization
11. **Milestone 11** - Testing & QA
12. **Milestone 12** - Launch Preparation
13. **Milestone 13** - Post-Launch & Live Ops

See `JalmarQuest_Roadmap.md` for detailed breakdown.

## 🗺️ Navigation System

The game features a revolutionary tile-based navigation system where Jalmar explores a grid-based world:

- **Terrain Variety**: Grass, mud, water, stone - each affecting movement
- **Smart Pathfinding**: Automatically finds best route around obstacles
- **Discovery System**: Fog of war reveals the world as you explore
- **Interactive POIs**: Quest givers, resources, encounters, and more
- **Stamina Management**: Movement costs stamina based on terrain

### Controls
- **Click-to-Move**: Click any visible tile to pathfind there
- **Arrow Keys**: Move one tile at a time
- **Interact Key**: Activate POIs at current location

## Testing

Current test coverage focuses on:
- **Time System**: 25+ tests covering tick progression, seasons, day/night, pause/resume
- **State Management**: Thread-safety, mutations, concurrent operations
- **Player Model**: Validation, leveling, stat calculations
- **Currency**: Transaction integrity, overflow protection
- **Quest System**: Tracking, completion, flags
- **Pathfinding**: Navigation intelligence and obstacle avoidance

**Total Tests**: 108+ comprehensive test cases

Run tests with: `./gradlew :shared:allTests`

## 🧪 Testing

The project includes comprehensive test coverage:
- **108+ Unit Tests** covering all core systems
- **Thread-Safety Tests** for concurrent operations
- **Pathfinding Tests** for navigation intelligence
- **"Quail Level Stupid" Tests** for edge cases

Run tests with: `./gradlew :shared:allTests`

## Development Philosophy

- **Quality First**: Every system is robust and fully tested
- **Integration-Centric**: All systems interconnect seamlessly
- **No Shortcuts**: 100% feature completeness is mandatory
- **Progressive Complexity**: Build foundations before advanced features

## 📈 Development Progress

**Current Status**: Milestone 1 - 70% Complete

### Recent Achievements
- ✅ Complete tile-based navigation overhaul
- ✅ 30+ new tests for tile system
- ✅ Full UI rendering components
- ✅ Butterfly Effect event tracking
- ✅ POI trigger system for future features

## Next Steps

**Phase 2.1**: Location System & World Map
- Location data model and catalog (42+ locations)
- Connection graph (exits/entrances)
- Biome system (8 biome types)
- Discovery tracking and fog of war
- Integration with player movement

## License

Copyright © 2025 JalmarQuest Development Team

---

**Version:** 1.0.0  
**Build Date:** 2025-10-31
