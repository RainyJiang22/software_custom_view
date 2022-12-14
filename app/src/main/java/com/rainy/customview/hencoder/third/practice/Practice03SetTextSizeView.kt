package com.rainy.customview.hencoder.third.practice

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.StaticLayout
import android.util.AttributeSet
import android.view.View

class Practice03SetTextSizeView : View {
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var text = "Hello HenCoder"

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var y = 100

        // 使用 paint.setTextSize() 来设置不同大小的文字

        // 第一处：文字大小 16
        paint.textSize = 16f
        canvas.drawText(text, 50f, y.toFloat(), paint)
        y += 30
        // 第一处：文字大小 24
        paint.textSize = 24f
        canvas.drawText(text, 50f, y.toFloat(), paint)
        y += 55
        // 第一处：文字大小 48
        paint.textSize = 48f
        canvas.drawText(text, 50f, y.toFloat(), paint)
        y += 80
        // 第一处：文字大小 72
        paint.textSize = 72f
        canvas.drawText(text, 50f, y.toFloat(), paint)
    }
}