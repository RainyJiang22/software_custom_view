package com.rainy.customview.hencoder.four.practice

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import com.rainy.customview.R

/**
 * @author jiangshiyu
 * @date 2024/7/12
 */
class Practice11CameraRotateView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val bitmap = BitmapFactory.decodeResource(resources, R.drawable.maps)

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val point1 = Point(200, 200)
    val point2 = Point(600, 200)
    val camera = Camera()


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.save()
        camera.save()
        camera.rotateX(30f)
        camera.applyToCanvas(canvas)
        canvas?.drawBitmap(bitmap, point1.x.toFloat(), point1.y.toFloat(), paint)
        canvas?.restore()

        canvas?.save()
        camera.save()
        camera.rotateY(30f)
        camera.applyToCanvas(canvas)
        canvas?.drawBitmap(bitmap, point2.x.toFloat(), point2.y.toFloat(), paint)
        canvas?.restore()
    }


}