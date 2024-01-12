package com.color.osd.models.interfaces;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.util.Log;

import com.color.notification.models.BrightnessManager;
import com.color.osd.ui.views.CltTouchBarBaseView;

public class OSDBrightnessListener implements DisplayManager.DisplayListener {
    private static final String TAG = "BrightnessListener";
    private static final boolean DBG = true;

    private int lastBrightness = -1;
    private Context mContext;
    private CltTouchBarBaseView view;

    public OSDBrightnessListener(Context mContext, CltTouchBarBaseView view) {
        this.mContext = mContext;
        this.view = view;
    }

    @Override
    public void onDisplayAdded(int displayId) {

    }

    @Override
    public void onDisplayRemoved(int displayId) {

    }

    @Override
    public void onDisplayChanged(int displayId) {
        final int brightness = BrightnessManager.readTemporaryBrightness(mContext);
        if (brightness < 0 || brightness > 255) {
            Log.e(TAG, "brightness is invalid: " + brightness);
            return;
        }
        // it will trigger twice times.
        if (lastBrightness == brightness) {
            return;
        }
        view.setProgress(brightness);
//        brightnessSeekBar.setProgress(brightness);
//        brightnessTextView.setText(Math.round(brightness/2.55)+"%");
        if (DBG) {
            Log.d(TAG, "sendBrightnessToArm: " + brightness);
        }
    }
}