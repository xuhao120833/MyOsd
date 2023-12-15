package com.color.systemui.view.hoverball;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;


public class HoverballImageView extends androidx.appcompat.widget.AppCompatImageView {

    int modeWidth;
    int modeHeight;

    int sizeWidth;
    int sizeHeight;

    public HoverballImageView(Context context) {
        super(context);
    }

    public HoverballImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//分辨率变化，onMeasure会被执行四次
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

//        Log.d("sizefWidth StatusBarImageView", String.valueOf(sizeWidth));
//        Log.d("sizefHeight StatusBarImageView", String.valueOf(sizeHeight));

        setMeasuredDimension(sizeWidth, sizeHeight);
    }


}
