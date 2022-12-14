package com.rainy.customview.hencoder.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class Practice9DrawPathView : View {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    private val path = Path()

    private val paint = Paint()


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//        练习内容：使用 canvas.drawPath() 方法画心形
        path.addArc(200f, 200f, 400f, 400f, -225f, 225f);
        path.arcTo(400f, 200f, 600f, 400f, -180f, 225f, false);
        path.lineTo(400f, 542f)
        canvas.drawPath(path, paint)
    }
}