package com.rainy.customview.customOkhttp

import android.text.TextUtils
import com.rainy.customview.customOkhttp.chain.getHost
import com.rainy.customview.customOkhttp.chain.getPort
import com.rainy.customview.customOkhttp.chain.getProtocol
import com.rainy.customview.customOkhttp.chain.getRequestHeaderAll
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket
import javax.net.ssl.SSLSocketFactory

/**
 * @author jiangshiyu
 * @date 2024/8/20
 * 创建与服务器连接的Socket
 */
class HttpConnection {
    var socket: Socket? = null
    var lastUseTime: Long = 0
    private var request: Request? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    fun setRequest(request: Request?) {
        this.request = request
    }

    fun updateLastUseTime() {
        lastUseTime = System.currentTimeMillis()
    }

    fun isSameAddress(host: String?, port: Int): Boolean {
        return if (null == socket) {
            false
        } else TextUtils.equals(
            request?.url?.getHost(),
            host
        ) && request?.url?.getPort() == port
    }

    //创建Socket连接
    @Throws(IOException::class)
    private fun createSocket() {
        if (socket == null || socket?.isClosed == true) {
            val httpUrl = request?.url
            if (httpUrl?.getProtocol().equals(HttpCodec.PROTOCOL_HTTPS, true)) {
                //如果是https, 就需要使用jdk默认的SSLSocketFactory来创建socket
                socket = SSLSocketFactory.getDefault().createSocket()
            } else {
                socket = Socket()
            }

            socket?.connect(InetSocketAddress(httpUrl?.getHost(), httpUrl?.getPort() ?: 0))
            inputStream = socket?.getInputStream()
        }
    }


    /**
     * 关闭Socket的连接
     */
    fun close() {
        if (socket != null) {
            try {
                socket?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    fun call(httpCodec: HttpCodec): InputStream? {
        //创建socket
        createSocket()
        //发送请求
        httpCodec.writeRequest(outputStream, request)
        //返回服务器响应
        return inputStream
    }
}
