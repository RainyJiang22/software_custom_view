package com.rainy.customview.hencoder.third.sample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.StaticLayout
import android.util.AttributeSet
import android.view.View

class Sample12MeasureTextView : View {
    var paint1 = Paint(Paint.ANTI_ALIAS_FLAG)
    var paint2 = Paint(Paint.ANTI_ALIAS_FLAG)
    var text1 = "三个月内你胖了"
    var text2 = "4.5"
    var text3 = "公斤"
    var measuredText1 = 0f
    var measuredText2 = 0f

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawText(text1, 50f, 200f, paint1)
        canvas.drawText(text2, 50 + measuredText1, 200f, paint2)
        canvas.drawText(text3, 50 + measuredText1 + measuredText2, 200f, paint1)
    }

    init {
        paint1.textSize = 60f
        paint2.textSize = 120f
        paint2.color = Color.parseColor("#E91E63")
        measuredText1 = paint1.measureText(text1)
        measuredText2 = paint2.measureText(text2)
    }
}