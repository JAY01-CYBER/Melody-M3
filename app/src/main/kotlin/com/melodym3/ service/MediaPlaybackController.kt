package com.melodym3.service

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.playback.PlaybackController
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaPlaybackController @Inject constructor(
    @ApplicationContext context: Context,
    private val player: ExoPlayer // Hilt से इंजेक्ट किया गया
) : PlaybackController {
    
    private var controllerFuture: ListenableFuture<MediaController>
    
    init {
        // Media Session Service से कनेक्ट करने के लिए SessionToken का उपयोग करें
        val sessionToken = SessionToken(context, 
            android.content.ComponentName(context, MelodyM3MediaService::class.java))
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
    }
    
    // YTM MusicItem को Media3 MediaItem में बदलना होगा
    private fun MusicItem.toMediaItem(): androidx.media3.common.MediaItem {
        return androidx.media3.common.MediaItem.Builder()
            .setMediaId(this.id)
            .setUri(this.imageUrl) // यहाँ आपको असली स्ट्रीमिंग URL डालना होगा!
            .setMediaMetadata(
                androidx.media3.common.MediaMetadata.Builder()
                    .setTitle(this.title)
                    .setArtist(this.subtitle)
                    .build()
            )
            .build()
    }

    override fun play(musicItem: MusicItem) {
        val mediaItem = musicItem.toMediaItem()
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }
    
    override fun pause() {
        player.pause()
    }

    override fun resume() {
        player.play()
    }

    // ... (अन्य Playback फंक्शन जैसे seekToNext, etc.) ...
    override fun seekToNext() {
        player.seekToNextMediaItem()
    }
    
    override fun getCurrentPosition(): Long {
        return player.currentPosition
    }
}
