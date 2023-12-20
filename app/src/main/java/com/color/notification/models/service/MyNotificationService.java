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

public class MyNotificationService extends NotificationListenerService {

    private Context mycontext;

    private Bundle bundle;

    private String packageName;

    private String AppName;

    private Drawable AppIcon;

    private PackageManager packageManager;

    ApplicationInfo applicationInfo;

    public Notification notification;

    public final String TAG = "MyNotificationService";

    public MyNotificationService() {

    }

    @Override
    public void onCreate() {
        mycontext = getApplicationContext();

        packageManager = mycontext.getPackageManager();
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

        AppName = getAppName();

        AppIcon = getAppIcon();

        if (notification != null) {
            // 获取通知内容
            CharSequence notificationText = notification.extras.getCharSequence(Notification.EXTRA_TEXT);
            CharSequence notificationTitle = notification.extras.getCharSequence(Notification.EXTRA_TITLE);


            Log.d(TAG, "内容: " + notificationText);
            Log.d(TAG, "标题: " + notificationTitle);
            Log.d(TAG, "APP包名: " + packageName);
            Log.d(TAG, "APP名字: " + AppName);
            Log.d(TAG, "APP图标: " + String.valueOf(AppIcon));
            //Log.d(TAG, "APP图标大小： " + "高——>" + String.valueOf(appIcon.getIntrinsicHeight()) + " 宽——>" + String.valueOf(appIcon.getIntrinsicWidth()));
            Log.d(TAG, "   ");


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
        Log.d(TAG,String.valueOf(mycontext));

        return (String) packageManager.getApplicationInfo(packageName,0).loadLabel(getPackageManager());
    }

    private Drawable getAppIcon() throws PackageManager.NameNotFoundException {
        return packageManager.getApplicationInfo(packageName,0).loadIcon(getPackageManager());
    }



}

