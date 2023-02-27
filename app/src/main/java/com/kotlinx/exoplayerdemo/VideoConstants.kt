package com.kotlinx.exoplayerdemo

//https://www.akamai.com/blog/performance/enhancing-video-streaming-quality-for-exoplayer-part-2-exoplayers-buffering-strategy-how-to-lower
//上面文章说明了Exoplayer对于缓冲的控制。
object VideoConstants {
    // 开始加载更多数据之前的最小缓冲媒体数据量
    const val MIN_BUFFER_DURATION = 1000

    //在停止加载更多媒体数据之前应缓冲的最大媒体数据量
    const val MAX_BUFFER_DURATION = 1500

    //开始播放之前要缓冲的最小视频
    const val MIN_PLAYBACK_START_BUFFER = 250

    //重新开始播放之前，ExoPlayer 应该缓冲多少毫秒的媒体数据。
    const val MIN_PLAYBACK_RESUME_BUFFER = 1000
}