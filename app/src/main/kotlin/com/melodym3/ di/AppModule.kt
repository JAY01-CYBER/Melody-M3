package com.melodym3.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
// ... (Existing Imports) ...
import dagger.hilt.android.qualifiers.ApplicationContext // Context Import
// ...

@Module
@InstallIn(SingletonComponent::class) 
object AppModule {
    
    // ... (Existing provideOkHttpClient, provideRetrofit, provideYTMService) ...
    
    // --- New: Provide ExoPlayer (Singleton) ---
    @Provides
    @Singleton
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer {
        return ExoPlayer.Builder(context)
            .build()
            .apply {
                // 
                // setMediaItem(MediaItem.fromUri("..."))
            }
    }
    
    // ... (Existing provideMusicRepository) ...
}

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
     * 
     */
    @Provides
    @Singleton
    fun provideMusicRepository(
        // Hilt अब MockMusicRepositoryImpl 
        mockRepo: MockMusicRepositoryImpl 
    ): MusicRepository {
        return mockRepo
    }
    
    //
    // 
}
