package com.color.systemui.view.navibar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.color.osd.R;
import com.color.systemui.utils.StaticInstanceUtils;

public class NaviBarFrameLayout extends FrameLayout {

    Context mycontext;

    WindowManager windowManager;

    View mynaviFrame, mynaviLinear;

    View mynaviFrame2, mynaviLinear2;

    View myBack, myHome, myRecent, myVolume, myComments, myWhiteboard, myCollapse, myleftDline1, myleftDline2;

    View myBack2, myHome2, myRecent2, myVolume2, myComments2, myWhiteboard2, myCollapse2, myrightDline1, myrightDline2;

    LayoutParams lpnaviLinear, lpnaviLinear2;

    android.widget.LinearLayout.LayoutParams lpnaviBack, lpnaviHome, lpnaviRecent, lpnavileftDline1, lpnaviVolume, lpnaviComments, lpnaviWhiteboard, lpnavileftDline2, lpnaviCollapse;
    android.widget.LinearLayout.LayoutParams lpnaviBack2, lpnaviHome2, lpnaviRecent2, lpnavirightDline1, lpnaviVolume2, lpnaviComments2, lpnaviWhiteboard2, lpnavirightDline2, lpnaviCollapse2;

    int modeWidth;
    int modeHeight;

    int sizeWidth;
    int sizeHeight;

    static int measure = 0;


    public NaviBarFrameLayout(@NonNull Context context) {
        super(context);
    }


    public NaviBarFrameLayout(Context context, @Nullable AttributeSet attrs) {//分辨率变化会被执行两次
        super(context, attrs);
        mycontext = context;
        windowManager = mycontext.getSystemService(WindowManager.class);
        Log.d("MyFrameLayout(Context context, @Nullable AttributeSet attrs)", "获取context");
    }


    //onMeasure计算子视图的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//分辨率变化，onMeasure会被执行四次
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //sizeWidth = MeasureSpec.getSize(widthMeasureSpec);

        //父view可以给多大空间
        sizeHeight = MeasureSpec.getSize(heightMeasureSpec);//分辨率的高度
        sizeWidth = sizeHeight * 1920 / 1080;//分辨率宽

        Log.d("sizef mode  widthMeasureSpec", String.valueOf(widthMeasureSpec));
        Log.d("sizef mode  heightMeasureSpec", String.valueOf(heightMeasureSpec));
        Log.d("sizefWidth  StatusBarFrameLayout", String.valueOf(sizeWidth));
        Log.d("sizefHeight  StatusBarFrameLayout", String.valueOf(sizeHeight));

        //左侧导航栏
        MeasureLeftNavibar();

        //右侧导航栏
        MeasureRightNavibar();


        setMeasuredDimension(sizeWidth * 108 / 1920, sizeHeight * 508 / 1080); //告诉父view自己要多大空间


    }

    private void MeasureLeftNavibar() {
        mynaviFrame = StaticInstanceUtils.navigationBar.leftNavibar.findViewById(R.id.naviFrame);

        mynaviLinear = StaticInstanceUtils.navigationBar.leftNavibar.findViewById(R.id.naviLinear);
        lpnaviLinear = (LayoutParams) mynaviLinear.getLayoutParams();
        lpnaviLinear.leftMargin = sizeWidth * 24 / 1920;
        mynaviLinear.setLayoutParams(lpnaviLinear);
        mynaviLinear.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 84 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 508 / 1080, MeasureSpec.EXACTLY));


        myBack = StaticInstanceUtils.navigationBar.leftNavibar.findViewById(R.id.Back);
        lpnaviBack = (android.widget.LinearLayout.LayoutParams) myBack.getLayoutParams();
        lpnaviBack.topMargin = sizeWidth * 20 / 1080;
        lpnaviBack.bottomMargin = sizeHeight * 10 / 1080;
        Log.d("sizef Backbottom", String.valueOf(sizeHeight * 10 / 1080));
        myBack.setLayoutParams(lpnaviBack);
        myBack.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 60 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));


        myHome = StaticInstanceUtils.navigationBar.leftNavibar.findViewById(R.id.Home);
        lpnaviHome = (android.widget.LinearLayout.LayoutParams) myHome.getLayoutParams();
        lpnaviHome.topMargin = sizeHeight * 10 / 1080;
        lpnaviHome.bottomMargin = sizeHeight * 10 / 1080;
        myHome.setLayoutParams(lpnaviHome);
        myHome.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 60 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));


        myRecent = StaticInstanceUtils.navigationBar.leftNavibar.findViewById(R.id.Recent);
        lpnaviRecent = (android.widget.LinearLayout.LayoutParams) myRecent.getLayoutParams();
        lpnaviRecent.topMargin = sizeHeight * 10 / 1080;
        lpnaviRecent.bottomMargin = sizeHeight * 10 / 1080;
        myRecent.setLayoutParams(lpnaviRecent);
        myRecent.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 60 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));


        myleftDline1 = StaticInstanceUtils.navigationBar.leftNavibar.findViewById(R.id.Dline1);
        lpnavileftDline1 = (android.widget.LinearLayout.LayoutParams) myleftDline1.getLayoutParams();
        lpnavileftDline1.topMargin = sizeHeight * 10 / 1080;
        lpnavileftDline1.bottomMargin = sizeHeight * 10 / 1080;
        myleftDline1.setLayoutParams(lpnavileftDline1);
        myleftDline1.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 40 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 2 / 1080, MeasureSpec.EXACTLY));


        myVolume = StaticInstanceUtils.navigationBar.leftNavibar.findViewById(R.id.Source);
        lpnaviVolume = (android.widget.LinearLayout.LayoutParams) myVolume.getLayoutParams();
        lpnaviVolume.topMargin = sizeHeight * 10 / 1080;
        lpnaviVolume.bottomMargin = sizeHeight * 10 / 1080;
        myVolume.setLayoutParams(lpnaviVolume);
        myVolume.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 60 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));


        myComments = StaticInstanceUtils.navigationBar.leftNavibar.findViewById(R.id.Comments);
        lpnaviComments = (android.widget.LinearLayout.LayoutParams) myComments.getLayoutParams();
        lpnaviComments.topMargin = sizeHeight * 10 / 1080;
        lpnaviComments.bottomMargin = sizeHeight * 10 / 1080;
        myComments.setLayoutParams(lpnaviComments);
        myComments.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 60 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));


        myWhiteboard = StaticInstanceUtils.navigationBar.leftNavibar.findViewById(R.id.Whiteboard);
        lpnaviWhiteboard = (android.widget.LinearLayout.LayoutParams) myWhiteboard.getLayoutParams();
        lpnaviWhiteboard.topMargin = sizeHeight * 10 / 1080;
        lpnaviWhiteboard.bottomMargin = sizeHeight * 10 / 1080;
        myWhiteboard.setLayoutParams(lpnaviWhiteboard);
        myWhiteboard.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 60 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));


        myleftDline2 = StaticInstanceUtils.navigationBar.leftNavibar.findViewById(R.id.Dline2);
        lpnavileftDline2 = (android.widget.LinearLayout.LayoutParams) myleftDline2.getLayoutParams();
        lpnavileftDline2.topMargin = sizeHeight * 10 / 1080;
        lpnavileftDline2.bottomMargin = sizeHeight * 10 / 1080;
        myleftDline2.setLayoutParams(lpnavileftDline2);
        myleftDline2.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 40 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 2 / 1080, MeasureSpec.EXACTLY));


        myCollapse = StaticInstanceUtils.navigationBar.leftNavibar.findViewById(R.id.Collapse);
        lpnaviCollapse = (android.widget.LinearLayout.LayoutParams) myCollapse.getLayoutParams();
        lpnaviCollapse.topMargin = sizeHeight * 10 / 1080;
//        lpnaviCollapse.bottomMargin=sizeHeight*16/1080;
        myCollapse.setLayoutParams(lpnaviCollapse);
        myCollapse.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 60 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));


//        Navibarfloat.lp.y = sizeHeight * Navibarfloat.lp.y / 1080;
//        MySystemUI.avts.wm.updateViewLayout(Navibarfloat.Nbfloat, Navibarfloat.lp);

    }

    private void MeasureRightNavibar() {
        mynaviFrame2 = StaticInstanceUtils.navigationBar.rightNavibar.findViewById(R.id.naviFrame2);

        mynaviLinear2 = StaticInstanceUtils.navigationBar.rightNavibar.findViewById(R.id.naviLinear2);
        lpnaviLinear2 = (LayoutParams) mynaviLinear2.getLayoutParams();
        lpnaviLinear2.rightMargin = sizeWidth * 24 / 1920;
        mynaviLinear2.setLayoutParams(lpnaviLinear2);
        mynaviLinear2.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 84 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 508 / 1080, MeasureSpec.EXACTLY));


        myBack2 = StaticInstanceUtils.navigationBar.rightNavibar.findViewById(R.id.Back2);
        lpnaviBack2 = (android.widget.LinearLayout.LayoutParams) myBack2.getLayoutParams();
        lpnaviBack2.topMargin = sizeWidth * 20 / 1080;
        lpnaviBack2.bottomMargin = sizeHeight * 10 / 1080;
        Log.d("sizef Backbottom", String.valueOf(sizeHeight * 10 / 1080));
        myBack2.setLayoutParams(lpnaviBack2);
        myBack2.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 60 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));


        myHome2 = StaticInstanceUtils.navigationBar.rightNavibar.findViewById(R.id.Home2);
        lpnaviHome2 = (android.widget.LinearLayout.LayoutParams) myHome2.getLayoutParams();
        lpnaviHome2.topMargin = sizeHeight * 10 / 1080;
        lpnaviHome2.bottomMargin = sizeHeight * 10 / 1080;
        myHome2.setLayoutParams(lpnaviHome2);
        myHome2.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 60 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));


        myRecent2 = StaticInstanceUtils.navigationBar.rightNavibar.findViewById(R.id.Recent2);
        lpnaviRecent2 = (android.widget.LinearLayout.LayoutParams) myRecent2.getLayoutParams();
        lpnaviRecent2.topMargin = sizeHeight * 10 / 1080;
        lpnaviRecent2.bottomMargin = sizeHeight * 10 / 1080;
        myRecent2.setLayoutParams(lpnaviRecent2);
        myRecent2.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 60 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));


        myrightDline1 = StaticInstanceUtils.navigationBar.rightNavibar.findViewById(R.id.Dline1);
        lpnavirightDline1 = (android.widget.LinearLayout.LayoutParams) myrightDline1.getLayoutParams();
        lpnavirightDline1.topMargin = sizeHeight * 10 / 1080;
        lpnavirightDline1.bottomMargin = sizeHeight * 10 / 1080;
        myrightDline1.setLayoutParams(lpnavirightDline1);
        myrightDline1.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 40 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 2 / 1080, MeasureSpec.EXACTLY));


        myVolume2 = StaticInstanceUtils.navigationBar.rightNavibar.findViewById(R.id.Source2);
        lpnaviVolume2 = (android.widget.LinearLayout.LayoutParams) myVolume2.getLayoutParams();
        lpnaviVolume2.topMargin = sizeHeight * 10 / 1080;
        lpnaviVolume2.bottomMargin = sizeHeight * 10 / 1080;
        myVolume2.setLayoutParams(lpnaviVolume2);
        myVolume2.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 60 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));


        myComments2 = StaticInstanceUtils.navigationBar.rightNavibar.findViewById(R.id.Comments2);
        lpnaviComments2 = (android.widget.LinearLayout.LayoutParams) myComments2.getLayoutParams();
        lpnaviComments2.topMargin = sizeHeight * 10 / 1080;
        lpnaviComments2.bottomMargin = sizeHeight * 10 / 1080;
        myComments2.setLayoutParams(lpnaviComments2);
        myComments2.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 60 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));


        myWhiteboard2 = StaticInstanceUtils.navigationBar.rightNavibar.findViewById(R.id.Whiteboard2);
        lpnaviWhiteboard2 = (android.widget.LinearLayout.LayoutParams) myWhiteboard2.getLayoutParams();
        lpnaviWhiteboard2.topMargin = sizeHeight * 10 / 1080;
        lpnaviWhiteboard2.bottomMargin = sizeHeight * 10 / 1080;
        myWhiteboard2.setLayoutParams(lpnaviWhiteboard2);
        myWhiteboard2.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 60 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));


        myrightDline2 = StaticInstanceUtils.navigationBar.rightNavibar.findViewById(R.id.Dline2);
        lpnavirightDline2 = (android.widget.LinearLayout.LayoutParams) myrightDline2.getLayoutParams();
        lpnavirightDline2.topMargin = sizeHeight * 10 / 1080;
        lpnavirightDline2.bottomMargin = sizeHeight * 10 / 1080;
        myrightDline2.setLayoutParams(lpnavirightDline2);
        myrightDline2.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 40 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 2 / 1080, MeasureSpec.EXACTLY));


        myCollapse2 = StaticInstanceUtils.navigationBar.rightNavibar.findViewById(R.id.Collapse2);
        lpnaviCollapse2 = (android.widget.LinearLayout.LayoutParams) myCollapse2.getLayoutParams();
        lpnaviCollapse2.topMargin = sizeHeight * 10 / 1080;
//        lpnaviCollapse.bottomMargin=sizeHeight*16/1080;
        myCollapse2.setLayoutParams(lpnaviCollapse2);
        myCollapse2.measure(MeasureSpec.makeMeasureSpec(sizeWidth * 60 / 1920, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(sizeHeight * 40 / 1080, MeasureSpec.EXACTLY));

//        Navibarfloat.lp2.y = sizeHeight * Navibarfloat.lp2.y / 1080;
//        MySystemUI.avts.wm.updateViewLayout(Navibarfloat.Nbfloat2, Navibarfloat.lp2);

    }

    //onLayout 设置子视图的位置

    //onDraw 绘制view


}
