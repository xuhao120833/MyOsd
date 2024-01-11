package com.color.notification.models;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.os.SystemProperties;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.color.notification.utils.BrightnessUtils;
import com.color.notification.view.quick_settings.CustomSeekBar_Brightness;
import com.color.notification.view.quick_settings.CustomSeekBar_Volume;
import com.color.osd.R;
import com.color.osd.models.service.ScreenShotService;
import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.StaticVariableUtils;


public class Notification_Quick_Settings_Adapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> implements Instance {

    Context mycontext;

    private View notification_quick_settings;

    private AudioManager audioManager;

    int brightness = 0;

    int volume = 0;

    public ImageView screenshot, eye_protection, camera;

    public TextView camera_text, brightnessSeekBar_text, volumeSeekBar_text;

    public CustomSeekBar_Brightness brightnessSeekBar;
    public CustomSeekBar_Volume volumeSeekBar;

    public Quick_Settings_ViewHolder quick_settings_viewHolder;

    public Notification_Quick_Settings_Adapter() {

    }

    public void setContext(Context context) {
        mycontext = context;

        if (audioManager == null) {
            audioManager = (AudioManager) mycontext.getSystemService(Context.AUDIO_SERVICE);
        }
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            brightnessSeekBar.setMin(0);
        }
        brightnessSeekBar.setMax(255);
        brightnessSeekBar_text = (TextView) notification_quick_settings.findViewById(R.id.brightnessSeekBar_text);
//        try {
//            brightness = Settings.System.getInt(mycontext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
//        } catch (Settings.SettingNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        brightnessSeekBar.setProgress(STATIC_INSTANCE_UTILS.brightnessChangeCompute.toPercent(brightness));
//        brightnessSeekBar_text.setText(STATIC_INSTANCE_UTILS.brightnessChangeCompute.toPercent(brightness) + "%");
        brightness = BrightnessManager.readTemporaryBrightness(mycontext);
        brightnessSeekBar.setProgress(brightness);
        brightnessSeekBar_text.setText(Math.round(brightness/2.55)+"%");

        volumeSeekBar = (CustomSeekBar_Volume) notification_quick_settings.findViewById(R.id.volumeSeekBar);
        volumeSeekBar.setProgress(volume);
        volumeSeekBar_text = (TextView) notification_quick_settings.findViewById(R.id.volumeSeekBar_text);
        volumeSeekBar_text.setText((int)(((float) volume / 15) * 100) + "%");

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
                Log.d("Notification_Quick_Settings_Adapter", "eye_protection.setOnClickListener");
                if (StaticVariableUtils.eye_protection_state.equals("off")) {
                    Settings.Global.putInt(mycontext.getContentResolver(), StaticVariableUtils.EYE_PROTECT_MODE, 1);
                    eye_protection.setImageResource(R.drawable.quick_settings_eye_protection_on);
                    StaticVariableUtils.eye_protection_state = "on";
                    Log.d("Notification_Quick_Settings_Adapter", "打开护眼模式");
                } else if (StaticVariableUtils.eye_protection_state.equals("on")) {
                    Settings.Global.putInt(mycontext.getContentResolver(), StaticVariableUtils.EYE_PROTECT_MODE, 0);
                    eye_protection.setImageResource(R.drawable.quick_settings_eye_protection);
                    StaticVariableUtils.eye_protection_state = "off";
                    Log.d("Notification_Quick_Settings_Adapter", "打开护眼模式");
                    return;
                }
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StaticVariableUtils.camera_rotate_degrees.equals("0")) {
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

        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 进度发生改变时的操作
                Log.d("CustomSeekBar_Brightness", " 进度条被拖动" + String.valueOf(progress));
//                int myprogress =  (int) ((float) progress / 100.0f * 255);
//                Log.d("CustomSeekBar_Brightness", " 设置的亮度值" + String.valueOf(myprogress));
//                Settings.System.putInt(mycontext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, myprogress);
                BrightnessManager.setBrightness(progress,mycontext);
                brightnessSeekBar_text.setText(Math.round(progress/2.55) + "%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 用户开始拖动SeekBar时的操作
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 用户停止拖动SeekBar时的操作
            }
        });

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 更新SeekBar的进度
//                STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.volumeSeekBar.setProgress(progress);
                if (audioManager != null) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_PLAY_SOUND);
                }
//                STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.volumeSeekBar_text.setText((int)(((float) progress / 15) * 100) + "%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 用户开始拖动SeekBar时的操作
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 用户停止拖动SeekBar时的操作
            }
        });


    }
}
