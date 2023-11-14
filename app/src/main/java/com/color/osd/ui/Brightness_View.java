package com.color.osd.ui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.color.osd.R;
import com.color.osd.models.interfaces.AbstractMenuBrightnessAndVolume;
import com.color.osd.ui.views.CltTouchBarBaseView;

public class Brightness_View extends AbstractMenuBrightnessAndVolume {
    private Context mContext;
    public CltTouchBarBaseView source;
    public WindowManager.LayoutParams lp;
    private int brightness = 0;

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
        source.setProgress(brightness);
        source.setSeekBarIconPositive(ContextCompat.getDrawable(mContext, R.drawable.white_brightness));
        source.setSeekBarIconNegative(ContextCompat.getDrawable(mContext, R.drawable.dark_brightness));
        source.baseValue = 255;   // 设置基底
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
    }

}
