package com.rainy.customview.hencoder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.View
import com.rainy.customview.R

/**
 * @author jiangshiyu
 * @date 2022/10/28
 */
class PaintView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val saved = canvas?.saveLayer(null, paint)

        val rectBitmap = BitmapFactory.decodeResource(resources, R.drawable.sample_rect)

        val ovalBitmap = BitmapFactory.decodeResource(resources, R.drawable.sample_oval)


        canvas?.drawBitmap(rectBitmap, 0f, 0f, paint)
        paint.xfermode = xfermode
        canvas?.drawBitmap(ovalBitmap, 0f, 0f, paint)

        paint.xfermode = null

        if (saved != null) {
            canvas.restoreToCount(saved)
        }

    }

}