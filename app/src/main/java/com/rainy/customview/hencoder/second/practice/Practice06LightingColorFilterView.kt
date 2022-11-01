package com.rainy.customview.hencoder.second.practice

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.graphics.Paint
import com.rainy.customview.R
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View

class Practice06LightingColorFilterView : View {
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

        // 使用 Paint.setColorFilter() 来设置 LightingColorFilter

        // 第一个 LightingColorFilter：去掉红色部分
        paint.colorFilter = colorFilter1
        canvas.drawBitmap(bitmap!!, 0f, 0f, paint)

        // 第二个 LightingColorFilter：增强绿色部分
        paint.colorFilter = colorFilter2
        canvas.drawBitmap(bitmap!!, (bitmap!!.width + 100).toFloat(), 0f, paint)
    }

    init {
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.batman)
    }
}