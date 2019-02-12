package com.gz.mozixing;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
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

import com.gz.mozixing.fragment.ChildrenFragment;
import com.gz.mozixing.network.model.ChildrenModel;
import com.gz.mozixing.utils.PermissionUtils;
import com.gz.mozixing.utils.statusBar.MVPConfig;
import com.gz.mozixing.utils.statusBar.StatusBarUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.message)
    ImageView message;
    @BindView(R.id.vp)
    ViewPager vp;

    private Activity activity;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private MyPagerAdapter pagerAdapter;
    private ArrayList<ChildrenModel.DataBean.ResultBean> childrenList = new ArrayList<>();

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
        requestPermission();
        message.setVisibility(View.VISIBLE);
        refreshData();
    }

    @OnClick(R.id.message)
    void addChildren() {
        Toast.makeText(this, "addChildren", Toast.LENGTH_SHORT).show();
    }


    /**
     * 获取数据
     */
    public void refreshData() {
        for (int i = 0; i < 5; i++) {
            ChildrenModel.DataBean.ResultBean model = new ChildrenModel.DataBean.ResultBean();
            model.setChildrenId(i + "");
            childrenList.add(model);
        }
        setData();
    }

    /**
     * 配置绑定  显示
     */
    private void setData() {
        mTitles.clear();
        mFragments.clear();
        for (int i = 0; i < childrenList.size(); i++) {
            mTitles.add(childrenList.get(i).getChildrenId());
            mFragments.add(ChildrenFragment.getInstance(childrenList.get(i).getChildrenId()));
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


}
