plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.kapt")
    // Kotlin 2.0+ ke liye ye plugin mandatory hai Compose chalane ke liye
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.melodym3.ui"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }

    // Note: Kotlin 2.0+ mein 'composeOptions' block ki zarurat nahi padti
    // kyunki Compose Compiler ab Kotlin ka hissa hai.

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Apne doosre modules ko connect karna
    implementation(project(":domain"))
    implementation(project(":data"))

    // Hilt Dependency Injection (UI module ke liye zaruri)
    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-compiler:2.52")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Jetpack Compose Libraries
    val composeBom = platform("androidx.compose:compose-bom:2024.02.01")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Coil (Images load karne ke liye)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Android Core & AppCompat
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
}
