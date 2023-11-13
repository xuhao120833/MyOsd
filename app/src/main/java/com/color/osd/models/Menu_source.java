package com.color.osd.models;

import static com.color.osd.models.service.MenuService.menuOn;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.color.osd.models.Enum.MenuState;
import com.color.osd.models.interfaces.DispatchKeyEventInterface;
import com.color.osd.models.service.MenuService;
import com.color.osd.ui.DialogMenu;
import com.color.osd.ui.Source_View;

public class Menu_source implements DispatchKeyEventInterface {

    Context mycontext;

    Source_View source_view;

    static String TAG = "Menu_source";

    public static boolean sourceon = false;
    Menu_source (Context context) {
        mycontext = context;

        source_view = new Source_View(mycontext);

        ((MenuService) mycontext).addKeyEventListener(this);   // 注册观察者
    }


    public void setOnclick(View menu_source) {
        menu_source.setOnClickListener(v -> {
            //添加信源切换界面
            FunctionBind.mavts.addView(source_view.source , source_view.lp);
            sourceon = true;
            MenuService.menuState = MenuState.MENU_SOURCE;

        });
    }

    @Override
    public boolean onKeyEvent(KeyEvent event, MenuState menuState) {

        //1、判断Home键
        //isHomeKeyEvent(event);

        //2、判断Back键
        //isBackKeyEvent(event);

        if(isHomeKeyEvent(event) || isBackKeyEvent(event)) {
            return true;
        }


        return false;
    }

    @Override
    public boolean isHomeKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_HOME && menuOn == true) {
            if(Menu_source.sourceon == true) {
                FunctionBind.mavts.clearView(Source_View.source);
                Menu_source.sourceon = false;
                MenuService.menuState = MenuState.NULL;
            }

            Log.d(TAG, "关闭Menu");

            DialogMenu.mydialog.dismiss();//收起菜单
            menuOn = false;

            return true;
        }
        return false;

    }

    @Override
    public boolean isBackKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && menuOn == true && Menu_source.sourceon == true) {
            FunctionBind.mavts.clearView(Source_View.source);
            Menu_source.sourceon = false;
            MenuService.menuState = MenuState.NULL;
            return true;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && menuOn == true && Menu_source.sourceon == false) {
            DialogMenu.mydialog.dismiss();//收起菜单
            menuOn = false;
            return true;
        }
        return false;
    }

}
