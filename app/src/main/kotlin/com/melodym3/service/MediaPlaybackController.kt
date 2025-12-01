package com.melodym3.service

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.playback.PlaybackController
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Singleton implementation of PlaybackController that manages the connection
 * to the background MediaSessionService and controls the ExoPlayer instance.
 */
@Singleton
class MediaPlaybackController @Inject constructor(
    @ApplicationContext context: Context,
    // Hilt injects the singleton ExoPlayer instance defined in AppModule
    private val player: ExoPlayer 
) : PlaybackController {

    // Future reference to the MediaController for communicating with the MediaSessionService
    private val controllerFuture: ListenableFuture<MediaController>
    
    // Once connected, this holds the actual controller instance
    private var mediaController: MediaController? = null 

    init {
        // 1. Define the SessionToken pointing to the MediaSessionService
        val sessionToken = SessionToken(
            context, 
            android.content.ComponentName(context, MelodyM3MediaService::class.java)
        )
        
        // 2. Build and start the asynchronous connection to the MediaSessionService
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

        // 3. Add a listener to handle the controller connection result
        controllerFuture.addListener({
            try {
                // Connection successful, store the controller
                mediaController = controllerFuture.get()
            } catch (e: Exception) {
                // Handle connection errors
                e.printStackTrace()
            }
        }, context.mainExecutor)
    }

    /**
     * Maps the clean domain MusicItem model to the Media3 MediaItem required for playback.
     */
    private fun MusicItem.toMediaItem(): MediaItem {
        return MediaItem.Builder()
            .setMediaId(this.id)
            // NOTE: The URI MUST be the actual streaming URL (e.g., .mp4, .m4a) obtained
            // from the unofficial API/Backend proxy (Step 13's responsibility).
            .setUri(this.imageUrl) // Temporarily using image URL for structure, must be stream URL
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(this.title)
                    .setArtist(this.subtitle)
                    .setArtworkUri(android.net.Uri.parse(this.imageUrl))
                    .build()
            )
            .build()
    }

    override fun play(musicItem: MusicItem) {
        val mediaItem = musicItem.toMediaItem()
        
        // Use the injected ExoPlayer instance directly
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

    override fun seekToNext() {
        player.seekToNextMediaItem()
    }
    
    override fun getCurrentPosition(): Long {
        return player.currentPosition
    }
    
    // NOTE: In a real app, you would also add methods here to expose the player state 
    // (isPlaying, currentMediaItem, position) via Kotlin Flow to the ViewModel.
}
