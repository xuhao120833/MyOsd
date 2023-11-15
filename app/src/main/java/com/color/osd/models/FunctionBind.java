package com.color.osd.models;

import android.content.Context;
import android.provider.Settings;
import android.view.View;

import com.color.osd.ContentObserver.ColorSystemUiContentOberver;
import com.color.osd.R;
import com.color.osd.models.Enum.MenuState;

public class FunctionBind {

    Context mycontext;

    public View Menu, Menu_source, Menu_brightness, Menu_recent , Menu_eye_off , Menu_eye_on , Menu_screenshot, Menu_comments;

    public static View Menu_volume;

    Menu_source menu_source;
    Menu_brightness menu_brightness;
    public static Menu_volume menu_volume;

    Menu_recent menu_recent;
    Menu_eye menu_eye;
    Menu_screenshot menu_screenshot;
    Menu_comments menu_comments;
    ColorSystemUiContentOberver systemUiContentOberver;

    private static final String OSD_OPEN_OTHER_SOURCE = "osd_open_other_source";

    public static AddViewToScreen mavts = new AddViewToScreen();

    public FunctionBind(Context context) {
        mycontext = context;

        mavts.setContext(mycontext);

        systemUiContentOberver = new ColorSystemUiContentOberver(mycontext);

        //按键初始化 添加
        menu_source = new Menu_source(mycontext);

        menu_brightness = new Menu_brightness(mycontext);
        menu_volume = new Menu_volume(mycontext);

        menu_recent = new Menu_recent();
//        menu_eye = new Menu_eye_off(mycontext);
//        menu_eye = new Menu_eye_on(mycontext);
        menu_screenshot = new Menu_screenshot(mycontext);
        menu_comments = new Menu_comments(mycontext);

    }

    public void Bind(View dMenu, View dMenu_source, View dMenu_brightness, View dMenu_volume, View dMenu_recent ,View dMenu_eye_off ,View dMenu_eye_on , View dMenu_screenshot, View dMenu_comments) {
        Menu = dMenu;
        Menu_source = dMenu_source;

        Menu_brightness = dMenu_brightness;
        Menu_volume = dMenu_volume;

        Menu_recent =dMenu_recent;

//        Menu_eye = dMenu_eye_off;
//        Menu_eye = dMenu_eye_on;
        Menu_screenshot = dMenu_screenshot;
        Menu_comments = dMenu_comments;

        //按键点击功能添加

        //1、信源
        menu_source.setOnclick(Menu_source);
        //监听SystemUI的信源设置，做到同步效果
        mycontext.getContentResolver().registerContentObserver(Settings.System.getUriFor(OSD_OPEN_OTHER_SOURCE), true, systemUiContentOberver);

        menu_brightness.setOnclick(Menu_brightness);
        menu_volume.setOnclick(Menu_volume);

        //最近按钮
        menu_recent.setOnclick(Menu_recent);


//        menu_eye.setOnclick(Menu_eye_off);
//        menu_eye.setOnclick(Menu_eye_on);

        //4、截图
        menu_screenshot.setOnclick(Menu_screenshot);

        //5、批注
        menu_comments.setOnclick(Menu_comments);

    }

    public static void removeItemViewByMenuState(MenuState menuState){
        if (menuState == MenuState.MENU_VOLUME || menuState == MenuState.MENU_VOLUME_DIRECT){
            menu_volume.removeView();
        }
    }


}
