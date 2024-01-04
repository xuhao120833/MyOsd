package com.color.notification.view.quick_settings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.color.systemui.interfaces.Instance;

public class CustomSeekBar_Brightness extends androidx.appcompat.widget.AppCompatSeekBar implements Instance {

    private Paint maskPaint;
    private boolean isSelected = false;

    public float brightness_maximum = 255;

    public CustomSeekBar_Brightness(Context context) {
        super(context);
        init();
    }

    public CustomSeekBar_Brightness(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSeekBar_Brightness(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        maskPaint = new Paint();
        // 设置遮罩层颜色为激活状态的颜色
        maskPaint.setColor(Color.argb(80, 200, 200, 200));

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
        Log.d("CustomSeekBar_Brightness", " 进入判读");
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            try {
                Log.d("CustomSeekBar_Brightness", " 向左调节亮度");
                int percentage = updateBrightness(-13);
                setProgress(percentage);
                STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.brightnessSeekBar_text.setText(percentage + "%");

            } catch (Settings.SettingNotFoundException e) {
                throw new RuntimeException(e);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            try {
                Log.d("CustomSeekBar_Brightness", " 向右调节亮度");
                int percentage = updateBrightness(13);
                setProgress(percentage);
                STATIC_INSTANCE_UTILS.notification_quick_settings_adapter.brightnessSeekBar_text.setText(percentage + "%");
            } catch (Settings.SettingNotFoundException e) {
                throw new RuntimeException(e);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    public int updateBrightness(int delta) throws Settings.SettingNotFoundException {
        int brightness = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);

        brightness = delta > 0 ? Math.min(255, brightness + delta) : Math.max(0, brightness + delta);
        //Log.d("TAG", "upBrightness: " + brightness);
        // 设置系统亮度
        Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);

        Log.d("CustomSeekBar_Brightness", " 亮度值" + String.valueOf(toPercent(brightness)));

        return toPercent(brightness);
    }

    private int toPercent(int brightness) {
        // 映射到百分比, 四舍五入取整
        return Math.round((brightness / brightness_maximum) * 100.0f);
    }
}
