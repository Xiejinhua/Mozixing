package com.gz.mozixing.network;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gz.mozixing.Constant;
import com.gz.mozixing.R;
import com.gz.mozixing.activity.SplashActivity;
import com.gz.mozixing.network.gsonutil.NullBooleanToEmptyAdapterFactory;
import com.gz.mozixing.network.gsonutil.NullDoubleToEmptyAdapterFactory;
import com.gz.mozixing.network.gsonutil.NullIntgerToEmptyAdapterFactory;
import com.gz.mozixing.network.gsonutil.NullStringToEmptyAdapterFactory;
import com.gz.mozixing.network.model.NetWorkCallback;
import com.gz.mozixing.utils.ACacheUtil;
import com.gz.mozixing.utils.ActivityUtil;
import com.gz.mozixing.utils.MapSortUtil;
import com.gz.mozixing.utils.RemindDialogUtil;
import com.gz.mozixing.utils.TimeUtil;
import com.gz.mozixing.utils.WaitingDialogUtil;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * 处理网络请求api类
 *
 * @author Alex
 * @since 19/1/30
 */

public class APIManagerUtil {

    private static APIManagerUtil apiManagerUtil;


    public static APIManagerUtil getInstance() {

        if (apiManagerUtil == null) {
            apiManagerUtil = new APIManagerUtil();
        }
        return apiManagerUtil;
    }

    private static boolean isFinsh = false;
    private static boolean isCustom = false;


    public void startPostResponse(String url, Map<String, String> params, final NetWorkCallback callback, final Class requestModel) {
        isCustom = true;
        isFinsh = false;
        try {
            if (isCustom) {
                WaitingDialogUtil.show(ActivityUtil.getActivity());
            }
            params = getGreenMap(params);
            NetWorkUtil.getInstance().startPostResponse(url, params, getHeader(), new NetWorkCallback() {
                @Override
                public void onResponse(Object response) {
                    WaitingDialogUtil.cancel();
                    if (response != null) {
                        try {
                            JSONObject json;
                            json = new JSONObject((String) response);
                            if (!json.isNull("code")) {
                                if (isRight(ActivityUtil.getActivity(), json.optString("code"), json.optString("msg"))) {
                                    Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullDoubleToEmptyAdapterFactory()).registerTypeAdapterFactory(new NullBooleanToEmptyAdapterFactory()).registerTypeAdapterFactory(new NullIntgerToEmptyAdapterFactory()).registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
                                    callback.onResponse(gson.fromJson(json.toString(), requestModel));
                                }
                            } else {
                                Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullDoubleToEmptyAdapterFactory()).registerTypeAdapterFactory(new NullBooleanToEmptyAdapterFactory()).registerTypeAdapterFactory(new NullIntgerToEmptyAdapterFactory()).registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
                                callback.onResponse(gson.fromJson(json.toString(), requestModel));
                            }
                        } catch (Exception e) {
                            callback.onFailure(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(String message) {
                    WaitingDialogUtil.cancel();
                    callback.onFailure(message);
                }

            });
        } catch (Exception e) {
            WaitingDialogUtil.cancel();
            e.printStackTrace();
            callback.onFailure(e.getLocalizedMessage());
        }
    }

    //返回的数据统一判断是否成功，不成功统一操作
    private boolean isOpenDialog = false;

    //新api判断是否返回200
    private boolean isRight(final Activity activity, String status, String message) {
        if (activity != null) {
            if (status != null && status.equalsIgnoreCase("1")) {
                return true;
            } else if (status != null && status.equalsIgnoreCase("-2")) {
                if (!isOpenDialog) {
                    isOpenDialog = true;
                    Constant.AuthTokenHolder = "";
                    ACacheUtil.get(activity).put(Constant.authToken, "");
                    RemindDialogUtil.show(activity, message, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isOpenDialog = false;
                            SplashActivity.actionStart(activity);
                            activity.finish();
                        }
                    });
                }
                return false;
            } else {
                if (!isOpenDialog) {
                    isOpenDialog = true;
                    RemindDialogUtil.show(activity, message, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isOpenDialog = false;
//                            activity.finish();
                        }
                    });
                }
                return false;
            }

        } else {
            return false;
        }
    }

    public static Map<String, String> getGreenMap(Map<String, String> map) {
//        if (!map.containsKey("sign")) {
//            String time = TimeUtil.getTimeMillis();
        map.put("token", Constant.AuthTokenHolder);
//            String sign = MapSortUtil.getInstance().getNetWorkMd5String(map, time);
//            map.put("time", time);
//            map.put("sign", sign);
//        }
        return map;
    }

    //统一头部获取
    public static Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("content-type", "application/x-www-form-urlencoded; charset=utf-8");
//        header.put("Key", Constant.GREEN_APP_KEY);
        return header;
    }
}
