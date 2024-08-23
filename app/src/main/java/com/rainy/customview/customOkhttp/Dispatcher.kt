package com.rainy.customview.customOkhttp

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

        if (runningAsyncCalls.size < maxRequests && runningCallsForHost(call) < maxRequestsPerHost) {
            runningAsyncCalls.add(call)
            executorService.execute(call)
        } else {
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
        if (readyAsyncCalls.size > 0) {
            val call = readyAsyncCalls.first()
            readyAsyncCalls.removeFirst()
            runningAsyncCalls.add(call)
            executorService.execute(call)
        }
    }

}