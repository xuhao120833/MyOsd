package com.color.osd.models;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;

import com.color.osd.ScreenShotActivity;
import com.color.osd.models.service.MenuService;
import com.color.osd.ui.DialogMenu;


public class Menu_screenshot {

    Context mycontext;

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    public static final int REQUEST_MEDIA_PROJECTION = 10001;


    public Menu_screenshot(Context context) {
        mycontext = context;
    }

    public void setOnclick(View baseView) {
        if (baseView == null) {
            Log.d("TAG", "setOnclick: baseView is null");
            return;
        }
        baseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开始截屏
                Log.d("TAG", "onClick: screen shot");
                DialogMenu.mydialog.dismiss();//收起菜单
                MenuService.menuOn = false;

                // 开启一个activity
                Intent intent = new Intent();
                intent.setClass(mycontext, ScreenShotActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mycontext.startActivity(intent);

            }
        });
    }


}
