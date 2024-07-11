package com.rainy.customview.linechart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * @author jiangshiyu
 * @date 2024/4/17
 * 普通折线图
 */
class SimpleLineChart : View {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        const val MOOE_ONE = 1
        const val MODE_TWO = 1
    }

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mYears = arrayOf("2021","2022","2023","2024")

    //自身宽高
    private var mSelfWidth = 0f
    private var mSelfHeight = 0f

    private val linePath = Path()
    private val bgPath =  Path()


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mSelfWidth = w.toFloat()
        mSelfHeight = h.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


    }
}