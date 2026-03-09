plugins {
    kotlin("jvm")  // Yeh sirf Kotlin JVM module banata hai – no Android
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // Domain layer mein sirf pure Kotlin libraries daal (no Android deps)
    // Examples:
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")  // If using coroutines for use cases
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")  // For testing

    // Agar tera domain mein koi common models ya utils hain jo app-wide use hote hain
    // implementation(project(":core"))  // Agar core module hai to

    // Test deps (optional)
    testImplementation("org.jetbrains.kotlin:kotlin-test:2.0.20")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
}
