package com.rainy.customview.customhandler

/**
 * @author jiangshiyu
 * @date 2024/8/16
 */

class CustomMessage {
    //消息标志
    var what = 0

    //消息内容
    var msg: Any? = null

    //Handler对象
    var target: CustomHandler? = null
}