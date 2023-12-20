package com.color.systemui.time;

import android.content.Context;
import android.os.Handler;
import android.provider.Settings;

import com.color.systemui.utils.StaticVariableUtils;

public class TimeManager {

    private Context mycontext;

    private Handler handler = new Handler();

    private Runnable runnable;

    private  final long Timing_Duration = 5000;

    public TimeManager() {

    }

    public void setContext(Context context) {
        mycontext = context;

        initRunnable();
    }

    private void initRunnable() {
        runnable = new Runnable() {
            @Override
            public void run() {
                //隐藏逻辑
                Settings.System.putInt(mycontext.getContentResolver(), StaticVariableUtils.WINDOWMANAGER_TO_OSD, 0);
            }

        };
    }

    // 开始计时
    public void Time_handler_postDelayed() {
            if (!handler.hasCallbacks(runnable)) {
                handler.postDelayed(runnable, Timing_Duration);
            }
    }

    // removeCallbacks 可以当暂停计时来用
    public void Time_handler_removeCallbacks() {
        if (handler.hasCallbacks(runnable)) {
            handler.removeCallbacks(runnable);
        }
    }


}
