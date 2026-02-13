package dev.gokanaz.kplayer.core.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.gokanaz.kplayer.core.database.dao.DirectoryDao
import dev.gokanaz.kplayer.core.database.dao.MediumDao
import dev.gokanaz.kplayer.core.database.dao.MediumStateDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    
    @Provides
    @Singleton
    fun provideDirectoryDao(database: MediaDatabase): DirectoryDao {
        return database.directoryDao()
    }
    
    @Provides
    @Singleton
    fun provideMediumDao(database: MediaDatabase): MediumDao {
        return database.mediumDao()
    }
    
    @Provides
    @Singleton
    fun provideMediumStateDao(database: MediaDatabase): MediumStateDao {
        return database.mediumStateDao()
    }
}
