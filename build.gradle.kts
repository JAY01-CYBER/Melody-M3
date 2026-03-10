// Path: build.gradle.kts (Project Root Level)
// Is file mein hum saare plugins ke versions define karte hain.

plugins {
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    
    // Kotlin 2.0.0+ ke liye Compose compiler plugin declaration
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
    
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}
