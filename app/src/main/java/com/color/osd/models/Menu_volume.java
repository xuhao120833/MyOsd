package com.color.osd.models;

import static com.color.osd.models.service.MenuService.menuOn;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.color.osd.models.Enum.MenuState;
import com.color.osd.models.interfaces.DispatchKeyEventInterface;
import com.color.osd.models.service.MenuService;
import com.color.osd.ui.BrightnessAndVolume_View;
import com.color.osd.ui.DialogMenu;
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
                Log.d(TAG, "onClick");
                volumeView.source.setFocusable(false);    // 单声音状态栏直接禁止聚焦
                volumeView.initSystemVolume();     // 每次点击事件加载view之前都初始化下当前progress的音量值，避免其他地方改了，当前的volumeView对象的值没有变
                if (MenuService.menuState == MenuState.NULL){
                    // 说明是来源于OSD的一级音量菜单点击事件，正统路线
                    // 改变当前Menu_state
                    MenuService.menuState = MenuState.MENU_VOLUME;
                    // 弹出声音touchBar的UI界面
                    FunctionBind.mavts.addView(volumeView.source, volumeView.lp);
                }else if (MenuService.menuState == MenuState.MENU_VOLUME_DIRECT){
                    // 说明是直接来源于按键小板的音量加减按钮，非正统路线
                    FunctionBind.mavts.addView(volumeView.source, volumeView.lp);
                }else if(MenuService.menuState == MenuState.MENU_BRIGHTNESS){
                    // 说明是二级菜单亮度的touchBar已经被唤出
                    // 那么这里要变身复合态
                    if (brightnessAndVolumeView != null){
                        MenuService.menuState = MenuState.MENU_BRIGHTNESS_VOLUME;
                        brightnessAndVolumeView.source.setCanFocusable(true);  // 亮度和声音的复合态才允许聚焦
                        // 先把当前的直接呼出的音量调节的touchBar的view给移除掉
                        functionBind.removeItemViewByMenuState(MenuState.MENU_BRIGHTNESS);
                        // 重新添加音量与亮度共同调整的view
                        FunctionBind.mavts.addView(brightnessAndVolumeView.source , brightnessAndVolumeView.lp);
                        // 设置延时自动关闭
                        brightnessAndVolumeView.reClose(brightnessAndVolumeView.source);
                    }
                }

                // 无论上面那种情况加载了音量调节的touchBar，都要设置自动关闭
                volumeView.autoClose(volumeView.source);

            }
        });
    }

    @Override
    public boolean onKeyEvent(KeyEvent event, MenuState menuState) {
        if (menuState == MenuState.MENU_VOLUME || menuState == MenuState.MENU_VOLUME_DIRECT){
            // 正常状态下，
            // 当前菜单停留在声音, 这个时候就要响应声音的增减了
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_DPAD_RIGHT:   // 19 up
                    volumeView.updateVolume(1);
                    volumeView.reClose(volumeView.source);   // 重置“自动关闭任务”
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:  // 20 down
                    volumeView.updateVolume(-1);
                    volumeView.reClose(volumeView.source);   // 重置“自动关闭任务”
                    break;
                case KeyEvent.KEYCODE_BACK:   //  4 back
                    volumeView.cancelAutoClose();   // 取消“自动关闭任务”，因为已经人工关闭了
                    FunctionBind.mavts.clearView(volumeView.source);
                    MenuService.menuState = MenuState.NULL;   // 恢复状态
                    break;
                case KeyEvent.KEYCODE_HOME:   // 3 home
                    volumeView.cancelAutoClose();   // 取消“自动关闭任务”，因为已经人为关闭了
                    FunctionBind.mavts.clearView(volumeView.source);
                    MenuService.menuState = MenuState.NULL;   // 恢复状态
                    DialogMenu.mydialog.dismiss();//收起菜单
                    menuOn = false;
                    break;
            }
            // 只要进入了这个state，就消耗掉此次按钮事件
            return true;
        }
//        if (menuState == MenuState.MENU_VOLUME_DIRECT){
//            // 直接进入MENU_VOLUME_DIRECT状态，这个时候按键的左右这里不要处理，否则OSD项目的左右不生效了
//            switch (event.getKeyCode()){
//                case KeyEvent.KEYCODE_BACK:   //  4 back
//                    volumeView.cancelAutoClose();   // 取消“自动关闭任务”，因为已经人为关闭了
//                    FunctionBind.mavts.clearView(volumeView.source);
//                    MenuService.menuState = MenuState.NULL;   // 恢复状态
//                    break;
//                case KeyEvent.KEYCODE_HOME:   // 3 home
//                    volumeView.cancelAutoClose();   // 取消“自动关闭任务”，因为已经人为关闭了
//                    FunctionBind.mavts.clearView(volumeView.source);
//                    MenuService.menuState = MenuState.NULL;   // 恢复状态
//                    DialogMenu.mydialog.dismiss();//收起菜单
//                    menuOn = false;
//                    break;
//            }
//            // 只要进入了这个state，就消耗掉此次按钮事件
//            return true;
//        }
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
