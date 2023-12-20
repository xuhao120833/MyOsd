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
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import android.graphics.PixelFormat;
import android.view.MotionEvent;

import android.widget.ImageView;

public class Hoverball implements Instance {
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
        lp.y = STATIC_INSTANCE_UTILS.mcalculateYposition.GetHoverballY();

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
        lp2.y = STATIC_INSTANCE_UTILS.mcalculateYposition.GetHoverballY();

        Top = STATIC_INSTANCE_UTILS.mcalculateYposition.GetHoverballTop();
        Bottom = STATIC_INSTANCE_UTILS.mcalculateYposition.GetHoverballBottom();

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
                //Log.d("setClickAndTouch", "进入onTouch");
                if (leftfirst) { //保证 getY 的是第一次落点的值
                    leftY = event.getY();
                    leftfirst = false;
                }
                if (action == MotionEvent.ACTION_MOVE) {
                    leftrawY = event.getRawY();
                    lp.y =(int)(leftrawY - leftY);

                    if (lp.y >= Top && lp.y <= Bottom) {
                        STATIC_INSTANCE_UTILS.navigationBar.lp.y = lp.y - Top;
                        //Log.d("setClickAndTouch", "更新导航栏位置");
                        STATIC_INSTANCE_UTILS.mavts.wm.updateViewLayout(leftlayout, lp);
                    }

                    lefthasACTION_MOVE = true;

                    //有操作，则重新计时
                    STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                    STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

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
                STATIC_INSTANCE_UTILS.manimationManager.lefthoverballHideAnimation();
                StaticVariableUtils.Timing_begins_leftHoverballShow = false;

//                InstanceUtils.navigationBar.leftNavibar.setVisibility(View.VISIBLE);
//                InstanceUtils.mavts.wm.updateViewLayout(InstanceUtils.navigationBar.leftNavibar, InstanceUtils.navigationBar.lp);
                STATIC_INSTANCE_UTILS.manimationManager.leftNavibarShowAnimation();
                StaticVariableUtils.Timing_begins_leftNavibarShow = true;

                //有操作，则重新计时
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

            }
        });


        //右侧悬浮球绑定Touch事件
        righthoverball.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                //Log.d("setClickAndTouch", "进入onTouch");
                if (rightfirst) { //保证 getY 的是第一次落点的值
                    rightY = event.getY();
                    rightfirst = false;
                }
                if (action == MotionEvent.ACTION_MOVE) {
                    rightrawY = event.getRawY();
                    lp2.y =(int)(rightrawY - rightY);

                    if (lp2.y >= Top && lp2.y <= Bottom) {
                        STATIC_INSTANCE_UTILS.navigationBar.lp2.y = lp2.y - Top;
                        //Log.d("setClickAndTouch", "更新导航栏位置");
                        STATIC_INSTANCE_UTILS.mavts.wm.updateViewLayout(rightlayout, lp2);
                    }

                    righthasACTION_MOVE = true;

                    //有操作，则重新计时
                    STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                    STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

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
                STATIC_INSTANCE_UTILS.manimationManager.righthoverballHideAnimation();
                StaticVariableUtils.Timing_begins_rightHoverballShow = false;

//                InstanceUtils.navigationBar.rightNavibar.setVisibility(View.VISIBLE);
//                InstanceUtils.mavts.wm.updateViewLayout(InstanceUtils.navigationBar.rightNavibar, InstanceUtils.navigationBar.lp2);
                STATIC_INSTANCE_UTILS.manimationManager.rightNavibarShowAnimation();
                StaticVariableUtils.Timing_begins_rightNavibarShow = true;

                //有操作，则重新计时
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

            }
        });

    }

    //addView
    public void start() {

        //Log.d("start hoverball", "添加addview");
        if( Settings.System.getInt(mycontext.getContentResolver(), STATIC_INSTANCE_UTILS.settingsControlHoverballObserver.OPEN_FAST_TOOLBAR, 5) == 0) {
            StaticVariableUtils.SettingsControlHoverballVisible = false;
            leftlayout.setVisibility(View.GONE);
            rightlayout.setVisibility(View.GONE);
            STATIC_INSTANCE_UTILS.mavts.addView(leftlayout, lp);
            STATIC_INSTANCE_UTILS.mavts.addView(rightlayout, lp2);
        }

        //Log.d("start hoverball", String.valueOf(Settings.System.getInt(mycontext.getContentResolver(), InstanceUtils.settingsControlHoverballObserver.OPEN_FAST_TOOLBAR, 5)));

        if(StaticVariableUtils.SettingsControlHoverballVisible) {
            leftlayout.setAlpha(0.0f);
            leftlayout.setTranslationX(-STATIC_INSTANCE_UTILS.hoverball.leftlayout.getWidth());
            rightlayout.setAlpha(0.0f);
            rightlayout.setTranslationX(STATIC_INSTANCE_UTILS.hoverball.rightlayout.getWidth());

            STATIC_INSTANCE_UTILS.mavts.addView(leftlayout, lp);
            STATIC_INSTANCE_UTILS.mavts.addView(rightlayout, lp2);


            //Log.d("start hoverball", "展示动画");
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
            STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();
        }

    }


}
