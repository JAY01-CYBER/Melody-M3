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

    /**
     * Searches for music items based on query string.
     * @param query Search term
     * @return List of matching MusicItem
     */
    suspend fun searchMusic(query: String): List<MusicItem>

    /**
     * Gets the streaming URL for a specific music item.
     * @param musicId Unique identifier for the music
     * @return Streaming URL as String
     */
    suspend fun getStreamUrl(musicId: String): String

    // suspend fun getAlbumDetails(id: String): AlbumDetails
}
