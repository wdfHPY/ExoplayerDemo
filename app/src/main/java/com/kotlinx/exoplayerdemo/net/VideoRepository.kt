package com.kotlinx.exoplayerdemo.net

class VideoRepository : BaseHttpService() {

    private fun getService() = RetrofitServiceCreator.getInstance().create(VideoService::class.java)
//
//    fun getIsNewUser() = flow {
//        emit(getService().getIsNewUser())
//    }
//
//    fun receiveGift(ring: Int) = flow {
//        emit(getService().receiveGift(ring))
//    }


    // 视频首页列表
//    fun videoListPage(params: HashMap<String, Any>) = flow {
//        emit(getService().indexVideoListPage(params = params))
//    }

    suspend fun safeGetVideoListPage(
        params: HashMap<String, Any>,
        onFailureCb: (t: Throwable) -> Unit = { _ -> },
        onSuccessCb: (data: VideoDOT?) -> Unit = { _ -> }
    ) = handleExceptionSafeApiCall (
        apiCall = {
            getService().indexVideoListPage(params = params)
        }
    ).fold(
        onSuccess = {
            onSuccessCb(it?.data)
        }, onFailure = {
            onFailureCb.invoke(it)
        }
    )

}