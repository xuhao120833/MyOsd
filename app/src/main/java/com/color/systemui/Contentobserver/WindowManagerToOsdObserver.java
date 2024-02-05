package com.color.systemui.Contentobserver;

import android.content.Context;

import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;
import android.database.ContentObserver;
import android.provider.Settings;
import android.os.Handler;
import android.util.Log;

public class WindowManagerToOsdObserver extends ContentObserver implements Instance {

    private Context mycontext;

    public int globalClick;//全局点击标志位

    public WindowManagerToOsdObserver() {
        super(new Handler());
    }

    public void setContext(Context context) {
        mycontext = context;

        Settings.System.putInt(mycontext.getContentResolver(), StaticVariableUtils.WINDOWMANAGER_TO_OSD, 5);

    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        if (Settings.System.getInt(mycontext.getContentResolver(), StaticVariableUtils.WINDOWMANAGER_TO_OSD, 5) != 5) {
            globalClick = Settings.System.getInt(mycontext.getContentResolver(), StaticVariableUtils.WINDOWMANAGER_TO_OSD, 5);
        }
        Log.d("WindowManagerToOsdObserver onChange",String.valueOf(globalClick));
        Log.d("WindowManagerToOsdObserver onChange",String.valueOf(StaticVariableUtils.TimeManagerRunning));
        Log.d("WindowManagerToOsdObserver onChange",String.valueOf(StaticVariableUtils.SettingsControlHoverballVisible));

        if (globalClick == 1 && !StaticVariableUtils.TimeManagerRunning && StaticVariableUtils.SettingsControlHoverballVisible) {
            //Log.d("WindowManagerToOsdObserver "," 显示组件");
            StaticVariableUtils.TimeManagerRunning = true;
            Timing_begins_WhichOneShow();
            STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();
        }


        if(globalClick == 0 && StaticVariableUtils.TimeManagerRunning && StaticVariableUtils.SettingsControlHoverballVisible) {
            StaticVariableUtils.TimeManagerRunning = false;
            Timed_end_WhichOneHide();
            STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
        }

        Settings.System.putInt(mycontext.getContentResolver(), StaticVariableUtils.WINDOWMANAGER_TO_OSD, 5);

    }

    public void Timing_begins_WhichOneShow() {
        if((StaticVariableUtils.Timing_begins_leftHoverballShow && StaticVariableUtils.leftSlide_Or_rightSlide.equals("left")) ||
                (StaticVariableUtils.Timing_begins_leftHoverballShow && StaticVariableUtils.leftSlide_Or_rightSlide.equals("left_and_right"))) {
            STATIC_INSTANCE_UTILS.manimationManager.lefthoverballShowAnimation();
        }
        if((StaticVariableUtils.Timing_begins_rightHoverballShow && StaticVariableUtils.leftSlide_Or_rightSlide.equals("right")) ||
                (StaticVariableUtils.Timing_begins_rightHoverballShow && StaticVariableUtils.leftSlide_Or_rightSlide.equals("left_and_right"))) {
            STATIC_INSTANCE_UTILS.manimationManager.righthoverballShowAnimation();
        }
        if((StaticVariableUtils.Timing_begins_leftNavibarShow && StaticVariableUtils.leftSlide_Or_rightSlide.equals("left")) ||
                (StaticVariableUtils.Timing_begins_leftNavibarShow && StaticVariableUtils.leftSlide_Or_rightSlide.equals("left_and_right"))) {
            STATIC_INSTANCE_UTILS.manimationManager.leftNavibarShowAnimation();
        }
        if((StaticVariableUtils.Timing_begins_rightNavibarShow && StaticVariableUtils.leftSlide_Or_rightSlide.equals("right")) ||
                (StaticVariableUtils.Timing_begins_rightNavibarShow && StaticVariableUtils.leftSlide_Or_rightSlide.equals("left_and_right"))) {
            STATIC_INSTANCE_UTILS.manimationManager.rightNavibarShowAnimation();
        }
        StaticVariableUtils.leftSlide_Or_rightSlide = " ";
    }

    public void Timed_end_WhichOneHide() {
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


