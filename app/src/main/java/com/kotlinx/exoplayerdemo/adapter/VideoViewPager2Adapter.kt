package com.kotlinx.exoplayerdemo.adapter

import android.content.Context
import android.graphics.Point
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.REPEAT_MODE_ALL
import com.google.android.exoplayer2.Player.REPEAT_MODE_ONE
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.kotlinx.exoplayerdemo.VideoCache
import com.kotlinx.exoplayerdemo.VideoConstants.MAX_BUFFER_DURATION
import com.kotlinx.exoplayerdemo.VideoConstants.MIN_BUFFER_DURATION
import com.kotlinx.exoplayerdemo.VideoConstants.MIN_PLAYBACK_RESUME_BUFFER
import com.kotlinx.exoplayerdemo.VideoConstants.MIN_PLAYBACK_START_BUFFER
import com.kotlinx.exoplayerdemo.core.ExoPlayerManager
import com.kotlinx.exoplayerdemo.databinding.ItemVideoItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


class VideoViewPager2Adapter(
    var targetPlayList: List<String>
) : RecyclerView.Adapter<VideoViewPager2Adapter.VideoViewHolder>() {

    companion object {
        const val TAG = "VideoViewPager2Adapter"
    }

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

//    private val mediaUrlSparseArray = SparseArray<String>()

    private val mStoredVideoPlayers: SparseArray<ExoPlayer> = SparseArray<ExoPlayer>()

    private val mStoredVideoView: SparseArray<StyledPlayerView> = SparseArray<StyledPlayerView>()


    /**
     * 创建一个VideoPlayer。
     * @param context View的上下文。
     */
    private fun createVideoPlayer(context: Context): ExoPlayer {
        val loadControl: LoadControl = DefaultLoadControl.Builder()
            .setAllocator(DefaultAllocator(true, 16))
            .setBufferDurationsMs(
                MIN_BUFFER_DURATION,
                MAX_BUFFER_DURATION,
                MIN_PLAYBACK_START_BUFFER,
                MIN_PLAYBACK_RESUME_BUFFER
            ).setTargetBufferBytes(1024)
            .setPrioritizeTimeOverSizeThresholds(true).build()
        val renderersFactory = DefaultRenderersFactory(context)
            .forceEnableMediaCodecAsynchronousQueueing()
        return ExoPlayer.Builder(context, renderersFactory).setLoadControl(loadControl).build()
    }

    class VideoViewHolder(
        val binding: ItemVideoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var videoSurfaceDefaultHeight = 0
        private var screenDefaultHeight = 0
        private lateinit var videoPlayerView: StyledPlayerView

        fun getVideoSource(mContext: Context): StyledPlayerView {
            val display =
                (mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            val point = Point()
            display.getSize(point)

            videoSurfaceDefaultHeight = point.x
            screenDefaultHeight = point.y
            videoPlayerView = StyledPlayerView(mContext)
            videoPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
            videoPlayerView.useController = false
            videoPlayerView.setShowBuffering(StyledPlayerView.SHOW_BUFFERING_WHEN_PLAYING)
            videoPlayerView.setOnClickListener {
                videoPlayerView.player?.let {
                    if (it.isPlaying)
                        it.pause()
                    else
                        it.playWhenReady = true
                }
                Log.i(TAG, "getVideoSource: click!!!")
            }
            return videoPlayerView
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            ItemVideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
//        mediaUrlSparseArray.append(position, targetPlayList[position])
//        mStoredVideoPlayers.append(position, createVideoPlayer(holder.binding.root.context))
        mStoredVideoView.append(position, holder.getVideoSource(holder.binding.root.context))
    }

    override fun getItemCount(): Int {
        return targetPlayList.size
    }

    override fun onViewAttachedToWindow(holder: VideoViewHolder) {
        super.onViewAttachedToWindow(holder)
        val adapterPosition = holder.absoluteAdapterPosition

        val mediaSource: MediaSource = ProgressiveMediaSource
            .Factory(VideoCache.cacheDataSource)
            .createMediaSource(MediaItem.fromUri(targetPlayList[adapterPosition]))

        val playerView = mStoredVideoView.get(adapterPosition)
        val player: ExoPlayer? = ExoPlayerManager.singletonExoPlayer

        player?.repeatMode = REPEAT_MODE_ONE
        player?.setMediaSource(mediaSource, false)
        player?.prepare()
        player?.playWhenReady = true
//        val video = playerView.videoSurfaceView
        playerView.player = player
        holder.binding.frameLl.addView(playerView)
    }

    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val adapterPosition = holder.absoluteAdapterPosition
        val indexOfChild =
            holder.binding.frameLl.indexOfChild(mStoredVideoView.get(adapterPosition))
        if (indexOfChild >= 0) {
            holder.binding.frameLl.removeViewAt(indexOfChild)
            mStoredVideoView.get(adapterPosition)?.let { styledPlayerView ->
//                styledPlayerView.player?.stop()
//                styledPlayerView.player?.clearMediaItems()
//                styledPlayerView.player?.clearVideoSurface()
                styledPlayerView.player = null
            }
//            mStoredVideoPlayers.get(adapterPosition).stop()
////            mStoredVideoPlayers.get(adapterPosition).m
//            Log.i(TAG, "onViewDetachedFromWindow: 、${mStoredVideoPlayers.get(adapterPosition).bufferedPercentage}")
//            mStoredVideoView.get(adapterPosition).player = null
//            mStoredVideoPlayers.get(adapterPosition).clearMediaItems()
//            mStoredVideoPlayers.get(adapterPosition).clearAuxEffectInfo()
        }
    }
}