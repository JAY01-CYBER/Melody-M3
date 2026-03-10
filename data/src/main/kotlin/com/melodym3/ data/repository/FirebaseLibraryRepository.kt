package com.melodym3.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.repository.LibraryRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseLibraryRepository @Inject constructor() : LibraryRepository {
    
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    
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
                    duration = document.getLong("duration") ?: 0L,
                    url = document.getString("url") ?: ""
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
            "duration" to item.duration,
            "url" to item.url
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
}
