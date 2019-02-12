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
import com.gz.mozixing.network.model.NetWorkCallback;
import com.gz.mozixing.network.model.RegisterModel;
import com.gz.mozixing.utils.RemindDialogUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        setupToolbar("注册", new View.OnClickListener() {
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
                initTimePicker1();
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
            Toast.makeText(this, "请填写手机号码", Toast.LENGTH_SHORT).show();
            userMobileNo.requestFocus();
            return;
        }
        if (!isMobileNO(phone)) {
            Toast.makeText(this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
            userMobileNo.requestFocus();
            return;
        }
        if (passWord.length() < 6) {
            Toast.makeText(this, "格式无效,密码大于6位", Toast.LENGTH_SHORT).show();
            loginEdPin.requestFocus();
            return;
        }
        if (name.equalsIgnoreCase("")) {
            Toast.makeText(this, "请填写姓名", Toast.LENGTH_SHORT).show();
            userName.requestFocus();
            return;
        }
        if (qq.equalsIgnoreCase("")) {
            Toast.makeText(this, "请填写QQ号码", Toast.LENGTH_SHORT).show();
            qqNumber.requestFocus();
            return;
        }
        if (birthday.equalsIgnoreCase("")) {
            Toast.makeText(this, "请选择出生日期", Toast.LENGTH_SHORT).show();
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
                RemindDialogUtil.show(activity, "注册成功吗，你可以登陆了。", new View.OnClickListener() {
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

    /**
     * 日期选择器
     */
    private void initTimePicker1() {//选择出生年月日
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        SimpleDateFormat formatter_year = new SimpleDateFormat("yyyy ");
        String year_str = formatter_year.format(curDate);
        int year_int = (int) Double.parseDouble(year_str);


        SimpleDateFormat formatter_mouth = new SimpleDateFormat("MM ");
        String mouth_str = formatter_mouth.format(curDate);
        int mouth_int = (int) Double.parseDouble(mouth_str);

        SimpleDateFormat formatter_day = new SimpleDateFormat("dd ");
        String day_str = formatter_day.format(curDate);
        int day_int = (int) Double.parseDouble(day_str);


        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(year_int, mouth_int - 1, day_int);

        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null

                birthdayTv.setText(getTime(date));
            }
        }).setType(new boolean[]{true, true, true, false, false, false}) //年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("年", "月", "日", "", "", "")//默认设置为年月日时分秒
                .isCenterLabel(false)
                .setDividerColor(activity.getResources().getColor(R.color.color_app_theme))
                .setTextColorCenter(activity.getResources().getColor(R.color.color_app_theme))//设置选中项的颜色
                .setTextColorOut(activity.getResources().getColor(R.color.color_999999))//设置没有被选中项的颜色
                .setSubmitColor(activity.getResources().getColor(R.color.color_app_theme))//确定按钮颜色
                .setCancelColor(activity.getResources().getColor(R.color.color_app_theme))//取消按钮颜色
                .setSubCalSize(14)
                .setContentSize(14)
                .setContentSize(19)
                .setDate(selectedDate)
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(-10, 0, 10, 0, 0, 0)//设置X轴倾斜角度[ -90 , 90°]
                .setRangDate(startDate, endDate)
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
        pvTime.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


    @Override
    public void onBackPressed() {
        finishSignUp();
    }

    /**
     * 退出
     */
    private void finishSignUp() {
        RemindDialogUtil.showYesNo(activity, "是否退出注册？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
