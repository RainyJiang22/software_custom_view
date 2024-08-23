package com.rainy.customview.customOkhttp

/**
 * @author jiangshiyu
 * @date 2024/8/19
 */
class HttpClient internal constructor(builder: Builder) {

    //调度器
    val dispatcher: Dispatcher = builder.dispatcher

    var retryTimes: Int = builder.retryTimes

    //连接池
    val connectionPool: ConnectionPool = builder.connectionPool

    fun newCall(request: Request): Call = RealCall(this, request)

    class Builder internal constructor(
        internal var dispatcher: Dispatcher = Dispatcher(),
        internal var retryTimes: Int = 0,
        internal var connectionPool: ConnectionPool = ConnectionPool(),
    ) {

        fun dispatcher(dispatcher: Dispatcher): Builder = apply { this.dispatcher = dispatcher }

        fun setConnectionPool(connectionPool: ConnectionPool): Builder = apply {
            this.connectionPool = connectionPool
        }

        //设置重置次数
        fun setRetryTimes(retryTimes: Int): Builder = apply {
            this.retryTimes = retryTimes
        }

        fun build(): HttpClient {

            return HttpClient(this)
        }
    }
}