package com.color.osd.models.interfaces;

public interface VolumeChangeListener {
    void onVolumeChange(int volume);
    void onVolumeChange(int keyAction, int keyCode, boolean settingChange);
}
