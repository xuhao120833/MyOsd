package com.color.osd.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

import com.color.osd.models.Enum.MenuState;
import com.color.osd.models.FunctionBind;
import com.color.osd.models.Menu_volume;
import com.color.osd.models.interfaces.VolumeChangeListener;
import com.color.osd.models.service.MenuService;

public class VolumeChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "VolumeChangeReceiver";

    public static final String VOLUME_CHANGE_ACTION = "android.media.VOLUME_CHANGED_ACTION";
    public static final String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";
    private VolumeChangeListener volumeChangeListener;

    public void setVolumeChangeListener(VolumeChangeListener volumeChangeListener) {
        this.volumeChangeListener = volumeChangeListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: intent");
        // 监听媒体音量的改变
        if (VOLUME_CHANGE_ACTION.equals(intent.getAction()) && intent.getIntExtra(EXTRA_VOLUME_STREAM_TYPE, -1) == AudioManager.STREAM_MUSIC) {
            // 调节媒体音量
            changeVolume(context, AudioManager.STREAM_MUSIC);
        }
    }

    private void changeVolume(Context context, int volumeType) {
        AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        int volume;
        if (audioManager != null) {
            volume = audioManager.getStreamVolume(volumeType);
            Log.d(TAG, "changeVolume: volType=" + volumeType + ", volume=" + volume);

//            ((Menu_volume) FunctionBind.Menu_volume).onVolumeChange();
            if (MenuService.menuState == MenuState.MENU_VOLUME) {
                // 音量调节窗口已经显示
                FunctionBind.menu_volume.onVolumeChanged(volume);
            } else if (MenuService.menuState == MenuState.MENU_BRIGHTNESS) {
                // TODO: 亮度和音量一起显示。暂时只显示亮度
                FunctionBind.Menu_volume.performClick();
            } else {
                // 显示亮度窗口
                FunctionBind.Menu_volume.performClick();
            }

        }
    }

}
