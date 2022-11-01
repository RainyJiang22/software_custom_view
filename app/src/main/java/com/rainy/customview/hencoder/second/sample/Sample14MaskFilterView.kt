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

class Sample14MaskFilterView : View {
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var bitmap: Bitmap? = null
    var maskFilter1: MaskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.NORMAL)
    var maskFilter2: MaskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.INNER)
    var maskFilter3: MaskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.OUTER)
    var maskFilter4: MaskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.SOLID)

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.maskFilter = maskFilter1
        canvas.drawBitmap(bitmap!!, 100f, 50f, paint)
        paint.maskFilter = maskFilter2
        canvas.drawBitmap(bitmap!!, (bitmap!!.width + 200).toFloat(), 50f, paint)
        paint.maskFilter = maskFilter3
        canvas.drawBitmap(bitmap!!, 100f, (bitmap!!.height + 100).toFloat(), paint)
        paint.maskFilter = maskFilter4
        canvas.drawBitmap(bitmap!!,
            (bitmap!!.width + 200).toFloat(),
            (bitmap!!.height + 100).toFloat(),
            paint)
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.what_the_fuck)
    }
}