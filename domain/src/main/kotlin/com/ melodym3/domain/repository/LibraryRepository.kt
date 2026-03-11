package com.melodym3.domain.repository

import com.melodym3.domain.model.LikedTrack
import com.melodym3.domain.model.MusicItem

interface LibraryRepository {
    // Library functions
    suspend fun getLibraryItems(): List<MusicItem>
    suspend fun addToLibrary(item: MusicItem): Boolean
    suspend fun removeFromLibrary(itemId: String): Boolean
    
    // Liked tracks functions
    suspend fun getLikedTracks(): List<LikedTrack>
    suspend fun addToLiked(track: MusicItem): Boolean
    suspend fun removeFromLiked(trackId: String): Boolean
    suspend fun isTrackLiked(trackId: String): Boolean
}
