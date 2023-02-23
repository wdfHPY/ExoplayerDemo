package com.kotlinx.exoplayerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.viewpager2.widget.ViewPager2
import com.kotlinx.exoplayerdemo.VideoCache.preCacheVideo
import com.kotlinx.exoplayerdemo.VideoCache.startCacheVideoJob
import com.kotlinx.exoplayerdemo.VideoCache.updateVideoPlayList
import com.kotlinx.exoplayerdemo.adapter.VideoViewPager2Adapter
import com.kotlinx.exoplayerdemo.databinding.ActivityMainBinding
import com.kotlinx.exoplayerdemo.net.VideoDOT
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.kotlinx.exoplayerdemo.net.VideoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class FullVideoViewModel : ViewModel() {
    private val videoRepo: VideoRepository = VideoRepository()

    private val innnerVideoDotLiveData: MutableLiveData<VideoDOT> = MutableLiveData()

    val videoLiveData: LiveData<VideoDOT> get() = innnerVideoDotLiveData

    fun safeGetVideoListPage(
        currentPage: Int, categoryType: Int, areaName: String? = null
    ) {
        val params = HashMap<String, Any>().also {
            it["size"] = 20
            it["current"] = currentPage
            it["categoryType"] = categoryType
            if (!areaName.isNullOrEmpty()) {
                it["areaName"] = areaName
            }
        }
        viewModelScope.launch {
            videoRepo.safeGetVideoListPage(params = params, onSuccessCb = { videoDOT ->
                if (videoDOT != null)
                    innnerVideoDotLiveData.value = videoDOT
            })
        }

    }
}

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null

    private var viewModel: FullVideoViewModel? = null

    private var mCurrentIndex: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        QMUIStatusBarHelper.translucent(this)
        viewModel = ViewModelProvider(this)[FullVideoViewModel::class.java]
        _binding?.initView()

    }

    private var lastPageSelected = 0

    private var firstCachePageSelected = 0

    private var lastCachePageSelected = 0

    private fun ActivityMainBinding.initView() {
        val mAdapter = VideoViewPager2Adapter(emptyList())
        videoViewPager2.adapter = mAdapter
        videoViewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL

        videoViewPager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (lastPageSelected < position) {
                    lastCachePageSelected += 1
                    firstCachePageSelected += 1
                    val nextVideoUrl = kotlin.runCatching {
                        mAdapter.targetPlayList[lastCachePageSelected]
                    }.getOrNull()
                    VideoCache.preCachePreviousAndNextVideo(nextVideoUrl)

                    if (position == mAdapter.targetPlayList.size - 3) {
                        Log.i("wwwrrr", "onPageSelected: 加载更多")
                        val originalPlayList = mAdapter.targetPlayList.toMutableList()
                        val modifyPlayList = mAdapter.targetPlayList.toMutableList()
                        modifyPlayList.addAll(modifyPlayList.size - 1, fakeTestData())
                        updateVideoPlayList(modifyPlayList)
                        mAdapter.targetPlayList = modifyPlayList
                        mAdapter.notifyItemRangeInserted(originalPlayList.size - 1, fakeTestData().size)
                    }

                    Log.i("wwwrrr", "onPageSelected: 下移 缓存 $lastCachePageSelected")
                } else if (lastPageSelected > position){
                    lastCachePageSelected -= 1
                    firstCachePageSelected -= 1
                    val nextVideoUrl = kotlin.runCatching {
                        mAdapter.targetPlayList[firstCachePageSelected]
                    }.getOrNull()
                    VideoCache.preCachePreviousAndNextVideo(nextVideoUrl)
                    Log.i("wwwrrr", "onPageSelected: 上移 缓存 $firstCachePageSelected")
                } else {
                    Log.i("wwwrrr", "onPageSelected: 初始为0")
                    //缓存下4个
                    //上界
                    firstCachePageSelected = position
                    //下界
                    lastCachePageSelected = position + 4

                    mAdapter.targetPlayList.subList(firstCachePageSelected, lastCachePageSelected).let {
                        preCacheVideo(it)
                    }
                }
                lastPageSelected = position
//                VideoCache.updateCurrentIndex(position)
//                position + 1 .. position + 4
                Log.i("wwwrrr", "onPageSelected: $position")
//                val previousVideoUrl = kotlin.runCatching {
//                    mAdapter.targetPlayList[position + 1]
//                }.getOrNull()
//
//                val nextVideoUrl = kotlin.runCatching {
//                    mAdapter.targetPlayList[position + 2]
//                }.getOrNull()
//
//                VideoCache.preCachePreviousAndNextVideo(
//                    previousVideoUrl, nextVideoUrl
//                )
//                if (position == mAdapter.targetPlayList.size - 4) {
//                    Log.i("wwwrrr", "onPageSelected: 预加载")
//                    mCurrentIndex += 1
//                    viewModel?.safeGetVideoListPage(
//                        mCurrentIndex,
//                        categoryType = 2,
//                        areaName = null
//                    )
//                }
            }
        })

        viewModel?.videoLiveData?.observe(this@MainActivity) {
            it.records.mapNotNull { bean ->
                bean.advertisementVideo
            }.let {
                if (mCurrentIndex != 1) {
                    val originalPlayList = mAdapter.targetPlayList.toMutableList()
                    val modifyPlayList = mAdapter.targetPlayList.toMutableList()
                    modifyPlayList.addAll(modifyPlayList.size - 1, it)
                    updateVideoPlayList(modifyPlayList)
                    mAdapter.targetPlayList = modifyPlayList
                    mAdapter.notifyItemRangeInserted(originalPlayList.size - 1, it.size)
                } else {
                    updateVideoPlayList(it)
                    mAdapter.targetPlayList = it
                    mAdapter.notifyDataSetChanged()
                }

//                lifecycleScope.launch {
////                    delay(500L)
//
////                    VideoCache.preCacheVideo(it)
//                    if (mCurrentIndex != 1) {
//                        val originalPlayList = mAdapter.targetPlayList.toMutableList()
//                        val modifyPlayList = mAdapter.targetPlayList.toMutableList()
//                        modifyPlayList.addAll(modifyPlayList.size - 1 , it)
//                        mAdapter.targetPlayList = modifyPlayList
//                        mAdapter.notifyItemRangeInserted(originalPlayList.size - 1, it.size)
//                    } else {
//                        mAdapter.targetPlayList = it
//                        mAdapter.notifyDataSetChanged()
//                    }
//                }
            }
        }

//        viewModel?.safeGetVideoListPage(
//            mCurrentIndex,
//            categoryType = 2,
//            areaName = null
//        )

        updateVideoPlayList(fakeTestData())
        mAdapter.targetPlayList = fakeTestData()
        mAdapter.notifyDataSetChanged()

    }

}