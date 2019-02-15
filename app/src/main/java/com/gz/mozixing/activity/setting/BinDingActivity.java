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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gz.mozixing.R;
import com.gz.mozixing.activity.BaseActivity;
import com.gz.mozixing.event.SwitchEvent;
import com.gz.mozixing.interfaces.TextOnClickListener;
import com.gz.mozixing.network.model.BinDingModel;
import com.gz.mozixing.network.model.NetWorkCallback;
import com.gz.mozixing.utils.ACacheUtil;
import com.gz.mozixing.utils.AppFolderUtil;
import com.gz.mozixing.utils.CapturePhotoHelper;
import com.gz.mozixing.utils.CircleImageView;
import com.gz.mozixing.utils.GetImagePath;
import com.gz.mozixing.utils.PickerUtil;
import com.gz.mozixing.utils.RemindDialogUtil;
import com.gz.mozixing.utils.WaitingDialogUtil;
import com.gz.mozixing.utils.zibinluban.Luban;
import com.gz.mozixing.utils.zibinluban.OnCompressListener;
import com.gz.mozixing.utils.zibinluban.UriToPathUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 绑定孩子页面
 *
 * @author Alex
 * @since 2019/2/13
 */
public class BinDingActivity extends BaseActivity {
    @BindView(R.id.second_bar_back)
    RelativeLayout secondBarBack;
    @BindView(R.id.second_bar_title)
    TextView secondBarTitle;
    @BindView(R.id.user_head)
    CircleImageView userHead;
    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.man_dot)
    TextView manDot;
    @BindView(R.id.man_ll)
    LinearLayout manLl;
    @BindView(R.id.women_dot)
    TextView womenDot;
    @BindView(R.id.woman_ll)
    LinearLayout womanLl;
    @BindView(R.id.phone_number)
    EditText phoneNumber;
    @BindView(R.id.weight_et)
    EditText weightEt;
    @BindView(R.id.height_et)
    EditText heightEt;
    @BindView(R.id.birthday_tv)
    TextView birthdayTv;
    @BindView(R.id.ll_1)
    LinearLayout ll1;
    @BindView(R.id.cancel_button)
    TextView cancelButton;
    @BindView(R.id.activity_login_page)
    RelativeLayout activityLoginPage;
    @BindView(R.id.binding_bt)
    TextView bindingBt;
    @BindView(R.id.layout_ll)
    ScrollView layoutLl;
    private Activity activity;
    private String head_bm = "";
    private String sex = "0";
    private String parentId;

    public static void actionStart(Activity activity, String parentId) {
        Intent intent = new Intent(activity, BinDingActivity.class);
        intent.putExtra("parentId", parentId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_binding_children);
        ButterKnife.bind(this);
        setupToolbar(getString(R.string.binding_child_information));
        parentId = getIntent().getStringExtra("parentId");
    }

    @OnClick({R.id.man_ll, R.id.woman_ll})
    void sex(View v) {//性别选择
        switch (v.getId()) {
            case R.id.man_ll://男
                if (sex.equalsIgnoreCase("1")) {
                    sex = "0";
                    manDot.setBackgroundResource(R.drawable.dot_selected);
                    womenDot.setBackgroundResource(R.drawable.dot_unselected);
                }
                break;
            case R.id.woman_ll://女
                if (sex.equalsIgnoreCase("0")) {
                    sex = "1";
                    manDot.setBackgroundResource(R.drawable.dot_unselected);
                    womenDot.setBackgroundResource(R.drawable.dot_selected);
                }
                break;
        }
    }

    @OnClick(R.id.user_head)
    void head() {//头像
        showButtomType();
    }


    @OnClick(R.id.binding_bt)
    void binding() {//绑定
        String name = userName.getText().toString().trim();
        String phone = phoneNumber.getText().toString().trim();
        String weight = weightEt.getText().toString().trim();
        String height = heightEt.getText().toString().trim();
        String birthday = birthdayTv.getText().toString().trim();

        if (head_bm == null || head_bm.equalsIgnoreCase("")) {
            Toast.makeText(this, getString(R.string.please_choose_your_avatar), Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.equalsIgnoreCase("")) {
            Toast.makeText(activity, getString(R.string.user_name_hint), Toast.LENGTH_SHORT).show();
            userName.requestFocus();
            return;
        }
        if (weight.equalsIgnoreCase("")) {
            Toast.makeText(activity, getString(R.string.weight_hint), Toast.LENGTH_SHORT).show();
            weightEt.requestFocus();
            return;
        }
        if (height.equalsIgnoreCase("")) {
            Toast.makeText(activity, getString(R.string.height_hint), Toast.LENGTH_SHORT).show();
            heightEt.requestFocus();
            return;
        }
        if (birthday.equalsIgnoreCase("")) {
            Toast.makeText(this, getString(R.string.birthday_hint), Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("sex", sex);
        map.put("birthday", birthday);
        map.put("weight", weight);
        map.put("height", height);
        map.put("photo", head_bm);
        map.put("parentId", parentId);
        map.put("phone", phone);
        map.put("uploadTime", "5");
        BinDingModel.getResponse(map, new NetWorkCallback<BinDingModel>() {
            @Override
            public void onResponse(BinDingModel response) {
                RemindDialogUtil.show(activity, getString(R.string.binding_success), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new SwitchEvent("true"));
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                RemindDialogUtil.showEasy(activity, message);
            }
        });

    }

    @OnClick(R.id.cancel_button)
    void cancel() {//取消
        finish();
    }

    @OnClick(R.id.birthday_tv)
    void birthday() {//出生日期
        hideInput();//关闭软键盘
        PickerUtil.getInstance().initTimePicker1(activity, new TextOnClickListener() {
            @Override
            public void onClickListener(String pin) {
                birthdayTv.setText(pin);
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
                        userHead.setImageBitmap(idcard_head_bm);
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
                        userHead.setImageBitmap(idcard_head_bm);
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
        final PopupWindow popupWindow = new PopupWindow(layout, ll1.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        ll1.getLocationOnScreen(location);

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
        Log.d("Base64", encodedImage);
        return encodedImage;
    }

}
