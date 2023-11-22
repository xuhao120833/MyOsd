package com.color.osd.ui.views.dialogmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.color.osd.R;
import com.color.osd.utils.ConstantProperties;
import com.color.osd.utils.DensityUtil;

public class DialogMenuFrameLayout extends FrameLayout {

    private View mLinearLayout;
    private View mSourceView, mBrightnessView, mVolumeView, mRecentsView, mScreenShotView, mCommentView;

    public DialogMenuFrameLayout(@NonNull Context context) {
        super(context);
    }

    public DialogMenuFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogMenuFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DialogMenuFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_WIDTH_DP);
        int heightSize = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_HEIGHT_DP);
        Log.d(getClass().getSimpleName() + "Adaptation-MyError", "widthSize=" + widthSize
                + ", heightSize=" + heightSize);

//        mLinearLayout = findViewById(R.id.Menu_linear_list);
//        mSourceView = mLinearLayout.findViewById(R.id.Menu_source);
//        mBrightnessView = mLinearLayout.findViewById(R.id.Menu_brightness);
//        mVolumeView = mLinearLayout.findViewById(R.id.Menu_volume);
//        mRecentsView = mLinearLayout.findViewById(R.id.Menu_recent);
//        mScreenShotView = mLinearLayout.findViewById(R.id.Menu_screenshot);
//        mCommentView = mLinearLayout.findViewById(R.id.Menu_comments);
//
//        setMenuItemSize(mSourceView, 1);
//        setMenuItemSize(mBrightnessView, 0);
//        setMenuItemSize(mVolumeView, 0);
//        setMenuItemSize(mRecentsView, 0);
//        setMenuItemSize(mScreenShotView, 0);
//        setMenuItemSize(mCommentView, -1);
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            child.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    /**
     *
     * @param view
     * @param firstOrLastFlag 标记变量，第一个为 1，最后一个为 -1
     */
    private void setMenuItemSize(View view, int firstOrLastFlag) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        int width, height;

        if (firstOrLastFlag == -1) {
            // 最后一个Item为“批注”按钮，其宽高与其他不同
            width = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_LAST_ITEM_WIDTH_DP);
            height = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_LAST_ITEM_HEIGHT_DP);
            layoutParams.rightMargin = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_ITEM_MARGIN_RIGHT_DP);
        } else if (firstOrLastFlag == 1) {
            width = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_ITEM_WIDTH_DP);
            height = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_ITEM_HEIGHT_DP);
            layoutParams.leftMargin = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_ITEM_MARGIN_LEFT_DP);
        } else {
            width = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_ITEM_WIDTH_DP);
            height = (int) DensityUtil.getScaledValue(ConstantProperties.OSD_MENU_ITEM_HEIGHT_DP);
        }

        layoutParams.width = width;
        layoutParams.height = height;

        view.setLayoutParams(layoutParams);
        view.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

}
