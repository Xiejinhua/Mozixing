package com.gz.mozixing.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gz.mozixing.R;
import com.gz.mozixing.activity.BaseActivity;
import com.gz.mozixing.network.model.NetWorkCallback;
import com.gz.mozixing.network.model.UpdatePassWordModel;
import com.gz.mozixing.utils.RemindDialogUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改密码
 *
 * @author Alex
 * @since 2019/2/14
 */
public class ChangePwdActivity extends BaseActivity {
    @BindView(R.id.second_bar_back)
    RelativeLayout secondBarBack;
    @BindView(R.id.second_bar_title)
    TextView secondBarTitle;
    @BindView(R.id.user_mobile_no)
    EditText userMobileNo;
    @BindView(R.id.old_login_ed_pin)
    EditText oldLoginEdPin;
    @BindView(R.id.new_login_ed_pin)
    EditText newLoginEdPin;
    @BindView(R.id.ll_1)
    LinearLayout ll1;
    @BindView(R.id.change_password_bt)
    TextView changePasswordBt;
    @BindView(R.id.cancel_button)
    TextView cancelButton;
    @BindView(R.id.activity_login_page)
    RelativeLayout activityLoginPage;
    private Activity activity;

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, ChangePwdActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_change_pwd);
        ButterKnife.bind(this);
        setupToolbar(getString(R.string.change_password));

    }

    @OnClick(R.id.change_password_bt)
    void changePassword() {//修改
        String phone = userMobileNo.getText().toString().trim();
        String oldPassWord = oldLoginEdPin.getText().toString().trim();
        String newPassWord = newLoginEdPin.getText().toString().trim();

        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("passWord", oldPassWord);
        map.put("newPassWord", newPassWord);
        UpdatePassWordModel.getResponse(map, new NetWorkCallback<UpdatePassWordModel>() {
            @Override
            public void onResponse(UpdatePassWordModel response) {
                Toast.makeText(activity, "密码修改成功!", Toast.LENGTH_SHORT).show();
                finish();
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
}
