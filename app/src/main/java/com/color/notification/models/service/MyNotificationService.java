package com.color.notification.models.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManagerGlobal;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.internal.globalactions.Action;
import com.color.notification.Contentobserver.BrightnessChangeObserver;
import com.color.notification.Contentobserver.EyeProtectionObserver;
import com.color.notification.MyNotification;
import com.color.notification.broadcast.BrightnessListener;
import com.color.notification.broadcast.VolumeChangeReceiver;
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

    public RecyclerView recyclerView;

    private Notification_Center_Adapter notification_center_adapter = new Notification_Center_Adapter();

    private Notification_Quick_Settings_Adapter notification_quick_settings_adapter = new Notification_Quick_Settings_Adapter<>();

    private Notification_Item notificationItem;

    private int number = 0;//消息通知计数

    public List<Notification_Item> list = new ArrayList<>();

    private EyeProtectionObserver eyeProtectionObserver = new EyeProtectionObserver();

    private BrightnessChangeObserver brightnessChangeObserver = new BrightnessChangeObserver();

    private BrightnessChangeCompute brightnessChangeCompute = new BrightnessChangeCompute();

    private VolumeChangeReceiver volumeChangeReceiver = new VolumeChangeReceiver();

    private BrightnessListener brightnessListener = new BrightnessListener();

    public LinearLayoutManager notification_center_manager;
    public LinearLayoutManager quick_settings_manager;

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
        StaticVariableUtils.recyclerView = recyclerView;
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
        notification_center_manager = new LinearLayoutManager(mycontext);
        quick_settings_manager = new LinearLayoutManager(mycontext);
        notification_center_manager.setOrientation(LinearLayoutManager.VERTICAL);
        quick_settings_manager.setOrientation(LinearLayoutManager.VERTICAL);
        notification_center_manager.setStackFromEnd(true);
        notification_center_manager.setReverseLayout(true);
        recyclerView.setLayoutManager(notification_center_manager);
        recyclerView.setAdapter(notification_center_adapter);
        Log.d("notification_xu_su ", "1、 recyclerView.setAdapter(notification_center_adapter)");
//        notification_center_adapter.setOnItemClickListener(new Notification_Center_Adapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//
//            }
//        });
        recyclerView_quick_settings.setLayoutManager(quick_settings_manager);
        recyclerView_quick_settings.setAdapter(notification_quick_settings_adapter);

        //4、各种ContentObserver
        //护眼模式
        eyeProtectionObserver.setContext(mycontext);
        mycontext.getContentResolver().registerContentObserver(Settings.Global.getUriFor(StaticVariableUtils.EYE_PROTECT_MODE), true, eyeProtectionObserver);
//        //亮度变化
        brightnessListener.setContext(mycontext);
        DisplayManagerGlobal.getInstance().registerDisplayListener(brightnessListener, null, DisplayManager.EVENT_FLAG_DISPLAY_BRIGHTNESS);
//        brightnessChangeObserver.setContext(mycontext);
//        mycontext.getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), true, brightnessChangeObserver);

        //5、各种Broadcast
        //音量变化
        volumeChangeReceiver.setContext(mycontext);
        IntentFilter intentFilter = new IntentFilter("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(volumeChangeReceiver, intentFilter);

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
            synchronized (mycontext) {
                Log.d("notification_xu_su ", "2、 showMsg");
                showMsg(sbn);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void showMsg(StatusBarNotification sbn) throws PackageManager.NameNotFoundException {

        notification = sbn.getNotification();
        Log.d("showMsg", "打印通知 " + String.valueOf(notification));
        bundle = sbn.getNotification().extras;
        Log.d("showMsg", "打印extras " + String.valueOf(bundle));
        packageName = sbn.getPackageName();
        appName = getAppName();
        appIcon = getAppIcon();
        // 获取通知内容
        notificationText = (String) notification.extras.getCharSequence(android.app.Notification.EXTRA_TEXT);
        notificationTitle = (String) notification.extras.getCharSequence(android.app.Notification.EXTRA_TITLE);

        // 从contentIntent中获取链接
        // PendingIntent contentIntent = notification.contentIntent;
//        if (contentIntent != null) {
//            try {
//                contentIntent.send();
//                Log.d("showMsg"," notification.contentIntent" +String.valueOf(contentIntent));
//                // 如果有链接，这里可以进行进一步的处理
//            } catch (PendingIntent.CanceledException e) {
//                Log.e(TAG, "Error sending contentIntent: " + e.getMessage());
//            }
//        }


        if (notification != null) {

            //遍历list，同一个APP发出的消息就不再重复添加到list中
            int i =-1;//-1表示没有找到同名APP
            i = traverse_list(appName);
            Log.d(TAG, " App名字 :"+appName);
            Log.d(TAG, " App包名 :"+packageName);
//            Log.d(TAG,String.valueOf(i));
            Log.d("notification_xu_su ", "3、 同名APP下标值 "+ String.valueOf(i));
//            // 获取通知内容
//            notificationText = (String) notification.extras.getCharSequence(android.app.Notification.EXTRA_TEXT);
//            notificationTitle = (String) notification.extras.getCharSequence(android.app.Notification.EXTRA_TITLE);

            //遍历text，只有当APP名字相同，但是text内容不同时，才会判定为同一个APP的另一个有效通知
            if (i != -1) {
                //同一个APP的不重复消息，做折叠操作，重复消息不做处理
                //notifyItemChanged(i);
                try {
//                    Log.d(TAG, " mynotification_center :"+String.valueOf(list.get(i).mynotification_center));
                    TextView content = (TextView) list.get(i).mynotification_center.findViewById(R.id.content);
                    ImageView imageView = (ImageView) list.get(i).mynotification_center.findViewById(R.id.Up_Or_Down);
//                    list.get(i).number++;
                    content.setText(list.get(i).number + "个通知");
                    Log.d(TAG, appName+"有"+String.valueOf(list.get(i).number)+ "个通知");
                    Log.d(TAG, " text值"+String.valueOf(content.getContext()));
                    list.get(i).content = String.valueOf(content.getContext());

                    imageView.setVisibility(View.VISIBLE);
                    list.get(i).isMultiple_Messages = true;
                    Log.d("notification_xu_su ", "3、 i != -1 ");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for(Notification_Item notification_item : list) {
                    Log.d("MoreXu ", String.valueOf(notification_item.multiple_content));

                }
            } else {
                //通知是新的，走一般路线。
                notificationItem = new Notification_Item();
//            notificationItem.time = String.valueOf(number++);
                notificationItem.time = "现在";
                notificationItem.appName = appName;
                notificationItem.Icon = appIcon;
                notificationItem.multiple_content.add(notificationText);
                notificationItem.content = notificationText;
                notificationItem.pendingIntent = notification.contentIntent;

                Log.d("notification_xu_su ", " list.size()大小 " + String.valueOf(list.size()));

                list.add(notificationItem);
                notification_center_adapter.notifyItemInserted(list.size() - 1);
                //注意，notifyItemInserted有个比较恶心的特性：插入的位置在list末尾会按照 onCreateViewHolder ——> onBindViewHolder 的顺序调用
                //插入的位置如果在list中间，则会直接调用onBindViewHolder
                Log.d("notification_xu_su ", "3、 i == -1  notification_center_adapter.notifyItemInserted  插入位置 " + String.valueOf(list.size() - 1));
            }
//            Log.d(TAG,"list.size() - 1 的值 "+String.valueOf(list.size() - 1));
//            Log.d(TAG,"     ");

//            notificationItem = new Notification_Item();
////            notificationItem.time = String.valueOf(number++);
//            notificationItem.time = "现在";
//            notificationItem.appName = appName;
//            notificationItem.Icon = appIcon;
//            notificationItem.content = notificationText;
//            notificationItem.pendingIntent = notification.contentIntent;
//
//            list.add(notificationItem);
//            Log.d(TAG, "内容: " + notificationText);
//            Log.d(TAG, "标题: " + notificationTitle);
//            Log.d(TAG, "APP包名: " + packageName);
//            Log.d(TAG, "APP名字: " + appName);
//            Log.d(TAG, "APP图标: " + String.valueOf(appIcon));
//            //Log.d(TAG, "APP图标大小： " + "高——>" + String.valueOf(appIcon.getIntrinsicHeight()) + " 宽——>" + String.valueOf(appIcon.getIntrinsicWidth()));
//            Log.d(TAG, "   ");
//            notification_center_adapter.notifyItemInserted(list.size() - 1);

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

    private int traverse_list(String appName) {

        int subscript = -1;

        if(list.size() == 0) {

            Log.d(TAG,"第一次收到通知，直接返回");
            return subscript;
        }

        for (int i = 0; i < list.size(); i++) {
            Notification_Item notificationItem = list.get(i);
            if (notificationItem.appName.equals(appName)) {
                subscript = i;  // 找到相同应用程序名称，更新下标值
                list.get(i).number++;
                list.get(i).multiple_content.add(notificationText);
                Log.d(TAG,"traverse_list 找到同名APP");
                break;  // 找到后可以提前结束循环
            }
        }

        //返回下标值
        return subscript;
    }

    private boolean traverse_text(String text, int i) {

        if (list.get(i).content.equals(text)) {
            return true;//有重复的消息
        }
        //返回下标值
        return false;
    }

}
