package com.color.osd.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.color.osd.R;
import com.color.osd.utils.ConstantProperties;
import com.color.osd.utils.DensityUtil;

public class CltSeekBar extends View {
    private static final String TAG = CltSeekBar.class.getSimpleName();

    private float mBgSeekHeight;
    private Paint mBgSeekPaint;
    private Paint mFgSeekPaint;

    private float mFgSeekHeight;
    private float mFgSeekAvailableWidth;    // 进度条实际宽度 = 总宽度（getWidth） - mFgSeekHeight(46) 因为进度条为0，此时看着是个圆角正方形

    private int iconMarginRight;
    private int iconSize;

    private int radius;

    public Drawable positiveIcon;
    public Drawable negativeIcon;

    private float brightnessPercent;

    private LinearGradient linearGradient;

    private Context mContext;

    public CltSeekBar(Context context) {
        super(context, null);
        mContext = context;
        mBgSeekHeight = DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_OR_VOLUME_SEEK_BAR_BACKGROUND_HEIGHT_DP);
        mBgSeekPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgSeekPaint.setStyle(Paint.Style.FILL);
        mBgSeekPaint.setColor(Color.argb(255, 39, 39, 39));

        radius = (int) DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_OR_VOLUME_BACKGROUND_CORNER_DP);
        mFgSeekHeight = (int) DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_OR_VOLUME_SEEK_BAR_HEIGHT_DP);
        brightnessPercent = 0;
        mFgSeekPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFgSeekPaint.setStyle(Paint.Style.FILL);
        linearGradient = new LinearGradient(0, 0, mFgSeekHeight, mFgSeekHeight,
                Color.rgb(70, 175, 253),
                Color.rgb(68, 79, 252), Shader.TileMode.MIRROR);
        mFgSeekPaint.setShader(linearGradient);

        // 给个初始默认值吧 避免空指针
        negativeIcon = ContextCompat.getDrawable(context, R.drawable.dark_brightness);
        positiveIcon = ContextCompat.getDrawable(context, R.drawable.white_brightness);

        iconMarginRight = (int) DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_OR_VOLUME_SEEK_BAR_ICON_MARGIN_RIGHT);
        iconSize = (int) DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_OR_VOLUME_SEEK_BAR_ICON_SIZE);
    }

    public void setBrightnessPercent(float percent){
        brightnessPercent = percent;
        //Log.d(TAG, "setBrightnessPercent: " + (mFgSeekAvailableWidth * brightnessPercent / 100));
        linearGradient = new LinearGradient(0, 0,mFgSeekAvailableWidth * brightnessPercent / 100, mFgSeekHeight,
                Color.rgb(70, 175, 253),
                Color.rgb(68, 79, 252), Shader.TileMode.MIRROR);
        mFgSeekPaint.setShader(linearGradient);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        //Log.d(TAG, "onMeasure: " + width + ", " + height);
        mFgSeekAvailableWidth = width - mFgSeekHeight;  // 计算进度条有效宽度
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //Log.d(TAG, "onLayout: " + l + ", " + t + ", " + r + ", " + b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.d(TAG, "onDraw: here");

        // 1、绘制seek bar的那个背景横杠
        RectF bgSeekRect = new RectF();
        bgSeekRect.top = (getHeight() - mBgSeekHeight) / 2;
        bgSeekRect.bottom = bgSeekRect.top + mBgSeekHeight;
        bgSeekRect.left = 0;
        bgSeekRect.right = bgSeekRect.left + getWidth();
        canvas.drawRoundRect(bgSeekRect, 10, 10, mBgSeekPaint);

        // 2、绘制seek bar前景滑动条
        RectF fgSeekRect = new RectF();
        fgSeekRect.top = 0;
        fgSeekRect.bottom = fgSeekRect.top + mFgSeekHeight;
        fgSeekRect.left = 0;
        // 注意下面打的两个括号，第一个括号保证最小都要绘制一个圆角正方形，后一个括号就是计算当前百分比下的进度条长度
        fgSeekRect.right = (fgSeekRect.left + mFgSeekHeight) + (mFgSeekAvailableWidth * brightnessPercent / 100);
        canvas.drawRoundRect(fgSeekRect, radius, radius, mFgSeekPaint);

        // 3、绘制亮度标志
        int iconLeft = (int) fgSeekRect.right - iconSize - iconMarginRight;
        int iconTop = (int) (fgSeekRect.bottom - iconSize) / 2;
        if (brightnessPercent == 0){
            negativeIcon.setBounds(iconLeft, iconTop, iconLeft + iconSize, iconTop + iconSize);
            negativeIcon.draw(canvas);
        }else{
            positiveIcon.setBounds(iconLeft, iconTop, iconLeft + iconSize, iconTop + iconSize);
            positiveIcon.draw(canvas);
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 和手势触摸相关的
    private int flag;
    private TouchMoveEvent touchMoveEvent;

    public void setTouchMoveEvent(TouchMoveEvent touchMoveEvent) {
        this.touchMoveEvent = touchMoveEvent;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float deltaDown = Math.max(0, event.getX() - mFgSeekHeight);
                if (touchMoveEvent != null){
                    touchMoveEvent.onMovePercent(Math.round(deltaDown / mFgSeekAvailableWidth * 100));   // 把结果回调出去
                }

//                Log.d(TAG, "onTouchEvent: here down: " + deltaDown);
                break;
            case MotionEvent.ACTION_MOVE:
                flag++;
                if (flag % 6 == 0){    // 抽帧处理  不然滑动触发太多次了 导致频繁的onDraw() 性能跟不上
                    float deltaDownMove = Math.min(Math.max(0, event.getX() - mFgSeekHeight), mFgSeekAvailableWidth);
                    if (touchMoveEvent != null){
                        touchMoveEvent.onMovePercent(Math.round(deltaDownMove / mFgSeekAvailableWidth * 100));    // 把结果回调出去
                    }

//                    Log.d(TAG, "onTouchEvent: here move: " + deltaDownMove);
                }

                break;

            case MotionEvent.ACTION_UP:
                //Log.d(TAG, "onTouchEvent: here up");
                break;
        }
        return true;
    }

    public interface TouchMoveEvent{
        void onMovePercent(int percent);
    }

    public void setNormalIcon(int res){
        positiveIcon = ContextCompat.getDrawable(mContext, res);
    }
}
