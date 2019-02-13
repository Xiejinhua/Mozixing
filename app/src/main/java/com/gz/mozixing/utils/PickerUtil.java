package com.gz.mozixing.utils;

import android.app.Activity;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.gz.mozixing.R;
import com.gz.mozixing.interfaces.TextOnClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期选择器
 *
 * @author Alex
 * @since 2019/2/13
 */
public class PickerUtil {
    private static PickerUtil pickerUtil;

    public static PickerUtil getInstance() {
        if (pickerUtil == null) {
            return new PickerUtil();
        } else {
            return pickerUtil;
        }
    }

    public TimePickerView pvTime;

    /**
     * 日期选择器
     */
    public void initTimePicker1(Activity activity, final TextOnClickListener listener) {//选择出生年月日
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
        pvTime = new TimePickerView.Builder(activity, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                listener.onClickListener(getTime(date));
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

}
