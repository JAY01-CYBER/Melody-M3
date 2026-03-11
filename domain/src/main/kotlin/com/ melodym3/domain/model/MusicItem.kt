package com.melodym3.domain.model

data class MusicItem(
    val id: String,
    val title: String,
    val artist: String,
    val imageUrl: String,
    val url: String,
    val duration: String
) {
    val subtitle: String get() = artist
}

enum class ItemType {
    SONG, ALBUM, ARTIST, PLAYLIST
}
