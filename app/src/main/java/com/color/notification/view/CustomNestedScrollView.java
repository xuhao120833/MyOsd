package com.color.notification.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.StaticVariableUtils;

public class CustomNestedScrollView extends NestedScrollView implements Instance {

    static boolean isFlag = true;

    int sizeWidth;
    int sizeHeight;

    public CustomNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, android.graphics.Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

    }

    //只测量大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        WindowManager.LayoutParams lp = (WindowManager.LayoutParams)getLayoutParams();
//
//        lp.x = 20*StaticVariableUtils.widthPixels/1920;
//
//        setLayoutParams(lp);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //分辨率变化时，整个快捷中心的各种边距设置在MenuService 的方法Adaptable_resolution中完成


        //父view可以给多大空间
//        sizeHeight = MeasureSpec.getSize(heightMeasureSpec);//分辨率的高度
//        sizeWidth = sizeHeight * 1920 / 1080;//分辨率宽
//        Log.d("CustomNestedScrollView", " onMeasure ");
//        Log.d("CustomNestedScrollView", " 分辨率 " + String.valueOf(sizeWidth) + String.valueOf(sizeHeight));
//        Log.d("CustomNestedScrollView", " 宽度 " + String.valueOf(getWidth()));

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        clearFocus();

//        STATIC_INSTANCE_UTILS.myNotificationService.Adaptable_resolution();
    }

    //View第一次被添加到屏幕上调用
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        Log.d("CustomNestedScrollView", " onAttachedToWindow ");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("CustomNestedScrollView", " onSizeChanged ");
        Log.d("CustomNestedScrollView", "  ");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("CustomNestedScrollView", " zzonConfigurationChanged ");
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        Log.d("CustomNestedScrollView", " 按键回调到View");
        // 处理遥控器事件
        int keycode = event.getKeyCode();
        if (keycode == KeyEvent.KEYCODE_BACK && getVisibility()==View.VISIBLE) {
            Log.d("CustomNestedScrollView", " 收到Back事件");

            setVisibility(View.GONE);

        }

        // 返回true表示事件已经被处理
        return super.dispatchKeyEvent(event);
    }



}
