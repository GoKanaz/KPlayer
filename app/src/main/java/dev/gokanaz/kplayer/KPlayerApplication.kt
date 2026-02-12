package dev.gokanaz.kplayer

import android.app.Application
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import dagger.hilt.android.HiltAndroidApp
import java.io.File
import javax.inject.Inject

@HiltAndroidApp
class KPlayerApplication : Application() {
    
    companion object {
        lateinit var instance: KPlayerApplication
            private set
        
        lateinit var simpleCache: SimpleCache
            private set
    }
    
    @Inject
    lateinit var exoDatabaseProvider: ExoDatabaseProvider
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        
        initializePlayerCache()
    }
    
    private fun initializePlayerCache() {
        val cacheFile = File(cacheDir, "media")
        val cacheEvictor = LeastRecentlyUsedCacheEvictor(1024 * 1024 * 1024L)
        simpleCache = SimpleCache(cacheFile, cacheEvictor, exoDatabaseProvider)
    }
}
