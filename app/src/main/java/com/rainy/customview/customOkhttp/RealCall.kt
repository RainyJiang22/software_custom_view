package com.rainy.customview.customOkhttp

import com.rainy.customview.customOkhttp.chain.CallServiceInterceptor
import com.rainy.customview.customOkhttp.chain.ConnectionInterceptor
import com.rainy.customview.customOkhttp.chain.ConnectionServerInterceptor
import com.rainy.customview.customOkhttp.chain.Interceptor
import com.rainy.customview.customOkhttp.chain.RealInterceptorChain
import com.rainy.customview.customOkhttp.chain.RequestHeaderInterceptor
import com.rainy.customview.customOkhttp.chain.RetryInterceptor
import com.rainy.customview.customOkhttp.chain.getHost
import java.io.IOException

/**
 * @author jiangshiyu
 * @date 2024/8/19
 */
class RealCall(val client: HttpClient, val request: Request) : Call {

    //是否被执行过
    private var executed = false

    //是否被关闭
    private var canceled = false


    fun isCanceled(): Boolean {
        return canceled
    }

    override fun enqueue(callBack: CallBack) {
        synchronized(this) {
            check(!executed) { "Already executed" }
            executed = true
        }
        client.dispatcher.enqueue(AsyncCall(callBack))
    }

    inner class AsyncCall(private val responseCallback: CallBack) : NamedRunnable() {

        fun getHost(): String {
            return request.url.getHost()
        }

        override fun executed() {
            var signalledCallback = false
            try {
                val response = getResponseWithInterceptorChain()

                if (canceled) {
                    signalledCallback = true
                    responseCallback.onFailure(this@RealCall, IOException("this task had canceled"))
                } else {
                    signalledCallback = true
                    responseCallback.onResponse(this, response)
                }
            } catch (e: IOException) {
                if (!signalledCallback) {
                    responseCallback.onFailure(this@RealCall, e)
                }
            } finally {
                //将这个任务从调度器里移除
                client.dispatcher.finish(this)
            }
        }

    }


    //责任链模式，拦截器
    @Throws(IOException::class)
    fun getResponseWithInterceptorChain(): Response {
        val interceptors = mutableListOf<Interceptor>()
        interceptors += RetryInterceptor()
        interceptors += RequestHeaderInterceptor()
        interceptors += ConnectionInterceptor()
      //  interceptors += CallServiceInterceptor()
        interceptors += ConnectionServerInterceptor()
        val chain = RealInterceptorChain(interceptors, 0, request, this@RealCall,null)
        return chain.proceed()
    }

}