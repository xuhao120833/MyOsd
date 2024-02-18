package com.color.systemui.view.new_statusbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.color.systemui.utils.StaticVariableUtils;

public class NewStatusBarFrameLayout extends FrameLayout {

    public NewStatusBarFrameLayout(Context context) {
        this(context, null);
    }

    public NewStatusBarFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewStatusBarFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        ViewGroup.MarginLayoutParams layoutParams;
//        //1、notification.xml
        if (getId() == getResources().getIdentifier("statusbar_icon_frame", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("statusbar_icon", "id", getContext().getPackageName())) {
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 40 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 40 * StaticVariableUtils.heightPixels / 1080;
                    layoutParams.setMargins(8 * StaticVariableUtils.widthPixels / 1920, 8 * StaticVariableUtils.heightPixels / 1080, 8 * StaticVariableUtils.widthPixels / 1920, 0);
                    child.setLayoutParams(layoutParams);
                }
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
