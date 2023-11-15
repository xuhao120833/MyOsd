package com.color.osd.ui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.color.osd.ui.views.CltBrightnessAndVolumeView;
import com.color.osd.utils.ConstantProperties;

/**
 * 亮度和声音的复合界面
 * 按照一体机的OSD项目设计稿，存在亮度和声音同时设置的情况
 * 比如在亮度调整的二级菜单，直接遥控器的音量加减，此时二级的亮度界面还未消失，这时就要弹出这个复合界面
 * 或者返过来，先点击音量加减，然后又进入OSD项目调整亮度二级菜单，这时也要弹出这个复合界面
 */
public class BrightnessAndVolume_View {
    private static final String TAG = "CltBrightnessAndVolumeView";
    private Context mContext;
    public CltBrightnessAndVolumeView source;
    public WindowManager.LayoutParams lp;
    private int brightness = 0;
    private int volume = 0;

    private Brightness_View brightness_view;
    private Volume_View volume_view;

    public BrightnessAndVolume_View(Context context){
        mContext = context;

        initView();
        initLp();
    }

    public void initView(){
        Log.d(TAG, "initView: here");

        // 初始化两个controller对象
        // 当前的BrightnessAndVolume_View对象持有这两个对象
        brightness_view = new Brightness_View(mContext);
        volume_view = new Volume_View(mContext);


        source = new CltBrightnessAndVolumeView(mContext);   // 初始化本身对应的背景View
        source.setTouchBarBaseView(brightness_view.source, volume_view.source);

    }

    public void initLp(){
        lp = new WindowManager.LayoutParams();
        lp.width = ConstantProperties.BRIGHTNESS_AND_VOLUME_BACKGROUND_WIDTH_DP;
        lp.height = ConstantProperties.BRIGHTNESS_AND_VOLUME_BACKGROUND_HEIGHT_DP;
        lp.gravity = Gravity.TOP;
        lp.y = ConstantProperties.BRIGHTNESS_AND_VOLUME_BACKGROUND_MARGIN_TOP_DP;
        lp.flags = WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        lp.format = PixelFormat.RGBA_8888;
    }

    public void updateBrightness(int delta){
        brightness_view.updateBrightness(delta);
    }

    public void updateVolume(int delta){
        volume_view.updateVolume(delta);
    }

}
