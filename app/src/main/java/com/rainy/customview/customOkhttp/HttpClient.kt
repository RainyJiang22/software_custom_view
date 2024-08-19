package com.rainy.customview.customOkhttp

import android.os.Build

/**
 * @author jiangshiyu
 * @date 2024/8/19
 */
class HttpClient internal constructor(builder: Builder) {


    //调度器
    val dispatcher: Dispatcher = builder.dispatcher

    fun newCall(request: Request): Call = RealCall(this, request)

    class Builder internal constructor(
        internal var dispatcher: Dispatcher = Dispatcher(),
    ) {

        fun dispatcher(dispatcher: Dispatcher): Builder = apply { this.dispatcher = dispatcher }

        fun build(): HttpClient {

            return HttpClient(this)
        }
    }
}