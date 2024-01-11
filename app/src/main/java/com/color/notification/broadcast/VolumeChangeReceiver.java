package com.color.notification.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.SeekBar;

import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.StaticVariableUtils;

public class VolumeChangeReceiver extends BroadcastReceiver implements Instance {

    Context mycontext;

    public VolumeChangeReceiver() {

    }

    public void setContext(Context context) {
        mycontext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
            // 获取当前音量
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

            Log.d("myVolumeChangeReceiver"," 音量发生变化");
            // 更新SeekBar的进度
            STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.volumeSeekBar.setProgress(currentVolume);
            STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.volumeSeekBar_text.setText((int)(((float) currentVolume / 15) * 100) + "%");
        }
    }
}
