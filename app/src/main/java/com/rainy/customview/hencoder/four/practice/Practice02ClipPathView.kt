package com.rainy.customview.hencoder.four.practice

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

/**
 * @author jiangshiyu
 * @date 2024/7/11
 */
class Practice02ClipPathView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.maps)

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    val path1 = Path()
    val path2 = Path()
    val point1 = Point(200, 200)
    val point2 = Point(600, 200)


    init {
        path1.addCircle(
            (point1.x + 200).toFloat(),
            (point1.y + 200).toFloat(),
            150f,
            Path.Direction.CW
        )

        path2.fillType = Path.FillType.INVERSE_WINDING
        path2.addCircle(
            (point2.x + 200).toFloat(),
            (point2.y + 200).toFloat(),
            150f,
            Path.Direction.CW
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.save()
        canvas?.clipPath(path1)
        canvas?.drawBitmap(bitmap, point1.x.toFloat(), point1.y.toFloat(), paint)
        canvas?.restore()

        canvas?.save()
        canvas?.clipPath(path2)
        canvas?.drawBitmap(bitmap, point2.x.toFloat(), point2.y.toFloat(), paint)
        canvas?.restore()
    }
}