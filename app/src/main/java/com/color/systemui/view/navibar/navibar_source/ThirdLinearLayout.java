package com.color.systemui.view.navibar.navibar_source;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import android.util.Log;


public class ThirdLinearLayout extends LinearLayout {

    int modeWidth;
    int modeHeight;

    int sizeWidth;
    int sizeHeight;



    public ThirdLinearLayout(Context context) {
        super(context);
    }

    public ThirdLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//分辨率变化，onMeasure会被执行四次
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        //sizeWidth = sizeHeight * 1920 / 1080;

        Log.d("sizefWidth ThirdLinearLayout", String.valueOf(sizeWidth));
        Log.d("sizefHeight ThirdLinearLayout", String.valueOf(sizeHeight));

        //MeasureSource();

        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    public void MeasureSource() {


    }

}