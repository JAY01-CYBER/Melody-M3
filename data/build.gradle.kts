// data/build.gradle.kts

plugins {
    kotlin("jvm")
}

dependencies {
    // 1. Dependency on Domain module
    implementation(project(":domain"))

    // 2. Networking (We'll add Retrofit/Ktor later)
    // implementation("com.squareup.retrofit2:retrofit:2.9.0") 
    
    // Kotlin Extensions
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}
