package com.kotlinx.exoplayerdemo

object VideoConstants {
    const val MIN_BUFFER_DURATION = 2000

    //播放期间要缓冲的最大视频
    const val MAX_BUFFER_DURATION = 5000

    //开始播放之前要缓冲的最小视频
    const val MIN_PLAYBACK_START_BUFFER = 1500

    //Min video 你想在用户恢复视频时缓冲
    const val MIN_PLAYBACK_RESUME_BUFFER = 2000
}