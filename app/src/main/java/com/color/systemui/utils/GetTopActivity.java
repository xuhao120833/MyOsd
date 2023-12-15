package com.color.systemui.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

public class GetTopActivity {
    Context mycontext;
    ActivityManager activityManager;
    ComponentName componentName;

    public GetTopActivity() {
    }

    public void setContext(Context context) {
        mycontext = context;
        activityManager = (ActivityManager) mycontext.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public String getActivity() {
        componentName = activityManager.getRunningTasks(1).get(0).topActivity;
        //Log.d("GetTopActivity 当前Activity ",now.getClassName());
        return componentName.getClassName();
    }

    public String getPackage() {
        componentName = activityManager.getRunningTasks(1).get(0).topActivity;
        //Log.d("GetTopActivity 当前包名 ",now.getPackageName());
        return componentName.getPackageName();
    }


}