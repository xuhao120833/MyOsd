package com.color.osd.models;

import android.util.Log;
import android.view.View;
import android.app.ActionBar;
import android.content.Context;
import android.view.WindowManager;

public class AddViewToScreen {
    public static WindowManager wm;
    private Context mcontext;

    public void addView(View v, WindowManager.LayoutParams p) {
        // 添加一个view之前先尝试删除这个view, 避免重复添加
        Log.d("AddViewToScreen", "v.isAttachedToWindow() " + String.valueOf(v.isAttachedToWindow()));
        if (v.isAttachedToWindow()) {
            // 视图已经添加到屏幕上
            try {
                v.clearFocus();
                wm.removeViewImmediate(v);
            } catch (Exception e) {
                Log.d("AddViewToScreen", "addView : removeView has Error： " + e.getMessage());
            }
        }
        v.clearFocus();
        wm.addView(v, p);
    }

    public void setContext(Context context) {
        mcontext = context;
        wm = mcontext.getSystemService(WindowManager.class);
    }

    public void clearView(View v) {
        if (v != null) {
            //Log.d("xuhao", "clearView");
            try {
                wm.removeViewImmediate(v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
