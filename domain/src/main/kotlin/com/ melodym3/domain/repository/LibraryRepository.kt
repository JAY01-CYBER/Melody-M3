package com.melodym3.domain.repository

import com.melodym3.domain.model.MusicItem

interface LibraryRepository {
    suspend fun getLibraryItems(): List<MusicItem>
    suspend fun addToLibrary(item: MusicItem): Boolean
    suspend fun removeFromLibrary(itemId: String): Boolean
}
