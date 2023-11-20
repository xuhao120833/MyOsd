package com.color.osd.ContentObserver;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;

import com.color.osd.models.service.MenuService;

public class BootFinishContentObserver extends ContentObserver {

    Context mcontext;
    int fswitch;

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
            MenuService.initcomplete = true;
        }
    }
}
