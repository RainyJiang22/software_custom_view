package com.rainy.customview.hencoder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * @author jiangshiyu
 * @date 2022/10/27
 */
class PathView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    private val paint = Paint()
    private val path = Path()


    init {

//        //心型view
//        path.addArc(200f, 200f, 400f, 400f, -225f, 225f);
//        path.arcTo(400f, 200f, 600f, 400f, -180f, 225f, false);
//        path.lineTo(400f, 542f)


//        //画一个封闭不规则矩形
//        path.lineTo(300f, 0f)
//        path.lineTo(400f, 100f)
//        path.lineTo(400f, 500f)
//        path.lineTo(100f, 500f)
//        path.lineTo(0f, 400f)
//        path.close()
//
////        path.lineTo(100f, 100f) // 画斜线
////        path.moveTo(200f, 100f) // 我移~~
////        path.lineTo(200f, 0f) // 画竖线
//
//        path.lineTo(100f, 100f)
//        //forceMoveTo ：是要抬下画笔过去，还是直接拖着画笔过去
//        path.arcTo(100f, 100f, 300f, 300f, -90f, 90f, false)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //绘制自定义view使用path
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
        paint.isAntiAlias = true
        //  canvas?.drawPath(path, paint)

        //绘制三角形
        paint.style = Paint.Style.FILL
//        path.moveTo(100f, 100f);
//        path.lineTo(200f, 100f);
//        path.lineTo(150f, 150f);
//        path.close()

        //绘制圆形 Path.FillType.EVEN_ODD 奇偶原则， Path.FillType.WINDING 环绕数原则
        path.moveTo(100f, 100f)
        path.addCircle(300f, 300f, 200f, Path.Direction.CW)
        path.addCircle(400f, 300f, 200f, Path.Direction.CW)
        path.fillType = Path.FillType.WINDING

        canvas?.drawPath(path, paint)

    }
}