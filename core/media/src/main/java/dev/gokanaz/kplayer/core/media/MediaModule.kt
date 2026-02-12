package dev.gokanaz.kplayer.core.media

import android.content.ContentResolver
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    @Singleton
    fun provideContentResolver(
        @ApplicationContext context: Context
    ): ContentResolver {
        return context.contentResolver
    }
    
    @Provides
    @Singleton
    fun provideLocalMediaService(
        contentResolver: ContentResolver
    ): LocalMediaService {
        return LocalMediaService(contentResolver)
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
    
    @Provides
    @Singleton
    fun provideExoPlayer(
        @ApplicationContext context: Context
    ): Player {
        return ExoPlayer.Builder(context).build()
    }
    
    @Provides
    @Singleton
    fun provideExoDatabaseProvider(
        @ApplicationContext context: Context
    ): ExoDatabaseProvider {
        return ExoDatabaseProvider(context)
    }
}
