package com.color.osd.models;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.color.osd.models.service.MenuService;
import com.color.osd.models.service.ScreenShotService;
import com.color.osd.ui.DialogMenu;
import com.color.systemui.interfaces.Instance;


public class Menu_screenshot implements Instance {
    Context mycontext;

    public boolean Osd_arouse_screenshot = false;

    public Menu_screenshot(Context context) {
        mycontext = context;
    }

    public void setOnclick(View baseView) {
        if (baseView == null) {
            //Log.d("TAG", "setOnclick: baseView is null");
            return;
        }
        baseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开始截屏
                //Log.d("TAG", "onClick: screen shot");
                DialogMenu.mydialog.dismiss();//收起菜单
                MenuService.menuOn = false;

//                // 开启一个activity
//                Intent intent = new Intent();
//                intent.setClass(mycontext, ScreenShotActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mycontext.startActivity(intent);

                Osd_arouse_screenshot = true;
                // 开启一个截屏服务
                Intent intentService = new Intent(mycontext, ScreenShotService.class);
                mycontext.startService(intentService);

            }
        });
    }


}
