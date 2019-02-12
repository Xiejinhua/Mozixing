package com.gz.mozixing.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.gz.mozixing.network.model.NetWorkCallback;
import com.gz.mozixing.utils.LogUtil;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Alex
 * @since 19/1/30
 */
public class NetWorkUtil {
    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    private static NetWorkUtil netWorkUtil;

    public static NetWorkUtil getInstance() {

        if (netWorkUtil == null) {
            netWorkUtil = new NetWorkUtil();
        }
        return netWorkUtil;
    }

    public void startGetResponse(final String url, Map<String, String> params, Map<String, String> header, final NetWorkCallback callback) {
        LogUtil.d("integrate", url);
        LogUtil.d("integrate", params.toString());
        OkHttpUtils
                .get()
                .url(url)
                .params(params)
                .headers(header)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        LogUtil.d("integrate", url + "\n" + e.getLocalizedMessage());
                        callback.onFailure(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtil.d("integrate", url + "\n" + response);
                        callback.onResponse(response);

                    }
                });
    }

    public void startPostResponse(final String url, Map<String, String> params, Map<String, String> header, final NetWorkCallback callback) {
        LogUtil.d("integrate", url);
        LogUtil.d("integrate", params.toString());

        OkHttpUtils
                .post()
                .url(url)
                .params(params)
                .headers(header)
                .build()
                .connTimeOut(30000)
                .writeTimeOut(30000)
                .readTimeOut(30000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        LogUtil.d("integrate", url + "\n" + e.getLocalizedMessage());
                        callback.onFailure(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {

                        LogUtil.d("integrate", url + "\n" + response);
                        callback.onResponse(response);
                    }
                });
    }
}
