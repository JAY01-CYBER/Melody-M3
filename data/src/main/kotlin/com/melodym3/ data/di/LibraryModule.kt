package com.melodym3.data.di

import com.melodym3.data.repository.FirebaseLibraryRepository
import com.melodym3.domain.repository.LibraryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LibraryModule {
    
    @Provides
    @Singleton
    fun provideLibraryRepository(
        firebaseLibraryRepository: FirebaseLibraryRepository
    ): LibraryRepository {
        return firebaseLibraryRepository
    }
}
