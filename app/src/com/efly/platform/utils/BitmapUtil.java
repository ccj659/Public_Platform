package com.efly.platform.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.efly.platform.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016/3/3.
 */
public class BitmapUtil {
    public static final String LOG_TAG = "HelloCamera";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final String TAG = "BitmapUtil";
    private static boolean isRun = true;
    private static Context context;
    private int i=0;
    private static BitmapUtil bitmapUtil;
    private static File tempFile;
    private boolean getAllPicIsRun = true;
    private int pic2=0,pic1=0;
    public  Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Log.e("getAllPic", "getAllPic is ok");
                    break;
                case 1:
                    Log.e("savePic", "savePicing is ok");

                    break;
                case 2:
                    Log.e("savePic", "tempFile");
                    if (pic1==1){
                        Intent intent = new Intent("android.intent.action.MY_BROADCAST");
                        intent.putExtra("msg", "1");
                        context.sendBroadcast(intent);
                    }else if (pic1==2) {
                        Intent intent = new Intent("android.intent.action.MY_BROADCAST");
                        intent.putExtra("msg", "2");
                        context.sendBroadcast(intent);
                    }
                    break;
                case -2:
                    Log.e("savePic", "IOException");
                    break;
            }
        }
    };

    public BitmapUtil(Context context){
        BitmapUtil.context =context;
    }
    public static BitmapUtil initBitmapUtil(Context context) {

        if (bitmapUtil == null) {
            bitmapUtil = new BitmapUtil(context);
        }
        return bitmapUtil;
    }
    public static   BitmapUtil getInstance() {

        if (bitmapUtil == null) {
            bitmapUtil = new BitmapUtil(context);
        }
        return bitmapUtil;
    }

    /**
     * Create a file Uri for saving an image or video
     */
    public static Uri getOutputMediaFileUri(int type, String name) {
        return Uri.fromFile(getOutputMediaFile(type, name));
    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(int type, String name) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = null;
        try {
            // This location works best if you want the created images to be
            // shared
            // between applications and persist after your app has been
            // uninstalled.
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "MyCamera");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error in Creating mediaStorageDir: "
                    + mediaStorageDir);
        }

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // <uses-permission
                // android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
                Log.e(LOG_TAG,
                        "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile = null;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + name + ".jpg");
            Log.d(LOG_TAG,
                    "sucessfully create mediafile");
        }
        return mediaFile;
    }

    public static String getPhotoURL(String name) {

        File mediaStorageDir;
        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCamera");

        String fileName = getFile(mediaStorageDir.getPath(), name);//判断文件是否包含文件名
        String file = mediaStorageDir.getPath() + File.separator + fileName;
        File file1 = new File(file);
        if (file1.isDirectory()) {
            return null;
        }
        return file;

    }

    public static String getFile(String dir, String contain) {
        if (contain==null){
            return "";
        }

        File file = new File(dir);
        File[] array = file.listFiles();
        Log.e("listFiles-->", array.length + "!"+contain);


        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                // only take file name
                Log.e("getFile-->", array[i].getName());
                if (array[i].getName().contains(contain)) {
                    return array[i].getName();
                } else if (array[i].isDirectory()) {
                    getFile(array[i].getPath(), contain);
                }
            }
        }
        return "";

    }

    public static Bitmap compressImage(Bitmap image, int imageSize) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > imageSize) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    //尺寸压缩：
    public static Bitmap scalePic(InputStream inputStream, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeResource(context.getResources(), R.mipmap.demo, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, new Rect(5, 5, 5, 5), options);
        return bitmap;
        // postInvalidate();
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        Log.e(TAG, bm.getByteCount() + "getByteCount");
        bm = compressImage(bm, 100);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        Log.e(TAG, baos.toByteArray().length + "length");
        return baos.toByteArray();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public Bitmap getPhotoFrom(Context context, String name) {
        Bitmap bitmap = BitmapFactory.decodeFile(getPhotoURL(name));
        if (bitmap == null || bitmap.getByteCount() == 0) {
            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.no_photo);
        } else {
            return bitmap;
        }


    }

    public static File transMsgPicFile(File file,Bitmap bitmap) {

         tempFile=BitmapUtil.getOutputMediaFile(MEDIA_TYPE_IMAGE, "temp");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }


    /*
    * 质量压缩：
    * */

    public int savePic(Context context, final File file, final Bitmap bitmap) {
        Log.e("savePic", "init isRun");
        isRun = true;
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (isRun) {
                    //compressImage(bitmap,)
                    BufferedOutputStream bos = null;
                    try {
                        bos = new BufferedOutputStream(new FileOutputStream(file));
                        Bitmap temp = comp(bitmap);//图片压缩
                        temp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                        bos.flush();
                        bos.close();
                        isRun = false;

                        handler.sendEmptyMessage(1);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(-1);
                        isRun = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(-2);
                        isRun = false;
                    }

                }
            }
        }).start();
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        return 1;
    }
    public synchronized int saveTempPic(Context context, final File file, final Bitmap bitmap, final int key) {
        tempFile=file;
        Log.e("savePic", file.getAbsolutePath());
        isRun = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    //compressImage(bitmap,)
                    BufferedOutputStream bos = null;
                    try {
                        bos = new BufferedOutputStream(new FileOutputStream(file));
                        Bitmap temp = comp(bitmap);//图片压缩
                        temp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                        bos.flush();
                        bos.close();
                        isRun = false;
                        if ( key==1){
                            pic1=1;
                        }else if ( key==2) {
                            pic1=2;
                        }

                        handler.sendEmptyMessage(2);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(-1);
                        isRun = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(-1);
                        isRun = false;
                    }

                }
            }
        }).start();
        return 1;
    }
    public Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }


    //由path 得到压缩后的bitmap
    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    //质量压缩图片
    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


}
