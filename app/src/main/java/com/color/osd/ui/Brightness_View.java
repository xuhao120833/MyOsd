package com.color.osd.ui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.color.osd.R;
import com.color.osd.models.Enum.MenuState;
import com.color.osd.models.interfaces.AbstractAutoClose;
import com.color.osd.models.interfaces.MenuBrightnessAndVolumeInterface;
import com.color.osd.models.service.MenuService;
import com.color.osd.ui.views.CltBrightnessAndVolumeView;
import com.color.osd.ui.views.CltTouchBarBaseView;
import com.color.osd.utils.ConstantProperties;

public class Brightness_View extends AbstractAutoClose implements MenuBrightnessAndVolumeInterface {
    private static final String TAG = Brightness_View.class.getSimpleName();
    private Context mContext;
    public CltTouchBarBaseView source;
    public WindowManager.LayoutParams lp;
    private int brightness = 0;

    // 这个对象用来判断当前对象是否是由BrightnessAndVolume_View创建的，如果true：此对象的自动关闭行为由其控制
    private CltBrightnessAndVolumeView cltBrightnessAndVolumeView;

    public void setCltBrightnessAndVolumeView(CltBrightnessAndVolumeView cltBrightnessAndVolumeView) {
        this.cltBrightnessAndVolumeView = cltBrightnessAndVolumeView;
    }

    public Brightness_View(Context context) {
        this.mContext = context;
        try {
            // 初始化当前类的时候先取出当前系统的亮度
            brightness = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            throw new RuntimeException(e);
        }
        Log.d("TAG", "Brightness_View: " + brightness);
        initView();
        initLp();
    }

    public void initView(){
        source = new CltTouchBarBaseView(mContext);
        source.setParentView(this);   // 把当前对象赋值给BrightnessView 这样Brightness_View和BrightnessView双向依赖
        source.baseValue = 255;   // 设置基底
        source.setProgress(brightness);
        source.setSeekBarIconPositive(ContextCompat.getDrawable(mContext, R.drawable.white_brightness));
        source.setSeekBarIconNegative(ContextCompat.getDrawable(mContext, R.drawable.dark_brightness));
        source.setOnFocusChangeListener((v, hasFocus) -> {
            Log.d(TAG, "setOnFocusChangeListener: " + hasFocus);
            MenuService.menuState = MenuState.MENU_BRIGHTNESS_FOCUS;    // 亮度被聚焦了（被选择了）
        });
    }

    private void initLp() {
        lp = new WindowManager.LayoutParams();
        lp.width = ConstantProperties.BRIGHTNESS_OR_VOLUME_BACKGROUND_WIDTH_DP;
        lp.height = ConstantProperties.BRIGHTNESS_OR_VOLUME_BACKGROUND_HEIGHT_DP;
        lp.gravity = Gravity.TOP;
        lp.y = ConstantProperties.BRIGHTNESS_AND_VOLUME_BACKGROUND_MARGIN_TOP_DP;
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

    public void updateBrightness(int delta){
        brightness = delta > 0 ? Math.min(255, brightness + delta) : Math.max(0, brightness + delta);
        Log.d("TAG", "upBrightness: " + brightness);
        // 设置系统亮度
        Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
        source.setProgress(brightness);
        source.reRenderView();  // 刷新界面
    }

    @Override
    public void setProgressFromTouchEvent(int progress) {
        this.brightness = progress;
        Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);

        // 触摸进度条的时候，也要重置"自动关闭任务"，否则人还在拖动呢，view给干没了
        if (cltBrightnessAndVolumeView == null){
            reClose(source);
        }else{
            reClose(cltBrightnessAndVolumeView);
        }
    }

    public void initSystemBrightness(){
        try {
            // 初始化当前类的时候先取出当前系统的亮度
            brightness = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            source.baseValue = 255;   // 设置基底
            source.setProgress(brightness);
        } catch (Settings.SettingNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
