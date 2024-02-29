package com.color.systemui.broadcast.statusbar.icons;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.view.View;

import com.color.osd.R;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

public class HotspotIconChangeReceiver extends BroadcastReceiver implements Instance {

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
                    STATIC_INSTANCE_UTILS.statusBar.hotspot.setVisibility(View.GONE);
                    STATIC_INSTANCE_UTILS.statusBar.statusbar.removeView(STATIC_INSTANCE_UTILS.statusBar.hotspot_frame);
                    StaticVariableUtils.HotspotOpen = false;
                    break;
                case 13:
                    if ("com.android.launcher3".equals(STATIC_INSTANCE_UTILS.mgetTopActivity.getPackage()) ||
                            "com.color.settings".equals(STATIC_INSTANCE_UTILS.mgetTopActivity.getPackage())) {

                        STATIC_INSTANCE_UTILS.statusBar.hotspot.setVisibility(View.VISIBLE);
                        STATIC_INSTANCE_UTILS.statusBar.hotspot.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.statusbar_hotspot));
                        STATIC_INSTANCE_UTILS.statusBar.statusbar.addView(STATIC_INSTANCE_UTILS.statusBar.hotspot_frame);
                    }
                    StaticVariableUtils.HotspotOpen = true;
                    break;
                default:
                    break;
            }
        }

    }
}
