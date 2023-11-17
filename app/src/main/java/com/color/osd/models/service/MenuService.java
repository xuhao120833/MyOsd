package com.color.osd.models.service;

import static com.color.osd.models.Enum.MenuState.NULL;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.color.osd.models.Enum.MenuState;
import com.color.osd.models.Menu_source;
import com.color.osd.models.interfaces.DispatchKeyEventInterface;
import com.color.osd.models.interfaces.VolumeChangeListener;
import com.color.osd.ui.DialogMenu;

import java.util.ArrayList;
import java.util.List;

import com.color.osd.broadcast.VolumeChangeReceiver;
import com.color.osd.models.interfaces.VolumeChangeListener;

import android.content.ContextWrapper;



public class MenuService extends AccessibilityService implements VolumeChangeListener {

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

    boolean ceshi ;

    public static Handler mainHandler;

    Looper mainLooper = Looper.getMainLooper();


    @Override
    public void onCreate() {

        Log.d(TAG, "服务启动: !!!!");
        super.onCreate();
        mycontext = this;
        menuState = NULL;

        mainHandler = new Handler(mainLooper);

        //Menu 初始化
        dialogMenu = new DialogMenu(this);
        dialogMenu.start();

        addVolumeChangedReceiver();
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
        Log.d("onConfigurationChanged","收到语言变化");

        onCreate();
        //onDestroy();
    }

    //按键的处理和拦截
    @Override
    protected boolean onKeyEvent(KeyEvent event) {//Menu 键 keyCode = 82

        number++;

        //奇数keyevent不做处理
        if (!isEven(number)) {
            //保证Menu键只有ColorOsd响应
            if(event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                return true;
            }

            return false;
        } else {//偶数处理

            return disPatchKeyEvent(event);
        }

    }

    private boolean disPatchKeyEvent(KeyEvent event) {
        Log.d(TAG, "进入 disPatchKeyEvent");

        //1、菜单键唤起、隐藏
        isMenuOnByKeyEvent(event);

        //保证Menu键只有ColorOsd响应
        if(event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            return true;
        }

        if((menuState == NULL || menuState == MenuState.MENU_VOLUME_DIRECT) && menuOn == true){
            //2、二级菜单没有打开，Home键处理
            firstHomeKeyEvent(event);

            //3、二级菜单没有打开，Back键处理
            firstBackKeyEvent(event);

            return false;
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

            if(Menu_source.sourceon == true) {
                //FunctionBind.mavts.clearView(Source_View.source);
                Settings.System.putInt(mycontext.getContentResolver(),OSD_OPEN_OTHER_SOURCE,0);
                Menu_source.sourceon = false;
                if(MenuService.menuState == MenuState.MENU_SOURCE) {
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
        if(event.getKeyCode() == KeyEvent.KEYCODE_HOME) {
            DialogMenu.mydialog.dismiss();//收起菜单
            menuOn = false;
        }
    }

    private void firstBackKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
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
        } else if (MenuService.menuState == MenuState.MENU_BRIGHTNESS) {
            // TODO: 亮度和音量一起显示。暂时只显示亮度 此功能尚未完成！！！
//            dialogMenu.mybind.Menu_volume.performClick();
        } else if (MenuService.menuState == MenuState.NULL){
            // OSD一级菜单栏都没有打开，直接进入MENU_VOLUME_DIRECT态，并打开声音TouchBar
            MenuService.menuState = MenuState.MENU_VOLUME_DIRECT;
            dialogMenu.mybind.Menu_volume.performClick();
        }else if(menuState == MenuState.MENU_BRIGHTNESS_VOLUME ||
                menuState == MenuState.MENU_BRIGHTNESS_FOCUS ||
                menuState == MenuState.MENU_VOLUME_FOCUS){
            // 进入复合态了，那么直接调节音量吧，但是由Brightness
            dialogMenu.mybind.menu_volume.onVolumeChangedInBrightnessAndVolume(volume);
        }
    }



}


