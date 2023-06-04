package com.rainy.customview

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * @author jiangshiyu
 * @date 2023/6/4
 */
class AppApplication : Application() {


    companion object {

        @SuppressLint("StaticFieldLeak")
        private var sContext:Context?= null

        @JvmStatic
        fun getApplication(): Context? {
            return sContext
        }

    }

    override fun onCreate() {
        super.onCreate()
        sContext = this
    }
}