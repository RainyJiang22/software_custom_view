package com.rainy.doubleindicatorseekbar

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import android.view.ViewConfiguration
import java.lang.Exception
import java.lang.reflect.Field

/**
 * 屏幕 分辨率适配 工具
 */
object ScreenUtil {
    /**
     * @return
     */
    @get:Deprecated(
        """might no init, use {@link #getScreenWidth(Context)} instead
	  """
    )
    var screenWidth = 0
        private set

    /**
     * @return
     */
    @get:Deprecated(
        """might no init, use {@link #getScreenHeight(Context)} instead
	  """
    )
    var screenHeight = 0
        private set
    private var sScale = 0f
    private var statusBarHeight = 0
    private var sScaledTouchSlop = 0

    /**
     * 获取手机的屏幕的密度
     *
     * @param context
     */
    fun init(context: Context?) {
        if (context != null) {
            val displayMetrics = context.resources
                .displayMetrics
            sScale = displayMetrics.density
            screenWidth = displayMetrics.widthPixels
            screenHeight = displayMetrics.heightPixels
        }
    }

    /**
     *
     * @param context
     * @return 屏幕宽带（不含按钮）
     */
    fun getScreenWidth(context: Context?): Int {
        if (screenWidth <= 0) {
            init(context)
        }
        return screenWidth
    }

    /**
     * @see .getRealHeight
     * @param context
     * @return 屏幕高度（不含按钮的高度）
     */
    fun getScreenHeight(context: Context?): Int {
        if (screenHeight <= 0) {
            init(context)
        }
        return screenHeight
    }

    fun getRealWidth(context: Context?): Int {
        return getScreenWidth(context)
    }

    /**
     * 获取屏幕高度（含按钮）
     *
     * @param context
     * @return
     */
    fun getRealHeight(context: Context?): Int {
        if (sReadHeight <= 0) {
            initRealSize(context)
        }
        return sReadHeight
    }

    fun getNavBarHeight(context: Context?): Int {
        if (sNavBarHeight < 0) {
            initRealSize(context)
        }
        return sNavBarHeight
    }

    private var sReadHeight = 0
    private var sNavBarHeight = -1
    fun initRealSize(context: Context?) {
        if (context != null) {
            val wm = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            try {
                val disPlayClass = Class.forName("android.view.Display")
                val realSize = Point()
                val method = disPlayClass.getMethod("getRealSize", Point::class.java)
                method.invoke(display, realSize)
                sReadHeight = realSize.y
            } catch (e: Exception) {
                sReadHeight = getScreenHeight(context)
            }
            sNavBarHeight = sReadHeight - getScreenHeight(context)
        }
    }

    /**
     * @param dipValue
     * @return
     */
    @Deprecated("""might no init, use {@link #dip2px(Context, float)} instead""")
    fun dip2px(dipValue: Float): Int {
        return Math.round(dipValue * sScale)
    }

    /**
     * @param dipValue
     * @return
     */
    @JvmStatic
    fun dip2px(context: Context?, dipValue: Float): Int {
        if (sScale <= 0) {
            init(context)
        }
        return Math.round(dipValue * sScale)
    }

    /**
     * sp 转 px
     *
     * @param spValue
     * sp大小
     * @return 像素值
     */
    @JvmStatic
    fun sp2px(context: Context?, spValue: Float): Int {
        if (sScale <= 0) {
            init(context)
        }
        return (sScale * spValue).toInt()
    }

    fun dip2pxForXH(context: Context?, dipValue: Float): Int {
        if (sScale <= 0) {
            init(context)
        }
        return Math.round(dipValue * sScale / 2)
    }

    /**
     * @param dipValue
     * @return
     */
    fun dip2pxF(context: Context?, dipValue: Float): Float {
        if (sScale <= 0) {
            init(context)
        }
        return dipValue * sScale
    }

    fun dip2pxForXHF(context: Context?, dipValue: Float): Float {
        if (sScale <= 0) {
            init(context)
        }
        return dipValue * sScale / 2
    }

    /**
     * @param pxValue
     * @return
     */
    @Deprecated(
        """might no init, use {@link #px2dip(Context, float)} instead
	  """
    )
    fun px2dip(pxValue: Float): Int {
        return Math.round(pxValue / sScale)
    }

    fun px2dip(context: Context?, pxValue: Float): Int {
        if (sScale <= 0) {
            init(context)
        }
        return Math.round(pxValue / sScale)
    }

    fun getStatusBarHeight(context: Context): Int {
        if (statusBarHeight <= 0) {
            var c: Class<*>? = null
            var obj: Any? = null
            var field: Field? = null
            var x = 0
            try {
                c = Class.forName("com.android.internal.R\$dimen")
                obj = c.newInstance()
                field = c.getField("status_bar_height")
                x = field[obj].toString().toInt()
                statusBarHeight = context.resources.getDimensionPixelSize(
                    x
                )
            } catch (ignore: Exception) {
//				e1.printStackTrace();
            }
        }
        return statusBarHeight
    }

    fun getScaledTouchSlop(context: Context?): Int {
        if (sScaledTouchSlop <= 0) {
            sScaledTouchSlop = ViewConfiguration.get(context)
                .scaledTouchSlop
        }
        return sScaledTouchSlop
    }
}