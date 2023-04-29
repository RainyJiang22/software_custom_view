package com.rainy.customview.loading

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.rainy.dp
import kotlin.math.atan2
import kotlin.math.pow

/**
 * @author jiangshiyu
 * @date 2022/8/9
 * 图片环绕方形加载loading
 */
class ImageLoadingView : View {

    private val offsetCenter = 2.dp

    private val paint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.WHITE
        }
    }

    private val progressPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.parseColor("#1B1B1B")
        }
    }

    var progress = 0f
        set(value) {
            field = value
            postInvalidateOnAnimation()
            if (progress == 1f) {
                this.visibility = GONE
            } else {
                this.visibility = VISIBLE
            }
        }


    constructor(context: Context) : super(context) {}
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {}


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.save()
        canvas?.translate(width / 2f, height / 2f)

        val radiusArc =
            (((height / 2f) * (height / 2f)) + ((width / 2f) * (width / 2f))).toDouble()
                .pow(0.5).toFloat()
        val tan = atan2((height / 2f).toDouble(), (width / 2f).toDouble())
        val angleA = 180 * tan / Math.PI
        canvas?.drawArc(
            -radiusArc,
            -radiusArc,
            radiusArc,
            radiusArc,
            (-(90 - angleA) - 90f).toFloat(),
            progress * 360,
            true,
            progressPaint
        )
        canvas?.restore()

        canvas?.drawRect(
            offsetCenter, offsetCenter,
            width - offsetCenter, height - offsetCenter,
            paint
        )

    }
}