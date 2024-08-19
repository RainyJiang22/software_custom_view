package com.rainy.customview.customOkhttp

import java.net.URLEncoder

/**
 * @author jiangshiyu
 * @date 2024/8/19
 */
class RequestBody {

    var bodys = mutableMapOf<String, String>()

    fun addBody(key: String, value: String) {
        bodys[URLEncoder.encode(key, "utf-8")] = URLEncoder.encode(value, "utf-8")
    }


    /**
     * 请求体body信息
     */
    fun getBody(): String {
        val stringBuffer = StringBuffer()

        bodys.forEach {
            stringBuffer
                .append(it.key)
                .append("=")
                .append(it.value)
                .append("&")
        }
        if (stringBuffer.isNotEmpty()) {
            stringBuffer.deleteCharAt(stringBuffer.length - 1)
        }

        return stringBuffer.toString()
    }

}