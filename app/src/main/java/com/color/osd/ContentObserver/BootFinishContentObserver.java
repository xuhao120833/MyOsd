package com.color.osd.ContentObserver;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.color.notification.models.service.MyNotificationService;
import com.color.osd.models.service.MenuService;
import com.color.systemui.MySystemUI;
import com.color.systemui.interfaces.Instance;

public class BootFinishContentObserver extends ContentObserver implements Instance {

    Context mycontext;
    int fswitch;

    private MySystemUI mySystemUI;

//    private MyNotificationService myNotificationService = new MyNotificationService();
//
//    private Intent start_notification_service;

    public BootFinishContentObserver(Context mContext) {
        super(new Handler());
        mycontext = mContext;
    }


    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        fswitch = Settings.Secure.getInt(mycontext.getContentResolver(),
                "tv_user_setup_complete", 5);
        if(fswitch == 1) {

            //开机引导结束，启动ColorSystemUi
            mySystemUI = new MySystemUI(mycontext);
            mySystemUI.start();

            //启动消息通知服务
//            setInstance(myNotificationService);
//            start_notification_service = new Intent(mycontext, MyNotificationService.class);
//            mycontext.startService(start_notification_service);

            //Log.d("BootFinishContentObserver","开机引导完成");
            MenuService.initcomplete = true;

        }
    }
}
