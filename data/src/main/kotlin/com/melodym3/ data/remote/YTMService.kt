package com.melodym3.data.remote

import com.melodym3.data.remote.model.MusicItemDto
import com.melodym3.data.remote.model.StreamUrlDto
import retrofit2.http.GET
import retrofit2.http.Path

interface YTMService {
    
    @GET("home")
    suspend fun getHomeRecommendations(): List<MusicItemDto>
    
    @GET("stream/{videoId}")
    suspend fun getStreamUrl(@Path("videoId") videoId: String): StreamUrlDto
}
