package com.melodym3.data.repository

import com.melodym3.domain.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.ServiceList
import org.schabi.newpipe.extractor.stream.StreamInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YouTubeRepository @Inject constructor() {

    init {
        NewPipe.init(org.schabi.newpipe.extractor.downloader.Downloader.getInstance())
    }

    suspend fun searchSongs(query: String): List<Song> = withContext(Dispatchers.IO) {
        val searchResult = ServiceList.YouTube.search(query).get()
        searchResult.items
            .filter { it is org.schabi.newpipe.extractor.search.SearchResult.SearchItem.Video }
            .take(20)
            .map { item ->
                Song(
                    id = item.id,
                    title = item.name,
                    subtitle = item.uploaderName ?: "Unknown Artist",
                    thumbnail = item.thumbnails.bestPreviewUrl,
                    duration = item.duration?.toMillis()?.let {
                        String.format("%d:%02d", it / 60000, (it % 60000) / 1000)
                    } ?: "0:00"
                )
            }
    }

    suspend fun getStreamUrl(videoId: String): String = withContext(Dispatchers.IO) {
        val info = StreamInfo.getInfo("https://youtube.com/watch?v=$videoId")
        info.audioStreams
            .maxByOrNull { it.bitrate ?: 0 }
            ?.url ?: ""
    }
}
