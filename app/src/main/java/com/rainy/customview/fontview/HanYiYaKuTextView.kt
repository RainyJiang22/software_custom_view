package com.rainy.customview.fontview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.rainy.customview.R

/**
 * @author jiangshiyu
 * @date 2023/5/31
 */
class HanYiYaKuTextView : AppCompatTextView {

    companion object {
        const val VERTICAL = 1
        const val HORIZONTAL = 2
    }


    private var outlineTextView: AppCompatTextView? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(attrs)
    }

    /**
     * orientation
     *  1 vertical
     *  2 horizontal
     */
    private var orientation = HORIZONTAL

    private var colorArray: IntArray? = null

    private var strokeColorArray: IntArray? = null

    private var mStrokeColor: Int = -1
    private var rect = Rect()

    @SuppressLint("Recycle")
    private fun initView(attrs: AttributeSet?) {
        typeface = FontManager.FONT_HANYI_YAKU
        outlineTextView = AppCompatTextView(context, attrs)
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.HanYiYaKuTextView)
            orientation = a.getInt(R.styleable.HanYiYaKuTextView_tv_orientation, HORIZONTAL)

            val startColor = a.getColor(R.styleable.HanYiYaKuTextView_start_color, -1)
            val centerColor = a.getColor(R.styleable.HanYiYaKuTextView_center_color, -1)
            val endColor = a.getColor(R.styleable.HanYiYaKuTextView_end_color, -1)


            val strokeColor = a.getColor(R.styleable.HanYiYaKuTextView_stroke_color, -1)
            val strokeWidth = a.getDimension(R.styleable.HanYiYaKuTextView_stroke_width, 0f)


            val strokeStartColor = a.getColor(R.styleable.HanYiYaKuTextView_stroke_start_color, -1)
            val strokeEndColor = a.getColor(R.styleable.HanYiYaKuTextView_stroke_end_color, -1)


            mStrokeColor = if (strokeColor != -1) {
                startColor
            } else {
                Color.TRANSPARENT
            }

            val outLinePaint = outlineTextView?.paint
            outLinePaint?.strokeWidth = dp2px(context, strokeWidth).toFloat()// 描边宽度
            outLinePaint?.style = Paint.Style.STROKE
            outlineTextView?.setTextColor(Color.WHITE) // 描边颜色
            outlineTextView?.gravity = gravity
            outlineTextView?.typeface = FontManager.FONT_HANYI_YAKU



            if (startColor != -1 && centerColor != -1 && endColor != -1) {
                colorArray = intArrayOf(startColor, centerColor, endColor)
            } else if (startColor != -1 && endColor != -1) {
                colorArray = intArrayOf(startColor, endColor)
            }

            if (strokeStartColor != -1 && strokeEndColor != -1) {
                strokeColorArray = intArrayOf(strokeStartColor, strokeEndColor)
            }
            a.recycle()
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (colorArray != null) {
            val toString = text.toString()
            paint.getTextBounds(toString, 0, toString.length, rect)
            val startX = (w - rect.width()) / 2f
            val endX = startX + rect.width()
            if (orientation == HORIZONTAL) {
                paint.shader = LinearGradient(
                    startX,
                    0f,
                    endX,
                    0f,
                    colorArray!!,
                    null,
                    Shader.TileMode.CLAMP
                )
            } else if (orientation == VERTICAL) {
                paint.shader = LinearGradient(
                    0f,
                    0f,
                    0f,
                    h.toFloat(),
                    colorArray!!,
                    null,
                    Shader.TileMode.CLAMP
                )
            }
        }
        //描边渐变
        if (strokeColorArray != null) {
            val outLinearGradient = LinearGradient(0f,
                0f,
                0f,
                h.toFloat(),
                strokeColorArray!!,
                null,
                Shader.TileMode.CLAMP)
            outlineTextView?.paint?.shader = outLinearGradient
        }
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        super.setLayoutParams(params)
        outlineTextView?.layoutParams = params
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //设置轮廓文字
        val outlineText = outlineTextView?.text
        if (outlineText == null || outlineText != this.text) {
            outlineTextView?.text = text
            postInvalidate()
        }
        outlineTextView?.measure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        outlineTextView?.draw(canvas)
        super.onDraw(canvas)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        outlineTextView?.layout(left, top, right, bottom)
    }

    private fun dp2px(mContext: Context?, dp: Float): Int {
        if (mContext == null) {
            return 0
        }
        val scale = mContext.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}