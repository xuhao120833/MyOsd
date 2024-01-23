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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.color.notification.utils.UiAdaptationUtils;

public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {

    private Paint paint;
    private Bitmap blurredBitmap;

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

//        if(getId() == getResources().getIdentifier("notification_quick_settings", "id", getContext().getPackageName())) {
//            Log.d("CustomRecyclerView"," 执行1 ");
//            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
//            layoutParams.setMargins(0,20* StaticVariableUtils.heightPixels/1080,0,20*StaticVariableUtils.heightPixels/1080);
//            setLayoutParams(layoutParams);
//        }
//
//        if(getId() == getResources().getIdentifier("notification_center", "id", getContext().getPackageName())) {
//            Log.d("CustomRecyclerView"," 执行2 ");
//            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
//            layoutParams.setMargins(0,10*StaticVariableUtils.heightPixels/1080,0,20*StaticVariableUtils.heightPixels/1080);
//            setLayoutParams(layoutParams);
//        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
