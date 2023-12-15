package com.color.systemui.utils;

import android.content.Context;
import android.view.WindowManager;
import android.util.Log;
import android.view.Display;
import android.util.DisplayMetrics;

public class CalculateYposition {

    public WindowManager mywindowmanager;
    private Context mycontext;
    Display defaultDisplay;
    DisplayMetrics metrics = new DisplayMetrics();
    int width, height, dpi;
    int overrideWidth, overrideHeight;

    public CalculateYposition(Context context) {
        mycontext = context;
        mywindowmanager = (WindowManager) mycontext.getSystemService(Context.WINDOW_SERVICE);
        defaultDisplay = mywindowmanager.getDefaultDisplay();
        mywindowmanager.getDefaultDisplay().getMetrics(metrics);
        GetRes();
        GetDpi();

    }

    private void GetRes() {
        width = defaultDisplay.getWidth();
        height = defaultDisplay.getHeight();

        Log.d("CalculateYposition 屏幕宽 ", String.valueOf(width));
        Log.d("CalculateYposition 屏幕高", String.valueOf(height));
    }

    private void GetDpi() {
//        defaultDisplay.getMetrics(metrics);
        dpi = (int) (metrics.density * 160);
        Log.d("CalculateYposition 屏幕dpi", String.valueOf(dpi));
    }

    public int GetHoverballY() {
        height = defaultDisplay.getHeight();
        Log.d("CalculateYposition 悬浮球Y坐标", String.valueOf(height / 2 - 50 * dpi / 160 / 2));
        return height / 2 - height * 100 / 1080 / 2;
    }

    public int GetHoverballTop() {
        height = defaultDisplay.getHeight();
        Log.d("CalculateYposition TOP", String.valueOf(height * 508 / 1080 / 2 - height * 100 / 1080 / 2));
        return height * 508 / 1080 / 2 - height * 100 / 1080 / 2;
    }

    public int GetHoverballBottom() {
        height = defaultDisplay.getHeight();
        Log.d("CalculateYposition Bottom", String.valueOf(height - 254 * dpi / 160 / 2 - 50 * dpi / 160 / 2));
        return height - height * 508 / 1080 / 2 - height * 100 / 1080 / 2;
    }

    public int GetNavibarY() {
        height = defaultDisplay.getHeight();
        Log.d("CalculateYposition 导航栏Y坐标", String.valueOf(height / 2 - 254 * dpi / 160 / 2));
        return height / 2 - height * 508 / 1080 / 2;
    }

}