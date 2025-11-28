package com.melodym3.service

import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// Hilt Media Service में इंजेक्शन को सपोर्ट करता है
@AndroidEntryPoint
class MelodyM3MediaService : MediaSessionService() {

    @Inject
    lateinit var player: ExoPlayer // Hilt द्वारा ExoPlayer इंजेक्ट किया जाएगा

    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        
        // 1. MediaSession बनाएं और इसे Player से जोड़ें
        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(null) // PendingIntent to open the main app activity
            .build()
        
        // Note: यहाँ आपको MediaSession.Callback में custom commands (जैसे लॉगिन) हैंडल करने होंगे।
        
        // 2. Playback तैयार करें (जैसे user preferences या last played item लोड करना)
        // player.prepare() 
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        // UI या OS को MediaSession प्रदान करें
        return mediaSession
    }

    override fun onDestroy() {
        // रिसोर्स रिलीज़ करें
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}
