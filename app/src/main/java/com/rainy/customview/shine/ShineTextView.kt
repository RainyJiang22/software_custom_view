package com.rainy.customview.shine

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * @author jiangshiyu
 * @date 2022/9/21
 * 流光字体
 */
class ShineTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {


    private var mLinearGradient: LinearGradient? = null
    private var mGradientMatrix: Matrix? = null
    private var mPaint: Paint? = null

    private var mViewWidth = 0
    private var mTranslate = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (mViewWidth == 0) {
            mViewWidth = measuredWidth
            if (mViewWidth > 0) {
                mPaint = paint
                mLinearGradient =
                    LinearGradient(
                        0f, 0f, (mViewWidth / 8).toFloat(), 0f, intArrayOf(
                            -0xa669, -0x1a12, -0xa669
                        ), null, Shader.TileMode.CLAMP
                    )
                mPaint?.shader = mLinearGradient
                mGradientMatrix = Matrix()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mGradientMatrix != null) {
            mTranslate += mViewWidth / 10
            if (mTranslate > 1.5 * mViewWidth) {
                mTranslate = -mViewWidth / 2
            }
            mGradientMatrix!!.setTranslate(mTranslate.toFloat(), 0f)
            mLinearGradient!!.setLocalMatrix(mGradientMatrix)
            postInvalidateDelayed(100)
        }
    }
}