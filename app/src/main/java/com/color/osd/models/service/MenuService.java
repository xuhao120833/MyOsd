package com.color.osd.models.service;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.color.osd.models.FunctionBind;
import com.color.osd.models.Menu_source;
import com.color.osd.ui.DialogMenu;
import com.color.osd.ui.Source_View;

import java.util.ArrayList;


public class MenuService extends AccessibilityService {

    Context mycontext;

    String TAG = "MenuService";
    //ArrayList<Integer> myarrylist = new ArrayList<>();
    static int number = 0;
    //static int Menumber = 0;
    public static boolean menuOn = false;

    public static boolean lastMenuOn;

    DialogMenu dialogMenu;



    @Override
    public void onCreate() {

        Log.d(TAG, "服务启动: !!!!");
        super.onCreate();
        mycontext = this;

        //Menu 初始化
        dialogMenu = new DialogMenu(this);
        dialogMenu.start();

    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }

    //按键的处理和拦截
    @Override
    protected boolean onKeyEvent(KeyEvent event) {//Menu 键 keyCode = 82

        number++;

        //奇数keyevent不做处理
        if (!isEven(number) || isLeftOrRight(event)) {
//            Log.d(TAG, "奇数 number 的值" + String.valueOf(number));
//            Log.d(TAG, String.valueOf(event.getKeyCode()));
//            Log.d(TAG, " ");
//            Log.d(TAG, " ");
//            Log.d(TAG, " ");
            return false;
        } else {//偶数处理
//            Log.d(TAG, "偶数 number 的值" + String.valueOf(number));
//            Log.d(TAG, String.valueOf(event.getKeyCode()));
//            Log.d(TAG, "menuOn 的值" + String.valueOf(menuOn));
            disPatchKeyEvent(event);
            return false;
        }

    }

    private void disPatchKeyEvent(KeyEvent event) {
        Log.d(TAG, "进入 disPatchKeyEvent");

        //1、菜单键唤起、隐藏
        isMenuOnByKeyEvent(event);

        //2、Home键
        isHomeKeyEvent(event);

        //3、Back键，后续和Back键有关的逻辑在这里加
        isBackKeyEvent(event);


    }

    private void isMenuOnByKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && menuOn == false) {

            Log.d(TAG, "启动Menu");

            DialogMenu.mydialog.show();//展示Osd 菜单
            menuOn = true;

        } else if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && menuOn == true) {

            Log.d(TAG, "关闭Menu");

            DialogMenu.mydialog.dismiss();//收起菜单
            menuOn = false;

        }
    }

    private void isHomeKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_HOME && menuOn == true) {
            if(Menu_source.sourceon == true) {
                FunctionBind.mavts.clearView(Source_View.source);
                Menu_source.sourceon = false;
            }

            Log.d(TAG, "关闭Menu");

            DialogMenu.mydialog.dismiss();//收起菜单
            menuOn = false;

        }

    }

    private void isBackKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && menuOn == true && Menu_source.sourceon == true) {
            FunctionBind.mavts.clearView(Source_View.source);
            Menu_source.sourceon = false;
            return;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && menuOn == true && Menu_source.sourceon == false) {
            DialogMenu.mydialog.dismiss();//收起菜单
            menuOn = false;
            return;
        }
    }

    private boolean isLeftOrRight(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            return true;
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

}


