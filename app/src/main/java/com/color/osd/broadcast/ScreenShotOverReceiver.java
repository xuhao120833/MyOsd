package com.color.osd.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.color.osd.models.service.MenuService;
import com.color.osd.ui.DialogMenu;
import com.color.systemui.interfaces.Instance;

import static com.color.osd.utils.ConstantProperties.DEBUG;

public class ScreenShotOverReceiver extends BroadcastReceiver implements Instance {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (DEBUG) Log.d("TAG", "onReceive: 接收到了截屏完毕广播");
        if (STATIC_INSTANCE_UTILS.functionBind.menu_screenshot.Osd_arouse_screenshot) {
            DialogMenu.mydialog.show();    // 弹出菜单
            MenuService.menuOn = true;
            STATIC_INSTANCE_UTILS.functionBind.menu_screenshot.Osd_arouse_screenshot = false;
        }else if(!STATIC_INSTANCE_UTILS.functionBind.menu_screenshot.Osd_arouse_screenshot){
            STATIC_INSTANCE_UTILS.myNotification.notification.setVisibility(View.VISIBLE);
        }

    }
}
