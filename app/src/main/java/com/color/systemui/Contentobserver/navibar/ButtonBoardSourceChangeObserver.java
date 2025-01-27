package com.color.systemui.Contentobserver.navibar;

import android.content.Context;

import java.lang.String;

import com.color.osd.R;
import com.color.osd.models.Enum.MenuState;
import com.color.osd.models.service.MenuService;
import com.color.osd.ui.DialogMenu;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import android.database.ContentObserver;
import android.provider.Settings;
import android.os.Handler;
import android.view.View;

public class ButtonBoardSourceChangeObserver extends ContentObserver implements Instance {

    private Context mycontext;

    public final String CURRENT_SOURCE = "current_source";

    public int buttonboard_select_source;

    public ButtonBoardSourceChangeObserver(Context context) {

        super(new Handler());
        mycontext = context;

    }

    @Override
    public void onChange(boolean selfChange) {

        super.onChange(selfChange);
        buttonboard_select_source = Settings.Global.getInt(mycontext.getContentResolver(),
                CURRENT_SOURCE, 5);

        if (buttonboard_select_source == 3) {
            //Log.d("ButtonBoardSourceObserver", "按键小板切到Ops信源");

            //信源界面打开则关闭信源界面
            if(STATIC_INSTANCE_UTILS.source.Source.getVisibility() == View.VISIBLE) {
                STATIC_INSTANCE_UTILS.source.Source.setVisibility(View.GONE);
            }
            if(MenuService.menuState == MenuState.MENU_SOURCE) {
                MenuService.menuState = null;
            }

            //切到非Android信源，如果Osd菜单打开则需要关闭
            if(MenuService.menuOn == true) {
                DialogMenu.mydialog.dismiss();
                MenuService.menuOn = false;
            }

            //状态栏
            STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.GONE);

            changeImage(3);


        }

        if (buttonboard_select_source == 1) {
            //Log.d("ButtonBoardSourceObserver", "按键小板切到Hdmi1信源");

            //信源打开则关闭信源界面
            if(STATIC_INSTANCE_UTILS.source.Source.getVisibility() == View.VISIBLE) {
                STATIC_INSTANCE_UTILS.source.Source.setVisibility(View.GONE);
            }
            if(MenuService.menuState == MenuState.MENU_SOURCE) {
                MenuService.menuState = null;
            }

            //切到非Android信源，如果Osd菜单打开则需要关闭
            if(MenuService.menuOn == true) {
                DialogMenu.mydialog.dismiss();
                MenuService.menuOn = false;
            }

            STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.GONE);

            changeImage(1);


        }

        if (buttonboard_select_source == 2) {
            //Log.d("ButtonBoardSourceObserver", "按键小板切到Hdmi2信源");

            //信源打开则关闭信源界面
            if(STATIC_INSTANCE_UTILS.source.Source.getVisibility() == View.VISIBLE) {
                STATIC_INSTANCE_UTILS.source.Source.setVisibility(View.GONE);
            }
            if(MenuService.menuState == MenuState.MENU_SOURCE) {
                MenuService.menuState = null;
            }

            //切到非Android信源，如果Osd菜单打开则需要关闭
            if(MenuService.menuOn == true) {
                DialogMenu.mydialog.dismiss();
                MenuService.menuOn = false;
            }

            STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.GONE);

            changeImage(2);


        }

        if (buttonboard_select_source == 0) {


            STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.GONE);

            if(StaticVariableUtils.SettingsControlStatusBarVisible == true) {
                STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.VISIBLE);
            }

            changeImage(0);


        }

    }

    public void changeImage(int i) {
        switch (i) {
            case 3:
                if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.ops_useful) {
                    STATIC_INSTANCE_UTILS.source.OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops_select_useful));
                } else {
                    STATIC_INSTANCE_UTILS.source.OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops_select));
                }
                STATIC_INSTANCE_UTILS.source.Android.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.android_useful));
                if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.hdmi1_useful) {
                    STATIC_INSTANCE_UTILS.source.HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1_useful));
                } else {
                    STATIC_INSTANCE_UTILS.source.HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1));
                }
                if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.hdmi2_useful) {
                    STATIC_INSTANCE_UTILS.source.HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2_useful));
                } else {
                    STATIC_INSTANCE_UTILS.source.HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2));
                }
                break;

            case 1:
                if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.ops_useful) {
                    STATIC_INSTANCE_UTILS.source.OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops_useful));
                } else {
                    STATIC_INSTANCE_UTILS.source.OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops));
                }
                STATIC_INSTANCE_UTILS.source.Android.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.android_useful));
                if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.hdmi1_useful) {
                    STATIC_INSTANCE_UTILS.source.HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1_select_useful));
                } else {
                    STATIC_INSTANCE_UTILS.source.HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1_select));
                }
                if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.hdmi2_useful) {
                    STATIC_INSTANCE_UTILS.source.HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2_useful));
                } else {
                    STATIC_INSTANCE_UTILS.source.HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2));
                }
                break;
            case 2:
                if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.ops_useful) {
                    STATIC_INSTANCE_UTILS.source.OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops_useful));
                } else {
                    STATIC_INSTANCE_UTILS.source.OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops));
                }
                STATIC_INSTANCE_UTILS.source.Android.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.android_useful));
                if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.hdmi1_useful) {
                    STATIC_INSTANCE_UTILS.source.HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1_useful));
                } else {
                    STATIC_INSTANCE_UTILS.source.HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1));
                }
                if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.hdmi2_useful) {
                    STATIC_INSTANCE_UTILS.source.HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2_select_useful));
                } else {
                    STATIC_INSTANCE_UTILS.source.HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2_select));
                }
                break;

            case 0:
                if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.ops_useful) {
                    STATIC_INSTANCE_UTILS.source.OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops_useful));
                } else {
                    STATIC_INSTANCE_UTILS.source.OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops));
                }
                STATIC_INSTANCE_UTILS.source.Android.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.android_select_useful));
                if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.hdmi1_useful) {
                    STATIC_INSTANCE_UTILS.source.HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1_useful));
                } else {
                    STATIC_INSTANCE_UTILS.source.HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1));
                }
                if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.hdmi2_useful) {
                    STATIC_INSTANCE_UTILS.source.HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2_useful));
                } else {
                    STATIC_INSTANCE_UTILS.source.HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2));
                }
                break;

        }

    }


}
