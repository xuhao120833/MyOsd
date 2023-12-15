package com.color.systemui.utils;

public class StaticVariableUtils {

    //1、状态栏
    public static boolean haveUsbDevice = false;//是否插入Usb设备

    public static boolean WifiOpen = true;//Wifi是否打开

    public static boolean EthernetConnected = false;//以太网是否连接

    public static boolean HotspotOpen = false;

    //2、设置对于悬浮球和状态栏的控制
    public static boolean SettingsControlHoverballVisible = true;

    public static boolean SettingsControlStatusBarVisible = false;

    //3、定时器状态
    public static boolean TimeManagerRunning = false;//记录定时器是否在计时

    //4、悬浮球
    public static boolean Timing_begins_leftHoverballShow = true;

    public static boolean Timing_begins_rightHoverballShow = true;


    //5、导航栏
    public static boolean Timing_begins_leftNavibarShow = false;

    public static boolean Timing_begins_rightNavibarShow = false;

    //6、全局点击定时功能
    public static final String WINDOWMANAGER_TO_OSD = "windowmanager_to_osd";


    //非定时之外，主动触发消失动画的标志位
    public static int Proactive_triggering_lefthoverball_hide = 1;

    public static int Proactive_triggering_righthoverball_hide = 1;

    public static int Proactive_triggering_leftnavibar_hide = 1;

    public static int Proactive_triggering_rightnavibar_hide = 1;

}
