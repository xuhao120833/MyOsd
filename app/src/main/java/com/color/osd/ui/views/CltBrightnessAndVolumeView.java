package com.color.osd.ui.views;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 复合状态的背景view
 */
public class CltBrightnessAndVolumeView extends ViewGroup {

    public CltBrightnessAndVolumeView(Context context) {
        this(context, null);
    }

    public CltBrightnessAndVolumeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CltBrightnessAndVolumeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
