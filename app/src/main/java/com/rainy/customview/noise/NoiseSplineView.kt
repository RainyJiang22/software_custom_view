package com.rainy.customview.noise

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.rainy.customview.R
import kotlin.math.abs
import kotlin.math.min
import kotlin.properties.Delegates

class NoiseSplineView : View {
    var totalWidth = 1050F
    var secondHeight = 96F
    var totalHeight = 129F
    var highUnitHeight = 120F
    var unitWidth = 50F
    var space = 8F
    var corner = 12F
    var currentDb = 30
        set(value) {
            invalidate()
            field = value
        }
    var lastDb = 40
        set(value) {
            invalidate()
            field = value
        }

    var backGroundPaint = Paint()
    var soundPaint = Paint()

    val totalAmount = 18
    var center by Delegates.notNull<Float>()


    lateinit var colorPair: Pair<Int, Int>

    var colorGreen = Color.parseColor("#FF0FDD72")
    var colorParentGreen = Color.parseColor("#660FDD72")
    var colorYellow = Color.parseColor("#FFFFE620")
    var colorParentYellow = Color.parseColor("#66FFE620")
    var colorDefault = Color.parseColor("#FF4C4C4C")

    constructor(context: Context) : super(context, null, 0) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val styleArray = context.obtainStyledAttributes(attrs, R.styleable.NoiseSplineView)

        currentDb = styleArray.getInt(R.styleable.NoiseSplineView_currentDb, 60)
        lastDb = styleArray.getInt(R.styleable.NoiseSplineView_lastDb, 40)

        styleArray.recycle()
        initNoiseSpline()
    }


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initNoiseSpline()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        initNoiseSpline()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //Width of total View
        totalWidth = (width - paddingLeft - paddingRight).toFloat()
        //Height of unitRect
        secondHeight = totalWidth / 1050 * 96
        //Height of total view
        totalHeight = totalWidth / 1050 * 129
        //Height of highUnitRect
        highUnitHeight = totalWidth / 1050 * 120
        //Width of unitRect
        unitWidth = totalWidth / 1050 * 50
        //space between unitRects
        space = totalWidth / 1050 * 8
        //corner of the rectangle
        corner = 4F
        val center = Pair(width / 2, height / 2)


        colorPair = if (currentDb < 80) {
            Pair(colorGreen, colorParentGreen)
        } else {
            Pair(colorYellow, colorParentYellow)
        }

        val leftBound = center.first - totalWidth / 2

        val unitUpperBound = center.second - secondHeight / 2
        val unitLowerBound = center.second + secondHeight / 2

        val highUnitUpperBound = center.second - highUnitHeight / 2
        val highUnitLowerBound = center.second + highUnitHeight / 2


        val numOfColor = ((min(lastDb, currentDb) - 30) / 5)
        val numOfChangingColor = abs(currentDb - lastDb) / 5

        for (index in 0..17) {
            //change paint color
            if (index < numOfColor) {
                soundPaint.color = colorPair.first
            } else if (index < numOfColor + numOfChangingColor) {
                soundPaint.color = colorPair.second
            } else {
                soundPaint.color = colorDefault
            }

            //if index ==10, draw highUnit
            val thisLeftBound = leftBound + space * (index + 1) + index * unitWidth
            if (index == 10) {
                canvas?.drawRoundRect(
                    thisLeftBound,
                    highUnitUpperBound,
                    thisLeftBound + unitWidth,
                    highUnitLowerBound,
                    corner, corner, soundPaint
                )
            } else {
                canvas?.drawRoundRect(
                    thisLeftBound,
                    unitUpperBound,
                    thisLeftBound + unitWidth,
                    unitLowerBound,
                    corner, corner, soundPaint
                )
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measureDimension(totalWidth.toInt(), widthMeasureSpec)
        val height = measureDimension(totalHeight.toInt(), heightMeasureSpec)
        setMeasuredDimension(width, height)

    }

    fun measureDimension(defaultSize: Int, measureSpec: Int): Int {
        var result = defaultSize
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = defaultSize
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }
        return result
    }


    private fun initNoiseSpline() {
        soundPaint.strokeWidth = 0F
        soundPaint.style = Paint.Style.FILL_AND_STROKE
        soundPaint.apply {
            isAntiAlias = true
            isDither = true
            isFilterBitmap = true
        }

    }

}