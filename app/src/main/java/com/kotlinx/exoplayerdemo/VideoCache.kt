package com.kotlinx.exoplayerdemo

import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheWriter
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import kotlinx.coroutines.*

object VideoCache {
    const val TAG = "VideoCache"
//    companion object {

//        private val cacheSize: Long = 90 * 1024 * 1024
//        private lateinit var cacheEvictor: LeastRecentlyUsedCacheEvictor
//        private lateinit var exoplayerDatabaseProvider: ExoDatabaseProvider
//        lateinit var cache: SimpleCache
//    }

    private var scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var httpDataSourceFactory: HttpDataSource.Factory = DefaultHttpDataSource.Factory()
        .setAllowCrossProtocolRedirects(true)

    var cacheDataSource: CacheDataSource.Factory = CacheDataSource.Factory()
        .setCache(VideoApp.cache)
        .setUpstreamDataSourceFactory(httpDataSourceFactory)

    private var cacheJob: Job? = null
    /**
     * @param previousVideoUrl 前一个视频缓存。
     * @param nextVideoUrl 后一个视频缓存。
     */
    fun preCachePreviousAndNextVideo(
        previousVideoUrl: String?, nextVideoUrl: String?
    ) {
        if (cacheJob?.isActive == true) cacheJob?.cancel()
        cacheJob = scope.launch {
            launch {
                previousVideoUrl?.let { pre ->
                    cacheVideo(
                        DataSpec(
                            Uri.parse(pre)
                        )
                    ) { requestLength, bytesCached, _ ->
                        val downloadPercentage: Double = (bytesCached * 100.0 / requestLength)
                        Log.d(
                            TAG,
                            "downloadPercentage $downloadPercentage previousVideoUrl: $previousVideoUrl"
                        )
                    }
                }
            }

            launch {
                nextVideoUrl?.let { next ->
                    cacheVideo(
                        DataSpec(
                            Uri.parse(next)
                        )
                    ) { requestLength, bytesCached, _ ->
                        val downloadPercentage: Double = (bytesCached * 100.0 / requestLength)
                        Log.d(
                            TAG,
                            "downloadPercentage $downloadPercentage previousVideoUrl: $previousVideoUrl"
                        )
                    }
                }
            }
        }
    }


    fun preCacheVideo(targetVideoUrlList: List<String>) {
        targetVideoUrlList.onEach { videoUrl: String ->
            scope.launch {
                cacheVideo(DataSpec(Uri.parse(videoUrl))) { requestLength, bytesCached, newBytesCached ->
                    val downloadPercentage: Double = (bytesCached * 100.0 / requestLength)
                    Log.d(TAG, "downloadPercentage $downloadPercentage videoUri: $videoUrl")
                }
            }
        }
    }

    private fun cacheVideo(
        dataSpec: DataSpec,
        progressListener: CacheWriter.ProgressListener
    ) {
        kotlin.runCatching {
            CacheWriter(
                cacheDataSource.createDataSource(),
                dataSpec,
                null,
                progressListener
            ).cache()
        }.onFailure {
            it.printStackTrace()
        }
    }
}