package com.color.notification.models.service;

import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.util.Log;

public class MyNotificationService extends NotificationListenerService {

    private Context mycontext;

    private Bundle bundle;

    private String packageName;

    private String AppName;

    public Notification notification;

    public final String TAG = "MyNotificationService";

    public MyNotificationService() {

    }

    public void setContext(Context context) {
        mycontext = context;
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d(TAG,"onListenerConnected");
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        Log.d(TAG,"onListenerDisconnected");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d(TAG,"onNotificationPosted");
        showMsg(sbn);
    }

    private void showMsg(StatusBarNotification sbn) {

        notification = sbn.getNotification();

        bundle = sbn.getNotification().extras;

        packageName = sbn.getPackageName();

        AppName = getAppName();

        if (notification!=null){
            // 获取通知内容
            CharSequence notificationText = notification.extras.getCharSequence(Notification.EXTRA_TEXT);
            CharSequence notificationTitle = notification.extras.getCharSequence(Notification.EXTRA_TITLE);

            Log.d(TAG,"内容: " + notificationText);
            Log.d(TAG,"标题: " + notificationTitle);
            Log.d(TAG,"APP包名: " + packageName);
            Log.d(TAG,"APP名字: " + AppName);
            Log.d(TAG,"   ");

            // 获取大图标
            int largeIconResId = notification.extras.getInt(Notification.EXTRA_LARGE_ICON);
            Bitmap largeIcon = (Bitmap) notification.extras.getParcelable(Notification.EXTRA_LARGE_ICON);
            Drawable drawable = new BitmapDrawable(getResources(), largeIcon);//Bitmap 转换成drawable
            //imageView.setImageBitmap(bitmap);

            // 获取小图标
            int smallIconResId = notification.icon;
            Drawable smallIcon = getApplicationContext().getResources().getDrawable(smallIconResId);

        }else{
            Log.d(TAG,"is null ...."+packageName);
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d(TAG,"onNotificationRemoved");
        //showMsg(sbn);
    }

    private String getAppName() {
        PackageManager packageManager = getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return packageName; // 如果找不到应用信息，返回包名
        }

        return (String) packageManager.getApplicationLabel(applicationInfo);
    }

}

