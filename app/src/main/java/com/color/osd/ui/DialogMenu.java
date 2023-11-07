package com.color.osd.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.color.osd.R;
import com.color.osd.models.FunctionBind;

public class DialogMenu extends Dialog {
    Context mycontext;
    Window wm;

    FunctionBind mybind;

    WindowManager.LayoutParams lp;

    public View Menu, Menu_source, Menu_brightness, Menu_volume, Menu_eye, Menu_screenshot, Menu_comments;
    public static CustomDialog mydialog;

    public DialogMenu(Context context) {

        super(context);
        mycontext = context;
        mydialog = new CustomDialog(mycontext);

        mybind = new FunctionBind(mycontext);

    }

    public void start() {
        initMenu();
    }

    private void initMenu() {
        //引入layout
        initview();

        //初始化window
        initwindow();

        //初始化LayoutParams
        initlp();

        //各个按钮功能绑定入口
        functionBind();


    }


    private void initview() {

        wm = mydialog.getWindow();
        wm.requestFeature(Window.FEATURE_NO_TITLE);

        mydialog.setCanceledOnTouchOutside(true);
        mydialog.setContentView(R.layout.dialog_menu);

        Menu = mydialog.findViewById(R.id.Menu);
        Menu_source = mydialog.findViewById(R.id.Menu_source);
        Menu_brightness = mydialog.findViewById(R.id.Menu_brightness);
        Menu_volume = mydialog.findViewById(R.id.Menu_volume);
        Menu_eye = mydialog.findViewById(R.id.Menu_eye);
        Menu_screenshot = mydialog.findViewById(R.id.Menu_screenshot);
        Menu_comments = mydialog.findViewById(R.id.Menu_comments);

    }

    private void initwindow() {

        wm.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        wm.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);
        wm.addFlags(WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        wm.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

    }

    private void initlp() {

        lp = wm.getAttributes();
        lp.format = PixelFormat.RGBA_8888;
        lp.setTitle("Menu Dialog..");
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        lp.windowAnimations = -1;
        wm.setAttributes(lp);
        wm.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //setCanceledOnTouchOutside(true);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.d("DialogMenu", "收到按键消息");
        return super.onKeyDown(keyCode, event);

    }

    private void functionBind() {
        mybind.Bind(Menu, Menu_source, Menu_brightness, Menu_volume, Menu_eye, Menu_screenshot, Menu_comments);

    }


    public final class CustomDialog extends Dialog implements DialogInterface {
        public CustomDialog(Context context) {
            super(context, 0);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            return super.dispatchTouchEvent(ev);
        }

        @Override
        protected void onStart() {
            super.setCanceledOnTouchOutside(true);
            super.onStart();
        }

        @Override
        protected void onStop() {
            super.onStop();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (isShowing()) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    return true;
                }
            }
            return false;
        }


        @Override
        public void onBackPressed() {

            super.onBackPressed();

        }

    }


}
