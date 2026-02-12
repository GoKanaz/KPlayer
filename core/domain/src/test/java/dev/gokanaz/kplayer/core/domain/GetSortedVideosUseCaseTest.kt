package dev.gokanaz.kplayer.core.domain

import android.net.Uri
import dev.gokanaz.kplayer.core.media.model.MediaVideo
import dev.gokanaz.kplayer.core.media.services.LocalMediaService
import dev.gokanaz.kplayer.core.domain.model.SortOption
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Date

class GetSortedVideosUseCaseTest {
    
    @MockK
    private lateinit var localMediaService: LocalMediaService
    
    private lateinit var getSortedVideosUseCase: GetSortedVideosUseCase
    
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        getSortedVideosUseCase = GetSortedVideosUseCase(localMediaService)
    }
    
    @Test
    fun `invoke with NAME_ASC returns videos sorted by name ascending`() = runBlocking {
        val mockVideos = listOf(
            createMockVideo("3", "Zebra Video"),
            createMockVideo("1", "Apple Video"),
            createMockVideo("2", "Banana Video")
        )
        
        every { localMediaService.allVideos.value } returns mockVideos
        
        val result = getSortedVideosUseCase(SortOption.NAME_ASC).first()
        
        assertEquals("Apple Video", result[0].title)
        assertEquals("Banana Video", result[1].title)
        assertEquals("Zebra Video", result[2].title)
    }
    
    @Test
    fun `invoke with NAME_DESC returns videos sorted by name descending`() = runBlocking {
        val mockVideos = listOf(
            createMockVideo("1", "Apple Video"),
            createMockVideo("2", "Banana Video"),
            createMockVideo("3", "Zebra Video")
        )
        
        every { localMediaService.allVideos.value } returns mockVideos
        
        val result = getSortedVideosUseCase(SortOption.NAME_DESC).first()
        
        assertEquals("Zebra Video", result[0].title)
        assertEquals("Banana Video", result[1].title)
        assertEquals("Apple Video", result[2].title)
    }
    
    @Test
    fun `invoke with DURATION_ASC returns videos sorted by duration ascending`() = runBlocking {
        val mockVideos = listOf(
            createMockVideo("1", "Long Video", 300000),
            createMockVideo("2", "Short Video", 60000),
            createMockVideo("3", "Medium Video", 180000)
        )
        
        every { localMediaService.allVideos.value } returns mockVideos
        
        val result = getSortedVideosUseCase(SortOption.DURATION_ASC).first()
        
        assertEquals("Short Video", result[0].title)
        assertEquals("Medium Video", result[1].title)
        assertEquals("Long Video", result[2].title)
    }
    
    @Test
    fun `searchVideos returns matching videos`() = runBlocking {
        val mockVideos = listOf(
            createMockVideo("1", "Apple Video"),
            createMockVideo("2", "Banana Video"),
            createMockVideo("3", "Pineapple Video")
        )
        
        every { localMediaService.searchVideos("apple") } returns listOf(mockVideos[0], mockVideos[2])
        
        val result = getSortedVideosUseCase.searchVideos("apple").first()
        
        assertEquals(2, result.size)
        assertTrue(result.any { it.title.contains("Apple", ignoreCase = true) })
        assertTrue(result.any { it.title.contains("Pineapple", ignoreCase = true) })
    }
    
    private fun createMockVideo(id: String, title: String, duration: Long = 0): MediaVideo {
        return MediaVideo(
            id = id,
            uri = mockk(),
            title = title,
            artist = "Test Artist",
            album = "Test Album",
            duration = duration,
            size = 1024 * 1024,
            path = "/storage/test/video.mp4",
            mimeType = "video/mp4",
            dateAdded = Date(),
            dateModified = Date()
        )
    }
}
