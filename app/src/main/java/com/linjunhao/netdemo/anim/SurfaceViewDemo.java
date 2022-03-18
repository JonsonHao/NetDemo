package com.linjunhao.netdemo.anim;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieResult;

public class SurfaceViewDemo extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private boolean mIsDrawing;
    private Paint mPaint;
//    private CustomView mCustomView;

    public SurfaceViewDemo(Context context) {
        this(context, null);
    }

    public SurfaceViewDemo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceViewDemo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(50f);
        mPaint.setStrokeWidth(5);

        initView();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        drawSomething();
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
//        while (mIsDrawing) {
//            drawSomething();
//        }
    }

    private void drawSomething() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            //绘制背景
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawText("哈哈", 100, 500, mPaint);

            mLottieDrawable = new LottieDrawable();
            mLottieDrawable.enableMergePathsForKitKatAndAbove(true);
//            mCustomView = new CustomView(getContext(),null);

            mLottieDrawable.setCallback(this);
            Log.d("ljh", mLottieDrawable.getCallback().toString());
            mLottieDrawable.setImagesAssetsFolder("wallpaper/images");
            mLottieDrawable.setBounds(0, 20, 300, 200);
            LottieResult<LottieComposition> result = LottieCompositionFactory.fromAssetSync(
                    getContext(), "wallpaper/data.json");
            mLottieDrawable.setComposition(result.getValue());
            mLottieDrawable.setRepeatCount(LottieDrawable.INFINITE);
            mLottieDrawable.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Log.d("ljh", "onAnimationUpdate");
                    mSurfaceHolder.lockCanvas();
                    mCanvas.drawColor(Color.WHITE);
                    mLottieDrawable.draw(mCanvas);
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);

                }
            });
            mLottieDrawable.draw(mCanvas);
            mLottieDrawable.start();

        } catch (Exception e) {

        } finally {
            if (mCanvas != null) {
                //释放canvas对象并提交画布
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    /**
     * 初始化View
     */
    private void initView() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
//        mCustomView = new CustomView(getContext(),null);
        setFocusable(true);
        setKeepScreenOn(true);
        setFocusableInTouchMode(true);
    }

    private LottieDrawable mLottieDrawable;

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        super.invalidateDrawable(drawable);
        Log.d("ljh", "invalidateDrawable");
//        drawSomething();
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
        super.scheduleDrawable(who, what, when);
        Log.d("ljh", "scheduleDrawable");
    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
        super.unscheduleDrawable(who, what);
        Log.d("ljh", "unscheduleDrawable");
    }

}