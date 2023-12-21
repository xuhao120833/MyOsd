package com.color.systemui;

import android.content.Context;
import android.provider.Settings;

import com.color.osd.models.AddViewToScreen;
import com.color.systemui.Contentobserver.ResolutionChangeObserver;
import com.color.systemui.Contentobserver.WindowManagerToOsdObserver;
import com.color.systemui.Contentobserver.hoverball.SettingsControlHoverballObserver;
import com.color.systemui.Contentobserver.navibar.ButtonBoardSourceChangeObserver;
import com.color.systemui.Contentobserver.statusbar.SettingsControlStatusBarObserver;
import com.color.systemui.anim.AnimationManager;
import com.color.systemui.broadcast.sideslip.SideSlipReceiver;
import com.color.systemui.broadcast.source.SourceChangeToUsefulReceiver;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.models.hoverball.Hoverball;
import com.color.systemui.models.navibar.NavigationBar;
import com.color.systemui.models.navibar.Source;
import com.color.systemui.models.statusbar.StatusBar;
import com.color.systemui.time.TimeManager;
import com.color.systemui.utils.CalculateYposition;
import com.color.systemui.utils.GetTopActivity;
import com.color.systemui.utils.InstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

public class MySystemUI implements Instance {

    private Context mycontext;

    AddViewToScreen mavts = new AddViewToScreen();

    CalculateYposition mcalculateYposition;

    GetTopActivity getTopActivity = new GetTopActivity();

    TimeManager timeManager = new TimeManager();

    AnimationManager animationManager = new AnimationManager();

    Hoverball hoverball;

    NavigationBar navigationBar;

    Source source;

    StatusBar statusBar;

    SettingsControlHoverballObserver settingsControlHoverballObserver = new SettingsControlHoverballObserver();

    SettingsControlStatusBarObserver settingsControlStatusBarObserver = new SettingsControlStatusBarObserver();

    WindowManagerToOsdObserver windowManagerToOsdObserver = new WindowManagerToOsdObserver();

    ResolutionChangeObserver resolutionChangeObserver;

    ButtonBoardSourceChangeObserver buttonBoardSourceChangeObserver;

    SourceChangeToUsefulReceiver sourceChangeToUsefulReceiver = new SourceChangeToUsefulReceiver();

    SideSlipReceiver sideSlipReceiver = new SideSlipReceiver();


    MySystemUI() {}

    public MySystemUI(Context context) {
        mycontext = context;
    }

    public void start() {

        //0、初始化工具类：
        //view添加、更新、移除
        mavts.setContext(mycontext);
        setInstance(mavts);
        //计算view初始Y坐标
        mcalculateYposition = new CalculateYposition(mycontext);
        setInstance(mcalculateYposition);
        //获取当前Top窗口
        getTopActivity.setContext(mycontext);
        setInstance(getTopActivity);
        //定时器
        timeManager.setContext(mycontext);
        setInstance(timeManager);
        //动画管理类
        animationManager.setContext(mycontext);
        setInstance(animationManager);


        //1、悬浮球初始化
        initializehoverball();

        //2、导航栏初始化
        initializenavibar();

        //3、信源界面
        initializesource();

        //4、状态栏初始化
        initializestatusbar();

        //5、广播注册:
        //注册监听信源是否可用的广播
        sourceChangeToUsefulReceiver.setContext(mycontext);
        setInstance(sourceChangeToUsefulReceiver);
        sourceChangeToUsefulReceiver.source_useful_intent.addAction(sourceChangeToUsefulReceiver.HDMI_IN_PLUG);
        mycontext.registerReceiver(sourceChangeToUsefulReceiver,sourceChangeToUsefulReceiver.source_useful_intent);

        //注册侧滑广播
        sideSlipReceiver.setContext(mycontext);
        setInstance(sideSlipReceiver);
        sideSlipReceiver.sideslip_filter.addAction(StaticVariableUtils.onSwipeFromLeft_Action);
        sideSlipReceiver.sideslip_filter.addAction(StaticVariableUtils.onSwipeFromRight_Action);
        mycontext.registerReceiver(sideSlipReceiver,sideSlipReceiver.sideslip_filter);

        //6、各种ContentObserver监听:
        //设置控制悬浮球显示与否，默认显示
        settingsControlHoverballObserver.setContext(mycontext);
        setInstance(settingsControlHoverballObserver);
        mycontext.getContentResolver().registerContentObserver(Settings.System.getUriFor(STATIC_INSTANCE_UTILS.settingsControlHoverballObserver.OPEN_FAST_TOOLBAR), true, settingsControlHoverballObserver);

        //设置控制导航栏显示与否，默认不显示
        settingsControlStatusBarObserver.setContext(mycontext);
        setInstance(settingsControlStatusBarObserver);
        mycontext.getContentResolver().registerContentObserver(Settings.System.getUriFor(STATIC_INSTANCE_UTILS.settingsControlStatusBarObserver.OPEN_STATUS_BAR),true,settingsControlStatusBarObserver);

        //全局空白无功能点击事件监听，处理悬浮球、导航栏定时逻辑
        windowManagerToOsdObserver.setContext(mycontext);
        setInstance(windowManagerToOsdObserver);
        mycontext.getContentResolver().registerContentObserver(Settings.System.getUriFor(StaticVariableUtils.WINDOWMANAGER_TO_OSD),true,windowManagerToOsdObserver);

        //监听分辨率变化，重置悬浮球、悬浮球导航栏的Y坐标
        resolutionChangeObserver = new ResolutionChangeObserver(mycontext);
        setInstance(resolutionChangeObserver);
        mycontext.getContentResolver().registerContentObserver(Settings.System.getUriFor(STATIC_INSTANCE_UTILS.resolutionChangeObserver.SYSTEM_RESOLUTION_CHANGE),true,resolutionChangeObserver);

        //按键小板切源监听,同步变换信源图片，隐藏Osd菜单、状态栏
        buttonBoardSourceChangeObserver = new ButtonBoardSourceChangeObserver(mycontext);
        setInstance(buttonBoardSourceChangeObserver);
        mycontext.getContentResolver().registerContentObserver(Settings.Global.getUriFor(STATIC_INSTANCE_UTILS.buttonBoardSourceChangeObserver.CURRENT_SOURCE),true,buttonBoardSourceChangeObserver);

//        //7、将各个View添加到屏幕上显示
        startAddView();

    }

    private void initializehoverball(){

        hoverball = new Hoverball();
        hoverball.setContext(mycontext);
        //Log.d("starthoverball","setInstance(hoverball.getClass())");
        setInstance(hoverball);


    }

    private void initializenavibar() {

        navigationBar = new NavigationBar();
        navigationBar.setContext(mycontext);
        setInstance(navigationBar);

    }

    private void initializesource() {

        source = new Source();
        source.setContext(mycontext);
        setInstance(source);

    }

    private void initializestatusbar() {

        statusBar = new StatusBar();
        statusBar.setContext(mycontext);
        setInstance(statusBar);

    }

    private void startAddView() {

        //1、悬浮球
        hoverball.start();

        //2、导航栏
        navigationBar.start();

        //3、信源切换界面
        source.start();

        //4、状态栏
        statusBar.start();
    }


}
