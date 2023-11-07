package com.color.osd.models;

import android.util.Log;
import android.view.View;
import android.app.ActionBar;
import android.content.Context;
import android.view.WindowManager;

public class AddViewToScreen {
    static WindowManager wm;
    private Context mcontext;

    public void addView(View v, WindowManager.LayoutParams p) {
        wm.addView(v, p);
    }

    public void setContext(Context context) {
        mcontext = context;
        wm = mcontext.getSystemService(WindowManager.class);
    }

    public void clearView(View v) {
        if (v != null) {
            Log.d("xuhao", "clearView");
            wm.removeViewImmediate(v);
        }
    }

}
