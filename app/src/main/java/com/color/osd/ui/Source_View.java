package com.color.osd.ui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.color.osd.R;

public class Source_View {

    public static View source;
    public static WindowManager.LayoutParams lp;
    boolean sourceTag = false;
    private static Context mycontext;
    static LayoutInflater inflater;

    public Source_View(Context context) {
        mycontext =context;

        inflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        initView();

        initLp();

    }

    private void initView() {
        source = inflater.inflate(R.layout.menu_souce, null);
    }

    private void initLp() {

        lp = new WindowManager.LayoutParams();
        lp.width = 600;
        lp.height = 500;
        lp.gravity = Gravity.CENTER;
        lp.flags = WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                   WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                   WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        lp.format = PixelFormat.RGBA_8888;


    }

}
