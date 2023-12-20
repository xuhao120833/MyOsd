package com.color.systemui.broadcast.sideslip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.Log;

import com.color.systemui.utils.StaticVariableUtils;

public class SideSlipReceiver extends BroadcastReceiver {

    Context mycontext;

    public IntentFilter sideslip_filter = new IntentFilter();

    public SideSlipReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(StaticVariableUtils.onSwipeFromLeft_Action)) {
            //Log.d("onSwipeFromLeft_Action"," 收到左侧侧滑通知");
            StaticVariableUtils.leftSlide_Or_rightSlide = "left";
            Settings.System.putInt(mycontext.getContentResolver(), StaticVariableUtils.WINDOWMANAGER_TO_OSD, 1);
        }

        if(intent.getAction().equals(StaticVariableUtils.onSwipeFromRight_Action)) {
            //Log.d("onSwipeFromRight_Action"," 收到右侧侧滑通知");
            StaticVariableUtils.leftSlide_Or_rightSlide = "right";
            Settings.System.putInt(mycontext.getContentResolver(), StaticVariableUtils.WINDOWMANAGER_TO_OSD, 1);
        }

    }

    public void setContext(Context context) {
        mycontext = context;
    }
}
