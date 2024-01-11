package com.color.notification.models;

import android.app.PendingIntent;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.ArrayList;
import com.color.notification.models.Notification_Center_Adapter.Center_ViewHolder;

public class Notification_Item {

    public View mynotification_center = null;

    public String appName;

    public String content;

    //同一个APP 多条消息
    public ArrayList<String> multiple_content = new ArrayList<>();


    public String time;

    public Drawable Icon;

    public PendingIntent pendingIntent;


    //同一个APP 多条消息 标志位
    public boolean isMultiple_Messages = false;

    //重复消息的数量
    public int number = 1;

    //是否已经展开
    public boolean isExpand;

    //已记录多条通知，处于折叠态Item对应的Holder
    Center_ViewHolder parent_ViewHolder = null;

}