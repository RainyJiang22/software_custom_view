package com.rainy.customview.customhandler

/**
 * @author jiangshiyu
 * @date 2024/8/16
 */

class CustomLooper private constructor() {
    val mQueue: CustomMessageQueue = CustomMessageQueue()

    companion object {
        private val sThreadLocal = ThreadLocal<CustomLooper?>()
        fun prepare() {
            if (sThreadLocal.get() != null) {
                throw RuntimeException("Only one CustomLooper may be created per thread")
            }
            sThreadLocal.set(CustomLooper())
        }

        fun myLooper(): CustomLooper? {
            return sThreadLocal.get()
        }

        fun loop() {
            //从全局ThreadLocalMap中获取唯一， looper对象
            val customLooper = myLooper()
            val mQueue = customLooper?.mQueue
            while (true) {
                val message = mQueue?.next()
                message?.target?.dispatchMessage(message)
            }
        }
    }
}