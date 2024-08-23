package com.rainy.customview.customOkhttp.chain

import android.util.Log
import com.rainy.customview.customOkhttp.Response
import java.io.IOException

/**
 * @author jiangshiyu
 * @date 2024/8/22
 * 重试拦截器
 */
class RetryInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(realInterceptorChain: RealInterceptorChain): Response {
        Log.e(TAG, "intercept: 重试拦截器")

        val call = realInterceptorChain.call
        var ioException: IOException? = null
        for (i in 0 until call.client.retryTimes) {

            if (call.isCanceled()) {
                throw IOException("this task had canceled")
            }

            ioException = try {
                return realInterceptorChain.proceed()
            } catch (e: IOException) {
                e
            }
        }
        throw ioException!!
    }
}