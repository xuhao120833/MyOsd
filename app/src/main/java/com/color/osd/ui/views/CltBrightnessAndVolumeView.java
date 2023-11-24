package com.color.osd.ui.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.color.osd.utils.ConstantProperties;
import com.color.osd.utils.DensityUtil;

/**
 * 复合状态的背景view
 */
public class CltBrightnessAndVolumeView extends ViewGroup {

    private float mDotRadius;
    private Paint mPaginationPaint;

    private int seekBarWidth, seekBarHeight;

    private CltTouchBarBaseView childView1;
    private CltTouchBarBaseView childView2;

    public CltBrightnessAndVolumeView(Context context) {
        this(context, null);
    }

    public CltBrightnessAndVolumeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CltBrightnessAndVolumeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);

        mDotRadius = DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_AND_VOLUME_BACKGROUND_CORNER_DP);
        mPaginationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaginationPaint.setStyle(Paint.Style.FILL);
        mPaginationPaint.setColor(Color.BLACK);

        seekBarWidth = (int) DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_OR_VOLUME_BACKGROUND_WIDTH_DP);
        seekBarHeight = (int) DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_OR_VOLUME_BACKGROUND_HEIGHT_DP);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        childView1.measure(MeasureSpec.makeMeasureSpec(seekBarWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(seekBarHeight, MeasureSpec.EXACTLY));

        childView2.measure(MeasureSpec.makeMeasureSpec(seekBarWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(seekBarHeight, MeasureSpec.EXACTLY));

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int measuredWidthChild1 = childView1.getMeasuredWidth();
        int measuredHeightChild1 = childView1.getMeasuredHeight();
        // 1、计算child1的左上角坐标
        int child1Left = (getWidth() - measuredWidthChild1) / 2;
        int child1Top = (int) DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_AND_VOLUME_CHILD_VIEW_MARGIN_DP);
        // 2、开始layout
        childView1.layout(child1Left, child1Top, child1Left + measuredWidthChild1 ,
                child1Top + measuredHeightChild1);


        int measuredWidthChild2 = childView2.getMeasuredWidth();
        int measuredHeightChild2 = childView2.getMeasuredHeight();
        // 3、计算child2的左上角坐标
        int child2Left = (getWidth() - measuredWidthChild2) / 2;
        int child2Top = (int) (DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_AND_VOLUME_CHILD_VIEW_MARGIN_DP) +
                measuredHeightChild1 + DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_AND_VOLUME_CHILD_2_CHILD_MARGIN_DP));
        childView2.layout(child2Left, child2Top, child2Left + measuredWidthChild2 ,
                child2Top + measuredHeightChild2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF tempRect = new RectF();
        tempRect.top = 0;
        tempRect.bottom = tempRect.top + getHeight();
        tempRect.left = 0;
        tempRect.right = tempRect.left + getWidth();
        canvas.drawRoundRect(tempRect, mDotRadius, mDotRadius, mPaginationPaint);
    }

    public void setTouchBarBaseView(CltTouchBarBaseView child1, CltTouchBarBaseView child2){
        childView1 = child1;
        childView2 = child2;

        // 添加两个子view
        addView(childView1);
        addView(childView2);
    }

    public void setCanFocusable(boolean focusable){
        if (childView1 != null){
            childView1.setBackgroundResource(0);
            childView1.setFocusable(focusable);
        }

        if (childView2 != null){
            childView2.setBackgroundResource(0);
            childView2.setFocusable(focusable);
        }
    }
}
