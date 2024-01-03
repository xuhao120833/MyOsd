package com.color.notification.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.color.systemui.interfaces.Instance;

public class MyNestedScrollView extends NestedScrollView implements Instance {

    public MyNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, android.graphics.Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            STATIC_INSTANCE_UTILS.myNotification.notification.setForeground(null);
//        }
//        STATIC_INSTANCE_UTILS.myNotification.notification.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        clearFocus();
    }

//    @Override
//    public boolean isFocused() {
//        // 返回 false 表示不获取焦点
//        return false;
//    }
//
//    @Override
//    public boolean hasWindowFocus() {
//        // 返回 false 表示不获取焦点
//        return false;
//    }
//
//    @Override
//    public boolean hasFocus() {
//        // 返回 false 表示不获取焦点
//        return false;
//    }
}
