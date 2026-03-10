package com.melodym3.data.remote

import com.melodym3.data.remote.model.MusicItemDto
import com.melodym3.data.remote.model.StreamUrlDto
import retrofit2.http.GET
import retrofit2.http.Query

interface YTMService {
    
    @GET("search")
    suspend fun searchSongs(
        @Query("query") query: String
    ): List<MusicItemDto>

    @GET("stream")
    suspend fun getStreamUrl(
        @Query("videoId") videoId: String
    ): StreamUrlDto

    @GET("recommendations")
    suspend fun getHomeRecommendations(): List<MusicItemDto>
}
