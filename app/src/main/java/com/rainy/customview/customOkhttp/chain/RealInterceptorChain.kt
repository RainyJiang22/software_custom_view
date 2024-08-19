package com.rainy.customview.customOkhttp.chain

import com.rainy.customview.customOkhttp.RealCall
import com.rainy.customview.customOkhttp.Request
import com.rainy.customview.customOkhttp.Response
import java.io.IOException
import java.lang.AssertionError

/**
 * @author jiangshiyu
 * @date 2024/8/19
 */
class RealInterceptorChain(
    private val interceptors: List<Interceptor>,
    private val index: Int,
    internal val request: Request,
    internal val call: RealCall,
) : Interceptor.Chain {
    override fun getRequest(): Request = request

    override fun getResponse(request: Request): Response {

        if (index >= interceptors.size) {
            throw AssertionError()
        }

        checkNotNull(interceptors) { IOException("interceptors is empty") }

        val interceptor = interceptors[index]
        //递归
        val realInterceptorChain = RealInterceptorChain(interceptors, index + 1, request, call)

        val response = interceptor.intercept(realInterceptorChain)

        return response
    }

}