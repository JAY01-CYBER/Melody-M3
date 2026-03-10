plugins {
    // Android Gradle Plugin versions
    id("com.android.application") version "8.3.2" apply false
    id("com.android.library") version "8.3.2" apply false
    
    // Kotlin and Compose Compiler versions
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
    
    // Dependency Injection (Hilt) and Annotation Processing (KAPT)
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("org.jetbrains.kotlin.kapt") version "2.0.0" apply false
    
    // Google Services for Firebase integration
    id("com.google.gms.google-services") version "4.4.1" apply false
}
