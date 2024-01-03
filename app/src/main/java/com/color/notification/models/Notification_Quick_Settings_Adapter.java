package com.color.notification.models;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.os.SystemProperties;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.color.notification.view.quick_settings.CustomSeekBar_Brightness;
import com.color.notification.view.quick_settings.CustomSeekBar_Volume;
import com.color.osd.R;
import com.color.osd.models.service.ScreenShotService;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.StaticVariableUtils;


public class Notification_Quick_Settings_Adapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> implements Instance {

    Context mycontext;

    private View notification_quick_settings;

    public ImageView screenshot, eye_protection, camera;

    public TextView camera_text, brightnessSeekBar_text;

    public CustomSeekBar_Brightness brightnessSeekBar;
    public CustomSeekBar_Volume volumeSeekBar;

    public Quick_Settings_ViewHolder quick_settings_viewHolder;

    public Notification_Quick_Settings_Adapter() {

    }

    public void setContext(Context context) {
        mycontext = context;
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        notification_quick_settings = LayoutInflater.from(mycontext).inflate(R.layout.notification_quick_settings, parent, false);
        setQuickSettingsClick();
        quick_settings_viewHolder = new Quick_Settings_ViewHolder(notification_quick_settings);
        return (T) quick_settings_viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull T holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class Quick_Settings_ViewHolder extends RecyclerView.ViewHolder {

        public Quick_Settings_ViewHolder(View itemView) {//这个view参数就是recyclerview子项的最外层布局
            super(itemView);
        }
    }

    private void setQuickSettingsClick() {
        screenshot = (ImageView) notification_quick_settings.findViewById(R.id.screenshot);
        eye_protection = (ImageView) notification_quick_settings.findViewById(R.id.eye_protection);
        camera = (ImageView) notification_quick_settings.findViewById(R.id.camera);
        camera_text = (TextView) notification_quick_settings.findViewById(R.id.camera_text);

        brightnessSeekBar = (CustomSeekBar_Brightness) notification_quick_settings.findViewById(R.id.brightnessSeekBar);
        brightnessSeekBar_text = (TextView) notification_quick_settings.findViewById(R.id.brightnessSeekBar_text);

        volumeSeekBar = (CustomSeekBar_Volume) notification_quick_settings.findViewById(R.id.volumeSeekBar);

        screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                STATIC_INSTANCE_UTILS.myNotification.notification.setVisibility(View.GONE);

                Intent intentService = new Intent(mycontext, ScreenShotService.class);
                mycontext.startService(intentService);
            }
        });

        eye_protection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Notification_Quick_Settings_Adapter","eye_protection.setOnClickListener");
                if (StaticVariableUtils.eye_protection_state.equals("off")) {
                    Settings.Global.putInt(mycontext.getContentResolver(), StaticVariableUtils.EYE_PROTECT_MODE, 1);
                    eye_protection.setImageResource(R.drawable.quick_settings_eye_protection_on);
                    StaticVariableUtils.eye_protection_state = "on";
                    Log.d("Notification_Quick_Settings_Adapter","打开护眼模式");
                }else if(StaticVariableUtils.eye_protection_state.equals("on")) {
                    Settings.Global.putInt(mycontext.getContentResolver(), StaticVariableUtils.EYE_PROTECT_MODE, 0);
                    eye_protection.setImageResource(R.drawable.quick_settings_eye_protection);
                    StaticVariableUtils.eye_protection_state = "off";
                    Log.d("Notification_Quick_Settings_Adapter","打开护眼模式");
                    return;
                }
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StaticVariableUtils.camera_rotate_degrees.equals("0")) {
                    SystemProperties.set("sys.camera.orientation", "90");
                    StaticVariableUtils.camera_rotate_degrees = "90";
                    camera.setImageResource(R.drawable.quick_settings_camera_90);
                    camera_text.setText("90°");
                } else if (StaticVariableUtils.camera_rotate_degrees.equals("90")) {
                    SystemProperties.set("sys.camera.orientation", "180");
                    StaticVariableUtils.camera_rotate_degrees = "180";
                    camera.setImageResource(R.drawable.quick_settings_camera_180);
                    camera_text.setText("180°");
                } else if (StaticVariableUtils.camera_rotate_degrees.equals("180")) {
                    SystemProperties.set("sys.camera.orientation", "270");
                    StaticVariableUtils.camera_rotate_degrees = "270";
                    camera.setImageResource(R.drawable.quick_settings_camera_270);
                    camera_text.setText("270°");
                } else if (StaticVariableUtils.camera_rotate_degrees.equals("270")) {
                    SystemProperties.set("sys.camera.orientation", "0");
                    StaticVariableUtils.camera_rotate_degrees = "0";
                    camera.setImageResource(R.drawable.quick_settings_camera_0);
                    camera_text.setText("摄像头");
                }
            }
        });

//        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Log.d("Notification_Quick_Settings_Adapter","onProgressChanged");
//                // 将进度调整为5的倍数
//                int adjustedProgress = (int) (Math.ceil(progress / 5.0) * 5);
//
//                // 判断用户是增加还是减少
//                if (progress > adjustedProgress) {
//                    // 减少，设置为adjustedProgress - 5
//                    adjustedProgress = Math.max(0, adjustedProgress - 5);
//                } else {
//                    // 增加，设置为adjustedProgress + 5
//                    adjustedProgress = Math.min(seekBar.getMax(), adjustedProgress + 5);
//                }
//
//                // 设置调整后的进度给SeekBar
//                seekBar.setProgress(adjustedProgress);
//
//                // 更新文本显示
//                brightnessSeekBar_text.setText(adjustedProgress + "%");
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {//开始滑动
//                Log.d("Notification_Quick_Settings_Adapter","onStartTrackingTouch");
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {//停止滑动
//                Log.d("Notification_Quick_Settings_Adapter","onStopTrackingTouch");
//            }
//        });


    }
}
