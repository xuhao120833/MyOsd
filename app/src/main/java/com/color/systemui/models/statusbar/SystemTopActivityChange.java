package com.color.systemui.models.statusbar;

import android.app.ActionBar;
import android.app.IProcessObserver;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.LayoutInflater;
import com.color.osd.R;
import com.color.systemui.utils.StaticInstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import android.graphics.PixelFormat;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.content.Intent;
import android.content.ContextWrapper;
import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.provider.Settings;
import android.util.Log;
import android.app.ActivityManager;
//import android.app.IActivityManager;
//import android.app.IProcessObserver;
//import android.app.TaskStackListener;
import android.annotation.SuppressLint;
import android.os.HandlerThread;
import android.app.Instrumentation;

import java.lang.reflect.Method;
import java.util.List;
import android.app.Activity;
import android.app.ActivityManager;
import android.os.RemoteException;
import android.graphics.Point;
import android.view.WindowManager;

public class SystemTopActivityChange {

    private Context mycontext;

    static Object IActivityManager;

    WindowManager windowManager;

    List<ActivityManager.RunningAppProcessInfo> processesList;

    ActivityManager.RunningAppProcessInfo runningAppProcessInfo;

    String processName = "";

    public ProcessObserver processObserver;

    public SystemTopActivityChange() {

    }

    public void setContext(Context context) throws NoSuchMethodException {

        mycontext = context;

        windowManager = (WindowManager) mycontext.getSystemService(Context.WINDOW_SERVICE);

        processObserver = new ProcessObserver();

        try {
            IActivityManager = getIActivityManager();

            // 获取registerProcessObserver方法
            Method registerProcessObserverMethod = IActivityManager.getClass()
                    .getMethod("registerProcessObserver", IProcessObserver.class);

            // 调用registerProcessObserver方法
            registerProcessObserverMethod.invoke(IActivityManager, processObserver);
        } catch (Exception e) {
            //Log.d("xuLL", "cannot register activity monitoring", e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    private Object getIActivityManager() {
        try {
            // 获取ActivityManagerNative类
            Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");

            // 获取getDefault方法
            Method getDefaultMethod = activityManagerNativeClass.getMethod("getDefault");

            // 调用getDefault方法，获取IActivityManager对象
            Object IActivityManager = getDefaultMethod.invoke(null);

            return IActivityManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @SuppressLint("DefaultLocale")
    private class ProcessObserver extends IProcessObserver.Stub {
        @Override
        public void onForegroundActivitiesChanged(int pid, int uid, boolean foregroundActivities) {

            try {
                processesList = getRunningAppProcesses();
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < processesList.size(); i++) {
                try {
                    runningAppProcessInfo = processesList.get(i);
                    if (runningAppProcessInfo.pid == pid) {
                        processName = runningAppProcessInfo.processName;
                        //Log.d("xuLL", "onForegroundActivitiesChanged " + processName + " ");
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //Log.d("SystemActivityChange : 前台活动的包名: ", processName);
            if ((!"com.android.launcher3".equals(processName) && foregroundActivities == true && !"com.color.settings".equals(processName) && !"com.android.tv.settings".equals(processName) && !"com.peasun.aispeechgl".equals(processName) && !"com.color.osd".equals(processName) && !"android.rockchip.update.service".equals(processName)) || ("com.android.launcher3".equals(processName) && foregroundActivities == false) ) {
                StaticInstanceUtils.statusBar.udisk.post(new Runnable() {
                    @Override
                    public void run() {
//                        if(StaticVariableUtils.haveUsbDevice) {
//                            StaticInstanceUtils.statusBar.udisk.setVisibility(View.GONE);
//                        }
//                        if (StaticVariableUtils.WifiOpen) {
//                            //Log.d("SystemActivityChange "," wifi设置不可见");
//                            StaticInstanceUtils.statusBar.wifi.setVisibility(View.GONE);
//                        }
//                        if (StaticVariableUtils.EthernetConnected) {
//                            StaticInstanceUtils.statusBar.ethernet.setVisibility(View.GONE);
//                        }
//                        if (StaticVariableUtils.HotspotOpen) {
//                            StaticInstanceUtils.statusBar.hotspot.setVisibility(View.GONE);
//                        }
                        StaticInstanceUtils.statusBar.statusbar.setVisibility(View.GONE);
                    }
                });
                //Log.d("xuLL", "不在launcher 选择隐藏");
            } else if ("com.android.launcher3".equals(processName) && foregroundActivities == true && StaticVariableUtils.SettingsControlStatusBarVisible ) {
                StaticInstanceUtils.statusBar.udisk.post(new Runnable() {
                    @Override
                    public void run() {
                        StaticInstanceUtils.statusBar.statusbar.setVisibility(View.VISIBLE);
                        if(StaticVariableUtils.haveUsbDevice) {
                            StaticInstanceUtils.statusBar.udisk.setVisibility(View.VISIBLE);
                        }
                        if (StaticVariableUtils.WifiOpen) {
                            StaticInstanceUtils.statusBar.wifi.setVisibility(View.VISIBLE);
                        }
                        if (StaticVariableUtils.EthernetConnected) {
                            StaticInstanceUtils.statusBar.ethernet.setVisibility(View.VISIBLE);
                        }
                        if (StaticVariableUtils.HotspotOpen) {
                            StaticInstanceUtils.statusBar.hotspot.setVisibility(View.VISIBLE);
                        }
                    }
                });
                //Log.d("xuLL", "在launcher 显示导航栏");
            }
            // Log.d("xuLL:", " ");
            // Log.d("xuLL:", " ");
            // Log.d("xuLL:", " ");
            // Log.d("xuLL:", " ");
        }

        @Override
        public void onForegroundServicesChanged(int pid, int uid, int fgServiceTypes) {
            //Log.d("xuLL", String.format("onForegroundServicesChanged uid %d pid %d %d", uid, pid, fgServiceTypes));
        }

        @Override
        public void onProcessDied(int pid, int uid) {
            /*
             应用进程死掉才会出发，应用闪退或者被系统杀死都会触发
            */
            //Log.d("xuLL", String.format("onProcessDied uid %d pid %d", uid, pid));
//            mHandler.requestProcessDied(pid, uid);
        }
    }

    public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() {
        try {

            // 获取getRunningAppProcesses方法
            Method getRunningAppProcessesMethod = IActivityManager.getClass().getMethod("getRunningAppProcesses");

            // 调用getRunningAppProcesses方法
            return (List<ActivityManager.RunningAppProcessInfo>) getRunningAppProcessesMethod.invoke(IActivityManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
