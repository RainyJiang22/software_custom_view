package com.rainy.customview.hencoder.second.practice

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ComposePathEffect
import android.graphics.CornerPathEffect
import android.graphics.DashPathEffect
import android.graphics.DiscretePathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathEffect
import com.rainy.customview.R
import android.graphics.Shader
import android.graphics.SumPathEffect
import android.util.AttributeSet
import android.view.View

class Practice12PathEffectView : View {
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var path = Path()

    var cornerPathEffect: PathEffect = CornerPathEffect(20f)
    var discretePathEffect: PathEffect = DiscretePathEffect(20f, 5f)
    var dashPathEffect: PathEffect = DashPathEffect(floatArrayOf(20f, 10f, 5f, 10f), 0f)
    var pathDashPathEffect: PathEffect? = null
    var sumPathEffect: PathEffect = SumPathEffect(dashPathEffect, discretePathEffect)
    var composePathEffect: PathEffect = ComposePathEffect(dashPathEffect, discretePathEffect)


    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 使用 Paint.setPathEffect() 来设置不同的 PathEffect

        // 第一处：CornerPathEffect
        paint.pathEffect = cornerPathEffect
        canvas.drawPath(path, paint)
        canvas.save()
        canvas.translate(500f, 0f)
        // 第二处：DiscretePathEffect
        paint.pathEffect = discretePathEffect
        canvas.drawPath(path, paint)
        canvas.restore()
        canvas.save()
        canvas.translate(0f, 200f)
        // 第三处：DashPathEffect
        paint.pathEffect = dashPathEffect
        canvas.drawPath(path, paint)
        canvas.restore()
        canvas.save()
        canvas.translate(500f, 200f)
        // 第四处：PathDashPathEffect
        paint.pathEffect = pathDashPathEffect
        canvas.drawPath(path, paint)
        canvas.restore()
        canvas.save()
        canvas.translate(0f, 400f)
        // 第五处：SumPathEffect
        paint.pathEffect = sumPathEffect
        canvas.drawPath(path, paint)
        canvas.restore()
        canvas.save()
        canvas.translate(500f, 400f)
        // 第六处：ComposePathEffect
        paint.pathEffect = composePathEffect
        canvas.drawPath(path, paint)
        canvas.restore()
    }

    init {
        paint.style = Paint.Style.STROKE
        path.moveTo(50f, 100f)
        path.rLineTo(50f, 100f)
        path.rLineTo(80f, -150f)
        path.rLineTo(100f, 100f)
        path.rLineTo(70f, -120f)
        path.rLineTo(150f, 80f)
    }
}