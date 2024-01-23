package com.color.notification.models;

import android.app.PendingIntent;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import com.color.notification.models.Notification_Center_Adapter.Center_ViewHolder;
import com.color.osd.R;

public class Notification_Item {

    public View mynotification_center = null;

    public String appName;

    public String content;

    //同一个APP 多条消息
    public ArrayList<String> multiple_content = new ArrayList<>();

    //同一个APP 多条消息对应的链接
    public ArrayList<PendingIntent> multiple_Intent = new ArrayList<>();

//    public HashMap<String, PendingIntent> multiple_content = new HashMap<>();

    public String time;

    public Drawable Icon;

    public PendingIntent pendingIntent;


    //同一个APP 多条消息 标志位
    public boolean isMultiple_Messages = false;

    //同一个APP 折叠消息的数量
    public int number = 0;

    //是否已经展开
    public boolean isExpand;

    //已记录多条通知，处于折叠态Item对应的Holder
    public Center_ViewHolder parent_ViewHolder = null;

    public Notification_Item parent_notification_item = null;

    //自己在父通知multiple_content中的展开位置，从0开始计数，数字为0表示在第一个位置
    public int subpostion;

//    public View notification_center_item ;
//
//    public View  notification_center_item_content;
//
//    public View icon;

    //蓝牙进度条progress，初始值为-1
    public int lanya_progress = -1;

}