package com.rainy.customview.customOkhttp.chain

import com.rainy.customview.customOkhttp.Response

/**
 * @author jiangshiyu
 * @date 2024/8/19
 * 请求头拦截器
 */
class RequestHeaderInterceptor : Interceptor {

    override fun intercept(realInterceptorChain: RealInterceptorChain): Response {
        val request = realInterceptorChain.request

        val mHeaderList = request.mHeaderList

        mHeaderList["Host"] = getHost(request)
        if ("POST".equals(request.requestMethod,true)){
            mHeaderList["Content-Length"] = request.requestBody?.getBody()?.length.toString()
            mHeaderList["Content-Type"] = TYPE
        }

        return realInterceptorChain.getResponse(request)
    }
}