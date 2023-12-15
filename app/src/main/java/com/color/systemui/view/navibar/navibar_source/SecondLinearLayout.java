package com.color.systemui.view.navibar.navibar_source;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import android.util.Log;


public class SecondLinearLayout extends LinearLayout {

    int modeWidth;
    int modeHeight;

    int sizeWidth;
    int sizeHeight;

    LayoutParams lpsignal1, lpsignal2;



    public SecondLinearLayout(Context context) {
        super(context);
    }

    public SecondLinearLayout(Context context, @Nullable AttributeSet attrs) {
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

        Log.d("sizefWidth SecondLinearLayout", String.valueOf(sizeWidth));
        Log.d("sizefHeight SecondLinearLayout", String.valueOf(sizeHeight));

        //MeasureSource();

        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    public void MeasureSource() {

//        lpsignal1 = (android.widget.LinearLayout.LayoutParams) VolumeView.i1.getLayoutParams();
//        lpsignal1.leftMargin = sizeWidth * 140 / 600;
//        lpsignal1.rightMargin = sizeWidth * 60 / 200;
//        VolumeView.i1.setLayoutParams(lpsignal1);
//        VolumeView.i1.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 130 / 1920, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(sizeHeight * 130 / 1080, MeasureSpec.EXACTLY));

    }


}