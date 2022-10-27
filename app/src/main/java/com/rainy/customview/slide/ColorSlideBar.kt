package com.rainy.customview.slide

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.rainy.customview.utils.ColorUtils
import com.rainy.customview.utils.ColorHelper
import com.rainy.dp
import java.util.*
import kotlin.collections.ArrayList

class ColorSlideBar @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    View(context, attrs) {

    companion object {
        private const val TAG = "ColorSeekBar"
        const val DEF_WHITE = Color.WHITE
        const val DEF_BLACK = Color.BLACK
    }


    private var mColorSeeds = intArrayOf(
        Color.parseColor("#000000"),
        Color.parseColor("#FF5722"),
        Color.parseColor("#FFFFFF"),
    )


    private var mBgRectf: RectF = RectF()

    private var mProgress = 0.5f
    private var mInitColor: Int = Color.WHITE
    private var mThumbRadius = 0f

    private var mStokeWidth = 1f.dp

    private var mThumbBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.style = Paint.Style.STROKE
        this.color = Color.WHITE
        this.strokeWidth = mStokeWidth
    }


    private var mColorChangeListener: OnColorChangeListener? = null

    /**
     * 设置当前颜色
     */
    fun setColor(@ColorInt currentColor: Int) {
        mInitColor = currentColor
        mColorSeeds = if (currentColor == DEF_WHITE || currentColor == DEF_BLACK) {
            intArrayOf(
                DEF_BLACK,
                DEF_WHITE
            )
        } else {
            intArrayOf(
                DEF_BLACK,
                currentColor,
                DEF_WHITE,
            )
        }
        val index = FloatArray(3)
        ColorUtils.m26968g(currentColor, index)
        mProgress = index[2]
        Log.d(TAG, "setColor() current color: ${currentColor}, progress:${mProgress}")
        resetBgColor()
        invalidate()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.also { c ->
            c.save()
            c.drawCircle(
                (mBgRectf.width() - mThumbRadius).coerceAtMost(
                    mThumbRadius.coerceAtLeast(mProgress * mBgRectf.width())
                ), mThumbRadius, mThumbRadius, mThumbBorderPaint
            )
            c.restore()
        }
    }


    private fun resetBgColor() {
        background = resetBackground(mInitColor)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBgRectf.set(0f, 0f, width.toFloat(), height.toFloat())
        mThumbRadius = mBgRectf.height() / 2f - mStokeWidth / 2
        resetBackground(mInitColor)

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                event.x.let {
                    mProgress = it / width
                    invalidate()
                }
                mColorChangeListener?.also { callback ->
                    getColor().also {
                        callback.onColorChangeListener(
                            it,
                            ColorHelper.formatColor(it).uppercase(Locale.getDefault())
                        )
                    }
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return true
    }

    @ColorInt
    private fun getColor(): Int {
        return ColorUtils.m40486g(mInitColor, mProgress)
    }


    fun setOnColorChangeListener(onColorChangeListener: OnColorChangeListener) {
        this.mColorChangeListener = onColorChangeListener
    }


    interface OnColorChangeListener {
        fun onColorChangeListener(@ColorInt color: Int, colorHex: String)
    }


    private fun resetBackground(currentColor: Int): ShapeDrawable {
        val fArr = FloatArray(3)
        ColorUtils.m26968g(currentColor, fArr)
        val arrayList = ArrayList<Int>()
        val arrayList2 = ArrayList<Float>()
        var i2 = 0
        while (true) {
            val i3 = i2 + 1
            val f = i2.toFloat() / 10.0f
            fArr[2] = f
            arrayList.add(Integer.valueOf(ColorUtils.m26962a(fArr)))
            arrayList2.add(java.lang.Float.valueOf(f))
            if (i3 > 10) {
                val cVar = BgDrawableFactory(arrayList, arrayList2)
                val m: Float = 18.dp
                val shapeDrawable =
                    ShapeDrawable(RoundRectShape(floatArrayOf(m, m, m, m, m, m, m, m), null, null))
                shapeDrawable.shaderFactory = cVar
                return shapeDrawable
            }
            i2 = i3
        }
    }

    class BgDrawableFactory internal constructor(
        val colors: ArrayList<Int>,
        val position: ArrayList<Float>
    ) :
        ShapeDrawable.ShaderFactory() {
        override fun resize(width: Int, height: Int): Shader {
            return LinearGradient(
                0.0f,
                0.0f,
                width.toFloat(),
                height.toFloat(),
                colors.toIntArray(),
                position.toFloatArray(),
                Shader.TileMode.REPEAT
            )
        }
    }

}