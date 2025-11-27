package com.melodym3.di

import com.melodym3.data.repository.MockMusicRepositoryImpl
import com.melodym3.domain.repository.MusicRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) 
object AppModule {

    /**
     * Provides the concrete implementation of the MusicRepository interface.
     * Hilt अब इस फ़ंक्शन को कॉल करेगा जब कोई MusicRepository मांगता है।
     */
    @Provides
    @Singleton
    fun provideMusicRepository(
        // Hilt अब MockMusicRepositoryImpl को स्वचालित रूप से इंजेक्ट करेगा
        mockRepo: MockMusicRepositoryImpl 
    ): MusicRepository {
        return mockRepo
    }
    
    // Note: Use Case को यहाँ Provides करने की आवश्यकता नहीं है, 
    // क्योंकि हमने उसमें @Inject constructor लगा दिया है।
}
