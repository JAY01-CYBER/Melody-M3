// Path: build.gradle.kts (Project Root Level)
// This file defines all the plugin versions for the entire project.

plugins {
    // Standard Android and Kotlin plugins
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    
    // Mandatory Compose compiler plugin for Kotlin 2.0.0+
    // Ensure the version matches your Kotlin version (2.0.0)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
    
    // Dependency Injection (Hilt) and Google Services
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}
