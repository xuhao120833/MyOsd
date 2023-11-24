package com.color.osd.utils;

import static com.color.osd.utils.ConstantProperties.DEBUG;
import static com.color.osd.utils.ConstantProperties.SCREENSHOT_SAVE_PATH;

import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class CltBitmapUtil {
    public static Bitmap image2Bitmap(Image image)
    {
        if (image == null){
            if (DEBUG) Log.d("CltBitmapUtil", "image2Bitmap: the image is null");
            return null;
        }

        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width+ rowPadding / pixelStride , height,Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        //截取图片
        // Bitmap cutBitmap = Bitmap.createBitmap(bitmap,0,0,width/2,height/2);
        //压缩图片
        // Matrix matrix = new Matrix();
        // matrix.setScale(0.5F, 0.5F);
        // System.out.println(bitmap.isMutable());
        // bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        image.close();
        return bitmap;
    }

    public static boolean saveBitmap(String name, Bitmap bm) {
        //指定我们想要存储文件的地址
        String targetPath = SCREENSHOT_SAVE_PATH;
        File picDir = new File(targetPath);

        //判断指定文件夹的路径是否存在
        if (!picDir.exists()) {
            if(DEBUG) Log.d("CltBitmapUtil", "saveBitmap: TargetPath isn't exist");
            picDir.mkdirs();
        }
        //如果指定文件夹创建成功，那么我们则需要进行图片存储操作
        File saveFile = new File(targetPath, name);

        try(FileOutputStream saveImgOut = new FileOutputStream(saveFile)) {
            // compress - 压缩的意思
            bm.compress(Bitmap.CompressFormat.PNG, 100, saveImgOut);
            //存储完成后需要清除相关的进程
            saveImgOut.flush();
            saveImgOut.close();
            if(DEBUG) Log.d("CltBitmapUtil", "saveBitmap: The picture is save to your phone!");
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 参数1转换类型，参数2压缩质量，参数3字节流资源
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

}
