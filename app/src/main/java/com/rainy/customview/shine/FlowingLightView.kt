package com.rainy.customview.shine

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec
import android.view.animation.LinearInterpolator
import androidx.annotation.Nullable


/**
 * @author jiangshiyu
 * @date 2022/9/21
 */
/**
 * 流光动画
 */
class FlowingLightView(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {
    private var mPaint: Paint? = null
    private var mPath: Path? = null
    private var mLinearGradient: LinearGradient? = null
    private var mValueAnimator: ValueAnimator? = null

    constructor(context: Context?) : this(context, null) {}
    constructor(context: Context?, @Nullable attrs: AttributeSet?) : this(context, attrs, 0) {}

    private fun init() {
        mPaint = Paint()
        mPath = Path()
    }

    private fun initPointAndAnimator(w: Int, h: Int) {
        val point1 = Point(0, 0)
        val point2 = Point(w, 0)
        val point3 = Point(w, h)
        val point4 = Point(0, h)
        mPath?.moveTo(point1.x.toFloat(), point1.y.toFloat())
        mPath?.lineTo(point2.x.toFloat(), point2.y.toFloat())
        mPath?.lineTo(point3.x.toFloat(), point3.y.toFloat())
        mPath?.lineTo(point4.x.toFloat(), point4.y.toFloat())
        mPath?.close()

        // 斜率k
        val k = 0f * h / w
        // 偏移
        val offset = 1f * w / 8
        // 0f - offset * 2 为数值左边界（屏幕外左侧）， w + offset * 2为数值右边界（屏幕外右侧）
        // 目的是使光影走完一遍，加一些时间缓冲，不至于每次光影移动的间隔都那么急促
        mValueAnimator = ValueAnimator.ofFloat(0f - offset / 2, w + offset / 2)
        mValueAnimator?.repeatCount = -1
        mValueAnimator?.interpolator = LinearInterpolator()
        mValueAnimator?.duration = 8000
        mValueAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
            val value = animation.animatedValue as Float
            mLinearGradient = LinearGradient(
                value,
                k * value,
                value + 2,
                k * (value + 2),
                intArrayOf(
                    Color.parseColor("#00FFE5EE"),
                    Color.parseColor("#FFE5EE"),
                    Color.parseColor("#00FFE5EE")
                ),
                null,
                Shader.TileMode.CLAMP
            )
            mPaint?.setShader(mLinearGradient)
            invalidate()
        })
        mValueAnimator?.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        initPointAndAnimator(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(mPath!!, mPaint!!)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mValueAnimator?.cancel()
    }

    init {
        init()
    }
}