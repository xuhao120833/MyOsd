package com.color.notification;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.color.osd.R;
import com.color.osd.models.AddViewToScreen;
import com.color.systemui.interfaces.Instance;

public class NotificationCenter implements Instance {

    private Context mycontext;

    private AddViewToScreen addViewToScreen = new AddViewToScreen();

    private String TAG = "NotificationCenter";

    private LayoutInflater inflater;

    public WindowManager.LayoutParams lp;

    public View view;

    public NotificationCenter() {

    }

    public void setContext(Context context) {
        mycontext = context;

        //1、初始化LayoutParams描述工具
        initLayoutParams();

        //2、初始化View对象
        initView();

        //3、给View添加Click、Touch事件监听
        //setClickAndTouch();

    }

    private void initLayoutParams() {
        lp = new WindowManager.LayoutParams();
        lp.format = PixelFormat.RGBA_8888;
//        lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
//                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //弹出来之后，还能点其它地方
        lp.flags = WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.LEFT;
    }

    private void initView() {
        Log.d(TAG,String.valueOf(mycontext));
        addViewToScreen.setContext(mycontext);
        inflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.notification_center, null);

        Log.d(TAG,String.valueOf(STATIC_INSTANCE_UTILS.mavts));
        //addViewToScreen.addView(view, lp);
    }

}
