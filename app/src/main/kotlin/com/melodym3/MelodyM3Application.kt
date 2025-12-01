package com.melodym3

import android.app.Application
import com.melodym3.service.FirebaseService
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The base Application class for Melody M3.
 * @HiltAndroidApp triggers Hilt's code generation, making the entire app Hilt-aware.
 */
@HiltAndroidApp
class MelodyM3Application : Application() {
    
    // Inject the FirebaseService instance
    @Inject 
    lateinit var firebaseService: FirebaseService

    override fun onCreate() {
        super.onCreate()
        
        // IMPORTANT: Trigger authentication immediately when the application starts.
        // This ensures the currentUserId is set up before any Repository attempts 
        // to access Firestore collections based on the user ID.
        CoroutineScope(Dispatchers.IO).launch {
            firebaseService.authenticateUser()
        }
    }
}
