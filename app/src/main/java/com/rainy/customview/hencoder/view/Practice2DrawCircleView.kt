package com.rainy.customview.hencoder.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class Practice2DrawCircleView : View {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    val paint = Paint()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//        练习内容：使用 canvas.drawCircle() 方法画圆
//        一共四个圆：1.实心圆 2.空心圆 3.蓝色实心圆 4.线宽为 20 的空心圆
        paint.style = Paint.Style.FILL
        canvas.drawCircle(300f, 300f, 100f, paint)
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(600f, 300f, 100f, paint)
        paint.color = Color.BLUE
        paint.style = Paint.Style.FILL
        canvas.drawCircle(300f, 600f, 100f, paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20f
        canvas.drawCircle(600f, 600f, 100f, paint)

    }
}