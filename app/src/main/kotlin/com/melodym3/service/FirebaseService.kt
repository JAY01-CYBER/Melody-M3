package com.melodym3.service

import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseService @Inject constructor(
    @ApplicationContext private val context: android.content.Context
) {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var appId: String
    
    // Publicly exposed User ID
    var currentUserId: String? = null
        private set

    init {
        initializeFirebase()
    }

    private fun initializeFirebase() {
        Log.d("FirebaseService", "Attempting Firebase initialization...")

        // Retrieve global variables provided by the Canvas environment
        val providedAppId = System.getProperty("__app_id")

        if (providedAppId.isNullOrEmpty()) {
            Log.e("FirebaseService", "Missing App ID. Falling back to default.")
            appId = "default-app-id"
            // Initialize with default instance if global config is missing
            FirebaseApp.initializeApp(context)
        } else {
            // In Canvas environment, Firebase is often initialized implicitly or requires minimal setup
            FirebaseApp.initializeApp(context)
            appId = providedAppId
            Log.d("FirebaseService", "Firebase initialized successfully. App ID: $appId")
        }

        auth = Firebase.auth
        db = Firebase.firestore
    }

    suspend fun authenticateUser() {
        val authToken = System.getProperty("__initial_auth_token")

        // 1. Authenticate using the provided custom token (Mandatory Canvas flow)
        if (!authToken.isNullOrEmpty()) {
            try {
                auth.signInWithCustomToken(authToken).await()
                currentUserId = auth.currentUser?.uid
                Log.i("FirebaseService", "Signed in with custom token. User ID: $currentUserId")
            } catch (e: Exception) {
                Log.e("FirebaseService", "Custom token sign-in failed: ${e.message}. Falling back to anonymous.")
                signInAnonymously()
            }
        } else {
            // 2. Fallback to Anonymous Sign-In if token is missing
            signInAnonymously()
        }
    }
    
    private suspend fun signInAnonymously() {
         try {
            auth.signInAnonymously().await()
            // Ensure ID is generated if anonymous sign-in somehow fails to provide UID
            currentUserId = auth.currentUser?.uid ?: "anonymous-${java.util.UUID.randomUUID()}"
            Log.w("FirebaseService", "Signed in anonymously. User ID: $currentUserId")
        } catch (e: Exception) {
            Log.e("FirebaseService", "Anonymous sign-in failed: ${e.message}")
            currentUserId = "failed-auth-user"
        }
    }

    /**
     * Provides the current Firestore instance.
     */
    fun getFirestore(): FirebaseFirestore {
        return db
    }

    /**
     * Constructs the base path for user-specific collections (Private Data).
     * Path: /artifacts/{appId}/users/{userId}/[collectionName]
     */
    fun getPrivateUserCollectionPath(collectionName: String): String? {
        val userId = currentUserId ?: return null
        return "artifacts/$appId/users/$userId/$collectionName"
    }
}
