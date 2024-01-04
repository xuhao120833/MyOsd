package com.color.notification.utils;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

public class BrightnessChangeCompute {

    private Context mycontext;

    public float brightness_maximum = 255;

    public BrightnessChangeCompute() {

    }

    public void setContext(Context context) {
        mycontext = context;
    }

    public int setBrightness(int delta) throws Settings.SettingNotFoundException {
        int brightness = Settings.System.getInt(mycontext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);

//        brightness = delta > 0 ? Math.min(255, brightness + delta) : Math.max(0, brightness + delta);
        //Log.d("TAG", "upBrightness: " + brightness);
        // 设置系统亮度
//        Settings.System.putInt(mycontext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);

        Log.d("BrightnessChangeCompute"," 亮度值"+String.valueOf(toPercent(brightness)));

        return toPercent(brightness);
    }

    public int toPercent(int brightness){
        // 映射到百分比, 四舍五入取整
        return Math.round((brightness / brightness_maximum) * 100.0f);
    }

    public int updateBrightnessByTouch(int delta) throws Settings.SettingNotFoundException {
        delta = Math.min(255, delta);
        //Log.d("TAG", "upBrightness: " + brightness);
        // 设置系统亮度
        Settings.System.putInt(mycontext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, delta);

        Log.d("CustomSeekBar_Brightness", " 亮度值" + String.valueOf(toPercent(delta)));

        return toPercent(delta);
    }

}
