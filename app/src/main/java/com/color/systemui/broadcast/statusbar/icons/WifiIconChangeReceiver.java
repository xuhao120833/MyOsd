package com.color.systemui.broadcast.statusbar.icons;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.widget.ImageView;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.color.osd.R;
import com.color.systemui.utils.StaticInstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import android.os.Parcelable;
import java.lang.*;
import android.net.wifi.WifiManager;
import android.view.View;
import android.util.Log;
import android.net.NetworkCapabilities;

public class WifiIconChangeReceiver extends BroadcastReceiver {

    private Context mycontext;

    private ConnectivityManager connectivityManager;

    private NetworkInfo networkInfo;

    private int wifiState;

    private Parcelable parcelable;

    private NetworkInfo.State state;

    private boolean wifiConnected;//wifi是否连接上，注意：连上了不代表可以用，区分概念

    private int rssi;//wifi信号强度

    private NetworkCapabilities networkCapabilities;

    private boolean wifiCanUse = false;



    public WifiIconChangeReceiver() {

    }

    public void setContext(Context context) {
        mycontext = context;

        connectivityManager = (ConnectivityManager) mycontext.getSystemService(Context.CONNECTIVITY_SERVICE);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) { //wifi开关
            //Log.d("MyWifi 收到wifi开关广播","xuhao");
            wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    //Log.d("MyWifi 收到wifi关闭广播","wifi图标消失");
                    StaticInstanceUtils.statusBar.wifi.setVisibility(View.GONE);
                    StaticVariableUtils.WifiOpen = false;
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    //Log.d("MyWifi 收到wifi打开广播","WIFI_STATE_ENABLED");
                    StaticInstanceUtils.statusBar.wifi.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.statusbar_wifiserach));
                    if("com.android.launcher3".equals(StaticInstanceUtils.mgetTopActivity.getPackage()) ||
                            "com.color.settings".equals(StaticInstanceUtils.mgetTopActivity.getPackage()) ||
                            "com.android.tv.settings".equals(StaticInstanceUtils.mgetTopActivity.getPackage()) ){
                        //Log.d("MyWifi 收到wifi打开广播","wifi图标出现");
                        StaticInstanceUtils.statusBar.wifi.setVisibility(View.VISIBLE);
                    }
                    StaticVariableUtils.WifiOpen = true;
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    break;
            }
        }

        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) { //wifi 是否连接上
            parcelable = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelable) {
                networkInfo = (NetworkInfo) parcelable;
                state = networkInfo.getState();
                wifiConnected = state == NetworkInfo.State.CONNECTED;
            }
            if (state == NetworkInfo.State.DISCONNECTED) {
                StaticInstanceUtils.statusBar.wifi.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.statusbar_wifiserach));
            }

        }

//        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) { //wifi网络是否可用\
//            info = intent
//                    .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
//
//        }


        if (WifiManager.RSSI_CHANGED_ACTION.equals(intent.getAction())) { //wifi强度

            //这函数可以计算出信号的等级
            rssi = WifiManager.calculateSignalLevel(intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, -1), 5);
            //Log.d("MyWifi 连接上","wifi强度发生变化");

            switch (rssi) {
                case 0:
                    if (wifiConnected && isWificanusful()) {
                        StaticInstanceUtils.statusBar.wifi.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.statusbar_wifi0));
                    } else {
                        StaticInstanceUtils.statusBar.wifi.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.statusbar_wifi0_nonet));
                    }
                    break;
                case 1:
                    if (wifiConnected && isWificanusful()) {
                        StaticInstanceUtils.statusBar.wifi.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.statusbar_wifi1));
                    } else {
                        StaticInstanceUtils.statusBar.wifi.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.statusbar_wifi1_nonet));
                    }
                    break;
                case 2:
                    if (wifiConnected && isWificanusful()) {
                        StaticInstanceUtils.statusBar.wifi.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.statusbar_wifi2));
                    } else {
                        StaticInstanceUtils.statusBar.wifi.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.statusbar_wifi2_nonet));
                    }
                    break;
                case 3:
                    if (wifiConnected && isWificanusful()) {
                        StaticInstanceUtils.statusBar.wifi.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.statusbar_wifi3));
                    } else {
                        StaticInstanceUtils.statusBar.wifi.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.statusbar_wifi4_nonet));
                    }
                    break;
                case 4:
                    if (wifiConnected && isWificanusful()) {
                        StaticInstanceUtils.statusBar.wifi.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.statusbar_wifi4));
                    } else {
                        StaticInstanceUtils.statusBar.wifi.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.statusbar_wifi4_nonet));
                    }
                    break;
                default:
                    break;
            }

        }
    }

    public boolean isWificanusful() {
        try {
            networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            wifiCanUse = networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wifiCanUse;
    }

    //判读网络是否可用,这种方法，时效性太差，延迟过高，不能用。
    public boolean available() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("ping -c 1 www.baidu.com");
            int exitValue = ipProcess.waitFor();
            Log.i("Avalible", "Process:" + exitValue);
            return (exitValue == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
