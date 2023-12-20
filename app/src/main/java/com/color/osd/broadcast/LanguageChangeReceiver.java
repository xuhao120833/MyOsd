package com.color.osd.broadcast;

import static com.color.osd.models.service.MenuService.dialogMenu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.color.osd.R;
import com.color.osd.ui.DialogMenu;

public class LanguageChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d("LanguageChangeReceiver","语言切换");
//        dialogMenu.start();//刷新资源

    }
}
