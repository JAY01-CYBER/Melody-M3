package com.melodym3.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing the response from the backend 
 * when requesting the actual audio streaming URL for a music item.
 */
data class StreamUrlDto(
    @SerializedName("url")
    val streamUrl: String // The actual link to the audio stream (e.g., .m4a or .mp4)
)
