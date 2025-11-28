// data/build.gradle.kts

plugins {
    kotlin("jvm")
    kotlin("kapt") // Hilt and Retrofit compiler processing के लिए
}

dependencies {
    // Dependency on Domain module
    implementation(project(":domain"))

    // Networking - Retrofit and OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") 
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    
    // Hilt integration for data layer
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    
    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}
