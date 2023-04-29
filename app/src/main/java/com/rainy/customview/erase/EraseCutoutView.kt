package com.rainy.customview.erase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.rainy.customview.R

/**
 * @author jiangshiyu
 * @date 2022/11/4
 */
class EraseCutoutView : View, View.OnTouchListener {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr)

    companion object {
        const val TAG = "EraseCutoutView"

        //静止
        const val STATE_IDLE = -1

        //滑动状态
        const val STATE_SCROLLING = 0

        //特殊手势处理状体，禁止滑动状态,直至ACTION_UP重置
        const val STATE_GESTURE_CONSUMED = 1

    }

    //默认状态
    private var currentState = STATE_IDLE

    //默认描点大小
    private var mActionPointerSize = 35

    private val mPhotoTop = 0
    private val mPhotoLeft = 0
    private val mDisplayPhotoWidth = 0
    private val mDisplayPhotoHeight = 0
    private val mViewWidth = 0
    private val mViewHeight = 0


    //绘制底图图片dst
    private var mPhotoBitmap: Bitmap? = null


    /**
     * 抠图操作路径相关画笔
     */
    private val mPhotoPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mActionPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mMaskSrcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mCutoutPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mEnlargePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTransparentPaint = Paint(Paint.ANTI_ALIAS_FLAG)


    /**
     * 显示区域
     */
    private var mDisplayPhotoRectF = RectF()

    init {

        mPhotoPaint.apply {
            //防抖
            isDither = true
            style = Paint.Style.FILL
        }

        mMaskSrcPaint.apply {
            isDither = true
            style = Paint.Style.FILL
        }

        mCutoutPaint.apply {
            isDither = true
            style = Paint.Style.FILL
        }

        //透明背景
        val transparentBitmap =
            BitmapFactory.decodeResource(resources, androidx.appcompat.R.drawable.abc_ic_clear_material)
        val transparentShader =
            BitmapShader(transparentBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        mTransparentPaint.apply {
            isDither = true
            shader = transparentShader
        }

        mActionPaint.apply {
            isDither = true
            style = Paint.Style.STROKE
            strokeWidth = mActionPointerSize.toFloat()
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }

        mEnlargePaint.apply {
            isDither = true
            style = Paint.Style.STROKE
            strokeWidth = 5f
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
        setOnTouchListener(this)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.save()

        //1.透明背景
        canvas?.drawRect(mDisplayPhotoRectF, mTransparentPaint)


        canvas?.restore()
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {

        //TODO 触摸操作
        return true
    }


    fun setOriginBitmap(bitmap: Bitmap?) {
        mPhotoBitmap = bitmap
    }

    fun setPointerSize(size: Int) {
        mActionPointerSize = size
    }
}