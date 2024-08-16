package com.rainy.customview.customhandler

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

/**
 * @author jiangshiyu
 * @date 2024/8/16
 */
class CustomMessageQueue {

    //阻塞队列
    val blackQueue: BlockingQueue<CustomMessage> = ArrayBlockingQueue(50)

    fun enqueueMessage(message: CustomMessage) {
        try {
            blackQueue.put(message)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun next(): CustomMessage? {
        try {
            return blackQueue.take()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }
}