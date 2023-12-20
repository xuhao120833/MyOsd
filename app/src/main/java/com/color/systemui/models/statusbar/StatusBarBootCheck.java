package com.color.systemui.models.statusbar;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import java.util.HashMap;
import java.util.Map;

import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

public class StatusBarBootCheck implements Instance {

    private Context mycontext;

    private WifiManager wifiManager;

    ConnectivityManager connectivityManager;

    NetworkInfo networkInfo;

    HashMap<String, UsbDevice> deviceHashMap;

    public StatusBarBootCheck() {

    }

    public void setContext(Context context) {
        mycontext = context;

        wifiManager = (WifiManager) mycontext.getSystemService(Context.WIFI_SERVICE);
        deviceHashMap = ((UsbManager) mycontext.getSystemService(Context.USB_SERVICE)).getDeviceList();
        connectivityManager = (ConnectivityManager) mycontext.getSystemService(Context.CONNECTIVITY_SERVICE);

        //Check
        Check();

    }

    public void Check() {
        //Wifi检查
        WifiCheck();

        //U盘检查
        UsbDeviceCheck();

        //以太网检查
        EthernetCheck();
    }

    private void WifiCheck() {
        if (wifiManager != null && wifiManager.isWifiEnabled() == true ) {
            //Log.d("WifiCheck", " wifiManager.isWifiEnabled() == true");
            STATIC_INSTANCE_UTILS.statusBar.wifi.setVisibility(View.VISIBLE);
            StaticVariableUtils.WifiOpen = true;
        }

        if (wifiManager != null && wifiManager.isWifiEnabled() == false ) {
            //Log.d("WifiCheck", " wifiManager.isWifiEnabled() == false");
            STATIC_INSTANCE_UTILS.statusBar.wifi.setVisibility(View.GONE);
            StaticVariableUtils.WifiOpen = false;
        }
    }

    private void  UsbDeviceCheck() {

        for (Map.Entry entry : deviceHashMap.entrySet()) {
            //Log.d("StatusBarBootCheck", "detectUsbDeviceWithUsbManager: " + entry.getKey() + ", " + entry.getValue());
            if(entry != null) {
                STATIC_INSTANCE_UTILS.statusBar.udisk.setVisibility(View.VISIBLE);
                StaticVariableUtils.haveUsbDevice = true;
            }
        }

    }

    private void EthernetCheck() {
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            //Log.d("UsbDeviceCheck 网络类型 " , String.valueOf(networkInfo.getType()));
            if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET && networkInfo.isConnected()) {

                STATIC_INSTANCE_UTILS.statusBar.ethernet.setVisibility(View.VISIBLE);
                StaticVariableUtils.EthernetConnected = true;

            }
        }

    }



}
