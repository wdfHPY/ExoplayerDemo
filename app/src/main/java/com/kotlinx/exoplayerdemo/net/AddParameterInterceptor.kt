package com.kotlinx.exoplayerdemo.net

import android.text.TextUtils
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AddParameterInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //获取原先的请求
        val originalRequest: Request = chain.request()
        val requestBuilder = originalRequest.newBuilder()
        val url = originalRequest.url.toString()
        requestBuilder.addHeader("tenant-id", "1")
        requestBuilder.addHeader("client-type", "APP")
        //        GlobalConstant instance = GlobalConstant.getInstance();
        val token = "\$2a\$10\$JUYZSrJsRD.71JnMJ3yPruq53ulyTXajlfRv0lPI1ryMHfMZC5daW"
        //添加/替换统一头部token
        Log.i("token==", token)
        if (!TextUtils.isEmpty(token)) {
            requestBuilder.addHeader("third-session", token)
            requestBuilder.addHeader("token", token)
        }
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}