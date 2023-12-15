package com.color.systemui.utils;

import android.util.Log;

import com.color.osd.models.AddViewToScreen;
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

public class StaticInstanceUtils<T> {

    public static AddViewToScreen mavts;

    public static CalculateYposition mcalculateYposition;

    public static GetTopActivity mgetTopActivity;

    public static TimeManager mtimeManager;

    public static AnimationManager manimationManager;

    public static Hoverball hoverball;

    public static NavigationBar navigationBar;

    public static Source source;

    public static StatusBar statusBar;

    public static SettingsControlHoverballObserver settingsControlHoverballObserver;

    public static SettingsControlStatusBarObserver settingsControlStatusBarObserver;

    public static WindowManagerToOsdObserver windowManagerToOsdObserver;

    public static ResolutionChangeObserver resolutionChangeObserver;

    public static SourceChangeToUsefulReceiver sourceChangeToUsefulReceiver;

    public static ButtonBoardSourceChangeObserver buttonBoardSourceChangeObserver;


    public T getInstance(T object) {

        //return GetwhichOne(object);
        return null;
    }

    public void setInstanc(T object) {
        SetwhichOne(object);
    }

    public void SetwhichOne(Object object) {
        Log.d("SetwhichOne", " if(object instanceof AddViewToScreen )");
        if (object instanceof AddViewToScreen) {
            mavts = (AddViewToScreen) object;
            Log.d("SetwhichOne", mavts.toString() + " AddViewToScreen");
        }

        if (object instanceof Hoverball) {
            hoverball = (Hoverball) object;
            Log.d("SetwhichOne", hoverball.toString() + " Hoverball");
        }

        if (object instanceof CalculateYposition) {
            mcalculateYposition = (CalculateYposition) object;
            Log.d("SetwhichOne", mcalculateYposition.toString() + " CalculateYposition");
        }

        if (object instanceof GetTopActivity) {
            mgetTopActivity = (GetTopActivity) object;
            Log.d("SetwhichOne", mgetTopActivity.toString() + " GetTopActivity");
        }

        if (object instanceof TimeManager) {
            mtimeManager = (TimeManager) object;
            Log.d("SetwhichOne", mtimeManager.toString() + " TimeManager");
        }

        if (object instanceof AnimationManager) {
            manimationManager = (AnimationManager) object;
            Log.d("SetwhichOne", manimationManager.toString() + " AnimationManager");
        }

        if (object instanceof SettingsControlHoverballObserver) {
            settingsControlHoverballObserver = (SettingsControlHoverballObserver) object;
            Log.d("SetwhichOne", settingsControlHoverballObserver.toString() + " SettingsControlHoverballObserver");
        }

        if (object instanceof SettingsControlStatusBarObserver) {
            settingsControlStatusBarObserver = (SettingsControlStatusBarObserver) object;
            Log.d("SetwhichOne", settingsControlStatusBarObserver.toString() + " SettingsControlStatusBarObserver");
        }

        if (object instanceof WindowManagerToOsdObserver) {
            windowManagerToOsdObserver = (WindowManagerToOsdObserver) object;
            Log.d("SetwhichOne", windowManagerToOsdObserver.toString() + " WindowManagerToOsdObserver");
        }

        if (object instanceof NavigationBar) {
            navigationBar = (NavigationBar) object;
            Log.d("SetwhichOne", navigationBar.toString() + " NavigationBar");
        }

        if (object instanceof ResolutionChangeObserver) {
            resolutionChangeObserver = (ResolutionChangeObserver) object;
            Log.d("SetwhichOne", resolutionChangeObserver.toString() + " ResolutionChangeObserver");
        }

        if (object instanceof Source) {
            source = (Source) object;
            Log.d("SetwhichOne", source.toString() + " Source");
        }

        if (object instanceof SourceChangeToUsefulReceiver) {
            sourceChangeToUsefulReceiver = (SourceChangeToUsefulReceiver) object;
            Log.d("SetwhichOne", sourceChangeToUsefulReceiver.toString() + " SourceChangeToUsefulReceiver");
        }

        if (object instanceof ButtonBoardSourceChangeObserver) {
            buttonBoardSourceChangeObserver = (ButtonBoardSourceChangeObserver) object;
            Log.d("SetwhichOne", buttonBoardSourceChangeObserver.toString() + " ButtonBoardSourceChangeObserver");
        }

        if (object instanceof StatusBar) {
            statusBar = (StatusBar) object;
            Log.d("SetwhichOne", statusBar.toString() + " StatusBar");
        }

    }


}
