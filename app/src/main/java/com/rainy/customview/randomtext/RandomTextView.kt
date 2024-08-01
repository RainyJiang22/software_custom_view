package com.rainy.customview.randomtext

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.icu.util.Measure
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.rainy.customview.R
import com.rainy.dp
import com.rainy.sp
import java.nio.file.attribute.AttributeView
import kotlin.random.Random

/**
 * @author jiangshiyu
 * @date 2024/8/1
 * 随机数字的自定义textview
 */
class RandomTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {


    private var mTitleText = "3210"

    private var mTitleTextColor = Color.WHITE

    private var mTitleTextSize = 16f


    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
    }

    private val mBound = Rect()


    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.RandomTextView,
            defStyleAttr,
            0
        )

        val n = a.indexCount

        for (i in 0 until n) {
            val attr = a.getIndex(i)
            when (attr) {
                R.styleable.RandomTextView_titleText -> {
                    mTitleText = a.getString(attr).toString()
                }

                R.styleable.RandomTextView_titleTextColor -> {
                    mTitleTextColor = a.getColor(attr, Color.BLACK)
                }

                R.styleable.RandomTextView_titleTextSize -> {
                    mTitleTextSize = a.getDimensionPixelSize(attr, 18f.sp.toInt()).toFloat()
                }
            }
        }
        a.recycle()

        mPaint.apply {
            textSize = mTitleTextSize
            getTextBounds(mTitleText, 0, mTitleText.length, mBound)
        }

        this.setOnClickListener {
            mTitleText = randomText()
            postInvalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val realWidth: Int
        val realHeight: Int
        if (widthMode == MeasureSpec.EXACTLY) {
            realWidth = widthSize
        } else {
            val textWidth = mBound.width()
            val desired = paddingLeft + textWidth + paddingRight
            realWidth = desired
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            realHeight = heightSize
        } else {
            val textHeight = mBound.height()
            val desired = paddingTop + textHeight + paddingBottom
            realHeight = desired
        }

        setMeasuredDimension(realWidth, realHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        mPaint.color = Color.YELLOW
        canvas?.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), mPaint)

        mPaint.color = mTitleTextColor
        canvas?.drawText(
            mTitleText,
            ((width / 2).toFloat() - (mBound.width() / 2).toFloat()),
            ((height / 2).toFloat() + (mBound.height() / 2).toFloat()),
            mPaint
        )
    }

    private fun randomText(): String {
        val random = Random.nextInt(1000, 10000)
        return random.toString()
    }
}