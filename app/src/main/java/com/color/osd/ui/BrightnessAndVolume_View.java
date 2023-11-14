package com.color.osd.ui;

import android.content.Context;
import android.view.WindowManager;

import com.color.osd.models.interfaces.AbstractMenuBrightnessAndVolume;
import com.color.osd.ui.views.CltBrightnessAndVolumeView;

/**
 * 亮度和声音的复合界面
 * 按照一体机的OSD项目设计稿，存在亮度和声音同时设置的情况
 * 比如在亮度调整的二级菜单，直接遥控器的音量加减，此时二级的亮度界面还未消失，这时就要弹出这个复合界面
 * 或者返过来，先点击音量加减，然后又进入OSD项目调整亮度二级菜单，这时也要弹出这个复合界面
 */
public class BrightnessAndVolume_View {
    private Context mContext;
    public CltBrightnessAndVolumeView source;
    public WindowManager.LayoutParams lp;
    private int brightness = 0;
    private int volume = 0;

    public BrightnessAndVolume_View(Context context){
        mContext = context;

        initView();
        initLp();
    }

    public void initView(){
        source = new CltBrightnessAndVolumeView(mContext);   // 初始化本身对应的背景View


    }

    public void initLp(){

    }

    public void setProgressFromTouchEvent(int progress) {

    }
}
