package com.color.systemui.models.statusbar;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import java.util.HashMap;
import java.util.Map;

import com.color.osd.R;
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
        Log.d("StatusBarBootCheck"," WifiCheck ");
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            //Log.d("WifiCheck", " wifiManager.isWifiEnabled() == true");
            //wifi开关已打开
            STATIC_INSTANCE_UTILS.statusBar.wifi.setVisibility(View.VISIBLE);
            StaticVariableUtils.WifiOpen = true;

            //wifi是否连接上了
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            Log.d("StatusBarBootCheck"," wifi检测 ");
            if (wifiInfo != null && wifiInfo.getNetworkId() != -1) {

                STATIC_INSTANCE_UTILS.statusBar.wifi.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.statusbar_wifi4_nonet));
                // WiFi 已连接
                int signalStrength = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5); // 5 是信号级别的最大数量

                Log.d("StatusBarBootCheck"," wifi强度 " + signalStrength);
                // 现在你可以使用 'signalStrength' 变量来确定 WiFi 信号强度
                // 例如，你可以根据信号强度更新 UI 元素
//                updateUIBasedOnSignalStrength(signalStrength);

            }

        }

        if (wifiManager != null && !wifiManager.isWifiEnabled()) {
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

//    private void updateUIBasedOnSignalStrength(int signalStrength) {
//        // 根据信号强度更新 UI 元素
//        if
//
//        STATIC_INSTANCE_UTILS.statusBar.wifi.setVisibility(signalStrength >= 3 ? View.VISIBLE : View.GONE);
//    }



}
