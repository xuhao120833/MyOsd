package com.color.osd.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.color.osd.utils.ConstantProperties;
import com.color.osd.utils.DensityUtil;


/**
 * 绘制圆形背景+文字
 */
public class CltCircleView extends ViewGroup {
    private static final String TAG = CltCircleView.class.getSimpleName();

    private float mRightCircleRadius;

    private Paint mRightCirclePaint;

    private String progressText;
//    private Paint mProgressTextPaint;

    private int textSize;
    private TextView textView;

    private int bgColor;
    public CltCircleView(Context context) {
        this(context, null);
    }

    public CltCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CltCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        bgColor = Color.argb(255, 39, 39, 39);
        mRightCircleRadius = DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_OR_VOLUME_BACKGROUND_CORNER_DP);
        mRightCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRightCirclePaint.setStyle(Paint.Style.FILL);
        mRightCirclePaint.setColor(bgColor);

        progressText = "0%";
        textSize = (int) DensityUtil.getScaledValue(DensityUtil.pxToSp(ConstantProperties.BRIGHTNESS_OR_VOLUME_CIRCLE_VIEW_TEXT_SIZE_DP));
        textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        textView.setText(progressText);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        addView(textView);

//        textSize = 20;
//        mProgressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mProgressTextPaint.setStyle(Paint.Style.FILL);
//        mProgressTextPaint.setColor(Color.WHITE);
//        mProgressTextPaint.setTextSize(textSize);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        textView.layout(0, 0, getWidth(), getHeight());
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

        //Log.d(TAG, "onMeasure: " + width + ", " + height);
        // 直接给textView设置宽高(当前布局有多大就给textView设多大)
        textView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
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
        // canvas.drawText(progressText, (getWidth() - textSize) / 2, (getHeight()) / 2 + textSize/2, mProgressTextPaint);
    }

    public void setPercent(int percent){
        progressText = percent + "%";
//        Log.d(TAG, "setPercent: " + progressText);
        textView.setText(progressText);
        invalidate();
    }
}
