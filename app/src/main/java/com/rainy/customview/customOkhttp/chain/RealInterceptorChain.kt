package com.rainy.customview.customOkhttp.chain

import com.rainy.customview.customOkhttp.HttpConnection
import com.rainy.customview.customOkhttp.RealCall
import com.rainy.customview.customOkhttp.Request
import com.rainy.customview.customOkhttp.Response
import java.io.IOException

/**
 * @author jiangshiyu
 * @date 2024/8/19
 */
class RealInterceptorChain(
    private val interceptors: List<Interceptor>,
    private val index: Int,
    internal val request: Request,
    internal val call: RealCall,
    internal var httpConnection: HttpConnection?,
)  {
    fun getRequest(): Request = request

    fun proceed(httpConnection: HttpConnection?): Response {
        this.httpConnection = httpConnection
        return proceed()
    }

    fun proceed(): Response {
        if (index > interceptors.size) {
            throw IOException("Interceptor Chain Error")
        }
        val interceptor = interceptors[index]
        val next = RealInterceptorChain(interceptors, index + 1, request, call, httpConnection)
        return interceptor.intercept(next)
    }
}