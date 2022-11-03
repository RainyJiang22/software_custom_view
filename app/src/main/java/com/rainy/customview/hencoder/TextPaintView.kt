package com.rainy.customview.hencoder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*

/**
 * @author jiangshiyu
 * @date 2022/11/3
 */
class TextPaintView : View {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    val text = "你好Hello World"

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr)


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawText(text, 400f, 100f, paint)
        //textAlign
        paint.textAlign = Paint.Align.CENTER

        paint.textSize = 80F

        //绘制不同的区域语言
        paint.textLocale = Locale.ENGLISH

        //开启次像素级的抗锯齿
        paint.isSubpixelText = true

        //加入下划线
        paint.isUnderlineText = true

        //isLinearText
        paint.isLinearText = true
    }
}