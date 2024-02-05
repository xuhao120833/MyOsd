package com.color.notification.models.service;

import android.app.Application;
import android.app.PendingIntent;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.color.notification.Contentobserver.BrightnessChangeObserver;
import com.color.notification.Contentobserver.EyeProtectionObserver;
import com.color.notification.MyNotification;
import com.color.notification.broadcast.BrightnessListener;
import com.color.notification.broadcast.VolumeChangeReceiver;
import com.color.notification.models.Notification_Item;
import com.color.notification.models.Notification_Center_Adapter;
import com.color.notification.models.Notification_Quick_Settings_Adapter;
import com.color.notification.utils.BrightnessChangeCompute;
import com.color.notification.view.CustomSeekBar;
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

    private WindowManager mywindowmanager;

    private Display defaultDisplay;

    private Point size;

    private String lastCountry;


    public LinearLayoutManager notification_center_manager;

    //消息中心备用得manager
    public StaggeredGridLayoutManager verticalManager;

    public LinearLayoutManager quick_settings_manager;

    public android.app.Notification notification;

    public final String TAG = "MyNotificationService";

    public String packageName;

    public String appName;

    public Drawable appIcon;

    public String notificationText;

    public String notificationTitle;

    public PendingIntent contextIntent;

    public float sNoncompatDensity = 0;

    public float sNoncompatScaledDensity = 0;


    public MyNotificationService() {

    }

    @Override
    public void onCreate() {
        if (!StaticVariableUtils.trigger_onCreate) {
            mycontext = getApplicationContext();

            //开机分辨率检测,初始化widthPixels、heightPixels的值，因为自定义View适配分辨率需要
            Display defaultDisplay;
            WindowManager mywindowmanager;
            mywindowmanager = (WindowManager) mycontext.getSystemService(Context.WINDOW_SERVICE);
            defaultDisplay = mywindowmanager.getDefaultDisplay();

            if (defaultDisplay.getWidth() > defaultDisplay.getHeight()) {

                StaticVariableUtils.widthPixels = defaultDisplay.getHeight() * 1920 / 1080;
                StaticVariableUtils.heightPixels = defaultDisplay.getHeight();

            }
            if (defaultDisplay.getWidth() < defaultDisplay.getHeight()) {

                StaticVariableUtils.widthPixels = defaultDisplay.getWidth();
                StaticVariableUtils.heightPixels = defaultDisplay.getWidth() * 1080 / 1920;

            }

            defaultDisplay = null;
            mywindowmanager = null;

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
                StaticVariableUtils.list = list;

            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
            notification_center_manager = new LinearLayoutManager(mycontext);
            quick_settings_manager = new LinearLayoutManager(mycontext);
            verticalManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            notification_center_manager.setOrientation(LinearLayoutManager.VERTICAL);
            quick_settings_manager.setOrientation(LinearLayoutManager.VERTICAL);
            notification_center_manager.setStackFromEnd(true);
            notification_center_manager.setReverseLayout(true);
            verticalManager.setReverseLayout(true);
            //这里使用verticalManager替代notification_center_manager 解决添加蓝牙通知到消息中心时的抖动问题
            recyclerView.setLayoutManager(verticalManager);
            recyclerView.setAdapter(notification_center_adapter);
            Log.d("notification_xu_su ", "1、 recyclerView.setAdapter(notification_center_adapter)");
            recyclerView_quick_settings.setLayoutManager(quick_settings_manager);
            recyclerView_quick_settings.setAdapter(notification_quick_settings_adapter);
            //这里设置为false的意思是，不能拖动NestedScrollingView中的RecyclerView
            recyclerView_quick_settings.setNestedScrollingEnabled(false);

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

            //6、记录语言国籍，在改变语言时使用。
            lastCountry = getResources().getConfiguration().locale.toLanguageTag();

            //7、解决快捷中心闪动问题
//            recyclerView.getItemAnimator().setChangeDuration(0);
//            recyclerView.setHasFixedSize(true);

            //8、监听分斌率变化,下面这段代码，整体上都没用了，分辨率变化放在了MenuService中实现监听。
            //今日头条方案，只使用于Activity
//        CustomDensityUtil.setCustomDensity(MyNotificationService.this, getApplication());
//        //下面的逻辑用来计算density当分辨率变化的时候
//        DisplayMetrics metrics = new DisplayMetrics();
//        mywindowmanager = (WindowManager) mycontext.getSystemService(Context.WINDOW_SERVICE);
//        defaultDisplay = mywindowmanager.getDefaultDisplay();
//        size = new Point();
//        defaultDisplay.getSize(size);
//        StaticVariableUtils.widthPixels = size.x;
//        StaticVariableUtils.heightPixels = size.y;
//        defaultDisplay = mywindowmanager.getDefaultDisplay();
//        mywindowmanager.getDefaultDisplay().getMetrics(metrics);
//        DisplayManager displayManager = (DisplayManager) mycontext.getSystemService(Context.DISPLAY_SERVICE);
//        if (displayManager != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                displayManager.registerDisplayListener(new DisplayManager.DisplayListener() {
//                    @Override
//                    public void onDisplayAdded(int displayId) {
//                        // Do something when a display is added
//                    }
//
//                    @Override
//                    public void onDisplayRemoved(int displayId) {
//                        // Do something when a display is removed
//                    }
//
//                    @Override
//                    public void onDisplayChanged(int displayId) {
//
//                        defaultDisplay.getSize(size);
//                        StaticVariableUtils.widthPixels = size.x;
//                        StaticVariableUtils.heightPixels = size.y;
//
//                        if(StaticVariableUtils.widthPixels == size.x && StaticVariableUtils.heightPixels == size.y ) {
//                            onDisplayChanged(displayId);
//                        } else {
//                            Log.d(" XuDisplayManager"," 分辨率发生变化"+String.valueOf(size.x)+" "+String.valueOf(size.y));
//                        }
//                        StaticVariableUtils.widthPixels = defaultDisplay.getWidth();
//                        StaticVariableUtils.heightPixels = defaultDisplay.getHeight();
//
//                         //Do something when the display properties change, e.g., resolution
//                        DisplayMetrics metrics = new DisplayMetrics();
//                        displayManager.getDisplay(displayId).getMetrics(metrics);
//
//                        StaticVariableUtils.widthPixels = metrics.widthPixels;
//                        StaticVariableUtils.heightPixels = metrics.heightPixels;
//
//                        StaticVariableUtils.shouldUpdateLayout = true;
//
//                        Log.d(" XuDisplayManager"," 分辨率发生变化"+String.valueOf(size.x)+" "+String.valueOf(size.y));
//
//                        //在这里设置各种和LayoutParams有关的边距
//                        Adaptable_resolution();
//
//                    }
//                }, null);
//            }
//        }
        } else if (StaticVariableUtils.trigger_onCreate) {

            mycontext = getApplicationContext();

            for (Notification_Item item : list) {
                // 在这里处理每个元素item
                item.Item_trigger_onCreate = true;
            }

            mynotification.setContext(mycontext);
            setInstance(mynotification);
            recyclerView = STATIC_INSTANCE_UTILS.myNotification.notification.findViewById(R.id.notification_center);
            StaticVariableUtils.recyclerView = recyclerView;
            recyclerView_quick_settings = STATIC_INSTANCE_UTILS.myNotification.notification.findViewById(R.id.notification_quick_settings);

            try {
                //快捷设置
                notification_quick_settings_adapter = new Notification_Quick_Settings_Adapter<>();
                notification_quick_settings_adapter.setContext(mycontext);
                setInstance(notification_quick_settings_adapter);

                //消息中心
                notification_center_adapter = new Notification_Center_Adapter();
                notification_center_adapter.setContext(mycontext, list);
                setInstance(notification_center_adapter);
                StaticVariableUtils.list = list;

            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }

            notification_center_manager = new LinearLayoutManager(mycontext);
            quick_settings_manager = new LinearLayoutManager(mycontext);
            verticalManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            notification_center_manager.setOrientation(LinearLayoutManager.VERTICAL);
            quick_settings_manager.setOrientation(LinearLayoutManager.VERTICAL);
            notification_center_manager.setStackFromEnd(true);
            notification_center_manager.setReverseLayout(true);
            verticalManager.setReverseLayout(true);
            recyclerView.setLayoutManager(verticalManager);
            recyclerView.setAdapter(notification_center_adapter);

            recyclerView_quick_settings.setLayoutManager(quick_settings_manager);
            recyclerView_quick_settings.setAdapter(notification_quick_settings_adapter);

//            StaticVariableUtils.recyclerView.getRecycledViewPool().clear();
//            notification_center_adapter.notifyDataSetChanged();

            lastCountry = getResources().getConfiguration().locale.toLanguageTag();

            //切换语言之后，恢复快捷中心的位置，及按照分辨率适配边距(左侧或者右侧)
            if ("left".equals(StaticVariableUtils.left_or_right)) {
                STATIC_INSTANCE_UTILS.myNotification.lp.gravity = Gravity.LEFT;
            } else if ("right".equals(StaticVariableUtils.left_or_right)) {
                STATIC_INSTANCE_UTILS.myNotification.lp.gravity = Gravity.RIGHT;
            }
            STATIC_INSTANCE_UTILS.myNotification.lp.x = 20 * StaticVariableUtils.widthPixels / 1920;
            STATIC_INSTANCE_UTILS.mavts.wm.updateViewLayout(STATIC_INSTANCE_UTILS.myNotification.notification, STATIC_INSTANCE_UTILS.myNotification.lp);

            StaticVariableUtils.trigger_onCreate = false;

        }


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
                addMsg(sbn);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void addMsg(StatusBarNotification sbn) throws PackageManager.NameNotFoundException {

        notification = sbn.getNotification();
        Log.d("showMsg", "打印通知 " + String.valueOf(notification));
        bundle = sbn.getNotification().extras;
        Log.d("showMsg", "打印extras " + String.valueOf(bundle));
        packageName = sbn.getPackageName();
        appName = getAppName();
        appIcon = getAppIcon();
        // 获取通知内容
        notificationText = (String) notification.extras.getCharSequence(android.app.Notification.EXTRA_TEXT);
        //notificationTitle = (String) notification.extras.getCharSequence(android.app.Notification.EXTRA_TITLE);
        contextIntent = notification.contentIntent;

        Log.d("showMsg", "appName " + appName);

        //蓝牙的单独拿出来处理，并从这里直接返回，后续逻辑不再执行
        if (("蓝牙".equals(appName) || "藍牙".equals(appName) || "Bluetooth".equals(appName)) && notification.extras != null) {
            try {

                Log.d("lanya", " 消息" + String.valueOf(notification.extras));

                //传输进度100%的判断
                if (notification.extras.containsKey("android.text") && StaticVariableUtils.lanya_first_accept_android_text) {
                    String lanya_android_text = notification.extras.getString("android.text");

                    //提取出text中的数字，提取第一个就行
                    StaticVariableUtils.lanya_number = extractNumberFromAndroidText(lanya_android_text);

                    Log.d("lanya", " lanya_android_text " + String.valueOf(StaticVariableUtils.lanya_number));
                    StaticVariableUtils.lanya_first_accept_android_text = false;

                }

                if (notification.extras.containsKey("android.progress")) {
                    Log.d("lanya", " 蓝牙传输百分比" + String.valueOf(notification.extras.getInt("android.progress", 0)));
                    Log.d("lanya", " StaticVariableUtils.notification_has_lanya " + String.valueOf(StaticVariableUtils.notification_has_lanya));
                    StaticVariableUtils.android_lanya_progress = notification.extras.getInt("android.progress", 0);
                    if (StaticVariableUtils.android_lanya_progress == 0 && !StaticVariableUtils.notification_has_lanya) {
                        int i = -1;
                        i = traverse_list(appName);

                        if (i >= 0) {
                            return;
                        }

                        //首次收到蓝牙传输通知，往RecyclerView中添加蓝牙传输进度Item
                        StaticVariableUtils.bluetooth_delivery = "on";

                        //添加蓝牙进度条到
                        notificationItem = new Notification_Item();
                        notificationItem.time = mycontext.getString(R.string.现在);
                        notificationItem.appName = appName;
                        notificationItem.Icon = appIcon;
                        notificationItem.lanya_progress = StaticVariableUtils.android_lanya_progress;
                        notificationItem.pendingIntent = contextIntent;

                        Log.d("lanya", " 添加蓝牙进度通知");

                        StaticVariableUtils.recyclerView.getRecycledViewPool().clear();

                        list.add(notificationItem);
                        StaticVariableUtils.notification_item_lanya = notificationItem;
                        Log.d("notification_xu_su ", "3、 i == -1  notification_center_adapter.notifyItemInserted  插入位置 " + String.valueOf(list.size() - 1));
                        notification_center_adapter.notifyItemInserted(list.size() - 1);

                        StaticVariableUtils.notification_has_lanya = true;

                    }
                    Log.d("lanya"," 断点 ");
                    if(StaticVariableUtils.notification_has_lanya){
                        //后面就是动态的改变传输的进度
                        int i = -1;
                        i = traverse_list(appName);
                        list.get(i).pendingIntent = null;

                        Log.d("lanya"," 设置蓝牙传输进度 "+i);

                        TextView lanya_appName = list.get(i).mynotification_center.findViewById(R.id.appName_lanya);
                        lanya_appName.setText(mycontext.getString(R.string.传输进度));

                        TextView lanya_progress = list.get(i).mynotification_center.findViewById(R.id.seekbar_lanya_text);
                        lanya_progress.setText(StaticVariableUtils.android_lanya_progress + "%");

                        CustomSeekBar lanya_seekbar = list.get(i).mynotification_center.findViewById(R.id.seekbar_lanya);
                        lanya_seekbar.setProgress(StaticVariableUtils.android_lanya_progress);
                    }

                }

                if (notification.extras.containsKey("android.text") && StaticVariableUtils.notification_has_lanya) {
                    String lanya_android_text = notification.extras.getString("android.text");
                    int i = -1;
                    i = traverse_list(appName);
                    TextView transmission = list.get(i).mynotification_center.findViewById(R.id.transmission);
                    transmission.setVisibility(View.VISIBLE);
                    transmission.setText(lanya_android_text);
                }

                if (notification.extras.containsKey("android.title") && StaticVariableUtils.notification_has_lanya) {
                    String lanya_android_title = notification.extras.getString("android.title");
                    if (lanya_android_title.contains("正在接收") || lanya_android_title.contains("Receiving")) {
                        int i = -1;
                        i = traverse_list(appName);
                        TextView filename = list.get(i).mynotification_center.findViewById(R.id.filename);
                        filename.setText(lanya_android_title);
                    }
                }

                //传输进度100%的判断

                //蓝牙非首次传输
                if (!StaticVariableUtils.lanya_first_transmit) {
                    if (notification.extras.containsKey("android.text")) {
                        String lanya_android_text = notification.extras.getString("android.text");

                        //提取出text中的数字
                        int mynumber = extractNumberFromAndroidText(lanya_android_text);
                        Log.d("lanya", " 蓝牙传输number" + mynumber);

                        boolean equal = compareLastDigits(mynumber, StaticVariableUtils.lanya_number);

                        if (mynumber != StaticVariableUtils.lanya_number && StaticVariableUtils.lanya_number != -1 && equal) {
                            int i = -1;
                            i = traverse_list(appName);
                            TextView lanya_appName = list.get(i).mynotification_center.findViewById(R.id.appName_lanya);
                            lanya_appName.setText(mycontext.getString(R.string.传输完成点击查看));

                            TextView lanya_progress = list.get(i).mynotification_center.findViewById(R.id.seekbar_lanya_text);
                            lanya_progress.setText(100 + "%");

                            CustomSeekBar lanya_seekbar = list.get(i).mynotification_center.findViewById(R.id.seekbar_lanya);
                            lanya_seekbar.setProgress(100);


                            StaticVariableUtils.lanya_first_accept_android_text = true;
                            StaticVariableUtils.lanya_number = -1;
                            StaticVariableUtils.android_lanya_progress = -1;

                        } else if (mynumber != StaticVariableUtils.lanya_number && StaticVariableUtils.lanya_number != -1 && !equal) {
                            int i = -1;
                            i = traverse_list(appName);
                            TextView lanya_appName = list.get(i).mynotification_center.findViewById(R.id.appName_lanya);
                            lanya_appName.setText(mycontext.getString(R.string.传输失败));

                            StaticVariableUtils.lanya_first_accept_android_text = true;
                            StaticVariableUtils.lanya_number = -1;
                            StaticVariableUtils.android_lanya_progress = -1;

                        }

                    }
                } else if (StaticVariableUtils.lanya_first_transmit) { //蓝牙首次传输

                    if (notification.extras.containsKey("android.text")) {
                        String lanya_android_text = notification.extras.getString("android.text");
                        //提取出text中的数字
                        int mynumber = extractNumberFromAndroidText(lanya_android_text);
                        if (mynumber == StaticVariableUtils.lanya_number && StaticVariableUtils.lanya_number != -1) {
                            int i = -1;
                            i = traverse_list(appName);
                            TextView lanya_appName = list.get(i).mynotification_center.findViewById(R.id.appName_lanya);
                            lanya_appName.setText(mycontext.getString(R.string.传输完成点击查看));

                            TextView lanya_progress = list.get(i).mynotification_center.findViewById(R.id.seekbar_lanya_text);
                            lanya_progress.setText(100 + "%");

                            CustomSeekBar lanya_seekbar = list.get(i).mynotification_center.findViewById(R.id.seekbar_lanya);
                            lanya_seekbar.setProgress(100);

                            //归位所有需要归位的值
                            StaticVariableUtils.lanya_first_accept_android_text = true;
                            StaticVariableUtils.lanya_number = -1;
                            StaticVariableUtils.android_lanya_progress = -1;
                            StaticVariableUtils.lanya_first_transmit = false;

                        }
                    }
                }

                int i = -1;
                i = traverse_list(appName);
                list.get(i).pendingIntent = contextIntent;

                //首次启用蓝牙传输，走特殊逻辑
                if (StaticVariableUtils.android_lanya_progress > 80 && StaticVariableUtils.lanya_number == -1) {
                    StaticVariableUtils.lanya_first_transmit = true;
                }

                Log.d("lanya", " return");
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }//蓝牙控制结束

        //过滤掉不需要显示的通知
        if (!filterAppName(appName)) {
            return;
        }

        //过滤掉内容为空的通知
        if (!filterContext(notificationText)) {
            return;
        }

        if (notification != null) {

            //遍历list，同一个APP发出的消息就不再重复添加到list中
            int i = -1;//-1表示没有找到同名APP，-2表示找到了同名APP，但是内容相同就过滤掉了，同样不做处理
            i = traverse_list(appName);
            if (i == -2) {
                return;
            }

            Log.d(TAG, " App名字 :" + appName);
            Log.d(TAG, " App包名 :" + packageName);
//            Log.d(TAG,String.valueOf(i));
            Log.d("notification_xu_su ", "3、 同名APP下标值 " + String.valueOf(i));
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

                    content.setText((list.get(i).number + 1) + mycontext.getString(R.string.个通知));
                    Log.d(TAG, appName + "有" + String.valueOf(list.get(i).number) + "个通知");
                    Log.d(TAG, " text值" + String.valueOf(content.getContext()));
//                    list.get(i).content = String.valueOf(content.getContext());

                    if (!list.get(i).isMultiple_Messages) {
                        imageView.setVisibility(View.VISIBLE);
                        list.get(i).isMultiple_Messages = true;
                    }

                    if (list.get(i).isExpand) {
                        Notification_Item notification_item = new Notification_Item();
                        notification_item.appName = list.get(i).appName;
                        notification_item.Icon = list.get(i).Icon;
                        notification_item.subpostion = list.get(i).number;
                        notification_item.content = notificationText;
                        notification_item.pendingIntent = list.get(i).multiple_Intent.get(list.get(i).number);
                        notification_item.parent_ViewHolder = list.get(i - 1).parent_ViewHolder;
                        notification_item.parent_notification_item = list.get(i);
                        StaticVariableUtils.recyclerView.getRecycledViewPool().clear();
                        list.add(i, notification_item);
                        StaticVariableUtils.recyclerView.getRecycledViewPool().clear();
                        notification_center_adapter.notifyItemInserted(i);

                    }
                    Log.d("notification_xu_su ", "3、 i != -1 ");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (Notification_Item notification_item : list) {
                    Log.d("MoreXu ", String.valueOf(notification_item.multiple_content));

                }
            } else {
                //通知是新的，走一般路线。
                notificationItem = new Notification_Item();
//            notificationItem.time = String.valueOf(number++);
                notificationItem.time = mycontext.getString(R.string.现在);
                notificationItem.appName = appName;
                notificationItem.Icon = appIcon;
                notificationItem.content = notificationText;
                notificationItem.pendingIntent = contextIntent;
                notificationItem.multiple_content.add(notificationText);
                notificationItem.multiple_Intent.add(contextIntent);

                Log.d("notification_xu_su ", " list.size()大小 " + String.valueOf(list.size()));
                StaticVariableUtils.recyclerView.getRecycledViewPool().clear();

                list.add(notificationItem);
                Log.d("notification_xu_su ", "3、 i == -1  notification_center_adapter.notifyItemInserted  插入位置 " + String.valueOf(list.size() - 1));
                notification_center_adapter.notifyItemInserted(list.size() - 1);
            }

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

        int i;

        if (list.size() == 0) {

            Log.d(TAG, "第一次收到通知，直接返回");
            return subscript;
        }

        for (i = 0; i < list.size(); i++) {
            Notification_Item notificationItem = list.get(i);
            if (notificationItem.appName.equals(appName)) {
                subscript = i;  // 找到相同应用程序名称，更新下标值
//                list.get(i).number++;
//                list.get(i).multiple_content.add(notificationText);
//                list.get(i).multiple_Intent.add(contextIntent);
//                Log.d(TAG,"traverse_list 找到同名APP");
//                break;  // 找到后可以提前结束循环
            }
        }

        //过滤掉内容重复的通知
        if (subscript != -1 && !traverse_text(notificationText, subscript)) {

            list.get(subscript).number++;
            list.get(subscript).multiple_content.add(notificationText);
            list.get(subscript).multiple_Intent.add(contextIntent);
        } else if (traverse_text(notificationText, subscript)) {
            return -2;
        }


        //返回下标值
        return subscript;
    }

    private boolean traverse_text(String text, int i) {

        try {
            //过滤特定位置的通知，无折叠的情况
            if (list.get(i).content.equals(text)) {
                Log.d("traverse_text", " 重复通知返回");
                return true;//有重复的消息
            }

            //有折叠还要过滤一下子通知的内容，看一下子通知是否有重复的
            for (int n = 0; n < list.get(i).multiple_content.size(); n++) {
                String content = list.get(i).multiple_content.get(n);

                if (text.equals(content)) {
                    return true;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        //返回下标值
        return false;
    }

//    public void Adaptable_resolution() {
//
//        //整个快捷中心的位置
//        STATIC_INSTANCE_UTILS.myNotification.lp.x =20 * StaticVariableUtils.widthPixels / StaticVariableUtils.original_width;
//        STATIC_INSTANCE_UTILS.mavts.wm.updateViewLayout(STATIC_INSTANCE_UTILS.myNotification.notification,STATIC_INSTANCE_UTILS.myNotification.lp);
//
//
//    }

    /**
     * 今日头条适配方案
     *
     * @param context
     * @param application
     */
    public void setCustomDensity(Context context, final Application application) {
        //通过资源文件getResources类获取DisplayMetrics
        DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        if (sNoncompatDensity == 0) {
            //保存之前density值
            sNoncompatDensity = appDisplayMetrics.density;
            //保存之前scaledDensity值，scaledDensity为字体的缩放因子，正常情况下和density相等，但是调节系统字体大小后会改变这个值
            sNoncompatScaledDensity = appDisplayMetrics.scaledDensity;
            //监听设备系统字体切换
            application.registerComponentCallbacks(new ComponentCallbacks() {

                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        //调节系统字体大小后改变的值
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                public void onLowMemory() {

                }
            });
        }

        //获取以设计图总宽度360dp下的density值
        float targetDensity = appDisplayMetrics.widthPixels / 360;
        //通过计算之前scaledDensity和density的比获得scaledDensity值
        float targetScaleDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity);
        //获取以设计图总宽度360dp下的dpi值
        int targetDensityDpi = (int) (160 * targetDensity);
        //设置系统density值
        appDisplayMetrics.density = targetDensity;
        //设置系统scaledDensity值
        appDisplayMetrics.scaledDensity = targetScaleDensity;
        //设置系统densityDpi值
        appDisplayMetrics.densityDpi = targetDensityDpi;

        //获取当前activity的DisplayMetrics
        final DisplayMetrics activityDisplayMetrics = context.getResources().getDisplayMetrics();
        //设置当前activity的density值
        activityDisplayMetrics.density = targetDensity;
        //设置当前activity的scaledDensity值
        activityDisplayMetrics.scaledDensity = targetScaleDensity;
        //设置当前activity的densityDpi值
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    private boolean filterAppName(String appName) {

        if ("VLC".equals(appName) || "osd".equals(appName) || "系统界面".equals(appName) || "Android 系统".equals(appName) || "蓝牙".equals(appName) || "藍牙".equals(appName) || "Bluetooth".equals(appName) || "Android System".equals(appName) || "Android 系統".equals(appName)) {
            return false;
        }

        return true;

    }

    private boolean filterContext(String context) {

        if (context == null || "".equals(context)) {
            Log.d("filterContext", "内容为空，直接过滤");
            return false;
        }

        return true;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //1、语言变化
        //Log.d("onConfigurationChanged", "语言_国家" + newConfig.locale.toLanguageTag());
        if (!newConfig.locale.toLanguageTag().equals(lastCountry)) {
            //Log.d("onConfigurationChanged", "语言变化" + newConfig.locale.toLanguageTag());

            if (STATIC_INSTANCE_UTILS.myNotification.notification.getVisibility() == View.VISIBLE) {

//                notification_center_adapter.notifyDataSetChanged();
                STATIC_INSTANCE_UTILS.myNotification.notification.setVisibility(View.GONE);
                StaticVariableUtils.trigger_onCreate = true;

                onCreate();

                STATIC_INSTANCE_UTILS.myNotification.notification.setVisibility(View.VISIBLE);

            } else {
//                notification_center_adapter.notifyDataSetChanged();
                StaticVariableUtils.trigger_onCreate = true;

                onCreate();
            }

        }
    }

    // 提取数字的方法
    private int extractNumberFromAndroidText(String androidText) {
        // 使用正则表达式提取数字
        String numberStr = androidText.replaceAll("\\D+", "");

        // 将提取的数字字符串转换为整数
        int number = Integer.parseInt(numberStr);

        return number;
    }

    //比较两个数的个位数是否相等
    private boolean compareLastDigits(int num1, int num2) {
        // 获取两个数的个位数字
        int lastDigitNum1 = num1 % 10;
        int lastDigitNum2 = num2 % 10;

        // 比较个位数字是否相等
        return lastDigitNum1 == lastDigitNum2;
    }


}
