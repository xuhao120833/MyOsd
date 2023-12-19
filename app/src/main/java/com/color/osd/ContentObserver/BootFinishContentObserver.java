package com.color.osd.ContentObserver;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.color.notification.models.service.MyNotificationService;
import com.color.osd.models.service.MenuService;
import com.color.systemui.MySystemUI;

public class BootFinishContentObserver extends ContentObserver {

    Context mcontext;
    int fswitch;

    private MySystemUI mySystemUI;

    private MyNotificationService myNotificationService = new MyNotificationService();

    public BootFinishContentObserver(Context mContext) {
        super(new Handler());
        mcontext = mContext;
    }


    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        fswitch = Settings.Secure.getInt(mcontext.getContentResolver(),
                "tv_user_setup_complete", 5);
        if(fswitch == 1) {

            //开机引导结束，启动ColorSystemUi
            mySystemUI = new MySystemUI(mcontext);
            mySystemUI.start();

            Log.d("BootFinishContentObserver","开机引导完成");
            MenuService.initcomplete = true;

        }
    }
}
