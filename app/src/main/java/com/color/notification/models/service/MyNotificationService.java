package com.color.notification.models.service;

import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.color.notification.NotificationCenter;
import com.color.notification.models.Notifi;
import com.color.notification.models.RecycleViewAdapter;
import com.color.osd.R;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.StaticVariableUtils;

import java.util.ArrayList;
import java.util.List;

public class MyNotificationService extends NotificationListenerService implements Instance {

    private Context mycontext;

    private Bundle bundle;

    private PackageManager packageManager;

    private ApplicationInfo applicationInfo;

    private NotificationCenter notificationCenter = new NotificationCenter();

    private RecyclerView recyclerView;

    private RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter();

    private Notifi notifi;

    private int number = 0;

    private List<Notifi> list = new ArrayList<>();

    public Notification notification;

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

        packageManager = mycontext.getPackageManager();

        //添加消息通知View到桌面
//        notificationCenter.setContext(mycontext);
//        setInstance(notificationCenter);
//        recyclerView = STATIC_INSTANCE_UTILS.notificationCenter.view.findViewById(R.id.recycleView);

//        //初始化RecycleViewAdapter
//        try {
//            recycleViewAdapter.setContext(mycontext, list);
//            setInstance(recycleViewAdapter);
//        } catch (PackageManager.NameNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        LinearLayoutManager manager = new LinearLayoutManager(mycontext);
//
//        manager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(recycleViewAdapter);
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
        //Log.d(TAG, "onNotificationPosted");
//        try {
//            showMsg(sbn);
//        } catch (PackageManager.NameNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void showMsg(StatusBarNotification sbn) throws PackageManager.NameNotFoundException {

        notification = sbn.getNotification();
        bundle = sbn.getNotification().extras;
        packageName = sbn.getPackageName();
        appName = getAppName();
        appIcon = getAppIcon();

        if (notification != null) {
            // 获取通知内容
            notificationText = (String) notification.extras.getCharSequence(Notification.EXTRA_TEXT);
            notificationTitle = (String) notification.extras.getCharSequence(Notification.EXTRA_TITLE);

            notifi = new Notifi();
            notifi.time = String.valueOf(number++);
            notifi.appName = appName;
            notifi.Icon = appIcon;
            notifi.content = notificationText;

            list.add(notifi);
            Log.d(TAG, "内容: " + notificationText);
            Log.d(TAG, "标题: " + notificationTitle);
            Log.d(TAG, "APP包名: " + packageName);
            Log.d(TAG, "APP名字: " + appName);
            Log.d(TAG, "APP图标: " + String.valueOf(appIcon));
            //Log.d(TAG, "APP图标大小： " + "高——>" + String.valueOf(appIcon.getIntrinsicHeight()) + " 宽——>" + String.valueOf(appIcon.getIntrinsicWidth()));
            Log.d(TAG, "   ");
            recycleViewAdapter.notifyItemInserted(list.size() - 1);


        } else {
            //Log.d(TAG, "is null ...." + packageName);
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        //Log.d(TAG, "onNotificationRemoved");
        //showMsg(sbn);
    }

    private String getAppName() throws PackageManager.NameNotFoundException {
        //Log.d(TAG,String.valueOf(mycontext));

        return (String) packageManager.getApplicationInfo(packageName,0).loadLabel(getPackageManager());
    }

    private Drawable getAppIcon() throws PackageManager.NameNotFoundException {
        return packageManager.getApplicationInfo(packageName,0).loadIcon(getPackageManager());
    }



}

