package com.rainy.customview.scenery

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import com.rainy.customview.R
import kotlin.math.cos
import kotlin.math.sin


/**
 * @author jiangshiyu
 * @date 2022/8/5
 */


/**
 * 整体思路
圆形背景
太阳(圆形)
山(三角形)
云朵(圆角矩形 + 三个圆)
需要进行的动画：

太阳 - 旋转动画
山 - 上下平移动画
云朵 - 左右平移动画
 */
class DribbleSceneryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val CLOUD_SCALE_RATIO = 0.85f
        private val DEFAULT_SUN_COLOR: Int = Color.YELLOW
        private val DEFAULT_LEFT_RIGHT_MOU_COLOR: Int = Color.parseColor("#E6E6E8")
        private val DEFAULT_MIDDLE_MOU_COLOR: Int = Color.WHITE
        private val DEFAULT_BACKGROUND_COLOR: Int = Color.parseColor("#6ABDE8")
        private val DEFAULT_CLOUD_COLOR: Int = Color.parseColor("#B6C4F3")

    }


    private var mParentWidth = 394
    private var mParentHeight = 394
    private var mViewCircle = 0f
    private var mSunAnimCircle = 0f
    private var mSunAnimX = 0f
    private var mSunAnimY = 0f
    private var mSunAnimXY: IntArray = intArrayOf()
    private var mIsStart = false

    private var mSunAnimatorValue = -120f
    private var mMaxCloudTranslationX = 0f
    private var mMaxMouTranslationY = 0f
    private var mCloudAnimatorValue = 0f
    private var mLeftMouAnimatorValue = 0f
    private var mRightMouAnimatorValue = 0f
    private var mMidMouAnimatorValue = 0f

    private var mRoundPath = Path()
    private var mSunPath = Path()
    private var mSunComputePath = Path()
    private var mLeftMountainPath = Path()
    private var mLeftComputePath = Path()
    private var mRightMountainPath = Path()
    private var mRightComputePath = Path()
    private var mMidMountainPath = Path()
    private var mMidComputePath = Path()
    private var mCloudPath = Path()
    private var mCloudComputePath = Path()


    private var mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mSunPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mLeftMountainPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mMidMountainPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mRightMountainPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mCloudPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mBackgroundColor = DEFAULT_BACKGROUND_COLOR
    private var mSunColor = DEFAULT_SUN_COLOR
    private var mCloudColor = DEFAULT_CLOUD_COLOR
    private var mLeftMouColor = DEFAULT_LEFT_RIGHT_MOU_COLOR
    private var mRightMouColor = DEFAULT_LEFT_RIGHT_MOU_COLOR
    private var mMidMouColor = DEFAULT_MIDDLE_MOU_COLOR

    private val mSunComputeMatrix = Matrix()
    private val mLeftComputeMatrix = Matrix()
    private val mRightComputeMatrix = Matrix()
    private val mMidComputeMatrix = Matrix()
    private val mCloudComputeMatrix = Matrix()

    init {
        //初始化动画属性
        initAttrs(context, attrs)
        //初始化view
        init()
    }

    @SuppressLint("CustomViewStyleable")
    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.SceneryView)
        //太阳颜色
        mSunColor = typedArray.getColor(R.styleable.SceneryView_sun_color, DEFAULT_SUN_COLOR)

        //左边山的颜色
        mLeftMouColor = typedArray.getColor(
            R.styleable.SceneryView_left_mountain_color,
            DEFAULT_LEFT_RIGHT_MOU_COLOR
        )

        //右边山的颜色
        mRightMouColor = typedArray.getColor(
            R.styleable.SceneryView_right_mountain_color,
            DEFAULT_LEFT_RIGHT_MOU_COLOR
        )

        //中间山的颜色
        mMidMouColor = typedArray.getColor(
            R.styleable.SceneryView_mid_mountain_color,
            DEFAULT_MIDDLE_MOU_COLOR
        )
        //云朵颜色
        mCloudColor = typedArray.getColor(R.styleable.SceneryView_cloud_color, DEFAULT_CLOUD_COLOR)

        //背景颜色
        mBackgroundColor =
            typedArray.getColor(R.styleable.SceneryView_background_color, DEFAULT_BACKGROUND_COLOR)
        typedArray.recycle()
    }

    private fun init() {
        mBackgroundPaint.color = mBackgroundColor
        mSunPaint.color = mSunColor
        mLeftMountainPaint.color = mLeftMouColor
        mMidMountainPaint.color = mMidMouColor
        mRightMountainPaint.color = mRightMouColor
        mCloudPaint.color = mCloudColor
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //绘制圆形背景
        mRoundPath.reset()
        mRoundPath.addCircle(mViewCircle, mViewCircle, mViewCircle, Path.Direction.CW)
        canvas?.clipPath(mRoundPath)
        canvas?.drawCircle(mViewCircle, mViewCircle, mViewCircle, mBackgroundPaint)

        //太阳
        mSunComputeMatrix.reset()
        mSunComputePath.reset()
        // x y 坐标
        // x y 坐标
        val circleXY = getCircleXY(
            mSunAnimX.toInt(), mSunAnimY.toInt(),
            mSunAnimCircle.toInt(), mSunAnimatorValue
        )
        mSunComputeMatrix.postTranslate(
            (circleXY[0] - mSunAnimXY[0]).toFloat(),
            (circleXY[1] - mSunAnimXY[1]).toFloat()
        )
        mSunPath.transform(mSunComputeMatrix, mSunComputePath)
        canvas?.drawPath(mSunComputePath, mSunPaint)


        // 左边的山
        mLeftComputeMatrix.reset()
        mLeftComputePath.reset()
        mLeftComputeMatrix.postTranslate(0f, mMaxMouTranslationY * mLeftMouAnimatorValue)
        mLeftMountainPath.transform(mLeftComputeMatrix, mLeftComputePath)
        canvas?.drawPath(mLeftComputePath, mLeftMountainPaint)

        //右边的山
        mRightComputeMatrix.reset()
        mRightComputePath.reset()
        mRightComputeMatrix.postTranslate(0f, mMaxMouTranslationY * mRightMouAnimatorValue)
        mRightMountainPath.transform(mRightComputeMatrix, mRightComputePath)
        canvas?.drawPath(mRightComputePath, mRightMountainPaint)

        //中间的山
        mMidComputeMatrix.reset()
        mMidComputePath.reset()
        mMidComputeMatrix.postTranslate(0f, mMaxMouTranslationY * mMidMouAnimatorValue)
        mMidMountainPath.transform(mMidComputeMatrix, mMidComputePath)
        canvas?.drawPath(mMidComputePath, mMidMountainPaint)


        //云朵
        mCloudComputeMatrix.reset()
        mCloudComputePath.reset()
        mCloudComputeMatrix.postTranslate(mMaxCloudTranslationX * mCloudAnimatorValue, 0f)
        mCloudPath.transform(mCloudComputeMatrix, mCloudComputePath)
        canvas?.drawPath(mCloudComputePath, mCloudPaint)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //取宽高的最小值
        mParentWidth = width.coerceAtMost(height).also { mParentHeight = it }
        // View的半径
        mViewCircle = (mParentWidth shr 1).toFloat()
        drawSun()
        drawMou(mViewCircle.toInt(), (mViewCircle - getValue(10f)).toInt(), getValue(10f))
        drawCloud(mParentWidth + getValue(200f), mParentHeight)
    }


    /**
     * 绘制太阳
     */
    private fun drawSun() {
        //太阳图形的直径
        val sunWidth = getValue(70f)
        //太阳图形的半径
        val sunCircle = sunWidth / 2
        // sun动画半径 = (sun半径 + 80(sun距离中心点的高度) + 整个View的半径 + sun半径 + 20(sun距离整个View的最下沿的间距)) / 2
        mSunAnimCircle = (sunWidth + getValue(100f) + mViewCircle) / 2
        //sun动画圆心的x坐标
        mSunAnimX = mViewCircle
        //sun动画圆形的y坐标
        mSunAnimY = mSunAnimCircle + (mViewCircle - getValue(80f) - sunCircle)
        //起始位置
        mSunAnimXY =
            getCircleXY(mSunAnimX.toInt(), mSunAnimY.toInt(), mSunAnimCircle.toInt(), -120f)
        mSunPath.addCircle(
            mSunAnimXY[0].toFloat(), mSunAnimXY[1].toFloat(), sunCircle.toFloat(), Path.Direction.CW
        )
    }

    /**
     * 画中间的三座山
     */
    private fun drawMou(x: Int, y: Int, down: Int) {
        // 左右山 Y坐标相对于中心点下移多少
        val lrmYpoint = down + getValue(30f)
        // 左右山 X坐标相对于中心点左移或右移多少
        val lrdPoint = getValue(120f)
        // 左右山 山的一半的X间距是多少
        val lrBanDis = getValue(140f)
        // 中间山 山的一半的X间距是多少
        val lrBanGao = getValue(150f)

        //左边的山
        mLeftMountainPath.reset()
        // 起点
        mLeftMountainPath.moveTo((x - lrdPoint).toFloat(), (y + lrmYpoint).toFloat())
        mLeftMountainPath.lineTo(
            (x - lrdPoint + lrBanDis).toFloat(),
            (y + lrmYpoint + lrBanGao).toFloat()
        )
        mLeftMountainPath.lineTo(
            (x - lrdPoint - lrBanDis).toFloat(),
            (y + lrmYpoint + lrBanGao).toFloat()
        )
        // 使这些点构成封闭的多边形
        mLeftMountainPath.close()

        //右边的山
        mRightMountainPath.reset()
        mRightMountainPath.moveTo(
            (x + lrdPoint + getValue(10f)).toFloat(),
            (y + lrmYpoint).toFloat()
        )
        mRightMountainPath.lineTo(
            (x + lrdPoint + getValue(10f) + lrBanDis).toFloat(),
            (y + lrmYpoint + lrBanGao).toFloat()
        )
        mRightMountainPath.lineTo(
            (x + lrdPoint + getValue(10f) - lrBanDis).toFloat(),
            (y + lrmYpoint + lrBanGao).toFloat()
        )
        mRightMountainPath.close()


        // 中间的山
        mMidMountainPath.reset()
        mMidMountainPath.moveTo(x.toFloat(), (y + down).toFloat());
        mMidMountainPath.lineTo(
            (x + getValue(220f)).toFloat(),
            (y + down + mParentHeight / 2 + mParentHeight / 14).toFloat()
        )
        mMidMountainPath.lineTo(
            (x - getValue(220f)).toFloat(),
            (y + down + mParentHeight / 2 + mParentHeight / 14).toFloat()
        )
        mMidMountainPath.close()

        // 左右山移动的距离
        mMaxMouTranslationY = (y + down + mViewCircle) / 14

    }

    /**
     * 画云朵
     */
    private fun drawCloud(w: Int, h: Int) {
        mCloudPath.reset()
        // 云的宽度
        val leftCloudWidth = mParentWidth / 1.4f
        // 云的 最底下圆柱的高度
        val leftCloudBottomHeight = leftCloudWidth / 3f
        // 云的 最底下圆柱的半径
        val leftCloudEndX =
            (w - leftCloudWidth - leftCloudWidth * CLOUD_SCALE_RATIO / 2) / 2 + leftCloudWidth
        val leftCloudEndY: Float =
            (h / 3 + getValue(342f)).toFloat()

        //add the bottom round rect
        mCloudPath.addRoundRect(
            RectF(
                leftCloudEndX - leftCloudWidth, leftCloudEndY - leftCloudBottomHeight,
                leftCloudEndX, leftCloudEndY
            ), leftCloudBottomHeight, leftCloudBottomHeight, Path.Direction.CW
        )

        val leftCloudTopCenterY = leftCloudEndY - leftCloudBottomHeight
        val leftCloudRightTopCenterX = leftCloudEndX - leftCloudBottomHeight
        val leftCloudLeftTopCenterX = leftCloudEndX - leftCloudWidth + leftCloudBottomHeight

        // 最右边的云
        mCloudPath.addCircle(
            leftCloudRightTopCenterX + getValue(12f),
            leftCloudTopCenterY + getValue(14f),
            leftCloudBottomHeight * 3 / 4,
            Path.Direction.CW
        )
        // 中间的云
        mCloudPath.addCircle(
            (leftCloudRightTopCenterX + leftCloudLeftTopCenterX - getValue(23f)) / 2 - getValue(
                10f
            ),
            leftCloudTopCenterY - getValue(0f),
            leftCloudBottomHeight / 7,
            Path.Direction.CW
        )
        // 左边的云
        mCloudPath.addCircle(
            leftCloudLeftTopCenterX - getValue(32f),
            leftCloudTopCenterY + getValue(16f),
            leftCloudBottomHeight / 2,
            Path.Direction.CW
        )

        mMaxCloudTranslationX = leftCloudBottomHeight / 2
    }


    /**
     * 得到父布局为 150dp 时的对应值
     *
     * @param originalValue 原始值
     */
    private fun getValue(originalValue: Float): Int {
        return (mParentWidth / (394 / originalValue)).toInt()
    }

    /**
     * sun旋转的时候，圆上的点，起点是最右边的点，顺时针
     * x1   =   x0   +   r   *   cos(a   *   PI  /180  )
     * y1   =   y0   +   r   *   sin(a   *   PI  /180  )
     * y1  = y0 + r * sin(a * PI / 180)
     */
    private fun getCircleXY(
        circleCenterX: Int,
        circleCenterY: Int,
        circleR: Int,
        angle: Float
    ): IntArray {
        val x = (circleCenterX + circleR * cos(angle * Math.PI / 180)).toInt()
        val y = (circleCenterY + circleR * sin(angle * Math.PI / 180)).toInt()
        return intArrayOf(x, y)
    }


    /**
     * 开始动画
     */
    fun playAnimator() {
        if (!mIsStart) {
            mIsStart = true
            playCloudMouAnimator(true)
            setSunAnimator()
        }
    }


    /**
     * 开始云和山的动画
     */
    private fun playCloudMouAnimator(isFirst: Boolean) {
        setCloudAnimator(isFirst)
        setLeftMouAnimator(isFirst)
        setRightMouAnimator(isFirst)
        setMidMouAnimator(isFirst)
    }

    /**
     * sun的动画 属性动画
     */
    @SuppressLint("Recycle")
    private fun setSunAnimator() {
        val mSunAnimator = ValueAnimator.ofFloat(-120f, 240f)
        mSunAnimator.duration = 2700
        mSunAnimator.interpolator = AccelerateDecelerateInterpolator()
        mSunAnimator.addUpdateListener { p0 ->
            mSunAnimatorValue = p0?.animatedValue as Float
            postInvalidate()
        }
        mSunAnimator.addListener(object : AnimationListenerAdapter() {
            override fun onAnimationEnd(p0: Animator?) {
                super.onAnimationEnd(p0)
                mIsStart = false
                if (listener != null) {
                    listener?.onAnimationEnd()
                }
            }
        })
        mSunAnimator.start()
    }


    /**
     * 左边山的动画
     */
    private fun setLeftMouAnimator(isFirst: Boolean) {
        val mLeftRightMouAnimator = if (isFirst) {
            ValueAnimator.ofFloat(0f, -1f, 10f)
        } else {
            ValueAnimator.ofFloat(10f, 0f)
        }
        mLeftRightMouAnimator.startDelay = 100
        mLeftRightMouAnimator.duration = 800
        mLeftRightMouAnimator.interpolator = AccelerateInterpolator()
        mLeftRightMouAnimator.addUpdateListener {
            mLeftMouAnimatorValue = it.animatedValue as Float
            postInvalidate()
        }
        mLeftRightMouAnimator.start()
    }

    /**
     * 中间山的动画
     */
    private fun setMidMouAnimator(isFirst: Boolean) {
        val mMidMouAnimator: ValueAnimator
        if (isFirst) {
            mMidMouAnimator = ValueAnimator.ofFloat(0f, -1f, 10f)
            mMidMouAnimator.startDelay = 200
            mMidMouAnimator.duration = 1000
        } else {
            mMidMouAnimator = ValueAnimator.ofFloat(10f, 0f)
            mMidMouAnimator.startDelay = 0
            mMidMouAnimator.duration = 600
        }
        mMidMouAnimator.interpolator = AccelerateInterpolator()
        mMidMouAnimator.addUpdateListener {
            mMidMouAnimatorValue = it.animatedValue as Float
        }
        mMidMouAnimator.addListener(object : AnimationListenerAdapter() {
            override fun onAnimationEnd(p0: Animator?) {
                super.onAnimationEnd(p0)
                if (isFirst) {
                    postDelayed({
                        playCloudMouAnimator(false)
                    }, 100)
                }
            }
        })
        mMidMouAnimator.start()
    }


    /**
     * 右边山的动画和左边一致
     */
    private fun setRightMouAnimator(isFirst: Boolean) {
        val mLeftRightMouAnimator = if (isFirst) {
            ValueAnimator.ofFloat(0f, -1f, 10f)
        } else {
            ValueAnimator.ofFloat(10f, 0f)
        }
        mLeftRightMouAnimator.startDelay = 100
        mLeftRightMouAnimator.duration = 800
        mLeftRightMouAnimator.interpolator = AccelerateInterpolator()
        mLeftRightMouAnimator.addUpdateListener {
            mRightMouAnimatorValue = it.animatedValue as Float
            postInvalidate()
        }
        mLeftRightMouAnimator.start()
    }


    /**
     * 云朵动画
     */
    private fun setCloudAnimator(isFirst: Boolean) {
        val mLeftCloudAnimator: ValueAnimator
        if (isFirst) {
            mLeftCloudAnimator = ValueAnimator.ofFloat(0f, 5f)
            mLeftCloudAnimator.startDelay = 0
        } else {
            mLeftCloudAnimator = ValueAnimator.ofFloat(-8f, 0f)
            mLeftCloudAnimator.startDelay = 200
        }
        mLeftCloudAnimator.duration = 800
        mLeftCloudAnimator.interpolator = AccelerateInterpolator()
        mLeftCloudAnimator.addUpdateListener {
            mCloudAnimatorValue = it.animatedValue as Float
            postInvalidate()
        }
        mLeftCloudAnimator.addListener(object : AnimationListenerAdapter() {
            override fun onAnimationEnd(p0: Animator?) {
                super.onAnimationEnd(p0)
            }
        })
        mLeftCloudAnimator.start()
    }

    private var listener: AnimationListener? = null

    fun setOnAnimationListener(listener: AnimationListener?) {
        this.listener = listener
    }

    interface AnimationListener {
        fun onAnimationEnd()
    }
}


