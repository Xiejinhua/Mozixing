package com.gz.mozixing.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gz.mozixing.R;
import com.gz.mozixing.activity.BaseActivity;
import com.gz.mozixing.utils.ACacheUtil;
import com.gz.mozixing.utils.AppFolderUtil;
import com.gz.mozixing.utils.CapturePhotoHelper;
import com.gz.mozixing.utils.CircleImageView;
import com.gz.mozixing.utils.GetImagePath;
import com.gz.mozixing.utils.RemindDialogUtil;
import com.gz.mozixing.utils.WaitingDialogUtil;
import com.gz.mozixing.utils.zibinluban.Luban;
import com.gz.mozixing.utils.zibinluban.OnCompressListener;
import com.gz.mozixing.utils.zibinluban.UriToPathUtil;
import com.warkiz.widget.IndicatorSeekBar;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置
 *
 * @author Alex
 * @since 2019/2/3
 */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.message)
    ImageView message;
    @BindView(R.id.second_bar_back)
    RelativeLayout secondBarBack;
    @BindView(R.id.second_bar_title)
    TextView secondBarTitle;
    @BindView(R.id.two_tv)
    TextView twoTv;
    @BindView(R.id.three_tv)
    TextView threeTv;
    @BindView(R.id.five_tv)
    TextView fiveTv;
    @BindView(R.id.ten_tv)
    TextView tenTv;
    @BindView(R.id.fifteen_tv)
    TextView fifteenTv;
    @BindView(R.id.minute_tv)
    TextView minuteTv;
    @BindView(R.id.tips_tv)
    TextView tipsTv;
    @BindView(R.id.select_iv)
    ImageView selectIv;
    @BindView(R.id.select_tv)
    TextView selectTv;
    @BindView(R.id.select_bt)
    LinearLayout selectBt;
    @BindView(R.id.follow_bt)
    TextView followBt;
    @BindView(R.id.seekbar)
    IndicatorSeekBar seekbar;
    @BindView(R.id.user_head)
    CircleImageView userHead;
    @BindView(R.id.layout_ll)
    LinearLayout layoutLl;
    private Activity activity;
    private boolean isMessage;
    private String head_bm;

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setupToolbar("设置");

        setSeekbar();
    }

    @Override
    public void refreshData() {
        super.refreshData();

    }

    @OnClick({R.id.user_head, R.id.two_tv, R.id.three_tv, R.id.five_tv, R.id.ten_tv, R.id.fifteen_tv, R.id.select_bt, R.id.follow_bt})
    void click(View v) {
        switch (v.getId()) {
            case R.id.two_tv://2
                setMinute(2);
                break;
            case R.id.three_tv://3
                setMinute(3);
                break;
            case R.id.five_tv://5
                setMinute(5);
                break;
            case R.id.ten_tv://10
                setMinute(10);
                break;
            case R.id.fifteen_tv://15
                setMinute(15);
                break;
            case R.id.select_bt://短信
                if (isMessage) {
                    isMessage = false;
                    selectIv.setImageResource(R.mipmap.ic_select_un);
                    selectTv.setTextColor(getResources().getColor(R.color.color_666666));
                } else {
                    isMessage = true;
                    selectIv.setImageResource(R.mipmap.ic_select);
                    selectTv.setTextColor(getResources().getColor(R.color.color_1A8EFE));
                }
                break;
            case R.id.follow_bt://关注
                RemindDialogUtil.showYesNo(activity, "是否取消关注", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(activity, "成功取消关注", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.user_head://15
                showButtomType();
                break;
        }
    }

    /**
     * 设置上传时间UI
     */
    private void setMinute(int minute) {
        twoTv.setTextColor(getResources().getColor(R.color.color_666666));
        twoTv.setBackgroundResource(R.drawable.five_white_shape);
        threeTv.setTextColor(getResources().getColor(R.color.color_666666));
        threeTv.setBackgroundResource(R.drawable.five_white_shape);
        fiveTv.setTextColor(getResources().getColor(R.color.color_666666));
        fiveTv.setBackgroundResource(R.drawable.five_white_shape);
        tenTv.setTextColor(getResources().getColor(R.color.color_666666));
        tenTv.setBackgroundResource(R.drawable.five_white_shape);
        fifteenTv.setTextColor(getResources().getColor(R.color.color_666666));
        fifteenTv.setBackgroundResource(R.drawable.five_white_shape);
        switch (minute) {
            case 2:
                twoTv.setTextColor(getResources().getColor(R.color.colorWhite));
                twoTv.setBackgroundResource(R.drawable.five_blue_shape);
                break;
            case 3:
                threeTv.setTextColor(getResources().getColor(R.color.colorWhite));
                threeTv.setBackgroundResource(R.drawable.five_blue_shape);
                break;
            case 5:
                fiveTv.setTextColor(getResources().getColor(R.color.colorWhite));
                fiveTv.setBackgroundResource(R.drawable.five_blue_shape);
                break;
            case 10:
                tenTv.setTextColor(getResources().getColor(R.color.colorWhite));
                tenTv.setBackgroundResource(R.drawable.five_blue_shape);
                break;
            case 15:
                fifteenTv.setTextColor(getResources().getColor(R.color.colorWhite));
                fifteenTv.setBackgroundResource(R.drawable.five_blue_shape);
                break;

        }
    }


    /**
     * 设置滑动条
     */
    private void setSeekbar() {
        seekbar.setProgress((float) 37.8);
        seekbar.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                //选择中
                Log.d("onProgressChanged>>>", progressFloat + "");
            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String tickBelowText, boolean fromUserTouch) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                //选择完成
                Log.d("onStopTrackingTouch>>>", seekBar.getProgressFloat() + "");
            }
        });
    }


    //Image request code
    private final int PICK_IMAGE_REQUEST_HEAD = 505;
    //Image request code
    private final int CROP_REQUEST_HEAD = 506;
    private final static int REQUEST_CODE_CAMERA_HEAD = 3;
    CapturePhotoHelper mCapturePhotoHelper;
    private Intent intent;

    String url;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA_HEAD) {
            if (resultCode == RESULT_OK) {
                url = ACacheUtil.get(activity).getAsString("capture");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri inputUri = FileProvider.getUriForFile(activity, activity.getPackageName(), new File(url));//通过FileProvider创建一个content类型的Uri
                    CropImg(inputUri, CROP_REQUEST_HEAD);
                } else {
                    getIdcardHeadBm(new File(url));
                }

            }
        }
        if (requestCode == CROP_REQUEST_HEAD && resultCode == RESULT_OK) {
            String url = ACacheUtil.get(activity).getAsString("capture");
            if (data != null) {
                if (data.getData() != null) {
                    String getPaths = GetImagePath.getPath(activity, data.getData());//设置输入类型/
                    Log.d("GetImagePath", getPaths);
                    getIdcardHeadBm(new File(getPaths));
                } else {
                    getIdcardHeadBm(new File(url));
                }
            } else {
                getIdcardHeadBm(new File(url));
            }
        }
        //头像相册选图
        if (requestCode == PICK_IMAGE_REQUEST_HEAD && resultCode == RESULT_OK && data != null && data.getData() != null) {
            getIdcardHeadBm(UriToPathUtil.getRealFilePath(activity, data.getData()));
        }
    }

    private void CropImg(Uri uri, int result) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri outPutUri = Uri.fromFile(mCapturePhotoHelper.getPhoto());
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        } else {
            intent.setDataAndType(uri, "image/*");
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, result);
    }

    private Bitmap idcard_head_bm;

    //获取头像的Bitmap
    public void getIdcardHeadBm(File photoFile) {
        WaitingDialogUtil.show(activity);
        Luban.with(activity)
                .load(photoFile)                                   // 传人要压缩的图片列表
                .ignoreBy(500)                                  // 忽略不压缩图片的大小
                .setTargetDir(AppFolderUtil.getAppFolder().getAbsolutePath())                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        Log.d("压缩", "开始压缩");
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
//                        int a = mCapturePhotoHelper.getBitmapDegree(file.getAbsolutePath());
                        idcard_head_bm = BitmapFactory.decodeFile(file.getAbsolutePath());
                        head_bm = getStringImage(idcard_head_bm);
                        new Thread(new IdcardHeadRunnable()).start();
//                        LogUtil.d("数据", "" + a);
                        Log.d("压缩", "完成压缩");
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                    }

                    @Override
                    public void onError(Throwable e) {
                        WaitingDialogUtil.cancel();
                        Log.d("压缩", "压缩出现问题");
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();

    }

    //获取头像的Bitmap
    public void getIdcardHeadBm(String path) {
        WaitingDialogUtil.show(activity);
        Luban.with(activity)
                .load(path)                                   // 传人要压缩的图片列表
                .ignoreBy(500)                                  // 忽略不压缩图片的大小
                .setTargetDir(AppFolderUtil.getAppFolder().getAbsolutePath())                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        Log.d("压缩", "开始压缩");
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
//                        int a = mCapturePhotoHelper.getBitmapDegree(file.getAbsolutePath());
                        idcard_head_bm = BitmapFactory.decodeFile(file.getAbsolutePath());
                        head_bm = getStringImage(idcard_head_bm);
                        new Thread(new IdcardHeadRunnable()).start();
//                        LogUtil.d("数据", "" + a);
                        Log.d("压缩", "完成压缩");
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                    }

                    @Override
                    public void onError(Throwable e) {
                        WaitingDialogUtil.cancel();
                        Log.d("压缩", "压缩出现问题");
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();

    }

    //处理图片传64和md4
    public class IdcardHeadRunnable implements Runnable {
        private final static String TAG = "My Runnable ===> ";

        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    userHead.setImageBitmap(idcard_head_bm);
                    WaitingDialogUtil.cancel();
                }
            });

        }
    }

    /**
     * 弹出选择框
     */

    private void showButtomType() {
        View layout = LinearLayout.inflate(activity, R.layout.item_image_head, null);
        TextView camera_text = (TextView) layout.findViewById(R.id.camera_text);
        TextView photos_text = (TextView) layout.findViewById(R.id.photos_text);
        TextView cancel_text = (TextView) layout.findViewById(R.id.cancel_text);

        layout.setBackground(getResources().getDrawable(R.drawable.add_lable_dialog_shape));
        backgroundAlpha(0.4f);
        final PopupWindow popupWindow = new PopupWindow(layout, layoutLl.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        layoutLl.getLocationOnScreen(location);

        //获取自身的长宽高
        layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        //在屏幕中间显示
        popupWindow.showAtLocation(layoutLl, Gravity.BOTTOM, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        //拍照
        camera_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                mCapturePhotoHelper = new CapturePhotoHelper(activity, REQUEST_CODE_CAMERA_HEAD);
                mCapturePhotoHelper.capture();
            }
        });
        //相册
        photos_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_HEAD);

            }
        });
        //取消
        cancel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    // 设置屏幕透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0~1.0
        activity.getWindow().setAttributes(lp);
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


}
