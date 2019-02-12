package com.gz.mozixing.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.gz.mozixing.Constant;
import com.gz.mozixing.R;
import com.wang.avi.AVLoadingIndicatorView;


/**
 * 等待框工具类
 *
 * @author Alex
 * @since 19/1/30
 */
public class WaitingDialogUtil {


    static DialogUtil dialog;

    private static DialogUtil getInstance(Activity activity) {


        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.loading_view, null);
//        SharedPreferencesUtils sharedPreferencesUtils = SharedPreferencesUtils.getInstance(activity);
//        String color = Constant.userTheme;
//        int colorCode = activity.getResources().getIdentifier(color, "color", activity.getPackageName());
//        AVLoadingIndicatorView indicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
//        indicatorView.getIndicator().setColor(ContextCompat.getColor(activity, colorCode));
        dialog = new DialogUtil(activity, view, false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {


                    //返回true禁止返回键关掉dialog
                    return true;
                } else {
                    return false;
                }
            }

        });
        return dialog;
    }

    //加载框是否在打开
    private static boolean isShow = false;

    public static void show(Activity activity) {
        if (!isShow && activity != null) {
            isShow = true;
            getInstance(activity).show();
        }
    }

    public static void cancel() {
        isShow = false;
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }


    public static void showNoActionBar(Activity activity) {

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.loading_view, null);
        SharedPreferencesUtils sharedPreferencesUtils = SharedPreferencesUtils.getInstance(activity);
        String color = "ThemeBlue";
        int colorCode = activity.getResources().getIdentifier(color, "color", activity.getPackageName());
        AVLoadingIndicatorView indicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        indicatorView.getIndicator().setColor(ContextCompat.getColor(activity, colorCode));

        if (!isShow && activity != null) {
            dialog = new DialogUtil(activity, view);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {


                        //返回true禁止返回键关掉dialog
                        return true;
                    } else {
                        return false;
                    }
                }

            });
            dialog.show();
        }

    }


}
