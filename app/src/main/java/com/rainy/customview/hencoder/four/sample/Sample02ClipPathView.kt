package com.rainy.customview.hencoder.four.sample

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import com.rainy.customview.R

class Sample02ClipPathView : View {
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var bitmap: Bitmap? = null
    var path1 = Path()
    var path2 = Path()
    var point1 = Point(200, 200)
    var point2 = Point(600, 200)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.maps)
        path1.addCircle(
            (point1.x + 200).toFloat(),
            (point1.y + 200).toFloat(),
            150f,
            Path.Direction.CW
        )

        //填充方式,这个会填充外部区域
        path2.fillType = Path.FillType.INVERSE_WINDING
        path2.addCircle(
            (point2.x + 200).toFloat(),
            (point2.y + 200).toFloat(),
            150f,
            Path.Direction.CW
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.clipPath(path1)
        canvas.drawBitmap(bitmap!!, point1.x.toFloat(), point1.y.toFloat(), paint)
        canvas.restore()
        canvas.save()
        canvas.clipPath(path2)
        canvas.drawBitmap(bitmap!!, point2.x.toFloat(), point2.y.toFloat(), paint)
        canvas.restore()
    }
}