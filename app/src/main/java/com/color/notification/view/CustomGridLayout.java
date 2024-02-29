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
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.color.notification.utils.UiAdaptationUtils;
import com.color.systemui.utils.StaticVariableUtils;

public class CustomGridLayout extends GridLayout {

    private Paint paint;
    private Bitmap blurredBitmap;

    public CustomGridLayout(Context context) {
        super(context);
    }

    public CustomGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        ViewGroup.MarginLayoutParams layoutParams;
        if (getId() == getResources().getIdentifier("gridlayout", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("screenshot_linear", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.setMarginStart(24* StaticVariableUtils.widthPixels / 1920);
//                    layoutParams.setMargins(24* StaticVariableUtils.widthPixels / 1920 , 0,0,0);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomGridLayout", "screenshot_linear");

                }

                if (child.getId() == getResources().getIdentifier("eye_protection_linear", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.setMarginStart(46* StaticVariableUtils.widthPixels / 1920);
//                    layoutParams.setMargins(46* StaticVariableUtils.widthPixels / 1920 , 0,0,0);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomGridLayout", "eye_protection_linear");

                }

                if (child.getId() == getResources().getIdentifier("camera_linear", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.setMarginStart(46* StaticVariableUtils.widthPixels / 1920);
//                    layoutParams.setMargins(46* StaticVariableUtils.widthPixels / 1920 , 0,0,0);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomGridLayout", "camera_linear");

                }

                if (child.getId() == getResources().getIdentifier("screenrecord_linear", "id", getContext().getPackageName())) {
                    //notification_center_title
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.setMarginStart(46* StaticVariableUtils.widthPixels / 1920);
                    layoutParams.setMarginEnd(24* StaticVariableUtils.widthPixels / 1920);
//                    layoutParams.setMargins(46* StaticVariableUtils.widthPixels / 1920 , 0,24* StaticVariableUtils.widthPixels / 1920,0);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("CustomGridLayout", "screenrecord_linear");

                }


            }
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

//        Log.d("CustomGridLayout"," 改变X坐标 ");
//        if(getId() == getResources().getIdentifier("quick_settings_frame", "id", getContext().getPackageName())) {
//            Log.d("CustomFrameLayout"," 执行1 ");
//            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
//            layoutParams.setMargins(0,0,0,20 * StaticVariableUtils.widthPixels / 1920);
////            layoutParams.bottomMargin = 20 * StaticVariableUtils.widthPixels / 1920;
//            setLayoutParams(layoutParams);
//        }
//
//        if(getId() == getResources().getIdentifier("brightness_frame", "id", getContext().getPackageName())) {
//            Log.d("CustomFrameLayout"," 执行2 ");
//            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
//            layoutParams.setMargins(24 * StaticVariableUtils.widthPixels/1920,0,0,0);
//            setLayoutParams(layoutParams);
//        }
//
//        if(getId() == getResources().getIdentifier("volume_frame", "id", getContext().getPackageName())) {
//            Log.d("CustomFrameLayout"," 执行3 ");
//            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
//            layoutParams.setMargins(24 * StaticVariableUtils.widthPixels/1920,0,0,16 * StaticVariableUtils.heightPixels / 1080);
//            setLayoutParams(layoutParams);
//        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
