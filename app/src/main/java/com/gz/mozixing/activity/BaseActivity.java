package com.gz.mozixing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gz.mozixing.R;
import com.gz.mozixing.SysApplication;
import com.gz.mozixing.utils.ActivityUtil;
import com.gz.mozixing.utils.WaitingDialogUtil;
import com.gz.mozixing.utils.statusBar.MVPConfig;
import com.gz.mozixing.utils.statusBar.StatusBarUtil;


/**
 * @author Alex
 * @since 2019/2/3
 */
public class BaseActivity extends AppCompatActivity {

    private RelativeLayout mBack;
    private TextView mTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtil.init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SysApplication.getInstance().addActivity(this);
        ActivityUtil.init(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        WaitingDialogUtil.cancel();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityUtil.init(this);
    }

    //设置标题栏
    public void setupToolbar(String title) {
        if (!title.equalsIgnoreCase("")) {
            mTitle = (TextView) findViewById(R.id.second_bar_title);
            mTitle.setText(title);
        }
        mBack = (RelativeLayout) findViewById(R.id.second_bar_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setupToolbar(String title, final View.OnClickListener listener) {
        if (!title.equalsIgnoreCase("")) {
            mTitle = (TextView) findViewById(R.id.second_bar_title);
            mTitle.setText(title);
        }
        mBack = (RelativeLayout) findViewById(R.id.second_bar_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });

    }


    //刷新数据
    public void refreshData() {

    }

    /**
     * 隐藏键盘
     */
    public void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
