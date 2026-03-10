package com.melodym3.domain.model

data class MusicItem(
    val id: String,
    val title: String,
    val artist: String,
    val duration: Long,
    val url: String
)
