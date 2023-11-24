package com.color.osd.models.interfaces;

/**
 * 声音调整和亮度调整的抽象类
 */
public interface MenuBrightnessAndVolumeInterface {
    void setProgressFromTouchEvent(int progress);    // 由touch事件反过来设置相关的进度值

    void onKeyDownFromBaseView(boolean positive);    // 由view的keyDown事件反过来传递相关按键
    void onKeyUpClose();    // 由view的keyDown事件反过来传递相关按键

}
