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
import android.support.v7.widget.LinearLayoutManager;
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
import com.gz.mozixing.adapter.MinuteAdapter;
import com.gz.mozixing.event.SwitchEvent;
import com.gz.mozixing.event.UpdateEvent;
import com.gz.mozixing.interfaces.MyOnClickListener;
import com.gz.mozixing.interfaces.TextOnClickListener;
import com.gz.mozixing.network.model.MinuteModel;
import com.gz.mozixing.network.model.NetWorkCallback;
import com.gz.mozixing.network.model.UpdateCancelFollowModel;
import com.gz.mozixing.network.model.UpdateChildrenNameModel;
import com.gz.mozixing.network.model.UpdateChildrenPhotoModel;
import com.gz.mozixing.utils.ACacheUtil;
import com.gz.mozixing.utils.AppFolderUtil;
import com.gz.mozixing.utils.CapturePhotoHelper;
import com.gz.mozixing.utils.CircleImageView;
import com.gz.mozixing.utils.CustomGridLayoutRecyclerView;
import com.gz.mozixing.utils.GetImagePath;
import com.gz.mozixing.utils.RemindDialogUtil;
import com.gz.mozixing.utils.WaitingDialogUtil;
import com.gz.mozixing.utils.zibinluban.Luban;
import com.gz.mozixing.utils.zibinluban.OnCompressListener;
import com.gz.mozixing.utils.zibinluban.UriToPathUtil;
import com.warkiz.widget.IndicatorSeekBar;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.recyclerview)
    CustomGridLayoutRecyclerView recyclerview;
    private Activity activity;
    private boolean isMessage;
    private String head_bm;
    private String[] MinuteList = {"2", "3", "5", "10", "15"};
    private ArrayList<MinuteModel> list = new ArrayList<>();
    private MinuteAdapter mMinuteAdapter;
    private String uploadTime = "5";
    private String childrenId, parentId;

    public static void actionStart(Activity activity, String children_id, String parentId) {
        Intent intent = new Intent(activity, SettingActivity.class);
        intent.putExtra("children_id", children_id);
        intent.putExtra("parentId", parentId);
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
        setupToolbar(getString(R.string.setting));
        childrenId = getIntent().getStringExtra("children_id");
        parentId = getIntent().getStringExtra("parentId");

        setView();
        setSeekbar();
        refreshData();
    }

    private void setView() {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        // 解决滑动不流畅
        recyclerview.setNestedScrollingEnabled(false);
        mMinuteAdapter = new MinuteAdapter(activity, list, new MyOnClickListener() {
            @Override
            public void onClickListener(int position) {
                for (MinuteModel minuteModel : list) {
                    minuteModel.setSelect(false);
                }
                list.get(position).setSelect(true);
                uploadTime = list.get(position).getMinute();
                mMinuteAdapter.notifyDataSetChanged();
            }
        });

        recyclerview.setAdapter(mMinuteAdapter);
    }

    @Override
    public void refreshData() {
        super.refreshData();
        for (int i = 0; i < 5; i++) {
            MinuteModel minuteModel = new MinuteModel();
            minuteModel.setMinute(MinuteList[i]);
            minuteModel.setSelect(false);
            list.add(minuteModel);
        }
        for (MinuteModel minuteModel : list) {
            if (uploadTime.equalsIgnoreCase(minuteModel.getMinute())) {
                minuteModel.setSelect(true);
            }
        }
        mMinuteAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.user_head, R.id.user_name, R.id.select_bt, R.id.follow_bt})
    void click(View v) {
        switch (v.getId()) {
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
                RemindDialogUtil.showYesNo(activity, getString(R.string.whether_to_cancel_the_concern), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateCancelFollow();
                    }
                });
                break;
            case R.id.user_head://头像修改
                showButtomType();
                break;
            case R.id.user_name://名字修改
                String name = userName.getText().toString().trim();
                RemindDialogUtil.getNameShow(activity, name, new TextOnClickListener() {
                    @Override
                    public void onClickListener(String pin) {
                        if (!pin.equalsIgnoreCase("")) {
                            userName.setText(pin);
                            updateName(pin);
                        }
                    }
                });
                break;
        }
    }

    /**
     * 修改名字
     */
    private void updateName(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("childrenId", childrenId);
        map.put("parentId", parentId);
        map.put("name", name);
        UpdateChildrenNameModel.getResponse(map, new NetWorkCallback<UpdateChildrenNameModel>() {
            @Override
            public void onResponse(UpdateChildrenNameModel response) {
                EventBus.getDefault().post(new UpdateEvent("true"));
                Toast.makeText(activity, getString(R.string.name_change_success), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                RemindDialogUtil.showEasy(activity, message);
            }
        });
    }

    /**
     * 取消关注
     */
    private void updateCancelFollow() {
        Map<String, String> map = new HashMap<>();
        map.put("childrenId", childrenId);
        map.put("parentId", parentId);
        UpdateCancelFollowModel.getResponse(map, new NetWorkCallback<UpdateCancelFollowModel>() {
            @Override
            public void onResponse(UpdateCancelFollowModel response) {
                EventBus.getDefault().post(new SwitchEvent("true"));
                Toast.makeText(activity, getString(R.string.successful_removal_of_attention), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(String message) {
                RemindDialogUtil.showEasy(activity, message);
            }
        });
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
                    updatePhoto();
                }
            });

        }
    }

    /**
     * 修改头像
     */
    private void updatePhoto() {
        Map<String, String> map = new HashMap<>();
        map.put("childrenId", childrenId);
        map.put("parentId", parentId);
        map.put("photo", head_bm);
        UpdateChildrenPhotoModel.getResponse(map, new NetWorkCallback<UpdateChildrenPhotoModel>() {
            @Override
            public void onResponse(UpdateChildrenPhotoModel response) {
                EventBus.getDefault().post(new UpdateEvent("true"));
                Toast.makeText(activity, getString(R.string.successful_modification_of_the_avatar), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                RemindDialogUtil.showEasy(activity, message);
            }
        });
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
