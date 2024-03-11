package com.color.systemui.view.navibar.new_navibar_source;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.color.osd.R;
import com.color.systemui.utils.StaticVariableUtils;

public class NewSourceLinearLayout extends LinearLayout {

    public NewSourceLinearLayout(Context context) {
        this(context, null);
    }

    public NewSourceLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewSourceLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        ViewGroup.MarginLayoutParams layoutParams;
//        //1、notification.xml
        if (getId() == getResources().getIdentifier("new_source_linear", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("First_line", "id", getContext().getPackageName())) {
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = 70 * StaticVariableUtils.heightPixels / 1080;
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                }

                if (child.getId() == getResources().getIdentifier("Second_line", "id", getContext().getPackageName())) {
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = 200 * StaticVariableUtils.heightPixels / 1080;
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                }

                if (child.getId() == getResources().getIdentifier("Third_line", "id", getContext().getPackageName())) {
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = 230 * StaticVariableUtils.heightPixels / 1080;
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                }

            }
        }

        if (getId() == getResources().getIdentifier("First_line", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("text", "id", getContext().getPackageName())) {
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 520 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 40 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.setMarginStart(20 * StaticVariableUtils.widthPixels / 1920);
                    TextView textView = (TextView) child;
                    textView.setText(R.string.soure_change);
                    ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, 26 * StaticVariableUtils.heightPixels / 1080);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                }

                if (child.getId() == getResources().getIdentifier("X", "id", getContext().getPackageName())) {
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 40 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 40 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.setMarginEnd(20 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                }
            }
        }

        if (getId() == getResources().getIdentifier("Second_line", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("OPS", "id", getContext().getPackageName())) {
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 130 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 130 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.setMarginStart(140 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.setMarginEnd(60 * StaticVariableUtils.widthPixels / 1920);
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                }

                if (child.getId() == getResources().getIdentifier("Android", "id", getContext().getPackageName())) {
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 130 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 130 * StaticVariableUtils.heightPixels / 1080;
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                }
            }
        }

        if (getId() == getResources().getIdentifier("Third_line", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("HDMI1", "id", getContext().getPackageName())) {
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 130 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 130 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.setMarginStart(140 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.setMarginEnd(60 * StaticVariableUtils.widthPixels / 1920);
                    layoutParams.topMargin = 20 * StaticVariableUtils.heightPixels / 1080;
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                }

                if (child.getId() == getResources().getIdentifier("HDMI2", "id", getContext().getPackageName())) {
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 130 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 130 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.topMargin = 20 * StaticVariableUtils.heightPixels / 1080;
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                }
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
