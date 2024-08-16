package com.rainy.customview

import com.rainy.customview.customhandler.CustomHandler
import com.rainy.customview.customhandler.CustomLooper
import com.rainy.customview.customhandler.CustomMessage
import org.junit.Test

import org.junit.Assert.*
import java.util.logging.Handler
import kotlin.concurrent.thread

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testCustomHandler() {
        //子线程存，子线程取
        Thread({
            val mThread_A = ThreadLocal<String>()
            mThread_A.set("thread-1")
            println("mThread_A:${mThread_A.get()}")
        }, "thread-1").start()

        //主线程存，子线程取
        val mThread_B = ThreadLocal<String>()
        mThread_B.set("thread_B")
        Thread {
            println("mThread_A ${mThread_B.get()}")
        }.start()

        //主线程存，主线程取
        val mThread_C = ThreadLocal<String>()
        mThread_C.set("thread_C")
        println("mThread_C ${mThread_C.get()}")

        CustomLooper.prepare()

        val customHandler = object : CustomHandler() {
            override fun handleMessage(msg: CustomMessage?) {
                val result = msg?.msg
                println("收到信息 $result")
            }
        }
        val customMessage = CustomMessage()
        customMessage.msg = "哈哈，简易Handler 架构实现"
        customHandler.sendMessage(customMessage)
        CustomLooper.loop()
    }
}