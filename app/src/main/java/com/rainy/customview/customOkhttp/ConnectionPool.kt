package com.rainy.customview.customOkhttp

import android.util.Log
import com.rainy.customview.customOkhttp.chain.TAG
import java.util.ArrayDeque
import java.util.Deque
import java.util.Objects
import java.util.concurrent.Executor
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * @author jiangshiyu
 * @date 2024/8/22
 * 与服务器之间的Socket连接池
 */
class ConnectionPool @JvmOverloads constructor(
    private val keepAliveTime: Long = 60L,
    val timeUnit: TimeUnit = TimeUnit.SECONDS,
) {


    private val httpConnections: Deque<HttpConnection> = ArrayDeque()

    private var cleanupRunning = false

    //5分钟延迟
    private var maxDuration: Long = 5 * 60 * 1000L

    private val executor: Executor = ThreadPoolExecutor(
        0, Int.MAX_VALUE, 60L, TimeUnit.SECONDS, SynchronousQueue()
    ) { runnable ->

        val thread = Thread(runnable, "This is Connection Pool")
        thread.isDaemon = true
        thread.isDaemon = true //设为守护线程
        thread
    }


    //TODO 生成一个清理线程,这个线程会定期去检查，并且清理那些无用的连接，这里的无用是指没使用的间期超过了保留时间
    val cleanupRunnable = Runnable {
        while (true) {
            val now = System.currentTimeMillis()
            val waitDuration = cleanup(now) //获取到下次检测时间
            if (waitDuration == -1L) {
                return@Runnable  //连接池为空，清理线程执行结束
            }
            if (waitDuration > 0) {
                synchronized(this@ConnectionPool) {
                    try {
                        (this@ConnectionPool as Object).wait(waitDuration)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    /**
     * 根据当前时间，清理无用的连接
     * @param now
     */
    private fun cleanup(now: Long): Long {
        var longestIdleDuration: Long = -1 //最长闲置的时间
        synchronized(this) {
            val connectionIterator =
                httpConnections.iterator()
            while (connectionIterator.hasNext()) {
                val httpConnection = connectionIterator.next() //获得连接
                //计算闲置时间
                val idleDuration = now - httpConnection.lastUseTime

                //根据闲置时间来判断是否需要被清理
                if (idleDuration > keepAliveTime) {
                    connectionIterator.remove()
                    httpConnection.close()
                    Log.e(TAG, "超过闲置时间,移出连接池")
                    continue
                }

                //然后就整个连接池中最大的闲置时间
                if (idleDuration > longestIdleDuration) {
                    longestIdleDuration = idleDuration
                }
            }
            if (longestIdleDuration >= 0) {
                return keepAliveTime - longestIdleDuration //这里返回的值，可以让清理线程知道，下一次清理要多久以后
            }

            //如果运行到这里的话，代表longestIdleDuration = -1，连接池中为空
            cleanupRunning = false
            return longestIdleDuration
        }
    }

    fun putHttpConnection(httpConnection: HttpConnection) {
        //首先判断线程池有没有在执行
        if (!cleanupRunning) {
            cleanupRunning = true
            executor.execute(cleanupRunnable)
        }
        httpConnections.add(httpConnection)
    }

    /**
     * 根据服务器地址与端口，来获取可复用的连接
     * @param host
     * @param port
     * @return
     */
    @Synchronized
    fun getHttpConnection(host: String?, port: Int): HttpConnection? {
        val httpConnectionIterator = httpConnections.iterator()
        while (httpConnectionIterator.hasNext()) {
            val httpConnection = httpConnectionIterator.next()
            if (httpConnection.isSameAddress(host, port)) {
                httpConnectionIterator.remove()
                return httpConnection
            }
        }
        return null
    }
}

