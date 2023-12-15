package com.color.systemui.models.hoverball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.LayoutInflater;

import com.color.osd.R;
import com.color.systemui.utils.StaticInstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import android.graphics.PixelFormat;
import android.util.Log;
import android.view.MotionEvent;

import android.widget.ImageView;

public class Hoverball {
    private Context mycontext;

    private LayoutInflater inflater;

    public View leftlayout, rightlayout;

    private ImageView lefthoverball, righthoverball;

    private boolean leftfirst = true;//移动左侧悬浮球时，计算getY时第一次落点的标记

    private boolean rightfirst = true;//移动右侧悬浮球时，计算getY时第一次落点的标记

    private float leftY, leftrawY, rightY, rightrawY;

    private boolean lefthasACTION_MOVE = false;//用于判断OnTouch中是否有Move事件，防止出现移动完触发点击事件的情况

    private boolean righthasACTION_MOVE = false;


    public WindowManager.LayoutParams lp, lp2;//lp左侧，lp2右侧

    public int Top, Bottom;//悬浮球移动的上下限



    public Hoverball() {
    }

    public void setContext(Context context) {

        mycontext = context;

        //1、初始化LayoutParams描述工具
        initLayoutParams();

        //2、初始化View对象
        initView();

        //3、给View添加Click、Touch事件监听
        setClickAndTouch();
    }

    private void initLayoutParams() {
        lp = new WindowManager.LayoutParams();
        lp.format = PixelFormat.RGBA_8888;
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.LEFT | Gravity.TOP;
        lp.y = StaticInstanceUtils.mcalculateYposition.GetHoverballY();

        lp2 = new WindowManager.LayoutParams();
        lp2.format = PixelFormat.RGBA_8888;
        lp2.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lp2.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            lp2.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        lp2.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp2.gravity = Gravity.RIGHT | Gravity.TOP;
        lp2.y = StaticInstanceUtils.mcalculateYposition.GetHoverballY();

        Top = StaticInstanceUtils.mcalculateYposition.GetHoverballTop();
        Bottom = StaticInstanceUtils.mcalculateYposition.GetHoverballBottom();

    }

    private void initView() {

        inflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        leftlayout = inflater.inflate(R.layout.hoverball_left, null);
        rightlayout = inflater.inflate(R.layout.hoverball_right, null);
        lefthoverball = (ImageView) leftlayout.findViewById(R.id.hoverball_left);
        righthoverball = (ImageView) rightlayout.findViewById(R.id.hoverball_right);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setClickAndTouch() {

        //左侧悬浮球绑定Touch事件
        lefthoverball.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                Log.d("setClickAndTouch", "进入onTouch");
                if (leftfirst) { //保证 getY 的是第一次落点的值
                    leftY = event.getY();
                    leftfirst = false;
                }
                if (action == MotionEvent.ACTION_MOVE) {
                    leftrawY = event.getRawY();
                    lp.y =(int)(leftrawY - leftY);

                    if (lp.y >= Top && lp.y <= Bottom) {
                        StaticInstanceUtils.navigationBar.lp.y = lp.y - Top;
                        Log.d("setClickAndTouch", "更新导航栏位置");
                        StaticInstanceUtils.mavts.wm.updateViewLayout(leftlayout, lp);
                    }

                    lefthasACTION_MOVE = true;

                    //有操作，则重新计时
                    StaticInstanceUtils.mtimeManager.Time_handler_removeCallbacks();
                    StaticInstanceUtils.mtimeManager.Time_handler_postDelayed();

                } else if (action == MotionEvent.ACTION_UP) {
                    leftfirst = true;
                    if(lefthasACTION_MOVE) {
                        lefthasACTION_MOVE = false;
                        return true;
                    }
                }

                return false;
            }
        });

        //左侧悬浮球绑定点击事件
        lefthoverball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //leftlayout.setVisibility(View.GONE);
                StaticVariableUtils.Proactive_triggering_lefthoverball_hide = 0;
                StaticInstanceUtils.manimationManager.lefthoverballHideAnimation();
                StaticVariableUtils.Timing_begins_leftHoverballShow = false;

//                StaticInstanceUtils.navigationBar.leftNavibar.setVisibility(View.VISIBLE);
//                StaticInstanceUtils.mavts.wm.updateViewLayout(StaticInstanceUtils.navigationBar.leftNavibar, StaticInstanceUtils.navigationBar.lp);
                StaticInstanceUtils.manimationManager.leftNavibarShowAnimation();
                StaticVariableUtils.Timing_begins_leftNavibarShow = true;

                //有操作，则重新计时
                StaticInstanceUtils.mtimeManager.Time_handler_removeCallbacks();
                StaticInstanceUtils.mtimeManager.Time_handler_postDelayed();

            }
        });


        //右侧悬浮球绑定Touch事件
        righthoverball.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                Log.d("setClickAndTouch", "进入onTouch");
                if (rightfirst) { //保证 getY 的是第一次落点的值
                    rightY = event.getY();
                    rightfirst = false;
                }
                if (action == MotionEvent.ACTION_MOVE) {
                    rightrawY = event.getRawY();
                    lp2.y =(int)(rightrawY - rightY);

                    if (lp2.y >= Top && lp2.y <= Bottom) {
                        StaticInstanceUtils.navigationBar.lp2.y = lp2.y - Top;
                        Log.d("setClickAndTouch", "更新导航栏位置");
                        StaticInstanceUtils.mavts.wm.updateViewLayout(rightlayout, lp2);
                    }

                    righthasACTION_MOVE = true;

                    //有操作，则重新计时
                    StaticInstanceUtils.mtimeManager.Time_handler_removeCallbacks();
                    StaticInstanceUtils.mtimeManager.Time_handler_postDelayed();

                } else if (action == MotionEvent.ACTION_UP) {
                    rightfirst = true;

                    if(righthasACTION_MOVE) {
                        righthasACTION_MOVE = false;
                        return true;
                    }
                }

                return false;
            }
        });

        //右侧悬浮球绑定点击事件
        righthoverball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rightlayout.setVisibility(View.GONE);
                StaticVariableUtils.Proactive_triggering_righthoverball_hide = 0;
                StaticInstanceUtils.manimationManager.righthoverballHideAnimation();
                StaticVariableUtils.Timing_begins_rightHoverballShow = false;

//                StaticInstanceUtils.navigationBar.rightNavibar.setVisibility(View.VISIBLE);
//                StaticInstanceUtils.mavts.wm.updateViewLayout(StaticInstanceUtils.navigationBar.rightNavibar, StaticInstanceUtils.navigationBar.lp2);
                StaticInstanceUtils.manimationManager.rightNavibarShowAnimation();
                StaticVariableUtils.Timing_begins_rightNavibarShow = true;

                //有操作，则重新计时
                StaticInstanceUtils.mtimeManager.Time_handler_removeCallbacks();
                StaticInstanceUtils.mtimeManager.Time_handler_postDelayed();

            }
        });

    }

    //addView
    public void start() {

        Log.d("start hoverball", "添加addview");
        if( Settings.System.getInt(mycontext.getContentResolver(), StaticInstanceUtils.settingsControlHoverballObserver.OPEN_FAST_TOOLBAR, 5) == 0) {
            StaticVariableUtils.SettingsControlHoverballVisible = false;
            leftlayout.setVisibility(View.GONE);
            rightlayout.setVisibility(View.GONE);
            StaticInstanceUtils.mavts.addView(leftlayout, lp);
            StaticInstanceUtils.mavts.addView(rightlayout, lp2);
        }

        Log.d("start hoverball", String.valueOf(Settings.System.getInt(mycontext.getContentResolver(), StaticInstanceUtils.settingsControlHoverballObserver.OPEN_FAST_TOOLBAR, 5)));

        if(StaticVariableUtils.SettingsControlHoverballVisible) {
            leftlayout.setAlpha(0.0f);
            leftlayout.setTranslationX(-StaticInstanceUtils.hoverball.leftlayout.getWidth());
            rightlayout.setAlpha(0.0f);
            rightlayout.setTranslationX(StaticInstanceUtils.hoverball.rightlayout.getWidth());

            StaticInstanceUtils.mavts.addView(leftlayout, lp);
            StaticInstanceUtils.mavts.addView(rightlayout, lp2);


            Log.d("start hoverball", "展示动画");
            leftlayout.animate()
                    .alpha(1.0f)
                    .translationX(0)
                    .setDuration(1000)
                    .start();
            rightlayout.animate()
                    .alpha(1.0f)
                    .translationX(0)
                    .setDuration(1000)
                    .start();

            StaticVariableUtils.TimeManagerRunning = true;
            StaticInstanceUtils.mtimeManager.Time_handler_postDelayed();
        }

    }


}
