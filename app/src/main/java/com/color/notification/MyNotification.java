package com.color.notification;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.core.widget.NestedScrollView;

import com.color.osd.R;
import com.color.osd.models.AddViewToScreen;
import com.color.systemui.interfaces.Instance;

public class MyNotification implements Instance {

    private Context mycontext;

    private String TAG = "MyNotification";

    private LayoutInflater inflater;

    private AddViewToScreen mavts = new AddViewToScreen();

    public WindowManager.LayoutParams lp;

    public NestedScrollView notification;

    public View quick_settings_linear;

    public View notification_center_linear;

    public View notification_quick_settings;

    public View up;

    public View down;

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
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

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
        quick_settings_linear = notification.findViewById(R.id.quick_settings_linear);
        notification_center_linear = notification.findViewById(R.id.notification_center_linear);
        notification_quick_settings = notification.findViewById(R.id.notification_quick_settings);
        up = notification.findViewById(R.id.up);
        down = notification.findViewById(R.id.down);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            notification.setForeground(null);
//        }

        STATIC_INSTANCE_UTILS.mavts.addView(notification,lp);
//        notification.setBackgroundColor(Color.TRANSPARENT);

    }

    private void setClickAndTouch() {


//        notification.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Log.d("MyNotification"," 收到按键事件 "+ event.getKeyCode());
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    // 返回false，允许系统继续处理Back键事件
//                    notification.setVisibility(View.GONE);
//                    return true;
//                }
//                return false;
//            }
//        });
    }



}
