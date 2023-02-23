package com.kotlinx.exoplayerdemo.net

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitServiceCreator private constructor() {

    private var mRetrofit: Retrofit
    private val mTimeOut = 60L
    private val appConstant by lazy {
        NetConstant()
    }

    init {
        mRetrofit = Retrofit.Builder()
            .baseUrl(appConstant.HOST)
            .client(getOkHttpClient())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .build()
    }

    companion object {
        fun getInstance(): RetrofitServiceCreator = SingletonHolder.INSTANCE
    }

    private object SingletonHolder {
        val INSTANCE = RetrofitServiceCreator()
    }

     fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.readTimeout(mTimeOut, TimeUnit.SECONDS)
        builder.writeTimeout(mTimeOut, TimeUnit.SECONDS)
        builder.callTimeout(mTimeOut, TimeUnit.SECONDS)
        builder.connectTimeout(mTimeOut, TimeUnit.SECONDS)

        builder.retryOnConnectionFailure(true)
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(
                        HttpLoggingInterceptor.Level.BODY
                    )
            ).addInterceptor(AddParameterInterceptor())
        return builder.build()
    }


    fun <T> create(service: Class<T>): T = mRetrofit.create(service)
}