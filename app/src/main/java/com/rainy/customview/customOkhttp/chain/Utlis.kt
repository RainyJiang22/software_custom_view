package com.rainy.customview.customOkhttp.chain

import com.rainy.customview.customOkhttp.Request
import java.net.URL
import java.util.concurrent.ThreadFactory

// 表单提交Type application/x-www-form-urlencoded
val TYPE = "application/x-www-form-urlencoded"
val K = " "
val VIERSION = "HTTP/1.1"
val GRGN = "\r\n"
val TAG = "OKHttp_Kotlin"


fun threadFactory(
    name: String,
    daemon: Boolean,
): ThreadFactory = ThreadFactory { runnable ->
    Thread(runnable, name).apply {
        isDaemon = daemon
    }
}

fun String.getProtocol() = run {
    val url = URL(this)
    url.protocol
}

/**
 * 域名
 */
fun String.getHost(): String {
    val url = URL(this)
    return url.host
}

/**
 * 端口
 */
fun String.getPort(): Int {
    val url = URL(this)
    return if (url.port == -1) url.defaultPort else url.port
}

fun getRequestHeaderAll(request_Custom_: Request): String {

    val url = URL(request_Custom_.url)

    val file = url.file

    val stringBuffer = StringBuffer()

    // TODO 拼接 请求头 的 请求行  GET /v3/weather/weatherInfo?city=110101&key=13cb58f5884f9749287abbead9c658f2 HTTP/1.1\r\n
    stringBuffer.append(request_Custom_.requestMethod)
        .append(K)
        .append(file)
        .append(K)
        .append(VIERSION)
        .append(GRGN)


    // TODO 获取请求集 进行拼接
    /**
     * Content-Length: 48\r\n
     * Host: restapi.amap.com\r\n
     * Content-Type: application/x-www-form-urlencoded\r\n
     */
    if (request_Custom_.mHeaderList.isNotEmpty()) {
        val map = request_Custom_.mHeaderList
        map.forEach {
            stringBuffer
                .append(it.key)
                .append(":").append(K)
                .append(it.value)
                .append(GRGN);
        }
        // 拼接空行，代表下面的POST，请求体了
        stringBuffer.append(GRGN)
    }


    // TODO POST请求才有 请求体的拼接
    if ("POST".equals(request_Custom_.requestMethod, ignoreCase = true)) {
        stringBuffer.append(request_Custom_.requestBody!!.getBody()).append(GRGN)
    }
    return stringBuffer.toString()
}