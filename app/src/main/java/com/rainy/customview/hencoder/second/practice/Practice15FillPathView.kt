package com.rainy.customview.hencoder.second.practice

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import com.rainy.customview.R
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View

class Practice15FillPathView : View {
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var pathPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var path = Path()
    var path1 = Path()
    var path2 = Path()
    var path3 = Path()

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 使用 Paint.getFillPath() 获取实际绘制的 Path
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = 0f
        // 第一处：获取 Path
        paint.getFillPath(path, path1)
        canvas.drawPath(path, paint)
        canvas.save()
        canvas.translate(500f, 0f)
        canvas.drawPath(path1, pathPaint)
        canvas.restore()
        canvas.save()
        canvas.translate(0f, 200f)
        paint.style = Paint.Style.STROKE
        // 第二处：设置 Style 为 STROKE 后再获取 Path
        paint.getFillPath(path, path2)
        canvas.drawPath(path, paint)
        canvas.restore()
        canvas.save()
        canvas.translate(500f, 200f)
        canvas.drawPath(path2, pathPaint)
        canvas.restore()
        canvas.save()
        canvas.translate(0f, 400f)
        paint.strokeWidth = 40f
        // 第三处：Style 为 STROKE 并且线条宽度为 40 时的 Path
        paint.getFillPath(path, path3)
        canvas.drawPath(path, paint)
        canvas.restore()
        canvas.save()
        canvas.translate(500f, 400f)
        canvas.drawPath(path3, pathPaint)
        canvas.restore()
    }

    init {
        path.moveTo(50f, 100f)
        path.rLineTo(50f, 100f)
        path.rLineTo(80f, -150f)
        path.rLineTo(100f, 100f)
        path.rLineTo(70f, -120f)
        path.rLineTo(150f, 80f)
        pathPaint.style = Paint.Style.STROKE
    }
}