package dev.gokanaz.kplayer.core.datastore.serializer

import android.content.SharedPreferences
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationPreferencesSerializer @Inject constructor() : Serializer<Preferences> {
    
    override val defaultValue: Preferences = emptyPreferences()
    
    override suspend fun readFrom(input: InputStream): Preferences {
        try {
            return withContext(Dispatchers.IO) {
                Preferences.Adapter.readFrom(input)
            }
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", exception)
        }
    }
    
    override suspend fun writeTo(t: Preferences, output: OutputStream) {
        withContext(Dispatchers.IO) {
            Preferences.Adapter.writeTo(t, output)
        }
    }
    
    suspend fun migrateFromSharedPreferences(
        sharedPreferences: SharedPreferences,
        dataStoreFile: java.io.File
    ) {
        withContext(Dispatchers.IO) {
            val prefsFile = preferencesDataStoreFile("app_preferences")
            if (!prefsFile.exists() && dataStoreFile.exists()) {
                return@withContext
            }
            
            val allPrefs = sharedPreferences.all
            if (allPrefs.isEmpty()) return@withContext
            
            val preferences = defaultValue.toMutablePreferences()
            
            allPrefs.forEach { (key, value) ->
                when (value) {
                    is String -> preferences[androidx.datastore.preferences.core.stringPreferencesKey(key)] = value
                    is Boolean -> preferences[androidx.datastore.preferences.core.booleanPreferencesKey(key)] = value
                    is Int -> preferences[androidx.datastore.preferences.core.intPreferencesKey(key)] = value
                    is Long -> preferences[androidx.datastore.preferences.core.longPreferencesKey(key)] = value
                    is Float -> preferences[androidx.datastore.preferences.core.floatPreferencesKey(key)] = value
                    is Set<*> -> {
                        @Suppress("UNCHECKED_CAST")
                        preferences[androidx.datastore.preferences.core.stringSetPreferencesKey(key)] = value as Set<String>
                    }
                }
            }
            
            writeTo(preferences.toPreferences(), dataStoreFile.outputStream())
            sharedPreferences.edit().clear().apply()
        }
    }
}
