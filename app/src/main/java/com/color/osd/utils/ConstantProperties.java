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

    public static final int DENSITY = 1;

    // UI布局相关的常量类
    /**************************************音量和亮度复合背景的参数************************************/
    public static final int BRIGHTNESS_AND_VOLUME_BACKGROUND_WIDTH_DP = 343 * DENSITY;
    public static final int BRIGHTNESS_AND_VOLUME_BACKGROUND_HEIGHT_DP = 188;
    public static final int BRIGHTNESS_AND_VOLUME_BACKGROUND_MARGIN_TOP_DP = 32;
    public static final int BRIGHTNESS_AND_VOLUME_BACKGROUND_CORNER_DP = 30;   // 圆角
    public static final int BRIGHTNESS_AND_VOLUME_CHILD_VIEW_MARGIN_DP = 20;   // 孩子与背景框之间的间隔
    public static final int BRIGHTNESS_AND_VOLUME_CHILD_2_CHILD_MARGIN_DP = 8;   // 两个孩子之间的间隔

    /**********************************************************************************************/


    /***************************************音量或亮度的背景的参数*************************************/
    public static final int BRIGHTNESS_OR_VOLUME_BACKGROUND_WIDTH_DP = 303;
    public static final int BRIGHTNESS_OR_VOLUME_BACKGROUND_HEIGHT_DP = 70;
    public static final int BRIGHTNESS_OR_VOLUME_BACKGROUND_CORNER_DP = 15;   // 圆角
    /**********************************************************************************************/


    /***********************************音量或亮度的TouchBar的参数************************************/
    public static final int BRIGHTNESS_OR_VOLUME_SEEK_BAR_WIDTH_DP = 197;
    public static final int BRIGHTNESS_OR_VOLUME_SEEK_BAR_HEIGHT_DP = 46;
    /**********************************************************************************************/


    /**********************************音量或亮度的右侧信息指示的参数***********************************/
    public static final int BRIGHTNESS_OR_VOLUME_CIRCLE_VIEW_HEIGHT_DP = 46;
    public static final int BRIGHTNESS_OR_VOLUME_CIRCLE_VIEW_RIGHT_MARGIN_DP = 16;
    public static final int BRIGHTNESS_OR_VOLUME_CIRCLE_VIEW_LEFT_MARGIN_DP = 22;
    public static final int BRIGHTNESS_OR_VOLUME_CIRCLE_VIEW_TEXT_SIZE_DP = 9;


    /**********************************************************************************************/
}
