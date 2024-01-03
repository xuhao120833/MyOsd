package com.color.systemui.models.navibar;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.LayoutInflater;
import com.color.osd.R;
import com.color.osd.models.Enum.MenuState;
import com.color.osd.models.service.MenuService;
import com.color.osd.ui.DialogMenu;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import android.graphics.PixelFormat;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.util.Log;

public class Source implements Instance {

    private Context mycontext;

    private LayoutInflater inflater;

    private Intent broadcast_to_webserver = new Intent();//发广播告诉webserver用户通过信源界面切源。

    public View Source;

    public WindowManager.LayoutParams lp;

    public  ImageView OPS, Android, HDMI1, HDMI2, X;

    public  TextView text;

    public String select_source = "Android";//信源选择标志位，开机默认选择Android

    public Source() {

    }

    public void setContext(Context context) {
        mycontext = context;

        //1、初始化LayoutParams描述工具
        initLayoutParams();

        //2、初始化View对象
        initView();

        //3、给View添加Click、Touch事件监听
        setClick();

    }

    private void initLayoutParams() {

        lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        lp.flags = WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        lp.format = PixelFormat.RGBA_8888;

    }

    private void initView() {

        inflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Source = inflater.inflate(R.layout.source, null);
        text = (TextView) Source.findViewById(R.id.text);
        X = (ImageView) Source.findViewById(R.id.X);
        OPS = (ImageView) Source.findViewById(R.id.OPS);
//        OPS.requestFocus();//默认OPS获取到焦点
        Android = (ImageView) Source.findViewById(R.id.Android);
        HDMI1 = (ImageView) Source.findViewById(R.id.HDMI1);
        HDMI2 = (ImageView) Source.findViewById(R.id.HDMI2);

    }

    private void setClick() {

        X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Source.setVisibility(View.GONE);

                if(MenuService.menuState == MenuState.MENU_SOURCE) {//如果是通过Osd唤起的信源界面，按X重新唤出Osd菜单
                    MenuService.menuState = MenuState.NULL;//二级菜单标志清空
                    DialogMenu.mydialog.show();
                    MenuService.menuOn = true;
                }
            }
        });

        OPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!select_source.equals("OPS")) {//从其它信源选择OPS
                    if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.ops_useful) {
                        OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops_select_useful));
                    } else {
                        OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops_select));
                    }
                    setDrawable(select_source);//设置上一个被选中的信源的图片切换
                    broadcast_to_webserver.setAction("com.color.systemui");
                    broadcast_to_webserver.putExtra("data", "3");
                    broadcast_to_webserver.setPackage("com.color.webserver");
                    mycontext.sendBroadcast(broadcast_to_webserver);
                    Log.d("Source 切换OPS","广播成功发出");
                    select_source = "OPS";
                    Source.setVisibility(View.GONE);
                    if(MenuService.menuState == MenuState.MENU_SOURCE) {
                        MenuService.menuState = MenuState.NULL;//二级菜单标志清空
                    }
                    if (MenuService.menuOn = true) {
                        DialogMenu.mydialog.dismiss();//收起Osd 菜单
                        MenuService.menuOn = false;
                    }
                    //状态栏
                    STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.GONE);

                } else {
                    if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.ops_useful) {
                        OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops_useful));
                    } else {
                        OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops));
                    }
                    select_source = "Android";
                    Log.d("select的值 ", select_source);
                    broadcast_to_webserver.setAction("com.color.systemui");
                    broadcast_to_webserver.putExtra("data", "0");
                    broadcast_to_webserver.setPackage("com.color.webserver");
                    mycontext.sendBroadcast(broadcast_to_webserver);
                    Android.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.android_select_useful));
                    Source.setVisibility(View.GONE);
                    if(MenuService.menuState == MenuState.MENU_SOURCE) {
                        MenuService.menuState = MenuState.NULL;//二级菜单标志清空
                    }
                    if(StaticVariableUtils.SettingsControlStatusBarVisible == true) {
                        STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        Android.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!select_source.equals("Android")) {
                    Android.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.android_select_useful));
                    setDrawable(select_source);
                    broadcast_to_webserver.setAction("com.color.systemui");
                    broadcast_to_webserver.putExtra("data", "0");
                    broadcast_to_webserver.setPackage("com.color.webserver");
                    mycontext.sendBroadcast(broadcast_to_webserver);
                    select_source = "Android";
                    Source.setVisibility(View.GONE);
                    if(MenuService.menuState == MenuState.MENU_SOURCE) {
                        MenuService.menuState = MenuState.NULL;//二级菜单标志清空
                    }
                    if(StaticVariableUtils.SettingsControlStatusBarVisible == true) {
                        STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.VISIBLE);
                    }


                } else {
                    select_source = "Android";
                    Source.setVisibility(View.GONE);
                    if(MenuService.menuState == MenuState.MENU_SOURCE) {
                        MenuService.menuState = MenuState.NULL;//二级菜单标志清空
                    }
                }
            }
        });

        HDMI1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!select_source.equals("HDMI1")) {
                    if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.hdmi1_useful) {
                        HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1_select_useful));
                    } else {
                        HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1_select));
                    }
                    setDrawable(select_source);
                    broadcast_to_webserver.setAction("com.color.systemui");
                    broadcast_to_webserver.putExtra("data", "1");
                    broadcast_to_webserver.setPackage("com.color.webserver");
                    mycontext.sendBroadcast(broadcast_to_webserver);
                    select_source = "HDMI1";
                    Source.setVisibility(View.GONE);
                    if(MenuService.menuState == MenuState.MENU_SOURCE) {
                        MenuService.menuState = MenuState.NULL;//二级菜单标志清空
                    }
                    if (MenuService.menuOn = true) {
                        DialogMenu.mydialog.dismiss();//收起Osd 菜单
                        MenuService.menuOn = false;
                    }
                    STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.GONE);
                } else {
                    if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.hdmi1_useful) {
                        HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1_useful));
                    } else {
                        HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1));
                    }
                    select_source = "Android";
                    broadcast_to_webserver.setAction("com.color.systemui");
                    broadcast_to_webserver.putExtra("data", "0");
                    broadcast_to_webserver.setPackage("com.color.webserver");
                    mycontext.sendBroadcast(broadcast_to_webserver);
                    Android.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.android_select_useful));
                    Source.setVisibility(View.GONE);
                    if(MenuService.menuState == MenuState.MENU_SOURCE) {
                        MenuService.menuState = MenuState.NULL;//二级菜单标志清空
                    }
                    if(StaticVariableUtils.SettingsControlStatusBarVisible == true) {
                        STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        HDMI2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!select_source.equals("HDMI2")) {
                    if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.hdmi2_useful) {
                        HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2_select_useful));
                    } else {
                        HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2_select));
                    }
                    setDrawable(select_source);
                    broadcast_to_webserver.setAction("com.color.systemui");
                    broadcast_to_webserver.putExtra("data", "2");
                    broadcast_to_webserver.setPackage("com.color.webserver");
                    mycontext.sendBroadcast(broadcast_to_webserver);
                    select_source = "HDMI2";
                    Source.setVisibility(View.GONE);
                    if(MenuService.menuState == MenuState.MENU_SOURCE) {
                        MenuService.menuState = MenuState.NULL;//二级菜单标志清空
                    }
                    if (MenuService.menuOn = true) {
                        DialogMenu.mydialog.dismiss();//收起Osd 菜单
                        MenuService.menuOn = false;
                    }
                    STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.GONE);

                } else {
                    if (STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.hdmi2_useful) {
                        HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2_useful));
                    } else {
                        HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2));
                    }
                    select_source = "Android";
                    broadcast_to_webserver.setAction("com.color.systemui");
                    broadcast_to_webserver.putExtra("data", "0");
                    broadcast_to_webserver.setPackage("com.color.webserver");
                    mycontext.sendBroadcast(broadcast_to_webserver);
                    Android.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.android_select_useful));
                    Source.setVisibility(View.GONE);
                    if(MenuService.menuState == MenuState.MENU_SOURCE) {
                        MenuService.menuState = MenuState.NULL;//二级菜单标志清空
                    }
                    if(StaticVariableUtils.SettingsControlStatusBarVisible == true) {
                        STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    private void setDrawable(String select) {
        switch (select) {
            case "OPS":
                if(STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.ops_useful) {
                    OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops_useful));
                } else {
                    OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops));
                }
                break;
            case "Android":
                if(STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.android_useful) {
                    Android.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.android_useful));
                } else {
                    Android.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.android));
                }
                break;
            case "HDMI1":
                if(STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.hdmi1_useful) {
                    HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1_useful));
                } else {
                    HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1));
                }
                break;
            case "HDMI2":
                if(STATIC_INSTANCE_UTILS.sourceChangeToUsefulReceiver.hdmi2_useful) {
                    HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2_useful));
                } else {
                    HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2));
                }
                break;
        }
    }

    public void start() {

        Source.setVisibility(View.GONE);
        STATIC_INSTANCE_UTILS.mavts.addView(Source,lp);
        OPS.clearFocus();
        OPS.requestFocus();//默认OPS获取到焦点

    }

}
