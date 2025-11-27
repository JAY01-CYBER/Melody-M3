package com.melodym3.domain.usecase

import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.repository.MusicRepository
import javax.inject.Inject // <-- यह ज़रूरी है

/**
 * Use Case to fetch recommendations specifically for the Home Screen.
 * @Inject constructor tells Hilt how to create an instance.
 */
class GetHomeRecommendationsUseCase @Inject constructor( // <-- @Inject constructor जोड़ा गया
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
            Result.failure(e)
        }
    }
}
