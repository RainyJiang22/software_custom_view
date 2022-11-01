package com.rainy.customview.hencoder.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.telephony.TelephonyCallback
import android.util.AttributeSet
import android.view.View
import com.rainy.customview.hencoder.Data
import kotlin.math.cos
import kotlin.math.sin

class Practice11PieChartView : View {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    val radius = 300f
    private var mDataList = mutableListOf<Data>()

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var rectF: RectF? = null

    private var total = 0f
    private var max = 0f

    // 开始的角度
    var startAngle = 0f

    // 扫过的角度
    var sweptAngle = 0f

    // 当前扇形一半的角度
    var halfAngle = 0f

    var lineStartX = 0f // 直线开始的X坐标

    var lineStartY = 0f // 直线开始的Y坐标

    var lineEndX = 0f // 直线结束的X坐标

    // 直线结束的Y坐标
    var lineEndY = 0f

    init {
        mDataList = ArrayList()
        var data = Data("Gingerbread", 15.0f, Color.WHITE)
        mDataList.add(data)
        data = Data("Ice Cream Sandwich", 20.0f, Color.MAGENTA)
        mDataList.add(data)
        data = Data("Jelly Bean", 22.0f, Color.GRAY)
        mDataList.add(data)
        data = Data("KitKat", 28.0f, Color.GREEN)
        mDataList.add(data)
        data = Data("Lollipop", 30.0f, Color.BLUE)
        mDataList.add(data)
        data = Data("Marshmallow", 70.0f, Color.RED)
        mDataList.add(data)
        data = Data("Nougat", 50.5f, Color.YELLOW)
        mDataList.add(data)

        max = Float.MIN_VALUE
        total = 0f
        for (data1 in mDataList) {
            total += data1.number
            max = max.coerceAtLeast(data1.number)
        }

        paint = Paint()
        paint.isAntiAlias = true
        paint.strokeWidth = 2f

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画饼图
        paint.textSize = 50f
        paint.color = Color.WHITE
        canvas.drawText(
            "饼图",
            width / 2 - 100 - paint.measureText("饼图") / 2,
            (height * 0.9).toFloat(),
            paint
        )

        //canvas原点x轴移动到x.width
        canvas.translate((width / 2 - 100).toFloat(), (height / 2 - 100).toFloat())
        rectF = RectF(-300f, -300f, 300f, 300f)

        paint.style = Paint.Style.FILL
        paint.textSize = 30f

        startAngle = 0f

        for (data: Data in mDataList) {
            paint.color = data.color
            sweptAngle = data.number / total * 360f
            halfAngle = startAngle + sweptAngle / 2


            //角度=弧度*180/Math.PI
            lineStartX = radius * cos((halfAngle / 180 * Math.PI).toFloat())
            lineStartY = radius * sin((halfAngle / 180 * Math.PI).toFloat())

            lineEndX = (radius + 50) * cos(halfAngle / 180 * Math.PI).toFloat()
            lineEndY = (radius + 50) * sin(halfAngle / 180 * Math.PI).toFloat()

            if (max == data.number) {
                canvas.save()
                canvas.translate(0.1f * lineStartX, lineStartY * 0.1f)
                canvas.drawArc(rectF!!, startAngle, sweptAngle, true, paint)
            } else {
                canvas.drawArc(rectF!!, startAngle, sweptAngle - 2f, true, paint);
            }

            paint.color = Color.WHITE
            paint.style = Paint.Style.STROKE
            canvas.drawLine(lineStartX, lineStartY, lineEndX, lineEndY, paint);
            if (halfAngle > 90 && halfAngle <= 270) {
                canvas.drawLine(lineEndX, lineEndY, lineEndX - 50, lineEndY, paint);
                paint.style = Paint.Style.FILL
                canvas.drawText(
                    data.name,
                    lineEndX - 50 - 10 - paint.measureText(data.name),
                    lineEndY,
                    paint
                );
            } else {
                canvas.drawLine(lineEndX, lineEndY, lineEndX + 50, lineEndY, paint);
                paint.style = Paint.Style.FILL;
                canvas.drawText(data.name, lineEndX + 50 + 10, lineEndY, paint);
            }
            if (max == data.number) {
                canvas.restore()//恢复save之前的状态
            }
            startAngle += sweptAngle
        }
    }
}