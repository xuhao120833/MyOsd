package com.color.systemui.utils;

import androidx.recyclerview.widget.RecyclerView;

import com.color.notification.models.Notification_Item;

import java.util.ArrayList;
import java.util.List;

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



    //屏幕左右侧滑手势广播 过滤Action
    //左侧
    public static String onSwipeFromLeft_Action = "onSwipeFromLeft_Action";

    //右侧
    public static String onSwipeFromRight_Action = "onSwipeFromRight_Action";

    public static String leftSlide_Or_rightSlide = " ";



    //下面为快捷设置、消息中心使用变量
    public static final String EYE_PROTECT_MODE = "eye_protect_mode";

    public static String eye_protection_state = "off";

    public static String camera_rotate_degrees = "0"; //摄像头选装，默认值为0，取值：0 90 180 270

    public static RecyclerView recyclerView;

    //notification_center_adapter.notifyItemInserted(list.size() - 1);
    //注意，notifyItemInserted有个比较恶心的特性：插入的位置在list末尾会按照 onCreateViewHolder ——> onBindViewHolder 的顺序调用
    //插入的位置如果在list中间，则会直接调用onBindViewHolder
    //
    //由于上述这个恶心的特性，我需要一个标志位在调用nBindViewHolder时判断之前是否已经调用过onCreateViewHolder，如果没有那么一些初始化的工作就需要在
    //onBindViewHolder中重新去做。
    public static boolean onCreate_To_onBind =false;//false 调用nBindViewHolder没有调用过onCreateViewHolder

    //消息中心分辨率适配初始值
    public static final int original_width = 1920;

    public static final int original_height = 1080;

    public static int widthPixels = 1920;

    public static int heightPixels = 1080;

    public static final int original_dpi = 320;

    public static int density = 2;

    //判断消息中心是从左侧还是从右侧打开，init初始值，left左侧，right右侧
    public static String left_or_right = "init";

    //避免自定义View循环执行
    public static boolean shouldUpdateLayout = false;

    public static List<Notification_Item> list = new ArrayList<>();

    //判断是否正在进行蓝牙传输，添加进度条到通知栏
    public static String bluetooth_delivery = "off";//on表示正在传输,off表示否

    //蓝牙传输进度progress==0时，保证只往通知中心添加一条，有可能出现不止一次progress==0的情况
    public static boolean lanya_first_get_progress_0 = true;

    //整个系统蓝牙第一次启动开始传输的标志位
    //常规情况，蓝牙传输期间，包含android.text的广播和不包含的是交替发送给MyNotificationService，蓝牙是否传输完成是靠通知子项android.text 中提取的数字是否发生变化来判断，
    //因为接收到的广播并没有包含写明传输完成进度100%的内容。
    //但是有一种情况例外，整个系统首次使用蓝牙传输文件时，只有在最后才会有包含android.text的通知，上述的判断失效，所已针对这种特殊情况，只能创建变量特殊判断对待。
    public static boolean lanya_first_transmit = false;

}
