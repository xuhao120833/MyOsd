package com.color.notification.view.quick_settings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;

import com.color.systemui.interfaces.Instance;

public class CustomSeekBar_Volume extends androidx.appcompat.widget.AppCompatSeekBar implements Instance {

    private Paint maskPaint;
    private boolean isSelected = false;

    private int volume = 0;

    private AudioManager audioManager;

    public CustomSeekBar_Volume(Context context) {
        super(context);
        init(context);
    }

    public CustomSeekBar_Volume(Context context, AttributeSet attrs) throws Settings.SettingNotFoundException {
        super(context, attrs);
        init(context);
    }

    public CustomSeekBar_Volume(Context context, AttributeSet attrs, int defStyleAttr) throws Settings.SettingNotFoundException {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        maskPaint = new Paint();
        // 设置遮罩层颜色为激活状态的颜色
        maskPaint.setColor(Color.argb(80, 200, 200, 200));

        if (audioManager == null) {
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }

        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    private int getActivatedColor() {
        int[] attrs = new int[]{android.R.attr.colorActivatedHighlight};
        int colorResId = 0;

        if (getContext().getTheme().resolveAttribute(android.R.attr.colorActivatedHighlight, new android.util.TypedValue(), true)) {
            colorResId = getContext().getTheme().obtainStyledAttributes(new android.util.TypedValue().data, attrs).getResourceId(0, 0);
        }

        return getResources().getColor(colorResId);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, android.graphics.Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

        isSelected = gainFocus;
        //invalidate(); // 重新绘制SeekBar
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isSelected) {
            // 绘制遮罩层
            canvas.drawRect(0, 0, getWidth(), getHeight(), maskPaint);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 这种view的keyDown回调中是监听不到音量、亮度变化的。所以按键小板的音量和亮度加减单独走一套逻辑，不会走这里。
        Log.d("CustomSeekBar_Volume", " 进入判读");
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            updateVolume(-1);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            updateVolume(1);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void updateVolume(int delta){
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volume = delta > 0 ? Math.min(15, volume + delta) : Math.max(0, volume + delta);  // 约束volume始终在[0, 15]之间
        //Log.d("TAG", "updateVolume: " + volume);
        // 设置音量
        setSystemMusicVolume(volume);
        setProgress(volume);
        STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.volumeSeekBar_text.setText((int)(((float) volume / 15) * 100) + "%");

    }

    public void setSystemMusicVolume(int volume) {
        if (audioManager != null) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
        }
    }


}
