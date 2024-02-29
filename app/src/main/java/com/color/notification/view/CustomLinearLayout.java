package com.color.notification.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.color.notification.utils.UiAdaptationUtils;
import com.color.systemui.utils.StaticVariableUtils;

public class CustomLinearLayout extends LinearLayout {

    private Paint paint;
    private Bitmap blurredBitmap;

    public CustomLinearLayout(Context context) {
        this(context, null);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initView();
    }

    /**
     * @param widthMeasureSpec  horizontal space requirements as imposed by the parent.
     *                          The requirements are encoded with
     *                          {@link android.view.View.MeasureSpec}.
     * @param heightMeasureSpec vertical space requirements as imposed by the parent.
     *                          The requirements are encoded with
     *                          {@link android.view.View.MeasureSpec}.
     *                          onMeasure 中能做的事：1、返回自己的大小。2、测量子View的大小，设置子View的外边距
     *                          注意：千万不能在onMearsure中设置自己的LayParams属性，不然就会导致死循环调用
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("CustomLinearLayout", " 宽 高 " + StaticVariableUtils.widthPixels + StaticVariableUtils.heightPixels);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        ViewGroup.MarginLayoutParams layoutParams;
//        //1、notification.xml
        if (getId() == getResources().getIdentifier("linear1", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("quick_settings_frame", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.setMargins(0, 20 * StaticVariableUtils.heightPixels / 1080, 0, 0);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "quick_settings_frame");

                }

                if (child.getId() == getResources().getIdentifier("up", "id", getContext().getPackageName())) {
                    //up
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 40 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 40 * StaticVariableUtils.heightPixels / 1080;
                    child.setLayoutParams(layoutParams);
                    child.measure(MeasureSpec.makeMeasureSpec(40 * StaticVariableUtils.widthPixels / 1920, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(40 * StaticVariableUtils.heightPixels / 1080, MeasureSpec.EXACTLY));
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "up");

                }

                if (child.getId() == getResources().getIdentifier("notification_center_linear", "id", getContext().getPackageName())) {
                    //notification_center_linear
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.setMargins(0, 0, 0, 20 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "notification_center_linear");

                }
                if (child.getId() == getResources().getIdentifier("down", "id", getContext().getPackageName())) {
                    //notification_center_linear
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.setMargins(0, 0, 0, 20 * StaticVariableUtils.heightPixels / 1080);
                    layoutParams.width = 40 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 40 * StaticVariableUtils.heightPixels / 1080;
                    child.setLayoutParams(layoutParams);
                    child.measure(MeasureSpec.makeMeasureSpec(40 * StaticVariableUtils.widthPixels / 1920, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(40 * StaticVariableUtils.heightPixels / 1080, MeasureSpec.EXACTLY));
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "down");

                }

            }
        }

        if (getId() == getResources().getIdentifier("notification_center_linear", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("notification_center_title", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    layoutParams.setMargins(24* StaticVariableUtils.widthPixels / 1920 , 28 * StaticVariableUtils.heightPixels / 1080, 24* StaticVariableUtils.widthPixels / 1920, 20 * StaticVariableUtils.heightPixels / 1080);
                    layoutParams.setMargins(24 * StaticVariableUtils.widthPixels / 1920, 28 * StaticVariableUtils.heightPixels / 1080, 24 * StaticVariableUtils.widthPixels / 1920, 0);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "notification_center_title");

                }

                if (child.getId() == getResources().getIdentifier("empty", "id", getContext().getPackageName())) {
                    //empty
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    layoutParams.setMargins(24* StaticVariableUtils.widthPixels / 1920 , 28 * StaticVariableUtils.heightPixels / 1080, 24* StaticVariableUtils.widthPixels / 1920, 20 * StaticVariableUtils.heightPixels / 1080);
                    layoutParams.setMargins(24 * StaticVariableUtils.widthPixels / 1920, 28 * StaticVariableUtils.heightPixels / 1080, 24 * StaticVariableUtils.widthPixels / 1920, 28 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "empty");

                }

                if (child.getId() == getResources().getIdentifier("notification_center", "id", getContext().getPackageName())) {
                    //notification_center
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.setMargins(0, 10 * StaticVariableUtils.heightPixels / 1080, 0, 20 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "notification_center");

                }
            }
        }


        //2、notification_center.xml

        if (getId() == getResources().getIdentifier("notification_center_item_settings", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("top", "id", getContext().getPackageName())) {
                    //top
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 40 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 40 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.setMarginStart(8 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.setMarginEnd(8 * StaticVariableUtils.widthPixels / 1920);
//                    layoutParams.setMargins(8 * StaticVariableUtils.widthPixels / 1920, 0, 8 * StaticVariableUtils.widthPixels / 1920, 0);
                    child.setLayoutParams(layoutParams);
                    child.measure(MeasureSpec.makeMeasureSpec(40 * StaticVariableUtils.widthPixels / 1920, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(40 * StaticVariableUtils.heightPixels / 1080, MeasureSpec.EXACTLY));
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "top");

                }

                if (child.getId() == getResources().getIdentifier("delete", "id", getContext().getPackageName())) {
                    //delete
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 40 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 40 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.setMarginStart(8 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.setMarginEnd(8 * StaticVariableUtils.widthPixels / 1920);
//                    layoutParams.setMargins(8 * StaticVariableUtils.widthPixels / 1920, 0, 8 * StaticVariableUtils.widthPixels / 1920, 0);
                    child.setLayoutParams(layoutParams);
                    child.measure(MeasureSpec.makeMeasureSpec(40 * StaticVariableUtils.widthPixels / 1920, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(40 * StaticVariableUtils.heightPixels / 1080, MeasureSpec.EXACTLY));
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "delete");

                }

                if (child.getId() == getResources().getIdentifier("settings", "id", getContext().getPackageName())) {
                    //settings
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 40 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 40 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.setMarginStart(8 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.setMarginEnd(8 * StaticVariableUtils.widthPixels / 1920);
//                    layoutParams.setMargins(8 * StaticVariableUtils.widthPixels / 1920, 0, 8 * StaticVariableUtils.widthPixels / 1920, 0);
                    child.setLayoutParams(layoutParams);
                    child.measure(MeasureSpec.makeMeasureSpec(40 * StaticVariableUtils.widthPixels / 1920, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(40 * StaticVariableUtils.heightPixels / 1080, MeasureSpec.EXACTLY));
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "settings");

                }
            }
        }


        if (getId() == getResources().getIdentifier("notification_center_item", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("Icon", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 60 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 60 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.setMarginStart(16 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.setMarginEnd(12 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.topMargin = 20 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.bottomMargin = 20 * StaticVariableUtils.heightPixels / 1080;
//                    layoutParams.setMargins(16 * StaticVariableUtils.widthPixels / 1920, 20 * StaticVariableUtils.heightPixels / 1080, 12 * StaticVariableUtils.widthPixels / 1920, 20 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    child.measure(MeasureSpec.makeMeasureSpec(60 * StaticVariableUtils.widthPixels / 1920, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(60 * StaticVariableUtils.heightPixels / 1080, MeasureSpec.EXACTLY));
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "Icon");

                }

                if (child.getId() == getResources().getIdentifier("notification_center_item_content", "id", getContext().getPackageName())) {
                    //notification_center
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.topMargin = 20 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.bottomMargin = 20 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.setMarginEnd(20 * StaticVariableUtils.widthPixels / 1920);
//                    layoutParams.setMargins(0, 20 * StaticVariableUtils.heightPixels / 1080, 20 * StaticVariableUtils.widthPixels / 1920, 20 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "notification_center_item_content");

                }
            }
        }

        if (getId() == getResources().getIdentifier("linear4", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("appName", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "appName");

                }

                if (child.getId() == getResources().getIdentifier("time", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "time");

                }


            }
        }

        //3、notification_quick_settings.xml
        if (getId() == getResources().getIdentifier("quick_settings", "id", getContext().getPackageName())) {
            Log.d("CustomLinearLayout", "执行到quick_settings");
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("tools", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.setMarginStart(24 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.topMargin = 24 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.bottomMargin = 8 * StaticVariableUtils.heightPixels / 1080;
//                    layoutParams.setMargins(24 * StaticVariableUtils.widthPixels / 1920, 24 * StaticVariableUtils.heightPixels / 1080, 0, 8 * StaticVariableUtils.heightPixels / 1080);
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "执行到tools");

                }

                if (child.getId() == getResources().getIdentifier("gridlayout", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
//                    layoutParams.width = 428 * StaticVariableUtils.widthPixels / 1920;
//                    layoutParams.height = 92 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "执行到gridlayout");

                }

                if (child.getId() == getResources().getIdentifier("brightnessTitle_text", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.setMarginStart(24 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.topMargin = 16 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.bottomMargin = 8 * StaticVariableUtils.heightPixels / 1080;
//                    layoutParams.setMargins(24 * StaticVariableUtils.widthPixels / 1920, 16 * StaticVariableUtils.heightPixels / 1080, 0, 8 * StaticVariableUtils.heightPixels / 1080);
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "brightnessTitle_text");

                }

                if (child.getId() == getResources().getIdentifier("brightness_frame", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.setMarginStart(24 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.setMarginEnd(24 * StaticVariableUtils.widthPixels / 1920);
//                    layoutParams.setMargins(24 * StaticVariableUtils.widthPixels / 1920, 0, 24 * StaticVariableUtils.widthPixels / 1920, 0);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "brightness_frame");

                }

                if (child.getId() == getResources().getIdentifier("volumeTitle_text", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.setMarginStart(24 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.topMargin = 16 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.bottomMargin = 8 * StaticVariableUtils.heightPixels / 1080;
//                    layoutParams.setMargins(24 * StaticVariableUtils.widthPixels / 1920, 16 * StaticVariableUtils.heightPixels / 1080, 0, 8 * StaticVariableUtils.heightPixels / 1080);
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "volumeTitle_text");

                }

                if (child.getId() == getResources().getIdentifier("volume_frame", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.setMarginStart(24 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.setMarginEnd(24 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.bottomMargin = 24 * StaticVariableUtils.heightPixels / 1080;
//                    layoutParams.setMargins(24 * StaticVariableUtils.widthPixels / 1920, 0, 24 * StaticVariableUtils.widthPixels / 1920, 24 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "volume_frame");

                }

            }
        }

        //截图
        if (getId() == getResources().getIdentifier("screenshot_linear", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("screenshot", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 60 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 60 * StaticVariableUtils.heightPixels / 1080;
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "screenshot");

                }

                if (child.getId() == getResources().getIdentifier("screenshot_text", "id", getContext().getPackageName())) {
                    //notification_center
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "screenshot_text");

                }
            }
        }

        //护眼
        if (getId() == getResources().getIdentifier("eye_protection_linear", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("eye_protection", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 60 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 60 * StaticVariableUtils.heightPixels / 1080;
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "eye_protection");

                }

                if (child.getId() == getResources().getIdentifier("eye_protection_text", "id", getContext().getPackageName())) {
                    //notification_center
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "eye_protection_text");

                }
            }
        }

        //摄像头
        if (getId() == getResources().getIdentifier("camera_linear", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("camera", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 60 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 60 * StaticVariableUtils.heightPixels / 1080;
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "camera");

                }

                if (child.getId() == getResources().getIdentifier("camera_text", "id", getContext().getPackageName())) {
                    //notification_center
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "camera_text");

                }
            }
        }

        if (getId() == getResources().getIdentifier("screenrecord_linear", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("screenrecord", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 60 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 60 * StaticVariableUtils.heightPixels / 1080;
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "screenrecord");

                }

                if (child.getId() == getResources().getIdentifier("screenrecord_text", "id", getContext().getPackageName())) {
                    //notification_center
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "screenrecord_text");

                }
            }
        }


        //以下为蓝牙部分
        if (getId() == getResources().getIdentifier("linearlanya", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("notification_center_item_lanya2", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
//                    layoutParams.width = 428*StaticVariableUtils.widthPixels / 1920;
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.setMarginStart(24 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.setMarginEnd(24 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.topMargin = 6 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.bottomMargin = 6 * StaticVariableUtils.heightPixels / 1080;
//                    layoutParams.setMargins(24 * StaticVariableUtils.widthPixels / 1920, 6 * StaticVariableUtils.heightPixels / 1080, 24 * StaticVariableUtils.widthPixels / 1920, 6 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "notification_center_item_lanya2");

                }

                if (child.getId() == getResources().getIdentifier("notification_center_item_lanya", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
//                    layoutParams.width = 428*StaticVariableUtils.widthPixels / 1920;
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.setMarginStart(24 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.setMarginEnd(24 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.topMargin = 6 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.bottomMargin = 6 * StaticVariableUtils.heightPixels / 1080;
//                    layoutParams.setMargins(24 * StaticVariableUtils.widthPixels / 1920, 6 * StaticVariableUtils.heightPixels / 1080, 24 * StaticVariableUtils.widthPixels / 1920, 6 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "notification_center_item_lanya");

                }
            }
        }

        if (getId() == getResources().getIdentifier("notification_center_item_lanya2", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("Icon_lanya2", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 60 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 60 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.setMarginStart(16 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.setMarginEnd(12 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.topMargin = 20 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.bottomMargin = 20 * StaticVariableUtils.heightPixels / 1080;
//                    layoutParams.setMargins(16 * StaticVariableUtils.widthPixels / 1920, 20 * StaticVariableUtils.heightPixels / 1080, 12 * StaticVariableUtils.widthPixels / 1920, 20 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "Icon_lanya2");

                }

                if (child.getId() == getResources().getIdentifier("notification_center_item_content_lanya2", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.topMargin = 20 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.bottomMargin = 20 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.setMarginEnd(20 * StaticVariableUtils.widthPixels / 1920);
//                    layoutParams.setMargins(0, 20 * StaticVariableUtils.heightPixels / 1080, 20 * StaticVariableUtils.widthPixels / 1920, 20 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "notification_center_item_content_lanya2");

                }
            }
        }

        if (getId() == getResources().getIdentifier("notification_center_item_content_lanya2", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("transmission", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "transmission");

                }

                if (child.getId() == getResources().getIdentifier("filename", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "filename");

                }
            }
        }

        if (getId() == getResources().getIdentifier("notification_center_item_lanya", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("Icon_lanya", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 60 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 60 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.setMarginStart(16 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.setMarginEnd(12 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.topMargin = 20 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.bottomMargin = 20 * StaticVariableUtils.heightPixels / 1080;
//                    layoutParams.setMargins(16 * StaticVariableUtils.widthPixels / 1920, 20 * StaticVariableUtils.heightPixels / 1080, 12 * StaticVariableUtils.widthPixels / 1920, 20 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "Icon_lanya");

                }
                if (child.getId() == getResources().getIdentifier("notification_center_item_content_lanya", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.setMarginEnd(20 * StaticVariableUtils.heightPixels / 1080);
                    layoutParams.topMargin = 20 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.bottomMargin = 20 * StaticVariableUtils.heightPixels / 1080;
//                    layoutParams.setMargins(0, 20 * StaticVariableUtils.heightPixels / 1080, 20 * StaticVariableUtils.widthPixels / 1920, 20 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "notification_center_item_content_lanya");

                }
            }
        }

//        if (getId() == getResources().getIdentifier("notification_center_item_content_lanya", "id", getContext().getPackageName())) {
//            int count = getChildCount();
//            //4个
//            for (int i = 0; i < count; i++) {
//                View child = getChildAt(i);
//                if (child.getId() == getResources().getIdentifier("linear_lanya", "id", getContext().getPackageName())) {
//                    //quick_settings_frame
//                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
//                    layoutParams.width = 60*StaticVariableUtils.widthPixels / 1920;
//                    layoutParams.height = 60*StaticVariableUtils.heightPixels / 1080;
//                    layoutParams.setMargins(16*StaticVariableUtils.widthPixels / 1920, 20 * StaticVariableUtils.heightPixels / 1080, 12*StaticVariableUtils.widthPixels / 1920, 20* StaticVariableUtils.heightPixels / 1080);
//                    child.setLayoutParams(layoutParams);
//                    layoutParams = null;
//                    Log.d("CustomLinearLayout", "linear_lanya");
//
//                }
//                if (child.getId() == getResources().getIdentifier("notification_center_item_content_lanya", "id", getContext().getPackageName())) {
//                    //quick_settings_frame
//                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
//                    layoutParams.setMargins(0, 20 * StaticVariableUtils.heightPixels / 1080, 20*StaticVariableUtils.widthPixels / 1920, 20* StaticVariableUtils.heightPixels / 1080);
//                    child.setLayoutParams(layoutParams);
//                    layoutParams = null;
//                    Log.d("CustomLinearLayout", "notification_center_item_content_lanya");
//
//                }
//            }
//        }

        if (getId() == getResources().getIdentifier("linear_lanya", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("appName_lanya", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "appName_lanya");

                }
                if (child.getId() == getResources().getIdentifier("time_lanya", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "time_lanya");

                }
            }
        }

        if (getId() == getResources().getIdentifier("linear_lanya2", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("seekbar_lanya", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 260 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 10 * StaticVariableUtils.heightPixels / 1080;
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "seekbar_lanya");

                }
                if (child.getId() == getResources().getIdentifier("seekbar_lanya_text", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "seekbar_lanya_text");

                }
            }
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

//    private void initView() {
//        ViewGroup.MarginLayoutParams layoutParams;
//        if (getId() == getResources().getIdentifier("notification_center_item", "id", getContext().getPackageName())) {
//            layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
//            layoutParams.width=428*StaticVariableUtils.widthPixels / 1920;
//            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            Log.d("CustomFrameLayout", "notification_center_item 宽"+428*StaticVariableUtils.widthPixels / 1920);
//            setLayoutParams(layoutParams);
//
//        }
//    }

}
