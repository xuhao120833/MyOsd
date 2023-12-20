package com.color.notification;

import android.content.Context;

public class NotificationCenter {

    private Context mycontext;

    public NotificationCenter() {

    }

    public void setContext(Context context) {
        mycontext = context;

        //1、初始化LayoutParams描述工具
        //initLayoutParams();

        //2、初始化View对象
        //initView();

        //3、给View添加Click、Touch事件监听
        //setClickAndTouch();
    }

}
