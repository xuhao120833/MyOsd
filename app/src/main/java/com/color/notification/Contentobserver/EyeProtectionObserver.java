package com.color.notification.Contentobserver;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;

import com.color.osd.R;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.StaticVariableUtils;

public class EyeProtectionObserver extends ContentObserver implements Instance{

    Context mycontext;

    int fswitch;

    public EyeProtectionObserver() {
        super(new Handler());
    }

    public void setContext(Context context) {

        mycontext = context;

    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        fswitch = Settings.Global.getInt(mycontext.getContentResolver(),
                StaticVariableUtils.EYE_PROTECT_MODE, 5);

        if(fswitch == 1) {
            STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.eye_protection.setImageResource(R.drawable.quick_settings_eye_protection_on);
            StaticVariableUtils.eye_protection_state = "on";
        }

        if(fswitch == 0) {
            STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.eye_protection.setImageResource(R.drawable.quick_settings_eye_protection);
            StaticVariableUtils.eye_protection_state = "off";
        }
    }

}
