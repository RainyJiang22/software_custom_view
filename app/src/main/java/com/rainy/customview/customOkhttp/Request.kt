package com.rainy.customview.customOkhttp

/**
 * @author jiangshiyu
 * @date 2024/8/19
 * 请求，建造者模式
 */
class Request internal constructor(
    val url: String,
    val mHeaderList: MutableMap<String, String>,
    val requestMethod: String,
    val requestBody: RequestBody?,
) {

    class Builder {
        private var url: String? = null
        private var mHeaderList = mutableMapOf<String, String>()
        private var requestMethod = "GET"
        private var requestBody: RequestBody? = null


        //请求链接
        fun url(url:String) = apply {
            this.url = url
        }

        //请求头
        fun addHeader(key: String, value: String) = apply {
            this.mHeaderList[key] = value
        }

        //post请求，请求body
        fun post(requestBody: RequestBody) = apply {
            this.requestMethod = "POST"
            this.requestBody = requestBody
        }

        fun build(): Request {
            return Request(
                checkNotNull(url) { "url == null" },
                mHeaderList,
                requestMethod,
                requestBody
            )
        }
    }

}