package com.color.osd.models;

import android.app.Instrumentation;
import android.view.KeyEvent;
import android.view.View;

import com.color.osd.models.Enum.MenuState;
import com.color.osd.models.service.MenuService;
import com.color.osd.ui.DialogMenu;

public class Menu_recent {

    public void setOnclick(View menu_recent) {
        menu_recent.setOnClickListener(v -> {

            DialogMenu.mydialog.dismiss();//收起菜单
            MenuService.menuOn = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_APP_SWITCH);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        });
    }

}
