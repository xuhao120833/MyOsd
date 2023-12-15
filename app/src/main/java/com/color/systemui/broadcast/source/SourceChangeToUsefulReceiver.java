package com.color.systemui.broadcast.source;

import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.util.Log;
import com.color.osd.R;
import com.color.systemui.utils.StaticInstanceUtils;

public class SourceChangeToUsefulReceiver extends BroadcastReceiver {

    private Context mycontext;

    private String source = null;//source 0->ops 1->hdmi1 2->hdmi2 3->android

    private String plug = null;//plug 1 表示信源可用，0表示信源不可用

    public final String HDMI_IN_PLUG = "com.color.hdmi_in_camera.hdmi_in_plug";

    public boolean ops_useful = false, android_useful = true, hdmi1_useful = false, hdmi2_useful = false;

    public IntentFilter source_useful_intent = new IntentFilter();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (HDMI_IN_PLUG.equals(intent.getAction())) {
            source = intent.getStringExtra("source");
            plug = intent.getStringExtra("plug");
            setWhichSource(StaticInstanceUtils.source.select_source, source , plug);
        }

    }

    public void setContext(Context context) {
        mycontext = context;
    }


    //source 0->ops 1->hdmi1 2->hdmi2 3->android
    private void setWhichSource (String myselect_source, String mysource, String myplug) {
        if("0".equals(mysource) && "1".equals(myplug)) { //volume_image1
            ops_useful = true;
            Log.d("信源收到广播 ","source的值"+source + "plug的值" +plug);
            switch (myselect_source){
                case "OPS":
                    StaticInstanceUtils.source.OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops_select_useful));
                    break;
                default:
                    StaticInstanceUtils.source.OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops_useful));
                    break;

            }
        }
        if ("0".equals(mysource) && "0".equals(myplug)) {
            Log.d("信源收到广播 ","source的值"+source + "plug的值" +plug);
            ops_useful = false;
            switch (myselect_source){
                case "OPS":
                    StaticInstanceUtils.source.OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops_select));
                    break;
                default:
                    StaticInstanceUtils.source.OPS.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.ops));
                    break;
            }
        }

        if("1".equals(mysource) && "1".equals(myplug)) { //volume_image3
            Log.d("信源收到广播 ","source的值"+source + "plug的值" +plug);
            hdmi1_useful = true;
            switch (myselect_source){
                case "HDMI1":
                    StaticInstanceUtils.source.HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1_select_useful));
                    break;
                default:
                    StaticInstanceUtils.source.HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1_useful));
                    break;
            }
        }
        if ("1".equals(mysource) && "0".equals(myplug)) {
            Log.d("信源收到广播 ","source的值"+source + "plug的值" +plug);
            hdmi1_useful = false;
            switch (myselect_source){
                case "HDMI1":
                    StaticInstanceUtils.source.HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1_select));
                    break;
                default:
                    StaticInstanceUtils.source.HDMI1.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi1));
                    break;
            }
        }

        if("2".equals(mysource) && "1".equals(myplug)) { //volume_image4
            Log.d("信源收到广播 ","source的值"+source + "plug的值" +plug);
            hdmi2_useful = true;
            switch (myselect_source){
                case "HDMI2":
                    StaticInstanceUtils.source.HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2_select_useful));
                    break;
                default:
                    StaticInstanceUtils.source.HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2_useful));
                    break;
            }
        }
        if ("2".equals(mysource) && "0".equals(myplug)) {
            Log.d("信源收到广播 ","source的值"+source + "plug的值" +plug);
            hdmi2_useful = false;
            switch (myselect_source){
                case "HDMI2":
                    StaticInstanceUtils.source.HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2_select));
                    break;
                default:
                    StaticInstanceUtils.source.HDMI2.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.hdmi2));
                    break;
            }
        }

    }

}
