package com.melodym3.data.repository

import com.melodym3.domain.model.MusicItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseLibraryRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    /**
     * This function fetches items from Firebase and maps them to the MusicItem model.
     * Fixed: Added .toString() for duration and included the missing imageUrl parameter.
     */
    suspend fun getLibraryItems(): List<MusicItem> {
        return try {
            val snapshot = firestore.collection("library").get().await()
            snapshot.documents.map { doc ->
                MusicItem(
                    id = doc.id,
                    title = doc.getString("title") ?: "Unknown Title",
                    artist = doc.getString("artist") ?: doc.getString("subtitle") ?: "Unknown Artist",
                    // Adding the missing imageUrl parameter
                    imageUrl = doc.getString("imageUrl") ?: doc.getString("imageurl") ?: "",
                    url = doc.getString("url") ?: doc.getString("streamUrl") ?: "",
                    // Fixed: Converting Long (from Firebase) to String (for the Model)
                    duration = doc.get("duration")?.toString() ?: "0:00"
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
