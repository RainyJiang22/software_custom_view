package com.rainy.customview.customOkhttp.chain

import com.rainy.customview.customOkhttp.Response

/**
 * @author jiangshiyu
 * @date 2024/8/19
 * 重试拦截器
 */
class RetryInterceptor : Interceptor {
    override fun intercept(realInterceptorChain: RealInterceptorChain): Response {
        val request = realInterceptorChain.request

        val call = realInterceptorChain.call

        for (i in 0 until call.client.retryTimes) {
            try {
                return realInterceptorChain.getResponse(request)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return Response()
    }
}