package com.color.osd.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * 绘制圆形背景+文字
 */
public class CltCircleView extends View {
    private static final String TAG = CltCircleView.class.getSimpleName();

    private float mRightCircleRadius;

    private float mRightCirclePaddingRight;
    private Paint mRightCirclePaint;

    private String progressText;
    private Paint mProgressTextPaint;

    private int textSize;

    private int bgColor;
    public CltCircleView(Context context) {
        this(context, null);
    }

    public CltCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CltCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bgColor = Color.argb(255, 39, 39, 39);
        mRightCircleRadius = 15;
        mRightCirclePaddingRight = 16;
        mRightCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRightCirclePaint.setStyle(Paint.Style.FILL);
        mRightCirclePaint.setColor(bgColor);

        progressText = "0%";
        textSize = 20;
        mProgressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressTextPaint.setStyle(Paint.Style.FILL);
        mProgressTextPaint.setColor(Color.WHITE);
        mProgressTextPaint.setTextSize(textSize);

    }

    public void setDrawBgColor(int bgColor) {
        this.bgColor = bgColor;
        mRightCirclePaint.setColor(bgColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        Log.d(TAG, "onMeasure: " + width + ", " + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景
        RectF rect = new RectF();
        rect.top = 0;
        rect.bottom = rect.top + getHeight();
        rect.left = 0;
        rect.right = rect.left + getWidth();
        canvas.drawRoundRect(rect, mRightCircleRadius, mRightCircleRadius, mRightCirclePaint);

        // 绘制文字
        canvas.drawText(progressText, (getWidth() - textSize) / 2, (getHeight()) / 2 + textSize/2, mProgressTextPaint);
    }

    public void setPercent(float percent){
        progressText = String.valueOf(percent);
        Log.d(TAG, "setPercent: " + progressText);
        invalidate();
    }
}
