package com.melodym3.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.melodym3.domain.model.LikedTrack
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.model.ItemType
import com.melodym3.domain.repository.LibraryRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseLibraryRepository @Inject constructor() : LibraryRepository {
    
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    
    // Library functions
    override suspend fun getLibraryItems(): List<MusicItem> {
        val userId = auth.currentUser?.uid ?: return emptyList()
        
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("library")
                .get()
                .await()
            
            snapshot.documents.mapNotNull { document ->
                MusicItem(
                    id = document.id,
                    title = document.getString("title") ?: "",
                    artist = document.getString("artist") ?: "",
                    duration = document.getLong("duration") ?: 0L,  // ✅ Long type
                    url = document.getString("url") ?: "",
                    imageUrl = document.getString("imageUrl") ?: "",
                    subtitle = document.getString("subtitle") ?: "",
                    type = ItemType.SONG  // Default value
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    override suspend fun addToLibrary(item: MusicItem): Boolean {
        val userId = auth.currentUser?.uid ?: return false
        
        val data = hashMapOf(
            "title" to item.title,
            "artist" to item.artist,
            "duration" to item.duration,  // ✅ Long type
            "url" to item.url,
            "imageUrl" to item.imageUrl,
            "subtitle" to item.subtitle
        )
        
        return try {
            firestore.collection("users")
                .document(userId)
                .collection("library")
                .document(item.id)
                .set(data)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun removeFromLibrary(itemId: String): Boolean {
        val userId = auth.currentUser?.uid ?: return false
        
        return try {
            firestore.collection("users")
                .document(userId)
                .collection("library")
                .document(itemId)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    // Liked tracks functions
    override suspend fun getLikedTracks(): List<LikedTrack> {
        val userId = auth.currentUser?.uid ?: return emptyList()
        
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("liked")
                .get()
                .await()
            
            snapshot.documents.mapNotNull { document ->
                LikedTrack(
                    id = document.id,
                    title = document.getString("title") ?: "",
                    artist = document.getString("artist") ?: "",
                    duration = document.getLong("duration") ?: 0L,  // ✅ Long type
                    url = document.getString("url") ?: "",
                    imageUrl = document.getString("imageUrl") ?: "",
                    subtitle = document.getString("subtitle") ?: "",
                    likedAt = document.getLong("likedAt") ?: System.currentTimeMillis()
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    override suspend fun addToLiked(track: MusicItem): Boolean {
        val userId = auth.currentUser?.uid ?: return false
        
        val data = hashMapOf(
            "title" to track.title,
            "artist" to track.artist,
            "duration" to track.duration,  // ✅ Long type
            "url" to track.url,
            "imageUrl" to track.imageUrl,
            "subtitle" to track.subtitle,
            "likedAt" to System.currentTimeMillis()
        )
        
        return try {
            firestore.collection("users")
                .document(userId)
                .collection("liked")
                .document(track.id)
                .set(data)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun removeFromLiked(trackId: String): Boolean {
        val userId = auth.currentUser?.uid ?: return false
        
        return try {
            firestore.collection("users")
                .document(userId)
                .collection("liked")
                .document(trackId)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun isTrackLiked(trackId: String): Boolean {
        val userId = auth.currentUser?.uid ?: return false
        
        return try {
            val doc = firestore.collection("users")
                .document(userId)
                .collection("liked")
                .document(trackId)
                .get()
                .await()
            doc.exists()
        } catch (e: Exception) {
            false
        }
    }
}
