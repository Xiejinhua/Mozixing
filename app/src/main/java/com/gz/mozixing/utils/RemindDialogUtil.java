package com.gz.mozixing.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.gz.mozixing.R;
import com.gz.mozixing.interfaces.TextOnClickListener;

import java.util.regex.Pattern;


/**
 * 提醒框
 *
 * @author Alex
 * @since 2019/2/3
 */
public class RemindDialogUtil {
    private static DialogUtil dialog;

    //这个是需要监听点击确定后处理点击事件的
    public static void show(Activity activity, String connent, final View.OnClickListener listener) {
        if (activity != null) {
            final RelativeLayout ok;

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_remind_view, null);
            ok = (RelativeLayout) view.findViewById(R.id.ok_button);
            TextView textView = (TextView) view.findViewById(R.id.connent);
            textView.setText(connent);
            if (!isShow) {
                if (dialog != null) {
                    cancel();
                }
                isShow = true;
                dialog = new DialogUtil(activity, view, true);
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(final DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {

                            isShow = false;
                            listener.onClick(ok);
                            //返回true禁止返回键关掉dialog
                            return false;
                        } else {
                            isShow = false;
                            return false;
                        }
                    }

                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isShow = false;
                        listener.onClick(v);
                        RemindDialogUtil.cancel();

                    }
                });
                dialog.show();
            }
        }
    }


    public static void cancel() {
        isShow = false;
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    //是否有
    private static boolean isShow = false;

    //这里为了适配大多数点击是为了关闭这个dialog而已的操作
    public static void showEasy(Activity activity, String connent) {
        if (activity != null) {
            if (!isShow) {
                if (dialog != null) {
                    cancel();
                }
                isShow = true;
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dialog_remind_view, null);
                dialog = new DialogUtil(activity, view, true);
                RelativeLayout ok = (RelativeLayout) view.findViewById(R.id.ok_button);
                TextView textView = (TextView) view.findViewById(R.id.connent);
                textView.setText(connent);
                dialog.show();
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {

                            isShow = false;
                            //返回true禁止返回键关掉dialog
                            return false;
                        } else {
                            isShow = false;
                            return false;
                        }
                    }

                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        RemindDialogUtil.cancel();
                    }
                });

            }
        }
    }

    //这个是需要监听点击确定后处理点击事件的
    public static void showYesNo(Activity activity, String connent, final View.OnClickListener listener) {
        if (activity != null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_del_view, null);
            TextView textView = (TextView) view.findViewById(R.id.connent);
            final TextView cancel_button = (TextView) view.findViewById(R.id.cancel_button);
            final TextView ok_button = (TextView) view.findViewById(R.id.ok_button);
            textView.setText(connent);
            if (!isShow) {
                isShow = true;
                dialog = new DialogUtil(activity, view, true);
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(final DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {

                            isShow = false;
                            //返回true禁止返回键关掉dialog
                            return false;
                        } else {
                            isShow = false;
                            return false;
                        }
                    }

                });
                cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isShow = false;
                        RemindDialogUtil.cancel();
                    }
                });
                ok_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isShow = false;
                        listener.onClick(v);
                        RemindDialogUtil.cancel();
                    }
                });
                dialog.show();
            }
        }
    }


    //输入名字
    public static void getNameShow(Activity activity, String name, final TextOnClickListener listener) {
        if (activity != null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_name_view, null);
            TextView cancel_button = (TextView) view.findViewById(R.id.cancel_button);
            final TextView ok_button = (TextView) view.findViewById(R.id.ok_button);
            final EditText pin_text = (EditText) view.findViewById(R.id.pin_text);
            pin_text.setText(name);
            if (!isShow) {
                isShow = true;
                dialog = new DialogUtil(activity, view, true);
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(final DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {

                            isShow = false;
                            //返回true禁止返回键关掉dialog
                            return false;
                        } else {
                            isShow = false;
                            return false;
                        }
                    }

                });
                cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isShow = false;
                        RemindDialogUtil.cancel();
                    }
                });
                ok_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isShow = false;
                        if (pin_text.getText().toString().trim().length() > 0) {
                            listener.onClickListener(pin_text.getText().toString().trim());
                            RemindDialogUtil.cancel();
                        } else {
                            listener.onClickListener("");
                        }
                    }
                });
                dialog.show();
            }
        }
    }

    /*
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
//        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        Pattern pattern = Pattern.compile("[1-9]*");
        return pattern.matcher(str).matches();
    }
}
