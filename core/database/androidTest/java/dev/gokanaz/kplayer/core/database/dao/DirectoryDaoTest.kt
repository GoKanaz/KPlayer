package dev.gokanaz.kplayer.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.gokanaz.kplayer.core.database.MediaDatabase
import dev.gokanaz.kplayer.core.database.entities.DirectoryEntity
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
class DirectoryDaoTest {
    
    private lateinit var database: MediaDatabase
    private lateinit var directoryDao: DirectoryDao
    
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            MediaDatabase::class.java
        ).build()
        directoryDao = database.directoryDao()
    }
    
    @After
    fun closeDb() {
        database.close()
    }
    
    @Test
    fun testInsertAndGetDirectory() = runTest {
        val directory = createTestDirectory()
        directoryDao.insertDirectory(directory)
        
        val retrieved = directoryDao.getDirectoryByBucketId(directory.bucketId)
        assertThat(retrieved).isNotNull()
        assertThat(retrieved?.bucketId).isEqualTo(directory.bucketId)
        assertThat(retrieved?.name).isEqualTo(directory.name)
        assertThat(retrieved?.path).isEqualTo(directory.path)
    }
    
    @Test
    fun testUpdateDirectory() = runTest {
        val directory = createTestDirectory()
        directoryDao.insertDirectory(directory)
        
        val updatedDirectory = directory.copy(
            name = "Updated Name",
            mediaCount = 10,
            totalSize = 1024L * 1024L * 100L
        )
        directoryDao.updateDirectory(updatedDirectory)
        
        val retrieved = directoryDao.getDirectoryByBucketId(directory.bucketId)
        assertThat(retrieved?.name).isEqualTo("Updated Name")
        assertThat(retrieved?.mediaCount).isEqualTo(10)
        assertThat(retrieved?.totalSize).isEqualTo(1024L * 1024L * 100L)
    }
    
    @Test
    fun testDeleteDirectory() = runTest {
        val directory = createTestDirectory()
        directoryDao.insertDirectory(directory)
        directoryDao.deleteDirectory(directory)
        
        val retrieved = directoryDao.getDirectoryByBucketId(directory.bucketId)
        assertThat(retrieved).isNull()
    }
    
    @Test
    fun testGetAllDirectories() = runTest {
        val directories = listOf(
            createTestDirectory("1", "Movies", "/storage/movies"),
            createTestDirectory("2", "TV Shows", "/storage/tv"),
            createTestDirectory("3", "Music", "/storage/music")
        )
        directoryDao.insertDirectories(directories)
        
        val allDirectories = directoryDao.getAllDirectories().first()
        assertThat(allDirectories).hasSize(3)
        assertThat(allDirectories.map { it.bucketId }).containsExactly("1", "2", "3")
    }
    
    @Test
    fun testSearchDirectories() = runTest {
        val directories = listOf(
            createTestDirectory("1", "Action Movies", "/storage/action"),
            createTestDirectory("2", "Comedy Movies", "/storage/comedy"),
            createTestDirectory("3", "Pop Music", "/storage/pop")
        )
        directoryDao.insertDirectories(directories)
        
        val searchResults = directoryDao.searchDirectories("Movies").first()
        assertThat(searchResults).hasSize(2)
        assertThat(searchResults.map { it.name }).containsExactly("Action Movies", "Comedy Movies")
    }
    
    @Test
    fun testUpdateDirectoryStatistics() = runTest {
        val directory = createTestDirectory()
        directoryDao.insertDirectory(directory)
        
        directoryDao.updateDirectoryStatistics(directory.bucketId, 25, 1024L * 1024L * 500L, 7200000L)
        
        val retrieved = directoryDao.getDirectoryByBucketId(directory.bucketId)
        assertThat(retrieved?.mediaCount).isEqualTo(25)
        assertThat(retrieved?.totalSize).isEqualTo(1024L * 1024L * 500L)
        assertThat(retrieved?.totalDuration).isEqualTo(7200000L)
    }
    
    @Test
    fun testGetDirectoriesCount() = runTest {
        val directories = listOf(
            createTestDirectory("1", "Movies", "/storage/movies"),
            createTestDirectory("2", "TV Shows", "/storage/tv")
        )
        directoryDao.insertDirectories(directories)
        
        val count = directoryDao.getDirectoriesCount()
        assertThat(count).isEqualTo(2)
    }
    
    @Test
    fun testGetTotalMediaCount() = runTest {
        val directories = listOf(
            createTestDirectory("1", "Movies", "/storage/movies", 10),
            createTestDirectory("2", "TV Shows", "/storage/tv", 20),
            createTestDirectory("3", "Music", "/storage/music", 30)
        )
        directoryDao.insertDirectories(directories)
        
        val totalMediaCount = directoryDao.getTotalMediaCount()
        assertThat(totalMediaCount).isEqualTo(60)
    }
    
    @Test
    fun testConcurrentOperations() = runTest {
        val directory1 = createTestDirectory("1", "Movies", "/storage/movies")
        val directory2 = createTestDirectory("2", "TV Shows", "/storage/tv")
        
        val jobs = List(10) { index ->
            kotlinx.coroutines.launch {
                if (index % 2 == 0) {
                    directoryDao.insertDirectory(directory1)
                } else {
                    directoryDao.insertDirectory(directory2)
                }
            }
        }
        jobs.forEach { it.join() }
        
        val retrieved1 = directoryDao.getDirectoryByBucketId("1")
        val retrieved2 = directoryDao.getDirectoryByBucketId("2")
        assertThat(retrieved1).isNotNull()
        assertThat(retrieved2).isNotNull()
    }
    
    private fun createTestDirectory(
        bucketId: String = "test_bucket_1",
        name: String = "Test Directory",
        path: String = "/storage/test",
        mediaCount: Int = 0
    ): DirectoryEntity {
        val now = System.currentTimeMillis()
        return DirectoryEntity(
            bucketId = bucketId,
            name = name,
            path = path,
            dateAdded = now,
            dateModified = now,
            mediaCount = mediaCount,
            totalSize = 0L,
            totalDuration = 0L
        )
    }
}
