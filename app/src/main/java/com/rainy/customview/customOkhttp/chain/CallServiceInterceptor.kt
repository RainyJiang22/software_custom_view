package com.rainy.customview.customOkhttp.chain

import android.util.Log
import com.rainy.customview.customOkhttp.HttpCodec
import com.rainy.customview.customOkhttp.Response
import java.io.IOException

/**
 * @author jiangshiyu
 * @date 2024/8/22
 */
class CallServiceInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(realInterceptorChain: RealInterceptorChain): Response {
        Log.e(TAG, "intercept: 通信拦截器")

        val httpConnection = realInterceptorChain.httpConnection

        val httpCodec = HttpCodec()
        val inputStream = httpConnection?.call(httpCodec)

        //获取服务器返回的响应行 HTTP/1.1 200OK\r\n
        val statusLine = inputStream?.let { httpCodec.readLine(it) }

        //读取服务器返回的响应头
        val headers = httpCodec.readHeaders(inputStream)

        val response = Response()
        var contentLength = -1
        if (headers.containsKey(HttpCodec.HEAD_CONTENT_LENGTH)) {
            contentLength = Integer.valueOf(headers[HttpCodec.HEAD_CONTENT_LENGTH].toString())
        }

        //是否为分块编码
        var isChunked = false
        if (headers.containsKey(HttpCodec.HEAD_TRANSFER_ENCODING)) {
            isChunked = headers.get(HttpCodec.HEAD_TRANSFER_ENCODING)
                .equals(HttpCodec.HEAD_VALUE_CHUNKED, true)
        }

        //获取服务器响应体
        var body = ""
        if (contentLength > 0) {
            val bodyBytes = httpCodec.readBytes(inputStream!!, contentLength)
            body = String(bodyBytes, Charsets.UTF_8)
        } else if (isChunked) {
            body = httpCodec.readChunked(inputStream!!, contentLength)
        }

        // HTTP/1.1 200 OK\r\n status[0] = "HTTP/1.1",status[1] = "200",status[2] = "OK\r\n"
        val status = statusLine?.split(" ")
        //根据响应头中的Connection的值，来判断是否能够复用连接

        //根据响应头中的Connection的值，来判断是否能够复用连接
        var isKeepAlive = false
        if (headers.containsKey(HttpCodec.HEAD_CONNECTION)) {
            isKeepAlive = headers[HttpCodec.HEAD_CONNECTION].equals(
                HttpCodec.HEAD_VALUE_KEEP_ALIVE,
                ignoreCase = true
            )
        }
        //更新此请求的最新使用时间，作用于线程池的清理工作
        httpConnection?.updateLastUseTime()

        response.statusCode = status?.get(1)?.toInt()
        response.headers = headers
        response.contentLength = contentLength
        response.isKeepAlive = isKeepAlive
        response.body = body

        return response
    }
}