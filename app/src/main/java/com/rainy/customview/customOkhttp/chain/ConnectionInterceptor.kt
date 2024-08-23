package com.rainy.customview.customOkhttp.chain

import android.util.Log
import com.rainy.customview.customOkhttp.HttpConnection
import com.rainy.customview.customOkhttp.Response
import java.io.IOException

/**
 * @author jiangshiyu
 * @date 2024/8/22
 */
class ConnectionInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(realInterceptorChain: RealInterceptorChain): Response {
        Log.e(TAG, "intercept: 获取连接拦截器")

        val request = realInterceptorChain.request
        val client = realInterceptorChain.call.client
        val httpUrl = realInterceptorChain.request.url

        var httpConnection = client.connectionPool.getHttpConnection(httpUrl.getHost(),httpUrl.getPort())
        if (httpConnection == null) {
            httpConnection = HttpConnection()
        } else {
            Log.e(TAG, "intercept:从连接池中获得连接")
        }
        httpConnection.setRequest(request)

        try {
            val response = realInterceptorChain.proceed(httpConnection)
            if (response.isKeepAlive) {
                client.connectionPool.putHttpConnection(httpConnection)
            } else {
                httpConnection.close()
            }
            return response
        }catch (e:IOException) {
            httpConnection.close()
            throw e
        }
    }
}