package com.melodym3.domain.usecase

import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.repository.MusicRepository

/**
 * Use Case to fetch recommendations specifically for the Home Screen.
 */
class GetHomeRecommendationsUseCase(
    private val repository: MusicRepository
) {
    /**
     * Operator function allows calling the use case like a function: useCase().
     */
    suspend operator fun invoke(): Result<List<MusicItem>> {
        return try {
            val items = repository.getHomeRecommendations()
            Result.success(items)
        } catch (e: Exception) {
            // Handle specific networking or API errors here later
            Result.failure(e)
        }
    }
}
