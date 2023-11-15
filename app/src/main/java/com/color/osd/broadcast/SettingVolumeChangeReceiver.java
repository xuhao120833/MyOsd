package com.color.osd.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.color.osd.models.service.MenuService;

public class SettingVolumeChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "SettingVolumeChangeReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        // 这个广播接收器监听settings应用的音量tag界面是否打开
        // true：遥控器的调节音量按钮事件，由settings应用来响应，osd的音量变化监听事件不需要处理
        // false：osd的音量变化就要做相关的逻辑处理
        // 如果不进行这样的处理，settings应用的音量调节功能和osd的音量调节功能重复了，而且osd获取了焦点，导致settings应用无法被遥控
        if ("com.color.settings.VOLUME_CHANGE".equals(intent.getAction())){
//            Log.d(TAG, "onReceive: here");
            MenuService.settingVolumeChange = intent.getBooleanExtra("volume_change", false);
        }
    }
}
