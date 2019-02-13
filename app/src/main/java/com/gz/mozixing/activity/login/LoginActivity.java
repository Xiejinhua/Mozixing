package com.gz.mozixing.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gz.mozixing.Constant;
import com.gz.mozixing.MainActivity;
import com.gz.mozixing.R;
import com.gz.mozixing.SysApplication;
import com.gz.mozixing.activity.BaseActivity;
import com.gz.mozixing.network.model.LoginModel;
import com.gz.mozixing.network.model.NetWorkCallback;
import com.gz.mozixing.utils.ACacheUtil;
import com.gz.mozixing.utils.RemindDialogUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登陆页面
 *
 * @author Alex
 * @since 2019/2/4
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_ed_mobile_no)
    EditText loginEdMobileNo;
    @BindView(R.id.login_ed_pin)
    EditText loginEdPin;
    @BindView(R.id.ll_1)
    LinearLayout ll1;
    @BindView(R.id.login_signup_btn)
    TextView loginSignupBtn;
    @BindView(R.id.login_bt)
    TextView loginBt;
    @BindView(R.id.forgot_pin_btn)
    TextView forgotPinBtn;
    @BindView(R.id.ll_2)
    LinearLayout ll2;
    @BindView(R.id.activity_login_page)
    RelativeLayout activityLoginPage;
    private Activity activity;

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_login_page);
        ButterKnife.bind(this);
        loginEdPin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    login(loginEdMobileNo.getText().toString().trim(), loginEdPin.getText().toString().trim());
                }
                return false;
            }
        });
    }

    @OnClick(R.id.login_signup_btn)
    void sign() {//注册
        SignUpActivity.actionStart(activity);
    }

    @OnClick(R.id.login_bt)
    void loginBt() {//登录
        login(loginEdMobileNo.getText().toString().trim(), loginEdPin.getText().toString().trim());
    }

    @OnClick(R.id.forgot_pin_btn)
    void forgot() {//忘记密码

    }

    private void login(String name, String pwd) {
        if (name.equalsIgnoreCase("")) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
            loginEdMobileNo.requestFocus();
            return;
        }
        if (pwd.length() < 6) {
            Toast.makeText(this, "格式无效,密码大于6位", Toast.LENGTH_SHORT).show();
            loginEdPin.requestFocus();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("phone", name);
        map.put("passWord", pwd);
        LoginModel.getResponse(map, new NetWorkCallback<LoginModel>() {
            @Override
            public void onResponse(LoginModel response) {
                if (response.getData() != null && response.getData().getToken() != null && response.getData().getResultX() != null) {
                    Constant.AuthTokenHolder = response.getData().getToken();
                    ACacheUtil.get(activity).put(Constant.authToken, response.getData().getToken());
                    ACacheUtil.get(activity).put(Constant.userLogin, new Gson().toJson(response));
                    MainActivity.actionStart(activity);
                } else {
                    RemindDialogUtil.showEasy(activity, response.getMsg());
                }
            }

            @Override
            public void onFailure(String message) {
                RemindDialogUtil.showEasy(activity, message);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
