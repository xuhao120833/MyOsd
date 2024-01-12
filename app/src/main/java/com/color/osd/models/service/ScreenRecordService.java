package com.color.osd.models.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.core.app.NotificationCompat;

import com.color.osd.R;
import com.color.osd.ScreenRecordActivity;
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
    private boolean isRecording = false;
    private WindowManager wm;

    private boolean isVideoSd = true;
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: i get the cmd: onStartCommand");
        Notification notification = createNotification();
        // 将服务设置为前台服务
        startForeground(NOTIFICATION_ID, notification);

        if (mediaProjection == null){
            //Log.d(TAG, "the mediaProjection is null need to" );
            // 说明没有拿到这个对象，那么需要开启一个activity去startActivityForResult，从而获取mediaProjection对象，再通过binder传递回来
            Log.d(TAG, "the mediaProjection is null, need to start activity" );
            // 说明没有拿到这个对象，那么需要开启一个activity去startActivityForResult，
            // 从而获取mediaProjection对象，再通过binder传递回来

            Intent activityIntent = new Intent();
            activityIntent.setClass(mContext, ScreenRecordActivity.class);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(activityIntent);
        }else{
            start();
        }
        return START_STICKY;
    }

    private Notification createNotification() {
        // 创建通知的代码，这里只是一个示例
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelId")
                .setSmallIcon(R.drawable.volume_positive)
                .setContentTitle("录屏服务")
                .setContentText("录屏服务正在运行中~");

        // 为了兼容Android 8.0及以上，需要设置通知的渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channelId", "Screen Capture Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        return builder.build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: i get bind service");
        return new ScreenRecordBinder();
    }




    public class ScreenRecordBinder extends Binder{
        public ScreenRecordService getService(){
            return ScreenRecordService.this;
        }
    }

    /**
     * 这个方法只会来自于ScreenRecordActivity中，说明用户同意录屏了
     * @param mediaProjection_ 在ScreenRecordActivity获取的对象，传递回来用
     */
    public void setMediaProjection(MediaProjection mediaProjection_){
        mediaProjection = mediaProjection_;
        start();
    }


    public void start() {
        if (!isRecording) {
            isRecording = true;
            startRecording();
        }
    }

    public void stopRecord() {
        if (isRecording) {
            isRecording = false;
            stopMediaProjection();
        }
    }


    private void stopMediaProjection() {
        Log.d(TAG, "stopMediaProjection: 结束录屏了~");
        if (mediaProjection != null) {
            mediaProjection.stop();
        }

        if (mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }


    private void startRecording(){
        Log.d(TAG, "startRecording: 开始录屏了~");
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

        mediaRecorder = new MediaRecorder();
        // 设置音频源 麦克风
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置视频相关的
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(ConstantProperties.SCREENRECORD_SAVE_PATH + "/" + videoQuality + curTime + ".mp4");
        mediaRecorder.setVideoSize(width, height);  //after setVideoSource(), setOutFormat()
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
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
            virtualDisplay = mediaProjection.createVirtualDisplay(TAG, mScreenWidth, mScreenHeight, (int)mScreenDensity,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder.getSurface(), null, null);
            mediaRecorder.start();
        }catch (Exception e){
            Log.d(TAG, "startRecording: " + e.getMessage());
            e.printStackTrace();
        }

    }


    private VirtualDisplay createVirtualDisplay() {
        return mediaProjection.createVirtualDisplay(TAG, mScreenWidth, mScreenHeight, (int) mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder.getSurface(), null, null);

    }

    @Override
    public void onDestroy() {
        stopRecord();
        super.onDestroy();

    }
}
