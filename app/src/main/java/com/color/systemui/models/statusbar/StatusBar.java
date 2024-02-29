package com.color.systemui.models.statusbar;

import android.content.Context;
import android.media.Image;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.LayoutInflater;

import com.color.osd.R;
import com.color.systemui.broadcast.statusbar.IconsStateChanageManager;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import android.graphics.PixelFormat;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class StatusBar implements Instance {

    private Context mycontext;

    private LayoutInflater inflater;

    private IconsStateChanageManager iconsStateChanageManager = new IconsStateChanageManager();

    private SystemTopActivityChange systemTopActivityChange = new SystemTopActivityChange();

    public StatusBarBootCheck statusBarBootCheck = new StatusBarBootCheck();

    public WindowManager.LayoutParams lp;

    public LinearLayout statusbar;

    public ImageView udisk, wifi, ethernet, mobile, hotspot;

    public View udisk_frame, wifi_frame, ethernet_frame, mobile_frame, hotspot_frame;

    public StatusBar() {

    }

    public void setContext(Context context) {
        try {
            mycontext = context;

            //1、初始化LayoutParams描述工具
            initLayoutParams();

            //2、初始化View对象
            initView();

            //3、初始化状态栏图标管理类
            initIconsManager();

            //4、注册TopActivity监听，在非launcher，非settings里面状态栏置为GONE

            systemTopActivityChange.setContext(mycontext);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    private void initLayoutParams() {

        lp = new WindowManager.LayoutParams();
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.RIGHT | Gravity.TOP;
        lp.format = PixelFormat.RGBA_8888;

    }

    private void initView() {

        inflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        statusbar = (ViewGroup) inflater.inflate(R.layout.statusbar, null);
//        udisk = (ImageView) statusbar.findViewById(R.id.Udisk);
//        wifi = (ImageView) statusbar.findViewById(R.id.Wifi);
//        ethernet = (ImageView) statusbar.findViewById(R.id.Ethernet);
//        mobile = (ImageView) statusbar.findViewById(R.id.Mobile);
//        hotspot = (ImageView) statusbar.findViewById(R.id.Hotspot);

        statusbar = (LinearLayout) inflater.inflate(R.layout.new_statusbar, null);
        udisk_frame = inflater.inflate(R.layout.new_statusbar_icon, null);
        udisk = (ImageView) udisk_frame.findViewById(R.id.statusbar_icon);

        wifi_frame = inflater.inflate(R.layout.new_statusbar_icon, null);
        wifi = (ImageView) wifi_frame.findViewById(R.id.statusbar_icon);

        ethernet_frame = inflater.inflate(R.layout.new_statusbar_icon, null);
        ethernet = (ImageView) ethernet_frame.findViewById(R.id.statusbar_icon);

        mobile_frame = inflater.inflate(R.layout.new_statusbar_icon, null);
        mobile = (ImageView) mobile_frame.findViewById(R.id.statusbar_icon);

        hotspot_frame = inflater.inflate(R.layout.new_statusbar_icon, null);
        hotspot = (ImageView) hotspot_frame.findViewById(R.id.statusbar_icon);

    }

    private void initIconsManager() {
        iconsStateChanageManager.setContext(mycontext);
    }

    public void start() {

        STATIC_INSTANCE_UTILS.mavts.addView(statusbar, lp);

//        statusbar.setVisibility(View.GONE);

        //开机检测wifi是否打开、Usb设备是否插入、以太网是否连接，初始化状态栏
        statusBarBootCheck.setContext(mycontext);

        //开机还原上一次关机的设置
        if (Settings.System.getInt(mycontext.getContentResolver(),
                STATIC_INSTANCE_UTILS.settingsControlStatusBarObserver.OPEN_STATUS_BAR, 0) == 1) {
            Log.d("startStatusBar ", " 开机状态栏Visible");
//            statusbar.setVisibility(View.VISIBLE);

            STATIC_INSTANCE_UTILS.manimationManager.statusbarShowAnimation();
            StaticVariableUtils.SettingsControlStatusBarVisible = true;
        } else {
            Log.d("startStatusBar ", " 开机状态栏GONE");
            wifi_frame.setVisibility(View.GONE);
            udisk_frame.setVisibility(View.GONE);
            ethernet_frame.setVisibility(View.GONE);
            hotspot_frame.setVisibility(View.GONE);
            statusbar.setVisibility(View.GONE);
        }

        Log.d("startStatusBar ", " statusbar可见性" + statusbar.getVisibility());

        Log.d("startStatusBar ", " wifi可见性" + wifi.getVisibility());
        Log.d("startStatusBar ", " wifi_frame可见性" + wifi_frame.getVisibility());
        Log.d("startStatusBar ", " usb可见性" + udisk.getVisibility());
        Log.d("startStatusBar ", " udisk_frame可见性" + udisk_frame.getVisibility());



//        STATIC_INSTANCE_UTILS.mavts.addView(statusbar, lp);
    }

}
