package com.rainy.customview.customOkhttp.chain

import com.rainy.customview.customOkhttp.Request
import com.rainy.customview.customOkhttp.Response
import java.io.IOException

/**
 * @author jiangshiyu
 * @date 2024/8/19
 */
interface Interceptor {

    @Throws(IOException::class)
    fun intercept(realInterceptorChain: RealInterceptorChain): Response

    interface Chain {
        fun getRequest(): Request

        @Throws(IOException::class)
        fun getResponse(request: Request): Response
    }

}