package com.color.systemui.broadcast.statusbar.icons;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;

import com.color.osd.R;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;

import java.lang.*;

import android.view.View;
import android.hardware.usb.UsbManager;
import java.util.HashMap;
import java.util.Map;
import android.hardware.usb.UsbDevice;

public class UdiskIconChangeReceiver extends BroadcastReceiver implements Instance {

    private Context mycontext;

    HashMap<String, UsbDevice> deviceHashMap;


    public UdiskIconChangeReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(intent.getAction())) { // U盘插入且挂载完毕
                //Log.d("MyUdisk 当前的package", InstanceUtils.mgetTopActivity.getPackage());
                if ("com.android.launcher3".equals(STATIC_INSTANCE_UTILS.mgetTopActivity.getPackage()) || "com.color.settings".equals(STATIC_INSTANCE_UTILS.mgetTopActivity.getPackage())) {
                    STATIC_INSTANCE_UTILS.statusBar.udisk.setVisibility(View.VISIBLE);
                    STATIC_INSTANCE_UTILS.statusBar.udisk.setImageDrawable(mycontext.getResources().getDrawable(R.drawable.statusbar_udisk));
                    //Log.d("MyUdisk ", "设置U盘可见");
                }
                if (!StaticVariableUtils.isViewGroupHasView(STATIC_INSTANCE_UTILS.statusBar.statusbar, STATIC_INSTANCE_UTILS.statusBar.udisk_frame)) {
                    STATIC_INSTANCE_UTILS.statusBar.statusbar.addView(STATIC_INSTANCE_UTILS.statusBar.udisk_frame);
                }
                StaticVariableUtils.haveUsbDevice = true;
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(intent.getAction())) { //U盘拔出卸载完毕
                deviceHashMap = ((UsbManager) mycontext.getSystemService(Context.USB_SERVICE)).getDeviceList();

                for (Map.Entry entry : deviceHashMap.entrySet()) {
                    //Log.d("MyUdisk", "detectUsbDeviceWithUsbManager: " + entry.getKey() + ", " + entry.getValue());
                    if (entry != null) {
                        //Log.d("MyUdisk", "entry != null: ");
                        StaticVariableUtils.haveUsbDevice = true;
                        return;
                    }
                }

                STATIC_INSTANCE_UTILS.statusBar.udisk.setVisibility(View.GONE);
                STATIC_INSTANCE_UTILS.statusBar.statusbar.removeView(STATIC_INSTANCE_UTILS.statusBar.udisk_frame);

                //Log.d("MyUdisk ", "设置U盘消失");
                StaticVariableUtils.haveUsbDevice = false;

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContext(Context context) {
        mycontext = context;
    }

}
