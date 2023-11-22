package com.color.osd.ContentObserver;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;

import com.color.osd.models.interfaces.BrightnessChangeListener;

public class BrightnessObeserver extends ContentObserver {
    private Context mContext;
    private int brightness;
    private BrightnessChangeListener brightnessChangeListener;

    public BrightnessObeserver(Context mContext) {
        super(new Handler());
        this.mContext = mContext;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        try {
            brightness = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            throw new RuntimeException(e);
        }

        brightnessChangeListener.onBrightnessChange(brightness);
    }

    public void register() {
        Uri brightnessUri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        mContext.getContentResolver().registerContentObserver(brightnessUri, true, this);
    }

    public void unregister() {
        mContext.getContentResolver().unregisterContentObserver(this);
    }

    public void setBrightnessChangeListener(BrightnessChangeListener brightnessChangeListener) {
        this.brightnessChangeListener = brightnessChangeListener;
    }
}
