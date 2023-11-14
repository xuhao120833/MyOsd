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
import com.color.osd.ui.Volume_View;

public class Menu_volume implements DispatchKeyEventInterface {
    private static final String TAG = Menu_volume.class.getSimpleName();
    Context mycontext;
    private Volume_View volumeView;

    public Menu_volume(Context context) {
        mycontext = context;

        volumeView = new Volume_View(context);
        ((MenuService)mycontext).addKeyEventListener(this);   // 注册观察者
    }

    public void setOnclick(View baseView){
        Log.d(TAG, "setOnclick: here");
        // 1、添加view的点击事件（弹出亮度设置的view）
        baseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 改变当前Menu_state
                MenuService.menuState = MenuState.MENU_VOLUME;
                // 弹出亮度设置UI界面
                FunctionBind.mavts.addView(volumeView.source , volumeView.lp);
            }
        });
    }

    @Override
    public boolean onKeyEvent(KeyEvent event, MenuState menuState) {
        if (menuState == MenuState.MENU_VOLUME){
            // 当前菜单停留在亮度, 这个时候就要响应亮度的增减了
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_DPAD_UP:   // 19 up
                    volumeView.updateVolume(1);
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:  // 20 down
                    volumeView.updateVolume(-1);
                    break;
                case KeyEvent.KEYCODE_BACK:   //  4 back
                    FunctionBind.mavts.clearView(volumeView.source);
                    MenuService.menuState = MenuState.NULL;   // 恢复状态
                    break;
                case KeyEvent.KEYCODE_HOME:   // 3 home
                    FunctionBind.mavts.clearView(volumeView.source);
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
