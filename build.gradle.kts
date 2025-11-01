plugins {
    // Kotlin Multiplatform
    kotlin("multiplatform") version "2.1.0" apply false
    kotlin("plugin.serialization") version "2.1.0" apply false
    
    // Android
    id("com.android.application") version "8.1.4" apply false
    id("com.android.library") version "8.1.4" apply false
    
    // Compose Multiplatform
    id("org.jetbrains.compose") version "1.7.1" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply false
}

allprojects {
    group = "com.jalmarquest"
    version = "1.0.0"
}
