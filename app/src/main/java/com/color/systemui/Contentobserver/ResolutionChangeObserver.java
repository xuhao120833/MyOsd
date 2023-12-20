package com.color.systemui.Contentobserver;

import android.content.Context;
import java.lang.String;

import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import android.content.Intent;
import android.database.ContentObserver;
import android.provider.Settings;
import android.os.Handler;


public class ResolutionChangeObserver extends ContentObserver implements Instance {

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
        //Log.d("ResolutionChangeObserver", " 收到分辨率变化 " + String.valueOf(fswitch));

        //1、更新悬浮球的位置
        STATIC_INSTANCE_UTILS.hoverball.lp.y = STATIC_INSTANCE_UTILS.mcalculateYposition.GetHoverballY();
        //Log.d("ResolutionChangeObserver", " hoverball " + String.valueOf(InstanceUtils.mcalculateYposition.GetHoverballY()));
        STATIC_INSTANCE_UTILS.hoverball.lp2.y = STATIC_INSTANCE_UTILS.mcalculateYposition.GetHoverballY();
        STATIC_INSTANCE_UTILS.mavts.wm.updateViewLayout(STATIC_INSTANCE_UTILS.hoverball.leftlayout, STATIC_INSTANCE_UTILS.hoverball.lp);
        STATIC_INSTANCE_UTILS.mavts.wm.updateViewLayout(STATIC_INSTANCE_UTILS.hoverball.rightlayout, STATIC_INSTANCE_UTILS.hoverball.lp2);

        //2、更新导航栏的位置
        STATIC_INSTANCE_UTILS.navigationBar.lp.y = STATIC_INSTANCE_UTILS.mcalculateYposition.GetNavibarY();
        STATIC_INSTANCE_UTILS.navigationBar.lp2.y = STATIC_INSTANCE_UTILS.mcalculateYposition.GetNavibarY();
        if (StaticVariableUtils.Timing_begins_leftNavibarShow == true) {
            STATIC_INSTANCE_UTILS.mavts.wm.updateViewLayout(STATIC_INSTANCE_UTILS.navigationBar.leftNavibar, STATIC_INSTANCE_UTILS.navigationBar.lp);
        }
        if (StaticVariableUtils.Timing_begins_rightNavibarShow == true) {
            STATIC_INSTANCE_UTILS.mavts.wm.updateViewLayout(STATIC_INSTANCE_UTILS.navigationBar.rightNavibar, STATIC_INSTANCE_UTILS.navigationBar.lp2);
        }

        //3、更新悬浮球移动上下限
        STATIC_INSTANCE_UTILS.hoverball.Top = STATIC_INSTANCE_UTILS.mcalculateYposition.GetHoverballTop();
        STATIC_INSTANCE_UTILS.hoverball.Bottom = STATIC_INSTANCE_UTILS.mcalculateYposition.GetHoverballBottom();

    }


}