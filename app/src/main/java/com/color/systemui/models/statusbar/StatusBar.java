package com.color.systemui.models.statusbar;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.LayoutInflater;
import com.color.osd.R;
import com.color.systemui.broadcast.statusbar.IconsStateChanageManager;
import com.color.systemui.utils.StaticInstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;
import android.graphics.PixelFormat;
import android.widget.ImageView;

public class StatusBar {

    private Context mycontext;

    private LayoutInflater inflater;

    private IconsStateChanageManager iconsStateChanageManager = new IconsStateChanageManager();

    private SystemTopActivityChange systemTopActivityChange = new SystemTopActivityChange();

    public StatusBarBootCheck statusBarBootCheck = new StatusBarBootCheck();

    public WindowManager.LayoutParams lp;

    public View statusbar;

    public ImageView udisk , wifi , ethernet , mobile , hotspot;

    public StatusBar() {

    }

    public void setContext(Context context) {

        mycontext = context;

        //1、初始化LayoutParams描述工具
        initLayoutParams();

        //2、初始化View对象
        initView();

        //3、初始化状态栏图标管理类
        initIconsManager();

        //4、注册TopActivity监听，在非launcher，非settings里面状态栏置为GONE
        try {
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
        statusbar = inflater.inflate(R.layout.statusbar, null);
        udisk = (ImageView) statusbar.findViewById(R.id.Udisk);
        wifi = (ImageView) statusbar.findViewById(R.id.Wifi);
        ethernet = (ImageView) statusbar.findViewById(R.id.Ethernet);
        mobile = (ImageView) statusbar.findViewById(R.id.Mobile);
        hotspot = (ImageView) statusbar.findViewById(R.id.Hotspot);

    }

    private void initIconsManager() {
        iconsStateChanageManager.setContext(mycontext);
    }

    public void start() {

        //开机检测wifi是否打开、Usb设备是否插入、以太网是否连接，初始化状态栏
        statusBarBootCheck.setContext(mycontext);

        StaticInstanceUtils.statusBar.statusbar.setVisibility(View.GONE);
        //开机还原上一次关机的设置
        if(Settings.System.getInt(mycontext.getContentResolver(),
                StaticInstanceUtils.settingsControlStatusBarObserver.OPEN_STATUS_BAR, 0) == 1) {
            Log.d("startStatusBar ", " 开机状态栏显示");
            StaticInstanceUtils.statusBar.statusbar.setVisibility(View.VISIBLE);
            StaticVariableUtils.SettingsControlStatusBarVisible = true;
        }
        StaticInstanceUtils.mavts.addView(statusbar,lp);
    }
}
