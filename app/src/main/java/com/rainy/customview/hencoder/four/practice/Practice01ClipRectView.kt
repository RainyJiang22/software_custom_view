package com.rainy.customview.hencoder.four.practice

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.rainy.customview.R

/**
 * @author jiangshiyu
 * @date 2024/7/11
 */
class Practice01ClipRectView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.maps)


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //左上的位置
        val left = (width - bitmap.width) / 2
        val top = (height - bitmap.height) / 2

        canvas?.save()
        canvas?.clipRect(left + 50, top + 50, left + 300, top + 200)
        canvas?.drawBitmap(bitmap, left.toFloat(), top.toFloat(), paint)
        canvas?.restore()
    }
}