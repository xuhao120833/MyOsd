package com.color.osd.ui.views.new_dialogmenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.color.systemui.utils.StaticVariableUtils;

public class NewDialogLinearLayout extends LinearLayout {

    private boolean isFocused = false;
    private boolean drawOverlay = false; // 是否绘制遮罩层
    private Paint paint = new Paint();
    private RectF rectF = new RectF();

    public NewDialogLinearLayout(Context context) {
        this(context, null);
    }

    public NewDialogLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewDialogLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initView();
        init();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("NewDialogLinearLayout", " 宽 高 " + StaticVariableUtils.widthPixels + StaticVariableUtils.heightPixels);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        ViewGroup.MarginLayoutParams layoutParams;

        if (getId() == getResources().getIdentifier("Menu_linear_list", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("Menu_source", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 112 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.setMarginStart(28 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.topMargin = 16 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.bottomMargin = 16 * StaticVariableUtils.heightPixels / 1080;
                    child.setPadding(0,8 * StaticVariableUtils.heightPixels / 1080,0,8 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_source");

                }
                if (child.getId() == getResources().getIdentifier("Menu_brightness", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 112 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.topMargin = 16 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.bottomMargin = 16 * StaticVariableUtils.heightPixels / 1080;
                    child.setPadding(0,8 * StaticVariableUtils.heightPixels / 1080,0,8 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_brightness");

                }
                if (child.getId() == getResources().getIdentifier("Menu_volume", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 112 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.topMargin = 16 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.bottomMargin = 16 * StaticVariableUtils.heightPixels / 1080;
                    child.setPadding(0,8 * StaticVariableUtils.heightPixels / 1080,0,8 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_volume");

                }
                if (child.getId() == getResources().getIdentifier("Menu_recent", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 112 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.topMargin = 16 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.bottomMargin = 16 * StaticVariableUtils.heightPixels / 1080;
                    child.setPadding(0,8 * StaticVariableUtils.heightPixels / 1080,0,8 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_recent");

                }
                if (child.getId() == getResources().getIdentifier("Menu_screenshot", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 112 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.topMargin = 16 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.bottomMargin = 16 * StaticVariableUtils.heightPixels / 1080;
                    child.setPadding(0,8 * StaticVariableUtils.heightPixels / 1080,0,8 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_screenshot");

                }
                if (child.getId() == getResources().getIdentifier("Menu_comments", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 112 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.setMarginEnd(28 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.topMargin = 16 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.bottomMargin = 16 * StaticVariableUtils.heightPixels / 1080;
                    child.setPadding(0,8 * StaticVariableUtils.heightPixels / 1080,0,8 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_comments");

                }
            }
        }

        if (getId() == getResources().getIdentifier("Menu_source", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("Menu_source_image", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 32 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 32 * StaticVariableUtils.heightPixels / 1080;
//                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_source_image");

                }

                if (child.getId() == getResources().getIdentifier("Menu_source_text", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_source_text");
                }
            }
        }

        if (getId() == getResources().getIdentifier("Menu_brightness", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("Menu_brightness_image", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 32 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 32 * StaticVariableUtils.heightPixels / 1080;
//                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_brightness_image");

                }

                if (child.getId() == getResources().getIdentifier("Menu_brightness_text", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_brightness_text");
                }
            }
        }

        if (getId() == getResources().getIdentifier("Menu_volume", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("Menu_volume_image", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 32 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 32 * StaticVariableUtils.heightPixels / 1080;
//                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_volume_image");

                }

                if (child.getId() == getResources().getIdentifier("Menu_volume_text", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_volume_text");
                }
            }
        }

        if (getId() == getResources().getIdentifier("Menu_recent", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("Menu_recent_image", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 32 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 32 * StaticVariableUtils.heightPixels / 1080;
//                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_recent_image");

                }

                if (child.getId() == getResources().getIdentifier("Menu_recent_text", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_recent_text");
                }
            }
        }

        if (getId() == getResources().getIdentifier("Menu_screenshot", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("Menu_screenshot_image", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 32 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 32 * StaticVariableUtils.heightPixels / 1080;
//                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_screenshot_image");

                }

                if (child.getId() == getResources().getIdentifier("Menu_screenshot_text", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_screenshot_text");
                }
            }
        }

        if (getId() == getResources().getIdentifier("Menu_comments", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("Menu_comments_image", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 32 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 32 * StaticVariableUtils.heightPixels / 1080;
//                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_comments_image");

                }

                if (child.getId() == getResources().getIdentifier("Menu_comments_text", "id", getContext().getPackageName())) {
                    //quick_settings_frame
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                    Log.d("NewDialogLinearLayout", "Menu_comments_text");
                }
            }
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        paint.setAntiAlias(true);
        paint.setColor(0x80FFFFFF); // 白色半透明背景
        paint.setStyle(Paint.Style.FILL);
    }

    // 设置是否绘制遮罩层
    public void setDrawOverlay(boolean drawOverlay) {
        this.drawOverlay = drawOverlay;
        invalidate();
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        isFocused = gainFocus;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (isFocused) {
//            rectF.set(0, 0, getWidth(), getHeight());
//            canvas.drawRect(rectF, paint);

            int radius = 20; // 圆角半径，根据需要调整
            rectF.set(0, 0, getWidth(), getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
        }
    }

}