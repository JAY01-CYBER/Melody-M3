package com.melodym3.data.remote.model

import com.google.gson.annotations.SerializedName
import com.melodym3.domain.model.ItemType
import com.melodym3.domain.model.MusicItem

/**
 * Data Transfer Object representing a music item as received from the API backend.
 */
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
    val type: String // API returns String, needs mapping to domain Enum
)

/**
 * Extension function to map the DTO to the clean Domain Model.
 * This ensures the domain layer remains decoupled from network specifics.
 */
fun MusicItemDto.toDomainModel(): MusicItem {
    return MusicItem(
        id = this.id,
        title = this.title,
        subtitle = this.artistName,
        imageUrl = this.thumbnailUrl,
        type = try {
            // Attempt to convert the API string (e.g., "SONG") to the ItemType enum
            ItemType.valueOf(this.type.uppercase()) 
        } catch (e: IllegalArgumentException) {
            ItemType.SONG // Fallback to SONG if type is unknown
        }
    )
}
