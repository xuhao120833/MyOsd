package com.color.osd.utils;

import android.os.Environment;

/**
 * 常量类
 */
public class ConstantProperties {
    // 系统截屏完毕的广播action
    public static final boolean DEBUG = false;
    public static final String SCREENSHOT_OVER_ACTION = "com.colorlight.SCREENSHOT_OVER";
    public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String SCREENSHOT_SAVE_PATH = BASE_PATH + "/DCIM/screen_shot";

}
