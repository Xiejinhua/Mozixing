package com.gz.mozixing.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 拍照辅助类
 * <p/>
 * Created by Clock on 2016/5/21.
 */
public class CapturePhotoHelper {

    private final static String TIMESTAMP_FORMAT = "yyyy_MM_dd_HH_mm_ss";

    public static int CAPTURE_PHOTO_REQUEST_CODE = 0x1111;

    private Activity mActivity;
    /**
     * 存放图片的目录
     */
    private File mPhotoFolder;
    /**
     * 拍照生成的图片文件
     */
    private File mPhotoFile;

    /**
     * @param activity
     */
    public CapturePhotoHelper(Activity activity, int code) {
        this.mActivity = activity;
        this.mPhotoFolder = AppFolderUtil.getAppFolder();
        CAPTURE_PHOTO_REQUEST_CODE = code;
    }

    /**
     * 拍照
     */
    public void capture() {
        if (hasCamera()) {
            createPhotoFile();

            if (mPhotoFile == null) {
                Toast.makeText(mActivity, "出现错误", Toast.LENGTH_SHORT).show();
                return;
            }
            ACacheUtil.get(mActivity).put("capture", mPhotoFile.toString());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        通过FileProvider创建一个content类型的Uri
                Uri imageUri = FileProvider.getUriForFile(mActivity, mActivity.getPackageName(), mPhotoFile);
                //设置权限
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if( Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT){
                    List<ResolveInfo> resInfoList = mActivity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        mActivity.grantUriPermission(packageName, Uri.fromFile(new File(mPhotoFile.toString())), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }
//                ACacheUtil.get(mActivity).put("capture", Uri.fromFile(mPhotoFile).toString());
            }
//                    ContentValues contentValues = new ContentValues(1);
//                    contentValues.put(MediaStore.Images.Media.DATA, cameraFile.getAbsolutePath());
//                    Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            mActivity.startActivityForResult(intent, CAPTURE_PHOTO_REQUEST_CODE);


//            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            Uri fileUri = Uri.fromFile(mPhotoFile);
//            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//            mActivity.startActivityForResult(captureIntent, CAPTURE_PHOTO_REQUEST_CODE);

        } else {
            Toast.makeText(mActivity, "出现错误", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 创建照片文件
     */
    private void createPhotoFile() {
        if (mPhotoFolder != null) {
            if (!mPhotoFolder.exists()) {//检查保存图片的目录存不存在
                mPhotoFolder.mkdirs();
            }

            String fileName = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
            mPhotoFile = new File(mPhotoFolder, fileName + ".jpg");
            if (mPhotoFile.exists()) {
                mPhotoFile.delete();
            }
            try {
                mPhotoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                mPhotoFile = null;
            }
        } else {
            mPhotoFile = null;
            Toast.makeText(mActivity, "出现错误", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 判断系统中是否存在可以启动的相机应用
     *
     * @return 存在返回true，不存在返回false
     */
    public boolean hasCamera() {
        PackageManager packageManager = mActivity.getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 获取当前拍到的图片文件
     *
     * @return
     */
    public File getPhoto() {

        return mPhotoFile;
    }

    /**
     * 设置照片文件
     *
     * @param photoFile
     */
    public void setPhoto(File photoFile) {
        this.mPhotoFile = photoFile;
    }


    /**
     * 获取图片的旋转角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照指定的角度进行旋转
     *
     * @param bitmap 需要旋转的图片
     * @param degree 指定的旋转角度
     * @return 旋转后的图片
     */
    public Bitmap rotateBitmapByDegree(Bitmap bitmap, int degree) {
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return newBitmap;
    }

}
