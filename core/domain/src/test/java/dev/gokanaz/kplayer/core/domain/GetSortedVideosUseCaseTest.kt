package dev.gokanaz.kplayer.core.domain

import android.net.Uri
import dev.gokanaz.kplayer.core.media.model.MediaVideo
import dev.gokanaz.kplayer.core.media.service.MediaService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetSortedVideosUseCaseTest {
    
    private lateinit var mediaService: MediaService
    private lateinit var getSortedVideosUseCase: GetSortedVideosUseCase
    
    @Before
    fun setup() {
        mediaService = mockk()
        getSortedVideosUseCase = GetSortedVideosUseCase(mediaService)
    }
    
    @Test
    fun `test get videos success`() = runTest {
        val mockVideos = createMockVideos()
        coEvery { mediaService.getVideos() } returns flowOf(mockVideos)
        
        val result = getSortedVideosUseCase().first()
        
        assertTrue(result is Result.Success)
        assertEquals(3, (result as Result.Success).data.size)
    }
    
    @Test
    fun `test get videos error`() = runTest {
        val exception = RuntimeException("Failed to load videos")
        coEvery { mediaService.getVideos() } throws exception
        
        val result = getSortedVideosUseCase().first()
        
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
    }
    
    @Test
    fun `test sort by name ascending`() = runTest {
        val mockVideos = createMockVideos()
        coEvery { mediaService.getVideos() } returns flowOf(mockVideos)
        
        val result = getSortedVideosUseCase(
            sortType = SortType.Name,
            order = SortOrder.Ascending
        ).first()
        
        assertTrue(result is Result.Success)
        val videos = (result as Result.Success).data
        assertEquals("Video A", videos[0].title)
        assertEquals("Video B", videos[1].title)
        assertEquals("Video C", videos[2].title)
    }
    
    @Test
    fun `test sort by name descending`() = runTest {
        val mockVideos = createMockVideos()
        coEvery { mediaService.getVideos() } returns flowOf(mockVideos)
        
        val result = getSortedVideosUseCase(
            sortType = SortType.Name,
            order = SortOrder.Descending
        ).first()
        
        assertTrue(result is Result.Success)
        val videos = (result as Result.Success).data
        assertEquals("Video C", videos[0].title)
        assertEquals("Video B", videos[1].title)
        assertEquals("Video A", videos[2].title)
    }
    
    @Test
    fun `test sort by date ascending`() = runTest {
        val mockVideos = createMockVideos()
        coEvery { mediaService.getVideos() } returns flowOf(mockVideos)
        
        val result = getSortedVideosUseCase(
            sortType = SortType.Date,
            order = SortOrder.Ascending
        ).first()
        
        assertTrue(result is Result.Success)
        val videos = (result as Result.Success).data
        assertEquals(1000, videos[0].dateAdded)
        assertEquals(2000, videos[1].dateAdded)
        assertEquals(3000, videos[2].dateAdded)
    }
    
    @Test
    fun `test sort by size descending`() = runTest {
        val mockVideos = createMockVideos()
        coEvery { mediaService.getVideos() } returns flowOf(mockVideos)
        
        val result = getSortedVideosUseCase(
            sortType = SortType.Size,
            order = SortOrder.Descending
        ).first()
        
        assertTrue(result is Result.Success)
        val videos = (result as Result.Success).data
        assertEquals(3000, videos[0].size)
        assertEquals(2000, videos[1].size)
        assertEquals(1000, videos[2].size)
    }
    
    @Test
    fun `test filter by bucket id`() = runTest {
        val mockVideos = createMockVideos()
        coEvery { mediaService.getVideos() } returns flowOf(mockVideos)
        
        val filter = VideoFilter(bucketId = "bucket1")
        val result = getSortedVideosUseCase(filter = filter).first()
        
        assertTrue(result is Result.Success)
        val videos = (result as Result.Success).data
        assertEquals(1, videos.size)
        assertEquals("bucket1", videos[0].bucketId)
    }
    
    @Test
    fun `test filter by duration range`() = runTest {
        val mockVideos = createMockVideos()
        coEvery { mediaService.getVideos() } returns flowOf(mockVideos)
        
        val filter = VideoFilter(
            minDuration = 150,
            maxDuration = 250
        )
        val result = getSortedVideosUseCase(filter = filter).first()
        
        assertTrue(result is Result.Success)
        val videos = (result as Result.Success).data
        assertEquals(1, videos.size)
        assertEquals(200, videos[0].duration)
    }
    
    @Test
    fun `test filter by size range`() = runTest {
        val mockVideos = createMockVideos()
        coEvery { mediaService.getVideos() } returns flowOf(mockVideos)
        
        val filter = VideoFilter(
            minSize = 1500,
            maxSize = 2500
        )
        val result = getSortedVideosUseCase(filter = filter).first()
        
        assertTrue(result is Result.Success)
        val videos = (result as Result.Success).data
        assertEquals(1, videos.size)
        assertEquals(2000, videos[0].size)
    }
    
    @Test
    fun `test filter by date range`() = runTest {
        val mockVideos = createMockVideos()
        coEvery { mediaService.getVideos() } returns flowOf(mockVideos)
        
        val filter = VideoFilter(
            startDate = 1500,
            endDate = 2500
        )
        val result = getSortedVideosUseCase(filter = filter).first()
        
        assertTrue(result is Result.Success)
        val videos = (result as Result.Success).data
        assertEquals(1, videos.size)
        assertEquals(2000, videos[0].dateAdded)
    }
    
    @Test
    fun `test empty list`() = runTest {
        coEvery { mediaService.getVideos() } returns flowOf(emptyList())
        
        val result = getSortedVideosUseCase().first()
        
        assertTrue(result is Result.Success)
        assertTrue((result as Result.Success).data.isEmpty())
    }
    
    @Test
    fun `test multiple filters combined`() = runTest {
        val mockVideos = createMockVideos()
        coEvery { mediaService.getVideos() } returns flowOf(mockVideos)
        
        val filter = VideoFilter(
            bucketId = "bucket2",
            minDuration = 180,
            maxDuration = 220,
            minSize = 1800,
            maxSize = 2200
        )
        val result = getSortedVideosUseCase(filter = filter).first()
        
        assertTrue(result is Result.Success)
        val videos = (result as Result.Success).data
        assertEquals(1, videos.size)
        assertEquals("bucket2", videos[0].bucketId)
        assertEquals(200, videos[0].duration)
        assertEquals(2000, videos[0].size)
    }
    
    private fun createMockVideos(): List<MediaVideo> {
        return listOf(
            MediaVideo(
                id = 1,
                title = "Video B",
                uri = Uri.parse("content://video/1"),
                duration = 100,
                size = 2000,
                dateAdded = 2000,
                dateModified = 2000,
                mimeType = "video/mp4",
                resolution = "1920x1080",
                bucketId = "bucket2",
                bucketDisplayName = "Folder B"
            ),
            MediaVideo(
                id = 2,
                title = "Video A",
                uri = Uri.parse("content://video/2"),
                duration = 300,
                size = 1000,
                dateAdded = 1000,
                dateModified = 1000,
                mimeType = "video/mp4",
                resolution = "1280x720",
                bucketId = "bucket1",
                bucketDisplayName = "Folder A"
            ),
            MediaVideo(
                id = 3,
                title = "Video C",
                uri = Uri.parse("content://video/3"),
                duration = 200,
                size = 3000,
                dateAdded = 3000,
                dateModified = 3000,
                mimeType = "video/mp4",
                resolution = "3840x2160",
                bucketId = "bucket3",
                bucketDisplayName = "Folder C"
            )
        )
    }
}
