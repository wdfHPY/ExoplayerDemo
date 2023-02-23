package com.kotlinx.exoplayerdemo.net

class NetConstant {

    companion object {
        // 线下
//        val DEBUG_HOST = "http://192.168.0.195:8080/"
//        val RELEASE_HOST = "http://192.168.0.195:8080/"
//
//        val H5_DEBUG_HOST = "http://192.168.0.195:8081/#/"
//        val H5_RELEASE_HOST = "http://192.168.0.195:8081/#/"

        // 内网穿透
//        private val DEBUG_HOST = "http://linli300.nat300.top/"
//        private val RELEASE_HOST = "http://linli300.nat300.top/"
//
//        private val H5_DEBUG_HOST = "http://linli300.nat300.top/#/"
//        private val H5_RELEASE_HOST = "http://linli300.nat300.top/#/"

        // 线上-正式
        val DEBUG_HOST = "https://mall.ydcp315.com/"
        val RELEASE_HOST = "https://mall.ydcp315.com/"

        val H5_DEBUG_HOST = "https://mall.ydcp315.com/app/#/"
        val H5_RELEASE_HOST = "https://mall.ydcp315.com/app/#/"

//        private val DEBUG_HOST = "http://124.71.235.235/"
//        private val RELEASE_HOST = "http://124.71.235.235/"
//
//        private val H5_DEBUG_HOST = "http://124.71.235.235/#/"
//        private val H5_RELEASE_HOST = "http://124.71.235.235/#/"
//
//        private val DEBUG_HOST = "http://121.36.11.174:8888/"
//        private val RELEASE_HOST = "http://121.36.11.174:8888/"
    }

    private val isRelease = false // 是否正式包
    var HOST = DEBUG_HOST
    var H5_HOST = H5_DEBUG_HOST

    init {

    }

} 