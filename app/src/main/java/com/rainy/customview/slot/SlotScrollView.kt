package com.rainy.customview.slot

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Interpolator
import android.widget.Scroller
import com.blankj.utilcode.util.ConvertUtils.dp2px
import com.rainy.customview.R
import java.util.ArrayList
import kotlin.math.abs

/**
 * @author jiangshiyu
 * @date 2023/3/22
 */
abstract class SlotScrollView<T> @JvmOverloads constructor(
    context: Context?, attrs: AttributeSet?,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    //可见item数量
    private var mVisibleItemCount = 3

    var isInertiaScroll = true

    private var isIsCirculation = true

    var isDisallowInterceptTouch = false

    //当前选中的item下标
    private var mSelected = 0

    private var mData: List<T>? = null

    //高度
    var itemHeight = 0

    //宽度
    var itemWidth = 0

    var itemSize = 0

    private var mCenterPosition = -1

    //中间item的起始坐标y
    private var centerY = 0

    //中间坐标的起始坐标x
    private var centerX = 0

    /**
     * 当垂直滚动时，mCenterPoint = mCenterY;水平滚动时，mCenterPoint = mCenterX
     */
    private var centerPoint = 0

    // item移动长度，负数表示向上移动，正数表示向下移动 computeScrollTo()
    private var mMoveLength = 0f

    private val mScroller: Scroller

    //是否正在惯性滑动
    private var isFling = false

    //是否滑向中间
    private var isMovingCenter = false

    // Scroller的坐标y
    private var mLastScrollY = 0

    // Scroller的坐标x
    private var mLastScrollX = 0

    private var isAutoScrolling = false

    private val mAutoScrollAnimator: ValueAnimator

    private val mPaint: Paint

    //中间选中item的背景色
    private var mCenterItemBackground: Drawable? = null

    //是否绘制全部item
    private var isDrawAllItem = false

    private var listener: OnSelectedListener? = null

    //用于标志第一次设置selected时把事件通知给监听器
    private var mHasCallSelectedListener = false

    /**
     * 中间item的位置,默认为 mVisibleItemCount / 2
     *
     * @return
     */
    var visibleItemCount: Int
        get() = mVisibleItemCount
        set(visibleItemCount) {
            mVisibleItemCount = visibleItemCount
            reset()
            invalidate()
        }

    /**
     * 中间item的位置，0 <= centerPosition <= mVisibleItemCount
     */
    var centerPosition: Int
        get() = mCenterPosition
        set(centerPosition) {
            mCenterPosition = if (centerPosition < 0) {
                0
            } else if (centerPosition >= mVisibleItemCount) {
                mVisibleItemCount - 1
            } else {
                centerPosition
            }
            centerY = mCenterPosition * itemHeight
            invalidate()
        }


    var centerItemBackground: Drawable?
        get() = mCenterItemBackground
        set(centerItemBackground) {
            mCenterItemBackground = centerItemBackground
            mCenterItemBackground?.setBounds(centerX,
                centerY,
                centerX + itemWidth,
                centerY + itemHeight)
            invalidate()
        }


    var data: List<T>?
        get() = mData
        set(data) {
            mData = data ?: ArrayList()
            mSelected = (mData?.size?.div(2)) ?: 0
            invalidate()
        }
    val selectedItem: T?
        get() = mData?.get(mSelected)

    //当前设置居中的位置
    var selectedPosition: Int
        get() = mSelected
        set(position) {
            if (position < 0 || position > mData!!.size - 1 || position == mSelected && mHasCallSelectedListener) {
                return
            }
            mHasCallSelectedListener = true
            mSelected = position
            invalidate()
            notifySelected()
        }

    companion object {
        private val sAutoScrollInterpolator = SlotInterpolator()
    }

    init {
        mScroller = Scroller(getContext())
        mAutoScrollAnimator = ValueAnimator.ofInt(0, 0)
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.style = Paint.Style.FILL
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollSlotView)
            if (typedArray.hasValue(R.styleable.ScrollSlotView_spv_center_item_background)) {
                centerItemBackground =
                    typedArray.getDrawable(R.styleable.ScrollSlotView_spv_center_item_background)
            }
            visibleItemCount = typedArray.getInt(
                R.styleable.ScrollSlotView_spv_visible_item_count, visibleItemCount)
            centerPosition = typedArray.getInt(
                R.styleable.ScrollSlotView_spv_center_item_position, centerPosition)
            setIsCirculation(typedArray.getBoolean(
                R.styleable.ScrollSlotView_spv_is_circulation, isIsCirculation))
            isDisallowInterceptTouch =
                typedArray.getBoolean(R.styleable.ScrollSlotView_spv_disallow_intercept_touch,
                    isDisallowInterceptTouch)
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (mData == null || mData?.isEmpty() == true) {
            return
        }

        if (mCenterItemBackground != null) {
            mCenterItemBackground?.draw(canvas)
        }

        val length = (mCenterPosition + 1).coerceAtLeast(mVisibleItemCount - mCenterPosition)
        var position: Int
        var start = mData?.size?.let { length.coerceAtMost(it) }
        if (isDrawAllItem) {
            start = mData?.size
        }

        //上下两边
        if (start != null) {
            for (i in start downTo 1) {
                if (isDrawAllItem || i <= mCenterPosition + 1) {
                    position =
                        if (mSelected - i < 0) mData!!.size + mSelected - i else mSelected - i
                    if (isIsCirculation) {
                        drawItem(canvas,
                            mData,
                            position,
                            -i,
                            mMoveLength,
                            centerPoint + mMoveLength - i * itemSize)
                    } else if (mSelected - i >= 0) { // 非循环滚动
                        drawItem(canvas,
                            mData,
                            position,
                            -i,
                            mMoveLength,
                            centerPoint + mMoveLength - i * itemSize)
                    }
                }

                if (isDrawAllItem || i <= mVisibleItemCount - mCenterPosition) {
                    position =
                        if (mSelected + i >= mData!!.size) mSelected + i - mData!!.size else mSelected + i
                    // 传入位置信息，绘制item
                    if (isIsCirculation) {
                        drawItem(canvas,
                            mData,
                            position,
                            i,
                            mMoveLength,
                            centerPoint + mMoveLength + i * itemSize)
                    } else if (mSelected + i < mData!!.size) { // 非循环滚动
                        drawItem(canvas,
                            mData,
                            position,
                            i,
                            mMoveLength,
                            centerPoint + mMoveLength + i * itemSize)
                    }
                }

            }
        }

        //选中的item
        drawItem(canvas, mData, mSelected, 0, mMoveLength, centerPoint + mMoveLength)
    }

    /**
     * 绘制item
     *
     * @param canvas
     * @param data       　数据集
     * @param position   在data数据集中的位置
     * @param relative   相对中间item的位置,relative==0表示中间item,relative<0表示上（左）边的item,relative>0表示下(右)边的item
     * @param moveLength 中间item滚动的距离，moveLength<0则表示向上（右）滚动的距离，moveLength＞0则表示向下（左）滚动的距离
     * @param top        当前绘制item的坐标,当垂直滚动时为顶部y的坐标；当水平滚动时为item最左边x的坐标
     */
    abstract fun drawItem(
        canvas: Canvas?,
        data: List<T>?,
        position: Int,
        relative: Int,
        moveLength: Float,
        top: Float,
    )


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        reset()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }


    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            //正在滚动
            mMoveLength = mMoveLength + mScroller.currY - mLastScrollY

            mLastScrollY = mScroller.currY
            mLastScrollX = mScroller.currX
            checkCirculation()
            invalidate()
        } else {
            //滚动完成
            if (isFling) {
                isFling = false
                if (mMoveLength == 0f) {
                    notifySelected()
                } else {
                    //滚动到中间的位置
                    moveToCenter()
                }
            } else if (isMovingCenter) {
                //回调
                notifySelected()
            }

        }
    }


    /**
     * 自动滚动
     */
    @JvmOverloads
    fun autoScrollFast(
        position: Int,
        duration: Long,
        speed: Float,
        interpolator: Interpolator? = sAutoScrollInterpolator,
        isUp: Boolean,
    ) {
        if (isAutoScrolling || !isIsCirculation) {
            return
        }
        cancelScroll()
        isAutoScrolling = true
        val length = (speed * duration).toInt()
        //圈数
        var circle = (length * 1f / (mData!!.size * itemSize) + 0.5f).toInt()
        circle = if (circle <= 0) 1 else circle
        val aPlan = circle * mData!!.size * itemSize + (mSelected - position) * itemSize
        val bPlan = aPlan + mData!!.size * itemSize // 多一圈

        //向上滚动圈数
        val aPlanUp = circle * mData!!.size * itemSize + (position - mSelected) * itemSize
        val bPlanUp = aPlanUp + mData!!.size * itemSize
        // 让其尽量接近length
        val end = if (abs(length - aPlan) < abs(length - bPlan)) aPlan else bPlan

        val endUp = if (abs(length - aPlanUp) < abs(length - bPlanUp)) aPlanUp else bPlanUp
        mAutoScrollAnimator.cancel()
        if (isUp) {
            mAutoScrollAnimator.setIntValues(0, -endUp)
        } else {
            mAutoScrollAnimator.setIntValues(0, end)
        }
        mAutoScrollAnimator.interpolator = interpolator
        mAutoScrollAnimator.duration = duration
        mAutoScrollAnimator.removeAllUpdateListeners()
        if (end != 0) { // itemHeight为0导致endy=0
            mAutoScrollAnimator.addUpdateListener { animation ->
                val rate: Float = animation.currentPlayTime * 1f / animation.duration
                if (isUp) {
                    computeScroll(animation.animatedValue as Int, rate)
                } else {
                    computeScroll(animation.animatedValue as Int, rate)
                }
            }
            mAutoScrollAnimator.removeAllListeners()
            mAutoScrollAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    isAutoScrolling = false
                }
            })
            mAutoScrollAnimator.start()
        } else {
            if (isUp) {
                computeScroll(-endUp, 1f)
            } else {
                computeScroll(end, 1f)
            }
            isAutoScrolling = false
        }
    }

    /**
     * @param endY         　需要滚动到的位置
     * @param duration     　滚动时间
     * @param interpolator
     */
    fun autoScrollTo(endY: Int, duration: Long, interpolator: Interpolator?) {
        if (isAutoScrolling) {
            return
        }
        isAutoScrolling = true
        mAutoScrollAnimator.cancel()
        mAutoScrollAnimator.setIntValues(0, endY)
        mAutoScrollAnimator.interpolator = interpolator
        mAutoScrollAnimator.duration = duration
        mAutoScrollAnimator.removeAllUpdateListeners()
        mAutoScrollAnimator.addUpdateListener { animation: ValueAnimator ->
            val rate = animation.currentPlayTime * 1f / animation.duration
            computeScroll(animation.animatedValue as Int, rate)
        }
        mAutoScrollAnimator.removeAllListeners()
        mAutoScrollAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                isAutoScrolling = false
            }
        })
        mAutoScrollAnimator.start()
    }


    private fun computeScroll(curr: Int, rate: Float) {
        if (rate < 1) {
            //正在滚动
            mMoveLength = mMoveLength + curr - mLastScrollY
            mLastScrollY = curr
            checkCirculation()
            invalidate()
        } else {
            isMovingCenter = false
            mLastScrollY = 0
            mLastScrollX = 0

            // 直接居中，不通过动画
            mMoveLength = if (mMoveLength > 0) {
                // 向下滑动
                if (mMoveLength < itemSize / 2) {
                    0f
                } else {
                    itemSize.toFloat()
                }
            } else {
                //向上滑动
                if (-mMoveLength < itemSize / 2) {
                    0f
                } else {
                    -itemSize.toFloat()
                }
            }
            checkCirculation()
            notifySelected()
            invalidate()
        }
    }


    //移动到中间位置
    private fun moveToCenter() {
        if (!mScroller.isFinished || isFling || mMoveLength == 0f) {
            return
        }
        cancelScroll()

        // 向下滑动
        if (mMoveLength > 0) {
            if (mMoveLength < itemHeight / 2) {
                scroll(mMoveLength, 0)
            } else {
                scroll(mMoveLength, itemHeight)
            }
        } else {
            if (-mMoveLength < itemHeight / 2) {
                scroll(mMoveLength, 0)
            } else {
                scroll(mMoveLength, -itemHeight)
            }
        }
    }


    //检测当前移动到的item
    private fun checkCirculation() {
        if (mMoveLength >= itemSize) {
            //向下滚动
            val span = (mMoveLength / itemSize).toInt()
            mSelected -= span
            //滚动顶部，判断是否循环滚动
            if (mSelected < 0) {
                if (isIsCirculation) {
                    do {
                        mSelected += mData!!.size
                    } while (mSelected < 0) // 当越过的item数量超过一圈时
                    mMoveLength = (mMoveLength - itemSize) % itemSize
                } else { // 非循环滚动
                    mSelected = 0
                    mMoveLength = itemSize.toFloat()
                    if (isFling) { // 停止惯性滑动，根据computeScroll()中的逻辑，下一步将调用moveToCenter()
                        mScroller.forceFinished(true)
                    }
                    if (isMovingCenter) { //  移回中间位置
                        scroll(mMoveLength, 0)
                    }
                }
            } else {
                mMoveLength = (mMoveLength - itemSize) % itemSize
            }
        } else if (mMoveLength <= -itemSize) {
            //向上滚动
            // 该次滚动距离中越过的item数量
            val span = (-mMoveLength / itemSize).toInt()
            mSelected += span
            if (mSelected >= mData!!.size) { // 滚动末尾，判断是否循环滚动
                if (isIsCirculation) {
                    do {
                        mSelected -= mData!!.size
                    } while (mSelected >= mData!!.size) // 当越过的item数量超过一圈时
                    mMoveLength = (mMoveLength + itemSize) % itemSize
                } else { // 非循环滚动
                    mSelected = mData!!.size - 1
                    mMoveLength = -itemSize.toFloat()
                    if (isFling) { // 停止惯性滑动，根据computeScroll()中的逻辑，下一步将调用moveToCenter()
                        mScroller.forceFinished(true)
                    }
                    if (isMovingCenter) { //  移回中间位置
                        scroll(mMoveLength, 0)
                    }
                }
            } else {
                mMoveLength = (mMoveLength + itemSize) % itemSize
            }
        }

    }

    fun autoScrollFast(position: Int, duration: Long, isUp: Boolean) {
        val speed = dp2px(0.6f).toFloat()
        autoScrollFast(position, duration, speed, sAutoScrollInterpolator, isUp)
    }

    // 平滑滚动
    private fun scroll(from: Float, to: Int) {
        mLastScrollY = from.toInt()
        isMovingCenter = true
        mScroller.startScroll(0, from.toInt(), 0, 0)
        mScroller.finalY = to
        invalidate()
    }

    // 惯性滑动，
    private fun fling(from: Float, vel: Float) {
        mLastScrollY = from.toInt()
        isFling = true
        // 最多可以惯性滑动10个item
        mScroller.fling(0, from.toInt(), 0, vel.toInt(), 0, 0, -10 * itemHeight,
            10 * itemHeight)
        invalidate()
    }

    private fun notifySelected() {
        mMoveLength = 0f
        cancelScroll()
        if (listener != null) {
            listener?.onSelected(this@SlotScrollView, mSelected)
        }
    }


    private fun cancelScroll() {
        mLastScrollY = 0
        mLastScrollX = 0
        isMovingCenter = false
        isFling = isMovingCenter
        mScroller.abortAnimation()
        stopAutoScroll()
    }

    /**
     * 停止自动滚动
     */
    fun stopAutoScroll() {
        isAutoScrolling = false
        mAutoScrollAnimator.cancel()
    }


    private fun setIsCirculation(isCirculation: Boolean) {
        isIsCirculation = isCirculation
    }

    fun setCenterItemBackground(centerItemBackgroundColor: Int) {
        mCenterItemBackground = ColorDrawable(centerItemBackgroundColor)
        mCenterItemBackground?.setBounds(centerX,
            centerY,
            centerX + itemWidth,
            centerY + itemHeight)
        invalidate()
    }

    private fun reset() {
        if (mCenterPosition < 0) {
            mCenterPosition = mVisibleItemCount / 2
        }
        itemHeight = measuredHeight / mVisibleItemCount
        itemWidth = measuredWidth
        centerY = mCenterPosition * itemHeight
        centerX = 0
        itemSize = itemHeight
        centerPoint = centerY

        if (centerItemBackground != null) {
            mCenterItemBackground?.setBounds(centerX,
                centerY,
                centerX + itemWidth,
                centerY + itemHeight)
        }
    }

    fun setOnSelectedListener(listener: OnSelectedListener?) {
        this.listener = listener
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == VISIBLE) {
            moveToCenter()
        }
    }

}