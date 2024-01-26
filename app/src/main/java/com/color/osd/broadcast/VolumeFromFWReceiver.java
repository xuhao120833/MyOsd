package com.color.osd.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.Log;

import com.color.osd.models.interfaces.VolumeChangeListener;
import com.color.osd.models.service.MenuService;
import com.color.osd.utils.ConstantProperties;

/**
 * 来自于framework中PhoneWindowManager响应KEYCODE_VOLUME_UP、KEYCODE_VOLUME_DOWN时发出的广播
 */
public class VolumeFromFWReceiver extends BroadcastReceiver {
    private static final String TAG = "VolumeFromFWReceiver";

    private VolumeChangeListener volumeChangeListener;

    public void setVolumeChangeListener(VolumeChangeListener volumeChangeListener) {
        this.volumeChangeListener = volumeChangeListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConstantProperties.VOLUME_CHANGE_ACTION.equals(intent.getAction())){
            int streamVolume = ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).getStreamVolume(AudioManager.STREAM_MUSIC);
            Log.d(TAG, "onReceive: here~ " + streamVolume);

            if (!MenuService.initcomplete) {
                // 如果在开机引导页面，则不走后续处理，不显示音量条（但由于不是监听拦截音量加减按键，其实音量还在变化）
                int inGuideFlag = Settings.Secure.getInt(context.getContentResolver(),
                        "tv_user_setup_complete", 5);
                if (inGuideFlag == 1) {
                    MenuService.initcomplete = true;
                } else {
                    return;
                }
            }

            if (MenuService.settingVolumeChange){
                // 说明settings应用在调整音量，osd这里就不要掺和了 正式版
                Log.d(TAG, "onReceive: here");
                volumeChangeListener.onVolumeChange(intent.getIntExtra("keyAction", 0),
                        intent.getIntExtra("keyCode", 0), true);
            }else{
                volumeChangeListener.onVolumeChange(intent.getIntExtra("keyAction", 0),
                        intent.getIntExtra("keyCode", 0), false);
            }

        }
    }
}
