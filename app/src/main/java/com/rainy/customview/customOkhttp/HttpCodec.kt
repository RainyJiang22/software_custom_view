package com.rainy.customview.customOkhttp

import android.text.TextUtils
import com.rainy.customview.customOkhttp.chain.GRGN
import com.rainy.customview.customOkhttp.chain.K
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer

/**
 * @author jiangshiyu
 * @date 2024/8/20
 */

class HttpCodec {
    private val byteBuffer: ByteBuffer = ByteBuffer.allocate(10 * 1024)

    /**
     * 拼接request数据流,写入到socket通道
     * @param os
     * @param request
     * @throws IOException
     */
    @Throws(IOException::class)
    fun writeRequest(os: OutputStream?, request: Request?) {
        val sb = StringBuffer()
        //GET /v3/weather/weatherInfo?key=064a7778b8389441e30f91b8a60c9b23&city=%25E6%25B7%25B1%25E5%259C%25B3 HTTP/1.1
        sb.append(request?.requestMethod) // GET
        sb.append(SPACE) //一个空格
        sb.append(request?.url) // "/v3/weather/weatherInfo?key=064a7778b8389441e30f91b8a60c9b23&city=%25E6%25B7%25B1%25E5%259C%25B3"
        sb.append(SPACE) //一个空格
        sb.append(HTTP_VERSION) // HTTP/1.1
        sb.append(CRLF) //一个回车换行

        //拼接请求头
        if (request?.mHeaderList?.isNotEmpty() == true){
            val map = request.mHeaderList
            map.forEach {
                sb
                    .append(it.key)
                    .append(":").append(K)
                    .append(it.value)
                    .append(GRGN)
            }
            // 拼接空行，代表下面的POST，请求体了
            sb.append(GRGN)
        }

        //拼接请求体
        val requestBody: RequestBody? = request?.requestBody
        if (null != requestBody) {
            sb.append(requestBody.getBody())
        }
        os?.write(sb.toString().toByteArray())
        os?.flush()
    }

    /**
     * 读取服务器返回回来的一行数据
     * @param is
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun readLine(`is`: InputStream): String {

        //先把byteBuffer清理一下
        byteBuffer.clear()
        //然后标记一下
        byteBuffer.mark()
        var isMaybeEofLine = false //可能为行结束的标志，当出现一个/r的时候，置为true，如果下一个是/n，就确定是行结束了
        var b: Byte
        while (`is`.read().toByte().also { b = it }.toInt() != -1) {
            byteBuffer.put(b)
            if (b.toInt() == CR) { //如果读到一个 /r
                isMaybeEofLine = true
            } else if (isMaybeEofLine) {
                if (b.toInt() == LF) { //如果读到一个 /n了，意味着，行结束了
                    val lineBytes = ByteArray(byteBuffer.position()) //new一个一行数据大小的字节数据
                    //然后重置byteBuffer
                    byteBuffer.reset() //与mark搭配使用，告诉ByteBuffer,使用者将要拿出从mark到当前保存的字节数据
                    byteBuffer[lineBytes]
                    byteBuffer.clear() //清空
                    byteBuffer.mark()
                    return String(lineBytes, Charsets.UTF_8)
                }
                //如果下一个字节不是 /n，把标志重新置为false
                isMaybeEofLine = false
            }
        }
        throw IOException("Response read line error")
    }

    /**
     * 读取服务器返回的响应头
     * @param is
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun readHeaders(`is`: InputStream?): Map<String, String> {
        val headers = HashMap<String, String>()
        while (true) {
            if(`is` == null) {
                break
            }
            val line = readLine(`is`)
            if (isEmptyLine(line)) {
                //如果读到空行 \r\n 响应头读完了
                break
            }
            val index = line.indexOf(":") //因为服务器返回的响应头中的格式也是key: value的格式
            if (index > 0) {
                val key = line.substring(0, index)
                //这里加2 是因为，value前面还有冒号和空格，所以，value的第一个位置，需要往后移

                //减2是因为line后面有/r/n两个字节
                val value = line.substring(index + 2, line.length - 2)
                headers[key] = value
            }
        }
        return headers
    }

    /**
     * 根据长度读取字节数据
     * @param is
     * @param length
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun readBytes(`is`: InputStream, length: Int): ByteArray {
        val bytes = ByteArray(length)
        var readNum = 0
        while (true) {
            readNum = `is`.read(bytes, readNum, length - readNum)
            if (readNum == length) {
                return bytes
            }
        }
    }

    /**
     * 服务器传输响应体的方式为分块方式，根据分块的方式获取响应体
     * @param is
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun readChunked(`is`: InputStream, length: Int): String {
        var length = length
        var len = -1
        var isEmptyData = false
        val chunked = StringBuffer()
        while (true) {
            if (len < 0) {
                //获取块的长度
                var line = readLine(`is`)
                length += line.length
                //去掉/r/n
                line = line.substring(0, line.length - 2)
                //获得长度 16进制字符串转成10进制整型
                len = Integer.valueOf(line, 16)

                //如果读到的是0，则再读一个/r/n就结束了
                isEmptyData = len == 0
            } else {
                length += len + 2
                val bytes = readBytes(`is`, len + 2) //读的时候，加上2，/r/n
                chunked.append(String(bytes,Charsets.UTF_8))
                len = -1
                if (isEmptyData) {
                    return chunked.toString()
                }
            }
        }
    }

    /**
     * 判断是否为空行，如果读到的是/r/n，就意味是空行
     * @param line
     * @return
     */
    private fun isEmptyLine(line: String): Boolean {
        return TextUtils.equals(line, CRLF)
    }

    companion object {
        const val CRLF = "\r\n"
        const val CR = 13 //回车的ASCII码
        const val LF = 10 //换行的ASCII码
        const val SPACE = " " //一个空格
        const val HTTP_VERSION = "HTTP/1.1" //http的版本信息
        const val COLON = ":" //冒号
        const val HEAD_HOST = "Host"
        const val HEAD_CONNECTION = "Connection"
        const val HEAD_CONTENT_TYPE = "Content-Type"
        const val HEAD_CONTENT_LENGTH = "Content-Length"
        const val HEAD_TRANSFER_ENCODING = "Transfer-Encoding"
        const val HEAD_VALUE_KEEP_ALIVE = "Keep-Alive"
        const val HEAD_VALUE_CHUNKED = "chunked"
        const val PROTOCOL_HTTPS = "https"
        const val PROTOCOL_HTTP = "http"
        const val ENCODE = "UTF-8"
    }
}
