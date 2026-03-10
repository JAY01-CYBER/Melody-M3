plugins {
    kotlin("jvm")
    id("com.google.dagger.hilt.android")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // Project dependencies
    implementation(project(":data"))
    
    // Hilt Core
    implementation("com.google.dagger:hilt-core:2.52")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    
    // Testing
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}
