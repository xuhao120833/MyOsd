package com.color.osd.models.service;

import static com.color.osd.models.Enum.MenuState.NULL;

import android.accessibilityservice.AccessibilityGestureEvent;
import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.NonNull;

import com.color.osd.ContentObserver.BootFinishContentObserver;
import com.color.osd.models.Enum.MenuState;
import com.color.osd.models.Menu_source;
import com.color.osd.models.interfaces.DispatchKeyEventInterface;
import com.color.osd.models.interfaces.VolumeChangeListener;
import com.color.osd.ui.DialogMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.color.osd.broadcast.VolumeChangeReceiver;
import com.color.osd.utils.ConstantProperties;
import com.color.osd.ContentObserver.BrightnessObeserver;
import com.color.osd.models.interfaces.BrightnessChangeListener;


public class MenuService extends AccessibilityService implements VolumeChangeListener, BrightnessChangeListener {

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

    private static final String OSD_OPEN_OTHER_SOURCE = "osd_open_other_source";

    private static final String SYSTEM_RESOLUTION_CHANGE = "system_resolution_change";

    boolean ceshi;

    public static Handler mainHandler;

    Looper mainLooper = Looper.getMainLooper();

    private Configuration mOldConfig;   // 存储当前的配置

    int lastWidthDp, lastHeightDp;

    String lastCountry;

    int screenWidthDp, screenHeightDp, b;

    BootFinishContentObserver bootObserver;

    private BrightnessObeserver brightnessObeserver;

    public static boolean initcomplete = false;

    Message m;

    int fswitch;

    LongClickSimulate runable;
    ExecutorService executors = Executors.newSingleThreadExecutor();
    private boolean isDown;
    private long downTime;
    private long sleepTime;

    @Override
    public void onCreate() {
        mycontext =this;

        bootObserver = new BootFinishContentObserver(mycontext);
        mycontext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("tv_user_setup_complete"), true, bootObserver);

        init();
    }

    public void init() {//开机向导结束后，再init，防止开机向导期间可以唤出Osd菜单，相关类BootFinishContentObserver
        Log.d(TAG, "服务启动: !!!!");
        super.onCreate();
        menuState = NULL;
        if (listenerList != null && !listenerList.isEmpty()) {
            listenerList.clear();
        }

        mainHandler = new Handler(mainLooper);
        runable = new LongClickSimulate();

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
        if (volumeChangeReceiver == null) {
            volumeChangeReceiver = new VolumeChangeReceiver();
            volumeChangeReceiver.setVolumeChangeListener(this::onVolumeChange);  // 注册回调
        }

        IntentFilter volumeChangeFilter = new IntentFilter();
        volumeChangeFilter.addAction(volumeChangeReceiver.VOLUME_CHANGE_ACTION);
        registerReceiver(volumeChangeReceiver, volumeChangeFilter);
    }


    public void addKeyEventListener(DispatchKeyEventInterface listener) {
        listenerList.add(listener);
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        Log.d("onAccessibilityEvent ", "检测到"+String.valueOf(event.getEventType()));
        //判断事件是否被消费
        int eventType = event.getEventType();
        if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            // 处理点击事件
            Log.d("onAccessibilityEvent TYPE_VIEW_CLICKED", "检测到");

        }

        if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            // 处理点击事件
            Log.d("onAccessibilityEvent TYPE_WINDOW_STATE_CHANGED", "检测到");

        }
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
        Log.d("onConfigurationChanged", "语言_国家" + newConfig.locale.toLanguageTag());
        if (!newConfig.locale.toLanguageTag().equals(lastCountry)) {
            Log.d("onConfigurationChanged", "语言变化" + newConfig.locale.toLanguageTag());
            onCreate();
        }

        //2、分辨率变化
        screenWidthDp = newConfig.screenWidthDp;
        screenHeightDp = newConfig.screenHeightDp;
        if (screenWidthDp != lastWidthDp ||
                screenHeightDp != lastHeightDp) {
            Log.d("onConfigurationChanged", "分辨率发生变化 宽" + screenWidthDp + "  高" + screenHeightDp);
            // 执行相应的操作
            lastWidthDp = screenWidthDp;
            lastHeightDp = screenHeightDp;

            Log.d("onConfigurationChanged", "judge(screenHeightDp)" + judge(screenHeightDp));

            //写Setting值通知ColorSystemUI
            b = screenWidthDp * (int) Math.pow(10, judge(screenHeightDp)) + screenHeightDp;
            Log.d("onConfigurationChanged", "分辨率Dp" + b);
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

        Log.d(TAG, String.valueOf(event.getKeyCode()) + ", " + event.getAction());
        // 单独处理按键小板上的亮度加减按键
        if (event.getKeyCode() == KeyEvent.KEYCODE_BRIGHTNESS_UP ||
                event.getKeyCode() == KeyEvent.KEYCODE_BRIGHTNESS_DOWN){
            if (event.getAction() == KeyEvent.ACTION_DOWN){
                // 1 能走这个判断条件，一定是按下了按键小板亮度按钮
                // 2 开个子线程把当前按键事件传递到相关的负责人（观察者）处
                isDown = true;
                Log.d(TAG, "onKeyEvent555: down");
                runable.currKeyEvent = event;
                downTime = System.currentTimeMillis();
                sleepTime = 1000;
                executors.submit(runable);
            }else if(event.getAction() == KeyEvent.ACTION_UP){
                isDown = false;   // 按键小板亮度按钮松手 取消子线程处理按键逻辑
                Log.d(TAG, "onKeyEvent555: up");
            }
        }

        number++;

        fswitch = Settings.Secure.getInt(mycontext.getContentResolver(),
                "tv_user_setup_complete", 5);
        if(fswitch == 1) {
            initcomplete = true;
        }
        if(!initcomplete) {
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
        Log.d("onGesture", "监听全局手势");
        return super.onGesture(gestureEvent);
    }


    private boolean disPatchKeyEvent(KeyEvent event) {
        Log.d(TAG, "进入 disPatchKeyEvent");

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

        if (event.getKeyCode() == KeyEvent.KEYCODE_HOME){
            dialogMenu.clearAllChildView();
            menuState = NULL;
            DialogMenu.mydialog.dismiss();//收起菜单
            menuOn = false;
        }

        //4、二级菜单 KeyEvent判断处理
        //ceshi = whichOne(event);
//        Log.d("Menu_source","返回值"+ String.valueOf(ceshi));
        return whichOne(event);


    }


    private void isMenuOnByKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && menuOn == false) {

            Log.d(TAG, "启动Menu");

            DialogMenu.mydialog.show();//展示Osd 菜单
            menuOn = true;

        } else if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && menuOn == true) {

            Log.d(TAG, "关闭Menu");

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
        if (menuState == MenuState.MENU_VOLUME ||
                menuState == MenuState.MENU_VOLUME_DIRECT) {
            // 音量调节窗口已经显示  直接改变音量的值
            dialogMenu.mybind.menu_volume.onVolumeChanged(volume);
        } else if (MenuService.menuState == MenuState.MENU_BRIGHTNESS ||
                MenuService.menuState == MenuState.MENU_BRIGHTNESS_DIRECT) {
             // TODO: 亮度和音量一起显示。目前已经是亮度调整了，这个时候再按音量调整，那么就要进入复合态
            //dialogMenu.mybind.Menu_volume.performClick();
            Log.d(TAG, "onClick: 当前处于亮度阶段，但我监听到了音量变化~");
            dialogMenu.mybind.Menu_volume.performClick();
        } else if (MenuService.menuState == MenuState.NULL) {
            // 说明是直接按下音量调节按钮（可能是遥控器也可能是按键小板），那么就打开音量调节按钮
            dialogMenu.mybind.Menu_volume.performClick();
        } else if (menuState == MenuState.MENU_BRIGHTNESS_VOLUME ||
                menuState == MenuState.MENU_BRIGHTNESS_FOCUS ||
                menuState == MenuState.MENU_VOLUME_FOCUS) {
            // 进入复合态了，那么直接调节音量吧，但是由Brightness
            dialogMenu.mybind.menu_volume.onVolumeChangedInBrightnessAndVolume(volume);
        }
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
        Log.d(TAG, "setDensityForAdaptation - change: width=" + width + ", height=" + height
                + ", mDensity=" + ConstantProperties.DENSITY);
    }

    class LongClickSimulate implements Runnable {
        public KeyEvent currKeyEvent;
        @Override
        public void run() {
            // 为啥要加个while循环，因为如果一直按着按键小板亮度调整按键（触发长按事件），所以每隔300秒要处理一次按键
            while (isDown){
                long currTime = System.currentTimeMillis();
                long delta = currTime - downTime;
                Log.d(TAG, "run: " + delta);

                mainHandler.post(() -> {
                    for (DispatchKeyEventInterface KeyEventDispatcher : listenerList) {
                        KeyEventDispatcher.onKeyEvent(currKeyEvent, menuState);
                    }
                });
                try {
                    // 睡300ms  不然while循环频率太快了，造成cpu卡顿
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

}


