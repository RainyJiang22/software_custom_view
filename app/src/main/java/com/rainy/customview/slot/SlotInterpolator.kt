package com.rainy.customview.slot

import android.view.animation.Interpolator
import kotlin.math.cos

/**
 * @author jiangshiyu
 * @date 2023/3/22
 * 自定义加速度器
 */
class SlotInterpolator : Interpolator {
    override fun getInterpolation(input: Float): Float {
        return (cos((input + 1) * Math.PI) / 2.0f).toFloat() + 0.5f
    }
}