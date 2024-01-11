package com.color.notification.models;

import android.content.Context;
import android.hardware.display.BrightnessInfo;
import android.hardware.display.DisplayManager;
import android.util.Log;
import android.util.MathUtils;

import com.color.notification.utils.BrightnessUtils;


/**
 * Created by lost on 2020/3/11.
 */

public class BrightnessManager {

    public final static String TAG = "BrightnessManager";
    public static final boolean DBG = true;

    public static boolean isLegalBrightness(int brightness) {
        return (brightness >= 0 && brightness <= 255);
    }

    public static void setBrightness(int brightness, Context context) {
        if (isLegalBrightness(brightness) && context != null) {
            // 将0~255的亮度值转换为0~65535，也就是2的8次方转换为2的16次方
            int valueIn65536 = (int) (brightness / 255f * 65535);
            final BrightnessInfo info = context.getDisplay().getBrightnessInfo();
            final float minBacklight = info.brightnessMinimum;
            final float maxBacklight = info.brightnessMaximum;
            final float valFloat = MathUtils.min(
                    BrightnessUtils.convertGammaToLinearFloat(valueIn65536, minBacklight, maxBacklight),
                    maxBacklight);
            DisplayManager displayManager = context.getSystemService(DisplayManager.class);
            displayManager.setBrightness(0, valFloat);
        } else {
            Log.e(TAG, "The brightness is illegal and it's value from 0 to 255,  or context is null.");
        }
    }

    public static int readTemporaryBrightness(Context context) {
        double brightness = BrightnessUtils.getCurrentBrightness(context);
        Log.d(TAG, "readTemporaryBrightness: "+brightness);
        // 将值从0~1的百分比亮度转换为0~255
        return (int) (brightness * 255);
    }
}