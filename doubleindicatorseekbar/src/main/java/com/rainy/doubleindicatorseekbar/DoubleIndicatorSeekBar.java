package com.rainy.doubleindicatorseekbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;

/**
 * @author jacky
 * @date 2022/7/23
 */
public class DoubleIndicatorSeekBar extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float circleStrokeWidth;
    private final float circleRadius;
    private final float lineHeight;
    private float percentage;
    private int progress;
    private int beforeProgress;
    private int maxProgress;
    private int minProgress;
    private final int backgroundColor;
    private final float horizontalPadding;
    private final float mPaddingLeft;

    private boolean isNormal = false;
    private boolean isColor = false;

    private PopupWindow mIndicatorWindow;
    private final BubbleView mBubbleView;
    private final DoubleIndicatorSeekBar mSeekBar;


    private static int[] mColors;
    private int currentColor;
    private LinearGradient mBgColorGradient;


    private OnProgressChangeListener mListener;
    private OnColorChangeListener mColorListener;
    private OnTouchingListener onTouchingListener;


    public DoubleIndicatorSeekBar(Context context) {
        this(context, null);
    }

    public DoubleIndicatorSeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleIndicatorSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSeekBar = this;
        mBubbleView = new BubbleView(context);
        initColors();
        if (attrs != null) {
            TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.AdjustSeekBar);
            minProgress = typedArray.getInt(R.styleable.AdjustSeekBar_seek_minProgress, 0);
            maxProgress = typedArray.getInt(R.styleable.AdjustSeekBar_seek_maxProgress, 100) - minProgress;
            progress = typedArray.getInt(R.styleable.AdjustSeekBar_seek_progress, 0) - minProgress;
            circleStrokeWidth = typedArray.getDimension(R.styleable.AdjustSeekBar_seek_circleStrokeWidth, 1f);
            if (progress < 0) progress = minProgress;
            circleRadius = typedArray.getDimension(R.styleable.AdjustSeekBar_seek_circleRadius, 20f);
            lineHeight = typedArray.getDimension(R.styleable.AdjustSeekBar_seek_lineHeight, 5f);
            backgroundColor = typedArray.getColor(R.styleable.AdjustSeekBar_seek_backgroundColor,
                    Color.parseColor("#F0F0F0"));
            typedArray.recycle();
            percentage = progress * 1.0f / maxProgress;
            horizontalPadding = circleRadius * 2 + getPaddingStart() + getPaddingEnd();
            mPaddingLeft = horizontalPadding - getPaddingEnd() - circleRadius;
            return;
        }
        //设置默认值
        maxProgress = 100;
        minProgress = -100;
        circleRadius = 20;
        lineHeight = 5;
        backgroundColor = Color.parseColor("#F0F0F0");
        horizontalPadding = circleRadius * 2 + getPaddingStart() + getPaddingEnd();
        mPaddingLeft = horizontalPadding - getPaddingEnd() - circleRadius;
    }

    @SuppressWarnings("ALL")
    @Override
    protected void onDraw(Canvas canvas) {
        final int width = (int) (getMeasuredWidth() - horizontalPadding);
        final int height = getMeasuredHeight();

        paint.setColor(backgroundColor);
        paint.setStrokeWidth(lineHeight);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);


        if (isColor) {
            //绘制色彩进度条
            paint.setShader(mBgColorGradient);
            canvas.drawLine(mPaddingLeft, height >> 1, mPaddingLeft + width, height >> 1, paint);
            paint.setShader(null);
        } else {
            //绘制进度条
            canvas.drawLine(mPaddingLeft, height >> 1, mPaddingLeft + width, height >> 1, paint);
        }


        //默认track颜色
        int currColor = Color.BLACK;
        int currStorkeColor = Color.WHITE;
        paint.setColor(currColor);
        //绘制trackActive
        if (!isColor) {
            if (isNormal) {
                canvas.drawLine(mPaddingLeft, height >> 1, mPaddingLeft + width * percentage, height >> 1, paint);
            } else {
                canvas.drawLine((mPaddingLeft + width) / 2 + circleRadius / 2, height >> 1, mPaddingLeft + width * percentage, height >> 1, paint);
            }
        }

        // draw circle border
        paint.setColor(currStorkeColor);
        canvas.drawCircle(mPaddingLeft + width * percentage, height >> 1, circleRadius + (circleStrokeWidth / 2f), paint);

        //绘制thumb
        paint.setColor(currColor);
        canvas.drawCircle(mPaddingLeft + width * percentage, height >> 1, circleRadius, paint);
    }


    //初始化色值,hsv换算
    private void initColors() {
        int colorCount = 12;
        float colorAngleStep = 360 / (colorCount * 1.0f);
        mColors = new int[colorCount + 1];
        float[] hsv = new float[]{0f, 1f, 1f};
        for (int i = 0; i < mColors.length; i++) {
            hsv[0] = 360 - (i * colorAngleStep) % 360;
            if (hsv[0] == 360) hsv[0] = 359;
            mColors[i] = Color.HSVToColor(hsv);
        }
    }


    private void changeColor() {
        final int width = (int) (getMeasuredWidth() - horizontalPadding);
        float position = mPaddingLeft + width * percentage;
        float colorH = 360 - position / width * 360;
        currentColor = Color.HSVToColor(new float[]{colorH, 1.0f, 1.0f});
        postInvalidate();
    }

    float downX = -1f;
    float downY = -1f;


    //拖拽 模拟seekbar
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean intercept = false;
        changeColor();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (onTouchingListener != null) {
                    onTouchingListener.onTouching(true);
                }
                downX = event.getX();
                downY = event.getY();
                refreshSeekBar();
                intercept = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (downX == -1 || downY == -1)
                    break;

                refreshSeekBar();
                float moveX = event.getX();
                float moveY = event.getY();
                float xMove = Math.abs(moveX - downX) - Math.abs(moveY - downY);
                if (xMove > 0f) {
                    float hX = moveX - downX;
                    boolean toLeft = hX < 0.0f;
                    float movePercent = Math.abs(hX) / getMeasuredWidth();
                    if (percentage < 1.0f && !toLeft) {
                        percentage += movePercent;
                    } else if (percentage > 0f && toLeft) {
                        percentage -= movePercent;
                    }

                    if (percentage < 0f) percentage = 0f;
                    if (percentage > 1f) percentage = 1f;

                    progress = (int) Math.floor(percentage * maxProgress);
                }
                intercept = true;
                downX = moveX;
                downY = moveY;
                postInvalidate();

                if (null != mListener && beforeProgress != getProgress()) {
                    beforeProgress = getProgress();
                    if (isColor) {
                        mColorListener.onColorProgress(beforeProgress, currentColor);
                    } else {
                        mListener.onProgress(getProgress());
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (onTouchingListener != null) {
                    onTouchingListener.onTouching(false);
                }
                downX = -1f;
                downY = -1f;
                dismissIndicator();
                break;
        }
        return intercept || super.onTouchEvent(event);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBgColorGradient = new LinearGradient(0f, 0f, w, 0f, mColors, null, Shader.TileMode.CLAMP);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }


    private void refreshSeekBar() {
        postInvalidate();
        updateIndicator();
    }

    private void updateIndicator() {
        initIndicatorPopWindow();
        if (isIndicatorShow()) {
            indicatorPopUpdate(getThumbCenterX());
        } else {
            indicatorPopShow(getThumbCenterX());
        }
    }

    private float getThumbCenterX() {
        float width = getMeasuredWidth() - horizontalPadding;
        return mPaddingLeft + width * percentage;
    }

    private void initIndicatorPopWindow() {
        if (mIndicatorWindow != null) {
            return;
        }
        if (mBubbleView != null) {
            mBubbleView.measure(0, 0);
            mIndicatorWindow = new PopupWindow(mBubbleView,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT, false);
        }
    }


    private void indicatorPopShow(float touchX) {
        if (!mSeekBar.isEnabled() || !(mSeekBar.getVisibility() == View.VISIBLE)) {
            return;
        }
        mBubbleView.setProgressText(String.valueOf(getProgress()));
        if (mIndicatorWindow != null) {
            mIndicatorWindow.getContentView().measure(0, 0);
            mIndicatorWindow.showAsDropDown(mSeekBar, (int) (touchX - mIndicatorWindow.getContentView().getMeasuredWidth() / 2f),
                    -(mSeekBar.getMeasuredHeight() + mIndicatorWindow.getContentView().getMeasuredHeight() - mSeekBar.getPaddingTop()));
        }
    }

    private void indicatorPopUpdate(float touchX) {
        if (!mSeekBar.isEnabled() || !(mSeekBar.getVisibility() == View.VISIBLE)) {
            return;
        }
        mBubbleView.setProgressText(String.valueOf(getProgress()));
        if (mIndicatorWindow != null) {
            mIndicatorWindow.getContentView().measure(0, 0);
            mIndicatorWindow.update(mSeekBar, (int) (touchX - mIndicatorWindow.getContentView().getMeasuredWidth() / 2f),
                    -(mSeekBar.getMeasuredHeight() + mIndicatorWindow.getContentView().getMeasuredHeight() - mSeekBar.getPaddingTop()), -1, -1);
        }
    }


    private void dismissIndicator() {
        if (mIndicatorWindow != null) {
            mIndicatorWindow.dismiss();
        }
    }

    private boolean isIndicatorShow() {
        return mIndicatorWindow != null && mIndicatorWindow.isShowing();
    }


    public int getProgress() {
        return this.progress + minProgress;
    }


    public void setProgress(int progress) {
        this.progress = progress - minProgress;
        this.percentage = this.progress * 1.0f / maxProgress;
        postInvalidate();
    }


    public void isNormal(boolean isNormal) {
        this.isNormal = isNormal;
    }

    public void isColor(boolean isColor) {
        this.isColor = isColor;
    }

    public void setCurrentProgress(int minProgress, int maxProgress) {
        this.minProgress = minProgress;
        this.maxProgress = maxProgress - minProgress;
        postInvalidate();
    }


    public void setOnProgressChangListener(OnProgressChangeListener listener) {
        this.mListener = listener;
    }

    public void setColorChangeListener(OnColorChangeListener colorListener) {
        this.mColorListener = colorListener;
    }

    public void setOnTouchingListener(OnTouchingListener onTouchingListener) {
        this.onTouchingListener = onTouchingListener;
    }

    public interface OnProgressChangeListener {
        void onProgress(int progress);
    }

    public interface OnTouchingListener {
        void onTouching(boolean isTouch);
    }

    public interface OnColorChangeListener {
        void onColorProgress(int progress, int color);
    }

}