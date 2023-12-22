package com.color.systemui.Contentobserver.hoverball;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import android.os.Handler;
import android.provider.Settings;
import android.database.ContentObserver;

public class SettingsControlHoverballObserver extends ContentObserver implements Instance {

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
//            InstanceUtils.hoverball.leftlayout.setVisibility(View.GONE);
//            InstanceUtils.hoverball.rightlayout.setVisibility(View.GONE);
            Log.d("SettingsControlHoverballObserver"," 主动触发消失动画");
            if(STATIC_INSTANCE_UTILS.hoverball.leftlayout.getVisibility() == View.VISIBLE){
                StaticVariableUtils.Proactive_triggering_lefthoverball_hide = 0;
                STATIC_INSTANCE_UTILS.manimationManager.lefthoverballHideAnimation();
            }
            if(STATIC_INSTANCE_UTILS.hoverball.rightlayout.getVisibility() == View.VISIBLE){
                StaticVariableUtils.Proactive_triggering_righthoverball_hide = 0;
                STATIC_INSTANCE_UTILS.manimationManager.righthoverballHideAnimation();
            }
            if(STATIC_INSTANCE_UTILS.navigationBar.leftNavibar.getVisibility() == View.VISIBLE) {
                StaticVariableUtils.Proactive_triggering_leftnavibar_hide = 0;
                STATIC_INSTANCE_UTILS.manimationManager.leftNavibarHideAnimation();
            }
            if(STATIC_INSTANCE_UTILS.navigationBar.rightNavibar.getVisibility() == View.VISIBLE) {
                StaticVariableUtils.Proactive_triggering_rightnavibar_hide = 0;
                STATIC_INSTANCE_UTILS.manimationManager.rightNavibarHideAnimation();
            }

            StaticVariableUtils.SettingsControlHoverballVisible = false;
            StaticVariableUtils.TimeManagerRunning = false;
            STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
            Settings.System.putInt(mycontext.getContentResolver(), StaticVariableUtils.WINDOWMANAGER_TO_OSD, 5);
        }else if(hoverballswitch == 1) {
//            InstanceUtils.hoverball.leftlayout.setVisibility(View.VISIBLE);
//            InstanceUtils.hoverball.rightlayout.setVisibility(View.VISIBLE);
            StaticVariableUtils.SettingsControlHoverballVisible = true;
            StaticVariableUtils.leftSlide_Or_rightSlide = "left_and_right";
            Settings.System.putInt(mycontext.getContentResolver(), StaticVariableUtils.WINDOWMANAGER_TO_OSD, 1);

        }


    }

    private void WhichOneShow() {
        if(StaticVariableUtils.Timing_begins_leftHoverballShow) {
            STATIC_INSTANCE_UTILS.hoverball.leftlayout.setVisibility(View.VISIBLE);
        }
        if(StaticVariableUtils.Timing_begins_rightHoverballShow ) {
            STATIC_INSTANCE_UTILS.hoverball.rightlayout.setVisibility(View.VISIBLE);

        }
        if(StaticVariableUtils.Timing_begins_leftNavibarShow ) {
            STATIC_INSTANCE_UTILS.navigationBar.leftNavibar.setVisibility(View.VISIBLE);
        }
        if(StaticVariableUtils.Timing_begins_rightNavibarShow) {
            STATIC_INSTANCE_UTILS.navigationBar.rightNavibar.setVisibility(View.VISIBLE);
        }
    }

    private void WhichOneHide() {
        if(StaticVariableUtils.Timing_begins_leftHoverballShow) {
            STATIC_INSTANCE_UTILS.manimationManager.lefthoverballHideAnimation();
        }
        if(StaticVariableUtils.Timing_begins_rightHoverballShow) {
            STATIC_INSTANCE_UTILS.manimationManager.righthoverballHideAnimation();
        }
        if(StaticVariableUtils.Timing_begins_leftNavibarShow) {
            STATIC_INSTANCE_UTILS.manimationManager.leftNavibarHideAnimation();
        }
        if(StaticVariableUtils.Timing_begins_rightNavibarShow) {
            STATIC_INSTANCE_UTILS.manimationManager.rightNavibarHideAnimation();
        }
    }


}
