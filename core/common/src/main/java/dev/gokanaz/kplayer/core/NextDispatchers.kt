package dev.gokanaz.kplayer.core

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import java.util.concurrent.Executors
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DatabaseDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FileIODispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MediaDispatcher

@Module
@InstallIn(SingletonComponent::class)
object NextDispatchersModule {

    @DatabaseDispatcher
    @Provides
    @Singleton
    fun provideDatabaseDispatcher(): CoroutineDispatcher =
        newSingleThreadContext("DatabaseThread").asCoroutineDispatcher()

    @FileIODispatcher
    @Provides
    @Singleton
    fun provideFileIODispatcher(): CoroutineDispatcher =
        newFixedThreadPoolContext(2, "FileIOThread").asCoroutineDispatcher()

    @MediaDispatcher
    @Provides
    @Singleton
    fun provideMediaDispatcher(): CoroutineDispatcher =
        Executors.newFixedThreadPool(3, { Thread(it, "MediaThread") })
            .asCoroutineDispatcher()
}

fun CoroutineDispatcher.close() {
    (this as? kotlinx.coroutines.ExecutorCoroutineDispatcher)?.close()
}
