package com.color.osd.ContentObserver;

import android.content.Context;
import android.database.ContentObserver;
import android.provider.Settings;
import android.util.Log;
import android.os.Handler;

import com.color.osd.models.service.MenuService;
import com.color.osd.ui.DialogMenu;

public class ColorSystemUiContentOberver extends ContentObserver {
    private static final String OSD_OPEN_OTHER_SOURCE = "osd_open_other_source";
    int fswitch = 0;
    Context mcontext;




    public ColorSystemUiContentOberver( Context mContext) {
        super(new Handler());
        mcontext = mContext;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        fswitch = Settings.System.getInt(mcontext.getContentResolver(),
                OSD_OPEN_OTHER_SOURCE, 1);
        Log.d("ColorSystemUiContentOberver ", String.valueOf(fswitch));

        if (fswitch == 2) {
            DialogMenu.mydialog.show();//展示Osd 菜单
            MenuService.menuOn = true;
        }

    }

}