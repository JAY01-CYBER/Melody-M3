package com.melodym3.data.repository

import com.melodym3.domain.model.ItemType
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.repository.MusicRepository
import kotlinx.coroutines.delay

/**
 * A temporary implementation of MusicRepository using hardcoded dummy data.
 * This will be replaced by the network implementation later.
 */
class MockMusicRepositoryImpl : MusicRepository {

    private val mockRecommendations = listOf(
        MusicItem("101", "Coding Flow State", "Playlist · Focus Music", "url_1", ItemType.PLAYLIST),
        MusicItem("102", "Weekend Chill Mix", "Album · Various Artists", "url_2", ItemType.ALBUM),
        MusicItem("103", "Top 50 India", "Chart · YouTube Music", "url_3", ItemType.PLAYLIST),
        MusicItem("104", "A. R. Rahman", "Artist · Genius", "url_4", ItemType.ARTIST),
        MusicItem("105", "New Release: Vibe Stream", "Song · Melody M3 Studio", "url_5", ItemType.SONG),
    )

    override suspend fun getHomeRecommendations(): List<MusicItem> {
        // Simulate network delay for a realistic feel
        delay(1000L) 
        
        println("Debug: Providing mock data for Home Screen.")
        return mockRecommendations
    }
}
