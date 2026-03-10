plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.melodym3"  // Confirm this matches AndroidManifest.xml package
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
    // Your modules
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":ui"))

    // Core deps
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    // Force modern replacements for legacy transitive deps
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("androidx.transition:transition:1.5.0")
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")

    // Exclude old legacy support to prevent conflict
    configurations.all {
        resolutionStrategy {
            exclude group: "androidx.legacy"
            exclude group: "com.android.support"
        }
    }
}
