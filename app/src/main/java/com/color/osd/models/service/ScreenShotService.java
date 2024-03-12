package com.color.osd.models.service;

import static com.color.osd.utils.CltBitmapUtil.image2Bitmap;
import static com.color.osd.utils.CltBitmapUtil.saveBitmap;
import static com.color.osd.utils.ConstantProperties.SCREENSHOT_OVER_ACTION;

import android.annotation.SuppressLint;
import android.app.Service;
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
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.color.osd.R;
import com.color.osd.ScreenShotActivity;
import com.color.osd.utils.ConstantProperties;
import com.color.osd.utils.DensityUtil;

import java.io.File;
import java.util.Objects;

public class ScreenShotService extends Service {
   private static final String TAG = "ScreenShotService";
   public MediaProjectionManager mediaProjectionManager;
   public MediaProjection mediaProjection;
   private Context mContext;

   private ImageView imageView;
   private View layout;

   private  WindowManager.LayoutParams lp;

   private Handler mainHandler;

   private WindowManager wm;

   private String saveName = "screen_shot.png";

   private static final int CLOSE_IMAGE_VIEW = 100;

   @Override
   public void onCreate() {
      super.onCreate();
      //Log.d(TAG, "onCreate: here~");
      mContext = this;
      wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
      // 构造出截屏结果的展示View
      initScreenShotResultShowView();
      mainHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
         @Override
         public boolean handleMessage(Message msg) {
            switch (msg.what){
               case CLOSE_IMAGE_VIEW:
                  // 移除展示截屏的view窗口
                  try{
                     // 1、移除当前显示的结果
                     wm.removeViewImmediate(layout);
                     // 2、发送一个截屏完毕广播，通知OSD的dialog显示出来
                     Intent screenShotOverIntent = new Intent();
                     screenShotOverIntent.setAction(SCREENSHOT_OVER_ACTION);
                     screenShotOverIntent.setPackage("com.color.osd");
                     sendBroadcast(screenShotOverIntent);
                  }catch (Exception e){
                     e.printStackTrace();
                  }
                  break;
            }
            return false;
         }
      });

   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
      //Log.d(TAG, "onStartCommand: start command");
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
      return new ScreenShotBinder();
   }


   /**
    * 构造一个binder对象，用于activity与service之间的通信
    */
   public class ScreenShotBinder extends Binder{
      public void setMediaProjection(MediaProjection mediaProjection_){
         mediaProjection = mediaProjection_;

         setUpVirtualDisplay();
      }
   }

   public void setUpVirtualDisplay(){
      Bitmap screenShotBitmap = screenShot(mediaProjection);
//        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
//        layoutParams.width = (int) DensityUtil.getScaledValue(ConstantProperties.SCREENSHOT_IMAGEVIEW_WIDTH_DP);
//        layoutParams.height = (int) DensityUtil.getScaledValue(ConstantProperties.SCREENSHOT_IMAGEVIEW_HEIGHT_DP);
//        imageView.setLayoutParams(layoutParams);
      imageView.setImageBitmap(screenShotBitmap);
      wm.addView(layout, lp);

      Message msg = Message.obtain();
      msg.what = CLOSE_IMAGE_VIEW;
      mainHandler.sendMessageDelayed(msg, 2000);

      // 开个任务去保存图片
      mainHandler.post(new Runnable() {
         @Override
         public void run() {
            saveName = System.currentTimeMillis() + ".png";
            // 1 先保存到本地
            saveBitmap(saveName, screenShotBitmap);
            // 2 通知相册
//                try {
//                    MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
//                            file.getAbsolutePath(), saveName, null);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
            // 3 最后通知图库更新
            String path = Environment.getExternalStorageDirectory().getPath();
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));

         }
      });

   }

   @RequiresApi(api = Build.VERSION_CODES.KITKAT)
   public Bitmap screenShot(MediaProjection mediaProjection) {
      DisplayMetrics metrics = new DisplayMetrics();
      wm.getDefaultDisplay().getRealMetrics(metrics);
      int width = metrics.widthPixels;
      int height = metrics.heightPixels;
      int density = (int) metrics.density;
      int densityDpi = (int) metrics.densityDpi;
      //Log.d(TAG, "screenShot: " + density + ", " + densityDpi);
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

   public void initScreenShotResultShowView(){
      if (imageView == null || layout == null){
         LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         layout = inflater.inflate(R.layout.screen_shot_result, null);
         imageView = layout.findViewById(R.id.screen_shot);

         lp = new WindowManager.LayoutParams();
         lp.width = (int) DensityUtil.getScaledValue(ConstantProperties.SCREENSHOT_IMAGEVIEW_WIDTH_DP);
         lp.height = (int) DensityUtil.getScaledValue(ConstantProperties.SCREENSHOT_IMAGEVIEW_HEIGHT_DP);
         lp.gravity = Gravity.BOTTOM | Gravity.START;
         lp.flags = WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                 WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                 WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
         lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
         lp.format = PixelFormat.RGBA_8888;


         // 添加点击事件，跳转到文件管理器--图片预览界面
         imageView.setOnClickListener(v -> {
            Log.d(TAG, "initScreenShotResultShowView: click here");
            String basePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String screenShotPath = basePath + "/DCIM/screen_shot/";
            File saveFile = new File(screenShotPath, saveName);
            Uri contentUri = FileProvider.getUriForFile(mContext, "com.color.osd.fileprovider", saveFile);

            try{
               Intent intent = new Intent();
               intent.setAction(Intent.ACTION_VIEW);
               intent.addCategory(Intent.CATEGORY_DEFAULT);
               intent.setDataAndType(contentUri, "image/*");
               intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
            }catch (Exception e){
               e.printStackTrace();
            }

         });

      }
   }

}
