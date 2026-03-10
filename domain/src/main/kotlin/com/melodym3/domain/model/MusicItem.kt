package com.melodym3.domain.model

data class MusicItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val streamUrl: String? = null,
    val duration: String? = null
)

enum class ItemType {
    SONG,
    ALBUM,
    ARTIST,
    PLAYLIST
}
