package com.kotlinx.exoplayerdemo.core

import android.content.Context
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.kotlinx.exoplayerdemo.VideoConstants
import kotlin.math.sin

/**
 * ExoPlayer Manager.
 * 之前的对于 ViewPager2 会在onBindView去创建 Exo Player, 会在onViewAttachedToWindow的时候prepare然后播放
 * 在 onViewDetachedFromWindow 的时候停止播放。
 *
 * 1. 尝试单个ExoPlayer。 -> 结论：不可以在 onViewAttachedToWindow 创建一个Exo Player, 在 onViewDetachedFromWindow
 * 销毁刚刚创建的 Exo Player。即使在onViewDetachedFromWindow 中调用 stop 和  release方法。
 *
 * 现象：上一个视频未被停止。猜测可能是因为 stop 是异步的方法，所以未被停止。
 *      --- player?.stop()
 *      --- player?.release()
 */
object ExoPlayerManager {

    //供全局使用的一个ExoPlayer。
    var singletonExoPlayer: ExoPlayer? = null

    //初始化单例 Singleton ExoPlayer.
//    fun initSingletonExoPlayer(
//        context: Context
//    ) :ExoPlayer {
//        val loadControl: LoadControl = DefaultLoadControl.Builder()
//            .setAllocator(DefaultAllocator(true, 16))
//            .setBufferDurationsMs(
//                VideoConstants.MIN_BUFFER_DURATION,
//                VideoConstants.MAX_BUFFER_DURATION,
//                VideoConstants.MIN_PLAYBACK_START_BUFFER,
//                VideoConstants.MIN_PLAYBACK_RESUME_BUFFER
//            ).setTargetBufferBytes(-1)
//            .setPrioritizeTimeOverSizeThresholds(true).build()
//
//            return ExoPlayer.Builder(context).setLoadControl(loadControl).build()
//    }
}