package com.rainy.customview.utils

import android.graphics.Color
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * @author jiangshiyu
 * @date 2022/8/9
 */
object ColorUtils {

    private val threadLocal = ThreadLocal<DoubleArray>()


    fun rangeToRGB(hslArray: FloatArray): Int {
        val b: Int
        val g: Int
        val r: Int
        //色值(0-360)
        val hValue = hslArray[0]
        //饱和度(0-1)
        val sValue = hslArray[1]
        //明度值(0-1)
        val lValue = hslArray[2]
        val abs1 = (1.0f - abs((lValue * 2.0f) - 1.0f)) * sValue
        val currentLValue = lValue - (0.5f * abs1)
        val abs2 = (1.0f - abs((hValue / 60.0f) % 2.0f) - 1.0f)

        //6个区域
        when ((hValue.toInt()) / 60) {
            0 -> {
                r = ((abs1 + currentLValue) * 255.0f).roundToInt()
                g = ((abs2 + currentLValue) * 255.0f).roundToInt()
                b = (currentLValue * 255.0f).roundToInt()
            }
            1 -> {
                r = ((abs2 + currentLValue) * 255.0f).roundToInt()
                g = ((abs1 + currentLValue) * 255.0f).roundToInt()
                b = (currentLValue * 255.0f).roundToInt()
            }

            2 -> {
                r = (currentLValue * 255.0f).roundToInt()
                g = ((abs1 + currentLValue) * 255.0f).roundToInt()
                b = ((abs2 + currentLValue) * 255.0f).roundToInt()
            }

            3 -> {
                r = (currentLValue * 255.0f).roundToInt()
                g = ((abs2 + currentLValue) * 255.0f).roundToInt()
                b = ((abs1 + currentLValue) * 255.0f).roundToInt()
            }

            4 -> {
                r = ((abs2 + currentLValue) * 255.0f).roundToInt()
                g = (currentLValue * 255.0f).roundToInt()
                b = ((abs1 + currentLValue) * 255.0f).roundToInt()
            }

            5, 6 -> {
                r = ((abs1 + currentLValue) * 255.0f).roundToInt()
                g = (currentLValue * 255.0f).roundToInt()
                b = ((abs2 + currentLValue) * 255.0f).roundToInt()
            }

            else -> {
                r = 0
                g = 0
                b = 0
            }
        }
        return Color.rgb(isTravelRGB(r), isTravelRGB(g), isTravelRGB(b))
    }


    fun getHSL(currentColor: Int, hslArray: FloatArray) {
        changeToHSL(
            Color.red(currentColor),
            Color.green(currentColor),
            Color.blue(currentColor),
            hslArray
        )
    }


    fun changeToHSL(r: Int, g: Int, b: Int, hslArray: FloatArray) {
        val hue: Float
        val saturation: Float
        val rWeight = (r.toFloat()) / 255.0f
        val gWeight = (g.toFloat()) / 255.0f
        val bWeight = (b.toFloat()) / 255.0f
        //找出rgb中的最大值和最小值
        val max = max(rWeight, max(gWeight, bWeight))
        val min = min(rWeight, min(gWeight, bWeight))

        val difference = max - min
        val midValue = (max + min) / 2.0f
        if (max == min) {
            hue = 0.0f
            saturation = 0.0f
        } else {
            hue = when (max) {
                rWeight -> (gWeight - bWeight) / difference % 6.0f
                gWeight -> (bWeight - rWeight) / difference + 2.0f
                else -> 4.0f + (rWeight - gWeight) / difference
            }
            saturation = difference / (1.0f - abs(2.0f * midValue - 1.0f))
        }
        var hValue = (hue * 60.0f) % 360.0f
        if (hValue < 0.0f) {
            hValue += 360.0f
        }
        hslArray[0] = isTravelHSL(hValue, 0f, 360.0f)
        hslArray[1] = isTravelHSL(saturation, 0f, 1.0f)
        hslArray[2] = isTravelHSL(difference, 0f, 1.0f)
    }


    //保证该值在范围内
    private fun isTravelHSL(currentValue: Float, minValue: Float, maxValue: Float): Float {
        return if (currentValue < minValue) {
            minValue
        } else if (currentValue > maxValue) {
            maxValue
        } else {
            currentValue
        }
    }

    //保证该值在0-255范围内
    private fun isTravelRGB(value: Int): Int {
        return if (value < 0) {
            0
        } else if (value > 255) {
            255
        } else {
            value
        }
    }

    /**
     * 获取当前进度的色值rgb
     */
    fun getCurrentColor(currentColor: Int, lightValue: Float): Int {
        val hslArray = FloatArray(3)
        getHSL(currentColor, hslArray)
        hslArray[2] = lightValue
        return rangeToRGB(hslArray)
    }
}