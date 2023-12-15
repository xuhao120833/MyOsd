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
//            StaticInstanceUtils.hoverball.leftlayout.setVisibility(View.GONE);
//            StaticInstanceUtils.hoverball.rightlayout.setVisibility(View.GONE);
            if(StaticInstanceUtils.hoverball.leftlayout.getVisibility() == View.VISIBLE){
                StaticVariableUtils.Proactive_triggering_lefthoverball_hide = 0;
                StaticInstanceUtils.manimationManager.lefthoverballHideAnimation();
            }
            if(StaticInstanceUtils.hoverball.rightlayout.getVisibility() == View.VISIBLE){
                StaticVariableUtils.Proactive_triggering_righthoverball_hide = 0;
                StaticInstanceUtils.manimationManager.righthoverballHideAnimation();
            }
            if(StaticInstanceUtils.navigationBar.leftNavibar.getVisibility() == View.VISIBLE) {
                StaticVariableUtils.Proactive_triggering_leftnavibar_hide = 0;
                StaticInstanceUtils.manimationManager.leftNavibarHideAnimation();
            }
            if(StaticInstanceUtils.navigationBar.rightNavibar.getVisibility() == View.VISIBLE) {
                StaticVariableUtils.Proactive_triggering_rightnavibar_hide = 0;
                StaticInstanceUtils.manimationManager.rightNavibarHideAnimation();
            }

            StaticVariableUtils.SettingsControlHoverballVisible = false;
            StaticVariableUtils.TimeManagerRunning = false;
            StaticInstanceUtils.mtimeManager.Time_handler_removeCallbacks();
            Settings.System.putInt(mycontext.getContentResolver(), StaticVariableUtils.WINDOWMANAGER_TO_OSD, 5);
        }else if(hoverballswitch == 1) {
//            StaticInstanceUtils.hoverball.leftlayout.setVisibility(View.VISIBLE);
//            StaticInstanceUtils.hoverball.rightlayout.setVisibility(View.VISIBLE);
            StaticVariableUtils.SettingsControlHoverballVisible = true;
            Settings.System.putInt(mycontext.getContentResolver(), StaticVariableUtils.WINDOWMANAGER_TO_OSD, 1);
        }


    }

}
