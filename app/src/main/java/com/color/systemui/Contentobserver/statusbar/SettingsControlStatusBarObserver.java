package com.color.systemui.Contentobserver.statusbar;

import android.content.Context;
import android.view.View;

import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import android.database.ContentObserver;
import android.provider.Settings;
import android.os.Handler;

public class SettingsControlStatusBarObserver extends ContentObserver implements Instance {

    public final String OPEN_STATUS_BAR = "is_open_status_bar";
    int fswitch = 0;
    Context mycontext;


    public SettingsControlStatusBarObserver() {
        super(new Handler());
    }

    public void setContext(Context context) {
        mycontext = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        fswitch = Settings.System.getInt(mycontext.getContentResolver(),
                OPEN_STATUS_BAR, 0);
        //Log.d("fswitch值为",String.valueOf(fswitch));
        if(fswitch == 0) {
//            STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.GONE);
            STATIC_INSTANCE_UTILS.manimationManager.statusBarHideAnimation();
            StaticVariableUtils.SettingsControlStatusBarVisible = false;
        }
        if(fswitch == 1) {
//            STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.VISIBLE);
            STATIC_INSTANCE_UTILS.statusBar.wifi_frame.setVisibility(View.VISIBLE);
            STATIC_INSTANCE_UTILS.statusBar.udisk_frame.setVisibility(View.VISIBLE);
            STATIC_INSTANCE_UTILS.statusBar.ethernet_frame.setVisibility(View.VISIBLE);
            STATIC_INSTANCE_UTILS.manimationManager.statusbarShowAnimation();
            StaticVariableUtils.SettingsControlStatusBarVisible = true;
        }

    }

}
