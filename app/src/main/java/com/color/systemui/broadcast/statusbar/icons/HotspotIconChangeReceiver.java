package com.color.systemui.broadcast.statusbar.icons;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.view.View;

import com.color.systemui.utils.StaticInstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

public class HotspotIconChangeReceiver extends BroadcastReceiver {

    private Context mycontext;

    private int hotspot_state;

    public HotspotIconChangeReceiver() {

    }

    public void setContext(Context context) {
        mycontext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(intent.getAction())) { //热点开关
            hotspot_state = intent.getIntExtra("wifi_state", 0);
            switch (hotspot_state) {
                case 11:
                    StaticInstanceUtils.statusBar.hotspot.setVisibility(View.GONE);
                    StaticVariableUtils.HotspotOpen = false;
                    break;
                case 13:
                    if("com.android.launcher3".equals(StaticInstanceUtils.mgetTopActivity.getPackage()) ||
                            "com.color.settings".equals(StaticInstanceUtils.mgetTopActivity.getPackage())) {
                        StaticInstanceUtils.statusBar.hotspot.setVisibility(View.VISIBLE);
                    }
                    StaticVariableUtils.HotspotOpen =true;
                    break;
                default:
                    break;
            }
        }

    }
}
