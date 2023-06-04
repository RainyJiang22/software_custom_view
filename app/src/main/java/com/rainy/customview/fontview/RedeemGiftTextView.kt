package com.rainy.customview.fontview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView


open class RedeemGiftTextView : AppCompatTextView {

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

    private fun initView(attrs: AttributeSet?) {
        outlineTextView = AppCompatTextView(context, attrs)

        val paint = outlineTextView?.paint
        paint?.strokeWidth = dp2px(context, 1f).toFloat()// 描边宽度
        paint?.style = Paint.Style.STROKE
        outlineTextView?.setTextColor(Color.parseColor("#58E1FF")) // 描边颜色
        outlineTextView?.gravity = gravity
        outlineTextView?.typeface = FontManager.FONT_HANYI_YAKU


        setTextType()
    }

    private fun setTextType() {
        typeface = FontManager.FONT_HANYI_YAKU
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //渐变
        paint.shader = LinearGradient(0f,
            0f,
            0f,
            h.toFloat(),
            Color.parseColor("#FFFFFFFF"),
            Color.parseColor("#FFDCF7FF"),
            Shader.TileMode.CLAMP)
//        //阴影
//        shadowTextView?.paint?.setShadowLayer(0.1f,
//            0f,
//            dp2px(context, 4f).toFloat(),
//            Color.parseColor("#C92C75CB"))

//        //描边
//        val outLinearGradient = LinearGradient(0f,
//            0f,
//            0f,
//            h.toFloat(),
//            Color.parseColor("#FF58E1FF"),
//            Color.parseColor("#FF58AAFF"),
//            Shader.TileMode.CLAMP)
//        outlineTextView?.paint?.shader = outLinearGradient


    }


    override fun setLayoutParams(params: ViewGroup.LayoutParams) {
        super.setLayoutParams(params)
        outlineTextView?.layoutParams = params
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // 设置轮廓文字
        val outlineText = outlineTextView?.text
        if (outlineText == null || outlineText != this.text) {
            outlineTextView?.text = text
            postInvalidate()
        }
        outlineTextView?.measure(widthMeasureSpec, heightMeasureSpec)

    }

//    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        super.onLayout(changed, left, top, right, bottom)
//        outlineTextView?.layout(left, top, right, bottom)
//    }

    override fun onDraw(canvas: Canvas) {
        outlineTextView?.draw(canvas)
        super.onDraw(canvas)
    }

    private fun dp2px(mContext: Context?, dp: Float): Int {
        if (mContext == null) {
            return 0
        }
        val scale = mContext.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}

