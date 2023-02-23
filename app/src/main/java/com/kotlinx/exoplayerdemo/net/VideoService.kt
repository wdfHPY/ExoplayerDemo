package com.kotlinx.exoplayerdemo.net

import retrofit2.http.*

interface VideoService {

    @GET("createrapp/advertisinglist/pages")
    suspend fun indexVideoListPage(
        @QueryMap params: HashMap<String, Any>
    ): BaseResponse<VideoDOT>
}