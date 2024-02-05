package com.color.systemui.view.navibar.navibar_source;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import android.util.Log;
import android.widget.TextView;

import com.color.osd.R;
import com.color.systemui.utils.StaticVariableUtils;

public class FirstLinearLayout extends LinearLayout {

    int modeWidth;
    int modeHeight;

    int sizeWidth;
    int sizeHeight;


    public FirstLinearLayout(Context context) {
        super(context);
    }

    public FirstLinearLayout(Context context, @Nullable AttributeSet attrs) {
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

        Log.d("FirstLinearLayout", String.valueOf(sizeWidth));
        Log.d("FirstLinearLayout", String.valueOf(sizeHeight));

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (i == 0) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                TextView textView = (TextView) child;
                textView.setText(R.string.soure_change);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 26 * sizeHeight / 70);
                layoutParams.leftMargin = sizeWidth * 20 / 600;
//                layoutParams.topMargin = sizeHeight * 16 / 70;
                textView.setLayoutParams(layoutParams);
                textView.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 520 / 600, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 70, MeasureSpec.EXACTLY));
            }

            if (i == 1) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                layoutParams = (LayoutParams) child.getLayoutParams();
//                layoutParams.topMargin = sizeHeight * 30 / 70;
                child.setLayoutParams(layoutParams);
                child.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 40 / 600, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 70, MeasureSpec.EXACTLY));
            }
        }


        setMeasuredDimension(sizeWidth, sizeHeight);
    }


}