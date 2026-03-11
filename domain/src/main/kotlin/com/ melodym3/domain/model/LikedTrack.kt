package com.melodym3.domain.model

data class LikedTrack(
    val id: String,
    val title: String,
    val artist: String,
    val duration: Long,
    val url: String,
    val imageUrl: String = "",
    val subtitle: String = "",
    val likedAt: Long = System.currentTimeMillis()
)
