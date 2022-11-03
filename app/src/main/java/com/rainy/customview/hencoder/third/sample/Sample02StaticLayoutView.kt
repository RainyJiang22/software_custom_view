package com.rainy.customview.hencoder.third.sample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Layout
import android.text.TextPaint
import android.text.StaticLayout
import android.util.AttributeSet
import android.view.View

class Sample02StaticLayoutView : View {
    var textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    var text = "Hello\nHenCoder"
    var staticLayout: StaticLayout? = null

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.translate(50f, 40f)
        staticLayout!!.draw(canvas)
        canvas.restore()
    }

    init {
        textPaint.textSize = 60f
        // 这两行的位置不能换哟
        staticLayout = StaticLayout(text, textPaint, 600, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true)
    }
}