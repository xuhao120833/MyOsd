package com.color.osd.models;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.color.osd.models.Enum.MenuState;
import com.color.osd.models.interfaces.DispatchKeyEventInterface;
import com.color.osd.models.service.MenuService;
import com.color.osd.ui.BrightnessAndVolume_View;
import com.color.osd.ui.Volume_View;

public class Menu_volume implements DispatchKeyEventInterface {
    private static final String TAG = Menu_volume.class.getSimpleName();
    Context mycontext;
    private Volume_View volumeView;
    private FunctionBind functionBind;

    private BrightnessAndVolume_View brightnessAndVolumeView;

    public void setBrightnessAndVolumeView(BrightnessAndVolume_View brightnessAndVolumeView) {
        this.brightnessAndVolumeView = brightnessAndVolumeView;
    }

    public Menu_volume(Context context, FunctionBind _functionBind) {
        mycontext = context;
        functionBind = _functionBind;
        volumeView = new Volume_View(context);
        ((MenuService)mycontext).addKeyEventListener(this);   // 注册观察者
    }

    public void setOnclick(View baseView){
        Log.d(TAG, "setOnclick: here");
        // 1、添加view的点击事件（弹出亮度设置的view）
        baseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volumeView.source.setFocusable(true);    // view开启聚焦，可以监听onKeyDown事件
                volumeView.initSystemVolume();     // 每次点击事件加载view之前都初始化下当前progress的音量值，避免其他地方改了，当前的volumeView对象的值没有变
                if (MenuService.menuState == MenuState.NULL){
                    Log.d(TAG, "onClick: 音量的普通状态");
                    // 说明是来源于OSD的一级音量菜单点击事件，正统路线
                    // 改变当前Menu_state
                    MenuService.menuState = MenuState.MENU_VOLUME;
                    // 加这个settingVolumeChange就是避免音量监听的广播响应（因为音量调节的二级菜单已经在处理音量加减了，那么这个广播就不要处理，避免重复调整出现bug）
//                    MenuService.settingVolumeChange = true;
                    // 弹出声音touchBar的UI界面
                    FunctionBind.mavts.addView(volumeView.source, volumeView.lp);
                    volumeView.autoClose(volumeView.source);
                }else if(MenuService.menuState == MenuState.MENU_BRIGHTNESS ||
                        MenuService.menuState == MenuState.MENU_BRIGHTNESS_DIRECT){
                    // 说明是二级菜单亮度的touchBar已经被唤出
                    // 那么这里要变身复合态
                    Log.d(TAG, "onClick: 我要变成复合态，且首先操控音量~");
                    if (brightnessAndVolumeView != null){
                        MenuState oldMenuState = MenuService.menuState;
                        MenuService.menuState = MenuState.MENU_BRIGHTNESS_VOLUME;
                        brightnessAndVolumeView.source.setCanFocusable(true);  // 亮度和声音的复合态才允许聚焦
                        brightnessAndVolumeView.source.setSelectView(false);  // 设置音量被默认聚焦（不需要上下选择是音量还是亮度）
                        brightnessAndVolumeView.checkProgress();
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

    public void onVolumeChanged(int volume) {
        volumeView.onVolumeChanged(volume);
    }

    public void onVolumeChangedInBrightnessAndVolume(int volume) {
        brightnessAndVolumeView.changeVolume(volume);
    }

    public void removeView(){
        volumeView.cancelAutoClose();
        FunctionBind.mavts.clearView(volumeView.source);
    }

    @Override
    public boolean onKeyEvent(KeyEvent event, MenuState menuState) {
        Log.d(TAG, "onKeyEvent: " + event.getKeyCode());
        // 屏蔽掉非音量加减相关的event
        if (event.getKeyCode() != KeyEvent.KEYCODE_VOLUME_DOWN &&
                event.getKeyCode() != KeyEvent.KEYCODE_VOLUME_UP){
            return false;
        }
        // 1 先加UI
        if (menuState == MenuState.NULL){
            // 如果当前状态时NULL，那么就要打开音量条的UI
            MenuService.menuState = MenuState.MENU_VOLUME;
            // 2、把亮度条给加载出来
            volumeView.source.setFocusable(true);   // 普通的亮度条 直接开启聚焦 方便其View响应onKeyDown事件
            volumeView.initSystemVolume();      // 每次点击事件加载view前更新下progress的值
            FunctionBind.mavts.addView(volumeView.source , volumeView.lp);
            volumeView.autoClose(volumeView.source); // 设置延时自动关闭
        } else if (menuState == MenuState.MENU_BRIGHTNESS) {
            // 如果当前是亮度调整状态，那么这里就要进行复合态，音量和亮度一起调整
            if (brightnessAndVolumeView != null){
                MenuService.menuState = MenuState.MENU_BRIGHTNESS_VOLUME;
                brightnessAndVolumeView.source.setCanFocusable(true);  // 亮度和声音的复合态才允许聚焦
                brightnessAndVolumeView.source.setSelectView(false);  // 设置亮度被默认聚焦（不需要上下选择是音量还是亮度）
                brightnessAndVolumeView.checkProgress();    // 检查一下当前系统的音量和亮度值，避免单独调整和这里的复合调整不同步
                // 先把当前的直接呼出的音量调节的touchBar的view给移除掉
                functionBind.removeItemViewByMenuState(menuState);
                // 重新添加音量与亮度共同调整的view
                FunctionBind.mavts.addView(brightnessAndVolumeView.source , brightnessAndVolumeView.lp);
                // 设置延时自动关闭
                brightnessAndVolumeView.reClose(brightnessAndVolumeView.source);
            }
        } else if (menuState == MenuState.MENU_VOLUME) {
            // 说明当前的音量条已经打开了，那么这里要开始音量的调整了
            if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP){
                volumeView.updateVolume(1);
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                volumeView.updateVolume(-1);
            }
            volumeView.reClose(volumeView.source);
        } else if (menuState == MenuState.MENU_BRIGHTNESS_VOLUME ||
                menuState == MenuState.MENU_BRIGHTNESS_FOCUS ||
                menuState == MenuState.MENU_VOLUME_FOCUS) {
            // 说明是在亮度和音量的复合态中，这里也要调整复合态UI下的音量
            if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP){
                // 声音加
                brightnessAndVolumeView.updateVolume(1);
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                // 声音减
                brightnessAndVolumeView.updateVolume(-1);
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
