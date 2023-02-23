package com.kotlinx.exoplayerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.viewpager2.widget.ViewPager2
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
            it["size"] = 10
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
        viewModel?.safeGetVideoListPage(
            mCurrentIndex,
            categoryType = 2,
            areaName = null
        )
    }



    private fun ActivityMainBinding.initView() {

        lifecycleScope.launchWhenResumed {
            kotlin.runCatching {
                withTimeout(10000) {
                    delay(30000)
                }
            }.exceptionOrNull()?.let {
                Log.i("PublishVideoActivity", "initData: $it")
            }
        }


        val mAdapter = VideoViewPager2Adapter(emptyList())
        videoViewPager2.adapter = mAdapter
        videoViewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL

        videoViewPager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.i("wwwrrr", "onPageSelected: $position")
                val previousVideoUrl = kotlin.runCatching {
                    mAdapter.targetPlayList[position + 1]
                }.getOrNull()

                val nextVideoUrl = kotlin.runCatching {
                    mAdapter.targetPlayList[position + 2]
                }.getOrNull()

                VideoCache.preCachePreviousAndNextVideo(
                    previousVideoUrl, nextVideoUrl
                )
                if (position == mAdapter.targetPlayList.size - 4) {
                    Log.i("wwwrrr", "onPageSelected: 预加载")
                    mCurrentIndex += 1
                    viewModel?.safeGetVideoListPage(
                        mCurrentIndex,
                        categoryType = 2,
                        areaName = null
                    )
                }
            }
        })

        viewModel?.videoLiveData?.observe(this@MainActivity) {
            it.records.mapNotNull { bean ->
                bean.advertisementVideo
            }.let {

                lifecycleScope.launch {
//                    delay(500L)

//                    VideoCache.preCacheVideo(it)
                    if (mCurrentIndex != 1) {
                        val originalPlayList = mAdapter.targetPlayList.toMutableList()
                        val modifyPlayList = mAdapter.targetPlayList.toMutableList()
                        modifyPlayList.addAll(modifyPlayList.size - 1 , it)
                        mAdapter.targetPlayList = modifyPlayList
                        mAdapter.notifyItemRangeInserted(originalPlayList.size - 1, it.size)
                    } else {
                        mAdapter.targetPlayList = it
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

}