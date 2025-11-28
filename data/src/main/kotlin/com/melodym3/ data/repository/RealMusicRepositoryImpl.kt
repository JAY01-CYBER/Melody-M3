package com.melodym3.data.repository

import com.melodym3.data.remote.YTMService
import com.melodym3.data.remote.model.toDomainModel
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.repository.MusicRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealMusicRepositoryImpl @Inject constructor(
    private val ytmService: YTMService // Hilt द्वारा इंजेक्ट किया गया
) : MusicRepository {

    private val DUMMY_SESSION_ID = "user_session_token_12345" // इसे बाद में असली लॉगिन से बदलना होगा

    override suspend fun getHomeRecommendations(): List<MusicItem> {
        // 1. नेटवर्क सेवा को कॉल करें
        val dtos = ytmService.getHomeRecommendations(sessionId = DUMMY_SESSION_ID)
        
        // 2. DTOs को Domain Models में मैप करें और लौटाएँ
        return dtos.map { it.toDomainModel() }
    }
}
