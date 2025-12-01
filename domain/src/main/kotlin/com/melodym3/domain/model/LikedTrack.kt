package com.melodym3.domain.model

/**
 * Domain model representing a song saved to the user's library.
 * Note: Firestore requires public no-argument constructor for automatic deserialization.
 */
data class LikedTrack(
    val id: String = "", // Same as MusicItem ID
    val title: String = "",
    val artist: String = "",
    val albumArtUrl: String = "",
    val savedTimestamp: Long = System.currentTimeMillis()
)
