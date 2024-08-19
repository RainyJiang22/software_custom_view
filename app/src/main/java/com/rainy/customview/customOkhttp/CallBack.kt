package com.rainy.customview.customOkhttp

import java.io.IOException

/**
 * @author jiangshiyu
 * @date 2024/8/19
 *
 * 响应结果回调
 */
interface CallBack {

    //失败
    fun onFailure(call: Call, e: IOException)

    //成功
    @Throws(IOException::class)
    fun onResponse(call: RealCall.AsyncCall, response: Response)

}