package com.color.systemui.models.navibar;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.LayoutInflater;

import com.color.osd.R;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import android.graphics.PixelFormat;
import android.widget.ImageView;
import android.view.KeyEvent;
import android.app.Instrumentation;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.content.ComponentName;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NavigationBar implements Instance {

    private Context mycontext;

    public ImageView leftback, lefthome, leftrecent, leftsource, leftcomments, leftwhiteboard, leftnotification, leftcollapse, leftdline1, leftdline2;

    public ImageView rightback, righthome, rightrecent, rightsource, rightcomments, rightwhiteboard, rightnotification, rightcollapse, rightdline1, rightdline2;

    private Intent leftcomments_intent = new Intent(), leftwhiteboard_intent,
            rightcomments_intent = new Intent(), rightwhiteboard_intent;

    private ComponentName componentName;

    private PackageManager packageManager;

    private final String PACKAGE_MARK = "com.mphotool.mark";

    private final String PACKAGE_WHITEBOARD = "com.mphotool.whiteboard";

    LayoutInflater inflater;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public View leftNavibar, rightNavibar;

    public WindowManager.LayoutParams lp, lp2;

    public NavigationBar() {
    }

    public void setContext(Context context) {

        mycontext = context;

        //初始化一些需要用到的工具类
        try {

            if (packageManager == null) {
                packageManager = mycontext.getPackageManager();
            }
            if (componentName == null) {
                componentName = new ComponentName(PACKAGE_MARK, PACKAGE_MARK + ".MarkService");
            }
            leftcomments_intent.setComponent(componentName);
            leftwhiteboard_intent = packageManager.getLaunchIntentForPackage(PACKAGE_WHITEBOARD);
            leftwhiteboard_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            rightcomments_intent.setComponent(componentName);
            rightwhiteboard_intent = packageManager.getLaunchIntentForPackage(PACKAGE_WHITEBOARD);
            rightwhiteboard_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //1、初始化LayoutParams描述工具
        initLayoutParams();

        //2、初始化View对象
        initView();

        //3、给View添加Click、Touch事件监听
        setClick();

    }

    private void initLayoutParams() {

        lp = new WindowManager.LayoutParams();
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.LEFT | Gravity.TOP;
        lp.format = PixelFormat.RGBA_8888;
        lp.y = STATIC_INSTANCE_UTILS.mcalculateYposition.GetNavibarY();

        lp2 = new WindowManager.LayoutParams();
        lp2.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lp2.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            lp2.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp2.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp2.gravity = Gravity.RIGHT | Gravity.TOP;
        lp2.format = PixelFormat.RGBA_8888;
        lp2.y = STATIC_INSTANCE_UTILS.mcalculateYposition.GetNavibarY();

    }

    private void initView() {

        inflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        leftNavibar = inflater.inflate(R.layout.navibar_left, null);
        rightNavibar = inflater.inflate(R.layout.navibar_right, null);

        leftback = (ImageView) leftNavibar.findViewById(R.id.Back);
        lefthome = (ImageView) leftNavibar.findViewById(R.id.Home);
        leftrecent = (ImageView) leftNavibar.findViewById(R.id.Recent);
        leftsource = (ImageView) leftNavibar.findViewById(R.id.Source);
        leftcomments = (ImageView) leftNavibar.findViewById(R.id.Comments);
        leftwhiteboard = (ImageView) leftNavibar.findViewById(R.id.Whiteboard);
        leftnotification = (ImageView) leftNavibar.findViewById(R.id.Notification);
        leftcollapse = (ImageView) leftNavibar.findViewById(R.id.Collapse);
        leftdline1 = (ImageView) leftNavibar.findViewById(R.id.Dline1);
        leftdline2 = (ImageView) leftNavibar.findViewById(R.id.Dline2);

        rightback = (ImageView) rightNavibar.findViewById(R.id.Back2);
        righthome = (ImageView) rightNavibar.findViewById(R.id.Home2);
        rightrecent = (ImageView) rightNavibar.findViewById(R.id.Recent2);
        rightsource = (ImageView) rightNavibar.findViewById(R.id.Source2);
        rightcomments = (ImageView) rightNavibar.findViewById(R.id.Comments2);
        rightwhiteboard = (ImageView) rightNavibar.findViewById(R.id.Whiteboard2);
        rightnotification = (ImageView) rightNavibar.findViewById(R.id.Notification2);
        rightcollapse = (ImageView) rightNavibar.findViewById(R.id.Collapse2);
        rightdline1 = (ImageView) rightNavibar.findViewById(R.id.Dline1);
        rightdline2 = (ImageView) rightNavibar.findViewById(R.id.Dline2);

    }

    private void setClick() {

        //左侧导航栏

        leftback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                executorService.submit(() -> {
                    try {
                        new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });

//                executorService.submit(() -> {
//                    try {
//                        ((Activity) mycontext).onBackPressed();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });

                if (STATIC_INSTANCE_UTILS.myNotification.notification.getVisibility() == View.VISIBLE) {
                    STATIC_INSTANCE_UTILS.myNotification.notification.setVisibility(View.GONE);
                }

                //有操作，则重新计时
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

            }
        });

        //我想生成一个计算素数的方法

        lefthome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.submit(() -> {
                    try {
                        new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_HOME);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });

                if (STATIC_INSTANCE_UTILS.myNotification.notification.getVisibility() == View.VISIBLE) {
                    STATIC_INSTANCE_UTILS.myNotification.notification.setVisibility(View.GONE);
                }

                //有操作，则重新计时
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

            }
        });

        leftrecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.submit(() -> {
                    try {
                        new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_APP_SWITCH);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                //有操作，则重新计时
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

            }
        });

        leftsource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //setFocusableByNavibar();
                setFocus();
                STATIC_INSTANCE_UTILS.source.Source.setVisibility(View.VISIBLE);

                //有操作，则重新计时
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

            }
        });

        leftcomments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    mycontext.startService(leftcomments_intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //有操作，则重新计时
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

            }
        });

        leftwhiteboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    mycontext.startActivity(leftwhiteboard_intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //有操作，则重新计时
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

            }
        });

        leftnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (STATIC_INSTANCE_UTILS.myNotification.notification.getVisibility() == View.GONE) {//悬浮窗在没有addView之前，状态也为GONE

                    STATIC_INSTANCE_UTILS.myNotification.lp.gravity = Gravity.LEFT;

                    //updateViewLayout这一步异常就走catch
                    STATIC_INSTANCE_UTILS.mavts.wm.updateViewLayout(STATIC_INSTANCE_UTILS.myNotification.notification, STATIC_INSTANCE_UTILS.myNotification.lp);

                    StaticVariableUtils.left_or_right = "left";//快捷中心在右侧打开
//                    STATIC_INSTANCE_UTILS.myNotification.notification.setVisibility(View.VISIBLE);
                    STATIC_INSTANCE_UTILS.manimationManager.NotificationShowAnimation();

                    STATIC_INSTANCE_UTILS.navigationBar.leftNavibar.setVisibility(View.GONE);

                } else if (STATIC_INSTANCE_UTILS.myNotification.notification.getVisibility() == View.VISIBLE) {
//                    STATIC_INSTANCE_UTILS.myNotification.notification.setVisibility(View.GONE);
                    STATIC_INSTANCE_UTILS.manimationManager.NotificationHideAnimation();
                }

//                //有操作，则重新计时
//                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
//                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();
            }
        });

        leftcollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //leftNavibar.setVisibility(View.GONE);
                StaticVariableUtils.Proactive_triggering_leftnavibar_hide = 0;
                STATIC_INSTANCE_UTILS.manimationManager.leftNavibarHideAnimation();
                StaticVariableUtils.Timing_begins_leftNavibarShow = false;

                //InstanceUtils.hoverball.leftlayout.setVisibility(View.VISIBLE);
                STATIC_INSTANCE_UTILS.manimationManager.lefthoverballShowAnimation();
                StaticVariableUtils.Timing_begins_leftHoverballShow = true;

                //有操作，则重新计时
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

            }
        });


        //右侧导航栏

        rightback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                executorService.submit(() -> {
                    try {
                        new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });

//                executorService.submit(() -> {
//                    try {
//                        ((Activity) mycontext).onBackPressed();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });

                if (STATIC_INSTANCE_UTILS.myNotification.notification.getVisibility() == View.VISIBLE) {
                    STATIC_INSTANCE_UTILS.myNotification.notification.setVisibility(View.GONE);
                }

                //有操作，则重新计时
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

            }
        });

        righthome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.submit(() -> {
                    try {
                        new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_HOME);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });

                if (STATIC_INSTANCE_UTILS.myNotification.notification.getVisibility() == View.VISIBLE) {
                    STATIC_INSTANCE_UTILS.myNotification.notification.setVisibility(View.GONE);
                }

                //有操作，则重新计时
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

            }
        });

        rightrecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.submit(() -> {
                    try {
                        new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_APP_SWITCH);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                //有操作，则重新计时
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

            }
        });

        rightsource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setFocusableByNavibar();
                setFocus();
                STATIC_INSTANCE_UTILS.source.Source.setVisibility(View.VISIBLE);

                //有操作，则重新计时
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

            }
        });

        rightcomments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    mycontext.startService(rightcomments_intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //有操作，则重新计时
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

            }
        });

        rightwhiteboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    mycontext.startActivity(rightwhiteboard_intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //有操作，则重新计时
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

            }
        });

        rightnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (STATIC_INSTANCE_UTILS.myNotification.notification.getVisibility() == View.GONE) {

                    STATIC_INSTANCE_UTILS.myNotification.lp.gravity = Gravity.RIGHT;
//                    STATIC_INSTANCE_UTILS.myNotification.lp.x = 1500;
                    STATIC_INSTANCE_UTILS.mavts.wm.updateViewLayout(STATIC_INSTANCE_UTILS.myNotification.notification, STATIC_INSTANCE_UTILS.myNotification.lp);

                    StaticVariableUtils.left_or_right = "right";//快捷中心在右侧打开
//                    STATIC_INSTANCE_UTILS.myNotification.notification.setVisibility(View.VISIBLE);
                    STATIC_INSTANCE_UTILS.manimationManager.NotificationShowAnimation();

                    STATIC_INSTANCE_UTILS.navigationBar.rightNavibar.setVisibility(View.GONE);


                } else if (STATIC_INSTANCE_UTILS.myNotification.notification.getVisibility() == View.VISIBLE) {
//                    STATIC_INSTANCE_UTILS.myNotification.notification.setVisibility(View.GONE);
                    STATIC_INSTANCE_UTILS.manimationManager.NotificationHideAnimation();
                }

//                //有操作，则重新计时
//                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
//                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();
            }
        });

        rightcollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rightNavibar.setVisibility(View.GONE);
                StaticVariableUtils.Proactive_triggering_rightnavibar_hide = 0;
                STATIC_INSTANCE_UTILS.manimationManager.rightNavibarHideAnimation();
                StaticVariableUtils.Timing_begins_rightNavibarShow = false;

                //InstanceUtils.hoverball.rightlayout.setVisibility(View.VISIBLE);
                STATIC_INSTANCE_UTILS.manimationManager.righthoverballShowAnimation();
                StaticVariableUtils.Timing_begins_rightHoverballShow = true;

                //有操作，则重新计时
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_removeCallbacks();
                STATIC_INSTANCE_UTILS.mtimeManager.Time_handler_postDelayed();

            }
        });

    }

    public void start() {

        STATIC_INSTANCE_UTILS.mavts.addView(leftNavibar, lp);
        STATIC_INSTANCE_UTILS.mavts.addView(rightNavibar, lp2);

    }

    private void setFocusableByNavibar() {
        STATIC_INSTANCE_UTILS.source.X.setFocusable(false);
        STATIC_INSTANCE_UTILS.source.OPS.setFocusable(false);
        STATIC_INSTANCE_UTILS.source.Android.setFocusable(false);
        STATIC_INSTANCE_UTILS.source.HDMI1.setFocusable(false);
        STATIC_INSTANCE_UTILS.source.HDMI2.setFocusable(false);
    }

    public void setFocus() {
        switch (STATIC_INSTANCE_UTILS.source.select_source) {
            case "OPS":
                STATIC_INSTANCE_UTILS.source.OPS.requestFocus();
                break;
            case "Android":
                STATIC_INSTANCE_UTILS.source.Android.requestFocus();
                break;
            case "HDMI1":
                STATIC_INSTANCE_UTILS.source.HDMI1.requestFocus();
                break;
            case "HDMI2":
                STATIC_INSTANCE_UTILS.source.HDMI2.requestFocus();
                break;
        }
    }


}
