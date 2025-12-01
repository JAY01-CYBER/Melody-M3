package com.melodym3.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.google.gson.GsonBuilder
import com.melodym3.data.remote.YTMService
import com.melodym3.data.repository.RealMusicRepositoryImpl
import com.melodym3.domain.playback.PlaybackController
import com.melodym3.domain.repository.MusicRepository
import com.melodym3.service.MediaPlaybackController
import com.melodym3.service.SnackbarManager // <-- New Import (Step 22)
import com.melodym3.service.FirebaseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) 
object AppModule {
    
    // ===========================================
    // 1. NETWORKING SETUP
    // ===========================================

    /**
     * Provides OkHttpClient for logging network requests and general configuration.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { 
            // Logging network body for debugging purposes
            level = HttpLoggingInterceptor.Level.BODY 
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    
    /**
     * Provides the Retrofit instance for API communication.
     */
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        // NOTE: Replace this with the actual URL of your Unofficial YTM Backend/Proxy.
        val BASE_URL = "https://your-ytm-proxy-backend.com/api/v1/" 
        
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }
    
    /**
     * Provides the Retrofit Service interface implementation.
     */
    @Provides
    @Singleton
    fun provideYTMService(retrofit: Retrofit): YTMService {
        return retrofit.create(YTMService::class.java)
    }
    
    // ===========================================
    // 2. DATA LAYER IMPLEMENTATION
    // ===========================================

    /**
     * Provides the concrete implementation of the MusicRepository interface.
     * Uses the Real implementation, which depends on the network service.
     */
    @Provides
    @Singleton
    fun provideMusicRepository(service: YTMService): MusicRepository {
        return RealMusicRepositoryImpl(service) 
    }
    
    // ===========================================
    // 3. MEDIA PLAYBACK SETUP
    // ===========================================

    /**
     * Provides a Singleton instance of the ExoPlayer for background playback.
     */
    @Provides
    @Singleton
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer {
        return ExoPlayer.Builder(context)
            .build()
    }
    
    /**
     * Provides the PlaybackController interface implemented by MediaPlaybackController.
     */
    @Provides
    @Singleton
    fun providePlaybackController(controller: MediaPlaybackController): PlaybackController {
        return controller 
    }

    // ===========================================
    // 4. FIREBASE AND UTILITY SERVICES
    // ===========================================

    /**
     * Provides the FirebaseService singleton for authentication and database access.
     */
    @Provides
    @Singleton
    fun provideFirebaseService(service: FirebaseService): FirebaseService {
        // FirebaseService's init{} block handles initialization and authentication
        return service
    }
    
    /**
     * Provides the SnackbarManager singleton for global UI message handling. (Step 22)
     */
    @Provides
    @Singleton
    fun provideSnackbarManager(manager: SnackbarManager): SnackbarManager {
        return manager
    }
}
