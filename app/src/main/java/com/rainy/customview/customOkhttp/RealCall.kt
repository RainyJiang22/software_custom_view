package com.rainy.customview.customOkhttp

import com.rainy.customview.customOkhttp.chain.ConnectionServerInterceptor
import com.rainy.customview.customOkhttp.chain.Interceptor
import com.rainy.customview.customOkhttp.chain.RealInterceptorChain
import com.rainy.customview.customOkhttp.chain.RequestHeaderInterceptor
import java.io.IOException

/**
 * @author jiangshiyu
 * @date 2024/8/19
 */
class RealCall(val client: HttpClient, val request: Request) : Call {

    private var executed = false

    override fun enqueue(callBack: CallBack) {
        synchronized(this) {
            check(!executed) { "Already executed" }
            executed = true
        }
        client.dispatcher.enqueue(AsyncCall(callBack))
    }

    inner class AsyncCall(private val responseCallback: CallBack) : NamedRunnable() {
        override fun executed() {
            try {
                val response = getResponseWithInterceptorChain()
                responseCallback.onResponse(this, response)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                client.dispatcher.finish(this)
            }
        }

    }


    //责任链模式，拦截器
    @Throws(IOException::class)
    fun getResponseWithInterceptorChain(): Response {
        val interceptors = mutableListOf<Interceptor>()
        interceptors += RequestHeaderInterceptor()
        interceptors += ConnectionServerInterceptor()
        val chain = RealInterceptorChain(interceptors, 0, request, this@RealCall)
        return chain.getResponse(request)
    }

}