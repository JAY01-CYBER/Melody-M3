plugins {
    kotlin("jvm") version "2.0.20"  // Version root se match kar (ya root mein declare hai to remove version)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // Add only pure Kotlin deps
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")  // if coroutines used
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}
