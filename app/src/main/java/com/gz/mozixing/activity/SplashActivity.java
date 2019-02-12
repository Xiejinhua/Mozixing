package com.gz.mozixing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.gz.mozixing.Constant;
import com.gz.mozixing.MainActivity;
import com.gz.mozixing.R;
import com.gz.mozixing.activity.login.LoginActivity;
import com.gz.mozixing.network.NetWorkUtil;
import com.gz.mozixing.utils.ACacheUtil;

/**
 * 欢迎页
 *
 * @author Alex
 * @since 2019/2/4
 */
public class SplashActivity extends AppCompatActivity {

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, SplashActivity.class);
        activity.startActivity(intent);
    }

    private static final int GO_HOME = 1000;
    private Activity activity;

    // 延迟时间
    private static final long SPLASH_DELAY_MILLIS = 2000;

    /**
     * Handler:跳转到不同界面
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    whereToGo();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        activity = this;

        mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);

    }

    //该跳转到哪里
    public void whereToGo() {
        if (NetWorkUtil.isNetworkAvailable(this)) {

            String token = ACacheUtil.get(activity).getAsString(Constant.authToken);
            if (token != null && !token.equalsIgnoreCase("")) {
                Constant.AuthTokenHolder = token;
                MainActivity.actionStart(activity);
            } else {
                LoginActivity.actionStart(activity);
            }
        } else {
            LoginActivity.actionStart(activity);
        }
    }
}