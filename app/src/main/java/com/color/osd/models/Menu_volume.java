package com.color.osd.models;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.color.osd.models.Enum.MenuState;
import com.color.osd.models.service.MenuService;
import com.color.osd.ui.BrightnessAndVolume_View;
import com.color.osd.ui.Volume_View;

public class Menu_volume {
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
}
