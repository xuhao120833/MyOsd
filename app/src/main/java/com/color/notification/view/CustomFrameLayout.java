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
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.StaticVariableUtils;

public class CustomFrameLayout extends FrameLayout implements Instance {

    private Paint paint;
    private Bitmap blurredBitmap;

    public CustomFrameLayout(Context context) {
        this(context, null);
    }

    public CustomFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        initView();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        ViewGroup.MarginLayoutParams layoutParams;
        //1、notification.xml
        if (getId() == getResources().getIdentifier("quick_settings_frame", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("notification_quick_settings", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.setMargins(0, 20 * StaticVariableUtils.heightPixels / 1080, 0, 20 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomFrameLayout", "notification_quick_settings");

                }
            }
        }

        //2、notification_center.xml
        if (getId() == getResources().getIdentifier("frame1", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("notification_center_item", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
//                    layoutParams.width = 428 * StaticVariableUtils.widthPixels / 1920;
//                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    Log.d("CustomFrameLayout", "notification_center_item 宽" + 428 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.setMargins(24 * StaticVariableUtils.widthPixels / 1920, 6 * StaticVariableUtils.heightPixels / 1080, 24 * StaticVariableUtils.widthPixels / 1920, 6 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    Log.d("CustomFrameLayout", "notification_center_item");
                    layoutParams = null;

                }
            }
        }

        //3、notification_quick_settings.xml
        if (getId() == getResources().getIdentifier("frame2", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("quick_settings", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
//                    layoutParams.width = 428 * StaticVariableUtils.widthPixels / 1920;
//                    layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.setMargins(24 * StaticVariableUtils.widthPixels / 1920, 6 * StaticVariableUtils.heightPixels / 1080, 24 * StaticVariableUtils.widthPixels / 1920, 6 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    Log.d("CustomFrameLayout", "quick_settings");
                    layoutParams = null;

                }
            }
        }

        //亮度条
        if (getId() == getResources().getIdentifier("brightness_frame", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("brightnessSeekBar", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
//                    layoutParams.width = 380 * StaticVariableUtils.widthPixels / 1920;
                    if (2000 < StaticVariableUtils.widthPixels && StaticVariableUtils.widthPixels < 3000) {
                        layoutParams.height = (40 * StaticVariableUtils.heightPixels / 1080 + 20);
                    } else if (StaticVariableUtils.widthPixels > 3000) {
                        layoutParams.height = (40 * StaticVariableUtils.heightPixels / 1080 + 30);
                    } else {
                        layoutParams.height = 40 * StaticVariableUtils.heightPixels / 1080;
                    }
                    child.setLayoutParams(layoutParams);
                    Log.d("CustomFrameLayout", "brightnessSeekBar");
                    layoutParams = null;

                }

                if (child.getId() == getResources().getIdentifier("brightness_icon", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 24 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 24 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.setMargins(8 * StaticVariableUtils.widthPixels / 1920, 0, 0, 0);
                    child.setLayoutParams(layoutParams);
                    Log.d("CustomFrameLayout", "brightness_icon");
                    layoutParams = null;

                }

                if (child.getId() == getResources().getIdentifier("brightnessSeekBar_text", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 60 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 40 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.setMargins(0, 0, 8 * StaticVariableUtils.widthPixels / 1920, 0);
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    Log.d("CustomFrameLayout", "brightnessSeekBar_text");
                    layoutParams = null;

                }
            }
        }

        //音量条
        if (getId() == getResources().getIdentifier("volume_frame", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("volumeSeekBar", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
//                    layoutParams.width = 380 * StaticVariableUtils.widthPixels / 1920;
                    if (2000 < StaticVariableUtils.widthPixels && StaticVariableUtils.widthPixels < 3000) {
                        layoutParams.height = (40 * StaticVariableUtils.heightPixels / 1080 + 20);
                    } else if (StaticVariableUtils.widthPixels > 3000) {
                        layoutParams.height = (40 * StaticVariableUtils.heightPixels / 1080 + 30);
                    } else {
                        layoutParams.height = 40 * StaticVariableUtils.heightPixels / 1080;
                    }
                    child.setLayoutParams(layoutParams);
                    Log.d("CustomFrameLayout", "volumeSeekBar");
                    layoutParams = null;

                }

                if (child.getId() == getResources().getIdentifier("volume_icon", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 24 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 24 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.setMargins(8 * StaticVariableUtils.widthPixels / 1920, 0, 0, 0);
                    child.setLayoutParams(layoutParams);
                    Log.d("CustomFrameLayout", "volume_icon");
                    layoutParams = null;

                }

                if (child.getId() == getResources().getIdentifier("volumeSeekBar_text", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 60 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 40 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.setMargins(0, 0, 8 * StaticVariableUtils.widthPixels / 1920, 0);
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    Log.d("CustomFrameLayout", "volumeSeekBar_text");
                    layoutParams = null;

                }
            }
        }

        if (getId() == getResources().getIdentifier("notification_center_title", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("center_text", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20*StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);

                    layoutParams = null;
                    Log.d("CustomLinearLayout", "center_text");

                }

                if (child.getId() == getResources().getIdentifier("quit", "id", getContext().getPackageName())) {
                    //notification_center
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width=22*StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 22*StaticVariableUtils.heightPixels / 1080;
                    child.setLayoutParams(layoutParams);
                    child.measure(MeasureSpec.makeMeasureSpec(22*StaticVariableUtils.widthPixels / 1920, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(22*StaticVariableUtils.heightPixels / 1080, MeasureSpec.EXACTLY));
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "quit");

                }
            }
        }

        if (getId() == getResources().getIdentifier("frame3", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("content", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
//                    layoutParams.width = 278*StaticVariableUtils.widthPixels / 1920;
//                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "content");

                }

                if (child.getId() == getResources().getIdentifier("Up_Or_Down", "id", getContext().getPackageName())) {
                    //notification_center
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 40*StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 40*StaticVariableUtils.heightPixels / 1080;
                    child.setLayoutParams(layoutParams);
                    child.measure(MeasureSpec.makeMeasureSpec(40*StaticVariableUtils.widthPixels / 1920, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(40*StaticVariableUtils.heightPixels / 1080, MeasureSpec.EXACTLY));
                    layoutParams = null;
                    Log.d("CustomLinearLayout", "Up_Or_Down");

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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    private void initView() {
        ViewGroup.MarginLayoutParams layoutParams;
        if (getId() == getResources().getIdentifier("frame1", "id", getContext().getPackageName())) {

        }
    }
}
