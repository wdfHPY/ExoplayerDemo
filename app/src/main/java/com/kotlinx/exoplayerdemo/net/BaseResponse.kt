package com.kotlinx.exoplayerdemo.net


data class BaseResponse<T>(
    val code: Int,
    val message: String?,
    val data: T?
)

