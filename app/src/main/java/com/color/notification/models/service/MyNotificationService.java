package com.color.notification.models.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.color.notification.Contentobserver.BrightnessChangeObserver;
import com.color.notification.Contentobserver.EyeProtectionObserver;
import com.color.notification.MyNotification;
import com.color.notification.models.Notification_Item;
import com.color.notification.models.Notification_Center_Adapter;
import com.color.notification.models.Notification_Quick_Settings_Adapter;
import com.color.notification.utils.BrightnessChangeCompute;
import com.color.osd.R;
import com.color.osd.models.AddViewToScreen;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.StaticVariableUtils;

import java.util.ArrayList;
import java.util.List;

public class MyNotificationService extends NotificationListenerService implements Instance {

    private Context mycontext;

    private Bundle bundle;

    private PackageManager packageManager;

    private ApplicationInfo applicationInfo;

    private AddViewToScreen avts = new AddViewToScreen();

    private MyNotification mynotification = new MyNotification();

    private RecyclerView recyclerView_quick_settings;

    private RecyclerView recyclerView;

    private Notification_Center_Adapter notification_center_adapter = new Notification_Center_Adapter();

    private Notification_Quick_Settings_Adapter notification_quick_settings_adapter = new Notification_Quick_Settings_Adapter<>();

    private Notification_Item notificationItem;

    private int number = 0;//消息通知计数

    private List<Notification_Item> list = new ArrayList<>();

    private EyeProtectionObserver eyeProtectionObserver = new EyeProtectionObserver();

    private BrightnessChangeObserver brightnessChangeObserver = new BrightnessChangeObserver();

    private BrightnessChangeCompute brightnessChangeCompute = new BrightnessChangeCompute();

    public android.app.Notification notification;

    public final String TAG = "MyNotificationService";

    public String packageName;

    public String appName;

    public Drawable appIcon;

    public String notificationText;

    public String notificationTitle;

    public MyNotificationService() {

    }

    @Override
    public void onCreate() {
        mycontext = getApplicationContext();

        //1、初始化工具类
        //包管理
        packageManager = mycontext.getPackageManager();
        if (STATIC_INSTANCE_UTILS.mavts == null) {
            avts.setContext(mycontext);
            setInstance(avts);
        }
        Log.d("mavts", "消息中心初始化avts工具类");
        //亮度设置工具类
        brightnessChangeCompute.setContext(mycontext);
        setInstance(brightnessChangeCompute);

        //2、添加View到桌面，包括两个部分：快捷设置和消息中心
        mynotification.setContext(mycontext);
        setInstance(mynotification);
        recyclerView = STATIC_INSTANCE_UTILS.myNotification.notification.findViewById(R.id.notification_center);
        recyclerView_quick_settings = STATIC_INSTANCE_UTILS.myNotification.notification.findViewById(R.id.notification_quick_settings);

        //3、初始化RecycleViewAdapter，并添加管理器
        try {
            //快捷设置
            notification_quick_settings_adapter.setContext(mycontext);
            setInstance(notification_quick_settings_adapter);

            //消息中心
            notification_center_adapter.setContext(mycontext, list);
            setInstance(notification_center_adapter);

        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        LinearLayoutManager manager = new LinearLayoutManager(mycontext);
        LinearLayoutManager manager2 = new LinearLayoutManager(mycontext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager2.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(notification_center_adapter);
        recyclerView_quick_settings.setLayoutManager(manager2);
        recyclerView_quick_settings.setAdapter(notification_quick_settings_adapter);

        //4、各种ContentObserver
        //护眼模式
        eyeProtectionObserver.setContext(mycontext);
        mycontext.getContentResolver().registerContentObserver(Settings.Global.getUriFor(StaticVariableUtils.EYE_PROTECT_MODE), true, eyeProtectionObserver);
        //亮度变化
        brightnessChangeObserver.setContext(mycontext);
        mycontext.getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), true, brightnessChangeObserver);

        STATIC_INSTANCE_UTILS.myNotification.notification.clearFocus();
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        //Log.d(TAG, "onListenerConnected");
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        //Log.d(TAG, "onListenerDisconnected");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d(TAG, "onNotificationPosted");
        try {
            showMsg(sbn);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void showMsg(StatusBarNotification sbn) throws PackageManager.NameNotFoundException {

        notification = sbn.getNotification();
        bundle = sbn.getNotification().extras;
        packageName = sbn.getPackageName();
        appName = getAppName();
        appIcon = getAppIcon();

        if (notification != null) {
            // 获取通知内容
            notificationText = (String) notification.extras.getCharSequence(android.app.Notification.EXTRA_TEXT);
            notificationTitle = (String) notification.extras.getCharSequence(android.app.Notification.EXTRA_TITLE);

            notificationItem = new Notification_Item();
            notificationItem.time = String.valueOf(number++);
            notificationItem.appName = appName;
            notificationItem.Icon = appIcon;
            notificationItem.content = notificationText;

            list.add(notificationItem);
//            Log.d(TAG, "内容: " + notificationText);
//            Log.d(TAG, "标题: " + notificationTitle);
//            Log.d(TAG, "APP包名: " + packageName);
//            Log.d(TAG, "APP名字: " + appName);
//            Log.d(TAG, "APP图标: " + String.valueOf(appIcon));
//            //Log.d(TAG, "APP图标大小： " + "高——>" + String.valueOf(appIcon.getIntrinsicHeight()) + " 宽——>" + String.valueOf(appIcon.getIntrinsicWidth()));
//            Log.d(TAG, "   ");
            notification_center_adapter.notifyItemInserted(list.size() - 1);


        } else {
            Log.d(TAG, "myNotification is null ...." + packageName);
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        //Log.d(TAG, "onNotificationRemoved");
        //showMsg(sbn);
    }

    private String getAppName() throws PackageManager.NameNotFoundException {
        //Log.d(TAG,String.valueOf(mycontext));

        return (String) packageManager.getApplicationInfo(packageName, 0).loadLabel(getPackageManager());
    }

    private Drawable getAppIcon() throws PackageManager.NameNotFoundException {
        return packageManager.getApplicationInfo(packageName, 0).loadIcon(getPackageManager());
    }


}

