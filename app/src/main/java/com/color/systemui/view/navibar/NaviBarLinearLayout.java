package com.color.systemui.view.navibar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import android.util.Log;

public class NaviBarLinearLayout extends LinearLayout {

    int modeWidth;
    int modeHeight;

    int sizeWidth;
    int sizeHeight;

    public NaviBarLinearLayout(Context context) {
        super(context);
    }

    public NaviBarLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//分辨率变化，onMeasure会被执行四次
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        Log.d("NaviBarLinearLayout", String.valueOf(sizeWidth));
        Log.d("NaviBarLinearLayout", String.valueOf(sizeHeight));

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
            if (i == 0) {
                layoutParams.topMargin = sizeHeight * 32 / 508;
                layoutParams.bottomMargin = sizeHeight * 10 / 508;
                layoutParams.width = sizeWidth * 60 / 84;
                layoutParams.height = sizeHeight * 40 / 508;
                child.setLayoutParams(layoutParams);
                child.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 60 / 84, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 508, MeasureSpec.EXACTLY));
            } else if (i == 3 || i == 7) {
                layoutParams.topMargin = sizeHeight * 10 / 508;
                layoutParams.bottomMargin = sizeHeight * 10 / 508;
                layoutParams.width = sizeWidth * 40 / 84;
                layoutParams.height = sizeHeight * 2 / 508;
                child.setLayoutParams(layoutParams);
                child.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 40 / 84, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(sizeHeight * 2 / 508, MeasureSpec.EXACTLY));
            } else if (i == 8) {
                layoutParams.topMargin = sizeHeight * 10 / 508;
                layoutParams.bottomMargin = sizeHeight * 32 / 508;
                layoutParams.width = sizeWidth * 60 / 84;
                layoutParams.height = sizeHeight * 40 / 508;
                child.setLayoutParams(layoutParams);
                child.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 60 / 84, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 508, MeasureSpec.EXACTLY));
            } else {

                layoutParams.topMargin = sizeHeight * 10 / 508;
                layoutParams.bottomMargin = sizeHeight * 10 / 508;
                layoutParams.width = sizeWidth * 60 / 84;
                layoutParams.height = sizeHeight * 40 / 508;
                child.setLayoutParams(layoutParams);
                child.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 60 / 84, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 508, MeasureSpec.EXACTLY));
            }

        }

        setMeasuredDimension(sizeWidth, sizeHeight);
    }


}
