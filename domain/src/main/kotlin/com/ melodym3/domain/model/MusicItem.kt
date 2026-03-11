package com.melodym3.domain.model

enum class ItemType {
    SONG, 
    ALBUM, 
    ARTIST, 
    PLAYLIST
}

data class MusicItem(
    val id: String,
    val title: String,
    val artist: String,
    val duration: Long,
    val url: String,
    val imageUrl: String = "",
    val subtitle: String = "",
    val type: ItemType = ItemType.SONG
) {
    val displaySubtitle: String get() = if (subtitle.isNotBlank()) subtitle else artist
}
