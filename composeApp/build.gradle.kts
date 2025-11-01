plugins {
    kotlin("multiplatform")
    kotlin("plugin.compose")
    id("com.android.application")
    id("org.jetbrains.compose")
}

kotlin {
    jvmToolchain(17)
    
    androidTarget()
    
    jvm("desktop")
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                
                // Navigation
                implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")
                
                implementation("io.insert-koin:koin-core:3.5.3")
                implementation("io.insert-koin:koin-compose:1.1.2")
            }
        }
        
        val androidMain by getting {
            dependencies {
                implementation("androidx.activity:activity-compose:1.8.2")
                implementation("androidx.core:core-ktx:1.12.0")
                implementation("io.insert-koin:koin-android:3.5.3")
            }
        }
        
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

android {
    namespace = "com.jalmarquest"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.jalmarquest"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.jalmarquest.MainKt"
        
        nativeDistributions {
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
            )
            packageName = "JalmarQuest"
            packageVersion = "1.0.0"
            
            windows {
                iconFile.set(project.file("src/desktopMain/resources/icon.ico"))
            }
        }
    }
}

// Create a custom run task for desktop
tasks.register<JavaExec>("runDesktop") {
    group = "application"
    description = "Run the desktop application"
    mainClass.set("com.jalmarquest.MainKt")
    val compilation = kotlin.targets.getByName("desktop").compilations.getByName("main")
    classpath = compilation.output.classesDirs + compilation.runtimeDependencyFiles
}
