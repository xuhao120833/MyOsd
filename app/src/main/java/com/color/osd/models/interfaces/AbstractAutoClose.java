package com.color.osd.models.interfaces;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.color.osd.models.Enum.MenuState;
import com.color.osd.models.FunctionBind;
import com.color.osd.models.service.MenuService;

/**
 * 自动关闭view的基类
 */
public abstract class AbstractAutoClose {
    private static final String TAG = AbstractAutoClose.class.getSimpleName();

    protected long defaultDelayMillis = 5000;    // 默认5秒延时
    public Handler mainHandler = MenuService.mainHandler;


    public void autoClose(View view){
        Log.d(TAG, "autoClose: " + view.getClass());
        autoClose(view, defaultDelayMillis);
    }

    /**
     * 自动关闭的定时任务
     * @param view             要被移除的view
     * @param delayMillis      延时时间
     */
    public void autoClose(View view, long delayMillis){
        if (mainHandler == null) return;
        mainHandler.postDelayed(() -> {
            try{
                FunctionBind.mavts.clearView(view);
            }catch (Exception e){
                Log.d(TAG, "autoClose: auto clear view error: " + e.getMessage());
            }
            MenuService.menuState = MenuState.NULL;
        }, delayMillis);
    }


    /**
     * 取消自动关闭的定时任务
     * 例如：按了返回按钮，view立马就被移除了，所以这里最好把延时任务取消了，避免出错
     */
    public void cancelAutoClose(){
        Log.d(TAG, "cancelAutoClose: ");
        if (mainHandler == null) return;
        mainHandler.removeCallbacksAndMessages(null);
    }

    public void reClose(View view){
        Log.d(TAG, "reClose: " + view.getClass());
        reClose(view, defaultDelayMillis);
    }

    /**
     * 重新自动关闭
     * 这种情况是：比如亮度条view被刚加载了，会自动autoClose()，
     * 此时遥控加减亮度值，这个时候就要在新的遥控事件过后重新定时关闭。
     * @param view              // 待移除的view
     * @param delayMillis       // 延时的时间
     */
    public void reClose(View view, long delayMillis){
        // 1 先取消当前handler中的延时任务
        cancelAutoClose();
        autoClose(view, delayMillis);
    }

}
