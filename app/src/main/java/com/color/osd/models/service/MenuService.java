package com.color.osd.models.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.color.osd.ui.DialogMenu;

import java.util.ArrayList;


public class MenuService extends AccessibilityService {

    Context mycontext;

    String TAG = "MenuService";
    ArrayList<Integer> myarrylist = new ArrayList<>();
    static int number = -1;
    static boolean menuOn = false;

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

    @Override
    protected boolean onKeyEvent(KeyEvent event) {//现在没有蓝牙遥控器,无法测试菜单键，先设置成Home+向上键唤出Menu，Home+向下键或者BACK键隐藏Menu。
        Log.d(TAG, "onKeyEvent");
        Log.d(TAG, String.valueOf(event.getKeyCode()));

        myarrylist.add(event.getKeyCode());
        number++;

        if (myarrylist.get(number) == 19 && myarrylist.get(number - 2) == 3 && menuOn == false) {

            Log.d(TAG, "启动Menu");

            DialogMenu.mydialog.show();//展示Osd 菜单

            menuOn = true;

            return true; //按钮事件在这里被消费，不会再传送到上层

        }
        if (myarrylist.get(number) == 20 && myarrylist.get(number - 2) == 3 && menuOn == true) {

            Log.d(TAG, "关闭Menu");

            DialogMenu.mydialog.dismiss();//收起菜单

            menuOn = false;

            return true; //按钮事件在这里被消费，不会再传送到上层

        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && menuOn == true) {
            DialogMenu.mydialog.dismiss();

            menuOn = false;

            return true;
        }

//        if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT && menuOn ==true) {
//
//            return  true;
//
//        }

        return false;
    }

}


