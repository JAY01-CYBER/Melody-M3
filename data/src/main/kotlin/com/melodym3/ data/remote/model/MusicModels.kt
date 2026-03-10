package com.melodym3.data.remote.model

import com.google.gson.annotations.SerializedName

data class MusicItemDto(
    @SerializedName("videoId") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("artist") val artist: String?,
    @SerializedName("artistName") val artistName: String?,
    @SerializedName("duration") val duration: String?,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("subtitle") val subtitle: String? = null
)

data class StreamUrlDto(
    @SerializedName("url") val url: String?
)
