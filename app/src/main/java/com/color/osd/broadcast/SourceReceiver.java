package com.color.osd.broadcast;

import android.widget.ImageView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import java.lang.*;

import android.view.View;
import android.util.Log;

import com.color.osd.R;
import com.color.osd.ui.Source_View;

public class SourceReceiver extends BroadcastReceiver {
    static ImageView fudisk;
    static Context mcontext;
    ImageView i1 , i3 , i4;

    private String source = null ;
    private String plug = null;//source 0->image1 1->image3 2->image4 3->image2 ; plug 1 表示信源可用，0表示信源不可用

    public static boolean volume_image1 = false , volume_image3 = false , volume_image4 = false;

    private static final String HDMI_IN_PLUG = "com.color.hdmi_in_camera.hdmi_in_plug";

    public void setImage(ImageView Image1 , ImageView Image3 , ImageView Image4 ,Context context ) {
        i1 = Image1;
        i3 = Image3;
        i4 = Image4;
        mcontext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (HDMI_IN_PLUG.equals(intent.getAction())) {
            source = intent.getStringExtra("source");
            plug = intent.getStringExtra("plug");
            setWhichImage(Source_View.select , source , plug);
        }
    }

    private void setWhichImage(int select , String mysource , String mplug) {
        if("0".equals(mysource) && "1".equals(mplug)) { //volume_image1
            volume_image1 = true;
            Log.d("信源收到广播 ","source的值"+source + "plug的值" +plug);
            switch (select){
                case 1:
                    i1.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal1_slect_useful));
                    break;
                default:
                    i1.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal1_useful));
                    break;

            }
        }
        if ("0".equals(mysource) && "0".equals(mplug)) {
            Log.d("信源收到广播 ","source的值"+source + "plug的值" +plug);
            volume_image1 = false;
            switch (select){
                case 1:
                    i1.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal1_slect));
                    break;
                default:
                    i1.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal1));
                    break;
            }
        }

        if("1".equals(mysource) && "1".equals(mplug)) { //volume_image3
            Log.d("信源收到广播 ","source的值"+source + "plug的值" +plug);
            volume_image3 = true;
            switch (select){
                case 3:
                    i3.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal3_slect_useful));
                    break;
                default:
                    i3.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal3_useful));
                    break;
            }
        }
        if ("1".equals(mysource) && "0".equals(mplug)) {
            Log.d("信源收到广播 ","source的值"+source + "plug的值" +plug);
            volume_image3 = false;
            switch (select){
                case 3:
                    i3.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal3_slect));
                    break;
                default:
                    i3.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal3));
                    break;
            }
        }

        if("2".equals(mysource) && "1".equals(mplug)) { //volume_image4
            Log.d("信源收到广播 ","source的值"+source + "plug的值" +plug);
            volume_image4 = true;
            switch (select){
                case 4:
                    i4.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal4_slect_useful));
                    break;
                default:
                    i4.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal4_useful));
                    break;
            }
        }
        if ("2".equals(mysource) && "0".equals(mplug)) {
            Log.d("信源收到广播 ","source的值"+source + "plug的值" +plug);
            volume_image4 = false;
            switch (select){
                case 4:
                    i4.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal4_slect));
                    break;
                default:
                    i4.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal4));
                    break;
            }
        }

    }

}