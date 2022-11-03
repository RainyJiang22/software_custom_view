package com.rainy.customview.hencoder.third.sample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.StaticLayout
import android.util.AttributeSet
import android.view.View

class Sample04SetTypefaceView : View {
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var text = "Hello HenCoder"
    var typeface: Typeface? = null

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.typeface = null
        canvas.drawText(text, 50f, 100f, paint)
        paint.typeface = Typeface.SERIF
        canvas.drawText(text, 50f, 200f, paint)
        paint.typeface = typeface
        canvas.drawText(text, 50f, 300f, paint)
    }

    init {
        paint.textSize = 60f
        typeface = Typeface.createFromAsset(context.assets, "Satisfy-Regular.ttf")
    }
}