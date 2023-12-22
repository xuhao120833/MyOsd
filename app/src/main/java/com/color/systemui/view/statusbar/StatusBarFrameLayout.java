package com.color.systemui.view.statusbar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.color.osd.R;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;

public class StatusBarFrameLayout extends FrameLayout implements Instance {

    Context mycontext;

    WindowManager windowManager;

    View statusbar_frame, statusbar_liner;

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
        //Log.d("MyFrameLayout(Context context, @Nullable AttributeSet attrs)", "获取context");
    }


    //onMeasure计算子视图的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//分辨率变化，onMeasure会被执行四次
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.d("StatusBarFrameLayout", "执行");
        modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //sizeWidth = MeasureSpec.getSize(widthMeasureSpec);

        //父view可以给多大空间
        sizeHeight = MeasureSpec.getSize(heightMeasureSpec);//分辨率高
        sizeWidth = sizeHeight * 1920 / 1080;//分辨率宽


//        if (getChildCount() > 0) {
//            View child = getChildAt(0);
//            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
//            layoutParams.topMargin = sizeWidth * 14 / 1080;
//            layoutParams.width = sizeWidth * 280 / 1920;
//            layoutParams.height = sizeHeight * 46 / 1080;
//            child.setLayoutParams(layoutParams);
//            child.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 280 / 1920, MeasureSpec.EXACTLY),
//                    MeasureSpec.makeMeasureSpec(sizeHeight * 46 / 1080, MeasureSpec.EXACTLY));
//        }

//        statusbar_liner = STATIC_INSTANCE_UTILS.statusBar.statusbar.findViewById(R.id.statusbar_linear);
//        LayoutParams statusbar_liner_lp = (LayoutParams) statusbar_liner.getLayoutParams();
//        statusbar_liner_lp.topMargin = sizeWidth * 14 / 1080;
//        statusbar_liner.setLayoutParams(statusbar_liner_lp);
//        statusbar_liner.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 280 / 1920, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(sizeHeight * 46 / 1080, MeasureSpec.EXACTLY));
//

        if (getChildCount() > 0) {
            View child = getChildAt(0);
//            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
//            layoutParams.topMargin = sizeWidth * 14 / 1080;
//            layoutParams.width = sizeWidth * 280 / 1920;
//            layoutParams.height = sizeHeight * 46 / 1080;
//            child.setLayoutParams(layoutParams);
            child.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 280 / 1920, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(sizeHeight * 46 / 1080, MeasureSpec.EXACTLY));
        }

        setMeasuredDimension(sizeWidth * 280 / 1920, sizeHeight * 60 / 1080); //告诉父view自己要多大空间

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
            Log.d("StatusBarFrameLayout left", String.valueOf(left));
            Log.d("StatusBarFrameLayout top", String.valueOf(top));
            Log.d("StatusBarFrameLayout right", String.valueOf(right));
            Log.d("StatusBarFrameLayout bottom", String.valueOf(bottom));

            Log.d("StatusBarFrameLayout leftMargin", String.valueOf(layoutParams.leftMargin));
            Log.d("StatusBarFrameLayout topMargin", String.valueOf(top + sizeHeight * 14 / 1080));
            Log.d("StatusBarFrameLayout rightMargin", String.valueOf(layoutParams.rightMargin));
            Log.d("StatusBarFrameLayout bottomMargin", String.valueOf(layoutParams.bottomMargin));

            int childLeft = left + layoutParams.leftMargin;
            int childTop = sizeHeight * 14 / 1080;
            int childRight = right - layoutParams.rightMargin;
            int childBottom = bottom - layoutParams.bottomMargin;

//            // 放置子视图
            child.layout(childLeft, childTop, childRight, childBottom);
//            }
        }
    }


    //onDraw 绘制view


}
