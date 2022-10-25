package com.rainy.customview.base

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<V : ViewBinding, VM : AndroidViewModel> : AppCompatActivity() {


    var binding: V? = null

    val viewModel: VM by lazy {
        val types = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        val clazz = types[1] as Class<VM>
        ViewModelProvider(
            viewModelStore,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(clazz)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //如果是8.0系统的手机，并且认为是透明主题的Activity
        if (Build.VERSION.SDK_INT == 26 && this.isTranslucentOrFloating()) {
            //通过反射取消方向的设置，这样绕开系统的检查，避免闪退
            val result: Boolean = this.fixOrientation()
        }
        super.onCreate(savedInstanceState)
        initContentView()
        intent.apply {
            extras?.apply {
                onBundle(this)
            }
        }
        createObserver()
        init(savedInstanceState)
    }

    override fun setContentView(layoutResID: Int) {
    }

    private fun initContentView() {
        val types = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        val aClass = types[0] as Class<V>
        try {
            binding =
                aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
                    .invoke(null, layoutInflater) as V?
            super.setContentView(binding?.root)
        } catch (e: Error) {
            e.printStackTrace();
        }
    }

    abstract fun onBundle(bundle: Bundle)

    abstract fun init(savedInstanceState: Bundle?)

    /**
     * 创建LiveData数据观察者
     */
    abstract fun createObserver()

    //通过反射判断是否是透明页面
    open fun isTranslucentOrFloating(): Boolean {
        var isTranslucentOrFloating = false
        try {
            val styleableRes = Class.forName("com.android.internal.R\$styleable")
                .getField("Window")[null] as IntArray
            val ta = obtainStyledAttributes(styleableRes)
            val m =
                ActivityInfo::class.java.getMethod(
                    "isTranslucentOrFloating",
                    TypedArray::class.java
                )
            m.isAccessible = true
            isTranslucentOrFloating = m.invoke(null, ta) as Boolean
            m.isAccessible = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isTranslucentOrFloating
    }

    //通过反射将方向设置为 SCREEN_ORIENTATION_UNSPECIFIED，绕开系统的检查
    open fun fixOrientation(): Boolean {
        try {
            val field = Activity::class.java.getDeclaredField("mActivityInfo")
            field.isAccessible = true
            val o = field[this] as ActivityInfo
            o.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            field.isAccessible = false
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}
