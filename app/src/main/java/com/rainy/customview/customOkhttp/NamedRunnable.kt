package com.rainy.customview.customOkhttp

/**
 * @author jiangshiyu
 * @date 2024/8/19
 */
abstract class NamedRunnable : Runnable {
    override fun run() {
        executed()
    }

    abstract fun executed()
}