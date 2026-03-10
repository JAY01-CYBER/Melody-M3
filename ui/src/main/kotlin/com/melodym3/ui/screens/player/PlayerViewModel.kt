package com.melodym3.ui.screens.player

import androidx.lifecycle.ViewModel
import com.melodym3.domain.model.MusicItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor() : ViewModel() {

    fun updatePlaybackInfo(item: MusicItem) {
        val title = item.title
        val subtitle = item.subtitle
        val imageUrl = item.imageUrl
    }

    fun saveTrack(item: MusicItem) {
        // Logic to save track using MusicItem
    }

    fun removeTrack(item: MusicItem) {
        // Logic to remove track
    }
}
