// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false   // For Android modules like :app, :ui
    id("org.jetbrains.kotlin.jvm") version "2.0.20" apply false       // IMPORTANT: Add this for pure Kotlin modules like :domain
    id("com.google.dagger.hilt.android") version "2.52" apply false
    // If using KSP (e.g. for Room): id("com.google.devtools.ksp") version "2.0.20-1.0.24" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
