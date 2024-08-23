package com.rainy.customview.customOkhttp

/**
 * @author jiangshiyu
 * @date 2024/8/19
 * 响应Response
 */
class Response{
    //状态码
    var statusCode: Int? = null
    //返回包的长度
    var contentLength = -1
    //返回包的头信息
    var headers:Map<String,String>?=null
    //响应结果body
    var body: String? = ""
    //是否保持连接
    var isKeepAlive =  false
}