package com.rainy.doubleindicatorseekbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

/**
 * @author jiangshiyu
 * @date 2022/3/24
 */
class BubbleView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val mBubbleColor // color of bubble
            : Int
    private val mBubbleTextSize // text size of bubble-progress
            : Int
    private val mBubbleTextColor // text color of bubble-progress
            : Int
    private val mBubbleRadius: Int
    private val mBubblePaint: Paint = Paint()
    private val mBubblePath: Path
    private val mBubbleRectF: RectF
    private val mRect: Rect
    private var mProgressText = ""
    private val mContext: Context?
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(3 * mBubbleRadius, 3 * mBubbleRadius)
        mBubbleRectF.set(
            measuredWidth / 2f - mBubbleRadius, 0f,
            measuredWidth / 2f + mBubbleRadius, (2 * mBubbleRadius).toFloat()
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mBubblePath.reset()
        val x0 = measuredWidth / 2f
        val y0 = measuredHeight - mBubbleRadius / 3f
        mBubblePath.moveTo(x0, y0)
        val x1 = (measuredWidth / 2f - Math.sqrt(3.0) / 2f * mBubbleRadius).toFloat()
        val y1 = 3 / 2f * mBubbleRadius
        mBubblePath.quadTo(
            x1 - ScreenUtil.dip2px(mContext, 2f), y1 - ScreenUtil.dip2px(mContext, 2f),
            x1, y1
        )
        mBubblePath.arcTo(mBubbleRectF, 150f, 240f)
        val x2 = (measuredWidth / 2f + Math.sqrt(3.0) / 2f * mBubbleRadius).toFloat()
        mBubblePath.quadTo(
            x2 + ScreenUtil.dip2px(mContext, 2f), y1 - ScreenUtil.dip2px(mContext, 2f),
            x0, y0
        )
        mBubblePath.close()
        mBubblePaint.color = mBubbleColor
        canvas.drawPath(mBubblePath, mBubblePaint)
        mBubblePaint.textSize = mBubbleTextSize.toFloat()
        mBubblePaint.color = mBubbleTextColor
        mBubblePaint.getTextBounds(mProgressText, 0, mProgressText.length, mRect)
        val fm = mBubblePaint.fontMetrics
        val baseline = mBubbleRadius + (fm.descent - fm.ascent) / 2f - fm.descent
        canvas.drawText(mProgressText, measuredWidth / 2f, baseline, mBubblePaint)
    }

    fun setProgressText(progressText: String?) {
        if (progressText != null && mProgressText != progressText) {
            mProgressText = progressText
            invalidate()
        }
    }

    init {
        mBubblePaint.isAntiAlias = true
        mBubblePaint.textAlign = Paint.Align.CENTER
        mBubblePath = Path()
        mBubbleRectF = RectF()
        mRect = Rect()
        mBubbleColor = ContextCompat.getColor(context!!, R.color.bubble_color)
        mBubbleRadius = ScreenUtil.dip2px(context, 18f)
        mBubbleTextColor = ContextCompat.getColor(context, R.color.white)
        mBubbleTextSize = ScreenUtil.sp2px(context, 14f)
        mContext = context
    }
}