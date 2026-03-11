package com.melodym3.domain.usecase

import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.repository.MusicRepository
import javax.inject.Inject

class SearchMusicUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(query: String): List<MusicItem> {
        return if (query.isBlank()) {
            emptyList()
        } else {
            musicRepository.searchMusic(query)
        }
    }
}
