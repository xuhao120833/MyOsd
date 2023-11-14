package com.color.osd.ui;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.color.osd.R;
import com.color.osd.broadcast.VolumeChangeReceiver;
import com.color.osd.models.interfaces.AbstractMenuBrightnessAndVolume;
import com.color.osd.models.interfaces.VolumeChangeListener;
import com.color.osd.ui.views.CltTouchBarBaseView;

public class Volume_View extends AbstractMenuBrightnessAndVolume {
    private static final String TAG = "Volume_View";
    private Context mContext;
    public CltTouchBarBaseView source;
    public WindowManager.LayoutParams lp;
    private AudioManager audioManager;
    VolumeChangeReceiver volumeChangeReceiver;
    private int volume = 0;

    public Volume_View(Context context) {
        this.mContext = context;
        // 初始化当前类的时候先取出当前系统的声音
        // volume = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        if (audioManager == null) {
            audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        }
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        // volume = 10;
        Log.d("TAG", "Volume_View: volume=" + volume);
        initView();
        initLp();
        addVolumeChangedReceiver();
    }

    public void initView(){
        source = new CltTouchBarBaseView(mContext);
        source.setParentView(this);   // 双向依赖
        source.setProgress(volume);
        source.setSeekBarIconPositive(ContextCompat.getDrawable(mContext, R.drawable.volume_positive));
        source.setSeekBarIconNegative(ContextCompat.getDrawable(mContext, R.drawable.volume_negative));
        source.baseValue = 15;   // 设置基底
    }

    private void initLp() {
        lp = new WindowManager.LayoutParams();
        lp.width = 303;
        lp.height = 70;
        lp.gravity = Gravity.TOP;
        lp.y = 32;
        lp.flags = WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        lp.format = PixelFormat.RGBA_8888;
    }

    public void updateVolume(int delta){
        volume = delta > 0 ? Math.min(15, volume + delta) : Math.max(0, volume + delta);
        Log.d("TAG", "updateVolume: " + volume);
        // 设置音量
        setSystemMusicVolume(volume);
        source.setProgress(volume);
        source.reRenderView();  // 刷新界面
    }


    @Override
    public void setProgressFromTouchEvent(int progress) {
        setSystemMusicVolume(progress);
    }

    // 设置音量
    public void setSystemMusicVolume(int volume) {
        if (audioManager != null) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
        } else {
            // 不应出现的情况
            Log.w(TAG, "setSystemMusicVolume: audioManager=null");
        }
    }

    public void onVolumeChanged(int volume) {
        // TODO： 先判断音量/亮度窗口是否显示，再根据不同情况进行处理。目前先默认在窗口显示时进行调音量，【之后改一下】
        source.setProgress(volume);
        source.reRenderView();
    }

    // 暂时先放这边，进行一个测试使用
    private void addVolumeChangedReceiver() {
        if (volumeChangeReceiver == null) {
            volumeChangeReceiver = new VolumeChangeReceiver();
        }
        //volumeChangeReceiver.setVolumeChangeListener();
        volumeChangeReceiver.setVolumeChangeListener(new VolumeChangeListener() {
            @Override
            public void onVolumeChange(int volume) {
                onVolumeChanged(volume);
            }
        });
        IntentFilter volumeChangeFilter = new IntentFilter();
        volumeChangeFilter.addAction(volumeChangeReceiver.VOLUME_CHANGE_ACTION);
        mContext.registerReceiver(volumeChangeReceiver, volumeChangeFilter);
    }
}
