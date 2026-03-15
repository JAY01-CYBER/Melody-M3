package com.melodym3.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.google.gson.GsonBuilder
import com.melodym3.data.remote.YTMService
import com.melodym3.data.repository.RealMusicRepositoryImpl
import com.melodym3.domain.playback.PlaybackController
import com.melodym3.domain.repository.MusicRepository
import com.melodym3.service.FirebaseService
import com.melodym3.service.MediaPlaybackController
import com.melodym3.service.SnackbarManager
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

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://example.com/")
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().create())
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideYTMService(retrofit: Retrofit): YTMService {
        return retrofit.create(YTMService::class.java)
    }

    @Provides
    @Singleton
    fun provideMusicRepository(service: YTMService): MusicRepository {
        return RealMusicRepositoryImpl(service)
    }

    @Provides
    @Singleton
    fun provideExoPlayer(
        @ApplicationContext context: Context
    ): ExoPlayer {
        return ExoPlayer.Builder(context).build()
    }

    @Provides
    @Singleton
    fun providePlaybackController(
        controller: MediaPlaybackController
    ): PlaybackController {
        return controller
    }

    @Provides
    @Singleton
    fun provideFirebaseService(
        service: FirebaseService
    ): FirebaseService {
        return service
    }

    @Provides
    @Singleton
    fun provideSnackbarManager(
        manager: SnackbarManager
    ): SnackbarManager {
        return manager
    }
}
