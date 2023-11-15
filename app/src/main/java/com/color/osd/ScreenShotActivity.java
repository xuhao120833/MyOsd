package com.color.osd;

import static com.color.osd.utils.CltBitmapUtil.image2Bitmap;
import static com.color.osd.utils.CltBitmapUtil.saveBitmap;
import static com.color.osd.utils.ConstantProperties.DEBUG;
import static com.color.osd.utils.ConstantProperties.SCREENSHOT_OVER_ACTION;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;


public class ScreenShotActivity extends Activity{
    private static final String TAG = ScreenShotActivity.class.getSimpleName();
    public static final int REQUEST_MEDIA_PROJECTION = 10001;
    private static final int EXIT = 0;

    private Context mContext;
    private MediaProjectionManager mediaProjectionManager;
    public MediaProjection mediaProjection;

    public ImageView imageView;

    private Handler mainHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case EXIT:
                    finishActivity();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 全屏显示 去掉顶部状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mContext = this;
        imageView = findViewById(R.id.screen_shot);
        if (DEBUG) Log.d(TAG, "onCreate 启动");
        getScreenShotPower();

//        button = findViewById(R.id.button1);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("TAG", "onClick: here123");
//            }
//        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void getScreenShotPower()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            mediaProjectionManager =  (MediaProjectionManager)getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            if (mediaProjection == null) {
                Intent intent = mediaProjectionManager.createScreenCaptureIntent();
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
        if (requestCode == REQUEST_MEDIA_PROJECTION && data != null)
        {
            if (DEBUG) Log.d(TAG, "onActivityResult: 拿到结果了，马上开始解析");
            mediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, data);
            setUpVirtualDisplay();
        }
    }

    public void setUpVirtualDisplay(){
        Bitmap screenShotBitmap = screenShot(mediaProjection);
        imageView.setImageBitmap(screenShotBitmap);

        // 开个任务去保存图片
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                String saveName = System.currentTimeMillis() + ".png";
                // 1 先保存到本地
                File file = saveBitmap(saveName, screenShotBitmap);

                // 2 通知相册
                try {
                    MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                            file.getAbsolutePath(), saveName, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // 最后通知图库更新
                String path = Environment.getExternalStorageDirectory().getPath();
                mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));

            }
        });

        // 1.5秒后执行退出当前activity  但是这里存在一个隐患，万一图片太大没保存完本地，就直接销毁activity咋办？
        Message msg = Message.obtain();
        msg.what = EXIT;
        mainHandler.sendMessageDelayed(msg, 1500);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Bitmap screenShot(MediaProjection mediaProjection) {
        WindowManager wm1 = this.getWindowManager();
        int width = wm1.getDefaultDisplay().getWidth();
        int height = wm1.getDefaultDisplay().getHeight();
        Objects.requireNonNull(mediaProjection);
        @SuppressLint("WrongConstant")
        ImageReader imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888,60);
        VirtualDisplay virtualDisplay = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
        {
            virtualDisplay = mediaProjection.createVirtualDisplay("screen", width, height, 1,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, imageReader.getSurface(),
                    null, null);
        }
        SystemClock.sleep(500);   // 隔500毫秒再取数据
        //取最新的图片
        Image image = imageReader.acquireLatestImage();
        // Image image = imageReader.acquireNextImage();
        //释放 virtualDisplay,不释放会报错
        virtualDisplay.release();
        return image2Bitmap(image);      //将Image转为Bitmap
    }

    public void finishActivity(){
        // 1、发送一个截屏完毕广播，通知OSD的dialog显示出来
        Intent screenShotOverIntent = new Intent();
        screenShotOverIntent.setAction(SCREENSHOT_OVER_ACTION);
        screenShotOverIntent.setPackage("com.color.osd");
        sendBroadcast(screenShotOverIntent);

        // 2、结束当前activity
        this.finish();
    }
}
