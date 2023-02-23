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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

object VideoCache {
    const val TAG = "VideoCache"

    //视频列表的状态流。
    private val videoListSharedFlow: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    //当前的ViewPager2的下标位置状态流
    private val currentIndexStateFlow: MutableStateFlow<Int> = MutableStateFlow(0)

    private val cachedIndexStateFlow: MutableStateFlow<Int> = MutableStateFlow(0)

    private var scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var httpDataSourceFactory: HttpDataSource.Factory = DefaultHttpDataSource.Factory()
        .setAllowCrossProtocolRedirects(true)

    var cacheDataSource: CacheDataSource.Factory = CacheDataSource.Factory()
        .setCache(VideoApp.cache)
        .setUpstreamDataSourceFactory(httpDataSourceFactory)

    private var cacheVideoJob: Job? = null

    fun updateCurrentIndex(newCurrentIndex: Int) {
        scope.launch {
            currentIndexStateFlow.emit(newCurrentIndex)
        }
    }

    fun updateVideoPlayList(targetList: List<String>) {
        scope.launch {
            videoListSharedFlow.emit(targetList)
        }
    }

    fun startCacheVideoJob() {
        if (cacheVideoJob?.isActive ==  true) return
        cacheVideoJob = scope.launch {
            currentIndexStateFlow.collectLatest { currentIndex ->
                val currentVideo = videoListSharedFlow.value
                Log.i(TAG, "startCacheVideoJob: ${videoListSharedFlow.value}")
                Log.i(TAG, "startCacheVideoJob: ${cachedIndexStateFlow.value}")
                Log.i(TAG, "startCacheVideoJob: ${currentIndex}")

                if (videoListSharedFlow.value.isNotEmpty() && currentIndex >= cachedIndexStateFlow.value) {
                    val nextIndex = currentIndex + 1
                    (nextIndex .. nextIndex + 4).onEach { preCachedIndex ->
                        kotlin.runCatching {
                            currentVideo[preCachedIndex]
                        }.getOrNull()?.let { videoUrl ->
                            launch {
                                cacheVideo(
                                    DataSpec(
                                        Uri.parse(videoUrl)
                                    )
                                ) { requestLength, bytesCached, _ ->
                                    val downloadPercentage: Double = (bytesCached * 100.0 / requestLength)
                                    if (downloadPercentage == 100.0) {
                                        Log.d(
                                            TAG,
                                            "downloadPercentage $downloadPercentage previousVideoUrl: $videoUrl"
                                        )
                                    }
                                }

                            }
                        }
                    }
                    cachedIndexStateFlow.value = nextIndex + 4
                }
            }
        }
    }



    private var cacheJob: Job? = null

    /**
     * @param previousVideoUrl 前一个视频缓存。
     * @param nextVideoUrl 后一个视频缓存。
     */
    fun preCachePreviousAndNextVideo(
        nextVideoUrl: String?
    ) {
        if (cacheJob?.isActive == true) cacheJob?.cancel()
        cacheJob = scope.launch {
            launch {
                nextVideoUrl?.let { next ->
                    cacheVideo(
                        DataSpec(
                            Uri.parse(next)
                        )
                    ) { requestLength, bytesCached, _ ->
                        val downloadPercentage: Double = (bytesCached * 100.0 / requestLength)
                        if (downloadPercentage == 100.0) {
                            Log.d(
                                TAG,
                                "downloadPercentage $requestLength previousVideoUrl: $nextVideoUrl"
                            )
                        }
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
                    if (downloadPercentage == 100.0) {
                        Log.d(TAG, "downloadPercentage $downloadPercentage videoUri: $videoUrl")
                    }
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