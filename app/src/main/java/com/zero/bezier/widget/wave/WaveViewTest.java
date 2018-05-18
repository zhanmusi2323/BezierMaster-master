package com.zero.bezier.widget.wave;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

public class WaveViewTest extends View {
    /**
     * 波峰
     */
    private float mWavePeak = 35f;
    /**
     * 波槽
     */
    private float mWaveTrough = 35f;
    /**
     * 水位
     */
    private float mWaveHeight = 450f;
    /**
     * 数据点
     */
    private PointF mPointLeft1, mPointLeft2;
    private PointF mFirstPoint;
    private PointF mPointRight1, mPointRight2;
    /**
     * 控制点
     */
    private PointF mControlLeftPoint1, mControlLeftPoint2;
    private PointF mControlRightPoint1, mControlRightPoint2;
    private Paint mPaint;
    private Path mPath;
    private int mWaterColor = 0xFF0000FF;
    private int mWidth; // 组件宽度
    private int mHeight; // 组件高度
    private WaveHandler mWaveHandler = new WaveHandler();
    private static final int ANIM_START = 1;
    private boolean mHasInit = false;

    public WaveViewTest(Context context) {
        this(context, null);
    }

    public WaveViewTest(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveViewTest(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!mHasInit) {
            mWidth = w;
            mHeight = h;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mHasInit) {
            return;
        }
        mPath.reset();
        mPath.moveTo(mPointLeft1.x, mPointLeft1.y);
        mPath.quadTo(mControlLeftPoint1.x, mControlLeftPoint1.y, mPointLeft2.x, mPointLeft2.y);
        mPath.quadTo(mControlLeftPoint2.x, mControlLeftPoint2.y, mFirstPoint.x, mFirstPoint.y);
        mPath.quadTo(mControlRightPoint1.x, mControlRightPoint1.y, mPointRight1.x, mPointRight1.y);
        mPath.quadTo(mControlRightPoint2.x, mControlLeftPoint2.y, mPointRight2.x, mPointRight2.y);
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.lineTo(0, mFirstPoint.y);
        canvas.drawPath(mPath, mPaint);
    }

    public void setRunning() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mWaveHandler.sendEmptyMessage(ANIM_START);
            }
        }).start();
    }

    private class WaveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == ANIM_START) {
                initOriginalData(); // 初始化数据点和控制点的数据
                startAnim(); // 开启动画，让所有的点移动相同的距离
            }
        }
    }

    private void startAnim() {
        ValueAnimator animator = ObjectAnimator.ofFloat(mPointLeft1.x, 0);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1000);
        animator.setRepeatCount(Animation.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPointLeft1.x = (float) animation.getAnimatedValue();
                mPointLeft2 = new PointF(mPointLeft1.x + mWidth / 2f, mHeight - mWaveHeight);
                mFirstPoint = new PointF(mPointLeft2.x + mWidth / 2f, mHeight - mWaveHeight);
                mPointRight1 = new PointF(mFirstPoint.x + mWidth / 2f, mHeight - mWaveHeight);
                mPointRight2 = new PointF(mPointRight1.x + mWidth / 2f, mHeight - mWaveHeight);
                mControlLeftPoint1 = new PointF(mPointLeft1.x + mWidth / 4f, mPointLeft1.y + mWavePeak);
                mControlLeftPoint2 = new PointF(mPointLeft2.x + mWidth / 4f, mPointLeft2.y - mWaveTrough);
                mControlRightPoint1 = new PointF(mFirstPoint.x + mWidth / 4f, mFirstPoint.y + mWavePeak);
                mControlRightPoint2 = new PointF(mPointRight1.x + mWidth / 4f, mPointRight1.y - mWaveTrough);
                invalidate();
            }
        });
        animator.start();
    }

    private void initOriginalData() {
        mPointLeft1 = new PointF(-mWidth, mHeight - mWaveHeight);
        mPointLeft2 = new PointF(-mWidth / 2f, mHeight - mWaveHeight);
        mFirstPoint = new PointF(0, mHeight - mWaveHeight);
        mPointRight1 = new PointF(mWidth / 2f, mHeight - mWaveHeight);
        mPointRight2 = new PointF(mWidth, mHeight - mWaveHeight);
        mControlLeftPoint1 = new PointF(mPointLeft1.x + mWidth / 4f, mPointLeft1.y + mWavePeak);
        mControlLeftPoint2 = new PointF(mPointLeft2.x + mWidth / 4f, mPointLeft2.y - mWaveTrough);
        mControlRightPoint1 = new PointF(mFirstPoint.x + mWidth / 4f, mFirstPoint.y + mWavePeak);
        mControlRightPoint2 = new PointF(mPointRight1.x + mWidth / 4f, mPointRight1.y - mWaveTrough);
        mHasInit = true;
        invalidate();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mWaterColor);
    }
}
