package com.rainy.customview.utils

import android.graphics.Color
import java.util.*

/**
 * @author jacky
 * @date 2021/12/27
 */
object ColorHelper {


    @JvmStatic
    fun parseColor(color: String): Int {
        return if (color.isBlank()) 0
        else Color.parseColor(color)
    }

    @JvmStatic
    fun formatColor(color: Int): String {
        return String.format("#%06x", color and 0xffffff).uppercase(Locale.getDefault())
    }


    /**
     * 判断深浅色
     */
    fun isLightColor(color: Int): Boolean {
        val darkness: Double =
            1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness < 0.5
    }
}