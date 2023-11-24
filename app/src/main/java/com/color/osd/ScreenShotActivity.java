package com.color.osd;

import static com.color.osd.utils.ConstantProperties.DEBUG;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.color.osd.models.service.ScreenShotService;


public class ScreenShotActivity extends Activity implements ServiceConnection {
    private static final String TAG = ScreenShotActivity.class.getSimpleName();
    public static final int REQUEST_MEDIA_PROJECTION = 10001;
    private MediaProjectionManager mediaProjectionManager;
    public MediaProjection mediaProjection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 全屏显示 去掉顶部状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        if (DEBUG) Log.d(TAG, "onCreate 启动");
        bindService(new Intent(this, ScreenShotService.class),
                this, Context.BIND_AUTO_CREATE);
        getScreenShotPower();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void getScreenShotPower() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaProjectionManager =  (MediaProjectionManager)getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            if (mediaProjection == null) {
                Intent intent = mediaProjectionManager.createScreenCaptureIntent();
                if (DEBUG) Log.d(TAG, "getScreenShotPower: " + intent);
                startActivityForResult(intent, REQUEST_MEDIA_PROJECTION);
            }else{
                setUpVirtualDisplay();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_PROJECTION && data != null) {
            if (DEBUG) Log.d("TAG", "getScreenShotPower: 拿到结果了，马上开始解析 " + data.getAction() + ", " + data);
            mediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, data);
            setUpVirtualDisplay();
        }
    }

    public void setUpVirtualDisplay(){
        if (binder != null){
            binder.setMediaProjection(mediaProjection);
        }
        finishActivity();
    }

    @Override
    protected void onDestroy() {
        unbindService(this);
        super.onDestroy();
    }

    public void finishActivity(){
        // 2、结束当前activity
        this.finish();
    }


    ScreenShotService.ScreenShotBinder binder = null;
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder = (ScreenShotService.ScreenShotBinder) service;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
