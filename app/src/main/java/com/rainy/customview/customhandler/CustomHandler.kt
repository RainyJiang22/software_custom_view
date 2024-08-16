package com.rainy.customview.customhandler

/**
 * @author jiangshiyu
 * @date 2024/8/16
 */


abstract class CustomHandler {
    private val mLooper = CustomLooper.myLooper()
    private val mMessageQueue: CustomMessageQueue

    init {
        if (mLooper == null) {
            throw RuntimeException(
                "Can't create handler inside thread " + Thread.currentThread()
                        + " that has not called Looper.prepare()"
            )
        }
        mMessageQueue = mLooper.mQueue
    }

    abstract fun handleMessage(msg: CustomMessage?)


    fun sendMessage(message: CustomMessage) {
        //将消息放入消息队列
        enqueueMessage(message)
    }

    private fun enqueueMessage(message: CustomMessage) {
        //赋值当前消息
        message.target = this
        //使用mMessageQueue，将消息传入
        mMessageQueue.enqueueMessage(message)
    }

    fun dispatchMessage(message: CustomMessage?) {
        handleMessage(message)
    }
}