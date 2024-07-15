package com.rainy.customview.hencoder.four.practice

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.LinearInterpolator
import com.rainy.customview.R

/**
 * @author jiangshiyu
 * @date 2024/7/15
 */
class Practice13CameraRotateHittingFaceView : View {


    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var bitmap: Bitmap? = null
    var point = Point(200, 200)
    var camera = Camera()
    private val matr = Matrix()
    var degrees = 0
    var animator: ObjectAnimator = ObjectAnimator.ofInt(this, "degree", 0, 360)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.maps)
        val scaledBitmap = Bitmap.createScaledBitmap(
            bitmap!!, bitmap!!.width * 2, bitmap!!.height * 2, true
        )
        bitmap!!.recycle()
        bitmap = scaledBitmap
        animator.duration = 3000
        animator.interpolator = LinearInterpolator()
        animator.repeatCount = ValueAnimator.INFINITE
        val displayMetrics: DisplayMetrics = resources.displayMetrics
        val newZ: Float = -displayMetrics.density * 6
        camera.setLocation(0f, 0f, newZ)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.end()
    }

    @Suppress("unused")
    fun setDegree(degree: Int) {
        this.degrees = degree
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val bitmapWidth = bitmap!!.width
        val bitmapHeight = bitmap!!.height
        val centerX = point.x + bitmapWidth / 2
        val centerY = point.y + bitmapHeight / 2
        camera.save()
        matr.reset()
        camera.rotateX(degrees.toFloat())
        camera.getMatrix(matr)
        camera.restore()
        matr.preTranslate(-centerX.toFloat(), -centerY.toFloat())
        matr.postTranslate(centerX.toFloat(), centerY.toFloat())
        canvas.save()
        canvas.concat(matr)
        canvas.drawBitmap(bitmap!!, point.x.toFloat(), point.y.toFloat(), paint)
        canvas.restore()
    }
}