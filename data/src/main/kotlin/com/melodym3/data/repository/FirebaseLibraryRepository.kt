package com.melodym3.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.melodym3.domain.model.LikedTrack
import com.melodym3.service.FirebaseService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseLibraryRepository @Inject constructor(
    private val firebaseService: FirebaseService
) {
    // Collection name for user's liked tracks
    private val COLLECTION_LIKED_TRACKS = "liked_tracks"

    private suspend fun getCollectionRef(db: FirebaseFirestore) = run {
        val path = firebaseService.getPrivateUserCollectionPath(COLLECTION_LIKED_TRACKS)
        if (path != null) db.collection(path) else null
    }

    /**
     * Saves a track to the user's personal library (or updates it if it exists).
     */
    suspend fun saveTrack(track: LikedTrack): Result<Unit> {
        val db = firebaseService.getFirestore()
        val collectionRef = getCollectionRef(db)
        
        if (collectionRef == null) return Result.failure(Exception("User not authenticated or App ID missing."))

        return try {
            // Use the track ID as the document ID for easy lookup and replacement
            collectionRef.document(track.id).set(track).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Gets a real-time stream of the user's entire liked track library.
     * NOTE: This is a simplified flow for initial data fetching.
     * In production, this should use a proper callbackFlow to handle real-time snapshots.
     */
    fun getLikedTracks(): Flow<List<LikedTrack>> = flow {
        // Ensure authentication is completed before fetching data
        firebaseService.authenticateUser()
        
        val db = firebaseService.getFirestore()
        val collectionRef = getCollectionRef(db)
        
        if (collectionRef == null) {
            emit(emptyList())
            return@flow
        }
        
        // Simple fetch mechanism (using await)
        try {
            val snapshot = collectionRef.get().await()
            val tracks = snapshot.documents.mapNotNull { 
                it.toObject(LikedTrack::class.java) 
            }
            emit(tracks)
        } catch (e: Exception) {
            Log.e("LibraryRepo", "Error fetching liked tracks: ${e.message}")
            // Emit empty list on failure
            emit(emptyList()) 
        }
    }
}
