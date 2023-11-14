package com.color.osd.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

import com.color.osd.models.interfaces.VolumeChangeListener;

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

            if (volumeChangeListener != null) {
                volumeChangeListener.onVolumeChange(volume);
            }
        }
    }

}
