package com.kotlinx.exoplayerdemo.core

import android.content.Context
import android.util.Log
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.kotlinx.exoplayerdemo.VideoCache
import com.kotlinx.exoplayerdemo.VideoConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.sin

/**
 * ExoPlayer Manager.
 * 之前的对于 ViewPager2 会在onBindView去创建 Exo Player, 会在onViewAttachedToWindow的时候prepare然后播放
 * 在 onViewDetachedFromWindow 的时候停止播放。
 */
object ExoPlayerManager {

    private const val TAG = "ExoPlayerManager"

    private val cachedFactory = ProgressiveMediaSource.Factory(VideoCache.cacheDataSource)

    private var exoPlayerScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val innerExoPlayerMediaPlayerList: MutableStateFlow<MutableList<MediaSource>> =
        MutableStateFlow(
            mutableListOf()
        )

    val exoplayerMediaPlayerList: StateFlow<MutableList<MediaSource>> get() = innerExoPlayerMediaPlayerList

    fun initExoPlayerMediaPlayerList(videoUrlCollection: List<String>) {
        videoUrlCollection.map {
            cachedFactory
                .createMediaSource(MediaItem.fromUri(it))
        }.let {
            Log.i(TAG, "initExoPlayerMediaPlayerList: $it")
            singletonExoPlayer?.setMediaSources(it)
        }
    }


    fun appendExoplayerMediaPlayerList(videoUrlCollection: List<String>) {
        videoUrlCollection.map {
            cachedFactory
                .createMediaSource(MediaItem.fromUri(it))
        }.let {
            singletonExoPlayer?.addMediaSources(it)
        }
    }

    //供全局使用的一个ExoPlayer。
    var singletonExoPlayer: ExoPlayer? = null

    //初始化单例 Singleton ExoPlayer.
    fun initSingletonExoPlayer(
        context: Context
    ): ExoPlayer? {
        val loadControl: LoadControl = DefaultLoadControl.Builder()
            .setAllocator(DefaultAllocator(true, 16))
            .setBufferDurationsMs(
                VideoConstants.MIN_BUFFER_DURATION,
                VideoConstants.MAX_BUFFER_DURATION,
                VideoConstants.MIN_PLAYBACK_START_BUFFER,
                VideoConstants.MIN_PLAYBACK_RESUME_BUFFER
            ).setTargetBufferBytes(1024)
            .setPrioritizeTimeOverSizeThresholds(true).build()

        singletonExoPlayer = ExoPlayer.Builder(context).setLoadControl(loadControl).build().also {
            it.repeatMode = Player.REPEAT_MODE_ONE
            it.playWhenReady = true
        }
        return singletonExoPlayer
    }
}