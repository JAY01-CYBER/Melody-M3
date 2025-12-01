package com.melodym3.data.remote

import com.melodym3.data.remote.model.MusicItemDto
import com.melodym3.data.remote.model.StreamUrlDto // <-- New Import
import retrofit2.http.GET
import retrofit2.http.Query

// अनऑफिशियल YTM API Backend के लिए काल्पनिक सेवा इंटरफ़ेस
interface YTMService {
    
    @GET("recommendations/home")
    suspend fun getHomeRecommendations(
        @Query("session_id") sessionId: String 
    ): List<MusicItemDto>
    
    // NEW: Function to fetch the actual stream URL for a video ID
    @GET("stream/url")
    suspend fun getStreamUrl(
        @Query("video_id") videoId: String
    ): StreamUrlDto // Expects a DTO containing the stream link
}
