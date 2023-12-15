package com.color.systemui.Contentobserver.hoverball;

import android.content.Context;
import android.view.View;

import com.color.systemui.utils.StaticInstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import android.os.Handler;
import android.provider.Settings;
import android.database.ContentObserver;

public class SettingsControlHoverballObserver extends ContentObserver {

    Context mycontext;

    public final String OPEN_FAST_TOOLBAR = "is_open_fast_toolbar";

    public int hoverballswitch;

    public SettingsControlHoverballObserver() {
        super(new Handler());
    }

    public void setContext(Context context) {
        mycontext = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        hoverballswitch = Settings.System.getInt(mycontext.getContentResolver(),
                OPEN_FAST_TOOLBAR, 5);
        if(hoverballswitch == 0) {
            StaticInstanceUtils.hoverball.leftlayout.setVisibility(View.GONE);
            StaticInstanceUtils.hoverball.rightlayout.setVisibility(View.GONE);
            StaticVariableUtils.SettingsControlHoverballVisible = false;
        }else if(hoverballswitch == 1) {
            StaticInstanceUtils.hoverball.leftlayout.setVisibility(View.VISIBLE);
            StaticInstanceUtils.hoverball.rightlayout.setVisibility(View.VISIBLE);
            StaticVariableUtils.SettingsControlHoverballVisible = true;
        }


    }

}
