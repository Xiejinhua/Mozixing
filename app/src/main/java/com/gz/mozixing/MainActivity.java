package com.gz.mozixing;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gz.mozixing.activity.setting.BinDingActivity;
import com.gz.mozixing.event.SwitchEvent;
import com.gz.mozixing.fragment.ChildrenFragment;
import com.gz.mozixing.network.model.ChildrenModel;
import com.gz.mozixing.network.model.LoginModel;
import com.gz.mozixing.network.model.NetWorkCallback;
import com.gz.mozixing.utils.ACacheUtil;
import com.gz.mozixing.utils.ActivityUtil;
import com.gz.mozixing.utils.PermissionUtils;
import com.gz.mozixing.utils.RemindDialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.message)
    ImageView message;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.none_message)
    ImageView noneMessage;

    private Activity activity;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private MyPagerAdapter pagerAdapter;
    private ArrayList<ChildrenModel.DataBean.ResultBean> childrenList = new ArrayList<>();
    private LoginModel loginModel;
    private String parentId;

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ActivityUtil.init(this);
        requestPermission();
        message.setVisibility(View.VISIBLE);
        EventBus.getDefault().register(this);//注册eventBus
        refreshData();
    }

    @OnClick(R.id.message)
    void addChildren() {
        BinDingActivity.actionStart(activity, parentId != null ? parentId : "");
    }


    /**
     * 获取数据
     */
    public void refreshData() {
        loginModel = new Gson().fromJson(ACacheUtil.get(activity).getAsString(Constant.userLogin), LoginModel.class);
        if (loginModel != null) {
            parentId = loginModel.getData().getResultX().getParentId();
            Map<String, String> map = new HashMap<>();
            map.put("parentId", parentId);
            ChildrenModel.getResponse(map, new NetWorkCallback<ChildrenModel>() {
                @Override
                public void onResponse(ChildrenModel response) {
                    if (response.getData() != null && response.getData().getResultX() != null && response.getData().getResultX().size() > 0) {
                        vp.setVisibility(View.VISIBLE);
                        noneMessage.setVisibility(View.GONE);
                        childrenList.addAll(response.getData().getResultX());
                        setData();
                    } else {
                        vp.setVisibility(View.GONE);
                        noneMessage.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(String message) {
                    vp.setVisibility(View.GONE);
                    noneMessage.setVisibility(View.VISIBLE);
                    RemindDialogUtil.showEasy(activity, message);
                }
            });
        } else {
            vp.setVisibility(View.GONE);
            noneMessage.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 配置绑定  显示
     */
    private void setData() {
        mTitles.clear();
        mFragments.clear();
        for (int i = 0; i < childrenList.size(); i++) {
            mTitles.add(childrenList.get(i).getChildrenId());
            mFragments.add(ChildrenFragment.getInstance(childrenList.get(i).getChildrenId(), parentId));
        }
        if (null == pagerAdapter) {
            pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments);
            vp.setCurrentItem(0);
            vp.setOffscreenPageLimit(mTitles.size());
            vp.addOnPageChangeListener(this);
            vp.setAdapter(pagerAdapter);

        } else {
            pagerAdapter.setFragments(mFragments);
//            pagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        vp.setCurrentItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class MyPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Fragment> mFragments;
        private FragmentManager fm;

        public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.mFragments = fragments;
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        public void setFragments(ArrayList<Fragment> fragments) {
            if (this.mFragments != null) {
                FragmentTransaction ft = fm.beginTransaction();
                for (Fragment f : this.mFragments) {
                    ft.remove(f);
                }
                ft.commit();
                ft = null;
                fm.executePendingTransactions();
            }
            this.mFragments = fragments;
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    // 多个权限请求Code
    private final int REQUEST_CODE_RECORD_AUDIO = 1;
    //获取androidId权限
    private static String[] PERMISSION_RECORD_AUDIO = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    // 普通申请一个权限
    private void requestPermission() {
        PermissionUtils.checkAndRequestMorePermissions(activity, PERMISSION_RECORD_AUDIO, REQUEST_CODE_RECORD_AUDIO,
                new PermissionUtils.PermissionRequestSuccessCallBack() {
                    @Override
                    public void onHasPermission() {
                        // 权限已被授予
                        Log.d("权限", "成功获取权限");
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case REQUEST_CODE_RECORD_AUDIO:
                if (PermissionUtils.isPermissionRequestSuccess(grantResults)) {
                    // 权限申请成功
                    Log.d("权限", "成功获取权限");
                } else {
                    Log.d("权限", "获取权限失败");

                }
                break;
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            SysApplication.getInstance().exit();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "再次点击关闭应用", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//注册eventBus
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(SwitchEvent event) {
        if (event.getMsg().equalsIgnoreCase("true")) {
            refreshData();
        }
    }

}
