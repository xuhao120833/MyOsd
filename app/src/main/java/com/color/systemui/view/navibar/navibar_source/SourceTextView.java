package com.color.systemui.view.navibar.navibar_source;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

public class SourceTextView extends TextView {

    int modeWidth;
    int modeHeight;

    int sizeWidth;
    int sizeHeight;

    public SourceTextView(Context context) {
        super(context);
    }

    public SourceTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//分辨率变化，onMeasure会被执行四次
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        Log.d("sizefWidth SourceTextView", String.valueOf(sizeWidth));
        Log.d("sizefHeight SourceTextView", String.valueOf(sizeHeight));

        setMeasuredDimension(sizeWidth, sizeHeight);
    }


}
