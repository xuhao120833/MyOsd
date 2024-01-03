package com.color.systemui.utils;

import com.color.notification.MyNotification;
import com.color.notification.models.Notification_Center_Adapter;
import com.color.notification.models.Notification_Quick_Settings_Adapter;
import com.color.notification.models.service.MyNotificationService;
import com.color.notification.utils.BrightnessChangeCompute;
import com.color.osd.models.AddViewToScreen;
import com.color.osd.models.FunctionBind;
import com.color.systemui.Contentobserver.ResolutionChangeObserver;
import com.color.systemui.Contentobserver.WindowManagerToOsdObserver;
import com.color.systemui.Contentobserver.hoverball.SettingsControlHoverballObserver;
import com.color.systemui.Contentobserver.navibar.ButtonBoardSourceChangeObserver;
import com.color.systemui.Contentobserver.statusbar.SettingsControlStatusBarObserver;
import com.color.systemui.anim.AnimationManager;
import com.color.systemui.broadcast.source.SourceChangeToUsefulReceiver;
import com.color.systemui.models.hoverball.Hoverball;
import com.color.systemui.models.navibar.NavigationBar;
import com.color.systemui.models.navibar.Source;
import com.color.systemui.models.statusbar.StatusBar;
import com.color.systemui.time.TimeManager;

public class InstanceUtils<T> {

    public AddViewToScreen mavts;

    public CalculateYposition mcalculateYposition;

    public GetTopActivity mgetTopActivity;

    public TimeManager mtimeManager;

    public AnimationManager manimationManager;

    public Hoverball hoverball;

    public NavigationBar navigationBar;

    public Source source;

    public StatusBar statusBar;

    public SettingsControlHoverballObserver settingsControlHoverballObserver;

    public SettingsControlStatusBarObserver settingsControlStatusBarObserver;

    public WindowManagerToOsdObserver windowManagerToOsdObserver;

    public ResolutionChangeObserver resolutionChangeObserver;

    public SourceChangeToUsefulReceiver sourceChangeToUsefulReceiver;

    public ButtonBoardSourceChangeObserver buttonBoardSourceChangeObserver;


    //另一个功能，通知消息中心
    public MyNotificationService myNotificationService;

    public MyNotification myNotification;

    public Notification_Center_Adapter notificationCenterAdapter;

    public Notification_Quick_Settings_Adapter notification_quick_settings_adapter;

    public BrightnessChangeCompute brightnessChangeCompute;

    public FunctionBind functionBind;


    public T getInstance(T object) {

        //return GetwhichOne(object);
        return null;
    }

    public void setInstanc(T object) {
        SetwhichOne(object);
    }

    public void SetwhichOne(Object object) {
        //Log.d("SetwhichOne", " if(object instanceof AddViewToScreen )");
        if (object instanceof AddViewToScreen) {
            mavts = (AddViewToScreen) object;
            //Log.d("SetwhichOne", mavts.toString() + " AddViewToScreen");
        }

        if (object instanceof Hoverball) {
            hoverball = (Hoverball) object;
            //Log.d("SetwhichOne", hoverball.toString() + " Hoverball");
        }

        if (object instanceof CalculateYposition) {
            mcalculateYposition = (CalculateYposition) object;
            //Log.d("SetwhichOne", mcalculateYposition.toString() + " CalculateYposition");
        }

        if (object instanceof GetTopActivity) {
            mgetTopActivity = (GetTopActivity) object;
            //Log.d("SetwhichOne", mgetTopActivity.toString() + " GetTopActivity");
        }

        if (object instanceof TimeManager) {
            mtimeManager = (TimeManager) object;
            //Log.d("SetwhichOne", mtimeManager.toString() + " TimeManager");
        }

        if (object instanceof AnimationManager) {
            manimationManager = (AnimationManager) object;
            //Log.d("SetwhichOne", manimationManager.toString() + " AnimationManager");
        }

        if (object instanceof SettingsControlHoverballObserver) {
            settingsControlHoverballObserver = (SettingsControlHoverballObserver) object;
            //Log.d("SetwhichOne", settingsControlHoverballObserver.toString() + " SettingsControlHoverballObserver");
        }

        if (object instanceof SettingsControlStatusBarObserver) {
            settingsControlStatusBarObserver = (SettingsControlStatusBarObserver) object;
            //Log.d("SetwhichOne", settingsControlStatusBarObserver.toString() + " SettingsControlStatusBarObserver");
        }

        if (object instanceof WindowManagerToOsdObserver) {
            windowManagerToOsdObserver = (WindowManagerToOsdObserver) object;
            //Log.d("SetwhichOne", windowManagerToOsdObserver.toString() + " WindowManagerToOsdObserver");
        }

        if (object instanceof NavigationBar) {
            navigationBar = (NavigationBar) object;
            //Log.d("SetwhichOne", navigationBar.toString() + " NavigationBar");
        }

        if (object instanceof ResolutionChangeObserver) {
            resolutionChangeObserver = (ResolutionChangeObserver) object;
            //Log.d("SetwhichOne", resolutionChangeObserver.toString() + " ResolutionChangeObserver");
        }

        if (object instanceof Source) {
            source = (Source) object;
            //Log.d("SetwhichOne", source.toString() + " Source");
        }

        if (object instanceof SourceChangeToUsefulReceiver) {
            sourceChangeToUsefulReceiver = (SourceChangeToUsefulReceiver) object;
            //Log.d("SetwhichOne", sourceChangeToUsefulReceiver.toString() + " SourceChangeToUsefulReceiver");
        }

        if (object instanceof ButtonBoardSourceChangeObserver) {
            buttonBoardSourceChangeObserver = (ButtonBoardSourceChangeObserver) object;
            //Log.d("SetwhichOne", buttonBoardSourceChangeObserver.toString() + " ButtonBoardSourceChangeObserver");
        }

        if (object instanceof StatusBar) {
            statusBar = (StatusBar) object;
            //Log.d("SetwhichOne", statusBar.toString() + " StatusBar");
        }


        //另一个功能，通知消息中心
        if (object instanceof MyNotificationService) {
            myNotificationService = (MyNotificationService) object;
            //Log.d("SetwhichOne", myNotificationService.toString() + " MyNotificationService");
        }

        if (object instanceof MyNotification) {
            myNotification = (MyNotification) object;
            //Log.d("SetwhichOne", myNotification.toString() + " MyNotification");
        }

        if (object instanceof Notification_Center_Adapter) {
            notificationCenterAdapter = (Notification_Center_Adapter) object;
            //Log.d("SetwhichOne", notificationCenterAdapter.toString() + " Notification_Center_Adapter");
        }

        if (object instanceof Notification_Quick_Settings_Adapter) {
            notification_quick_settings_adapter = (Notification_Quick_Settings_Adapter) object;
            //Log.d("SetwhichOne", notification_quick_settings_adapter.toString() + " Notification_Quick_Settings_Adapter");
        }

        if (object instanceof BrightnessChangeCompute) {
            brightnessChangeCompute = (BrightnessChangeCompute) object;
            //Log.d("SetwhichOne", brightnessChangeCompute.toString() + " BrightnessChangeCompute");
        }

        if (object instanceof FunctionBind) {
            functionBind = (FunctionBind) object;
            //Log.d("SetwhichOne", functionBind.toString() + " FunctionBind");
        }

    }


}
