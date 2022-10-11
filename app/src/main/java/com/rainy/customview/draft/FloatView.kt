package com.rainy.customview.draft

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.google.android.material.imageview.ShapeableImageView
import com.rainy.customview.R
import kotlin.math.abs

/**
 * @author jiangshiyu
 * @date 2022/10/11
 */
class FloatView : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        initView()
    }

    private var mDownX = 0F
    private var mDownY = 0F
    private var mFirstX: Int = 0
    private var mFirstY: Int = 0
    private var mViewWidth = 0
    private var mViewHeight = 0
    private var isMove = false

    private fun initView() {
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams = lp

        val imageView = ShapeableImageView(context)
        imageView.setImageResource(R.drawable.ic_avatar)

        addView(imageView)


        post {
            mViewWidth = this.width
            mViewHeight = this.height
        }
    }


    //拖拽
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = event.x
                mDownY = event.y

                //记录第一次在屏幕坐标
                mFirstX = event.rawX.toInt()
                mFirstY = event.rawY.toInt()
            }

            MotionEvent.ACTION_MOVE -> {
                if (y != null) {
                    offsetTopAndBottom((y - mDownY).toInt())
                }
                if (x != null) {
                    offsetLeftAndRight((x - mDownX).toInt())
                }
                isMove = true
            }

            MotionEvent.ACTION_UP -> {
                if (isMove) {
                    absLeftAndRight(event)
                }
                isMove = false
            }
        }

        return true
    }


    //上下吸顶
    private fun absTopAndBottom(event: MotionEvent) {
        if (isOriginTop()) {
            //上半部分
            val centerY = mViewHeight / 2 + abs(event.rawY - mFirstY)
            if (centerY < getScreenHeight() / 2) {
                //吸顶
                val topY = 0f
                animate().setInterpolator(DecelerateInterpolator()).setDuration(300).y(topY).start()
            } else {
                val bottomY = getScreenHeight() - mViewHeight
                animate().setInterpolator(DecelerateInterpolator()).setDuration(300)
                    .y(bottomY.toFloat()).start()
            }
        } else {
            //下半部分
            val centerY = mViewHeight / 2 + abs(event.rawY - mFirstY)
            if (centerY < getScreenHeight() / 2) {
                //吸底
                val bottomY = getScreenHeight() - mViewHeight
                animate().setInterpolator(DecelerateInterpolator()).setDuration(300)
                    .y(bottomY.toFloat()).start()
            } else {
                val topY = 0f
                animate().setInterpolator(DecelerateInterpolator()).setDuration(300).y(topY).start()
            }
        }
    }

    //左右吸顶
    private fun absLeftAndRight(event: MotionEvent) {
        if (isOriginLeft()) {
            val centerX = mViewWidth / 2 + abs(event.rawX - mFirstX)
            if (centerX < getScreenWidth() / 2) {
                val left = 0f
                animate().setInterpolator(DecelerateInterpolator()).setDuration(300).x(left).start()
            } else {
                val right = getScreenWidth() - mViewWidth
                animate().setInterpolator(DecelerateInterpolator()).setDuration(300)
                    .x(right.toFloat()).start()
            }
        } else {
            val centerX = mViewWidth / 2 + abs(event.rawX - mFirstX)
            if (centerX < getScreenWidth() / 2) {
                val right = getScreenWidth() - mViewWidth
                animate().setInterpolator(DecelerateInterpolator()).setDuration(300)
                    .x(right.toFloat()).start()
            } else {
                val left = 0f
                animate().setInterpolator(DecelerateInterpolator()).setDuration(300).x(left).start()

            }
        }
    }

    /**
     * 初始位置是否在顶部
     */
    private fun isOriginTop(): Boolean {
        return mFirstY < getScreenHeight() / 2
    }


    /**
     * 初始位置是否在左边
     */
    private fun isOriginLeft(): Boolean {
        return mFirstX < getScreenWidth() / 2
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    private fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

}