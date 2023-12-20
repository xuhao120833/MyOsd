package com.color.osd.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.color.osd.models.interfaces.MenuBrightnessAndVolumeInterface;
import com.color.osd.utils.ConstantProperties;
import com.color.osd.utils.DensityUtil;

public class CltTouchBarBaseView extends ViewGroup implements CltSeekBar.TouchMoveEvent {

    private static final String TAG = CltTouchBarBaseView.class.getSimpleName();

    private float mDotRadius;
    private Paint mPaginationPaint;

    private CltSeekBar seekBar;
    private CltCircleView circleRight;

    private int seekBarWidth;
    private int seekBarHeight;

    private int circleViewSize;

    private int circleRightMargin;
    private int circleLeftMargin;
    private int mProgress;   // 亮度值/声音值
    private int mProgressPercent;   // 比例
    public float baseValue = 255;     // 比值的基底  如果是亮度，基底是255；如果是音量，基底15  给个默认值，避免被除数为0

    private MenuBrightnessAndVolumeInterface parentView;

    public void setParentView(MenuBrightnessAndVolumeInterface parentView) {
        this.parentView = parentView;
    }

    public CltTouchBarBaseView(Context context) {
        this(context, null);
    }

    public CltTouchBarBaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CltTouchBarBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CltTouchBarBaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setWillNotDraw(false);
        mDotRadius = DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_OR_VOLUME_BACKGROUND_CORNER_DP);
        mPaginationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaginationPaint.setStyle(Paint.Style.FILL);
        mPaginationPaint.setColor(Color.BLACK);

        // seekbar的宽高
        seekBarHeight = (int) DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_OR_VOLUME_SEEK_BAR_HEIGHT_DP);
        seekBarWidth = (int) DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_OR_VOLUME_SEEK_BAR_WIDTH_DP);

        // 圆形的大小
        circleViewSize = (int) DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_OR_VOLUME_CIRCLE_VIEW_HEIGHT_DP);
        circleRightMargin = (int) DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_OR_VOLUME_CIRCLE_VIEW_RIGHT_MARGIN_DP);
        circleLeftMargin = (int) DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_OR_VOLUME_CIRCLE_VIEW_LEFT_MARGIN_DP);

        // 添加seek bar
        seekBar = new CltSeekBar(context);
        seekBar.setTouchMoveEvent(this);
        addView(seekBar);

        // 添加右侧圆，显示当前数值
        circleRight = new CltCircleView(context);
        addView(circleRight);


        // 添加左侧圆，显示三个点 最新的设计稿给取消了
//        circleLeft = new CircleView(context);
//        addView(circleLeft);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        //Log.d(TAG, "onMeasure: " + width + ", " + height);
        seekBar.measure(MeasureSpec.makeMeasureSpec(seekBarWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(seekBarHeight, MeasureSpec.EXACTLY));
        //Log.d(TAG, "onMeasure-seekbar: " + seekBarWidth + ", " + seekBarHeight);

        circleRight.measure(MeasureSpec.makeMeasureSpec(circleViewSize, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(circleViewSize, MeasureSpec.EXACTLY));

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //Log.d(TAG, "onLayout: " + l + ", " + t + ", " + r + ", " + b);
        // 摆放seekBar
        int measuredWidthSeekBar = seekBar.getMeasuredWidth();
        int measuredHeightSeekBar = seekBar.getMeasuredHeight();
        //Log.d(TAG, "onLayout: measuredWidthSeekBar=" + measuredWidthSeekBar
                //+ ", measuredHeightSeekBar=" + measuredHeightSeekBar);
        int seekBarLeft = circleLeftMargin;
        int seekBarTop = (getHeight() - measuredHeightSeekBar) / 2;
        seekBar.layout(seekBarLeft, seekBarTop, seekBarLeft + measuredWidthSeekBar , seekBarTop + measuredHeightSeekBar);

        // 摆放circleView
        int measuredWidthCircle = circleRight.getMeasuredWidth();
        int measuredHeightCircle = circleRight.getMeasuredHeight();
        int circleLeft = getWidth() - circleRightMargin - measuredWidthCircle;
        int circleTop = (getHeight() - measuredHeightCircle) / 2;
        circleRight.layout(circleLeft, circleTop, circleLeft + measuredWidthCircle, circleTop + measuredHeightCircle);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.d(TAG, "onDraw: here");

        // 绘制背景
        RectF tempRect = new RectF();
        tempRect.top = 0;
        tempRect.bottom = tempRect.top + getHeight();
        tempRect.left = 0;
        tempRect.right = tempRect.left + getWidth();
        canvas.drawRoundRect(tempRect, mDotRadius, mDotRadius, mPaginationPaint);
    }

    public void setProgress(int progress){
        mProgress = progress;
        mProgressPercent = toPercent(progress);   // 转化成百分比
        //Log.d(TAG, "setProgress: " + mProgressPercent);
        updateUI();
    }

    private void updateUI(){
        // 调整UI
        // 1 调整右侧圆的百分比文字
        circleRight.setPercent(mProgressPercent);
        // 2 调整进度条百分比
        seekBar.setBrightnessPercent(mProgressPercent);
    }

    public void reRenderView(){
        invalidate();
    }

    private int toPercent(int brightness){
        // 映射到百分比, 四舍五入取整
        return Math.round((brightness / baseValue) * 100.0f);
    }

    private int toNumber(int percentage) {
        return (int) ((percentage / 100.0) * baseValue);
    }

    /**
     * CltSeekBar的触摸事件回调
     * @param percent    百分比
     */
    @Override
    public void onMovePercent(int percent) {
        mProgressPercent = percent;
        mProgress = toNumber(mProgressPercent);      // 根据百分比再转数值
        parentView.setProgressFromTouchEvent(mProgress);   // 更新下Brightness_View的brightness，否则遥控的时候还是用的旧的值

        // 更新UI
        updateUI();
    }

    public void setSeekBarIconPositive(Drawable res){
        seekBar.positiveIcon = res;
    }

    public void setSeekBarIconNegative(Drawable res){
        seekBar.negativeIcon = res;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 这种view的keyDown回调中是监听不到音量、亮度变化的。所以按键小板的音量和亮度加减单独走一套逻辑，不会走这里。
        //Log.d(TAG, "onKeyDown: CltTouchBarBaseView down" + event.getKeyCode() + ", " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
            parentView.onKeyDownFromBaseView(false);    // 把这个事件传递出去
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
            parentView.onKeyDownFromBaseView(true);    // 把这个事件传递出去
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //Log.d(TAG, "onKeyDown: CltTouchBarBaseView up " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK){   // 专门针对取消按键，在up抬起的时候时候传递出去
            parentView.onKeyUpClose();
        }

        return true;
    }
}
