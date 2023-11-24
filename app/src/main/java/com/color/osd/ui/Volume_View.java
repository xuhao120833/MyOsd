package com.color.osd.ui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.color.osd.R;
import com.color.osd.models.Enum.MenuState;
import com.color.osd.models.FunctionBind;
import com.color.osd.models.interfaces.AbstractAutoClose;
import com.color.osd.models.interfaces.MenuBrightnessAndVolumeInterface;
import com.color.osd.models.service.MenuService;
import com.color.osd.ui.views.CltBrightnessAndVolumeView;
import com.color.osd.ui.views.CltTouchBarBaseView;
import com.color.osd.utils.ConstantProperties;
import com.color.osd.utils.DensityUtil;

public class Volume_View extends AbstractAutoClose implements MenuBrightnessAndVolumeInterface {
    private static final String TAG = "Volume_View";
    private Context mContext;
    public CltTouchBarBaseView source;
    public WindowManager.LayoutParams lp;
    private AudioManager audioManager;
    private int volume = 0;

    // 这个条件用来判断当前对象是否是由BrightnessAndVolume_View创建的，如果true：此对象的自动关闭行为由其控制
    private CltBrightnessAndVolumeView cltBrightnessAndVolumeView;

    public void setCltBrightnessAndVolumeView(CltBrightnessAndVolumeView cltBrightnessAndVolumeView) {
        this.cltBrightnessAndVolumeView = cltBrightnessAndVolumeView;
    }

    public Volume_View(Context context) {
        this.mContext = context;
        // 初始化当前类的时候先取出当前系统的声音
        if (audioManager == null) {
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        // volume = 10;
        Log.d("TAG", "Volume_View: volume=" + volume);
        initView();
        initLp();
        // addVolumeChangedReceiver();
    }

    public void initView(){
        source = new CltTouchBarBaseView(mContext);
        source.setParentView(this);   // 双向依赖
        source.setBackgroundResource(R.drawable.focus_background);     // 给设置一个聚焦状态下的背景
        source.baseValue = 15;   // 设置基底
        source.setProgress(volume);
        source.setSeekBarIconPositive(ContextCompat.getDrawable(mContext, R.drawable.volume_positive));
        source.setSeekBarIconNegative(ContextCompat.getDrawable(mContext, R.drawable.volume_negative));

        // 设置一个聚焦状态监听回调
        source.setOnFocusChangeListener((v, hasFocus) -> {
            Log.d(TAG, "setOnFocusChangeListener: " + hasFocus);
            if (cltBrightnessAndVolumeView == null){
                MenuService.menuState = MenuState.MENU_VOLUME;    // 声音被聚焦了（被选择了）
            }else{
                MenuService.menuState = MenuState.MENU_VOLUME_FOCUS;    // 声音被聚焦了（被选择了）
            }

        });
    }

    private void initLp() {
        lp = new WindowManager.LayoutParams();
        lp.width = (int) DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_OR_VOLUME_BACKGROUND_WIDTH_DP);
        lp.height = (int) DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_OR_VOLUME_BACKGROUND_HEIGHT_DP);
        lp.gravity = Gravity.TOP;
        lp.y = (int) DensityUtil.getScaledValue(ConstantProperties.BRIGHTNESS_AND_VOLUME_BACKGROUND_MARGIN_TOP_DP);
        lp.flags = WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        lp.format = PixelFormat.RGBA_8888;
    }

    public void updateVolume(int delta){
        volume = delta > 0 ? Math.min(15, volume + delta) : Math.max(0, volume + delta);  // 约束volume始终在[0, 15]之间
        Log.d("TAG", "updateVolume: " + volume);
        // 设置音量
        setSystemMusicVolume(volume);
        source.setProgress(volume);
        source.reRenderView();  // 刷新界面
    }


    @Override
    public void setProgressFromTouchEvent(int progress) {
        setSystemMusicVolume(progress);

        // 触摸进度条的时候，也要重置取消任务，否则人还在拖动呢，view给干没了
        reClose(cltBrightnessAndVolumeView == null ? source : cltBrightnessAndVolumeView);
    }

    @Override
    public void onKeyDownFromBaseView(boolean positive) {
        Log.d(TAG, "onKeyDownFromBaseView: here volume view event: " + (cltBrightnessAndVolumeView == null));
        Log.d(TAG, "reClose: onKeyDownFromBaseView 123 " + this + ", " + MenuService.menuState);
        if (positive) {
            updateVolume(1);
            reClose(cltBrightnessAndVolumeView == null ? source : cltBrightnessAndVolumeView);
        }else{
            updateVolume(-1);
            reClose(cltBrightnessAndVolumeView == null ? source : cltBrightnessAndVolumeView);
        }
    }

    @Override
    public void onKeyUpClose() {
        cancelAutoClose();
        FunctionBind.mavts.clearView(cltBrightnessAndVolumeView == null ? source : cltBrightnessAndVolumeView);
        MenuService.menuState = MenuState.NULL;   // 恢复状态
        MenuService.settingVolumeChange = false;
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
        // 按键小板直接调整音量的时候，也要重置取消任务
        if (cltBrightnessAndVolumeView == null){
            Log.d(TAG, "reClose: here 123 " + this + ", " + MenuService.menuState);
            reClose(source);
        }else{
            Log.d(TAG, "reClose: here 456 " + this + ", " + MenuService.menuState);
            reClose(cltBrightnessAndVolumeView);
        }

        // TODO： 先判断音量/亮度窗口是否显示，再根据不同情况进行处理。目前先默认在窗口显示时进行调音量，【之后改一下】
        this.volume = volume;
        source.setProgress(volume);
        source.reRenderView();
    }

    public void initSystemVolume(){
        if (audioManager != null){
            volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            source.baseValue = 15;   // 设置基底
            source.setProgress(volume);
        }
    }
}
