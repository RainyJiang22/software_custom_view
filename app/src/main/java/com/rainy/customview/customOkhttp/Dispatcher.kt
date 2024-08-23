package com.rainy.customview.customOkhttp

import android.util.Log
import com.rainy.customview.customOkhttp.chain.TAG
import com.rainy.customview.customOkhttp.chain.threadFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * @author jiangshiyu
 * @date 2024/8/19
 */
class Dispatcher {

    //等待双端队列
    private val readyAsyncCalls = ArrayDeque<RealCall.AsyncCall>()

    //运行中的双端队列
    private val runningAsyncCalls = ArrayDeque<RealCall.AsyncCall>()


    private val maxRequests = 64
    private val maxRequestsPerHost = 5

    private var executorServiceOrNull: ExecutorService? = null

    val executorService: ExecutorService
        get() {
            if (executorServiceOrNull == null) {
                executorServiceOrNull = ThreadPoolExecutor(
                    0, Int.MAX_VALUE, 60, TimeUnit.SECONDS,
                    SynchronousQueue(), threadFactory("自定义okhttp", false)
                )
            }
            return executorServiceOrNull!!
        }

    internal fun enqueue(call: RealCall.AsyncCall) {
        Log.e(TAG, "同时有:" + runningAsyncCalls.size)
        Log.e(TAG, "host同时有:" + runningCallsForHost(call))
        if (runningAsyncCalls.size < maxRequests && runningCallsForHost(call) < maxRequestsPerHost) {
            Log.e(TAG, "enqueue: 提交执行" )
            runningAsyncCalls.add(call)
            executorService.execute(call)
        } else {
            Log.e(TAG, "enqueue: 等待执行")
            readyAsyncCalls.add(call)
        }
    }


    /**
     * 获取正在运行队列的数量
     *
     */
    private fun runningCallsForHost(asyncCall: RealCall.AsyncCall): Int {
        var count = 0
        for (runningAsyncCall in runningAsyncCalls) {
            if (runningAsyncCall.getHost() == asyncCall.getHost()) {
                count++
            }
        }
        return count
    }

    fun finish(asyncCall: RealCall.AsyncCall) {
        synchronized(this) {
            runningAsyncCalls.remove(asyncCall)
            checkReadyCalls()
        }
//        if (readyAsyncCalls.size > 0) {
//            val call = readyAsyncCalls.first()
//            readyAsyncCalls.removeFirst()
//            runningAsyncCalls.add(call)
//            executorService.execute(call)
//        }
    }


    /**
     * 检查是否可以运行等待中的请求
     */
    private fun checkReadyCalls() {
        //达到了同时请求的最大数
        if (runningAsyncCalls.size >= maxRequests) {
            return
        }

        //没有等待执行的任务
        if (readyAsyncCalls.isEmpty()) {
            return
        }

        val asyncCallIterator: MutableIterator<RealCall.AsyncCall> = readyAsyncCalls.iterator()
        while (asyncCallIterator.hasNext()) {
            val asyncCall: RealCall.AsyncCall = asyncCallIterator.next()
            //如果获得的等待执行的任务 执行后 小于host相同最大允许数 就可以去执行
            if (runningCallsForHost(asyncCall) < maxRequestsPerHost) {
                asyncCallIterator.remove()
                runningAsyncCalls.add(asyncCall)
                executorService.execute(asyncCall)
            }
            if (runningAsyncCalls.size >= maxRequests) {
                return
            }
        }
    }
}