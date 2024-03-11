package com.color.systemui.view.navibar.new_navibar_source;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.color.systemui.utils.StaticVariableUtils;

public class NewSourceFrameLayout extends FrameLayout {

    public NewSourceFrameLayout(Context context) {
        this(context, null);
    }

    public NewSourceFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewSourceFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        ViewGroup.MarginLayoutParams layoutParams;
//        //1、notification.xml
        if (getId() == getResources().getIdentifier("new_source_frame", "id", getContext().getPackageName())) {
            int count = getChildCount();
            //4个
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("new_source_linear", "id", getContext().getPackageName())) {
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = 600 * StaticVariableUtils.widthPixels / 1920;
                    layoutParams.height = 500 * StaticVariableUtils.heightPixels / 1080;
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                }
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
