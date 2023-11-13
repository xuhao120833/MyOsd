package com.color.osd.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.color.osd.R;
import com.color.osd.broadcast.SourceReceiver;
import com.color.osd.models.FunctionBind;
import com.color.osd.models.Menu_source;
import com.color.osd.models.service.MenuService;

public class Source_View {

    public static View source;
    public static WindowManager.LayoutParams lp;
    boolean sourceTag = false;
    private static Context mycontext;
    static LayoutInflater inflater;

    public static ImageView i1, i2, i3, i4;

    static FrameLayout r1, r2, r3, r4;

    public static int select = 2;

    static Intent intent = new Intent();

    static IntentFilter intent_volume = new IntentFilter();

    static SourceReceiver sourcereceiver = new SourceReceiver();

    private static final String HDMI_IN_PLUG = "com.color.hdmi_in_camera.hdmi_in_plug";

    private static final String OSD_OPEN_OTHER_SOURCE = "osd_open_other_source";

    public Source_View(Context context) {
        mycontext =context;

        inflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Settings.System.putInt(mycontext.getContentResolver(),OSD_OPEN_OTHER_SOURCE,5);

        initView();

        initLp();

    }

    private void initView() {
        source = inflater.inflate(R.layout.menu_souce, null);

        i1 = (ImageView) source.findViewById(R.id.i1);
        i2 = (ImageView) source.findViewById(R.id.i2);
        i3 = (ImageView) source.findViewById(R.id.i3);
        i4 = (ImageView) source.findViewById(R.id.i4);

        r1 = source.findViewById(R.id.r1);
        r2 = source.findViewById(R.id.r2);
        r3 = source.findViewById(R.id.r3);
        r4 = source.findViewById(R.id.r4);

        intent_volume.addAction(HDMI_IN_PLUG);
        sourcereceiver.setImage(i1 , i3 , i4 ,mycontext);
        mycontext.registerReceiver(sourcereceiver,intent_volume);

        setOnClick(i1, i2, i3, i4);
        FrameLayout_setOnClick(r1, r2, r3, r4);
    }

    private void FrameLayout_setOnClick(FrameLayout r1, FrameLayout r2, FrameLayout r3, FrameLayout r4) {
        r1.setOnClickListener(v -> {
            i1.performClick();
        });

        r2.setOnClickListener(v -> {
            i2.performClick();
        });

        r3.setOnClickListener(v -> {
            i3.performClick();
        });

        r4.setOnClickListener(v -> {
            i4.performClick();
        });
    }

    private void initLp() {

        lp = new WindowManager.LayoutParams();
        lp.width = 600;
        lp.height = 500;
        lp.gravity = Gravity.CENTER;
        lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                   WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        lp.format = PixelFormat.RGBA_8888;

    }

    public void setOnClick(ImageView i1, ImageView i2, ImageView i3, ImageView i4 ) {
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select != 1) {
                    if(SourceReceiver.volume_image1){
                        i1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal1_slect_useful));
                    }else {
                        i1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal1_slect));
                    }
                    msetDrawable(select);
                    select = 1;
                    FunctionBind.mavts.clearView(source);
                    Menu_source.sourceon = true;

                    intent.setAction("com.color.systemui");
                    intent.putExtra("data", "0");
                    intent.setPackage("com.color.webserver");
                    mycontext.sendBroadcast(intent);

                    DialogMenu.mydialog.dismiss();//切换其它信源，收起菜单
                    MenuService.menuOn = false;

                    Settings.System.putInt(mycontext.getContentResolver(),OSD_OPEN_OTHER_SOURCE,1);//通知SystemUI已切换非Android信源。1 3 4代表三个图标。
                    //StatusBarfloat.myframe.setVisibility(View.GONE);
                } else {
                    if(SourceReceiver.volume_image1){
                        i1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal1_useful));
                    }else {
                        i1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal1));
                    }
                    select = 2;

                    intent.setAction("com.color.systemui");
                    intent.putExtra("data", "3");
                    intent.setPackage("com.color.webserver");
                    mycontext.sendBroadcast(intent);

                    i2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal2_slect_useful));
                    FunctionBind.mavts.clearView(source);
                    Menu_source.sourceon = true;

                    Settings.System.putInt(mycontext.getContentResolver(),OSD_OPEN_OTHER_SOURCE,2);//通知SystemUI恢复状态栏
                    //StatusBarfloat.myframe.setVisibility(View.VISIBLE);
                }
            }
        });
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select != 2) {
                    i2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal2_slect_useful));
                    msetDrawable(select);
                    select = 2;

                    intent.setAction("com.color.systemui");
                    intent.putExtra("data", "3");
                    intent.setPackage("com.color.webserver");
                    mycontext.sendBroadcast(intent);

                    FunctionBind.mavts.clearView(source);
                    Menu_source.sourceon = true;

                    Settings.System.putInt(mycontext.getContentResolver(),OSD_OPEN_OTHER_SOURCE,2);//通知SystemUI恢复状态栏
                    //StatusBarfloat.myframe.setVisibility(View.VISIBLE);
                } else {
                    //i2.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal2_useful));
                    select = 2;
                    FunctionBind.mavts.clearView(source);
                    Menu_source.sourceon = true;

                    Settings.System.putInt(mycontext.getContentResolver(),OSD_OPEN_OTHER_SOURCE,2);//通知SystemUI恢复状态栏
                }
            }
        });
        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select != 3) {
                    if(SourceReceiver.volume_image3){
                        i3.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal3_slect_useful));
                    }else {
                        i3.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal3_slect));
                    }
                    msetDrawable(select);

                    intent.setAction("com.color.systemui");
                    intent.putExtra("data", "1");
                    intent.setPackage("com.color.webserver");
                    mycontext.sendBroadcast(intent);

                    select = 3;
                    FunctionBind.mavts.clearView(source);
                    Menu_source.sourceon = true;

                    DialogMenu.mydialog.dismiss();//切换其它信源，收起菜单
                    MenuService.menuOn = false;

                    Settings.System.putInt(mycontext.getContentResolver(),OSD_OPEN_OTHER_SOURCE,3);//通知SystemUI已切换非Android信源。1 3 4代表三个图标。
                    //StatusBarfloat.myframe.setVisibility(View.GONE);
                } else {
                    if(SourceReceiver.volume_image3){
                        i3.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal3_useful));
                    }else {
                        i3.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal3));
                    }
                    select = 2;

                    intent.setAction("com.color.systemui");
                    intent.putExtra("data", "3");
                    intent.setPackage("com.color.webserver");
                    mycontext.sendBroadcast(intent);

                    i2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal2_slect_useful));
                    FunctionBind.mavts.clearView(source);
                    Menu_source.sourceon = true;

                    Settings.System.putInt(mycontext.getContentResolver(),OSD_OPEN_OTHER_SOURCE,2);//通知SystemUI恢复状态栏
                    //StatusBarfloat.myframe.setVisibility(View.VISIBLE);
                }
            }
        });
        i4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select != 4) {
                    if(SourceReceiver.volume_image4){
                        i4.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal4_slect_useful));
                    }else {
                        i4.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal4_slect));
                    }
                    msetDrawable(select);
                    select = 4;
                    intent.setAction("com.color.systemui");
                    intent.putExtra("data", "2");
                    intent.setPackage("com.color.webserver");
                    mycontext.sendBroadcast(intent);
                    FunctionBind.mavts.clearView(source);
                    Menu_source.sourceon = true;

                    DialogMenu.mydialog.dismiss();//切换其它信源，收起菜单
                    MenuService.menuOn = false;

                    Settings.System.putInt(mycontext.getContentResolver(),OSD_OPEN_OTHER_SOURCE,4);//通知SystemUI已切换非Android信源。1 3 4代表三个图标。
                    //StatusBarfloat.myframe.setVisibility(View.GONE);
                } else {
                    if(SourceReceiver.volume_image4){
                        i4.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal4_useful));
                    }else {
                        i4.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal4));
                    }
                    select = 2;

                    intent.setAction("com.color.systemui");
                    intent.putExtra("data", "3");
                    intent.setPackage("com.color.webserver");
                    mycontext.sendBroadcast(intent);

                    i2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal2_slect_useful));
                    FunctionBind.mavts.clearView(source);
                    Menu_source.sourceon = true;

                    Settings.System.putInt(mycontext.getContentResolver(),OSD_OPEN_OTHER_SOURCE,2);//通知SystemUI恢复状态栏
                    //StatusBarfloat.myframe.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public static void msetDrawable(int i) {
        switch (i) {
            case 1:
                if(SourceReceiver.volume_image1){
                    i1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal1_useful));
                }else {
                    i1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal1));
                }
                break;
            case 2:
                i2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal2_useful));
                break;
            case 3:
                if(SourceReceiver.volume_image3){
                    i3.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal3_useful));
                }else {
                    i3.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal3));
                }
                break;
            case 4:
                if(SourceReceiver.volume_image4){
                    i4.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal4_useful));
                }else {
                    i4.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.signal4));
                }
                break;
            default:
                break;
        }

    }

}
