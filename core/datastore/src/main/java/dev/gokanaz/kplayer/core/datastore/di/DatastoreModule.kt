package dev.gokanaz.kplayer.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.gokanaz.kplayer.core.datastore.datasource.AppPreferencesDataSource
import dev.gokanaz.kplayer.core.datastore.datasource.PlayerPreferencesDataSource
import dev.gokanaz.kplayer.core.datastore.proto.PlayerSettingsProto
import dev.gokanaz.kplayer.core.datastore.serializer.ApplicationPreferencesSerializer
import dev.gokanaz.kplayer.core.datastore.serializer.PlayerPreferencesSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {
    
    private const val APP_PREFERENCES_FILE_NAME = "app_preferences.pb"
    private const val PLAYER_PREFERENCES_FILE_NAME = "player_preferences.pb"
    
    @Provides
    @Singleton
    fun provideAppPreferencesDataStore(
        @ApplicationContext context: Context,
        serializer: ApplicationPreferencesSerializer
    ): DataStore<Preferences> {
        return DataStoreFactory.create(
            serializer = serializer,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(APP_PREFERENCES_FILE_NAME) }
        )
    }
    
    @Provides
    @Singleton
    fun providePlayerPreferencesDataStore(
        @ApplicationContext context: Context,
        serializer: PlayerPreferencesSerializer
    ): DataStore<PlayerSettingsProto> {
        return DataStoreFactory.create(
            serializer = serializer,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.dataStoreFile(PLAYER_PREFERENCES_FILE_NAME) }
        )
    }
    
    @Provides
    @Singleton
    fun provideApplicationPreferencesSerializer(): ApplicationPreferencesSerializer {
        return ApplicationPreferencesSerializer()
    }
    
    @Provides
    @Singleton
    fun providePlayerPreferencesSerializer(): PlayerPreferencesSerializer {
        return PlayerPreferencesSerializer()
    }
    
    @Provides
    @Singleton
    fun provideAppPreferencesDataSource(
        dataStore: DataStore<Preferences>
    ): AppPreferencesDataSource {
        return AppPreferencesDataSource(dataStore)
    }
    
    @Provides
    @Singleton
    fun providePlayerPreferencesDataSource(
        dataStore: DataStore<PlayerSettingsProto>
    ): PlayerPreferencesDataSource {
        return PlayerPreferencesDataSource(dataStore)
    }
}
