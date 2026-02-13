package dev.gokanaz.kplayer.core.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.gokanaz.kplayer.core.data.repository.LocalMediaRepository
import dev.gokanaz.kplayer.core.data.repository.LocalPreferencesRepository
import dev.gokanaz.kplayer.core.data.repository.MediaRepository
import dev.gokanaz.kplayer.core.data.repository.PreferencesRepository
import dev.gokanaz.kplayer.core.data.repository.fake.FakeMediaRepository
import dev.gokanaz.kplayer.core.data.repository.fake.FakePreferencesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    
    @Binds
    @Singleton
    abstract fun bindMediaRepository(
        localMediaRepository: LocalMediaRepository
    ): MediaRepository
    
    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        localPreferencesRepository: LocalPreferencesRepository
    ): PreferencesRepository
    
    @Binds
    @Singleton
    abstract fun bindFakeMediaRepository(
        fakeMediaRepository: FakeMediaRepository
    ): MediaRepository
    
    @Binds
    @Singleton
    abstract fun bindFakePreferencesRepository(
        fakePreferencesRepository: FakePreferencesRepository
    ): PreferencesRepository
}
