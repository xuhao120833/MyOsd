package com.color.systemui.view.statusbar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;

import com.color.systemui.interfaces.Instance;

public class StatusBarLinearLayout extends LinearLayout implements Instance {

    int modeWidth;
    int modeHeight;

    int sizeWidth;
    int sizeHeight;

    public StatusBarLinearLayout(Context context) {
        super(context);
    }

    public StatusBarLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//分辨率变化，onMeasure会被执行四次
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

//        Log.d("sizefWidth StatusBarLinearLayout", String.valueOf(sizeWidth));
//        Log.d("sizefHeight StatusBarLinearLayout", String.valueOf(sizeHeight));

//        Udisk = STATIC_INSTANCE_UTILS.statusBar.udisk;
//        android.widget.LinearLayout.LayoutParams Udisk_lp = (android.widget.LinearLayout.LayoutParams) Udisk.getLayoutParams();
//        Udisk_lp.leftMargin = sizeWidth * 8 / 1920;
//        Udisk_lp.rightMargin = sizeWidth * 8 / 1920;
//        Udisk.setLayoutParams(Udisk_lp);
//        Udisk.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 40 / 1920, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));
////
//        Wifi = STATIC_INSTANCE_UTILS.statusBar.wifi;
//        android.widget.LinearLayout.LayoutParams Wifi_lp = (android.widget.LinearLayout.LayoutParams) Wifi.getLayoutParams();
//        Wifi_lp.leftMargin = sizeWidth * 8 / 1920;
//        Wifi_lp.rightMargin = sizeWidth * 8 / 1920;
//        Wifi.setLayoutParams(Wifi_lp);
//        Wifi.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 40 / 1920, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));
////
//        Ethernet = STATIC_INSTANCE_UTILS.statusBar.ethernet;
//        android.widget.LinearLayout.LayoutParams Ethernet_lp = (android.widget.LinearLayout.LayoutParams) Ethernet.getLayoutParams();
//        Ethernet_lp.leftMargin = sizeWidth * 8 / 1920;
//        Ethernet_lp.rightMargin = sizeWidth * 8 / 1920;
//        Ethernet.setLayoutParams(Ethernet_lp);
//        Ethernet.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 40 / 1920, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));
////
//        Hotspot = STATIC_INSTANCE_UTILS.statusBar.hotspot;
//        android.widget.LinearLayout.LayoutParams Hotspot_lp = (android.widget.LinearLayout.LayoutParams) Hotspot.getLayoutParams();
//        Hotspot_lp.leftMargin = sizeWidth * 8 / 1920;
//        Hotspot.setLayoutParams(Hotspot_lp);
//        Hotspot.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 40 / 1920, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();

            if(i==3) {
                layoutParams.leftMargin = sizeWidth * 8 / 280;
                child.setLayoutParams(layoutParams);
                child.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 40 / 280, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 46, MeasureSpec.EXACTLY));
            }else {
                layoutParams.leftMargin = sizeWidth * 8 / 280;
                layoutParams.rightMargin = sizeWidth * 8 / 280;
                child.setLayoutParams(layoutParams);
                child.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 40 / 280, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 46, MeasureSpec.EXACTLY));
            }
        }

        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            // 设置外边距
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
//            if (child.getVisibility() != GONE) {
//                // 获取子视图的布局参数
            Log.d("StatusBarLinearLayout left", String.valueOf(left)+" "+i);
            Log.d("StatusBarLinearLayout top", String.valueOf(top)+" "+i);
            Log.d("StatusBarLinearLayout right", String.valueOf(right)+" "+i);
            Log.d("StatusBarLinearLayout bottom", String.valueOf(bottom)+" "+i);

            Log.d("StatusBarLinearLayout leftMargin", String.valueOf(layoutParams.leftMargin)+" "+i);
            Log.d("StatusBarLinearLayout topMargin", String.valueOf(layoutParams.rightMargin)+" "+i);
            Log.d("StatusBarLinearLayout rightMargin", String.valueOf(layoutParams.rightMargin)+" "+i);
            Log.d("StatusBarLinearLayout bottomMargin", String.valueOf(layoutParams.bottomMargin)+" "+i);

            int childLeft = left + layoutParams.leftMargin;
            int childTop = top;
            int childRight = right - layoutParams.rightMargin;
            int childBottom = bottom - layoutParams.bottomMargin;

//            // 放置子视图
            //child.layout(childLeft, childTop, childRight, childBottom);
//            }
        }
    }

}
