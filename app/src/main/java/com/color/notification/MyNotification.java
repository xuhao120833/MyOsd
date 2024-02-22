package com.color.notification;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.RenderEffect;
import android.graphics.RenderNode;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;

import androidx.core.widget.NestedScrollView;

import com.color.osd.R;
import com.color.osd.models.AddViewToScreen;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.StaticVariableUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.function.Consumer;

public class MyNotification implements Instance {

    private Context mycontext;

    private String TAG = "MyNotification";

    private LayoutInflater inflater;

    private AddViewToScreen mavts = new AddViewToScreen();

    //窗口背景高斯模糊程度
    private int mBackgroundBlurRadius;

    private int mBackgroundCornersRadius;

    // 根据窗口高斯模糊功能是否开启来为窗口设置不同的不透明度
    private final int mWindowBackgroundAlphaWithBlur = 170;

    private final int mWindowBackgroundAlphaNoBlur = 255;

    private Drawable mWindowBackgroundDrawable;


    public WindowManager.LayoutParams lp;

    public NestedScrollView notification;

    public View quick_settings_frame;

    public View notification_center, notification_center_linear, notification_center_title;

    public View notification_quick_settings;

    public View backgroundView;

    public View up;

    public View empty;

    public View down;

    public View center_text;
    public View notification_quit;

    public MyNotification() {

    }

    public void setContext(Context context) {
        mycontext = context;

        //1、初始化LayoutParams描述工具
        initLayoutParams();

        //2、初始化View对象
        initView();

//        //3、给View添加Click、Touch事件监听
        setClickAndTouch();

    }

    private void initLayoutParams() {
        lp = new WindowManager.LayoutParams();
        lp.format = PixelFormat.RGBA_8888;
        //弹出来之后，还能点其它地方
//        lp.flags = WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

//        lp.flags = (WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
//                | WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        lp.flags = WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

//        setBlurBehindRadius(100);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.LEFT;
        lp.x = 20;
    }

    private void initView() {
        Log.d(TAG,String.valueOf(mycontext));
        inflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //读取顶层布局文件
        notification = (NestedScrollView )inflater.inflate(R.layout.notification, null);

        //快捷设置
        quick_settings_frame = notification.findViewById(R.id.quick_settings_frame);
        notification_quick_settings = notification.findViewById(R.id.notification_quick_settings);

        //up
        up = notification.findViewById(R.id.up);

        //暂无任何通知
        empty = notification.findViewById(R.id.empty);

        //消息中心Title
        notification_center_title = notification.findViewById(R.id.notification_center_title);
        center_text = notification.findViewById(R.id.center_text);
        notification_quit = notification.findViewById(R.id.quit);

        //消息中心
        notification_center_linear = notification.findViewById(R.id.notification_center_linear);
        notification_center = notification.findViewById(R.id.notification_center);

        //down
        down = notification.findViewById(R.id.down);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            notification.setForeground(null);
//        }

//        initBlur();

        STATIC_INSTANCE_UTILS.mavts.addView(notification,lp);



//        notification.setBackgroundColor(Color.TRANSPARENT);

    }

    private void setClickAndTouch() {

        notification.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 处理窗口外的触摸事件
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE && notification.getVisibility()==View.VISIBLE) {

//                    notification.setVisibility(View.GONE);
                    STATIC_INSTANCE_UTILS.manimationManager.NotificationHideAnimation();

                    return true; // 返回 true 表示已处理触摸事件
                }
                return false;
            }
        });


        notification_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                notification.setVisibility(View.GONE);
                //有蓝牙通知的话得复位标志位状态
                if(StaticVariableUtils.notification_has_lanya) {
                    //复位所有需要复位的值
                    StaticVariableUtils.lanya_first_accept_android_text = true;
                    StaticVariableUtils.lanya_number = -1;
                    StaticVariableUtils.android_lanya_progress = -1;
                    StaticVariableUtils.lanya_first_transmit = false;
                    StaticVariableUtils.notification_has_lanya = false;
                }

                if (StaticVariableUtils.recyclerView.getChildCount() > 0 ) {
                    StaticVariableUtils.recyclerView.removeAllViews();
                    StaticVariableUtils.recyclerView.getRecycledViewPool().clear();//清除缓存
                    STATIC_INSTANCE_UTILS.notificationCenterAdapter.list.clear();
                    STATIC_INSTANCE_UTILS.notificationCenterAdapter.notifyDataSetChanged();
                    STATIC_INSTANCE_UTILS.myNotification.empty.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void initBlur() {
        mBackgroundBlurRadius = dp2px(40);
        mBackgroundCornersRadius = dp2px(8);
        mWindowBackgroundDrawable = mycontext.getDrawable(R.drawable.shapetop_notification);
        notification.setBackground(mWindowBackgroundDrawable);
        setupWindowBlurListener();
    }

    /**
     * dip转换成px
     */
    private int dp2px(float dpValue) {
        final float scale = mycontext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void setupWindowBlurListener() {
        Consumer<Boolean> windowBlurEnabledListener = this::updateWindowForBlurs;

        notification.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    STATIC_INSTANCE_UTILS.mavts.wm.addCrossWindowBlurEnabledListener(windowBlurEnabledListener);
                }
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    STATIC_INSTANCE_UTILS.mavts.wm.removeCrossWindowBlurEnabledListener(windowBlurEnabledListener);
                }
            }
        });
    }

    private void updateWindowForBlurs(boolean blursEnabled) {
        // 根据窗口高斯模糊功能是否开启来为窗口设置不同的不透明度
        Log.d("gaosimohu "," updateWindowForBlurs");
        mWindowBackgroundDrawable.setAlpha(blursEnabled ? mWindowBackgroundAlphaWithBlur : mWindowBackgroundAlphaNoBlur);//调整背景的透明度
        setBackgroundBlurRadius(notification);//设置背景模糊程度
    }

    /**
     * 为View设置高斯模糊背景
     *
     * @param view
     */
    private void setBackgroundBlurRadius(View view) {
        if (view == null) {
            return;
        }
        ViewParent target = view.getParent();
        Drawable backgroundBlurDrawable = getBackgroundBlurDrawableByReflect(target);
        Drawable originDrawable = view.getBackground();
        Drawable destDrawable = new LayerDrawable(new Drawable[]{backgroundBlurDrawable, originDrawable});
        Log.d("gaosimohu "," view.setBackground(destDrawable)");
        view.setBackground(destDrawable);
    }

    /**
     * 通过反射获取BackgroundBlurDrawable实例对象
     *
     * @param viewRootImpl
     * @return
     */
    private Drawable getBackgroundBlurDrawableByReflect(Object viewRootImpl) {
        Log.d("gaosimohu "," getBackgroundBlurDrawableByReflect");
        Drawable drawable = null;
        try {
            //调用ViewRootImpl的createBackgroundBlurDrawable方法创建实例
            Method method_createBackgroundBlurDrawable = viewRootImpl.getClass().getDeclaredMethod("createBackgroundBlurDrawable");
            method_createBackgroundBlurDrawable.setAccessible(true);
            drawable = (Drawable) method_createBackgroundBlurDrawable.invoke(viewRootImpl);
            Log.d("gaosimohu "," createBackgroundBlurDrawable");

            //调用BackgroundBlurDrawable的setBlurRadius方法
            Method method_setBlurRadius = drawable.getClass().getDeclaredMethod("setBlurRadius", int.class);
            method_setBlurRadius.setAccessible(true);
            method_setBlurRadius.invoke(drawable, mBackgroundBlurRadius);
            Log.d("gaosimohu "," setBlurRadius");

            //调用BackgroundBlurDrawable的setCornerRadius方法
            Method method_setCornerRadius = drawable.getClass().getDeclaredMethod("setCornerRadius", float.class);
            method_setCornerRadius.setAccessible(true);
            method_setCornerRadius.invoke(drawable, mBackgroundCornersRadius);
            Log.d("gaosimohu "," setCornerRadius");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }



}
