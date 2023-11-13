package com.color.osd.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.util.Log;

import com.color.osd.models.service.MenuService;
import com.color.osd.ui.DialogMenu;
import static com.color.osd.utils.ConstantProperties.DEBUG;

public class ScreenShotOverReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (DEBUG) Log.d("TAG", "onReceive: 接收到了截屏完毕广播");
        DialogMenu.mydialog.show();    // 弹出菜单
        MenuService.menuOn = true;
    }
}
