package com.color.osd.broadcast;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.color.osd.models.service.MenuService;

public class BootCompleteReceiver extends BroadcastReceiver {

    private final static String TAG = "MyBootCompleteReceiver";
    private static final boolean DBG = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DBG)
            Log.d(TAG, "收到广播....");
        StartMyAccessibilityService(context);

    }

    private void StartMyAccessibilityService(Context context) {

        boolean accessibilitySettingsOn = isAccessibilitySettingsOn(context, MenuService.class);

        Log.d(TAG, "isAccessibilitySettingsOn: " + accessibilitySettingsOn);
        if (!accessibilitySettingsOn) {
            String enabledServicesSetting = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

            Log.d(TAG, "package name: " + context.getPackageName());

            ComponentName selfComponentName = new ComponentName(context.getPackageName(), MenuService.class.getCanonicalName());
            String flattenToString = selfComponentName.flattenToString();

            Log.d(TAG, "flattenToString: " + flattenToString);
            //null 表示没有任何服务
            if (enabledServicesSetting == null) {
                enabledServicesSetting = flattenToString;
            } else if (!enabledServicesSetting.contains(flattenToString)) {
                enabledServicesSetting = enabledServicesSetting + ":" + flattenToString;
            }

            Log.d(TAG, "enabledServicesSetting: " + enabledServicesSetting);

            Settings.Secure.putString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, enabledServicesSetting);
            Settings.Secure.putInt(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 1);
        }
    }


    public static boolean isAccessibilitySettingsOn(Context mContext, Class<? extends AccessibilityService> clazz) {
        int accessibilityEnabled = 0;
        final String service = mContext.getPackageName() + "/" + clazz.getCanonicalName();

        Log.d(TAG, "service: " + service);
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        TextUtils.SimpleStringSplitter mStringSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringSplitter.setString(settingValue);
                while (mStringSplitter.hasNext()) {
                    String accessibilityService = mStringSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}