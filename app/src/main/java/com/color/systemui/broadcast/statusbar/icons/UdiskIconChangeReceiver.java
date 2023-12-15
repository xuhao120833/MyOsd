package com.color.systemui.broadcast.statusbar.icons;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;

import com.color.systemui.utils.StaticInstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import java.lang.*;

import android.view.View;
import android.util.Log;
import android.hardware.usb.UsbManager;
import java.util.HashMap;
import java.util.Map;
import android.hardware.usb.UsbDevice;

public class UdiskIconChangeReceiver extends BroadcastReceiver{

    private Context mycontext;

    HashMap<String, UsbDevice> deviceHashMap;


    public UdiskIconChangeReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(intent.getAction())) { // U盘插入且挂载完毕
            Log.d("MyUdisk 当前的package", StaticInstanceUtils.mgetTopActivity.getPackage());
            if ("com.android.launcher3".equals(StaticInstanceUtils.mgetTopActivity.getPackage()) || "com.color.settings".equals(StaticInstanceUtils.mgetTopActivity.getPackage())) {
                StaticInstanceUtils.statusBar.udisk.setVisibility(View.VISIBLE);
                Log.d("MyUdisk ", "设置U盘可见");
            }
            StaticVariableUtils.haveUsbDevice = true;
        } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(intent.getAction())) { //U盘拔出卸载完毕
            deviceHashMap = ((UsbManager) mycontext.getSystemService(Context.USB_SERVICE)).getDeviceList();

            for (Map.Entry entry : deviceHashMap.entrySet()) {
                Log.d("MyUdisk", "detectUsbDeviceWithUsbManager: " + entry.getKey() + ", " + entry.getValue());
                if (entry != null) {
                    Log.d("MyUdisk", "entry != null: ");
                    StaticVariableUtils.haveUsbDevice = true;
                    return;
                }
            }

            StaticInstanceUtils.statusBar.udisk.setVisibility(View.GONE);
            Log.d("MyUdisk ", "设置U盘消失");
            StaticVariableUtils.haveUsbDevice = false;

        }
    }

    public void setContext(Context context) {
        mycontext = context;
    }

}
