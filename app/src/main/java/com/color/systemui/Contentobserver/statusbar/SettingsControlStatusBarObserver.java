package com.color.systemui.Contentobserver.statusbar;

import android.content.Context;
import android.app.ActionBar;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.LayoutInflater;
import com.color.osd.R;
import com.color.systemui.utils.StaticInstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import android.graphics.PixelFormat;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.content.Intent;
import android.content.ContextWrapper;
import android.database.ContentObserver;
import android.provider.Settings;
import android.util.Log;
import android.os.Handler;

public class SettingsControlStatusBarObserver extends ContentObserver {

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
        Log.d("fswitch值为",String.valueOf(fswitch));
        if(fswitch == 0) {
            StaticInstanceUtils.statusBar.statusbar.setVisibility(View.GONE);
            StaticVariableUtils.SettingsControlStatusBarVisible = false;
        }
        if(fswitch == 1) {
            StaticInstanceUtils.statusBar.statusbar.setVisibility(View.VISIBLE);
            StaticVariableUtils.SettingsControlStatusBarVisible = true;
        }

    }

}
