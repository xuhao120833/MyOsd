package com.color.osd.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

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
        if (VOLUME_CHANGE_ACTION.equals(intent.getAction())
                && intent.getIntExtra(EXTRA_VOLUME_STREAM_TYPE, -1) == AudioManager.STREAM_MUSIC
                && !MenuService.settingVolumeChange) {
            // 调节媒体音量
            changeVolume(context, AudioManager.STREAM_MUSIC);
        }
    }

    private void changeVolume(Context context, int volumeType) {
        AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        int volume;
        if (audioManager != null) {
            volume = audioManager.getStreamVolume(volumeType);
            // 把当前音量改变情况传递给MenuService中  不要在广播中操作任何MenuServer相关的对象
            // 之前的写法全都是在这里操作FunctionBind的静态View对象
            // 把View这种带context的对象给弄成静态的，容易造成内存泄露，而且AS中也飘黄了，说明不建议把View的对象弄成静态的
            volumeChangeListener.onVolumeChange(volume);


            Log.d(TAG, "changeVolume: volType=" + volumeType + ", volume=" + volume + ", " + MenuService.menuState);

        }
    }

}
