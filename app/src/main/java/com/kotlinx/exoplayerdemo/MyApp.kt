package com.kotlinx.exoplayerdemo

import android.app.Application
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.kotlinx.exoplayerdemo.core.ExoPlayerManager

class VideoApp : Application() {
    companion object{
        lateinit var cache: SimpleCache
    }

    //缓存区大小数量，这里设置缓存的大小为100MB。设置这么大是存在原因的，因为后端上传视频的时候最大的大小是20MB。
    //所以在这里默认设置缓存5个视频。
    private val cacheSize: Long = 100 * 1024 * 1024 //100MB
    private lateinit var cacheEvictor: LeastRecentlyUsedCacheEvictor
    private lateinit var exoplayerDatabaseProvider: ExoDatabaseProvider

    override fun onCreate() {
        super.onCreate()
        cacheEvictor = LeastRecentlyUsedCacheEvictor(cacheSize)
        exoplayerDatabaseProvider = ExoDatabaseProvider(this)
        cache = SimpleCache(cacheDir, cacheEvictor, exoplayerDatabaseProvider)
        ExoPlayerManager.initSingletonExoPlayer(applicationContext)
    }
}