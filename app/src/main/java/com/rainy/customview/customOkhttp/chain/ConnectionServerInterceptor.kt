package com.rainy.customview.customOkhttp.chain

import android.util.Log
import com.rainy.customview.customOkhttp.Response
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import javax.net.ssl.SSLSocketFactory

/**
 * @author jiangshiyu
 * @date 2024/8/19
 * 链接服务器拦截器
 */
class ConnectionServerInterceptor : Interceptor {
    override fun intercept(realInterceptorChain: RealInterceptorChain): Response {

        val request = realInterceptorChain.request

        val host = request.url.getHost()
        val protocol = request.url.getProtocol()
        val port = request.url.getPort()
        //http，https
        val socket = if (protocol.equals("HTTP", true)) {
            Socket(host, port)
        } else {
            SSLSocketFactory.getDefault().createSocket(host, port)
        }

        //请求
        val bufferWriter = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
        val requestAll = getRequestHeaderAll(request)

        Log.d(TAG, "intercept: requestAll = \n$requestAll ")

        bufferWriter.write(requestAll)
        bufferWriter.flush()

        //响应
        val bufferReader = BufferedReader(InputStreamReader(socket.getInputStream()))

        val response = Response()

        val readLine = bufferReader.readLine()//读取首行

        val str = readLine.split(" ")
        response.statusCode = str[1].toInt()

        var readerLine = bufferReader.readLine()

        while (readerLine != null) {
            if ("" == readerLine) {     // 读到空行了，就代表下面就是 响应体了
                response.body = bufferReader.readLine()
                break
            }
            readerLine = bufferReader.readLine()
        }

        return response

    }
}