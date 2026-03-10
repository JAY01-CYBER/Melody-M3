package com.melodym3.data.repository

import com.melodym3.data.remote.YTMService
import com.melodym3.data.remote.model.MusicItemDto
import javax.inject.Inject

class RealMusicRepositoryImpl @Inject constructor(
    private val api: YTMService
) {
    suspend fun fetchSongs(query: String): List<MusicItemDto> {
        return try {
            api.searchSongs(query)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
