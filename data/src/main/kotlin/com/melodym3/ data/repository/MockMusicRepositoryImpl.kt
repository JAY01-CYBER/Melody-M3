package com.melodym3.data.repository

import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.model.ItemType
import com.melodym3.domain.repository.MusicRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockMusicRepositoryImpl @Inject constructor() : MusicRepository {
    
    private val mockSongs = listOf(
        MusicItem("1", "Song 1", "Artist 1", 180000L, "https://example.com/1", "", "", ItemType.SONG),
        MusicItem("2", "Song 2", "Artist 2", 200000L, "https://example.com/2", "", "", ItemType.SONG),
        MusicItem("3", "Song 3", "Artist 3", 210000L, "https://example.com/3", "", "", ItemType.SONG),
        MusicItem("4", "Song 4", "Artist 4", 190000L, "https://example.com/4", "", "", ItemType.SONG),
        MusicItem("5", "Song 5", "Artist 5", 220000L, "https://example.com/5", "", "", ItemType.SONG)
    )
    
    override suspend fun getHomeRecommendations(): List<MusicItem> {
        return mockSongs
    }
    
    override suspend fun searchMusic(query: String): List<MusicItem> {
        return mockSongs.filter { 
            it.title.contains(query, ignoreCase = true) || 
            it.artist.contains(query, ignoreCase = true)
        }
    }
    
    override suspend fun getStreamUrl(videoId: String): String {
        return "https://example.com/stream/$videoId"
    }
}
