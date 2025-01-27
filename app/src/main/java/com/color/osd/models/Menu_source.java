package com.color.osd.models;

import static com.color.osd.models.service.MenuService.menuOn;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.color.osd.models.Enum.MenuState;
import com.color.osd.models.interfaces.DispatchKeyEventInterface;
import com.color.osd.models.service.MenuService;
import com.color.osd.ui.DialogMenu;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;

public class Menu_source implements DispatchKeyEventInterface, Instance {

    Context mycontext;

    //Source_View source_view;

    static String TAG = "Menu_source";

    private static final String OSD_OPEN_OTHER_SOURCE = "osd_open_other_source";

    public static boolean sourceon = false;

//    public static boolean fromOsd;

    Menu_source(Context context) {
        mycontext = context;

        Settings.System.putInt(mycontext.getContentResolver(), OSD_OPEN_OTHER_SOURCE, 5);

        ((MenuService) mycontext).addKeyEventListener(this);   // 注册观察者
    }


    public void setOnclick(View menu_source) {
        menu_source.setOnClickListener(v -> {
            setFocusableByMenu_source();
            STATIC_INSTANCE_UTILS.source.Source.setVisibility(View.VISIBLE);
            //打开信源界面，默认焦点落在用户已选择的信源图标上
            setFocus();

            //Settings.System.putInt(mycontext.getContentResolver(), OSD_OPEN_OTHER_SOURCE, 1);
            sourceon = true;
            MenuService.menuState = MenuState.MENU_SOURCE;
            DialogMenu.mydialog.dismiss();//收起菜单
            menuOn = false;
//            fromOsd = true;

        });
    }

    private void setFocusableByMenu_source() {
        STATIC_INSTANCE_UTILS.source.OPS.setFocusable(true);
        STATIC_INSTANCE_UTILS.source.Android.setFocusable(true);
        STATIC_INSTANCE_UTILS.source.HDMI1.setFocusable(true);
        STATIC_INSTANCE_UTILS.source.HDMI2.setFocusable(true);
        STATIC_INSTANCE_UTILS.source.X.setFocusable(true);
    }

    public void setFocus() {
        switch (STATIC_INSTANCE_UTILS.source.select_source) {
            case "OPS":
                STATIC_INSTANCE_UTILS.source.OPS.requestFocus();
                break;
            case "Android":
                STATIC_INSTANCE_UTILS.source.Android.requestFocus();
                break;
            case "HDMI1":
                STATIC_INSTANCE_UTILS.source.HDMI1.requestFocus();
                break;
            case "HDMI2":
                STATIC_INSTANCE_UTILS.source.HDMI2.requestFocus();
                break;
        }
    }

    @Override
    public boolean onKeyEvent(KeyEvent event, MenuState menuState) {

        //1、判断Home键
        isHomeKeyEvent(event);

        //2、判断Back键
        isBackKeyEvent(event);

        if (!menuState.equals(MenuState.MENU_SOURCE))
            return false;

        if (isHomeKeyEvent(event) || isBackKeyEvent(event)) {
            Log.d("22BUG","Menu_source onKeyEvent 返回true");
            return true;
        }


        return false;
    }

    @Override
    public boolean isHomeKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_HOME && sourceon == true) {

            //Settings.System.putInt(mycontext.getContentResolver(), OSD_OPEN_OTHER_SOURCE, 0);
            STATIC_INSTANCE_UTILS.source.Source.setVisibility(View.GONE);
            Menu_source.sourceon = false;
            MenuService.menuState = MenuState.NULL;


            //Log.d(TAG, "关闭Menu");

            return true;
        }
        return false;

    }

    @Override
    public boolean isBackKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && sourceon == true) {

            //Settings.System.putInt(mycontext.getContentResolver(), OSD_OPEN_OTHER_SOURCE, 0);
            //Log.d("Menu_source", "发消息");
            STATIC_INSTANCE_UTILS.source.Source.setVisibility(View.GONE);
            sourceon = false;
            MenuService.menuState = MenuState.NULL;
            //Settings.System.putInt(mycontext.getContentResolver(), OSD_OPEN_OTHER_SOURCE, 2);

            DialogMenu.mydialog.show();//展示Osd 菜单
            MenuService.menuOn = true;
            return true;
        }

        return false;
    }

}
