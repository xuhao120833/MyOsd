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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.color.notification.utils.UiAdaptationUtils;
import com.color.systemui.utils.StaticVariableUtils;

public class CustomImageView extends androidx.appcompat.widget.AppCompatImageView {

    private Paint paint;
    private Bitmap blurredBitmap;

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (getId() == getResources().getIdentifier("up", "id", getContext().getPackageName())) {
            setMeasuredDimension(sizeWidth, sizeHeight);
        }

        if (getId() == getResources().getIdentifier("down", "id", getContext().getPackageName())) {
            setMeasuredDimension(sizeWidth, sizeHeight);
        }

        if (getId() == getResources().getIdentifier("quit", "id", getContext().getPackageName())) {
            setMeasuredDimension(sizeWidth, sizeHeight);
        }

        if (getId() == getResources().getIdentifier("Icon", "id", getContext().getPackageName())) {
            setMeasuredDimension(sizeWidth, sizeHeight);
        }

        if (getId() == getResources().getIdentifier("Up_Or_Down", "id", getContext().getPackageName())) {
            setMeasuredDimension(sizeWidth, sizeHeight);
        }


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

//        Log.d("CustomNestedScrollView"," 改变X坐标 ");
//
//        if(getId() == getResources().getIdentifier("brightness_frame", "id", getContext().getPackageName())) {
//            Log.d("CustomFrameLayout"," 执行2 ");
//            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
//            layoutParams.setMargins(24 * StaticVariableUtils.widthPixels/1920,0,0,0);
//            setLayoutParams(layoutParams);
//        }
//
//        if(getId() == getResources().getIdentifier("volume_frame", "id", getContext().getPackageName())) {
//            Log.d("CustomFrameLayout"," 执行3 ");
//            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
//            layoutParams.setMargins(24 * StaticVariableUtils.widthPixels/1920,0,0,16 * StaticVariableUtils.heightPixels / 1080);
//            setLayoutParams(layoutParams);
//        }

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
