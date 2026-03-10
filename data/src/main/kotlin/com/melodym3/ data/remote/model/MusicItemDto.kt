package com.melodym3.data.remote.model

import com.google.gson.annotations.SerializedName
import com.melodym3.domain.model.MusicItem

data class MusicItemDto(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("artist")
    val artist: String,
    
    @SerializedName("thumbnail")
    val thumbnail: String,
    
    @SerializedName("duration")
    val duration: Long = 0
) {
    fun toDomain(): MusicItem {
        return MusicItem(
            id = id,
            title = title,
            artist = artist,
            duration = duration,
            url = ""
        )
    }
}
