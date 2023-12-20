package com.color.systemui.broadcast.statusbar.icons;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

public class EthernetIconChangeReceiver extends BroadcastReceiver implements Instance {

    private Context mycontext;

    private NetworkInfo networkInfo;

    private ConnectivityManager connectivityManager;

    public EthernetIconChangeReceiver() {

    }

    public void setContext(Context context) {
        mycontext = context;
        connectivityManager = (ConnectivityManager) mycontext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) { //以太网插拔
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                //Log.d("MyEthernet 网络类型" , String.valueOf(networkInfo.getType()));
                if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET && networkInfo.isConnected()) {
                    if("com.android.launcher3".equals(STATIC_INSTANCE_UTILS.mgetTopActivity.getPackage()) ||
                            "com.color.settings".equals(STATIC_INSTANCE_UTILS.mgetTopActivity.getPackage())) {
                        STATIC_INSTANCE_UTILS.statusBar.ethernet.setVisibility(View.VISIBLE);
                    }
                    //fethernet.setVisibility(View.VISIBLE);
                    StaticVariableUtils.EthernetConnected = true;
                }
                if(networkInfo.getType() != ConnectivityManager.TYPE_ETHERNET) {
                    STATIC_INSTANCE_UTILS.statusBar.ethernet.setVisibility(View.GONE);
                    StaticVariableUtils.EthernetConnected = false;
                }
            }
            if (networkInfo == null) {
                STATIC_INSTANCE_UTILS.statusBar.ethernet.setVisibility(View.GONE);
                StaticVariableUtils.EthernetConnected = false;
            }
        }
    }

}
