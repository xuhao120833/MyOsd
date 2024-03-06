package com.color.systemui.models.statusbar;

import android.app.Activity;
import android.app.IProcessObserver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.ArrayMap;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemTopActivityChange implements Instance {

    private Context mycontext;

    static Object IActivityManager;

    WindowManager windowManager;

    List<ActivityManager.RunningAppProcessInfo> processesList;

    ActivityManager.RunningAppProcessInfo runningAppProcessInfo;

    String processName = "";

    String packageName = null;

    public ProcessObserver processObserver;

    public boolean isHome;

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

            packageName = getForegroundActivity(mycontext);

            Log.d("SystemActivityChange", " packageName的名字 " + packageName);
            for (int i = 0; i < processesList.size(); i++) {
                try {
                    runningAppProcessInfo = processesList.get(i);
                    if (runningAppProcessInfo.pid == pid) {
                        processName = runningAppProcessInfo.processName;
                        Log.d("SystemActivityChange", "onForegroundActivitiesChanged " + processName + " "
                                + String.valueOf(foregroundActivities) + " packagename " + packageName);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Log.d("SystemActivityChange : 前台活动的包名: ", processName);
            if ((!"com.android.launcher3".equals(processName) && foregroundActivities == true && !"com.color.settings".equals(processName)
                    && !"com.android.tv.settings".equals(processName) && !"com.peasun.aispeechgl".equals(processName) && !"com.color.osd".equals(processName)
                    && !"com.android.toofifi".equals(processName) && !"android.rockchip.update.service".equals(processName))
                    || ("com.android.launcher3".equals(processName) && foregroundActivities == false)) {

                if (STATIC_INSTANCE_UTILS.statusBar.statusbar.getVisibility() == View.GONE) {
                    return;
                }
                STATIC_INSTANCE_UTILS.statusBar.statusbar.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("SystemActivityChange", "StaticVariableUtils.SettingsControlStatusBarVisible" + StaticVariableUtils.SettingsControlStatusBarVisible);
                        STATIC_INSTANCE_UTILS.manimationManager.statusBarHideAnimation();
                        isHome = false;

                    }
                });
                Log.d("SystemActivityChange", "不在launcher 选择隐藏");
            } else if (("com.android.launcher3".equals(processName) && foregroundActivities == true)
                    || ("com.color.player".equals(processName) && foregroundActivities == false)
                    || ("com.color.filemanager".equals(processName) && foregroundActivities == false)
                    || (!"com.android.launcher3".equals(processName) && foregroundActivities == false)) {

                if (STATIC_INSTANCE_UTILS.statusBar.statusbar.getVisibility() == View.VISIBLE) {
                    return;
                }
                STATIC_INSTANCE_UTILS.statusBar.statusbar.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("SystemActivityChange", "StaticVariableUtils.SettingsControlStatusBarVisible" + StaticVariableUtils.SettingsControlStatusBarVisible);
                        isHome = isHome(mycontext);
                        if (StaticVariableUtils.SettingsControlStatusBarVisible && isHome == true) {
//                        STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.VISIBLE);
                            if (StaticVariableUtils.haveUsbDevice) {
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

    public void getPackageName() {

        try {
            Log.d("SystemActivityChange", "getPackageNameXXX 进入方法 " + packageName);

            ProcessBuilder builder = new ProcessBuilder("sh", "-c", "dumpsys window | grep mCurrentFocus");
            builder.redirectErrorStream(true);
            Process process = builder.start();

            // 获取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream())); // 新增的错误流读取器
            StringBuilder output = new StringBuilder();
            String line = reader.readLine();

            // 打印命令输出
            Log.d("SystemActivityChange", " 命令输出" + output.toString());

//            String lines = output.toString();

            // 定义正则表达式
            String regex = "([a-zA-Z]+\\.[a-zA-Z]+\\.[a-zA-Z]+)";

            // 创建 Pattern 对象
            Pattern pattern = Pattern.compile(regex);

            // 创建 Matcher 对象
            Matcher matcher = pattern.matcher(line);

            // 查找匹配
            if (matcher.find()) {
                // 提取匹配到的内容
                packageName = matcher.group();

                // 输出包名
                Log.d("SystemActivityChange", " 输出包名 " + packageName);
            } else {
                Log.d("SystemActivityChange", " 未找到包名 ");
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getForegroundActivity(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            // 获取正在运行的进程列表
            for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    // 返回前台活动的进程名称
                    return processInfo.processName;
                }
            }
        }
        return null;
    }

    public boolean isAppRunning(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            // 获取正在运行的进程列表
            for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
                // 检查进程是否属于指定的包名
                if (processInfo.processName.equals(packageName)) {
                    // 检查进程的重要性，判断应用程序是否在前台运行
                    return processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
                }
            }
        }
        return false;
    }


    public boolean isHome(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes(context).contains(rti.get(0).topActivity.getPackageName());
    }

    /**
     * 获得属于桌面的应用的应用包名称
     * * @return返回包含所有包名的字符串列表
     */
    public List getHomes(Context context) {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);
        for (ResolveInfo resolveInfo : resolveInfos) {
            names.add(resolveInfo.activityInfo.packageName);
        }
        return names;
    }

}
