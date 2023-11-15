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
import com.color.osd.ui.Brightness_View;
import com.color.osd.ui.DialogMenu;

public class Menu_brightness implements DispatchKeyEventInterface {
    private static final String TAG = Menu_brightness.class.getSimpleName();
    private Context mycontext;

    private Brightness_View brightnessView;
    private BrightnessAndVolume_View brightnessAndVolumeView;

    public Menu_brightness(Context context) {
        mycontext = context;

        brightnessView = new Brightness_View(mycontext);

        ((MenuService)mycontext).addKeyEventListener(this);   // 注册观察者
    }


    public void setOnclick(View baseView){
        Log.d(TAG, "setOnclick: here");
        // 1、添加view的点击事件（弹出亮度设置的view）
        baseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MenuService.menuState == MenuState.NULL){
                    // 如果一开始menuState是NULL状态，说明是OSD没有开启任何二级菜单，那么直接打开亮度的二级菜单
                    // 改变当前Menu_state
                    MenuService.menuState = MenuState.MENU_BRIGHTNESS;
                    // 弹出亮度设置UI界面
                    brightnessView.source.setFocusable(false);   // 普通的亮度条 不允许聚焦（不能被选中）
                    brightnessView.initSystemBrightness();   // 每次点击事件加载view前更新下progress的值
                    FunctionBind.mavts.addView(brightnessView.source , brightnessView.lp);
                }else if(MenuService.menuState == MenuState.MENU_VOLUME_DIRECT){
                    Log.d(TAG, "onClick: here MENU_VOLUME_DIRECT");
                    // 如果当前menuState是MENU_VOLUME_DIRECT状态，说明声音二级菜单已经被遥控器直接唤起
                    // 那么这里要打开亮度和声音的复合菜单
                    MenuService.menuState = MenuState.MENU_BRIGHTNESS_VOLUME;
                    brightnessAndVolumeView = new BrightnessAndVolume_View(mycontext);
                    brightnessAndVolumeView.source.setCanFocusable(true);  // 亮度和声音的复合态才允许聚焦
                    // 先把当前的直接呼出的音量调节的touchBar的view给移除掉
                    FunctionBind.removeItemViewByMenuState(MenuState.MENU_VOLUME_DIRECT);
                    // 重新添加音量与亮度共同调整的view
                    FunctionBind.mavts.addView(brightnessAndVolumeView.source , brightnessAndVolumeView.lp);
                }

            }
        });
    }



    @Override
    public boolean onKeyEvent(KeyEvent event, MenuState menuState) {
        if (menuState == MenuState.MENU_BRIGHTNESS){
            Log.d(TAG, "onKeyEvent: MENU_BRIGHTNESS: " + event.getKeyCode());
            // 当前菜单停留在亮度, 这个时候就要响应亮度的增减了
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_DPAD_RIGHT:   // 19 up
                    brightnessView.updateBrightness(13);
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:  // 20 down
                    brightnessView.updateBrightness(-13);
                    break;
                case KeyEvent.KEYCODE_BACK:   //  4 back
                    FunctionBind.mavts.clearView(brightnessView.source);
                    MenuService.menuState = MenuState.NULL;   // 恢复状态
                    break;
                case KeyEvent.KEYCODE_HOME:   // 3 home
                    FunctionBind.mavts.clearView(brightnessView.source);
                    MenuService.menuState = MenuState.NULL;   // 恢复状态
                    DialogMenu.mydialog.dismiss();//收起菜单
                    menuOn = false;
                    break;
            }
            // 只要进入了这个state，就消耗掉此次按钮事件
            return true;
        } else if (menuState == MenuState.MENU_BRIGHTNESS_FOCUS) {
            // （亮度和声音复合态时）亮度被选择了
            Log.d(TAG, "onKeyEvent: MENU_BRIGHTNESS_FOCUS: " + event.getKeyCode());
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_DPAD_LEFT:   // 21 left
                    brightnessAndVolumeView.updateBrightness(-30);
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:  // 22 right
                    brightnessAndVolumeView.updateBrightness(30);
                    break;
                case KeyEvent.KEYCODE_BACK:   //  4 back
                    FunctionBind.mavts.clearView(brightnessAndVolumeView.source);
                    MenuService.menuState = MenuState.NULL;   // 恢复状态
                    break;
                case KeyEvent.KEYCODE_HOME:   // 3 home
                    FunctionBind.mavts.clearView(brightnessAndVolumeView.source);
                    MenuService.menuState = MenuState.NULL;   // 恢复状态
                    DialogMenu.mydialog.dismiss();//收起菜单
                    menuOn = false;
                    break;
            }
            // 只要进入了这个state，就消耗掉此次按钮事件
            return true;
        }
        else if (menuState == MenuState.MENU_VOLUME_FOCUS) {
            // （亮度和声音复合态时）声音被选择了
            Log.d(TAG, "onKeyEvent: MENU_VOLUME_FOCUS: " + event.getKeyCode());
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_DPAD_LEFT:   // 21 left
                    brightnessAndVolumeView.updateVolume(-1);
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:  // 22 right
                    brightnessAndVolumeView.updateVolume(1);
                    break;
                case KeyEvent.KEYCODE_BACK:   //  4 back
                    FunctionBind.mavts.clearView(brightnessAndVolumeView.source);
                    MenuService.menuState = MenuState.NULL;   // 恢复状态
                    break;
                case KeyEvent.KEYCODE_HOME:   // 3 home
                    FunctionBind.mavts.clearView(brightnessAndVolumeView.source);
                    MenuService.menuState = MenuState.NULL;   // 恢复状态
                    DialogMenu.mydialog.dismiss();//收起菜单
                    menuOn = false;
                    break;
            }
            // 只要进入了这个state，就消耗掉此次按钮事件
            return true;
        }else if (menuState == MenuState.MENU_BRIGHTNESS_VOLUME){
            // 亮度和声音复合态时  直接点击返回或者home  那么这里也要响应下
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:   //  4 back
                    FunctionBind.mavts.clearView(brightnessAndVolumeView.source);
                    MenuService.menuState = MenuState.NULL;   // 恢复状态
                    break;
                case KeyEvent.KEYCODE_HOME:   // 3 home
                    FunctionBind.mavts.clearView(brightnessAndVolumeView.source);
                    MenuService.menuState = MenuState.NULL;   // 恢复状态
                    DialogMenu.mydialog.dismiss();//收起菜单
                    menuOn = false;
                    break;
            }
            // 只要进入了这个state，就消耗掉此次按钮事件
            return true;
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
