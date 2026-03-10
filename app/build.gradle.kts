plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.melodym3"   // IMPORTANT: Change this to match your actual package name
                                 // Open app/src/main/AndroidManifest.xml and look for the "package" attribute
                                 // Example: if package="com.melodym3.app" then use "com.melodym3.app"

    compileSdk = 34

    defaultConfig {
        applicationId = "com.melodym3"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Your project modules
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":ui"))

    // Core Android dependencies
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    // Optional: Add more dependencies based on your app needs
    // implementation("androidx.constraintlayout:constraintlayout:2.1.4")  // If using XML layouts
    // implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    // implementation("androidx.navigation:navigation-fragment-ktx:2.8.3")
    // implementation("androidx.room:room-runtime:2.6.1")
    // kapt("androidx.room:room-compiler:2.6.1")   // If using Room and KAPT
}
