package com.gz.mozixing.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.gz.mozixing.R;
import com.gz.mozixing.activity.BaseActivity;
import com.gz.mozixing.interfaces.TextOnClickListener;
import com.gz.mozixing.network.model.NetWorkCallback;
import com.gz.mozixing.network.model.RegisterModel;
import com.gz.mozixing.utils.PickerUtil;
import com.gz.mozixing.utils.RemindDialogUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册页面
 *
 * @author Alex
 * @since 2019/2/12
 */
public class SignUpActivity extends BaseActivity {
    @BindView(R.id.second_bar_back)
    RelativeLayout secondBarBack;
    @BindView(R.id.second_bar_title)
    TextView secondBarTitle;
    @BindView(R.id.user_mobile_no)
    EditText userMobileNo;
    @BindView(R.id.login_ed_pin)
    EditText loginEdPin;
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
    @BindView(R.id.qq_number)
    EditText qqNumber;
    @BindView(R.id.birthday_tv)
    TextView birthdayTv;
    @BindView(R.id.ll_1)
    LinearLayout ll1;
    @BindView(R.id.login_bt)
    TextView loginBt;
    @BindView(R.id.cancel_button)
    TextView cancelButton;
    @BindView(R.id.activity_login_page)
    RelativeLayout activityLoginPage;
    private Activity activity;
    private TimePickerView pvTime;
    private String sex = "0";

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, SignUpActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        setupToolbar(getString(R.string.register), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishSignUp();
            }
        });

    }


    @OnClick({R.id.birthday_tv, R.id.login_bt, R.id.cancel_button, R.id.man_ll, R.id.woman_ll})
    void click(View v) {
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
            case R.id.birthday_tv://选择日期
                hideInput();//关闭软键盘
                PickerUtil.getInstance().initTimePicker1(activity, new TextOnClickListener() {
                    @Override
                    public void onClickListener(String pin) {
                        birthdayTv.setText(pin);
                    }
                });
                break;
            case R.id.login_bt://注册
                SignUp();
                break;
            case R.id.cancel_button://取消
                finishSignUp();
                break;
        }
    }

    /**
     * 注册
     */
    private void SignUp() {
        String phone = userMobileNo.getText().toString().trim();
        String passWord = loginEdPin.getText().toString().trim();
        String name = userName.getText().toString().trim();
        String qq = qqNumber.getText().toString().trim();
        String birthday = birthdayTv.getText().toString().trim();

        if (phone.equalsIgnoreCase("")) {
            Toast.makeText(activity, getString(R.string.phone_number_hint), Toast.LENGTH_SHORT).show();
            userMobileNo.requestFocus();
            return;
        }
        if (!isMobileNO(phone)) {
            Toast.makeText(activity, getString(R.string.phone_number_r_hint), Toast.LENGTH_SHORT).show();
            userMobileNo.requestFocus();
            return;
        }
        if (passWord.length() < 6) {
            Toast.makeText(activity, getString(R.string.passwd_error), Toast.LENGTH_SHORT).show();
            loginEdPin.requestFocus();
            return;
        }
        if (name.equalsIgnoreCase("")) {
            Toast.makeText(activity, getString(R.string.user_name_hint), Toast.LENGTH_SHORT).show();
            userName.requestFocus();
            return;
        }
        if (qq.equalsIgnoreCase("")) {
            Toast.makeText(activity, getString(R.string.qq_hint), Toast.LENGTH_SHORT).show();
            qqNumber.requestFocus();
            return;
        }
        if (birthday.equalsIgnoreCase("")) {
            Toast.makeText(activity, getString(R.string.birthday_hint), Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("phone", phone);
        map.put("passWord", passWord);
        map.put("sex", sex);
        map.put("birthday", birthday);
        map.put("qq", qq);

        RegisterModel.getResponse(map, new NetWorkCallback<RegisterModel>() {
            @Override
            public void onResponse(RegisterModel response) {
                RemindDialogUtil.show(activity, getString(R.string.login_hint), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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

    public static boolean isMobileNO(String mobileNums) {
        /**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         */
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }


    @Override
    public void onBackPressed() {
        finishSignUp();
    }

    /**
     * 退出
     */
    private void finishSignUp() {
        RemindDialogUtil.showYesNo(activity, getString(R.string.yes_no_logut_register), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
