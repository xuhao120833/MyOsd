package com.color.osd.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import com.color.osd.models.Menu_brightness;
import com.color.osd.models.service.MenuService;
import com.color.osd.utils.ConstantProperties;
import com.color.osd.utils.DensityUtil;
import com.color.systemui.interfaces.Instance;

import java.util.function.Consumer;


public class DialogMenu extends Dialog implements Instance {
    Context mycontext;
    Window wm;

    public FunctionBind mybind;

    WindowManager.LayoutParams lp;

    public View Menu, Menu_source, Menu_brightness, Menu_volume, Menu_recent, Menu_eye_off, Menu_eye_on, Menu_screenshot, Menu_comments;
    public static CustomDialog mydialog;

    public DialogMenu(Context context) {

        super(context);
        mycontext = context;
        mydialog = new CustomDialog(mycontext);

        mybind = new FunctionBind(mycontext);
        setInstance(mybind);
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

        //高斯模糊
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mydialog.getWindow().setDimAmount(0f);
            mydialog.getWindow().setBackgroundBlurRadius(90);
        }
    }


    private void initview() {

        wm = mydialog.getWindow();
        wm.requestFeature(Window.FEATURE_NO_TITLE);

        mydialog.setCanceledOnTouchOutside(true);
//        mydialog.setContentView(R.layout.dialog_menu);
        mydialog.setContentView(R.layout.new_dialog_menu);

        Menu = mydialog.findViewById(R.id.Menu);
        Menu_source = mydialog.findViewById(R.id.Menu_source);
        Menu_brightness = mydialog.findViewById(R.id.Menu_brightness);
        Menu_volume = mydialog.findViewById(R.id.Menu_volume);
        Menu_recent = mydialog.findViewById(R.id.Menu_recent);

        //Menu_eye = mydialog.findViewById(R.id.Menu_eye_off);
        //Menu_eye = mydialog.findViewById(R.id.Menu_eye_on);
        Menu_screenshot = mydialog.findViewById(R.id.Menu_screenshot);
        Menu_comments = mydialog.findViewById(R.id.Menu_comments);

    }

    private void initwindow() {

        wm.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        wm.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);
        wm.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                | WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            wm.setBackgroundBlurRadius(50);
        }

        //setCanceledOnTouchOutside(true);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //Log.d("DialogMenu", "收到按键消息");
        return super.onKeyDown(keyCode, event);

    }

    private void functionBind() {
        mybind.Bind(Menu, Menu_source, Menu_brightness, Menu_volume, Menu_recent ,Menu_eye_off ,Menu_eye_on , Menu_screenshot, Menu_comments);

    }


    public final class CustomDialog extends Dialog implements DialogInterface {
        public CustomDialog(Context context) {
            super(context, R.style.BlurDialogTheme);
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

    public void refreshMenuView() {
        //Log.d(getClass().getSimpleName() + "Adaptation-MyError", "refreshMenuView");

        // clearAllChildView();
        Menu.requestLayout();
    }

    public void clearAllChildView() {
        // 根据MenuService.menuState，移除当前的亮度/音量二级窗口
        mybind.removeItemViewByMenuState(MenuService.menuState);
    }

}
