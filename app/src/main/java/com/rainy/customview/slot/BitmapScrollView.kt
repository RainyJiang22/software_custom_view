package com.rainy.customview.slot

import android.content.Context

import kotlin.jvm.JvmOverloads
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import com.rainy.customview.R
import kotlin.math.abs

/**
 * 图片滚动选择器
 * @author jiangshiyu
 * @date 2022/2/6
 */
class BitmapScrollView @JvmOverloads constructor(
    context: Context?, attrs: AttributeSet?,
    defStyleAttr: Int = 0,
) : SlotScrollView<Bitmap?>(context, attrs, defStyleAttr) {


    companion object {
        /**
         * 图片绘制模式：填充
         */
        const val DRAW_MODE_FULL = 1

        /**
         * 图片绘制模式：居中
         */
        const val DRAW_MODE_CENTER = 2

        /**
         * 图片绘制模式：指定大小
         */
        const val DRAW_MODE_SPECIFIED_SIZE = 3
    }

    private var mMeasureWidth = 0
    private var mMeasureHeight = 0

    private val mRect1: Rect = Rect()
    private val mRect2: Rect = Rect()
    private val mSpecifiedSizeRect: Rect = Rect()
    private val mRectTemp: Rect = Rect()


    private var mDrawMode = DRAW_MODE_CENTER

    // item内容缩放倍数
    var minScale = 1f
        private set

    //被选中的时候为原来的2倍
    var maxScale = 2f
        private set


    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BitmapScrollView)
            mDrawMode = typedArray.getInt(
                R.styleable.BitmapScrollView_spv_draw_bitmap_mode, mDrawMode)
            mSpecifiedSizeWidth = typedArray.getDimensionPixelOffset(
                R.styleable.BitmapScrollView_spv_draw_bitmap_width, mSpecifiedSizeWidth)
            mSpecifiedSizeHeight = typedArray.getDimensionPixelOffset(
                R.styleable.BitmapScrollView_spv_draw_bitmap_height, mSpecifiedSizeHeight)
            minScale = typedArray.getFloat(R.styleable.BitmapScrollView_spv_min_scale, minScale)
            maxScale = typedArray.getFloat(R.styleable.BitmapScrollView_spv_max_scale, maxScale)
            typedArray.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mMeasureWidth = measuredWidth
        mMeasureHeight = measuredHeight
        // 当垂直滚动时，item的左边和右边的坐标x可确定
        when (mDrawMode) {
            // 填充
            DRAW_MODE_FULL -> {
                mRect2.left = 0
                mRect2.right = mMeasureWidth
            }
            // 指定大小
            DRAW_MODE_SPECIFIED_SIZE -> {
                if (mSpecifiedSizeWidth == -1) {
                    mSpecifiedSizeWidth = mMeasureWidth
                    mSpecifiedSizeHeight = mMeasureHeight
                }
                setDrawModeSpecifiedSize(mSpecifiedSizeWidth, mSpecifiedSizeHeight)
            }
            // 居中
            else -> {
                val size: Int = mMeasureWidth.coerceAtMost(itemHeight)
                mRect2.left = mMeasureWidth / 2 - size / 2
                mRect2.right = mMeasureWidth / 2 + size / 2
            }
        }
    }

    // 缩放item内容
    // 需要上下size为中间的一半position 2022/12/6
    private fun scale(
        rect: Rect,
        relative: Int,
        itemSize: Int,
        moveLength: Float,
        isTopBottom: ((isTopBottom: Boolean) -> Unit?)? = null,
    ) {

        var topBottom = false
        if (minScale == 1f && maxScale == 1f) {
            return
        }
        val spanWidth: Float
        val spanHeight: Float
        if (minScale == maxScale) {
            spanWidth = (rect.width() - minScale * rect.width()) / 2
            spanHeight = (rect.height() - minScale * rect.height()) / 2
            rect.left += spanWidth.toInt()
            rect.right -= spanWidth.toInt()
            rect.top += spanHeight.toInt()
            rect.bottom -= spanHeight.toInt()
            return
        }
        when (relative) {
            -1, 1 -> { // 上一个或下一个
                // 处理上一个item且向上滑动　或者　处理下一个item且向下滑动,
                if (relative == -1 && moveLength < 0
                    || relative == 1 && moveLength > 0
                ) {
                    spanWidth = (rect.width() - minScale * rect.width()) / 2
                    spanHeight = (rect.height() - minScale * rect.height()) / 2
                } else { // 计算渐变
                    val rate = abs(moveLength) / itemSize
                    spanWidth =
                        (rect.width() - (minScale + (maxScale - minScale) * rate) * rect.width()) / 2
                    spanHeight =
                        (rect.height() - (minScale + (maxScale - minScale) * rate) * rect.height()) / 2
                }
                topBottom = true
            }
            0 -> { // 中间item
                val rate = (itemSize - abs(moveLength)) / itemSize
                spanWidth =
                    (rect.width() - (minScale + (maxScale - minScale) * rate) * rect.width()) / 2
                spanHeight =
                    (rect.height() - (minScale + (maxScale - minScale) * rate) * rect.height()) / 2
                topBottom = false
            }
            else -> {
                spanWidth = (rect.width() - minScale * rect.width()) / 2
                spanHeight = (rect.height() - minScale * rect.height()) / 2
                topBottom = false
            }
        }
        rect.left += spanWidth.toInt()
        rect.right -= spanWidth.toInt()
        rect.top += spanHeight.toInt()
        rect.bottom -= spanHeight.toInt()

        isTopBottom?.invoke(topBottom)
    }

    private var mSpecifiedSizeWidth = -1
    private var mSpecifiedSizeHeight = -1
    fun setDrawModeSpecifiedSize(width: Int, height: Int) {
        mSpecifiedSizeRect.left = (mMeasureWidth - width) / 2
        mSpecifiedSizeRect.right = (mMeasureWidth - width) / 2 + width
        mSpecifiedSizeWidth = width
        mSpecifiedSizeHeight = height
        invalidate()
    }

    /**
     * 图片绘制模式 ，默认为居中
     *
     * @param mode
     */
    var drawMode: Int
        get() = mDrawMode
        set(mode) {
            val size: Int =
                mMeasureWidth.coerceAtMost(itemHeight)
            mDrawMode = mode
            when (mDrawMode) {
                DRAW_MODE_FULL -> {
                    mRect2.left = 0
                    mRect2.right = mMeasureWidth
                }
                DRAW_MODE_SPECIFIED_SIZE -> {
                    //do nothing
                }
                else -> {
                    mRect2.left = mMeasureWidth / 2 - size / 2
                    mRect2.right = mMeasureWidth / 2 + size / 2
                }
            }
            invalidate()
        }

    override fun drawItem(
        canvas: Canvas?,
        data: List<Bitmap?>?,
        position: Int,
        relative: Int,
        moveLength: Float,
        top: Float,
    ) {
        val itemSize = itemSize
        val bitmap = data!![position]
        mRect1.right = bitmap!!.width
        mRect1.bottom = bitmap.height
        var span = 0

        // 根据不同的绘制模式，计算出item内容的最终绘制位置和大小
        // 当垂直滚动时，item的顶部和底部的坐标y
        when (mDrawMode) {
            // 填充
            DRAW_MODE_FULL -> {
                span = 0
                mRect2.top = top.toInt() + span
                mRect2.bottom = (top + itemSize - span).toInt()
                mRectTemp.set(mRect2)
                scale(mRectTemp, relative, itemSize, moveLength)
                canvas?.drawBitmap(bitmap, mRect1, mRectTemp, null)
            }
            // 指定大小
            DRAW_MODE_SPECIFIED_SIZE -> {
                span = (itemSize - mSpecifiedSizeHeight) / 2
                mSpecifiedSizeRect.top = top.toInt() + span
                mSpecifiedSizeRect.bottom = top.toInt() + span + mSpecifiedSizeHeight
                mRectTemp.set(mSpecifiedSizeRect)
                scale(mRectTemp, relative, itemSize, moveLength) {
                    if (it) {
                        val paint = Paint()
                        //60%透明
                        paint.alpha = (255 * 0.6).toInt()
                        canvas?.drawBitmap(bitmap, mRect1, mRectTemp, paint)
                    } else {
                        //绘制上下的不透明度
                        canvas?.drawBitmap(bitmap, mRect1, mRectTemp, null)
                    }
                }
            }
            // 居中
            else -> {
                val scale = mRect2.width() * 1f / bitmap.width
                span = ((itemSize - bitmap.height * scale) / 2).toInt()
                mRect2.top = (top + span).toInt()
                mRect2.bottom = (top + itemSize - span).toInt()
                mRectTemp.set(mRect2)
                scale(mRectTemp, relative, itemSize, moveLength)
                canvas?.drawBitmap(bitmap, mRect1, mRectTemp, null)
            }
        }
    }

    /**
     * item内容缩放倍数
     *
     * @param minScale 沒有被选中时的最小倍数
     * @param maxScale 被选中时的最大倍数
     */
    fun setItemScale(minScale: Float, maxScale: Float) {
        this.minScale = minScale
        this.maxScale = maxScale
        invalidate()
    }
}