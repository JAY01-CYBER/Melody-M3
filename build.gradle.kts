// Path: build.gradle.kts (Project Root)
// Updated versions to fix internal Gradle API errors

plugins {
    // Upgraded AGP to 8.3.2 for better compatibility
    id("com.android.application") version "8.3.2" apply false
    id("com.android.library") version "8.3.2" apply false
    
    // Kotlin 2.0.0 and Compose Plugin
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
    
    // Hilt and Google Services
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("org.jetbrains.kotlin.kapt") version "2.0.0" apply false
}
