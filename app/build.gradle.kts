// app/build.gradle.kts

plugins {
    id("com.android.application")
    kotlin("android")
    
    // Hilt Plugin (Required for dependency injection)
    id("com.google.dagger.hilt.android") 
    kotlin("kapt") // For Hilt and Dagger annotation processing
}

android {
    namespace = "com.melodym3.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.melodym3.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    // --- 1. Project Module Dependencies ---
    implementation(project(":ui"))
    implementation(project(":domain"))
    
    // --- 2. Core Android & Compose Foundation ---
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    
    // --- 3. Compose (Material 3 & UI) ---
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.compose.material:material-icons-extended")

    // --- 4. Hilt (Dependency Injection) ---
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // --- 5. Image Loading (Coil) ---
    implementation("io.coil-kt:coil-compose:2.5.0")
    
    // --- 6. Media Playback (Media3) ---
    implementation("androidx.media3:media3-exoplayer:1.2.1")
    implementation("androidx.media3:media3-session:1.2.1")
    
    // --- 7. Firebase (Persistence & Auth) ---
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-firestore-ktx") // Firestore database
    implementation("com.google.firebase:firebase-auth-ktx")     // Authentication
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3") // Coroutines support for Firebase tasks
    
    // Testing Dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
