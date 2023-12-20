package com.color.osd.ui.views.dialogmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.color.osd.utils.ConstantProperties;
import com.color.osd.utils.DensityUtil;

public class DialogMenuLinearLayout extends LinearLayout {
    public DialogMenuLinearLayout(Context context) {
        super(context);
    }

    public DialogMenuLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogMenuLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DialogMenuLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = getMeasuredWidth();// MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = getMeasuredHeight();// MeasureSpec.getSize(heightMeasureSpec);

        //Log.d(getClass().getSimpleName() + "Adaptation-MyError", "widthMode="+widthMode
                //+ ", width=" + widthSize + ", height=" + heightSize);
        //setMeasuredDimension(widthSize, heightSize);

        int count = getChildCount();
        for (int i=0; i < count; i++) {
            View child = getChildAt(i);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();

            int width = layoutParams.width;
            int height = layoutParams.height;
            if (child instanceof DialogMenuItemView) {
                if (i == count - 1) {
                    // 最后一个Item为“批注”按钮，其宽高与其他不同
                    width = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_LAST_ITEM_WIDTH_DP);
                    height = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_LAST_ITEM_HEIGHT_DP);
                    layoutParams.rightMargin = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_ITEM_MARGIN_RIGHT_DP);
                } else if (i == 0) {
                    width = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_ITEM_WIDTH_DP);
                    height = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_ITEM_HEIGHT_DP);
                    layoutParams.leftMargin = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_ITEM_MARGIN_LEFT_DP);
                } else {
                    width = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_ITEM_WIDTH_DP);
                    height = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_ITEM_HEIGHT_DP);
                }
            }
            layoutParams.width = width;
            layoutParams.height = height;

            child.setLayoutParams(layoutParams);
            child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        }

        // setMeasuredDimension(widthSize, heightSize);
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
