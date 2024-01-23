package com.color.notification.utils;


import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class UiAdaptationUtils {

    private Context mycontext;

    private static UiAdaptationUtils utils;


    public static UiAdaptationUtils getInstance(Context context) {
        utils = new UiAdaptationUtils(context);

        return utils;
    }

    //参照宽高
    public final float STANDARD_WIDTH = 1920;
    public final float STANDARD_HEIGHT = 1080;

    //当前设备实际宽高
    public float displayMetricsWidth;
    public float displayMetricsHeight;

    private final String DIMEN_CLASS = "com.android.internal.R$dimen";

    private UiAdaptationUtils(Context context) {
        mycontext = context;
        //
        WindowManager windowManager = (WindowManager) mycontext.getSystemService(Context.WINDOW_SERVICE);

        //加载当前界面信息
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        Log.i("testbarry", "displayMetrics:" + String.valueOf(displayMetrics.widthPixels));
        Log.i("testbarry", "displayMetrics:" + String.valueOf(displayMetrics.heightPixels));

        if (displayMetricsWidth == 0.0f || displayMetricsHeight == 0.0f) {
            //获取状态框信息
//            int systemBarHeight = getValue(mycontext,"system_bar_height",48);

//            if(displayMetrics.widthPixels > displayMetrics.heightPixels){
//                this.displayMetricsWidth = displayMetrics.heightPixels;
//                this.displayMetricsHeight = displayMetrics.widthPixels ;
//            }else{
            this.displayMetricsWidth = displayMetrics.widthPixels;
            this.displayMetricsHeight = displayMetrics.heightPixels;
//            }

        }
    }

    //对外提供系数
    public float getHorizontalScaleValue() {
        return displayMetricsWidth / STANDARD_WIDTH;
    }

    public float getVerticalScaleValue() {

        Log.i("testbarry", "displayMetricsHeight:" + displayMetricsHeight);
        return displayMetricsHeight / STANDARD_HEIGHT;
    }

    public int getValue(Context context, String systemid, int defValue) {

        try {
            Class<?> clazz = Class.forName(DIMEN_CLASS);
            Object r = clazz.newInstance();
            Field field = clazz.getField(systemid);
            int x = (int) field.get(r);
            return context.getResources().getDimensionPixelOffset(x);

        } catch (Exception e) {
            return defValue;
        }
    }
}
