package dev.gokanaz.kplayer.core.media

import android.content.ContentResolver
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.gokanaz.kplayer.core.media.service.MediaService
import dev.gokanaz.kplayer.core.media.services.LocalMediaService
import dev.gokanaz.kplayer.core.media.sync.LocalMediaInfoSynchronizer
import dev.gokanaz.kplayer.core.media.sync.LocalMediaSynchronizer
import dev.gokanaz.kplayer.core.media.sync.MediaInfoSynchronizer
import dev.gokanaz.kplayer.core.media.sync.MediaSynchronizer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {

    @Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @Singleton
    fun provideMediaService(
        localMediaService: LocalMediaService
    ): MediaService {
        return localMediaService
    }

    @Provides
    @Singleton
    fun provideMediaSynchronizer(
        localMediaSynchronizer: LocalMediaSynchronizer
    ): MediaSynchronizer {
        return localMediaSynchronizer
    }

    @Provides
    @Singleton
    fun provideMediaInfoSynchronizer(
        localMediaInfoSynchronizer: LocalMediaInfoSynchronizer
    ): MediaInfoSynchronizer {
        return localMediaInfoSynchronizer
    }
}
