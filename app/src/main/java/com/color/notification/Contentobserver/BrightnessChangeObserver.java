package com.color.notification.Contentobserver;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.color.osd.R;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.StaticVariableUtils;

public class BrightnessChangeObserver extends ContentObserver implements Instance {

    Context mycontext;

    int screen_brightness;

    int percentage;

    public BrightnessChangeObserver() {
        super(new Handler());
    }

    public void setContext(Context context) {

        mycontext = context;

    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        try {
            percentage = STATIC_INSTANCE_UTILS.brightnessChangeCompute.setBrightness(screen_brightness);
        } catch (Settings.SettingNotFoundException e) {
            throw new RuntimeException(e);
        }
        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.brightnessSeekBar.setProgress(percentage);
        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.brightnessSeekBar_text.setText(percentage+"%");

    }


}
