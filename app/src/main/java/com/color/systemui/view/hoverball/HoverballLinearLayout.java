package com.color.systemui.view.hoverball;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.color.osd.R;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class HoverballLinearLayout extends LinearLayout implements Instance {

    int modeWidth;
    int modeHeight;

    int sizeWidth;
    int sizeHeight;

    View hoverball2 , hoverball3;

    public HoverballLinearLayout(Context context) {
        super(context);
    }

    public HoverballLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//分辨率变化，onMeasure会被执行四次
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.d("HoverballLinearLayout"," onMeasure执行");
        modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        sizeWidth = sizeHeight * 1920/1080;

        hoverball2 = STATIC_INSTANCE_UTILS.hoverball.leftlayout.findViewById(R.id.hoverball_left);
        hoverball2.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 50 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 100 / 1080, MeasureSpec.EXACTLY));

        hoverball3 = STATIC_INSTANCE_UTILS.hoverball.rightlayout.findViewById(R.id.hoverball_right);
        //android.widget.LinearLayout.LayoutParams lphover2=(android.widget.LinearLayout.LayoutParams)hoverball2.getLayoutParams();
        hoverball3.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 50 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 100 / 1080, MeasureSpec.EXACTLY));


        setMeasuredDimension(sizeWidth * 50 / 1920, sizeHeight * 100 / 1080);
    }


}