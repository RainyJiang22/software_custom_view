package com.rainy.customview.hencoder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * @author jiangshiyu
 * @date 2022/10/27
 */
class HenCoderView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {


    }


    private val paint = Paint().apply {
        color = Color.parseColor("#88880000")
        strokeWidth = 20f
        isAntiAlias = true //抗锯齿
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

//        //绘制模式，实心，空心,实心并且描边
//        paint.style = Paint.Style.STROKE
//        paint.strokeWidth = 30f //stroke宽度
//        //绘制圆形
//        canvas?.drawCircle(300f, 300f, 200f, paint)
//
//        //绘制矩形,另外可以重载两个方法，可以使用Rect和RectF进行
//        paint.style = Paint.Style.FILL
//        paint.style = Paint.Style.FILL_AND_STROKE
//        canvas?.drawRect(100F, 100F, 500F, 500F, paint);
//
//        paint.style = Paint.Style.STROKE
//        canvas?.drawRect(600F, 100f, 1000f, 500f, paint);
//
//        //绘制多个点
//        val points =
//            floatArrayOf(0f, 0f, 50f, 50f, 50f, 100f, 100f, 50f, 100f, 100f, 150f, 50f, 150f, 100f)
//// 绘制四个点：(50, 50) (50, 100) (100, 50) (100, 100)
//        canvas?.drawPoints(
//            points, 2 /* 跳过两个数，即前两个 0 */,
//            8 /* 一共绘制 8 个数（4 个点）*/, paint
//        )
//
//
//        //绘制椭圆
//        paint.style = Paint.Style.FILL
//        canvas?.drawOval(50f, 50f, 350f, 200f, paint)
//
//        paint.style = Paint.Style.STROKE
//        canvas?.drawOval(400f, 50f, 700f, 200f, paint)
//
//
//        //绘制线条 对于绘制模式没有影响
//        canvas?.drawLine(200f, 200f, 800f, 500f,paint)


        //绘制多个线条
        val linePoints = floatArrayOf(
            20f, 20f, 120f, 20f, 70f, 20f, 70f, 120f, 20f,
            120f, 120f, 120f, 150f, 20f, 250f, 20f, 150f, 20f,
            150f, 120f, 250f, 20f, 250f, 120f, 150f, 120f, 250f, 120f
        )
        canvas?.drawLines(linePoints, paint)

        //绘制圆角矩形(下面是个正方形)
        paint.style = Paint.Style.FILL
        canvas?.drawRoundRect(100f, 100f, 500f, 500f, 10f, 10f, paint)

        //绘制弧形/扇形
        paint.style = Paint.Style.FILL; // 填充模式
        canvas?.drawArc(200f, 100f, 800f, 500f, 0f,360f, true, paint)
//        canvas?.drawArc(200f, 100f, 800f, 500f, -110f, 100f, true, paint); // 绘制扇形
//        canvas?.drawArc(200f, 100f, 800f, 500f, 20f, 140f, false, paint); // 绘制弧形
//        paint.style = Paint.Style.STROKE; // 画线模式
//        canvas?.drawArc(200f, 100f, 800f, 500f, 180f, 60f, false, paint); // 绘制不封口的弧形


    }
}