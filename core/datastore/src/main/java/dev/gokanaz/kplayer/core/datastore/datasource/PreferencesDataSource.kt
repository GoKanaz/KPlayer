package dev.gokanaz.kplayer.core.datastore.datasource

import kotlinx.coroutines.flow.Flow

data class PreferencesKey<T>(
    val name: String,
    val defaultValue: T
)

interface PreferencesDataSource {
    suspend fun <T> put(key: PreferencesKey<T>, value: T)
    suspend fun <T> get(key: PreferencesKey<T>, defaultValue: T): T
    suspend fun <T> remove(key: PreferencesKey<T>)
    suspend fun clear()
    fun <T> observe(key: PreferencesKey<T>, defaultValue: T): Flow<T>
}
