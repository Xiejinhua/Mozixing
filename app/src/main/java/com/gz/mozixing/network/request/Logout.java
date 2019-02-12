package com.gz.mozixing.network.request;

import com.gz.mozixing.Constant;
import com.gz.mozixing.network.APIManagerUtil;
import com.gz.mozixing.network.model.NetWorkCallback;
import com.gz.mozixing.network.model.Response;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex
 * @since 19/1/30
 */

public class Logout extends Response {

    private static final String url = Constant.API_Target_url + "user/logout";

    //请求数据
    public static void getResponse(NetWorkCallback netWorkCallback) {
        Map<String, String> map = new HashMap<>();

        APIManagerUtil.getInstance().startPostResponse(url, map, netWorkCallback, Logout.class);

    }

    //取消请求
    public static void cancelResponse() {
        RequestCall call = OkHttpUtils.get().url(url).build();
        call.cancel();
    }
}
