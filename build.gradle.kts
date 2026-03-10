// Path: build.gradle.kts (Project Root)
// This file defines plugin versions for the entire project.

plugins {
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    
    // Define the Compose compiler plugin for Kotlin 2.0.0
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
    
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("org.jetbrains.kotlin.kapt") version "2.0.0" apply false
}
