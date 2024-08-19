package com.rainy.customview.customOkhttp

import java.io.IOException

/**
 * @author jiangshiyu
 * @date 2024/8/19
 */
class RealCall(val client: HttpClient, val request: Request) : Call {

    private var executed = true

    override fun enqueue(callBack: CallBack) {
        synchronized(this) {
            check(!executed) { "Already executed" }
            executed = true
        }

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


    //todo 责任链模式，拦截器
    @Throws(IOException::class)
    fun getResponseWithInterceptorChain():Response {
        //todo
        return Response()
    }

}