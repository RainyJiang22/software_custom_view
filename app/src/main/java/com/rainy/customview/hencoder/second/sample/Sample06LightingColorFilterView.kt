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
import android.util.AttributeSet
import android.view.View

class Sample06LightingColorFilterView : View {
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var bitmap: Bitmap? = null
    var colorFilter1: ColorFilter = LightingColorFilter(0x00ffff, 0x000000)
    var colorFilter2: ColorFilter = LightingColorFilter(0xffffff, 0x003000)

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.colorFilter = colorFilter1
        canvas.drawBitmap(bitmap!!, 0f, 0f, paint)
        paint.colorFilter = colorFilter2
        canvas.drawBitmap(bitmap!!, (bitmap!!.width + 100).toFloat(), 0f, paint)
    }

    init {
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.batman)
    }
}