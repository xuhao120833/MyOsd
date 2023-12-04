package com.color.osd.ContentObserver;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.color.osd.models.service.MenuService;

public class WindowManagerServiceObserver extends ContentObserver {

    public  static Context mcontext;
    public static int fswitch;

    private final String WINDOW_MANAGER_TO_OSD = "windowmanager_osd_to_osd";

    public WindowManagerServiceObserver(Context mContext) {
        super(new Handler());
        mcontext = mContext;
    }


    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);



        //Settings.System.putInt(mcontext.getContentResolver(),WINDOWMANAGER_OSD_TO_SYSTEMUI,1);

        fswitch = Settings.System.getInt(mcontext.getContentResolver(),
                "window_manager_to_osd", 5);
//        if(fswitch == 1) {
//            MenuService.initcomplete = true;
//        }

    }
}
