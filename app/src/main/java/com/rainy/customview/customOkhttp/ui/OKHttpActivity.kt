package com.rainy.customview.customOkhttp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.rainy.customview.R
import com.rainy.customview.customOkhttp.Call
import com.rainy.customview.customOkhttp.CallBack
import com.rainy.customview.customOkhttp.HttpClient
import com.rainy.customview.customOkhttp.RealCall
import com.rainy.customview.customOkhttp.Request
import com.rainy.customview.customOkhttp.RequestBody
import com.rainy.customview.customOkhttp.Response
import com.rainy.customview.customOkhttp.chain.TAG
import java.io.IOException

/**
 * @author jiangshiyu
 * @date 2024/8/19
 */
class OKHttpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actvitiy_okhttp)

//        getRequest()
//        getRequest2()
//        postRequest()
        postRequest2()
    }


    private fun getRequest() {
        val okHttpClient = HttpClient.Builder().build()

        val request = Request.Builder()
            .url("http://restapi.amap.com/v3/weather/weatherInfo?city=110101&key=13cb58f5884f9749287abbead9c658f2")
            .build()

        val call = okHttpClient.newCall(request)

        call.enqueue(object : CallBack {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "onFailure: e = \n$e")
            }

            override fun onResponse(call: RealCall.AsyncCall, response: Response) {
                Log.d(TAG, "onResponse: response = \n${response.body}")
            }

        })
    }

    private fun getRequest2() {
        val okHttpClient = HttpClient.Builder().setRetryTimes(3).build()

        val request =
            Request.Builder().url("http://www.kuaidi100.com/query?type=yuantong&postid=222222222")
                .build()

        val call = okHttpClient.newCall(request)

        call.enqueue(object : CallBack {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "onFailure: e = \n$e")
            }

            override fun onResponse(call: RealCall.AsyncCall, response: Response) {
                Log.d(TAG, "onResponse: response = \n${response.body}")
            }

        })
    }

    private fun postRequest() {
        val requestBody = RequestBody()
        requestBody.apply {
            addBody("city", "110101")
            addBody("key", "13cb58f5884f9749287abbead9c658f2")
        }
        val client = HttpClient.Builder().build()

        val request = Request.Builder().post(requestBody)
            .url("http://restapi.amap.com/v3/weather/weatherInfo").build()

        val call = client.newCall(request)
        call.enqueue(object : CallBack {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "\n onFailure: e = \n$e")
            }

            override fun onResponse(call: RealCall.AsyncCall, response: Response) {
                Log.d(TAG, "onResponse: response = \n${response.body}")
            }
        })

    }

    private fun postRequest2() {
        val requestBody: RequestBody = RequestBody().apply {
            addBody("city", "深圳")
            addBody("key", "064a7778b8389441e30f91b8a60c9b23")
        }


        val client = HttpClient.Builder().build()

        val request = Request.Builder().post(requestBody)
            .url("http://restapi.amap.com/v3/weather/weatherInfo").build()

        val call = client.newCall(request)
        call.enqueue(object : CallBack {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "\n onFailure: e = \n$e")
            }

            override fun onResponse(call: RealCall.AsyncCall, response: Response) {
                Log.d(TAG, "onResponse: response = \n${response.body}")
            }
        })
    }
}