package com.rainy.customview.hencoder.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class Practice10HistogramView : View {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画直方图
        val width = 70f
        mPaint.color = Color.WHITE
        mPaint.strokeWidth = 3f
        //纵坐标
        canvas.drawLine(50f, 50f, 50f, 600f, mPaint)
        //横坐标
        canvas.drawLine(50f, 600f, 600f, 600f, mPaint)

        mPaint.textSize = 18f
        canvas.drawText("Froyo",85f,650f,mPaint);
        canvas.drawText("GB",85+width*1,650f,mPaint);
        canvas.drawText("ICS",85+width*2,650f,mPaint);
        canvas.drawText("JB",85+width*3,650f,mPaint);
        canvas.drawText("KitKat",85+width*4,650f,mPaint);
        canvas.drawText("L",85+width*5,650f,mPaint);
        canvas.drawText("M",85+width*6,650f,mPaint);

        mPaint.color = Color.GREEN
        mPaint.strokeWidth = 60f
        canvas.drawLine(110f,600f,110f,580f,mPaint);
        canvas.drawLine(110+width,600f,110+width,550f,mPaint);
        canvas.drawLine(110+width*2,600f,110+width*2,550f,mPaint);
        canvas.drawLine(110+width*3,600f,110+width*3,450f,mPaint);
        canvas.drawLine(110+width*4,600f, 110 + width * 4, 300f, mPaint);
        canvas.drawLine(110 + width * 5, 600f,110+width*5,250f,mPaint);
        canvas.drawLine(110+width*6,600f,110+width*6,480f,mPaint);


    }
}