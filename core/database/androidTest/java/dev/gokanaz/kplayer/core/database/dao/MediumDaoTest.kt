package dev.gokanaz.kplayer.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.gokanaz.kplayer.core.database.MediaDatabase
import dev.gokanaz.kplayer.core.database.entities.DirectoryEntity
import dev.gokanaz.kplayer.core.database.entities.MediumEntity
import dev.gokanaz.kplayer.core.database.entities.MediumStateEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList

@RunWith(AndroidJUnit4::class)
class MediumDaoTest {
    
    private lateinit var database: MediaDatabase
    private lateinit var mediumDao: MediumDao
    private lateinit var directoryDao: DirectoryDao
    private lateinit var mediumStateDao: MediumStateDao
    
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            MediaDatabase::class.java
        ).build()
        mediumDao = database.mediumDao()
        directoryDao = database.directoryDao()
        mediumStateDao = database.mediumStateDao()
    }
    
    @After
    fun closeDb() {
        database.close()
    }
    
    @Test
    fun testInsertAndGetMedium() = runTest {
        val directory = createTestDirectory()
        directoryDao.insertDirectory(directory)
        
        val medium = createTestMedium(directory.bucketId)
        mediumDao.insertMedium(medium)
        
        val retrieved = mediumDao.getMediumById(medium.id).first()
        assertThat(retrieved).isNotNull()
        assertThat(retrieved?.id).isEqualTo(medium.id)
        assertThat(retrieved?.title).isEqualTo(medium.title)
        assertThat(retrieved?.fileName).isEqualTo(medium.fileName)
    }
    
    @Test
    fun testUpdateMedium() = runTest {
        val directory = createTestDirectory()
        directoryDao.insertDirectory(directory)
        
        val medium = createTestMedium(directory.bucketId)
        mediumDao.insertMedium(medium)
        
        val updatedMedium = medium.copy(
            title = "Updated Title",
            duration = 7200000L
        )
        mediumDao.updateMedium(updatedMedium)
        
        val retrieved = mediumDao.getMediumById(medium.id).first()
        assertThat(retrieved?.title).isEqualTo("Updated Title")
        assertThat(retrieved?.duration).isEqualTo(7200000L)
    }
    
    @Test
    fun testDeleteMedium() = runTest {
        val directory = createTestDirectory()
        directoryDao.insertDirectory(directory)
        
        val medium = createTestMedium(directory.bucketId)
        mediumDao.insertMedium(medium)
        mediumDao.deleteMedium(medium)
        
        val retrieved = mediumDao.getMediumById(medium.id).first()
        assertThat(retrieved).isNull()
    }
    
    @Test
    fun testGetMediaByBucketId() = runTest {
        val directory = createTestDirectory("bucket1", "Movies", "/storage/movies")
        directoryDao.insertDirectory(directory)
        
        val media = listOf(
            createTestMedium(directory.bucketId, "1", "video1.mp4"),
            createTestMedium(directory.bucketId, "2", "video2.mp4"),
            createTestMedium(directory.bucketId, "3", "video3.mp4")
        )
        mediumDao.insertMedia(media)
        
        val bucketMedia = mediumDao.getMediaByBucketId(directory.bucketId).first()
        assertThat(bucketMedia).hasSize(3)
        assertThat(bucketMedia.map { it.id }).containsExactly("1", "2", "3")
    }
    
    @Test
    fun testGetAllVideos() = runTest {
        val directory = createTestDirectory()
        directoryDao.insertDirectory(directory)
        
        val media = listOf(
            createTestMedium(directory.bucketId, "1", "video1.mp4", isVideo = true, isAudio = false),
            createTestMedium(directory.bucketId, "2", "audio1.mp3", isVideo = false, isAudio = true),
            createTestMedium(directory.bucketId, "3", "video2.mp4", isVideo = true, isAudio = false)
        )
        mediumDao.insertMedia(media)
        
        val videos = mediumDao.getAllVideos().first()
        assertThat(videos).hasSize(2)
        assertThat(videos.map { it.id }).containsExactly("1", "3")
    }
    
    @Test
    fun testGetAllAudios() = runTest {
        val directory = createTestDirectory()
        directoryDao.insertDirectory(directory)
        
        val media = listOf(
            createTestMedium(directory.bucketId, "1", "video1.mp4", isVideo = true, isAudio = false),
            createTestMedium(directory.bucketId, "2", "audio1.mp3", isVideo = false, isAudio = true),
            createTestMedium(directory.bucketId, "3", "audio2.mp3", isVideo = false, isAudio = true)
        )
        mediumDao.insertMedia(media)
        
        val audios = mediumDao.getAllAudios().first()
        assertThat(audios).hasSize(2)
        assertThat(audios.map { it.id }).containsExactly("2", "3")
    }
    
    @Test
    fun testSearchMedia() = runTest {
        val directory = createTestDirectory()
        directoryDao.insertDirectory(directory)
        
        val media = listOf(
            createTestMedium(directory.bucketId, "1", "avengers_endgame.mp4", "Avengers: Endgame"),
            createTestMedium(directory.bucketId, "2", "inception.mp4", "Inception"),
            createTestMedium(directory.bucketId, "3", "avengers_infinity_war.mp4", "Avengers: Infinity War")
        )
        mediumDao.insertMedia(media)
        
        val searchResults = mediumDao.searchMedia("avengers").first()
        assertThat(searchResults).hasSize(2)
        assertThat(searchResults.map { it.id }).containsExactly("1", "3")
    }
    
    @Test
    fun testGetRecentMedia() = runTest {
        val directory = createTestDirectory()
        directoryDao.insertDirectory(directory)
        
        val now = System.currentTimeMillis()
        val media = listOf(
            createTestMedium(directory.bucketId, "1", "old.mp4", dateAdded = now - 1000000),
            createTestMedium(directory.bucketId, "2", "recent.mp4", dateAdded = now - 100000),
            createTestMedium(directory.bucketId, "3", "newest.mp4", dateAdded = now - 50000)
        )
        mediumDao.insertMedia(media)
        
        val recentMedia = mediumDao.getRecentMedia(2).first()
        assertThat(recentMedia).hasSize(2)
        assertThat(recentMedia.map { it.id }).containsExactly("3", "2")
    }
    
    @Test
    fun testGetMediaStatistics() = runTest {
        val directory = createTestDirectory()
        directoryDao.insertDirectory(directory)
        
        val media = listOf(
            createTestMedium(directory.bucketId, "1", "video1.mp4", 1024L * 1024L * 100L, 3600000L, true, false),
            createTestMedium(directory.bucketId, "2", "video2.mp4", 1024L * 1024L * 200L, 5400000L, true, false),
            createTestMedium(directory.bucketId, "3", "audio1.mp3", 1024L * 1024L * 50L, 1800000L, false, true)
        )
        mediumDao.insertMedia(media)
        
        val totalCount = mediumDao.getTotalMediaCount()
        val videoCount = mediumDao.getTotalVideoCount()
        val audioCount = mediumDao.getTotalAudioCount()
        val totalSize = mediumDao.getTotalMediaSize()
        val totalDuration = mediumDao.getTotalMediaDuration()
        
        assertThat(totalCount).isEqualTo(3)
        assertThat(videoCount).isEqualTo(2)
        assertThat(audioCount).isEqualTo(1)
        assertThat(totalSize).isEqualTo(1024L * 1024L * 350L)
        assertThat(totalDuration).isEqualTo(10800000L)
    }
    
    @Test
    fun testGetMediumWithInfo() = runTest {
        val directory = createTestDirectory()
        directoryDao.insertDirectory(directory)
        
        val medium = createTestMedium(directory.bucketId)
        mediumDao.insertMedium(medium)
        
        val state = MediumStateEntity(
            mediumId = medium.id,
            lastPlayedPosition = 120000L,
            isFavorite = true,
            watchCount = 5,
            lastPlayedAt = System.currentTimeMillis()
        )
        mediumStateDao.insertState(state)
        
        val mediumWithInfo = mediumDao.getMediumWithInfo(medium.id).first()
        assertThat(mediumWithInfo).isNotNull()
        assertThat(mediumWithInfo?.medium?.id).isEqualTo(medium.id)
        assertThat(mediumWithInfo?.state).isNotNull()
        assertThat(mediumWithInfo?.state?.isFavorite).isTrue()
        assertThat(mediumWithInfo?.state?.watchCount).isEqualTo(5)
    }
    
    @Test
    fun testFlowObservability() = runTest {
        val directory = createTestDirectory()
        directoryDao.insertDirectory(directory)
        
        val mediumFlow = mediumDao.getAllVideos()
        
        val initialList = mediumFlow.first()
        assertThat(initialList).isEmpty()
        
        val medium = createTestMedium(directory.bucketId, "1", "video.mp4", isVideo = true)
        mediumDao.insertMedium(medium)
        
        val updatedList = mediumFlow.first()
        assertThat(updatedList).hasSize(1)
        assertThat(updatedList[0].id).isEqualTo("1")
    }
    
    @Test
    fun testTransactionRollback() = runTest {
        val directory = createTestDirectory()
        directoryDao.insertDirectory(directory)
        
        val medium1 = createTestMedium(directory.bucketId, "1", "video1.mp4")
        val medium2 = createTestMedium(directory.bucketId, "2", "video2.mp4")
        
        try {
            mediumDao.insertMedia(listOf(medium1, medium2))
            throw RuntimeException("Simulated failure")
        } catch (e: Exception) {
        }
        
        val media = mediumDao.getMediaByBucketId(directory.bucketId).first()
        assertThat(media).hasSize(2)
    }
    
    private fun createTestDirectory(
        bucketId: String = "test_bucket_1",
        name: String = "Test Directory",
        path: String = "/storage/test"
    ): DirectoryEntity {
        val now = System.currentTimeMillis()
        return DirectoryEntity(
            bucketId = bucketId,
            name = name,
            path = path,
            dateAdded = now,
            dateModified = now,
            mediaCount = 0,
            totalSize = 0L,
            totalDuration = 0L
        )
    }
    
    private fun createTestMedium(
        bucketId: String,
        id: String = "test_medium_1",
        fileName: String = "test_video.mp4",
        title: String? = "Test Video",
        size: Long = 1024L * 1024L * 50L,
        duration: Long = 3600000L,
        isVideo: Boolean = true,
        isAudio: Boolean = false,
        dateAdded: Long = System.currentTimeMillis()
    ): MediumEntity {
        return MediumEntity(
            id = id,
            uri = "content://media/$id",
            title = title,
            fileName = fileName,
            filePath = "/storage/test/$fileName",
            mimeType = if (isVideo) "video/mp4" else "audio/mpeg",
            size = size,
            duration = duration,
            dateAdded = dateAdded,
            dateModified = dateAdded,
            bucketId = bucketId,
            bucketDisplayName = "Test Directory",
            width = if (isVideo) 1920 else 0,
            height = if (isVideo) 1080 else 0,
            resolution = if (isVideo) "1920x1080" else null,
            isVideo = isVideo,
            isAudio = isAudio,
            thumbnail = null
        )
    }
}
