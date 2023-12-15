package com.color.systemui.Contentobserver;

import android.content.Context;
import java.lang.String;

import com.color.systemui.utils.StaticInstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import android.content.Intent;
import android.database.ContentObserver;
import android.provider.Settings;
import android.util.Log;
import android.os.Handler;


public class ResolutionChangeObserver extends ContentObserver {

    public final String SYSTEM_RESOLUTION_CHANGE = "system_resolution_change";

    int fswitch = 0;
    Context mycontext;

    static Intent intent = new Intent();


    public ResolutionChangeObserver(Context mContext) {
        super(new Handler());
        mycontext = mContext;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        fswitch = Settings.System.getInt(mycontext.getContentResolver(),
                SYSTEM_RESOLUTION_CHANGE, 960516);
        Log.d("ResolutionChangeObserver", " 收到分辨率变化 " + String.valueOf(fswitch));

        //1、更新悬浮球的位置
        StaticInstanceUtils.hoverball.lp.y = StaticInstanceUtils.mcalculateYposition.GetHoverballY();
        Log.d("ResolutionChangeObserver", " hoverball " + String.valueOf(StaticInstanceUtils.mcalculateYposition.GetHoverballY()));
        StaticInstanceUtils.hoverball.lp2.y = StaticInstanceUtils.mcalculateYposition.GetHoverballY();
        StaticInstanceUtils.mavts.wm.updateViewLayout(StaticInstanceUtils.hoverball.leftlayout, StaticInstanceUtils.hoverball.lp);
        StaticInstanceUtils.mavts.wm.updateViewLayout(StaticInstanceUtils.hoverball.rightlayout, StaticInstanceUtils.hoverball.lp2);

        //2、更新导航栏的位置
        StaticInstanceUtils.navigationBar.lp.y = StaticInstanceUtils.mcalculateYposition.GetNavibarY();
        StaticInstanceUtils.navigationBar.lp2.y = StaticInstanceUtils.mcalculateYposition.GetNavibarY();
        if (StaticVariableUtils.Timing_begins_leftNavibarShow == true) {
            StaticInstanceUtils.mavts.wm.updateViewLayout(StaticInstanceUtils.navigationBar.leftNavibar, StaticInstanceUtils.navigationBar.lp);
        }
        if (StaticVariableUtils.Timing_begins_rightNavibarShow == true) {
            StaticInstanceUtils.mavts.wm.updateViewLayout(StaticInstanceUtils.navigationBar.rightNavibar, StaticInstanceUtils.navigationBar.lp2);
        }

        //3、更新悬浮球移动上下限
        StaticInstanceUtils.hoverball.Top = StaticInstanceUtils.mcalculateYposition.GetHoverballTop();
        StaticInstanceUtils.hoverball.Bottom = StaticInstanceUtils.mcalculateYposition.GetHoverballBottom();

    }


}