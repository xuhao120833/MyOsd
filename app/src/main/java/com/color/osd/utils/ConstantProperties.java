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

    public static float DENSITY = 1;
    public static final float DESIGN_SCREEN_WIDTH_DP = 1920;         // 设计稿屏幕宽度
    public static final float DESIGN_SCREEN_HEIGHT_DP = 1080;        // 设计稿屏幕高度

    // UI布局相关的常量类
    /**************************************OSD一级菜单界面的参数************************************/
    public static final int OSD_MENU_WIDTH_DP = 700;
    public static final int OSD_MENU_HEIGHT_DP = 104;
    public static final int OSD_MENU_ITEM_WIDTH_DP = 110;
    public static final int OSD_MENU_ITEM_HEIGHT_DP = 67;
    public static final int OSD_MENU_ITEM_MARGIN_LEFT_DP = 20;  // 最左侧按钮到菜单边框的间距
    public static final int OSD_MENU_ITEM_MARGIN_RIGHT_DP = 20; // 最右侧按钮到菜单边框的间距
    public static final int OSD_MENU_LAST_ITEM_WIDTH_DP = 100;  // 最右侧的“批注”按钮，宽高不同于其他按钮，故单独设置
    public static final int OSD_MENU_LAST_ITEM_HEIGHT_DP = 62;

    /**********************************************************************************************/


    /**************************************音量和亮度复合背景的参数************************************/
    public static final int BRIGHTNESS_AND_VOLUME_BACKGROUND_WIDTH_DP = (int) (343 * DENSITY);
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
    public static final int BRIGHTNESS_OR_VOLUME_SEEK_BAR_BACKGROUND_HEIGHT_DP = 6; // 未滑动到的进度条背景高度
    public static final int BRIGHTNESS_OR_VOLUME_SEEK_BAR_ICON_SIZE = 26;   // 进度条内的图标大小
    public static final int BRIGHTNESS_OR_VOLUME_SEEK_BAR_ICON_MARGIN_RIGHT = 6;
    /**********************************************************************************************/


    /**********************************音量或亮度的右侧信息指示的参数***********************************/
    public static final int BRIGHTNESS_OR_VOLUME_CIRCLE_VIEW_HEIGHT_DP = 46;
    public static final int BRIGHTNESS_OR_VOLUME_CIRCLE_VIEW_RIGHT_MARGIN_DP = 16;
    public static final int BRIGHTNESS_OR_VOLUME_CIRCLE_VIEW_LEFT_MARGIN_DP = 22;
    public static final int BRIGHTNESS_OR_VOLUME_CIRCLE_VIEW_TEXT_SIZE_DP = 18;


    /**********************************************************************************************/


    /**************************************截图显示的参数************************************/
    public static final int SCREENSHOT_IMAGEVIEW_WIDTH_DP = 400;
    public static final int SCREENSHOT_IMAGEVIEW_HEIGHT_DP = 300;

    /**********************************************************************************************/
}
