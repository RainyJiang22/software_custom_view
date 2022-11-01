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

class Sample08XfermodeView : View {
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var bitmap1: Bitmap? = null
    var bitmap2: Bitmap? = null
    var xfermode1: Xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
    var xfermode2: Xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    var xfermode3: Xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)
        canvas.drawBitmap(bitmap1!!, 0f, 0f, paint)
        paint.xfermode = xfermode1
        canvas.drawBitmap(bitmap2!!, 0f, 0f, paint)
        paint.xfermode = null
        canvas.drawBitmap(bitmap1!!, (bitmap1!!.width + 100).toFloat(), 0f, paint)
        paint.xfermode = xfermode2
        canvas.drawBitmap(bitmap2!!, (bitmap1!!.width + 100).toFloat(), 0f, paint)
        paint.xfermode = null
        canvas.drawBitmap(bitmap1!!, 0f, (bitmap1!!.height + 20).toFloat(), paint)
        paint.xfermode = xfermode3
        canvas.drawBitmap(bitmap2!!, 0f, (bitmap1!!.height + 20).toFloat(), paint)
        paint.xfermode = null
        canvas.restoreToCount(saved)
    }

    init {
        bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.batman)
        bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.batman_logo)
    }
}