package dev.gokanaz.kplayer.core.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.gokanaz.kplayer.core.common.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton
import android.content.Context

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideMediaDatabase(
        @ApplicationContext context: Context,
        @ApplicationScope scope: CoroutineScope
    ): MediaDatabase {
        return MediaDatabase.getInstance(context, scope)
    }
}
