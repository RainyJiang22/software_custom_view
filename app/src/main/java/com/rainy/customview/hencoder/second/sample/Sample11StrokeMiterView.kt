package com.rainy.customview.hencoder.second.sample

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Xfermode
import android.graphics.PorterDuffXfermode
import android.graphics.PorterDuff
import android.graphics.BitmapFactory
import com.rainy.customview.R
import android.graphics.PathEffect
import android.graphics.CornerPathEffect
import android.graphics.DiscretePathEffect
import android.graphics.DashPathEffect
import android.graphics.SumPathEffect
import android.graphics.ComposePathEffect
import android.graphics.PathDashPathEffect
import android.graphics.MaskFilter
import android.graphics.BlurMaskFilter
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Shader
import android.graphics.SweepGradient
import android.graphics.ComposeShader
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class Sample11StrokeMiterView : View {
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var path = Path()

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.translate(100f, 100f)
        paint.strokeMiter = 1f
        canvas.drawPath(path, paint)
        canvas.translate(300f, 0f)
        paint.strokeMiter = 3f
        canvas.drawPath(path, paint)
        canvas.translate(300f, 0f)
        paint.strokeMiter = 5f
        canvas.drawPath(path, paint)
        canvas.restore()
    }

    init {
        paint.strokeWidth = 40f
        paint.style = Paint.Style.STROKE
        path.rLineTo(200f, 0f)
        path.rLineTo(-160f, 120f)
    }
}