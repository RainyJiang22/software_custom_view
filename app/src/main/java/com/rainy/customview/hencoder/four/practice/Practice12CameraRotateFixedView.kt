package com.rainy.customview.hencoder.four.practice

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import com.rainy.customview.R

/**
 * @author jiangshiyu
 * @date 2024/7/12
 */
class Practice12CameraRotateFixedView : View {

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

    private val matr = Matrix()


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height
        val center1X = point1.x + bitmapWidth / 2
        val center1Y = point1.y + bitmapHeight / 2
        val center2X = point2.x + bitmapWidth / 2
        val center2Y = point2.y + bitmapHeight / 2

        camera.save()
        matr.reset()
        camera.rotateX(30f)
        camera.getMatrix(matr)
        camera.restore()
        matr.preTranslate(-center1X.toFloat(), -center1Y.toFloat())
        matr.postTranslate(center1X.toFloat(), center1Y.toFloat())
        canvas?.save()
        canvas?.concat(matr)
        canvas?.drawBitmap(bitmap!!, point1.x.toFloat(), point1.y.toFloat(), paint)
        canvas?.restore()
        camera.save()
        matr.reset()
        camera.rotateY(30f)
        camera.getMatrix(matr)
        camera.restore()
        matr.preTranslate(-center2X.toFloat(), -center2Y.toFloat())
        matr.postTranslate(center2X.toFloat(), center2Y.toFloat())
        canvas?.save()
        canvas?.concat(matr)
        canvas?.drawBitmap(bitmap, point2.x.toFloat(), point2.y.toFloat(), paint)
        canvas?.restore()

        camera.save()
        matr.reset()
        camera.rotateX(30f)
        camera.getMatrix(matr)
        camera.restore()
        matr.preTranslate(-center1X.toFloat(), -center1Y.toFloat())
        matr.postTranslate(center1X.toFloat(), center1Y.toFloat())
        canvas?.save()
        canvas?.concat(matr)
        canvas?.drawBitmap(bitmap, point1.x.toFloat(), point1.y.toFloat(), paint)
        canvas?.restore()


        camera.save()
        matr.reset()
        camera.rotateY(30f)
        camera.getMatrix(matr)
        camera.restore()
        matr.preTranslate(-center2X.toFloat(), -center2Y.toFloat())
        matr.postTranslate(center2X.toFloat(), center2Y.toFloat())
        canvas?.save()
        canvas?.concat(matr)
        canvas?.drawBitmap(bitmap, point2.x.toFloat(), point2.y.toFloat(), paint)
        canvas?.restore()

    }


}