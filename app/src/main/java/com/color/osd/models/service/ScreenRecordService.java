package com.color.osd.models.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.color.osd.ScreenShotActivity;
import com.color.osd.utils.ConstantProperties;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenRecordService extends Service {
    private static final String TAG = ScreenRecordService.class.getSimpleName();
    private Context mContext;
    private MediaProjection mediaProjection;
    //录像机MediaRecorder
    private MediaRecorder mediaRecorder;
    //用于录屏的虚拟屏幕
    private VirtualDisplay virtualDisplay;
    private int mScreenWidth, mScreenHeight;
    private float mScreenDensity;
    private boolean running;
    private WindowManager wm;

    private boolean isVideoSd = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaProjection == null){
            //Log.d(TAG, "the mediaProjection is null need to" );
            // 说明没有拿到这个对象，那么需要开启一个activity去startActivityForResult，从而获取mediaProjection对象，再通过binder传递回来
            Intent activityIntent = new Intent();
            activityIntent.setClass(mContext, ScreenShotActivity.class);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(activityIntent);
        }else{
            setUpVirtualDisplay();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ScreenRecordBinder();
    }




    public class ScreenRecordBinder extends Binder{
        public void setMediaProjection(MediaProjection mediaProjection_){
            mediaProjection = mediaProjection_;

            setUpVirtualDisplay();
        }
    }

    public void setUpVirtualDisplay(){
        mediaRecorder = createMediaRecorder();



    }

    private MediaRecorder createMediaRecorder(){
        // 设置一些保存格式
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date curDate = new Date(System.currentTimeMillis());
        String curTime = formatter.format(curDate).replace(" ", "");
        String videoQuality = "HD";
        if (isVideoSd) videoQuality = "SD";

        // 获取wm的参数
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        mScreenWidth = width;
        mScreenHeight = height;
        mScreenDensity = metrics.density;

        MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(ConstantProperties.SCREENRECORD_SAVE_PATH + "/" + videoQuality + curTime + ".mp4");
        mediaRecorder.setVideoSize(width, height);  //after setVideoSource(), setOutFormat()
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);  //after setOutputFormat()
        int bitRate;
        if (isVideoSd) {
            mediaRecorder.setVideoEncodingBitRate(width * height);
            mediaRecorder.setVideoFrameRate(30);
            bitRate = width * height / 1000;
        } else {
            mediaRecorder.setVideoEncodingBitRate(5 * width * height);
            mediaRecorder.setVideoFrameRate(60); //after setVideoSource(), setOutFormat()
            bitRate = 5 * width * height / 1000;
        }

        try{
            mediaRecorder.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }

        return mediaRecorder;

    }

    private VirtualDisplay createVirtualDisplay() {
        //Log.i(TAG, "Create VirtualDisplay");
        return mediaProjection.createVirtualDisplay(TAG, mScreenWidth, mScreenHeight, (int)mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder.getSurface(), null, null);
    }


}
