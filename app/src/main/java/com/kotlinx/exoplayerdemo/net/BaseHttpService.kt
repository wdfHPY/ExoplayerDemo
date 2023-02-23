package com.kotlinx.exoplayerdemo.net

import android.util.Log

abstract class BaseHttpService {

    companion object {
        const val BASE_HTTP_SERVICE_TAG = "BaseHttpService"
    }

    /**
     * 安全 NetWorkApiCall.
     * @param apiCall Api Call 函数。
     * @return 可判断成功失败的 Result。
     */
    protected suspend fun <T : Any> safeApiCall(apiCall: suspend () -> BaseResponse<T>): Result<BaseResponse<T>> {
        val response: BaseResponse<T>?
        try {
            response = apiCall.invoke()
        } catch (t: Throwable) {
            Log.i(BASE_HTTP_SERVICE_TAG, "safeApiCall: ${t.message}")
            return Result.failure(t)
        }
        return Result.success(response)
    }

    protected suspend fun <T : Any> handleExceptionSafeApiCall(
        apiCall: suspend () -> BaseResponse<T>
    ): Result<BaseResponse<T>?> = safeApiCall(apiCall).fold(
        onSuccess = {
            when (it.code) {
                0 -> Result.success(it)
                60001 -> {
                    Result.success(null)
                }
                else -> {
                    Result.success(null)
                }
            }
        }, onFailure = {
            Log.i("exceptionSafeApiCall", "handleExceptionSafeApiCall: $it")
            Result.failure(it)
        }
    )
}