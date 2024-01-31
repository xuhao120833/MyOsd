package com.color.notification.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.color.notification.utils.UiAdaptationUtils;
import com.color.systemui.utils.StaticVariableUtils;

public class CustomRecyclerView extends RecyclerView {

    private Paint paint;
    private Bitmap blurredBitmap;

    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        ViewGroup.MarginLayoutParams layoutParams;
        if(getId() == getResources().getIdentifier("notification_quick_settings", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getId() == getResources().getIdentifier("frame2", "id", getContext().getPackageName())) {
                    Log.d("CustomRecyclerView"," frame2 ");
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                }
            }

            Log.d("CustomRecyclerView"," notification_quick_settings ");

        }
//
//        if(getId() == getResources().getIdentifier("notification_center", "id", getContext().getPackageName())) {
//            int count = getChildCount();
//            for (int i = 0; i < count; i++) {
//                View child = getChildAt(i);
//                if (child.getId() == getResources().getIdentifier("frame1", "id", getContext().getPackageName())) {
//                    Log.d("CustomRecyclerView"," frame1 ");
//                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
//                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    child.setLayoutParams(layoutParams);
//                    layoutParams = null;
//                }
//
//                if (child.getId() == getResources().getIdentifier("linearlanya", "id", getContext().getPackageName())) {
//                    Log.d("CustomRecyclerView"," linearlanya ");
//                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
//                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    child.setLayoutParams(layoutParams);
//                    layoutParams = null;
//                }
//
//            }
//            Log.d("CustomRecyclerView"," notification_center ");
//        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);


    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
