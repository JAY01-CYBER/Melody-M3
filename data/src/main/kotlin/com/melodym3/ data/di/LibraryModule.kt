package com.melodym3.data.di

import com.melodym3.data.repository.FirebaseLibraryRepository
import com.melodym3.domain.repository.LibraryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LibraryModule {
    
    @Binds
    @Singleton
    abstract fun bindLibraryRepository(
        firebaseLibraryRepository: FirebaseLibraryRepository
    ): LibraryRepository
}
