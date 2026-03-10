package com.melodym3.data.repository

import com.melodym3.data.remote.YTMService
import com.melodym3.data.remote.model.MusicItemDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YouTubeRepository @Inject constructor(
    private val api: YTMService
) {
    suspend fun searchSongs(query: String): kotlin.Result<List<MusicItemDto>> {
        return try {
            val response = api.searchSongs(query)
            kotlin.Result.success(response)
        } catch (e: Exception) {
            kotlin.Result.failure(e)
        }
    }

    suspend fun getHomeRecommendations(): kotlin.Result<List<MusicItemDto>> {
        return try {
            val response = api.getHomeRecommendations()
            kotlin.Result.success(response)
        } catch (e: Exception) {
            kotlin.Result.failure(e)
        }
    }

    suspend fun getStreamUrl(videoId: String): kotlin.Result<String> {
        return try {
            val response = api.getStreamUrl(videoId)
            kotlin.Result.success(response.url ?: "")
        } catch (e: Exception) {
            kotlin.Result.failure(e)
        }
    }
}
