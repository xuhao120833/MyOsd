package com.color.osd.models;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.color.osd.models.Enum.MenuState;
import com.color.osd.models.interfaces.DispatchKeyEventInterface;
import com.color.osd.models.service.MenuService;
import com.color.osd.ui.BrightnessAndVolume_View;
import com.color.osd.ui.Brightness_View;

public class Menu_brightness implements DispatchKeyEventInterface {
    private static final String TAG = Menu_brightness.class.getSimpleName();
    private Context mycontext;

    private Brightness_View brightnessView;
    private BrightnessAndVolume_View brightnessAndVolumeView;

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private FunctionBind functionBind;

    public Menu_brightness(Context context, FunctionBind _functionBind) {
        mycontext = context;
        functionBind = _functionBind;

        brightnessView = new Brightness_View(mycontext);

        ((MenuService)mycontext).addKeyEventListener(this);   // 注册观察者
    }

    public void setBrightnessAndVolumeView(BrightnessAndVolume_View brightnessAndVolumeView) {
        this.brightnessAndVolumeView = brightnessAndVolumeView;
    }

    public void setOnclick(View baseView){
        //Log.d(TAG, "setOnclick: here");
        // 1、添加view的点击事件（弹出亮度设置的view）
        baseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuService.menuState == MenuState.NULL){
                    //Log.d(TAG, "onClick: 亮度普通状态");
                    // 如果一开始menuState是NULL状态，说明是OSD没有开启任何二级菜单
                    // 那么这个点击事件就是来自与OSD的一级菜单“亮度”的点击事件，正常流程
                    // 改变当前Menu_state
                    MenuService.menuState = MenuState.MENU_BRIGHTNESS;
                    // 弹出亮度设置UI界面
                    brightnessView.source.setFocusable(true);   // 普通的亮度条 直接开启聚焦 方便其View响应onKeyDown事件
                    brightnessView.initSystemBrightness();   // 每次点击事件加载view前更新下progress的值
                    FunctionBind.mavts.addView(brightnessView.source , brightnessView.lp);
                    // 设置延时自动关闭
                    brightnessView.autoClose(brightnessView.source);
                }else if (MenuService.menuState == MenuState.MENU_BRIGHTNESS_DIRECT) {
                    //Log.d(TAG, "onClick: 直接亮度");
                    // 说明是直接来源于按键小板的亮度加减按钮，非正统路线
                    brightnessView.source.setFocusable(true);   // 普通的亮度条 直接开启聚焦 方便其View响应onKeyDown事件
                    brightnessView.initSystemBrightness();   // 每次点击事件加载view前更新下progress的值
                    FunctionBind.mavts.addView(brightnessView.source , brightnessView.lp);
                    brightnessView.autoClose(brightnessView.source); // 设置延时自动关闭
                }else if(MenuService.menuState == MenuState.MENU_VOLUME_DIRECT ||
                        MenuService.menuState == MenuState.MENU_VOLUME ||
                        MenuService.menuState == MenuState.MENU_VOLUME_FOCUS){
                    //Log.d(TAG, "onClick: 我要变成复合态，且首先操作亮度~");
                    // 如果当前menuState是MENU_VOLUME_DIRECT状态，或者是MENU_VOLUME
                    // 说明声音二级菜单已经被遥控器唤起，并且没有自动消失
                    // 那么这里要打开亮度和声音的复合菜单
                    if (brightnessAndVolumeView != null){
                        MenuState oldMenuState = MenuService.menuState;
                        MenuService.menuState = MenuState.MENU_BRIGHTNESS_VOLUME;
                        brightnessAndVolumeView.source.setCanFocusable(true);  // 亮度和声音的复合态才允许聚焦
                        brightnessAndVolumeView.source.setSelectView(true);  // 设置亮度被默认聚焦（不需要上下选择是音量还是亮度）
                        brightnessAndVolumeView.checkProgress();    // 检查一下当前系统的音量和亮度值，避免单独调整和这里的复合调整不同步
                        // 先把当前的直接呼出的音量调节的touchBar的view给移除掉
                        functionBind.removeItemViewByMenuState(oldMenuState);
                        // 重新添加音量与亮度共同调整的view
                        FunctionBind.mavts.addView(brightnessAndVolumeView.source , brightnessAndVolumeView.lp);
                        // 设置延时自动关闭
                        brightnessAndVolumeView.reClose(brightnessAndVolumeView.source);
                    }
                }

            }
        });
    }

    public void removeView(){
        brightnessView.cancelAutoClose();
        FunctionBind.mavts.clearView(brightnessView.source);
    }

    @Override
    public boolean onKeyEvent(KeyEvent event, MenuState menuState) {
        //Log.d(TAG, "onKeyEvent: " + event.getKeyCode() + ", " + event.getAction() + ", " + menuState);
        if (event.getAction() == KeyEvent.ACTION_UP ||
                (event.getKeyCode() != KeyEvent.KEYCODE_BRIGHTNESS_UP &&
                        event.getKeyCode() != KeyEvent.KEYCODE_BRIGHTNESS_DOWN)) return false;    // 屏蔽掉除开亮度调整的事件

        // 这个事件只会来自于按键小板上的亮度加减时的down事件
        // 1、先看加那个UI
        if (menuState == MenuState.NULL){
            // menuState为NULL状态，说明亮度条还没显示出来，先给他整出来
            // 1、变化当前状态，方便其他事件能知晓当前在干嘛
            MenuService.menuState = MenuState.MENU_BRIGHTNESS;
            // 2、把亮度条给加载出来
            brightnessView.source.setFocusable(true);   // 普通的亮度条 直接开启聚焦 方便其View响应onKeyDown事件
            brightnessView.initSystemBrightness();      // 每次点击事件加载view前更新下progress的值
            FunctionBind.mavts.addView(brightnessView.source , brightnessView.lp);
            brightnessView.autoClose(brightnessView.source); // 设置延时自动关闭
        } else if (menuState == MenuState.MENU_VOLUME) {
            //Log.d(TAG, "onKeyEvent: 当前处于音量调整中，但我监听到亮度变化，所以我要变成复合态，且首先调整亮度");
            if (brightnessAndVolumeView != null){
                MenuService.menuState = MenuState.MENU_BRIGHTNESS_VOLUME;
                brightnessAndVolumeView.source.setCanFocusable(true);  // 亮度和声音的复合态才允许聚焦
                brightnessAndVolumeView.source.setSelectView(true);  // 设置亮度被默认聚焦（不需要上下选择是音量还是亮度）
                brightnessAndVolumeView.checkProgress();    // 检查一下当前系统的音量和亮度值，避免单独调整和这里的复合调整不同步
                // 先把当前的直接呼出的音量调节的touchBar的view给移除掉
                functionBind.removeItemViewByMenuState(menuState);
                // 重新添加音量与亮度共同调整的view
                FunctionBind.mavts.addView(brightnessAndVolumeView.source , brightnessAndVolumeView.lp);
                // 设置延时自动关闭
                brightnessAndVolumeView.reClose(brightnessAndVolumeView.source);
            }
        }

        // 2、对应UI加上其变化的值
        if (menuState == MenuState.MENU_BRIGHTNESS){
            if (event.getKeyCode() == KeyEvent.KEYCODE_BRIGHTNESS_UP){
                // 声音加
                brightnessView.updateBrightness(13);
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_BRIGHTNESS_DOWN) {
                // 声音减
                brightnessView.updateBrightness(-13);
            }
            brightnessView.reClose(brightnessView.source);
        } else if (menuState == MenuState.MENU_BRIGHTNESS_VOLUME ||
                menuState == MenuState.MENU_BRIGHTNESS_FOCUS ||
                menuState == MenuState.MENU_VOLUME_FOCUS) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BRIGHTNESS_UP){
                // 声音加
                brightnessAndVolumeView.updateBrightness(13);
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_BRIGHTNESS_DOWN) {
                // 声音减
                brightnessAndVolumeView.updateBrightness(-13);
            }
            brightnessAndVolumeView.reClose(brightnessAndVolumeView.source);
        }



        return false;
    }

    @Override
    public boolean isHomeKeyEvent(KeyEvent event) {
        return false;
    }

    @Override
    public boolean isBackKeyEvent(KeyEvent event) {
        return false;
    }
}
