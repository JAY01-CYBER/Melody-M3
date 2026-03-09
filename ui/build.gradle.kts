plugins {
    id("com.android.library")           // Use "com.android.library" for modules like :ui, :data
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.melodym3.ui"       // IMPORTANT: Change this to match your actual package
                                        // Check app/src/main/AndroidManifest.xml or Kotlin files
                                        // Examples:
                                        // - For :ui module: "com.melodym3.ui"
                                        // - For :data: "com.melodym3.data"
                                        // - For :domain: "com.melodym3.domain" (if it's Android lib)

    compileSdk = 34

    defaultConfig {
        minSdk = 24
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
    // Add your dependencies here, e.g.:
    // implementation("androidx.core:core-ktx:1.13.1")
    // implementation("androidx.appcompat:appcompat:1.7.0")
    // If using Compose:
    // implementation(platform("androidx.compose:compose-bom:2024.09.03"))
    // implementation("androidx.compose.ui:ui")
}
