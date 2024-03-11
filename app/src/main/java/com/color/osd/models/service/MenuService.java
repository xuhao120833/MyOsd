package com.color.osd.models.service;

import static com.color.osd.models.Enum.MenuState.NULL;

import android.accessibilityservice.AccessibilityGestureEvent;
import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;

//import com.color.myNotification.models.service.MyNotificationService;
import com.blankj.utilcode.util.AdaptScreenUtils;
import com.color.notification.models.BlurWindowHelper;
import com.color.notification.models.Notification_Item;
import com.color.osd.ContentObserver.BootFinishContentObserver;
import com.color.osd.ContentObserver.WindowManagerServiceObserver;
import com.color.osd.models.Enum.MenuState;
import com.color.osd.models.Menu_source;
import com.color.osd.models.interfaces.DispatchKeyEventInterface;
import com.color.osd.models.interfaces.VolumeChangeListener;
import com.color.osd.ui.DialogMenu;
import com.color.osd.broadcast.VolumeChangeReceiver;
import com.color.osd.utils.ConstantProperties;
import com.color.osd.ContentObserver.BrightnessObeserver;
import com.color.osd.models.interfaces.BrightnessChangeListener;
import com.color.osd.broadcast.VolumeFromFWReceiver;
import com.color.systemui.MySystemUI;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.StaticVariableUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.media.AudioManager;

import com.color.osd.ContentObserver.BrightnessObeserver;
import com.color.osd.broadcast.VolumeChangeReceiver;
import com.color.osd.broadcast.VolumeFromFWReceiver;
import com.color.osd.models.interfaces.BrightnessChangeListener;


public class MenuService extends AccessibilityService implements VolumeChangeListener, BrightnessChangeListener, Instance {

    Context mycontext;

    String TAG = "MenuService";
    //ArrayList<Integer> myarrylist = new ArrayList<>();
    static int number = 0;
    //static int Menumber = 0;
    public static boolean menuOn = false;

    public static boolean settingVolumeChange = false;

    public static MenuState menuState;
    public List<DispatchKeyEventInterface> listenerList = new ArrayList<>();  // 初始化keyEvent的监听list集合

    public static DialogMenu dialogMenu;

    VolumeChangeReceiver volumeChangeReceiver;

    VolumeFromFWReceiver fromFWReceiver;

    private static final String OSD_OPEN_OTHER_SOURCE = "osd_open_other_source";

    private static final String SYSTEM_RESOLUTION_CHANGE = "system_resolution_change";

    //监听WindowManagerService发来的信息
    //static final String WINDOW_MANAGER_TO_OSD = "window_manager_to_osd";

    //将WindowManagerService发来的信息转发给SystemUI
    private final String WINDOWMANAGER_OSD_TO_SYSTEMUI = "windowmanager_osd_to_systemui";

    boolean ceshi;

    public static Handler mainHandler;

    Looper mainLooper = Looper.getMainLooper();

    private Configuration mOldConfig;   // 存储当前的配置

    int lastWidthDp, lastHeightDp;

    String lastCountry;

    int screenWidthDp, screenHeightDp, b;

    BootFinishContentObserver bootObserver;

    private BrightnessObeserver brightnessObeserver;

    WindowManagerServiceObserver windowOsd;

    public static boolean initcomplete = false;

    Message m;

    int fswitch;

    LongClickSimulate brightnessLongClickRunnable;
    LongClickSimulate volumeLongClickRunnable;

    ExecutorService executors = Executors.newSingleThreadExecutor();

    private boolean isDown;
    private long downTime;
    private long sleepTime;

    List<Integer> clickList = new ArrayList<>();

    //线程池
    ExecutorService executor = Executors.newSingleThreadExecutor();

    int size;

    AccessibilityEvent myevent;

    AccessibilityNodeInfo nodeInfo;

    private MySystemUI mySystemUI;

    private boolean SystemUIFirstBoot = true;//保证SystemUI在onCreate中只在开机启动时执行一次

//    private boolean NotificationFirstBoot = true;//保证SystemUI在onCreate中只在开机启动时执行一次

//    private MyNotificationService myNotificationService = new MyNotificationService();

//    private Intent start_notification_service;

    @Override
    public void onCreate() {
        mycontext = this;

//        Log.d(TAG,String.valueOf(mycontext));

        //开机完成标志位
        fswitch = Settings.Secure.getInt(mycontext.getContentResolver(),
                "tv_user_setup_complete", 5);

        //开机启动ColorSystemUI
        if (SystemUIFirstBoot && fswitch == 1) {
            mySystemUI = new MySystemUI(mycontext);
            mySystemUI.start();
            SystemUIFirstBoot = false;
        }

        //监听开机引导完成标志位
        bootObserver = new BootFinishContentObserver(mycontext);
        mycontext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("tv_user_setup_complete"), true, bootObserver);


//        if (NotificationFirstBoot && fswitch == 1) {
//            //启动消息通知服务
//            //Log.d("BootFinishContentObserver", "启动消息通知服务");
//            setInstance(myNotificationService);
//            start_notification_service = new Intent(mycontext, MyNotificationService.class);
//            mycontext.startService(start_notification_service);
//            NotificationFirstBoot = false;
//        }

        init();
    }


    public void init() {//开机向导结束后，再init，防止开机向导期间可以唤出Osd菜单，相关类BootFinishContentObserver
        //Log.d(TAG, "服务启动: !!!!");
        super.onCreate();
        menuState = NULL;
        if (listenerList != null && !listenerList.isEmpty()) {
            listenerList.clear();
        }

        mainHandler = new Handler(mainLooper);
        brightnessLongClickRunnable = new LongClickSimulate();
        volumeLongClickRunnable = new LongClickSimulate();

        mOldConfig = new Configuration(getResources().getConfiguration());
        setDensityForAdaptation();


        //Menu 初始化
        dialogMenu = new DialogMenu(mycontext);
        dialogMenu.start();

        addVolumeChangedReceiver();

        //记录上一次的资源属性：国家、分辨率
        lastCountry = getResources().getConfiguration().locale.toLanguageTag();
        lastWidthDp = getResources().getConfiguration().screenWidthDp;
        lastHeightDp = getResources().getConfiguration().screenHeightDp;
        //Settings.System.putInt(mycontext.getContentResolver(), SYSTEM_RESOLUTION_CHANGE, 960516);

    }

    private void addBrightnessChangedObeserver() {
        if (brightnessObeserver == null) {
            brightnessObeserver = new BrightnessObeserver(mycontext);
            brightnessObeserver.setBrightnessChangeListener(this::onBrightnessChange);
        }

        brightnessObeserver.register();
    }

    // 增加音量变化的广播接收。静态注册有点问题，暂时使用动态注册方式
    private void addVolumeChangedReceiver() {
        // 旧的音量广播接收器，这种方式只能监听音量发生变化的广播
//        if (volumeChangeReceiver == null) {
//            volumeChangeReceiver = new VolumeChangeReceiver();
//            volumeChangeReceiver.setVolumeChangeListener(this);  // 注册回调
//        }
//
//        IntentFilter volumeChangeFilter = new IntentFilter();
//        volumeChangeFilter.addAction(volumeChangeReceiver.VOLUME_CHANGE_ACTION);
//        registerReceiver(volumeChangeReceiver, volumeChangeFilter);

        // 新的音量接收广播，在phoneWindowManager中单独添加的广播，只要音量+-按键按下，这里就会接收到广播
        if (fromFWReceiver == null) {
            fromFWReceiver = new VolumeFromFWReceiver();
            fromFWReceiver.setVolumeChangeListener(this);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantProperties.VOLUME_CHANGE_ACTION);
        registerReceiver(fromFWReceiver, intentFilter);
    }


    public void addKeyEventListener(DispatchKeyEventInterface listener) {
        listenerList.add(listener);
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        myevent = event;

        //Log.d("onAccessibilityEvent allTypes", "检测到" + String.valueOf(event.getEventType()));
        //判断事件是否被消费
        int eventType = event.getEventType();

        if (eventType == AccessibilityEvent.WINDOWS_CHANGE_PIP && event.getText().contains("ClickBlandUp")) {
            //Log.d("onAccessibilityEvent WINDOWS_CHANGE_PIP", "全局点击");
            clickList.add(eventType);

            execut();
        }

        if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) {//WINDOWS_CHANGE_PIP
            // 处理点击事件
            clickList.add(eventType);

            //Log.d("onAccessibilityEvent TYPE_VIEW_CLICKED", "检测到点击功能按键");
        }

        if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            // 处理点击事件
            clickList.add(eventType);
            //Log.d("onAccessibilityEvent TYPE_WINDOW_STATE_CHANGED", "检测到窗口发生变化");

        }

        if (eventType == AccessibilityEvent.TYPE_WINDOWS_CHANGED) {
            clickList.add(eventType);
        }

        if (eventType == AccessibilityEvent.CONTENT_CHANGE_TYPE_PANE_DISAPPEARED) {
            clickList.add(eventType);
        }

    }

    public void execut() {
        executor.submit(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            size = clickList.size();
            //Log.d("onAccessibilityEvent execut", " " + String.valueOf(clickList.get(size - 1)));

            if (clickList.get(size - 1) == AccessibilityEvent.WINDOWS_CHANGE_PIP) {
                //Log.d("mtimeManager", " 全局空白处点击" + String.valueOf(StaticVariableUtils.TimeManagerRunning));

                //屏蔽掉全局空白无功能处点击控制悬浮球、导航栏显示与否的方案，使用侧滑唤起方案
                //Settings.System.putInt(mycontext.getContentResolver(), StaticVariableUtils.WINDOWMANAGER_TO_OSD, 1);
            }

        });
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;//返回START_STICKY，确保Service被意外杀死后能够自动重启
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    //语言变化回调
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //1、语言变化
        //Log.d("onConfigurationChanged", "语言_国家" + newConfig.locale.toLanguageTag());
        if (!newConfig.locale.toLanguageTag().equals(lastCountry)) {
            //Log.d("onConfigurationChanged", "语言变化" + newConfig.locale.toLanguageTag());

            if (DialogMenu.mydialog.isShowing()) {
                DialogMenu.mydialog.dismiss();
                onCreate();
                DialogMenu.mydialog.show();
            } else {
                onCreate();
            }

        }

        //2、分辨率变化
        screenWidthDp = newConfig.screenWidthDp;
        screenHeightDp = newConfig.screenHeightDp;
        if (screenWidthDp != lastWidthDp ||
                screenHeightDp != lastHeightDp) {
//            Log.d("onConfigurationChanged", "分辨率发生变化 宽" + screenWidthDp + "  高" + screenHeightDp);
            // 执行相应的操作
            lastWidthDp = screenWidthDp;
            lastHeightDp = screenHeightDp;

//            StaticVariableUtils.widthPixels = screenWidthDp * 2;
//            StaticVariableUtils.heightPixels = screenWidthDp * 2 * 1080 / 1920;
//            Log.d("onConfigurationChanged", "分辨率发生变化 宽" + StaticVariableUtils.widthPixels + "  高" + StaticVariableUtils.heightPixels);
//            Adaptable_resolution();
//
//            if(StaticVariableUtils.widthPixels > StaticVariableUtils.heightPixels) {
//
//                StaticVariableUtils.widthPixels = StaticVariableUtils.heightPixels * 1920/1080;
//
//            }
//            if(StaticVariableUtils.widthPixels < StaticVariableUtils.heightPixels) {
//
//                StaticVariableUtils.heightPixels = StaticVariableUtils.widthPixels * 1080/1920;
//
//            }

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

            Adaptable_resolution();


            Log.d("onConfigurationChanged", "judge(screenHeightDp)" + judge(screenHeightDp));

            //写Setting值通知ColorSystemUI
            b = screenWidthDp * (int) Math.pow(10, judge(screenHeightDp)) + screenHeightDp;
            //Log.d("onConfigurationChanged", "分辨率Dp" + b);
            Settings.System.putInt(mycontext.getContentResolver(), SYSTEM_RESOLUTION_CHANGE, b);

            // osd 界面适配与计算
            setDensityForAdaptation(); // 计算新的Density(Clt)值
            // Log.d("onConfigurationChanged","屏幕大小变化 Adaptation, mDensity=" + ConstantProperties.DENSITY);
            dialogMenu.clearAllChildView(); // 清空音量/亮度条的二级菜单的显示

            if (DialogMenu.mydialog.isShowing()) {
                // 在配置变化时，一级菜单仍然显示的话，先关闭之前的菜单，再通过onCreate()重新计算和绘制各窗口，并再显示。
                DialogMenu.mydialog.dismiss();
                onCreate(); // dialogMenu.refreshMenuView();
                DialogMenu.mydialog.show();
            } else {
                onCreate();
            }

        }

    }

    //按键的处理和拦截
    @Override
    protected boolean onKeyEvent(KeyEvent event) {//Menu 键 keyCode = 82

        //Log.d(TAG, String.valueOf(event.getKeyCode()) + ", " + event.getAction());
        // 单独处理按键小板上的亮度加减按键
        if (event.getKeyCode() == KeyEvent.KEYCODE_BRIGHTNESS_UP ||
                event.getKeyCode() == KeyEvent.KEYCODE_BRIGHTNESS_DOWN) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                // 1 能走这个判断条件，一定是按下了按键小板亮度按钮
                // 2 开个子线程把当前按键事件传递到相关的负责人（观察者）处
                brightnessLongClickRunnable.isDown = true;
                brightnessLongClickRunnable.currKeyEvent = event;
                executors.submit(brightnessLongClickRunnable);
                Log.d(TAG, "brightnessDebug: brightness action down");
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                Log.d(TAG, "brightnessDebug: brightness action up");
                brightnessLongClickRunnable.isDown = false;   // 按键小板亮度按钮松手 取消子线程处理按键逻辑
                //Log.d(TAG, "onKeyEvent555: up");
            }
        }

        number++;

        fswitch = Settings.Secure.getInt(mycontext.getContentResolver(),
                "tv_user_setup_complete", 5);
        if (fswitch == 1) {
            initcomplete = true;
        }
        if (!initcomplete) {
            return false;
        }

        //奇数keyevent不做处理
        if (!isEven(number)) {
            //保证Menu键只有ColorOsd响应
            if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                return true;
            }

            return false;
        } else {//偶数处理

            return disPatchKeyEvent(event);
        }

    }

    @Override
    public boolean onGesture(@NonNull AccessibilityGestureEvent gestureEvent) {
        //Log.d("onGesture", "监听全局手势");
        return super.onGesture(gestureEvent);
    }


    private boolean disPatchKeyEvent(KeyEvent event) {
        //Log.d(TAG, "进入 disPatchKeyEvent");

        //1、菜单键唤起、隐藏
        isMenuOnByKeyEvent(event);

        //保证Menu键只有ColorOsd响应
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            return true;
        }

//        if (menuState == NULL && (event.getKeyCode() == KeyEvent.KEYCODE_BRIGHTNESS_UP ||
//                event.getKeyCode() == KeyEvent.KEYCODE_BRIGHTNESS_DOWN )) {
//            // 未唤起菜单时，点击亮度加减键，唤起亮度条
//            Log.d(TAG + ", brightnessKey", "disPatchKeyEvent: push brightness key : " + event.getKeyCode());
//
//            if (menuOn) {
//                //二级菜单没有打开，Home键处理
//                firstHomeKeyEvent(event);
//
//                //二级菜单没有打开，Back键处理
//                firstBackKeyEvent(event);
//            }
//
//            menuState = MenuState.MENU_BRIGHTNESS_DIRECT;
//            dialogMenu.mybind.Menu_brightness.performClick();
//
//            return false;
//        }

        if ((menuState == NULL || menuState == MenuState.MENU_BRIGHTNESS_DIRECT) && menuOn == true) {

            //2、二级菜单没有打开，Home键处理
            firstHomeKeyEvent(event);

            //3、二级菜单没有打开，Back键处理
            firstBackKeyEvent(event);

            return false;
        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_HOME) {
            dialogMenu.clearAllChildView();
            menuState = NULL;
            DialogMenu.mydialog.dismiss();//收起菜单
            menuOn = false;

            //快捷中心打开的话，将其关闭
            try {
                if (STATIC_INSTANCE_UTILS.myNotification.notification.getVisibility() == View.VISIBLE) {
                    STATIC_INSTANCE_UTILS.myNotification.notification.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //4、二级菜单 KeyEvent判断处理
        //ceshi = whichOne(event);
//        Log.d("Menu_source","返回值"+ String.valueOf(ceshi));
        return whichOne(event);


    }


    private void isMenuOnByKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && menuOn == false) {

            //Log.d(TAG, "启动Menu");

            DialogMenu.mydialog.show();//展示Osd 菜单
            menuOn = true;

        } else if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && menuOn == true) {

            //Log.d(TAG, "关闭Menu");

            if (Menu_source.sourceon == true) {
                //FunctionBind.mavts.clearView(Source_View.source);
                Settings.System.putInt(mycontext.getContentResolver(), OSD_OPEN_OTHER_SOURCE, 0);
                Menu_source.sourceon = false;
                if (MenuService.menuState == MenuState.MENU_SOURCE) {
                    MenuService.menuState = MenuState.NULL;
                }

            }

            DialogMenu.mydialog.dismiss();//收起菜单
            menuOn = false;

        }
    }


    private boolean whichOne(KeyEvent event) {
        for (DispatchKeyEventInterface KeyEventDispatcher : listenerList) {
            if (KeyEventDispatcher.onKeyEvent(event, menuState)) {
                Log.d("22BUG", "MenuService whichOne 返回true");
                return true;
            }
        }
        return false;
    }


    //判断奇偶性
    public boolean isEven(int mynumber) {
        if (mynumber % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void firstHomeKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_HOME) {
            DialogMenu.mydialog.dismiss();//收起菜单
            menuOn = false;
        }
    }

    private void firstBackKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            DialogMenu.mydialog.dismiss();//收起菜单
            menuOn = false;
        }
    }

    @Override
    public void onVolumeChange(int volume) {
        // 这些老代码都屏蔽了
//        if (menuState == MenuState.MENU_VOLUME ||
//                menuState == MenuState.MENU_VOLUME_DIRECT) {
//            // 音量调节窗口已经显示  直接改变音量的值
//            dialogMenu.mybind.menu_volume.onVolumeChanged(volume);
//        } else if (MenuService.menuState == MenuState.MENU_BRIGHTNESS ||
//                MenuService.menuState == MenuState.MENU_BRIGHTNESS_DIRECT) {
//             // TODO: 亮度和音量一起显示。目前已经是亮度调整了，这个时候再按音量调整，那么就要进入复合态
//            //dialogMenu.mybind.Menu_volume.performClick();
//            Log.d(TAG, "onClick: 当前处于亮度阶段，但我监听到了音量变化~");
//            dialogMenu.mybind.Menu_volume.performClick();
//        } else if (MenuService.menuState == MenuState.NULL) {
//            // 说明是直接按下音量调节按钮（可能是遥控器也可能是按键小板），那么就打开音量调节按钮
//            dialogMenu.mybind.Menu_volume.performClick();
//        } else if (menuState == MenuState.MENU_BRIGHTNESS_VOLUME ||
//                menuState == MenuState.MENU_BRIGHTNESS_FOCUS ||
//                menuState == MenuState.MENU_VOLUME_FOCUS) {
//            // 进入复合态了，那么直接调节音量吧，但是由Brightness
//            dialogMenu.mybind.menu_volume.onVolumeChangedInBrightnessAndVolume(volume);
//        }

    }

    @Override
    public void onBrightnessChange(int brightness) {
        if (menuState == MenuState.MENU_BRIGHTNESS) {
            // 亮度窗口已经显示
            //dialogMenu.mybind.menu_volume.o
        } else if (menuState == MenuState.MENU_VOLUME) {
            // 音量窗口已经显示 TODO: 亮度和音量一起显示
        } else if (menuState == MenuState.NULL) {

        }
    }

    //判断一个数是几位数
    private static int judge(int num) {
        // TODO Auto-generated method stub
        int count = 0;
        while (num >= 10) {
            num /= 10;
            count++;
        }
        return count + 1;
    }

    // 计算新的Density(Clt)
    public void setDensityForAdaptation() {

        // 获取屏幕宽高
        WindowManager windowManager = (WindowManager) mycontext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        ConstantProperties.DENSITY = Math.min(width / ConstantProperties.DESIGN_SCREEN_WIDTH_DP,
                height / ConstantProperties.DESIGN_SCREEN_HEIGHT_DP);
        //Log.d(TAG, "setDensityForAdaptation - change: width=" + width + ", height=" + height
        //+ ", mDensity=" + ConstantProperties.DENSITY);
    }

    class LongClickSimulate implements Runnable {
        public KeyEvent currKeyEvent;
        public boolean isDown;

        public boolean settingChange;

        private AudioManager audioManager;

        private int loop = 0;


        @Override
        public void run() {
            // 为啥要加个while循环，因为如果一直按着按键小板亮度调整按键（触发长按事件），所以每隔300秒要处理一次按键
            while (isDown) {
                mainHandler.post(() -> {
                    Log.d(TAG, "run: " + settingChange);
                    if (settingChange) {
                        if (loop == 0) return;   // 第一次不执行，避免与settings应用的按键冲突，造成音量一瞬间减了两格
                        Log.d(TAG, "run: only settings");
                        // 注意！！！！此处这个判断条件是后来加的：settings应用的音量调整面板中，没有长按事件，因此把长按的功能集成到这里
                        // 那么settings应用调整音量，就不显示OSD的音量条了。
                        // 直接把AudioManager的值调整后，settings应用音量相关的UI就会跟着变了
                        if (audioManager == null) {
                            audioManager = (AudioManager) mycontext.getSystemService(Context.AUDIO_SERVICE);
                        }
                        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        if (currKeyEvent.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, --volume, AudioManager.FLAG_PLAY_SOUND);
                        } else if (currKeyEvent.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, ++volume, AudioManager.FLAG_PLAY_SOUND);
                        }
                    } else {
                        Log.d(TAG, "run: not settings");
                        // 把事件分发下去，让OSD中的声音UI加载出来，并调整相关的音量
                        for (DispatchKeyEventInterface KeyEventDispatcher : listenerList) {
                            KeyEventDispatcher.onKeyEvent(currKeyEvent, menuState);
                        }
                    }

                });
                try {
                    // 睡300ms  不然while循环频率太快了，造成cpu卡顿
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                loop++;
            }
        }
    }


    @Override
    public void onVolumeChange(int keyAction, int keyCode, boolean settingChange) {
        if (keyAction == KeyEvent.ACTION_DOWN) {
            // 按下
            KeyEvent keyEvent = new KeyEvent(keyAction, keyCode);    // 自己构造一个keyEvent事件
            volumeLongClickRunnable.isDown = true;
            volumeLongClickRunnable.currKeyEvent = keyEvent;
            volumeLongClickRunnable.settingChange = settingChange;
            volumeLongClickRunnable.loop = 0;
            executors.submit(volumeLongClickRunnable);      // 开个子线程去循环处理按下事件（模拟长按）
        } else if (keyAction == KeyEvent.ACTION_UP) {
            // 抬起
            volumeLongClickRunnable.isDown = false;   // 按键小板音量按钮松手 取消子线程处理按键逻辑
//            Log.d(TAG, "onKeyEvent555: up");
        }

    }

    public void Adaptable_resolution() {

        ViewGroup.MarginLayoutParams layoutParams;

        //整个快捷中心的位置
        STATIC_INSTANCE_UTILS.myNotification.lp.x = 20 * StaticVariableUtils.widthPixels / 1920;
        STATIC_INSTANCE_UTILS.mavts.wm.updateViewLayout(STATIC_INSTANCE_UTILS.myNotification.notification, STATIC_INSTANCE_UTILS.myNotification.lp);

//        //1、notification.xml
//        //CustomFrameLayout 的各种外边距
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.myNotification.quick_settings_frame.getLayoutParams();
//        layoutParams.setMargins(0, 0, 0, 20 * StaticVariableUtils.heightPixels / 1080);
//        STATIC_INSTANCE_UTILS.myNotification.quick_settings_frame.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        //CustomLinearLayout 的各种外边距
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.myNotification.notification_center_linear.getLayoutParams();
//        layoutParams.setMargins(0,0,0,20 * StaticVariableUtils.heightPixels / 1080);
//        STATIC_INSTANCE_UTILS.myNotification.notification_center_linear.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.myNotification.notification_center_title.getLayoutParams();
//        layoutParams.setMargins(24 * StaticVariableUtils.widthPixels/1920,28 * StaticVariableUtils.heightPixels/1080,24 * StaticVariableUtils.widthPixels/1920,0);
////        layoutParams.width = 428 * StaticVariableUtils.widthPixels / 1920;
//        STATIC_INSTANCE_UTILS.myNotification.notification_center_title.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        //CustomRecyclerView 各种边距
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.myNotification.notification_quick_settings.getLayoutParams();
//        layoutParams.setMargins(0,20*StaticVariableUtils.heightPixels/1080,0,20*StaticVariableUtils.heightPixels/1080);
//        STATIC_INSTANCE_UTILS.myNotification.notification_quick_settings.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.myNotification.notification_center.getLayoutParams();
//        layoutParams.setMargins(0,10*StaticVariableUtils.heightPixels/1080,0,20*StaticVariableUtils.heightPixels/1080);
//        STATIC_INSTANCE_UTILS.myNotification.notification_center.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        //ImageView 各种烦人边距
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.myNotification.notification_quit.getLayoutParams();
//        layoutParams.setMargins(324* StaticVariableUtils.widthPixels / 1920, 6* StaticVariableUtils.heightPixels/1080, 0, 0);
//        STATIC_INSTANCE_UTILS.myNotification.notification_quit.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.myNotification.down.getLayoutParams();
//        layoutParams.setMargins(0, 0, 0, 20* StaticVariableUtils.heightPixels/1080);
//        STATIC_INSTANCE_UTILS.myNotification.down.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//
//
//        //2、notification_quick_settings.xml
//        //CustomFrameLayout 的各种外边距
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.brightness_frame.getLayoutParams();
//        layoutParams.setMargins(24 * StaticVariableUtils.widthPixels / 1920, 0, 0, 0);
//        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.brightness_frame.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.volume_frame.getLayoutParams();
//        layoutParams.setMargins(24 * StaticVariableUtils.widthPixels/1920,0,0,16 * StaticVariableUtils.heightPixels / 1080);
//        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.volume_frame.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.quick_settings.getLayoutParams();
//        layoutParams.setMargins(24 * StaticVariableUtils.widthPixels/1920,6 * StaticVariableUtils.heightPixels/1080,24 * StaticVariableUtils.widthPixels/1920,6 * StaticVariableUtils.heightPixels/1080);
//        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.quick_settings.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.screenshot_linear.getLayoutParams();
//        layoutParams.setMargins(24 * StaticVariableUtils.widthPixels/1920,0,0,0);
//        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.screenshot_linear.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.eye_protection_linear.getLayoutParams();
//        layoutParams.setMargins(46 * StaticVariableUtils.widthPixels/1920,0,0,0);
//        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.eye_protection_linear.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.camera_linear.getLayoutParams();
//        layoutParams.setMargins(46 * StaticVariableUtils.widthPixels/1920,0,0,0);
//        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.camera_linear.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.screenrecord_linear.getLayoutParams();
//        layoutParams.setMargins(48 * StaticVariableUtils.widthPixels/1920,0,0,0);
//        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.screenrecord_linear.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        //CustomTextView
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.tools.getLayoutParams();
//        layoutParams.setMargins(24*StaticVariableUtils.widthPixels/1920,24* StaticVariableUtils.heightPixels/1080,0,8*StaticVariableUtils.heightPixels/1080);
//        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.tools.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.brightnessTitle_text.getLayoutParams();
//        layoutParams.setMargins(24*StaticVariableUtils.widthPixels/1920,16* StaticVariableUtils.heightPixels/1080,0,8*StaticVariableUtils.heightPixels/1080);
//        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.brightnessTitle_text.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.brightnessSeekBar_text.getLayoutParams();
//        layoutParams.setMargins(0,0,8*StaticVariableUtils.widthPixels/1920,0);
//        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.brightnessSeekBar_text.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.volumeTitle_text.getLayoutParams();
//        layoutParams.setMargins(24*StaticVariableUtils.widthPixels/1920,16* StaticVariableUtils.heightPixels/1080,0,8*StaticVariableUtils.heightPixels/1080);
//        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.volumeTitle_text.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.volumeSeekBar_text.getLayoutParams();
//        layoutParams.setMargins(0,0,8*StaticVariableUtils.widthPixels/1920,0);
//        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.volumeSeekBar_text.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.brightness_icon.getLayoutParams();
//        layoutParams.setMargins(8*StaticVariableUtils.widthPixels/1920, 0, 0, 0);
//        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.brightness_icon.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        layoutParams = (ViewGroup.MarginLayoutParams) STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.volume_icon.getLayoutParams();
//        layoutParams.setMargins(8*StaticVariableUtils.widthPixels/1920, 0, 0, 0);
//        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.volume_icon.setLayoutParams(layoutParams);
//        layoutParams = null;
//
//        //3、notification_center.xml
//        for(int i = 0; i<StaticVariableUtils.list.size();i++) {
//            layoutParams = (ViewGroup.MarginLayoutParams) StaticVariableUtils.list.get(i).notification_center_item.getLayoutParams();
//            layoutParams.setMargins(24 * StaticVariableUtils.widthPixels / 1920, 6 * StaticVariableUtils.heightPixels / 1080, 24 * StaticVariableUtils.widthPixels / 1920, 6 * StaticVariableUtils.heightPixels / 1080);
//            StaticVariableUtils.list.get(i).notification_center_item.setLayoutParams(layoutParams);
//            layoutParams = null;
//
//            layoutParams = (ViewGroup.MarginLayoutParams) StaticVariableUtils.list.get(i).notification_center_item_content.getLayoutParams();
//            layoutParams.setMargins(0, 20 * StaticVariableUtils.heightPixels / 1080, 20 * StaticVariableUtils.widthPixels / 1920, 20 * StaticVariableUtils.heightPixels / 1080);
//            StaticVariableUtils.list.get(i).notification_center_item_content.setLayoutParams(layoutParams);
//            layoutParams = null;
//
//            layoutParams = (ViewGroup.MarginLayoutParams) StaticVariableUtils.list.get(i).icon.getLayoutParams();
//            layoutParams.setMargins(16 * StaticVariableUtils.widthPixels / 1920, 20 * StaticVariableUtils.heightPixels / 1080, 12 * StaticVariableUtils.widthPixels / 1920, 20 * StaticVariableUtils.heightPixels / 1080);
//            StaticVariableUtils.list.get(i).icon.setLayoutParams(layoutParams);
//            layoutParams = null;
//        }

    }


}


