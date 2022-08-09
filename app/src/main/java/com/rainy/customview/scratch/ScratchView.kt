package com.rainy.customview.scratch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.rainy.customview.R
import kotlin.math.abs


/**
 * @author jiangshiyu
 * @date 2022/8/9
 */
class ScratchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    //上面的画笔
    private val mOuterPaint = Paint()

    //遮罩的画笔
    private val mMaskPaint = Paint()

    //下面的画笔
    private val mBackPaint = Paint()

    //绘制的图片
    private var mSrcBitmap: Bitmap? = null

    /**
     * 记录用户绘制的Path
     */
    private val mPath: Path = Path()

    /**
     * 内存中创建的Canvas
     */
    private var mCanvas: Canvas? = null

    //是否刮卡完成
    private var isComplete = false
    private val mTextBound: Rect = Rect()
    private val mText = "￥500,0000"
    private var mLastX = 0
    private var mLastY = 0
    private var mWidth = 0
    private var mHeight = 0

    init {
        setUpPaint()
        setBackPaint()
    }


    private fun setBackPaint() {
        mBackPaint.style = Paint.Style.FILL
        mBackPaint.textScaleX = 2f
        mBackPaint.color = Color.DKGRAY
        mBackPaint.textSize = 32f
        mBackPaint.getTextBounds(mText, 0, mText.length, mTextBound)
    }

    private fun setUpPaint() {
        mOuterPaint.color = Color.parseColor("#c0c0c0")
        mOuterPaint.isAntiAlias = true
        mOuterPaint.isDither = true
        mOuterPaint.style = Paint.Style.STROKE
        mOuterPaint.strokeJoin = Paint.Join.ROUND
        mOuterPaint.strokeCap = Paint.Cap.ROUND
        // 设置画笔宽度
        mOuterPaint.strokeWidth = 50f
    }

    /**
     * 绘制线条
     */
    private fun drawPath() {
        mOuterPaint.style = Paint.Style.STROKE
        mOuterPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        mCanvas?.drawPath(mPath, mOuterPaint)
    }

    override fun onDraw(canvas: Canvas?) {
        if (!isComplete) {
            drawPath()
            mSrcBitmap?.let { canvas?.drawBitmap(it, 0f, 0f, null) }
        } else {
            this.visibility = GONE
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mWidth = measuredWidth
        mHeight = measuredHeight

        mSrcBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mSrcBitmap!!)
        //遮层透明
        mMaskPaint.color = Color.parseColor("#00000000")

        mMaskPaint.style = Paint.Style.FILL
        mCanvas?.drawRoundRect(
            RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat()),
            0f,
            0f,
            mMaskPaint
        )
        mCanvas?.drawBitmap(
            BitmapFactory.decodeResource(
                resources, R.drawable.award_1
            ), null, RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat()), null
        )
    }


    //使用path及其二阶贝塞尔曲线来实现圆润的曲线。(使用一阶时会有明显的转折痕迹，给人一种卡顿的现象）
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event!!.action
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = x
                mLastY = y
                mPath.moveTo(mLastX.toFloat(), mLastY.toFloat())
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = abs(x - mLastX)
                val dy = abs(y - mLastY)
                if (dx > 3 || dy > 3) mPath.lineTo(x.toFloat(), y.toFloat())
                mLastX = x
                mLastY = y
                Thread(mRunnable).start()
            }
            MotionEvent.ACTION_UP -> Thread(mRunnable).start()
        }
        invalidate()
        return true
    }


    /**
     * 统计下擦除的区域
     */

    private val mRunnable = object : Runnable {
        var mPixels = intArrayOf()
        override fun run() {
            val bitmap: Bitmap = mSrcBitmap!!

            val w = bitmap.width
            val h = bitmap.height
            var wipeArea = 0f
            val totalArea = (w * h).toFloat()

            mPixels = IntArray(w * h)
            /**
             * 拿到所有的像素信息
             */
            bitmap.getPixels(mPixels, 0, w, 0, 0, w, h)
            /**
             * 遍历统计擦除的区域
             */
            for (i in 0 until w) {
                for (j in 0 until h) {
                    val index = i + j * w
                    if (mPixels[index] == 0) {
                        wipeArea++
                    }
                }
            }
            /**
             * 根据所占百分比，进行一些操作
             */
            if (wipeArea > 0 && totalArea > 0) {
                val percent = (wipeArea * 100 / totalArea).toInt()
                if (percent > 50) {
                    Log.e("Scratch", "清除区域达到50%，下面自动清除");
                    isComplete = true
                    postInvalidate()
                }
            }
        }
    }


    /**
    1.混合是针对图片的， 需要先将手势轨迹输出到图像上，从而与特定的图片混合实现刮的效果。
    2、手势的输出使用path及二阶贝塞尔曲线，并注意move时控制点的计算。
    3、混合时不能使用canvas.save()方法，而应该用saveLayer方法。因为save方法只保存了matrix及clip信息，而没alpha通道相关信息。
    4、刮开面积的比例是通过获取bitmap的像素计算的，注意如果某个像素点为0则为未绘制，非0为绘制。
    5、在调试时可以通过Bitmap.compressed方法将bitmap输出到文件查看。
     */
}