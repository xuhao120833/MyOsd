package com.color.osd.models;

import android.content.Context;
import android.view.View;

import com.color.osd.R;

public class FunctionBind {

    Context mycontext;

    public View Menu, Menu_source, Menu_brightness, Menu_volume, Menu_eye, Menu_screenshot, Menu_comments;

    Menu_source menu_source;
    Menu_brightness menu_brightness;
    Menu_volume menu_volume;
    Menu_eye menu_eye;
    Menu_screenshot menu_screenshot;
    Menu_comments menu_comments;

    public static AddViewToScreen mavts = new AddViewToScreen();

    public FunctionBind(Context context) {
        mycontext = context;

        mavts.setContext(mycontext);

        //按键初始化 添加
        menu_source = new Menu_source(mycontext);
//        menu_brightness = new Menu_brightness(mycontext);
//        menu_volume = new Menu_volume(mycontext);
//        menu_eye = new Menu_eye(mycontext);
//        menu_screenshot = new Menu_screenshot(mycontext);
//        menu_comments = new Menu_comments(mycontext);

    }

    public void Bind(View dMenu, View dMenu_source, View dMenu_brightness, View dMenu_volume, View dMenu_eye, View dMenu_screenshot, View dMenu_comments) {
        Menu = dMenu;
        Menu_source = dMenu_source;
//        Menu_brightness = dMenu_brightness;
//        Menu_volume = dMenu_volume;
//        Menu_eye = dMenu_eye;
//        Menu_screenshot = dMenu_screenshot;
//        Menu_comments = dMenu_comments;

        //按键点击功能添加
        menu_source.setOnclick(Menu_source);
//        menu_brightness.setOnclick(Menu_brightness);
//        menu_volume.setOnclick(Menu_volume);
//        menu_eye.setOnclick(Menu_eye);
//        menu_screenshot.setOnclick(Menu_screenshot);
//        menu_comments.setOnclick(Menu_comments);

    }


}
