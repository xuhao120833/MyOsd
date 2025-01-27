package com.color.systemui.view.navibar.navibar_source;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.color.osd.R;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import android.view.View;
import android.view.Display;
import android.util.TypedValue;
import android.widget.TextView;

public class SourceLinearLayout extends LinearLayout implements Instance {

    int modeWidth;
    int modeHeight;

    Display defaultDisplay;

    LayoutParams textlp, Xlp;

    LayoutParams opslp, androidlp, hdmi1lp, hdmi2lp;

    TextView text;

    SecondLinearLayout secondLinearLayout;

    ThirdLinearLayout thirdLinearLayout;

    FirstLinearLayout firstLinearLayout;

    View X;

    public int sizeWidth;
    public int sizeHeight;

    public SourceLinearLayout(Context context) {
        super(context);
    }

    public SourceLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//分辨率变化，onMeasure会被执行四次
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
//        sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
//        sizeWidth = sizeHeight * 1920 / 1080;

        sizeHeight = StaticVariableUtils.heightPixels;
        sizeWidth = StaticVariableUtils.widthPixels;

//        Log.d("sizefWidth SourceLinearLayout", String.valueOf(sizeWidth));
//        Log.d("sizefHeight SourceLinearLayout", String.valueOf(sizeHeight));

        Log.d("SourceLinearLayout", String.valueOf(sizeWidth));
        Log.d("SourceLinearLayout", String.valueOf(sizeHeight));

        //MeasureSource();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if(i==0) {
                child.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 600 / 1920, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(sizeHeight * 70 / 1080, MeasureSpec.EXACTLY));
            }
            if(i==1) {
                child.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 600 / 1920, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(sizeHeight * 200 / 1080, MeasureSpec.EXACTLY));
            }
            if(i==2) {
                child.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 600 / 1920, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(sizeHeight * 230 / 1080, MeasureSpec.EXACTLY));
            }
        }

        setMeasuredDimension(sizeWidth * 600 / 1920, sizeHeight * 500 / 1080);
    }

    public void MeasureSource() {

        //firstLinearLayout
//        firstLinearLayout = STATIC_INSTANCE_UTILS.source.Source.findViewById(R.id.First_line);
//        firstLinearLayout.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 600 / 1920, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(sizeHeight * 70 / 1080, MeasureSpec.EXACTLY));

        //标题textview
//        text = STATIC_INSTANCE_UTILS.source.Source.findViewById(R.id.text);
//        text.setText(R.string.soure_change);
//        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeHeight * 28 / 1080);
//        textlp = (LayoutParams) text.getLayoutParams();
//        textlp.leftMargin = sizeWidth * 20 / 1920;
//        textlp.topMargin = sizeHeight * 40 / 1080;
//        text.setLayoutParams(textlp);
//        text.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 520 / 1920, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));

        //X
//        X = STATIC_INSTANCE_UTILS.source.Source.findViewById(R.id.X);
//        Xlp = (LayoutParams) X.getLayoutParams();
//        Xlp.topMargin = sizeHeight * 24 / 1080;
//        X.setLayoutParams(Xlp);
//        X.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 40 / 1920, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));

        //secondLinearLayout
//        secondLinearLayout = STATIC_INSTANCE_UTILS.source.Source.findViewById(R.id.Second_line);
//        secondLinearLayout.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 600 / 1920, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(sizeHeight * 200 / 1080, MeasureSpec.EXACTLY));

        //信源 OPS
//        opslp = (LayoutParams) STATIC_INSTANCE_UTILS.source.OPS.getLayoutParams();
//        opslp.leftMargin = sizeWidth * 140 / 1920;
//        opslp.rightMargin = sizeWidth * 60 / 1920;
//        STATIC_INSTANCE_UTILS.source.OPS.setLayoutParams(opslp);
//        STATIC_INSTANCE_UTILS.source.OPS.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 130 / 1920, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(sizeHeight * 130 / 1080, MeasureSpec.EXACTLY));
//
//        //Android
//        STATIC_INSTANCE_UTILS.source.Android.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 130 / 1920, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(sizeHeight * 130 / 1080, MeasureSpec.EXACTLY));

        //thirdLinearLayout
//        thirdLinearLayout = STATIC_INSTANCE_UTILS.source.Source.findViewById(R.id.Third_line);
//        thirdLinearLayout.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 600 / 1920, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(sizeHeight * 230 / 1080, MeasureSpec.EXACTLY));

        //HDMI1
//        hdmi1lp = (LayoutParams) STATIC_INSTANCE_UTILS.source.HDMI1.getLayoutParams();
//        hdmi1lp.leftMargin = sizeWidth * 140 / 1920;
//        hdmi1lp.rightMargin = sizeWidth * 60 / 1920;
//        STATIC_INSTANCE_UTILS.source.HDMI1.setLayoutParams(hdmi1lp);
//        STATIC_INSTANCE_UTILS.source.HDMI1.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 130 / 1920, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(sizeHeight * 130 / 1080, MeasureSpec.EXACTLY));
//
//        //HDMI2
//        STATIC_INSTANCE_UTILS.source.HDMI2.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 130 / 1920, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(sizeHeight * 130 / 1080, MeasureSpec.EXACTLY));
    }

}