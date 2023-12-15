package com.color.systemui.view.statusbar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.color.osd.R;
import com.color.systemui.utils.StaticInstanceUtils;

public class StatusBarFrameLayout extends FrameLayout {

    Context mycontext;

    WindowManager windowManager;

    View  statusbar_frame, statusbar_liner;

    View Udisk, Wifi, Ethernet, Hotspot;

    int modeWidth;
    int modeHeight;

    int sizeWidth;
    int sizeHeight;

    static int measure = 0;


    public StatusBarFrameLayout(@NonNull Context context) {
        super(context);
    }


    public StatusBarFrameLayout(Context context, @Nullable AttributeSet attrs) {//分辨率变化会被执行两次
        super(context, attrs);
        mycontext = context;
        windowManager = mycontext.getSystemService(WindowManager.class);
        Log.d("MyFrameLayout(Context context, @Nullable AttributeSet attrs)", "获取context");
    }


    //onMeasure计算子视图的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//分辨率变化，onMeasure会被执行四次
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //sizeWidth = MeasureSpec.getSize(widthMeasureSpec);

        //父view可以给多大空间
        sizeHeight = MeasureSpec.getSize(heightMeasureSpec);//分辨率高
        sizeWidth = sizeHeight*1920/1080;//分辨率宽


        statusbar_liner = StaticInstanceUtils.statusBar.statusbar.findViewById(R.id.statusbar_linear);
        LayoutParams statusbar_liner_lp = (LayoutParams) statusbar_liner.getLayoutParams();
        statusbar_liner_lp.topMargin = sizeWidth * 14 / 1080;
        statusbar_liner.setLayoutParams(statusbar_liner_lp);
        statusbar_liner.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 280 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 46 / 1080, MeasureSpec.EXACTLY));
//
        Udisk = StaticInstanceUtils.statusBar.udisk;
        android.widget.LinearLayout.LayoutParams Udisk_lp = (android.widget.LinearLayout.LayoutParams) Udisk.getLayoutParams();
        Udisk_lp.leftMargin = sizeWidth * 8 / 1920;
        Udisk_lp.rightMargin = sizeWidth * 8 / 1920;
        Udisk.setLayoutParams(Udisk_lp);
        Udisk.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 40 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));
//
        Wifi = StaticInstanceUtils.statusBar.wifi;
        android.widget.LinearLayout.LayoutParams Wifi_lp = (android.widget.LinearLayout.LayoutParams) Wifi.getLayoutParams();
        Wifi_lp.leftMargin = sizeWidth * 8 / 1920;
        Wifi_lp.rightMargin = sizeWidth * 8 / 1920;
        Wifi.setLayoutParams(Wifi_lp);
        Wifi.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 40 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));
//
        Ethernet = StaticInstanceUtils.statusBar.ethernet;
        android.widget.LinearLayout.LayoutParams Ethernet_lp = (android.widget.LinearLayout.LayoutParams) Ethernet.getLayoutParams();
        Ethernet_lp.leftMargin = sizeWidth * 8 / 1920;
        Ethernet_lp.rightMargin = sizeWidth * 8 / 1920;
        Ethernet.setLayoutParams(Ethernet_lp);
        Ethernet.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 40 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));
//
        Hotspot = StaticInstanceUtils.statusBar.hotspot;
        android.widget.LinearLayout.LayoutParams Hotspot_lp = (android.widget.LinearLayout.LayoutParams) Hotspot.getLayoutParams();
        Hotspot_lp.leftMargin = sizeWidth * 8 / 1920;
        Hotspot.setLayoutParams(Hotspot_lp);
        Hotspot.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 40 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));


        setMeasuredDimension(sizeWidth * 280 / 1920, sizeHeight * 60 / 1080); //告诉父view自己要多大空间

    }

    //onLayout 设置子视图的位置

    //onDraw 绘制view


}
