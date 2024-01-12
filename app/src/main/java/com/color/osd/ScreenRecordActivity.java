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

import com.color.osd.models.service.ScreenRecordService;

/**
 * 关于为啥要写这个ScreenRecordActivity或者ScreenShotActivity？
 * 因为MediaProjection对象初始化需要startActivityForResult()去请求
 * startActivityForResult()又只能通过Activity来启动，然后通过onActivityResult()来处理结果。
 *
 * 换句话说MediaProjection和Activity是绑定的一对。
 * 即使MediaProjection是在Service中使用的，但是也得开一个Activity来初始化该对象，在把这个对象通过binder传递回Service。
 * 搞得这个代码极其恶心，fuck google。
 */
public class ScreenRecordActivity extends Activity implements ServiceConnection {
    private static final String TAG = ScreenRecordActivity.class.getSimpleName();
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
        // 通过bind方式拿到service对象
        bindService(new Intent(this, ScreenRecordService.class),
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
                // 就是因为录屏、截屏需要用户点击确认，必须走这个方法去弹出一个对话框，让用户选择。
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
            ScreenRecordService service = binder.getService();
            service.setMediaProjection(mediaProjection);   // 把这个对象传递给service
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


    ScreenRecordService.ScreenRecordBinder binder = null;
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder = (ScreenRecordService.ScreenRecordBinder) service;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        binder = null;
    }
}
