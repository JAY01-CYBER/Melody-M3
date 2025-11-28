package com.melodym3.data.remote.model

import com.google.gson.annotations.SerializedName
import com.melodym3.domain.model.ItemType
import com.melodym3.domain.model.MusicItem

// यह API से आने वाले डेटा स्ट्रक्चर को दर्शाता है
data class MusicItemDto(
    @SerializedName("video_id") 
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("artist_name") 
    val artistName: String,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,
    @SerializedName("item_type")
    val type: String 
)

// DTO को Domain Model में मैप करने के लिए Extension function
fun MusicItemDto.toDomainModel(): MusicItem {
    return MusicItem(
        id = this.id,
        title = this.title,
        subtitle = this.artistName, // कलाकार का नाम सबटाइटल के रूप में
        imageUrl = this.thumbnailUrl,
        type = try {
            ItemType.valueOf(this.type.uppercase()) 
        } catch (e: IllegalArgumentException) {
            ItemType.SONG 
        }
    )
}
