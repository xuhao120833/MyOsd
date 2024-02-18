package com.color.systemui.models.statusbar;

import android.app.IProcessObserver;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import android.app.ActivityManager;
//import android.app.IActivityManager;
//import android.app.IProcessObserver;
//import android.app.TaskStackListener;
import android.annotation.SuppressLint;

import java.lang.reflect.Method;
import java.util.List;

public class SystemTopActivityChange implements Instance {

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
                        Log.d("SystemActivityChange", "onForegroundActivitiesChanged " + processName + " " + String.valueOf(foregroundActivities));
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Log.d("SystemActivityChange : 前台活动的包名: ", processName);
            if ((!"com.android.launcher3".equals(processName) && foregroundActivities == true && !"com.color.settings".equals(processName) && !"com.android.tv.settings".equals(processName) && !"com.peasun.aispeechgl".equals(processName) && !"com.color.osd".equals(processName)&& !"com.android.toofifi".equals(processName) && !"android.rockchip.update.service".equals(processName)) || ("com.android.launcher3".equals(processName) && foregroundActivities == false) ) {
                STATIC_INSTANCE_UTILS.statusBar.statusbar.post(new Runnable() {
                    @Override
                    public void run() {
//                        if(StaticVariableUtils.haveUsbDevice) {
//                            InstanceUtils.statusBar.udisk.setVisibility(View.GONE);
//                        }
//                        if (StaticVariableUtils.WifiOpen) {
//                            //Log.d("SystemActivityChange "," wifi设置不可见");
//                            InstanceUtils.statusBar.wifi.setVisibility(View.GONE);
//                        }
//                        if (StaticVariableUtils.EthernetConnected) {
//                            InstanceUtils.statusBar.ethernet.setVisibility(View.GONE);
//                        }
//                        if (StaticVariableUtils.HotspotOpen) {
//                            InstanceUtils.statusBar.hotspot.setVisibility(View.GONE);
//                        }
//                        STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.GONE);
                        STATIC_INSTANCE_UTILS.manimationManager.statusBarHideAnimation();
                    }
                });
                Log.d("SystemActivityChange", "不在launcher 选择隐藏");
            } else if ("com.android.launcher3".equals(processName) && foregroundActivities == true && StaticVariableUtils.SettingsControlStatusBarVisible || ("com.color.player".equals(processName) && foregroundActivities == false && StaticVariableUtils.SettingsControlStatusBarVisible) || ("com.color.filemanager".equals(processName) && foregroundActivities == false && StaticVariableUtils.SettingsControlStatusBarVisible)) {
                STATIC_INSTANCE_UTILS.statusBar.statusbar.post(new Runnable() {
                    @Override
                    public void run() {
//                        STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.VISIBLE);
                        if(StaticVariableUtils.haveUsbDevice) {
                            STATIC_INSTANCE_UTILS.statusBar.udisk.setVisibility(View.VISIBLE);
                        }
                        if (StaticVariableUtils.WifiOpen) {
                            STATIC_INSTANCE_UTILS.statusBar.wifi.setVisibility(View.VISIBLE);
                        }
                        if (StaticVariableUtils.EthernetConnected) {
                            STATIC_INSTANCE_UTILS.statusBar.ethernet.setVisibility(View.VISIBLE);
                        }
                        if (StaticVariableUtils.HotspotOpen) {
                            STATIC_INSTANCE_UTILS.statusBar.hotspot.setVisibility(View.VISIBLE);
                        }
                        STATIC_INSTANCE_UTILS.manimationManager.statusbarShowAnimation();
                    }
                });
                Log.d("SystemActivityChange", "在launcher 显示导航栏");
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
