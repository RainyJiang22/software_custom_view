package com.rainy.customview.customOkhttp.chain

import android.util.Log
import com.rainy.customview.customOkhttp.HttpCodec
import com.rainy.customview.customOkhttp.Response

/**
 * @author jiangshiyu
 * @date 2024/8/19
 * 请求头拦截器
 */
class RequestHeaderInterceptor : Interceptor {

    override fun intercept(realInterceptorChain: RealInterceptorChain): Response {

        Log.e(TAG, "intercept: Http头拦截器")

        val request = realInterceptorChain.request

        val mHeaderList = request.mHeaderList

        mHeaderList[HttpCodec.HEAD_HOST] = request.url.getHost()
        mHeaderList[HttpCodec.HEAD_CONNECTION] = HttpCodec.HEAD_VALUE_KEEP_ALIVE

        if ("POST".equals(request.requestMethod,true)){
            mHeaderList["Content-Length"] = request.requestBody?.getBody()?.length.toString()
            mHeaderList["Content-Type"] = TYPE
        }

        return realInterceptorChain.proceed()
    }
}