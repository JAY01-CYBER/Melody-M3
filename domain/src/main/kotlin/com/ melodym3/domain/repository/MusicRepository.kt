package com.melodym3.domain.repository

import com.melodym3.domain.model.MusicItem

/**
 * Defines the contract for fetching music data.
 * All data modules must implement this interface.
 */
interface MusicRepository {
    
    /**
     * Fetches personalized music recommendations for the home screen.
     */
    suspend fun getHomeRecommendations(): List<MusicItem>

    // suspend fun searchMusic(query: String): List<MusicItem>
    // suspend fun getAlbumDetails(id: String): AlbumDetails
}
