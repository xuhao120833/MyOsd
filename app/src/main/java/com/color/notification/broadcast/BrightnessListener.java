package com.color.notification.broadcast;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.util.Log;

import com.color.notification.models.BrightnessManager;
import com.color.systemui.interfaces.Instance;

public class BrightnessListener implements DisplayManager.DisplayListener , Instance {

    private Context mycontext;

    private static final String TAG = "BrightnessListener";
    private static final boolean DBG = true;

    private int lastBrightness = -1;

    public void setContext(Context context) {
        mycontext = context;
    }

    @Override
    public void onDisplayAdded(int displayId) {

    }

    @Override
    public void onDisplayRemoved(int displayId) {

    }

    @Override
    public void onDisplayChanged(int displayId) {
        final int brightness = BrightnessManager.readTemporaryBrightness(mycontext);
        if (brightness < 0 || brightness > 255) {
            Log.e(TAG, "brightness is invalid: " + brightness);
            return;
        }
        // it will trigger twice times.
        if (lastBrightness == brightness) {
            return;
        }

        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.brightnessSeekBar.setProgress(brightness);
        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.brightnessSeekBar_text.setText(Math.round(brightness/2.55)+"%");

//        brightnessSeekBar.setProgress(brightness);
//        brightnessTextView.setText(Math.round(brightness/2.55)+"%");
        if (DBG) {
            Log.d(TAG, "sendBrightnessToArm: " + brightness);
        }
    }
}