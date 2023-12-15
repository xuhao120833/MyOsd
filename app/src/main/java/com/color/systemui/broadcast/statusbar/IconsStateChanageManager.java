package com.color.systemui.broadcast.statusbar;

import android.widget.ImageView;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.ConnectivityManager;
import android.hardware.usb.UsbManager;

import com.color.systemui.broadcast.statusbar.icons.EthernetIconChangeReceiver;
import com.color.systemui.broadcast.statusbar.icons.HotspotIconChangeReceiver;
import com.color.systemui.broadcast.statusbar.icons.UdiskIconChangeReceiver;
import com.color.systemui.broadcast.statusbar.icons.WifiIconChangeReceiver;

public class IconsStateChanageManager {

    private Context mycontext;

    private IntentFilter udisk_intentFilter = new IntentFilter();

    private IntentFilter wifi_intentFilter = new IntentFilter();

    private IntentFilter ethernet_intentFilter = new IntentFilter();

    private IntentFilter hotspot_intentFilter = new IntentFilter();

    private UdiskIconChangeReceiver udiskIconChangeReceiver = new UdiskIconChangeReceiver();

    private WifiIconChangeReceiver wifiIconChangeReceiver = new WifiIconChangeReceiver();

    private EthernetIconChangeReceiver ethernetIconChangeReceiver = new EthernetIconChangeReceiver();

    private HotspotIconChangeReceiver hotspotIconChangeReceiver = new HotspotIconChangeReceiver();

    public IconsStateChanageManager() {

    }

    public void setContext(Context context) {
        mycontext = context;

        //1、初始化各个icon的广播接收者
        initBroadcastReceiver();
    }

    private void initBroadcastReceiver() {

        //1、U盘
        udiskIconChangeReceiver.setContext(mycontext);
//        udisk_intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
//        udisk_intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        udisk_intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        udisk_intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        mycontext.registerReceiver(udiskIconChangeReceiver,udisk_intentFilter);

        //2、Wifi
        wifiIconChangeReceiver.setContext(mycontext);
        wifi_intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//wifi 开关打开与否
        wifi_intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//wifi的连接状态
        wifi_intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);//wifi连接之后信号强度
        mycontext.registerReceiver(wifiIconChangeReceiver,wifi_intentFilter);

        //3、以太网
        ethernetIconChangeReceiver.setContext(mycontext);
        ethernet_intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mycontext.registerReceiver(ethernetIconChangeReceiver,ethernet_intentFilter);

        //4、移动网络，预留

        //5、热点
        hotspotIconChangeReceiver.setContext(mycontext);
        hotspot_intentFilter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        mycontext.registerReceiver(hotspotIconChangeReceiver,hotspot_intentFilter);
    }

}
