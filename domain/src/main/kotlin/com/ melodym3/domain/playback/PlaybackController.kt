package com.melodym3.domain.playback

// यह Interface UI को Media Service से संवाद करने की अनुमति देता है
interface PlaybackController {
    fun play(musicItem: com.melodym3.domain.model.MusicItem)
    fun pause()
    fun resume()
    fun seekToNext()
    fun getCurrentPosition(): Long
    // State Flow for current playing item, duration, etc. would be added here
}
