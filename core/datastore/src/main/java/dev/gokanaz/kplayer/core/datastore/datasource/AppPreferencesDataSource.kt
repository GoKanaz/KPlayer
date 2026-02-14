package dev.gokanaz.kplayer.core.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import dev.gokanaz.kplayer.core.domain.SortOrder
import dev.gokanaz.kplayer.core.domain.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

enum class DarkMode {
    SYSTEM, LIGHT, DARK
}

enum class AppLanguage {
    AUTO, ID, EN
}

enum class ViewType {
    GRID, LIST
}

@Singleton
class AppPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesDataSource {
    
    private companion object {
        val THEME_DARK_MODE = stringPreferencesKey("theme_dark_mode")
        val THEME_DYNAMIC_COLOR = booleanPreferencesKey("theme_dynamic_color")
        val THEME_FONT_SCALE = intPreferencesKey("theme_font_scale")
        
        val LANGUAGE_APP = stringPreferencesKey("language_app")
        
        val FIRST_RUN = booleanPreferencesKey("first_run")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        
        val SORT_VIDEO_TYPE = stringPreferencesKey("sort_video_type")
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val VIEW_TYPE = stringPreferencesKey("view_type")
        
        val STORAGE_PERMISSION_GRANTED = booleanPreferencesKey("storage_permission_granted")
        val STORAGE_PATHS = stringSetPreferencesKey("storage_paths")
    }

    override suspend fun <T> put(key: PreferencesKey<T>, value: T) {
        dataStore.edit { preferences ->
            when (value) {
                is String -> preferences[stringPreferencesKey(key.name)] = value
                is Boolean -> preferences[booleanPreferencesKey(key.name)] = value
                is Int -> preferences[intPreferencesKey(key.name)] = value
                is Set<*> -> preferences[stringSetPreferencesKey(key.name)] = value as Set<String>
            }
        }
    }

    override suspend fun <T> get(key: PreferencesKey<T>, defaultValue: T): T {
        val flow = observe(key, defaultValue)
        return flow.first()
    }

    override suspend fun <T> remove(key: PreferencesKey<T>) {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(key.name))
        }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    override fun <T> observe(key: PreferencesKey<T>, defaultValue: T): Flow<T> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(androidx.datastore.preferences.core.emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                @Suppress("UNCHECKED_CAST")
                when (defaultValue) {
                    is String -> preferences[stringPreferencesKey(key.name)] ?: defaultValue
                    is Boolean -> preferences[booleanPreferencesKey(key.name)] ?: defaultValue
                    is Int -> preferences[intPreferencesKey(key.name)] ?: defaultValue
                    is Set<*> -> preferences[stringSetPreferencesKey(key.name)] ?: defaultValue
                    else -> defaultValue
                } as T
            }
    }

    suspend fun setDarkMode(mode: DarkMode) {
        dataStore.edit { preferences ->
            preferences[THEME_DARK_MODE] = mode.name
        }
    }

    fun observeDarkMode(): Flow<DarkMode> {
        return dataStore.data
            .catch { emit(androidx.datastore.preferences.core.emptyPreferences()) }
            .map { preferences ->
                val value = preferences[THEME_DARK_MODE] ?: DarkMode.SYSTEM.name
                DarkMode.valueOf(value)
            }
    }

    suspend fun setDynamicColorEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_DYNAMIC_COLOR] = enabled
        }
    }

    fun observeDynamicColorEnabled(): Flow<Boolean> {
        return dataStore.data
            .catch { emit(androidx.datastore.preferences.core.emptyPreferences()) }
            .map { preferences ->
                preferences[THEME_DYNAMIC_COLOR] ?: true
            }
    }

    suspend fun setFontScale(scale: Int) {
        dataStore.edit { preferences ->
            preferences[THEME_FONT_SCALE] = scale
        }
    }

    fun observeFontScale(): Flow<Int> {
        return dataStore.data
            .catch { emit(androidx.datastore.preferences.core.emptyPreferences()) }
            .map { preferences ->
                preferences[THEME_FONT_SCALE] ?: 100
            }
    }

    suspend fun setAppLanguage(language: AppLanguage) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_APP] = language.name
        }
    }

    fun observeAppLanguage(): Flow<AppLanguage> {
        return dataStore.data
            .catch { emit(androidx.datastore.preferences.core.emptyPreferences()) }
            .map { preferences ->
                val value = preferences[LANGUAGE_APP] ?: AppLanguage.AUTO.name
                AppLanguage.valueOf(value)
            }
    }

    suspend fun setFirstRun(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[FIRST_RUN] = completed
        }
    }

    fun isFirstRun(): Flow<Boolean> {
        return dataStore.data
            .catch { emit(androidx.datastore.preferences.core.emptyPreferences()) }
            .map { preferences ->
                preferences[FIRST_RUN] ?: true
            }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = completed
        }
    }

    fun isOnboardingCompleted(): Flow<Boolean> {
        return dataStore.data
            .catch { emit(androidx.datastore.preferences.core.emptyPreferences()) }
            .map { preferences ->
                preferences[ONBOARDING_COMPLETED] ?: false
            }
    }

    suspend fun setDefaultVideoSortType(sortType: SortType) {
        dataStore.edit { preferences ->
            preferences[SORT_VIDEO_TYPE] = sortType.javaClass.simpleName
        }
    }

    fun observeDefaultVideoSortType(): Flow<SortType> {
        return dataStore.data
            .catch { emit(androidx.datastore.preferences.core.emptyPreferences()) }
            .map { preferences ->
                val value = preferences[SORT_VIDEO_TYPE] ?: SortType.Date::class.java.simpleName
                when (value) {
                    SortType.Name::class.java.simpleName -> SortType.Name
                    SortType.Date::class.java.simpleName -> SortType.Date
                    SortType.Size::class.java.simpleName -> SortType.Size
                    SortType.Duration::class.java.simpleName -> SortType.Duration
                    else -> SortType.Date
                }
            }
    }

    suspend fun setDefaultSortOrder(order: SortOrder) {
        dataStore.edit { preferences ->
            preferences[SORT_ORDER] = order.javaClass.simpleName
        }
    }

    fun observeDefaultSortOrder(): Flow<SortOrder> {
        return dataStore.data
            .catch { emit(androidx.datastore.preferences.core.emptyPreferences()) }
            .map { preferences ->
                val value = preferences[SORT_ORDER] ?: SortOrder.Descending::class.java.simpleName
                when (value) {
                    SortOrder.Ascending::class.java.simpleName -> SortOrder.Ascending
                    SortOrder.Descending::class.java.simpleName -> SortOrder.Descending
                    else -> SortOrder.Descending
                }
            }
    }

    suspend fun setDefaultViewType(viewType: ViewType) {
        dataStore.edit { preferences ->
            preferences[VIEW_TYPE] = viewType.name
        }
    }

    fun observeDefaultViewType(): Flow<ViewType> {
        return dataStore.data
            .catch { emit(androidx.datastore.preferences.core.emptyPreferences()) }
            .map { preferences ->
                val value = preferences[VIEW_TYPE] ?: ViewType.GRID.name
                ViewType.valueOf(value)
            }
    }

    suspend fun setStoragePermissionGranted(granted: Boolean) {
        dataStore.edit { preferences ->
            preferences[STORAGE_PERMISSION_GRANTED] = granted
        }
    }

    fun isStoragePermissionGranted(): Flow<Boolean> {
        return dataStore.data
            .catch { emit(androidx.datastore.preferences.core.emptyPreferences()) }
            .map { preferences ->
                preferences[STORAGE_PERMISSION_GRANTED] ?: false
            }
    }

    suspend fun addStoragePath(path: String) {
        dataStore.edit { preferences ->
            val currentPaths = preferences[STORAGE_PATHS] ?: emptySet()
            preferences[STORAGE_PATHS] = currentPaths + path
        }
    }

    suspend fun removeStoragePath(path: String) {
        dataStore.edit { preferences ->
            val currentPaths = preferences[STORAGE_PATHS] ?: emptySet()
            preferences[STORAGE_PATHS] = currentPaths - path
        }
    }

    fun observeStoragePaths(): Flow<List<String>> {
        return dataStore.data
            .catch { emit(androidx.datastore.preferences.core.emptyPreferences()) }
            .map { preferences ->
                preferences[STORAGE_PATHS]?.toList() ?: emptyList()
            }
    }
}
