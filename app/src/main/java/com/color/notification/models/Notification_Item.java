package com.color.notification.models;

import android.app.PendingIntent;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.ArrayList;

public class Notification_Item {

    public View mynotification_center = null;

    public String appName;

    public String content;

    //同一个APP 多条消息
    //public ArrayList<String> content = new ArrayList<>();

    public String time;

    public Drawable Icon;

    public PendingIntent pendingIntent;


    //同一个APP 多条消息 标志位
    public boolean isMultiple_Messages = false;

    //重复消息的数量
    public int number = 1;

    //是否已经展开
    public boolean isExpand = false;

}