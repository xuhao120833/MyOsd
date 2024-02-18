package com.color.systemui.view.new_statusbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class NewStatusBarLinearLayout extends LinearLayout {

    public NewStatusBarLinearLayout(Context context) {
        this(context, null);
    }

    public NewStatusBarLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewStatusBarLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
