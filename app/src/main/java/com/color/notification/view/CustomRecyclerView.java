package com.color.notification.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.color.notification.models.ItemTouchHelperCallback;
import com.color.notification.models.Notification_Center_Adapter;
import com.color.notification.utils.UiAdaptationUtils;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.StaticVariableUtils;

public class CustomRecyclerView extends RecyclerView implements Instance {

    private Paint paint;
    private Bitmap blurredBitmap;

    private ItemTouchHelper itemTouchHelper;
    private ItemTouchHelperCallback itemTouchHelperCallback;

    public CustomRecyclerView(Context context) {
        this(context, null);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
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

        if(getId() == getResources().getIdentifier("notification_center", "id", getContext().getPackageName())) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
//                if (child.getId() == getResources().getIdentifier("frame1", "id", getContext().getPackageName())) {
//                    Log.d("CustomRecyclerView"," frame1 ");
//                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
//                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    child.setLayoutParams(layoutParams);
//                    layoutParams = null;
//                }

                if (child.getId() == getResources().getIdentifier("linearlanya", "id", getContext().getPackageName())) {
                    Log.d("CustomRecyclerView"," linearlanya ");
                    layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    child.setLayoutParams(layoutParams);
                    layoutParams = null;
                }

            }
            Log.d("CustomRecyclerView"," notification_center ");
        }

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

    public void init() {

    }

}
